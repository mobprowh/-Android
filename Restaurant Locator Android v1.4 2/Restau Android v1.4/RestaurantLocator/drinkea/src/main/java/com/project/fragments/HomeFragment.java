package com.project.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.config.Config;
import com.config.UIConfig;
import com.db.Queries;
import com.fasterxml.jackson.databind.JsonNode;
import com.libraries.adapters.MGListAdapter;
import com.libraries.adapters.MGListAdapter.OnMGListAdapterAdapterListener;
import com.libraries.asynctask.MGAsyncTask;
import com.libraries.asynctask.MGAsyncTask.OnMGAsyncTaskListener;
import com.libraries.dataparser.DataParser;
import com.libraries.slider.MGSlider;
import com.libraries.slider.MGSlider.OnMGSliderListener;
import com.libraries.slider.MGSliderAdapter;
import com.libraries.slider.MGSliderAdapter.OnMGSliderAdapterListener;
import com.libraries.utilities.MGUtilities;
import com.models.Category;
import com.models.Photo;
import com.models.Restaurant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.project.fragments.activity.DetailsActivity;
import com.project.fragments.activity.RestaurantActivity;
import com.project.drinkea.MainActivity;
import com.project.drinkea.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements OnItemClickListener{

	private View viewInflate = null;
	private ArrayList<Category> categoryList;
	private ArrayList<Restaurant> restaurantList;
	DisplayImageOptions options;
    View lastViewSelected;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(viewInflate == null)
			viewInflate = inflater.inflate(R.layout.fragment_categories, container, false);

        return viewInflate;
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
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		// this is really important in order to save the state across screen
		// configuration changes for example
		setRetainInstance(true);
		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(UIConfig.SLIDER_PLACEHOLDER)
			.showImageForEmptyUri(UIConfig.SLIDER_PLACEHOLDER)
			.showImageOnFail(UIConfig.SLIDER_PLACEHOLDER)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();

		MainActivity main = (MainActivity) getActivity();
		if(MGUtilities.hasConnection(getActivity()) && Config.WILL_DOWNLOAD_DATA ) {
			main.showSwipeProgress();
			downloadData();
		}
		else if(main.getQueries().getRestaurants().size() == 0) {
        	Toast.makeText(
        			getActivity(), 
        			"Cannot Fetch data. No Network Connection.", 
        			Toast.LENGTH_SHORT).show();
        }
        else {
        	showList();
        	createSlider();
        }
	}

	private void downloadData() {
		MGAsyncTask asyncTask = new MGAsyncTask(getActivity());
		asyncTask.setMGAsyncTaskListener(new OnMGAsyncTaskListener() {

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
				showList();
				createSlider();
			}

			@Override
			public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				try {
					DataParser parser = new DataParser();
					ArrayList<Object> objList = new ArrayList<Object>();
					if(Config.WILL_USE_JSON_FORMAT) {
						JsonNode node = parser.getJsonRootNode(Config.DATA_JSON_URL);
						objList = parser.getJSON(node);
					}
					else if(!Config.WILL_USE_JSON_FORMAT) {
						objList = parser.getXML(Config.DATA_XML_URL);
					}
					insertToDb(objList);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		asyncTask.execute();
	}
	
	private void insertToDb(ArrayList<Object> objList) {
		MainActivity main = (MainActivity) getActivity();
		Queries q = main.getQueries();
		q.deleteTable("restaurants");
		q.deleteTable("categories");
		q.deleteTable("photos");
		if(objList != null) {
			for(Object obj : objList) {
				if(obj instanceof Restaurant)
					q.insertRestaurant( ((Restaurant) obj) );
				
				if(obj instanceof Photo)
					q.insertPhoto( ((Photo) obj) );
				
				if(obj instanceof Category)
					q.insertCategory( ((Category) obj) );
			}
		}
	}
	
	private void showList() {
		MainActivity main = (MainActivity) getActivity();
		Queries q = main.getQueries();
		
		restaurantList = q.getFeaturedRestaurants();
		categoryList = q.getCategories();

		ListView listView = (ListView) viewInflate.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		MGListAdapter adapter = new MGListAdapter(
				getActivity(), categoryList.size(), R.layout.category_entry);
		
		adapter.setOnMGListAdapterAdapterListener(new OnMGListAdapterAdapterListener() {

			@Override
			public void OnMGListAdapterAdapterCreated(MGListAdapter adapter, View v,
													  int position, ViewGroup viewGroup) {
				// TODO Auto-generated method stub
				Category cat = categoryList.get(position);
				Spanned category = Html.fromHtml(cat.category);
				TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
				tvTitle.setText(category);
			}
		});
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long resId) {
		// TODO Auto-generated method stub
		MGSlider slider = (MGSlider) viewInflate.findViewById(R.id.slider);
		slider.stopSliderAnimation();
		
		TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
		tvTitle.setTextColor(getResources().getColor(UIConfig.THEME_COLOR));
		ImageView imgArrow = (ImageView) v.findViewById(R.id.imgArrow);
		imgArrow.setImageResource(UIConfig.LIST_ARROW_SELECTED);

        if(lastViewSelected != null) {
            tvTitle = (TextView) lastViewSelected.findViewById(R.id.tvTitle);
            tvTitle.setTextColor(getResources().getColor(UIConfig.DEFAULT_CATEGORY_COLOR));
            imgArrow = (ImageView) lastViewSelected.findViewById(R.id.imgArrow);
            imgArrow.setImageResource(UIConfig.LIST_ARROW_NORMAL);
        }
        lastViewSelected = v;

        Category cat = categoryList.get(pos);
        Intent i = new Intent(getActivity(), RestaurantActivity.class);
        i.putExtra("category", cat);
        startActivity(i);
	}
	
	// Create Slider
	private void createSlider() {
		MainActivity main = (MainActivity) getActivity();
		final Queries q = main.getQueries();
		MGSlider slider = (MGSlider) viewInflate.findViewById(R.id.slider);
		slider.setMaxSliderThumb(restaurantList.size());
    	MGSliderAdapter adapter = new MGSliderAdapter(
    			R.layout.slider_entry, restaurantList.size(), restaurantList.size());
    	
    	adapter.setOnMGSliderAdapterListener(new OnMGSliderAdapterListener() {
			
			@Override
			public void onOnMGSliderAdapterCreated(MGSliderAdapter adapter, View v,
					int position) {
				// TODO Auto-generated method stub
				Restaurant res = restaurantList.get(position);
				Photo p = q.getPhotoByRestaurantId(res.restaurant_id);
				ImageView imageViewSlider = (ImageView) v.findViewById(R.id.imageViewSlider);
				if(p != null) {
					MainActivity main = (MainActivity)getActivity();
					main.getImageLoader().displayImage(p.photo_url, imageViewSlider, options);
				}
				else {
					imageViewSlider.setImageResource(R.drawable.placeholder);
				}
				
				imageViewSlider.setTag(position);
                imageViewSlider.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = Integer.parseInt(view.getTag().toString());
                        Restaurant restaurant = restaurantList.get(pos);
                        Intent i = new Intent(getActivity(), DetailsActivity.class);
                        i.putExtra("restaurant", restaurant);
                        startActivity(i);
                    }
                });
				Spanned name = Html.fromHtml(res.name);
				Spanned address = Html.fromHtml(res.address);
				
				TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
				tvTitle.setText(name);
				
				TextView tvSubtitle = (TextView) v.findViewById(R.id.tvSubtitle);
				tvSubtitle.setText(address);
			}
		});

    	slider.setOnMGSliderListener(new OnMGSliderListener() {

			@Override
			public void onItemThumbSelected(MGSlider slider, ImageView[] buttonPoint,
					ImageView imgView, int pos) { }

			@Override
			public void onItemThumbCreated(MGSlider slider, ImageView imgView, int pos) { }

			@Override
			public void onAllItemThumbCreated(MGSlider slider, LinearLayout linearLayout) { }

			@Override
			public void onItemPageScrolled(MGSlider slider, ImageView[] buttonPoint, int pos) { }

			@Override
			public void onItemMGSliderToView(MGSlider slider, int pos) { }

			@Override
			public void onItemMGSliderViewClick(AdapterView<?> adapterView, View v,
					int pos, long resid) {
            }

		});

    	slider.setOffscreenPageLimit(restaurantList.size() - 1);
    	slider.setAdapter(adapter);
    	slider.setActivity(this.getActivity());
    	slider.setSliderAnimation(5000);
    	slider.resumeSliderAnimation();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MGSlider slider = (MGSlider) viewInflate.findViewById(R.id.slider);
		slider.resumeSliderAnimation();
		Log.e("RESUME STATE", "RESUME STATE");
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MGSlider slider = (MGSlider) viewInflate.findViewById(R.id.slider);
		slider.pauseSliderAnimation();
		Log.e("PAUSE STATE", "PAUSE STATE");
	}
}
