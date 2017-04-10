package com.project.fragments;

import java.util.ArrayList;
import com.libraries.adapters.MGListAdapter;
import com.libraries.adapters.MGListAdapter.OnMGListAdapterAdapterListener;
import com.config.UIConfig;
import com.libraries.imageview.RoundedImageView;
import com.models.Photo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.project.fragments.activity.ImageViewerActivity;
import com.project.drinkea.MainActivity;
import com.project.drinkea.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class GalleryFragment extends Fragment implements OnItemClickListener{

	private View viewInflate = null;
	ArrayList<Photo> photoList;
	DisplayImageOptions options;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// save the reference of the inflated view
		if(viewInflate == null)
			viewInflate = inflater.inflate(R.layout.fragment_gallery, container, false);
        return viewInflate;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// this is really important in order to save the state across screen
		// configuration changes for example
		setRetainInstance(true);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)  {
        options = new DisplayImageOptions.Builder()
			.showImageOnLoading(UIConfig.SLIDER_PLACEHOLDER)
			.showImageForEmptyUri(UIConfig.SLIDER_PLACEHOLDER)
			.showImageOnFail(UIConfig.SLIDER_PLACEHOLDER)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
        
        GridView gridView = (GridView) viewInflate.findViewById(R.id.gridView);
        gridView.setNumColumns(4);
        gridView.setOnItemClickListener(this);
        
        MainActivity main = (MainActivity) getActivity();
        main.showSwipeProgress();
        photoList = main.getQueries().getPhotos();
        
        MGListAdapter adapter = new MGListAdapter(getActivity(), photoList.size(), R.layout.gallery_entry);
        adapter.setOnMGListAdapterAdapterListener(new OnMGListAdapterAdapterListener() {
			
			@Override
			public void OnMGListAdapterAdapterCreated(MGListAdapter adapter, View v,
					int position, ViewGroup viewGroup) {
				// TODO Auto-generated method stub
				
				Photo p = photoList.get(position);
				
				String strUrl = p.thumb_url;
				if(!strUrl.contains("http")) {
					strUrl = "http://" + strUrl;
				}

				RoundedImageView imgViewThumb = (RoundedImageView) v.findViewById(R.id.imgViewThumb);
				imgViewThumb.setCornerRadius(UIConfig.BORDER_RADIUS);
				imgViewThumb.setBorderWidth(UIConfig.BORDER_WIDTH);
				imgViewThumb.setBorderColor(getResources().getColor(UIConfig.THEME_COLOR));

				MainActivity main = (MainActivity)getActivity();
				main.getImageLoader().displayImage(strUrl, imgViewThumb, options);
			}
		});
        
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        main.hideSwipeProgress();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long resid) {
		// TODO Auto-generated method stub
		Intent i = new Intent(this.getActivity(), ImageViewerActivity.class);
		i.putExtra("photoList", photoList);
		i.putExtra("index", pos);
        startActivity(i);
	}
}
