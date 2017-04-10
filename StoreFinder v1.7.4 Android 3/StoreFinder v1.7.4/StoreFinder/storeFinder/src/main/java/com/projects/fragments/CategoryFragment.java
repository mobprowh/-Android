package com.projects.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.libraries.adapters.MGListAdapter;
import com.libraries.adapters.MGListAdapter.OnMGListAdapterAdapterListener;
import com.config.Config;
import com.db.Queries;
import com.libraries.asynctask.MGAsyncTask;
import com.libraries.dataparser.DataParser;
import com.libraries.usersession.UserAccessSession;
import com.models.Data;
import com.models.News;
import com.models.Photo;
import com.models.Store;
import com.projects.activities.StoreActivity;
import com.models.Category;
import com.libraries.utilities.MGUtilities;
import com.projects.storefinder.MainActivity;
import com.projects.storefinder.R;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryFragment extends Fragment implements OnItemClickListener{
	
	private View viewInflate;
	private ArrayList<Category> categoryList;
	private Queries q;
	MGAsyncTask task;
	
	public CategoryFragment() { }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		viewInflate = inflater.inflate(R.layout.fragment_category, null);
		return viewInflate;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(task != null)
			task.cancel(true);
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
				categoryList = q.getCategories();
				showList();
				MainActivity main = (MainActivity) getActivity();
				main.hideSwipeProgress();
			}

			@Override
			public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				if(MGUtilities.hasConnection(getActivity())) {
					try {
						String strUrl = String.format("%s?api_key=%s",
								Config.GET_CATEGORIES_JSON_URL,
								Config.API_KEY);

						DataParser parser = new DataParser();
						Data data = parser.getData(strUrl);
						MainActivity main = (MainActivity) getActivity();
						if (main == null)
							return;

						Queries q = main.getQueries();
						if (data == null)
							return;

						q.deleteTable("categories");
						if (data.getCategories() != null && data.getCategories().size() > 0) {
							for (Category cat : data.getCategories()) {
								q.insertCategory(cat);
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

	private void showList() {
		MainActivity main = (MainActivity) this.getActivity();
		main.hideSwipeProgress();
		if(categoryList != null && categoryList.size() == 0) {
			MGUtilities.showNotifier(this.getActivity(), MainActivity.offsetY);
			return;
		}
		
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
				Category category = categoryList.get(position);
				Spanned title = Html.fromHtml(category.getCategory());
				TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
				tvTitle.setText(title);
			}
		});
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long resId) {
		// TODO Auto-generated method stub
		Category category = categoryList.get(pos);
		Intent i = new Intent(getActivity(), StoreActivity.class);
		i.putExtra("category", category);
		getActivity().startActivity(i);
	}
}
