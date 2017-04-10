package com.bahisadam.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.bahisadam.R;
import com.bahisadam.adapter.StandingsAdapter;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.interfaces.RestClient;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.model.StandingsRequest;
import com.bahisadam.utility.Utilities;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StandingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StandingsFragment extends Fragment {

    private static final int STANDING_GENERAL = 0;
    private static final int STANDING_HOME = 1;
    private static final int STANDING_AWAY=2;

    public static final String ARG_LEAGUE_ID = "LEAGUE_ID";


    private RestClient mClient;
    private StandingsAdapter mAdapter;

    private List<Group> mGenersStandings;
    private List<Group> mHomeStandings;
    private List<Group> mAwayStandings;


    private Integer mLeagueId;

    @BindView(R.id.homeStandingsLeagues) LinearLayout homeLayout;
    @BindView(R.id.awayStandingsLeagues) LinearLayout awayLayout;
    @BindView(R.id.generalStandingsLeagues) LinearLayout generalLayout;
    @BindView(R.id.underlinesLayout) LinearLayout underlines;
    @BindView(R.id.leagueStandings) RecyclerView rv;
    public StandingsFragment() {
        // Required empty public constructor
    }


    public static StandingsFragment newInstance(Integer leagueId) {
        StandingsFragment fragment = new StandingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LEAGUE_ID, leagueId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLeagueId = getArguments().getInt(ARG_LEAGUE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_standings, container, false);
        ButterKnife.bind(this,view);


        TabHost tabHost = (TabHost) view.findViewById(android.R.id.tabhost);

        tabHost.setup();


        TabHost.TabSpec tabSpec;


        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator(getString(R.string.main));
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator(getString(R.string.homeTeam));
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator(getString(R.string.awayTeam));
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
                    showStandings(STANDING_GENERAL);
                } else if(s.equals("tag2")){
                    showStandings(STANDING_HOME);
                } else if(s.equals("tag3")){
                    showStandings(STANDING_AWAY);
                }
            }
        });

        mGenersStandings = new ArrayList<Group>();
        mHomeStandings = new ArrayList<Group>();
        mAwayStandings = new ArrayList<Group>();
        mAdapter = new StandingsAdapter(getActivity());
        rv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rv.setAdapter(mAdapter);
        //mClient= Utilities.buildRetrofit();
        clickStandings(generalLayout);
       // loadGeneralStandings();
        //loadHomeAwayStandings();
        loadStandings();

        return view;
    }

    private void loadGeneralStandings() {

        Call<MatchPOJO.StandingsRequest> call = mClient.standingsRequest(mLeagueId);
        call.enqueue(new Callback<MatchPOJO.StandingsRequest>() {
            @Override
            public void onResponse(Call<MatchPOJO.StandingsRequest> call, Response<MatchPOJO.StandingsRequest> response) {
                MatchPOJO.Standings standings = response.body().getStandings();
                if(standings != null)
                    loadStandingsToList(standings, mGenersStandings);
                mAdapter.add(mGenersStandings);
            }

            @Override
            public void onFailure(Call<MatchPOJO.StandingsRequest> call, Throwable t) {
                int x=0;
            }
        });
    }
    private void loadStandings(){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(StandingsRequest.class, new JsonDeserializer<StandingsRequest>() {
                    @Override
                    public StandingsRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        JsonArray arr = json.getAsJsonArray();
                        StandingsRequest request = new StandingsRequest();
                        for (int i = 0; i < arr.size(); i++)
                            request.standings.add(new Gson().fromJson(arr.get(i), StandingsRequest.Standing.class));
                        return request;
                    }
                })
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.ROOT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // prepare call in Retrofit 2.0
        mClient=  retrofit.create(RestClient.class);
        Call<StandingsRequest> call = mClient.standings(mLeagueId);
        call.enqueue(new Callback<StandingsRequest>() {
            @Override
            public void onResponse(Call<StandingsRequest> call, Response<StandingsRequest> response) {
                for (StandingsRequest.Standing standing : response.body().getStandings()){
                    if(standing.getType().equals("total")){
                        loadStandingsToList(standing.getGroups(),mGenersStandings);
                    }else if(standing.getType().equals("home")){
                        loadStandingsToList(standing.getGroups(),mHomeStandings);
                    }else if(standing.getType().equals("away")){
                        loadStandingsToList(standing.getGroups(),mAwayStandings);
                    }
                }
            }

            @Override
            public void onFailure(Call<StandingsRequest> call, Throwable t) {
                int x=0;
                if(isAdded()) {
                    Utilities.showSnackBar(getActivity(),
                            rv,getString(R.string.unable_to_retrive_data_from_server));
                }
                Crashlytics.logException(t);
            }
        });
    }
    private void loadStandingsToList(List<StandingsRequest.Group> groups,List<Group> list){
        for(int i = 0; i < groups.size(); i++){
            Group group = new Group();
            group.mList.addAll(groups.get(i).getTeamStandings());
            list.add(group);
        }
        mAdapter.notifyDataSetChanged();
        showStandings(STANDING_GENERAL);

    }
    private void loadStandingsToList(MatchPOJO.Standings standings, List<Group> list) {
       /* if(standings.get1() != null){
            Group group = new Group();
            group.mlist.addAll(standings.get1());
            list.add(group);
        }
        if(standings.get2() != null){
            Group group = new Group();
            group.mlist.addAll(standings.get2());
            list.add(group);
        }
        if(standings.get3() != null){
            Group group = new Group();
            group.mlist.addAll(standings.get3());
            list.add(group);
        }
        if(standings.get4() != null){
            Group group = new Group();
            group.mlist.addAll(standings.get4());
            list.add(group);
        }
        if(standings.get5() != null){
            Group group = new Group();
            group.mlist.addAll(standings.get5());
            list.add(group);
        }
        if(standings.get6() != null){
            Group group = new Group();
            group.mlist.addAll(standings.get6());
            list.add(group);
        }
        if(standings.get7() != null){
            Group group = new Group();
            group.mlist.addAll(standings.get7());
            list.add(group);
        }
        if(standings.get8() != null){
            Group group = new Group();
            group.mlist.addAll(standings.get8());
            list.add(group);
        }
        if(standings.get9() != null){
            Group group = new Group();
            group.mlist.addAll(standings.get9());
            list.add(group);
        }
        */

        mAdapter.notifyDataSetChanged();

    }
    private void loadHomeAwayStandings(){
        Call<MatchPOJO.HomeAwayRequest> call = mClient.homeAwayRequest(mLeagueId);
        call.enqueue(new Callback<MatchPOJO.HomeAwayRequest>() {
            @Override
            public void onResponse(Call<MatchPOJO.HomeAwayRequest> call, Response<MatchPOJO.HomeAwayRequest> response) {
                MatchPOJO.Standings home = response.body().getHome();
                MatchPOJO.Standings away = response.body().getAway();

                if(home != null ){
                    loadStandingsToList(home,mHomeStandings);
                }
                if(away != null ){
                    loadStandingsToList(away,mAwayStandings);
                }


            }

            @Override
            public void onFailure(Call<MatchPOJO.HomeAwayRequest> call, Throwable t) {

            }
        });

    }

    @OnClick({R.id.generalStandingsLeagues,R.id.awayStandingsLeagues,R.id.homeStandingsLeagues})
    public void clickStandings(View view){


        switch (view.getId()) {
            case R.id.generalStandingsLeagues : showStandings(STANDING_GENERAL); break;
            case R.id.homeStandingsLeagues : showStandings(STANDING_HOME); break;
            case R.id.awayStandingsLeagues :showStandings(STANDING_AWAY); break;
            default:
                Log.d("tabs", view.toString());
                break;
        }

    }

    private void showStandings(int standing){
        setGoneAllStandingsUnderlines();

        switch (standing) {
            case STANDING_GENERAL :
                mAdapter.clear();
                mAdapter.add(mGenersStandings);
                mAdapter.notifyDataSetChanged();
                ((LinearLayout)underlines.getChildAt(0)).getChildAt(0).setVisibility(View.VISIBLE);
                break;
            case STANDING_HOME :
                mAdapter.clear();
                mAdapter.add(mHomeStandings);
                mAdapter.notifyDataSetChanged();
                ((LinearLayout)underlines.getChildAt(1)).getChildAt(0).setVisibility(View.VISIBLE);
                break;
            case STANDING_AWAY :
                mAdapter.clear();
                mAdapter.add(mAwayStandings);
                mAdapter.notifyDataSetChanged();
                ((LinearLayout)underlines.getChildAt(2)).getChildAt(0).setVisibility(View.VISIBLE);
                break;

        }

    }
    private void setGoneAllStandingsUnderlines() {
        for(int i=0; i< underlines.getChildCount(); i++) {
            ((LinearLayout)underlines.getChildAt(i)).getChildAt(0).setVisibility(View.INVISIBLE);
        }
    }
    public class Group{
       // public List<MatchPOJO.Standing> mlist;
        public List<StandingsRequest.TeamStanding> mList;
        public Group(){
           // mlist = new ArrayList<MatchPOJO.Standing>();
            mList = new ArrayList<StandingsRequest.TeamStanding>();
        }

    }


}
