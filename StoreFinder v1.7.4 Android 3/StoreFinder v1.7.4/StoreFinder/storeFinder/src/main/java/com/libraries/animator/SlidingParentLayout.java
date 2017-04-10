package com.libraries.animator;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

public class SlidingParentLayout extends RelativeLayout {

    private float yFraction = 0;

    public SlidingParentLayout(Context context) {
        super(context);
    }

    public SlidingParentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingParentLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private ViewTreeObserver.OnPreDrawListener preDrawListener = null;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setYFraction(float fraction) {

        this.yFraction = fraction;

        if (getHeight() == 0) {
            if (preDrawListener == null) {
                preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
                        setYFraction(yFraction);
                        return true;
                    }
                };
                getViewTreeObserver().addOnPreDrawListener(preDrawListener);
            }
            return;
        }

        float translationY = getHeight() * fraction;
        setTranslationY(translationY);
    }

    public float getYFraction() {
        return this.yFraction;
    }
}
