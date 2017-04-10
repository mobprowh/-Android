package com.bahisadam.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bahisadam.R;
import com.bahisadam.view.DetailedPage.Item;
import com.txusballesteros.widgets.FitChart;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by atata on 30/11/2016.
 */

public class DetailedPageAdapter extends RecyclerView.Adapter<DetailedPageAdapter.CardHolder> {

    public static final int TYPE_VOTE = 1,TYPE_STANDINGS = 2, TYPE_HOMEAWAY = 3, TYPE_COMMENTS = 4, TYPE_IDEA=5;
    private List<Item> itemsList;

    public DetailedPageAdapter(List<Item> itemsList) {
        this.itemsList = itemsList;
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.vote_layout;
        switch (viewType) {
            case TYPE_VOTE: layout = R.layout.vote_layout;
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(layout, parent, false);
                    return new VoteHolder(v);
            case TYPE_STANDINGS : layout = R.layout.standings_layout;
                View standingsView = LayoutInflater.from(parent.getContext())
                        .inflate(layout, parent, false);
                return new StandingsHolder(standingsView);
            case TYPE_HOMEAWAY: layout = R.layout.homeaway_layout;
                View homeawayView = LayoutInflater.from(parent.getContext())
                        .inflate(layout,parent,false);
                return new GoalAveragesHolder(homeawayView);
            case TYPE_COMMENTS: layout = R.layout.comments_layout;
                View commentsView = LayoutInflater.from(parent.getContext())
                        .inflate(layout,parent,false);
                return new CommentsHolder(commentsView);
            case TYPE_IDEA: layout = R.layout.prediction_layout;
                View predictionView = LayoutInflater.from(parent.getContext())
                        .inflate(layout,parent,false);
                return new PredictionHolder(predictionView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {
        itemsList.get(position).bindViewHolder(holder);
    }

    @Override
    public int getItemViewType(int position) {
        Item item = itemsList.get(position);
        /*if(item instanceof VoteItem)
            return TYPE_VOTE;
        if(item instanceof StandingsItem)
            return TYPE_STANDINGS;
        if(item instanceof GoalAveragesItem)
            return TYPE_HOMEAWAY;
        if(item instanceof CommentsItem)
            return TYPE_COMMENTS;
        if(item instanceof PredictionItem)
            return TYPE_IDEA;
            */
        return itemsList.get(position).getItemType();
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class CardHolder extends RecyclerView.ViewHolder{

        public CardHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public class VoteHolder extends CardHolder{
        public @BindView(R.id.chart1) FitChart chart1;
        public @BindView(R.id.chartx) FitChart chartx;
        public @BindView(R.id.chart2) FitChart chart2;
        public @BindView(R.id.labelX) TextView labelX;
        public @BindView(R.id.label1) TextView label1;
        public @BindView(R.id.label2) TextView label2;
        public @BindView(R.id.chartsHomeTeam) TextView chartsHomeTeam;
        public @BindView(R.id.chartsAwayTeam) TextView chartsAwayTeam;
        public @BindView(R.id.vote1) FrameLayout vote1;
        public @BindView(R.id.vote2) FrameLayout vote2;
        public @BindView(R.id.voteX) FrameLayout voteX;
        public VoteHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    public class StandingsHolder extends CardHolder{
        public @BindView(R.id.homeTeamNum) TextView homeTeamNum;
        public @BindView(R.id.awayTeamNum) TextView awayTeamNum;
        public @BindView(R.id.homeTeamName) TextView homeTeamName;
        public @BindView(R.id.awayTeamName) TextView awayTeamName;
        public @BindView(R.id.homeTeamOm) TextView homeTeamOm;
        public @BindView(R.id.homeTeamG) TextView homeTeamG;
        public @BindView(R.id.homeTeamB) TextView homeTeamB;
        public @BindView(R.id.homeTeamPTS) TextView homeTeamPTS;
        public @BindView(R.id.homeTeamM) TextView homeTeamM;
        public @BindView(R.id.awayTeamOm) TextView awayTeamOm;
        public @BindView(R.id.awayTeamG) TextView awayTeamG;
        public @BindView(R.id.awayTeamB) TextView awayTeamB;
        public @BindView(R.id.awayTeamPTS) TextView awayTeamPTS;
        public @BindView(R.id.awayTeamM) TextView awayTeamM;
        public @BindView(R.id.homeTeamLogoStandings) ImageView homeTeamLogo;
        public @BindView(R.id.awayTeamLogoStandings) ImageView awayTeamLogo;
        public @BindView(R.id.homeTeamLayout) LinearLayout homeTeamLayout;
        public @BindView(R.id.awayTeamLayout) LinearLayout awayTeamLayout;
        public @BindView(R.id.standingsRoot) LinearLayout rootLayout;
        public @BindView(R.id.standings) CardView standingsLayout;
        public @BindView(R.id.awayStandings) LinearLayout awayTitleLayout;
        public @BindView(R.id.homeStandings) LinearLayout homeTitleLayout;
        public @BindView(R.id.generalStandings) LinearLayout generalTitleLayout;
        public @BindView(R.id.generalStandingsUnderline) View generalStandingsUnderline;
        public @BindView(R.id.homeStandingsUnderline) View homeStandingsUnderline;
        public @BindView(R.id.awayStandingsUnderline) View awayStandingsUnderline;
        public @BindView(R.id.generalStandingsText) TextView generalTv;
        public @BindView(R.id.homeStandingsText) TextView homeTv;
        public @BindView(R.id.awayStandingsText) TextView awayTv;
        public StandingsHolder(View view){
            super(view);
            ButterKnife.bind(this,view);


        }
    }
    public class GoalAveragesHolder extends CardHolder {
        public @BindView(R.id.goalHomeTeam) TextView goalHomeTeam;
        public @BindView(R.id.goalHomeAG) TextView goalHomeAG;
        public @BindView(R.id.goalHomeAG2) TextView goalHomeAG2;
        public @BindView(R.id.goalHomeYG) TextView goalHomeYG;
        public @BindView(R.id.goalHomeYG2) TextView goalHomeYG2;
        public @BindView(R.id.goalAwayTeam) TextView goalAwayTeam;
        public @BindView(R.id.goalAwayAG) TextView goalAwayAG;
        public @BindView(R.id.goalAwayAG2) TextView goalAwayAG2;
        public @BindView(R.id.goalAwayYG) TextView goalAwayYG;
        public @BindView(R.id.goalAwayYG2) TextView goalAwayYG2;
        public GoalAveragesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public class CommentsHolder extends CardHolder {
        public @BindView(R.id.noComments) TextView noComments;
        public @BindView(R.id.comments) RecyclerView commentsView;

        public CommentsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
    public class PredictionHolder extends CardHolder {

        public PredictionHolder(View itemView) {
            super(itemView);
        }
    }

}
