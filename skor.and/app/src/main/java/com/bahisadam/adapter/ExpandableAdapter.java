package com.bahisadam.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bahisadam.Cache;
import com.bahisadam.R;
import com.bahisadam.model.TournamentListRequest;
import com.bahisadam.utility.Utilities;

import java.util.List;

/**
 * Created by atata on 16/12/2016.
 */

public class ExpandableAdapter extends BaseExpandableListAdapter {
    List<TournamentListRequest.Country> mData;

    public ExpandableAdapter(List<TournamentListRequest.Country> mData) {
        this.mData = mData;
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mData.get(i).getLeagues().size();
    }

    @Override
    public Object getGroup(int i) {
        return mData.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mData.get(i).getLeagues().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup parent) {
        GroupViewHolder holder;
        Context ctx = parent.getContext();
        if( view== null ){
            view = LayoutInflater.from(ctx).inflate(R.layout.tournament_item_header,parent,false);
            holder = new GroupViewHolder(view);


            view.setTag(holder);
        }
        holder = (GroupViewHolder) view.getTag();

        holder.countryTv.setText(mData.get(i).getCountryNameTr());
        int id = ctx.getResources().getIdentifier(mData.get(i).getCountryCode().replace('-', '_'),"drawable",ctx.getPackageName());
        Bitmap bitmap = getBitmapCountry(id,holder.countryIv.getContext());
        if(bitmap != null) {
            holder.countryIv.setImageBitmap(bitmap);
        } else {
            holder.countryIv.setImageResource(android.R.color.transparent);
        }
        //holder.countryIv.setImageResource(id);

        return view;
    }
    public Bitmap getBitmapCountry(int id,Context context){
        Bitmap bitmap = Cache.getBitmap("Country"+id);
        if(bitmap == null){
            try {
                Drawable dr;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    dr = context.getResources().getDrawable(id, context.getTheme());
                } else {
                    dr = context.getResources().getDrawable(id);
                }
                Bitmap bmp = Bitmap.createBitmap(200,
                        150, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bmp);
                dr.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                dr.draw(canvas);
                bitmap= Utilities.getRoundedCornerBitmap(bmp,Utilities.getPx(context,0));
                Cache.addBitmap("Country"+id,bitmap);

            } catch (Resources.NotFoundException e){
                //e.printStackTrace();
            }
        }
        return bitmap;

    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder holder;
        int x=0;
        if(view == null){
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.tournament_item,viewGroup,false);
            holder = new ChildViewHolder(view);
            view.setTag(holder);
        }
        holder = (ChildViewHolder) view.getTag();
        holder.itemTv.setText(mData.get(i).getLeagues().get(i1).getLeagueNameTr());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    private class GroupViewHolder{
        final TextView countryTv;
        final ImageView countryIv;

        public GroupViewHolder(View group) {
            countryIv = (ImageView) group.findViewById(R.id.itemIV);
            countryTv = (TextView) group.findViewById(R.id.itemTV);


        }
    }
    private class ChildViewHolder{
        final TextView itemTv;
        final ImageView itemIv;
        final LinearLayout layout;

        public ChildViewHolder(View view) {
            itemIv = (ImageView) view.findViewById(R.id.itemIV);
            itemTv = (TextView) view.findViewById(R.id.itemTV);
            layout = (LinearLayout) view.findViewById(R.id.layout);
        }
    }

}
