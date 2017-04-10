package com.project.fragments.activity;

import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.config.UIConfig;
import com.db.DbHelper;
import com.db.Queries;
import com.libraries.refreshlayout.SwipeRefreshActivity;
import com.libraries.segment.control.MGSegmentControl;
import com.models.Favorite;
import com.models.Photo;
import com.models.Restaurant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.project.fragments.details.sub.SubDetailAboutView;
import com.project.fragments.details.sub.SubDetailGalleryView;
import com.project.fragments.details.sub.SubDetailMapView;
import com.project.drinkea.MainActivity;
import com.project.drinkea.R;
import java.util.Timer;
import java.util.TimerTask;

public class DetailsActivity extends SwipeRefreshActivity implements
        MGSegmentControl.OnMGSegmentSelectedListener, View.OnClickListener {

	public Queries q;
	private SQLiteDatabase db;
    DisplayImageOptions options;
    Restaurant restaurant;
    SubDetailAboutView aboutView = null;
    SubDetailMapView mapView = null;
    SubDetailGalleryView galleryView = null;
    boolean isFave = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		setContentView(R.layout.fragment_details);
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        this.getActionBar().setIcon(R.drawable.header_logo);
        this.getActionBar().setTitle("");
		
		DbHelper dbHelper = new DbHelper(this);
		q = new Queries(db, dbHelper);
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(UIConfig.SLIDER_PLACEHOLDER)
		.showImageForEmptyUri(UIConfig.SLIDER_PLACEHOLDER)
		.showImageOnFail(UIConfig.SLIDER_PLACEHOLDER)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

        showSwipeProgress();
        restaurant = (Restaurant)this.getIntent().getSerializableExtra("restaurant");
        updateView();
	}

    public void updateView() {
        ImageView imgViewPic = (ImageView) findViewById(R.id.imgViewPic);
        Photo p = q.getPhotoByRestaurantId(restaurant.restaurant_id);
        if(p != null) {
            String strUrl = p.photo_url;
            if(!strUrl.contains("http")) {
                strUrl = "http://" + strUrl;
            }
            MainActivity.getImageLoader().displayImage(strUrl, imgViewPic, options);
        }

        try {
            MGSegmentControl segmentControl = (MGSegmentControl) findViewById(R.id.segmentControl);
            segmentControl.setOnMGSegmentSelectedListener(this);
            segmentControl.setSegmentCreation(UIConfig.SELECTED_INNER_TAB_BG,
                    UIConfig.UNSELECTED_INNER_TAB_BG, UIConfig.INNER_TAB_TITLE);
            segmentControl.setSelectedSegment(0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        checkFavoriteState();
        hideSwipeProgress();
    }

    @Override
    public void OnMGSegmentControlSelected(MGSegmentControl control, Button button,
                                           int position) {
        // TODO Auto-generated method stub
        showDetailFragment(position);
    }

    @Override
    public void OnMGSegmentControlCreated(MGSegmentControl control, Button button,
                                          int position) {
        // TODO Auto-generated method stub
        button.setTextColor(Color.WHITE);
        button.setTypeface(button.getTypeface(), Typeface.BOLD);
    }

    private void showDetailFragment(int pos) {
        FrameLayout frameDetails = (FrameLayout) findViewById(R.id.containerDetails);
        frameDetails.removeAllViews();
        if(pos == 0) {
            if(aboutView == null) {
                aboutView = new SubDetailAboutView(this);
                aboutView.setDetail(restaurant);
            }
            frameDetails.addView(aboutView.getView());
        }
        else if(pos == 1) {
            if(mapView == null) {
                mapView = new SubDetailMapView(this);
                mapView.setMapData(restaurant);

                final Timer t = new Timer();
                t.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        t.cancel();
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                mapView.setMapData(restaurant);
                            }
                        });
                    }
                }, 500);
            }
            frameDetails.addView(mapView.getView());
        }
        else if(pos == 2) {
            if(galleryView == null) {
                galleryView = new SubDetailGalleryView(this);
                galleryView.showGalleries(restaurant);
            }
            frameDetails.addView(galleryView.getView());
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()) {
        }
    }

    private void checkFavoriteState() {
        Restaurant resFave = q.getFavoriteRestaurantsByRestaurantId(restaurant.restaurant_id);
        if(resFave == null) {
            isFave = false;
        }
        else {
            isFave = true;
        }
        invalidateOptionsMenu();
    }

    private void addToFavorites() {
        q.insertFavorite(restaurant.restaurant_id);
    }

    private void deleteFavorite() {
        Favorite fave = q.getFavoriteByRestaurantId(restaurant.restaurant_id);
        q.deleteFavorite(fave.favorite_id);
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.addFave:
                Restaurant resFave = q.getFavoriteRestaurantsByRestaurantId(restaurant.restaurant_id);
                if(resFave == null) {
                    addToFavorites();
                }
                else {
                    deleteFavorite();
                }
                checkFavoriteState();
                return true;
	        default:
	        	finish();	
	            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        if(isFave)
            getMenuInflater().inflate(R.menu.menu_del_fave, menu);
        else
            getMenuInflater().inflate(R.menu.menu_add_fave, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        // if nav drawer is opened, hide the action items
        return super.onPrepareOptionsMenu(menu);
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
    
    
}
