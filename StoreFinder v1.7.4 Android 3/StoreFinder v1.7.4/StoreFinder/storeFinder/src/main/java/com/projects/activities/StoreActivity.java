package com.projects.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.config.Config;
import com.libraries.adapters.MGListAdapter;
import com.libraries.adapters.MGListAdapter.OnMGListAdapterAdapterListener;
import com.config.UIConfig;
import com.db.DbHelper;
import com.db.Queries;
import com.libraries.asynctask.MGAsyncTask;
import com.libraries.dataparser.DataParser;
import com.libraries.imageview.MGImageView;
import com.libraries.usersession.UserAccessSession;
import com.models.Category;
import com.models.Data;
import com.models.Favorite;
import com.models.Photo;
import com.models.Store;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.libraries.refreshlayout.SwipeRefreshActivity;
import com.libraries.utilities.MGUtilities;
import com.projects.storefinder.MainActivity;
import com.projects.storefinder.R;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class StoreActivity extends SwipeRefreshActivity implements OnItemClickListener{
	
	private ArrayList<Store> arrayData;
	DisplayImageOptions options;
	private Queries q;
	private SQLiteDatabase db;
	MGAsyncTask task;
	Category category;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		setContentView(R.layout.fragment_store);
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        this.getActionBar().setTitle("");

		this.getActionBar().setIcon(R.drawable.empty_image);
		this.getActionBar().setDisplayShowCustomEnabled(true);
		View viewActionBar = getLayoutInflater().inflate(R.layout.header, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT,
				Gravity.CENTER);

		this.getActionBar().setCustomView(viewActionBar, params);
		
		DbHelper dbHelper = new DbHelper(this);
		q = new Queries(db, dbHelper);
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(UIConfig.SLIDER_PLACEHOLDER)
		.showImageForEmptyUri(UIConfig.SLIDER_PLACEHOLDER)
		.showImageOnFail(UIConfig.SLIDER_PLACEHOLDER)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

		category = (Category)this.getIntent().getSerializableExtra("category");
		getData();
	}

	public void getData() {
		showSwipeProgress();
		task = new MGAsyncTask(this);
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
				sortData();
				showList();

				if(arrayData != null && arrayData.size() == 0) {
					MGUtilities.showNotifier(StoreActivity.this, MainActivity.offsetY);
					return;
				}
			}

			@Override
			public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				arrayData = new ArrayList<Store>();
				if(MainActivity.location != null && MGUtilities.hasConnection(StoreActivity.this)) {
					try {
						UserAccessSession accessSession = UserAccessSession.getInstance(StoreActivity.this);
						float radius = accessSession.getFilterDistance();
						if(radius == 0)
							radius = Config.DEFAULT_FILTER_DISTANCE_IN_KM;

						String strUrl = String.format("%s?api_key=%s&lat=%f&lon=%f&radius=%f&category_id=%d",
								Config.GET_STORES_JSON_URL,
								Config.API_KEY,
								MainActivity.location.getLatitude(),
								MainActivity.location.getLongitude(),
								radius,
								category.getCategory_id());

						DataParser parser = new DataParser();
						Data data = parser.getData(strUrl);
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
								UserAccessSession.getInstance(StoreActivity.this).setFilterDistanceMax(data.getMax_distance());
							}

							if(UserAccessSession.getInstance(StoreActivity.this).getFilterDistance() == 0) {
								UserAccessSession.getInstance(StoreActivity.this).setFilterDistance(data.getDefault_distance());
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
		arrayData = q.getStoresByCategoryId(category.getCategory_id());
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
	
	private void showList() {
		hideSwipeProgress();
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		MGListAdapter adapter = new MGListAdapter(
				StoreActivity.this, arrayData.size(), R.layout.store_search_entry);
		
		adapter.setOnMGListAdapterAdapterListener(new OnMGListAdapterAdapterListener() {
			
			@SuppressLint("DefaultLocale") 
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
						Intent i = new Intent(StoreActivity.this, DetailActivity.class);
						i.putExtra("store", store);
						StoreActivity.this.startActivity(i);
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
				
				float rating = 0;
				if(store.getRating_total() > 0 && store.getRating_count() > 0)
					rating = store.getRating_total() / store.getRating_count();
				
				String strRating = String.format("%.2f %s %d %s", 
						rating, 
						StoreActivity.this.getResources().getString(R.string.average_based_on),
						store.getRating_count(),
						StoreActivity.this.getResources().getString(R.string.rating));
				
				RatingBar ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
				ratingBar.setRating(rating);
				
				TextView tvRatingBarInfo = (TextView) v.findViewById(R.id.tvRatingBarInfo);
				if(rating > 0)
					tvRatingBarInfo.setText(strRating);
				else
					tvRatingBarInfo.setText(
							StoreActivity.this.getResources().getString(R.string.no_rating));

				Favorite fave = q.getFavoriteByStoreId(store.getStore_id());
				ImageView imgViewFeatured = (ImageView) v.findViewById(R.id.imgViewFeatured);
				imgViewFeatured.setVisibility(View.INVISIBLE);
				
				ImageView imgViewStarred = (ImageView) v.findViewById(R.id.imgViewStarred);
				imgViewStarred.setVisibility(View.INVISIBLE);
				
				if(fave != null)
					imgViewStarred.setVisibility(View.VISIBLE);
				
				if(store.getFeatured() == 1)
					imgViewFeatured.setVisibility(View.VISIBLE);

				TextView tvDistance = (TextView) v.findViewById(R.id.tvDistance);
				tvDistance.setVisibility(View.INVISIBLE);
				if(MGUtilities.isLocationEnabled(StoreActivity.this) && MainActivity.location != null) {
					if(store.getDistance() != -1) {
						tvDistance.setVisibility(View.VISIBLE);
						double km = store.getDistance();
						String format = String.format(
								"%.2f %s",
								km,
								MGUtilities.getStringFromResource(StoreActivity.this, R.string.km));
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
	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long resid) {
		// TODO Auto-generated method stub
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        // Handle action bar actions click
        switch (item.getItemId()) {
	        default:
	        	finish();	
	            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        // if nav drawer is opened, hide the action items
        return super.onPrepareOptionsMenu(menu);
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(task != null)
			task.cancel(true);
	}
}
