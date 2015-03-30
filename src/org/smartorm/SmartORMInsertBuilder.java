package org.smartorm;

import java.util.ArrayList;
import java.util.UUID;

public class SmartORMInsertBuilder {
	private ArrayList<StringBuilder> values = new ArrayList<StringBuilder>();
	private StringBuilder columns = null;
	private StringBuilder value;
	private StringBuilder column;

	private int index = 0;

	public SmartORMInsertBuilder() {

	}

	public StringBuilder getAllValues() {
		StringBuilder sb = null;
		for (StringBuilder s : values) {
			if (sb == null) {
				sb = new StringBuilder();
				sb.append(s);
			} else {
				sb.append(" ,").append(s);
			}
		}

		return sb;
	}

	public boolean isNext() {
		if (index < values.size()) {
			return true;
		}
		return false;
	}

	public void clearIndex() {
		index = 0;
	}

	public StringBuilder getNumberOfValues(int count) {

		StringBuilder sb = null;
		int size = values.size();
		if (count >= size) {
			index = size;
			return getAllValues();
		}
		if (index < size && count > 0) {
			if (index + count >= size) {
				count = size - index;
			}
			for (int i = index; i < index + count; i++) {

				if (sb == null) {
					sb = new StringBuilder();
					sb.append(values.get(i));
				} else {
					sb.append(" ,").append(values.get(i));
				}
			}
			index += count;

		}
		return sb;
	}

	public void put(String col, String val) {
		if (columns == null) {
			column = addStringColumn(column, col);
		}
		value = addString(value, val);
	}

	public void put(String col, UUID val) {
		if (columns == null) {
			column = addStringColumn(column, col);
		}
		if (val == null) {
			value = addString(value, null);
		} else {
			value = addString(value, val.toString());
		}
	}

	public void put(String col, int val) {
		if (columns == null) {
			column = addStringColumn(column, col);
		}
		value = addInt(value, val);
	}

	public void put(String col, double val) {
		if (columns == null) {
			column = addStringColumn(column, col);
		}
		value = addDouble(value, val);
	}

	public void put(String col, boolean val) {
		if (columns == null) {
			column = addStringColumn(column, col);
		}
		value = addBoolean(value, val);
	}

	public void put(String col, long val) {
		if (columns == null) {
			column = addStringColumn(column, col);
		}
		value = addLong(value, val);
	}

	public void endBean() {
		value.append(")");
		values.add(value);
		value = null;
		if (columns == null) {
			column.append(")");
			columns = column;
		}
	}

	private StringBuilder addString(StringBuilder b, String s) {
		if (b == null) {
			b = new StringBuilder();
			b.append("( ").append(s);
		} else {
			b.append(", ").append(s);
		}
		return b;
	}

	private StringBuilder addStringColumn(StringBuilder b, String s) {
		if (b == null) {
			b = new StringBuilder();
			b.append("( \"").append(s).append("\"");
		} else {
			b.append(", \"").append(s).append("\"");
		}
		return b;
	}

	private StringBuilder addLong(StringBuilder b, long s) {
		if (b == null) {
			b = new StringBuilder();
			b.append("( ").append(s);
		} else {
			b.append(", ").append(s);
		}
		return b;
	}

	private StringBuilder addInt(StringBuilder b, int s) {
		if (b == null) {
			b = new StringBuilder();
			b.append("( ").append(s);
		} else {
			b.append(", ").append(s);
		}
		return b;
	}

	private StringBuilder addDouble(StringBuilder b, double s) {
		if (b == null) {
			b = new StringBuilder();
			b.append("( ").append(s);
		} else {
			b.append(", ").append(s);
		}
		return b;
	}

	private StringBuilder addBoolean(StringBuilder b, boolean s) {
		if (b == null) {
			b = new StringBuilder();
			b.append("( ").append(s ? 1 : 0);
		} else {
			b.append(", ").append(s ? 1 : 0);
		}
		return b;
	}

	public StringBuilder getColumns() {
		return columns;
	}

	public void setColumns(StringBuilder columns) {
		this.columns = columns;
	}

	public ArrayList<StringBuilder> getValues() {
		return values;
	}

	public void setValues(ArrayList<StringBuilder> values) {
		this.values = values;
	}
}
