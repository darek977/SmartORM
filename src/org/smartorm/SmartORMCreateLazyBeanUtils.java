package org.smartorm;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Element;

class SmartORMCreateLazyBeanUtils extends SmartORMUtils {
	private Map<String, String[]> lazyMap = new HashMap<>();

	public SmartORMCreateLazyBeanUtils(Element table)
			throws NoClassDefFoundError {
		super(table);

	}

	public static SmartORMCreateLazyBeanUtils compareLazy(String className) {
		return new SmartORMCreateLazyBeanUtils(className);
	}

	private SmartORMCreateLazyBeanUtils(String className) {
		super(className);
	}

	public void createMethod(SmartORMCreateBeanUtils create,
			SmartORMSelectUtils select) {
		for (String methodName : lazyMap.keySet()) {
			String[] selectString = lazyMap.get(methodName);
			StringBuilder result = getCreateBean(create, select, selectString);
			sb.append(String
					.format("\n\tpublic static List<%s> %s(String where,String sortOrder,int limit,int offset) {\n",
							table.asType().toString(), methodName));
			sb.append(String
					.format("\t\tList<%s> beans = new ArrayList<%s>();\n"
							+ "\t\tCursor cursor = null;"
							+ "\t\ttry {\n"
							+ "\t\t\tSQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();\n"
							+ "\t\t\tsqlBuilder.setTables(\"%s\");\n"
							+ "\t\t\tsqlBuilder.appendWhere(where);\n"
							+ "\t\t\tSQLiteDatabase db = SmartORMDatabaseHelper.getDB();\n"
							+ "\t\t\tcursor = sqlBuilder.query(db, %s, null, null, null, null, sortOrder,limit>0&&offset>-1?String.valueOf(offset)+\",\"+String.valueOf(limit):null);\n"
							+ "\t\t\twhile (cursor.moveToNext()) {\n"
							+ "\t\t\t\tint i = 0;\n\t\t\tboolean isFully=false;\n"
							+ "\t\t\t\t%s bean = new %s();\r\n" + result
							+ "\t\t\t\tbeans.add(bean);" + "\t\t}\n"
							+ "\t\t} catch (Exception e) {\n"
							+ "\t\t\tLog.e(\"%s\", e.toString());\n"
							+ "\t\t} finally {\n" + "\n\t\t\t"
							+ "if(cursor!=null) cursor.close();"
							+ "\n\t\t}\n\treturn beans;\n" + "\t}\n", type,
							type, table.getAnnotation(SmartORMTable.class)
									.tableName(), getSelect(selectString),
							type, type, SmartORM.smartName));
		}
	}

	@Override
	public StringBuilder getCompletedString() {
		return sb;
	}

	@Override
	public void addField(Element field) throws NoClassDefFoundError {
		lazyMap.put(field.getSimpleName().toString(),
				field.getAnnotation(SmartORMLazyMethod.class).values());
	}

	private StringBuilder getCreateBean(SmartORMCreateBeanUtils create,
			SmartORMSelectUtils select, String[] values) {
		StringBuilder result = new StringBuilder();
		for (String s : values) {
			result.append(create.getLine(select.indexOf(s)));
		}
		return result;
	}

	private String getSelect(String[] selectString) {
		StringBuilder result = new StringBuilder();
		result.append(" new String[]{");
		int size = selectString.length;
		for (int i = 0; i < size; i++) {
			if (i == size - 1) {
				result.append("\"" + selectString[i] + "\"");
			} else {
				result.append("\"" + selectString[i] + "\", ");
			}
		}
		result.append("}");
		return result.toString();
	}
}
