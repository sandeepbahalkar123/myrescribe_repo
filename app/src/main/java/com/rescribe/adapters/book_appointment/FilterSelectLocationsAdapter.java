package com.rescribe.adapters.book_appointment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.rescribe.R;

import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 28/9/17.
 */

public class FilterSelectLocationsAdapter extends RecyclerView.Adapter<FilterSelectLocationsAdapter.ListViewHolder> {

    private Fragment mFragment;
    private Context mContext;
    private ArrayList<String> mDataList;
    // private HashSet<String> mSelectedLocation = new HashSet<>();
    private SparseBooleanArray mSelectedLocation = new SparseBooleanArray();

    public FilterSelectLocationsAdapter(Context mContext, ArrayList<String> dataList) {
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
    public void onBindViewHolder(ListViewHolder holder, final int position) {

        holder.locationName.setText(mDataList.get(position));

        holder.locationName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mSelectedLocation.put(position, isChecked);

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

    public interface OnFilterDocListClickListener {
        void onClickOfDoctorRowItem(Bundle bundleData);
    }

    public HashSet<String> getSelectedLocation() {
        HashSet<String> temp = new HashSet<>();
        for (int i = 0; i < mSelectedLocation.size(); i++) {
            int i1 = mSelectedLocation.keyAt(i);
            boolean b = mSelectedLocation.get(i1);
            if (b)
                temp.add(mDataList.get(i1));
        }
        return temp;
    }
}
