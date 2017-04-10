package com.bahisadam.adapter.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bahisadam.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by atata on 22/12/2016.
 */

public class SectionedHeaderHolder extends RecyclerView.ViewHolder {
    public @BindView(R.id.titleLabel)
    TextView label;
    public SectionedHeaderHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
