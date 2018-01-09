package com.rescribe.adapters.dashboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.rescribe.R;
import com.rescribe.model.dashboard_api.ClickOption;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.customesViews.switch_button.SwitchButton;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 28/9/17.
 */

@SuppressWarnings("CheckResult")
public class NotificationSettingListAdapter extends RecyclerView.Adapter<NotificationSettingListAdapter.ListViewHolder> {
    private OnSwitchButtonClickListener listener;
    private ArrayList<ClickOption> clickOptionList;
    private Context mContext;

    public NotificationSettingListAdapter(Context mContext, ArrayList<ClickOption> mDashboardMenuList, OnSwitchButtonClickListener listener) {
        this.mContext = mContext;
        this.clickOptionList = mDashboardMenuList;
        this.listener = listener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_setting, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, final int position) {

        final ClickOption clickOption = clickOptionList.get(position);
        holder.textName.setText(clickOption.getName());

        //------------
        if (clickOption.getIconImageUrl() != null) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.dontAnimate();

            if (clickOption.getIconImageUrl().getTime().isEmpty()) {
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                requestOptions.skipMemoryCache(true);
            } else
                requestOptions.signature(new ObjectKey(clickOption.getIconImageUrl().getTime()));

            Glide.with(mContext)
                    .load(clickOption.getIconImageUrl().getUrl())
                    .apply(requestOptions)
                    .into(holder.menuIcon);
        }
        //--------------
        holder.radioSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchButton v1 = (SwitchButton) v;
                listener.onClick(clickOption, v1.isChecked());
            }
        });

       /* holder.radioSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SwitchButton v1 = (SwitchButton) buttonView;
                listener.onClick(clickOption, v1.isChecked());
            }
        });*/
        holder.radioSwitch.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                SwitchButton v1 = (SwitchButton) v;
                listener.onClick(clickOption, v1.isChecked());
                return false;
            }
        });
        holder.radioSwitch.setAnimationDuration(250);
        holder.radioSwitch.setChecked(RescribePreferencesManager.getBoolean(clickOption.getName(), mContext));

    }

    @Override
    public int getItemCount() {
        return clickOptionList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.menuIcon)
        ImageView menuIcon;
        @BindView(R.id.textName)
        CustomTextView textName;
        @BindView(R.id.radioSwitch)
        SwitchButton radioSwitch;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public interface OnSwitchButtonClickListener {
        public void onClick(ClickOption clickOption, boolean isChecked);
    }

}
