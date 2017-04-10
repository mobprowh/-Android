package com.libraries.twitter;

import twitter4j.auth.RequestToken;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;

public class TwitterSession 
{
	private SharedPreferences sharedPref;
	private Editor editor;

	private static final String TWEET_AUTH_KEY 			= "24khd1qkhaKNaLaA";
	private static final String TWEET_AUTH_SECRET_KEY 	= "34590uhefi3lknAA";
	private static final String TWEET_USER_NAME 		= "4359090jsdnladnB";
	private static final String SHARED 					= "Twitter_Preferences";

	public TwitterSession(Context context) 
	{
		sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);

		editor = sharedPref.edit();
	}

	public void storeRequestToken(RequestToken accessToken, String username) 
	{
		editor.putString(TWEET_AUTH_KEY, accessToken.getToken());
		editor.putString(TWEET_AUTH_SECRET_KEY, accessToken.getTokenSecret());
		editor.putString(TWEET_USER_NAME, username);

		editor.commit();
	}

	public void resetRequestToken() 
	{
		editor.putString(TWEET_AUTH_KEY, null);
		editor.putString(TWEET_AUTH_SECRET_KEY, null);
		editor.putString(TWEET_USER_NAME, null);

		editor.commit();
	}

	public String getUsername() 
	{
		return sharedPref.getString(TWEET_USER_NAME, "");
	}

	public RequestToken getRequestToken() 
	{
		String token = sharedPref.getString(TWEET_AUTH_KEY, null);
		String tokenSecret = sharedPref.getString(TWEET_AUTH_SECRET_KEY, null);

		if (token != null && tokenSecret != null)
			return new RequestToken(token, tokenSecret);
		else
			return null;
	}
	
	public Boolean isLoggedIn()
	{
		String token = sharedPref.getString(TWEET_AUTH_KEY, "");
		
		if(token.length() == 0)
			return false;
		
		return true;
	}
}
