package com.rescribe.adapters.book_appointment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by jeetal on 28/9/17.
 */

public class ShowReviewsAdapter extends RecyclerView.Adapter<ShowReviewsAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<Review> mDataList;

    public ShowReviewsAdapter(Context mContext, ArrayList<Review> dataList) {
        this.mDataList = dataList;
        this.mContext = mContext;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.services_item_layout, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        final Review doctorObject = mDataList.get(position);
       /* holder.reviewName.setText(doctorObject.getRevierName());
        if(doctorObject.getReviewCommment().equals("")){
            holder.review.setVisibility(View.GONE);
        }else {
            holder.review.setText(doctorObject.getReviewCommment());
            holder.review.setVisibility(View.VISIBLE);
        }
        holder.reviewDate.setText(CommonMethods.getFormattedDate(doctorObject.getReviewDate(), RescribeConstants.DATE_PATTERN.UTC_PATTERN,RescribeConstants.DATE_PATTERN.DD_MM_YYYY));*/
        if(!doctorObject.getRating().equals("NA")) {
            holder.ratingBar.setRating(Float.parseFloat(doctorObject.getRating()));
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
     /*   @BindView(R.id.reviewName)
        CustomTextView reviewName;
        @BindView(R.id.review)
        CustomTextView review;
        @BindView(R.id.reviewDate)
        CustomTextView reviewDate;*/
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
