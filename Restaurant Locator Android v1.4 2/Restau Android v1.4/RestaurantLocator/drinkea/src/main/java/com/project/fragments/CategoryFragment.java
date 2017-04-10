package com.project.fragments;

import java.util.ArrayList;

import com.config.Config;
import com.libraries.adapters.MGListAdapter;
import com.libraries.adapters.MGListAdapter.OnMGListAdapterAdapterListener;
import com.config.UIConfig;
import com.db.Queries;
import com.project.fragments.activity.RestaurantActivity;
import com.models.Category;
import com.project.drinkea.MainActivity;
import com.project.drinkea.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class CategoryFragment extends Fragment implements OnItemClickListener{

	private View viewInflate = null;
	private ArrayList<Category> categoryList;
    View lastViewSelected;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// save the reference of the inflated view
		if(viewInflate == null)
			viewInflate = inflater.inflate(R.layout.fragment_favorites, container, false);

        return viewInflate;
	}
	
	@Override
    public void onDestroyView()  {
        super.onDestroyView();
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
        MainActivity main = (MainActivity) getActivity();
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

	private void showList() {
		MainActivity main = (MainActivity) getActivity();
		Queries q = main.getQueries();
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
        main.hideSwipeProgress();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long resId) {
		// TODO Auto-generated method stub
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
}
