package com.rescribe.adapters.dashboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.BuildConfig;
import com.rescribe.R;
import com.rescribe.model.dashboard_api.DashboardMenuList;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rescribe.util.RescribeConstants.DRAWABLE;

/**
 * Created by jeetal on 28/9/17.
 */

@SuppressWarnings("CheckResult")
public class MenuOptionsDashBoardAdapter extends RecyclerView.Adapter<MenuOptionsDashBoardAdapter.ListViewHolder> {

    private static final String TAG = "MenuOption";
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

        if (mDashboardMenuList.get(position).getName().equals(mContext.getString(R.string.health_offers)))
            holder.healthoffersTag.setVisibility(View.VISIBLE);
        else holder.healthoffersTag.setVisibility(View.GONE);

        int resourceId = mContext.getResources().getIdentifier(dashboardMenuList.getIconImageUrl(), DRAWABLE, BuildConfig.APPLICATION_ID);
        if (resourceId > 0) {
            holder.menuIcon.setImageResource(resourceId);
        } else {
            // Do something is the resource does not exist
            CommonMethods.Log(TAG, "Resource not found");
        }
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
        @BindView(R.id.healthoffersTag)
        CustomTextView healthoffersTag;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface onMenuListClickListener {
        void onClickOfMenu(DashboardMenuList menu);
    }
}
