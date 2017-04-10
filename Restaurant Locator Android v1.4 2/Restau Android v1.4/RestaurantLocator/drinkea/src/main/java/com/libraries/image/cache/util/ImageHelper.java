package com.libraries.image.cache.util;

import java.io.IOException;
import java.io.InputStream;
import com.libraries.image.cache.ImageLoader;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

public class ImageHelper {
	
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int radiusPixel) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = radiusPixel;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        
        return output;
    }
    
    public static Bitmap getRoundedCornerBitmap2(Bitmap bitmap, int radiusPixel) {
    	Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
    	
    	BitmapShader shader = new BitmapShader (bitmap,  TileMode.CLAMP, TileMode.CLAMP);
    	Paint paint = new Paint();
    	paint.setShader(shader);
    	paint.setAntiAlias(true);
    	        
    	Canvas c = new Canvas(circleBitmap);
    	c.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, paint);
  
    	return circleBitmap;
    }

    public static Bitmap addWhiteBorder(Bitmap bmp, int borderSize) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }
    
    
    /** Creates and returns a new bitmap which is the same as the provided bitmap
     * but with horizontal or vertical padding (if necessary)
     * either side of the original bitmap
     * so that the resulting bitmap is a square.
     * @param bitmap is the bitmap to pad.
     * @return the padded bitmap.*/
    public static Bitmap padBitmap(Bitmap bitmap) {
      int paddingX;
      int paddingY;

      if (bitmap.getWidth() == bitmap.getHeight()) {
        paddingX = 0;
        paddingY = 0; 
      }
      else if (bitmap.getWidth() > bitmap.getHeight()) {
        paddingX = 0;
        paddingY = bitmap.getWidth() - bitmap.getHeight();
      }
      else {
        paddingX = bitmap.getHeight() - bitmap.getWidth();
        paddingY = 0;
      }

      Bitmap paddedBitmap = Bitmap.createBitmap(
          bitmap.getWidth() + paddingX,
          bitmap.getHeight() + paddingY,
          Bitmap.Config.ARGB_8888);

      Canvas canvas = new Canvas(paddedBitmap);
      canvas.drawARGB(0xFF, 0xFF, 0xFF, 0xFF); // this represents white color
      canvas.drawBitmap(
          bitmap,
          paddingX / 2,
          paddingY / 2,
          new Paint(Paint.FILTER_BITMAP_FLAG));

      return paddedBitmap;
    }
    
    
    /*
     * Drawable imageDrawable = new BitmapDrawable(context.getResources(), bitmap);
     * Bitmap framedBitmap = ImageHelper.createFramedImage(imageDrawable, borderThickness);
     */
    public static Bitmap createFramedImage(Drawable imageDrawable, int borderThickness) {
    	int size = Math.min(imageDrawable.getMinimumWidth(), imageDrawable.getMinimumHeight());
		Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		RectF outerRect = new RectF(0, 0, size, size);
		
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.RED);
		canvas.drawRect(outerRect, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		imageDrawable.setBounds(0, 0, size, size);
	

		// Save the layer to apply the paint
		canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
		imageDrawable.draw(canvas);
		canvas.restore();

		// FRAMING THE PHOTO
		float border = size / 15f;

		// 1. Create offscreen bitmap link: http://www.youtube.com/watch?feature=player_detailpage&v=jF6Ad4GYjRU#t=1035s
		Bitmap framedOutput = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		Canvas framedCanvas = new Canvas(framedOutput);
		// End of Step 1

		// Start - TODO IMPORTANT - this section shouldn't be included in the final code
		// It's needed here to differentiate step 2 (red) with the background color of the activity
		// It's should be commented out after the codes includes step 3 onwards
		// Paint squaredPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// squaredPaint.setColor(Color.BLUE);
		// framedCanvas.drawRoundRect(outerRect, 0f, 0f, squaredPaint);
		// End

		// 2. Draw an opaque rounded rectangle link:
		RectF innerRect = new RectF(border, border, size - border, size - border);

		Paint innerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		innerPaint.setColor(Color.RED);
//		framedCanvas.drawRoundRect(innerRect, cornerRadius, cornerRadius, outerPaint);
		framedCanvas.drawRect(innerRect, innerPaint);

		// 3. Set the Power Duff mode link:
		Paint outerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		outerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

		// 4. Draw a translucent rounded rectangle link:
		outerPaint.setColor(Color.argb(255, 255, 255, 255));
//		framedCanvas.drawRoundRect(outerRect, cornerRadius, cornerRadius, outerPaint);
		framedCanvas.drawRect(outerRect,  outerPaint);

		// 5. Draw the frame on top of original bitmap
		canvas.drawBitmap(framedOutput, 0f, 0f, null);

		return output;
	}
    
    
    /*
     * Drawable imageDrawable = new BitmapDrawable(context.getResources(), bitmap);
     * Bitmap framedBitmap = ImageHelper.createFramedImage(imageDrawable, borderThickness);
     */
    public static Bitmap createRoundedFramedImage(Drawable imageDrawable, int borderThickness) {
    	int size = Math.min(imageDrawable.getMinimumWidth(), imageDrawable.getMinimumHeight());
//		Drawable imageDrawable = (image != null) ? new BitmapDrawable(image) : placeHolder;

		Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		RectF outerRect = new RectF(0, 0, size, size);
		float cornerRadius = size / 18f;

		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.RED);
		canvas.drawRoundRect(outerRect, cornerRadius, cornerRadius, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		imageDrawable.setBounds(0, 0, size, size);
	

		// Save the layer to apply the paint
		canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
		imageDrawable.draw(canvas);
		canvas.restore();

		// FRAMING THE PHOTO
		float border = size / 15f;

		// 1. Create offscreen bitmap link: http://www.youtube.com/watch?feature=player_detailpage&v=jF6Ad4GYjRU#t=1035s
		Bitmap framedOutput = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		Canvas framedCanvas = new Canvas(framedOutput);
		// End of Step 1

		// Start - TODO IMPORTANT - this section shouldn't be included in the final code
		// It's needed here to differentiate step 2 (red) with the background color of the activity
		// It's should be commented out after the codes includes step 3 onwards
		// Paint squaredPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// squaredPaint.setColor(Color.BLUE);
		// framedCanvas.drawRoundRect(outerRect, 0f, 0f, squaredPaint);
		// End

		// 2. Draw an opaque rounded rectangle link:
		// http://www.youtube.com/watch?feature=player_detailpage&v=jF6Ad4GYjRU#t=1044s
		RectF innerRect = new RectF(border, border, size - border, size - border);

		Paint innerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		innerPaint.setColor(Color.RED);
		framedCanvas.drawRoundRect(innerRect, cornerRadius, cornerRadius, innerPaint);

		// 3. Set the Power Duff mode link:
		// http://www.youtube.com/watch?feature=player_detailpage&v=jF6Ad4GYjRU#t=1056s
		Paint outerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		outerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

		// 4. Draw a translucent rounded rectangle link:
		// http://www.youtube.com/watch?feature=player_detailpage&v=jF6Ad4GYjRU
		outerPaint.setColor(Color.argb(100, 0, 0, 0));
		framedCanvas.drawRoundRect(outerRect, cornerRadius, cornerRadius, outerPaint);

		// 5. Draw the frame on top of original bitmap
		canvas.drawBitmap(framedOutput, 0f, 0f, null);

		return output;
	}
    
    public static Bitmap createRoundedFramedImage(Drawable imageDrawable, int borderThickness, int color) {
    	int size = Math.min(imageDrawable.getMinimumWidth(), imageDrawable.getMinimumHeight());
//		Drawable imageDrawable = (image != null) ? new BitmapDrawable(image) : placeHolder;

		Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		RectF outerRect = new RectF(0, 0, size, size);
		float cornerRadius = size / 18f;

		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.RED);
		canvas.drawRoundRect(outerRect, cornerRadius, cornerRadius, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		imageDrawable.setBounds(0, 0, size, size);
	

		// Save the layer to apply the paint
		canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
		imageDrawable.draw(canvas);
		canvas.restore();

		// FRAMING THE PHOTO
		float border = size / 15f;

		// 1. Create offscreen bitmap link: http://www.youtube.com/watch?feature=player_detailpage&v=jF6Ad4GYjRU#t=1035s
		Bitmap framedOutput = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		Canvas framedCanvas = new Canvas(framedOutput);
		// End of Step 1

		// Start - TODO IMPORTANT - this section shouldn't be included in the final code
		// It's needed here to differentiate step 2 (red) with the background color of the activity
		// It's should be commented out after the codes includes step 3 onwards
		// Paint squaredPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// squaredPaint.setColor(Color.BLUE);
		// framedCanvas.drawRoundRect(outerRect, 0f, 0f, squaredPaint);
		// End

		// 2. Draw an opaque rounded rectangle link:
		// http://www.youtube.com/watch?feature=player_detailpage&v=jF6Ad4GYjRU#t=1044s
		RectF innerRect = new RectF(border, border, size - border, size - border);

		Paint innerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		innerPaint.setColor(Color.RED);
		framedCanvas.drawRoundRect(innerRect, cornerRadius, cornerRadius, innerPaint);

		// 3. Set the Power Duff mode link:
		// http://www.youtube.com/watch?feature=player_detailpage&v=jF6Ad4GYjRU#t=1056s
		Paint outerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		outerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

		// 4. Draw a translucent rounded rectangle link:
		// http://www.youtube.com/watch?feature=player_detailpage&v=jF6Ad4GYjRU
		outerPaint.setColor(color);
		framedCanvas.drawRoundRect(outerRect, cornerRadius, cornerRadius, outerPaint);

		// 5. Draw the frame on top of original bitmap
		canvas.drawBitmap(framedOutput, 0f, 0f, null);

		return output;
	}
    
    
    /*
     * Drawable imageDrawable = new BitmapDrawable(context.getResources(), bitmap);
     * Bitmap framedBitmap = ImageHelper.createFramedImage(imageDrawable, borderThickness);
     */
    public static Bitmap createRoundedImage(Drawable imageDrawable, int radius)  {
    	int size = Math.min(imageDrawable.getMinimumWidth(), imageDrawable.getMinimumHeight());
//		Drawable imageDrawable = (image != null) ? new BitmapDrawable(image) : placeHolder;

		Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		RectF outerRect = new RectF(0, 0, size, size);
		float cornerRadius = radius;

		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.RED);
		canvas.drawCircle(outerRect.centerX(), outerRect.centerY(), cornerRadius, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		imageDrawable.setBounds(0, 0, size, size);
	

		// Save the layer to apply the paint
		canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
		imageDrawable.draw(canvas);
		canvas.restore();

		// FRAMING THE PHOTO
		float border = size / 15f;

		// 1. Create offscreen bitmap link: http://www.youtube.com/watch?feature=player_detailpage&v=jF6Ad4GYjRU#t=1035s
		Bitmap framedOutput = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		Canvas framedCanvas = new Canvas(framedOutput);
		// End of Step 1

		// Start - TODO IMPORTANT - this section shouldn't be included in the final code
		// It's needed here to differentiate step 2 (red) with the background color of the activity
		// It's should be commented out after the codes includes step 3 onwards
		// Paint squaredPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// squaredPaint.setColor(Color.BLUE);
		// framedCanvas.drawRoundRect(outerRect, 0f, 0f, squaredPaint);
		// End

		// 2. Draw an opaque rounded rectangle link:
		// http://www.youtube.com/watch?feature=player_detailpage&v=jF6Ad4GYjRU#t=1044s
		RectF innerRect = new RectF(border, border, size - border, size - border);

		Paint innerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		innerPaint.setColor(Color.RED);
		framedCanvas.drawCircle(innerRect.centerX(), innerRect.centerY(), cornerRadius, innerPaint);
//		framedCanvas.drawRoundRect(innerRect, cornerRadius, cornerRadius, innerPaint);

		// 3. Set the Power Duff mode link:
		// http://www.youtube.com/watch?feature=player_detailpage&v=jF6Ad4GYjRU#t=1056s
		Paint outerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		outerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

		// 4. Draw a translucent rounded rectangle link:
		// http://www.youtube.com/watch?feature=player_detailpage&v=jF6Ad4GYjRU
		outerPaint.setColor(Color.argb(255, 255, 255, 255));
//		framedCanvas.drawRoundRect(outerRect, cornerRadius, cornerRadius, outerPaint);
		framedCanvas.drawCircle(outerRect.centerX(), outerRect.centerY(), cornerRadius, outerPaint);

		// 5. Draw the frame on top of original bitmap
		canvas.drawBitmap(framedOutput, 0f, 0f, null);

		return output;
	}
    
    
    public static Bitmap getRoundedCornerBitmap3(Drawable imageDrawable, int radius) {
    
    	Bitmap d = ((BitmapDrawable)imageDrawable).getBitmap();
    	BitmapShader shader = new BitmapShader (d,  TileMode.CLAMP, TileMode.CLAMP);
    
    	int size = Math.min(d.getWidth(), d.getHeight());
		Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		RectF outerRect = new RectF(0, 0, size, size);
//		float cornerRadius = radius;

		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setShader(shader);
    	paint.setAntiAlias(true);
		paint.setColor(Color.RED);
		canvas.drawCircle(outerRect.centerX(), outerRect.centerY(), d.getWidth()/2, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		imageDrawable.setBounds(0, 0, size, size);

		Canvas canvas1 = new Canvas(output);

		RectF outerRect1 = new RectF(0, 0, size, size);

		Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint1.setShader(shader);
    	paint1.setAntiAlias(true);
		paint1.setColor(Color.RED);
		canvas1.drawCircle(outerRect1.centerX(), outerRect1.centerY(), d.getWidth()/2, paint);

		paint1.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

		return output;
    }
    
    
    public static Bitmap getBitmapFromAsset(FragmentActivity fragmentActivity, String strName) {
    	try {
    		AssetManager assetManager = fragmentActivity.getAssets();
    	    
    	    InputStream inStream = null;
    	    try {
    	    	inStream = assetManager.open(strName);
    	    } 
    	    catch (IOException e) {
    	    	e.printStackTrace();
    	    }
    	    
    	    Bitmap bitmap = BitmapFactory.decodeStream(inStream);
    	    return bitmap;
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	return null;
    }
    
    
    public static Bitmap convertBitmapWithFrame(
    		FragmentActivity fragmentActivity, Bitmap bitmap, int borderThickness) {
    	
    	Drawable imageDrawable = new BitmapDrawable(
    			fragmentActivity.getResources(), bitmap);
		
	    Bitmap framedBitmap = ImageHelper.createFramedImage(
	    		imageDrawable, borderThickness);
	    
	    return framedBitmap; 
    }
    
    
    public static Bitmap convertDrawableWithFrame(
    		FragmentActivity fragmentActivity, Drawable imageDrawable, int borderThickness) {
    	
	    Bitmap framedBitmap = ImageHelper.createFramedImage(
	    		imageDrawable, borderThickness);
	    
	    return framedBitmap; 
    }
    
    
    public static Bitmap convertBitmapWithRoundedFrame(
    		FragmentActivity fragmentActivity, Bitmap bitmap, int borderThickness) {
    	
    	try {
    		Drawable imageDrawable = new BitmapDrawable(
        			fragmentActivity.getResources(), bitmap);
    		
        	Bitmap framedBitmap = ImageHelper.getRoundedCornerBitmap3(
    	    		imageDrawable, borderThickness);
    	    
    	    return framedBitmap; 
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	return null;
    }
    
    /*
     * 
     * Bitmap bitmap = ((BitmapDrawable)imgThumb.getDrawable()).getBitmap();
     * 
     * Bitmap bitmap = ((BitmapDrawable)imgThumb.getDrawable()).getBitmap();
     * Bitmap rounderBitmap = ImageHelper.getRoundedCornerBitmap2(bitmap, 20);
     * Bitmap rounderBitmap = ImageHelper.addWhiteBorder(bitmap, 0);
     * imgThumb.setImageBitmap(rounderBitmap);
     * loader1.DisplayImage(listEntry.imageList.get(0).photoImageThumbUrl, imgThumb);
     * 
     */
    public static void loadPhoto(final ImageView imgPhoto, String imgUrl, final Activity activity, final int residThumb) {
    	ImageLoader loader = new ImageLoader(activity, residThumb);
    	
    	loader.DisplayImageWithTag(imgUrl, imgPhoto, 1);
		loader.setOnCacheListener(new com.libraries.image.cache.ImageLoader.OnCacheListener() {
			
			@Override
			public void onImageLoaded(ImageLoader loader, Bitmap bitmap, int tag) {
				
				imgPhoto.setImageBitmap(bitmap);
			}
		});
    }
    
    
    public static void loadPhotoWithFrame(final ImageView imgPhoto, String imgUrl, 
    		final Activity activity, int residThumb, final int ThumbTickness) {
    	
    	ImageLoader loader = new ImageLoader( activity, residThumb);
    	
    	loader.DisplayImageWithTag(imgUrl, imgPhoto, 1);
		loader.setOnCacheListener(new com.libraries.image.cache.ImageLoader.OnCacheListener() {
			@Override
			public void onImageLoaded(ImageLoader loader, Bitmap bitmap, int tag) 
			{
				// TODO Auto-generated method stub
				Bitmap framedBitmap = ImageHelper.
						convertBitmapWithFrame((FragmentActivity)activity, bitmap, ThumbTickness);

				imgPhoto.setImageBitmap(framedBitmap);
			}
		});
    }
}


