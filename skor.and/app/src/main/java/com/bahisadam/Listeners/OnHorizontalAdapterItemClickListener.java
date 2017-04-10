package com.bahisadam.Listeners;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.bahisadam.adapter.HorizontalAdapter;

/**
 * Created by atata on 20/11/2016.
 */

public class OnHorizontalAdapterItemClickListener implements RecyclerView.OnItemTouchListener {

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_DOWN) {
            View view = rv.findChildViewUnder(e.getX(), e.getY());
            int position = rv.getChildAdapterPosition(view);
            ((HorizontalAdapter) rv.getAdapter()).performClick(position);
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