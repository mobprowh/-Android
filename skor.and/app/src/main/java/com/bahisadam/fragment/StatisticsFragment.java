package com.bahisadam.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.bahisadam.R;
import com.bahisadam.adapter.EventListAdapter;
import com.bahisadam.adapter.GameStatsAdapter;
import com.bahisadam.adapter.MatchAdapter;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.model.EventListComparator;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.view.DetailPageActivity;
import com.txusballesteros.widgets.FitChart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StatisticsFragment extends Fragment implements MatchAdapter.UpdateMatchListener,Constant {
    private static final String TAG  = StatisticsFragment.class.getSimpleName();

    List<Stat> mList;
    List<Stat> mMatchList;
    List<Events> mEventList;
    private DetailPageActivity mActivity;

    private GameStatsAdapter gameStatsAdapter;
    private GameStatsAdapter matchStatsAdapter;
    private EventListAdapter eventListAdapter;

    TabHost tabHost;
    @BindView(R.id.statisticRv) RecyclerView gameStatistics;
    @BindView(R.id.chartHomeTeam) FitChart homeTeamChart;
    @BindView(R.id.chartAwayTeam) FitChart awayTeamChart;
    @BindView(R.id.labelHomeTeam) TextView labelHomeTeam;
    @BindView(R.id.labelAwayTeam) TextView labelAwayTeam;
    @BindView(R.id.chartHomeTeamMatch) FitChart homeTeamChartMatch;
    @BindView(R.id.chartAwayTeamMatch) FitChart awayTeamChartMatch;
    @BindView(R.id.labelHomeTeamMatch) TextView labelHomeTeamMatch;
    @BindView(R.id.labelAwayTeamMatch) TextView labelAwayTeamMatch;
    @BindView(R.id.teamHomeStats) TextView teamHomeStatsName;
    @BindView(R.id.teamAwayStats) TextView teamAwayStatsName;
    @BindView(R.id.team1label) TextView teamHomeStatsNameMatch;
    @BindView(R.id.team2label) TextView teamAwayStatsNameMatch;
    @BindView(R.id.homeTeamName) TextView teamHomeStatsName2;
    @BindView(R.id.awayTeamName) TextView teamAwayStatsName2;

    @BindView(R.id.statisticMatchRv) RecyclerView matchStatistics;
    @BindView(R.id.goalsRv) RecyclerView matchRv;

    private float homePos=0;
    private float awayPos=0;
    private float matchHomePos =0;
    private float matchAwayPos = 0;

    public StatisticsFragment() {
        // Required empty public constructor
    }


    public static StatisticsFragment newInstance() {
        StatisticsFragment fragment = new StatisticsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        ButterKnife.bind(this,view);


        mList = new ArrayList<Stat>();
        mMatchList = new ArrayList<Stat>();
        mEventList = new LinkedList<Events>();

        gameStatistics.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        gameStatsAdapter  = new GameStatsAdapter(mList,new DecimalFormat("#0.0"));
        gameStatistics.setAdapter(gameStatsAdapter);

        matchStatistics.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        matchStatsAdapter = new GameStatsAdapter(mMatchList, new DecimalFormat("#0"));
        matchStatistics.setAdapter(matchStatsAdapter);
        mActivity = ((DetailPageActivity)getActivity());

        matchRv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        eventListAdapter = new EventListAdapter(mEventList,mActivity);

        matchRv.setAdapter(eventListAdapter);
        eventListAdapter.notifyDataSetChanged();
        matchStatsAdapter.notifyDataSetChanged();
        gameStatsAdapter.notifyDataSetChanged();

        tabHost = (TabHost) view.findViewById(android.R.id.tabhost);

        tabHost.setup();


        TabHost.TabSpec tabSpec;


        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator(getString(R.string.matchStatistics));
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator(getString(R.string.gameStatistics));
        tabSpec.setContent(R.id.tab2);

        tabHost.addTab(tabSpec);
        tabHost.setCurrentTabByTag("tag1");



        // tabHost.setCurrentTabByTag("tag2");

        ViewGroup.LayoutParams params = tabHost.getTabWidget().getChildAt(0).getLayoutParams();

        params.height = (int) (params.height * 0.7);
        for(int i =0 ;i < tabHost.getTabWidget().getChildCount(); i++){
            View child = tabHost.getTabWidget().getChildAt(i);
            child.setLayoutParams(params);
            child.setBackgroundResource(R.drawable.tab_selector);
        }


        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if(s.equals("tag2")){
                    homeTeamChart.setValue(homePos);
                    awayTeamChart.setValue(awayPos);
                } else
                {
                    homeTeamChartMatch.setValue(matchHomePos);
                    awayTeamChartMatch.setValue(matchAwayPos);
                }

            }
        });
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity.subscribeForChanges(this);
        updateMatch(mActivity.getMatchDetailed());
    }



    @Override
    public void updateMatch(MatchPOJO.MatchDetailed matchDetailed) {
        if(matchDetailed==null) {
            Log.e(TAG,"No match loaded");
            return;
        }
        teamAwayStatsName.setText(matchDetailed.getAwayTeam().getTeamNameTr()+ " " + getString(R.string.away));
        teamHomeStatsName.setText(matchDetailed.getHomeTeam().getTeamNameTr() + " "+getString(R.string.home));
        teamAwayStatsNameMatch.setText(matchDetailed.getAwayTeam().getTeamNameTr() + " "+ getString(R.string.away));
        teamHomeStatsNameMatch.setText(matchDetailed.getHomeTeam().getTeamNameTr() + " "+getString(R.string.home));

        teamAwayStatsName2.setText(matchDetailed.getAwayTeam().getTeamNameTr() + " " + getString(R.string.away));
        teamHomeStatsName2.setText(matchDetailed.getHomeTeam().getTeamNameTr() + " "+getString(R.string.home));
        MatchPOJO.Stats home = matchDetailed.getHomeAverageStats();
        MatchPOJO.Stats away = matchDetailed.getAwayAverageStats();
        if(home != null || away != null) {
            Double pos1 = home.getPos() != null ? home.getPos() : 0;
            Double pos2 = away.getPos() != null ? away.getPos() : 0;
            homePos = pos1.floatValue();
            awayPos = pos2.floatValue();
            homeTeamChart.setMinValue(0);
            homeTeamChart.setMaxValue(100);
            homeTeamChart.setValue(homePos);
            awayTeamChart.setMinValue(0);
            awayTeamChart.setMaxValue(100);
            awayTeamChart.setValue(awayPos);
            labelHomeTeam.setText(""+pos1.intValue());
            labelAwayTeam.setText(""+pos2.intValue());
            showGameStats(home,away,mList,gameStatsAdapter);
        }

        List<MatchPOJO.Stats> stats = matchDetailed.getStats();
        if(!"NotPlayed".equals(matchDetailed.getResult_type()) ){
            tabHost.setCurrentTabByTag("tag1");
            tabHost.getTabWidget().getChildAt(0).setEnabled(true);

        }else{
            tabHost.setCurrentTabByTag("tag2");

            tabHost.getTabWidget().getChildAt(0).setEnabled(false);

        }
        if(stats != null && stats.size() == 2)  {
            showGameStats(stats.get(0),stats.get(1),mMatchList,matchStatsAdapter);
            Double pos1 = stats.get(0).getPos() != null ? stats.get(0).getPos() :0;
            Double pos2 = stats.get(1).getPos() != null ? stats.get(1).getPos() : 0;
            matchHomePos = pos1.floatValue();
            matchAwayPos = pos2.floatValue();
            homeTeamChartMatch.setMinValue(0);
            homeTeamChartMatch.setMaxValue(100);
            homeTeamChartMatch.setValue(pos1.floatValue());
            awayTeamChartMatch.setMinValue(0);
            awayTeamChartMatch.setMaxValue(100);
            awayTeamChartMatch.setValue(pos1.floatValue());
            labelHomeTeamMatch.setText(""+pos1.intValue());
            labelAwayTeamMatch.setText(""+pos2.intValue());
        }


        if (matchDetailed.getEvents() != null){
            loadEvents(matchDetailed.getEvents());
        }
        eventListAdapter.setHomeTeamId(matchDetailed.getHomeTeam().getId());







    }
    private void loadEvents(List<MatchPOJO.Event> events){
        mEventList.clear();
        Events goals = new Events(EVENT_GOAL);
        Events assists = new Events(EVENT_ASSIST);
        Events cards = new Events(EVENT_CARD);
        Events substitutions = new Events(EVENT_SUBSTITUTION);
        for(int i=0;i< events.size(); i++){
            MatchPOJO.Event ev= events.get(i);
            if(ev.getEventType().equals(EVENT_ASSIST)){
                assists.addEvent(ev);
            } else if(ev.getEventType().equals(EVENT_GOAL)){
                goals.addEvent(ev);
            } else if(ev.getEventType().equals(EVENT_SUBSTITUTION)){
                substitutions.addEvent(ev);
            } else if(ev.getEventType().equals(EVENT_REDCARD) || ev.getEventType().equals(EVENT_YELLOWCARD)){
                cards.addEvent(ev);

            }
        }
        mEventList.add(goals);
        mEventList.add(assists);
        mEventList.add(cards);
        mEventList.add(substitutions);
        for(int i=0; i<  mEventList.size(); i++){
            Collections.sort(mEventList.get(i).getEvents(), EventListComparator.compareEventByMinute);
        }
        eventListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unsubscribe(this);
    }

    private void showGameStats(MatchPOJO.Stats home, MatchPOJO.Stats away, List<Stat> list, GameStatsAdapter adapter){
        list.clear();
        Stat fou = new      Stat(getString(R.string.fou),
                home.getFou(),
                away.getFou());
        list.add(fou);

        Stat sot = new Stat(getString(R.string.sot),
                home.getSot(),
                away.getSot());
        list.add(sot);

        Stat son = new Stat(getString(R.string.son),
                home.getSon(),
                away.getSon());
        list.add(son);

        Stat soff = new Stat(getString(R.string.soff),
                home.getSoff(),
                away.getSoff());
        list.add(soff);


        Stat blk = new Stat(getString(R.string.blk),
                home.getBlk(),
                away.getBlk());
        list.add(blk);

        Stat frk = new Stat(getString(R.string.frk),
                home.getFrk(),
                away.getFrk());
        list.add(frk);

        Stat cor = new Stat(getString(R.string.cor),
                home.getCor(),
                away.getCor());
        list.add(cor);
        Stat off = new Stat(getString(R.string.off),
                home.getOff(),
                away.getOff());
        list.add(off);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void updateData() {
        updateMatch(mActivity.getMatchDetailed());
    }

    @Override
    public void updateGoalAverages() {

    }

    @Override
    public void updateStandings() {

    }

    public class Events {
        String eventType;
        List<MatchPOJO.Event> events;

        public Events(String eventType) {
            this.eventType = eventType;
            events= new ArrayList<MatchPOJO.Event>();
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public List<MatchPOJO.Event> getEvents() {
            return events;
        }

        public void addEvent(MatchPOJO.Event event) {
            this.events.add(event);
        }
    }
    public class Stat{
        String key;
        Double valueHome;
        Double valueAway;

        public Stat(String key, Double valueHome, Double valueAway) {
            this.key = key;
            this.valueHome = valueHome;
            this.valueAway = valueAway;
        }

        public String getKey() {
            return key;
        }

        public Double getValueHome() {
            return valueHome;
        }

        public Double getValueAway() {
            return valueAway;
        }
    }

}