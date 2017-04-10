/*
 Copyright (c) 2012 Roman Truba

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package ru.truba.touchgallery.GalleryWidget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import ru.truba.touchgallery.TouchView.UrlTouchImageView;

/**
 Class wraps URLs to adapter, then it instantiates <b>UrlTouchImageView</b> objects to paging up through them.
 */
public class BaseUrlPagerAdapter extends PagerAdapter {
	
	protected final String[] mResources;
    protected final Context mContext;
    protected int mCurrentPosition = -1;
    protected OnItemChangeListener mOnItemChangeListener;
    protected int resid = 0;
    protected int residPlaceHolder;
    
    public BaseUrlPagerAdapter(int residPlaceHolder) {
        mResources = null;
        mContext = null;
        this.residPlaceHolder = residPlaceHolder;
    }
    
    public BaseUrlPagerAdapter(Context context, String[] resources, int resid, int residPlaceHolder) {
        
    	this.mResources = resources;
        this.mContext = context;
        this.resid = resid;
        this.residPlaceHolder = residPlaceHolder;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        
    	super.setPrimaryItem(container, position, object);
        ((GalleryViewPager)container).mCurrentView = ((UrlTouchImageView)object).getImageView();
    }
    
    @Override
	public Object instantiateItem(ViewGroup container, int position) {
    	
		// TODO Auto-generated method stub
    	final UrlTouchImageView iv = new UrlTouchImageView(mContext);
        iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    	iv.residPlaceholder = residPlaceHolder;
    	
//    	iv.setUrl(mResources[position]);
    	iv.setUrl(mResources[position], mOnItemChangeListener);
		
        container.addView(iv, 0);
        return iv;
        
//		LayoutInflater inflater = (LayoutInflater) 
//				container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View view = inflater.inflate(resid, null);
//		((ViewPager) container).addView(view,0);
//		ImageEntry listEntry = mResources.get(mCurrentPosition);
//		mOnItemChangeListener.onGalleryAdapterCreated(this, view, position, listEntry);
//		return view;
	}

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view){
        collection.removeView((View) view);
    }
    
    public void setOnItemChangeListener(OnItemChangeListener listener) { mOnItemChangeListener = listener; }
    
    public static interface OnItemChangeListener {
    	public void onItemChange(int currentPosition);
    	
    	public void onItemImageView(String imageUrl, ImageView imgView, ProgressBar mProgressBar);
    	public void onGalleryAdapterCreated(
    			BaseUrlPagerAdapter adapter, View v, int currentPosition);
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mResources.length;
	}
	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view.equals(object);
	}
}
