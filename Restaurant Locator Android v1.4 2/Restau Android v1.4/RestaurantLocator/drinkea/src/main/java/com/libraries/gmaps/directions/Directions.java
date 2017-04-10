package com.libraries.gmaps.directions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;

public class Directions {
	
	Context context;
	private String ROUTES = "routes";
	private String SUMMARY = "summary";
	private String LEGS = "legs";
	private String DISTANCE = "distance";
	private String TEXT = "text";
	
	private String STEPS = "steps";
	private String DURATION = "duration";
	private String END_LOCATION = "end_location";
	private String LATITUDE = "lat";
	private String HTML_INSTRUCTION = "html_instructions";
	private String POLYLINE = "polyline";
	private String POINTS = "points";
	
	private String START_LOCATION = "start_location";
	private String LONGITUDE = "lng";

	private String VALUE = "value";
	
	public Directions(Context context) {
		this.context = context;
	}
	
	
	public String convertStreamToString(final InputStream input) throws Exception {
	    try {
	        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	        final StringBuffer sBuf = new StringBuffer();
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	            sBuf.append(line);
	        }
	        return sBuf.toString();
	    } catch (Exception e) {
	        throw e;
	    } finally {
	        try {
	            input.close();
	        } catch (Exception e) {
	            throw e;
	        }
	    }
	}
	
	public List<Route> parse(String routesJSONString) throws Exception {
	    try {
	        List<Route> routeList = new ArrayList<Route>();
	        
	        final JSONObject jSONObject = new JSONObject(routesJSONString);
	        JSONArray routeJSONArray = jSONObject.getJSONArray(ROUTES);
	        Route route;
	        JSONObject routesJSONObject;
	        for (int m = 0; m < routeJSONArray.length(); m++) {
	            route = new Route(context);
	            routesJSONObject = routeJSONArray.getJSONObject(m);
	            JSONArray legsJSONArray;
	            route.setSummary(routesJSONObject.getString(SUMMARY));
	            legsJSONArray = routesJSONObject.getJSONArray(LEGS);
	            JSONObject legJSONObject;
	            Leg leg;
	            JSONArray stepsJSONArray;
	            for (int b = 0; b < legsJSONArray.length(); b++) {
	                leg = new Leg();
	                legJSONObject = legsJSONArray.getJSONObject(b);
	                leg.setDistance(new Distance(legJSONObject.optJSONObject(DISTANCE).optString(TEXT), legJSONObject.optJSONObject(DISTANCE).optLong(VALUE)));
	                leg.setDuration(new Duration(legJSONObject.optJSONObject(DURATION).optString(TEXT), legJSONObject.optJSONObject(DURATION).optLong(VALUE)));
	                stepsJSONArray = legJSONObject.getJSONArray(STEPS);
	                JSONObject stepJSONObject, stepDurationJSONObject, legPolyLineJSONObject, stepStartLocationJSONObject, stepEndLocationJSONObject;
	                Step step;
	                String encodedString;
	                LatLng stepStartLocationLatLng, stepEndLocationLatLng;
	                for (int i = 0; i < stepsJSONArray.length(); i++) {
	                    stepJSONObject = stepsJSONArray.getJSONObject(i);
	                    step = new Step();
	                    JSONObject stepDistanceJSONObject = stepJSONObject.getJSONObject(DISTANCE);
	                    step.setDistance(new Distance(stepDistanceJSONObject.getString(TEXT), stepDistanceJSONObject.getLong(VALUE)));
	                    stepDurationJSONObject = stepJSONObject.getJSONObject(DURATION);
	                    step.setDuration(new Duration(stepDurationJSONObject.getString(TEXT), stepDurationJSONObject.getLong(VALUE)));
	                    stepEndLocationJSONObject = stepJSONObject.getJSONObject(END_LOCATION);
	                    stepEndLocationLatLng = new LatLng(stepEndLocationJSONObject.getDouble(LATITUDE), stepEndLocationJSONObject.getDouble(LONGITUDE));
	                    step.setEndLocation(stepEndLocationLatLng);
	                    step.setHtmlInstructions(stepJSONObject.getString(HTML_INSTRUCTION));
	                    legPolyLineJSONObject = stepJSONObject.getJSONObject(POLYLINE);
	                    encodedString = legPolyLineJSONObject.getString(POINTS);
	                    step.setPoints(decodePolyLines(encodedString));
	                    
	                    stepStartLocationJSONObject = stepJSONObject.getJSONObject(START_LOCATION);
	                    stepStartLocationLatLng = new LatLng(stepStartLocationJSONObject.getDouble(LATITUDE), stepStartLocationJSONObject.getDouble(LONGITUDE));
	                    step.setStartLocation(stepStartLocationLatLng);
	                    leg.addStep(step);
	                }
	                route.addLeg(leg);
	            }
	            routeList.add(route);
	        }
	        return routeList;
	    } catch (Exception e) {
	        throw e;
	    }
	    
	}
	
	private ArrayList<LatLng> decodePolyLines(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }
	    
}
