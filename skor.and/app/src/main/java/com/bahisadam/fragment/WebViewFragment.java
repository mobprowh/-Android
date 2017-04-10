package com.bahisadam.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.bahisadam.R;
import com.bahisadam.utility.Utilities;
import com.bahisadam.utility.WebClient;


public class WebViewFragment extends Fragment {
    private static final String ARG_LINK = "mLink";
    private String mLink;
    private ProgressBar progressBar;
    private WebView mWebView;



    public WebViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param link Parameter 1.
     * @return A new instance of fragment WebViewFragment.
     */

    public static WebViewFragment newInstance(String link) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LINK,link);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLink = getArguments().getString(ARG_LINK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_web_view, container, false);
        mWebView = (WebView) v.findViewById(R.id.contentWebView);
        mWebView.setVisibility(View.INVISIBLE);
        mWebView.loadUrl(mLink);
        progressBar =(ProgressBar) v.findViewById(R.id.progressBar);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUserAgentString("bahisadam-android"); // here set user agent info i got it


        mWebView.setWebViewClient(new WebClient(getActivity(),progressBar));
        Utilities.showProgressDialog(getActivity(),progressBar);
        return v;
    }






}
