package com.projects.activities;

import java.util.ArrayList;
import ru.truba.touchgallery.GalleryWidget.BaseUrlPagerAdapter;
import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;
import ru.truba.touchgallery.GalleryWidget.BaseUrlPagerAdapter.OnItemChangeListener;
import com.config.UIConfig;
import com.libraries.cache.util.ImageFetcherWrapper;
import com.models.Photo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.projects.storefinder.MainActivity;
import com.projects.storefinder.R;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ImageViewerActivity extends FragmentActivity implements OnClickListener {

	public ImageFetcherWrapper imageFetcher;
	DisplayImageOptions options;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		setContentView(R.layout.fragment_imageviewer);
		
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
		
		imageFetcher = new ImageFetcherWrapper(
				ImageViewerActivity.this, 0, 0, UIConfig.SLIDER_PLACEHOLDER);
		
        options = new DisplayImageOptions.Builder()
			.showImageOnLoading(UIConfig.SLIDER_PLACEHOLDER)
			.showImageForEmptyUri(UIConfig.SLIDER_PLACEHOLDER)
			.showImageOnFail(UIConfig.SLIDER_PLACEHOLDER)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
        
        @SuppressWarnings("unchecked")
		ArrayList<Photo> photoList = (ArrayList<Photo>) this.getIntent().getSerializableExtra("photoList");
        int index = this.getIntent().getIntExtra("index", -1);
        setPhotos(photoList, index);
	}
	
	public void setPhotos(ArrayList<Photo> photoList, int position) {
		String[] urls = new String[photoList.size()];
		for(int x = 0; x < photoList.size(); x++) {
			Photo p = photoList.get(x);
			String strUrl = p.getPhoto_url();
			if(!strUrl.contains("http")) {
				strUrl = "http://" + strUrl;
			}
			urls[x] = strUrl;
		}
		BaseUrlPagerAdapter adapter = new BaseUrlPagerAdapter(
				ImageViewerActivity.this, urls, R.layout.imageviewer_entry, UIConfig.SLIDER_PLACEHOLDER); 
		
		adapter.setOnItemChangeListener(new OnItemChangeListener() {
			
			@Override
			public void onItemChange(int currentPosition) { }
			
			@Override
			public void onGalleryAdapterCreated(BaseUrlPagerAdapter adapter, View v,
					int currentPosition) { }

			@Override
			public void onItemImageView(String imageUrl, ImageView imgView, final ProgressBar mProgressBar) {
				// TODO Auto-generated method stub
				
				MainActivity.getImageLoader().displayImage(imageUrl, imgView, options, 
						
					new SimpleImageLoadingListener() {
					 
					@Override
					 public void onLoadingStarted(String imageUri, View view) {
						mProgressBar.setProgress(0);
						mProgressBar.setVisibility(View.VISIBLE);
					 }

					 @Override
					 public void onLoadingFailed(String imageUri, View view,
							 FailReason failReason) {
						 mProgressBar.setVisibility(View.GONE);
					 }

					 @Override
					 public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						 mProgressBar.setVisibility(View.GONE);
					 }
				 }, new ImageLoadingProgressListener() {
					 @Override
					 public void onProgressUpdate(String imageUri, View view, int current,
							 int total) {
						 mProgressBar.setProgress(Math.round(100.0f * current / total));
					 }
				 });
			}
		});
		
		GalleryViewPager galleryPager = (GalleryViewPager) findViewById(R.id.imageViewer);
        galleryPager.setOffscreenPageLimit(2);
        galleryPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        
        if(position < photoList.size() && position >= 0)
        	galleryPager.setCurrentItem(position);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
	
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

}
