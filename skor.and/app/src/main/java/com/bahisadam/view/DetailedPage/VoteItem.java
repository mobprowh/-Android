package com.bahisadam.view.DetailedPage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bahisadam.R;
import com.bahisadam.fragment.DetailFragment;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.interfaces.RestClient;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.model.UpdateForecastBody;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by atata on 01/12/2016.
 */

public class VoteItem implements Item, View.OnClickListener {
    private Double away,home,draw;
    private String homeTeam="",awayTeam="";
    String matchId;
    private Boolean voted=false;
    private DetailFragment.VoteHolder holder;
    private SharedPreferences mPrefs;
    Activity mActivity;

    public VoteItem(Double home, Double away, Double draw,String homeTeam, String awayTeam,String matchId, Activity activity) {
        this.away = away;
        this.home = home;
        this.draw = draw;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchId = matchId;
        this.mActivity = activity;
        loadVoted();
    }
    public VoteItem(){

    }


    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public void setAway(Double away) {
        this.away = away;
    }

    public void setHome(Double home) {
        this.home = home;
    }

    public void setDraw(Double draw) {
        this.draw = draw;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setVoted(Boolean voted) {
        this.voted = voted;
        if(!voted)
            loadVoted();

    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder cardHolder) {
        holder = (DetailFragment.VoteHolder) cardHolder;

       /* LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(holder.vote1.getLayoutParams());
        params.height = width/6;
        params.width = width/6;
        holder.chart1.setLayoutParams(params);
        holder.chartx.setLayoutParams(params);
        holder.chart2.setLayoutParams(params); */
        holder.chartsHomeTeam.setText(homeTeam);
        holder.chartsAwayTeam.setText(awayTeam);
        holder.vote1.setOnClickListener(this);
        holder.vote2.setOnClickListener(this);
        holder.voteX.setOnClickListener(this);


        if(voted)
            showCicleCharts();
    }


    public void showCicleCharts() {
        Double totalForecast = away + home + draw;
        Double homePercent  = totalForecast ==0 ? 0: home / totalForecast * 100;
        Double awayPercent =  totalForecast ==0 ? 0:  away / totalForecast * 100;
        Double drawPercent =  totalForecast ==0 ? 0: draw / totalForecast * 100;
        totalForecast = totalForecast *100;
        holder.chart1.setMinValue(0);
        holder.chart1.setMaxValue(totalForecast.floatValue());
        holder.chart2.setMinValue(0);
        holder.chart2.setMaxValue(totalForecast.floatValue());
        holder.chartx.setMinValue(0);
        holder.chartx.setMaxValue(totalForecast.floatValue());
        holder.chart1.setValue(home.floatValue()*100);
        holder.chart2.setValue(away.floatValue()*100);
        holder.chartx.setValue(draw.floatValue()*100);

        NumberFormat df = new DecimalFormat("#0.##");
        holder.label1.setText(df.format(homePercent) + "%");
        holder.labelX.setText(df.format(drawPercent) + "%");
        holder.label2.setText(df.format(awayPercent) + "%");
    }

    @Override
    public void onClick(View view) {
        String key="";
        if(voted)return;
        switch(view.getId()){
            case R.id.vote1 : home = home + 1 ; key="home"; break;
            case R.id.vote2 : away = away + 1; key = "away"; break;
            case R.id.voteX : draw = draw + 1; key = "draw"; break;
        }


        Gson gson = new GsonBuilder()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.ROOT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // prepare call in Retrofit 2.0
        RestClient restClientAPI = retrofit.create(RestClient.class);
        Call<MatchPOJO.ForecastUpdate> call = restClientAPI.updateForecastRequest(new UpdateForecastBody(matchId, key));
        call.enqueue(new Callback<MatchPOJO.ForecastUpdate>() {
            @Override
            public void onResponse(Call<MatchPOJO.ForecastUpdate> call, Response<MatchPOJO.ForecastUpdate> response) {
                int x=0;
                if(response.body().getSuccess()){
                    voted = true;
                    saveVoted();
                    showCicleCharts();

                }


            }

            @Override
            public void onFailure(Call<MatchPOJO.ForecastUpdate> call, Throwable t) {
                int x=0;
            }
        });


    }
    void saveVoted() {
        mPrefs = mActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putBoolean(matchId,true );
        ed.commit();
    }

    void loadVoted() {
        mPrefs = mActivity.getPreferences(Context.MODE_PRIVATE);
        voted = mPrefs.getBoolean(matchId, false);
    }
    @Override
    public int getItemType() {
        return TYPE_VOTE;
    }
}
