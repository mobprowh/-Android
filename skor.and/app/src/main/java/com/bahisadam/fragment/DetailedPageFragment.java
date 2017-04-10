package com.bahisadam.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bahisadam.R;
import com.bahisadam.adapter.CommentsAdapter;
import com.bahisadam.adapter.DetailedPageAdapter;
import com.bahisadam.adapter.MatchAdapter;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.interfaces.RestClient;
import com.bahisadam.view.DetailedPage.CommentsItem;
import com.bahisadam.view.DetailedPage.GoalAveragesItem;
import com.bahisadam.view.DetailedPage.Item;
import com.bahisadam.view.DetailedPage.PredictionItem;
import com.bahisadam.view.DetailedPage.StandingsItem;
import com.bahisadam.view.DetailedPage.VoteItem;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.view.DetailPageActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by atata on 01/12/2016.
 */

public  class DetailedPageFragment extends Fragment implements MatchAdapter.UpdateMatchListener, CommentsAdapter.OnLikeClickListener {



    @BindView(R.id.recyclerViewDetailPage) RecyclerView rv;
    private List<MatchPOJO.Comment> comments;
    private CommentsAdapter adapter;
    private Retrofit retrofit;
    private RestClient restClientAPI;
    List<Item> itemsList;
    private MatchPOJO.HomeTeam homeTeam;
    private MatchPOJO.AwayTeam awayTeam;
    private DetailedPageAdapter dpadapter;
    StandingsItem standingsItem;
    GoalAveragesItem goalAveragesItem;
    CommentsItem commentsItem;
    String matchId;
    String resultType;
    DetailPageActivity mActivity;
    public DetailedPageFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DetailPageActivity)getActivity()).setDetailFragment(this);
    }

    public static DetailedPageFragment newInstance(String matchId) {
        DetailedPageFragment fragment = new DetailedPageFragment();
        Bundle args = new Bundle();
        fragment.matchId = matchId;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_page, container, false);
        ButterKnife.bind(this,rootView);
        mActivity = (DetailPageActivity) getActivity();
        buildRetrofit();

        itemsList = new ArrayList<Item>();
        dpadapter = new DetailedPageAdapter(itemsList);
        rv.setAdapter(dpadapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        MatchPOJO.MatchDetailed matchDetailed = mActivity.getMatchDetailed();
        updateMatch(matchDetailed);
        comments = new ArrayList<MatchPOJO.Comment>();
        adapter = new CommentsAdapter(getActivity(),comments);
        adapter.setOnLikeClickListener(this);
        getComments();

        dpadapter.notifyDataSetChanged();
        return rootView;
    }

    public void updateMatch(MatchPOJO.MatchDetailed match){
        if( null == match ) return;
        itemsList.clear();
        homeTeam = match.getHomeTeam();
        awayTeam = match.getAwayTeam();
        Double away = match.getForecast().getAway() == null ? 0 : match.getForecast().getAway().doubleValue();
        Double home = match.getForecast().getHome() == null ? 0 : match.getForecast().getHome().doubleValue();
        Double draw = match.getForecast().getDraw() == null ? 0 : match.getForecast().getDraw().doubleValue();
        //boolean voted = match.getResult_type().equals("NOT_PLAYED");
        boolean voted = false;
        itemsList.add(new VoteItem(
                home,away,draw,
                homeTeam.getTeamNameTr().split(" ")[0],
                awayTeam.getTeamNameTr().split(" ")[0],
                matchId,mActivity));
        if( !"NotPlayed".equals(match.getResult_type()) )
            ((VoteItem)itemsList.get(0)).setVoted(true);

        standingsItem = new StandingsItem(homeTeam,awayTeam,getActivity());
        goalAveragesItem = new GoalAveragesItem(homeTeam,awayTeam,getActivity());
        itemsList.add(standingsItem);
        itemsList.add(goalAveragesItem);

        commentsItem = new CommentsItem(comments,adapter,getActivity());
        itemsList.add(commentsItem);
        itemsList.add(new PredictionItem(matchId,(DetailPageActivity) getActivity()));
        dpadapter.notifyDataSetChanged();
        int leagueId = match.getLeagueId().getId();
        homeTeam = mActivity.getHomeTeam();
        awayTeam = mActivity.getAwayTeam();
        getGeneralStandings();
        getHomeAwayStandings();


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public void updateData(){
        itemsList.clear();
        MatchPOJO.MatchDetailed match = mActivity.getMatchDetailed();
        updateMatch(match);

    }

    @Override
    public void updateGoalAverages() {

    }

    @Override
    public void updateStandings() {

    }

    public void buildRetrofit(){
        Gson gson = new GsonBuilder()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.ROOT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // prepare call in Retrofit 2.0
        restClientAPI = retrofit.create(RestClient.class);

    }

    public void getGeneralStandings(){
        MatchPOJO.Standings standings = mActivity.getGeneralStandings();
        if(standings == null) return;
        MatchPOJO.Standing homeStanding = null;
        MatchPOJO.Standing awayStanding = null;
        if( null != standings.get1()){
            homeStanding = checkGroup(standings.get1(),homeTeam.getId());
            awayStanding = checkGroup(standings.get1(),awayTeam.getId());
        }
        if(homeStanding==null && standings.get2() !=null)
            homeStanding = checkGroup(standings.get2(),homeTeam.getId());
        if(homeStanding==null && standings.get3() !=null)
            homeStanding = checkGroup(standings.get3(),homeTeam.getId());
        if(homeStanding==null && standings.get4() !=null)
            homeStanding = checkGroup(standings.get4(),homeTeam.getId());
        if(homeStanding==null && standings.get5() !=null)
            homeStanding = checkGroup(standings.get5(),homeTeam.getId());
        if(homeStanding==null && standings.get6() !=null)
            homeStanding = checkGroup(standings.get6(),homeTeam.getId());
        if(homeStanding==null && standings.get7() !=null)
            homeStanding = checkGroup(standings.get7(),homeTeam.getId());
        if(homeStanding==null && standings.get8() !=null)
            homeStanding = checkGroup(standings.get8(),homeTeam.getId());
        if(homeStanding==null && standings.get9() !=null)
            homeStanding = checkGroup(standings.get9(),homeTeam.getId());


        if(awayStanding==null && standings.get2() !=null)
            awayStanding = checkGroup(standings.get2(),awayTeam.getId());
        if(awayStanding==null && standings.get3() !=null)
            awayStanding = checkGroup(standings.get3(),awayTeam.getId());
        if(awayStanding==null && standings.get4() !=null)
            awayStanding = checkGroup(standings.get4(),awayTeam.getId());
        if(awayStanding==null && standings.get5() !=null)
            awayStanding = checkGroup(standings.get5(),awayTeam.getId());
        if(awayStanding==null && standings.get6() !=null)
            awayStanding = checkGroup(standings.get6(),awayTeam.getId());
        if(awayStanding==null && standings.get7() !=null)
            awayStanding = checkGroup(standings.get7(),awayTeam.getId());
        if(awayStanding==null && standings.get8() !=null)
            awayStanding = checkGroup(standings.get8(),awayTeam.getId());
        if(awayStanding==null && standings.get9() !=null)
            awayStanding = checkGroup(standings.get9(),awayTeam.getId());

        standingsItem.setHomeStanding(homeStanding);
        standingsItem.setAwayStanding(awayStanding);
        dpadapter.notifyDataSetChanged();
    }

    public void getHomeAwayStandings(){
        MatchPOJO.Standings standingsHome = mActivity.getGAstandingsHome();
        MatchPOJO.Standings standingsAway = mActivity.getGAstandingsAway();
        if(standingsAway == null || standingsHome == null) return;
        MatchPOJO.Standing homeStandingHome = null;
        MatchPOJO.Standing homeStandingAway = null;


        if(standingsHome.get1() != null) homeStandingHome = checkGroup(standingsHome.get1(),homeTeam.getId());
        if(standingsAway.get1() != null) homeStandingAway = checkGroup(standingsAway.get1(),homeTeam.getId());


        homeStandingHome =  homeStandingHome == null && standingsHome.get2() != null ?
                checkGroup(standingsHome.get2(),homeTeam.getId()) : homeStandingHome;
        homeStandingAway = homeStandingAway == null && standingsAway.get2() != null ?
                checkGroup(standingsAway.get2(),homeTeam.getId()) : homeStandingAway;

        homeStandingHome =  homeStandingHome == null && standingsHome.get3() != null ?
                checkGroup(standingsHome.get3(),homeTeam.getId()) : homeStandingHome;
        homeStandingAway = homeStandingAway == null && standingsAway.get3() != null ?
                checkGroup(standingsAway.get3(),homeTeam.getId()) : homeStandingAway;

        homeStandingHome =  homeStandingHome == null && standingsHome.get4() != null ?
                checkGroup(standingsHome.get4(),homeTeam.getId()) : homeStandingHome;
        homeStandingAway = homeStandingAway == null && standingsAway.get4() != null ?
                checkGroup(standingsAway.get4(),homeTeam.getId()) : homeStandingAway;

        homeStandingHome =  homeStandingHome == null && standingsHome.get5() != null ?
                checkGroup(standingsHome.get5(),homeTeam.getId()) : homeStandingHome;
        homeStandingAway = homeStandingAway == null && standingsAway.get5() != null ?
                checkGroup(standingsAway.get5(),homeTeam.getId()) : homeStandingAway;

        homeStandingHome =  homeStandingHome == null && standingsHome.get6() != null ?
                checkGroup(standingsHome.get6(),homeTeam.getId()) : homeStandingHome;
        homeStandingAway = homeStandingAway == null && standingsAway.get6() != null ?
                checkGroup(standingsAway.get6(),homeTeam.getId()) : homeStandingAway;

        homeStandingHome =  homeStandingHome == null && standingsHome.get7() != null ?
                checkGroup(standingsHome.get7(),homeTeam.getId()) : homeStandingHome;
        homeStandingAway = homeStandingAway == null && standingsAway.get7() != null ?
                checkGroup(standingsAway.get7(),homeTeam.getId()) : homeStandingAway;

        homeStandingHome =  homeStandingHome == null && standingsHome.get8() != null ?
                checkGroup(standingsHome.get8(),homeTeam.getId()) : homeStandingHome;
        homeStandingAway = homeStandingAway == null && standingsAway.get8() != null ?
                checkGroup(standingsAway.get8(),homeTeam.getId()) : homeStandingAway;

        homeStandingHome =  homeStandingHome == null && standingsHome.get9() != null ?
                checkGroup(standingsHome.get9(),homeTeam.getId()) : homeStandingHome;
        homeStandingAway = homeStandingAway == null && standingsAway.get9() != null ?
                checkGroup(standingsAway.get9(),homeTeam.getId()) : homeStandingAway;



        MatchPOJO.Standing awayStandingHome = null;
        MatchPOJO.Standing awayStandingAway = null;
        if(standingsHome.get1() != null) awayStandingHome = checkGroup(standingsHome.get1(),awayTeam.getId());
        if(standingsAway.get1() != null) awayStandingAway = checkGroup(standingsAway.get1(),awayTeam.getId());

        awayStandingHome =  awayStandingHome == null && standingsHome.get2() != null ?
                checkGroup(standingsHome.get2(),awayTeam.getId()) : awayStandingHome;
        awayStandingAway = awayStandingAway == null && standingsAway.get2() != null ?
                checkGroup(standingsAway.get2(),awayTeam.getId()) : awayStandingAway;

        awayStandingHome =  awayStandingHome == null && standingsHome.get3() != null ?
                checkGroup(standingsHome.get3(),awayTeam.getId()) : awayStandingHome;
        awayStandingAway = awayStandingAway == null && standingsAway.get3() != null ?
                checkGroup(standingsAway.get3(),awayTeam.getId()) : awayStandingAway;

        awayStandingHome =  awayStandingHome == null && standingsHome.get4() != null ?
                checkGroup(standingsHome.get4(),awayTeam.getId()) : awayStandingHome;
        awayStandingAway = awayStandingAway == null && standingsAway.get4() != null ?
                checkGroup(standingsAway.get4(),awayTeam.getId()) : awayStandingAway;

        awayStandingHome =  awayStandingHome == null && standingsHome.get5() != null ?
                checkGroup(standingsHome.get5(),awayTeam.getId()) : awayStandingHome;
        awayStandingAway = awayStandingAway == null && standingsAway.get5() != null ?
                checkGroup(standingsAway.get5(),awayTeam.getId()) : awayStandingAway;

        awayStandingHome =  awayStandingHome == null && standingsHome.get6() != null ?
                checkGroup(standingsHome.get6(),awayTeam.getId()) : awayStandingHome;
        awayStandingAway = awayStandingAway == null && standingsAway.get6() != null ?
                checkGroup(standingsAway.get6(),awayTeam.getId()) : awayStandingAway;

        awayStandingHome =  awayStandingHome == null && standingsHome.get7() != null ?
                checkGroup(standingsHome.get7(),awayTeam.getId()) : awayStandingHome;
        awayStandingAway = awayStandingAway == null && standingsAway.get7() != null ?
                checkGroup(standingsAway.get7(),awayTeam.getId()) : awayStandingAway;

        awayStandingHome =  awayStandingHome == null && standingsHome.get8() != null ?
                checkGroup(standingsHome.get8(),awayTeam.getId()) : awayStandingHome;
        awayStandingAway = awayStandingAway == null && standingsAway.get8() != null ?
                checkGroup(standingsAway.get8(),awayTeam.getId()) : awayStandingAway;

        awayStandingHome =  awayStandingHome == null && standingsHome.get9() != null ?
                checkGroup(standingsHome.get9(),awayTeam.getId()) : awayStandingHome;
        awayStandingAway = awayStandingAway == null && standingsAway.get9() != null ?
                checkGroup(standingsAway.get9(),awayTeam.getId()) : awayStandingAway;



        standingsItem.setAwayStandingAway(awayStandingAway);
        standingsItem.setAwayStandingHome(awayStandingHome);
        standingsItem.setHomeStandingAway(homeStandingAway);
        standingsItem.setHomeStandingHome(homeStandingHome);

        goalAveragesItem.setAwayStandingAway(awayStandingAway);
        goalAveragesItem.setAwayStandingHome(awayStandingHome);
        goalAveragesItem.setHomeStandingAway(homeStandingAway);
        goalAveragesItem.setHomeStandingHome(homeStandingHome);



        dpadapter.notifyDataSetChanged();
    }


    public void getComments(){



        Call<List<MatchPOJO.Comment>> call = restClientAPI.commentsRequest(matchId);

        //asynchronous call
        call.enqueue(new Callback<List<MatchPOJO.Comment>>() {
            @Override
            public void onResponse(Call<List<MatchPOJO.Comment>> call, Response<List<MatchPOJO.Comment>> response) {
                if(response.body() != null ){
                    comments.clear();
                    comments.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<MatchPOJO.Comment>> call, Throwable t) {
                int x=0;
                t.printStackTrace();

            }
        });
    }

    public void updateGeneralStandings(int leagueId){

        Call<MatchPOJO.StandingsRequest> call = restClientAPI.standingsRequest(leagueId);

        //asynchronous call
        call.enqueue(new Callback<MatchPOJO.StandingsRequest>() {
            @Override
            public void onResponse(Call<MatchPOJO.StandingsRequest> call, Response<MatchPOJO.StandingsRequest> response) {
                MatchPOJO.Standings standings = response.body().getStandings();
                MatchPOJO.Standing homeStanding = null;
                MatchPOJO.Standing awayStanding = null;
                if( null != standings.get1()){
                    homeStanding = checkGroup(standings.get1(),homeTeam.getId());
                    awayStanding = checkGroup(standings.get1(),awayTeam.getId());
                }
                if(homeStanding==null && standings.get2() !=null)
                    homeStanding = checkGroup(standings.get2(),homeTeam.getId());
                if(homeStanding==null && standings.get3() !=null)
                    homeStanding = checkGroup(standings.get3(),homeTeam.getId());
                if(homeStanding==null && standings.get4() !=null)
                    homeStanding = checkGroup(standings.get4(),homeTeam.getId());
                if(homeStanding==null && standings.get5() !=null)
                    homeStanding = checkGroup(standings.get5(),homeTeam.getId());
                if(homeStanding==null && standings.get6() !=null)
                    homeStanding = checkGroup(standings.get6(),homeTeam.getId());
                if(homeStanding==null && standings.get7() !=null)
                    homeStanding = checkGroup(standings.get7(),homeTeam.getId());
                if(homeStanding==null && standings.get8() !=null)
                    homeStanding = checkGroup(standings.get8(),homeTeam.getId());
                if(homeStanding==null && standings.get9() !=null)
                    homeStanding = checkGroup(standings.get9(),homeTeam.getId());


                if(awayStanding==null && standings.get2() !=null)
                    awayStanding = checkGroup(standings.get2(),awayTeam.getId());
                if(awayStanding==null && standings.get3() !=null)
                    awayStanding = checkGroup(standings.get3(),awayTeam.getId());
                if(awayStanding==null && standings.get4() !=null)
                    awayStanding = checkGroup(standings.get4(),awayTeam.getId());
                if(awayStanding==null && standings.get5() !=null)
                    awayStanding = checkGroup(standings.get5(),awayTeam.getId());
                if(awayStanding==null && standings.get6() !=null)
                    awayStanding = checkGroup(standings.get6(),awayTeam.getId());
                if(awayStanding==null && standings.get7() !=null)
                    awayStanding = checkGroup(standings.get7(),awayTeam.getId());
                if(awayStanding==null && standings.get8() !=null)
                    awayStanding = checkGroup(standings.get8(),awayTeam.getId());
                if(awayStanding==null && standings.get9() !=null)
                    awayStanding = checkGroup(standings.get9(),awayTeam.getId());

                standingsItem.setHomeStanding(homeStanding);
                standingsItem.setAwayStanding(awayStanding);
                dpadapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MatchPOJO.StandingsRequest> call, Throwable t) {

            }
        });

    }

    public void updateHomeAwayStandings(int leagueId){
        Call<MatchPOJO.HomeAwayRequest> call = restClientAPI.homeAwayRequest(leagueId);
        call.enqueue(new Callback<MatchPOJO.HomeAwayRequest>() {
            @Override
            public void onResponse(Call<MatchPOJO.HomeAwayRequest> call, Response<MatchPOJO.HomeAwayRequest> response) {
                MatchPOJO.Standings standingsHome = response.body().getHome();

                MatchPOJO.Standings standingsAway = response.body().getAway();

                MatchPOJO.Standing homeStandingHome = null;
                MatchPOJO.Standing homeStandingAway = null;
                //checkGroup(standingsHome.get1());
                //checkGroup(standingsAway.get1());
                if(standingsHome.get1() != null) homeStandingHome = checkGroup(standingsHome.get1(),homeTeam.getId());
                if(standingsAway.get1() != null) homeStandingAway = checkGroup(standingsAway.get1(),homeTeam.getId());


                homeStandingHome =  homeStandingHome == null && standingsHome.get2() != null ?
                        checkGroup(standingsHome.get2(),homeTeam.getId()) : homeStandingHome;
                homeStandingAway = homeStandingAway == null && standingsAway.get2() != null ?
                        checkGroup(standingsAway.get2(),homeTeam.getId()) : homeStandingAway;

                homeStandingHome =  homeStandingHome == null && standingsHome.get3() != null ?
                        checkGroup(standingsHome.get3(),homeTeam.getId()) : homeStandingHome;
                homeStandingAway = homeStandingAway == null && standingsAway.get3() != null ?
                        checkGroup(standingsAway.get3(),homeTeam.getId()) : homeStandingAway;

                homeStandingHome =  homeStandingHome == null && standingsHome.get4() != null ?
                        checkGroup(standingsHome.get4(),homeTeam.getId()) : homeStandingHome;
                homeStandingAway = homeStandingAway == null && standingsAway.get4() != null ?
                        checkGroup(standingsAway.get4(),homeTeam.getId()) : homeStandingAway;

                homeStandingHome =  homeStandingHome == null && standingsHome.get5() != null ?
                        checkGroup(standingsHome.get5(),homeTeam.getId()) : homeStandingHome;
                homeStandingAway = homeStandingAway == null && standingsAway.get5() != null ?
                        checkGroup(standingsAway.get5(),homeTeam.getId()) : homeStandingAway;

                homeStandingHome =  homeStandingHome == null && standingsHome.get6() != null ?
                        checkGroup(standingsHome.get6(),homeTeam.getId()) : homeStandingHome;
                homeStandingAway = homeStandingAway == null && standingsAway.get6() != null ?
                        checkGroup(standingsAway.get6(),homeTeam.getId()) : homeStandingAway;

                homeStandingHome =  homeStandingHome == null && standingsHome.get7() != null ?
                        checkGroup(standingsHome.get7(),homeTeam.getId()) : homeStandingHome;
                homeStandingAway = homeStandingAway == null && standingsAway.get7() != null ?
                        checkGroup(standingsAway.get7(),homeTeam.getId()) : homeStandingAway;

                homeStandingHome =  homeStandingHome == null && standingsHome.get8() != null ?
                        checkGroup(standingsHome.get8(),homeTeam.getId()) : homeStandingHome;
                homeStandingAway = homeStandingAway == null && standingsAway.get8() != null ?
                        checkGroup(standingsAway.get8(),homeTeam.getId()) : homeStandingAway;

                homeStandingHome =  homeStandingHome == null && standingsHome.get9() != null ?
                        checkGroup(standingsHome.get9(),homeTeam.getId()) : homeStandingHome;
                homeStandingAway = homeStandingAway == null && standingsAway.get9() != null ?
                        checkGroup(standingsAway.get9(),homeTeam.getId()) : homeStandingAway;



                MatchPOJO.Standing awayStandingHome = null;
                MatchPOJO.Standing awayStandingAway = null;
                if(standingsHome.get1() != null) awayStandingHome = checkGroup(standingsHome.get1(),awayTeam.getId());
                if(standingsAway.get1() != null) awayStandingAway = checkGroup(standingsAway.get1(),awayTeam.getId());

                awayStandingHome =  awayStandingHome == null && standingsHome.get2() != null ?
                        checkGroup(standingsHome.get2(),awayTeam.getId()) : awayStandingHome;
                awayStandingAway = awayStandingAway == null && standingsAway.get2() != null ?
                        checkGroup(standingsAway.get2(),awayTeam.getId()) : awayStandingAway;

                awayStandingHome =  awayStandingHome == null && standingsHome.get3() != null ?
                        checkGroup(standingsHome.get3(),awayTeam.getId()) : awayStandingHome;
                awayStandingAway = awayStandingAway == null && standingsAway.get3() != null ?
                        checkGroup(standingsAway.get3(),awayTeam.getId()) : awayStandingAway;

                awayStandingHome =  awayStandingHome == null && standingsHome.get4() != null ?
                        checkGroup(standingsHome.get4(),awayTeam.getId()) : awayStandingHome;
                awayStandingAway = awayStandingAway == null && standingsAway.get4() != null ?
                        checkGroup(standingsAway.get4(),awayTeam.getId()) : awayStandingAway;

                awayStandingHome =  awayStandingHome == null && standingsHome.get5() != null ?
                        checkGroup(standingsHome.get5(),awayTeam.getId()) : awayStandingHome;
                awayStandingAway = awayStandingAway == null && standingsAway.get5() != null ?
                        checkGroup(standingsAway.get5(),awayTeam.getId()) : awayStandingAway;

                awayStandingHome =  awayStandingHome == null && standingsHome.get6() != null ?
                        checkGroup(standingsHome.get6(),awayTeam.getId()) : awayStandingHome;
                awayStandingAway = awayStandingAway == null && standingsAway.get6() != null ?
                        checkGroup(standingsAway.get6(),awayTeam.getId()) : awayStandingAway;

                awayStandingHome =  awayStandingHome == null && standingsHome.get7() != null ?
                        checkGroup(standingsHome.get7(),awayTeam.getId()) : awayStandingHome;
                awayStandingAway = awayStandingAway == null && standingsAway.get7() != null ?
                        checkGroup(standingsAway.get7(),awayTeam.getId()) : awayStandingAway;

                awayStandingHome =  awayStandingHome == null && standingsHome.get8() != null ?
                        checkGroup(standingsHome.get8(),awayTeam.getId()) : awayStandingHome;
                awayStandingAway = awayStandingAway == null && standingsAway.get8() != null ?
                        checkGroup(standingsAway.get8(),awayTeam.getId()) : awayStandingAway;

                awayStandingHome =  awayStandingHome == null && standingsHome.get9() != null ?
                        checkGroup(standingsHome.get9(),awayTeam.getId()) : awayStandingHome;
                awayStandingAway = awayStandingAway == null && standingsAway.get9() != null ?
                        checkGroup(standingsAway.get9(),awayTeam.getId()) : awayStandingAway;



                standingsItem.setAwayStandingAway(awayStandingAway);
                standingsItem.setAwayStandingHome(awayStandingHome);
                standingsItem.setHomeStandingAway(homeStandingAway);
                standingsItem.setHomeStandingHome(homeStandingHome);

                goalAveragesItem.setAwayStandingAway(awayStandingAway);
                goalAveragesItem.setAwayStandingHome(awayStandingHome);
                goalAveragesItem.setHomeStandingAway(homeStandingAway);
                goalAveragesItem.setHomeStandingHome(homeStandingHome);



                dpadapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MatchPOJO.HomeAwayRequest> call, Throwable t) {
                int x=0;
            }
        });

    }

    private MatchPOJO.Standing checkGroup(List<MatchPOJO.Standing> standings, Integer id){
        for(int i=0; i < standings.size(); i++){
            MatchPOJO.Standing st =  standings.get(i);
            st.setNum(i+1);
            if( st.getId().getTeam().equals(id) ) {
                return st;
            }

        }
        return null;
    }


    @Override
    public void onLikeClick(int pos) {
        String commentId = comments.get(pos).getId();
        Call<MatchPOJO.LikeUpdate>  call = restClientAPI.likeUpdateRequest(commentId);
        call.enqueue(new Callback<MatchPOJO.LikeUpdate>() {
            @Override
            public void onResponse(Call<MatchPOJO.LikeUpdate> call, Response<MatchPOJO.LikeUpdate> response) {

                int x = 0;
            }

            @Override
            public void onFailure(Call<MatchPOJO.LikeUpdate> call, Throwable t) {
                int x = 0;
            }
        });
    }
}
