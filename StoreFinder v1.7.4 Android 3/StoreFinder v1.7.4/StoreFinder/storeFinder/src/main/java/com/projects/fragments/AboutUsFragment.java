package com.projects.fragments;

import com.config.Config;
import com.projects.storefinder.R;
import com.libraries.utilities.MGUtilities;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class AboutUsFragment extends Fragment implements OnClickListener{
	
	private View viewInflate;
	
	public AboutUsFragment() { }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		viewInflate = inflater.inflate(R.layout.fragment_about_us, null);
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
		Button btnContactUs = (Button) viewInflate.findViewById(R.id.btnContactUs);
		btnContactUs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				email();
			}
		});
	}

	@Override
	public void onClick(View v) { }
	
	private void email() {
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ Config.ABOUT_US_EMAIL } );
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, 
				MGUtilities.getStringFromResource(getActivity(), R.string.email_subject_company) );
		
		emailIntent.putExtra(Intent.EXTRA_TEXT, 
				MGUtilities.getStringFromResource(getActivity(), R.string.email_body_company) );
		emailIntent.setType("message/rfc822");
		getActivity().startActivity(Intent.createChooser(emailIntent, 
				MGUtilities.getStringFromResource(getActivity(), R.string.choose_email_client)) );
	}
}
