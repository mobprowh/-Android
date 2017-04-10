package com.bahisadam.view.DetailedPage;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.bahisadam.fragment.DetailFragment;
import com.bahisadam.model.MatchPOJO;

import java.text.DecimalFormat;

/**
 * Created by atata on 01/12/2016.
 */

public class GoalAveragesItem implements Item {
    DetailFragment.GoalAveragesHolder holder;
    MatchPOJO.HomeTeam homeTeam;
    MatchPOJO.AwayTeam awayTeam;
    Activity mActivity;

    MatchPOJO.Standing homeStandingHome;
    MatchPOJO.Standing homeStandingAway;
    MatchPOJO.Standing awayStandingHome;
    MatchPOJO.Standing awayStandingAway;

    public GoalAveragesItem(MatchPOJO.HomeTeam homeTeam,
                            MatchPOJO.AwayTeam awayTeam,
                            Activity activity) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.mActivity = activity;

    }

    public MatchPOJO.Standing getHomeStandingHome() {
        return homeStandingHome;
    }

    public void setHomeStandingHome(MatchPOJO.Standing homeStandingHome) {
        this.homeStandingHome = homeStandingHome;
    }

    public MatchPOJO.Standing getHomeStandingAway() {
        return homeStandingAway;
    }

    public void setHomeStandingAway(MatchPOJO.Standing homeStandingAway) {
        this.homeStandingAway = homeStandingAway;
    }

    public MatchPOJO.Standing getAwayStandingHome() {
        return awayStandingHome;
    }

    public void setAwayStandingHome(MatchPOJO.Standing awayStandingHome) {
        this.awayStandingHome = awayStandingHome;
    }

    public MatchPOJO.Standing getAwayStandingAway() {
        return awayStandingAway;
    }

    public void setAwayStandingAway(MatchPOJO.Standing awayStandingAway) {
        this.awayStandingAway = awayStandingAway;
    }

    @Override
    public int getItemType() {
        return TYPE_HOMEAWAY;
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder cardHolder) {
        holder =  (DetailFragment.GoalAveragesHolder) cardHolder;
        showGoalAverages();

    }
    private void showGoalAverages() {
        DecimalFormat df = new DecimalFormat("#0.0");

        holder.goalHomeTeam.setText(homeTeam.getTeamNameTr());
        if(homeStandingHome != null){
            Double ag = homeStandingHome.getAG().doubleValue() / homeStandingHome.getOM().doubleValue();
            ag = ag.isNaN() ? 0 : ag;
            holder.goalHomeAG.setText(df.format(ag));
            Double yg = homeStandingHome.getYG().doubleValue() / homeStandingHome.getOM().doubleValue();
            yg = yg.isNaN() ? 0 : yg;
            holder.goalHomeYG.setText(df.format(yg));

        }
        if(homeStandingAway != null){
            Double ag2 = homeStandingAway.getAG().doubleValue() / homeStandingAway.getOM().doubleValue();
            ag2 = ag2.isNaN() ? 0 : ag2;
            holder.goalHomeAG2.setText(df.format(ag2));
            Double yg2 = homeStandingAway.getYG().doubleValue() / homeStandingAway.getOM().doubleValue();
            yg2 = yg2.isNaN() ? 0 : yg2;
            holder.goalHomeYG2.setText(df.format(yg2));

        }


        holder.goalAwayTeam.setText(awayTeam.getTeamNameTr());
        if(awayStandingHome !=null ){
            Double ag = awayStandingHome.getAG().doubleValue() / awayStandingHome.getOM().doubleValue();
            ag = ag.isNaN() ? 0 : ag;
            holder.goalAwayAG.setText(df.format(ag));
            Double yg = awayStandingHome.getYG().doubleValue() / awayStandingHome.getOM().doubleValue();
            yg = yg.isNaN() ? 0 : yg;
            holder.goalAwayYG.setText(df.format(yg));
        }
        if(awayStandingAway !=null){
            Double ag2 = awayStandingAway.getAG().doubleValue() / awayStandingAway.getOM().doubleValue();
            ag2 = ag2.isNaN() ? 0 : ag2;
            holder.goalAwayAG2.setText(df.format(ag2));

            Double yg2 = awayStandingAway.getYG().doubleValue() / awayStandingAway.getOM().doubleValue();
            yg2 = yg2.isNaN() ? 0 : yg2;
            holder.goalAwayYG2.setText(df.format(yg2));
        }




    }
}
