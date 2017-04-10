package com.projects.activities;

import com.models.News;
import com.config.Config;
import com.projects.storefinder.R;
import com.libraries.refreshlayout.SwipeRefreshActivity;
import com.libraries.utilities.MGUtilities;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class NewsDetailActivity extends SwipeRefreshActivity implements OnClickListener {

	private News news;
	private WebView mWebview;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		setContentView(R.layout.fragment_news_detail);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        this.getActionBar().setTitle("");

		this.getActionBar().setIcon(R.drawable.empty_image);
		this.getActionBar().setDisplayShowCustomEnabled(true);
		View viewActionBar = getLayoutInflater().inflate(R.layout.header, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT,
				Gravity.CENTER);

		this.getActionBar().setCustomView(viewActionBar, params);
        
		news = (News)this.getIntent().getSerializableExtra("news");
		mWebview = (WebView) findViewById(R.id.webView);
		
		ImageView imgWebBack = (ImageView) findViewById(R.id.imgWebBack);
		imgWebBack.setOnClickListener(this);
		
		ImageView imgWebForward = (ImageView) findViewById(R.id.imgWebForward);
		imgWebForward.setOnClickListener(this);
		
		ImageView imgWebRefresh = (ImageView) findViewById(R.id.imgWebRefresh);
		imgWebRefresh.setOnClickListener(this);
		
		showSwipeProgress();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				loadWebView();
			}
		}, Config.DELAY_SHOW_ANIMATION);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.imgWebBack:
				if(mWebview.canGoBack())
					mWebview.goBack();
				break;
			case R.id.imgWebForward:
				if(mWebview.canGoForward())
					mWebview.goForward();
				break;
			case R.id.imgWebRefresh:
				loadWebView();
				break;
		}
	}
	
	@SuppressLint("SetJavaScriptEnabled") 
	private void loadWebView() {
		if(!MGUtilities.hasConnection(this)) {
			MGUtilities.showAlertView(
					this, 
					R.string.network_error, 
					R.string.no_network_connection);
			hideSwipeProgress();
			return;
		}
		
		String strUrl = news.getNews_url();
		if(!news.getNews_url().contains("http")) {
			strUrl = "http://" + news.getNews_url();
		}
		final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(NewsDetailActivity.this, description, Toast.LENGTH_SHORT).show();
            }

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				progressBar.setVisibility(View.GONE);
				hideSwipeProgress();
			}
        });
        mWebview.loadUrl(strUrl);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        // Handle action bar actions click
        switch (item.getItemId()) {
	        default:
	        	finish();	
	            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        // if nav drawer is opened, hide the action items
        return super.onPrepareOptionsMenu(menu);
    }

}
