package com.rescribe.adapters.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 20/11/17.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ListViewHolder> {

    private Context mContext;
    private int imageSize;
    OnClickOofSettingItemListener mOnClickOofSettingItemListener;
    String[] mSettingOptions = {
            "Notifications",
            "How it Works ?",
            "Terms of Use",
            "Privacy Policy",
            "Recommend Us",
            "Feedback"

    };

    public SettingsAdapter(final Context mContext,OnClickOofSettingItemListener mOnClickOofSettingItemListener) {
        this.mOnClickOofSettingItemListener = mOnClickOofSettingItemListener;
        this.mContext = mContext;
        setColumnNumber(mContext, 2);
    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = (widthPixels / columnNum) - CommonMethods.convertDpToPixel(30);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings_menu_item_layout, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, final int position) {
        holder.menuOptionName.setText(mSettingOptions[position]);
        holder.selectMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickOofSettingItemListener.onClickOfSettingMenuOption(mSettingOptions[position]);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mSettingOptions.length;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.menuIcon)
        ImageView menuIcon;
        @BindView(R.id.menuOptionName)
        CustomTextView menuOptionName;
        @BindView(R.id.dashboardArrowIcon)
        ImageView dashboardArrowIcon;
        @BindView(R.id.selectMenuLayout)
        LinearLayout selectMenuLayout;
        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
    public interface OnClickOofSettingItemListener {
        void onClickOfSettingMenuOption(String mSettingName);
    }

}