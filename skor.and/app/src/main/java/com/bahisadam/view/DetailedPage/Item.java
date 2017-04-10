package com.bahisadam.view.DetailedPage;

import android.support.v7.widget.RecyclerView;

/**
 * Created by atata on 01/12/2016.
 */

public interface Item {
    public int TYPE_VOTE=1,TYPE_STANDINGS = 2, TYPE_HOMEAWAY = 3, TYPE_COMMENTS = 4, TYPE_IDEA=5;
    public void bindViewHolder(RecyclerView.ViewHolder cardHolder);
    public int getItemType();
}
