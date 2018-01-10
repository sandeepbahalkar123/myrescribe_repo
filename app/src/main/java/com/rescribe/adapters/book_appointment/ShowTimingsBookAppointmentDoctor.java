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
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 31/10/17.
 */

public class ShowTimingsBookAppointmentDoctor extends RecyclerView.Adapter<ShowTimingsBookAppointmentDoctor.ListViewHolder> {

    private String mFormattedCurrentDateString;
    private String mSelectedDate;
    private Context mContext;
    private ArrayList<TimeSlotsInfoList.TimeSlotData> mDataList;
    private static String mSelectedTimeSlot;

    public ShowTimingsBookAppointmentDoctor(Context mContext, ArrayList<TimeSlotsInfoList.TimeSlotData> dataList, String mSelectedDate) {
        this.mDataList = dataList;
        this.mContext = mContext;
        this.mSelectedDate = mSelectedDate;
        mFormattedCurrentDateString = CommonMethods.formatDateTime(CommonMethods.getCurrentDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DD_MM_YYYY, RescribeConstants.DATE);

    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_appointment_select_slot_childitem, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        final TimeSlotsInfoList.TimeSlotData timeSlotData = mDataList.get(position);

        String fromTime = timeSlotData.getFromTime();

        //-----------
        String s = CommonMethods.formatDateTime(fromTime, RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.DATE_PATTERN.HH_mm_ss, RescribeConstants.TIME);
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

                String mFormattedCurrentTimeString = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.HH_mm_ss);
                String s = "" + v.getTag();
                TimeSlotsInfoList.TimeSlotData tag1 = mDataList.get(Integer.parseInt(s));
                String fromTime = tag1.getFromTime();

                Date fromTimeDate = CommonMethods.convertStringToDate(mSelectedDate + " " + fromTime, RescribeConstants.DATE_PATTERN.YYYY_MM_DD_HH_mm_ss);
                Date currentDate = CommonMethods.convertStringToDate(mFormattedCurrentDateString + " " + mFormattedCurrentTimeString, RescribeConstants.DATE_PATTERN.YYYY_MM_DD_HH_mm_ss);

                if ((currentDate.getTime() > fromTimeDate.getTime()) && (mSelectedDate.equalsIgnoreCase(mFormattedCurrentDateString))) {
                    CommonMethods.showToast(mContext, mContext.getString(R.string.book_time_slot_err));
                } else {
                    if (tag1.isAvailable()) {
                        fromTime = tag1.getFromTime();
                        if (fromTime.equalsIgnoreCase(mSelectedTimeSlot)) {
                            mSelectedTimeSlot = "";
                            holder.mainLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.tab_white));
                            holder.showTime.setTextColor(ContextCompat.getColor(mContext, R.color.tagColor));

                        } else {
                            mSelectedTimeSlot = fromTime;
                            holder.mainLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.green_round_rectangle));
                            holder.showTime.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                            notifyDataSetChanged();
                        }
                    }
                }
            }
        });

        if (timeSlotData.getFromTime().equalsIgnoreCase(mSelectedTimeSlot)) {
            mSelectedTimeSlot = timeSlotData.getFromTime();
            holder.mainLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.green_round_rectangle));
            holder.showTime.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        } else {
            holder.mainLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.tab_white));
            if (timeSlotData.isAvailable()) {
                holder.showTime.setTextColor(ContextCompat.getColor(mContext, R.color.tagColor));
            } else {
                holder.showTime.setTextColor(ContextCompat.getColor(mContext, R.color.Gray));
            }
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


    public static String getSelectedTimeSlot() {
        return mSelectedTimeSlot;
    }
}
