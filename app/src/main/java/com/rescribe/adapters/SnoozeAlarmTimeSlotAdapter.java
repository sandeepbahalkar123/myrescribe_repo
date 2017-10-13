package com.rescribe.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rescribe.R;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SnoozeAlarmTimeSlotAdapter extends RecyclerView.Adapter<SnoozeAlarmTimeSlotAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<String> mDataList;
    private String mSelectedTimeSlot;

    public SnoozeAlarmTimeSlotAdapter(Context mContext, ArrayList<String> dataList) {
        this.mDataList = dataList;
        this.mContext = mContext;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_time_slot, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        String doctorObject = mDataList.get(position);
        holder.timeSlot.setText(doctorObject + " minutes");

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = (String) v.getTag();
                if (tag.equalsIgnoreCase(mSelectedTimeSlot)) {
                    mSelectedTimeSlot = "";
                    holder.mainLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rounded_rectangle_blue));
                } else {
                    mSelectedTimeSlot = tag;
                    holder.mainLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rounded_rectangle_light_blue));
                    notifyDataSetChanged();
                }
            }
        });

        if (doctorObject.equalsIgnoreCase(mSelectedTimeSlot)) {
            mSelectedTimeSlot = doctorObject;
            holder.mainLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rounded_rectangle_light_blue));
        } else {
            holder.mainLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rounded_rectangle_blue));
        }

        holder.view.setTag(doctorObject);

        holder.mainLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        holder.mainLayout.setGravity(Gravity.CENTER);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.timeSlot)
        CustomTextView timeSlot;
        @BindView(R.id.mainLayout)
        LinearLayout mainLayout;
        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public String getSelectedTimeSlot() {
        return mSelectedTimeSlot;
    }
}