package com.bahisadam.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bahisadam.Cache;
import com.bahisadam.MyApplication;
import com.bahisadam.R;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.utility.Utilities;
import com.bahisadam.view.DetailPageActivity;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by atata on 02/12/2016.
 */

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.MaclarItemVH> implements Constant {
    public List<MatchPOJO.Match> mList;
    Activity mActivity;
    int teamId;

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public MatchListAdapter(List<MatchPOJO.Match> mList, Activity activity) {
        this.mList = mList;
        this.mActivity = activity;
    }

    @Override
    public MaclarItemVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MaclarItemVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.maclar_item,parent,false));
    }

    @Override
    public void onBindViewHolder(MaclarItemVH holder, final int position) {
        MatchPOJO.Match m = mList.get(position);
        MatchPOJO.HomeTeam homeTeam = m.getHomeTeam();
        MatchPOJO.AwayTeam awayTeam = m.getAwayTeam();
        if(homeTeam == null || awayTeam == null) return;
        String team1 = homeTeam.getTeamNameTr().length() > 10 ? m.getHomeTeam().getTeamNameTr().substring(0,10) + "...": m.getHomeTeam().getTeamNameTr();
        String team2 = awayTeam.getTeamNameTr().length() > 10 ? m.getAwayTeam().getTeamNameTr().substring(0,10) + "...": m.getAwayTeam().getTeamNameTr();

        holder.matchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchPOJO.Match match = mList.get(position);
                Context ctx = view.getContext();
                if(view.getContext() instanceof Activity){
                    Activity activity = (Activity) ctx;
                    Bundle bundle = new Bundle();
                    bundle.putString(DetailPageActivity.MATCH_ID,match.getId());
                    bundle.putInt(DetailPageActivity.LEAGUE_ID,match.getLeagueId().getId());
                    bundle.putString(DetailPageActivity.ARG_TEAM_HOME_NAME,match.getHomeTeam().getTeamNameTr());
                    bundle.putString(DetailPageActivity.ARG_TEAM_AWAY_NAME,match.getAwayTeam().getTeamNameTr());
                    bundle.putString(DetailPageActivity.ARG_REUSULT_TYPE,match.getResultType());
                    Utilities.openMatchDetails(activity,bundle);
                }
            }
        });
        holder.team1Name.setText(team1);
        holder.team2Name.setText(team2);
        Calendar cal = Utilities.parseDate(m.getMatchDate());
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month= cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        //String dateStr = day + "." + month + "." + year;
       // String lnthFix = day < 10 ? " " : "";
        String dateStr = SHORTMONTH[month] + " " + day;// + lnthFix;

        holder.matchDate.setText(dateStr);

        int home = m.getHomeGoals();
        int away = m.getAwayGoals();
        String scoreStr = home + " : " + away;
        holder.score.setText(scoreStr);
        if(home==away){
            holder.matchStatus.setText("B");
            holder.matchStatus.setBackgroundResource(R.drawable.shape_yellow_maclar);
        } else if( isTeamWinner(home,away, m.getHomeTeam().getId() == teamId) ){
            holder.matchStatus.setText("G");
            holder.matchStatus.setBackgroundResource(R.drawable.shape_green_maclar);
        } else  {
            holder.matchStatus.setText("M");
            holder.matchStatus.setBackgroundResource(R.drawable.shape_red_maclar);
        }

        String homeStr = m.getHalfTimeHomeScore() != null ? m.getHalfTimeHomeScore().toString() : "";
        String awayStr = m.getHalfTimeAwayScore() != null ? m.getHalfTimeAwayScore().toString() : "";
        String res = mActivity.getString(R.string.iv) + " " + homeStr + " : " + awayStr;
        if("".equals(homeStr) || "".equals(awayStr)) {
            holder.halfTImeScore.setVisibility(View.GONE);
        }else {
            holder.halfTImeScore.setText(res);
        }

        if(MyApplication.sUse_Logo){
            Utilities.loadLogoToView(holder.team1Logo,m.getHomeTeam().getId());
            Utilities.loadLogoToView(holder.team2logo,m.getAwayTeam().getId());

        } else
        {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Bitmap team1logo  = getBitmap(m.getHomeTeam().getColor1(),
                        m.getHomeTeam().getColor2());
                holder.team1Logo.setImageBitmap(team1logo);
                Bitmap team2logo = getBitmap(m.getAwayTeam().getColor1(),
                        m.getAwayTeam().getColor2());
                holder.team2logo.setImageBitmap(team2logo);
            }
        }
    }
  
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Bitmap getBitmap(String color1, String color2){
        color1 = color1 == null ? "" : color1;
        color2 = color2 == null ? "" : color2;
        Bitmap bitmap = Cache.getBitmap(color1+color2);
        if(bitmap== null){
            bitmap = Utilities.paintTeamIcon(mActivity,color1,color2);
            Cache.addBitmap(color1+color2,bitmap);
        }
        return bitmap;
    }
    private boolean isTeamWinner(int home,int away,boolean isHomeTeam){
        if(isHomeTeam)
            return home > away ? true : false;
        else
            return home < away ? true : false;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MaclarItemVH extends RecyclerView.ViewHolder  {


        @BindView(R.id.team1) TextView team1Name;
        @BindView(R.id.team2) TextView team2Name;
        @BindView(R.id.team1logo) ImageView team1Logo;
        @BindView(R.id.team2logo) ImageView team2logo;
        @BindView(R.id.score) TextView score;
        @BindView(R.id.matchDate) TextView matchDate;
        @BindView(R.id.matchStatus) TextView matchStatus;
        @BindView(R.id.matchLayout) LinearLayout matchLayout;
        @BindView(R.id.halfTimeScore) TextView halfTImeScore;
        @BindView(R.id.ll_team1) LinearLayout ll_team1;
        @BindView(R.id.ll_team2) LinearLayout ll_team2;

        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MatchPOJO.Match match = mList.get(getAdapterPosition());
                int id;
                if(v.getId() == R.id.ll_team1) {
                    id = match.getHomeTeam().getId();
                } else {
                    id = match.getAwayTeam().getId();
                }
                Utilities.openTeamDetails(mActivity,id);
            }
        };


        public MaclarItemVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            ll_team1.setOnClickListener(onClick);
            ll_team2.setOnClickListener(onClick);

        }
    }
}