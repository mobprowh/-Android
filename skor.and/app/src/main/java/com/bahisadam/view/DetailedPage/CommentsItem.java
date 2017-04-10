package com.bahisadam.view.DetailedPage;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bahisadam.R;
import com.bahisadam.adapter.CommentsAdapter;
import com.bahisadam.fragment.DetailFragment;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.utility.Utilities;
import com.crashlytics.android.Crashlytics;

import java.util.List;

/**
 * Created by atata on 02/12/2016.
 */

public class CommentsItem implements Item {
    private Activity activity;
    private List<MatchPOJO.Comment> comments;
    private CommentsAdapter adapter;
    DetailFragment.CommentsHolder holder;

    public CommentsItem(List<MatchPOJO.Comment> comments, CommentsAdapter adapter,Activity activity) {
        this.comments = comments;
        this.adapter = adapter;
        this.activity = activity;



    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder cardHolder) {
        holder = (DetailFragment.CommentsHolder) cardHolder;
        holder.commentsView.setAdapter(adapter);
        holder.commentsView.setLayoutFrozen(true);
        //LinearLayoutManager llm = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
        LinearLayoutManager llm = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
        holder.commentsView.setLayoutManager(llm);
        if(comments!= null) {
            if (comments.size() == 0){
                holder.noComments.setVisibility(View.VISIBLE);
            }
            else{
                holder.noComments.setVisibility(View.GONE);
            }
        } else {
            holder.noComments.setVisibility(View.VISIBLE);
            Utilities.showSnackBar(activity,holder.commentsView,activity.getString(R.string.failed_to_load_comments));
            Crashlytics.log("bindViewHolder, comments = null\n" + Thread.currentThread().getStackTrace());
        }


    }
    public void updateComments(){
        bindViewHolder(holder);
    }
    @Override
    public int getItemType() {
        return TYPE_COMMENTS;
    }



}
