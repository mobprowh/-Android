package com.bahisadam.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.view.Menu;
import android.widget.Toast;
import com.bahisadam.BuildConfig;
import com.bahisadam.MyApplication;
import com.bahisadam.R;
import com.bahisadam.fragment.FootballFragment;
import com.bahisadam.fragment.LiveFragment;
import com.bahisadam.fragment.TournamentsFragment;
import com.bahisadam.fragment.WebViewFragment;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.utility.Preferences;
import com.bahisadam.utility.Utilities;

import java.lang.ref.WeakReference;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class HomeActivity extends BaseActivity implements Constant, AppBarLayout.OnOffsetChangedListener {

    private static final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;
    private Toolbar mToolbar;
    private CoordinatorLayout coordinatorLayout;
    private static WeakReference<HomeActivity> mContext;
    private boolean backClicked = false;
    private boolean homePage;
    private DrawerLayout mDrawer;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    // Make sure to be using android.support.v7.app.ActionBarDrawerToggle version.
    // The android.support.v4.app.ActionBarDrawerToggle has been deprecated.
    private ActionBarDrawerToggle drawerToggle;

    // HomeActivity Instance
    public static HomeActivity getInstance() {
        return mContext.get();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.i("key", key);
                Log.i("value", value.toString());
                if(key.equals("match_id")) {
                    Bundle bundle = new Bundle();
                    bundle.putString(DetailPageActivity.MATCH_ID, value.toString());
                    String type = getIntent().getExtras().get("type").toString();
                    if (type.equals("lineup")) {
                        bundle.putString(DetailPageActivity.ARG_REUSULT_TYPE, LINEUP);
                    } else {
                        bundle.putString(DetailPageActivity.ARG_REUSULT_TYPE, PLAYING);
                    }

                    Utilities.openMatchDetails(HomeActivity.this, bundle);
                    break;
                }
            }
        }

        if (Utilities.isNetworkAvailable(getApplicationContext())) {
            if(Preferences.isLogged()) Utilities.login(this,false);
        } else {
            Utilities.showSnackBarInternet(this, coordinatorLayout,
                    getString(R.string.no_internet_connection));
        }

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        // Create Remote Config Setting to enable developer mode.
        // Fetching configs from the server is normally limited to 5 requests per hour.
        // Enabling developer mode allows many more requests to be made per hour, so developers
        // can test different config values during development.
        // [START enable_dev_mode]

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
        //        .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        // [END enable_dev_mode]


        // Set default Remote Config values. In general you should have in app defaults for all
        // values that you may configure using Remote Config later on. The idea is that you
        // use the in app defaults and when you need to adjust those defaults, you set an updated
        // value in the App Manager console. Then the next time you application fetches from the
        // server, the updated value will be used. You can set defaults via an xml file like done
        // here or you can set defaults inline by using one of the other setDefaults methods.S
        // [START set_default_values]
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        // [END set_default_values]

        fetchConfig();

        setContentView(R.layout.activity_home);
        if(Preferences.isLogged()) Utilities.login(this,false);

        mContext = new WeakReference<>(HomeActivity.this);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        Intent intent = getIntent();

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // ...From section above...
        // Find our drawer view
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);
        mDrawer.addDrawerListener(drawerToggle);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setSupportActionBar(mToolbar);

        drawerToggle = setupDrawerToggle();
        drawerToggle.setDrawerIndicatorEnabled(true);
        // Tie DrawerLayout events to the ActionBarToggle

        initFooterToolbar();

        int resultCode = intent.getIntExtra(PAGE, -1);
        int id = intent.getIntExtra(ID,-1);
        String player = intent.getStringExtra(PLAYER);

        switch (resultCode){
            case RESULT_LOAD_HOME_PAGE: loadHomePage();break;
            case RESULT_LOAD_TOURNAMENTS: loadTournaments();break;
            case RESULT_LOAD_LIVE: loadLive();break;
            case RESULT_LOAD_FAVORITE: loadFavorite();break;
            case RESULT_LOAD_TEAM_PAGE: loadTeam(id); break;
            case RESULT_LOAD_PLAYER: loadPlayer(id,player);break;
            default:loadHomePage();break;
        }
        //resultCode = -1;
         /* Load Fragment For First Time */
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

    }

    /**
     * Fetch welcome message from server.
     */
    private void fetchConfig() {
        long cacheExpiration = 3600 * 12; // 1 hour in seconds.
        // If in developer mode cacheExpiration is set to 0 so each fetch will retrieve values from
        // the server.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        // [START fetch_config_with_callback]
        // cacheExpirationSeconds is set to cacheExpiration here, indicating that any previously
        // fetched and cached config would be considered expired because it would have been fetched
        // more than cacheExpiration seconds ago. Thus the next fetch would go to the server unless
        // throttling is in progress. The default expiration duration is 43200 (12 hours).
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            // Once the config is successfully fetched it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                            MyApplication.sUse_Logo = mFirebaseRemoteConfig.getBoolean("is_logo_enabled");
                        } else {

                        }
                    }
                });
        // [END fetch_config_with_callback]
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //from action bar
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, R.string.drawer_open,  R.string.drawer_close);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        //Fragment fragment;
        homePage = false;
        switch(menuItem.getItemId()) {
            case R.id.home_page:
                loadHomePage(); homePage = true; break;
            case R.id.iddaa_bulletin:
                loadFragment(WebViewFragment.newInstance("http://www.bahisadam.com/iddaa-bulteni")); break;
            case R.id.forecast_league:
                loadFragment(WebViewFragment.newInstance("http://www.bahisadam.com/tahmin-ligi")); break;
            case R.id.transfers:
                loadFragment(WebViewFragment.newInstance("http://www.bahisadam.com/transferler")); break;
            default: break;
        }

        setActiveToolbarItem(-1);
        ActionBar currentBar = getSupportActionBar();
        if(currentBar != null) {

            currentBar.setDisplayShowHomeEnabled(true);
        }
        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    /*
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    */
    /* Get Coordinate Layout */
    public CoordinatorLayout getCoordinateLayout() {
        return coordinatorLayout;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int currentScrollPercentage = (Math.abs(i)) * 100
                / mMaxScrollSize;

        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
            if (!mIsImageHidden) {
                mIsImageHidden = true;

                // ViewCompat.animate(mFab).scaleY(0).scaleX(0).start();
            }
        }

        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
            if (mIsImageHidden) {
                mIsImageHidden = false;
                //ViewCompat.animate(mFab).scaleY(1).scaleX(1).start();
            }
        }
    }


    @Override
    public void loadHomePage() {

        homePage = true;
        loadFragment(new FootballFragment());
        setActiveToolbarItem(0);
        mToolbar.setVisibility(View.VISIBLE);
        mToolbar.setTitle(getString(R.string.app_name));

    }

    @Override
    public void loadLive() {
        homePage = false;
        loadFragment(new LiveFragment());
        setActiveToolbarItem(2);
        mToolbar.setVisibility(View.VISIBLE);
        mToolbar.setTitle(getString(R.string.live_toolbar));

    }

    @Override
    public void loadTournaments() {
        homePage = false;
        loadFragment(new TournamentsFragment());
        setActiveToolbarItem(1);
        mToolbar.setVisibility(View.VISIBLE);
        mToolbar.setTitle(getString(R.string.tournaments));
    }

    @Override
    public void loadFavorite() {
        /*homePage = false;
        loadFragment(WebViewFragment.newInstance("http://www.bahisadam.com/istatistikler"));

        mToolbar.setVisibility(View.GONE);*/
        homePage = false;
        setActiveToolbarItem(3);
        FootballFragment fr = FootballFragment.newInstance(true);
        loadFragment(fr);
        mToolbar.setTitle(getString(R.string.app_name));
    }

    public void loadTeam(int id) {
        homePage = false;
        Utilities.openTeamDetails(this,id);
        setActiveToolbarItem(1);
        mToolbar.setVisibility(View.VISIBLE);
        mToolbar.setTitle("");
    }

    public void loadPlayer(int id, String player) {
        homePage = false;
        loadFragment(WebViewFragment.newInstance("http://www.bahisadam.com/oyuncu/" + player +
                "/" + id + "/istatistikler"));
        setActiveToolbarItem(-1);
        mToolbar.setVisibility(View.GONE);
    }

    public void loadFragment(Fragment fragment) {

        if (fragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            //   fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

        }
    }

    @Override
    public void onBackPressed() {

        if(backClicked){
            super.onBackPressed();
        }

        if(homePage){
            backClicked = true;
            Utilities.showSnackBar(this,coordinatorLayout,"Press back again to exit");
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backClicked=false;
                }
            }, 2000);
        } else {
            loadHomePage();
            setActiveToolbarItem(0);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

        super.onSaveInstanceState(outState, outPersistentState);
    }
}
