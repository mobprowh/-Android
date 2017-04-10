package com.db;

import java.util.ArrayList;
import com.models.Category;
import com.models.Favorite;
import com.models.News;
import com.models.Photo;
import com.models.Store;

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
	
	public void insertNews(News entry) {
		
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("news_content", entry.getNews_content());
		values.put("news_title", entry.getNews_title());
		values.put("news_url", entry.getNews_url());
		values.put("photo_url", entry.getPhoto_url());
		values.put("created_at", entry.getCreated_at());
		values.put("is_deleted", entry.getIs_deleted());
		values.put("news_id", entry.getNews_id());
		values.put("updated_at", entry.getUpdated_at());

		db.insert("news", null, values);
		db.close();
	}
	
	public void insertStore(Store entry) {
		
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("email", entry.getEmail());
		values.put("phone_no", entry.getPhone_no());
		values.put("sms_no", entry.getSms_no());
		values.put("store_address", entry.getStore_address());
		values.put("store_desc", entry.getStore_desc());
		values.put("store_name", entry.getStore_name());
		values.put("website", entry.getWebsite());
		values.put("category_id", entry.getCategory_id());
		values.put("created_at", entry.getCreated_at());
		values.put("distance", entry.getDistance());
		
		values.put("featured", entry.getFeatured());
		values.put("icon_id", entry.getIcon_id());
		values.put("is_deleted", entry.getIs_deleted());
		values.put("lat", entry.getLat());
		values.put("lon", entry.getLon());
		values.put("rating_count", entry.getRating_count());
		values.put("rating_total", entry.getRating_total());
		values.put("store_id", entry.getStore_id());
		values.put("updated_at", entry.getUpdated_at());
		
		db.insert("stores", null, values);
		db.close();
	}
	
	public void updateStore(Store entry) {
		
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("email", entry.getEmail());
		values.put("phone_no", entry.getPhone_no());
		values.put("sms_no", entry.getSms_no());
		values.put("store_address", entry.getStore_address());
		values.put("store_desc", entry.getStore_desc());
		values.put("store_name", entry.getStore_name());
		values.put("website", entry.getWebsite());
		values.put("category_id", entry.getCategory_id());
		values.put("created_at", entry.getCreated_at());
		values.put("distance", entry.getDistance());
		
		values.put("featured", entry.getFeatured());
		values.put("icon_id", entry.getIcon_id());
		values.put("is_deleted", entry.getIs_deleted());
		values.put("lat", entry.getLat());
		values.put("lon", entry.getLon());
		values.put("rating_count", entry.getRating_count());
		values.put("rating_total", entry.getRating_total());
		values.put("store_id", entry.getStore_id());
		values.put("updated_at", entry.getUpdated_at());
		values.put("updated_at", entry.getUpdated_at());
		
		db.update("stores", values, "store_id = " + entry.getStore_id(), null);
		db.close();
	}
	
	public void insertCategory(Category entry) {
		
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("category", entry.getCategory());
		values.put("category_icon", entry.getCategory_icon());
		values.put("category_id", entry.getCategory_id());
		values.put("created_at", entry.getCreated_at());
		values.put("is_deleted", entry.getIs_deleted());
		values.put("updated_at", entry.getUpdated_at());
		
		db.insert("categories", null, values);
		db.close();
	}
	
	public void insertPhoto(Photo entry) {
		
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("photo_url", entry.getPhoto_url());
		values.put("thumb_url", entry.getThumb_url());
		values.put("created_at", entry.getCreated_at());
		values.put("is_deleted", entry.getIs_deleted());
		values.put("photo_id", entry.getPhoto_id());
		values.put("store_id", entry.getStore_id());
		values.put("updated_at", entry.getUpdated_at());
		
		db.insert("photos", null, values);
		db.close();
	}
	
	public void insertFavorite(Favorite entry) {
		
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("store_id", entry.getStore_id());
		
		db.insert("favorites", null, values);
		db.close();
	}

	public void deleteNews(int news_id) {
		db = dbHelper.getWritableDatabase();
		db.delete("news", "news_id = " + news_id, null);
		db.close();
	}

	public void deletePhoto(int photo_id) {
		db = dbHelper.getWritableDatabase();
		db.delete("photos", "photo_id = " + photo_id, null);
		db.close();
	}

	public void deleteCategory(int category_id) {
		db = dbHelper.getWritableDatabase();
		db.delete("categories", "category_id = " + category_id, null);
		db.close();
	}

	public void deleteStore(int store_id) {
		db = dbHelper.getWritableDatabase();
		db.delete("stores", "store_id = " + store_id, null);
		db.close();
	}

	public void deleteFavorite(int store_id) {
		db = dbHelper.getWritableDatabase();
		db.delete("favorites", "store_id = " + store_id, null);
		db.close();
	}
	
	public Favorite getFavoriteByStoreId(int storeId) {
		
		Favorite entry = null;
		String sql = String.format("SELECT * FROM favorites WHERE store_id = %d", storeId);
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery(sql , null); 
		mCursor.moveToFirst();
		
		if (!mCursor.isAfterLast()) {
			do {
				entry = new Favorite();
				entry.setFavorite_id( mCursor.getInt( mCursor.getColumnIndex("favorite_id")) );
				entry.setStore_id( mCursor.getInt( mCursor.getColumnIndex("store_id")) );
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return entry;
	}
	
	public ArrayList<News> getNews() {
		
		ArrayList<News> list = new ArrayList<News>();
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM news ORDER BY updated_at DESC", null); 
		mCursor.moveToFirst();
		
		if (!mCursor.isAfterLast()) {
			do {
				
				News news = formatNews(mCursor);
				
				list.add(news);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return list;
	}
	
	public News getNewsByNewsId(int newsId) {
		
		News news = null;
		db = dbHelper.getReadableDatabase();
		
		String sql = String.format("SELECT * FROM news WHERE news_id = %d", newsId);
		Cursor mCursor = db.rawQuery(sql, null); 
		mCursor.moveToFirst();
		
		if (!mCursor.isAfterLast()) {
			do {

                news = formatNews(mCursor);
				
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return news;
	}
	
	public ArrayList<Favorite> getFavorites() {
		
		ArrayList<Favorite> list = new ArrayList<Favorite>();
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM favorites", null); 
		mCursor.moveToFirst();
		
		if (!mCursor.isAfterLast()) {
			do {
				
				Favorite fave = new Favorite();
				fave.setFavorite_id( mCursor.getInt( mCursor.getColumnIndex("favorite_id")) );
				fave.setStore_id( mCursor.getInt( mCursor.getColumnIndex("store_id")) );
				
				list.add(fave);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return list;
	}
	
	
	public ArrayList<Store> getStores() {
		
		ArrayList<Store> list = new ArrayList<Store>();
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM stores", null); 
		mCursor.moveToFirst();
		
		if (!mCursor.isAfterLast()) {
			do {

                Store entry = formatStore(mCursor);
				
				list.add(entry);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return list;
	}
	
	public ArrayList<Store> getStoresByCategoryId(int categoryId) {
		
		ArrayList<Store> list = new ArrayList<Store>();
		db = dbHelper.getReadableDatabase();
		
		String sql = String.format("SELECT * FROM stores WHERE category_id = %d", categoryId);
		Cursor mCursor = db.rawQuery(sql, null); 
		mCursor.moveToFirst();
		
		if (!mCursor.isAfterLast()) {
			do {

                Store entry = formatStore(mCursor);
				
				list.add(entry);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return list;
	}
	
	
	public Store getStoresByStoreId(int storeId) {
		
		Store entry = null;
		db = dbHelper.getReadableDatabase();
		
		String sql = String.format("SELECT * FROM stores WHERE store_id = %d", storeId);
		Cursor mCursor = db.rawQuery(sql, null); 
		mCursor.moveToFirst();
		
		if (!mCursor.isAfterLast()) {
			do {

                entry = formatStore(mCursor);
				
			} while (mCursor.moveToNext());
		}
		mCursor.close();

		return entry;
	}
	
	public ArrayList<Store> getStoresFeatured() {
		
		ArrayList<Store> list = new ArrayList<Store>();
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM stores WHERE featured = 1", null); 
		mCursor.moveToFirst();
		
		if (!mCursor.isAfterLast()) {
			do {

                Store entry = formatStore(mCursor);
				
				list.add(entry);
			} while (mCursor.moveToNext());
		}
		mCursor.close();

		return list;
	}
	
	public Photo getPhotoByStoreId(int storeId) {
		
		Photo entry = null;
		db = dbHelper.getReadableDatabase();
		
		String sql = String.format("SELECT * FROM photos WHERE store_id = %d ORDER BY photo_id ASC", storeId);
		Cursor mCursor = db.rawQuery(sql, null); 
		mCursor.moveToFirst();
		
		if (!mCursor.isAfterLast()) {
			do {

                entry = formatPhoto(mCursor);
				
			} while (mCursor.moveToNext());
		}
		mCursor.close();

		return entry;
	}
	
	public ArrayList<Photo> getPhotosByStoreId(int storeId) {
		
		ArrayList<Photo> list = new ArrayList<Photo>();
		db = dbHelper.getReadableDatabase();
		
		String sql = String.format("SELECT * FROM photos WHERE store_id = %d", storeId);
		Cursor mCursor = db.rawQuery(sql, null);  
		mCursor.moveToFirst();
		
		if (!mCursor.isAfterLast()) {
			do {

                Photo entry = formatPhoto(mCursor);
				
				list.add(entry);
			} while (mCursor.moveToNext());
		}
		mCursor.close();

		return list;
	}
	
	
	public ArrayList<Category> getCategories() {
		
		ArrayList<Category> list = new ArrayList<Category>();
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM categories ORDER BY category ASC", null); 
		mCursor.moveToFirst();
		
		if (!mCursor.isAfterLast()) {
			do {
				
				Category entry = formatCategory(mCursor);
				
				list.add(entry);
			} while (mCursor.moveToNext());
		}
		mCursor.close();

		return list;
	}
	
	public ArrayList<Store> getStoresFavorites() {
		
		ArrayList<Store> list = new ArrayList<Store>();
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM stores INNER JOIN favorites ON stores.store_id = favorites.store_id ORDER BY stores.store_name", null); 
		mCursor.moveToFirst();
		
		if (!mCursor.isAfterLast()) {
			do {
				
				Store entry = formatStore(mCursor);
				
				list.add(entry);
			} while (mCursor.moveToNext());
		}
		mCursor.close();

		return list;
	}
	
	
	public ArrayList<String> getCategoryNames() {
		
		ArrayList<String> list = new ArrayList<String>();
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM categories ORDER BY category ASC", null); 
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				list.add(mCursor.getString( mCursor.getColumnIndex("category")));
			} while (mCursor.moveToNext());
		}
		mCursor.close();

		return list;
	}
	
	
	public Category getCategoryByCategory(String cat) {
		
		Category entry = null;
		db = dbHelper.getReadableDatabase();
		
		String sql = String.format("SELECT * FROM categories WHERE category = '%s' ORDER BY category ASC", cat);
		Cursor mCursor = db.rawQuery(sql, null); 
		mCursor.moveToFirst();
		
		if (!mCursor.isAfterLast()) {
			do {

                entry = formatCategory(mCursor);
				
			} while (mCursor.moveToNext());
		}
		mCursor.close();

		return entry;
	}
	
	public Category getCategoryByCategoryId(int categoryId) {
		
		Category entry = null;
		db = dbHelper.getReadableDatabase();
		
		String sql = String.format("SELECT * FROM categories WHERE category_id = %d", categoryId);
		Cursor mCursor = db.rawQuery(sql, null); 
		mCursor.moveToFirst();
		
		if (!mCursor.isAfterLast()) {
			do {

                entry = formatCategory(mCursor);
				
			} while (mCursor.moveToNext());
		}
		mCursor.close();

		return entry;
	}

    public Category formatCategory(Cursor mCursor) {

        Category entry = new Category();
        entry.setCategory( mCursor.getString( mCursor.getColumnIndex("category")) );
        entry.setCategory_icon( mCursor.getString( mCursor.getColumnIndex("category_icon")) );
        entry.setCategory_id( mCursor.getInt( mCursor.getColumnIndex("category_id")) );
        entry.setCreated_at( mCursor.getInt( mCursor.getColumnIndex("created_at")) );
        entry.setIs_deleted( mCursor.getInt( mCursor.getColumnIndex("is_deleted")) );
        entry.setUpdated_at( mCursor.getInt( mCursor.getColumnIndex("updated_at")) );

        return entry;
    }

    public Store formatStore(Cursor mCursor) {

        Store entry = new Store();
        entry.setCategory_id( mCursor.getInt( mCursor.getColumnIndex("category_id")) );
        entry.setCreated_at( mCursor.getInt( mCursor.getColumnIndex("created_at")) );
        entry.setDistance( mCursor.getDouble( mCursor.getColumnIndex("distance")) );
        entry.setEmail( mCursor.getString( mCursor.getColumnIndex("email")) );
        entry.setFeatured( mCursor.getInt( mCursor.getColumnIndex("featured")) );
        entry.setIcon_id( mCursor.getInt( mCursor.getColumnIndex("icon_id")) );
        entry.setIs_deleted( mCursor.getInt( mCursor.getColumnIndex("is_deleted")) );
        entry.setLat( mCursor.getDouble( mCursor.getColumnIndex("lat")) );
        entry.setLon( mCursor.getDouble( mCursor.getColumnIndex("lon")) );

        entry.setPhone_no( mCursor.getString( mCursor.getColumnIndex("phone_no")) );
        entry.setRating_count( mCursor.getInt( mCursor.getColumnIndex("rating_count")) );
        entry.setRating_total( mCursor.getInt( mCursor.getColumnIndex("rating_total")) );
        entry.setSms_no( mCursor.getString( mCursor.getColumnIndex("sms_no")) );

        entry.setStore_address( mCursor.getString( mCursor.getColumnIndex("store_address")) );
        entry.setStore_desc( mCursor.getString( mCursor.getColumnIndex("store_desc")) );
        entry.setStore_id( mCursor.getInt( mCursor.getColumnIndex("store_id")) );

        entry.setStore_name( mCursor.getString( mCursor.getColumnIndex("store_name")) );
        entry.setUpdated_at( mCursor.getInt( mCursor.getColumnIndex("updated_at")) );
        entry.setWebsite( mCursor.getString( mCursor.getColumnIndex("website")) );

        return entry;
    }

    public Photo formatPhoto(Cursor mCursor) {

        Photo entry = new Photo();
        entry.setCreated_at( mCursor.getInt( mCursor.getColumnIndex("created_at")) );
        entry.setIs_deleted( mCursor.getInt( mCursor.getColumnIndex("is_deleted")) );
        entry.setPhoto_id( mCursor.getInt( mCursor.getColumnIndex("photo_id")) );
        entry.setPhoto_url( mCursor.getString( mCursor.getColumnIndex("photo_url")) );
        entry.setStore_id( mCursor.getInt( mCursor.getColumnIndex("store_id")) );
        entry.setThumb_url( mCursor.getString( mCursor.getColumnIndex("thumb_url")) );
        entry.setUpdated_at( mCursor.getInt( mCursor.getColumnIndex("updated_at")) );


        return entry;
    }

    public News formatNews(Cursor mCursor) {
        News news = new News();
        news.setCreated_at( mCursor.getInt( mCursor.getColumnIndex("created_at")) );
        news.setIs_deleted( mCursor.getInt( mCursor.getColumnIndex("is_deleted")) );
        news.setNews_content( mCursor.getString( mCursor.getColumnIndex("news_content")) );
        news.setNews_id( mCursor.getInt( mCursor.getColumnIndex("news_id")) );
        news.setNews_title( mCursor.getString( mCursor.getColumnIndex("news_title")) );
        news.setNews_url( mCursor.getString( mCursor.getColumnIndex("news_url")) );
        news.setPhoto_url( mCursor.getString( mCursor.getColumnIndex("photo_url")) );
        news.setUpdated_at( mCursor.getInt( mCursor.getColumnIndex("updated_at")) );

        return news;
    }

	public void closeDatabase() {
		dbHelper.close();
	}
}
