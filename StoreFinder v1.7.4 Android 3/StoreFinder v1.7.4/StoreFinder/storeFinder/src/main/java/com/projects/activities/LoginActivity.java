package com.projects.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import com.libraries.asynctask.MGAsyncTask;
import com.libraries.asynctask.MGAsyncTask.OnMGAsyncTaskListener;
import com.config.Config;
import com.libraries.dataparser.DataParser;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.models.DataResponse;
import com.models.Status;
import com.models.User;
import com.projects.storefinder.MainActivity;
import com.projects.storefinder.R;
import com.libraries.twitter.TwitterApp;
import com.libraries.twitter.TwitterApp.TwitterAppListener;
import com.libraries.usersession.UserAccessSession;
import com.libraries.usersession.UserSession;
import com.libraries.utilities.MGUtilities;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends FragmentActivity implements OnClickListener {

	private Bundle savedInstanceState;
	private Session.StatusCallback statusCallback;
	private TwitterApp mTwitter;
	private MGAsyncTask task;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		setContentView(R.layout.fragment_login);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
		this.getActionBar().setTitle("");

		this.getActionBar().setIcon(R.drawable.empty_image);
		this.getActionBar().setDisplayShowCustomEnabled(true);
		View viewActionBar = getLayoutInflater().inflate(R.layout.header, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT,
				Gravity.CENTER);

		this.getActionBar().setCustomView(viewActionBar, params);
		
		Button btnLogin = (Button) this.findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		
		Button btnFacebook = (Button) this.findViewById(R.id.btnFacebook);
		btnFacebook.setOnClickListener(this);
		
		Button btnTwitter = (Button) this.findViewById(R.id.btnTwitter);
		btnTwitter.setOnClickListener(this);
		
		this.savedInstanceState = savedInstanceState;
		
		statusCallback = new SessionStatusCallback();
        mTwitter = new TwitterApp(this, twitterAppListener);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.btnLogin:
				login();
				break;
			case R.id.btnFacebook:
				Session session = Session.getActiveSession();
		        if (session == null) { // not logged in
		        	loginToFacebook(savedInstanceState);
		        }
		        else if(!session.isOpened() && session.isClosed()){
		        	loginToFacebook(savedInstanceState);
		        }
		        else {
		        	getUsername(session);
		        }
				break;
			case R.id.btnTwitter:
				loginToTwitter();
				break;
		}
	}

	public void login() {
		if(!MGUtilities.hasConnection(LoginActivity.this)) {
			MGUtilities.showAlertView(
					LoginActivity.this, 
					R.string.network_error, 
					R.string.no_network_connection);
			return;
		}
		
        task = new MGAsyncTask(LoginActivity.this);
        task.setMGAsyncTaskListener(new OnMGAsyncTaskListener() {
			
        	DataResponse response;
        	
			@Override
			public void onAsyncTaskProgressUpdate(MGAsyncTask asyncTask) { }
			
			@Override
			public void onAsyncTaskPreExecute(MGAsyncTask asyncTask) { }
			
			@Override
			public void onAsyncTaskPostExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				updateLogin(response);
			}
			
			@Override
			public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				response = syncData();
			}
		});
        task.execute();
        
	}
	
	public DataResponse syncData() {
		EditText txtUsername = (EditText) findViewById(R.id.txtUsername);
		EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", txtUsername.getText().toString()));
		params.add(new BasicNameValuePair("password", txtPassword.getText().toString() ));
        DataResponse response = DataParser.getJSONFromUrlWithPostRequest(Config.LOGIN_URL, params);
        return response;
	}
	
	public void updateLogin(DataResponse response) {
		Status status = response.getStatus();
		if(response == null) {
			MGUtilities.showAlertView(
					LoginActivity.this,
					R.string.login_error,
					R.string.problems_encountered_login);
			return;
		}

        if(response != null && status != null) {
        	if(status.getStatus_code() == -1 && response.getUser_info() != null ) {
        		User user = response.getUser_info();
        		UserAccessSession session = UserAccessSession.getInstance(LoginActivity.this);
        		UserSession userSession = new UserSession();
        		userSession.setEmail(user.getEmail());
        		userSession.setFacebook_id(user.getFacebook_id());
        		userSession.setFull_name(user.getFull_name());
        		userSession.setLogin_hash(user.getLogin_hash());
        		userSession.setPhoto_url(user.getPhoto_url());
        		userSession.setThumb_url(user.getThumb_url());
        		userSession.setTwitter_id(user.getTwitter_id());
        		userSession.setUser_id(user.getUser_id());
        		userSession.setUsername(user.getUsername());
        		session.storeUserSession(userSession);
        		finish();
        	}
        	else {
        		MGUtilities.showAlertView(LoginActivity.this, R.string.network_error, status.getStatus_text());
        	}
        }
	}
	
	public void syncFacebookUser(final GraphUser user) {
		if(!MGUtilities.hasConnection(LoginActivity.this)) {
			MGUtilities.showAlertView(
					LoginActivity.this, 
					R.string.network_error, 
					R.string.no_network_connection);
			return;
		}
		
        task = new MGAsyncTask(LoginActivity.this);
        task.setMGAsyncTaskListener(new OnMGAsyncTaskListener() {
			
        	DataResponse response;
        	
			@Override
			public void onAsyncTaskProgressUpdate(MGAsyncTask asyncTask) { }
			
			@Override
			public void onAsyncTaskPreExecute(MGAsyncTask asyncTask) { }
			
			@Override
			public void onAsyncTaskPostExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				updateLogin(response);
			}
			
			@Override
			public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				String imageURL = String.format("https://graph.facebook.com/%s/picture?type=large", user.getId());

				Map<String, Object> userObj = user.asMap();
				String email = "";
				if(userObj.get("email") != null)
					email = userObj.get("email").toString();

				params.add(new BasicNameValuePair("facebook_id", user.getId() ));
				params.add(new BasicNameValuePair("full_name", user.getName() ));
				params.add(new BasicNameValuePair("thumb_url", imageURL ));
				params.add(new BasicNameValuePair("email", email != null ? email : "" ));
				Log.e("FB IMAGE URL", imageURL);
				response = DataParser.getJSONFromUrlWithPostRequest(Config.REGISTER_URL, params);
			}
		});
        task.execute();
	}
	
	public void syncTwitterUser(final AccessToken accessToken, final String screenName) {
		if(!MGUtilities.hasConnection(this)) {
			MGUtilities.showAlertView(
					LoginActivity.this, 
					R.string.network_error, 
					R.string.no_network_connection);
			return;
		}
        task = new MGAsyncTask(LoginActivity.this);
        task.setMGAsyncTaskListener(new OnMGAsyncTaskListener() {
			
        	DataResponse response;
        	
			@Override
			public void onAsyncTaskProgressUpdate(MGAsyncTask asyncTask) { }
			
			@Override
			public void onAsyncTaskPreExecute(MGAsyncTask asyncTask) { }
			
			@Override
			public void onAsyncTaskPostExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				updateLogin(response);
			}
			
			@Override
			public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				@SuppressWarnings("static-access")
				Twitter tw = mTwitter.getTwitterInstance();
				twitter4j.User user = null;
				try {
					user = tw.showUser(accessToken.getUserId());
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				if(user != null) {
					String imageURL = user.getOriginalProfileImageURL();
					params.add(new BasicNameValuePair("thumb_url", imageURL ));
					Log.e("TWITTER IMAGE URL", imageURL);
				}
				params.add(new BasicNameValuePair("twitter_id", String.valueOf(accessToken.getUserId()) ));
				params.add(new BasicNameValuePair("full_name", String.valueOf(screenName) ));
				params.add(new BasicNameValuePair("email", "" ));
				response = DataParser.getJSONFromUrlWithPostRequest(Config.REGISTER_URL, params);
			}
		});
        task.execute();
	}

	// FACEBOOK
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
	}
	
	@Override
    public void onStart()  {
        super.onStart();
        if(Session.getActiveSession() != null)
        	Session.getActiveSession().addCallback(statusCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(Session.getActiveSession() != null)
        	Session.getActiveSession().removeCallback(statusCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }
    
    // ###############################################################################################
   	// FACEBOOK INTEGRATION METHODS
   	// ###############################################################################################
    public void loginToFacebook(Bundle savedInstanceState) {
      	Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
      	Session session = Session.getActiveSession();
      	if (session == null) {
      		session = new Session(this);
             Session.setActiveSession(session);
      	}
      	if (!session.isOpened() && !session.isClosed()) {
             session.openForRead(new Session.OpenRequest(this)
                 .setPermissions(Arrays.asList("public_profile", "email"))
                 .setCallback(statusCallback));
        } else {
             Session.openActiveSession(this, true, statusCallback);
             updateView();
        }
    }
      
	private void updateView() {
		Session session = Session.getActiveSession();
			if (session.isOpened()) {
          	getUsername(session);
			} 
      }
      
	private void getUsername(final Session session) {
       	Request request = Request.newMeRequest(session, 
       	        new Request.GraphUserCallback() {
       		
   			@Override
   			public void onCompleted(GraphUser user, Response response) {
   				// If the response is successful
       	        if (session == Session.getActiveSession()) {
       	            if (user != null) {
       	                // Set the id for the ProfilePictureView
       	                // view that in turn displays the profile picture.
       	            	Log.e("FACEBOOK USERNAME**", user.getName());
       	            	Log.e("FACEBOOK ID**", user.getId());
       	            	Log.e("FACEBOOK EMAIL**", ""+user.asMap().get("email"));
       	            	syncFacebookUser(user);
       	            }
       	        }
       	        if (response.getError() != null) {
       	            // Handle errors, will do so later.
       	        	Log.e("ERROR", response.getError().getErrorMessage());
       	        }
   			}

       	});
       	request.executeAsync();
	}
	
	private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            updateView();
        }
    }

	// ###############################################################################################
   	// TWITTER INTEGRATION METHODS
   	// ###############################################################################################
   	public void loginToTwitter() {
   		if (mTwitter.hasAccessToken() == true) {
   			try {
   				syncTwitterUser(mTwitter.getAccessToken(), mTwitter.getScreenName());
   			} 
   			catch (Exception e) {
   				e.printStackTrace();
   			}
   		} 
   		else {
   			mTwitter.loginToTwitter();
   		}
   	}
   	
   	TwitterAppListener twitterAppListener = new TwitterAppListener() {
 		
 		@Override
 		public void onError(String value)  {
 			// TODO Auto-generated method stub
 			Log.e("TWITTER ERROR**", value);
 		}
 		
 		@Override
 		public void onComplete(AccessToken accessToken) {
 			// TODO Auto-generated method stub
 			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					syncTwitterUser(mTwitter.getAccessToken(), mTwitter.getScreenName());
				}
			});
 		}
 	};
	
 	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        // Handle action bar actions click
        switch (item.getItemId()) {
	        default:
	        	finish();	
	            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        // if nav drawer is opened, hide the action items
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public void onDestroy()  {
        super.onDestroy();
        if(task != null)
        	task.cancel(true);
	}
}
