package com.rescribe.adapters.health_repository;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rescribe.R;
import com.rescribe.ui.customesViews.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 27/11/17.
 */

public class HealthRepositoryAdapter extends RecyclerView.Adapter<HealthRepositoryAdapter.ListViewHolder> {

    private Context mContext;
    String[] mFindDoctorOptions = {
            "My Records",
            "Vital Graphs",
            "Doctor Visits",
            "Saved Articles"
    };
    Integer[] mSetIcons = {R.drawable.dashboard_my_records_icon,
            R.drawable.dashboard_my_records_icon, R.drawable.dashboard_my_records_icon, R.drawable.dashboard_my_records_icon};

    public HealthRepositoryAdapter(Context mContext) {
        this.mContext = mContext;

    }

    @Override
    public HealthRepositoryAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.services_item_layout, parent, false);

        return new HealthRepositoryAdapter.ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HealthRepositoryAdapter.ListViewHolder holder, int position) {

        holder.serviceNameTextView.setText(mFindDoctorOptions[position]);
        holder.recyclerViewClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // onServicesClickListener.setOnClickOfServices(servicesList);
            }
        });
        holder.serviceIcon.setImageResource(mSetIcons[position]);
    }

    @Override
    public int getItemCount() {
        return mFindDoctorOptions.length;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.serviceNameTextView)
        CustomTextView serviceNameTextView;
        @BindView(R.id.serviceIcon)
        ImageView serviceIcon;
        @BindView(R.id.recyclerViewClick)
        LinearLayout recyclerViewClick;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
