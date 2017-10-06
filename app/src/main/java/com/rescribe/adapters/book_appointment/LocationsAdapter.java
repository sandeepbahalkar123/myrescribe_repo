package com.rescribe.adapters.book_appointment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.rescribe.R;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<String> mDataList;
    private HashSet<String> mSelectedLocation = new HashSet<>();

    public LocationsAdapter(Context mContext, ArrayList<String> dataList) {
        this.mDataList = dataList;
        this.mContext = mContext;

    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        String doctorObject = mDataList.get(position);
        holder.locationName.setText(doctorObject);
        holder.locationName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSelectedLocation.add(buttonView.getText().toString());
                } else {
                    mSelectedLocation.remove(buttonView.getText().toString());
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.locationName)
        CheckBox locationName;
        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public HashSet<String> getSelectedLocation() {
        return mSelectedLocation;
    }
}