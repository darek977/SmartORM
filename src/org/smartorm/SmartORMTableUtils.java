package org.smartorm;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;

class SmartORMTableUtils extends SmartORMUtils {
	private StringBuilder index;

	protected SmartORMTableUtils(Element table) throws NoClassDefFoundError {
		super(table);
		index = new StringBuilder();
	}

	@Override
	public StringBuilder getCompletedString() {
		StringBuilder result = new StringBuilder();
		result.append("CREATE TABLE ").append(getTableName()).append(" (")
				.append(sb).append(");");
		return result;
	}

	protected boolean hasWhereIndex() {
		return index.length() > 0;
	}

	protected StringBuilder getWhereIndex() {
		StringBuilder result = new StringBuilder();
		result.append("CREATE INDEX IF NOT EXISTS  ").append(getTableName())
				.append("_index ON ").append(getTableName()).append("(")
				.append(index).append(");");
		return result;
	}

	protected StringBuilder getDropIndex() {
		StringBuilder result = new StringBuilder();
		result.append("DROP INDEX ").append(getTableName()).append("_index")
				.append(";");
		return result;
	}

	public void addSuperClassString(StringBuilder builder) {
		sb.append(",").append(builder);
	}

	public void addSuperClassIndex(StringBuilder builder) {
		if (index.length() > 0) {
			index.append(",").append(builder);
		} else {
			index.append(builder);
		}

	}

	@Override
	public void addField(Element field) throws NoClassDefFoundError {
		SmartORMField annotation = field.getAnnotation(SmartORMField.class);
		if (sb.length() > 0) {
			sb.append(",");
		}
		sb.append("'" + annotation.columnName() + "'").append(getType(field))
				.append(isKey(annotation)).append(isNotNull(annotation))
				.append(addDefault(annotation));
		if (annotation.index()) {
			if (index.length() > 0) {
				index.append(", ");
			}
			index.append(annotation.columnName());
		}
	}

	private String getType(Element field) {
		TypeMirror fieldType = field.asType();
		String fullTypeClassName = fieldType.toString();

		if (fullTypeClassName.equals("java.lang.String")) {
			return " TEXT ";
		} else if (fullTypeClassName.equals(boolean.class.toString())) {
			return " INTEGER ";
		} else if (fullTypeClassName.equals(short.class.toString())) {
			return " INTEGER ";
		} else if (fullTypeClassName.equals(int.class.toString())) {
			return " INTEGER ";
		} else if (fullTypeClassName.equals(long.class.toString())) {
			return " INTEGER ";
		} else if (fullTypeClassName.equals(float.class.toString())) {
			return " REAL ";
		} else if (fullTypeClassName.equals(double.class.toString())) {
			return " REAL ";
		} else if (fullTypeClassName.equals("java.util.UUID")) {
			return " TEXT ";
		} else if (fullTypeClassName.equals("java.util.Date")) {
			return " INTEGER ";
		} else {
			return " TEXT ";
		}

	}

	private String isKey(SmartORMField annotation) {
		if (annotation.id()) {
			return " PRIMARY KEY ";
		} else {
			return "";
		}

	}

	private String isNotNull(SmartORMField annotation) {
		if (annotation.canBeNull()) {
			return "";
		} else {
			return " NOT NULL ";
		}

	}

	private String addDefault(SmartORMField annotation) {
		if (annotation.defaultValue().equals(ABSTRACT_CLASS)) {
			return "";
		} else {
			if (annotation.defaultValue().equalsIgnoreCase("false")) {
				return " DEFAULT 0";
			} else if (annotation.defaultValue().equalsIgnoreCase("true")) {
				return " DEFAULT 1";
			} else {
				return " DEFAULT " + annotation.defaultValue();
			}
		}

	}

	public static void createTableUtils(Filer filer,
			List<SmartORMTableUtils> tableArray) {
		HashMap<String, SmartORMTableUtils> abstractBuilder = new HashMap<>();
		for (SmartORMTableUtils util : tableArray) {
			if (util.isAbstract() && util.getSuperClassName() == null) {
				abstractBuilder.put(util.getClassName(), util);
			}
		}
		for (SmartORMTableUtils util : tableArray) {
			if (util.isAbstract() && util.getSuperClassName() != null) {
				util.addSuperClassString(abstractBuilder.get(
						util.getSuperClassName()).getStringBuilder());
				if (abstractBuilder.get(util.getSuperClassName()).getIndex()
						.length() > 0) {
					util.addSuperClassIndex(abstractBuilder.get(
							util.getSuperClassName()).getIndex());
				}
				abstractBuilder.put(util.getClassName(), util);
			}
		}

		for (SmartORMTableUtils util : tableArray) {
			if (!util.isAbstract() && util.getSuperClassName() != null) {
				util.addSuperClassString(abstractBuilder.get(
						util.getSuperClassName()).getStringBuilder());
				if (abstractBuilder.get(util.getSuperClassName()).getIndex()
						.length() > 0) {
					util.addSuperClassIndex(abstractBuilder.get(
							util.getSuperClassName()).getIndex());
				}
			}
		}
		try {
			JavaFileObject file = filer.createSourceFile(SmartORM.packageName
					+ "." + SmartORM.tableUtils);
			Writer writer = file.openWriter();
			writer.append("package " + SmartORM.packageName + ";\n");
			writer.append("import java.util.List;\nimport java.util.Collections;\n"
					+ "import android.database.sqlite.SQLiteDatabase;\n import java.sql.SQLException;\nimport "
					+ SmartORM.packageNameLocal + ".SmartORMInterface;\n\n");
			writer.append("public class " + SmartORM.tableUtils + " {\n");

			// create all tables
			writer.append("\tpublic static void createTables(SQLiteDatabase db) throws SQLException {\n");
			for (SmartORMTableUtils table : tableArray) {
				if (!table.isAbstract()) {
					writer.append("\t\tdb.execSQL(\"");
					writer.append(table.getCompletedString());
					writer.append("\");\n");
				}
			}

			for (SmartORMTableUtils table : tableArray) {
				if (!table.isAbstract() && table.hasWhereIndex()) {
					writer.append("\t\tdb.execSQL(\"");
					writer.append(table.getWhereIndex());
					writer.append("\");\n");
				}
			}
			// drop all tables
			writer.append("\t}\n\n");
			writer.append("\tpublic static void dropTables(SQLiteDatabase db) throws SQLException {\n");
			for (SmartORMTableUtils table : tableArray) {
				if (!table.isAbstract()) {
					writer.append("\t\tdb.execSQL(\"");
					writer.append("DROP TABLE IF EXISTS "
							+ table.getTableName());
					writer.append("\");\n");
				}
			}
			
			writer.append("\t}\n\n");

			// get table with where
			writer.append("\tpublic static List<SmartORMInterface> getEmptyTable(String tableName, String where, String sortOrder, int limit, int offset) throws SQLException {\n");
			for (SmartORMTableUtils t : tableArray) {
				if (!t.isAbstract()) {
					writer.append("\t\tif(\"").append(t.getTableName())
							.append("\".equals(tableName)){");
					writer.append(String
							.format(LOCALE,
									"return %s%s.getEmpty(where, sortOrder, limit, offset);",
									t.getTable().getSimpleName(),
									SmartORM.smartName));
					writer.append("}\n");
				}
			}
			writer.append("\treturn Collections.emptyList();\n");
			writer.append("\t}\n\n");

			// clearTable
			for (SmartORMTableUtils table : tableArray) {
				if (!table.isAbstract()) {
					writer.append("\tpublic static int clearTable"
							+ table.getClassName() + "(SQLiteDatabase db){\n");
					writer.append("\t\treturn db.delete(\""
							+ table.getTableName() + "\", null, null);");
					writer.append("\t}\n\n");
				}
			}

			// persist interface
			writer.append("\tpublic static void persist(SmartORMInterface bean) {\n"
					+ "\t\tbean.persist();\n"
					+ "\t}\n"
					+ "\tpublic static void refresh(SmartORMInterface bean){\n"
					+ "\t\tbean.refresh();\n" + "\t}");
			writer.append("}");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected StringBuilder getIndex() {
		return index;
	}
}
