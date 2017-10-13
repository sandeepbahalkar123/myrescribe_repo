package com.rescribe.adapters.dashboard;

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

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.model.dashboard.DoctorData;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 28/9/17.
 */

public class DoctorsDashBoardAdapter extends RecyclerView.Adapter<DoctorsDashBoardAdapter.ListViewHolder> {

    @BindView(R.id.doctorAddress)
    CustomTextView doctorAddress;
    @BindView(R.id.recentVisit)
    CustomTextView recentVisit;
    private Context mContext;
    private int mImageSize;
    private ColorGenerator mColorGenerator;
    private ArrayList<DoctorData> mDataList;

    public DoctorsDashBoardAdapter(Context mContext, ArrayList<DoctorData> dataList) {
        this.mDataList = dataList;
        this.mContext = mContext;
        mColorGenerator = ColorGenerator.MATERIAL;
        setColumnNumber(mContext, 2);
    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        mImageSize = (widthPixels / columnNum) - CommonMethods.convertDpToPixel(30);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_list_item_dashboard, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        final DoctorData doctorObject = mDataList.get(position);
        if (doctorObject.getDoctorImageUrl().equals("")) {
            String doctorName = doctorObject.getDocName();
            if (doctorName.contains("Dr. ")) {
                doctorName = doctorName.replace("Dr. ", "");
            }

            if (doctorName != null) {
                int color2 = mColorGenerator.getColor(doctorName);
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .width(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // width in px
                        .height(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // height in px
                        .endConfig()
                        .buildRound(("" + doctorName.charAt(0)).toUpperCase(), color2);
                holder.imageURL.setImageDrawable(drawable);
            }
        } else {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.dontAnimate();
            requestOptions.override(mImageSize, mImageSize);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.skipMemoryCache(true);
            requestOptions.placeholder(R.drawable.layer_12);

            Glide.with(mContext)
                    .load(doctorObject.getDoctorImageUrl())
                    .apply(requestOptions).thumbnail(0.5f)
                    .into(holder.imageURL);
        }
        holder.doctorName.setText(doctorObject.getDocName());
        holder.doctorType.setText(doctorObject.getDegree());
        holder.doctorExperience.setText(doctorObject.getExperience() + mContext.getString(R.string.space) + mContext.getString(R.string.years_experience));
        holder.doctorAddress.setText(doctorObject.getDoctorAddress());
        holder.doctorCategory.setText(doctorObject.getCategoryName());
        holder.feesToPaid.setText(doctorObject.getAmount());
        if (doctorObject.getRecentlyVisited()) {
            holder.recentVisit.setVisibility(View.VISIBLE);
        } else {
            holder.recentVisit.setVisibility(View.GONE);
        }
        if (doctorObject.getFavourite()) {
            holder.favorite.setVisibility(View.VISIBLE);
        } else {
            holder.favorite.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.favorite)
        ImageView favorite;
        @BindView(R.id.imageURL)
        CircularImageView imageURL;
        @BindView(R.id.thumbnail)
        LinearLayout thumbnail;
        @BindView(R.id.doctorName)
        CustomTextView doctorName;
        @BindView(R.id.doctorType)
        CustomTextView doctorType;
        @BindView(R.id.doctorExperience)
        CustomTextView doctorExperience;
        @BindView(R.id.doctorAddress)
        CustomTextView doctorAddress;
        @BindView(R.id.recentVisit)
        CustomTextView recentVisit;
        @BindView(R.id.doctorCategoryVisit)
        CustomTextView doctorCategory;
        @BindView(R.id.feesToPaidVisit)
        CustomTextView feesToPaid;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}