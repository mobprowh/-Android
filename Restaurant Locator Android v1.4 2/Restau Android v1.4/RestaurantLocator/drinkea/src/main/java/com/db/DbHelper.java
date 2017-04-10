package com.db;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	
	static final String TAG = "DbHelper";
	static final String DB_NAME = "db_restaurants";
	static final int DB_VERSION = 2;
	static Activity activity;
	
	public DbHelper(Activity act) {
		super(act.getApplicationContext(), DB_NAME, null, DB_VERSION);
		activity = act;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
			
	    db.execSQL("CREATE TABLE IF NOT EXISTS categories("
	    		+ "category_id INTEGER PRIMARY KEY,"
	    		+ "category TEXT,"
	    		+ "created_at TEXT"
	    		+ ");");
	    
	    db.execSQL("CREATE TABLE IF NOT EXISTS favorites("
	    		+ "favorite_id INTEGER PRIMARY KEY AUTOINCREMENT,"
	    		+ "restaurant_id INTEGER"
	    		+ ");");
	    
	    
	    db.execSQL("CREATE TABLE IF NOT EXISTS photos("
	    		+ "photo_id INTEGER PRIMARY KEY,"
	    		+ "created_at TEXT,"
	    		+ "thumb_url TEXT,"
	    		+ "photo_url TEXT,"
	    		+ "restaurant_id INTEGER"
	    		+ ");");
	    
	    db.execSQL("CREATE TABLE IF NOT EXISTS restaurants("
	    		+ "restaurant_id INTEGER PRIMARY KEY," //AUTOINCREMENT
	    		+ "address TEXT,"
	    		+ "amenities TEXT,"
	    		+ "created_at TEXT,"
	    		+ "desc TEXT,"
	    		+ "email TEXT,"
	    		+ "featured TEXT,"
	    		+ "food_rating FLOAT,"
	    		+ "hours TEXT,"
	    		+ "lat TEXT,"
	    		+ "lon TEXT,"
	    		+ "name TEXT,"
	    		+ "phone TEXT,"
	    		+ "price_rating FLOAT,"
	    		+ "website TEXT,"
	    		+ "category_id INTEGER"
	    		+ ");");
	    
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { 
		db.execSQL("DROP TABLE IF EXISTS categories");
		db.execSQL("DROP TABLE IF EXISTS favorites");
		db.execSQL("DROP TABLE IF EXISTS photos");
		db.execSQL("DROP TABLE IF EXISTS restaurants");
	}
}