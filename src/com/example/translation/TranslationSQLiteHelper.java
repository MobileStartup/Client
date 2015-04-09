package com.example.translation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TranslationSQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_TRANSLATIONS = "translations";
	public static final String COLUMN_KEY = "key";
	public static final String COLUMN_VALUE = "value";
	
	public static final String DATABASE_NAME = "translations.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "create table " + TABLE_TRANSLATIONS + "(" + 
			COLUMN_KEY + " text primary key, " + COLUMN_VALUE + " text not null);";

	public TranslationSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(TranslationSQLiteHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSLATIONS);
		    onCreate(database);
	}

}
