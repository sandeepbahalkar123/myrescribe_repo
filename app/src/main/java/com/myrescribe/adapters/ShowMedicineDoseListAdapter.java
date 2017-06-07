package com.myrescribe.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.prescription_response_model.PrescriptionData;
import com.myrescribe.ui.customesViews.CustomTextView;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;

/**
 * Created by jeetal on 10/5/17.
 */

public class ShowMedicineDoseListAdapter extends RecyclerView.Adapter<ShowMedicineDoseListAdapter.ListViewHolder> {

    private ArrayList<PrescriptionData> mDataSet;
    RowClickListener mRowClickListener;
    Context mContext;
    Boolean isPatientLogin;
    String mGetMealTime;

    public ShowMedicineDoseListAdapter(Context context, ArrayList<PrescriptionData> dataSet, Boolean isPatientLogin, String mMealTime) {
        this.mDataSet = dataSet;
        this.mContext = context;
        this.isPatientLogin = isPatientLogin;
        this.mGetMealTime = mMealTime;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView mTextViewInstructions;
        CustomTextView mTextviewNameOfMedicine;
        TextView mTextviewTabletLabel;
        View mExpandLayout;
        CardView mCardViewLayout;
        ImageView mUpIcon;
        ImageView mdownIcon;
        RelativeLayout mRelativeLayoutShowBreakfastIcon, mRelativeLayoutShowLunchIcon, mRelativeLayoutShowDinnerIcon;
        LinearLayout mDetailedInstructions;
        TextView mDoseQuantity;
        TextView mDays;
        TextView mDosePeriod,mDoseQuantityNumber,mDoseSlot;
        LinearLayout mHighlightedInstructionView;
        CustomTextView mTextViewhightlightInstructions;
        TextView mShowMorningFullFormOfDose, mShowAfterNoonFullFormOfDose, mShowNightFullFormOfDose;
        TextView mMorningDoseQuanity, mLunchDoseQuantity, mDinnerDoseQuantity;
        ImageView mHightLightMorningDose, mHightLightAfternoonDose, mHightLightNightDose;
        TextView mDoseQuantityPeriod;

        public ListViewHolder(View view) {
            super(view);
            mUpIcon = (ImageView) view.findViewById(R.id.imageViewUpArrow);
            mdownIcon = (ImageView) view.findViewById(R.id.imageViewdownArrow);
            mTextViewInstructions = (TextView) view.findViewById(R.id.tv_instructions);
            mDetailedInstructions = (LinearLayout) view.findViewById(R.id.detailedInstructions);
            mTextviewNameOfMedicine = (CustomTextView) view.findViewById(R.id.medicineName);
            mTextviewTabletLabel = (TextView) view.findViewById(R.id.textviewTabletLabel);
            mExpandLayout = (View) view.findViewById(R.id.expandPrescriptionView);
            mTextViewhightlightInstructions = (CustomTextView) view.findViewById(R.id.textViewhightlightInstructions);
            mHighlightedInstructionView = (LinearLayout) view.findViewById(R.id.highlightedInstructionView);
            mCardViewLayout = (CardView) view.findViewById(R.id.card_view);
            // Dose Time
            mDoseQuantity = (TextView) view.findViewById(R.id.doseQuantity);
            // Dose Time
            mDays = (TextView) view.findViewById(R.id.days);
            mShowMorningFullFormOfDose = (TextView) view.findViewById(R.id.morningDose);
            mShowAfterNoonFullFormOfDose = (TextView) view.findViewById(R.id.afternoonDose);
            mShowNightFullFormOfDose = (TextView) view.findViewById(R.id.nightDose);
            mMorningDoseQuanity = (TextView) view.findViewById(R.id.morningDoseQuantity);
            mLunchDoseQuantity = (TextView) view.findViewById(R.id.afternoonDoseQuantity);
            mDinnerDoseQuantity = (TextView) view.findViewById(R.id.nightDoseQuantity);
            mHightLightMorningDose = (ImageView) view.findViewById(R.id.imageViewMorningDose);
            mHightLightAfternoonDose = (ImageView) view.findViewById(R.id.imageViewAfternoonDose);
            mHightLightNightDose = (ImageView) view.findViewById(R.id.imageViewNightDose);
            //mDoseQuantityPeriod = (TextView) view.findViewById(R.id.doseQuantityPeriod);
            mDosePeriod = (TextView) view.findViewById(R.id.dosePeriod);
            mDoseQuantityNumber = (TextView) view.findViewById(R.id.doseQuantityNumber);
            mDoseSlot = (TextView) view.findViewById(R.id.doseSlot);
            mRelativeLayoutShowBreakfastIcon = (RelativeLayout) view.findViewById(R.id.relativeLayoutShowBreakfastIcon);
            mRelativeLayoutShowLunchIcon = (RelativeLayout) view.findViewById(R.id.relativeLayoutShowLunchIcon);
            mRelativeLayoutShowDinnerIcon = (RelativeLayout) view.findViewById(R.id.relativeLayoutShowDinnerIcon);
        }
    }

    @Override
    public ShowMedicineDoseListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.medicine_prescribtion_activity, parent, false);
        return new ShowMedicineDoseListAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShowMedicineDoseListAdapter.ListViewHolder holder, final int position) {
        final PrescriptionData dataObject = mDataSet.get(position);

        if (dataObject.getExpanded()) {
            holder.mExpandLayout.setVisibility(View.VISIBLE);
            holder.mUpIcon.setVisibility(View.VISIBLE);
            holder.mdownIcon.setVisibility(View.GONE);
            holder.mHighlightedInstructionView.setVisibility(View.GONE);

        } else {
            holder.mExpandLayout.setVisibility(View.GONE);
            holder.mUpIcon.setVisibility(View.GONE);
            holder.mdownIcon.setVisibility(View.VISIBLE);
            holder.mHighlightedInstructionView.setVisibility(View.VISIBLE);
        }

        holder.mCardViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (PrescriptionData object : mDataSet) {
                    object.setExpanded(false);
                }
                if (holder.mExpandLayout.getVisibility() == View.GONE) {
                   holder.mHighlightedInstructionView.setVisibility(View.GONE);
                    holder.mExpandLayout.setVisibility(View.VISIBLE);
                    dataObject.setExpanded(true);
                } else {
                    holder.mExpandLayout.setVisibility(View.GONE);
                   holder.mHighlightedInstructionView.setVisibility(View.VISIBLE);
                    dataObject.setExpanded(false);
                }
                notifyDataSetChanged();

            }
        });

        holder.mDoseQuantity.setText("("+dataObject.getDosage()+")");
        holder.mTextviewNameOfMedicine.setText("" + dataObject.getMedicineName());
        if(dataObject.getInstruction().equals("")){
            holder.mDetailedInstructions.setVisibility(View.GONE);
            holder.mHighlightedInstructionView.setVisibility(View.GONE);
        }else{
            holder.mDetailedInstructions.setVisibility(View.VISIBLE);
            holder.mTextViewInstructions.setText("" + dataObject.getInstruction());
            holder.mTextViewhightlightInstructions.setText("" + dataObject.getInstruction());
        }



        if (Integer.parseInt(dataObject.getDays()) > 1) {
            holder.mDays.setText("" + dataObject.getDays() + " days");
        } else {
            holder.mDays.setText("" + dataObject.getDays() + " day");
        }
        //+++++++++
        String data = "";
        String time = "";
        String breakFast = "";
        String mBreakFast = "";
        String lunch = "";
        String dinner = "";
        String quantity = "";
        //::::::::::::::::::::::
      //  **************************BreakFast********************************************
        if (!dataObject.getMorningB().isEmpty()) {
            data = dataObject.getDosage();
            breakFast = "Before BreakFast";
            time = mContext.getString(R.string.before);
            quantity = dataObject.getMorningB();
            mBreakFast = mContext.getString(R.string.break_fast);
        }
        if (!dataObject.getMorningA().isEmpty()) {
            data = dataObject.getDosage();
            breakFast = "After BreakFast";
            time = mContext.getString(R.string.after);
            quantity = dataObject.getMorningA();
            mBreakFast = mContext.getString(R.string.break_fast);
        }
        if (data.isEmpty()) {
            holder.mRelativeLayoutShowBreakfastIcon.setVisibility(View.GONE);
            holder.mShowMorningFullFormOfDose.setVisibility(View.GONE);
            holder.mMorningDoseQuanity.setVisibility(View.GONE);
        } else {   //if current time is within breakfast time ie. 7 am to 11 am then breakfast image highlighted with circular background

                holder.mRelativeLayoutShowBreakfastIcon.setVisibility(View.VISIBLE);
                holder.mShowMorningFullFormOfDose.setVisibility(View.VISIBLE);
                holder.mMorningDoseQuanity.setVisibility(View.VISIBLE);

        }  if (mGetMealTime.equals(mContext.getString(R.string.break_fast))) {
            holder.mHightLightMorningDose.setBackground(mContext.getResources().getDrawable(R.mipmap.highlight));
            holder.mDosePeriod.setText(time);
            holder.mDoseQuantityNumber.setText(quantity);
            holder.mDoseSlot.setText(mBreakFast);
        }


        holder.mShowMorningFullFormOfDose.setText(breakFast);
        holder.mMorningDoseQuanity.setText("("+quantity+"Tab)");
        //***************************************Lunch*****************************************************
        //::::::::::::::::::::::
        data = "";
         time = "";
         breakFast = "";
       mBreakFast = "";
        lunch = "";
        dinner = "";
         quantity = "";
        //::::::::::::::::::::::
        if (!dataObject.getLunchB().isEmpty()) {
            data = dataObject.getDosage();
            lunch = "Before Lunch";
            time = mContext.getString(R.string.before);
            quantity = dataObject.getLunchB();
            mBreakFast = mContext.getString(R.string.lunch);
        }
        if (!dataObject.getLunchA().isEmpty()) {
            data = dataObject.getDosage();
            lunch = "After Lunch";
            time = mContext.getString(R.string.after);
            quantity = dataObject.getLunchA();
            mBreakFast = mContext.getString(R.string.lunch);
        }
        if (data.isEmpty()) {
            holder.mRelativeLayoutShowLunchIcon.setVisibility(View.GONE);
            holder.mShowAfterNoonFullFormOfDose.setVisibility(View.GONE);
            holder.mLunchDoseQuantity.setVisibility(View.GONE);



        } else {
            //if current time is within lunch time ie. 11 am to 3 pm then lunch image highlighted with circular background
                holder.mRelativeLayoutShowLunchIcon.setVisibility(View.VISIBLE);
                holder.mLunchDoseQuantity.setVisibility(View.VISIBLE);
                holder.mShowAfterNoonFullFormOfDose.setVisibility(View.VISIBLE);

        }
        if (mGetMealTime.equals(mContext.getString(R.string.mlunch))) {
            holder.mHightLightAfternoonDose.setBackground(mContext.getResources().getDrawable(R.mipmap.highlight));
            holder.mDosePeriod.setText(time);
            holder.mDoseQuantityNumber.setText(quantity);
            holder.mDoseSlot.setText(mBreakFast);
        }


        holder.mShowAfterNoonFullFormOfDose.setText(lunch);
        holder.mLunchDoseQuantity.setText("("+quantity+"Tab)");
        //::::::::::::::::::::::
        //****************************************Dinner********************************************
        data = "";
       time = "";
       breakFast = "";
         mBreakFast = "";
        lunch = "";
         dinner = "";
        quantity = "";
        //::::::::::::::::::::::
        if (!dataObject.getDinnerB().isEmpty()) {
            time = mContext.getString(R.string.before);
            quantity = dataObject.getDinnerB();
            mBreakFast = mContext.getString(R.string.dinner);
            data = dataObject.getDosage();
            dinner = "Before Dinner";
        }
        if (!dataObject.getDinnerA().isEmpty()) {
            time = mContext.getString(R.string.after);
            quantity = dataObject.getDinnerA();
            mBreakFast = mContext.getString(R.string.dinner);
            data = dataObject.getDosage();
            dinner = "After Dinner";
        }
        if (data.isEmpty()) {
            holder.mRelativeLayoutShowDinnerIcon.setVisibility(View.GONE);
            holder.mDinnerDoseQuantity.setVisibility(View.GONE);
            holder.mShowNightFullFormOfDose.setVisibility(View.GONE);
        } else {
            //if current time is within dinner time ie. 7 pm to 11 pm then dinner image highlighted with circular background
                holder.mShowNightFullFormOfDose.setVisibility(View.VISIBLE);
                holder.mDinnerDoseQuantity.setVisibility(View.VISIBLE);
                holder.mRelativeLayoutShowDinnerIcon.setVisibility(View.VISIBLE);
                holder.mShowNightFullFormOfDose.setVisibility(View.VISIBLE);
        }
        if (mGetMealTime.equals(mContext.getString(R.string.mdinner))) {
            holder.mHightLightNightDose.setBackground(mContext.getResources().getDrawable(R.mipmap.highlight));
            holder.mDosePeriod.setText(time);
            holder.mDoseQuantityNumber.setText(quantity);
            holder.mDoseSlot.setText(mBreakFast);
        }


        holder.mDinnerDoseQuantity.setText("("+quantity+"Tab)");
        holder.mShowNightFullFormOfDose.setText(dinner);
        //::::::::::::::::::::::
        //---------------

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface RowClickListener {
        void onRowClicked(ArrayList<PrescriptionData> dataObjects, int position, View v, String mClickCodes);
    }

    public void setRowClickListener(RowClickListener mRowClickListener) {
        this.mRowClickListener = mRowClickListener;
    }
}
