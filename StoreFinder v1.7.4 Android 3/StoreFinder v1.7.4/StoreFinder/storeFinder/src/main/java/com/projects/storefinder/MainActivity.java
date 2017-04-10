package com.projects.storefinder;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import com.libraries.adapters.MGListAdapter;
import com.libraries.adapters.MGListAdapter.OnMGListAdapterAdapterListener;
import com.config.Config;
import com.config.UIConfig;
import com.db.DbHelper;
import com.db.Queries;
import com.libraries.utilities.MGUtilities;
import com.projects.fragments.AboutUsFragment;
import com.projects.fragments.SettingsFragment;
import com.projects.fragments.CategoryFragment;
import com.projects.fragments.FavoriteFragment;
import com.projects.fragments.FeaturedFragment;
import com.projects.fragments.HomeFragment;
import com.projects.fragments.MapFragment;
import com.projects.fragments.NewsFragment;
import com.projects.fragments.SearchFragment;
import com.projects.fragments.SplashFragment;
import com.projects.fragments.TermsConditionFragment;
import com.projects.fragments.WeatherFragment;
import com.projects.activities.LoginActivity;
import com.projects.activities.ProfileActivity;
import com.projects.activities.RegisterActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.libraries.location.LocationUtils;
import com.models.Menu;
import com.models.Menu.HeaderType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.libraries.refreshlayout.SwipeRefreshActivity;
import com.libraries.usersession.UserAccessSession;
import com.libraries.usersession.UserSession;

import android.app.ActionBar;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SwipeRefreshActivity implements
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {

	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private Menu[] MENUS;
    public static Location location;
	public static List<Address> address;
	public static int offsetY = 0;
	private static SQLiteDatabase db;
	private static DbHelper dbHelper;
	private static Queries q;
	protected static ImageLoader imageLoader;
	private static boolean isShownSplash = false;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    boolean mUpdatesRequested = false;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;
    private Fragment currFragment;
    private GetAddressTask getAddressTask;
	boolean doubleBackToExitPressedOnce = false;
    private OnLocationListener mCallbackLocation;
    private OnActivityResultListener mCallbackActivityResult;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        this.getActionBar().setTitle("");
        this.getActionBar().setIcon(R.drawable.empty_image);
        this.getActionBar().setDisplayShowCustomEnabled(true);
        View viewActionBar = getLayoutInflater().inflate(R.layout.header, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        this.getActionBar().setCustomView(viewActionBar, params);
        
        dbHelper = new DbHelper(this);
		q = new Queries(db, dbHelper);
		
		imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));

        mTitle = mDrawerTitle = "";
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        updateMenuList();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_menu, //nav menu toggle icon
                R.string.no_name, // nav drawer open - description for accessibility
                R.string.no_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView) {
//                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
                updateMenuList();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disables the animation
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            offsetY = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        if(!isShownSplash) {
        	isShownSplash = true;
        	this.getActionBar().hide();
        	FragmentManager fragmentManager = this.getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, new SplashFragment()).commit();
        }
        else if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(1);
        }

        mUpdatesRequested = false;
        // Open Shared Preferences

        mPrefs = getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        // Get an editor
        mEditor = mPrefs.edit();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addApi(LocationServices.API)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .build();
        
        FrameLayout frameAds = (FrameLayout) findViewById(R.id.frameAds);
		frameAds.setVisibility(View.GONE);

        UserAccessSession accessSession = UserAccessSession.getInstance(this);
        if(accessSession.getFilterDistanceMax() == 0) {
            accessSession.setFilterDistanceMax(Config.MAX_RADIUS_STORE_VALUE_IN_KM);
        }

        if(MGUtilities.hasConnection(this)) {
            q.deleteTable("news");
            q.deleteTable("stores");
            q.deleteTable("photos");
        }

        hideSwipeProgress();
    }
    
    public void showMainView() {
    	getActionBar().show();
    	displayView(1);
    	showAds();
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
        	updateMenuList();
            return true;
        }
        switch (item.getItemId()) {
	        default:
	            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        // if nav drawer is opened, hide the action items
        return super.onPrepareOptionsMenu(menu);
    }
    
    private void displayView(int position) {
    	// clear back stack
    	FragmentManager fm = this.getSupportFragmentManager();
	    for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {    
	        fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); 
	    }

	    // update the main content by replacing fragments
        Intent i = null;
        Fragment fragment = null;
        switch (position) {
	        case 1:
	            fragment = new HomeFragment();
	            break;
	        case 2:
	            fragment = new CategoryFragment();
	            break;
	        case 3:
	            fragment = new FavoriteFragment();
	            break;
	        case 4:
	            fragment = new FeaturedFragment();
	            break;
	        case 5:
	            fragment = new MapFragment();
	            break;
	        case 6:
	            fragment = new SearchFragment();
	            break;
	        case 7:
	            fragment = new NewsFragment();
	            break;
	        case 8:
	            fragment = new WeatherFragment();
	            break;
	        case 10:
	            fragment = new AboutUsFragment();
	            break;
	        case 11:
	            fragment = new TermsConditionFragment();
	            break;
            case 12:
                fragment = new SettingsFragment();
                break;
	        case 14:
				UserAccessSession session = UserAccessSession.getInstance(this);
				if(session.getUserSession() == null) {
					i = new Intent(MainActivity.this, RegisterActivity.class);
					startActivity(i);
				}
				else { 
					i = new Intent(this, ProfileActivity.class);
					startActivity(i);
				}
				break;
			case 15:
				i = new Intent(this, LoginActivity.class);
				this.startActivity(i);
				break;
	        default:
	            break;
        }
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        mDrawerLayout.closeDrawer(mDrawerList);
        if(currFragment != null && fragment != null) {
    	   boolean result = fragment.getClass().equals( currFragment.getClass());
           if(result)
        	   return;
        }
        
        if (fragment != null) {
        	if(fragment instanceof MapFragment) {
        		currFragment = fragment;
        		Handler h = new Handler();
        		h.postDelayed(new Runnable() {
        			
        			@Override
        			public void run() {
        				// TODO Auto-generated method stub
                        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, currFragment).commit();
        			}
        		}, Config.DELAY_SHOW_ANIMATION + 200);
        	}
        	else {
        		currFragment = fragment;
                FragmentManager fragmentManager = this.getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment).commit();
        	}
        }
    }
 
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
 
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
    public void updateMenuList() {
		UserAccessSession accessSession = UserAccessSession.getInstance(this);
		UserSession userSession = accessSession.getUserSession();
		if(userSession == null) {
			MENUS = UIConfig.MENUS_NOT_LOGGED;
		}
		else {
			MENUS = UIConfig.MENUS_LOGGED;
		}
		showList();
	}
    
    public void showList() {
    	MGListAdapter adapter = new MGListAdapter(this, MENUS.length, R.layout.menu_entry);
		adapter.setOnMGListAdapterAdapterListener(new OnMGListAdapterAdapterListener() {
			
			@Override
			public void OnMGListAdapterAdapterCreated(MGListAdapter adapter, View v,
					int position, ViewGroup viewGroup) {
				// TODO Auto-generated method stub
				FrameLayout frameCategory = (FrameLayout) v.findViewById(R.id.frameCategory);
				FrameLayout frameHeader = (FrameLayout) v.findViewById(R.id.frameHeader);
				frameCategory.setVisibility(View.GONE);
				frameHeader.setVisibility(View.GONE);
				Menu menu = MENUS[position];
				if(menu.getHeaderType() == HeaderType.HeaderType_CATEGORY) {
					frameCategory.setVisibility(View.VISIBLE);
					Spanned title = Html.fromHtml(MainActivity.this.getResources().getString(menu.getMenuResTitle()));
					TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
					tvTitle.setText(title);
					ImageView imgViewIcon = (ImageView) v.findViewById(R.id.imgViewIcon);
					imgViewIcon.setImageResource(menu.getMenuResIconSelected());
				}
				else {
					frameHeader.setVisibility(View.VISIBLE);
					Spanned title = Html.fromHtml(MainActivity.this.getResources().getString(menu.getMenuResTitle()));
					TextView tvTitleHeader = (TextView) v.findViewById(R.id.tvTitleHeader);
					tvTitleHeader.setText(title);
				}
			}
		});
		mDrawerList.setAdapter(adapter);
		adapter.notifyDataSetChanged();
    }

    // ====================================================================================
    // ====================================================================================
    // ====================================================================================
	public interface OnLocationListener {
        public void onLocationChanged(Location prevLoc, Location currentLoc);
    }
	
	public void setOnLocationListener(OnLocationListener listener) {
		try {
			mCallbackLocation = (OnLocationListener) listener;
        } catch (ClassCastException e)  {
            throw new ClassCastException(this.toString() + " must implement OnLocationListener");
        }
	}

	public Queries getQueries() {	
		return q;
	}
	public static ImageLoader getImageLoader() {
		return imageLoader;
	}

	@Override
    public void onStart()  {
        super.onStart();
        if(!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mCallbackActivityResult != null) {
        	mCallbackActivityResult.onActivityResultCallback(this, requestCode, resultCode, data);
        }
    }

    public void getDebugKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getApplicationContext().getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
               Log.e("KeyHash:", "------------------------------------------");
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.e("KeyHash:", "------------------------------------------");
                }

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public interface OnActivityResultListener {
        public void onActivityResultCallback(Activity activity, int requestCode, int resultCode, Intent data);
     }

    public void setOnActivityResultListener(OnActivityResultListener listener) {
        try {
            mCallbackActivityResult = (OnActivityResultListener) listener;
        } catch (ClassCastException e)  {
             throw new ClassCastException(this.toString() + " must implement OnActivityResultListener");
        }
    }

    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d(LocationUtils.APPTAG, "Google Play Service available.");
            // Continue
            return true;
        // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            dialog.show();
            return false;
        }
    }

    @SuppressLint("NewApi")
    public void getAddress(View v) {
        // In Gingerbread and later, use Geocoder.isPresent() to see if a geocoder is available.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && !Geocoder.isPresent()) {
            // No geocoder is present. Issue an error message
            Toast.makeText(this, "No Geocoder available", Toast.LENGTH_LONG).show();
            return;
        }
        if (servicesConnected()) { }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(
                        this,
                        LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {

                // Log the error
                e.printStackTrace();
            }
        }
        else { }
    }

    @Override
    public void onLocationChanged(Location loc) {
        Log.e("Location LOG", "Location Updated");
        if(mCallbackLocation != null)
            mCallbackLocation.onLocationChanged(location, loc);

        location = loc;
        if(Config.DEBUG_LOCATION) {
            location.setLatitude(Config.DEBUG_LATITUDE);
            location.setLongitude(Config.DEBUG_LONGITUDE);
        }
        if(address == null) {
            getAddressTask = new MainActivity.GetAddressTask(this);
            getAddressTask.execute(location);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPrefs.contains(LocationUtils.KEY_UPDATES_REQUESTED)) {
            mUpdatesRequested = mPrefs.getBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);
        }
        else {
            mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);
            mEditor.commit();
        }
    }

    @Override
    public void onPause() {
        mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, mUpdatesRequested);
        mEditor.commit();
        super.onPause();
    }

    public void showAds() {
        FrameLayout frameAds = (FrameLayout) findViewById(R.id.frameAds);
        if(Config.WILL_SHOW_ADS) {
            frameAds.setVisibility(View.VISIBLE);
            if(adView == null) {
                adView = new AdView(this);
                adView.setAdSize(AdSize.SMART_BANNER);
                adView.setAdUnitId(Config.BANNER_UNIT_ID);
                frameAds.addView(adView);

                Builder builder = new AdRequest.Builder();
                if(Config.TEST_ADS_USING_EMULATOR)
                    builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);

                if(Config.TEST_ADS_USING_TESTING_DEVICE)
                    builder.addTestDevice(Config.TESTING_DEVICE_HASH);

                AdRequest adRequest = builder.build();
                // Start loading the ad in the background.
                adView.loadAd(adRequest);
            }
        }
        else {
            frameAds.setVisibility(View.GONE);
        }
    }

    protected class GetAddressTask extends AsyncTask<Location, Void, String> {
        // Store the context passed to the AsyncTask when the system instantiates it.
        Context localContext;
        // Constructor called by the system to instantiate the task
        public GetAddressTask(Context context) {
            // Required by the semantics of AsyncTask
            super();
            // Set a Context for the background task
            localContext = context;
        }

        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());
            // Get the current location from the input parameter list
            Location location = params[0];
            // Create a list to contain the result address
            List <Address> addresses = null;
            // Try to get an address for the current location. Catch IO or network problems.
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1
                );
                // Catch network or other I/O problems.
                } catch (IOException exception1) {
                    // print the stack trace
                    exception1.printStackTrace();
                // Catch incorrect latitude or longitude values
                } catch (IllegalArgumentException exception2) {
                    exception2.printStackTrace();
                }
                // If the reverse geocode returned an address
                if (addresses != null && addresses.size() > 0) {
                    // Get the first address
                    address = addresses;
                    Address address = MainActivity.address.get(0);
                    String locality = address.getLocality();
                    String countryName = address.getCountryName();
                    String addressStr = String.format("%s, %s", locality, countryName);
                    Log.e("Location LOG", addressStr);
                // If there aren't any addresses, post a message
                }
                return null;
        }

        @Override
        protected void onPostExecute(String address) { }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // TODO Auto-generated method stub
        Log.i("GoogleApiClient", "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // TODO Auto-generated method stub
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.tap_back_again_to_exit, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
