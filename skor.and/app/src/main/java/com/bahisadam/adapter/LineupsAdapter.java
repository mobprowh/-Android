package com.bahisadam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.bahisadam.R;
import com.bahisadam.adapter.ViewHolders.SectionedHeaderHolder;
import com.bahisadam.fragment.LineupsFragment;
import com.bahisadam.model.MatchPOJO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by atata on 22/12/2016.
 */
public class LineupsAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {
    List<LineupsFragment.Lineup> mData;
    @Override
    public int getSectionCount() {
        return mData.size();
    }

    public LineupsAdapter(List<LineupsFragment.Lineup> mData) {
        this.mData = mData;
    }

    @Override
    public int getItemCount(int section) {
        LineupsFragment.Lineup l = mData.get(section);
        return l.playersLocal.size() > l.playersVisitor.size() ? l.playersLocal.size() : l.playersVisitor.size() ;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder headerHolder, int section) {
        SectionedHeaderHolder holder = (SectionedHeaderHolder) headerHolder;
        Context ctx = holder.label.getContext();

        switch (mData.get(section).type){
            case LineupsFragment.Lineup.MAIN : holder.label.setVisibility(View.GONE); break;
            case LineupsFragment.Lineup.SUB:
                holder.label.setText(ctx.getString(R.string.bench));;break;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder, int section, int relativePosition, int absolutePosition) {
        ItemHolder holder = (ItemHolder) itemHolder;
        List<MatchPOJO.Player> playersLocal = mData.get(section).playersLocal;
        List<MatchPOJO.Player> playersVisitor = mData.get(section).playersVisitor;
        MatchPOJO.Player player = null;

        if(playersLocal.size() > relativePosition && playersLocal.get(relativePosition) != null) {
            player = playersLocal.get(relativePosition);
        }
        fillViews(holder.homePlayer,holder.homePlayerNumber,player);
        player = null;
        if(playersVisitor.size() > relativePosition && playersVisitor.get(relativePosition) != null){
            player = playersVisitor.get(relativePosition);
        }
        fillViews(holder.awayPlayer,holder.awayPlayerNumber,player);

    }
    public void fillViews(TextView playerTv, TextView numberTv, MatchPOJO.Player player) {
        if(player != null){
            playerTv.setText(player.name);
            if(player.jersey_number != null){
                numberTv.setText(player.jersey_number.toString());
            }

        } else {
            playerTv.setText("");
            numberTv.setText("");
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_HEADER)
            return new SectionedHeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.title_item,parent,false));
        else
            return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lineup_item,parent,false));
    }
    public class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.homePlayer) TextView homePlayer;
        @BindView(R.id.homePlayerNumber) TextView homePlayerNumber;
        @BindView(R.id.awayPlayer) TextView awayPlayer;
        @BindView(R.id.awayPlayerNumber) TextView awayPlayerNumber;
        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
