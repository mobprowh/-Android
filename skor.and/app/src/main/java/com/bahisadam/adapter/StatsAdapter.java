package com.bahisadam.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bahisadam.R;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.utility.RoundedCornersTransform;
import com.bahisadam.utility.Utilities;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by atata on 10/12/2016.
 */

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsVH> {
    List<MatchPOJO.LeagueDetailStat> mList;
    Activity mActivity;

    public StatsAdapter(Activity activity) {
        this.mList = new LinkedList<MatchPOJO.LeagueDetailStat>();
        mActivity = activity;
    }
    public void clear(){
        mList.clear();
    }
    public void add(List<MatchPOJO.LeagueDetailStat>  sublist){
        mList.addAll(sublist);
    }
    public void add(MatchPOJO.LeagueDetailStat item){
        mList.add(item);
    }


    @Override
    public StatsVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StatsVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_item,parent,false));
    }

    @Override
    public void onBindViewHolder(StatsVH holder, int position) {
        MatchPOJO.LeagueDetailStat item = mList.get(position);
        holder.nameTv.setText(item.getNick());
        Picasso.with(holder.avatarIv.getContext())
                .load(item.getPlayerImage())
                .transform(new RoundedCornersTransform())
                .into(holder.avatarIv);
        Picasso.with(holder.teamLogoIv.getContext())
                .load(item.getTeamShield())
                .transform(new RoundedCornersTransform())
                .into(holder.teamLogoIv);
        holder.teamNameTv.setText(item.getTeamName());
        holder.totalTv.setText(item.getTotal().toString());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class StatsVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.playerAvatar) ImageView avatarIv;
        @BindView(R.id.playerName) TextView nameTv;
        @BindView(R.id.playerTotal) TextView totalTv;
        @BindView(R.id.teamName) TextView teamNameTv;
        @BindView(R.id.teamLogo) ImageView teamLogoIv;
        @BindView(R.id.ll_player_info) LinearLayout ll_player_info;

        public StatsVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            ll_player_info.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            MatchPOJO.LeagueDetailStat stat = mList.get(getAdapterPosition());
            Utilities.openPlayerDetails(mActivity,
                    Integer.parseInt(stat.getPlayerId()),
                    stat.getNick());
        }
    }
}