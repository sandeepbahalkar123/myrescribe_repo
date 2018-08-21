package com.rescribe.adapters.book_appointment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rescribe.R;
import com.rescribe.model.book_appointment.search_doctors.RecentlyVisitedAreaList;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 16/11/17.
 */

public class RecentPlacesAdapter extends RecyclerView.Adapter<RecentPlacesAdapter.ListViewHolder> {

    private OnRecentPlacesListener mOnRecentPlacesListener;
    private ArrayList<RecentlyVisitedAreaList> mDataList;

    public RecentPlacesAdapter(ArrayList<RecentlyVisitedAreaList> dataList,OnRecentPlacesListener mOnRecentPlacesListener) {
        this.mDataList = dataList;
        this.mOnRecentPlacesListener = mOnRecentPlacesListener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_popular_places_item, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, final int position) {

        holder.recentPlaceName.setText(mDataList.get(position).getAddress());
        holder.recentPlaceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRecentPlacesListener.onClickOfRecentPlaces(mDataList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recentPlaceName)
        CustomTextView recentPlaceName;
        @BindView(R.id.recentPlaceLayout)
        LinearLayout recentPlaceLayout;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
    public interface OnRecentPlacesListener {
        void onClickOfRecentPlaces(RecentlyVisitedAreaList location);
    }
}