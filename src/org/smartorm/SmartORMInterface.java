package org.smartorm;

public interface SmartORMInterface {
	public static final String ID = "id";

	public Object getId();

	public void persist();

	public void refresh();

	public void setId(Object id);
}
