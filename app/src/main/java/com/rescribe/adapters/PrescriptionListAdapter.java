package com.rescribe.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.prescription_response_model.PrescriptionData;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by jeetal on 10/5/17.
 */

public class PrescriptionListAdapter extends RecyclerView.Adapter<PrescriptionListAdapter.ListViewHolder> {

    private List<PrescriptionData> mPrescriptionData;
    private Context mContext;

    private List<PrescriptionData> mSearchListByMedicineName;

    public PrescriptionListAdapter(Context context, List<PrescriptionData> dataSet) {
        this.mPrescriptionData = dataSet;
        this.mContext = context;
        this.mSearchListByMedicineName = new ArrayList<>();
        this.mSearchListByMedicineName.addAll(mPrescriptionData);

    }

    @Override
    public PrescriptionListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.medicine_prescribtion_activity, parent, false);
        return new PrescriptionListAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PrescriptionListAdapter.ListViewHolder holder, final int position) {

        final PrescriptionData prescriptionDataObject = mPrescriptionData.get(position);
        if (prescriptionDataObject.getInstruction().equals("")) {
            holder.mHighlightedInstructionView.setVisibility(View.GONE);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,0
            );
            param.setMargins(0,0,0,10);
            holder.showMedicineLayout.setLayoutParams(param);

        } else {
            holder.mHighlightedInstructionView.setVisibility(View.VISIBLE);
            holder.mTextViewhightlightInstructions.setText(prescriptionDataObject.getInstruction());
        }

        if (prescriptionDataObject.getFreqSchedule().equals("") && prescriptionDataObject.getFreq().equals("")) {
            holder.mFrequencyString.setText("");
        } else if (!prescriptionDataObject.getFreqSchedule().equals("")) {
            holder.mFrequencyString.setText(prescriptionDataObject.getFreqSchedule());
        } else if (!prescriptionDataObject.getFreq().equals("") && prescriptionDataObject.getFreqSchedule().equals("")) {
            holder.mFrequencyString.setText(prescriptionDataObject.getFreq() + mContext.getString(R.string.times));

        }
        if (prescriptionDataObject.isExpanded()) {
            holder.mExpandLayout.setVisibility(View.VISIBLE);
        } else {
            holder.mExpandLayout.setVisibility(View.GONE);
        }

        holder.mCardViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
       //Expand and Collapse function
                for (PrescriptionData object : mPrescriptionData) {
                    object.setExpanded(false);
                }
                if (holder.mExpandLayout.getVisibility() == View.GONE) {
                    if (prescriptionDataObject.getBreakfastBefore().isEmpty() && prescriptionDataObject.getBreakfastAfter().isEmpty() &&
                            prescriptionDataObject.getLunchBefore().isEmpty() && prescriptionDataObject.getLunchAfter().isEmpty() &&
                            prescriptionDataObject.getDinnerBefore().isEmpty() && prescriptionDataObject.getDinnerAfter().isEmpty() &&
                            prescriptionDataObject.getSnacksBefore().isEmpty() && prescriptionDataObject.getSnacksAfter().isEmpty()) {
                        holder.mExpandLayout.setVisibility(View.GONE);
                        prescriptionDataObject.setExpanded(false);
                    } else {
                        holder.mExpandLayout.setVisibility(View.VISIBLE);
                        prescriptionDataObject.setExpanded(true);
                    }
                } else {
                    holder.mExpandLayout.setVisibility(View.GONE);
                    prescriptionDataObject.setExpanded(false);
                }
                notifyDataSetChanged();
            }
        });
        //condition check for before after dosage slot wise
       if(prescriptionDataObject.getMealTime().equalsIgnoreCase(mContext.getString(R.string.break_fast))){
           if(!prescriptionDataObject.getBreakfastAfter().isEmpty()){
               holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.VISIBLE);
               holder.mShowDoseAndSlot.setText(prescriptionDataObject.getBreakfastAfter() + mContext.getString(R.string.space) + mContext.getString(R.string.after) + mContext.getString(R.string.space) +mContext.getString(R.string.breakfast));

           }else if(!prescriptionDataObject.getBreakfastBefore().isEmpty()){
               holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.VISIBLE);
               holder.mShowDoseAndSlot.setText(prescriptionDataObject.getBreakfastBefore() + mContext.getString(R.string.space) +  mContext.getString(R.string.before) + mContext.getString(R.string.space) +mContext.getString(R.string.breakfast));
           }else{
               holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.GONE);
           }

       }else if(prescriptionDataObject.getMealTime().equalsIgnoreCase(mContext.getString(R.string.mlunch))){
           if(!prescriptionDataObject.getLunchAfter().isEmpty()){
               holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.VISIBLE);
               holder.mShowDoseAndSlot.setText(prescriptionDataObject.getLunchAfter() + mContext.getString(R.string.space) + mContext.getString(R.string.after) + mContext.getString(R.string.space) +mContext.getString(R.string.lunch));

           }else if(!prescriptionDataObject.getLunchBefore().isEmpty()){
               holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.VISIBLE);
               holder.mShowDoseAndSlot.setText(prescriptionDataObject.getLunchBefore() + mContext.getString(R.string.space) +  mContext.getString(R.string.before) + mContext.getString(R.string.space) +mContext.getString(R.string.lunch));
           }else{
               holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.GONE);

           }

       }else if(prescriptionDataObject.getMealTime().equalsIgnoreCase(mContext.getString(R.string.msnacks))){
           if(!prescriptionDataObject.getSnacksAfter().isEmpty()){
               holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.VISIBLE);
               holder.mShowDoseAndSlot.setText(prescriptionDataObject.getSnacksAfter() + mContext.getString(R.string.space) + mContext.getString(R.string.after) + mContext.getString(R.string.space) +mContext.getString(R.string.snacks));

           }else if(!prescriptionDataObject.getSnacksBefore().isEmpty()){
               holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.VISIBLE);
               holder.mShowDoseAndSlot.setText(prescriptionDataObject.getSnacksBefore() + mContext.getString(R.string.space) +  mContext.getString(R.string.before) + mContext.getString(R.string.space) +mContext.getString(R.string.snacks));
           }else{
               holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.GONE);

           }
       }else if(prescriptionDataObject.getMealTime().equalsIgnoreCase(mContext.getString(R.string.mdinner))){
           if(!prescriptionDataObject.getDinnerAfter().isEmpty()){
               holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.VISIBLE);
               holder.mShowDoseAndSlot.setText(prescriptionDataObject.getDinnerAfter() + mContext.getString(R.string.space) + mContext.getString(R.string.after) + mContext.getString(R.string.space) +mContext.getString(R.string.dinner));

           }else if(!prescriptionDataObject.getDinnerBefore().isEmpty()){
               holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.VISIBLE);
               holder.mShowDoseAndSlot.setText(prescriptionDataObject.getDinnerBefore() + mContext.getString(R.string.space) +  mContext.getString(R.string.before) + mContext.getString(R.string.space) +mContext.getString(R.string.dinner));
           }else{
               holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.GONE);

           }
       }else if(prescriptionDataObject.getMealTime().equals("")){
           holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.GONE);
       }
        //-split medicineName at 15th, if long string-----------

        holder.mTextviewNameOfMedicine.setText(prescriptionDataObject.getMedicineName());
        holder.mDays.setText(prescriptionDataObject.getDays()+mContext.getString(R.string.space)+mContext.getString(R.string.days));
    //    holder.mDays.setText(calculateDays(CommonMethods.getCurrentDateTime(),CommonMethods.getFormatedDate(prescriptionDataObject.getEndDate(),RescribeConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm_ss,RescribeConstants.DATE_PATTERN.DD_MM_YYYY)));
        holder.mDoseAge.setText(prescriptionDataObject.getDosage());
        holder.mMedicineType.setBackgroundDrawable(CommonMethods.getMedicineTypeImage(prescriptionDataObject.getMedicineTypeName(), mContext));

        setPrescriptionDosageData(holder, position);


    }

    private void setPrescriptionDosageData(ListViewHolder holder, int position) {

        final PrescriptionData prescriptionData = mPrescriptionData.get(position);
        String quantityOfDose = "";
        String timeOfDosage = "";
        String durationOfBreakFast = "";
        String showSlotLabel = "";
        String durationOfLunch = "";
        String durationOfDinner = "";
        String durationOfEvening = "";
        String doseQuantity = "";
        //  **************************BreakFast********************************************
        if (!prescriptionData.getBreakfastBefore().isEmpty()) {
            quantityOfDose = prescriptionData.getDosage();
            durationOfBreakFast = mContext.getString(R.string.before) + mContext.getString(R.string.space) + mContext.getString(R.string.breakfast);
            timeOfDosage = mContext.getString(R.string.before);
            doseQuantity = prescriptionData.getBreakfastBefore();
            showSlotLabel = mContext.getString(R.string.break_fast);

        }
        if (!prescriptionData.getBreakfastAfter().isEmpty()) {
            quantityOfDose = prescriptionData.getDosage();
            durationOfBreakFast = mContext.getString(R.string.after) + mContext.getString(R.string.space) + mContext.getString(R.string.breakfast);
            timeOfDosage = mContext.getString(R.string.after);
            doseQuantity = prescriptionData.getBreakfastAfter();
            showSlotLabel = mContext.getString(R.string.break_fast);

    }
        if (quantityOfDose.isEmpty()) {
            holder.mShowMorningDosage.setVisibility(View.GONE);
        } else {
            holder.mShowMorningDosage.setVisibility(View.VISIBLE);
        }
        //if current timeOfDosage is within breakfast timeOfDosage ie. 7 am to 11 am then breakfast image highlighted with circular background

        holder.mShowMorningFullFormOfDose.setText(doseQuantity + mContext.getString(R.string.space) + durationOfBreakFast);

        //***************************************Lunch*****************************************************

        quantityOfDose = "";
        timeOfDosage = "";
        durationOfBreakFast = "";
        showSlotLabel = "";
        durationOfLunch = "";
        durationOfDinner = "";
        doseQuantity = "";

        if (!prescriptionData.getLunchBefore().isEmpty()) {
            quantityOfDose = prescriptionData.getDosage();
            durationOfLunch = mContext.getString(R.string.before) + mContext.getString(R.string.space) + mContext.getString(R.string.lunch);
            timeOfDosage = mContext.getString(R.string.before);
            doseQuantity = prescriptionData.getLunchBefore();
           showSlotLabel = mContext.getString(R.string.mlunch);

        }
        if (!prescriptionData.getLunchAfter().isEmpty()) {
            quantityOfDose = prescriptionData.getDosage();
            durationOfLunch = mContext.getString(R.string.after) + mContext.getString(R.string.space) + mContext.getString(R.string.lunch);
            timeOfDosage = mContext.getString(R.string.after);
            doseQuantity = prescriptionData.getLunchAfter();
            showSlotLabel = mContext.getString(R.string.mlunch);

        }
        if (quantityOfDose.isEmpty()) {
            holder.mShowAfternoonDosage.setVisibility(View.GONE);

        } else {
            holder.mShowAfternoonDosage.setVisibility(View.VISIBLE);
        }

        holder.mShowAfterNoonFullFormOfDose.setText(doseQuantity + mContext.getString(R.string.space) + durationOfLunch);
        //************************************Evening************************************************
        quantityOfDose = "";
        timeOfDosage = "";
        durationOfBreakFast = "";
        showSlotLabel = "";
        durationOfLunch = "";
        durationOfDinner = "";
        durationOfEvening = "";
        doseQuantity = "";

        if (!prescriptionData.getSnacksBefore().isEmpty()) {
            quantityOfDose = prescriptionData.getDosage();
            durationOfEvening = mContext.getString(R.string.before) + mContext.getString(R.string.space) + mContext.getString(R.string.snacks);
            timeOfDosage = mContext.getString(R.string.before);
            doseQuantity = prescriptionData.getSnacksBefore();
            showSlotLabel = mContext.getString(R.string.msnacks);


        }
        if (!prescriptionData.getSnacksAfter().isEmpty()) {
            quantityOfDose = prescriptionData.getDosage();
            durationOfEvening = mContext.getString(R.string.after) + mContext.getString(R.string.space) + mContext.getString(R.string.snacks);
            timeOfDosage = mContext.getString(R.string.after);
            doseQuantity = prescriptionData.getSnacksAfter();
            showSlotLabel = mContext.getString(R.string.msnacks);

        }
        if (quantityOfDose.isEmpty()) {
            holder.mShowEveningDosage.setVisibility(View.GONE);
        } else {
            holder.mShowEveningDosage.setVisibility(View.VISIBLE);
        }


        holder.mEveningDose.setText(doseQuantity + mContext.getString(R.string.space) + durationOfEvening);


        //************************Dinner********************************************

        quantityOfDose = "";
        timeOfDosage = "";
        durationOfBreakFast = "";
        showSlotLabel = "";
        durationOfLunch = "";
        durationOfDinner = "";
        doseQuantity = "";

        if (!prescriptionData.getDinnerBefore().isEmpty()) {
            timeOfDosage = mContext.getString(R.string.before);
            doseQuantity = prescriptionData.getDinnerBefore();
            showSlotLabel = mContext.getString(R.string.mdinner);
            quantityOfDose = prescriptionData.getDosage();
            durationOfDinner = mContext.getString(R.string.before) + mContext.getString(R.string.space) + mContext.getString(R.string.dinner);

        }
        if (!prescriptionData.getDinnerAfter().isEmpty()) {
            timeOfDosage = mContext.getString(R.string.after);
            doseQuantity = prescriptionData.getDinnerAfter();
            showSlotLabel = mContext.getString(R.string.mdinner);
            quantityOfDose = prescriptionData.getDosage();
            durationOfDinner = mContext.getString(R.string.after) + mContext.getString(R.string.space) + mContext.getString(R.string.dinner);

        }
        if (quantityOfDose.isEmpty()) {
            holder.mShowDinnerDosage.setVisibility(View.GONE);
        } else {
            holder.mShowDinnerDosage.setVisibility(View.VISIBLE);
        }


        holder.mShowNightFullFormOfDose.setText(doseQuantity + mContext.getString(R.string.space) + durationOfDinner);


    }

    @Override
    public int getItemCount() {
        return mPrescriptionData.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.medicineName)
        TextView mTextviewNameOfMedicine;
        @BindView(R.id.expandPrescriptionView)
        View mExpandLayout;
        @BindView(R.id.textViewhightlightInstructions)
        TextView mTextViewhightlightInstructions;
        @BindView(R.id.highlightedInstructionView)
        LinearLayout mHighlightedInstructionView;
        @BindView(R.id.days)
        TextView mDays;
        @BindView(R.id.morningDose)
        TextView mShowMorningFullFormOfDose;
        @BindView(R.id.afternoonDose)
        TextView mShowAfterNoonFullFormOfDose;
        @BindView(R.id.nightDose)
        TextView mShowNightFullFormOfDose;
        @BindView(R.id.morningDoseQuantity)
        TextView mMorningDoseQuanity;
        @BindView(R.id.card_view)
        LinearLayout mCardViewLayout;
        @BindView(R.id.afternoonDoseQuantity)
        TextView mLunchDoseQuantity;
        @BindView(R.id.nightDoseQuantity)
        TextView mDinnerDoseQuantity;
        @BindView(R.id.showDoseAndSlot)
        TextView mShowDoseAndSlot;
        @BindView(R.id.showMorningDosage)
        LinearLayout mShowMorningDosage;
        @BindView(R.id.showAfternoonDosage)
        LinearLayout mShowAfternoonDosage;
        @BindView(R.id.showDinnerDosage)
        LinearLayout mShowDinnerDosage;
        View view;
        @BindView(R.id.doseImageLinearLayout)
        LinearLayout mDoseImageLinearLayout;
        @BindView(R.id.showEveningDosage)
        LinearLayout mShowEveningDosage;
        @BindView(R.id.eveningDoseQuantity)
        TextView mEveningDoseQuantity;
        @BindView(R.id.eveningDose)
        TextView mEveningDose;
        @BindView(R.id.medicineType)
        ImageView mMedicineType;
        @BindView(R.id.frequencyString)
        TextView mFrequencyString;
        @BindView(R.id.expandedMedicineDoseLayout)
        LinearLayout mExpandedMedicineDoseLayout;
        @BindView(R.id.dividerForInstruction)
        View mDividerForInstruction;
        @BindView(R.id.showDurationAndQuantityOfDoseLayout)
        LinearLayout mShowDurationAndQuantityOfDoseLayout;
        @BindView(R.id.doseAge)
        TextView mDoseAge;
        @BindView(R.id.showMedicineLayout)
        LinearLayout showMedicineLayout;


        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public String calculateDays(String currentDate, String actualStartDate) {
        long diff = 0;
        String days = "";
        SimpleDateFormat myFormat = new SimpleDateFormat(RescribeConstants.DATE_PATTERN.DD_MM_YYYY);

        try {
            Date date1 = myFormat.parse(currentDate);
            Date date2 = myFormat.parse(actualStartDate);
            diff = date2.getTime() - date1.getTime();
            days = String.valueOf(diff);
            System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }
}
