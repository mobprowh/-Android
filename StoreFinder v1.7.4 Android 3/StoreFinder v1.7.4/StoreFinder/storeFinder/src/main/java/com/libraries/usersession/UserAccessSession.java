package com.libraries.usersession;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.config.Config;

public class UserAccessSession {

	private SharedPreferences sharedPref;
	private Editor editor;

	private static final String FACEBOOK_ID 	= "sekln0LDNKANWdskf";
	private static final String TWITTER_ID 		= "OIanlknfalk3lnk2a";
	private static final String USER_ID 		= "23vponrnkl32brlkn";
	private static final String LOGIN_HASH 		= "340bji4riwbnlrvas";
	private static final String FULL_NAME 		= "5b03i3ipbp3454LLK";
	private static final String USER_NAME 		= "65po7jboyioen2Kid";
	private static final String EMAIL 			= "54690j945safnKNKI";
	private static final String THUMB_URL 		= "sadnka008adklasdk";
	private static final String PHOTO_URL 		= "8dsfu99s121kn3jkk";
	private static final String IS_LOGIN 		= "3b90jKADN3902q3v2";
	private static final String FILTER_RADIUS_DISTANCE 		= "FILTER_RADIUS_DISTANCE";
	private static final String FILTER_RADIUS_DISTANCE_MAX 		= "FILTER_RADIUS_DISTANCE_MAX";


	private static final String SHARED = "UserAccessSession_Preferences";
	private static UserAccessSession instance;
	
	public static UserAccessSession getInstance(Context context) {
		if(instance == null)
			instance = new UserAccessSession(context);
		return instance;
	}

	public UserAccessSession(Context context) {
		sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
		editor = sharedPref.edit();
	}

	public void storeUserSession(UserSession session) {
		editor.putString(FACEBOOK_ID, session.getFacebook_id());
		editor.putString(TWITTER_ID, session.getTwitter_id());
		editor.putInt(USER_ID, session.getUser_id());
		editor.putString(LOGIN_HASH, session.getLogin_hash());
		editor.putString(FULL_NAME, session.getFull_name());
		editor.putString(USER_NAME, session.getUsername());
		editor.putString(THUMB_URL, session.getThumb_url());
		editor.putString(PHOTO_URL, session.getPhoto_url());
		editor.putString(EMAIL, session.getEmail());
		editor.putBoolean(IS_LOGIN, true);
		editor.commit();
	}

	public void clearUserSession() {
		editor.putString(FACEBOOK_ID, null);
		editor.putString(TWITTER_ID, null);
		editor.putInt(USER_ID, -1);
		editor.putString(LOGIN_HASH, null);
		editor.putString(FULL_NAME, null);
		editor.putString(USER_NAME, null);
		editor.putString(THUMB_URL, null);
		editor.putString(PHOTO_URL, null);
		editor.putString(EMAIL, null);
		editor.putBoolean(IS_LOGIN, false);
		editor.commit();
	}

	public Boolean isLoggedIn() {
		if(sharedPref == null)
			return false;
		
		return sharedPref.getBoolean(IS_LOGIN, false);
	}

	public UserSession getUserSession() {
		int userId = sharedPref.getInt(USER_ID, -1);
		if(userId <= 0)
			return null;
		
		UserSession session = new UserSession();
		session.setEmail( sharedPref.getString(EMAIL, null) );
		session.setFacebook_id( sharedPref.getString(FACEBOOK_ID, null) );
		session.setFull_name( sharedPref.getString(FULL_NAME, null) );
		session.setLogin_hash( sharedPref.getString(LOGIN_HASH, null) );
		session.setPhoto_url( sharedPref.getString(PHOTO_URL, null) );
		session.setThumb_url( sharedPref.getString(THUMB_URL, null) );
		session.setTwitter_id( sharedPref.getString(TWITTER_ID, null) );
		session.setUser_id( sharedPref.getInt(USER_ID, -1) );
		session.setUsername( sharedPref.getString(USER_NAME, null) );
		return session;
	}
	
	public Boolean isLoggedInFromSocial() {
		if(sharedPref == null)
			return false;
		
		String facebookId = sharedPref.getString(FACEBOOK_ID, null);
		String twitterId = sharedPref.getString(TWITTER_ID, null);
		boolean isSocial = ( (facebookId != null && facebookId.length() != 0) || 
								(twitterId != null && twitterId.length() != 0) ) ? true : false;
		
		return isSocial;
	}

	public void setFilterDistance(float radius) {
		editor.putFloat(FILTER_RADIUS_DISTANCE, radius);
		editor.commit();
	}

	public float getFilterDistance() {
		return  sharedPref.getFloat(FILTER_RADIUS_DISTANCE, 0);
	}

	public void setFilterDistanceMax(float radius) {
		editor.putFloat(FILTER_RADIUS_DISTANCE_MAX, radius);
		editor.commit();
	}

	public float getFilterDistanceMax() {
		return  sharedPref.getFloat(FILTER_RADIUS_DISTANCE_MAX, Config.MAX_RADIUS_STORE_VALUE_IN_KM);
	}
}
