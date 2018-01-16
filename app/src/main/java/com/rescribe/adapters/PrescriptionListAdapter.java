package com.rescribe.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.prescription_response_model.PrescriptionModel;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by jeetal on 10/5/17.
 */

public class PrescriptionListAdapter extends RecyclerView.Adapter<PrescriptionListAdapter.ListViewHolder> {

    private List<PrescriptionModel> mPrescriptionData;
    private Context mContext;

    public PrescriptionListAdapter(Context context, List<PrescriptionModel> dataSet) {
        this.mPrescriptionData = dataSet;
        this.mContext = context;
    }

    @Override
    public PrescriptionListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.medicine_prescribtion_activity, parent, false);
        return new PrescriptionListAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PrescriptionListAdapter.ListViewHolder holder, final int position) {

        final PrescriptionModel prescriptionDataObject = mPrescriptionData.get(position);
        if (prescriptionDataObject.getInstruction().equals("")) {
            holder.mHighlightedInstructionView.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)holder.showMedicineLayout.getLayoutParams();
            params.setMargins(0, 0, 0,mContext.getResources().getDimensionPixelSize(R.dimen.dp26) );
            holder.showMedicineLayout.setLayoutParams(params);
        } else {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)holder.showMedicineLayout.getLayoutParams();
            params.setMargins(0, 0, 0,0 );
            holder.showMedicineLayout.setLayoutParams(params);
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
            holder.mDividerForInstruction.setVisibility(View.VISIBLE);
        } else {
            holder.mExpandLayout.setVisibility(View.GONE);
            holder.mDividerForInstruction.setVisibility(View.INVISIBLE);
        }

        holder.mCardViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Expand and Collapse function
                for (PrescriptionModel object : mPrescriptionData) {
                    object.setExpanded(false);
                }
                if (holder.mExpandLayout.getVisibility() == View.GONE) {
                    if (prescriptionDataObject.getBreakfastBefore().isEmpty() && prescriptionDataObject.getBreakfastAfter().isEmpty() &&
                            prescriptionDataObject.getLunchBefore().isEmpty() && prescriptionDataObject.getLunchAfter().isEmpty() &&
                            prescriptionDataObject.getDinnerBefore().isEmpty() && prescriptionDataObject.getDinnerAfter().isEmpty() &&
                            prescriptionDataObject.getSnacksBefore().isEmpty() && prescriptionDataObject.getSnacksAfter().isEmpty()) {
                        holder.mExpandLayout.setVisibility(View.GONE);
                        holder.mDividerForInstruction.setVisibility(View.INVISIBLE);
                        prescriptionDataObject.setExpanded(false);
                    } else {
                        holder.mExpandLayout.setVisibility(View.VISIBLE);
                        holder.mDividerForInstruction.setVisibility(View.VISIBLE);
                        prescriptionDataObject.setExpanded(true);
                    }
                } else {
                    holder.mExpandLayout.setVisibility(View.GONE);
                    holder.mDividerForInstruction.setVisibility(View.INVISIBLE);
                    prescriptionDataObject.setExpanded(false);
                }
                notifyDataSetChanged();
            }
        });
        //condition check for before after dosage slot wise
        if(prescriptionDataObject.getMealTime().equalsIgnoreCase(mContext.getString(R.string.break_fast))){
            if(!prescriptionDataObject.getBreakfastAfter().isEmpty()){
                holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.VISIBLE);
                holder.mShowDoseAndSlot.setText(prescriptionDataObject.getBreakfastAfter() + " " + mContext.getString(R.string.after) + " " +mContext.getString(R.string.breakfast));

            }else if(!prescriptionDataObject.getBreakfastBefore().isEmpty()){
                holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.VISIBLE);
                holder.mShowDoseAndSlot.setText(prescriptionDataObject.getBreakfastBefore() + " " +  mContext.getString(R.string.before) + " " +mContext.getString(R.string.breakfast));
            }else{
                holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.INVISIBLE);
            }

        }else if(prescriptionDataObject.getMealTime().equalsIgnoreCase(mContext.getString(R.string.mlunch))){
            if(!prescriptionDataObject.getLunchAfter().isEmpty()){
                holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.VISIBLE);
                holder.mShowDoseAndSlot.setText(prescriptionDataObject.getLunchAfter() + " " + mContext.getString(R.string.after) + " " +mContext.getString(R.string.lunch));

            }else if(!prescriptionDataObject.getLunchBefore().isEmpty()){
                holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.VISIBLE);
                holder.mShowDoseAndSlot.setText(prescriptionDataObject.getLunchBefore() + " " +  mContext.getString(R.string.before) + " " +mContext.getString(R.string.lunch));
            }else{
                holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.INVISIBLE);

            }

        }else if(prescriptionDataObject.getMealTime().equalsIgnoreCase(mContext.getString(R.string.msnacks))){
            if(!prescriptionDataObject.getSnacksAfter().isEmpty()){
                holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.VISIBLE);
                holder.mShowDoseAndSlot.setText(prescriptionDataObject.getSnacksAfter() + " " + mContext.getString(R.string.after) + " " +mContext.getString(R.string.snacks));

            }else if(!prescriptionDataObject.getSnacksBefore().isEmpty()){
                holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.VISIBLE);
                holder.mShowDoseAndSlot.setText(prescriptionDataObject.getSnacksBefore() + " " +  mContext.getString(R.string.before) + " " +mContext.getString(R.string.snacks));
            }else{
                holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.INVISIBLE);

            }
        }else if(prescriptionDataObject.getMealTime().equalsIgnoreCase(mContext.getString(R.string.mdinner))){
            if(!prescriptionDataObject.getDinnerAfter().isEmpty()){
                holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.VISIBLE);
                holder.mShowDoseAndSlot.setText(prescriptionDataObject.getDinnerAfter() + " " + mContext.getString(R.string.after) + " " +mContext.getString(R.string.dinner));

            }else if(!prescriptionDataObject.getDinnerBefore().isEmpty()){
                holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.VISIBLE);
                holder.mShowDoseAndSlot.setText(prescriptionDataObject.getDinnerBefore() + " " +  mContext.getString(R.string.before) + " " +mContext.getString(R.string.dinner));
            }else{
                holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.INVISIBLE);

            }
        }else if(prescriptionDataObject.getMealTime().equals("")){
            holder.mShowDurationAndQuantityOfDoseLayout.setVisibility(View.INVISIBLE);
        }
        //-split medicineName at 15th, if long string-----------
        String medicineType = prescriptionDataObject.getMedicineTypeName();

        holder.mTextviewNameOfMedicine.setText(medicineType.charAt(0)+". "+prescriptionDataObject.getMedicineName());
        //holder.mDays.setText(prescriptionDataObject.getDays()+" "+mContext.getString(R.string.days));
        holder.mDays.setText(calculateDays(CommonMethods.getCurrentDateTime(),CommonMethods.getFormattedDate(prescriptionDataObject.getEndDate(),RescribeConstants.DATE_PATTERN.UTC_PATTERN,RescribeConstants.DATE_PATTERN.DD_MM_YYYY)));
        holder.mDoseAge.setText(prescriptionDataObject.getDosage());
        holder.mMedicineType.setBackgroundDrawable(CommonMethods.getMedicineTypeImage(prescriptionDataObject.getMedicineTypeName(), mContext, ContextCompat.getColor(mContext,R.color.tagColor)));

        setPrescriptionDosageData(holder, position);


    }

    private void setPrescriptionDosageData(ListViewHolder holder, int position) {

        final PrescriptionModel prescriptionData = mPrescriptionData.get(position);
        String durationOfBreakFast = "";
        String durationOfLunch = "";
        String durationOfDinner = "";
        String durationOfEvening = "";
        String doseQuantity = "";
        //  **************************BreakFast********************************************
        if (!prescriptionData.getBreakfastBefore().isEmpty()) {
            durationOfBreakFast = mContext.getString(R.string.before) + " " + mContext.getString(R.string.breakfast);
            doseQuantity = prescriptionData.getBreakfastBefore();

        }
        if (!prescriptionData.getBreakfastAfter().isEmpty()) {
            durationOfBreakFast = mContext.getString(R.string.after) + " " + mContext.getString(R.string.breakfast);
            doseQuantity = prescriptionData.getBreakfastAfter();

        }
        if (doseQuantity.isEmpty()) {
            holder.mShowMorningDosage.setVisibility(View.GONE);
        } else {
            holder.mShowMorningDosage.setVisibility(View.VISIBLE);
        }
        //if current timeOfDosage is within breakfast timeOfDosage ie. 7 am to 11 am then breakfast image highlighted with circular background

        holder.mShowMorningFullFormOfDose.setText(doseQuantity + " " + durationOfBreakFast);

        //***************************************Lunch*****************************************************

        durationOfLunch = "";
        doseQuantity = "";

        if (!prescriptionData.getLunchBefore().isEmpty()) {
            durationOfLunch = mContext.getString(R.string.before) + " " + mContext.getString(R.string.lunch);
            doseQuantity = prescriptionData.getLunchBefore();

        }
        if (!prescriptionData.getLunchAfter().isEmpty()) {
            durationOfLunch = mContext.getString(R.string.after) + " " + mContext.getString(R.string.lunch);
            doseQuantity = prescriptionData.getLunchAfter();

        }
        if (doseQuantity.isEmpty()) {
            holder.mShowAfternoonDosage.setVisibility(View.GONE);

        } else {
            holder.mShowAfternoonDosage.setVisibility(View.VISIBLE);
        }

        holder.mShowAfterNoonFullFormOfDose.setText(doseQuantity + " " + durationOfLunch);
        //************************************Evening************************************************
        durationOfEvening = "";
        doseQuantity = "";

        if (!prescriptionData.getSnacksBefore().isEmpty()) {
            durationOfEvening = mContext.getString(R.string.before) + " " + mContext.getString(R.string.snacks);
            doseQuantity = prescriptionData.getSnacksBefore();


        }
        if (!prescriptionData.getSnacksAfter().isEmpty()) {
            durationOfEvening = mContext.getString(R.string.after) + " " + mContext.getString(R.string.snacks);
            doseQuantity = prescriptionData.getSnacksAfter();

        }
        if (doseQuantity.isEmpty()) {
            holder.mShowEveningDosage.setVisibility(View.GONE);
        } else {
            holder.mShowEveningDosage.setVisibility(View.VISIBLE);
        }


        holder.mEveningDose.setText(doseQuantity + " " + durationOfEvening);


        //************************Dinner********************************************

        durationOfDinner = "";
        doseQuantity = "";

        if (!prescriptionData.getDinnerBefore().isEmpty()) {
            doseQuantity = prescriptionData.getDinnerBefore();
            durationOfDinner = mContext.getString(R.string.before) + " " + mContext.getString(R.string.dinner);

        }
        if (!prescriptionData.getDinnerAfter().isEmpty()) {
            doseQuantity = prescriptionData.getDinnerAfter();
            durationOfDinner = mContext.getString(R.string.after) + " " + mContext.getString(R.string.dinner);

        }
        if (doseQuantity.isEmpty()) {
            holder.mShowDinnerDosage.setVisibility(View.GONE);
        } else {
            holder.mShowDinnerDosage.setVisibility(View.VISIBLE);
        }


        holder.mShowNightFullFormOfDose.setText(doseQuantity + " " + durationOfDinner);


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
        long delta = 0;
        SimpleDateFormat myFormat = new SimpleDateFormat(RescribeConstants.DATE_PATTERN.DD_MM_YYYY, Locale.US);

        try {
            Date date1 = myFormat.parse(currentDate);
            Date date2 = myFormat.parse(actualStartDate);
            long timeOne = date1.getTime();
            long timeTwo = date2.getTime();
            long oneDay = 1000 * 60 * 60 * 24;
            delta = (timeTwo - timeOne) / oneDay;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (delta > 0) {
            return delta + " "+mContext.getString(R.string.days);
        } else {
            delta *= -1;
            return delta+ " "+mContext.getString(R.string.days);
        }

    }

}