package com.bahisadam.utility;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by atata on 22/11/2016.
 */

public class WebClient extends WebViewClient {
    private Context context;
    private ProgressBar progressBar;

    public WebClient(Context context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
    }
    public WebClient(){

    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }


    @Override
    public void onPageFinished(WebView view, String url)
    {
        //view.loadUrl("javascript:window.stateProvider.go('soccer.match-detail.overview', { match_id : '582946208a505612001741f9', home_team : 'besiktas', away_team: 'benfica', season: '2017', league: 'sampiyonlar-ligi',country: 'uefa'})");

        view.setVisibility(View.VISIBLE);
        Utilities.dismissProgressDialog(context,progressBar);

    }
}
