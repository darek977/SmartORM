package org.smartorm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface SmartORMField {
	public final static String INDEX_COLUMN = "_id";
	public final static String ID = "id";

	String columnName();

	SmartORMDataType dataType() default SmartORMDataType.PRIMITIVE_TYPE;

	boolean canBeNull() default true;

	String defaultValue() default SmartORMUtils.ABSTRACT_CLASS;

	boolean id() default false;

	boolean index() default false;
}
