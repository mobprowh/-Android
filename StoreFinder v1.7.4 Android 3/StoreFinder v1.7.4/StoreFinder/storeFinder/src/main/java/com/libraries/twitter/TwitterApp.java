package com.libraries.twitter;


import java.io.InputStream;
import com.libraries.asynctask.MGAsyncTask;
import com.libraries.asynctask.MGAsyncTask.OnMGAsyncTaskListener;
import com.config.Config;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class TwitterApp {
	
	// Preference Constants
	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_OAUTH_SECRET2 = "oauth_token_secret2";
	static final String PREF_USER_ID = "twitter_user_id";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

	static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

	// Twitter oauth urls
	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_SCREEN_NAME = "oauth_token_secret";
	
	// Twitter
	private static Twitter twitter;
	private static RequestToken requestToken;
	
	// Shared Preferences
	private static SharedPreferences mSharedPreferences;

	private ProgressDialog mProgressDlg;
	private TwitterAppListener twitterAppListener;
	
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();
	
	Activity activity;
	InputStream inputStream = null;
	
	public Twitter getTwitterInstance() {
		return  twitter;
	}
	
	OnSocialAuthTwitterListener mCallback;
	
	public interface OnSocialAuthTwitterListener {
        public void socialAuthenticationTwitterCompleted(TwitterApp twitterApp, AccessToken accessToken);
    }
	
	public void setOnSocialAuthTwitterListener(OnSocialAuthTwitterListener listener) {
		try {
            mCallback = (OnSocialAuthTwitterListener) listener;
        } catch (ClassCastException e)  {
            throw new ClassCastException(this.toString() + " must implement OnSocialAuthTwitterListener");
        }
	}
	
	public TwitterApp(Activity activity, TwitterAppListener twitterAppListener) {
		this.activity = activity;
		this.twitterAppListener = twitterAppListener;

		// Check if twitter keys are set
		if(Config.TWITTER_CONSUMER_KEY.trim().length() == 0 || Config.TWITTER_CONSUMER_SECRET.trim().length() == 0) {
			
			// Internet Connection is not present
			alert.showAlertDialog(
					activity, 
					"Twitter oAuth tokens", 
					"Please set your twitter oauth tokens first!", false);
			
			// stop executing code by return
			return;
		}
		
		mProgressDlg = new ProgressDialog(activity);
		
		// Shared Preferences
		mSharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, 0);
		
		/** This if conditions is tested once is
		 * redirected from twitter page. Parse the uri to get oAuth
		 * Verifier
		 * */
		if (!isLoggedInTwitter()) {
			Uri uri = activity.getIntent().getData();
			
			if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
				// oAuth verifier
				String verifier = uri
						.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

				try {
					// Get the access token
					AccessToken accessToken = twitter.getOAuthAccessToken(
							requestToken, verifier);

					// Shared Preferences
					Editor e = mSharedPreferences.edit();

					// After getting access token, access token secret
					// store them in application preferences
					e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
					e.putString(PREF_KEY_OAUTH_SECRET,
							accessToken.getTokenSecret());
					// Store login status - true
					e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
					e.commit(); // save changes

					Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

				} 
				catch (Exception e) {
					// Check log for login errors
					Log.e("Twitter Login Error", "> " + e.getMessage());
				}
			}
		}
		else {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(Config.TWITTER_CONSUMER_KEY);
			builder.setOAuthConsumerSecret(Config.TWITTER_CONSUMER_SECRET);
			Configuration configuration = builder.build();
			
//			System.setProperty("twitter4j.http.useSSL","false");
			TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();
		}
	}
	
	
	public void loginToTwitter() {
		
		// Check if already logged in
		if (!isLoggedInTwitter()) {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(Config.TWITTER_CONSUMER_KEY);
			builder.setOAuthConsumerSecret(Config.TWITTER_CONSUMER_SECRET);
			Configuration configuration = builder.build();
			
//			System.setProperty("twitter4j.http.useSSL","false");
			TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();

			getAuth();
		} else {
			
			// user already logged into twitter
			Toast.makeText(activity,
					"Already Logged into twitter", Toast.LENGTH_LONG).show();
		}
	}
	
	
	public void getAuth() {
		
		MGAsyncTask asyncTask = new MGAsyncTask(activity);
		asyncTask.setMGAsyncTaskListener(new OnMGAsyncTaskListener() {
			
			@Override
			public void onAsyncTaskProgressUpdate(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAsyncTaskPreExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAsyncTaskPostExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				try {
					Log.e("authenticaton url", requestToken.getAuthenticationURL());
					showLoginDialog(requestToken.getAuthenticationURL());
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				try {
					requestToken = twitter
							.getOAuthRequestToken(TWITTER_CALLBACK_URL);
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		asyncTask.startAsyncTask();
	}
	
	
	public boolean isLoggedInTwitter() {
		
		// return twitter login status from Shared Preferences
		if(mSharedPreferences == null)
			return false;
		
		return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
	}
	
	public Boolean hasAccessToken() {
		String token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, null);
		
		return token == null ? false : true;
	}
	
	public String getScreenName() {
		
		String name = mSharedPreferences.getString(PREF_KEY_SCREEN_NAME, "");
		return name;
	}
	
	public void resetAccessToken() {
		// Clear the shared preferences
		
		mSharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, 0);
		Editor e = mSharedPreferences.edit();
		e.remove(PREF_KEY_OAUTH_TOKEN);
		e.remove(PREF_KEY_OAUTH_SECRET);
		e.remove(PREF_KEY_TWITTER_LOGIN);
		e.commit();
	}
	
	private void showLoginDialog(String url) {
		
		final TwDialogListener listener = new TwDialogListener() {

			public void onComplete(String value) {
				processToken(value);
			}

			public void onError(String value) {
//				mListener.onError("Failed opening authorization page");
				Log.e("ERROR", value);
			}
		};

		new TwitterDialog(activity, url, listener).show();
	}
	
	public interface TwDialogListener {
		public void onComplete(String value);

		public void onError(String value);
	}
	
	
	public void processToken(String callbackUrl) {
		mProgressDlg.setMessage("Finalizing ...");
		mProgressDlg.show();

		Uri uri = Uri.parse(callbackUrl);
	    final String verifier = uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

		new Thread() {
			@Override
			public void run() {
				
				try {
					final AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

			        // Shared Preferences
			        Editor e = mSharedPreferences.edit();

			        // After getting access token, access token secret
			        // store them in application preferences
			        e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
			        e.putString(PREF_KEY_OAUTH_SECRET,accessToken.getTokenSecret());
			        e.putString(PREF_KEY_OAUTH_SECRET2,accessToken.getTokenSecret());
			        e.putString(PREF_KEY_SCREEN_NAME,accessToken.getScreenName());
			        e.putLong(PREF_USER_ID,accessToken.getUserId());
			        // Store login status - true
			        e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
			        // save changes	
			        e.commit(); 

			        twitterAppListener.onComplete(accessToken);
			        			        
			        activity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							mProgressDlg.hide();
							
							if(mCallback != null)
								mCallback.socialAuthenticationTwitterCompleted(TwitterApp.this, accessToken);
						}
					});
			        
			        
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}.start();
	}
	
	public void updateStatusWithLogo(InputStream f, String status) {
		
		inputStream = f;
		new updateTwitterStatus1().execute(status);
	}
	
	public void updateStatus(String status) {
//		try {
//			twitter.updateStatus(status);
//		} catch (TwitterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		new updateTwitterStatus().execute(status);
		
//        Twitter twitter = new TwitterFactory().getInstance();
//        twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
//        
//        try {
//			RequestToken requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
//			String authUrl = requestToken.getAuthenticationURL();
//			Uri uri = Uri.parse(authUrl);
//			String verifier = uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
//			AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
//			
//			
//	        twitter.setOAuthAccessToken(accessToken);
//	        
//	        twitter.updateStatus(status);
//			
//		} catch (TwitterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	
	public AccessToken getAccessToken() {
		
		String token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, null); 
        String secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET2, null);
        AccessToken accessToken = new AccessToken(token,secret);
        
		return accessToken;
	}
	
	public long getUserId() {
		long userId = mSharedPreferences.getLong(PREF_USER_ID, -1);
        
		return userId;
	}
	
	public interface TwitterAppListener {
		
		public void onComplete(AccessToken accessToken);

		public void onError(String value);
	}
	
	
	class updateTwitterStatus extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(activity);
			pDialog.setMessage("Updating to twitter...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		protected String doInBackground(String... args) {
			
			Log.d("Tweet Text", "> " + args[0]);
			String status = args[0];
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(Config.TWITTER_CONSUMER_KEY);
				builder.setOAuthConsumerSecret(Config.TWITTER_CONSUMER_SECRET);
				
				// Access Token 
//				String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
				// Access Token Secret
//				String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");

				AccessToken accessToken = getAccessToken();
//				AccessToken accessToken = new AccessToken(access_token, access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

				// Update status
				StatusUpdate statusUpdate = new StatusUpdate(status);
				twitter4j.Status response = twitter.updateStatus(statusUpdate);
				
				String statusText = response.getText();
				Log.d("Status", "> " + statusText);
				
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
//						Toast.makeText(activity, 
//								activity.getString(R.string.successfullyTweetPropertyToTwitter),
//		                     Toast.LENGTH_LONG).show();
					}
				});
				
			} catch (TwitterException e) {
				// Error in updating status
				Log.d("Twitter Update Error", e.getMessage());
				
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
//						Toast.makeText(activity, 
//								activity.getString(R.string.failedTweet),
//		                     Toast.LENGTH_LONG).show();
					}
				});
			}
			return null;
		}
		
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			
		}

	}
	
	
	
	class updateTwitterStatus1 extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(activity);
			pDialog.setMessage("Updating to twitter...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		protected String doInBackground(String... args) {
			Log.d("Tweet Text", "> " + args[0]);
			String status = args[0];
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(Config.TWITTER_CONSUMER_KEY);
				builder.setOAuthConsumerSecret(Config.TWITTER_CONSUMER_SECRET);
		
				AccessToken accessToken = getAccessToken();
//				AccessToken accessToken = new AccessToken(access_token, access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

				// Update status
				StatusUpdate statusUpdate = new StatusUpdate(status);
				statusUpdate.media("Milugar", inputStream);
				twitter4j.Status response = twitter.updateStatus(statusUpdate);
				
				String statusText = response.getText();
				Log.d("Status", "> " + statusText);
				
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
//						Toast.makeText(activity, 
//								activity.getString(R.string.successfullyTweetPropertyToTwitter),
//		                     Toast.LENGTH_LONG).show();
					}
				});
				
			} catch (TwitterException e) {
				// Error in updating status
				Log.d("Twitter Update Error", e.getMessage());
				
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
//						Toast.makeText(activity, 
//								activity.getString(R.string.failedTweet),
//		                     Toast.LENGTH_LONG).show();
					}
				});
			}
			return null;
		}
		
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			
		}

	}
}
