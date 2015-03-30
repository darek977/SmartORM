package org.smartorm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

class SmartORMCreateBeanUtils extends SmartORMUtils implements
		SmartORMUtilsInterface {
	private final List<String> createBean;

	public SmartORMCreateBeanUtils(Element table) throws NoClassDefFoundError {
		super(table);
		createBean = new ArrayList<String>();
	}

	public static SmartORMCreateBeanUtils compareSelect(String className) {
		return new SmartORMCreateBeanUtils(className);
	}

	private SmartORMCreateBeanUtils(String className) {
		super(className);
		createBean = new ArrayList<String>();
	}

	@Override
	public StringBuilder getCompletedString() {
		StringBuilder result = new StringBuilder();
		append(result,
				"\n\tpublic static %s createEmpty(Cursor cursor){\n\t\t %s bean = new %s();\n\t\t int i =0;\n",
				type, type, type);
		append(result,
				"\t\tswitch(cursor.getType (i)){\n\t\t\tcase Cursor.FIELD_TYPE_NULL:\n\t\t\t\tbreak;\n\t\t\tcase Cursor.FIELD_TYPE_INTEGER:");
		append(result,
				"\n\t\t\t\t((SmartORMInterface)bean).setId(cursor.getInt(i));\n\t\t\t\tbreak;\n\t\t\tcase Cursor.FIELD_TYPE_FLOAT:");
		append(result,
				"\n\t\t\t\t((SmartORMInterface)bean).setId(cursor.getFloat(i));\n\t\t\t\tbreak;\n\t\t\tcase Cursor.FIELD_TYPE_STRING:");
		append(result,
				"\n\t\t\t\t((SmartORMInterface)bean).setId(cursor.getString(i));\n\t\t\t\tbreak;\n\t\t\tcase Cursor.FIELD_TYPE_BLOB:");
		append(result,
				"\n\t\t\t\t((SmartORMInterface)bean).setId(cursor.getBlob(i));\n\t\t\t\tbreak;\n\t\t}");
		append(result, "\n\t\treturn bean;\n\t}\n");
		append(result,
				"\tpublic static %s createBean(Cursor cursor,%s bean, boolean isFully){\n\t\tif(bean==null){\n\t\t\t bean = new %s();\n\t\t}\n\t\t int i =0;\n",
				type, type, type);
		result.append(sb).append("\n\t\treturn bean;\n\t}\n");
		return result;
	}

	@Override
	public void addField(Element field) throws NoClassDefFoundError {
		appendFieldType(field, sb);
	}

	private void appendFieldType(Element field, StringBuilder sb) {
		TypeMirror fieldType = field.asType();
		String fullTypeClassName = fieldType.toString();
		StringBuilder builder;
		sb.append("\t\t\t");
		if (fullTypeClassName.equals("java.lang.String")) {
			builder = new StringBuilder();
			builder.append("bean.set")
					.append(Character.toUpperCase(field.getSimpleName().charAt(
							0)))
					.append(field.getSimpleName().subSequence(1,
							field.getSimpleName().length()))
					.append("(cursor.getString(i++));\n");

			sb.append(builder);
			createBean.add(builder.toString());
		} else if (fullTypeClassName.equals(boolean.class.toString())) {
			builder = new StringBuilder();
			builder.append("bean.set");
			if (!field.getSimpleName().subSequence(0, 2).equals("is")) {
				builder.append(
						Character.toUpperCase(field.getSimpleName().charAt(0)))
						.append(field.getSimpleName().subSequence(1,
								field.getSimpleName().length())).append("(")
						.append("cursor.getInt(i++) > 0 );\n");
			} else {
				builder.append(
						field.getSimpleName().subSequence(2,
								field.getSimpleName().length())).append("(")
						.append("cursor.getInt(i++) > 0 );\n");
			}
			sb.append(builder);
			createBean.add(builder.toString());
		} else if (fullTypeClassName.equals(short.class.toString())) {
			builder = new StringBuilder();
			builder.append("bean.set")
					.append(Character.toUpperCase(field.getSimpleName().charAt(
							0)))
					.append(field.getSimpleName().subSequence(1,
							field.getSimpleName().length())).append("(")
					.append("cursor.getShort(i++));\n");
			sb.append(builder);
			createBean.add(builder.toString());
		} else if (fullTypeClassName.equals(int.class.toString())) {
			builder = new StringBuilder();
			builder.append("bean.set")
					.append(Character.toUpperCase(field.getSimpleName().charAt(
							0)))
					.append(field.getSimpleName().subSequence(1,
							field.getSimpleName().length())).append("(")
					.append("cursor.getInt(i++));\n");
			sb.append(builder);
			createBean.add(builder.toString());
		} else if (fullTypeClassName.equals(long.class.toString())) {
			builder = new StringBuilder();
			builder.append("bean.set")
					.append(Character.toUpperCase(field.getSimpleName().charAt(
							0)))
					.append(field.getSimpleName().subSequence(1,
							field.getSimpleName().length())).append("(")
					.append("cursor.getLong(i++));\n");
			sb.append(builder);
			createBean.add(builder.toString());
		} else if (fullTypeClassName.equals(float.class.toString())) {
			builder = new StringBuilder();
			builder.append("bean.set")
					.append(Character.toUpperCase(field.getSimpleName().charAt(
							0)))
					.append(field.getSimpleName().subSequence(1,
							field.getSimpleName().length())).append("(")
					.append("cursor.getFloat(i++));\n");
			sb.append(builder);
			createBean.add(builder.toString());
		} else if (fullTypeClassName.equals(double.class.toString())) {
			builder = new StringBuilder();
			builder.append("bean.set")
					.append(Character.toUpperCase(field.getSimpleName().charAt(
							0)))
					.append(field.getSimpleName().subSequence(1,
							field.getSimpleName().length())).append("(")
					.append("cursor.getDouble(i++));\n");
			sb.append(builder);
			createBean.add(builder.toString());
		} else if (fullTypeClassName.equals(byte[].class.toString())) {
			builder = new StringBuilder();
			builder.append("bean.set")
					.append(Character.toUpperCase(field.getSimpleName().charAt(
							0)))
					.append(field.getSimpleName().subSequence(1,
							field.getSimpleName().length())).append("(")
					.append("cursor.getBlob(i++));\n");
			sb.append(builder);
			createBean.add(builder.toString());
		} else if (fullTypeClassName.equals("java.util.UUID")) {
			builder = new StringBuilder();
			builder.append("bean.set")
					.append(Character.toUpperCase(field.getSimpleName().charAt(
							0)))
					.append(field.getSimpleName().subSequence(1,
							field.getSimpleName().length())).append("(")
					.append("cursor.getString(i)").append("!=null ? ")
					.append("UUID.fromString(cursor.getString(i))")
					.append(" :null);\n\t\t\ti++;\n");

			sb.append(builder);
			createBean.add(builder.toString());
		} else if (fullTypeClassName.equals("java.util.Date")) {
			builder = new StringBuilder();
			builder.append("bean.set")
					.append(Character.toUpperCase(field.getSimpleName().charAt(
							0)))
					.append(field.getSimpleName().subSequence(1,
							field.getSimpleName().length())).append("(")
					.append("cursor.getLong(i)").append(" >0 ? ")
					.append("new Date(cursor.getLong(i))")
					.append(" :null);\n\t\t\ti++;\n");
			sb.append(builder);
			createBean.add(builder.toString());
		} else {
			builder = new StringBuilder();
			if (field.getAnnotation(SmartORMField.class).dataType()
					.equals(SmartORMDataType.ENUM_STRING)) {
				builder.append("bean.set")
						.append(Character.toUpperCase(field.getSimpleName()
								.charAt(0)))
						.append(field.getSimpleName().subSequence(1,
								field.getSimpleName().length())).append("(")
						.append("cursor.getString(i)").append("!=null ? ")
						.append(fullTypeClassName).append(".valueOf(")
						.append("cursor.getString(i)")
						.append("):null);\n\t\t\ti++;\n");
				sb.append(builder);
				createBean.add(builder.toString());
			} else {
				String simpleClassName = "";
				Pattern pattern = Pattern.compile("(.*)(\\.)(\\w*$)");
				Matcher match = pattern.matcher(fullTypeClassName);
				if (match.find()) {
					simpleClassName = match.group(3);
				}

				builder.append("bean.set")
						.append(Character.toUpperCase(field.getSimpleName()
								.charAt(0)))
						.append(field.getSimpleName().subSequence(1,
								field.getSimpleName().length()))
						.append("(")
						.append("cursor.getString(i)")
						.append("!=null ? (isFully ?")
						.append(simpleClassName)
						.append(SmartORM.smartName)
						.append(".get" + simpleClassName
								+ "(UUID.fromString(cursor.getString(i))): ")
						.append("new ").append(fullTypeClassName).append("(")
						.append("UUID.fromString(cursor.getString(i))")
						.append(")): null);\n\t\t\ti++;\n");
				sb.append(builder);
				createBean.add(builder.toString());
			}

		}
	}

	public String getLine(int index) {
		return createBean.get(index);
	}

	public List<String> getCreateBean() {
		return createBean;
	}
}