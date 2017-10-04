package com.rescribe.adapters.book_appointment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rescribe.R;
import com.rescribe.model.book_appointment.reviews.Review;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 28/9/17.
 */

public class ShowReviewsAdapter extends RecyclerView.Adapter<ShowReviewsAdapter.ListViewHolder> {

    private Fragment mFragment;
    private Context mContext;
    private ArrayList<Review> mDataList;

    public ShowReviewsAdapter(Context mContext, ArrayList<Review> dataList) {
        this.mDataList = dataList;
        this.mContext = mContext;


    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor_reviews_layout, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        final Review doctorObject = mDataList.get(position);


        holder.reviewName.setText(doctorObject.getRevierName());
        holder.review.setText(doctorObject.getReviewCommment());

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reviewName)
        CustomTextView reviewName;
        @BindView(R.id.review)
        CustomTextView review;

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
