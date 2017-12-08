package com.rescribe.adapters.unread_notification_message_list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.notification.Medication;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

// Eclipse wanted me to use a sparse array instead of my hashmaps, I just suppressed that suggestion
@SuppressLint("UseSparseArrays")
public class UnreadMedicationNotificationAdapter_Test extends StatelessSection {

    private final OnMedicationNotificationEventClick mListener;
    private boolean isDisplayMoreElementsView;
    private Context mContext;
    String title;
    ArrayList<Medication> list;

    public UnreadMedicationNotificationAdapter_Test(SectionParameters builder, Context mContext, String groupName, ArrayList<Medication> list, boolean isDisplayMoreElementsView, OnMedicationNotificationEventClick listener) {

        super(builder);

        this.mContext = mContext;
        this.isDisplayMoreElementsView = isDisplayMoreElementsView;
        this.mListener = listener;
        String hveUTaken = mContext.getString(R.string.have_u_taken).toString();
        String dinnerMed = mContext.getString(R.string.dinner_medication).toString();
        String lunchMed = mContext.getString(R.string.lunch_medication).toString();
        String snacks_med = mContext.getString(R.string.snacks_medication).toString();
        String breakfast_med = mContext.getString(R.string.breakfast_medication).toString();

        if (groupName.equalsIgnoreCase(hveUTaken + dinnerMed)) {
            this.title = groupName;
            this.list = list;
        } else if (groupName.equalsIgnoreCase(hveUTaken + lunchMed)) {
            this.title = groupName;
            this.list = list;
        } else if (groupName.equalsIgnoreCase(hveUTaken + snacks_med)) {
            this.title = groupName;
            this.list = list;
        } else if (groupName.equalsIgnoreCase(hveUTaken + breakfast_med)) {
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
        itemHolder.tabImageView.setImageDrawable(CommonMethods.getMedicineTypeImage(item.getMedicineTypeName(), mContext));

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
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        headerHolder.mGroupText.setText(title);
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
        footerHolder.loadLessMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMedicationLoadLessFooterClicked();
            }
        });
    }


    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView mGroupText;

        HeaderViewHolder(View view) {
            super(view);
            mGroupText = (TextView) view.findViewById(R.id.tabNameTextView);
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
        CheckBox medicationCheckBox;
        ImageView tabImageView;

        ItemViewHolder(View view) {
            super(view);
            rootView = view;
            tabNameTextView = (CustomTextView) view
                    .findViewById(R.id.tabNameTextView);
            medicationCheckBox = (CheckBox) view
                    .findViewById(R.id.medicationCheckBox);
            tabImageView = (ImageView) view
                    .findViewById(R.id.tabImageView);
        }
    }

    public interface OnMedicationNotificationEventClick {
        public void onMedicationLoadMoreFooterClicked();

        public void onMedicationLoadLessFooterClicked();

        public void onMedicationCheckBoxClicked(Medication medication, String title, int position);
    }
}
