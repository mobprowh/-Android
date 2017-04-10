package com.bahisadam.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.bahisadam.R;
import com.bahisadam.adapter.StatsAdapter;
import com.bahisadam.interfaces.RestClient;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.utility.Utilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeagueStatisticsFragment extends Fragment {
    public static final String ARG_LEAGUE_ID = "LEAGUE_ID";
    private Integer mLeagueId;

    private StatsAdapter mAdapter;

    private static final int STATISTICS_GOAL = 1, STATISTICS_ASISTS = 2, STATISTICS_YELLOWCARDS = 3, STATISTICS_REDCARDS=4;
    private List<MatchPOJO.LeagueDetailStat> mGoals;
    private List<MatchPOJO.LeagueDetailStat> mAsists;
    private List<MatchPOJO.LeagueDetailStat> mYellowCards;
    private List<MatchPOJO.LeagueDetailStat> mRedCards;
    @BindView(R.id.titleStats) TextView titleTv;
    @BindView(R.id.leagueStats) RecyclerView rv;
    @BindView(R.id.statsUnderlinesLayout) LinearLayout underlines;
    @BindView(R.id.goalLayout) LinearLayout goalLayout;
    private RestClient mClient;
    public static LeagueStatisticsFragment newInstance(Integer leagueId) {
        LeagueStatisticsFragment fragment = new LeagueStatisticsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LEAGUE_ID, leagueId);
        fragment.setArguments(args);
        return fragment;
    }

    public LeagueStatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLeagueId = getArguments().getInt(ARG_LEAGUE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_league_statistics, container, false);
        ButterKnife.bind(this,view);
        mGoals = new ArrayList<MatchPOJO.LeagueDetailStat>();
        mAsists = new ArrayList<MatchPOJO.LeagueDetailStat>();
        mYellowCards = new ArrayList<MatchPOJO.LeagueDetailStat>();
        mRedCards = new ArrayList<MatchPOJO.LeagueDetailStat>();
        mClient = Utilities.buildRetrofit();
        mAdapter = new StatsAdapter(getActivity());
        rv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rv.setAdapter(mAdapter);
        loadData();
        TabHost tabHost = (TabHost) view.findViewById(android.R.id.tabhost);

        tabHost.setup();


        TabHost.TabSpec tabSpec;


        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator(getString(R.string.goal));
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator(getString(R.string.asist));
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator(getString(R.string.yellowCards));
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator(getString(R.string.redCards));
        tabHost.addTab(tabSpec);

        ViewGroup.LayoutParams params = tabHost.getTabWidget().getChildAt(0).getLayoutParams();

        params.height = (int) (params.height * 0.7);
        for(int i =0 ;i < tabHost.getTabWidget().getChildCount(); i++) {
            View child = tabHost.getTabWidget().getChildAt(i);
            child.setLayoutParams(params);
            child.setBackgroundResource(R.drawable.tab_selector);
        }
        tabHost.setCurrentTabByTag("tag1");
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if(s.equals("tag1")){
                    showStatistics(STATISTICS_GOAL);
                } else if(s.equals("tag2")){
                    showStatistics(STATISTICS_ASISTS);
                } else if(s.equals("tag3")){
                    showStatistics(STATISTICS_YELLOWCARDS);
                } else if(s.equals("tag4")){
                    showStatistics(STATISTICS_REDCARDS);
                }
            }
        });

        return view;
    }

    private void loadData() {
        Call<MatchPOJO.LeagueStats> call = mClient.statsByLeague(mLeagueId);
        call.enqueue(new Callback<MatchPOJO.LeagueStats>() {
            @Override
            public void onResponse(Call<MatchPOJO.LeagueStats> call, Response<MatchPOJO.LeagueStats> response) {
                MatchPOJO.LeagueStats stats = response.body();
                mAsists.clear();
                mGoals.clear();
                mRedCards.clear();
                mYellowCards.clear();
                mGoals.addAll(stats.getGoals());
                mAsists.addAll(stats.getAsists());
                mYellowCards.addAll(stats.getYellowCards());
                mRedCards.addAll(stats.getRedCards());
                showStatistics(STATISTICS_GOAL);
                //clickHeader(goalLayout);
            }

            @Override
            public void onFailure(Call<MatchPOJO.LeagueStats> call, Throwable t) {

            }
        });

    }

    @OnClick({R.id.goalLayout,R.id.asistLayout,R.id.redCardsLayout,R.id.yellowCardsLayout})
    public void clickHeader(View view){
        setGoneAllStandingsUnderlines();
        LinearLayout layout  = (LinearLayout) view;
        TextView tv = (TextView)layout.getChildAt(0);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP ,14);

        switch (view.getId()) {
            case R.id.goalLayout :
                mAdapter.clear();
                mAdapter.add(mGoals);
                titleTv.setText(getString(R.string.goal) + " " +getString(R.string.king));
                mAdapter.notifyDataSetChanged();
                ((LinearLayout)underlines.getChildAt(0)).getChildAt(0).setVisibility(View.VISIBLE);
                break;
            case R.id.asistLayout :
                mAdapter.clear();
                mAdapter.add(mAsists);
                titleTv.setText(getString(R.string.asist) + " " +getString(R.string.king));
                mAdapter.notifyDataSetChanged();
                ((LinearLayout)underlines.getChildAt(1)).getChildAt(0).setVisibility(View.VISIBLE);
                break;
            case R.id.redCardsLayout :
                mAdapter.clear();
                mAdapter.add(mRedCards);
                titleTv.setText(getString(R.string.redCards) + " " +getString(R.string.king));

                mAdapter.notifyDataSetChanged();
                ((LinearLayout)underlines.getChildAt(2)).getChildAt(0).setVisibility(View.VISIBLE);
                break;
            case R.id.yellowCardsLayout :
                mAdapter.clear();
                mAdapter.add(mYellowCards);
                titleTv.setText(getString(R.string.yellowCards) + " " +getString(R.string.king));

                mAdapter.notifyDataSetChanged();
                ((LinearLayout)underlines.getChildAt(3)).getChildAt(0).setVisibility(View.VISIBLE);
                break;
            default:
                Log.d("tabs", view.toString());
                break;
        }

    }
    private void showStatistics(int type){
        switch (type) {
            case STATISTICS_GOAL:
                mAdapter.clear();
                mAdapter.add(mGoals);
                titleTv.setText(getString(R.string.goal) + " " + getString(R.string.king));
                mAdapter.notifyDataSetChanged();
                ((LinearLayout) underlines.getChildAt(0)).getChildAt(0).setVisibility(View.VISIBLE);
                break;
            case STATISTICS_ASISTS:
                mAdapter.clear();
                mAdapter.add(mAsists);
                titleTv.setText(getString(R.string.asist) + " " + getString(R.string.king));
                mAdapter.notifyDataSetChanged();
                ((LinearLayout) underlines.getChildAt(1)).getChildAt(0).setVisibility(View.VISIBLE);
                break;
            case STATISTICS_REDCARDS:
                mAdapter.clear();
                mAdapter.add(mRedCards);
                titleTv.setText(getString(R.string.redCards) + " " + getString(R.string.king));

                mAdapter.notifyDataSetChanged();
                ((LinearLayout) underlines.getChildAt(2)).getChildAt(0).setVisibility(View.VISIBLE);
                break;
            case STATISTICS_YELLOWCARDS:
                mAdapter.clear();
                mAdapter.add(mYellowCards);
                titleTv.setText(getString(R.string.yellowCards) + " " + getString(R.string.king));

                mAdapter.notifyDataSetChanged();
                ((LinearLayout) underlines.getChildAt(3)).getChildAt(0).setVisibility(View.VISIBLE);
                break;
        }
    }
    private void setGoneAllStandingsUnderlines() {
        for(int i=0; i< underlines.getChildCount(); i++) {
            ((LinearLayout)underlines.getChildAt(i)).getChildAt(0).setVisibility(View.INVISIBLE);
        }
    }

}
