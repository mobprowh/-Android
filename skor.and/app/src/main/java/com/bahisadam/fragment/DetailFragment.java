package com.bahisadam.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.bahisadam.R;
import com.bahisadam.adapter.CommentsAdapter;
import com.bahisadam.adapter.MatchAdapter;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.interfaces.RestClient;
import com.bahisadam.view.DetailedPage.CommentsItem;
import com.bahisadam.view.DetailedPage.GoalAveragesItem;
import com.bahisadam.view.DetailedPage.PredictionItem;
import com.bahisadam.view.DetailedPage.StandingsItem;
import com.bahisadam.view.DetailedPage.VoteItem;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.view.DetailPageActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.txusballesteros.widgets.FitChart;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class DetailFragment extends Fragment implements Constant,CommentsAdapter.OnLikeClickListener, MatchAdapter.UpdateMatchListener {


    private static final String ARG_MATCH_ID = "MATCH_ID";
    private MatchAdapter matchAdapter;


    private View rootView;
    @BindView(R.id.rootLayout) LinearLayout rootLayout;
    public List<MatchPOJO.Comment> comments;
    private CommentsAdapter adapter;
    private Retrofit retrofit;
    private RestClient restClientAPI;
    private MatchPOJO.HomeTeam homeTeam;
    private MatchPOJO.AwayTeam awayTeam;
    StandingsItem standingsItem;
    GoalAveragesItem goalAveragesItem;
    CommentsItem commentsItem;
    String matchId;
    String resultType;
    DetailPageActivity mActivity;


    private boolean voted;
    private StandingsHolder standingsHolder;
    private GoalAveragesHolder goalAveragesHolder;
    private CommentsHolder commentsHolder;
    private View voteView;
    private View standingView;
    private View goalAveragesView;
    private View commentsView;
    private View predictionView;
    private VoteHolder voteHolder;
    private PredictionHolder predictionHolder;

    public DetailFragment() {
        // Required empty public constructor
    }


    public static DetailFragment newInstance(String matchId ) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MATCH_ID, matchId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            matchId = getArguments().getString(ARG_MATCH_ID);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        rootView = view;
        ButterKnife.bind(this,view);
        mActivity = (DetailPageActivity) getActivity();
        buildRetrofit();

        comments = new ArrayList<MatchPOJO.Comment>();
        adapter = new CommentsAdapter(getActivity(),comments);
        adapter.setOnLikeClickListener(this);
        getComments();


        standingView = LayoutInflater
                .from(rootView.getContext()).inflate(R.layout.standings_layout,null,false);
        rootLayout.addView(standingView);
        standingsHolder = new StandingsHolder(standingView);


        goalAveragesView = LayoutInflater
                .from(rootView.getContext())
                .inflate(R.layout.homeaway_layout,null,false);
        rootLayout.addView(goalAveragesView);
        goalAveragesHolder = new GoalAveragesHolder(goalAveragesView);


        commentsView = LayoutInflater.from(rootView.getContext())
                .inflate(R.layout.comments_layout,null,false);
        rootLayout.addView(commentsView);
        commentsHolder = new CommentsHolder(commentsView);


        predictionView = LayoutInflater
                .from(rootView.getContext())
                .inflate(R.layout.prediction_layout,null,false);

        rootLayout.addView(predictionView);
        predictionHolder = new PredictionHolder(predictionView);

        voteView =  LayoutInflater.from(rootView.getContext()).inflate(R.layout.vote_layout,null,false);
        rootLayout.addView(voteView,0);
        voteHolder= new VoteHolder(voteView);

        mActivity.subscribeForChanges(this);
        updateData();
        return view;
    }


    public void updateMatch(MatchPOJO.MatchDetailed match){
        if( null == match  || rootLayout == null) return;
        homeTeam = match.getHomeTeam();
        awayTeam = match.getAwayTeam();
        Double away = match.getForecast().getAway() == null ? 0 : match.getForecast().getAway().doubleValue();
        Double home = match.getForecast().getHome() == null ? 0 : match.getForecast().getHome().doubleValue();
        Double draw = match.getForecast().getDraw() == null ? 0 : match.getForecast().getDraw().doubleValue();
        //boolean voted = match.getResult_type().equals("NOT_PLAYED");
        boolean voted = false;
        VoteItem voteItem = new VoteItem(
                home,away,draw,
                homeTeam.getTeamNameTr().split(" ")[0],
                awayTeam.getTeamNameTr().split(" ")[0],
                matchId,mActivity);
        if( !"NotPlayed".equals(match.getResult_type()) )
            voteItem.setVoted(true);
        voteItem.bindViewHolder(voteHolder);




        standingsItem = new StandingsItem(homeTeam,awayTeam,getActivity());
        standingsItem.bindViewHolder(standingsHolder);
        goalAveragesItem = new GoalAveragesItem(homeTeam,awayTeam,getActivity());


        commentsItem = new CommentsItem(comments,adapter,getActivity());
        commentsItem.bindViewHolder(commentsHolder);

        PredictionItem predictionItem = new PredictionItem(matchId,(DetailPageActivity) getActivity());
        predictionItem.setFragment(this);
        predictionItem.bindViewHolder(predictionHolder);
        int leagueId = match.getLeagueId().getId();
        homeTeam = mActivity.getHomeTeam();
        awayTeam = mActivity.getAwayTeam();



    }
    public void updateData(){
        MatchPOJO.MatchDetailed match = mActivity.getMatchDetailed();
        updateMatch(match);
        updateStandings();
        updateGoalAverages();

    }

    @Override
    public void updateGoalAverages() {
        if(goalAveragesItem == null || standingsItem == null) return;
        getHomeAwayStandings();
    }

    @Override
    public void updateStandings() {
        if( standingsItem == null) return;
        getGeneralStandings();

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
        standingsItem.bindViewHolder(standingsHolder);
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



        standingsItem.bindViewHolder(standingsHolder);
        goalAveragesItem.bindViewHolder(goalAveragesHolder);
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
                    if(commentsItem!=null && commentsHolder != null)
                        commentsItem.bindViewHolder(commentsHolder);
                }

            }

            @Override
            public void onFailure(Call<List<MatchPOJO.Comment>> call, Throwable t) {
                int x=0;
                t.printStackTrace();

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
                if(response.body().success)
                    getComments();
            }

            @Override
            public void onFailure(Call<MatchPOJO.LikeUpdate> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public class StandingsHolder extends RecyclerView.ViewHolder {
        public @BindView(R.id.homeTeamNum) TextView homeTeamNum;
        public @BindView(R.id.awayTeamNum) TextView awayTeamNum;
        public @BindView(R.id.homeTeamName) TextView homeTeamName;
        public @BindView(R.id.awayTeamName) TextView awayTeamName;
        public @BindView(R.id.homeTeamOm) TextView homeTeamOm;
        public @BindView(R.id.homeTeamG) TextView homeTeamG;
        public @BindView(R.id.homeTeamB) TextView homeTeamB;
        public @BindView(R.id.homeTeamPTS) TextView homeTeamPTS;
        public @BindView(R.id.homeTeamM) TextView homeTeamM;
        public @BindView(R.id.awayTeamOm) TextView awayTeamOm;
        public @BindView(R.id.awayTeamG) TextView awayTeamG;
        public @BindView(R.id.awayTeamB) TextView awayTeamB;
        public @BindView(R.id.awayTeamPTS) TextView awayTeamPTS;
        public @BindView(R.id.awayTeamM) TextView awayTeamM;
        public @BindView(R.id.homeTeamLogoStandings)
        ImageView homeTeamLogo;
        public @BindView(R.id.awayTeamLogoStandings) ImageView awayTeamLogo;
        public @BindView(R.id.homeTeamLayout)
        LinearLayout homeTeamLayout;
        public @BindView(R.id.awayTeamLayout) LinearLayout awayTeamLayout;
        public @BindView(R.id.standingsRoot) LinearLayout rootLayout;
        public @BindView(R.id.standings)
        CardView standingsLayout;
        public @BindView(R.id.awayStandings) LinearLayout awayTitleLayout;
        public @BindView(R.id.homeStandings) LinearLayout homeTitleLayout;
        public @BindView(R.id.generalStandings) LinearLayout generalTitleLayout;
        public @BindView(R.id.generalStandingsUnderline) View generalStandingsUnderline;
        public @BindView(R.id.homeStandingsUnderline) View homeStandingsUnderline;
        public @BindView(R.id.awayStandingsUnderline) View awayStandingsUnderline;
        public @BindView(R.id.generalStandingsText) TextView generalTv;
        public @BindView(R.id.homeStandingsText) TextView homeTv;
        public @BindView(R.id.awayStandingsText) TextView awayTv;
        public TabHost tabHost;
        public StandingsHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
            tabHost = (TabHost) view.findViewById(android.R.id.tabhost);

            tabHost.setup();
            TabHost.TabSpec tabSpec;

            Context ctx = tabHost.getContext();

            tabSpec = tabHost.newTabSpec("tag1");
            tabSpec.setIndicator(ctx.getString(R.string.main));
            tabSpec.setContent(R.id.tab1);
            tabHost.addTab(tabSpec);

            tabSpec = tabHost.newTabSpec("tag2");
            tabSpec.setIndicator(ctx.getString(R.string.homeTeam));
            tabSpec.setContent(R.id.tab1);
            tabHost.addTab(tabSpec);

            tabSpec = tabHost.newTabSpec("tag3");
            tabSpec.setContent(R.id.tab1);
            tabSpec.setIndicator(ctx.getString(R.string.awayTeam));
            tabHost.addTab(tabSpec);



            ViewGroup.LayoutParams params = tabHost.getTabWidget().getChildAt(0).getLayoutParams();

            params.height = (int) (params.height * 0.7);
            for(int i =0 ;i < tabHost.getTabWidget().getChildCount(); i++) {
                View child = tabHost.getTabWidget().getChildAt(i);
                child.setLayoutParams(params);
                child.setBackgroundResource(R.drawable.tab_selector);
                TextView tv =  (TextView) child.findViewById(android.R.id.title);
                tv.setAllCaps(false);
            }
            tabHost.setCurrentTabByTag("tag1");


        }
    }
    public class GoalAveragesHolder extends RecyclerView.ViewHolder {
        public @BindView(R.id.goalHomeTeam) TextView goalHomeTeam;
        public @BindView(R.id.goalHomeAG) TextView goalHomeAG;
        public @BindView(R.id.goalHomeAG2) TextView goalHomeAG2;
        public @BindView(R.id.goalHomeYG) TextView goalHomeYG;
        public @BindView(R.id.goalHomeYG2) TextView goalHomeYG2;
        public @BindView(R.id.goalAwayTeam) TextView goalAwayTeam;
        public @BindView(R.id.goalAwayAG) TextView goalAwayAG;
        public @BindView(R.id.goalAwayAG2) TextView goalAwayAG2;
        public @BindView(R.id.goalAwayYG) TextView goalAwayYG;
        public @BindView(R.id.goalAwayYG2) TextView goalAwayYG2;
        public GoalAveragesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public class CommentsHolder extends RecyclerView.ViewHolder {
        public @BindView(R.id.noComments) TextView noComments;
        public @BindView(R.id.comments) RecyclerView commentsView;

        public CommentsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
    public class PredictionHolder extends RecyclerView.ViewHolder {
        public @BindView(R.id.oddsIdda1) TextView oddsIdda1;
        public @BindView(R.id.oddsIddaX) TextView oddsIddaX;
        public @BindView(R.id.oddsIdda2) TextView oddsIdda2;
        public @BindView(R.id.IY1) TextView IY1;
        public @BindView(R.id.IYX) TextView IYX;
        public @BindView(R.id.IY2) TextView IY2;
        public @BindView(R.id.idda1x) TextView idda1x;
        public @BindView(R.id.idda12) TextView idda12;
        public @BindView(R.id.iddax2) TextView iddax2;
        public @BindView(R.id.h1) TextView h1;
        public @BindView(R.id.hx) TextView hx;
        public @BindView(R.id.h2) TextView h2;
        public @BindView(R.id.alt) TextView alt;
        public @BindView(R.id.ust) TextView ust;
        public @BindView(R.id.alt15) TextView alt15;
        public @BindView(R.id.ust15) TextView ust15;
        public @BindView(R.id.alt35) TextView alt35;
        public @BindView(R.id.ust35) TextView ust35;
        public @BindView(R.id.kgv) TextView kgv;
        public @BindView(R.id.kgy) TextView kgy;
        public @BindView(R.id.gs01) TextView gs01;
        public @BindView(R.id.gs23) TextView gs23;
        public @BindView(R.id.gs46) TextView gs46;
        public @BindView(R.id.gs7p) TextView gs7p;
        public @BindView(R.id.sf1x) TextView sf1x;
        public @BindView(R.id.sfx1) TextView sfx1;
        public @BindView(R.id.sf11) TextView sf11;
        public @BindView(R.id.sf12) TextView sf12;
        public @BindView(R.id.sf21) TextView sf21;
        public @BindView(R.id.sf22) TextView sf22;
        public @BindView(R.id.sf2X) TextView sf2x;
        public @BindView(R.id.sfx2) TextView sfx2;
        public @BindView(R.id.sfxx) TextView sfxx;

        public @BindView(R.id.submit) TextView submit;
        public @BindView(R.id.reason) EditText reasonET;
        public View view;
        public PredictionHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            view = itemView;
        }
    }
    public class VoteHolder  extends RecyclerView.ViewHolder{
        public @BindView(R.id.chart1)
        FitChart chart1;
        public @BindView(R.id.chartx) FitChart chartx;
        public @BindView(R.id.chart2) FitChart chart2;
        public @BindView(R.id.labelX) TextView labelX;
        public @BindView(R.id.label1) TextView label1;
        public @BindView(R.id.label2) TextView label2;
        public @BindView(R.id.chartsHomeTeam) TextView chartsHomeTeam;
        public @BindView(R.id.chartsAwayTeam) TextView chartsAwayTeam;
        public @BindView(R.id.vote1) FrameLayout vote1;
        public @BindView(R.id.vote2) FrameLayout vote2;
        public @BindView(R.id.voteX) FrameLayout voteX;
        public VoteHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

}
