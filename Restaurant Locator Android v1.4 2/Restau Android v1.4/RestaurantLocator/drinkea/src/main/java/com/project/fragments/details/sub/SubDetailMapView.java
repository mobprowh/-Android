package com.project.fragments.details.sub;


import com.config.Config;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.models.Restaurant;
import com.project.drinkea.R;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;

public class SubDetailMapView{

	private View viewInflate = null;
	private SupportMapFragment mapFragment;
	private GoogleMap googleMap;
	private FragmentActivity act;
	
	public SubDetailMapView(FragmentActivity act) {
		// TODO Auto-generated method stub
		this.act = act;
		LayoutInflater inflater = act.getLayoutInflater();
		viewInflate = inflater.inflate(R.layout.sub_detail_map, null, false);
		
		setMap();
	}
	
	private void setMap() {
		
		mapFragment = new SupportMapFragment();
		FragmentTransaction fragmentTransaction = act.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameMapSubDetail, mapFragment);
        fragmentTransaction.commit();
	}
	
	public View setMapData(Restaurant res) {
		
		googleMap = mapFragment.getMap();
		
		if(googleMap == null)
			return viewInflate;
		
		googleMap.setMyLocationEnabled(true);
		
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.title(res.name);
		markerOptions.snippet(res.address);
		
		markerOptions.position(
				new LatLng(
						Double.parseDouble(res.lat), 
						Double.parseDouble(res.lon)));
		
		markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin));
		Marker mark = googleMap.addMarker(markerOptions);
		mark.showInfoWindow();
		
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(Config.MAP_ZOOM_LEVEL);
    	googleMap.moveCamera(zoom);
    	
		CameraUpdate center = CameraUpdateFactory.newLatLng(markerOptions.getPosition());
		
		googleMap.animateCamera(center);
		
		return viewInflate;
	}
	
	public View getView() {
		return viewInflate;
	}
}
