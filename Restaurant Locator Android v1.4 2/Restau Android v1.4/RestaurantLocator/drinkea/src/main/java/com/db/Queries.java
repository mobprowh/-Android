package com.db;

import java.util.ArrayList;
import com.models.Category;
import com.models.Favorite;
import com.models.Photo;
import com.models.Restaurant;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Queries {
	
	private SQLiteDatabase db;
	private DbHelper dbHelper;

	public Queries(SQLiteDatabase db, DbHelper dbHelper) {
		this.db = db;
		this.dbHelper = dbHelper;
	}
	
	public void deleteTable(String tableName) {
		db = dbHelper.getWritableDatabase();
		try{
			db.delete(tableName, null, null);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
        db.close();
	}

	public void insertRestaurant(Restaurant entry) {
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("address", entry.address);
		values.put("amenities", entry.amenities);
		values.put("created_at", entry.created_at);
		values.put("desc", entry.desc);
		values.put("email", entry.email);
		values.put("featured", entry.featured);
		values.put("hours", entry.hours);
		values.put("lat", entry.lat);
		values.put("lon", entry.lon);
		values.put("name", entry.name);
		values.put("phone", entry.phone);
		values.put("website", entry.website);
		values.put("category_id", entry.category_id);
		values.put("food_rating", entry.food_rating);
		values.put("price_rating", entry.price_rating);
		values.put("restaurant_id", entry.restaurant_id);
        db.insert("restaurants", null, values);
        db.close();
	}
	
	public void insertFavorite(int restaurantId) {
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("restaurant_id", restaurantId);
        db.insert("favorites", null, values);
        db.close();
	}
	
	public void deleteFavorite(int favoriteId) {
		db = dbHelper.getWritableDatabase();
		try{
			db.delete("favorites", "favorite_id = " + favoriteId, null);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void insertPhoto(Photo entry) {
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("created_at", entry.created_at);
		values.put("photo_url", entry.photo_url);
		values.put("thumb_url", entry.thumb_url);
		values.put("photo_id", entry.photo_id);
		values.put("restaurant_id", entry.restaurant_id);
        db.insert("photos", null, values);
        db.close();
	}
	
	public void insertCategory(Category entry) {
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("category", entry.category);
		values.put("created_at", entry.created_at);
		values.put("category_id", entry.category_id);
        db.insert("categories", null, values);
        db.close();
	}
	
	public Category getCategoryByCategoryId(int categoryId) {
		Category entry = null;
		String sql = String.format("SELECT * FROM categories WHERE category_id = %d", categoryId);
		ArrayList<Category> list = new ArrayList<Category>();
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery(sql, null); 
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				entry = new Category();
				entry.category = mCursor.getString( mCursor.getColumnIndex("category") );
				entry.category_id = mCursor.getInt( mCursor.getColumnIndex("category_id") );
				entry.created_at = mCursor.getString( mCursor.getColumnIndex("created_at") );
				list.add(entry);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		db.close();
		return entry;
	}
	
	public Category getCategoryByCategoryName(String category) {
		Category entry = null;
		String sql = String.format("SELECT * FROM categories WHERE category = '%s'", category);
		ArrayList<Category> list = new ArrayList<Category>();
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery(sql, null); 
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				entry = new Category();
				entry.category = mCursor.getString( mCursor.getColumnIndex("category") );
				entry.category_id = mCursor.getInt( mCursor.getColumnIndex("category_id") );
				entry.created_at = mCursor.getString( mCursor.getColumnIndex("created_at") );
				list.add(entry);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		db.close();
		return entry;
	}

	public ArrayList<Category> getCategories() {
		ArrayList<Category> list = new ArrayList<Category>();
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM categories ORDER BY category ASC", null); 
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				Category entry = new Category();
				entry.category = mCursor.getString( mCursor.getColumnIndex("category") );
				entry.category_id = mCursor.getInt( mCursor.getColumnIndex("category_id") );
				entry.created_at = mCursor.getString( mCursor.getColumnIndex("created_at") );
				list.add(entry);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		db.close();
		return list;
	}
	
	private ArrayList<Restaurant> getRestaurantsUsingSQL(String sql) {
		ArrayList<Restaurant> list = new ArrayList<Restaurant>();
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery(sql, null); 
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				Restaurant entry = new Restaurant();
				entry.address = mCursor.getString( mCursor.getColumnIndex("address") );
				entry.amenities = mCursor.getString( mCursor.getColumnIndex("amenities") );
				entry.category_id = mCursor.getInt( mCursor.getColumnIndex("category_id") );
				entry.created_at = mCursor.getString( mCursor.getColumnIndex("created_at") );
				entry.desc = mCursor.getString( mCursor.getColumnIndex("desc") );
				entry.email = mCursor.getString( mCursor.getColumnIndex("email") );
				entry.featured = mCursor.getString( mCursor.getColumnIndex("featured") );
				entry.food_rating = mCursor.getFloat( mCursor.getColumnIndex("food_rating") );
				entry.hours = mCursor.getString( mCursor.getColumnIndex("hours") );
				entry.lat = mCursor.getString( mCursor.getColumnIndex("lat") );
				entry.lon = mCursor.getString( mCursor.getColumnIndex("lon") );
				entry.name = mCursor.getString( mCursor.getColumnIndex("name") );
				entry.phone = mCursor.getString( mCursor.getColumnIndex("phone") );
				entry.price_rating = mCursor.getFloat( mCursor.getColumnIndex("price_rating") );
				entry.restaurant_id = mCursor.getInt( mCursor.getColumnIndex("restaurant_id") );
				entry.website = mCursor.getString( mCursor.getColumnIndex("website") );
				list.add(entry);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		db.close();
		return list;
	}

	public ArrayList<Restaurant> getFeaturedRestaurants() {
		String sql = String.format("SELECT * FROM restaurants WHERE featured = 1 ORDER BY name ASC");
		ArrayList<Restaurant> list = getRestaurantsUsingSQL(sql);
		return list;
	}

	public ArrayList<Restaurant> getRestaurants() {
		ArrayList<Restaurant> list = getRestaurantsUsingSQL("SELECT * FROM restaurants ORDER BY name ASC");
		return list;
	}
	
	public ArrayList<Restaurant> getRestaurantsByCategoryId(int categoryId) {
		String sql = String.format("SELECT * FROM restaurants WHERE category_id = %d ORDER BY name ASC", categoryId);
		ArrayList<Restaurant> list = getRestaurantsUsingSQL(sql);
		return list;
	}
	
	public Restaurant getRestaurantByRestaurantId(int restaurantId) {
		String sql = String.format("SELECT * FROM restaurants WHERE restaurant_id = %d ORDER BY name ASC", restaurantId);
		ArrayList<Restaurant> list = getRestaurantsUsingSQL(sql);
		return list.size() == 0 ? null : list.get(0);
	}
	
	
	private ArrayList<Photo> getPhotosBySQL(String sql) {
		ArrayList<Photo> list = new ArrayList<Photo>();
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery(sql, null); 
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				Photo entry = new Photo();
				entry.created_at = mCursor.getString( mCursor.getColumnIndex("created_at") );
				entry.photo_id = mCursor.getInt( mCursor.getColumnIndex("photo_id") );
				entry.photo_url = mCursor.getString( mCursor.getColumnIndex("photo_url") );
				entry.restaurant_id = mCursor.getInt( mCursor.getColumnIndex("restaurant_id") );
				entry.thumb_url = mCursor.getString( mCursor.getColumnIndex("thumb_url") );
				list.add(entry);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		db.close();
		return list;
	}

	public ArrayList<Photo> getPhotos() {
		return getPhotosBySQL("SELECT * FROM photos");
	}
	
	public ArrayList<Photo> getPhotosByRestaurantId(int restaurantId) {
		String sql = String.format("SELECT * FROM photos WHERE restaurant_id = %d", restaurantId);
		return getPhotosBySQL(sql);
	}

	public Photo getPhotoByPhotoId(int photoId) {
		String sql = String.format("SELECT * FROM photos WHERE photo_id = %d", photoId);
		ArrayList<Photo> photos = getPhotosBySQL(sql);
		return photos.size() == 0 ? null : photos.get(0);
	}
	
	public Photo getPhotoByRestaurantId(int restaurantId) {
		Photo entry = null;
		String sql = String.format("SELECT * FROM photos WHERE restaurant_id = %d", restaurantId);
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery(sql , null); 
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				entry = new Photo();
				entry.created_at = mCursor.getString( mCursor.getColumnIndex("created_at") );
				entry.photo_id = mCursor.getInt( mCursor.getColumnIndex("photo_id") );
				entry.photo_url = mCursor.getString( mCursor.getColumnIndex("photo_url") );
				entry.restaurant_id = mCursor.getInt( mCursor.getColumnIndex("restaurant_id") );
				entry.thumb_url = mCursor.getString( mCursor.getColumnIndex("thumb_url") );
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		db.close();
		return entry;
	}
	
	public ArrayList<Favorite> getFavorites() {
		ArrayList<Favorite> list = new ArrayList<Favorite>();
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM favorites", null); 
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				Favorite entry = new Favorite();
				entry.favorite_id = mCursor.getInt(mCursor.getColumnIndex("favorite_id"));
				entry.restaurant_id = mCursor.getInt( mCursor.getColumnIndex("restaurant_id") );
				list.add(entry);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		db.close();
		return list;
	}

	public ArrayList<Restaurant> getFavoriteRestaurants() {
		String sql = String.format("SELECT * FROM restaurants INNER JOIN favorites ON restaurants.restaurant_id = favorites.restaurant_id ORDER BY name ASC");
		ArrayList<Restaurant> list = getRestaurantsUsingSQL(sql);
		return list;
	}
	
	public Restaurant getFavoriteRestaurantsByRestaurantId(int restaurantId) {
		String sql = String.format(
				"SELECT * FROM restaurants INNER JOIN favorites ON " 
						+ "restaurants.restaurant_id = favorites.restaurant_id " 
						+ "WHERE restaurants.restaurant_id = %d", restaurantId);
		
		ArrayList<Restaurant> list = getRestaurantsUsingSQL(sql);
		return list.size() == 0 ? null : list.get(0);
	}

	public Favorite getFavoriteByRestaurantId(int restaurantId) {
		Favorite entry = null;
		String sql = String.format("SELECT * FROM favorites WHERE restaurant_id = %d", restaurantId);
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery(sql , null); 
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				entry = new Favorite();
				entry.favorite_id = mCursor.getInt( mCursor.getColumnIndex("favorite_id") );
				entry.restaurant_id = mCursor.getInt( mCursor.getColumnIndex("restaurant_id") );
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		db.close();
		return entry;
	}
	
	public ArrayList<String> getCategoryNames() {
		ArrayList<String> list = new ArrayList<String>();
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM categories", null); 
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				String cat = mCursor.getString( mCursor.getColumnIndex("category") );
				list.add(cat);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		db.close();
		return list;
	}

	public void closeDatabase(){
		db.close();
	}
}
