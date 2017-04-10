package com.libraries.refreshlayout;

/*
 * Copyright (C) 2014 Antonio Leiva Gordillo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

import com.project.drinkea.R;

public abstract class SwipeRefreshActivity extends FragmentActivity implements SwipeRefreshLayout.OnRefreshListener {
 
    private SwipeRefreshLayout refreshLayout;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_swipe_refresh);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
    }
 
    @Override
    public void setContentView(int layoutResID) {
        View v = getLayoutInflater().inflate(layoutResID, refreshLayout, false);
        setContentView(v);
    }
 
    @Override
    public void setContentView(View view) {
        setContentView(view, view.getLayoutParams());
    }
 
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        refreshLayout.addView(view, params);
        initSwipeOptions();
    }
 
    private void initSwipeOptions() {
        refreshLayout.setOnRefreshListener(this);
        setAppearance();
        disableSwipe();
    }
 
    @SuppressWarnings("deprecation")
	private void setAppearance() {
        refreshLayout.setColorScheme(android.R.color.holo_blue_bright,
        android.R.color.holo_green_light,
        android.R.color.holo_orange_light,
        android.R.color.holo_red_light);
    }
 
    /**
     * It shows the SwipeRefreshLayout progress
     */
    public void showSwipeProgress() {
        refreshLayout.setRefreshing(true);
    }
 
    /**
     * It shows the SwipeRefreshLayout progress
     */
    public void hideSwipeProgress() {
        refreshLayout.setRefreshing(false);
    }
 
    /**
     * Enables swipe gesture
     */
    public void enableSwipe() {
        refreshLayout.setEnabled(true);
    }
 
    /**
     * Disables swipe gesture. It prevents manual gestures but keeps the option tu show
     * refreshing programatically.
     */
    public void disableSwipe() {
        refreshLayout.setEnabled(false);
    }
 
    /**
     * It must be overriden by parent classes if manual swipe is enabled.
     */
    @Override
    public void onRefresh() {
        // Empty implementation
    }
}
