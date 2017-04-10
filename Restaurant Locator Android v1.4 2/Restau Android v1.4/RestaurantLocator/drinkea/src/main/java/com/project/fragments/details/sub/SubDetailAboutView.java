package com.project.fragments.details.sub;

import com.config.UIConfig;
import com.models.Restaurant;
import com.project.drinkea.R;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class SubDetailAboutView implements OnClickListener{

	private View viewInflate = null;
	private Restaurant restaurant;
	private FragmentActivity act;
	
	public SubDetailAboutView(FragmentActivity act) {
		// TODO Auto-generated method stub
		this.act = act;
		LayoutInflater inflater = act.getLayoutInflater();
		viewInflate = inflater.inflate(R.layout.sub_detail_about, null, false);
	}
	

	
	public View setDetail(Restaurant res) {
        this.restaurant = res;
        
		TextView tvTitle = (TextView) viewInflate.findViewById(R.id.tvTitle);
		tvTitle.setTextColor(act.getResources().getColor(UIConfig.THEME_COLOR));
		
		TextView tvAddress = (TextView) viewInflate.findViewById(R.id.tvAddress);
		TextView tvWorkingHours = (TextView) viewInflate.findViewById(R.id.tvWorkingHours);
		TextView tvAmenities = (TextView) viewInflate.findViewById(R.id.tvAmenities);
		TextView tvDescription = (TextView) viewInflate.findViewById(R.id.tvDescription);
		
		RatingBar ratingBarFood = (RatingBar) viewInflate.findViewById(R.id.ratingBarFood);
		RatingBar ratingBarPrice = (RatingBar) viewInflate.findViewById(R.id.ratingBarPrice);
		
		Spanned desc = Html.fromHtml(res.desc);
		Spanned name = Html.fromHtml(res.name);
		Spanned address = Html.fromHtml(res.address);
		Spanned hours = Html.fromHtml(res.hours);
		Spanned amenities = Html.fromHtml(res.amenities);
		
		tvTitle.setText(name);
		tvAddress.setText(address);
		tvWorkingHours.setText(hours);
		tvAmenities.setText(amenities);
		tvDescription.setText(desc);
		
		ratingBarFood.setRating((float)res.food_rating);
		ratingBarPrice.setRating((float)res.price_rating);
		
		ratingBarFood.setFocusable(false);
		ratingBarFood.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return true;
			}
	    });
		
		ratingBarPrice.setFocusable(false);
		ratingBarPrice.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return true;
			}
	    });
		
		
		ImageView imgViewCall = (ImageView) viewInflate.findViewById(R.id.imgViewCall);
		imgViewCall.setOnClickListener(this);
		
		ImageView imgViewEmail = (ImageView) viewInflate.findViewById(R.id.imgViewEmail);
		imgViewEmail.setOnClickListener(this);
		
		ImageView imgViewWebsite = (ImageView) viewInflate.findViewById(R.id.imgViewWebsite);
		imgViewWebsite.setOnClickListener(this);
		
		return viewInflate;
	}
	
	public View getView() {
		return viewInflate;
	}
	

	@Override
	public void onClick(View v) {
		
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.imgViewCall:
				call(restaurant.phone);
				break;
				
			case R.id.imgViewEmail:
				email(restaurant.email);
				break;
				
			case R.id.imgViewWebsite:
				website(restaurant.website);
				break;
		}
	}
	
	private void call(String phoneNo) {
		
		if(phoneNo == null || phoneNo.length() == 0) {
			Toast.makeText(act, "No phone number provided.", Toast.LENGTH_SHORT).show();
			return;
		}

		String uri = "tel:" + phoneNo.trim() ;
		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse(uri));
		act.startActivity(intent);
	}
	
	private void website(String url) {
		
		if(url == null || url.length() == 0) {
			Toast.makeText(act, "No website provided.", Toast.LENGTH_SHORT).show();
			return;
		}

		String strUrl = url;
		if(!url.contains("http")) {
			strUrl = "http://" + url;
		}
		
		Intent webIntent = new Intent(Intent.ACTION_VIEW);
		webIntent.setData(Uri.parse(strUrl));
		act.startActivity(Intent.createChooser(webIntent, "Choose a browser :"));
	}
			
	private void email(String email) {

		if(email == null || email.length() == 0) {
			Toast.makeText(act, "No email provided.", Toast.LENGTH_SHORT).show();
			return;
		}

		
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email} );		  
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Restaurant Inquiry");
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Your Message Here...");
		emailIntent.setType("message/rfc822");
		act.startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
	}
}
