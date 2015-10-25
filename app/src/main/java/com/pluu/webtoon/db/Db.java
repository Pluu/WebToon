package com.pluu.webtoon.db;

import android.database.Cursor;

/**
 * Created by nohhs on 2015-03-17.
 */
public final class Db {
	public static final int BOOLEAN_FALSE = 0;
	public static final int BOOLEAN_TRUE = 1;

	public static String getString(Cursor cursor, String columnName) {
		return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
	}

	public static boolean getBoolean(Cursor cursor, String columnName) {
		return getInt(cursor, columnName) == BOOLEAN_TRUE;
	}

	public static long getLong(Cursor cursor, String columnName) {
		return cursor.getLong(cursor.getColumnIndexOrThrow(columnName));
	}

	public static int getInt(Cursor cursor, String columnName) {
		return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
	}

	private Db() {
		throw new AssertionError("No instances.");
	}
}
