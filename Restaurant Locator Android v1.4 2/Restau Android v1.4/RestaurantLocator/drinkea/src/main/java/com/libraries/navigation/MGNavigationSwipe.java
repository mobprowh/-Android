package com.libraries.navigation;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class MGNavigationSwipe extends ViewPager {

    public MGNavigationSwipe(Context context) {
        super(context);
    }

    public MGNavigationSwipe(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent arg0) {
//        // Never allow swiping to switch between pages
//        return false;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        // Never allow swiping to switch between pages
//        return true;
//    }
}