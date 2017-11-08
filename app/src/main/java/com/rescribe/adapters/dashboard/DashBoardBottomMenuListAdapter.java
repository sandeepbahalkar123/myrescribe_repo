package com.rescribe.adapters.dashboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.model.dashboard_api.DashboardBottomMenuList;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;

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
    private int mImageSize;


    public DashBoardBottomMenuListAdapter(Context mContext, onBottomMenuListClickListener mBottomMenuListClickListener, ArrayList<DashboardBottomMenuList> mDashboardMenuList) {
        this.mContext = mContext;
        setColumnNumber(mContext, 2);
        this.mBottomMenuListClickListener = mBottomMenuListClickListener;
        this.mDashboardMenuList = mDashboardMenuList;


    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        mImageSize = (widthPixels / columnNum) - CommonMethods.convertDpToPixel(30);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_bottom_menu_list_item, parent, false);

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
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
          if(dashBoardBottomMenuList.getName().equals(mContext.getString(R.string.alerts))){
              holder.bottomMenuTab.setVisibility(View.VISIBLE);
          }else {
              holder.bottomMenuTab.setVisibility(View.INVISIBLE);
          }
         /* if(dashBoardBottomMenuList.getName().equals(mContext.getString(R.string.app_logo))){
              holder.bottomMenuName.setVisibility(View.INVISIBLE);
          }else{
              holder.bottomMenuName.setVisibility(View.VISIBLE);

          }*/
        Glide.with(mContext)
                .load(mDashboardMenuList.get(position).getImageUrl())
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
