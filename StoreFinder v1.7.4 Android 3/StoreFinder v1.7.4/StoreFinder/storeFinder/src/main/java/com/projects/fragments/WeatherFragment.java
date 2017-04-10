package com.projects.fragments;

import com.libraries.asynctask.MGAsyncTask;
import com.libraries.asynctask.MGAsyncTask.OnMGAsyncTaskListener;
import com.config.Config;
import com.libraries.dataparser.DataParser;
import com.models.DataWeather;
import com.models.Weather;
import com.projects.storefinder.MainActivity;
import com.projects.storefinder.R;
import com.libraries.utilities.MGUtilities;
import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WeatherFragment extends Fragment {
	
	private View viewInflate;
	private DataWeather dataWeather;
	MGAsyncTask task ;
	
	public WeatherFragment() { }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		viewInflate = inflater.inflate(R.layout.fragment_weather, null);
		return viewInflate;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		if(task != null)
			task.cancel(true);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		if(MainActivity.location == null) {
			MGUtilities.showAlertView(
					getActivity(), 
					R.string.location_error, 
					R.string.cannot_determine_location);
			return;
		}
		
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				getData();
			}
		}, Config.DELAY_SHOW_ANIMATION);
		
		MainActivity main = (MainActivity) getActivity();
		main.showSwipeProgress();
	}

	public void getData() {
		task = new MGAsyncTask(getActivity());
		task.setMGAsyncTaskListener(new OnMGAsyncTaskListener() {
			
			@Override
			public void onAsyncTaskProgressUpdate(MGAsyncTask asyncTask) { }
			
			@Override
			public void onAsyncTaskPreExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				asyncTask.dialog.hide();
			}
			
			@Override
			public void onAsyncTaskPostExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				MainActivity main = (MainActivity) getActivity();
				main.hideSwipeProgress();
				updateView();
			}
			
			@SuppressLint("DefaultLocale") 
			@Override
			public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				Location location = MainActivity.location;
				String weatherUrl = String.format("%slat=%f&lon=%f&APPID=%s",
						Config.WEATHER_URL, 
						location.getLatitude(), 
						location.getLongitude(),
						Config.WEATHER_APP_ID);
				DataParser parser = new DataParser();
				dataWeather = parser.getDataWeather(weatherUrl);
			}
		});
		task.execute();
	}
	
	
	@SuppressLint("DefaultLocale") 
	private void updateView() {
		TextView tvFarenheit = (TextView) viewInflate.findViewById(R.id.tvFarenheit);
		TextView tvCelsius = (TextView) viewInflate.findViewById(R.id.tvCelsius);
		TextView tvAddress = (TextView) viewInflate.findViewById(R.id.tvAddress);
		TextView tvDescription = (TextView) viewInflate.findViewById(R.id.tvDescription);
		
		tvFarenheit.setText(R.string.weather_placeholder);
		tvCelsius.setText(R.string.weather_placeholder);
		tvAddress.setText(R.string.weather_placeholder);
		tvDescription.setText(R.string.weather_placeholder);

		if(dataWeather == null)
			return;
		
		if(dataWeather.getMain() != null) {
			
			double kelvin = dataWeather.getMain().getTemp();
			double celsius = kelvin - 273.15;
			double fahrenheit = (celsius * 1.8) + 32 ;
			
			String farenheitStr = String.format("%.2f %s", 
					fahrenheit, 
					MGUtilities.getStringFromResource(getActivity(), R.string.fahrenheit));
			
			String celsiusStr = String.format("%.2f %s", 
					celsius, 
					MGUtilities.getStringFromResource(getActivity(), R.string.celsius));
			
			tvFarenheit.setText(farenheitStr);
			tvCelsius.setText(celsiusStr);
		}

		if(dataWeather.getWeather() != null && dataWeather.getWeather().size() > 0) {
			Weather weather = dataWeather.getWeather().get(0);
			tvDescription.setText(weather.getDescription());
		}
		
		if(MainActivity.address != null && MainActivity.address.size() > 0) {
			Address address = MainActivity.address.get(0);
			String locality = address.getLocality();
			String countryName = address.getCountryName();
			String addressStr = String.format("%s, %s", locality, countryName);
			tvAddress.setText(addressStr);
		}
	}
}
