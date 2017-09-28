package com.rescribe.adapters.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rescribe.R;
import com.rescribe.model.book_appointment.doctor_data.ReviewList;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 28/9/17.
 */

public class MenuDashBoardAdapter extends RecyclerView.Adapter<MenuDashBoardAdapter.ListViewHolder> {

    private Fragment mFragment;
    private Context mContext;
    private ArrayList<ReviewList> mDataList;

    public MenuDashBoardAdapter(Context mContext, ArrayList<ReviewList> dataList) {
        this.mDataList = dataList;
        this.mContext = mContext;


    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item_row_dashboard_layout, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        final ReviewList doctorObject = mDataList.get(position);


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.menuImage)
        ImageView menuImage;
        @BindView(R.id.menuName)
        CustomTextView menuName;
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
