package com.libraries.segment.control;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class MGSegmentControl extends LinearLayout implements OnClickListener{

	int[] selectedResId;
	int[] unselectedResId;
	int[] titleResId;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public MGSegmentControl(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MGSegmentControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MGSegmentControl(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setSegmentCreation(int[] selectedResId, 
			int[] unselectedResId, int[] titleResId) throws Exception {
		
		if(selectedResId.length != unselectedResId.length && 
				selectedResId.length != titleResId.length) {
			
			throw new Exception("selectedResid and unselectedResId are not equal.");
		}
		
		this.selectedResId = selectedResId;
		this.unselectedResId = unselectedResId;
		this.titleResId = titleResId;
		
		this.removeAllViews();
		this.setOrientation(LinearLayout.HORIZONTAL);
		for(int x = 0; x < selectedResId.length; x++) {
			Button btn = new Button(getContext());
			btn.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
			
			btn.setText(titleResId[x]);
//			StateListDrawable state = createStates(selectedResId[x], unselectedResId[x]);
//			btn.setBackgroundDrawable(state);
			
			btn.setBackgroundResource(unselectedResId[x]);
			
			btn.setTag(x);
			btn.setOnClickListener(this);
			
			this.addView(btn);
			
			if(mCallback != null) {
				mCallback.OnMGSegmentControlCreated(this, btn, x);
			}
			
		}
		
	}
	
	public StateListDrawable createStates(int selectedResId, int unselectedResId) {
		
		StateListDrawable states = new StateListDrawable();
		
		states.addState(
				new int[] {android.R.attr.state_pressed}, 
				getResources().getDrawable(selectedResId) );
		
//		states.addState(new int[] {android.R.attr.state_focused},
//		    getResources().getDrawable(R.drawable.focused));
		
		states.addState(new int[] { },
		    getResources().getDrawable(unselectedResId) );
		
		return states;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		resetButtons();
		
		Button btn = (Button) v;
		int pos = Integer.parseInt(btn.getTag().toString());
		
		if(mCallback != null) {
			btn.setBackgroundResource(selectedResId[pos]);
			mCallback.OnMGSegmentControlSelected(this, btn, pos);
		}
	}
	
	private void resetButtons() {
		
		for(int x = 0; x < this.getChildCount(); x++) {
			View v = this.getChildAt(x);
			if(v instanceof Button) {
				((Button) v).setBackgroundResource(unselectedResId[x]);
			}
		}
	}
	

	public void setSelectedSegment(int index) {
		
		int selectedIndex = index < this.getChildCount() ? index : this.getChildCount() - 1;
		
		View v = (View) this.getChildAt(selectedIndex);
		onClick(v);
	}
	
	
	OnMGSegmentSelectedListener mCallback;
	
	public interface OnMGSegmentSelectedListener {
        public void OnMGSegmentControlSelected(MGSegmentControl 
        		control, Button button, int position);
        
        public void OnMGSegmentControlCreated(MGSegmentControl 
        		control, Button button, int position);
    }
	
	public void setOnMGSegmentSelectedListener(OnMGSegmentSelectedListener listener) {
		
		try {
            mCallback = (OnMGSegmentSelectedListener) listener;
        } catch (ClassCastException e)  {
            throw new ClassCastException(this.toString() + " must implement OnMGSegmentSelectedListener");
        }
	}
}
