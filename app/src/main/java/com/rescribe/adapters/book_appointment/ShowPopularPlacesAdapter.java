package com.rescribe.adapters.book_appointment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rescribe.R;
import com.rescribe.model.book_appointment.search_doctors.AreaList;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 16/11/17.
 */

public class ShowPopularPlacesAdapter extends RecyclerView.Adapter<ShowPopularPlacesAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<AreaList> mDataList;
    private OnPopularPlacesListener mOnPopularPlacesListener;

    public ShowPopularPlacesAdapter(Context mContext, ArrayList<AreaList> dataList, OnPopularPlacesListener mOnPopularPlacesListener) {
        this.mDataList = dataList;
        this.mContext = mContext;
        this.mOnPopularPlacesListener = mOnPopularPlacesListener;

    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.popular_locations_item, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        final AreaList doctorObject = mDataList.get(position);
        holder.popularPlaceName.setText(doctorObject.getArea());
        if(doctorObject.getDoctorCount()==1) {
            holder.countOfDoctors.setText("" + doctorObject.getDoctorCount() + " " + mContext.getString(R.string.doctor));
        }else{
            holder.countOfDoctors.setText("" + doctorObject.getDoctorCount() + " " + mContext.getString(R.string.doctors));

        }
        holder.popularLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnPopularPlacesListener.onClickOfPopularPlaces(doctorObject.getArea()+","+doctorObject.getCity());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.popularPlaceName)
        CustomTextView popularPlaceName;
        @BindView(R.id.countOfDoctors)
        CustomTextView countOfDoctors;
        @BindView(R.id.popularLayout)
        LinearLayout popularLayout;
        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }


    public interface OnPopularPlacesListener {
        void onClickOfPopularPlaces(String location);
    }
}
