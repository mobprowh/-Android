package com.bahisadam.view.DetailedPage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.bahisadam.MyApplication;
import com.bahisadam.R;
import com.bahisadam.fragment.DetailFragment;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.utility.Utilities;

/**
 * Created by atata on 01/12/2016.
 */

public class StandingsItem implements Item, View.OnClickListener {
    private static final int STANDING_GENERAL = 0;

    private static final int STANDING_HOME = 1;
    private static final int STANDING_AWAY=2;

    DetailFragment.StandingsHolder holder;
    MatchPOJO.HomeTeam homeTeam;
    MatchPOJO.AwayTeam awayTeam;
    Activity mActivity;



    MatchPOJO.Standing homeStanding;
    MatchPOJO.Standing awayStanding;
    MatchPOJO.Standing homeStandingHome;
    MatchPOJO.Standing homeStandingAway;
    MatchPOJO.Standing awayStandingHome;
    MatchPOJO.Standing awayStandingAway;
    public StandingsItem(MatchPOJO.HomeTeam homeTeam,
                         MatchPOJO.AwayTeam awayTeam,
                         Activity mActivity) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.mActivity = mActivity;


    }


    public MatchPOJO.Standing getHomeStanding() {
        return homeStanding;
    }

    public void setHomeStanding(MatchPOJO.Standing homeStanding) {
        this.homeStanding = homeStanding;
    }

    public MatchPOJO.Standing getAwayStanding() {
        return awayStanding;
    }

    public void setAwayStanding(MatchPOJO.Standing awayStanding) {
        this.awayStanding = awayStanding;
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

    private Activity getActivity() {
        return mActivity;
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder cardHolder) {
        holder = (DetailFragment.StandingsHolder) cardHolder;
        holder.generalTitleLayout.setOnClickListener(this);
        holder.awayTitleLayout.setOnClickListener(this);
        holder.homeTitleLayout.setOnClickListener(this);
        holder.homeTeamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.openTeamDetails(mActivity,homeTeam.getId());
            }
        });
        holder.awayTeamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.openTeamDetails(mActivity,awayTeam.getId());
            }
        });
        TabHost tabHost = holder.tabHost;


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

        showStandings(STANDING_GENERAL);
    }

    @Override
    public int getItemType() {
        return TYPE_STANDINGS;
    }

    private void showStandings(int i) {

    //Home standings
        if(MyApplication.sUse_Logo){
            Utilities.loadLogoToView(holder.homeTeamLogo,homeTeam.getId());
            Utilities.loadLogoToView(holder.awayTeamLogo,awayTeam.getId());
        }
        else {
           if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
               LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(holder.homeTeamLayout.getLayoutParams());

               Bitmap logoHome = Utilities.paintTeamIcon(getActivity(),homeTeam.getColor1(),homeTeam.getColor2());
               holder.homeTeamLogo.setImageBitmap(logoHome);
               Bitmap logoAway = Utilities.paintTeamIcon(getActivity(),awayTeam.getColor1(),awayTeam.getColor2());
               holder.awayTeamLogo.setImageBitmap(logoAway);
            }
        }

        int team1Rating=1;
        int team2Rating=2;
        if( i == STANDING_GENERAL ){
            if(homeStanding == null || awayStanding ==null ) return;
            holder.homeTeamNum.setText(homeStanding.getNum().toString());
            holder.homeTeamName.setText(homeStanding.getTeamNameTr());
            holder.homeTeamB.setText(homeStanding.getB().toString());
            holder.homeTeamG.setText(homeStanding.getG().toString());
            holder.homeTeamOm.setText(homeStanding.getOM().toString());
            holder.homeTeamPTS.setText(homeStanding.getPTS().toString());
            holder.homeTeamM.setText(homeStanding.getM().toString());


            //away standings
            holder.awayTeamNum.setText(awayStanding.getNum().toString());
            holder.awayTeamName.setText(awayStanding.getTeamNameTr());
            holder.awayTeamB.setText(awayStanding.getB().toString());
            holder.awayTeamG.setText(awayStanding.getG().toString());
            holder.awayTeamOm.setText(awayStanding.getOM().toString());
            holder.awayTeamPTS.setText(awayStanding.getPTS().toString());
            holder.awayTeamM.setText(awayStanding.getM().toString());
            team1Rating = homeStanding.getNum();
            team2Rating = awayStanding.getNum();
        } else if( i == STANDING_HOME ){
            if(homeStandingHome == null || awayStandingHome ==null ) return;
            holder.homeTeamNum.setText(homeStandingHome.getNum().toString());
            holder.homeTeamName.setText(homeStandingHome.getTeamNameTr());
            holder.homeTeamB.setText(homeStandingHome.getB().toString());
            holder.homeTeamG.setText(homeStandingHome.getG().toString());
            holder.homeTeamOm.setText(homeStandingHome.getOM().toString());
            holder.homeTeamPTS.setText(homeStandingHome.getPTS().toString());
            holder.homeTeamM.setText(homeStandingHome.getM().toString());



            //away standings
            holder.awayTeamNum.setText(awayStandingHome.getNum().toString());
            holder.awayTeamName.setText(awayStandingHome.getTeamNameTr());
            holder.awayTeamB.setText(awayStandingHome.getB().toString());
            holder.awayTeamG.setText(awayStandingHome.getG().toString());
            holder.awayTeamOm.setText(awayStandingHome.getOM().toString());
            holder.awayTeamPTS.setText(awayStandingHome.getPTS().toString());
            holder.awayTeamM.setText(awayStandingHome.getM().toString());
            team1Rating = homeStandingHome.getNum();
            team2Rating = awayStandingHome.getNum();
        }if( i==STANDING_AWAY ){
            if(homeStandingAway == null || awayStandingAway ==null ) return;
            holder.homeTeamNum.setText(homeStandingAway.getNum().toString());
            holder.homeTeamName.setText(homeStandingAway.getTeamNameTr());
            holder.homeTeamB.setText(homeStandingAway.getB().toString());
            holder.homeTeamG.setText(homeStandingAway.getG().toString());
            holder.homeTeamOm.setText(homeStandingAway.getOM().toString());
            holder.homeTeamPTS.setText(homeStandingAway.getPTS().toString());
            holder.homeTeamM.setText(homeStandingAway.getM().toString());



            //away standings
            holder.awayTeamNum.setText(awayStandingAway.getNum().toString());
            holder.awayTeamName.setText(awayStandingAway.getTeamNameTr());
            holder.awayTeamB.setText(awayStandingAway.getB().toString());
            holder.awayTeamG.setText(awayStandingAway.getG().toString());
            holder.awayTeamOm.setText(awayStandingAway.getOM().toString());
            holder.awayTeamPTS.setText(awayStandingAway.getPTS().toString());
            holder.awayTeamM.setText(awayStandingAway.getM().toString());
            team1Rating = homeStandingAway.getNum();
            team2Rating = awayStandingAway.getNum();
        }

        View l1 = holder.rootLayout.getChildAt(6);
        View l2 = holder.rootLayout.getChildAt(5);
        holder.rootLayout.removeViewAt(6);
        holder.rootLayout.removeViewAt(5);

        if(l1.getId() == R.id.homeTeamLayout && team1Rating < team2Rating) {
            holder.rootLayout.addView(l1);
            holder.rootLayout.addView(l2);
        } else if(l2.getId() == R.id.homeTeamLayout && team1Rating < team2Rating){
            holder.rootLayout.addView(l2);
            holder.rootLayout.addView(l1);
        } else if( l1.getId() == R.id.awayTeamLayout && team2Rating < team1Rating) {
            holder.rootLayout.addView(l1);
            holder.rootLayout.addView(l2);
        } else if(l2.getId() == R.id.awayTeamLayout && team2Rating < team1Rating) {
            holder.rootLayout.addView(l2);
            holder.rootLayout.addView(l1);
        } else {
            holder.rootLayout.addView(l1);
            holder.rootLayout.addView(l2);
        }






    }

    @Override
    public void onClick(View view) {
        setGoneAllStandingsUnderlines();
        //TextView tv = (TextView)layout.getChildAt(0);
       //tv.setTextSize(TypedValue.COMPLEX_UNIT_SP ,14);

        switch (view.getId()) {
            case R.id.generalStandings :
                holder.generalStandingsUnderline.setVisibility(View.VISIBLE);

                showStandings(STANDING_GENERAL);
                break;
            case R.id.homeStandings :
                showStandings(STANDING_HOME);
                holder.homeStandingsUnderline.setVisibility(View.VISIBLE);

                break;
            case R.id.awayStandings :
                showStandings(STANDING_AWAY);
                holder.awayStandingsUnderline.setVisibility(View.VISIBLE);

                break;
            default:
                showStandings(STANDING_GENERAL);
                holder.generalStandingsUnderline.setVisibility(View.VISIBLE);

                break;
        }
    }
    private void setGoneAllStandingsUnderlines(){
       // holder.generalTv.setTextSize(TypedValue.COMPLEX_UNIT_SP ,12);
       // holder.awayTv.setTextSize(TypedValue.COMPLEX_UNIT_SP ,12);
       // holder.homeTv.setTextSize(TypedValue.COMPLEX_UNIT_SP ,12);
        holder.awayStandingsUnderline.setVisibility(View.INVISIBLE);
        holder.generalStandingsUnderline.setVisibility(View.INVISIBLE);
        holder.homeStandingsUnderline.setVisibility(View.INVISIBLE);
    }
}
