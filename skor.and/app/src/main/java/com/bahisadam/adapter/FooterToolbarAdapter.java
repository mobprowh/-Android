package com.bahisadam.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bahisadam.R;
import com.bahisadam.model.ToolbarItem;
import com.bahisadam.utility.Utilities;

import java.util.List;

/**
 * Created by atata on 24/11/2016.
 */

public class FooterToolbarAdapter extends RecyclerView.Adapter<FooterToolbarAdapter.FooterToolbarVH>  {
    List<ToolbarItem> data;
    Activity ctx;
    ToolbarItem active;

    public FooterToolbarAdapter(Activity ctx, List<ToolbarItem> data) {
        this.ctx = ctx;
        this.data = data;
    }


    @Override
    public FooterToolbarVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.toolbar_item, parent, false);
        return new FooterToolbarVH(itemView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(FooterToolbarVH holder, int position) {
        ToolbarItem item = data.get(position);
        holder.text.setText(item.name);
        holder.text.measure(0, 0);       //must call measure!

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(holder.layout.getLayoutParams());
        int width = Utilities.getScreenWidth(ctx)/5;
        int textWidth = holder.text.getMeasuredWidth();

        layoutParams.width = textWidth > width ? textWidth : width;
        holder.layout.setLayoutParams(layoutParams);



        String img = item.img;
        int textColor = Color.BLACK;
        if(item == active) {
            img = img+"_active";
            textColor =ctx.getResources().getColor(R.color.colorAccent);

        }
        holder.text.setTextColor(textColor);

        int id = ctx.getResources().getIdentifier(img,"drawable",ctx.getPackageName());
        holder.image.setImageResource(id);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(holder.image.getLayoutParams());
        params.width = Utilities.getPx(ctx,item.width);
        params.height = Utilities.getPx(ctx,item.height);
        holder.image.setLayoutParams(params);



    }



    public void setActive(int position) {
        if(position==-1) {
            this.active = null;
        }else {
            this.active = data.get(position);
            notifyDataSetChanged();
        }

    }

    class FooterToolbarVH extends RecyclerView.ViewHolder {
        LinearLayout layout;
        ImageView image;
        TextView text;
        public FooterToolbarVH(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.toolbarItemLayout);
            image = (ImageView) itemView.findViewById(R.id.toolbarItemImageView);
            text = (TextView) itemView.findViewById(R.id.toolbarItemTextView);
        }
    }
}
