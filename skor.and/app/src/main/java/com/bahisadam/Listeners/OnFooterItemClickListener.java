package com.bahisadam.Listeners;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.bahisadam.adapter.FooterToolbarAdapter;
import com.bahisadam.view.BaseActivity;

/**
 * Created by atata on 24/11/2016.
 */

public class OnFooterItemClickListener implements RecyclerView.OnItemTouchListener {
    BaseActivity activity;

    public OnFooterItemClickListener(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_DOWN) {
            View view = rv.findChildViewUnder(e.getX(), e.getY());
            int position = rv.getChildAdapterPosition(view);
            activity.onFooterItemSelected(position);

            ((FooterToolbarAdapter)rv.getAdapter()).setActive(position);
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}