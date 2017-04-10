package com.project.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.libraries.asynctask.MGAsyncTask;
import com.libraries.asynctask.MGAsyncTask.OnMGAsyncTaskListener;
import com.config.Config;
import com.db.DbHelper;
import com.db.Queries;
import com.libraries.gmaps.GMapDirection;
import com.libraries.gmaps.directions.Directions;
import com.libraries.gmaps.directions.Leg;
import com.libraries.gmaps.directions.Route;
import com.libraries.gmaps.directions.Step;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.libraries.json.MGJSONHelper;
import com.models.Restaurant;
import com.project.fragments.activity.DetailsActivity;
import com.project.drinkea.MainActivity;
import com.project.drinkea.R;
import com.libraries.utilities.MGUtilities;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MapFragment extends Fragment implements 
				OnClickListener, OnMyLocationChangeListener,
		OnMarkerClickListener, GoogleMap.OnMapLoadedCallback, OnInfoWindowClickListener{

	private View viewInflate = null;
	private DbHelper dbHelper;
	private SQLiteDatabase db;
	public Queries q;
	private ArrayList<Restaurant> restaurantList;
	private HashMap<String, Restaurant> markers = null;
	private ArrayList<Marker> markerList = null;
	private Location myLocation;
	private GoogleMap googleMap;
	private final int RQS_GooglePlayServices = 1;
	private GMapDirection directions;
	private Marker selectedMarker = null;
	private boolean isLocationFound = true;
	private TextView tvDirectionKm;
	
	@Override
    public void onDestroyView()  {
        super.onDestroyView();
		try {
			if (googleMap != null) {
				FragmentManager fManager = this.getActivity().getSupportFragmentManager();
				fManager.beginTransaction()
						.remove(fManager.findFragmentById(R.id.googleMap)).commit();
				googleMap = null;
			}
			if (viewInflate != null) {
				ViewGroup parentViewGroup = (ViewGroup) viewInflate.getParent();
				if (parentViewGroup != null) {
					parentViewGroup.removeAllViews();
				}
			}
		}
		catch(Exception e) { }
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// save the reference of the inflated view
		if(viewInflate == null)
			viewInflate = inflater.inflate(R.layout.fragment_map, null, false);
        return viewInflate;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// this is really important in order to save the state across screen
		// configuration changes for example
		setRetainInstance(true);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)  {
		dbHelper = new DbHelper(getActivity());
        q = new Queries(db, dbHelper);

        directions = new GMapDirection();

		FragmentManager fManager = getChildFragmentManager();
		SupportMapFragment supportMapFragment =
				((SupportMapFragment) fManager.findFragmentById(R.id.googleMap));

		if(supportMapFragment == null) {
			fManager = getActivity().getSupportFragmentManager();
			supportMapFragment = ((SupportMapFragment) fManager.findFragmentById(R.id.googleMap));
		}

		MainActivity main = (MainActivity) getActivity();
		main.showSwipeProgress();

		googleMap = supportMapFragment.getMap();
		googleMap.setOnMapLoadedCallback(this);
	}

	@Override
	public void onMapLoaded() {
		FragmentManager fManager = getChildFragmentManager();
		SupportMapFragment supportMapFragment =
				((SupportMapFragment) fManager.findFragmentById(R.id.googleMap));

		if(supportMapFragment == null) {
			fManager = getActivity().getSupportFragmentManager();
			supportMapFragment = ((SupportMapFragment) fManager.findFragmentById(R.id.googleMap));
		}

		googleMap = supportMapFragment.getMap();
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		googleMap.setMyLocationEnabled(true);
		googleMap.setOnInfoWindowClickListener(this);
		googleMap.setOnMyLocationChangeListener(this);

		Button btnAllPins = (Button) viewInflate.findViewById(R.id.btnAllPins);
		btnAllPins.setOnClickListener(this);

		Button btnCurrentLocation = (Button) viewInflate.findViewById(R.id.btnCurrentLocation);
		btnCurrentLocation.setOnClickListener(this);

		Button btnRoute = (Button) viewInflate.findViewById(R.id.btnRoute);
		btnRoute.setOnClickListener(this);

		tvDirectionKm = (TextView) viewInflate.findViewById(R.id.tvDirectionKm);
		restaurantList = q.getRestaurants();


		addMarkers();
		showAllPins();

		MainActivity main = (MainActivity) getActivity();
		main.hideSwipeProgress();
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub
		Restaurant res = markers.get(marker.getId());
		Intent i = new Intent(getActivity(), DetailsActivity.class);
		i.putExtra("restaurant", res);
		startActivity(i);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.btnAllPins:
				showAllPins();
				break;
			case R.id.btnCurrentLocation:
				getLocation();
				break;
			case R.id.btnRoute:
				getRoute();
				break;
		}
	}
	
	private void showAllPins() {
		if(markerList == null || markerList.size() == 0 )
			return;
		
		LatLngBounds.Builder bld = new LatLngBounds.Builder();
	    for (int i = 0; i < markerList.size(); i++) {
	    		Marker marker = markerList.get(i);
	            bld.include(marker.getPosition());            
	    }
	    
	    LatLngBounds bounds = bld.build();
	    DisplayMetrics metrics = this.getResources().getDisplayMetrics();
	    googleMap.moveCamera(
	    		CameraUpdateFactory.newLatLngBounds(
	    				bounds, 
	    				metrics.widthPixels, 
	    				metrics.heightPixels, 
	    				70) );
	}
	
	private void getLocation() {
        isLocationFound = false;
	}
	
	private void getRoute() {
		if(myLocation != null) {
			if(selectedMarker != null) {
				getLocation(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 
						selectedMarker.getPosition());
			}
			else {
				Toast.makeText(getActivity(), 
						"Please select atleast 1 Marker.", 
						Toast.LENGTH_SHORT).show();
			}
		}
		else {
			Toast.makeText(getActivity(), 
					"We cannot find your current location. Please try again.", 
					Toast.LENGTH_SHORT).show();
		}
	}


	@Override
	public void onMyLocationChange(Location location) {
		// TODO Auto-generated method stub
		myLocation = location;
		calculateDistance();
		if(!isLocationFound) {
			isLocationFound = true;
			CameraUpdate zoom = CameraUpdateFactory.zoomTo(Config.MAP_ZOOM_LEVEL);
	    	googleMap.moveCamera(zoom);
	    	
			CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(myLocation.getLatitude(),
					myLocation.getLongitude()));
			
			googleMap.animateCamera(center);
		}
	}
	
	
	private void calculateDistance() {
		if(myLocation == null || selectedMarker == null) 
			return;
		
		Location dest = new Location("");
		dest.setLongitude(selectedMarker.getPosition().longitude);
		dest.setLatitude(selectedMarker.getPosition().latitude);
				
		float mts = myLocation.distanceTo(dest);
		float km = mts / 1000;
		
		String kmStr = String.format("%.2f Km", km);
		tvDirectionKm.setText(kmStr);
	}
	
	private void getLocation(final LatLng origin, final LatLng destination) {
		if(!MGUtilities.hasConnection(getActivity())) {
			Toast.makeText(getActivity(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		MGAsyncTask asyncTask = new MGAsyncTask(getActivity());
		asyncTask.setMGAsyncTaskListener(new OnMGAsyncTaskListener() {
			
			String json = null;
			@Override
			public void onAsyncTaskProgressUpdate(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAsyncTaskPreExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAsyncTaskPostExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				if(json != null) {
					drawRoute(json);
				}
			}
			
			@Override
			public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				json = MGJSONHelper.getJSONFromUrl(
						directions.getUrl(origin, destination, GMapDirection.MODE_DRIVING, false));
			}
		});
		asyncTask.execute();
	}
	
	private void drawRoute(String json) {
		addMarkers();
		if(json != null) {
			Directions directions = new Directions(getActivity());
			try {
				List<Route> routes = directions.parse(json);
				for(Route route : routes) {
					
					PolylineOptions rectLineRed = new PolylineOptions().width(3).color(Color.RED);
					
					for(Leg leg : route.getLegs()) {
						for(Step step : leg.getSteps()) {
							
							rectLineRed.add(step.getStartLocation());
							for(LatLng latlng : step.getPoints()) {
								rectLineRed.add(latlng);
							}
							rectLineRed.add(step.getEndLocation());
						}
						googleMap.addPolyline(rectLineRed);
					}
				}
				if(routes != null && routes.size() == 0) {
					Toast.makeText(
							getActivity(), 
							"We cannot route your current location. Maybe you are too far.", 
							Toast.LENGTH_SHORT).show();
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void addMarkers() {
		if(googleMap != null)
			googleMap.clear();
		
		markers = new HashMap<String, Restaurant>();
		markerList = new ArrayList<Marker>();
		for(Restaurant res : restaurantList) {
			try {
				if( res.lat == null || res.lon == null )
					continue;
				
				if( res.lat.length() == 0 || res.lon.length() == 0 )
					continue;
				
				if( Double.parseDouble(res.lat) == 0 || Double.parseDouble(res.lon) == 0 )
					continue;
			}
			catch(Exception e) { }
			
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.title(res.name);
			markerOptions.snippet(res.address);
			
			markerOptions.position(
					new LatLng(
							Double.parseDouble(res.lat), 
							Double.parseDouble(res.lon)));
			
			markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin));
			Marker mark = googleMap.addMarker(markerOptions);
			
			markerList.add(mark);
			markers.put(mark.getId(), res);
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		selectedMarker = marker;
		return false;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity());
		if (resultCode == ConnectionResult.SUCCESS) {
			Log.e("onResume", "GooglePlayServicesUtil SUCCESS");
		}
		else {
			GooglePlayServicesUtil.getErrorDialog(resultCode, this.getActivity(), RQS_GooglePlayServices);
		}
	}
	
}
