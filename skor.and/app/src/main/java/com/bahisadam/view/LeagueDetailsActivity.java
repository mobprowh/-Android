package com.bahisadam.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bahisadam.R;
import com.bahisadam.fragment.FixtureFragment;
import com.bahisadam.fragment.LeagueStatisticsFragment;
import com.bahisadam.fragment.StandingsFragment;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.utility.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeagueDetailsActivity extends BaseActivity  {

    public static final String ARG_LEAGUE_ID = "LEAGUE_ID";
    public static final String ARG_ROUND = "ROUND";
    public static final String ARG_LEAGUE_NAME = "LEAGUE_NAME";
    public static final String ARG_LEAGUE_ICON = "LEAGUE_ICON";
    private Integer mLeagueId;
    private Integer mRound;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    @BindView(R.id.leagueName) TextView leagueNameTV;
    @BindView(R.id.leagueLogo) ImageView leagueLogoIV;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_details);
        initFooterToolbar();
        Intent intent = getIntent();
        mLeagueId = intent.getIntExtra(ARG_LEAGUE_ID,1);
        mRound = intent.getIntExtra(ARG_ROUND,-1);
        String leagueName = intent.getStringExtra(ARG_LEAGUE_NAME);
        String icon = intent.getStringExtra(ARG_LEAGUE_ICON);
        if(Constant.TURKEY_SUPER_LEAGUE == mLeagueId)
            setActiveToolbarItem(4);
        ButterKnife.bind(this);
        if(leagueName != null){
            leagueNameTV.setText(leagueName);
        }
        if(icon != null){
            int id = getResources().getIdentifier(icon,"drawable",getPackageName());
            Bitmap bitmap = Utilities.getBitmapCountry(id,this);
            if(bitmap!= null){
                leagueLogoIV.setImageBitmap(bitmap);
            } else {
                leagueLogoIV.setVisibility(View.GONE);
            }
        } else {
            leagueLogoIV.setVisibility(View.GONE);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_league_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

*/

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0)
                return StandingsFragment.newInstance(mLeagueId);
            if(position == 2)
                return LeagueStatisticsFragment.newInstance(mLeagueId);
            return FixtureFragment.newInstance(mLeagueId,mRound);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "P. Durumu";
                case 1:
                    return getString(R.string.fixture);
                case 2:
                    return getString(R.string.statistics);
            }
            return null;
        }
    }
}
