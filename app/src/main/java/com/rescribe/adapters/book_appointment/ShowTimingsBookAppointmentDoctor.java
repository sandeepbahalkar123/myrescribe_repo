package com.rescribe.adapters.book_appointment;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rescribe.R;
import com.rescribe.model.book_appointment.select_slot_book_appointment.TimeSlotsInfoList;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 31/10/17.
 */

public class ShowTimingsBookAppointmentDoctor extends RecyclerView.Adapter<ShowTimingsBookAppointmentDoctor.ListViewHolder> {

    private Context mContext;
    private ArrayList<TimeSlotsInfoList.TimeSlotData> mDataList;
    private String mSelectedTimeSlot;

    public ShowTimingsBookAppointmentDoctor(Context mContext, ArrayList<TimeSlotsInfoList.TimeSlotData> dataList) {
        this.mDataList = dataList;
        this.mContext = mContext;

    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_appointment_select_slot_childitem, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        TimeSlotsInfoList.TimeSlotData timeSlotData = mDataList.get(position);

        String fromTime = timeSlotData.getFromTime();

        //-----------
        String s = CommonMethods.formatDateTime(fromTime, RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.DATE_PATTERN.HH_mm, RescribeConstants.TIME);
        holder.showTime.setText(s);
        //-----------

        if (timeSlotData.isAvailable()) {
            holder.showTime.setTextColor(ContextCompat.getColor(mContext, R.color.tagColor));
        } else {
            holder.showTime.setTextColor(ContextCompat.getColor(mContext, R.color.Gray));
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "" + v.getTag();
                TimeSlotsInfoList.TimeSlotData tag1 = mDataList.get(Integer.parseInt(s));
                if (tag1.isAvailable()) {
                    String fromTime = tag1.getFromTime();
                    if (fromTime.equalsIgnoreCase(mSelectedTimeSlot)) {
                        mSelectedTimeSlot = "";
                        holder.mainLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.tab_white));
                    } else {
                        mSelectedTimeSlot = fromTime;
                        holder.mainLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.green_round_rectangle));
                        notifyDataSetChanged();
                    }
                }
            }
        });

        if (timeSlotData.getFromTime().equalsIgnoreCase(mSelectedTimeSlot)) {
            mSelectedTimeSlot = timeSlotData.getFromTime();
            holder.mainLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.green_round_rectangle));
        } else {
            holder.mainLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.tab_white));
        }

        holder.view.setTag("" + position);

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.showTime)
        CustomTextView showTime;
        @BindView(R.id.mainLayout)
        LinearLayout mainLayout;
        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
