package com.bahisadam.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bahisadam.MyApplication;
import com.bahisadam.R;
import com.bahisadam.fragment.StandingsFragment;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.model.StandingsRequest;
import com.bahisadam.utility.Utilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by atata on 09/12/2016.
 */

public class StandingsAdapter extends SectionedAdapter<StandingsAdapter.StandingVH> implements Constant {
    List<StandingsFragment.Group> mData;
    Activity activity;
    public StandingsAdapter(Activity activity) {
        this.mData = new ArrayList<StandingsFragment.Group>();
        this.activity = activity;
    }
    public void add(StandingsFragment.Group group){
        mData.add(group);
    }
    public void clear(){
        mData.clear();
    }
    public void add(List<StandingsFragment.Group> sublist){
        mData.addAll(sublist);
    }
    @Override
    public int getSectionCount() {
        return mData.size();
    }

    @Override
    public int getItemCount(int section) {
        return mData.get(section).mList.size();
    }

    @Override
    public void onBindHeaderViewHolder(StandingVH holder, int section) {

    }

    @Override
    public void onBindViewHolder(StandingVH itemHolder, int section, int relativePosition, int absolutePosition) {
        ItemVH holder = (ItemVH) itemHolder;
        //MatchPOJO.Standing standing = mData.get(section).mList.get(relativePosition);
        StandingsRequest.TeamStanding standing = mData.get(section).mList.get(relativePosition);
        String team = standing.getTeamNameTr();
        if(team == null) return;
        team = team.length() > 10 ? team.substring(0,9)+"...": team;
        holder.teamName.setText(team);
        holder.teamNum.setText(""+(relativePosition+1));

        if(MyApplication.sUse_Logo){
            Utilities.loadLogoToView(holder.teamLogoStandings,standing.getId());
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Bitmap bitmap = Utilities.paintTeamIcon(holder.teamLogoStandings.getContext(),standing.getColor1(),standing.getColor2());
                holder.teamLogoStandings.setImageBitmap(bitmap);
            }
        }

        holder.teamName.setText(standing.getTeamNameTr());
        holder.teamB.setText(standing.getB().toString());
        holder.teamG.setText(standing.getG().toString());
        holder.teamOm.setText(standing.getOM().toString());
        holder.teamPTS.setText(standing.getPTS().toString());
        holder.teamM.setText(standing.getM().toString());
        holder.teamAVG.setText(standing.getAVG().toString());
        holder.teamYG.setText(standing.getYG().toString());
        holder.teamAG.setText(standing.getAG().toString());

    }

    @Override
    public StandingVH onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_HEADER)
            return new StandingVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.standings_header_item,parent,false));
        else
            return new ItemVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.standings_item,parent,false));
    }

    public class StandingVH extends RecyclerView.ViewHolder{

        public StandingVH(View itemView) {
            super(itemView);
        }
    }
    public class ItemVH extends StandingVH implements View.OnClickListener {
        @BindView(R.id.teamNum) TextView teamNum;
        @BindView(R.id.teamLogoStandings) ImageView teamLogoStandings;
        @BindView(R.id.teamName) TextView teamName;
        @BindView(R.id.teamOm) TextView teamOm;
        @BindView(R.id.teamG) TextView teamG;
        @BindView(R.id.teamB) TextView teamB;
        @BindView(R.id.teamM) TextView teamM;
        @BindView(R.id.teamAG) TextView teamAG;
        @BindView(R.id.teamYG) TextView teamYG;
        @BindView(R.id.teamAVG) TextView teamAVG;
        @BindView(R.id.teamPTS) TextView teamPTS;
        @BindView(R.id.homeTeamLayout) LinearLayout ll_teamLayout;


        public ItemVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            ll_teamLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos[] = getSectionIndexAndRelativePosition(getAdapterPosition());
            Utilities.openTeamDetails(activity,mData.get(pos[0]).mList.get(pos[1]).getId());
        }
    }
}