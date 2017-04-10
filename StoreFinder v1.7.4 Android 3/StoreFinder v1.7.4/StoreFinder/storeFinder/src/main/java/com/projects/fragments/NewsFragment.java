package com.projects.fragments;

import java.util.ArrayList;

import com.libraries.adapters.MGListAdapter;
import com.libraries.adapters.MGListAdapter.OnMGListAdapterAdapterListener;
import com.config.Config;
import com.config.UIConfig;
import com.db.Queries;
import com.libraries.asynctask.MGAsyncTask;
import com.libraries.dataparser.DataParser;
import com.models.Category;
import com.models.Data;
import com.projects.activities.NewsDetailActivity;
import com.libraries.helpers.DateTimeHelper;
import com.libraries.imageview.MGImageView;
import com.models.News;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.libraries.utilities.MGUtilities;
import com.projects.storefinder.MainActivity;
import com.projects.storefinder.R;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ListView;
import android.widget.TextView;

public class NewsFragment extends Fragment implements OnItemClickListener, OnClickListener{
	
	private View viewInflate;
	private ArrayList<News> arrayData;
	DisplayImageOptions options;
	private Queries q;
	MGAsyncTask task;
	
	public NewsFragment() { }

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(task != null)
			task.cancel(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		viewInflate = inflater.inflate(R.layout.fragment_news, null);
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
				getData();
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
		
		ListView listView = (ListView) viewInflate.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		MGListAdapter adapter = new MGListAdapter(
				getActivity(), arrayData.size(), R.layout.news_entry);
		
		adapter.setOnMGListAdapterAdapterListener(new OnMGListAdapterAdapterListener() {
			
			@Override
			public void OnMGListAdapterAdapterCreated(MGListAdapter adapter, View v,
					int position, ViewGroup viewGroup) {
				// TODO Auto-generated method stub
				News news = arrayData.get(position);
				MGImageView imgViewPhoto = (MGImageView) v.findViewById(R.id.imgViewPhoto);
				imgViewPhoto.setCornerRadius(0.0f);
				imgViewPhoto.setBorderWidth(UIConfig.BORDER_WIDTH);
				imgViewPhoto.setBorderColor(getResources().getColor(UIConfig.THEME_BLACK_COLOR));
				
				if(news.getPhoto_url() != null) {
					MainActivity.getImageLoader().displayImage(news.getPhoto_url(), imgViewPhoto, options);
				}
				else {
					imgViewPhoto.setImageResource(UIConfig.SLIDER_PLACEHOLDER);
				}
				
				imgViewPhoto.setTag(position);
				Spanned name = Html.fromHtml(news.getNews_title());
				Spanned address = Html.fromHtml(news.getNews_content());
				
				TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
				tvTitle.setText(name);
				
				TextView tvSubtitle = (TextView) v.findViewById(R.id.tvSubtitle);
				tvSubtitle.setText(address);

				String date = DateTimeHelper.getStringDateFromTimeStamp(news.getCreated_at(), "MM/dd/yyyy" );
				TextView tvDate = (TextView) v.findViewById(R.id.tvDate);
				tvDate.setText(date);
			}
		});
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long resId) {
		// TODO Auto-generated method stub
		News news = arrayData.get(pos);
		Intent i = new Intent(getActivity(), NewsDetailActivity.class);
		i.putExtra("news", news);
		getActivity().startActivity(i);
	}

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
				arrayData  = q.getNews();
				showList();
			}

			@Override
			public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				try {
					String strUrl = String.format("%s?api_key=%s",
							Config.GET_NEWS_JSON_URL,
							Config.API_KEY);

					DataParser parser = new DataParser();
					Data data = parser.getData(strUrl);
					MainActivity main = (MainActivity) getActivity();
					if (main == null)
						return;

					Queries q = main.getQueries();
					if (data == null)
						return;

					q.deleteTable("news");
					if (data.getNews() != null && data.getNews().size() > 0) {
						for (News news : data.getNews()) {
							q.insertNews(news);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		task.execute();
	}
}
