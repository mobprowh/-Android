package com.libraries.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHelper 
{
	private LocationManager locationManager;
	public Location location;
	private boolean hasGpsProvider, hasNetwrokProvider;
	
	OnLocationListener mCallback;
	public static final int GPS_PROVIDER = 0;
	public static final int NETWORK_PROVIDER = 1;
	
	public interface OnLocationListener 
    {
        public void onLocationUpdated(LocationHelper helper, Location loc, int tag);
    }
	
	public void setOnLocationListener(OnLocationListener listener)
	{
		try 
		{
            mCallback = (OnLocationListener) listener;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(this.toString() + " must implement OnLocationListener");
        }
	}
	
	public void getLocation(Context mContext)
	{
        if (locationManager == null) 
        {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        }

        hasGpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        hasNetwrokProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (hasGpsProvider) {
        	checkGPS();
        }
        
        if (hasNetwrokProvider) {
        	checkNetwork();
        }
        
    }
	
	public void checkGPS() {
		
		locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 100, new LocationListener() {
                	
					@Override
					public void onStatusChanged(String provider, int status, Bundle extras) {
						// TODO Auto-generated method stub
					}
					
					@Override
					public void onProviderEnabled(String provider) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onProviderDisabled(String provider) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLocationChanged(Location location) {
						// TODO Auto-generated method stub
						LocationHelper.this.location = location;
						mCallback.onLocationUpdated(LocationHelper.this, location, GPS_PROVIDER);
					}
				});
        
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	}
	
	public void checkNetwork() {
		locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 100, new LocationListener() {
					@Override
					public void onStatusChanged(String provider, int status, Bundle extras) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onProviderEnabled(String provider) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onProviderDisabled(String provider) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLocationChanged(Location location) {
						// TODO Auto-generated method stub
						LocationHelper.this.location = location;
						mCallback.onLocationUpdated(LocationHelper.this, location, GPS_PROVIDER);
					}
				});
        
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	}
	
	
	public static List<Address> getAddress(Context c, Location loc) throws IOException
	{
		Geocoder geocoder = new Geocoder(c, Locale.getDefault());
		List<Address> addresses = null;
		
		addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
		
		return addresses;
	}
}
