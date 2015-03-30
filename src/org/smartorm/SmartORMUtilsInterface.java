package org.smartorm;

import javax.lang.model.element.Element;

public interface SmartORMUtilsInterface {

	public StringBuilder getStringBuilder();

	public String getClassName();

	public String getSuperClassName();

	public StringBuilder getCompletedString();

	public void addField(Element field) throws NoClassDefFoundError;

	public boolean isAbstract();
}
