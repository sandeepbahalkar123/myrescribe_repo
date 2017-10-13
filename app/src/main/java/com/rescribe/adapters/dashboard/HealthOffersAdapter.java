package com.rescribe.adapters.dashboard;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import com.rescribe.R;
import com.rescribe.model.dashboard.HealthOffersData;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 28/9/17.
 */

public class HealthOffersAdapter extends RecyclerView.Adapter<HealthOffersAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<HealthOffersData> mDataList;

    public HealthOffersAdapter(Context mContext, ArrayList<HealthOffersData> dataList) {
        this.mDataList = dataList;
        this.mContext = mContext;


    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.health_offers_item_dashboad, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        final HealthOffersData doctorObject = mDataList.get(position);

        holder.fees.setText(doctorObject.getActualPrice());
        holder.fees.setPaintFlags(holder.fees.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.discountAmount.setText(doctorObject.getDiscountedPrice());
        holder.percentOff.setText(doctorObject.getDiscountPercentage() + "%");
        holder.saleImage.setImageResource(CommonMethods.getDiagnosticCentreImages(doctorObject.getDiagnosticCentreName()));
     }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.fees)
        CustomTextView fees;
        @BindView(R.id.discountAmount)
        CustomTextView discountAmount;
        @BindView(R.id.percentOff)
        CustomTextView percentOff;
        @BindView(R.id.showVitalUnitNameIconLayout)
        LinearLayout showVitalUnitNameIconLayout;
        @BindView(R.id.vitalImageDialog)
        ImageView vitalImageDialog;
        @BindView(R.id.bpMinLayout)
        LinearLayout bpMinLayout;
        @BindView(R.id.saleImageView)
        ImageView saleImage;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
