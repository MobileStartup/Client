package com.example.translation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TranslationDataSource {

	private SQLiteDatabase dataBase;
	private TranslationSQLiteHelper dbHelper;
	private String[] allColumns = {TranslationSQLiteHelper.COLUMN_KEY, TranslationSQLiteHelper.COLUMN_VALUE};
	
	public TranslationDataSource (Context context){
		dbHelper = new TranslationSQLiteHelper(context);
	}
	
	public void open(){
		dataBase = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public void createTranslationRecord(String key, String value){
		ContentValues values = new ContentValues();
		values.put(TranslationSQLiteHelper.COLUMN_KEY, key);
		values.put(TranslationSQLiteHelper.COLUMN_VALUE, value);
		dataBase.insert(TranslationSQLiteHelper.TABLE_TRANSLATIONS, null, values);
		Log.i("DATA BASE", "Records inserted into data base, key: " + key + ", value: " + value);
	}
	
	public String queryTranslationRecord(String key){
		Cursor cursor = dataBase.query(TranslationSQLiteHelper.TABLE_TRANSLATIONS,
		        allColumns, TranslationSQLiteHelper.COLUMN_KEY + " = '" + key + "'", null,
		        null, null, null);
		String result = "";
		cursor.moveToFirst();
		if(!cursor.isAfterLast()){
			result += cursor.getString(1);
			cursor.moveToNext();
		}
		Log.i("DATA BASE", "Query result from data base: " + key + ", value: " + result);
		return result;
	}
}
