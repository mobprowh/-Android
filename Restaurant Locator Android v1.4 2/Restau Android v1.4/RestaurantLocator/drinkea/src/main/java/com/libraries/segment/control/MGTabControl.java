package com.libraries.segment.control;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class MGTabControl extends LinearLayout implements OnClickListener{

	int count;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public MGTabControl(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MGTabControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MGTabControl(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setTabCreation(int count, int resId) {
		
		this.count = count;
		
		this.removeAllViews();
		this.setOrientation(LinearLayout.HORIZONTAL);
		for(int x = 0; x < count; x++) {
			
			LayoutInflater inf = (LayoutInflater) 
					getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			View v = inf.inflate(resId, null);
			v.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
			v.setOnClickListener(this);
			v.setTag(x);
			this.addView(v);
			
			if(mCallback != null) {
				mCallback.OnMGTabControlCreated(this, v, x);
			}
			
		}
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		int pos = Integer.parseInt(v.getTag().toString());
		
		if(mCallback != null) {
			mCallback.OnMGTabControlSelected(this, v, pos);
		}
	}
	
	public void setSelectedTab(int index) {
		
		int selectedIndex = index < this.getChildCount() ? index : this.getChildCount() - 1;
		
		View v = (View) this.getChildAt(selectedIndex);
		onClick(v);
	}
	
	
	OnMGTabSelectedListener mCallback;
	
	public interface OnMGTabSelectedListener {
        public void OnMGTabControlSelected(MGTabControl 
        		control, View v, int position);
        
        public void OnMGTabControlCreated(MGTabControl 
        		control, View v, int position);
    }
	
	public void setOnMGTabSelectedListener(OnMGTabSelectedListener listener) {
		
		try {
            mCallback = (OnMGTabSelectedListener) listener;
        } catch (ClassCastException e)  {
            throw new ClassCastException(this.toString() + " must implement OnMGTabSelectedListener");
        }
	}
}
