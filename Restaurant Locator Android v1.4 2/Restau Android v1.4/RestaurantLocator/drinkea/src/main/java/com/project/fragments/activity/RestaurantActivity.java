package com.project.fragments.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.config.Config;
import com.config.UIConfig;
import com.db.DbHelper;
import com.db.Queries;
import com.libraries.adapters.MGListAdapter;
import com.libraries.imageview.MGImageView;
import com.libraries.refreshlayout.SwipeRefreshActivity;
import com.libraries.utilities.MGUtilities;
import com.models.Category;
import com.models.Favorite;
import com.models.Photo;
import com.models.Restaurant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.project.drinkea.MainActivity;
import com.project.drinkea.R;
import java.util.ArrayList;

public class RestaurantActivity extends SwipeRefreshActivity implements OnItemClickListener {
	
	private ArrayList<Restaurant> arrayData;
	DisplayImageOptions options;
	private Queries q;
	private SQLiteDatabase db;
	private Category category;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		setContentView(R.layout.fragment_restaurants);
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        this.getActionBar().setIcon(R.drawable.header_logo);
        this.getActionBar().setTitle("");
		
		DbHelper dbHelper = new DbHelper(this);
		q = new Queries(db, dbHelper);
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(UIConfig.RESTAURANT_LIST_PLACEHOLDER)
		.showImageForEmptyUri(UIConfig.RESTAURANT_LIST_PLACEHOLDER)
		.showImageOnFail(UIConfig.RESTAURANT_LIST_PLACEHOLDER)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

		showSwipeProgress();
        category = (Category)this.getIntent().getSerializableExtra("category");
        if(category != null) {
            arrayData = q.getRestaurantsByCategoryId(category.category_id);
        }
        else {
            arrayData = (ArrayList<Restaurant>) this.getIntent().getSerializableExtra("searchList");
        }

        Handler h = new Handler();
        h.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                setList();
            }
        }, Config.DELAY_SHOW_ANIMATION);
	}
	
	private void setList() {
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        View v = MGUtilities.getNoResultView(this);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout.addView(
                v, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        listView.setEmptyView(v);

        MGListAdapter adapter = new MGListAdapter(
                this, arrayData.size(), R.layout.restaurant_entry);

        adapter.setOnMGListAdapterAdapterListener(new MGListAdapter.OnMGListAdapterAdapterListener() {

            @Override
            public void OnMGListAdapterAdapterCreated(MGListAdapter adapter, View v,
                                                      int position, ViewGroup viewGroup) {
                // TODO Auto-generated method stub
                Restaurant res = arrayData.get(position);
                Photo p = q.getPhotoByRestaurantId(res.restaurant_id);
                Favorite fave = q.getFavoriteByRestaurantId(res.restaurant_id);

                MGImageView imgViewThumb = (MGImageView) v.findViewById(R.id.imgViewThumb);
                imgViewThumb.setCornerRadius(UIConfig.BORDER_RADIUS);
                imgViewThumb.setBorderWidth(UIConfig.BORDER_WIDTH);
                imgViewThumb.setBorderColor(getResources().getColor(UIConfig.THEME_COLOR));
                imgViewThumb.setBorderColor(getResources().getColor(UIConfig.THEME_COLOR));

                if (p != null)
                    MainActivity.getImageLoader().displayImage(p.thumb_url, imgViewThumb, options);
                else
                    imgViewThumb.setImageResource(UIConfig.RESTAURANT_LIST_PLACEHOLDER);

                ImageView imgListFave = (ImageView) v.findViewById(R.id.imgListFave);
                ImageView imgListFeatured = (ImageView) v.findViewById(R.id.imgListFeatured);
                imgListFave.setVisibility(View.INVISIBLE);
                imgListFeatured.setVisibility(View.INVISIBLE);
                if (res.featured.contains("1"))
                    imgListFeatured.setVisibility(View.VISIBLE);

                if (fave != null)
                    imgListFave.setVisibility(View.VISIBLE);

                Spanned name = Html.fromHtml(res.name);
                TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
                tvTitle.setText(name);

                Spanned address = Html.fromHtml(res.address);
                TextView tvSubtitle = (TextView) v.findViewById(R.id.tvSubtitle);
                tvSubtitle.setText(address);
            }
        });
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        hideSwipeProgress();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long resid) {
		// TODO Auto-generated method stub
        Restaurant restaurant = arrayData.get(pos);
        Intent i = new Intent(this, DetailsActivity.class);
        i.putExtra("restaurant", restaurant);
        startActivity(i);
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
		// TODO Auto-generated method stub
		super.onDestroy();
	}
    
    
}
