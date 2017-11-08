package com.rescribe.adapters.book_appointment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.dashboard_api.DashboardClinicList;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<DashboardClinicList> mDataList;
    private String mSelectedTimeSlot;

    public TimeSlotAdapter(Context mContext, ArrayList<DashboardClinicList> dataList) {
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
        DashboardClinicList doctorObject = mDataList.get(position);
        //TODO : NEED TO IMPLEMENT
      //  holder.timeSlot.setText(doctorObject.ge);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String tag = (String) v.getTag();
                if (tag.equalsIgnoreCase(mSelectedTimeSlot)) {
                    mSelectedTimeSlot = "";
                 //   holder.mainLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rounded_rectangle_blue));
                } else {
                    mSelectedTimeSlot = tag;
                  //  holder.mainLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rounded_rectangle_light_blue));
                    notifyDataSetChanged();
                }*/
            }
        });

        /*if (doctorObject.equalsIgnoreCase(mSelectedTimeSlot)) {
            mSelectedTimeSlot = doctorObject;
            holder.mainLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rounded_rectangle_light_blue));
        } else {
            holder.mainLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rounded_rectangle_blue));
        }*/

        holder.view.setTag(doctorObject);

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
}