package com.bahisadam.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bahisadam.R;
import com.bahisadam.model.MatchPOJO;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.bahisadam.utility.FontManager;

/**
 * Created by atata on 26/11/2016.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentVH> {
    List<MatchPOJO.Comment> data;
    Context ctx;

    OnLikeClickListener mListener;

    public void setOnLikeClickListener(OnLikeClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnLikeClickListener {
        public void onLikeClick(int pos);
    }
    public CommentsAdapter(Context ctx,List<MatchPOJO.Comment> data) {
        this.data = data;
        this.ctx = ctx;

    }


    @Override
    public CommentVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_item,parent,false);


        return new CommentVH(view);
    }

    @Override
    public void onBindViewHolder(CommentVH holder, int position) {
        MatchPOJO.Comment comment = data.get(position);
        holder.text.setText(comment.getReason());
        holder.nickname.setText(comment.getUserId().getNickname());
        DecimalFormat df = new DecimalFormat("#.00");
        holder.avatar.setImageResource(R.drawable.comment_user);
        //TODO: get from resources
        holder.likesCount.setText(comment.getTotalLike().toString());
        Typeface tf = FontManager.getTypeface(this.ctx, FontManager.FONTAWESOME);
        holder.likeIcon.setTypeface(tf);

        holder.userScore.setText("Skor: "+df.format(comment.getUserId().getScore()));
        holder.forecast.setText("Tahmin: "+comment.getForecast());
    }


    @Override

    public int getItemCount() {
        return data.size();
    }

    public class CommentVH extends RecyclerView.ViewHolder{
        @BindView(R.id.commentsAvatar) ImageView avatar;
        @BindView(R.id.commentsForecast) TextView forecast;
        @BindView(R.id.commentsNickname) TextView nickname;
        @BindView(R.id.commentsText) TextView text;
        @BindView(R.id.commentsUserScore) TextView userScore;
        @BindView(R.id.like_icon) TextView likeIcon;
        @BindView(R.id.likesCount) TextView likesCount;
        public CommentVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            likeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null)
                        mListener.onLikeClick(getAdapterPosition());
                }
            });
        }
    }
}
