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
 * Created by jeetal on 16/11/17.
 */

public class RecentPlacesAdapter extends RecyclerView.Adapter<RecentPlacesAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<String> mDataList;

    public RecentPlacesAdapter(Context mContext, ArrayList<String> dataList) {
        this.mDataList = dataList;
        this.mContext = mContext;

    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_popular_places_item, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        holder.recentPlaceName.setText(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recentPlaceName)
        CustomTextView recentPlaceName;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}