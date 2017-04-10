package com.bahisadam.view.DetailedPage;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bahisadam.MyApplication;
import com.bahisadam.R;
import com.bahisadam.fragment.DetailFragment;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.interfaces.RestClient;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.model.requests.UpdatePredictionRequest;
import com.bahisadam.utility.Preferences;
import com.bahisadam.utility.Utilities;
import com.bahisadam.view.DetailPageActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by atata on 03/12/2016.
 */

public class PredictionItem  implements Item, View.OnClickListener {
    DetailFragment.PredictionHolder holder;
    String mForecast = "";
    String mReason = "";
    String mMatchId = "";
    View mActiveView;
    DetailPageActivity mActivity;
    DetailFragment mFragment;
    boolean voteEnabled  = false;
    public PredictionItem(String mMatchId, DetailPageActivity activity) {
        this.mMatchId = mMatchId;
        this.mActivity = activity;
    }

    public void setFragment(DetailFragment fragment) {
        this.mFragment = fragment;
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder cardHolder) {
        holder = (DetailFragment.PredictionHolder) cardHolder;
        MatchPOJO.MatchDetailed match = mActivity.getMatchDetailed();
        String status = match.getResult_type();
        setupRates(match.getIddaa_odds());
        if(status != null  && status.equals(Constant.NOT_PLAYED)) voteEnabled=true;
        ((View)holder.oddsIdda1.getParent().getParent()).setOnClickListener(this);
        ((View)holder.oddsIddaX.getParent().getParent()).setOnClickListener(this);
        ((View)holder.oddsIdda2.getParent().getParent()).setOnClickListener(this);
        ((View)holder.IY1.getParent().getParent()).setOnClickListener(this);
        ((View)holder.IYX.getParent().getParent()).setOnClickListener(this);
        ((View)holder.IY2.getParent().getParent()).setOnClickListener(this);
        ((View)holder.idda1x.getParent().getParent()).setOnClickListener(this);
        ((View)holder.idda12.getParent().getParent()).setOnClickListener(this);
        ((View)holder.iddax2.getParent().getParent()).setOnClickListener(this);
        ((View)holder.h1.getParent().getParent()).setOnClickListener(this);
        ((View)holder.hx.getParent().getParent()).setOnClickListener(this);
        ((View)holder.h2.getParent().getParent()).setOnClickListener(this);
        ((View)holder.alt.getParent().getParent()).setOnClickListener(this);
        ((View)holder.ust.getParent().getParent()).setOnClickListener(this);
        ((View)holder.alt15.getParent().getParent()).setOnClickListener(this);
        ((View)holder.ust15.getParent().getParent()).setOnClickListener(this);
        ((View)holder.alt35.getParent().getParent()).setOnClickListener(this);
        ((View)holder.ust35.getParent().getParent()).setOnClickListener(this);
        ((View)holder.kgv.getParent().getParent()).setOnClickListener(this);
        ((View)holder.kgy.getParent().getParent()).setOnClickListener(this);
        ((View)holder.gs01.getParent().getParent()).setOnClickListener(this);
        ((View)holder.gs23.getParent().getParent()).setOnClickListener(this);
        ((View)holder.gs46.getParent().getParent()).setOnClickListener(this);
        ((View)holder.gs7p.getParent().getParent()).setOnClickListener(this);
        ((View) holder.sf1x.getParent().getParent()).setOnClickListener(this);
        ((View) holder.sfx1.getParent().getParent()).setOnClickListener(this);
        ((View) holder.sf11.getParent().getParent()).setOnClickListener(this);
        ((View) holder.sf12.getParent().getParent()).setOnClickListener(this);
        ((View) holder.sf21.getParent().getParent()).setOnClickListener(this);
        ((View) holder.sf22.getParent().getParent()).setOnClickListener(this);
        ((View) holder.sf2x.getParent().getParent()).setOnClickListener(this);
        ((View) holder.sfx2.getParent().getParent()).setOnClickListener(this);
        ((View) holder.sfxx.getParent().getParent()).setOnClickListener(this);
        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        holder.view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();

                return false;
            }
        });
    }

    private void setupRates(MatchPOJO.Iddaa odds) {
        if(odds!=null){


            loadToView(holder.oddsIdda1,odds.get1());
            loadToView(holder.oddsIddaX,odds.getX());
            loadToView(holder.oddsIdda2,odds.get2());
            loadToView(holder.IY1,odds.getiY1());
            loadToView(holder.IYX,odds.getiYX());
            loadToView(holder.IY2,odds.getiY2());
            loadToView(holder.idda1x,odds.get_1X());
            loadToView(holder.idda12,odds.get12());
            loadToView(holder.iddax2,odds.getX2());
            loadToView(holder.h1,odds.getH1());
            loadToView(holder.hx,odds.gethX());
            loadToView(holder.h2,odds.getH2());
            loadToView(holder.alt,odds.getAlt());
            loadToView(holder.ust,odds.getSt());
            loadToView(holder.alt15,odds.getiY15Alt());
            loadToView(holder.ust15,odds.getiY15St());
            loadToView(holder.alt35,odds.get_35Alt());
            loadToView(holder.ust35,odds.get_35St());
            loadToView(holder.kgv,odds.getkGV());
            loadToView(holder.kgy,odds.getkGY());
            loadToView(holder.gs01,odds.getgS01());
            loadToView(holder.gs23,odds.getgS23());
            loadToView(holder.gs46,odds.getgS46());
            loadToView(holder.gs7p,odds.getgS7P());
            loadToView(holder.sf1x,odds.getsF1X());
            loadToView(holder.sfx1,odds.getsFX1());
            loadToView(holder.sf11,odds.getsF11());
            loadToView(holder.sf12,odds.getsF12());
            loadToView(holder.sf21,odds.getsF21());
            loadToView(holder.sf22,odds.getsF22());
            loadToView(holder.sf2x,odds.getsF2X());
            loadToView(holder.sfx2,odds.getsFX2());
            loadToView(holder.sfxx,odds.getsFXX());
        }
    }

    private void loadToView(TextView tv, Double odd) {
        if( odd!= null){
            String str = String.format("%.2f",odd);
            tv.setText(str);
        }

    }


    @Override
    public int getItemType() {
        return TYPE_IDEA;
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        if(Preferences.getUser()!=null){
            if(!voteEnabled){
                Utilities.showSnackBar(mActivity,holder.view,mActivity.getString(R.string.votinDisabled));
            } else {
                int id = ((LinearLayout) ((CardView) v).getChildAt(0)).getChildAt(1).getId();
                click(id);
                int color = ContextCompat.getColor(MyApplication.getAppContext(), R.color.colorPrimary);
                toggleCard(false);

                mActiveView = ((CardView) v).getChildAt(0);
                toggleCard(true);
            }
        } else{
            Utilities.login(mActivity);
        }


    }

    private void toggleCard(boolean show){
        if(show){
            int color = ContextCompat.getColor(MyApplication.getAppContext(),R.color.colorPrimary);
            mActiveView.setBackgroundColor(color);
        }else {
            if(mActiveView != null){
                mActiveView.setBackgroundColor(0);
            }
        }

    }
    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
    }
    private void click(int id){
        String forecast ="";

        switch (id){
            case R.id.oddsIdda1: setForecast("1"); break;
            case R.id.oddsIddaX: setForecast("X"); break;
            case R.id.oddsIdda2: setForecast("2"); break;
            case R.id.IY1: setForecast("IY1"); break;
            case R.id.IYX: setForecast("IYX"); break;
            case R.id.IY2: setForecast("IY2"); break;
            case R.id.idda1x: setForecast("1X"); break;
            case R.id.idda12: setForecast("12"); break;
            case R.id.iddax2: setForecast("X2"); break;
            case R.id.h1: setForecast("H1"); break;
            case R.id.hx: setForecast("HX"); break;
            case R.id.h2: setForecast("H2"); break;
            case R.id.alt: setForecast("Alt"); break;
            case R.id.ust: setForecast("Üst"); break;
            case R.id.alt15: setForecast("IY15Alt"); break;
            case R.id.ust15: setForecast("IY15Üst"); break;
            case R.id.alt35: setForecast("35Alt"); break;
            case R.id.ust35: setForecast("35Üst"); break;
            case R.id.kgv: setForecast("KGV"); break;
            case R.id.kgy: setForecast("KGY"); break;
            case R.id.gs01: setForecast("GS01"); break;
            case R.id.gs23: setForecast("GS23"); break;
            case R.id.gs46: setForecast("GS46"); break;
            case R.id.gs7p: setForecast("GS7P"); break;
            case R.id.sf1x: setForecast("SF1X"); break;
            case R.id.sfx1: setForecast("SFX1"); break;
            case R.id.sf11: setForecast("SF11"); break;
            case R.id.sf12: setForecast("SF12"); break;
            case R.id.sf21: setForecast("SF21"); break;
            case R.id.sf22: setForecast("SF22"); break;
            case R.id.sf2X: setForecast("SF2X"); break;
            case R.id.sfx2: setForecast("SFX2"); break;
            case R.id.sfxx: setForecast("SFXX"); break;
        }


    }
    private void setForecast(String forecast){
        mForecast = forecast;
    }
    private void submit() {
        final RestClient rest = Utilities.buildRetrofit();
        mReason = holder.reasonET.getText().toString();
        Call<UpdatePredictionRequest.Response> call = rest.updatePrediction(new UpdatePredictionRequest(mForecast,mMatchId,mReason));
        call.enqueue(new Callback<UpdatePredictionRequest.Response>() {
            @Override
            public void onResponse(Call<UpdatePredictionRequest.Response> call, Response<UpdatePredictionRequest.Response> response) {
                UpdatePredictionRequest.Response body = response.body();
                if(body.error== null){
                    if(mFragment != null)
                    mFragment.getComments();
                    holder.reasonET.setText("");
                    mForecast="";
                    mReason="";
                    toggleCard(false);

                } else {
                    if (body.errorType.equals("Authorization")) {
                        Preferences.setIsLogged(false);
                        Utilities.login(mActivity);
                    }
                    else {
                        Utilities.showSnackBar(mActivity,holder.view,body.error);
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdatePredictionRequest.Response> call, Throwable t) {
                int x=0;
                t.printStackTrace();
                Utilities.showSnackBar(mActivity,holder.view,t.getMessage());
            }
        });
    }
}
