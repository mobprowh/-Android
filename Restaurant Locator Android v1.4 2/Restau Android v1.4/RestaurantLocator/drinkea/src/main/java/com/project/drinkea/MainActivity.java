package com.project.drinkea;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.config.Config;
import com.config.UIConfig;
import com.db.DbHelper;
import com.db.Queries;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.libraries.adapters.MGListAdapter;
import com.libraries.adapters.MGListAdapter.OnMGListAdapterAdapterListener;
import com.libraries.location.LocationUtils;
import com.libraries.refreshlayout.SwipeRefreshActivity;
import com.libraries.utilities.MGUtilities;
import com.models.Menu;
import com.models.Menu.HeaderType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.project.fragments.CategoryFragment;
import com.project.fragments.FavoritesFragment;
import com.project.fragments.FeaturedFragment;
import com.project.fragments.GalleryFragment;
import com.project.fragments.HomeFragment;
import com.project.fragments.MapFragment;
import com.project.fragments.SearchFragment;
import com.project.fragments.SplashFragment;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class MainActivity extends SwipeRefreshActivity
        implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    // nav drawer title
    private CharSequence mDrawerTitle;
    // used to store app title
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
    // Handle to SharedPreferences for this app
    SharedPreferences mPrefs;
    // Handle to a SharedPreferences editor
    SharedPreferences.Editor mEditor;
    private Fragment currFragment;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        dbHelper = new DbHelper(this);
		q = new Queries(db, dbHelper);
		imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mTitle = mDrawerTitle = "";
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        mDrawerList.setBackgroundResource(R.drawable.bg_side);
        mDrawerList.setDivider(null);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        showList();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_menu, //nav menu toggle icon
                R.string.no_name, // nav drawer open - description for accessibility
                R.string.no_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            offsetY = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        isShownSplash = true;
        this.getActionBar().hide();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, new SplashFragment()).commit();

        mUpdatesRequested = false;
        mPrefs = getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addApi(LocationServices.API)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .build();

        FrameLayout frameAds = (FrameLayout) findViewById(R.id.frameAds);
		frameAds.setVisibility(View.GONE);
//        resetDataStorageWhenNetworkIsAvailable();
    }

    public void resetDataStorage() {
        q.deleteTable("restaurants");
        q.deleteTable("categories");
        q.deleteTable("photos");
    }

    public void resetDataStorageWhenNetworkIsAvailable() {
        if(MGUtilities.hasConnection(this)) {
            resetDataStorage();
        }
    }

    public void showMainView() {
    	getActionBar().show();
        this.getActionBar().setIcon(R.drawable.header_logo);
        this.getActionBar().setTitle("");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
    	displayView(1);
    	showAds();
    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            displayView(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
        	updateMenuList();
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menuFave:
                displayView(3);
	        default:
	            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        if(currFragment != null && currFragment instanceof FavoritesFragment)
            getMenuInflater().inflate(R.menu.menu_default, menu);
        else
            getMenuInflater().inflate(R.menu.menu_fave, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    private void displayView(int position) {
        invalidateOptionsMenu();

    	FragmentManager fm = this.getSupportFragmentManager();
	    for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
	        fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	    }
        Fragment fragment = null;
        Intent i;
        switch (position) {
            case 1:
                fragment = new HomeFragment();
                break;
            case 2:
                fragment = new CategoryFragment();
                break;
            case 3:
                fragment = new FavoritesFragment();
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
                fragment = new GalleryFragment();
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
                        FragmentManager fragmentManager = getSupportFragmentManager();
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

	}

    public void showList() {

        MENUS = UIConfig.MENUS_NOT_LOGGED;
    	MGListAdapter adapter = new MGListAdapter(
				this, MENUS.length, R.layout.menu_entry);
		adapter.setOnMGListAdapterAdapterListener(new OnMGListAdapterAdapterListener() {

			@Override
			public void OnMGListAdapterAdapterCreated(MGListAdapter adapter, View v,
					int position, ViewGroup viewGroup) {
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

	public Queries getQueries() {
		return q;
	}

	public static ImageLoader getImageLoader() {
		return imageLoader;
	}

	private AdView adView;

	@Override
    public void onStart()  {
        super.onStart();
        if(mGoogleApiClient != null && !mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

 	private boolean servicesConnected() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {
            Log.d(LocationUtils.APPTAG, "Google Play Service available.");
            return true;
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            dialog.show();
            return false;
        }
    }

 	@SuppressLint("NewApi")
    public void getAddress(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && !Geocoder.isPresent()) {
            Toast.makeText(this, "No Geocoder available", Toast.LENGTH_LONG).show();
            return;
        }
        if (servicesConnected()) { }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else { }
    }

    @Override
    public void onLocationChanged(Location loc) {
        // Report to the UI that the location was updated
        location = loc;
//        Log.e("Location LOG", location.getLatitude() + "," + location.getLongitude());
    }

    @Override
    public void onResume() {
        super.onResume();
        // If the app already has a setting for getting location updates, get it
        if (mPrefs.contains(LocationUtils.KEY_UPDATES_REQUESTED)) {
            mUpdatesRequested = mPrefs.getBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);
        // Otherwise, turn off location updates until requested
        } else {
            mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);
            mEditor.commit();
        }
    }

    @Override
    public void onPause() {
        // Save the current setting for updates
        mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, mUpdatesRequested);
        mEditor.commit();
        super.onPause();
    }

    public void showAds() {
		FrameLayout frameAds = (FrameLayout) findViewById(R.id.frameAds);
        if(Config.WILL_SHOW_ADS) {
        	frameAds.setVisibility(View.VISIBLE);
        	// Create an ad.
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
                adView.loadAd(adRequest);
            }
        }
        else {
        	frameAds.setVisibility(View.GONE);
        }
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		Log.i("GoogleApiClient", "GoogleApiClient connection has been suspend");
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
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
    public void onDestroy() {
        super.onDestroy();
    }
}
