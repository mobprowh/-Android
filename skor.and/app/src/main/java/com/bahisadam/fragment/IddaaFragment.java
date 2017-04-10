package com.bahisadam.fragment;


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bahisadam.Cache;
import com.bahisadam.R;
import com.bahisadam.adapter.MatchAdapter;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.view.DetailPageActivity;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class IddaaFragment extends Fragment  implements MatchAdapter.UpdateMatchListener{
    DetailPageActivity mActivity;
    private static final String ASSETS_PATH = "file:///android_asset/";
    @BindView(R.id.teamsTitleLabel) TextView teamNames;

    @BindView(R.id.label1) TextView label1;
    @BindView(R.id.labelX) TextView labelX;
    @BindView(R.id.label2) TextView label2;
    @BindView(R.id.halfTimeLabel1) TextView halfTimeLabel1;
    @BindView(R.id.halfTimeLabelX) TextView halfTimeLabelX;
    @BindView(R.id.halfTimeLabel2) TextView halfTimeLabel2;
    @BindView(R.id.doubleChanceLabel1X) TextView doubleChanceLabel1X;
    @BindView(R.id.doublceChanceLabelX2) TextView doublceChanceLabelX2;
    @BindView(R.id.doubleChanceLabel12) TextView doubleChanceLabel12;
    @BindView(R.id.handicapLabel1) TextView handicapLabel1;
    @BindView(R.id.handicapLabelX) TextView handicapLabelX;
    @BindView(R.id.handicapLabel2) TextView handicapLabel2;
    @BindView(R.id.aboveBelow15LabelAbove) TextView aboveBelow15LabelAbove;
    @BindView(R.id.aboveBelow15LabelBelow) TextView aboveBelow15LabelBelow;
    @BindView(R.id.aboveBelow25LabelAbove) TextView aboveBelow25LabelAbove;
    @BindView(R.id.aboveBelow25LabelBelow) TextView aboveBelow25LabelBelow;
    @BindView(R.id.aboveBelow35LabelAbove) TextView aboveBelow35LabelAbove;
    @BindView(R.id.aboveBelow35LabelBelow) TextView aboveBelow35LabelBelow;
    @BindView(R.id.aboveBelowHalfTime15LabelAbove) TextView aboveBelowHalfTime15LabelAbove;
    @BindView(R.id.aboveBelowHalfTime15LabelBelow) TextView aboveBelowHalfTime15LabelBelow;
    @BindView(R.id.bothTeamScoredLabelVar) TextView bothTeamScoredLabelVar;
    @BindView(R.id.bothTeamScoredLabelYok) TextView bothTeamScoredLabelYok;
    @BindView(R.id.totalGoalLabel12) TextView totalGoalLabel12;
    @BindView(R.id.totalGoalLabel23) TextView totalGoalLabel23;
    @BindView(R.id.totalGoalLabel46) TextView totalGoalLabel46;
    @BindView(R.id.totalGoalLabel7) TextView totalGoalLabel7;


    @BindView(R.id.macSonuImg) ImageView macSonuImg;
    @BindView(R.id.halfTimeImg) ImageView halfTimeImg;
    @BindView(R.id.doubleChanceImg) ImageView doubleChanceImg;
    @BindView(R.id.hadicapImg) ImageView hadicapImg;
    @BindView(R.id.aboveBelow15Img) ImageView aboveBelow15Img;
    @BindView(R.id.aboveBelow25Img) ImageView aboveBelow25Img;
    @BindView(R.id.aboveBelow35Img) ImageView aboveBelow35Img;
    @BindView(R.id.aboveBelowHalfTimeImg) ImageView aboveBelowHalfTimeImg;
    @BindView(R.id.bothTeamScoredImg) ImageView bothTeamScoredImg;
    @BindView(R.id.totalGoalImg) ImageView totalGoalImg;




    public IddaaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_iddaa, container, false);
        ButterKnife.bind(this,view);
        mActivity = (DetailPageActivity) getActivity();
        initIcons();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity.subscribeForChanges(this);
        updateData();
    }



    private Bitmap getBitmap(String key) throws IOException {
        Bitmap bitmap = Cache.getBitmap(key);
        if(bitmap == null){
            AssetManager assets = mActivity.getAssets();
            bitmap = BitmapFactory.decodeStream(assets.open(key));
            Cache.addBitmap(key,bitmap);
        }
        return  bitmap;

    }
    private void initIcons(){
        try {

            macSonuImg.setImageBitmap(getBitmap("icons/mac_sonu.png"));


            halfTimeImg.setImageBitmap(getBitmap("icons/ilk_yarı.png"));


            doubleChanceImg.setImageBitmap(getBitmap("icons/cifte_sans.png"));


            hadicapImg.setImageBitmap(getBitmap("icons/handikap.png"));


            aboveBelow15Img.setImageBitmap(getBitmap("icons/alt_ust.png"));


            aboveBelow25Img.setImageBitmap(getBitmap("icons/alt_ust.png"));



            aboveBelow35Img.setImageBitmap(getBitmap("icons/alt_ust.png"));

            aboveBelowHalfTimeImg.setImageBitmap(getBitmap("icons/alt_ust.png"));

            bothTeamScoredImg.setImageBitmap(getBitmap("icons/karsilikli_goll.png"));

            totalGoalImg.setImageBitmap(getBitmap("icons/toplam_gol.png"));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void updateMatch(MatchPOJO.MatchDetailed matchDetailed) {
        if(matchDetailed==null) return;
        teamNames.setText(matchDetailed.getHomeTeam().getTeamNameTr() + " - "+
        matchDetailed.getAwayTeam().getTeamNameTr() + " "+
        "maçının iddaa oranları");
        MatchPOJO.Odds odds = matchDetailed.getOdds();
        NumberFormat df = new DecimalFormat("#0.##");
        if(odds != null){
            MatchPOJO.Iddaa iddaa = odds.getIddaa();
            loadToView(label1,iddaa.get1());
            loadToView(label2,iddaa.get2());
            loadToView(labelX,iddaa.getX());
            loadToView(halfTimeLabel1,iddaa.getH1());
            loadToView(halfTimeLabel2,iddaa.getH2());
            loadToView(halfTimeLabelX,iddaa.gethX());
            loadToView(doublceChanceLabelX2,iddaa.getX2());
            loadToView(doubleChanceLabel1X,iddaa.get_1X());
            loadToView(doubleChanceLabel12,iddaa.get12());
            loadToView(handicapLabel1,iddaa.getHan());
            loadToView(handicapLabel2,iddaa.getH2());
            loadToView(handicapLabelX,iddaa.gethX());
            loadToView(aboveBelow15LabelAbove,iddaa.get_15St());
            loadToView(aboveBelow15LabelBelow,iddaa.get_15Alt());
            loadToView(aboveBelow25LabelAbove,iddaa.getSt());
            loadToView(aboveBelow25LabelBelow,iddaa.getAlt());
            loadToView(aboveBelow35LabelAbove,iddaa.get_35St());
            loadToView(aboveBelow35LabelBelow,iddaa.get_35Alt());
            loadToView(aboveBelowHalfTime15LabelAbove,iddaa.getiY15St());
            loadToView(aboveBelowHalfTime15LabelBelow,iddaa.getiY15St());
            loadToView(bothTeamScoredLabelVar,iddaa.getkGV());
            loadToView(bothTeamScoredLabelYok,iddaa.getkGY());
            loadToView(totalGoalLabel12,iddaa.getgS01());
            loadToView(totalGoalLabel23,iddaa.getgS23());
            loadToView(totalGoalLabel46,iddaa.getgS46());
            loadToView(totalGoalLabel7,iddaa.getgS7P());

        }

    }
    private void loadToView(TextView tv, Double data){
        if (null != data) {
            NumberFormat df = new DecimalFormat("#0.##");
            tv.setText(df.format(data));
        }
    }

    @Override
    public void updateData() {
        updateMatch(mActivity.getMatchDetailed());
    }

    @Override
    public void updateGoalAverages() {

    }

    @Override
    public void updateStandings() {

    }


}
