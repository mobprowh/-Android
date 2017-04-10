package com.bahisadam.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bahisadam.R;
import com.bahisadam.adapter.FixtureAdapter;
import com.bahisadam.interfaces.RestClient;
import com.bahisadam.model.MatchListComparator;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.utility.Utilities;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FixtureFragment extends Fragment {
    public static final String ARG_LEAGUE_ID = "LEAGUE_ID";
    public static final String ARG_ROUND = "ROUND";

    private Integer mLeagueId;
    private Integer mRound;
    private RestClient mClient;
    private FixtureAdapter mAdapter;
    @BindView(R.id.matches) RecyclerView rv;
    @BindView(R.id.roundTv) TextView roundTv;
    public FixtureFragment() {
        // Required empty public constructor
    }

    public static FixtureFragment newInstance(Integer leagueId,Integer round) {
        FixtureFragment fragment = new FixtureFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LEAGUE_ID, leagueId);
        args.putInt(ARG_ROUND,round);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLeagueId = getArguments().getInt(ARG_LEAGUE_ID);
            mRound = getArguments().getInt(ARG_ROUND);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fixture, container, false);;
        ButterKnife.bind(this,view);
        mClient = Utilities.buildRetrofit();
        mAdapter = new FixtureAdapter(mLeagueId,getActivity());
        rv.setLayoutManager(new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                false));
        rv.setAdapter(mAdapter);

        loadData();
        return view;
    }
    @OnClick({R.id.prevRoundIv,R.id.nextRoundIv})
    public void prevNextClicked(View view){
        switch (view.getId()){
            case R.id.prevRoundIv: mRound= mRound - 1; loadData(); break;
            case R.id.nextRoundIv: mRound = mRound + 1; loadData();break;
        }
    }
    void loadData(){

        Call<MatchPOJO.Fixture> call = null;
        if(mRound == -1)
            call = mClient.fixture(mLeagueId);
        else
            call= mClient.fixture(mLeagueId, mRound);

        call.enqueue(new Callback<MatchPOJO.Fixture>() {
            @Override
            public void onResponse(Call<MatchPOJO.Fixture> call, Response<MatchPOJO.Fixture> response) {
                MatchPOJO.Fixture fixture = response.body();
                mAdapter.mData.clear();

                for (int i = 0; i < fixture.getGroups().size(); i++){
                    MatchPOJO.Group group = fixture.getGroups().get(i);
                    List<MatchPOJO.Match> matches = fixture.getGroups().get(i).getMatches();
                    Collections.sort(group.getMatches(), MatchListComparator.compareMatchByDateReversed);

                    mAdapter.mData.add(group);
                }
                Collections.sort(mAdapter.mData, new Comparator<MatchPOJO.Group>() {
                            @Override
                            public int compare(MatchPOJO.Group group, MatchPOJO.Group t1) {
                                return Integer.parseInt(group.getGroup()) - Integer.parseInt(t1.getGroup());
                            }
                        });
                mAdapter.notifyDataSetChanged();
                mRound =fixture.getLeagueStatus().getRound();
                roundTv.setText(mRound + ". Round");
            }

            @Override
            public void onFailure(Call<MatchPOJO.Fixture> call, Throwable t) {
                int x =0;
            }
        });

    }

}
