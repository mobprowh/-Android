package com.bahisadam.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.bahisadam.R;
import com.bahisadam.adapter.MatchAdapter;
import com.bahisadam.adapter.MatchListAdapter;
import com.bahisadam.model.MatchListComparator;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.utility.Utilities;
import com.bahisadam.view.DetailPageActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MaclarFragment extends Fragment implements MatchAdapter.UpdateMatchListener, RecyclerView.OnItemTouchListener {


    private static final String ARG_TEAM_HOME_NAME = "teamHomeName";
    private static final String ARG_TEAM_AWAY_NAME = "teamAwayName";

    private String teamHome;
    private String teamAway;
    @BindView(R.id.rvAllTeam1) RecyclerView rvAllteam1;
    @BindView(R.id.titleAllTeam1) TextView titleAllTeam1;
    @BindView(R.id.rvHomeTeam1) RecyclerView rvHomeTeam1;
    @BindView(R.id.titleHomeTeam1) TextView titleHomeTeam1;
    @BindView(R.id.rvAwayTeam1) RecyclerView rvAwayTeam1;
    @BindView(R.id.titleAwayTeam1) TextView titleAwayTeam1;
    @BindView(R.id.rvAllTeam2) RecyclerView rvAllteam2;
    @BindView(R.id.titleAllTeam2) TextView titleAllTeam2;
    @BindView(R.id.rvHomeTeam2) RecyclerView rvHomeTeam2;
    @BindView(R.id.titleHomeTeam2) TextView titleHomeTeam2;
    @BindView(R.id.rvAwayTeam2) RecyclerView rvAwayTeam2;
    @BindView(R.id.titleAwayTeam2) TextView titleAwayTeam2;
    @BindView(R.id.rvHeadToHead) RecyclerView rvHeadToHead;
    @BindView(R.id.titleHeadToHead) TextView titleHeadToHead;


    DetailPageActivity mActivity;
    private MatchListAdapter allAdapterTeam1;
    private List<MatchPOJO.Match> all5ListTeam1;
    private MatchListAdapter homeAdapterTeam1;
    private List<MatchPOJO.Match> homeListTeam1;
    private MatchListAdapter awayAdapterTeam1;
    private List<MatchPOJO.Match> awayListTeam1;

    private MatchListAdapter allAdapterTeam2;
    private List<MatchPOJO.Match> all5ListTeam2;
    private MatchListAdapter homeAdapterTeam2;
    private List<MatchPOJO.Match> homeListTeam2;
    private MatchListAdapter awayAdapterTeam2;
    private List<MatchPOJO.Match> awayListTeam2;

    private MatchListAdapter headToHeadAdapter;
    private List<MatchPOJO.Match> headToHeadList;
    private String mCurrentMatchId;


    public MaclarFragment() {
    }


    public static MaclarFragment newInstance(String param1, String param2) {
        MaclarFragment fragment = new MaclarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEAM_HOME_NAME, param1);
        args.putString(ARG_TEAM_AWAY_NAME, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            teamHome = getArguments().getString(ARG_TEAM_HOME_NAME);
            teamAway = getArguments().getString(ARG_TEAM_AWAY_NAME);
        }
        mActivity = (DetailPageActivity) getActivity();
        all5ListTeam1 = new ArrayList<MatchPOJO.Match>();
        allAdapterTeam1 = new MatchListAdapter(all5ListTeam1,mActivity);
        homeListTeam1 = new ArrayList<MatchPOJO.Match>();
        homeAdapterTeam1 = new MatchListAdapter(homeListTeam1,mActivity);
        awayListTeam1 = new ArrayList<MatchPOJO.Match>();
        awayAdapterTeam1 = new MatchListAdapter(awayListTeam1,mActivity);

        all5ListTeam2 = new ArrayList<MatchPOJO.Match>();
        allAdapterTeam2 = new MatchListAdapter(all5ListTeam2,mActivity);
        homeListTeam2 = new ArrayList<MatchPOJO.Match>();
        homeAdapterTeam2 = new MatchListAdapter(homeListTeam2,mActivity);
        awayListTeam2 = new ArrayList<MatchPOJO.Match>();
        awayAdapterTeam2 = new MatchListAdapter(awayListTeam2,mActivity);

        headToHeadList = new ArrayList<MatchPOJO.Match>();
        headToHeadAdapter = new MatchListAdapter(headToHeadList,mActivity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((DetailPageActivity)getActivity()).subscribeForChanges(this);
        MatchPOJO.MatchDetailed matchDetailed = mActivity.getMatchDetailed();
        if(matchDetailed != null)
            updateMatch(matchDetailed);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maclar, container, false);
        ButterKnife.bind(this,view);

        TabHost tabHost = (TabHost) view.findViewById(android.R.id.tabhost);

        tabHost.setup();


        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator(Utilities.truncateTeamName(teamHome));
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator(getString(R.string.head2head));
        tabSpec.setContent(R.id.tab2);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator(Utilities.truncateTeamName(teamAway));
        tabSpec.setContent(R.id.tab3);
        tabHost.addTab(tabSpec);

        ViewGroup.LayoutParams params = tabHost.getTabWidget().getChildAt(0).getLayoutParams();

        params.height = (int) (params.height * 0.7);
        for(int i =0 ;i < tabHost.getTabWidget().getChildCount(); i++){
            View child = tabHost.getTabWidget().getChildAt(i);
            child.setLayoutParams(params);
            child.setBackgroundResource(R.drawable.tab_selector);
            TextView tv =  (TextView) child.findViewById(android.R.id.title);
            tv.setAllCaps(false);

        }




        tabHost.setCurrentTabByTag("tag1");
        initViews();
        titleAllTeam1.setText(teamHome + " " + getString(R.string.recent5));
        titleHomeTeam1.setText(teamHome + " " + getString(R.string.recent5home));
        titleAwayTeam1.setText(teamHome + " " + getString(R.string.recent5away));


        titleAllTeam2.setText(teamAway+ " " + getString(R.string.recent5));
        titleHomeTeam2.setText(teamAway+ " " + getString(R.string.recent5home));
        titleAwayTeam2.setText(teamAway+ " " + getString(R.string.recent5away));

        titleHeadToHead.setText(getString(R.string.head2headMatches));

        return view;
    }

    private void initViews(){
        rvAllteam1.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rvAllteam1.setAdapter(allAdapterTeam1);
        rvAwayTeam1.addOnItemTouchListener(this);
        allAdapterTeam1.notifyDataSetChanged();
        rvHomeTeam1.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rvHomeTeam1.setAdapter(homeAdapterTeam1);

        rvAwayTeam1.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rvAwayTeam1.setAdapter(awayAdapterTeam1);

        rvAllteam2.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rvAllteam2.setAdapter(allAdapterTeam2);
        allAdapterTeam2.notifyDataSetChanged();
        rvHomeTeam2.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rvHomeTeam2.setAdapter(homeAdapterTeam2);

        rvAwayTeam2.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rvAwayTeam2.setAdapter(awayAdapterTeam2);

        rvHeadToHead.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rvHeadToHead.setAdapter(headToHeadAdapter);


    }
    @Override
    public void updateMatch(MatchPOJO.MatchDetailed matchDetailed) {
        mCurrentMatchId = matchDetailed.getId();
        if (null != matchDetailed.getHomeTeam()) {
            MatchPOJO.HomeTeam homeTeam = matchDetailed.getHomeTeam();
            allAdapterTeam1.setTeamId(homeTeam.getId());
            homeAdapterTeam1.setTeamId(homeTeam.getId());
            if (null != homeTeam) {
                displayChanges(all5ListTeam1, homeTeam.getAllMatches(), allAdapterTeam1);
            }
            if (null != homeTeam.getHomeMatches()) {
                displayChanges(homeListTeam1, homeTeam.getHomeMatches(), homeAdapterTeam1);
            }
            if (null != homeTeam.getAwayMatches()) {
                displayChanges(awayListTeam1, homeTeam.getAwayMatches(), awayAdapterTeam1);
            }
        }
        if( null != matchDetailed.getHeadToHead()) {
            displayChanges(headToHeadList,matchDetailed.getHeadToHead(),headToHeadAdapter);
        }

        if (null != matchDetailed.getAwayTeam()) {
            MatchPOJO.AwayTeam awayTeam = matchDetailed.getAwayTeam();
            allAdapterTeam2.setTeamId(awayTeam.getId());
            homeAdapterTeam2.setTeamId(awayTeam.getId());
            if (null != awayTeam) {
                displayChanges(all5ListTeam2, awayTeam.getAllMatches(), allAdapterTeam2);
            }
            if (null != awayTeam.getHomeMatches()) {
                displayChanges(homeListTeam2, awayTeam.getHomeMatches(), homeAdapterTeam2);
            }
            if (null != awayTeam.getAwayMatches()) {
                displayChanges(awayListTeam2, awayTeam.getAwayMatches(), awayAdapterTeam2);
            }
        }
    }

    @Override
    public void updateData() {
        MatchPOJO.MatchDetailed matchDetailed = mActivity.getMatchDetailed();
        //updateMatch(matchDetailed);



    }

    @Override
    public void updateGoalAverages() {

    }

    @Override
    public void updateStandings() {

    }

    public void displayChanges(List<MatchPOJO.Match> list, List<MatchPOJO.Match> sublist, MatchListAdapter adapter){
        list.clear();
        Collections.sort(sublist, MatchListComparator.compareMatchByDate);
        int to= sublist.size() >5 ? 5 : sublist.size();
        list.addAll(sublist.subList(0,to));
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if(e.getAction() != MotionEvent.ACTION_DOWN) return false;
        View child = rv.findChildViewUnder(e.getX(),e.getY());
        if(child.getId() == R.id.matchLayout){
            int position = rv.getChildAdapterPosition(child);
            MatchListAdapter adapter = (MatchListAdapter) rv.getAdapter();
            MatchPOJO.Match match = adapter.mList.get(position);
            if (match.getId() != mCurrentMatchId) {
                Bundle bundle = new Bundle();
                bundle.putString(DetailPageActivity.MATCH_ID,match.getId());
                bundle.putInt(DetailPageActivity.LEAGUE_ID,match.getLeagueId().getId());
                bundle.putString(DetailPageActivity.ARG_TEAM_HOME_NAME,match.getHomeTeam().getTeamNameTr());
                bundle.putString(DetailPageActivity.ARG_TEAM_AWAY_NAME,match.getAwayTeam().getTeamNameTr());
                bundle.putString(DetailPageActivity.ARG_REUSULT_TYPE,match.getResultType());
                Utilities.openMatchDetails(getActivity(),bundle);
            }
        }

        return true;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
