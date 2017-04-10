package com.db;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	
	static final String TAG = "DbHelper";
	static final String DB_NAME = "storefinder_db";
	static final int DB_VERSION = 1;
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
	    		+ "category_icon TEXT,"
	    		+ "created_at INTEGER, "
	    		+ "is_deleted INTEGER, "
	    		+ "updated_at INTEGER "
	    		+ ");");
	    
	    db.execSQL("CREATE TABLE IF NOT EXISTS stores("
	    		+ "store_id INTEGER PRIMARY KEY,"
	    		+ "category_id NTEGER, "
	    		+ "created_at INTEGER, "
	    		+ "distance TEXT, "
	    		+ "email TEXT, "
	    		+ "featured INTEGER, "
	    		+ "icon_id INTEGER, "
	    		+ "lat TEXT, "
	    		+ "lon TEXT, "
	    		+ "phone_no TEXT, "
	    		+ "rating_count TEXT, "
	    		+ "rating_total TEXT, "
	    		+ "sms_no TEXT, "
	    		+ "store_address TEXT, "
	    		+ "store_desc TEXT, "
	    		+ "store_name TEXT, "
	    		+ "is_deleted INTEGER, "
	    		+ "updated_at INTEGER, "
	    		+ "website TEXT "
	    		
	    		+ ");");
	    
	    
	    db.execSQL("CREATE TABLE IF NOT EXISTS reviews("
	    		+ "review_id INTEGER PRIMARY KEY,"
	    		+ "created_at TEXT,"
	    		+ "first_name TEXT,"
	    		+ "last_name TEXT,"
				+ "review TEXT,"
				+ "store_id INTEGER,"
				+ "updated_at INTEGER,"
				+ "is_deleted INTEGER, "
				+ "user_id INTEGER"
	    		+ ");");
	    
	    db.execSQL("CREATE TABLE IF NOT EXISTS ratings("
	    		+ "rating_id INTEGER PRIMARY KEY," //AUTOINCREMENT
	    		+ "created_at INTEGER,"
	    		+ "rating TEXT,"
	    		+ "store_id INTEGER,"
	    		+ "updated_at INTEGER,"
	    		+ "is_deleted INTEGER, "
	    		+ "user_id INTEGER"
	    		+ ");");
	    
	    db.execSQL("CREATE TABLE IF NOT EXISTS photos("
	    		+ "photo_id INTEGER PRIMARY KEY," //AUTOINCREMENT
	    		+ "created_at INTEGER,"
	    		+ "photo_url TEXT,"
	    		+ "store_id INTEGER,"
	    		+ "thumb_url TEXT,"
	    		+ "is_deleted INTEGER, "
	    		+ "updated_at INTEGER"
	    		+ ");");
	    
	    db.execSQL("CREATE TABLE IF NOT EXISTS news("
	    		+ "news_id INTEGER PRIMARY KEY," //AUTOINCREMENT
	    		+ "created_at INTEGER,"
	    		+ "news_content TEXT,"
	    		+ "news_title TEXT,"
	    		+ "news_url TEXT,"
	    		+ "photo_url TEXT,"
	    		+ "is_deleted INTEGER, "
	    		+ "updated_at INTEGER"
	    		+ ");");
	    
	    
	    db.execSQL("CREATE TABLE IF NOT EXISTS favorites("
	    		+ "favorite_id INTEGER PRIMARY KEY AUTOINCREMENT," //AUTOINCREMENT
	    		+ "store_id INTEGER"
	    		+ ");");
	    
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { 
		db.execSQL("DROP TABLE IF EXISTS categories");
		db.execSQL("DROP TABLE IF EXISTS favorites");
		db.execSQL("DROP TABLE IF EXISTS stores");
		db.execSQL("DROP TABLE IF EXISTS reviews");
		db.execSQL("DROP TABLE IF EXISTS ratings");
		db.execSQL("DROP TABLE IF EXISTS photos");
		db.execSQL("DROP TABLE IF EXISTS news");
		db.execSQL("DROP TABLE IF EXISTS favorites");
		
	}
}