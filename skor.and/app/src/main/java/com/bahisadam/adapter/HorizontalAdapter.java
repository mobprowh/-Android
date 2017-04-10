package com.bahisadam.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bahisadam.R;
import com.bahisadam.model.WeeksModel;
import com.bahisadam.utility.Utilities;

import java.util.List;


public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

    private List<WeeksModel> horizontalList;
    private Context ctx;
    OnCalendarClickListener mListener;
    OnDateSelectedListener mDateSelectedListener;

    public interface OnCalendarClickListener{
        public void onClick();
    }
    public interface OnDateSelectedListener{
        public void onDateSelected(String date);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_week_names;
        public TextView tv_days;
        public LinearLayout linear_horizontal;

        public MyViewHolder(View view) {
            super(view);
            tv_week_names = (TextView) view.findViewById(R.id.tv_week_names);
            Utilities.setTypeFace(ctx, tv_week_names);
            tv_days = (TextView) view.findViewById(R.id.tv_days);
            Utilities.setTypeFace(ctx, tv_days);
            linear_horizontal = (LinearLayout) view.findViewById(R.id.linear_horizontal);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(linear_horizontal.getLayoutParams());
            params.width = Utilities.getScreenWidth(ctx)/8;
            linear_horizontal.setLayoutParams(params);

        }

    }


    public HorizontalAdapter(Context ctx, List<WeeksModel> horizontalList) {
        this.ctx = ctx;
        this.horizontalList = horizontalList;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_list_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv_week_names.setText(horizontalList.get(position).getWeeks());
        holder.tv_days.setText(horizontalList.get(position).getDays());

        if (horizontalList.get(position).getCurrentDate() != null && horizontalList.get(position).getCurrentDate()) {
            holder.tv_days.setBackgroundResource(R.drawable.ic_check_circle_24dp);


        } else {
            int color = ContextCompat.getColor(holder.tv_days.getContext(),R.color.colorAccentDark);
            holder.tv_days.setBackgroundColor(color);

        }

        if (horizontalList.size() - 1 == position) {
            holder.tv_days.setBackgroundResource(R.drawable.ic_calendar_icon_24dp);
        }


    }
    public void setOnCalendarclickListener(OnCalendarClickListener mListener) {
        this.mListener = mListener;
    }
    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener){
        mDateSelectedListener = onDateSelectedListener;
    }
    public void performClick(int position){
        if(position==-1)return;
        if (horizontalList != null) {

            if (horizontalList.size() - 1 == position) {
                if(mListener!=null)
                    mListener.onClick();
                return;
            }

            if (horizontalList.size() - 1 != position) {
                if(mDateSelectedListener != null)
                    mDateSelectedListener.onDateSelected(Utilities.getDateList().get(position));
                for(int i = 0; i < horizontalList.size() -1; i++){
                    horizontalList.get(i).setCurrentDate(i == position);
                }
            }


            notifyDataSetChanged();
        }



    }
    public void clear(){
        horizontalList.clear();
        notifyDataSetChanged();
    }
    public void add(List<WeeksModel> weeks){
        horizontalList.addAll(weeks);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return horizontalList.size();
    }


}
