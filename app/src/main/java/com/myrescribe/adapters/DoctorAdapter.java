package com.myrescribe.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.doctors.DoctorDetail;
import com.myrescribe.model.prescription_response_model.PrescriptionD;
import com.myrescribe.ui.customesViews.CustomTextView;
import com.myrescribe.util.CommonMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 11/7/17.
 */

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ListViewHolder> {

    private final String TAG = getClass().getName();
    private int mParentDataContainerBackground;
    Context mContext;
    int layoutResourceId;
    int colorId = 0;
    ArrayList<DoctorDetail> mDataList;


    public DoctorAdapter(Context context, ArrayList<DoctorDetail> dataList) {

        this.mContext = context;
        mDataList = dataList;

    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        CustomTextView date;

        @BindView(R.id.circularBulletChildElement)
        ImageView circularBulletChildElement;
        @BindView(R.id.circularBulletMainElement)
        ImageView circularBulletMainElement;

        @BindView(R.id.upperLine)
        TextView upperLine;
        @BindView(R.id.lowerLine)
        TextView lowerLine;

        @BindView(R.id.doctorName)
        CustomTextView doctorName;
        @BindView(R.id.doctorListLinearLayout)
        LinearLayout doctorListLinearLayout;

        View view;


        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }


    @Override
    public DoctorAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_doctor_list_layout, parent, false);
        return new DoctorAdapter.ListViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final DoctorAdapter.ListViewHolder holder, final int position) {
        DoctorDetail dataObject = mDataList.get(position);
        holder.doctorName.setText(dataObject.getName());

        if (dataObject.getIsStartElement()) {
            holder.date.setText(dataObject.getRespectiveDate());
            holder.date.setVisibility(View.VISIBLE);
            holder.circularBulletMainElement.setVisibility(View.VISIBLE);
            holder.circularBulletChildElement.setVisibility(View.GONE);
           /* if(colorId==dataObject.getColor()) {
                holder.doctorListLinearLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey));
                dataObject.setColor(ContextCompat.getColor(mContext,R.color.grey));
                colorId = dataObject.getColor();
                notifyDataSetChanged();
            }else{
                holder.doctorListLinearLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                dataObject.setColor(ContextCompat.getColor(mContext,R.color.white));
                colorId = dataObject.getColor();
                notifyDataSetChanged();
            }*/

        } else {
            holder.date.setVisibility(View.INVISIBLE);
            holder.circularBulletChildElement.setVisibility(View.VISIBLE);
            holder.circularBulletMainElement.setVisibility(View.GONE);
            holder.doctorListLinearLayout.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));

        }

        if (position == 0)
            holder.upperLine.setVisibility(View.INVISIBLE);
        else {
            holder.upperLine.setVisibility(View.VISIBLE);
        }

        //---
        if (position == mDataList.size() - 1)
            holder.lowerLine.setVisibility(View.INVISIBLE);
        else {
            holder.lowerLine.setVisibility(View.VISIBLE);
        }

    }



    @Override
    public int getItemCount() {
        return mDataList.size();
    }


}

