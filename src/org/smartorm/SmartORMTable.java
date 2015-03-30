package org.smartorm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
/**
 * superClass is simple name database class which is extended by this class
 * @author Dariusz Motyka
 *
 */
public @interface SmartORMTable {

	String tableName() default SmartORMUtils.ABSTRACT_CLASS;

	String superClass() default SmartORMUtils.ABSTRACT_CLASS;
	
	
	
}