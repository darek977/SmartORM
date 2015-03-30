package org.smartorm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class SmartORMDatabaseHelper extends SQLiteOpenHelper {
	protected static SQLiteDatabase db;
	protected static SmartORMDatabaseHelper instance;
	protected volatile boolean isOpen = true;

	public SmartORMDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		SmartORMDatabaseHelper.instance = this;
	}

	public static SQLiteDatabase getDB() {
		if (db == null || !db.isOpen()) {
			db = instance.getWritableDatabase();
		}
		return db;
	}

	public static void setDB(SQLiteDatabase db) {
		SmartORMDatabaseHelper.db = db;
	}

	public boolean isOpen() {
		return isOpen;
	}

	@Override
	public void close() {
		super.close();
		isOpen = false;
	}
}
