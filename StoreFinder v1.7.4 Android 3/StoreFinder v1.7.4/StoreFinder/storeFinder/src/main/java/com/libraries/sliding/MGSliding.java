package com.libraries.sliding;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.FrameLayout;

public class MGSliding extends FrameLayout {

public MGSliding(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

    public MGSliding(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	private Animation inAnimation;
    private Animation outAnimation;

    public MGSliding(Context context) {
        super(context);
    }

    public void setInAnimation(Animation inAnimation) {
        this.inAnimation = inAnimation;
    }

    public void setOutAnimation(Animation outAnimation) {
        this.outAnimation = outAnimation;
    }

    @Override
    public void setVisibility(int visibility) {
        if (getVisibility() != visibility) {
        	
            if (visibility == VISIBLE) {
                if (inAnimation != null) startAnimation(inAnimation);
            }
//            else if ((visibility == INVISIBLE) || (visibility == GONE)) {
            else if ((visibility == INVISIBLE)) {
                if (outAnimation != null) startAnimation(outAnimation);
            }
        }

        super.setVisibility(visibility);
    }
    
}
