package com.libraries.imageview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MGSquareImageView extends ImageView {

	Paint paint;
	boolean isRoundedEdge = false;
	boolean isSquareEdge = false;
	float radius = 0;
	RectF rectF;
	Rect rect;
	
    public MGSquareImageView(final Context context) {
        super(context);
    }

    public MGSquareImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        
    }

    public MGSquareImageView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(w, w, oldw, oldh);
    }
    
    
    public void applyBorder(int thickness, int color) {
    	paint = new Paint();
        paint.setStrokeWidth(thickness);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        isSquareEdge = true;
        
    }
    
    public void applyRoundedBorder(int thickness, int color, float radius) {
    	paint = new Paint();
        paint.setStrokeWidth(thickness);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        paint.setAntiAlias(true);
        isRoundedEdge = true;
        this.radius = radius;
    }

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		if(paint != null && isSquareEdge) {
			int height = this.getMeasuredWidth();
			int width = this.getMeasuredWidth();
			canvas.drawRect(0, 0, width, height, paint);
		}
		
		else if(paint != null && isRoundedEdge) {
			int height = this.getMeasuredWidth();
			int width = this.getMeasuredWidth();
			
			if(rect == null) {
				rect = new Rect(0, 0, width, height);
				rectF = new RectF(rect);
			}
			
			canvas.drawRoundRect(rectF, radius, radius, paint);
		}
		
		
	}
    
    
}
