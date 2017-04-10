package com.projects.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.libraries.asynctask.MGAsyncTask;
import com.libraries.asynctask.MGAsyncTask.OnMGAsyncTaskListener;
import com.config.Config;
import com.db.Queries;
import com.libraries.dataparser.DataParser;
import com.libraries.usersession.UserAccessSession;
import com.models.Data;
import com.models.News;
import com.models.Photo;
import com.projects.activities.SearchResultActivity;
import com.models.Category;
import com.models.Store;
import com.projects.storefinder.MainActivity;
import com.projects.storefinder.R;
import com.projects.storefinder.MainActivity.OnLocationListener;
import com.libraries.utilities.MGUtilities;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SearchFragment extends Fragment implements OnClickListener, OnLocationListener{
	
	private View viewInflate;
	private EditText txtKeywords;
	private SeekBar seekbarRadius;
	private Spinner spinnerCategories;
	private ToggleButton toggleButtonNearby;
	private MGAsyncTask task;
    Queries q;
	
	public SearchFragment() { }
	
	@Override
    public void onDestroyView()  {
        super.onDestroyView();
        if(task != null)
        	task.cancel(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		viewInflate = inflater.inflate(R.layout.fragment_search, null);
		return viewInflate;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		MainActivity main = (MainActivity) this.getActivity();
		q = main.getQueries();

		Button btnSearch = (Button) viewInflate.findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(this);

		txtKeywords = (EditText) viewInflate.findViewById(R.id.txtKeywords);
		toggleButtonNearby = (ToggleButton) viewInflate.findViewById(R.id.toggleButtonNearby);

		toggleButtonNearby.setOnClickListener(this);
		final TextView tvRadiusText = (TextView) viewInflate.findViewById(R.id.tvRadiusText);
		seekbarRadius = (SeekBar) viewInflate.findViewById(R.id.seekbarRadius);
		seekbarRadius.setMax(Config.MAX_SEARCH_RADIUS);
		seekbarRadius.setProgress(Config.MAX_SEARCH_RADIUS / 3);
		seekbarRadius.setEnabled(false);
		seekbarRadius.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) { }
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) { }
			
			@SuppressLint("DefaultLocale") 
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				String strSeekVal = String.format("%s: %d %s", 
						MGUtilities.getStringFromResource(getActivity(), R.string.radius), 
						progress,
						MGUtilities.getStringFromResource(getActivity(), R.string.km));
				
				tvRadiusText.setText(strSeekVal);
			}
		});
		ArrayList<String> categories = new ArrayList<String>();
		String allCategories = this.getActivity().getResources().getString(R.string.all_categories);
		categories.add(0, allCategories);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item, categories);
         
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCategories = (Spinner) viewInflate.findViewById(R.id.spinnerCategories);
		spinnerCategories.setAdapter(dataAdapter);

		getData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.toggleButtonNearby:
				if(toggleButtonNearby.isChecked()) {
					MainActivity main = (MainActivity) this.getActivity();
					if(MainActivity.location == null) 
						main.setOnLocationListener(this);
					
					seekbarRadius.setEnabled(true);
				}
				else {
					seekbarRadius.setEnabled(false);
				}
				break;
			case R.id.btnSearch:
				asyncSearch();
				break;
		}
	}

	private void asyncSearch() {
		task = new MGAsyncTask(getActivity());
		task.setMGAsyncTaskListener(new OnMGAsyncTaskListener() {
			
			ArrayList<Store> arrayFilter;
			
			@Override
			public void onAsyncTaskProgressUpdate(MGAsyncTask asyncTask) { }
			
			@Override
			public void onAsyncTaskPreExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				asyncTask.dialog.hide();
				MainActivity main = (MainActivity) getActivity();
				main.showSwipeProgress();
			}
			
			@Override
			public void onAsyncTaskPostExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				MainActivity main = (MainActivity) getActivity();
				main.hideSwipeProgress();
				if(arrayFilter != null && arrayFilter.size() == 0) {
					MGUtilities.showNotifier(SearchFragment.this.getActivity(), MainActivity.offsetY);
					return;
				}
				
				Intent i = new Intent(getActivity(), SearchResultActivity.class);
				i.putExtra("searchResults", arrayFilter);
				getActivity().startActivity(i);
			}
			
			@Override
			public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				arrayFilter = search();
			}
		});
		task.execute();
	}

	private ArrayList<Store> search() {
		MainActivity main = (MainActivity) this.getActivity();
		Queries q = main.getQueries();
		
		String strKeywords = txtKeywords.getText().toString().toLowerCase().trim();
	    int radius = seekbarRadius.getProgress();
	    String category = spinnerCategories.getSelectedItem().toString();
	    
	    int countParams = strKeywords.length() > 0 ? 1 : 0;
	    countParams += radius > 0 && toggleButtonNearby.isChecked() ? 1 : 0;
	    countParams += category.length() > 0 ? 1 : 0;

	    ArrayList<Store> arrayStores = q.getStores();
	    ArrayList<Store> arrayFilter = new ArrayList<Store>();
	    for(Store store : arrayStores) {
	    	int qualifyCount = 0;
	        boolean isFoundKeyword = store.getStore_name().toLowerCase().contains(strKeywords) ||
	                               store.getStore_address().toLowerCase().contains(strKeywords);
	        
	        if( strKeywords.length() > 0  && isFoundKeyword)
	            qualifyCount += 1;
	        
	        if( category.length() > 0) {
	            Category storeCat = q.getCategoryByCategory(category);
	            boolean isFoundCat = false;
	            
	            if(storeCat != null && storeCat.getCategory_id() == store.getCategory_id())
	                isFoundCat = true;
	            
	            if(spinnerCategories.getSelectedItemPosition() == 0)
	                isFoundCat = true;
	            
	            if(isFoundCat)
	                qualifyCount += 1;
	        }
	        store.setDistance(-1);
	        if(toggleButtonNearby.isChecked()) {
				if(MainActivity.location != null) {
					Location locStore = new Location("Store");
					locStore.setLatitude(store.getLat());
					locStore.setLongitude(store.getLon());
					
					double distance = locStore.distanceTo(MainActivity.location) / 1000;
					store.setDistance(distance);
					if(distance <= radius)
		                qualifyCount += 1;
				}
	        }
	        if(qualifyCount == countParams)
	        	arrayFilter.add(store);
	    }
		if(toggleButtonNearby.isChecked()) {
			Collections.sort(arrayFilter, new Comparator<Store>() {
				@Override
				public int compare(Store store, Store t1) {
					if (store.getDistance() < t1.getDistance())
						return -1;
					if (store.getDistance() > t1.getDistance())
						return 1;
					return 0;
				}
			});
		}
	    return arrayFilter;
	}

	@Override
	public void onLocationChanged(Location prevLoc, Location currentLoc) { }

	public void getData() {
		final MainActivity main = (MainActivity) getActivity();
		main.showSwipeProgress();

		task = new MGAsyncTask(getActivity());
		task.setMGAsyncTaskListener(new OnMGAsyncTaskListener() {

			@Override
			public void onAsyncTaskProgressUpdate(MGAsyncTask asyncTask) { }

			@Override
			public void onAsyncTaskPreExecute(MGAsyncTask asyncTask) {
				asyncTask.dialog.hide();
			}

			@Override
			public void onAsyncTaskPostExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
                ArrayList<String> categories = q.getCategoryNames();
                String allCategories = getActivity().getResources().getString(R.string.all_categories);
                categories.add(0, allCategories);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                        getActivity(), android.R.layout.simple_spinner_item, categories);

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategories = (Spinner) viewInflate.findViewById(R.id.spinnerCategories);
                spinnerCategories.setAdapter(dataAdapter);

				main.hideSwipeProgress();
			}

			@Override
			public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				if(MainActivity.location != null) {
					try {
						UserAccessSession accessSession = UserAccessSession.getInstance(getActivity());
						String strUrl = String.format("%s?api_key=%s&lat=%f&lon=%f&radius=%d&fetch_category=1",
								Config.GET_STORES_NEWS_JSON_URL,
								Config.API_KEY,
								MainActivity.location.getLatitude(),
								MainActivity.location.getLongitude(),
								accessSession.getFilterDistance());

						DataParser parser = new DataParser();
						Data data = parser.getData(strUrl);
						MainActivity main = (MainActivity) getActivity();
						if (main == null)
							return;

						Queries q = main.getQueries();
						if (data == null)
							return;

						if (data.getCategories() != null && data.getCategories().size() > 0) {
							for (Category cat : data.getCategories()) {
								q.deleteCategory(cat.getCategory_id());
								q.insertCategory(cat);
							}
						}

						if (data.getStores() != null && data.getStores().size() > 0) {
							for (Store store : data.getStores()) {
								q.deleteStore(store.getStore_id());
								q.insertStore(store);
								if (store.getPhotos() != null && store.getPhotos().size() > 0) {
									for (Photo photo : store.getPhotos()) {
										q.deletePhoto(photo.getPhoto_id());
										q.insertPhoto(photo);
									}
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		task.execute();
	}
}
