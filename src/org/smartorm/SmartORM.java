package org.smartorm;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes(value = { "org.smartorm.SmartORMTable" })
public class SmartORM extends AbstractProcessor {
	protected static final String packageName = "org.smartorm.generated";
	protected static final String packageNameLocal = "org.smartorm";
	protected static final String smartName = "TableSmartORM";
	protected static final String tableUtils = "TableUtils";
	private Filer filer;
	private boolean isDone = false;
	private Map<String, SmartORMHelper> tableClasses = new HashMap<>();
	private static final String sillyClassContent = "package " + packageName
			+ ";\n" + "import android.database.Cursor;\n"
			+ "import android.database.DatabaseUtils;\n"
			+ "import android.database.sqlite.SQLiteQueryBuilder;\n"
			+ "import java.util.UUID;\n" + "import java.util.Date;\n"
			+ "import java.util.List;\n"
			+ "import android.database.sqlite.SQLiteDatabase;\n"
			+ "import android.util.Log;\n" + "import " + packageNameLocal
			+ ".SmartORMDatabaseHelper;\n" + "import " + packageNameLocal
			+ ".SmartORMInsertBuilder;\n" + "import java.sql.SQLException;\n"
			+ "import java.util.ArrayList;\n" + "import "
			+ SmartORM.packageNameLocal + ".SmartORMInterface;\n\n";

	@Override
	public void init(ProcessingEnvironment env) {
		filer = env.getFiler();
	}

	@Override
	public boolean process(Set<? extends TypeElement> arg0,
			RoundEnvironment roundEnv) {
		Set<? extends Element> set = roundEnv
				.getElementsAnnotatedWith(SmartORMTable.class);
		if (!isDone) {
			prepareBeans(set);
			createTableUtils();
			buildFiles();
			isDone = true;
		}
		return true;

	}

	private void prepareBeans(Set<? extends Element> elements) {
		for (Element element : elements) {
			try {
				SmartORMCreateBeanUtils create = new SmartORMCreateBeanUtils(
						element);
				SmartORMInsertUtils insert = new SmartORMInsertUtils(element);
				SmartORMSelectUtils select = new SmartORMSelectUtils(element);
				SmartORMGetterUtils getter = new SmartORMGetterUtils(element);
				SmartORMTableUtils table = new SmartORMTableUtils(element);
				SmartORMCreateLazyBeanUtils lazy = new SmartORMCreateLazyBeanUtils(
						element);
				SmartORMHelper helper = new SmartORMHelper(select, insert,
						create, table, getter, lazy);

				getter.addField(element);
				for (Element field : ElementFilter.fieldsIn(element
						.getEnclosedElements())) {
					if (field.getAnnotation(SmartORMField.class) != null) {
						select.addField(field);
						create.addField(field);
						insert.addField(field);
						table.addField(field);
					}
				}

				for (Element method : ElementFilter.methodsIn(element
						.getEnclosedElements())) {
					if (method.getAnnotation(SmartORMLazyMethod.class) != null) {
						lazy.addField(method);
					}
				}

				tableClasses.put(element.getSimpleName().toString(), helper);
			} catch (NoClassDefFoundError e) {
			}

		}
	}

	private void buildFiles() {
		for (String className : tableClasses.keySet()) {
			try {
				SmartORMHelper helper = tableClasses.get(className);

				SmartORMSelectUtils select = helper.getSelect();
				SmartORMCreateBeanUtils create = helper.getCreate();
				SmartORMInsertUtils insert = helper.getInsert();
				SmartORMGetterUtils getter = helper.getGetter();
				SmartORMCreateLazyBeanUtils lazy = helper.getLazy();

				if (select != null && select.getSuperClassName() != null
						&& !select.isAbstract()) {
					searchSuperClassSelect(select, select.getStringBuilder());
				}
				if (create != null && create.getSuperClassName() != null
						&& !create.isAbstract()) {
					searchSuperClassCreate(create, create.getStringBuilder());
				}
				if (insert != null && insert.getSuperClassName() != null
						&& !insert.isAbstract()) {
					searchSuperClassInsert(insert, insert.getStringBuilder());
				}

				lazy.createMethod(create, select);

				JavaFileObject file = null;

				if (!getter.isAbstract()) {
					file = filer.createSourceFile(packageName + "." + className
							+ smartName);
					Writer writer = file.openWriter();
					writer.append(sillyClassContent);
					writer.append("public class " + className + smartName
							+ " {\n");
					writer.append(select.getCompletedString());
					writer.append(insert.getCompletedString());
					writer.append(create.getCompletedString());
					writer.append(getter.getCompletedString());
					writer.append(lazy.getCompletedString());
					writer.append("\n}");
					writer.close();
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}

	private void createTableUtils() {
		List<SmartORMTableUtils> tableArray = new ArrayList<SmartORMTableUtils>();
		for (SmartORMHelper helper : tableClasses.values()) {
			tableArray.add(helper.getTable());
		}
		SmartORMTableUtils.createTableUtils(filer, tableArray);
	}

	private void searchSuperClassSelect(SmartORMSelectUtils select,
			StringBuilder sb) {
		SmartORMSelectUtils superClassSelect = tableClasses.get(
				select.getSuperClassName()).getSelect();

		if (superClassSelect != null) {
			if (superClassSelect.getSuperClassName() != null) {
				searchSuperClassSelect(superClassSelect, sb);
			}
			sb.append(",").append(superClassSelect.getStringBuilder());
			select.getSelectList().addAll(superClassSelect.getSelectList());
		}
	}

	private void searchSuperClassCreate(SmartORMCreateBeanUtils create,
			StringBuilder sb) {
		SmartORMCreateBeanUtils superClassCreate = tableClasses.get(
				create.getSuperClassName()).getCreate();
		if (superClassCreate != null) {
			if (superClassCreate.getSuperClassName() != null) {
				searchSuperClassCreate(superClassCreate, sb);
			}
			sb.append(superClassCreate.getStringBuilder());
			create.getCreateBean().addAll(superClassCreate.getCreateBean());
		}
	}

	private void searchSuperClassInsert(SmartORMInsertUtils insert,
			StringBuilder sb) {
		SmartORMInsertUtils superClassInsert = tableClasses.get(
				insert.getSuperClassName()).getInsert();
		if (superClassInsert != null) {
			if (superClassInsert.getSuperClassName() != null) {
				searchSuperClassInsert(superClassInsert, sb);
			}
			sb.append(superClassInsert.getStringBuilder());
		}
	}
}
