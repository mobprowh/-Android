package com.libraries.cache.util;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.libraries.cache.util.ImageCache.ImageCacheParams;

public class ImageFetcherWrapper {
	
	private int mImageThumbSizeHeight = 190;
	private int mImageThumbSizeWidth = 320;
    private ImageFetcher mImageFetcher;
    public FragmentActivity activity;
    
    public final float MEMORY_CACHE_SIZE_PERCENT = 0.50f;
    public final String IMAGE_CACHE_DIR = "thumbs";
    
    
    public ImageFetcherWrapper(FragmentActivity activity, int width, int height, int placeholder) {
    	this.activity = activity;
    	
    	mImageThumbSizeHeight = height;
    	mImageThumbSizeWidth = width;
    	
    	Log.d("mImageThumbSizeWidth = " + mImageThumbSizeWidth,
    			"mImageThumbSizeHeight = " + mImageThumbSizeHeight);
    	
    	ImageCacheParams cacheParams = new ImageCacheParams(activity, IMAGE_CACHE_DIR);
    	
        // Set memory cache to 25% of app memory
        cacheParams.setMemCacheSizePercent(MEMORY_CACHE_SIZE_PERCENT); 
    	
    	// The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(activity, mImageThumbSizeWidth, mImageThumbSizeHeight);
        mImageFetcher.setLoadingImage(placeholder);
        mImageFetcher.addImageCache(activity.getSupportFragmentManager(), cacheParams);
    }
    
    
    public ImageFetcher getImageFetcher() {
    	return mImageFetcher;
    }

}
