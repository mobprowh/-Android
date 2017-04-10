package com.bahisadam.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bahisadam.R;
import com.bahisadam.model.teamInfo.Team;
import com.bahisadam.utility.RoundedCornersTransform;
import com.squareup.picasso.Picasso;


public class TeamDetailsFragment extends Fragment {

    public static final String ARG_TEAM_INFO = "team_info";
    private Team mTeam;

    public static TeamDetailsFragment newInstance(Team team) {
        TeamDetailsFragment fragment = new TeamDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TEAM_INFO, team);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTeam = getArguments().getParcelable(ARG_TEAM_INFO);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_details, container, false);

        ImageView stadiumImage = (ImageView)view.findViewById(R.id.image_view_stadium);
        TextView stadiumName = (TextView)view.findViewById(R.id.text_view_stadium_name);
        TextView officialName = (TextView)view.findViewById(R.id.text_view_club_official_name);
        TextView yearFounded = (TextView)view.findViewById(R.id.text_view_year_founded);
        TextView clubHead = (TextView)view.findViewById(R.id.text_view_club_head);
        TextView coach = (TextView)view.findViewById(R.id.text_view_coach);
        TextView website = (TextView)view.findViewById(R.id.text_view_website);
        TextView twitter = (TextView)view.findViewById(R.id.text_view_twitter);

        if(mTeam!=null){
            try{
                Picasso.with(getActivity()).load(mTeam.getStadiumImageUrl()).transform(new RoundedCornersTransform(10)).fit().into(stadiumImage);
            } catch (Exception e){
                e.printStackTrace();
            }
            stadiumName.setText(mTeam.getStadiumName()+"\n" + "Kapasite: " + mTeam.getStadiumCapacity()+"\n"+ "Insa Tarihi: "+mTeam.getStadiumYearBuilt());
            officialName.setText(mTeam.getClubOfficialName());
            yearFounded.setText(mTeam.getYearFounded());
            clubHead.setText(mTeam.getClubHead());
            coach.setText(mTeam.getCoach());
            website.setText(mTeam.getWebsite());
            twitter.setText(mTeam.getTwitter());
        }
        return view;
    }

}
