package com.bahisadam.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bahisadam.R;
import com.bahisadam.fragment.SquadFragment;
import com.bahisadam.fragment.TeamDetailsFragment;
import com.bahisadam.fragment.TeamMatchesFragment;
import com.bahisadam.fragment.TeamStatisticsFragment;
import com.bahisadam.model.teamInfo.TeamFixture;
import com.bahisadam.model.teamInfo.TeamMatch;
import com.bahisadam.model.teamInfo.TeamPlayerInfo;
import com.bahisadam.model.teamInfo.TeamPlayerStats;
import com.bahisadam.model.teamInfo.Team;
import com.bahisadam.model.teamInfo.TeamInfoFetcher;
import com.bahisadam.utility.Utilities;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class TeamDetailsActivity extends BaseActivity {

    public static final String ARG_TEAM_INFO = "TEAM_INFO";
    public static final String ARG_TEAM_NAME = "TEAM_NAME";
    public static final String ARG_TEAM_ICON = "TEAM_ICON";
    private Integer mTeamId;
    private TeamDetailsActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    TextView teamNameTV;
    CircleImageView teamLogoIV;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Bundle mTeamInfo;
    private Team mTeam;
    private ArrayList<TeamPlayerInfo> mPlayers;
    private ArrayList<TeamPlayerStats> mPlayerStats;
    private ArrayList<TeamMatch> mMatches;
    private ArrayList<TeamFixture> mFixtures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);

        initFooterToolbar();

        teamNameTV = (TextView)findViewById(R.id.team_details_teamName);
        teamLogoIV = (CircleImageView)findViewById(R.id.team_details_teamLogo);
        Intent intent = getIntent();
        mTeamId = intent.getIntExtra(BaseActivity.ID,1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        mSectionsPagerAdapter = new TeamDetailsActivity.SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        if(savedInstanceState == null) {
            new FetchTask(mTeamId.toString()).execute();
        } else{
            mTeamInfo = savedInstanceState.getBundle(ARG_TEAM_INFO);
            updateUI();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(ARG_TEAM_INFO,mTeamInfo);
    }

    public class FetchTask extends AsyncTask<Void,Void,Bundle> {

        private ProgressDialog mProgressDialog= new ProgressDialog(TeamDetailsActivity.this);
        private String mId;

        FetchTask(String id) {
            mId = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();
            } catch(Exception e){e.printStackTrace();}
        }

        @Override
        protected Bundle doInBackground(Void... voids) {
            Bundle info = null;
            info = new TeamInfoFetcher(mId).getTeamInfo();
            return info;

        }

        @Override
        protected void onPostExecute(Bundle info) {
            mTeamInfo = info;
            updateUI();
            try {
                mProgressDialog.dismiss();
            } catch (Exception e){e.printStackTrace();}
        }
    }

    private void updateUI(){
     parseInfo(mTeamInfo);
     teamNameTV.setText(mTeam.getName());
     Picasso.with(getApplicationContext()).load(mTeam.getLogoPath()).fit().into(teamLogoIV);
     mViewPager.setAdapter(mSectionsPagerAdapter);
     mTabLayout.setupWithViewPager(mViewPager);
    }

   private void parseInfo(Bundle info){
       mTeam = info.getParcelable(TeamInfoFetcher.TEAM_INFO_MAIN);
       mPlayers = info.getParcelableArrayList(TeamInfoFetcher.TEAM_INFO_PLAYERS);
       mPlayerStats = info.getParcelableArrayList(TeamInfoFetcher.TEAM_INFO_STATS);
       mMatches = info.getParcelableArrayList(TeamInfoFetcher.TEAM_INFO_MATCHES);
       mFixtures = info.getParcelableArrayList(TeamInfoFetcher.TEAM_INFO_FIXTURES);
   }






    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0)
                return SquadFragment.newInstance(mPlayers,mTeam.getName());
            if(position == 1)
                return TeamMatchesFragment.newInstance(mMatches,mFixtures,mTeam.getName());
            if(position == 2)
                return TeamStatisticsFragment.newInstance(mPlayerStats);
            return TeamDetailsFragment.newInstance(mTeam);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.kadro);
                case 1:
                    return getString(R.string.matches);
                case 2:
                    return getString(R.string.statistics);
                case 3:
                    return getString(R.string.detay);
            }
            return null;
        }
    }
}
