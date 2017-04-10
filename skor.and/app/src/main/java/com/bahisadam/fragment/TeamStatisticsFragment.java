package com.bahisadam.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.bahisadam.R;
import com.bahisadam.model.teamInfo.TeamPlayerStats;
import com.bahisadam.utility.Utilities;
import com.bahisadam.view.BaseActivity;
import com.bahisadam.view.HomeActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashSet;


public class TeamStatisticsFragment extends Fragment {

    private static final String ARG_TEAM_STATS = "team_stats";
    private ArrayList<TeamPlayerStats> mPlayers = new ArrayList<>();
    private ArrayList<TeamPlayerStats> mGoals = new ArrayList<>();
    private ArrayList<TeamPlayerStats> mAssists = new ArrayList<>();
    private ArrayList<TeamPlayerStats> mYellowCards = new ArrayList<>();
    private ArrayList<TeamPlayerStats> mRedCards = new ArrayList<>();
    private LinkedHashSet<String> mLeagues = new LinkedHashSet<>();

    private RecyclerView mRecyclerView;

public static TeamStatisticsFragment newInstance(ArrayList<TeamPlayerStats> players){
    TeamStatisticsFragment fragment= new TeamStatisticsFragment();
    Bundle args = new Bundle();
    args.putParcelableArrayList(ARG_TEAM_STATS, players);
    fragment.setArguments(args);
    return fragment;
}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlayers = getArguments().getParcelableArrayList(ARG_TEAM_STATS);
            mLeagues = sortData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_team_statistics, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_cards_player_stats);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new StatsCardAdapter(mLeagues,mPlayers));
        return view;

    }

    private LinkedHashSet<String> sortData(){
        LinkedHashSet<String> leagues = new LinkedHashSet<>();
        if(mPlayers!=null) {
            for (TeamPlayerStats player : mPlayers){
                if(player.getEvent().equals("Goal")) mGoals.add(player);
                if(player.getEvent().equals("Asist"))mAssists.add(player);
                if(player.getEvent().equals("YellowCard"))mYellowCards.add(player);
                if(player.getEvent().equals("RedCard") || player.getEvent().equals("YellowRedCard")) mRedCards.add(player);
                leagues.add(player.getLeagueName());
            }
        }
        return leagues;
    }



    public class StatsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TeamPlayerStats mPlayer;
        private ImageView mImageView;
        private TextView mTextViewName;
        private TextView mTextViewCount;

        public StatsHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView)itemView.findViewById(R.id.image_view_stats_item);
            mTextViewName = (TextView)itemView.findViewById(R.id.text_view_stats_item_player_name);
            mTextViewCount = (TextView)itemView.findViewById(R.id.text_view_stats_item_player_count);
            itemView.setOnClickListener(this);
        }

        void bindItem(TeamPlayerStats player){
            mPlayer = player;
            mTextViewName.setText(player.getName());
            String text = player.getCount()+"";
            mTextViewCount.setText(text);
            String imagePath = "http://thumb.resfu.com/img_data/players/medium/"+player.getID()+".jpg?size=60x&ext=png&lossy=1&1";
            try{
                Picasso.with(getActivity()).load(imagePath).fit().into(mImageView);
            } catch (Exception e){e.printStackTrace();}
        }

        @Override
        public void onClick(View view) {
            if(mPlayer.getID()!=null) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.putExtra(BaseActivity.PAGE, BaseActivity.RESULT_LOAD_PLAYER);
                intent.putExtra(BaseActivity.ID, Integer.parseInt(mPlayer.getID()));
                intent.putExtra(BaseActivity.PLAYER, "1");
                startActivity(intent);
            }
        }
    }

    public class StatsAdapter extends RecyclerView.Adapter<StatsHolder>{

        private ArrayList<TeamPlayerStats> mPlayers;

        public StatsAdapter(ArrayList<TeamPlayerStats> players) {
            mPlayers = players;
        }

        @Override
        public StatsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.team_info_player_stats_item,parent,false);
            return new StatsHolder(view);
        }

        @Override
        public void onBindViewHolder(StatsHolder holder, int position) {
            TeamPlayerStats player = mPlayers.get(position);
            holder.bindItem(player);
        }

        @Override
        public int getItemCount() {
            return mPlayers.size();
        }
    }

    public class StatsCardHolder extends RecyclerView.ViewHolder{

        private ArrayList<TeamPlayerStats> mPlayers;
        private ArrayList<TeamPlayerStats> mGoals = new ArrayList<>();
        private ArrayList<TeamPlayerStats> mAssists = new ArrayList<>();
        private ArrayList<TeamPlayerStats> mYellowCards = new ArrayList<>();
        private ArrayList<TeamPlayerStats> mRedCards = new ArrayList<>();
        private TextView mTextView;
        private RecyclerView mRecyclerView;
        private View mView;

        public StatsCardHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTextView = (TextView)itemView.findViewById(R.id.text_view_card_league_name);
            TabHost tabHost = (TabHost) itemView.findViewById(android.R.id.tabhost);

            tabHost.setup();
            TabHost.TabSpec tabSpec;


            tabSpec = tabHost.newTabSpec("tag1");
            tabSpec.setIndicator(getString(R.string.goal));
            tabSpec.setContent(R.id.tab1);
            tabHost.addTab(tabSpec);

            tabSpec = tabHost.newTabSpec("tag2");
            tabSpec.setIndicator(getString(R.string.asist));
            tabSpec.setContent(R.id.tab1);
            tabHost.addTab(tabSpec);

            tabSpec = tabHost.newTabSpec("tag3");
            tabSpec.setContent(R.id.tab1);
            tabSpec.setIndicator(getString(R.string.yellowCards));
            tabHost.addTab(tabSpec);
            tabSpec = tabHost.newTabSpec("tag4");
            tabSpec.setContent(R.id.tab1);
            tabSpec.setIndicator(getString(R.string.redCards));
            tabHost.addTab(tabSpec);

            ViewGroup.LayoutParams params = tabHost.getTabWidget().getChildAt(0).getLayoutParams();

            params.height = (int) (params.height * 0.7);
            for(int i =0 ;i < tabHost.getTabWidget().getChildCount(); i++) {
                View child = tabHost.getTabWidget().getChildAt(i);
                child.setLayoutParams(params);
                child.setBackgroundResource(R.drawable.tab_selector);
            }

            final TextView titleText = (TextView)itemView.findViewById(R.id.titleStats);
            titleText.setText(R.string.goal);
            final ImageView titleImage = (ImageView)itemView.findViewById(R.id.titleImage);
            Picasso.with(getActivity()).load(R.drawable.goal).fit().centerCrop().into(titleImage);
            mRecyclerView = (RecyclerView)itemView.findViewById(R.id.recycler_view_stats);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            tabHost.setCurrentTabByTag("tag1");
            tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String s) {
                    if(s.equals("tag1")){
                        mRecyclerView.setAdapter(new StatsAdapter(mGoals));
                        titleText.setText(R.string.goal);
                        Picasso.with(getActivity()).load(R.drawable.goal).fit().centerCrop().into(titleImage);
                    } else if(s.equals("tag2")){
                        mRecyclerView.setAdapter(new StatsAdapter(mAssists));
                        titleText.setText(R.string.asist);
                        Picasso.with(getActivity()).load(R.drawable.assist).fit().centerCrop().into(titleImage);
                    } else if(s.equals("tag3")){
                        mRecyclerView.setAdapter(new StatsAdapter(mYellowCards));
                        titleText.setText(R.string.yellowCards);
                        Picasso.with(getActivity()).load(R.drawable.yellow_card).fit().centerCrop().into(titleImage);
                    } else if(s.equals("tag4")){
                        titleText.setText(R.string.redCards);
                        mRecyclerView.setAdapter(new StatsAdapter(mRedCards));
                        Picasso.with(getActivity()).load(R.drawable.red_card).fit().centerCrop().into(titleImage);
                    }
                }
            });


        }

        void bindItem(ArrayList<TeamPlayerStats> players, String leagueName,final int leagueID){
            mPlayers = players;
            for (TeamPlayerStats player : mPlayers){
                if(player.getEvent().equals("Goal")) mGoals.add(player);
                if(player.getEvent().equals("Asist"))mAssists.add(player);
                if(player.getEvent().equals("YellowCard"))mYellowCards.add(player);
                if(player.getEvent().equals("RedCard") || player.getEvent().equals("YellowRedCard")) mRedCards.add(player);
            }
            if(mPlayers.size() == 0){
                mView.setVisibility(View.GONE);
            }
            mTextView.setText(leagueName);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utilities.openLeagueDetails(getActivity(),leagueID);
                }
            });

            mRecyclerView.setAdapter(new StatsAdapter(mPlayers));
        }
    }

    public class StatsCardAdapter extends RecyclerView.Adapter<StatsCardHolder>{

        private ArrayList<String> mLeagues = new ArrayList<>();
        private ArrayList<TeamPlayerStats> mPlayers = new ArrayList<>();

        public StatsCardAdapter(LinkedHashSet<String> leagues,ArrayList<TeamPlayerStats> players) {
            mLeagues.addAll(leagues);
            mPlayers = players;
        }

        @Override
        public StatsCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.team_info_card_stats_item,parent,false);
            return new StatsCardHolder(view);
        }

        @Override
        public void onBindViewHolder(StatsCardHolder holder, int position) {
            String leagueName = mLeagues.get(position);
            int leagueID = 1;
            ArrayList<TeamPlayerStats> players = new ArrayList<>();
            for(TeamPlayerStats player : mPlayers){
                if(player.getLeagueName().equals(leagueName)){
                    players.add(player);
                    leagueID = Integer.valueOf(player.getLeagueId());
                }
            }
            holder.bindItem(players,leagueName,leagueID);


        }

        @Override
        public int getItemCount() {
            return mLeagues.size();
        }
    }

}
