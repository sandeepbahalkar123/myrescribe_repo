package com.rescribe.adapters.book_appointment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rescribe.R;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.RescribeConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 27/10/17.
 */

public class SortByPriceNameFilterAdapter extends RecyclerView.Adapter<SortByPriceNameFilterAdapter.ListViewHolder> {

    private String lowToHigh = "(low to high)";
    private String highToLow = "(high to low)";
    private String ratings = " Ratings ";
    private String fees = " Fees ";
    private String asc = "asc";
    private String desc = "desc";
    private String selectedSortedOptionLabel = "";//

    private String[] sortOptions = new String[]{"Star" + ratings + lowToHigh,
            "Star" + ratings + highToLow,
            "Doctor" + fees + lowToHigh,
            "Doctor" + fees + highToLow
    };

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sort_item_row_layout, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        String sortOption = sortOptions[position];
        holder.sortName.setText(sortOption);

        holder.recyclerViewClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String receivedTitle = "" + v.getTag();

                if (receivedTitle.equalsIgnoreCase(selectedSortedOptionLabel)) {
                    selectedSortedOptionLabel = "";
                    holder.serviceIcon.setVisibility(View.GONE);
                } else {
                    selectedSortedOptionLabel = receivedTitle;
                    holder.serviceIcon.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();
                }

            }
        });

        if (sortOption.equalsIgnoreCase(selectedSortedOptionLabel)) {
            selectedSortedOptionLabel = sortOption;
            holder.serviceIcon.setVisibility(View.VISIBLE);
        } else {
            holder.serviceIcon.setVisibility(View.GONE);
        }

        holder.recyclerViewClick.setTag(sortOption);
    }

    @Override
    public int getItemCount() {
        return sortOptions.length;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sortName)
        CustomTextView sortName;
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

    // sortBy|sortOrder
    public String getSelectedSortedOption() {
        String temp;
        if (selectedSortedOptionLabel.toLowerCase().contains(ratings.toLowerCase())) {
            temp = ratings.trim();
        } else {
            temp = fees.trim();
        }
        //-------
        //-------

        if (selectedSortedOptionLabel.toLowerCase().endsWith(lowToHigh.toLowerCase())) {
            temp = temp + "|" + asc.trim();
        } else {
            temp = temp + "|" + desc.trim();
        }
        return temp;
    }

    public String getSelectedSortedOptionLabel() {
        return selectedSortedOptionLabel;
    }
}