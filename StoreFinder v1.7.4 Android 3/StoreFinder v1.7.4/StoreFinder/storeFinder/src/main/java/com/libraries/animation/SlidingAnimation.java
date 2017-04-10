package com.libraries.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

public class SlidingAnimation extends Animation{
	
	private FrameLayout mAnimatedView;
	private int mEndHeight;
	private int mType;
	
	/**
	 * Initializes expand collapse animation, has two types, collapse (1) and expand (0).
	 * @param view The view to animate
	 * @param duration
	 * @param type The type of animation: 0 will expand from gone and 0 size to visible and layout size defined in xml. 
	 * 1 will collapse view and set to gone
	 */
	public SlidingAnimation(FrameLayout view, int duration, int viewType) {
	    setDuration(duration);
	    mAnimatedView = view;
	    mEndHeight = mAnimatedView.getLayoutParams().height;
	    mType = viewType;
	    
	    if(mType == View.VISIBLE) {
	        mAnimatedView.getLayoutParams().height = 0;
	        mAnimatedView.setVisibility(View.VISIBLE);
	    }
	}



	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
	    super.applyTransformation(interpolatedTime, t);
	    
	    if (interpolatedTime < 1.0f) {
	    	
	        if(mType == 0) {
	            mAnimatedView.getLayoutParams().height = (int) (mEndHeight * interpolatedTime);
	        } else {
	            mAnimatedView.getLayoutParams().height = mEndHeight - (int) (mEndHeight * interpolatedTime);
	        }

	        mAnimatedView.requestLayout();
	            
	    } else {
	    	
	        if(mType == 0) {
	            mAnimatedView.getLayoutParams().height = mEndHeight;
	            mAnimatedView.requestLayout();
	        } else {
	            mAnimatedView.getLayoutParams().height = 0;
	            mAnimatedView.setVisibility(View.GONE);
	            mAnimatedView.requestLayout();
	            mAnimatedView.getLayoutParams().height = mEndHeight;
	        }
	    }
	  }

}
