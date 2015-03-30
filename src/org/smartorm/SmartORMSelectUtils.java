package org.smartorm;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

class SmartORMSelectUtils extends SmartORMUtils implements
		SmartORMUtilsInterface {
	private final List<String> selectList;

	protected SmartORMSelectUtils(Element table) throws NoClassDefFoundError {
		super(table);
		selectList = new ArrayList<String>();
	}

	protected static SmartORMSelectUtils compareSelect(String className) {
		return new SmartORMSelectUtils(className);
	}

	private SmartORMSelectUtils(String className) {
		super(className);
		selectList = new ArrayList<String>();
	}

	@Override
	public StringBuilder getCompletedString() {
		StringBuilder result = new StringBuilder();

		result.append("\n\t public static String[] selectID = {\"")
				.append(SmartORMInterface.ID).append("\"};\n\t");

		result.append("\n\t public static String[] select = {").append(sb)
				.append("};\n\t");
		return result;
	}

	@Override
	public void addField(Element field) throws NoClassDefFoundError {

		if (sb.length() > 0) {
			append(sb, ",\"%s\"\n\t\t", field
					.getAnnotation(SmartORMField.class).columnName());
		} else {
			append(sb, "\"%s\"\n\t\t", field.getAnnotation(SmartORMField.class)
					.columnName());
		}
		selectList.add(field.getAnnotation(SmartORMField.class).columnName());
	}

	public List<String> getSelectList() {
		return selectList;
	}

	public int indexOf(String s) {
		int i = selectList.indexOf(s);
		if (i == -1) {
			throw new IllegalArgumentException("In this table column " + s
					+ " doesn't exist!");
		}
		return i;
	}
}
