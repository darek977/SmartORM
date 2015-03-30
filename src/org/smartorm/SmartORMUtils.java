package org.smartorm;

import java.util.Locale;

import javax.lang.model.element.Element;

abstract class SmartORMUtils implements SmartORMUtilsInterface {
	protected final StringBuilder sb;
	protected final String className;
	protected final String superClassName;
	protected final Element table;
	protected static final Locale LOCALE = Locale.US;
	public static final String ABSTRACT_CLASS = "null";
	protected final String type;

	protected final void append(StringBuilder sb, String format, Object... args) {
		sb.append(String.format(LOCALE, format, args));
	}

	protected SmartORMUtils(Element table) throws NoClassDefFoundError {
		sb = new StringBuilder();
		if (table == null) {
			throw new IllegalArgumentException("Element can not be null");
		}
		this.className = table.getSimpleName().toString();
		this.superClassName = table.getAnnotation(SmartORMTable.class)
				.superClass();
		this.table = table;
		this.type = table.asType().toString();
	}

	protected SmartORMUtils(String className) {
		this.className = className;
		this.sb = null;
		this.superClassName = null;
		this.table = null;
		this.type = null;
	}

	@Override
	public StringBuilder getStringBuilder() {
		return sb;
	}

	@Override
	public boolean isAbstract() {
		return table.getAnnotation(SmartORMTable.class).tableName()
				.equals(ABSTRACT_CLASS);
	};

	@Override
	public String getClassName() {
		return className;
	}

	@Override
	public String getSuperClassName() {
		if (superClassName.equals(ABSTRACT_CLASS)) {
			return null;
		} else {
			return superClassName;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof SmartORMUtils) {
			return className.equals(((SmartORMUtils) obj).getClassName());
		} else {
			return false;
		}
	}

	public String getTableName() {
		return table.getAnnotation(SmartORMTable.class).tableName();
	}

	public String getType() {
		return type;
	}

	public Element getTable() {
		return table;
	}
}
