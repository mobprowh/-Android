package com.bahisadam.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bahisadam.R;
import com.bahisadam.adapter.LineupsAdapter;
import com.bahisadam.adapter.MatchAdapter;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.view.DetailPageActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LineupsFragment extends Fragment implements MatchAdapter.UpdateMatchListener{


    private DetailPageActivity mActivity;
    private List<Lineup> mList;
    @BindView(R.id.noInfo) TextView noInfoMsgTV;
    @BindView(R.id.lineupsRv) RecyclerView linuepsRV;
    @BindView(R.id.teamLocal) TextView teamLocalTV;
    @BindView(R.id.teamVisitor) TextView teamVisitorTV;
    LineupsAdapter mAdapter;
    public LineupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lineups, container, false);
        ButterKnife.bind(this,view);
        mActivity = ((DetailPageActivity)getActivity());
        mActivity.subscribeForChanges(this);
        mList = new ArrayList<>();
        mAdapter = new LineupsAdapter(mList);
        linuepsRV.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        linuepsRV.setAdapter(mAdapter);

        updateData();
        return view;
    }

    @Override
    public void updateMatch(MatchPOJO.MatchDetailed matchDetailed) {
        int x=0;
        if(matchDetailed == null || !matchDetailed.getHasLineup() || matchDetailed.lineups == null){
            showNoInformationMessage();
            return;
        }
        noInfoMsgTV.setVisibility(View.GONE);
        mList.clear();
        mAdapter.notifyDataSetChanged();

        teamLocalTV.setText(matchDetailed.getHomeTeam().getTeamNameTr());
        teamVisitorTV.setText(matchDetailed.getAwayTeam().getTeamNameTr());

        loadPlayers(matchDetailed.lineups.local, matchDetailed.lineups.visitor, Lineup.MAIN);
        loadPlayers(matchDetailed.lineups.local_substitutes, matchDetailed.lineups.visitor_substitutes, Lineup.SUB);


    }

    private void showNoInformationMessage() {
        noInfoMsgTV.setVisibility(View.VISIBLE);
    }

    private void loadPlayers(List<MatchPOJO.Player> playersLocal, List<MatchPOJO.Player> playersVisitor, int type){
        playersLocal = playersLocal == null ? new ArrayList<MatchPOJO.Player>() : playersLocal;
        playersVisitor = playersVisitor == null ? new ArrayList<MatchPOJO.Player>() : playersVisitor;

        mList.add(new Lineup(playersLocal,playersVisitor,type));
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void updateData() {
        MatchPOJO.MatchDetailed matchDetailed = mActivity.getMatchDetailed();
        updateMatch(matchDetailed);
    }

    @Override
    public void updateGoalAverages() {

    }

    @Override
    public void updateStandings() {

    }

    public class Lineup{
        public static final int MAIN = 1,SUB = 2;
        public int type;
        public List<MatchPOJO.Player> playersLocal;
        public List<MatchPOJO.Player> playersVisitor;

        public Lineup(List<MatchPOJO.Player> playersLocal, List<MatchPOJO.Player> playersVisitor, int type) {
            this.playersLocal = playersLocal;
            this.playersVisitor = playersVisitor;
            this.type = type;
        }
    }
}
