package com.libraries.imageview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MGHSquareImageView extends ImageView {

    public MGHSquareImageView(final Context context) {
        super(context);
    }

    public MGHSquareImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public MGHSquareImageView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), widthMeasureSpec);
        setMeasuredDimension(height, height);
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(h, h, oldw, oldh);
    }
}
