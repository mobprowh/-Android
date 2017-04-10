package com.project.fragments.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.config.UIConfig;
import com.libraries.image.cache.util.ImageFetcherWrapper;
import com.libraries.refreshlayout.SwipeRefreshActivity;
import com.models.Photo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.project.drinkea.MainActivity;
import com.project.drinkea.R;
import java.util.ArrayList;
import ru.truba.touchgallery.GalleryWidget.BaseUrlPagerAdapter;
import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;

public class ImageViewerActivity extends SwipeRefreshActivity {

    public ImageFetcherWrapper imageFetcher;
    DisplayImageOptions options;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		setContentView(R.layout.fragment_imageviewer);
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        this.getActionBar().setIcon(R.drawable.header_logo);
        this.getActionBar().setTitle("");

        imageFetcher = new ImageFetcherWrapper(
                this, 0, 0, UIConfig.SLIDER_PLACEHOLDER);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(UIConfig.SLIDER_PLACEHOLDER)
                .showImageForEmptyUri(UIConfig.SLIDER_PLACEHOLDER)
                .showImageOnFail(UIConfig.SLIDER_PLACEHOLDER)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ArrayList<Photo> photoList = (ArrayList<Photo>) this.getIntent().getSerializableExtra("photoList");
        int index = this.getIntent().getIntExtra("index", 0);
        setPhotos(photoList, index);
	}

    public void setPhotos(ArrayList<Photo> photoList, int position) {

        String[] urls = new String[photoList.size()];

        for(int x = 0; x < photoList.size(); x++) {
            Photo p = photoList.get(x);

            String strUrl = p.photo_url;
            if(!strUrl.contains("http")) {
                strUrl = "http://" + strUrl;
            }

            urls[x] = strUrl;
        }

        BaseUrlPagerAdapter adapter = new BaseUrlPagerAdapter(
                this, urls, R.layout.imageviewer_entry, UIConfig.SLIDER_PLACEHOLDER);

        adapter.setOnItemChangeListener(new BaseUrlPagerAdapter.OnItemChangeListener() {

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
                        }
                );
            }
        });

        GalleryViewPager galleryPager = (GalleryViewPager) findViewById(R.id.imageViewer);
        galleryPager.setOffscreenPageLimit(2);
        galleryPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if(position < photoList.size())
            galleryPager.setCurrentItem(position);

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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
    
    
}
