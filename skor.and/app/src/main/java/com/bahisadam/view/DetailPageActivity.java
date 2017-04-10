package com.bahisadam.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bahisadam.MyApplication;
import com.bahisadam.R;
import com.bahisadam.adapter.MatchAdapter;
import com.bahisadam.fragment.DetailFragment;
import com.bahisadam.fragment.StatisticsFragment;
import com.bahisadam.fragment.DetailedPageFragment;
import com.bahisadam.fragment.IddaaFragment;
import com.bahisadam.fragment.LineupsFragment;
import com.bahisadam.fragment.MaclarFragment;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.interfaces.RestClient;
import com.bahisadam.model.MatchListComparator;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.model.requests.AddRemoveFavoriteRequest;
import com.bahisadam.utility.Preferences;
import com.bahisadam.utility.Utilities;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DetailPageActivity extends BaseActivity  implements Constant, Callback<MatchPOJO.MatchDetailed>, View.OnClickListener {

    public static final String MATCH_ID = "MATCH_ID";
    public static final String LEAGUE_ID = "LEAGUE_ID";
    public static final String ARG_TEAM_HOME_NAME = "teamHomeName";
    public static final String ARG_TEAM_AWAY_NAME = "teamAwayName";
    public static final String ARG_REUSULT_TYPE = "resultType";
    public static final String ARG_BUNDLE = "bundle";
    private String teamHomeName;
    private String teamAwayName;
    private String matchId;
    private Integer leagueId;


    DetailedPageFragment fragment;
    private RestClient restClientAPI;
    private MatchPOJO.HomeTeam homeTeam;
    private MatchPOJO.AwayTeam awayTeam;
    MatchPOJO.Standings generalStandings = null;
    MatchPOJO.Standings GAstandingsHome = null;
    MatchPOJO.Standings GAstandingsAway = null;
    private List<MatchPOJO.Comment> comments;

    Toolbar toolbar;
    //Long millis;
    MatchAdapter matchAdapter;

    MatchPOJO.MatchDetailed matchDetailed;

   // @BindView(R.id.background) ImageView background;
    @BindView(R.id.fav_icon) TextView fav_icon;
    @BindView(R.id.teamHomeLogo) ImageView homeLogo;
    @BindView(R.id.teamHome) TextView teamHome;
    @BindView(R.id.teamAwayLogo) ImageView awayLogo;
    @BindView(R.id.teamAway) TextView teamAway;
    @BindView(R.id.leagueName) TextView leagueName;
    @BindView(R.id.countdown) TextView countdown;
    @BindView(R.id.liveDetailPage) TextView liveDetail;
    @BindView(R.id.appbar) AppBarLayout appbar;
    @BindView(R.id.homeRecentGames) LinearLayout homeRecentGames;
    @BindView(R.id.awayRecentGames) LinearLayout awayRecentGames;
    @BindView(R.id.minutes) TextView liveMinutes;
    @BindView(R.id.container) ViewPager pager;
    @BindView(R.id.score) TextView score;
    @BindView(R.id.main_content)
    CoordinatorLayout rootView;
    public MatchPOJO.HomeTeam getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(MatchPOJO.HomeTeam homeTeam) {
        this.homeTeam = homeTeam;
    }

    public MatchPOJO.AwayTeam getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(MatchPOJO.AwayTeam awayTeam) {
        this.awayTeam = awayTeam;
    }

    public MatchPOJO.Standings getGeneralStandings() {
        return generalStandings;
    }

    @SuppressWarnings("unused")
    public void setGeneralStandings(MatchPOJO.Standings generalStandings) {
        this.generalStandings = generalStandings;
    }

    public MatchPOJO.Standings getGAstandingsHome() {
        return GAstandingsHome;
    }

    @SuppressWarnings("unused")
    public void setGAstandingsHome(MatchPOJO.Standings GAstandingsHome) {
        this.GAstandingsHome = GAstandingsHome;
    }

    public MatchPOJO.Standings getGAstandingsAway() {
        return GAstandingsAway;
    }

    @SuppressWarnings("unused")
    public void setGAstandingsAway(MatchPOJO.Standings GAstandingsAway) {
        this.GAstandingsAway = GAstandingsAway;
    }

    public List<MatchPOJO.Comment> getComments() {
        return comments;
    }

    public void setComments(List<MatchPOJO.Comment> comments) {
        this.comments = comments;
    }

    Handler handler = new Handler();
    Runnable iteration = new Runnable() {
        @Override
        public void run() {

            updateTime();
        }
    };

    public void unsubscribe(MatchAdapter.UpdateMatchListener subscriber){
        matchAdapter.unSubscribe(subscriber);
    }

    private long matchTimeMillis;
    //public int padding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);
        initFooterToolbar();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(ARG_BUNDLE);
        matchId = bundle.getString(MATCH_ID);
        leagueId = bundle.getInt(LEAGUE_ID,0);
        teamHomeName = bundle.getString(ARG_TEAM_HOME_NAME);
        teamAwayName = bundle.getString(ARG_TEAM_AWAY_NAME);
        String resultType = bundle.getString(ARG_REUSULT_TYPE);
        resultType = resultType == null ? "" : resultType;
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar currentBar = getSupportActionBar();
        if(currentBar != null) {

            currentBar.setDisplayHomeAsUpEnabled(true);
            currentBar.setDisplayShowHomeEnabled(true);
            currentBar.setTitle("");
        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPageMargin(0);

        if(resultType.equals(PLAYING) || resultType.equals(PLAYED)) {
            mViewPager.setCurrentItem(3);
        }

        if (resultType.equals(LINEUP)) {
            mViewPager.setCurrentItem(2);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        ButterKnife.bind(this);
        teamHome.setOnClickListener(this);
        teamAway.setOnClickListener(this);
        homeLogo.setOnClickListener(this);
        awayLogo.setOnClickListener(this);

        comments = new ArrayList<>();
        matchAdapter = new MatchAdapter();
        buildRetrofit();
        getMatchRequest(matchId);

        makeHeader();
        fav_icon.setOnClickListener(this);
        Utilities.paintFavoriteIcon(this, fav_icon, Preferences.getFavoriteMatches().contains(matchId));
    }
    @OnClick(R.id.leagueName)
    public void onLeagueClick(View view){
        try{
            int id = matchDetailed.getLeagueId().getId();
            String name = matchDetailed.getLeagueId().getLeagueNameTr();

            Utilities.openLeagueDetails(this,id,name,null);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }
    public void buildRetrofit(){
        Gson gson = new GsonBuilder()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.ROOT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // prepare call in Retrofit 2.0
        restClientAPI = retrofit.create(RestClient.class);
    }
    private void makeHeader(){
        try {
            InputStream is = getAssets().open("stadium_header.png");
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            int width = Utilities.getScreenWidth(this);
            Double height =  width / 1.875;
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap,width,height.intValue(),false);

            appbar.setBackground(new BitmapDrawable(getResources(),scaled));
      //      background.setImageBitmap(scaled);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void getMatchRequest(String id) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(MatchPOJO.Lineups.class, new JsonDeserializer<MatchPOJO.Lineups>() {
                    @Override
                    public MatchPOJO.Lineups deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

                        return json instanceof JsonArray ? null : new Gson().fromJson(json, MatchPOJO.Lineups.class);
                    }
                })
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.ROOT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // prepare call in Retrofit 2.0
        RestClient restClientAPI = retrofit.create(RestClient.class);

        Call<MatchPOJO.MatchDetailed> call = restClientAPI.matchDetailRequest(id);

        //asynchronous call
        call.enqueue(this);
    }
    private void showScore(TextView tv,Integer teamHomeResult, Integer teamAwayResult){
        tv.setVisibility(View.VISIBLE);
        String score = String.format(Locale.US, "%d  :  %d", teamHomeResult, teamAwayResult);
        tv.setText(score);
    }
    @Override
    public void onResponse(Call<MatchPOJO.MatchDetailed> call, Response<MatchPOJO.MatchDetailed> response) {
        MatchPOJO.MatchDetailed match = response.body();
        teamHome.setText(match.getHomeTeam().getTeamNameTr());
        teamAway.setText(match.getAwayTeam().getTeamNameTr());

        leagueName.setText(match.getLeagueId().getLeagueNameTr());

        countdown.setText(match.getMatch_date());
        //if(detailFragment!=null) detailFragment.updateMatch(match);
        String resultType = match.getResult_type();
        List<MatchPOJO.Match> homeMatches = match.getHomeTeam().getAllMatches();
        List<MatchPOJO.Match> awayMatches = match.getAwayTeam().getAllMatches();
        showMatchStatueses(match.getHomeTeam().getId(),homeMatches,homeRecentGames);
        showMatchStatueses(match.getAwayTeam().getId(),awayMatches,awayRecentGames);
        switch (resultType) {
            case NOT_PLAYED: {
                Date d = Utilities.parseJSONDate(match.getMatch_date());
                matchTimeMillis = d.getTime();
                liveDetail.setText("");


                updateTime();
                break;
            }
            case PLAYING: {
                if (null != match.getIs_half_time() && match.getIs_half_time()) {
                    liveDetail.setVisibility(View.VISIBLE);
                    String halfTimeStr = getString(R.string.halfTime) + " " +
                            match.getHalf_time_home_score() + " : " +
                            match.getHalf_time_away_score();
                    liveDetail.setText(halfTimeStr);

                }
                Calendar matchDate = Utilities.parseDate(match.getMatch_date());
                matchDate.setTimeZone(TimeZone.getTimeZone("UTC+2"));
                matchTimeMillis = matchDate.getTimeInMillis();
                Calendar cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone("UTC+2"));
                //long liveMinute = cal.getTimeInMillis() - matchTimeMillis;
                liveMinutes.setVisibility(View.VISIBLE);
                //  liveMinutes.setText(TimeUnit.MILLISECONDS.toMinutes(liveMinute)+"'");
                if (null != match.getLive_minute()) {
                    liveMinutes.setText(String.format(Locale.US, "%d'", match.getLive_minute()));
                    liveMinutes.setVisibility(View.VISIBLE);
                }

                countdown.setVisibility(View.GONE);
                showScore(score, match.getHome_goals(), match.getAway_goals());
                break;
            }
            case PLAYED:
                liveMinutes.setVisibility(View.VISIBLE);
                liveMinutes.setText(getString(R.string.ms));
                liveDetail.setVisibility(View.VISIBLE);
                String halfTimeStr = getString(R.string.halfTime) + " " +
                        match.getHalf_time_home_score() + " : " +
                        match.getHalf_time_away_score();
                liveDetail.setText(halfTimeStr);
                countdown.setVisibility(View.GONE);
                showScore(score, match.getHome_goals(), match.getAway_goals());

                break;
            case CANCELLED:
                //countdown.setVisibility(View.GONE);
                countdown.setText(R.string.statusCanceled);

                break;
            case POSTPONED:
                countdown.setText(R.string.postponed);

                break;
        }
        if(MyApplication.sUse_Logo) {
            Utilities.loadLogoToView(homeLogo, match.getHomeTeam().getId(), true);
            Utilities.loadLogoToView(awayLogo, match.getAwayTeam().getId(), true);
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Bitmap homeLogoBitmap = Utilities.paintTeamIcon(this,match.getHomeTeam().getColor1(),match.getHomeTeam().getColor2());
                Bitmap awayLogoBitmap = Utilities.paintTeamIcon(this,match.getAwayTeam().getColor1(),match.getAwayTeam().getColor2());

                homeLogo.setImageBitmap(homeLogoBitmap);
                awayLogo.setImageBitmap(awayLogoBitmap);
            }
        }
        matchDetailed = match;
        homeTeam = match.getHomeTeam();
        awayTeam = match.getAwayTeam();
        if(leagueId == null) leagueId = match.getLeagueId().getId();


        updateGeneralStandings(leagueId);
        updateHomeAwayStandings(leagueId);
        matchAdapter.notifyDatasetChanged(match);
        //matchAdapter.notifyDatasetChanged();

    }

    private void loadMatchStatusToView(MatchPOJO.Match match, int teamId,TextView recentGame){
        if(match.getHomeGoals().equals(match.getAwayGoals())) {
            recentGame.setText("B");
            recentGame.setBackgroundResource(R.drawable.shape_yellow);

        } else if(match.getHomeTeam() != null && match.getHomeTeam().getId() == teamId){
            if( match.getHomeGoals() > match.getAwayGoals()){
                recentGame.setText("G");
                recentGame.setBackgroundResource(R.drawable.shape_green);
            } else if(match.getAwayGoals() > match.getHomeGoals()) {
                recentGame.setText("M");

                recentGame.setBackgroundResource(R.drawable.shape_red);

            }
        } else {
            if( match.getHomeGoals() < match.getAwayGoals()){
                recentGame.setText("G");
                recentGame.setBackgroundResource(R.drawable.shape_green);
            } else if(match.getAwayGoals() < match.getHomeGoals()) {
                recentGame.setText("M");
                recentGame.setBackgroundResource(R.drawable.shape_red);

            }
        }
    }


    private void showMatchStatueses(int teamId,List<MatchPOJO.Match> matches,LinearLayout layout) {
        Collections.sort(matches, MatchListComparator.compareMatchByDate);
        for (int i = 0; i < 5 && i < matches.size(); i++) {
            MatchPOJO.Match match = matches.get(i);
            TextView recentGame = (TextView) layout.getChildAt(i);
            loadMatchStatusToView(match, teamId, recentGame);
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateTime(){
        long millis = matchTimeMillis - new Date().getTime();
        if ( millis < 0 ) {
            countdown.setText("time minus");
            return;
        }
        int seconds = (int)Math.floor((millis / 1000) % 60);
        int minutes =(int) Math.floor((millis / 1000 / 60) % 60);
        int hours = (int) Math.floor((millis / (1000 * 60 * 60)) % 24);
        int days = (int) Math.floor(millis / (1000 * 60 * 60 * 24));
        /*String dateTime = String.format("%d:%d:%d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))-
                        TimeUnit.HOURS.toSeconds(TimeUnit.MILLISECONDS.toHours(millis))


        );*/

        String daysStr = days == 0 ? "" : String.valueOf(days) +":";
        String hoursStr = hours < 10 ? "0"+hours : String.valueOf(hours);
        String minutesStr = minutes < 10 ? "0"+minutes : String.valueOf(minutes);
        String secondsStr = seconds < 10 ? "0"+seconds : String.valueOf(seconds);
        String dateStr = daysStr + hoursStr + ":" + minutesStr +":" + secondsStr;
        countdown.setText(dateStr);

        if(millis>0)
            handler.postDelayed(iteration,1000);
    }
    @Override
    public void onFailure(Call<MatchPOJO.MatchDetailed> call, Throwable t) {
        Crashlytics.logException(t);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public MatchPOJO.MatchDetailed getMatchDetailed() {
        return matchDetailed;
    }
    public void setDetailFragment(DetailedPageFragment fragment){
        this.fragment = fragment;
        matchAdapter.subscribe(fragment);
    }



    public void updateHomeAwayStandings(int leagueId){
        Call<MatchPOJO.HomeAwayRequest> call = restClientAPI.homeAwayRequest(leagueId);
        call.enqueue(new Callback<MatchPOJO.HomeAwayRequest>() {
            @Override
            public void onResponse(Call<MatchPOJO.HomeAwayRequest> call, Response<MatchPOJO.HomeAwayRequest> response) {
                GAstandingsHome = response.body().getHome();

                GAstandingsAway = response.body().getAway();

                matchAdapter.updateGoalAverages();
            }
            public void onFailure(Call<MatchPOJO.HomeAwayRequest> call, Throwable t) {

                //int x=0;
                Crashlytics.logException(t);

            }
        });

    }
    public void updateGeneralStandings(int leagueId){

        Call<MatchPOJO.StandingsRequest> call = restClientAPI.standingsRequest(leagueId);

        //asynchronous call
        call.enqueue(new Callback<MatchPOJO.StandingsRequest>() {
            @Override
            public void onResponse(Call<MatchPOJO.StandingsRequest> call, Response<MatchPOJO.StandingsRequest> response) {
                 generalStandings= response.body().getStandings();

                matchAdapter.updateStandings();

            }
            @Override
            public void onFailure(Call<MatchPOJO.StandingsRequest> call, Throwable t) {

                //int x=0;
                t.printStackTrace();
                Crashlytics.logException(t);

            }
        });

    }
    // onclick event for home/away team name or logo
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(matchDetailed==null) return;

        if(id == R.id.fav_icon){
            toggleFavoriteMatch(matchId);
            return;
        }

        if(id == R.id.teamHomeLogo || id == R.id.teamHome) {
            Utilities.openTeamDetails(this,matchDetailed.getHomeTeam().getId());
        }else{
            Utilities.openTeamDetails(this,matchDetailed.getAwayTeam().getId());
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        @BindView(R.id.recyclerViewDetailPage) RecyclerView rv;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail_page, container, false);
            ButterKnife.bind(this,rootView);

            return rootView;
        }



    }
    public void subscribeForChanges(MatchAdapter.UpdateMatchListener listener){
        matchAdapter.subscribe(listener);
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
           /* if(position ==0){

              //  fragment =  DetailedPageFragment.newInstance(matchId);
                DetailFragment fragment = DetailFragment.newInstance(matchId);
                matchAdapter.subscribe(fragment);
                return fragment;
            }
            if(position == 1){
                return MaclarFragment.newInstance(teamHomeName,teamAwayName);
            }
            if(position == 2)
            if(position == 3){
                return StatisticsFragment.newInstance();
            }
            if(position == 4) {
                return new IddaaFragment();
            }*/
            switch (position){
                case 0: return DetailFragment.newInstance(matchId);
                case 1: return MaclarFragment.newInstance(teamHomeName,teamAwayName);
                case 2: return new LineupsFragment();
                case 3: return StatisticsFragment.newInstance();
                case 4: return new IddaaFragment();

            }
            return PlaceholderFragment.newInstance(1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.detay);
                case 1:
                    return getString(R.string.matches);
                case 2:
                    return getString(R.string.lineup);
                case 3:
                    return getString(R.string.statistics);
                case 4:
                    return getString(R.string.iddaa);
            }
            return null;
        }
    }


    private void toggleFavoriteMatch(final String match_id){
        if(Preferences.isLogged()) {
            RestClient client = Utilities.buildRetrofit();
            Call<AddRemoveFavoriteRequest.Response> call;
            AddRemoveFavoriteRequest request = new AddRemoveFavoriteRequest(match_id);
            final Boolean add = !Preferences.getFavoriteMatches().contains(match_id);

            if(add){
                call= client.addFavorite(request);
            } else  {
                call = client.removeFavorite(request);
            }

            call.enqueue(new Callback<AddRemoveFavoriteRequest.Response>() {
                @Override
                public void onResponse(Call<AddRemoveFavoriteRequest.Response> call, Response<AddRemoveFavoriteRequest.Response> response) {
                    AddRemoveFavoriteRequest.Response body = response.body();

                    if(body.error == null){
                        if(add){
                            Preferences.getFavoriteMatches().add(match_id);
                            FirebaseMessaging.getInstance().subscribeToTopic(match_id);
                        }
                        else {
                            Preferences.getFavoriteMatches().remove(match_id);
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(match_id);
                        }

                        Utilities.paintFavoriteIcon(DetailPageActivity.this, fav_icon, Preferences.getFavoriteMatches().contains(match_id));

                    } else {
                        if(body.errorType.equals("Authorization")) {
                            Preferences.setIsLogged(false);
                            Utilities.login(DetailPageActivity.this);
                            return;
                        }
                        else {
                            Log.e("Unexpected error", body.toString());
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddRemoveFavoriteRequest.Response> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            Utilities.login(DetailPageActivity.this, true, new Utilities.LoginCallback() {
                @Override
                public void onSuccess() {
                    Log.i("login success", "login success");
                }
                @Override
                public void onError(String error) {
                    Preferences.setIsLogged(true);
                    Log.e("Unexpected error", error);
                }
            });

        }
    }


}
