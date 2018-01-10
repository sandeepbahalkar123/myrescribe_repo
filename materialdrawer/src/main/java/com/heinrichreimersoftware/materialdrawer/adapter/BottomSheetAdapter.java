package com.heinrichreimersoftware.materialdrawer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.heinrichreimersoftware.materialdrawer.R;
import com.heinrichreimersoftware.materialdrawer.app_logo.BottomSheetMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuActivity;

import java.util.ArrayList;


/**
 * Created by jeetal on 10/1/18.
 */

public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<BottomSheetMenu> mDataList;

    public BottomSheetAdapter(Context mContext, ArrayList<BottomSheetMenu> dataList) {
        this.mDataList = dataList;
        this.mContext = mContext;
    }

    @Override
    public BottomSheetAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bottom_sheet_menu_item_list, parent, false);

        return new BottomSheetAdapter.ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BottomSheetAdapter.ListViewHolder holder, int position) {

        final BottomSheetMenu doctorObject = mDataList.get(position);
       /* holder.reviewName.setText(doctorObject.getRevierName());
        if(doctorObject.getReviewCommment().equals("")){
            holder.review.setVisibility(View.GONE);
        }else {
            holder.review.setText(doctorObject.getReviewCommment());
            holder.review.setVisibility(View.VISIBLE);
        }
        holder.reviewDate.setText(CommonMethods.getFormattedDate(doctorObject.getReviewDate(), RescribeConstants.DATE_PATTERN.UTC_PATTERN,RescribeConstants.DATE_PATTERN.DD_MM_YYYY));*/
        holder.bottomMenuName.setText(doctorObject.getName());

        if (doctorObject.getName().equalsIgnoreCase(mContext.getResources().getString(R.string.notifications))) {

            if (doctorObject.getNotificationCount() > 0) {
                holder.badgeView.setVisibility(View.VISIBLE);
                holder.badgeView.setText(String.valueOf(doctorObject.getNotificationCount()));
            } else holder.badgeView.setVisibility(View.GONE);
        } else holder.badgeView.setVisibility(View.GONE);

        holder.bottomSheetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // BottomMenuActivity.onBottomSheetMenuClick(doctorObject);
            }
        });

        holder.menuBottomIcon.setImageDrawable(doctorObject.getIconImageUrl());

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
        ImageView menuBottomIcon;
        TextView bottomMenuName;
        TextView badgeView;
        LinearLayout bottomSheetLayout;

        ListViewHolder(View view) {
            super(view);
            bottomMenuName = (TextView) view.findViewById(R.id.menuName);
            menuBottomIcon = (ImageView) view.findViewById(R.id.menuImage);
            badgeView = (TextView) view.findViewById(R.id.showCount);
            bottomSheetLayout = (LinearLayout) view.findViewById(R.id.bottomSheetLayout);
        }
    }
}

