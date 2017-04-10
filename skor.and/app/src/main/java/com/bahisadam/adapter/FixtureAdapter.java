package com.bahisadam.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bahisadam.MyApplication;
import com.bahisadam.R;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.utility.Utilities;
import com.bahisadam.view.DetailPageActivity;
import com.bahisadam.view.LeagueDetailsActivity;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by atata on 10/12/2016.
 */

public class FixtureAdapter  extends SectionedAdapter<RecyclerView.ViewHolder> implements Constant {
    public List<MatchPOJO.Group> mData;
    public Activity mActivity;
    Integer leagueId;
    String[] SHORTMONTH = {"Oca","Şub","Mar","Nis","May","Haz","Tem","Ağu","Eyl","Eki","Kas","Ara"};

    Map<String,String> groupNames;

    public FixtureAdapter(Integer leagueId,Activity activity) {
        mData = new LinkedList<MatchPOJO.Group>();
        this.leagueId = leagueId;
        groupNames = new HashMap<String,String>();
        groupNames.put("1" ,"A Grubu");
        groupNames.put("2" ,"B Grubu");
        groupNames.put("3" ,"C Grubu");
        groupNames.put("4" ,"D Grubu");
        groupNames.put("5" ,"E Grubu");
        groupNames.put("6" ,"F Grubu");
        groupNames.put("7" ,"G Grubu");
        groupNames.put("8" ,"H Grubu");
        groupNames.put("9" ,"I Grubu");
        groupNames.put("10", "J Grubu");
        groupNames.put("11", "K Grubu");
        groupNames.put("12", "L Grubu");
        groupNames.put("13", "M Grubu");
        groupNames.put("14", "N Grubu");
        groupNames.put("15", "M Grubu");
        groupNames.put("16", "O Grubu");
        mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM)
            return new FixtureItemVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.fixture_item,parent,false));
        else
            return new FixtureHeaderVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.fixture_header,parent,false));



    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int section, int relativePosition, int absolutePosition) {
        final MatchPOJO.Match match = mData.get(section).getMatches().get(relativePosition);
        FixtureItemVH holder = (FixtureItemVH) viewHolder;
        holder.team1tv.setText(Utilities.truncateTeamName(match.getHomeTeam().getTeamNameTr()));
        holder.team2tv.setText(Utilities.truncateTeamName(match.getAwayTeam().getTeamNameTr()));


        if(MyApplication.sUse_Logo){
            Utilities.loadLogoToView(holder.team1Iv,match.getHomeTeam().getId());
            Utilities.loadLogoToView(holder.team2Iv,match.getAwayTeam().getId());
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Bitmap team1logo = Utilities.paintTeamIcon(holder.team1Iv.getContext(),
                        match.getHomeTeam().getColor1(),
                        match.getHomeTeam().getColor2());
                Bitmap team2logo = Utilities.paintTeamIcon(holder.team2Iv.getContext(),
                        match.getAwayTeam().getColor1(),
                        match.getAwayTeam().getColor2());
                holder.team1Iv.setImageBitmap(team1logo);
                holder.team2Iv.setImageBitmap(team2logo);
            }
        }


        if(match.getResultType().equals("Played")){
            holder.matchDataeTv.setVisibility(View.GONE);
            holder.matchTimeTv.setVisibility(View.GONE);
            holder.scoreTv.setVisibility(View.VISIBLE);
            holder.scoreTv.setText(match.getHomeGoals() + " : " + match.getAwayGoals());
            //holder.scoreTv.setBackgroundColor(Color.parseColor("#e1e6ea"));
            holder.scoreTv.setBackgroundResource(R.drawable.rounded_grey);

            String homeStr = match.getHalfTimeHomeScore() != null ? match.getHalfTimeHomeScore().toString() : "";
            String awayStr = match.getHalfTimeAwayScore() != null ? match.getHalfTimeAwayScore().toString() : "";
            if(homeStr.isEmpty() || awayStr.isEmpty()){
                holder.halfTimeScoreTv.setVisibility(View.GONE);

            } else {
                holder.halfTimeScoreTv.setVisibility(View.VISIBLE);
                String res = holder.halfTimeScoreTv.getContext().getString(R.string.iv) + " " + homeStr + " : " + awayStr;
                holder.halfTimeScoreTv.setText(res);
            }




        } else {
            holder.matchDataeTv.setVisibility(View.VISIBLE);
            holder.matchTimeTv.setVisibility(View.VISIBLE);
            holder.scoreTv.setVisibility(View.GONE);
            holder.halfTimeScoreTv.setVisibility(View.GONE);
            Date d = Utilities.parseJSONDate(match.getMatchDate());
            holder.matchDataeTv.setText(Utilities.formatDate(d, "dd MMM"));
            holder.matchTimeTv.setText(Utilities.formatDate(d, "HH:mm"));
        }
        holder.matchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Context ctx = view.getContext();
                if(ctx instanceof LeagueDetailsActivity)    {
                    Bundle bundle = new Bundle();
                    bundle.putString(DetailPageActivity.MATCH_ID,match.getId());
                    bundle.putInt(DetailPageActivity.LEAGUE_ID,leagueId);
                    bundle.putString(DetailPageActivity.ARG_TEAM_HOME_NAME,match.getHomeTeam().getTeamNameTr());
                    bundle.putString(DetailPageActivity.ARG_TEAM_AWAY_NAME,match.getAwayTeam().getTeamNameTr());
                    bundle.putString(DetailPageActivity.ARG_REUSULT_TYPE,match.getResultType());
                    LeagueDetailsActivity activity = (LeagueDetailsActivity) view.getContext();
                    Utilities.openMatchDetails(activity,bundle);
                }

            }
        });

    }

    @Override
    public int getSectionCount() {
        return mData.size();
    }

    @Override
    public int getItemCount(int section) {
        return mData.get(section).getMatches().size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int section) {
        FixtureHeaderVH holder = (FixtureHeaderVH) viewHolder;
        if(mData.size() > 1){
            String groupName= groupNames.get(mData.get(section).getGroup());
            groupName = groupName == null ? "" : groupName;
            holder.groupTv.setText(groupName);
        } else {
            holder.headerLL.removeAllViews();
        }

    }



    public class FixtureHeaderVH extends RecyclerView.ViewHolder{
        @BindView(R.id.groupName) TextView groupTv;
        @BindView(R.id.headerLayout) LinearLayout headerLL;
        public FixtureHeaderVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public class FixtureItemVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.team1) TextView team1tv;
        @BindView(R.id.team2) TextView team2tv;
        @BindView(R.id.team1logo) ImageView team1Iv;
        @BindView(R.id.team2logo) ImageView team2Iv;
        @BindView(R.id.matchDate) TextView matchDataeTv;
        @BindView(R.id.matchTime) TextView matchTimeTv;
        @BindView(R.id.matchLayout) LinearLayout matchLayout;
        @BindView(R.id.score) TextView scoreTv;
        @BindView(R.id.halfTimeScore) TextView halfTimeScoreTv;
        @BindView(R.id.ll_team1) LinearLayout ll_team1;
        @BindView(R.id.ll_team2) LinearLayout ll_team2;

        public FixtureItemVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            ll_team1.setOnClickListener(this);
            ll_team2.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int[] positions = getSectionIndexAndRelativePosition(getAdapterPosition());
            MatchPOJO.Match match = mData.get(positions[0]).getMatches().get(positions[1]);
            int id;
            if(v.getId() == R.id.ll_team1 ) {
                id = match.getHomeTeam().getId();
            } else {
                id = match.getAwayTeam().getId();
            }
            Utilities.openTeamDetails(mActivity,id);

        }
    }
}