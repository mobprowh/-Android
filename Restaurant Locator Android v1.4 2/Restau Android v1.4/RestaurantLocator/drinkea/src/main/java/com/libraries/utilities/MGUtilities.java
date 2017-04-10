package com.libraries.utilities;

import com.project.drinkea.R;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;

public class MGUtilities {

	public static boolean hasConnection(Context c) {
		
	    ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

	    NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    if (wifiNetwork != null && wifiNetwork.isConnected()) {
	      return true;
	    }

	    NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    if (mobileNetwork != null && mobileNetwork.isConnected()) {
	      return true;
	    }

	    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	    if (activeNetwork != null && activeNetwork.isConnected()) {
	      return true;
	    }

	    return false;
	    
	}
	
	public static View getNoResultView(Context c) {
		
		LayoutInflater inf = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inf.inflate(R.layout.empty_list, null);
		
		return v;
	}
}
