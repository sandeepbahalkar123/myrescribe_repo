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
import com.rescribe.preference.RescribePreferencesManager;
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
    private UnreadNotificationMessageActivity parentActivity;
    private String mTimeStamp;
    private boolean isDisplayMoreElementsView;
    private Context mContext;
    String title;
    ArrayList<Medication> list;

    public UnreadMedicationNotificationAdapter(SectionParameters builder, Context mContext, String groupName, ArrayList<Medication> list, boolean isDisplayMoreElementsView, String timeStamp, OnMedicationNotificationEventClick listener) {

        super(builder);

        this.mContext = mContext;
        this.parentActivity = (UnreadNotificationMessageActivity) mContext;
        this.isDisplayMoreElementsView = isDisplayMoreElementsView;
        this.mListener = listener;
        this.mTimeStamp = timeStamp;
        String hveUTaken = mContext.getString(R.string.have_u_taken);
        String dinnerMed = mContext.getString(R.string.dinner_medication);
        String lunchMed = mContext.getString(R.string.lunch_medication);
        String snacks_med = mContext.getString(R.string.snacks_medication);
        String breakfast_med = mContext.getString(R.string.breakfast_medication);

        if (groupName.equalsIgnoreCase(hveUTaken + dinnerMed + "?")) {
            this.title = groupName;
            this.list = list;
        } else if (groupName.equalsIgnoreCase(hveUTaken + lunchMed + "?")) {
            this.title = groupName;
            this.list = list;
        } else if (groupName.equalsIgnoreCase(hveUTaken + snacks_med + "?")) {
            this.title = groupName;
            this.list = list;
        } else if (groupName.equalsIgnoreCase(hveUTaken + breakfast_med + "?")) {
            this.title = groupName;
            this.list = list;
        }
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

        Medication item = list.get(position);

        itemHolder.tabNameTextView.setText(item.getMedicineName());
        itemHolder.tabCountTextView.setText(item.getQuantity());
        itemHolder.tabImageView.setImageDrawable(CommonMethods.getMedicineTypeImage(item.getMedicineTypeName(), mContext, ContextCompat.getColor(mContext, R.color.white)));

        itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(mContext, String.format("Clicked on position #%s of Section %s", sectionAdapter.getPositionInSection(itemHolder.getAdapterPosition()), title), Toast.LENGTH_SHORT).show();
            }
        });

        //---------------
        itemHolder.medicationCheckBox.setTag(item);
        itemHolder.medicationCheckBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Medication item = (Medication) cb.getTag();
                item.setTabSelected(cb.isChecked());
                list.get(position).setTabSelected(cb.isChecked());

                mListener.onMedicationCheckBoxClicked(item, title, position);
            }
        });

        if (item.isTabSelected()) {
            itemHolder.medicationCheckBox.setChecked(true);
            itemHolder.medicationCheckBox.setEnabled(false);
            itemHolder.rootView.setEnabled(false);
        } else {
            itemHolder.medicationCheckBox.setEnabled(true);
            itemHolder.rootView.setEnabled(true);
            itemHolder.medicationCheckBox.setChecked(false);
        }
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

        int showFirstMessageTimeStamp = parentActivity.isShowFirstMessageTimeStamp(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT);
        if (showFirstMessageTimeStamp == View.VISIBLE) {
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
        private final View rootView;
        CustomTextView tabNameTextView;
        CustomTextView tabCountTextView;
        TextView divider;
        CheckBox medicationCheckBox;
        ImageView tabImageView;

        ItemViewHolder(View view) {
            super(view);
            rootView = view;
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
        public void onMedicationLoadMoreFooterClicked();

        public void onMedicationCheckBoxClicked(Medication medication, String title, int position);
    }
}
