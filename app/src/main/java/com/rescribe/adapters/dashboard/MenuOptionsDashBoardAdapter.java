package com.rescribe.adapters.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.book_appointment.BookAppointFilteredDocList;
import com.rescribe.model.dashboard.HealthOffersData;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 28/9/17.
 */

public class MenuOptionsDashBoardAdapter extends RecyclerView.Adapter<MenuOptionsDashBoardAdapter.ListViewHolder> {
    private onMenuListClickListener mMenuListClickListener;
    private Context mContext;
    String[] menuOptions = new String[]{"Find Doctors",
            "On Going Treatment",
            "Health Repository",
            "Health Offers",
            "Health Education"
    };
    Integer[] mImageMenuICons = {
            R.drawable.dashboard_find_doctor,
            R.drawable.dashboard_on_going_treatment,
            R.drawable.dashboard_health_repository,
            R.drawable.dashboard_health_offers,
            R.drawable.dashboard_health_education

    };

    public MenuOptionsDashBoardAdapter(Context mContext, onMenuListClickListener mMenuListClickListener) {
        this.mContext = mContext;
        this.mMenuListClickListener = mMenuListClickListener;


    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_options_item_layout, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, final int position) {
         holder.menuOptionName.setText(menuOptions[position]);
        holder.menuIcon.setImageResource(mImageMenuICons[position]);
        holder.selectMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mMenuListClickListener.onClickOfMenu(menuOptions[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuOptions.length;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.menuIcon)
        ImageView menuIcon;
        @BindView(R.id.menuOptionName)
        CustomTextView menuOptionName;
        @BindView(R.id.dashboardArrowIcon)
        ImageView dashboardArrowIcon;
        @BindView(R.id.selectMenuLayout)
        RelativeLayout selectMenuLayout;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public interface onMenuListClickListener {
        void onClickOfMenu(String menuName);
    }
}
