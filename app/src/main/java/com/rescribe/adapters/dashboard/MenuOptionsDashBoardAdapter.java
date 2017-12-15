package com.rescribe.adapters.dashboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.model.dashboard_api.DashboardMenuList;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 28/9/17.
 */

@SuppressWarnings("CheckResult")
public class MenuOptionsDashBoardAdapter extends RecyclerView.Adapter<MenuOptionsDashBoardAdapter.ListViewHolder> {
    private onMenuListClickListener mMenuListClickListener;
    private ArrayList<DashboardMenuList> mDashboardMenuList;
    private Context mContext;

    public MenuOptionsDashBoardAdapter(Context mContext, onMenuListClickListener mMenuListClickListener, ArrayList<DashboardMenuList> mDashboardMenuList) {
        this.mContext = mContext;
        this.mMenuListClickListener = mMenuListClickListener;
        this.mDashboardMenuList = mDashboardMenuList;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_options_item_layout, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, final int position) {
        final DashboardMenuList dashboardMenuList = mDashboardMenuList.get(position);
        holder.menuOptionName.setText(dashboardMenuList.getName());
        holder.selectMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuListClickListener.onClickOfMenu(dashboardMenuList);
            }
        });
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);

        Glide.with(mContext)
                .load(mDashboardMenuList.get(position).getIconImageUrl())
                .into(holder.menuIcon);
    }

    @Override
    public int getItemCount() {
        return mDashboardMenuList.size();
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
        void onClickOfMenu(DashboardMenuList menu);
    }
}
