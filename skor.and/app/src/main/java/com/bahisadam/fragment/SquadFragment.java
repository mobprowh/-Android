package com.bahisadam.fragment;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bahisadam.R;
import com.bahisadam.model.teamInfo.TeamPlayerInfo;
import com.bahisadam.utility.RoundedCornersTransform;
import com.bahisadam.view.BaseActivity;
import com.bahisadam.view.HomeActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class SquadFragment extends Fragment {

    public static final String ARG_TEAM_INFO = "team_info";
    public static final String ARG_TEAM_NAME = "team_name";
    private ArrayList<TeamPlayerInfo> mPlayers = new ArrayList<>();
    private String mTeamName;

    private TextView mHeaderTextView;
    private RecyclerView mRecyclerView;

    public static SquadFragment newInstance(ArrayList<TeamPlayerInfo> players,String teamName) {

        SquadFragment fragment = new SquadFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_TEAM_INFO, players);
        args.putString(ARG_TEAM_NAME,teamName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlayers = getArguments().getParcelableArrayList(ARG_TEAM_INFO);
            mTeamName = getArguments().getString(ARG_TEAM_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_squad,container,false);
        mHeaderTextView = (TextView)view.findViewById(R.id.text_view_squad_header);
        mHeaderTextView.setText(String.format(getString(R.string.team_squad),mTeamName));
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_squad);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(mPlayers!=null) mRecyclerView.setAdapter(new SquadAdapter(mPlayers));
        return view;
    }



    public class SquadHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TeamPlayerInfo mPlayer;
        private TextView mTextViewNumber;
        private TextView mTextViewName;
        private TextView mTextViewPosition;
        private TextView mTextViewGoals;
        private TextView mTextViewAssists;
        private ImageView mImageViewPlayer;
        private CircleImageView mImageViewCountry;

        SquadHolder(View itemView) {
            super(itemView);
            mTextViewNumber = (TextView) itemView.findViewById(R.id.text_view_squad_number);
            mTextViewName = (TextView) itemView.findViewById(R.id.text_view_squad_name);
            mTextViewPosition = (TextView) itemView.findViewById(R.id.text_view_squad_position);
            mTextViewGoals = (TextView) itemView.findViewById(R.id.text_view_squad_goals);
            mTextViewAssists = (TextView) itemView.findViewById(R.id.text_view_squad_assists);
            mImageViewPlayer = (ImageView)itemView.findViewById(R.id.image_view_squad_image);
            mImageViewCountry = (CircleImageView)itemView.findViewById(R.id.image_view_squad_country);
            itemView.setOnClickListener(this);
        }

        void bindItem(TeamPlayerInfo player){
            mPlayer = player;
            mTextViewNumber.setText(mPlayer.getSquadNumber().equals("null") ? "-":mPlayer.getSquadNumber());
            mTextViewName.setText(mPlayer.getName());
            mTextViewPosition.setText(mPlayer.getPosition());
            mTextViewGoals.setText(mPlayer.getGoals());
            mTextViewAssists.setText(mPlayer.getAssists());
            String uri = "drawable/"+ mPlayer.getCountryCode().toLowerCase();
            int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
//            if(imageResource!=0) mImageViewCountry.setBackgroundResource(imageResource);
            try{
                Picasso.with(getActivity()).load(mPlayer.getImageUrl()).transform(new RoundedCornersTransform(5)).fit().into(mImageViewPlayer);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(),HomeActivity.class);
            intent.putExtra(BaseActivity.PAGE,BaseActivity.RESULT_LOAD_PLAYER);
            intent.putExtra(BaseActivity.ID,Integer.parseInt(mPlayer.getPlayerId()));
            intent.putExtra(BaseActivity.PLAYER,"1");
            startActivity(intent);
        }
    }

    public class SquadAdapter extends RecyclerView.Adapter<SquadHolder>{

        private ArrayList<TeamPlayerInfo> mPlayers;

        SquadAdapter(ArrayList<TeamPlayerInfo> players) {
            mPlayers = players;
        }

        @Override
        public SquadHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.team_info_squad_item,parent,false);
            return new SquadHolder(view);
        }

        @Override
        public void onBindViewHolder(SquadHolder holder, int position) {
            TeamPlayerInfo player = mPlayers.get(position);
            holder.bindItem(player);
        }

        @Override
        public int getItemCount() {
            return mPlayers.size();
        }
    }

}
