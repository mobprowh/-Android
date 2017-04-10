package com.projects.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.libraries.adapters.MGListAnimatedAdapter;
import com.libraries.adapters.MGListAnimatedAdapter.OnMGListAnimatedAdapter;
import com.config.Config;
import com.config.UIConfig;
import com.db.Queries;
import com.projects.activities.DetailActivity;
import com.libraries.imageview.MGImageView;
import com.models.Photo;
import com.models.Store;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.libraries.utilities.MGUtilities;
import com.projects.storefinder.MainActivity;
import com.projects.storefinder.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
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

public class FavoriteFragment extends Fragment implements OnItemClickListener, OnClickListener{
	
	private View viewInflate;
	private ArrayList<Store> arrayData;
	DisplayImageOptions options;
	
	public FavoriteFragment() { }
	
	@SuppressLint("InflateParams") 
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
		final Queries q = main.getQueries();
		
		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(UIConfig.SLIDER_PLACEHOLDER)
			.showImageForEmptyUri(UIConfig.SLIDER_PLACEHOLDER)
			.showImageOnFail(UIConfig.SLIDER_PLACEHOLDER)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
		
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				arrayData = q.getStoresFavorites();
				showList();
			}
		}, Config.DELAY_SHOW_ANIMATION);
		main.showSwipeProgress();
	}
	
	private void showList() {
		MainActivity main = (MainActivity) this.getActivity();
		main.hideSwipeProgress();
		if(arrayData != null && arrayData.size() == 0) {
			MGUtilities.showNotifier(this.getActivity(), MainActivity.offsetY);
			return;
		}

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
		
		final Queries q = main.getQueries();
		ListView listView = (ListView) viewInflate.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		MGListAnimatedAdapter adapter = new MGListAnimatedAdapter(
				getActivity(), arrayData.size(), R.layout.store_search_entry);
		
		adapter.setOnMGListAnimatedAdapter(new OnMGListAnimatedAdapter() {
			
			@SuppressLint("DefaultLocale") 
			@Override
			public void OnMGListAnimatedAdapterCreated(MGListAnimatedAdapter adapter,
					View v, int position, ViewGroup viewGroup) {
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
				
				ImageView imgViewFeatured = (ImageView) v.findViewById(R.id.imgViewFeatured);
				imgViewFeatured.setVisibility(View.INVISIBLE);
				
				ImageView imgViewStarred = (ImageView) v.findViewById(R.id.imgViewStarred);
				imgViewStarred.setVisibility(View.VISIBLE);
				
				if(store.getFeatured() == 1)
					imgViewFeatured.setVisibility(View.VISIBLE);

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
	public void onClick(View v) { }
}
