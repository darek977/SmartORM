package org.smartorm;

import javax.lang.model.element.Element;

class SmartORMGetterUtils extends SmartORMUtils implements
		SmartORMUtilsInterface {

	public SmartORMGetterUtils(Element table) throws NoClassDefFoundError {
		super(table);
	}

	public static SmartORMGetterUtils compareSelect(String className) {
		return new SmartORMGetterUtils(className);
	}

	private SmartORMGetterUtils(String className) {
		super(className);
	}

	@Override
	public StringBuilder getCompletedString() {
		return sb;
	}

	@Override
	public void addField(Element field) throws NoClassDefFoundError {
		sb.append(createElementGetter(field));
		sb.append(createElementFullGetter(field));
		sb.append(createElementAllGetter(field));
		sb.append(createElementFullWhereGetter(field));
		sb.append(createElementWhereGetter(field));
		sb.append(createElementAllFullGetter(field));
		sb.append(createElementEmptyGetter(field));
		sb.append(createElementFullRefresh(field));
		sb.append(createElementRefresh(field));
		sb.append(createElementWhereGetterFirst(field));
		sb.append(createElementWhereGetterFullFirst(field));
		sb.append(createElementWhereGetterShort(field));
		sb.append(createElementFullWhereGetterShort(field));
		sb.append(createElementGetterFirst(field));
		sb.append(createElementGetterFullFirst(field));
		sb.append(createPersistList(field));
		sb.append(createPersist(field));
	}

	private StringBuilder createElementGetter(Element root)
			throws NoClassDefFoundError {
		StringBuilder getter = new StringBuilder();
		getter.append(String.format("\n\tpublic static %s get%s(UUID id) {\n",
				root.asType().toString(), root.getSimpleName()));
		getter.append(String
				.format("\t\tCursor cursor =null;\n"
						+ "\t\ttry {\n"
						+ "\t\t\tSQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();\n"
						+ "\t\t\tsqlBuilder.setTables(\"%s\");\n"
						+ "\t\t\tsqlBuilder.appendWhere(\"id = \\\"\" + id.toString() + \"\\\"\");\n"
						+ "\t\t\tSQLiteDatabase db = SmartORMDatabaseHelper.getDB();\n"
						+ "\t\t\tcursor = sqlBuilder.query(db, %s%s.select, null, null, null, null, null);\n"
						+ "\t\t\tif (cursor.moveToFirst()) {\n"
						+ "\t\t\t\treturn %s%s.createBean(cursor,null,false);\n"
						+ "\t\t\t}\n" + "\t\t} catch (Exception e) {\n"
						+ "\t\t\tLog.e(\"%s\", e.toString());\n"
						+ "\t\t} finally {\n"
						+ "\t\t\tif(cursor!=null) cursor.close();\n\t\t}\n"
						+ "\t\treturn null;\n" + "\t}\n",
						root.getAnnotation(SmartORMTable.class).tableName(),
						root.getSimpleName(), SmartORM.smartName,
						root.getSimpleName(), SmartORM.smartName,
						SmartORM.smartName));

		return getter;
	}

	private StringBuilder createElementFullGetter(Element root)
			throws NoClassDefFoundError {
		StringBuilder getter = new StringBuilder();
		getter.append(String.format(
				"\n\tpublic static %s getFull(UUID id) {\n", root.asType()
						.toString()));
		getter.append(String
				.format("\t\tCursor cursor =null;\n"
						+ "\t\ttry {\n"
						+ "\t\t\tSQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();\n"
						+ "\t\t\tsqlBuilder.setTables(\"%s\");\n"
						+ "\t\t\tsqlBuilder.appendWhere(\"id = \\\"\" + id.toString() + \"\\\"\");\n"
						+ "\t\t\tSQLiteDatabase db = SmartORMDatabaseHelper.getDB();\n"
						+ "\t\t\tcursor = sqlBuilder.query(db, %s%s.select, null, null, null, null, null);\n"
						+ "\t\t\tif(cursor.moveToFirst()) {\n"
						+ "\t\t\t\treturn %s%s.createBean(cursor,null,true);\n"
						+ "\t\t\t}\n" + "\t\t} catch (Exception e) {\n"
						+ "\t\t\tLog.e(\"%s\", e.toString());\n"
						+ "\t\t} finally {\n"
						+ "\t\t\tif(cursor!=null) cursor.close();\n\t\t}\n"
						+ "\t\treturn null;\n" + "\t}\n",
						root.getAnnotation(SmartORMTable.class).tableName(),
						root.getSimpleName(), SmartORM.smartName,
						root.getSimpleName(), SmartORM.smartName,
						SmartORM.smartName));

		return getter;
	}

	private StringBuilder createElementFullRefresh(Element root)
			throws NoClassDefFoundError {
		StringBuilder getter = new StringBuilder();
		getter.append(String.format(
				"\n\tpublic static %s refresh(%s bean) {\n", root.asType()
						.toString(), root.asType().toString()));
		getter.append(String
				.format("\t\tif(bean!=null){\n"
						+ "\t\tCursor cursor =null;\n"
						+ "\t\t\ttry {\n"
						+ "\t\t\t\tSQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();\n"
						+ "\t\t\t\tsqlBuilder.setTables(\"%s\");\n"
						+ "\t\t\t\tsqlBuilder.appendWhere(\"id = \\\"\" + bean.getId().toString() + \"\\\"\");\n"
						+ "\t\t\t\tSQLiteDatabase db = SmartORMDatabaseHelper.getDB();\n"
						+ "\t\t\t\tcursor = sqlBuilder.query(db, %s%s.select, null, null, null, null, null);\n"
						+ "\t\t\t\tif(cursor.moveToFirst()) {\n"
						+ "\t\t\t\t\treturn %s%s.createBean(cursor,bean,false);\n"
						+ "\t\t\t\t}\n" + "\t\t\t} catch (Exception e) {\n"
						+ "\t\t\t\t\tLog.e(\"%s\", e.toString());\n"
						+ "\t\t\t} finally {\n"
						+ "\t\t\t\tif(cursor!=null) cursor.close();\n\t\t\t}\n"
						+ "\t\t}\n\t\treturn null;\n" + "\t}\n", root
						.getAnnotation(SmartORMTable.class).tableName(), root
						.getSimpleName(), SmartORM.smartName, root
						.getSimpleName(), SmartORM.smartName,
						SmartORM.smartName));

		return getter;
	}

	private StringBuilder createElementRefresh(Element root)
			throws NoClassDefFoundError {
		StringBuilder getter = new StringBuilder();
		getter.append(String.format(
				"\n\tpublic static %s refreshFull(%s bean) {\n", root.asType()
						.toString(), root.asType().toString()));
		getter.append(String
				.format("\t\tif(bean!=null){\n"
						+ "\t\tCursor cursor =null;\n"
						+ "\t\t\ttry {\n"
						+ "\t\t\t\tSQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();\n"
						+ "\t\t\t\tsqlBuilder.setTables(\"%s\");\n"
						+ "\t\t\t\tsqlBuilder.appendWhere(\"id = \\\"\" + bean.getId().toString() + \"\\\"\");\n"
						+ "\t\t\t\tSQLiteDatabase db = SmartORMDatabaseHelper.getDB();\n"
						+ "\t\t\t\tcursor = sqlBuilder.query(db, %s%s.select, null, null, null, null, null);\n"
						+ "\t\t\t\tif (cursor.moveToFirst()) {\n"
						+ "\t\t\t\t\treturn %s%s.createBean(cursor,bean,false);\n"
						+ "\t\t\t\t}\n" + "\t\t\t} catch (Exception e) {\n"
						+ "\t\t\t\t\tLog.e(\"%s\", e.toString());\n"
						+ "\t\t\t} finally {\n"
						+ "\t\t\t\tif(cursor!=null) cursor.close();\n\t\t\t}\n"
						+ "\t\t}\n\t\treturn null;\n" + "\t}\n", root
						.getAnnotation(SmartORMTable.class).tableName(), root
						.getSimpleName(), SmartORM.smartName, root
						.getSimpleName(), SmartORM.smartName,
						SmartORM.smartName));

		return getter;
	}

	private StringBuilder createPersistList(Element root) {
		StringBuilder persist = new StringBuilder();
		persist.append(String.format(
				"\n\tpublic static void persist(List<%s> beans) throws SQLException {\n"
						+ "\t\tif (!beans.isEmpty()) {\n"
						+ "\t\t\tlong start = System.currentTimeMillis();\n"
						+ "\t\t\tSQLiteDatabase db = SmartORMDatabaseHelper.getDB();\n"
						+ "\t\t\tSmartORMInsertBuilder args = getInsert%s(beans);\n"
						+ "\t\t\tArrayList<String> queries = new ArrayList<String>();\n"
						+ "\t\t\tdo {\n"
						+ "\t\t\t\tStringBuilder sb = new StringBuilder();\n"
						+ "\t\t\t\tsb.append(\"INSERT OR REPLACE INTO \").append(\"%s\").append(\" \")\n"
						+ "\t\t\t\t.append(args.getColumns()).append(\" VALUES \").append(args.getNumberOfValues(20))\n"
						+ "\t\t\t\t.append(\";\");\n"
						+ "\t\t\t\tqueries.add(sb.toString());\n"
						+ "\t\t\t\t} while (args.isNext());\n"
						+ "\t\t\tfor (String query : queries) {\n"
						+ "\t\t\t\tdb.execSQL(query);\n"
						+ "\t\t\t\t}\n"
						+ "\t\t\tlong time = (System.currentTimeMillis() - start);\n"
						+ "\t\t\tLog.i(\"%s\", \"Persist bean: \" + time);\n"
						+ "\t\t}\n" + "\t}", root.asType().toString(), root
						.getSimpleName(),
				root.getAnnotation(SmartORMTable.class).tableName(),
				SmartORM.smartName));
		return persist;

	}

	private StringBuilder createPersist(Element root) {
		StringBuilder persist = new StringBuilder();
		persist.append(String.format(
				"\n\tpublic static void persist(%s bean) throws SQLException {\n"
						+ "\t\tif(bean != null) {\n"
						+ "\t\t\tlong start = System.currentTimeMillis();\n"
						+ "\t\t\tSQLiteDatabase db = SmartORMDatabaseHelper.getDB();\n"
						+ "\t\t\tSmartORMInsertBuilder args = getInsert%s(bean);\n"
						+ "\t\t\t\tStringBuilder sb = new StringBuilder();\n"
						+ "\t\t\t\tsb.append(\"INSERT OR REPLACE INTO \").append(\"%s\").append(\" \")\n"
						+ "\t\t\t\t.append(args.getColumns()).append(\" VALUES \").append(args.getNumberOfValues(1))\n"
						+ "\t\t\t\t\t.append(\";\");\n"
						+ "\t\t\t\tdb.execSQL(sb.toString());\n"
						+ "\t\t\tlong time = (System.currentTimeMillis() - start);\n"
						+ "\t\t\tLog.i(\"%s\", \"Persist bean: \" + time);\n"
						+ "\t\t}\n" + "\t}", root.asType().toString(), root
						.getSimpleName(),
				root.getAnnotation(SmartORMTable.class).tableName(),
				SmartORM.smartName));
		return persist;

	}

	private StringBuilder createElementAllGetter(Element root)
			throws NoClassDefFoundError {
		StringBuilder getter = new StringBuilder();
		getter.append(String
				.format("\n\tpublic static List<%s> getAll(String sortOrder, int limit,int offset) {\n",
						root.asType().toString()));
		getter.append(String
				.format("\t\tList<%s> beans = new ArrayList<%s>();\n"
						+ "\t\tCursor cursor =null;\n"
						+ "\t\ttry {\n"
						+ "\t\t\tSQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();\n"
						+ "\t\t\tsqlBuilder.setTables(\"%s\");\n"
						+ "\t\t\tSQLiteDatabase db = SmartORMDatabaseHelper.getDB();\n"
						+ "\t\t\tcursor = sqlBuilder.query(db, %s%s.select, null, null, null, null, sortOrder,limit>0&&offset>-1?String.valueOf(offset)+\",\"+String.valueOf(limit):null);\n"
						+ "\t\t\twhile (cursor.moveToNext()) {\n"
						+ "\t\t\t\t%s bean = %s%s.createBean(cursor, null, false);\r\n"
						+ "\t\t\t\tbeans.add(bean);" + "\t\t}\n"
						+ "\t\t} catch (Exception e) {\n"
						+ "\t\t\tLog.e(\"%s\", e.toString());\n"
						+ "\t\t} finally{\n"
						+ "\t\t\tif(cursor!=null) cursor.close();\n\t\t}\n"
						+ "\t\treturn beans;\n" + "\t}\n", root.asType()
						.toString(), root.asType().toString(), root
						.getAnnotation(SmartORMTable.class).tableName(), root
						.getSimpleName(), SmartORM.smartName, root.asType()
						.toString(), root.getSimpleName(), SmartORM.smartName,
						SmartORM.smartName));

		return getter;
	}

	private StringBuilder createElementAllFullGetter(Element root)
			throws NoClassDefFoundError {
		StringBuilder getter = new StringBuilder();
		getter.append(String
				.format("\n\tpublic static List<%s> getFullAll(String sortOrder,int limit,int offset) {\n",
						root.asType().toString()));
		getter.append(String
				.format("\t\tList<%s> beans = new ArrayList<%s>();\n"
						+ "\t\tCursor cursor =null;\n"
						+ "\t\ttry {\n"
						+ "\t\t\tSQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();\n"
						+ "\t\t\tsqlBuilder.setTables(\"%s\");\n"
						+ "\t\t\tSQLiteDatabase db = SmartORMDatabaseHelper.getDB();\n"
						+ "\t\t\tcursor = sqlBuilder.query(db, %s%s.select, null, null, null, null, sortOrder,limit>0&&offset>-1?String.valueOf(offset)+\",\"+String.valueOf(limit):null);\n"
						+ "\t\t\twhile (cursor.moveToNext()) {\n"
						+ "\t\t\t\t%s bean = %s%s.createBean(cursor, null, true);\r\n"
						+ "\t\t\t\tbeans.add(bean);" + "\t\t}\n"
						+ "\t\t} catch (Exception e) {\n"
						+ "\t\t\tLog.e(\"%s\", e.toString());\n"
						+ "\t\t} finally{\n"
						+ "\t\t\tif(cursor!=null) cursor.close();\n\t\t}\n"
						+ "\t\treturn beans;\n" + "\t}\n", root.asType()
						.toString(), root.asType().toString(), root
						.getAnnotation(SmartORMTable.class).tableName(), root
						.getSimpleName(), SmartORM.smartName, root.asType()
						.toString(), root.getSimpleName(), SmartORM.smartName,
						SmartORM.smartName));

		return getter;
	}

	private StringBuilder createElementWhereGetter(Element root)
			throws NoClassDefFoundError {
		StringBuilder getter = new StringBuilder();
		getter.append(String
				.format("\n\tpublic static List<%s> get(String where,String sortOrder, int limit,int offset) {\n",
						root.asType().toString()));
		getter.append(String
				.format("\t\tList<%s> beans = new ArrayList<%s>();\n"
						+ "\t\tCursor cursor =null;\n"
						+ "\t\ttry {\n"
						+ "\t\t\tSQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();\n"
						+ "\t\t\tsqlBuilder.setTables(\"%s\");\n"
						+ "\t\t\tsqlBuilder.appendWhere(where);\n"
						+ "\t\t\tSQLiteDatabase db = SmartORMDatabaseHelper.getDB();\n"
						+ "\t\t\tcursor = sqlBuilder.query(db, %s%s.select, null, null, null, null, sortOrder,limit>0&&offset>-1?String.valueOf(offset)+\",\"+String.valueOf(limit):null);\n"
						+ "\t\t\twhile (cursor.moveToNext()) {\n"
						+ "\t\t\t\t%s bean = %s%s.createBean(cursor, null, false);\r\n"
						+ "\t\t\t\tbeans.add(bean);" + "\t\t}\n"
						+ "\t\t} catch (Exception e) {\n"
						+ "\t\t\tLog.e(\"%s\", e.toString());\n"
						+ "\t\t} finally {\n"
						+ "\t\t\tif(cursor!=null) cursor.close();\n\t\t}\n"
						+ "\t\treturn beans;\n" + "\t}\n", root.asType()
						.toString(), root.asType().toString(), root
						.getAnnotation(SmartORMTable.class).tableName(), root
						.getSimpleName(), SmartORM.smartName, root.asType()
						.toString(), root.getSimpleName(), SmartORM.smartName,
						SmartORM.smartName));

		return getter;
	}

	private StringBuilder createElementWhereGetterFirst(Element root)
			throws NoClassDefFoundError {
		StringBuilder getter = new StringBuilder();
		getter.append(String
				.format("\n\tpublic static %s getFirst(String where,String sortOrder, int limit,int offset) {\n",
						type));
		getter.append(String.format(
				"\t\tList<%s> beans = get(where,sortOrder,limit,offset);\n"
						+ "\t\tif(beans.size()>0){\n"
						+ "\t\t\treturn beans.get(0);\n\t\t}\n"
						+ "\t\treturn null;\n\t}", type));

		return getter;
	}

	private StringBuilder createElementWhereGetterFullFirst(Element root)
			throws NoClassDefFoundError {
		StringBuilder getter = new StringBuilder();
		getter.append(String
				.format("\n\tpublic static %s getFirstFull(String where,String sortOrder, int limit,int offset) {\n",
						type));
		getter.append(String.format(
				"\t\tList<%s> beans = getFull(where,sortOrder,limit,offset);\n"
						+ "\t\tif(beans.size()>0){\n"
						+ "\t\t\treturn beans.get(0);\n\t\t}\n"
						+ "\t\treturn null;\n\t}", type));

		return getter;
	}

	private StringBuilder createElementGetterFirst(Element root)
			throws NoClassDefFoundError {
		StringBuilder getter = new StringBuilder();
		getter.append(String
				.format("\n\tpublic static %s getFirst(String where,String sortOrder) {\n",
						type));
		getter.append(String.format(
				"\t\tList<%s> beans = get(where,sortOrder);\n"
						+ "\t\tif(beans.size()>0){\n"
						+ "\t\t\treturn beans.get(0);\n\t\t}\n"
						+ "\t\treturn null;\n\t}", type));

		return getter;
	}

	private StringBuilder createElementGetterFullFirst(Element root)
			throws NoClassDefFoundError {
		StringBuilder getter = new StringBuilder();
		getter.append(String
				.format("\n\tpublic static %s getFirstFull(String where,String sortOrder) {\n",
						type));
		getter.append(String.format(
				"\t\tList<%s> beans = getFull(where,sortOrder);\n"
						+ "\t\tif(beans.size()>0){\n"
						+ "\t\t\treturn beans.get(0);\n\t\t}\n"
						+ "\t\treturn null;\n\t}", type));

		return getter;
	}

	private StringBuilder createElementFullWhereGetter(Element root)
			throws NoClassDefFoundError {
		StringBuilder getter = new StringBuilder();
		getter.append(String
				.format("\n\tpublic static List<%s> getFull(String where, String sortOrder, int limit, int offset) {\n",
						root.asType().toString()));
		getter.append(String
				.format("\t\tList<%s> beans = new ArrayList<%s>();\n"
						+ "\t\tCursor cursor =null;\n"
						+ "\t\ttry {\n"
						+ "\t\t\tSQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();\n"
						+ "\t\t\tsqlBuilder.setTables(\"%s\");\n"
						+ "\t\t\tsqlBuilder.appendWhere(where);\n"
						+ "\t\t\tSQLiteDatabase db = SmartORMDatabaseHelper.getDB();\n"
						+ "\t\t\tcursor = sqlBuilder.query(db, %s%s.select, null, null, null, null, sortOrder,limit>0&&offset>-1?String.valueOf(offset)+\",\"+String.valueOf(limit):null);\n"
						+ "\t\t\twhile (cursor.moveToNext()) {\n"
						+ "\t\t\t\t%s bean = %s%s.createBean(cursor, null, true);\r\n"
						+ "\t\t\t\tbeans.add(bean);" + "\t\t}\n"
						+ "\t\t} catch (Exception e) {\n"
						+ "\t\t\tLog.e(\"%s\", e.toString());\n"
						+ "\t\t} finally {\n"
						+ "\t\t\tif(cursor!=null) cursor.close();\n\t\t}\n"
						+ "\t\treturn beans;\n" + "\t}\n", root.asType()
						.toString(), root.asType().toString(), root
						.getAnnotation(SmartORMTable.class).tableName(), root
						.getSimpleName(), SmartORM.smartName, root.asType()
						.toString(), root.getSimpleName(), SmartORM.smartName,
						SmartORM.smartName));

		return getter;
	}

	private StringBuilder createElementFullWhereGetterShort(Element root)
			throws NoClassDefFoundError {
		StringBuilder getter = new StringBuilder();
		getter.append(String
				.format("\n\tpublic static List<%s> getFull(String where, String sortOrder) {\n",
						root.asType().toString()));
		getter.append(String
				.format("\t\tList<%s> beans = new ArrayList<%s>();\n"
						+ "\t\tCursor cursor =null;\n"
						+ "\t\ttry {\n"
						+ "\t\t\tSQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();\n"
						+ "\t\t\tsqlBuilder.setTables(\"%s\");\n"
						+ "\t\t\tsqlBuilder.appendWhere(where);\n"
						+ "\t\t\tSQLiteDatabase db = SmartORMDatabaseHelper.getDB();\n"
						+ "\t\t\tcursor = sqlBuilder.query(db, %s%s.select, null, null, null, null, sortOrder,null);\n"
						+ "\t\t\twhile (cursor.moveToNext()) {\n"
						+ "\t\t\t\t%s bean = %s%s.createBean(cursor, null, true);\r\n"
						+ "\t\t\t\tbeans.add(bean);" + "\t\t\t}\n"
						+ "\t\t} catch (Exception e) {\n"
						+ "\t\t\tLog.e(\"%s\", e.toString());\n"
						+ "\t\t} finally {\n"
						+ "\t\t\tif(cursor!=null) cursor.close();\n\t\t}\n"
						+ "\t\treturn beans;\n" + "\t}\n", root.asType()
						.toString(), root.asType().toString(), root
						.getAnnotation(SmartORMTable.class).tableName(), root
						.getSimpleName(), SmartORM.smartName, root.asType()
						.toString(), root.getSimpleName(), SmartORM.smartName,
						SmartORM.smartName));

		return getter;
	}

	private StringBuilder createElementWhereGetterShort(Element root)
			throws NoClassDefFoundError {
		StringBuilder getter = new StringBuilder();
		getter.append(String
				.format("\n\tpublic static List<%s> get(String where, String sortOrder) {\n",
						root.asType().toString()));
		getter.append(String
				.format("\t\tList<%s> beans = new ArrayList<%s>();\n"
						+ "\t\tCursor cursor =null;\n"
						+ "\t\ttry {\n"
						+ "\t\t\tSQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();\n"
						+ "\t\t\tsqlBuilder.setTables(\"%s\");\n"
						+ "\t\t\tsqlBuilder.appendWhere(where);\n"
						+ "\t\t\tSQLiteDatabase db = SmartORMDatabaseHelper.getDB();\n"
						+ "\t\t\tcursor = sqlBuilder.query(db, %s%s.select, null, null, null, null, sortOrder,null);\n"
						+ "\t\t\twhile (cursor.moveToNext()) {\n"
						+ "\t\t\t\t%s bean = %s%s.createBean(cursor, null, false);\r\n"
						+ "\t\t\t\tbeans.add(bean);" + "\t\t\t}\n"
						+ "\t\t} catch (Exception e) {\n"
						+ "\t\t\tLog.e(\"%s\", e.toString());\n"
						+ "\t\t} finally {\n"
						+ "\t\t\tif(cursor!=null) cursor.close();\n\t\t}\n"
						+ "\t\treturn beans;\n" + "\t}\n", root.asType()
						.toString(), root.asType().toString(), root
						.getAnnotation(SmartORMTable.class).tableName(), root
						.getSimpleName(), SmartORM.smartName, root.asType()
						.toString(), root.getSimpleName(), SmartORM.smartName,
						SmartORM.smartName));

		return getter;
	}

	private StringBuilder createElementEmptyGetter(Element root)
			throws NoClassDefFoundError {
		StringBuilder getter = new StringBuilder();
		getter.append(String
				.format("\n\tpublic static List<SmartORMInterface> getEmpty(String where,String sortOrder, int limit, int offset) {\n"));
		getter.append(String
				.format("\t\tList<SmartORMInterface> beans = new ArrayList<SmartORMInterface>();\n"
						+ "\t\tCursor cursor =null;\n"
						+ "\t\ttry {\n"
						+ "\t\t\tSQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();\n"
						+ "\t\t\tsqlBuilder.setTables(\"%s\");\n"
						+ "\t\t\tsqlBuilder.appendWhere(where);\n"
						+ "\t\t\tSQLiteDatabase db = SmartORMDatabaseHelper.getDB();\n"
						+ "\t\t\tcursor = sqlBuilder.query(db, %s%s.selectID, null, null, null, null, sortOrder,limit>0&&offset>-1?String.valueOf(offset)+\",\"+String.valueOf(limit):null);\n"
						+ "\t\t\twhile (cursor.moveToNext()) {\n"
						+ "\t\t\t\t%s bean = %s%s.createEmpty(cursor);\r\n"
						+ "\t\t\t\tbeans.add((SmartORMInterface)bean);"
						+ "\t\t\t}\n"
						+ "\t\t} catch (Exception e) {\n"
						+ "\t\t\tLog.e(\"%s\", e.toString());\n"
						+ "\t\t} finally {\n"
						+ "\t\t\tif(cursor!=null) cursor.close();\n"
						+ "\t\t}\n" + "\t\treturn beans;\n" + "\t}\n", root
						.getAnnotation(SmartORMTable.class).tableName(), root
						.getSimpleName(), SmartORM.smartName, root.asType()
						.toString(), root.getSimpleName(), SmartORM.smartName,
						SmartORM.smartName));

		return getter;
	}
}
