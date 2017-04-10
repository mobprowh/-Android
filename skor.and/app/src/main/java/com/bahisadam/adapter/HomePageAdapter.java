package com.bahisadam.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.bahisadam.Cache;
import com.bahisadam.MyApplication;
import com.bahisadam.R;
import com.bahisadam.fragment.FootballFragment;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.model.LeagueMatchList;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.utility.Utilities;
import com.bahisadam.view.HomeActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by atata on 19/11/2016.
 */

public class HomePageAdapter extends SectionedRecyclerViewAdapter<HomePageAdapter.MainVH> implements Constant {
    private Activity mActivity;
    private FootballFragment frag;
    private List<LeagueMatchList> data;
    private OnItemClickListener mListener;
    private final ArrayMap<Integer, Integer> mHeaderLocationMap;

    public HomePageAdapter(Activity mActivity, FootballFragment frag,List<LeagueMatchList> data ) {
        this.mActivity = mActivity;
        this.frag = frag;
        this.data  = data;
        mHeaderLocationMap = new ArrayMap<>();


    }

    public int[] getSectionIndexAndRelativePosition(int itemPosition) {
        synchronized (mHeaderLocationMap) {
            Integer lastSectionIndex = -1;
            for (final Integer sectionIndex : mHeaderLocationMap.keySet()) {
                if (itemPosition > sectionIndex) {
                    lastSectionIndex = sectionIndex;
                } else {
                    break;
                }
            }
            return new int[]{mHeaderLocationMap.get(lastSectionIndex), itemPosition - lastSectionIndex - 1};
        }
    }
    public int getSection(int absolutePath){
        return mHeaderLocationMap.get(absolutePath);
    }
    public int allocateHeaders(){
        int count = 0;
        mHeaderLocationMap.clear();
        for (int s = 0; s < getSectionCount(); s++) {
            int itemCount = getItemCount(s);
            if ((itemCount > 0)) {
                mHeaderLocationMap.put(count, s);
                count += itemCount + 1;
            }
        }
        return count;
    }

    public void onLeagueClick(int position) {
        MatchPOJO.LeagueId league = data.get(position).getLeauge();
        if(mListener!=null){
            mListener.onLeagueClick(league.getId(),league.getCurrentRound());
        }


    }

    public void onMatchClick(int section,int position){
        if(mListener!=null){
            String link = "http://www.bahisadam.com"+
                    data.get(section).get(position).getLink();
            String matchId = data.get(section).get(position).getId();
            int leagueId = data.get(section).getLeauge().getId();
            String home = data.get(section).get(position).getHomeTeam().getTeamNameTr();
            String away = data.get(section).get(position).getAwayTeam().getTeamNameTr();
            String result = data.get(section).get(position).getResultType();
            mListener.onMatchClick(matchId,leagueId,home,away,result);
        }
    }

    public interface OnItemClickListener {
        public void onLeagueClick(Integer leagueId,Integer round);
        public void onMatchClick(String matchId,int leagueId,String home,String away, String result);
    }
    public void setmOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }
    @Override
    public int getSectionCount() {
        return data.size();
    }

    @Override
    public int getItemCount(int section) {
        return data.get(section).size();
    }


    public Bitmap getBitmapCountry(int id){
        Bitmap bitmap = Cache.getBitmap("Country"+id);
        if(bitmap == null){
            try {
                Drawable dr;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    dr = getActivity().getResources().getDrawable(id, getActivity().getTheme());
                } else {
                    dr = getActivity().getResources().getDrawable(id);
                }
                Bitmap bmp = Bitmap.createBitmap(200,
                        200, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bmp);
                dr.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                dr.draw(canvas);
                bitmap= Utilities.getRoundedCornerBitmap(bmp,Utilities.getPx(getActivity(),50));
                Cache.addBitmap("Country"+id,bitmap);

            } catch (Resources.NotFoundException e){
                e.printStackTrace();
            }
        }
        return bitmap;

    }
    @Override
    public void onBindHeaderViewHolder(MainVH headerHolder, int section) {

        String leagueName = data.get(section).getLeauge().getLeagueName();
        headerHolder.sectionTitle.setText(leagueName);
        String countryCode = data.get(section).get(0).getCountry().getCountryCode();
        int id = mActivity.getResources().getIdentifier(countryCode,"drawable",mActivity.getPackageName());

/*
        try {
            Drawable dr;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                dr = getActivity().getResources().getDrawable(id, getActivity().getTheme());
            } else {
                dr = getActivity().getResources().getDrawable(id);
            }
            Bitmap bitmap = Bitmap.createBitmap(200,
                    200, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            dr.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            dr.draw(canvas);
            Bitmap rounded = Utilities.getRoundedCornerBitmap(bitmap,Utilities.getPx(getActivity(),50));
            //headerHolder.img_title_logo_img.setImageDrawable(dr);

        } catch (Resources.NotFoundException e){
            e.printStackTrace();
            //left placeholder
        }*/
        Bitmap bitmap = getBitmapCountry(id);
        if(bitmap != null){
            headerHolder.img_title_logo_img.setImageBitmap(bitmap);
        }




    }

    private Activity getActivity(){
        return mActivity;
    }
    @Override
    public void onBindViewHolder(MainVH itemHolder, final int section, final int relativePosition, final int absolutePosition) {
            MatchPOJO.Match currentMatch = data.get(section).get(relativePosition);

        if (currentMatch.getResultType().equals(PLAYED)) {

            itemHolder.linear_item_played_playing.setVisibility(View.VISIBLE);
            itemHolder.linear_item_not_played.setVisibility(View.GONE);
            itemHolder.linear_item_cancelled.setVisibility(View.GONE);
            itemHolder.logo_conversation.setVisibility(View.GONE);
            itemHolder.logo_conversation2.setVisibility(View.VISIBLE);
            itemHolder.tv_date_time.setTextColor(Color.parseColor("#000000"));

            itemHolder.linear_right_count.setBackgroundColor(Color.parseColor("#FFFFFF"));

            itemHolder.tv_match_count_played_playing.setText(String.valueOf(currentMatch.getHomeGoals()));
            itemHolder.tv_match_count_two_played_playing.setText(String.valueOf(currentMatch.getAwayGoals()));
            itemHolder.tv_date_time.setText(getActivity().getString(R.string.iv)
                    + "(" + currentMatch.getHalfTimeHomeScore() + "-" +
                    currentMatch.getHalfTimeAwayScore() + ")" + "\n\n" +
                    getString(R.string.ms) + "\n\n" + getString(R.string.detay));

            itemHolder.logo_conversation2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_expand_more_black_24dp));

        } else if (currentMatch.getResultType().equals(PLAYING)) {

            itemHolder.linear_item_played_playing.setVisibility(View.VISIBLE);
            itemHolder.linear_item_not_played.setVisibility(View.GONE);
            itemHolder.linear_item_cancelled.setVisibility(View.GONE);
            itemHolder.logo_conversation.setVisibility(View.GONE);
            itemHolder.logo_conversation2.setVisibility(View.VISIBLE);

            itemHolder.linear_right_count.setBackgroundColor(Color.parseColor("#39d58b"));
            itemHolder.tv_match_count_played_playing.setText(String.valueOf(currentMatch.getHomeGoals()));
            itemHolder.tv_match_count_two_played_playing.setText(String.valueOf(currentMatch.getAwayGoals()));
            itemHolder.tv_date_time.setTextColor(Color.parseColor("#FFFFFF"));

            if (currentMatch.getIsHalfTime() !=null && currentMatch.getIsHalfTime() == TRUE ) {
                itemHolder.tv_date_time.setText(getString(R.string.iv)
                        + "(" + currentMatch.getHalfTimeHomeScore() + "-" +
                        currentMatch.getHalfTimeAwayScore() + ")" + "\n\n" +
                        getString(R.string.ms) + "\n\n" + getString(R.string.detay));

            } else if (currentMatch.getIsHalfTime() !=null && currentMatch.getIsHalfTime() == FALSE) {
                itemHolder.tv_date_time.setText(currentMatch.getLiveMinute() + " " + getString(R.string.dk) + "\n\n" + getString(R.string.detay));

            }
            itemHolder.divider.setVisibility(View.GONE);
            itemHolder.logo_conversation2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_expand_more_white_24dp));

        } else if (currentMatch.getResultType().equals(NOT_PLAYED)) {

            itemHolder.linear_item_played_playing.setVisibility(View.GONE);
            itemHolder.linear_item_not_played.setVisibility(View.VISIBLE);
            itemHolder.linear_item_cancelled.setVisibility(View.GONE);
            itemHolder.logo_conversation.setVisibility(View.VISIBLE);
            itemHolder.logo_conversation2.setVisibility(View.GONE);
            itemHolder.tv_date_time.setTextColor(Color.parseColor("#000000"));

            itemHolder.linear_right_count.setBackgroundColor(Color.parseColor("#FFFFFF"));
            itemHolder.tv_date_time.setText(Utilities.formatDate(currentMatch.getMatchDate())
                    + "\n\n" +
                    Utilities.formatTime(currentMatch.getMatchDate()));
            itemHolder.logo_conversation.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mode_comment_white_24dp));
            itemHolder.logo_conversation.setText(String.valueOf(currentMatch.getForecastCount()));
            if (currentMatch.getOdds() != null) {
                itemHolder.tv_match_point_not_played.setText(String.valueOf(currentMatch.getOdds().getIddaa().get1()));
                itemHolder.tv_match_point_X_not_played.setText(String.valueOf(currentMatch.getOdds().getIddaa().getX()));
                itemHolder.tv_match_point_two_not_played.setText(String.valueOf(currentMatch.getOdds().getIddaa().get2()));
            } else {
                itemHolder.tv_match_point_not_played.setText("-");
                itemHolder.tv_match_point_X_not_played.setText("-");
                itemHolder.tv_match_point_two_not_played.setText("-");
            }

        } else if (currentMatch.getResultType().equals(CANCELLED)) {

            itemHolder.linear_item_played_playing.setVisibility(View.GONE);
            itemHolder.linear_item_not_played.setVisibility(View.GONE);
            itemHolder.linear_item_cancelled.setVisibility(View.INVISIBLE);
            itemHolder.logo_conversation.setVisibility(View.GONE);
            itemHolder.logo_conversation2.setVisibility(View.VISIBLE);
            itemHolder.tv_date_time.setTextColor(Color.parseColor("#000000"));
            itemHolder.linear_right_count.setBackgroundColor(Color.parseColor("#FFFFFF"));
            itemHolder.logo_conversation2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_expand_more_black_24dp));
            itemHolder.tv_date_time.setText(getString(R.string.postponed)
                    + "\n\n" +
                    getString(R.string.ms) + "\n\n" + getString(R.string.detay));

        } else if (currentMatch.getResultType().equals(POSTPONED)) {
            itemHolder.linear_item_played_playing.setVisibility(View.GONE);
            itemHolder.linear_item_not_played.setVisibility(View.GONE);
            itemHolder.linear_item_cancelled.setVisibility(View.INVISIBLE);
            itemHolder.logo_conversation.setVisibility(View.GONE);
            itemHolder.logo_conversation2.setVisibility(View.VISIBLE);
            itemHolder.tv_date_time.setTextColor(Color.parseColor("#000000"));
            itemHolder.linear_right_count.setBackgroundColor(Color.parseColor("#FFFFFF"));
            itemHolder.logo_conversation2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_expand_more_black_24dp));

            itemHolder.tv_date_time.setText(getString(R.string.postponed)
                    + "\n\n" +
                    getString(R.string.ms) + "\n\n" + getString(R.string.detay));

        } else if (currentMatch.getResultType().equals(DELAYED)) {
            itemHolder.linear_item_played_playing.setVisibility(View.GONE);
            itemHolder.linear_item_not_played.setVisibility(View.GONE);
            itemHolder.linear_item_cancelled.setVisibility(View.INVISIBLE);
            itemHolder.logo_conversation.setVisibility(View.GONE);
            itemHolder.logo_conversation2.setVisibility(View.VISIBLE);
            itemHolder.tv_date_time.setTextColor(Color.parseColor("#000000"));
            itemHolder.linear_right_count.setBackgroundColor(Color.parseColor("#FFFFFF"));
            itemHolder.logo_conversation2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_expand_more_black_24dp));

            itemHolder.tv_date_time.setText(getString(R.string.postponed)
                    + "\n\n" +
                    getString(R.string.ms) + "\n\n" + getString(R.string.detay));
        } else {
            itemHolder.linear_item_played_playing.setVisibility(View.GONE);
            itemHolder.linear_item_not_played.setVisibility(View.GONE);
            itemHolder.linear_item_cancelled.setVisibility(View.INVISIBLE);
            itemHolder.logo_conversation.setVisibility(View.GONE);
            itemHolder.logo_conversation2.setVisibility(View.VISIBLE);
            itemHolder.linear_right_count.setBackgroundColor(Color.parseColor("#FFFFFF"));
            itemHolder.tv_date_time.setTextColor(Color.parseColor("#000000"));
            itemHolder.logo_conversation2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_expand_more_black_24dp));

            itemHolder.tv_date_time.setText(getString(R.string.postponed)
                    + "\n\n" +
                    getString(R.string.ms) + "\n\n" + getString(R.string.detay));
        }

        String homeName= currentMatch.getHomeTeam().getTeamName();
        if(homeName.length() > 10 && homeName.contains(" ")){
            String[] s = homeName.split(" ");
            homeName = s[0] + " " +  s[1].substring(0,1) + ".";
        }
        String awayName= currentMatch.getAwayTeam().getTeamName();
        if(awayName.length() > 10 && awayName.contains(" ")){
            String[] s = awayName.split(" ");
            awayName = s[0] + " " + s[1].substring(0,1) + ".";
        }

        itemHolder.tv_home_team_name.setText(homeName);
        itemHolder.tv_away_team_name.setText(awayName);
    if(MyApplication.sUse_Logo){
        Picasso.with(mActivity).
                load(IMAGES_ROOT+currentMatch.getHomeTeam().getTeamLogos().get(0))
                .into(itemHolder.home_team_logo);
        Picasso.with(mActivity).
                load(IMAGES_ROOT+currentMatch.getAwayTeam().getTeamLogos().get(0))
                .into(itemHolder.away_team_logo);

    } else {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            String color1 = currentMatch.getHomeTeam().getColor1();
            String color2 = currentMatch.getHomeTeam().getColor2();
            //Bitmap homeLogo = frag.getBitmapFromMemCache(color1,color2);
            //itemHolder.home_team_logo.setImageBitmap(homeLogo);
            frag.loadBitmatIntoView(itemHolder.home_team_logo,color1,color2);

            color1 = currentMatch.getAwayTeam().getColor1();
            color2 = currentMatch.getAwayTeam().getColor2();
            // Bitmap awayLogo = frag.getBitmapFromMemCache(color1,color2);
            //itemHolder.away_team_logo.setImageBitmap(awayLogo);
            frag.loadBitmatIntoView(itemHolder.away_team_logo,color1,color2);

        }
    }


        itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMatchClick(section,relativePosition);
                if (Utilities.isNetworkAvailable(getActivity())) {

                    Log.e("Click", "Click??" + absolutePosition);

                } else {
                    Utilities.showSnackBarInternet(getActivity(),
                            HomeActivity.getInstance().getCoordinateLayout(),
                            getString(R.string.no_internet_connection));
                }

            }
        });
    }



    private String getString(int id) {
        return  getActivity().getString(id);
    }

    @Override
    public MainVH onCreateViewHolder(ViewGroup parent, int viewType) {
        boolean header = viewType == VIEW_TYPE_HEADER;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(viewType == VIEW_TYPE_HEADER ? R.layout.list_item_section : R.layout.item_matches, parent, false);
        return new MainVH(header,v);
    }


    public class MainVH extends RecyclerView.ViewHolder {
        private  TextView sectionTitle ;
        private SimpleDraweeView img_title_logo_play ;
        private ImageView img_title_logo_img;
        private View rootView;

        private TextView tv_home_team_name;
        private TextView tv_away_team_name;

        private TextView tv_date_time;

        private TextView tv_match_count_not_played;
        private TextView tv_match_point_not_played;
        private TextView tv_iddaa_not_played;
        private TextView tv_match_count_X_not_played;
        private TextView tv_match_point_X_not_played;
        private TextView tv_iddaa_X_not_played;
        private TextView tv_match_count_two_not_played;
        private TextView tv_match_point_two_not_played;
        private TextView tv_iddaa_two_not_played;

        private TextView tv_match_count_played_playing;
        private TextView tv_match_count_two_played_playing;

        private SimpleDraweeView home_team_logo;
        private SimpleDraweeView away_team_logo;
        private Button logo_conversation;
        private SimpleDraweeView logo_conversation2;
        private View divider;

        private LinearLayout linear_item_not_played;
        private LinearLayout linear_item_played_playing;
        private LinearLayout linear_item_cancelled;
        private LinearLayout linear_right_count;
        public MainVH(boolean header, View view){
            super(view);
            if(header){
                sectionTitle = (TextView) view.findViewById(R.id.sectionTitle);
                Utilities.setTypeFace2(getActivity(), sectionTitle);
                //img_title_logo_play = (SimpleDraweeView) view.findViewById(R.id.img_title_logo_play);
                img_title_logo_img= (ImageView) view.findViewById(R.id.img_title_logo_img);
            }else{
                rootView = view;

                linear_item_not_played = (LinearLayout) view.findViewById(R.id.linear_item_not_played);
                linear_item_played_playing = (LinearLayout) view.findViewById(R.id.linear_item_played_playing);
                linear_item_cancelled = (LinearLayout) view.findViewById(R.id.linear_item_cancelled);
                linear_right_count = (LinearLayout) view.findViewById(R.id.linear_right_count);

                tv_home_team_name = (TextView) view.findViewById(R.id.tv_home_team_name);
                Utilities.setTypeFace(getActivity(), tv_home_team_name);
                tv_away_team_name = (TextView) view.findViewById(R.id.tv_away_team_name);
                Utilities.setTypeFace(getActivity(), tv_away_team_name);

                tv_match_count_not_played = (TextView) view.findViewById(R.id.tv_match_count_not_played);
                Utilities.setTypeFace(getActivity(), tv_match_count_not_played);
                tv_match_point_not_played = (TextView) view.findViewById(R.id.tv_match_point_not_played);
    //            Utilities.setTypeFace(getActivity(), tv_match_point_not_played);
                tv_iddaa_not_played = (TextView) view.findViewById(R.id.tv_iddaa_not_played);
    //            Utilities.setTypeFace(getActivity(), tv_iddaa_not_played);
                tv_match_count_X_not_played = (TextView) view.findViewById(R.id.tv_match_count_X_not_played);
                Utilities.setTypeFace(getActivity(), tv_match_count_X_not_played);
                tv_match_point_X_not_played = (TextView) view.findViewById(R.id.tv_match_point_X_not_played);
    //            Utilities.setTypeFace(getActivity(), tv_match_point_X_not_played);
                tv_iddaa_X_not_played = (TextView) view.findViewById(R.id.tv_iddaa_X_not_played);
  //              Utilities.setTypeFace(getActivity(), tv_iddaa_X_not_played);
                tv_match_count_two_not_played = (TextView) view.findViewById(R.id.tv_match_count_two_not_played);
                Utilities.setTypeFace(getActivity(), tv_match_count_two_not_played);
                tv_match_point_two_not_played = (TextView) view.findViewById(R.id.tv_match_point_two_not_played);
    //            Utilities.setTypeFace(getActivity(), tv_match_point_two_not_played);
                tv_iddaa_two_not_played = (TextView) view.findViewById(R.id.tv_iddaa_two_not_played);
   //             Utilities.setTypeFace(getActivity(), tv_iddaa_two_not_played);

                tv_match_count_played_playing = (TextView) view.findViewById(R.id.tv_match_count_played_playing);
                Utilities.setTypeFace(getActivity(), tv_match_count_played_playing);
                tv_match_count_two_played_playing = (TextView) view.findViewById(R.id.tv_match_count_two_played_playing);
                Utilities.setTypeFace(getActivity(), tv_match_count_two_played_playing);

                tv_date_time = (TextView) view.findViewById(R.id.tv_date_time);
                Utilities.setTypeFace(getActivity(), tv_date_time);

                home_team_logo = (SimpleDraweeView) view.findViewById(R.id.home_team_logo);
                away_team_logo = (SimpleDraweeView) view.findViewById(R.id.away_team_logo);
                logo_conversation = (Button) view.findViewById(R.id.logo_conversation);
                logo_conversation2 = (SimpleDraweeView) view.findViewById(R.id.logo_conversation2);
                divider = view.findViewById(R.id.commentsDivider);
            }

        }
        public MainVH(View view) {
            super(view);

        }

    }


}
