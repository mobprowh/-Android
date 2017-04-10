package com.bahisadam.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * @Author Eduardo Barrenechea
 */


public interface StickyHeaderAdapter<T extends RecyclerView.ViewHolder> {

    /**
     * Returns the header id for the item at the given position.
     *
     * @param position the item position
     * @return the header id
     */
    long getHeaderId(int position);
    /**
     * Returns the header relative position for the header at the absolute position.
     *
     * @param headerAbsolutePosition the header absolute position
     * @return the header relative position
     */
    int getHeaderRelativePosition(int headerAbsolutePosition);
    /**
     * Creates a new header ViewHolder.
     *
     * @param parent the header's view parent
     * @return a view holder for the created view
     */
    T onCreateHeaderViewHolder(ViewGroup parent);

    /**
     * Updates the header view to reflect the header data for the given position
     * @param viewholder the header view holder
     * @param position the header's item position
     */
    void onBindHeaderViewHolder(T viewholder, int position);
    void onBindViewHolder(T holder, int position);
    RecyclerView.ViewHolder getHolder(int position);
    void onHeaderClick(int position);

}