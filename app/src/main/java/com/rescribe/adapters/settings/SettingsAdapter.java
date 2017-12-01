package com.rescribe.adapters.settings;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rescribe.R;
import com.rescribe.model.dashboard_api.ClickOption;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RiteshP on 29/11/17.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ListViewHolder> {

    private ArrayList<ClickOption> mClickOptionList;
    private Context mContext;
    private OnClickOofSettingItemListener mOnClickOofSettingItemListener;


    public SettingsAdapter(final Context mContext, ArrayList<ClickOption> mClickOption, OnClickOofSettingItemListener mOnClickOofSettingItemListener) {
        this.mOnClickOofSettingItemListener = mOnClickOofSettingItemListener;
        this.mContext = mContext;
        this.mClickOptionList = mClickOption;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings_menu_item_layout, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, final int position) {
        final ClickOption clickOption = mClickOptionList.get(position);
        holder.menuOptionName.setText(clickOption.getName());
        holder.selectMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickOofSettingItemListener.onClickOfSettingMenuOption(clickOption);
            }
        });

        if (clickOption.getName().toLowerCase().startsWith(mContext.getString(R.string.log).toLowerCase())) {
            holder.menuOptionName.setTextColor(ContextCompat.getColor(mContext, R.color.tagColor));
            holder.dashboardArrowIcon.setImageResource(R.drawable.logout_settings_icon);
        } else {
            holder.menuOptionName.setTextColor(ContextCompat.getColor(mContext, R.color.menu_option_on_dashboard_color));
            holder.dashboardArrowIcon.setImageResource(R.drawable.dashboard_arrow);
        }

    }

    @Override
    public int getItemCount() {
        return mClickOptionList.size();
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
        void onClickOfSettingMenuOption(ClickOption mSettingName);
    }

}