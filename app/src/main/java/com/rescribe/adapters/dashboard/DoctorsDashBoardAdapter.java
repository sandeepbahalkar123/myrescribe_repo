package com.rescribe.adapters.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rescribe.R;
import com.rescribe.model.dashboard.DoctorData;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 28/9/17.
 */

public class DoctorsDashBoardAdapter extends RecyclerView.Adapter<DoctorsDashBoardAdapter.ListViewHolder> {


    private Fragment mFragment;
    private Context mContext;
    private ArrayList<DoctorData> mDataList;

    public DoctorsDashBoardAdapter(Context mContext, ArrayList<DoctorData> dataList) {
        this.mDataList = dataList;
        this.mContext = mContext;


    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_list_item_dashboard, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        final DoctorData doctorObject = mDataList.get(position);
        holder.doctorName.setText(doctorObject.getDocName());
        holder.doctorType.setText(doctorObject.getDegree());
        holder.doctorExperience.setText(doctorObject.getExperience() + mContext.getString(R.string.space) + mContext.getString(R.string.years_experience));
        holder.doctorAddress.setText(doctorObject.getDoctorAddress());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageURL)
        CircularImageView imageURL;
        @BindView(R.id.thumbnail)
        LinearLayout thumbnail;
        @BindView(R.id.doctorName)
        CustomTextView doctorName;
        @BindView(R.id.doctorType)
        CustomTextView doctorType;
        @BindView(R.id.doctorExperience)
        CustomTextView doctorExperience;
        @BindView(R.id.doctorAddress)
        CustomTextView doctorAddress;
        @BindView(R.id.recentVisit)
        CustomTextView recentVisit;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public interface OnFilterDocListClickListener {
        void onClickOfDoctorRowItem(Bundle bundleData);
    }
}