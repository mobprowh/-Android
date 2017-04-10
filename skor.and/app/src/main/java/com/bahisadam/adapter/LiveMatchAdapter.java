package com.bahisadam.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.bahisadam.MyApplication;
import com.bahisadam.R;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.model.LiveResponse;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.utility.Utilities;
import com.bahisadam.view.DetailPageActivity;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by atata on 13/12/2016.
 */

public class LiveMatchAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder>{
    public List<LiveResponse.LiveItem> data;
    private final ArrayMap<Integer, Integer> mHeaderLocationMap;

    public LiveMatchAdapter() {
        data = new LinkedList<LiveResponse.LiveItem>();
        mHeaderLocationMap = new ArrayMap<>();

    }
    public int[] getSectionIndexAndRelativePosition(int itemPosition) {
        synchronized (mHeaderLocationMap) {
            Integer lastSectionIndex = -1;
            for (final Integer sectionIndex : mHeaderLocationMap.keySet()) {
                if (itemPosition > sectionIndex) {
                    lastSectionIndex = sectionIndex;
                } else {
                    break;
                }
            }
            return new int[]{mHeaderLocationMap.get(lastSectionIndex), itemPosition - lastSectionIndex - 1};
        }
    }
    public int getSection(int absolutePath){
        return mHeaderLocationMap.get(absolutePath);
    }
    public int allocateHeaders(){
        int count = 0;
        mHeaderLocationMap.clear();
        for (int s = 0; s < getSectionCount(); s++) {
            int itemCount = getItemCount(s);
            if ((itemCount > 0)) {
                mHeaderLocationMap.put(count, s);
                count += itemCount + 1;
            }
        }
        return count;
    }

    @Override
    public int getSectionCount() {
        return data.size();
    }

    @Override
    public int getItemCount(int section) {
        return data.get(section).getMatches().size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder headerHolder, int section) {
        LeagueVH holder = (LeagueVH) headerHolder;
        holder.leagueTv.setText(data.get(section).getLeague_name_tr());
        Context ctx = holder.img.getContext();
        int id = ctx.getResources().getIdentifier(data.get(section).getFlag(),"drawable",ctx.getPackageName());


        try {
            Drawable dr;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                dr = ctx.getResources().getDrawable(id, ctx.getTheme());
            } else {
                dr = ctx.getResources().getDrawable(id);
            }
            Bitmap bitmap = Bitmap.createBitmap(200,
                    200, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            dr.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            dr.draw(canvas);
            Bitmap rounded = Utilities.getRoundedCornerBitmap(bitmap,Utilities.getPx(ctx,50));
            holder.img.setImageBitmap(rounded);

        } catch (Resources.NotFoundException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder, int section, int relativePosition, int absolutePosition) {

        MatchVH holder = (MatchVH) itemHolder;
        if(relativePosition == 0 ) holder.divider.setVisibility(View.GONE);
        final MatchPOJO.Match match = data.get(section).getMatches().get(relativePosition);
        holder.score.setText(match.getHomeGoals() + " : " + match.getAwayGoals());
        String team1 = Utilities.truncateTeamName(match.getHomeTeam().getTeamNameTr());
        String team2 = Utilities.truncateTeamName(match.getAwayTeam().getTeamNameTr());
        holder.team1tv.setText(team1);
        holder.team2tv.setText(team2);
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

        String matchStatusStr = "-";
        if(match.getResultType().equals("Played")){
            matchStatusStr  = holder.matchStatus.getContext().getString(R.string.ms);
            //holder.score.setBackgroundResource(R.drawable.shape_green_live_played);
            //holder.score.setBackgroundColor(Color.parseColor("#e1e6ea"));
            holder.score.setBackgroundResource(R.drawable.rounded_grey);
        } else {

            try {
                if (match.getIsHalfTime()) {
                    String homeStr = match.getHalfTimeHomeScore() != null ? match.getHalfTimeHomeScore().toString() : "";
                    String awayStr = match.getHalfTimeAwayScore() != null ? match.getHalfTimeAwayScore().toString() : "";
                    String res = holder.score.getContext().getString(R.string.iv) + " " + homeStr + " : " + awayStr;
                    if("".equals(homeStr) || "".equals(awayStr)) {
                        holder.halfTimeScore.setVisibility(View.GONE);
                    }else {
                        holder.halfTimeScore.setVisibility(View.VISIBLE);
                        holder.halfTimeScore.setText(res);
                    }
                    /*holder.halfTimeScore.setText( + " " +
                            +match.getHalfTimeHomeScore() + " : "
                            + match.getHalfTimeAwayScore()); */
                }
                if(match.getLiveMinute() == null ) {
                    matchStatusStr = "-";
                    matchStatusStr = match.getLiveMinute().toString() + "'";
                }
                holder.score.setBackgroundResource(R.drawable.rounded_green);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
        if(match.getLiveMinute() != null ){
            matchStatusStr = match.getLiveMinute().toString() + "'";
            holder.matchStatus.setText(matchStatusStr);
        }


        holder.matchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_HEADER) {
            return new LeagueVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_section,parent,false));
        } else{
            return new MatchVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.live_item,parent,false));
        }


    }

    public class MatchVH extends RecyclerView.ViewHolder{
        @BindView(R.id.team1) TextView team1tv;
        @BindView(R.id.team2) TextView team2tv;
        @BindView(R.id.team1logo) ImageView team1Iv;
        @BindView(R.id.team2logo) ImageView team2Iv;
        @BindView(R.id.score) TextView score;
        @BindView(R.id.halfTimeScore) TextView halfTimeScore;
        @BindView(R.id.matchStatus) TextView matchStatus;
        @BindView(R.id.matchLayout) LinearLayout matchLayout;
        @BindView(R.id.divider) View divider;
        public MatchVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            score.setBackgroundResource(R.drawable.rounded_green);
        }
    }
    public class LeagueVH extends RecyclerView.ViewHolder{
        @BindView(R.id.img_title_logo_img) ImageView img;
        @BindView(R.id.sectionTitle) TextView leagueTv;
        public LeagueVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}