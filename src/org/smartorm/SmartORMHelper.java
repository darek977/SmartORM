package org.smartorm;

class SmartORMHelper {
	private final SmartORMSelectUtils select;
	private final SmartORMInsertUtils insert;
	private final SmartORMCreateBeanUtils create;
	private final SmartORMTableUtils table;
	private final SmartORMGetterUtils getter;
	private final SmartORMCreateLazyBeanUtils lazy;

	protected SmartORMHelper(SmartORMSelectUtils select,
			SmartORMInsertUtils insert, SmartORMCreateBeanUtils create,
			SmartORMTableUtils table, SmartORMGetterUtils getter,
			SmartORMCreateLazyBeanUtils lazy) {

		this.select = select;
		this.create = create;
		this.getter = getter;
		this.insert = insert;
		this.lazy = lazy;
		this.table = table;
	}

	protected SmartORMSelectUtils getSelect() {
		return select;
	}

	protected SmartORMInsertUtils getInsert() {
		return insert;
	}

	protected SmartORMCreateBeanUtils getCreate() {
		return create;
	}

	protected SmartORMTableUtils getTable() {
		return table;
	}

	protected SmartORMGetterUtils getGetter() {
		return getter;
	}

	protected SmartORMCreateLazyBeanUtils getLazy() {
		return lazy;
	}
}
