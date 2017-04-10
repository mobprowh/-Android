package com.bahisadam.fragment;


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
import com.bahisadam.model.teamInfo.TeamFixture;
import com.bahisadam.model.teamInfo.TeamMatch;
import com.bahisadam.utility.Utilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashSet;


public class TeamMatchesFragment extends Fragment {

    public static final String ARG_TEAM_MATCHES = "team_matches";
    public static final String ARG_TEAM_FIXTURES = "team_fixtures";
    public static final String ARG_TEAM_NAME = "team_name";
    private ArrayList<TeamMatch> mMatches = new ArrayList<>();
    private ArrayList<TeamFixture> mFixtures = new ArrayList<>();
    private LinkedHashSet<String> mMatchLeagues = new LinkedHashSet<>();
    private LinkedHashSet<String> mFixtureLeagues = new LinkedHashSet<>();
    private String mTeamName;

    private RecyclerView mRecyclerView;

    public static TeamMatchesFragment newInstance(ArrayList<TeamMatch> matches, ArrayList<TeamFixture> fixtures,String teamName) {
        TeamMatchesFragment fragment = new TeamMatchesFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_TEAM_MATCHES, matches);
        args.putParcelableArrayList(ARG_TEAM_FIXTURES, fixtures);
        args.putString(ARG_TEAM_NAME,teamName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMatches = getArguments().getParcelableArrayList(ARG_TEAM_MATCHES);
            mFixtures = getArguments().getParcelableArrayList(ARG_TEAM_FIXTURES);
            mMatchLeagues = getMatchLeagues();
            mFixtureLeagues = getFixtureLeagues();
            mTeamName = getArguments().getString(ARG_TEAM_NAME);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_matches, container, false);

        TabHost tabHost = (TabHost) view.findViewById(android.R.id.tabhost);

        tabHost.setup();
        TabHost.TabSpec tabSpec;


        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator(getString(R.string.matches));
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator(getString(R.string.fixtures));
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        ViewGroup.LayoutParams params = tabHost.getTabWidget().getChildAt(0).getLayoutParams();

        params.height = (int) (params.height * 0.7);
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View child = tabHost.getTabWidget().getChildAt(i);
            child.setLayoutParams(params);
            child.setBackgroundResource(R.drawable.tab_selector);
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_cards_team_matches);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new TeamMatchesFragment.CardMatchAdapter(mMatchLeagues, mMatches));

        tabHost.setCurrentTabByTag("tag1");
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if (s.equals("tag1")) {
                    mRecyclerView.setAdapter(new TeamMatchesFragment.CardMatchAdapter(mMatchLeagues, mMatches));
                } else if (s.equals("tag2")) {
                    mRecyclerView.setAdapter(new TeamMatchesFragment.CardFixtureAdapter(mFixtureLeagues, mFixtures));

                }
            }
        });

        return view;
    }

    private LinkedHashSet<String> getMatchLeagues() {
        LinkedHashSet<String> leagues = new LinkedHashSet<>();
        if (mMatches != null) {
            for (TeamMatch match : mMatches) {
                leagues.add(match.getLeagueName());
            }
        }
        return leagues;
    }

    private LinkedHashSet<String> getFixtureLeagues() {
        LinkedHashSet<String> leagues = new LinkedHashSet<>();
        if (mFixtures != null) {
            for (TeamFixture fixture : mFixtures) {
                leagues.add(fixture.getLeagueName());
            }
        }
        return leagues;
    }

    public class MatchHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewScore;
        private TextView mTextViewDate;
        private TextView mTextViewHomeTeam;
        private TextView mTextViewAwayTeam;
        private TextView mTextViewResult;
        private ImageView mImageViewHome;
        private ImageView mImageViewAway;


        public MatchHolder(View itemView) {
            super(itemView);
            mTextViewScore = (TextView) itemView.findViewById(R.id.match_item_score);
            mTextViewDate = (TextView) itemView.findViewById(R.id.match_item_date);
            mTextViewHomeTeam = (TextView) itemView.findViewById(R.id.match_item_home_name);
            mTextViewAwayTeam = (TextView) itemView.findViewById(R.id.match_item_away_name);
            mTextViewResult = (TextView) itemView.findViewById(R.id.match_item_result);
            mImageViewHome = (ImageView) itemView.findViewById(R.id.match_item_home_logo);
            mImageViewAway = (ImageView) itemView.findViewById(R.id.match_item_away_logo);
            mTextViewResult = (TextView) itemView.findViewById(R.id.match_item_result);


        }

        void bindItem(TeamMatch match) {
            mTextViewScore.setText(match.getHomeTeamGoals() + " : " + match.getAwayTeamGoals());
            mTextViewDate.setText(match.getDate());
            mTextViewHomeTeam.setText(match.getHomeTeamName());
            mTextViewAwayTeam.setText(match.getAwayTeamName());
            try {
                Picasso.with(getActivity()).load(match.getHomeTeamImageUrl()).fit().into(mImageViewHome);
                Picasso.with(getActivity()).load(match.getAwayTeamImageUrl()).fit().into(mImageViewAway);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int homeGoals = Integer.valueOf(match.getHomeTeamGoals());
            int awayGoals = Integer.valueOf(match.getAwayTeamGoals());
            if((match.getHomeTeamName().equals(mTeamName) && homeGoals>awayGoals) ||
                    (match.getAwayTeamName().equals(mTeamName) && homeGoals<awayGoals)){
                mTextViewResult.setBackgroundResource(R.drawable.shape_green);
                mTextViewResult.setText("G");
            } else if (homeGoals == awayGoals){
                mTextViewResult.setBackgroundResource(R.drawable.shape_yellow);
                mTextViewResult.setText("B");
            } else{
                mTextViewResult.setBackgroundResource(R.drawable.shape_red);
                mTextViewResult.setText("M");
            }
        }
    }

        public class MatchAdapter extends RecyclerView.Adapter<MatchHolder> {

            private ArrayList<TeamMatch> mMatches;

            public MatchAdapter(ArrayList<TeamMatch> matches) {
                mMatches = matches;
            }

            @Override
            public MatchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.team_info_match_item, parent, false);
                return new MatchHolder(view);
            }

            @Override
            public void onBindViewHolder(MatchHolder holder, int position) {
                TeamMatch match = mMatches.get(position);
                holder.bindItem(match);
            }

            @Override
            public int getItemCount() {
                return mMatches.size();
            }
        }

        public class FixtureHolder extends RecyclerView.ViewHolder {

            private TextView mTextViewScore;
            private TextView mTextViewDate;
            private TextView mTextViewHomeTeam;
            private TextView mTextViewAwayTeam;
            private TextView mTextViewResult;
            private ImageView mImageViewHome;
            private ImageView mImageViewAway;


            public FixtureHolder(View itemView) {
                super(itemView);
                mTextViewScore = (TextView) itemView.findViewById(R.id.match_item_score);
                mTextViewDate = (TextView) itemView.findViewById(R.id.match_item_date);
                mTextViewHomeTeam = (TextView) itemView.findViewById(R.id.match_item_home_name);
                mTextViewAwayTeam = (TextView) itemView.findViewById(R.id.match_item_away_name);
                mTextViewResult = (TextView) itemView.findViewById(R.id.match_item_result);
                mImageViewHome = (ImageView) itemView.findViewById(R.id.match_item_home_logo);
                mImageViewAway = (ImageView) itemView.findViewById(R.id.match_item_away_logo);


            }

            void bindItem(TeamFixture fixture) {
                mTextViewScore.setText("-:-");
                mTextViewDate.setText(fixture.getDate());
                mTextViewHomeTeam.setText(fixture.getHomeTeamName());
                mTextViewAwayTeam.setText(fixture.getAwayTeamName());
                try {
                    Picasso.with(getActivity()).load(fixture.getHomeTeamImageUrl()).fit().into(mImageViewHome);
                    Picasso.with(getActivity()).load(fixture.getAwayTeamImageUrl()).fit().into(mImageViewAway);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }

            public class FixtureAdapter extends RecyclerView.Adapter<FixtureHolder> {

                private ArrayList<TeamFixture> mFixtures;

                public FixtureAdapter(ArrayList<TeamFixture> fixtures) {
                    mFixtures = fixtures;
                }


                @Override
                public FixtureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.team_info_match_item, parent, false);
                    return new FixtureHolder(view);
                }

                @Override
                public void onBindViewHolder(FixtureHolder holder, int position) {
                    TeamFixture fixture = mFixtures.get(position);
                    holder.bindItem(fixture);
                }

                @Override
                public int getItemCount() {
                    return mFixtures.size();
                }
            }


            public class StatsCardHolder extends RecyclerView.ViewHolder {

                private ArrayList<TeamMatch> mMatches;
                private ArrayList<TeamFixture> mFixtures;
                private TextView mTextView;
                private RecyclerView mRecyclerView;

                public StatsCardHolder(View itemView) {
                    super(itemView);
                    mTextView = (TextView) itemView.findViewById(R.id.text_view_card_league_name);
                    mRecyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_stats);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }

                void bindMatch(ArrayList<TeamMatch> matches, String leagueName, final int leagueID) {
                    mMatches = matches;
                    mTextView.setText(leagueName);
                    mTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Utilities.openLeagueDetails(getActivity(),leagueID);
                        }
                    });
                    mRecyclerView.setAdapter(new MatchAdapter(mMatches));
                }

                void bindFixture(ArrayList<TeamFixture> fixtures, String leagueName, final int leagueID) {
                    mFixtures = fixtures;
                    mTextView.setText(leagueName);
                    mTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Utilities.openLeagueDetails(getActivity(),leagueID);
                        }
                    });
                    mRecyclerView.setAdapter(new FixtureAdapter(mFixtures));
                }
            }

            public class CardMatchAdapter extends RecyclerView.Adapter<StatsCardHolder> {

                private ArrayList<String> mLeagues = new ArrayList<>();
                private ArrayList<TeamMatch> mMatches = new ArrayList<>();

                public CardMatchAdapter(LinkedHashSet<String> leagues, ArrayList<TeamMatch> matches) {
                    mLeagues.addAll(leagues);
                    mMatches = matches;
                }

                @Override
                public StatsCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.team_info_card_match_item, parent, false);
                    return new StatsCardHolder(view);
                }

                @Override
                public void onBindViewHolder(StatsCardHolder holder, int position) {
                    String leagueName = mLeagues.get(position);
                    int leagueID = 1;
                    ArrayList<TeamMatch> matches = new ArrayList<>();
                    for (TeamMatch match : mMatches) {
                        if (match.getLeagueName().equals(leagueName)) {
                            matches.add(match);
                            leagueID = Integer.valueOf(match.getLeagueId());
                        }
                    }
                    holder.bindMatch(matches, leagueName,leagueID);


                }

                @Override
                public int getItemCount() {
                    return mLeagues.size();
                }
            }

            public class CardFixtureAdapter extends RecyclerView.Adapter<StatsCardHolder> {

                private ArrayList<String> mLeagues = new ArrayList<>();
                private ArrayList<TeamFixture> mFixtures = new ArrayList<>();

                public CardFixtureAdapter(LinkedHashSet<String> leagues, ArrayList<TeamFixture> fixtures) {
                    mLeagues.addAll(leagues);
                    mFixtures = fixtures;
                }

                @Override
                public StatsCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.team_info_card_match_item, parent, false);
                    return new StatsCardHolder(view);
                }

                @Override
                public void onBindViewHolder(StatsCardHolder holder, int position) {
                    String leagueName = mLeagues.get(position);
                    int leagueID = 1;
                    ArrayList<TeamFixture> fixtures = new ArrayList<>();
                    for (TeamFixture fixture : mFixtures) {
                        if (fixture.getLeagueName().equals(leagueName)) {
                            fixtures.add(fixture);
                            leagueID = Integer.valueOf(fixture.getLeagueId());
                        }
                    }
                    holder.bindFixture(fixtures, leagueName,leagueID);


                }

                @Override
                public int getItemCount() {
                    return mLeagues.size();
                }
            }


}
