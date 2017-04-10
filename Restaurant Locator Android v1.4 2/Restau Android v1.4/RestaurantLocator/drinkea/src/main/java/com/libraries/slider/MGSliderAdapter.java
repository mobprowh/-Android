package com.libraries.slider;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

public class MGSliderAdapter extends PagerAdapter {
	
	private int resid = 0;
	private OnMGSliderAdapterListener mCallback;
	public View[] selectionViews;
	public int numOfItems = 0;
	public int maxThumbCount = 0;
	
	public interface OnMGSliderAdapterListener {
        public void onOnMGSliderAdapterCreated(MGSliderAdapter 
        		adapter, View v, int position);
    }
	
	public void setOnMGSliderAdapterListener(OnMGSliderAdapterListener listener) {
		try {
            mCallback = (OnMGSliderAdapterListener) listener;
        } catch (ClassCastException e) {
            throw new ClassCastException(this.toString() + " must implement OnMGSliderAdapterListener");
        }
	}
	
	
	public MGSliderAdapter(int resid, int numOfItems, int maxThumbCount) {
		// TODO Auto-generated constructor stub
		this.resid = resid;
		this.numOfItems = numOfItems;
		selectionViews = new View[numOfItems];
		this.maxThumbCount = maxThumbCount;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int size = maxThumbCount <= numOfItems ? maxThumbCount : numOfItems;
		return size;
	}

	@Override
	public boolean isViewFromObject(View v, Object obj) {
		// TODO Auto-generated method stub
		return v == ((View) obj);
	}

	@Override
	public Object instantiateItem(View container, int position)  {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) 
				container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view = inflater.inflate(resid, null);
		
		selectionViews[position] = view;
		
		((ViewPager) container).addView(view,0);
		
		mCallback.onOnMGSliderAdapterCreated(this, view, position);
		
		return view;
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return super.saveState();
	}
	
	@Override
    public void destroyItem(View collection, int position, Object view)  {
		// disabled so that the view will not be removed and to avoid
		// flickering when animation begins
//         ((ViewPager) collection).removeView((View) view);
    }

}
