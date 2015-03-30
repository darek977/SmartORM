package org.smartorm;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

class SmartORMInsertUtils extends SmartORMUtils implements
		SmartORMUtilsInterface {

	protected SmartORMInsertUtils(Element table) throws NoClassDefFoundError {
		super(table);
	}

	public static SmartORMInsertUtils compareSelect(String className) {
		return new SmartORMInsertUtils(className);
	}

	private SmartORMInsertUtils(String className) {
		super(className);
	}

	@Override
	public void addField(Element field) throws NoClassDefFoundError {
		TypeMirror fieldType = field.asType();
		String fullTypeClassName = fieldType.toString();
		sb.append("\t\t\t");
		sb.append("args.put(\"")
				.append(field.getAnnotation(SmartORMField.class).columnName())
				.append("\", ");
		if (fullTypeClassName.equals(boolean.class.toString())) {
			if (!field.getSimpleName().subSequence(0, 2).equals("is")) {
				sb.append("bean.is");
				sb.append(
						Character.toUpperCase(field.getSimpleName().charAt(0)))
						.append(field.getSimpleName().subSequence(1,
								field.getSimpleName().length()));
			} else {
				sb.append("bean." + field.getSimpleName());
			}

			sb.append("());\n");
		} else if (fullTypeClassName.equals("java.util.Date")) {
			sb.append("bean.get");
			sb.append(Character.toUpperCase(field.getSimpleName().charAt(0)))
					.append(field.getSimpleName().subSequence(1,
							field.getSimpleName().length()));
			sb.append("() !=null ? bean.get");
			sb.append(Character.toUpperCase(field.getSimpleName().charAt(0)))
					.append(field.getSimpleName().subSequence(1,
							field.getSimpleName().length()))
					.append("().getTime() : 0);\n");
		} else if (fullTypeClassName.equals(boolean.class.toString())
				|| fullTypeClassName.equals(short.class.toString())
				|| fullTypeClassName.equals(int.class.toString())
				|| fullTypeClassName.equals(int.class.toString())
				|| fullTypeClassName.equals(long.class.toString())
				|| fullTypeClassName.equals(float.class.toString())
				|| fullTypeClassName.equals(double.class.toString())
				|| fullTypeClassName.equals(byte[].class.toString())) {
			sb.append("bean.get");
			sb.append(Character.toUpperCase(field.getSimpleName().charAt(0)))
					.append(field.getSimpleName().subSequence(1,
							field.getSimpleName().length()));
			sb.append("());\n");
		} else if (fullTypeClassName.equals("java.util.UUID")) {
			sb.append("bean.get")
					.append(Character.toUpperCase(field.getSimpleName().charAt(
							0)))
					.append(field.getSimpleName().subSequence(1,
							field.getSimpleName().length()))
					.append("()!=null?");
			sb.append("DatabaseUtils.sqlEscapeString(bean.get");
			sb.append(Character.toUpperCase(field.getSimpleName().charAt(0)))
					.append(field.getSimpleName().subSequence(1,
							field.getSimpleName().length()));
			sb.append("().toString()):null);\n");
		} else if (fullTypeClassName.equals("java.lang.String")) {
			sb.append("bean.get")
					.append(Character.toUpperCase(field.getSimpleName().charAt(
							0)))
					.append(field.getSimpleName().subSequence(1,
							field.getSimpleName().length()))
					.append("()!=null?");
			sb.append("DatabaseUtils.sqlEscapeString(bean.get");
			sb.append(Character.toUpperCase(field.getSimpleName().charAt(0)))
					.append(field.getSimpleName().subSequence(1,
							field.getSimpleName().length()));
			sb.append("()):null);\n");
		} else {
			if (field.getAnnotation(SmartORMField.class).dataType()
					.equals(SmartORMDataType.ENUM_STRING)) {
				sb.append("bean.get");
				sb.append(
						Character.toUpperCase(field.getSimpleName().charAt(0)))
						.append(field.getSimpleName().subSequence(1,
								field.getSimpleName().length()));
				sb.append("() !=null ? DatabaseUtils.sqlEscapeString(bean.get");
				sb.append(
						Character.toUpperCase(field.getSimpleName().charAt(0)))
						.append(field.getSimpleName().subSequence(1,
								field.getSimpleName().length()))
						.append("().toString()) : null);\n");
			} else {
				sb.append("bean.get");
				sb.append(
						Character.toUpperCase(field.getSimpleName().charAt(0)))
						.append(field.getSimpleName().subSequence(1,
								field.getSimpleName().length()));
				sb.append("() !=null ? DatabaseUtils.sqlEscapeString(bean.get");
				sb.append(
						Character.toUpperCase(field.getSimpleName().charAt(0)))
						.append(field.getSimpleName().subSequence(1,
								field.getSimpleName().length()))
						.append("().getId().toString()) : null);\n");
			}
		}
	}

	@Override
	public StringBuilder getCompletedString() {
		StringBuilder result = new StringBuilder();
		result.append(
				"\n\tpublic static SmartORMInsertBuilder getInsert" + className
						+ "(List<" + type + "> beans){\n")
				.append("\t\tSmartORMInsertBuilder args = new SmartORMInsertBuilder();\n")
				.append("\t\tfor (" + type + " bean : beans) {\n")
				.append(sb)
				.append("\t\t\targs.endBean();\n\t\t}\n\t\treturn args;\n\t}\n");
		result.append(
				"\tpublic static SmartORMInsertBuilder getInsert" + className
						+ "(" + type + " bean){\n")
				.append("\t\tSmartORMInsertBuilder args = new SmartORMInsertBuilder();\n")
				.append(sb)
				.append("\t\t\targs.endBean();\n\t\treturn args;\n\t}\n");
		return result;
	}

}
