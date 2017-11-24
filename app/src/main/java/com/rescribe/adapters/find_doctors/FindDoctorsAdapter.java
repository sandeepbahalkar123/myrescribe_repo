package com.rescribe.adapters.find_doctors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.rescribe.R;
import com.rescribe.model.book_appointment.reviews.Review;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 24/11/17.
 */

public class FindDoctorsAdapter  extends RecyclerView.Adapter<FindDoctorsAdapter.ListViewHolder> {

    private Context mContext;
    String[] mFindDoctorOptions = {
            "Book Appointment",
            "Doctor Connect"

    };
    Integer[] mSetIcons = {R.drawable.book_appointment_icon,
                           R.drawable.doctor_connect_icon};
    public FindDoctorsAdapter(Context mContext) {
        this.mContext = mContext;

    }

    @Override
    public FindDoctorsAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.services_item_layout, parent, false);

        return new FindDoctorsAdapter.ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FindDoctorsAdapter.ListViewHolder holder, int position) {

        holder.serviceNameTextView.setText(mFindDoctorOptions[position]);
        holder.recyclerViewClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // onServicesClickListener.setOnClickOfServices(servicesList);
            }
        });
        holder.serviceIcon.setImageResource(mSetIcons[position]);
    }

    @Override
    public int getItemCount() {
        return mFindDoctorOptions.length;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.serviceNameTextView)
        CustomTextView serviceNameTextView;
        @BindView(R.id.serviceIcon)
        ImageView serviceIcon;
        @BindView(R.id.recyclerViewClick)
        LinearLayout recyclerViewClick;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
