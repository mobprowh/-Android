package com.project.fragments.details.sub;

import java.util.ArrayList;
import com.libraries.adapters.MGListAdapter;
import com.libraries.adapters.MGListAdapter.OnMGListAdapterAdapterListener;
import com.config.UIConfig;
import com.libraries.imageview.RoundedImageView;
import com.models.Photo;
import com.models.Restaurant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.project.fragments.activity.DetailsActivity;
import com.project.fragments.activity.ImageViewerActivity;
import com.project.drinkea.MainActivity;
import com.project.drinkea.R;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class SubDetailGalleryView implements OnItemClickListener{

	private View viewInflate = null;
	DisplayImageOptions options;
	private Restaurant restaurant;
	ArrayList<Photo> photoList;
	private FragmentActivity act;
	
	public SubDetailGalleryView(FragmentActivity act) {
		// TODO Auto-generated method stub
		this.act = act;
		LayoutInflater inflater = act.getLayoutInflater();
		viewInflate = inflater.inflate(R.layout.sub_detail_gallery, null, false);
	}

	public View showGalleries(Restaurant res) {
		this.restaurant = res;
		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(UIConfig.SLIDER_PLACEHOLDER)
			.showImageForEmptyUri(UIConfig.SLIDER_PLACEHOLDER)
			.showImageOnFail(UIConfig.SLIDER_PLACEHOLDER)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
		
		GridView gridView = (GridView) viewInflate.findViewById(R.id.gridViewDetail);
        gridView.setNumColumns(4);
        gridView.setOnItemClickListener(this);

        final DetailsActivity main = (DetailsActivity)act;
        photoList = main.q.getPhotosByRestaurantId(restaurant.restaurant_id);
        MGListAdapter adapter = new MGListAdapter(act, photoList.size(), R.layout.gallery_entry);
        adapter.setOnMGListAdapterAdapterListener(new OnMGListAdapterAdapterListener() {
			
			@Override
			public void OnMGListAdapterAdapterCreated(MGListAdapter adapter, View v,
					int position, ViewGroup viewGroup) {
				// TODO Auto-generated method stub
				Photo p = photoList.get(position);
				RoundedImageView imgViewThumb = (RoundedImageView) v.findViewById(R.id.imgViewThumb);
				imgViewThumb.setCornerRadius(UIConfig.BORDER_RADIUS);
				imgViewThumb.setBorderWidth(UIConfig.BORDER_WIDTH);
				imgViewThumb.setBorderColor(act.getResources().getColor(UIConfig.THEME_COLOR));
				
				Log.e("PHOTO-URL", p.photo_url);
				MainActivity.getImageLoader().displayImage(p.thumb_url, imgViewThumb, options);
			}
		});
        
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        
        return viewInflate;
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long resid) {
		// TODO Auto-generated method stub
		Intent i = new Intent(act, ImageViewerActivity.class);
		i.putExtra("photoList", photoList);
		i.putExtra("index", pos);
		act.startActivity(i);
	}
	
	public View getView() {
		return viewInflate;
	}
}
