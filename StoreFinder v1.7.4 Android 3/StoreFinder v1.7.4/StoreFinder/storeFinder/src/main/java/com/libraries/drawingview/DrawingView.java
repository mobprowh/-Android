package com.libraries.drawingview;


import java.util.List;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {

	//drawing path
	private Path drawPath;
	
	//drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	
	//initial color
	private int paintColor = 0xFF660000;
	
	//canvas
	private Canvas drawCanvas;
	
	//canvas bitmap
	private Bitmap canvasBitmap;
	
	//brush sizes
	private float brushSize, lastBrushSize;
	
	//erase flag
	private boolean erase=false;
	
	private Boolean isDrawing = false;
	
	private GoogleMap googleMap;
	
	private Boolean isDrawPolyline = false;
	private Boolean isDrawPolygon = false;
	
	PolylineOptions options;
	PolygonOptions polygonOptions;
	
	private int fillColor;
	private int polyLineColor;

	public DrawingView(Context context, AttributeSet attrs){
		super(context, attrs);
		setupDrawing();
		
		if(!isInEditMode()) {
			options = new PolylineOptions();
			polygonOptions = new PolygonOptions();
		}
	}
	
	public void setGoogleMap(GoogleMap googleMap) {
		this.googleMap = googleMap;
	}
	
	public void startDrawingPolyLine(Boolean enabled) {
		isDrawPolyline = enabled;
	}
	
	public void startDrawingPolygon(Boolean enabled) {
		isDrawPolygon = enabled;
	}

	//setup drawing
	private void setupDrawing(){

		//prepare for drawing and setup paint stroke properties
		brushSize = 20;//getResources().getInteger(R.integer.medium_size);
		lastBrushSize = brushSize;
		drawPath = new Path();
		drawPaint = new Paint();
		drawPaint.setColor(paintColor);
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(brushSize);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}
	
	public void enableDrawing(Boolean enabled) {
		isDrawing = enabled;
	}

	//size assigned to view
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
	}

	//draw the view - will be called after touch event
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);
	}

	//register user touches as drawing action
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float touchX = event.getX();
		float touchY = event.getY();
		//respond to down, move and up events
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			drawPath.moveTo(touchX, touchY);
			break;
		case MotionEvent.ACTION_MOVE:
			drawPath.lineTo(touchX, touchY);
			
			if(isDrawPolyline)
				addPolyline(touchX, touchY);
			
			if(isDrawPolygon)
				addPolygon(touchX, touchY);
			
			break;
		case MotionEvent.ACTION_UP:
			drawPath.lineTo(touchX, touchY);
			drawCanvas.drawPath(drawPath, drawPaint);
			drawPath.reset();
			
			if(isDrawPolyline)
				endPolyline();
			
			if(isDrawPolygon)
				endPolygon();
			
			break;
		default:
			return isDrawing;//false;
			
		}
		
		//redraw
		invalidate();
		return isDrawing;//true;

	}
	
	public void setPolylineColor(int color)
	{
		this.polyLineColor = color;
	}
	
	public void addPolyline(float touchX, float touchY)
	{
		LatLng latLng = googleMap.getProjection().fromScreenLocation(new Point( (int)touchX, (int)touchY ));
		
		options.add(latLng);
		options.width(brushSize);
		options.color(polyLineColor);
		
		Log.e("ADD MARKER LATLONG", latLng.latitude + " , " + latLng.longitude);
	}
	
	public void addPolygon(float touchX, float touchY)
	{
		LatLng latLng = googleMap.getProjection().fromScreenLocation(new Point( (int)touchX, (int)touchY ));	
		polygonOptions.add(latLng);
		polygonOptions.fillColor(fillColor);
		polygonOptions.strokeColor(paintColor);
		polygonOptions.strokeWidth(brushSize);
		
		Log.e("ADD MARKER LATLONG", latLng.latitude + " , " + latLng.longitude);
	}
	
	public void setPolygonFillColor(int color) {
		this.fillColor = color;
	}
	
	public void endPolyline()
	{
		if(options.getPoints().size() == 0)
			return;
		
		googleMap.addPolyline( options );
//		options = new PolylineOptions();
		if(mCallback != null)
		{
			mCallback.onUserDidFinishDrawPolyline(options);
		}
	}

	public void endPolygon()
	{
		if(polygonOptions.getPoints().size() == 0)
			return;
		
		googleMap.addPolygon( polygonOptions );
		
		if(mCallback != null)
		{
			mCallback.onUserDidFinishDrawPolygon(polygonOptions);
		}
		
//		polygonOptions = new PolygonOptions();
	}
	
	public void resetPolyline()
	{
		options = new PolylineOptions();
	}
	
	public void resetPolygon()
	{
		polygonOptions = new PolygonOptions();
	}
	
	//update color
	public void setColor(String newColor){
		invalidate();
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
	}
	
	public void setColor(int newColor){
		invalidate();
		paintColor = newColor;
		drawPaint.setColor(paintColor);
	}

	//set brush size
	public void setBrushSize(float newSize){
		float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
				newSize, getResources().getDisplayMetrics());
		brushSize=pixelAmount;
		drawPaint.setStrokeWidth(brushSize);
	}

	//get and set last brush size
	public void setLastBrushSize(float lastSize){
		lastBrushSize=lastSize;
	}
	public float getLastBrushSize(){
		return lastBrushSize;
	}

	//set erase true or false
	public void setErase(boolean isErase){
		erase=isErase;
		if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		else drawPaint.setXfermode(null);
	}

	//start new drawing
	public void startNew(){
		drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		invalidate();
	}
	
	
	public Boolean latLongContainsInPolygon(LatLng latLong, PolygonOptions mPolygonOptions)
	{
		// ray casting alogrithm http://rosettacode.org/wiki/Ray-casting_algorithm
		int crossings = 0;
		List<LatLng> path = mPolygonOptions.getPoints();

		int j = 0;
		
		// for each edge
        for (int i=0; i < path.size(); i++) {
        	LatLng a = path.get(i);
            j = i + 1;
            
            if (j >= path.size()) {
                j = 0;
            }
            
            LatLng b = path.get(j);
            
            if (rayCrossesSegment(latLong, a, b)) 
            {
                crossings++;
            }
        }

        // odd number of crossings?
        return (crossings % 2 == 1);
//        return ( crossings > 0 ? true : false);
    	
	}
	
	public Boolean latLongContainsInPolyline(LatLng latLong, PolylineOptions mPolylineOptions)
	{
		// ray casting alogrithm http://rosettacode.org/wiki/Ray-casting_algorithm
		int crossings = 0;
		List<LatLng> path = mPolylineOptions.getPoints();

		int j = 0;
		
		// for each edge
        for (int i=0; i < path.size(); i++) {
        	LatLng a = path.get(i);
            j = i + 1;
            
            if (j >= path.size()) {
                j = 0;
            }
            
            LatLng b = path.get(j);
            
            if (rayCrossesSegment(latLong, a, b)) 
            {
                crossings++;
            }
        }

        // odd number of crossings?
        return (crossings % 2 == 1);
//        return ( crossings > 0 ? true : false);
    	
	}
	
	public Boolean rayCrossesSegment(LatLng point, LatLng a, LatLng b) {
		
        double px = point.longitude,
            py = point.latitude,
            ax = a.longitude,
            ay = a.latitude,
            bx = b.longitude,
            by = b.latitude;
        
        if (ay > by) {
            ax = b.longitude;
            ay = b.latitude;
            bx = a.longitude;
            by = a.latitude;
        }
        // alter longitude to cater for 180 degree crossings
        if (px < 0) { px += 360; }
        if (ax < 0) { ax += 360; }
        if (bx < 0) { bx += 360; }

        if (py == ay || py == by) py += 0.00000001;
        
        if ((py > by || py < ay) || (px > Math.max(ax, bx))) return false;
        if (px < Math.min(ax, bx)) return true;

        double red = (ax != bx) ? ((by - ay) / (bx - ax)) : -1;
        double blue = (ax != px) ? ((py - ay) / (px - ax)) : -1;
        
        return (blue >= red);

    }
	
	
	OnDrawingViewListener mCallback;
	
	public interface OnDrawingViewListener 
    {
        public void onUserDidFinishDrawPolygon(PolygonOptions polygonOptions);
        public void onUserDidFinishDrawPolyline(PolylineOptions polylineOptions);
    }
	
	public void setOnDrawingViewListener(OnDrawingViewListener listener) {
		
		try {
            mCallback = (OnDrawingViewListener) listener;
        } catch (ClassCastException e) {
            throw new ClassCastException(this.toString() + " must implement OnDrawingViewListener");
        }
	}
}
