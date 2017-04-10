package com.projects.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.libraries.adapters.MGListAdapter;
import com.libraries.adapters.MGListAdapter.OnMGListAdapterAdapterListener;
import com.libraries.asynctask.MGAsyncTask;
import com.libraries.asynctask.MGAsyncTask.OnMGAsyncTaskListener;
import com.config.Config;
import com.config.UIConfig;
import com.libraries.dataparser.DataParser;
import com.db.Queries;
import com.libraries.usersession.UserAccessSession;
import com.projects.activities.DetailActivity;
import com.projects.activities.NewsDetailActivity;
import com.libraries.helpers.DateTimeHelper;
import com.libraries.imageview.MGImageView;
import com.models.Category;
import com.models.Data;
import com.models.DataNews;
import com.models.News;
import com.models.Photo;
import com.models.Store;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.projects.storefinder.MainActivity;
import com.projects.storefinder.R;
import com.libraries.slider.MGSlider;
import com.libraries.slider.MGSlider.OnMGSliderListener;
import com.libraries.slider.MGSliderAdapter;
import com.libraries.slider.MGSliderAdapter.OnMGSliderAdapterListener;
import com.libraries.utilities.MGUtilities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HomeFragment extends Fragment implements OnItemClickListener, OnClickListener, MainActivity.OnLocationListener {
	
	private View viewInflate;
	DisplayImageOptions options;
	ArrayList<Store> storeList;
	ArrayList<News> newsList;
	MGAsyncTask task;
    Queries q;

	public HomeFragment() { }
	
	@SuppressLint("InflateParams") 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		viewInflate = inflater.inflate(R.layout.fragment_home, null);
		return viewInflate;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);		
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(task != null)
			task.cancel(true);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(UIConfig.SLIDER_PLACEHOLDER)
			.showImageForEmptyUri(UIConfig.SLIDER_PLACEHOLDER)
			.showImageOnFail(UIConfig.SLIDER_PLACEHOLDER)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();

        storeList = new ArrayList<Store>();
        newsList = new ArrayList<News>();

		MainActivity main = (MainActivity) getActivity();
		main.showSwipeProgress();
		main.getDebugKey();

        q = main.getQueries();
		if(MGUtilities.isLocationEnabled(getActivity())) {
			if(MainActivity.location == null) {
				main.setOnLocationListener(this);
			}
			else {
				Handler h = new Handler();
				h.postDelayed(new Runnable() {

					@Override
					public void run() {
						getData();
					}
				}, Config.DELAY_SHOW_ANIMATION);
			}
		}
		else {
            main.hideSwipeProgress();
			MGUtilities.showAlertView(getActivity(), R.string.location_error, R.string.gps_not_on);
		}
	}

	public void getData() {
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

				if(!MGUtilities.hasConnection(getActivity())) {
                    ArrayList<Store> stores = q.getStoresFeatured();
                    ArrayList<News> news = q.getNews();
                    if (Config.HOME_STORE_FEATURED_COUNT != -1 && Config.RANK_STORES_ACCORDING_TO_NEARBY) {
                        int storeCount = stores.size() < Config.HOME_STORE_FEATURED_COUNT ?
                                stores.size() : Config.HOME_STORE_FEATURED_COUNT;

                        if (MainActivity.location != null) {
                            for (Store store : stores) {
                                Location locStore = new Location("Store");
                                locStore.setLatitude(store.getLat());
                                locStore.setLongitude(store.getLon());
                                double userDistanceFromStore = MainActivity.location.distanceTo(locStore) / 1000;
                                store.setDistance(userDistanceFromStore);
                            }

                            Collections.sort(stores, new Comparator<Store>() {
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
                        storeList = new ArrayList<Store>();
                        for (int x = 0; x < storeCount; x++) {
                            storeList.add(stores.get(x));
                        }
                    } else {
                        storeList = stores;
                    }

                    if (Config.HOME_NEWS_COUNT != -1 && Config.RANK_STORES_ACCORDING_TO_NEARBY) {
                        int newsCount = news.size() < Config.HOME_NEWS_COUNT ? news.size() : Config.HOME_NEWS_COUNT;
                        newsList = new ArrayList<News>();
                        for (int x = 0; x < newsCount; x++) {
                            newsList.add(news.get(x));
                        }
                    } else {
                        newsList = news;
                    }
                }

				createSlider();
				showList();

                MainActivity main = (MainActivity) getActivity();
				main.hideSwipeProgress();
			}

			@Override
			public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				if( MGUtilities.hasConnection(getActivity()) &&MainActivity.location != null) {
					try {
                        UserAccessSession accessSession = UserAccessSession.getInstance(getActivity());
                        String strUrl = "";

						if(accessSession.getFilterDistance() == 0) {
							strUrl = String.format("%s?api_key=%s&lat=%f&lon=%f&radius=%f&news_count=%d&featured_count=%d&default_store_count_to_find_distance=%d",
									Config.GET_HOME_STORES_NEWS_JSON_URL,
									Config.API_KEY,
									MainActivity.location.getLatitude(),
									MainActivity.location.getLongitude(),
									accessSession.getFilterDistance(),
									Config.HOME_NEWS_COUNT,
									Config.HOME_FEATURED_COUNT,
									Config.DEFAULT_STORE_COUNT_TO_FIND_DISTANCE);
						}
						else {
							strUrl = String.format("%s?api_key=%s&lat=%f&lon=%f&radius=%f&news_count=%d&default_store_count_to_find_distance=%d",
									Config.GET_HOME_STORES_NEWS_JSON_URL,
									Config.API_KEY,
									MainActivity.location.getLatitude(),
									MainActivity.location.getLongitude(),
									accessSession.getFilterDistance(),
									Config.HOME_NEWS_COUNT,
									Config.DEFAULT_STORE_COUNT_TO_FIND_DISTANCE);
						}

						DataParser parser = new DataParser();
						Data data = parser.getData(strUrl);
						MainActivity main = (MainActivity) getActivity();
						if (main == null)
							return;

						Queries q = main.getQueries();
						if (data == null)
							return;

						if(Config.AUTO_ADJUST_DISTANCE) {
							if(data.getMax_distance() > 0) {
								UserAccessSession.getInstance(getActivity()).setFilterDistanceMax(data.getMax_distance());
							}

							if(UserAccessSession.getInstance(getActivity()).getFilterDistance() == 0) {
								UserAccessSession.getInstance(getActivity()).setFilterDistance(data.getDefault_distance());
							}
						}

						if (data.getStores() != null && data.getStores().size() > 0) {
							for (Store store : data.getStores()) {
                                q.deleteStore(store.getStore_id());
								q.insertStore(store);
                                storeList.add(store);
                                if (store.getPhotos() != null && store.getPhotos().size() > 0) {
                                    for (Photo photo : store.getPhotos()) {
                                        q.deletePhoto(photo.getPhoto_id());
                                        q.insertPhoto(photo);
                                    }
                                }
							}
						}

                        if (data.getNews() != null && data.getNews().size() > 0) {
                            for (News news : data.getNews()) {
                                q.deleteNews(news.getNews_id());
                                q.insertNews(news);
                                newsList.add(news);
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
	
	@Override
    public void onDestroyView()  {
        super.onDestroyView();
        if (viewInflate != null) {
            ViewGroup parentViewGroup = (ViewGroup) viewInflate.getParent();
            if (parentViewGroup != null) {
            	MGSlider slider = (MGSlider) viewInflate.findViewById(R.id.slider);
        		slider.pauseSliderAnimation();
                parentViewGroup.removeAllViews();
            }
        }


        if(task != null)
            task.cancel(true);
    }
	
	private void showList() {
		ListView listView = (ListView) viewInflate.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		MGListAdapter adapter = new MGListAdapter(
				getActivity(), newsList.size(), R.layout.news_entry);
		
		adapter.setOnMGListAdapterAdapterListener(new OnMGListAdapterAdapterListener() {
			
			@Override
			public void OnMGListAdapterAdapterCreated(MGListAdapter adapter, View v,
					int position, ViewGroup viewGroup) {
				// TODO Auto-generated method stub
				News news = newsList.get(position);
				MGImageView imgViewPhoto = (MGImageView) v.findViewById(R.id.imgViewPhoto);
				imgViewPhoto.setCornerRadius(0.0f);
				imgViewPhoto.setBorderWidth(UIConfig.BORDER_WIDTH);
				imgViewPhoto.setBorderColor(getResources().getColor(UIConfig.THEME_BLACK_COLOR));
				
				if(news.getPhoto_url() != null) {
					MainActivity.getImageLoader().displayImage(news.getPhoto_url(), imgViewPhoto, options);
				}
				else {
					MainActivity.getImageLoader().displayImage(null, imgViewPhoto, options);
				}
				
				imgViewPhoto.setTag(position);
				Spanned name = Html.fromHtml(news.getNews_title());
				Spanned address = Html.fromHtml(news.getNews_content());
				
				TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
				tvTitle.setText(name);
				
				TextView tvSubtitle = (TextView) v.findViewById(R.id.tvSubtitle);
				tvSubtitle.setText(address);
				
				String date = DateTimeHelper.getStringDateFromTimeStamp(news.getCreated_at(), "MM/dd/yyyy" );
				TextView tvDate = (TextView) v.findViewById(R.id.tvDate);
				tvDate.setText(date);
			}
		});
		listView.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long resId) {
		// TODO Auto-generated method stub
		MGSlider slider = (MGSlider) viewInflate.findViewById(R.id.slider);
		slider.stopSliderAnimation();
		
		News news = newsList.get(pos);
		Intent i = new Intent(getActivity(), NewsDetailActivity.class);
		i.putExtra("news", news);
		getActivity().startActivity(i);
	}
	
	// Create Slider
	private void createSlider() {
		if(storeList != null && storeList.size() == 0 && newsList != null && newsList.size() == 0) {
			MGUtilities.showNotifier(this.getActivity(), MainActivity.offsetY, R.string.failed_data);
			return;
		}
		
		final MainActivity main = (MainActivity) getActivity();
		final Queries q = main.getQueries();
		
		MGSlider slider = (MGSlider) viewInflate.findViewById(R.id.slider);
		slider.setMaxSliderThumb(storeList.size());
    	MGSliderAdapter adapter = new MGSliderAdapter(
    			R.layout.slider_entry, storeList.size(), storeList.size());
    	
    	adapter.setOnMGSliderAdapterListener(new OnMGSliderAdapterListener() {
			
			@Override
			public void onOnMGSliderAdapterCreated(MGSliderAdapter adapter, View v,
					int position) {
				// TODO Auto-generated method stub
				final Store entry = storeList.get(position);
				Photo p = q.getPhotoByStoreId(entry.getStore_id());
				ImageView imageViewSlider = (ImageView) v.findViewById(R.id.imageViewSlider);
				if(p != null) {
					MainActivity.getImageLoader().displayImage(p.getPhoto_url(), imageViewSlider, options);
				}
				else {
					imageViewSlider.setImageResource(UIConfig.SLIDER_PLACEHOLDER);
				}
				
				imageViewSlider.setTag(position);
				imageViewSlider.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(getActivity(), DetailActivity.class);
						i.putExtra("store", entry);
						getActivity().startActivity(i);
					}
				});
				
				Spanned name = Html.fromHtml(entry.getStore_name());
				Spanned address = Html.fromHtml(entry.getStore_address());
				
				TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
				tvTitle.setText(name);
				
				TextView tvSubtitle = (TextView) v.findViewById(R.id.tvSubtitle);
				tvSubtitle.setText(address);

				TextView tvDistance = (TextView) v.findViewById(R.id.tvDistance);
				tvDistance.setVisibility(View.INVISIBLE);
				if(MGUtilities.isLocationEnabled(getActivity()) && MainActivity.location != null) {
					if(entry.getDistance() != -1) {
						tvDistance.setVisibility(View.VISIBLE);
						double miles = entry.getDistance();
						String format = String.format(
								"%.2f %s", miles,
								MGUtilities.getStringFromResource(getActivity(), R.string.km));
						tvDistance.setText(format);
					}
					else {
						tvDistance.setText(R.string.empty_distance);
					}
				}
			}
		});
    	
    	slider.setOnMGSliderListener(new OnMGSliderListener() {
			
			@Override
			public void onItemThumbSelected(MGSlider slider, ImageView[] buttonPoint,
					ImageView imgView, int pos) { }
			
			@Override
			public void onItemThumbCreated(MGSlider slider, ImageView imgView, int pos) { }
			
			
			@Override
			public void onItemPageScrolled(MGSlider slider, ImageView[] buttonPoint, int pos) { }
			
			@Override
			public void onItemMGSliderToView(MGSlider slider, int pos) { }
			
			@Override
			public void onItemMGSliderViewClick(AdapterView<?> adapterView, View v,
					int pos, long resid) { }

			@Override
			public void onAllItemThumbCreated(MGSlider slider, LinearLayout linearLayout) { }
			
		});
    	slider.setOffscreenPageLimit(storeList.size() - 1);
    	slider.setAdapter(adapter);
    	slider.setActivity(this.getActivity());
    	slider.setSliderAnimation(5000);
    	slider.resumeSliderAnimation();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		MGSlider slider = (MGSlider) viewInflate.findViewById(R.id.slider);
		slider.stopSliderAnimation();
		switch(v.getId()) { }
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MGSlider slider = (MGSlider) viewInflate.findViewById(R.id.slider);
		slider.resumeSliderAnimation();
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MGSlider slider = (MGSlider) viewInflate.findViewById(R.id.slider);
		slider.pauseSliderAnimation();
	}

	@Override
	public void onLocationChanged(Location prevLoc, Location currentLoc) {
		MainActivity main = (MainActivity) getActivity();
		main.setOnLocationListener(null);
		getData();
	}
}
