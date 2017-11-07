package com.rescribe.adapters.dashboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.rescribe.R;
import com.rescribe.model.dashboard_api.DashboardDoctorList;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 7/11/17.
 */

public class MyAppointmentAdapter extends RecyclerView.Adapter<MyAppointmentAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<DashboardDoctorList> mDataList;

    public MyAppointmentAdapter(Context mContext, ArrayList<DashboardDoctorList> dataList) {
        this.mDataList = dataList;
        this.mContext = mContext;

    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_appointment_doctor_list, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        final DashboardDoctorList doctorObject = mDataList.get(position);
        holder.doctorCategoryType.setText(doctorObject.getCategoryName());
        holder.doctorName.setText(doctorObject.getDocName());
        holder.doctorExperience.setText(doctorObject.getExperience()+mContext.getString(R.string.space)+mContext.getString(R.string.years_experience));
        if(doctorObject.getClinicList().size()==1){
            holder.doctorAddress.setText(doctorObject.getClinicList().get(0).getClinicAddress());
        }else{
            holder.doctorAddress.setText(doctorObject.getClinicList().size()+mContext.getString(R.string.space)+mContext.getString(R.string.locations));
        }
        if(doctorObject.getRating()==0){
            holder.ratingBar.setVisibility(View.INVISIBLE);
            holder.doctorRating.setVisibility(View.INVISIBLE);
        }else{
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.doctorRating.setVisibility(View.VISIBLE);
            holder.ratingBar.setRating((float) doctorObject.getRating());
            holder.doctorRating.setText(""+doctorObject.getRating());
        }
        if(doctorObject.getCategoryName().equals(mContext.getString(R.string.my_appointments))){
            holder.tokenNo.setVisibility(View.INVISIBLE);
        }else{
            holder.tokenNo.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.blueLine)
        ImageView blueLine;
        @BindView(R.id.doctorCategoryType)
        CustomTextView doctorCategoryType;
        @BindView(R.id.ruppessIcon)
        ImageView ruppessIcon;
        @BindView(R.id.doctorFee)
        CustomTextView doctorFee;
        @BindView(R.id.imageURL)
        CircularImageView imageURL;
        @BindView(R.id.thumbnail)
        LinearLayout thumbnail;
        @BindView(R.id.clinicName)
        CustomTextView clinicName;
        @BindView(R.id.doctorName)
        CustomTextView doctorName;
        @BindView(R.id.aboutDoctor)
        CustomTextView aboutDoctor;
        @BindView(R.id.doctorExperience)
        CustomTextView doctorExperience;
        @BindView(R.id.doctorAddress)
        CustomTextView doctorAddress;
        @BindView(R.id.dataLayout)
        LinearLayout dataLayout;
        @BindView(R.id.favoriteView)
        ImageView favoriteView;
        @BindView(R.id.gMapLocationView)
        ImageView gMapLocationView;
        @BindView(R.id.distance)
        CustomTextView distance;
        @BindView(R.id.doctorRating)
        CustomTextView doctorRating;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        @BindView(R.id.tokenNo)
        ImageView tokenNo;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}

