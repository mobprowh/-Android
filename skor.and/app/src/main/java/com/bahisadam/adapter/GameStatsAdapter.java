package com.bahisadam.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bahisadam.R;
import com.bahisadam.fragment.StatisticsFragment;

import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by atata on 05/12/2016.
 */

public class GameStatsAdapter extends RecyclerView.Adapter<GameStatsAdapter.GameStatsVH> {
    NumberFormat df;

    List<StatisticsFragment.Stat> mList;
    public GameStatsAdapter(List<StatisticsFragment.Stat> mList, NumberFormat df) {
        this.mList = mList;
        this.df = df;
    }
    @Override
    public GameStatsVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GameStatsVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.game_statistics_item,parent,false));
    }

    @Override
    public void onBindViewHolder(GameStatsVH holder, int position) {
        StatisticsFragment.Stat stat = mList.get(position);
        holder.statNameTv.setText(stat.getKey());

        String homeStatStr = stat.getValueHome() != null ? df.format(stat.getValueHome()) : "";
        String awayStatStr = stat.getValueAway() != null ? df.format(stat.getValueAway()) : "";
        holder.homeStatTv.setText(homeStatStr);
        holder.awayStatTv.setText(awayStatStr);
        holder.homePb.setMax((int) (stat.getValueAway() + stat.getValueHome()));
        holder.awayPb.setMax((int) (stat.getValueAway() + stat.getValueHome()));
        holder.homePb.setProgress(stat.getValueHome().intValue());
        holder.awayPb.setProgress(stat.getValueAway().intValue());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class GameStatsVH extends RecyclerView.ViewHolder{
        @BindView(R.id.homeTeamStat) TextView homeStatTv;
        @BindView(R.id.awayTeamStat) TextView awayStatTv;
        @BindView(R.id.StatName) TextView statNameTv;
        @BindView(R.id.pbHome) ProgressBar homePb;
        @BindView(R.id.pbAway) ProgressBar awayPb;

        public GameStatsVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            homePb.setRotation(180);
            homePb.setScaleY(1.3f);
            awayPb.setScaleY(1.3f);

        }
    }
}