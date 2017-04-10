package com.projects.fragments;

import com.config.Config;
import com.libraries.usersession.UserAccessSession;
import com.libraries.utilities.MGUtilities;
import com.projects.storefinder.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsFragment extends Fragment {
	
	private View viewInflate;
    private TextView tvRadius;
	
	public SettingsFragment() { }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		viewInflate = inflater.inflate(R.layout.fragment_animation, null);
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
		UserAccessSession accessSession = UserAccessSession.getInstance(getActivity());
        tvRadius = (TextView) viewInflate.findViewById(R.id.tvRadius);

        String strNearbyRadius = String.format("%s (Max: %.2f %s)",
                MGUtilities.getStringFromResource(getActivity(), R.string.store_nearby_radius),
                accessSession.getFilterDistanceMax(),
                MGUtilities.getStringFromResource(getActivity(), R.string.km));

        TextView tvTitleNearbyRadius = (TextView) viewInflate.findViewById(R.id.tvTitleNearbyRadius);
        tvTitleNearbyRadius.setText(strNearbyRadius);

        updateRadiusText(accessSession.getFilterDistance());

        FrameLayout frameRadius = (FrameLayout) viewInflate.findViewById(R.id.frameRadius);
        frameRadius.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
	}

	private void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.enter_radius_in_km);

		View viewInflated = LayoutInflater.from(getActivity()).inflate(R.layout.text_input, (ViewGroup) getView(), false);
		final EditText txtRadius = (EditText) viewInflated.findViewById(R.id.txtRadius);

        UserAccessSession accessSession = UserAccessSession.getInstance(getActivity());
		String strRadius = String.format("%.2f", accessSession.getFilterDistance());
        txtRadius.setText(strRadius);

		builder.setView(viewInflated);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				String valueStr = txtRadius.getText().toString();
				float value = 0;
				if(!valueStr.isEmpty()) {
                    value = Float.parseFloat(valueStr);
                }

                UserAccessSession.getInstance(getActivity()).setFilterDistance(value);
                updateRadiusText(value);
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		builder.show();
	}

    private void updateRadiusText(float value) {
        String str = String.format("%.2f %s",
                value,
                MGUtilities.getStringFromResource(getActivity(), R.string.km));
        tvRadius.setText(str);
    }
}
