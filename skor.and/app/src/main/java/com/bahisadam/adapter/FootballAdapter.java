package com.bahisadam.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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
import com.bahisadam.utility.FontManager;
import com.bahisadam.utility.Preferences;
import com.bahisadam.utility.Utilities;

import java.util.*;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by atata on 26/12/2016.
 * Football Adapter
 */

public class FootballAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> implements Constant {

    public List<LeagueMatchList> data;
    private final ArrayMap<Integer, Integer> mHeaderLocationMap;
    private Activity activity;
    private FootballFragment frag;
    private Set<String> favoriteMatches;
    private OnMatchClickListener mListener;

    public FootballAdapter(List<LeagueMatchList> data, Activity activity, FootballFragment frag) {

        this.data = data;
        mHeaderLocationMap = new ArrayMap<>();
        this.activity = activity;
        this.frag = frag;
        favoriteMatches = Preferences.getFavoriteMatches();
    }

    /*
    int[] getSectionIndexAndRelativePosition(int itemPosition) {
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
    */

    public int getSection(int absolutePath){
        return mHeaderLocationMap.get(absolutePath);
    }

    /*
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
    */

    @Override
    public int getSectionCount() {
        return data.size();
    }

    @Override
    public int getItemCount(int section) {
        return data.get(section).getData().size();
    }

    public Activity getActivity() {
        return activity;
    }

    private Bitmap getBitmapCountry(int id){

        Bitmap bitmap = Cache.getBitmap("Country"+id);
        if(bitmap == null){
            try {
                Drawable dr;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    dr = getActivity().getResources().getDrawable(id, getActivity().getTheme());
                } else {
                    //noinspection deprecation
                    dr = getActivity().getResources().getDrawable(id);
                }
                Bitmap bmp = Bitmap.createBitmap(200,
                        150, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bmp);
                dr.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                dr.draw(canvas);
                bitmap= Utilities.getRoundedCornerBitmap(bmp,Utilities.getPx(getActivity(),0));
                Cache.addBitmap("Country"+id,bitmap);

            } catch (Resources.NotFoundException e){
                Log.e("Resourcen not found", e.getMessage());
            }
        }
        return bitmap;

    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder headerHolder, final int section) {
        HeaderVH holder = (HeaderVH) headerHolder;
        String leagueName = data.get(section).getLeauge().getLeagueName();
        holder.sectionTitle.setText(leagueName);
        final String countryCode = data.get(section).get(0).getCountry().getCountryCode().replace('-', '_');
        Activity ctx = getActivity();
        int id = ctx.getResources().getIdentifier(countryCode,"drawable",ctx.getPackageName());
        Bitmap bitmap = getBitmapCountry(id);
        if(bitmap != null){
            holder.img_title_logo_img.setImageBitmap(bitmap);
        }

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MatchPOJO.LeagueId league = data.get(section).getLeauge();

                Utilities.openLeagueDetails(getActivity(),
                        league.getId(),
                        league.getLeagueNameTr(),
                        countryCode);
            }
        });

        Typeface tf = FontManager.getTypeface(activity, FontManager.FONTAWESOME);
        holder.rightArrow.setTypeface(tf);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder, int section, int relativePosition, int absolutePosition) {
        final ItemVH holder = (ItemVH) itemHolder;
        final MatchPOJO.Match currentMatch = data.get(section).get(relativePosition);
        MatchPOJO.HomeTeam homeTeam = currentMatch.getHomeTeam();
        MatchPOJO.AwayTeam awayTeam = currentMatch.getAwayTeam();

        if(homeTeam != null && awayTeam != null ) {
            holder.homeTeam.setText(homeTeam.getTeamNameTr());
            holder.awayTeam.setText(awayTeam.getTeamNameTr());
            if(MyApplication.sUse_Logo) {
                Utilities.loadLogoToView(holder.homeTeamLogo,
                        currentMatch.getHomeTeam().getId());
                Utilities.loadLogoToView(holder.awayTeamLogo,
                        currentMatch.getAwayTeam().getId());
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                String color1 = currentMatch.getHomeTeam().getColor1();
                String color2 = currentMatch.getHomeTeam().getColor2();
                frag.loadBitmatIntoView(holder.homeTeamLogo,color1,color2);

                color1 = currentMatch.getAwayTeam().getColor1();
                color2 = currentMatch.getAwayTeam().getColor2();
                frag.loadBitmatIntoView(holder.awayTeamLogo,color1,color2);

            }
        }

        holder.notPlayedLayout.setVisibility(View.GONE);
        holder.commentsLayout.setVisibility(View.GONE);
        holder.liveLayout.setVisibility(View.GONE);
        holder.scoreLayout.setVisibility(View.GONE);
        holder.playedPlayingLayout.setVisibility(View.GONE);
        holder.itemSection = section;
        holder.relativePosition = relativePosition;
        holder.absolutePosition = absolutePosition;
        holder.code.setText(currentMatch.iddaa_code);
        if(currentMatch.iddaa_code == null) {
            holder.code.setText("-");
        }

        switch (currentMatch.getResultType()) {
            case CANCELLED:
            case POSTPONED:
                holder.details.setText(R.string.statusCanceled);
                holder.details.setVisibility(View.VISIBLE);
                holder.scoreLayout.setVisibility(View.VISIBLE);
                holder.playedPlayingLayout.setVisibility(View.VISIBLE);
                break;
            case PLAYED:
            case PLAYING:
                holder.playedPlayingLayout.setVisibility(View.VISIBLE);
                holder.scoreLayout.setVisibility(View.VISIBLE);

                if (currentMatch.getHomeGoals() != null)
                    holder.homeTeamScore.setText(String.format(Locale.US, "%d", currentMatch.getHomeGoals()));

                    //holder.homeTeamScore.setText(currentMatch.getHomeGoals().toString());
                if (currentMatch.getAwayGoals() != null)
                    holder.awayTeamScore.setText(String.format(Locale.US, "%d", currentMatch.getAwayGoals()));
                    //holder.awayTeamScore.setText(currentMatch.getAwayGoals().toString());

                String detailsStr = activity.getString(R.string.detay);
                if (currentMatch.getResultType().equals(PLAYED)) {
                    detailsStr = activity.getString(R.string.iv) + "(" + currentMatch.getHalfTimeHomeScore() + "-" +
                            currentMatch.getHalfTimeAwayScore() + ")" + "\n" +
                            activity.getString(R.string.ms) + "\n" + detailsStr;
                    holder.playedPlayingLayout.setBackgroundColor(0);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        holder.details.setTextColor(activity.getResources().getColor(R.color.dark_grey, null));
                    }else{
                        //noinspection deprecation
                        holder.details.setTextColor(activity.getResources().getColor(R.color.dark_grey));
                    }

                } else {


                    try {
                        if (currentMatch.getIsHalfTime()) {

                            detailsStr = activity.getString(R.string.iv) + "\n" + detailsStr;
                        } else {

                            detailsStr = currentMatch.getLiveMinute() + " " + getActivity().getString(R.string.dk) + "\n\n" + detailsStr;
                        }
                    } catch (NullPointerException e) {

                        detailsStr = "0 " + getActivity().getString(R.string.dk) + "\n\n" + detailsStr;
                        e.printStackTrace();
                    }

               /* ColorDrawable[] color = {new ColorDrawable(R.color.live_score), new ColorDrawable(R.color.grass_green)};
                TransitionDrawable trans = new TransitionDrawable(color);
                holder.playedPlayingLayout.setBackground(trans);*/
                    //  trans.startTransition(600); // duration 3 seconds

                    final Animation animation1 = new AlphaAnimation(0.7f, 1f);
                    animation1.setDuration(200);

                    final Animation animation2 = new AlphaAnimation(1f, 0.7f);
                    animation2.setDuration(200);

                    //animation1 AnimationListener
                    animation1.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            // start animation2 when animation1 ends (continue)
                            holder.playedPlayingLayout.startAnimation(animation2);
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onAnimationStart(Animation arg0) {
                            // TODO Auto-generated method stub

                        }

                    });

                    //animation2 AnimationListener
                    animation2.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            // start animation1 when animation2 ends (repeat)
                            holder.playedPlayingLayout.startAnimation(animation1);
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onAnimationStart(Animation arg0) {
                            // TODO Auto-generated method stub
                        }

                    });

                    holder.playedPlayingLayout.setBackgroundColor(Color.parseColor("#36d78b"));
                    holder.details.setTextColor(Color.parseColor("#ffffff"));
                    holder.playedPlayingLayout.startAnimation(animation1);

                }
                holder.details.setText(detailsStr);

                break;
            case NOT_PLAYED:
                holder.commentsLayout.setVisibility(View.VISIBLE);
                holder.liveLayout.setVisibility(View.VISIBLE);
                holder.notPlayedLayout.setVisibility(View.VISIBLE);
                holder.comments.setText(String.valueOf(currentMatch.getForecastCount()));
                if (currentMatch.getMatchDate() != null) {
                    Date d = Utilities.parseJSONDate(currentMatch.getMatchDate());
                    String dateStr = Utilities.formatDate(d,"HH:mm");
                    holder.time.setText(dateStr);
                }
                Typeface tf = FontManager.getTypeface(activity, FontManager.FONTAWESOME);
                holder.favorite.setTypeface(tf);
                holder.tv.setTypeface(tf);
                final String matchId = currentMatch.getId();
                paintFavoriteIcon(holder.favorite, currentMatch.getIsFavorite());
                break;
        }

    }

    private void paintFavoriteIcon(TextView icon,boolean active){
        int color = Color.parseColor("#4e5757");
        String text = activity.getString(R.string.fa_star_o);

        if(active){
            color = ContextCompat.getColor(MyApplication.getAppContext(),R.color.colorPrimary);
            text = activity.getString(R.string.fa_star);

        }
        icon.setText(text);
        icon.setTextColor(color);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == VIEW_TYPE_HEADER){
            return new HeaderVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_section,parent,false));
        }else {
            return new ItemVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_page_item,parent,false));
        }
    }

    public interface OnMatchClickListener {
        void onFavoriteClick(int section,int relativePosition,int absolutePosition);
        //void onIddaaClick(int section,int relativePosition);
        //void onCommentsClick(int section,int relativePosition);
        void onMatchClick(int section,int relativePosition);
    }

    public void setMatchClickListener(OnMatchClickListener listener ){
        mListener = listener;
    }

    class HeaderVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.img_title_logo_img) ImageView img_title_logo_img;
        @BindView(R.id.sectionTitle) TextView sectionTitle;
        @BindView(R.id.right_arrow) TextView rightArrow;
        @BindView(R.id.card_view) CardView card_view;

        HeaderVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            card_view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener == null) return;
        }
    }

    class ItemVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.homeTeamLogo) ImageView homeTeamLogo;
        @BindView(R.id.awayTeamLogo) ImageView awayTeamLogo;
        @BindView(R.id.homeTeam) TextView homeTeam;
        @BindView(R.id.awayTeam) TextView awayTeam;
        @BindView(R.id.comments) TextView comments;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.favorite) TextView favorite;
        @BindView(R.id.code) TextView code;
        @BindView(R.id.homeTeamScore) TextView homeTeamScore;
        @BindView(R.id.awayTeamScore) TextView awayTeamScore;
        @BindView(R.id.notPlayed) LinearLayout notPlayedLayout;
        @BindView(R.id.playedPlaying) LinearLayout playedPlayingLayout;
        @BindView(R.id.details) TextView details;
        @BindView(R.id.tv) TextView tv;
        @BindView(R.id.rootLayout) LinearLayout rootLayout;
        @BindView(R.id.commentsLayout) LinearLayout commentsLayout;
        @BindView(R.id.liveLayout) LinearLayout liveLayout;
        @BindView(R.id.scoreLayout) LinearLayout scoreLayout;

        int itemSection = -1;
        int relativePosition = -1;
        int absolutePosition = -1;

        ItemVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            rootLayout.setOnClickListener(this);
            favorite.setOnClickListener(this);
            this.setIsRecyclable(false);

        }

        @Override
        public void onClick(View v) {
            if(mListener ==null) return;

            try {
                //int[] positions = getSectionIndexAndRelativePosition(getAdapterPosition());

                switch (v.getId()) {
                    case R.id.rootLayout:
                        //mListener.onMatchClick(positions[0], positions[1]);
                        mListener.onMatchClick(itemSection, relativePosition);
                        break;
                    case R.id.favorite:
                        //mListener.onFavoriteClick(positions[0], positions[1], getAdapterPosition());
                        mListener.onFavoriteClick(itemSection, relativePosition, absolutePosition);
                        break;
                }
            } catch (NullPointerException e) {

                // pass
                e.printStackTrace();
            }
        }
    }
}
