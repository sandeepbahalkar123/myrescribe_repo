package com.rescribe.adapters.dashboard;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.model.dashboard_api.DashboardBottomMenuList;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 6/11/17.
 */

public class DashBoardBottomMenuListAdapter extends RecyclerView.Adapter<DashBoardBottomMenuListAdapter.ListViewHolder> {

    private onBottomMenuListClickListener mBottomMenuListClickListener;
    private ArrayList<DashboardBottomMenuList> mDashboardMenuList;
    private Context mContext;
    private String selectedMenu;


    public DashBoardBottomMenuListAdapter(Context mContext, onBottomMenuListClickListener mBottomMenuListClickListener, ArrayList<DashboardBottomMenuList> mDashboardMenuList, String selectedMenu) {
        this.mContext = mContext;
        this.mBottomMenuListClickListener = mBottomMenuListClickListener;
        this.mDashboardMenuList = mDashboardMenuList;
        this.selectedMenu = selectedMenu;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_bottom_menu_list_item, parent, false);

        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        int width = (widthPixels * 20) / 100;

        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.width = width;

        itemView.setLayoutParams(layoutParams);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, final int position) {
        final DashboardBottomMenuList dashBoardBottomMenuList = mDashboardMenuList.get(position);
        holder.bottomMenuName.setText(dashBoardBottomMenuList.getName());
        holder.menuBottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomMenuListClickListener.onBottomClickOfMenu(dashBoardBottomMenuList.getName());
            }
        });

        if (dashBoardBottomMenuList.getName().equals(mContext.getString(R.string.app_logo))) {
            holder.bottomMenuName.setVisibility(View.GONE);
            holder.bottomMenuTab.setVisibility(View.INVISIBLE);
        } else {
            holder.bottomMenuName.setVisibility(View.VISIBLE);
            holder.bottomMenuTab.setVisibility(View.VISIBLE);
        }

        if (dashBoardBottomMenuList.getName().equals(selectedMenu)) {
            holder.bottomMenuTab.setVisibility(View.VISIBLE);
        } else {
            holder.bottomMenuTab.setVisibility(View.INVISIBLE);
        }

        RequestOptions options = new RequestOptions()
                .centerInside()
                .priority(Priority.HIGH);

        Glide.with(mContext)
                .load(mDashboardMenuList.get(position).getImageUrl()).apply(options)
                .into(holder.menuBottomIcon);
    }

    @Override
    public int getItemCount() {
        return mDashboardMenuList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.menuBottomIcon)
        ImageView menuBottomIcon;
        @BindView(R.id.bottomMenuName)
        CustomTextView bottomMenuName;
        @BindView(R.id.bottomMenuTab)
        ImageView bottomMenuTab;
        @BindView(R.id.menuBottomLayout)
        LinearLayout menuBottomLayout;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public interface onBottomMenuListClickListener {
        void onBottomClickOfMenu(String menuName);
    }
}
