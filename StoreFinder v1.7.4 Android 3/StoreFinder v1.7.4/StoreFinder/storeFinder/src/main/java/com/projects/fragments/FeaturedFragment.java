package com.projects.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.libraries.adapters.MGListAdapter;
import com.libraries.adapters.MGListAdapter.OnMGListAdapterAdapterListener;
import com.config.Config;
import com.config.UIConfig;
import com.db.Queries;
import com.libraries.asynctask.MGAsyncTask;
import com.libraries.dataparser.DataParser;
import com.libraries.slider.MGSlider;
import com.libraries.usersession.UserAccessSession;
import com.models.Data;
import com.models.News;
import com.projects.activities.DetailActivity;
import com.libraries.imageview.MGImageView;
import com.models.Favorite;
import com.models.Photo;
import com.models.Store;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.libraries.utilities.MGUtilities;
import com.projects.storefinder.MainActivity;
import com.projects.storefinder.R;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class FeaturedFragment extends Fragment implements OnItemClickListener, OnClickListener{
	
	private View viewInflate;
	private ArrayList<Store> arrayData;
	DisplayImageOptions options;
	private MGAsyncTask task;
	Queries q;
	
	public FeaturedFragment() { }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		viewInflate = inflater.inflate(R.layout.fragment_category, null);
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
        arrayData = new ArrayList<Store>();

		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(UIConfig.SLIDER_PLACEHOLDER)
			.showImageForEmptyUri(UIConfig.SLIDER_PLACEHOLDER)
			.showImageOnFail(UIConfig.SLIDER_PLACEHOLDER)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();

		if(MGUtilities.isLocationEnabled(getActivity())) {
			Handler h = new Handler();
			h.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					getData();

				}
			}, Config.DELAY_SHOW_ANIMATION);
			main.showSwipeProgress();
		}
		else {
            main.hideSwipeProgress();
			MGUtilities.showAlertView(getActivity(), R.string.location_error, R.string.gps_not_on);
		}
	}

	private void showList() {
		MainActivity main = (MainActivity) this.getActivity();
		main.hideSwipeProgress();
		if(arrayData != null && arrayData.size() == 0) {
			MGUtilities.showNotifier(this.getActivity(), MainActivity.offsetY);
			return;
		}

		ListView listView = (ListView) viewInflate.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		listView.setFocusable(false);
		MGListAdapter adapter = new MGListAdapter(
				getActivity(), arrayData.size(), R.layout.store_search_entry);
		
		adapter.setOnMGListAdapterAdapterListener(new OnMGListAdapterAdapterListener() {
			
			@Override
			public void OnMGListAdapterAdapterCreated(MGListAdapter adapter, View v,
					int position, ViewGroup viewGroup) {
				// TODO Auto-generated method stub
				final Store store = arrayData.get(position);
				Photo p = q.getPhotoByStoreId(store.getStore_id());
				MGImageView imgViewPhoto = (MGImageView) v.findViewById(R.id.imgViewPhoto);
				imgViewPhoto.setCornerRadius(0.0f);
				imgViewPhoto.setBorderWidth(UIConfig.BORDER_WIDTH);
				imgViewPhoto.setBorderColor(getResources().getColor(UIConfig.THEME_BLACK_COLOR));
				imgViewPhoto.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent i = new Intent(getActivity(), DetailActivity.class);
						i.putExtra("store", store);
						getActivity().startActivity(i);
					}
				});
				
				if(p != null) {
					MainActivity.getImageLoader().displayImage(p.getPhoto_url(), imgViewPhoto, options);
				}
				else {
					imgViewPhoto.setImageResource(UIConfig.SLIDER_PLACEHOLDER);
				}
				
				Spanned name = Html.fromHtml(store.getStore_name());
				Spanned address = Html.fromHtml(store.getStore_address());
				
				TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
				tvTitle.setText(name);
				
				TextView tvSubtitle = (TextView) v.findViewById(R.id.tvSubtitle);
				tvSubtitle.setText(address);

				// SETTING VALUES
				float rating = 0;
				
				if(store.getRating_total() > 0 && store.getRating_count() > 0)
					rating = store.getRating_total() / store.getRating_count();
				
				String strRating = String.format("%.2f %s %d %s", 
						rating, 
						getActivity().getResources().getString(R.string.average_based_on),
						store.getRating_count(),
						getActivity().getResources().getString(R.string.rating));
				
				RatingBar ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
				ratingBar.setRating(rating);
				
				TextView tvRatingBarInfo = (TextView) v.findViewById(R.id.tvRatingBarInfo);
				if(rating > 0)
					tvRatingBarInfo.setText(strRating);
				else
					tvRatingBarInfo.setText(
							getActivity().getResources().getString(R.string.no_rating));

				Favorite fave = q.getFavoriteByStoreId(store.getStore_id());
				
				ImageView imgViewFeatured = (ImageView) v.findViewById(R.id.imgViewFeatured);
				imgViewFeatured.setVisibility(View.VISIBLE);
				
				ImageView imgViewStarred = (ImageView) v.findViewById(R.id.imgViewStarred);
				imgViewStarred.setVisibility(View.VISIBLE);
				
				if(fave == null)
					imgViewStarred.setVisibility(View.INVISIBLE);
				
				if(store.getFeatured() == 0)
					imgViewFeatured.setVisibility(View.INVISIBLE);

                TextView tvDistance = (TextView) v.findViewById(R.id.tvDistance);
                tvDistance.setVisibility(View.INVISIBLE);

				if(MGUtilities.isLocationEnabled(getActivity()) && MainActivity.location != null) {
					if(store.getDistance() != -1) {
						tvDistance.setVisibility(View.VISIBLE);
						double km = store.getDistance();
						String format = String.format(
								"%.2f %s",
								km,
								MGUtilities.getStringFromResource(getActivity(), R.string.km));
						tvDistance.setText(format);
					}
					else {
						tvDistance.setText(R.string.empty_distance);
					}
				}
			}
		});
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long resId) { }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	public void getData() {
		task = new MGAsyncTask(getActivity());
		task.setMGAsyncTaskListener(new MGAsyncTask.OnMGAsyncTaskListener() {

			@Override
			public void onAsyncTaskProgressUpdate(MGAsyncTask asyncTask) { }

			@Override
			public void onAsyncTaskPreExecute(MGAsyncTask asyncTask) {
				asyncTask.dialog.hide();
			}

			@Override
			public void onAsyncTaskPostExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				showList();
				MainActivity main = (MainActivity) getActivity();
				main.hideSwipeProgress();

                if(arrayData != null && arrayData.size() == 0) {
                    MGUtilities.showNotifier(getActivity(), MainActivity.offsetY);
                    return;
                }
			}

			@Override
			public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				if( MGUtilities.hasConnection(getActivity()) && MainActivity.location != null) {
					try {
						UserAccessSession accessSession = UserAccessSession.getInstance(getActivity());

						float radius = accessSession.getFilterDistance();
						if(radius == 0)
							radius = Config.DEFAULT_FILTER_DISTANCE_IN_KM;;

						String strUrl = String.format("%s?api_key=%s&lat=%f&lon=%f&radius=%f&featured=1",
								Config.GET_STORES_JSON_URL,
								Config.API_KEY,
								MainActivity.location.getLatitude(),
								MainActivity.location.getLongitude(),
								radius);

						Log.e("URL", strUrl);
						DataParser parser = new DataParser();
						Data data = parser.getData(strUrl);
						MainActivity main = (MainActivity) getActivity();
						if (main == null)
							return;

						if (data == null)
							return;

						if (data.getStores() != null && data.getStores().size() > 0) {
							for (Store store : data.getStores()) {
								q.deleteStore(store.getStore_id());
								q.insertStore(store);
								arrayData.add(store);
								if (store.getPhotos() != null && store.getPhotos().size() > 0) {
									for (Photo photo : store.getPhotos()) {
										q.deletePhoto(photo.getPhoto_id());
										q.insertPhoto(photo);
									}
								}
							}
						}
						if(Config.AUTO_ADJUST_DISTANCE) {
							if(data.getMax_distance() > 0) {
								UserAccessSession.getInstance(getActivity()).setFilterDistanceMax(data.getMax_distance());
							}

							if(UserAccessSession.getInstance(getActivity()).getFilterDistance() == 0) {
								UserAccessSession.getInstance(getActivity()).setFilterDistance(data.getDefault_distance());
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else {
					sortData();
				}
			}
		});
		task.execute();
	}

	private void sortData() {
		MainActivity main = (MainActivity) this.getActivity();
		final Queries q = main.getQueries();
		arrayData = q.getStoresFeatured();
		if(MainActivity.location != null && Config.RANK_STORES_ACCORDING_TO_NEARBY) {
			for(Store store : arrayData) {
				Location locStore = new Location("Store");
				locStore.setLatitude(store.getLat());
				locStore.setLongitude(store.getLon());
				double userDistanceFromStore = MainActivity.location.distanceTo(locStore) / 1000;
				store.setDistance(userDistanceFromStore);
			}
			Collections.sort(arrayData, new Comparator<Store>() {
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
	}

	@Override
	public void onDestroyView()  {
		super.onDestroyView();
		if(task != null)
			task.cancel(true);

		if (viewInflate != null) {
			ViewGroup parentViewGroup = (ViewGroup) viewInflate.getParent();
			if (parentViewGroup != null) {
				parentViewGroup.removeAllViews();
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(task != null)
			task.cancel(true);
	}
}
