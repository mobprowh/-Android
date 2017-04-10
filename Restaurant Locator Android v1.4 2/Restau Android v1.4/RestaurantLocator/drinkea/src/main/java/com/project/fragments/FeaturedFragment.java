package com.project.fragments;

import java.util.ArrayList;

import com.config.Config;
import com.libraries.adapters.MGListAdapter;
import com.libraries.adapters.MGListAdapter.OnMGListAdapterAdapterListener;
import com.config.UIConfig;
import com.libraries.imageview.MGImageView;
import com.project.fragments.activity.DetailsActivity;
import com.models.Favorite;
import com.models.Photo;
import com.models.Restaurant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.project.drinkea.MainActivity;
import com.project.drinkea.R;
import com.libraries.utilities.MGUtilities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class FeaturedFragment extends Fragment implements OnItemClickListener {

	private View viewInflate = null;
	private ArrayList<Restaurant> restaurantList;
	DisplayImageOptions options;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// save the reference of the inflated view
		if(viewInflate == null)
			viewInflate = inflater.inflate(R.layout.fragment_featured, container, false);
        return viewInflate;
	}
	
	@Override
    public void onDestroyView()  {
        super.onDestroyView();
        if (viewInflate != null) {
            ViewGroup parentViewGroup = (ViewGroup) viewInflate.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
        options = new DisplayImageOptions.Builder()
			.showImageOnLoading(UIConfig.RESTAURANT_LIST_PLACEHOLDER)
			.showImageForEmptyUri(UIConfig.RESTAURANT_LIST_PLACEHOLDER)
			.showImageOnFail(UIConfig.RESTAURANT_LIST_PLACEHOLDER)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();

		final MainActivity main = (MainActivity) getActivity();
		restaurantList = main.getQueries().getFeaturedRestaurants();
		main.showSwipeProgress();

		Handler h = new Handler();
		h.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				showList();
			}
		}, Config.DELAY_SHOW_ANIMATION);

	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// this is really important in order to save the state across screen
		// configuration changes for example
		setRetainInstance(true);
	}

	private void showList() {
		final MainActivity main = (MainActivity) getActivity();
		ListView listView = (ListView) viewInflate.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		
		View v = MGUtilities.getNoResultView(getActivity());
		((ViewGroup)viewInflate.getParent()).addView(
				v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		listView.setEmptyView(v);
		MGListAdapter adapter = new MGListAdapter(
				getActivity(), restaurantList.size(), R.layout.restaurant_entry);
		
		adapter.setOnMGListAdapterAdapterListener(new OnMGListAdapterAdapterListener() {
			
			@Override
			public void OnMGListAdapterAdapterCreated(MGListAdapter adapter, View v,
					int position, ViewGroup viewGroup) {
				// TODO Auto-generated method stub
				Restaurant res = restaurantList.get(position);
				Photo p = main.getQueries().getPhotoByRestaurantId(res.restaurant_id);
				Favorite fave = main.getQueries().getFavoriteByRestaurantId(res.restaurant_id);

                MGImageView imgViewThumb = (MGImageView) v.findViewById(R.id.imgViewThumb);
                imgViewThumb.setCornerRadius(UIConfig.BORDER_RADIUS);
                imgViewThumb.setBorderWidth(UIConfig.BORDER_WIDTH);
				imgViewThumb.setBorderColor(getResources().getColor(UIConfig.THEME_COLOR));
				
				if(p != null)
					main.getImageLoader().displayImage(p.thumb_url, imgViewThumb, options);
				else
					imgViewThumb.setImageResource(UIConfig.RESTAURANT_LIST_PLACEHOLDER);
				
				ImageView imgListFave = (ImageView) v.findViewById(R.id.imgListFave);
				ImageView imgListFeatured = (ImageView) v.findViewById(R.id.imgListFeatured);
				
				imgListFave.setVisibility(View.INVISIBLE);
				imgListFeatured.setVisibility(View.INVISIBLE);
				
				if(res.featured.contains("1"))
					imgListFeatured.setVisibility(View.VISIBLE);
				
				if(fave != null)
					imgListFave.setVisibility(View.VISIBLE);
					
				Spanned name = Html.fromHtml(res.name);
				Spanned address = Html.fromHtml(res.address);
				
				TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
				tvTitle.setText(name);
				
				TextView tvSubtitle = (TextView) v.findViewById(R.id.tvSubtitle);
				tvSubtitle.setText(address);
			}
		});
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		main.hideSwipeProgress();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long resId) {
		// TODO Auto-generated method stub
		Restaurant res = restaurantList.get(pos);
		Intent i = new Intent(this.getActivity(), DetailsActivity.class);
		i.putExtra("restaurant", res);
		startActivity(i);
	}
}

