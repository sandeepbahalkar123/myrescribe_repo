package com.rescribe.adapters.unread_notification_message_list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.notification.Medication;
import com.rescribe.ui.activities.dashboard.UnreadNotificationMessageActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

// Eclipse wanted me to use a sparse array instead of my hashmaps, I just suppressed that suggestion
@SuppressLint("UseSparseArrays")
public class UnreadMedicationNotificationAdapter extends StatelessSection {

    private final OnMedicationNotificationEventClick mListener;
    private String mTimeStamp;
    private Context mContext;
    private String title;
    private ArrayList<Medication> list;
    private boolean isExpanded;

    public UnreadMedicationNotificationAdapter(SectionParameters builder, Context mContext, String groupName, ArrayList<Medication> list, String timeId, OnMedicationNotificationEventClick listener) {

        super(builder);

        this.mContext = mContext;
        this.mListener = listener;
        String[] timeIdArray = timeId.split("\\|");

        this.mTimeStamp = timeIdArray[0];

        this.title = groupName;
        this.list = list;

        isExpanded = ((UnreadNotificationMessageActivity) mContext).isExpanded;
    }


    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;

        final Medication item = list.get(position);

        itemHolder.tabNameTextView.setText(item.getMedicineName());
        itemHolder.tabCountTextView.setText(item.getQuantity());
        itemHolder.tabImageView.setImageDrawable(CommonMethods.getMedicineTypeImage(item.getMedicineTypeName(), mContext, ContextCompat.getColor(mContext, R.color.white)));

        itemHolder.medicationCheckBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.onMedicationCheckBoxClicked(item, title, position, itemHolder.medicationCheckBox.isChecked());
            }
        });

        if (item.isTabSelected() == 1)
            itemHolder.medicationCheckBox.setChecked(true);
        else
            itemHolder.medicationCheckBox.setChecked(false);
        //---------------
        if (position == list.size() - 1) {
            itemHolder.divider.setVisibility(View.VISIBLE);
        } else {
            itemHolder.divider.setVisibility(View.GONE);
        }
        //---------------
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        SpannableString modifiedText = new SpannableString(title);
        modifiedText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.Gray)),
                0, 14,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        headerHolder.mGroupText.setText(modifiedText);

        if (!isExpanded) {
            headerHolder.medicationTimeStamp.setVisibility(View.INVISIBLE);
        } else {
            //----
            headerHolder.medicationTimeStamp.setVisibility(View.VISIBLE);
            //----
            String formattedDate = CommonMethods.getFormattedDate(mTimeStamp, RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.DD_MM_YYYY);
            String time = CommonMethods.formatDateTime(mTimeStamp, RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.DATE_PATTERN.DD_MM_YYYY + " " + RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.TIME);
            String dayFromDate = CommonMethods.getDayFromDate(RescribeConstants.DATE_PATTERN.DD_MM_YYYY, formattedDate);
            if (mContext.getString(R.string.today).equalsIgnoreCase(dayFromDate)) {
                headerHolder.medicationTimeStamp.setText(time);
            } else {
                headerHolder.medicationTimeStamp.setText(dayFromDate + " " + time);
            }
            //----
        }
    }

    @Override
    public RecyclerView.ViewHolder getFooterViewHolder(View view) {
        return new FooterViewHolder(view);
    }

    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder) {
        FooterViewHolder footerHolder = (FooterViewHolder) holder;

        footerHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMedicationLoadMoreFooterClicked();
            }
        });
        footerHolder.loadMoreMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMedicationLoadMoreFooterClicked();
            }
        });

    }


    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView mGroupText;
        private final TextView medicationTimeStamp;

        HeaderViewHolder(View view) {
            super(view);
            mGroupText = (TextView) view.findViewById(R.id.tabNameTextView);
            medicationTimeStamp = (TextView) view.findViewById(R.id.medicationTimeStamp);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        private final View rootView;
        CustomTextView loadMoreMedication;
        CustomTextView loadLessMedication;

        FooterViewHolder(View view) {
            super(view);
            rootView = view;
            loadMoreMedication = (CustomTextView) view
                    .findViewById(R.id.loadMoreMedication);
            loadLessMedication = (CustomTextView) view
                    .findViewById(R.id.loadLessMedication);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tabNameTextView;
        CustomTextView tabCountTextView;
        TextView divider;
        CheckBox medicationCheckBox;
        ImageView tabImageView;

        ItemViewHolder(View view) {
            super(view);
            tabNameTextView = (CustomTextView) view
                    .findViewById(R.id.tabNameTextView);
            tabCountTextView = (CustomTextView) view
                    .findViewById(R.id.tabCountTextView);
            divider = (TextView) view
                    .findViewById(R.id.divider);
            medicationCheckBox = (CheckBox) view
                    .findViewById(R.id.medicationCheckBox);
            tabImageView = (ImageView) view
                    .findViewById(R.id.tabImageView);
        }
    }

    public interface OnMedicationNotificationEventClick {
        void onMedicationLoadMoreFooterClicked();
        void onMedicationCheckBoxClicked(Medication medication, String title, int position, boolean checked);
    }
}
