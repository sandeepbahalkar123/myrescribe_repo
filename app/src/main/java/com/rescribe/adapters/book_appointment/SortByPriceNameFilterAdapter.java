package com.rescribe.adapters.book_appointment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.rescribe.R;
import com.rescribe.model.book_appointment.reviews.Review;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 27/10/17.
 */

public class SortByPriceNameFilterAdapter extends RecyclerView.Adapter<SortByPriceNameFilterAdapter.ListViewHolder> {
    String[] sortOptions = new String[]{"Star Ratings (low to high)",
            "Star Ratings (high to low)",
            "Doctor Fees (low to high)",
            "Doctor Fees (high to low)"
    };
    private Context mContext;

    public SortByPriceNameFilterAdapter(Context mContext) {
        this.mContext = mContext;

    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sort_item_row_layout, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        holder.sortName.setText(sortOptions[position]);
        holder.recyclerViewClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.serviceIcon.setVisibility(View.VISIBLE);
            }
        });

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
}