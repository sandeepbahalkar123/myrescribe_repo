package com.rescribe.adapters.book_appointment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rescribe.R;
import com.rescribe.model.book_appointment.ServicesList;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 15/9/17.
 */

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<ServicesList> servicesLists;
    OnServicesClickListener onServicesClickListener;

    public ServicesAdapter(Context mContext, ArrayList<ServicesList> ServicesList, OnServicesClickListener onServicesClickListener) {
        this.onServicesClickListener = onServicesClickListener;
        this.servicesLists = ServicesList;
        this.mContext = mContext;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_services_list, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        final ServicesList servicesList = servicesLists.get(position);
        holder.serviceNameTextView.setText(servicesList.getServiceName());
        holder.recyclerViewClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onServicesClickListener.setOnClickOfServices(servicesList);
            }
        });
        holder.serviceIcon.setImageResource(CommonMethods.getServices(servicesList.getServiceName(), mContext));

    }

    @Override
    public int getItemCount() {
        return servicesLists.size();
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

    public interface OnServicesClickListener {
        void setOnClickOfServices(ServicesList servicesObject);
    }

}
