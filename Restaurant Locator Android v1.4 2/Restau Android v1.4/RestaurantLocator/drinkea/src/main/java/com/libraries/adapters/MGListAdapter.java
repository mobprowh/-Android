package com.libraries.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MGListAdapter extends BaseAdapter  {
	
	Context c;
	private int count;
	private int resId;
	OnMGListAdapterAdapterListener mCallback;
	
	public interface OnMGListAdapterAdapterListener {
        public void OnMGListAdapterAdapterCreated(MGListAdapter 
        		adapter, View v, int position, ViewGroup viewGroup);
    }
	
	public void setOnMGListAdapterAdapterListener(OnMGListAdapterAdapterListener listener) {
		try {
            mCallback = (OnMGListAdapterAdapterListener) listener;
        } catch (ClassCastException e)  {
            throw new ClassCastException(this.toString() + " must implement OnMGListAdapterAdapterListener");
        }
	}
	
	
	public MGListAdapter(Context c, int count, int resId) {
		this.c = c;
		this.count = count;
		this.resId = resId;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public View getView(int pos, View v, ViewGroup viewGroup) {
		// TODO Auto-generated method stub
		
		if(v == null) {
			LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(resId, null);
		}
		
		if(mCallback != null)
			mCallback.OnMGListAdapterAdapterCreated(this, v, pos, viewGroup);
		
		return v;
	}
	
	
}

