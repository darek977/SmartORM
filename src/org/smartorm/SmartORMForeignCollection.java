package org.smartorm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface SmartORMForeignCollection {

	boolean autoRefreshForeignCollection() default false;

	String foreignColumnName();
}
