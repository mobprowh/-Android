package com.project.fragments;

import java.util.ArrayList;

import com.libraries.asynctask.MGAsyncTask;
import com.libraries.asynctask.MGAsyncTask.OnMGAsyncTaskListener;
import com.config.Config;
import com.models.Category;
import com.models.Restaurant;
import com.project.fragments.activity.RestaurantActivity;
import com.project.drinkea.MainActivity;
import com.project.drinkea.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class SearchFragment extends Fragment implements OnClickListener{

	private View viewInflate = null;
	private EditText txtCategories, txtKeywords;
	private RatingBar ratingBarPriceFrom, ratingBarPriceTo;
	private RatingBar ratingBarFoodFrom, ratingBarFoodTo;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// save the reference of the inflated view
		if(viewInflate == null)
			viewInflate = inflater.inflate(R.layout.fragment_search, container, false);
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
		txtKeywords = (EditText) viewInflate.findViewById(R.id.txtKeywords);
        txtCategories = (EditText) viewInflate.findViewById(R.id.txtCategory);
        txtCategories.setOnClickListener(this);
        txtCategories.setInputType(0);
        
        ratingBarFoodFrom = (RatingBar) viewInflate.findViewById(R.id.ratingBarFoodFrom);
        ratingBarFoodTo = (RatingBar) viewInflate.findViewById(R.id.ratingBarFoodTo);
        
        ratingBarPriceFrom = (RatingBar) viewInflate.findViewById(R.id.ratingBarPriceFrom);
        ratingBarPriceTo = (RatingBar) viewInflate.findViewById(R.id.ratingBarPriceTo);
		
        Button btnClear = (Button) viewInflate.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);
        
        Button btnSearch = (Button) viewInflate.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		MainActivity main = (MainActivity) getActivity();
		switch(v.getId()) {
			case R.id.txtCategory:
				createCategoryDialog();
				break;
			case R.id.btnClear:
				clear();
				break;
			case R.id.btnSearch:
				search();
				break;
		}
	}
	
	private void clear() {
		txtKeywords.setText("");
		txtCategories.setText("");
		ratingBarFoodFrom.setRating(3);
		ratingBarFoodTo.setRating(5);
		ratingBarPriceFrom.setRating(1);
		ratingBarPriceTo.setRating(5);
	}
	
	private void search() {
		final MainActivity main = (MainActivity) getActivity();
		main.showSwipeProgress();

		MGAsyncTask asyncTask = new MGAsyncTask(getActivity());
		asyncTask.setMGAsyncTaskListener(new OnMGAsyncTaskListener() {
			
			ArrayList<Restaurant> searchList;
			
			@Override
			public void onAsyncTaskProgressUpdate(MGAsyncTask asyncTask) { }
			
			@Override
			public void onAsyncTaskPreExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				asyncTask.setMessage("Searching...");
			}
			
			@Override
			public void onAsyncTaskPostExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
                main.hideSwipeProgress();
				displayResults(searchList);
			}
			
			@Override
			public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				searchList = doSearch();
			}
		});
		asyncTask.execute();
	}

	private ArrayList<Restaurant> doSearch() {
		MainActivity main = (MainActivity) getActivity();
		ArrayList<Restaurant> arrayFilter = new ArrayList<Restaurant>();

		String strKeywords = txtKeywords.getText().toString().toLowerCase();
		String strCategory = txtCategories.getText().toString();
	    
	    float ratingPriceFrom = ratingBarPriceFrom.getRating();
	    float ratingPriceTo = ratingBarPriceTo.getRating();
	    
	    float ratingFoodFrom = ratingBarFoodFrom.getRating();
	    float ratingFoodTo = ratingBarFoodTo.getRating();

	    int countParams = strKeywords.length() > 0 ? 1 : 0;
	    countParams += strCategory.length() > 0 ? 1 : 0;
	    countParams += (ratingPriceFrom > 0 && ratingPriceTo > 0) ? 1 : 0;
	    countParams += (ratingFoodFrom > 0 && ratingFoodTo > 0) ? 1 : 0;

	    ArrayList<Restaurant> arrayRestaurants = main.getQueries().getRestaurants();
	    for(Restaurant restaurant : arrayRestaurants) {
	        int qualifyCount = 0;
	        boolean isFoundKeyword = containsString(restaurant.name, strKeywords) ||
	        		containsString(restaurant.address, strKeywords) ||
	        		containsString(restaurant.amenities, strKeywords);
	        
	        if( strKeywords.length() > 0  && isFoundKeyword)
	            qualifyCount += 1;

	        if( strCategory.length() > 0) {
	        	Category cat = main.getQueries().getCategoryByCategoryName(strCategory);
	            boolean isFoundCat = false;
	            if(cat != null && cat.category_id == restaurant.category_id)
	                isFoundCat = true;
	            
	            if(compareString(strCategory, Config.CATEGORY_ALL))
	                isFoundCat = true;
	            
	            if(isFoundCat)
	                qualifyCount += 1;
	        }
	        
	        boolean isFoundPrice = (ratingPriceFrom <= restaurant.price_rating &&
	        						restaurant.price_rating <= ratingPriceTo);
	        
	        if(ratingPriceFrom > 0 && ratingPriceTo > 0 && isFoundPrice)
	            qualifyCount += 1;
	        
	        boolean isFoundFood = (ratingFoodFrom <= restaurant.price_rating &&
	        						restaurant.price_rating <= ratingFoodTo);
	        
	        if(ratingFoodFrom > 0 && ratingFoodTo > 0 && isFoundFood)
	            qualifyCount += 1;

	        if(qualifyCount == countParams)
	            arrayFilter.add(restaurant);
	    }
		return arrayFilter;
	}
	
	public void createCategoryDialog() {
		MainActivity main = (MainActivity) getActivity();
		ArrayList<String> categories = main.getQueries().getCategoryNames();
		categories.add(0, Config.CATEGORY_ALL);

		final String[] strList = new String[categories.size()];
		for(int x = 0; x < categories.size(); x++) {
			Spanned name = Html.fromHtml(categories.get(x));
			strList[x] = name.toString();
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(R.string.select_category)
	           .setItems(strList, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	               
	            	   txtCategories.setText(strList[which]);
	           }
	    });
	    AlertDialog dialog =  builder.create();
	    dialog.show();
	}

	private boolean containsString(String toSearchWord, String fromWord) {
		if(toSearchWord.toLowerCase().contains(fromWord.toLowerCase()))
			return true;
		return false;
	}
	
	private boolean compareString(String toSearchWord, String fromWord) {
		if(toSearchWord.compareToIgnoreCase(fromWord) == 0)
			return true;

		return false;
	}

	private void displayResults(ArrayList<Restaurant> searchList) {
		Intent i = new Intent(this.getActivity(), RestaurantActivity.class);
		i.putExtra("categoryId", -1);
		i.putExtra("searchList", searchList);
		startActivity(i);
	}
}
