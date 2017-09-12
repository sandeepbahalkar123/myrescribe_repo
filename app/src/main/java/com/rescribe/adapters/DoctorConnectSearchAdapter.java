package com.rescribe.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rescribe.R;
import com.rescribe.model.doctor_connect_search.DoctorSpeciality;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 6/9/17.
 */

public class DoctorConnectSearchAdapter extends RecyclerView.Adapter<DoctorConnectSearchAdapter.ListViewHolder> {

    private Context mContext;
    private List<DoctorSpeciality> doctorConnectSearchModels;
    private OnDoctorSpecialityClickListener mOnDoctorSpecialityClickListener;


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

    public DoctorConnectSearchAdapter(Context mContext, OnDoctorSpecialityClickListener mOnDoctorSpecialityClickListener, ArrayList<DoctorSpeciality> searchDataModels) {
        this.doctorConnectSearchModels = searchDataModels;
        this.mContext = mContext;
        this.mOnDoctorSpecialityClickListener = mOnDoctorSpecialityClickListener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_connect_grid_search, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        DoctorSpeciality doctorConnectSearchModel = doctorConnectSearchModels.get(position);
        holder.specialityName.setText(doctorConnectSearchModel.getSpeciality());
        holder.specialityImage.setImageResource(CommonMethods.getDoctorSpecialistIcons(doctorConnectSearchModel.getSpeciality(), mContext));
        holder.specialityOfDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDoctorSpecialityClickListener.setOnClickOfDoctorSpeciality();

            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorConnectSearchModels.size();
    }

    public interface OnDoctorSpecialityClickListener {
        void setOnClickOfDoctorSpeciality();
    }


}
