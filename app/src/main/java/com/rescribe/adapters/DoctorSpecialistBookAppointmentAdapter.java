package com.rescribe.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rescribe.R;
import com.rescribe.model.book_appointment.doctor_data.DoctorSpeciality;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 19/9/17.
 */

public class DoctorSpecialistBookAppointmentAdapter extends RecyclerView.Adapter<DoctorSpecialistBookAppointmentAdapter.ListViewHolder> {

    private Context mContext;
    private List<DoctorSpeciality> doctorConnectSearchModels;
    private DoctorSpecialistBookAppointmentAdapter.OnSpecialityClickListener mOnDoctorSpecialityClickListener;

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.specialityImage)
        ImageView specialityImage;
        @BindView(R.id.specialityName)
        CustomTextView specialityName;
        @BindView(R.id.specialityOfDoctor)
        LinearLayout specialityOfDoctor;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public DoctorSpecialistBookAppointmentAdapter(Context mContext, DoctorSpecialistBookAppointmentAdapter.OnSpecialityClickListener mOnDoctorSpecialityClickListener, ArrayList<DoctorSpeciality> searchDataModels) {
        this.doctorConnectSearchModels = searchDataModels;
        this.mContext = mContext;
        this.mOnDoctorSpecialityClickListener = mOnDoctorSpecialityClickListener;
    }

    @Override
    public DoctorSpecialistBookAppointmentAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_connect_grid_search_appointment, parent, false);

        return new DoctorSpecialistBookAppointmentAdapter.ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DoctorSpecialistBookAppointmentAdapter.ListViewHolder holder, int position) {

        final DoctorSpeciality doctorConnectSearchModel = doctorConnectSearchModels.get(position);

        holder.specialityName.setText(doctorConnectSearchModel.getSpeciality());
        holder.specialityImage.setImageResource(CommonMethods.getDoctorSpecialistIcons(doctorConnectSearchModel.getSpeciality(), mContext));
        holder.specialityOfDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(RescribeConstants.ITEM_DATA, doctorConnectSearchModel.getSpeciality());
                b.putString(RescribeConstants.ITEM_DATA_VALUE, mContext.getString(R.string.doctors_speciality));
                mOnDoctorSpecialityClickListener.setOnClickOfDoctorSpeciality(b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorConnectSearchModels.size();
    }

    public interface OnSpecialityClickListener {
        void setOnClickOfDoctorSpeciality(Bundle bundleData);
    }


}
