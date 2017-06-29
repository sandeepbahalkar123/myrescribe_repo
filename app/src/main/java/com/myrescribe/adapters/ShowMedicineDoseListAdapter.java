package com.myrescribe.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.prescription_response_model.PrescriptionData;
import com.myrescribe.util.CommonMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by jeetal on 10/5/17.
 */

public class ShowMedicineDoseListAdapter extends RecyclerView.Adapter<ShowMedicineDoseListAdapter.ListViewHolder> {

    private ArrayList<PrescriptionData> mPrescriptionData;
    private Context mContext;
    private Boolean isPatientLogin;
    private String mGetMealTime;
    private List<PrescriptionData> mSearchListByMedicineName;

    public ShowMedicineDoseListAdapter(Context context, ArrayList<PrescriptionData> dataSet, Boolean isPatientLogin, String mMealTime) {
        this.mPrescriptionData = dataSet;
        this.mContext = context;
        this.isPatientLogin = isPatientLogin;
        this.mGetMealTime = mMealTime;
        this.mSearchListByMedicineName = new ArrayList<>();
        this.mSearchListByMedicineName.addAll(mPrescriptionData);
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_instructions)
        TextView mTextViewInstructions;
        @BindView(R.id.detailedInstructions)
        LinearLayout mDetailedInstructions;
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
        @BindView(R.id.imageViewMorningDose)
        ImageView mHightLightMorningDose;
        @BindView(R.id.imageViewAfternoonDose)
        ImageView mHightLightAfternoonDose;
        @BindView(R.id.imageViewNightDose)
        ImageView mHightLightNightDose;
        @BindView(R.id.imageViewEveningDose)
        ImageView mHightLightEveningDose;
        @BindView(R.id.dosePeriod)
        TextView mDosePeriod;
        @BindView(R.id.doseQuantityNumber)
        TextView mDoseQuantityNumber;
        @BindView(R.id.doseSlot)
        TextView mDoseSlot;
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

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
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
        final PrescriptionData prescriptionDataObject = mPrescriptionData.get(position);

        if (prescriptionDataObject.getExpanded()) {
            holder.mExpandLayout.setVisibility(View.VISIBLE);
            holder.mHighlightedInstructionView.setVisibility(View.GONE);

        } else {
            holder.mExpandLayout.setVisibility(View.GONE);
            holder.mHighlightedInstructionView.setVisibility(View.VISIBLE);
        }

        holder.mCardViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (PrescriptionData object : mPrescriptionData) {
                    object.setExpanded(false);
                }
                if (holder.mExpandLayout.getVisibility() == View.GONE) {
                    holder.mHighlightedInstructionView.setVisibility(View.GONE);
                    holder.mExpandLayout.setVisibility(View.VISIBLE);
                    prescriptionDataObject.setExpanded(true);

                } else {
                    holder.mExpandLayout.setVisibility(View.GONE);
                    holder.mHighlightedInstructionView.setVisibility(View.VISIBLE);
                    prescriptionDataObject.setExpanded(false);
                }
                notifyDataSetChanged();
            }
        });
        holder.mTextviewNameOfMedicine.setText(prescriptionDataObject.getMedicineName());
        if (prescriptionDataObject.getInstruction().equals("")) {
            holder.mDetailedInstructions.setVisibility(View.GONE);
            holder.mHighlightedInstructionView.setVisibility(View.GONE);
        } else {
            holder.mDetailedInstructions.setVisibility(View.VISIBLE);
            holder.mTextViewInstructions.setText(prescriptionDataObject.getInstruction());
            holder.mTextViewhightlightInstructions.setText(prescriptionDataObject.getInstruction());
        }

        holder.mDays.setText("" + prescriptionDataObject.getDosage());
        setPrescriptionDosageData(holder, position);
    }

    private void setPrescriptionDosageData(ListViewHolder holder, int position) {
        final PrescriptionData prescriptionData = mPrescriptionData.get(position);
        prescriptionData.setEveningB("2");
        String quantityOfDose = "";
        String timeOfDosage = "";
        String durationOfBreakFast = "";
        String showSlotLabel = "";
        String durationOfLunch = "";
        String durationOfDinner = "";
        String durationOfEvening = "";
        String doseQuantity = "";
        holder.mMedicineType.setBackgroundDrawable(CommonMethods.getMedicineTypeImage(prescriptionData.getMedicineTypeName(),mContext));
        //  **************************BreakFast********************************************
        if (!prescriptionData.getMorningB().isEmpty()) {
            quantityOfDose = prescriptionData.getDosage();
            durationOfBreakFast = mContext.getString(R.string.before) + " " + mContext.getString(R.string.breakfast);
            timeOfDosage = mContext.getString(R.string.before);
            doseQuantity = prescriptionData.getMorningB();
            showSlotLabel = mContext.getString(R.string.breakfast);
        }
        if (!prescriptionData.getMorningA().isEmpty()) {
            quantityOfDose = prescriptionData.getDosage();
            durationOfBreakFast = mContext.getString(R.string.after) + " " + mContext.getString(R.string.breakfast);
            timeOfDosage = mContext.getString(R.string.after);
            doseQuantity = prescriptionData.getMorningA();
            showSlotLabel = mContext.getString(R.string.breakfast);
        }
        if (quantityOfDose.isEmpty()) {
            holder.mHightLightMorningDose.setVisibility(View.GONE);
            holder.mShowMorningDosage.setVisibility(View.GONE);
        } else {
            holder.mHightLightMorningDose.setVisibility(View.VISIBLE);
            holder.mShowMorningDosage.setVisibility(View.VISIBLE);
        }
        //if current timeOfDosage is within breakfast timeOfDosage ie. 7 am to 11 am then breakfast image highlighted with circular background
        if (mGetMealTime.equals(mContext.getString(R.string.break_fast))) {

            holder.mDosePeriod.setText(timeOfDosage);
            holder.mDoseQuantityNumber.setText(doseQuantity);
            holder.mDoseSlot.setText(showSlotLabel);
            //if dose quantity is 1/2 or 1/4 etc then change textSize of respective textview.
            if (doseQuantity.contains("/")) {
                holder.mDoseQuantityNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp18));
            } else {
                holder.mDoseQuantityNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp28));
            }
        }
        holder.mShowMorningFullFormOfDose.setText(durationOfBreakFast);
        holder.mMorningDoseQuanity.setText(mContext.getString(R.string.opening_brace) + doseQuantity + mContext.getString(R.string.closing_brace));// + PrescriptionData.getMedicineTypeAbbreviation(prescriptionData.getMedicineTypeName()) + mContext.getString(R.string.closing_brace));

        //***************************************Lunch*****************************************************

        quantityOfDose = "";
        timeOfDosage = "";
        durationOfBreakFast = "";
        showSlotLabel = "";
        durationOfLunch = "";
        durationOfDinner = "";
        doseQuantity = "";

        if (!prescriptionData.getLunchB().isEmpty()) {
            quantityOfDose = prescriptionData.getDosage();
            durationOfLunch = mContext.getString(R.string.before) + " " + mContext.getString(R.string.lunch);
            timeOfDosage = mContext.getString(R.string.before);
            doseQuantity = prescriptionData.getLunchB();
            showSlotLabel = mContext.getString(R.string.lunch);
        }
        if (!prescriptionData.getLunchA().isEmpty()) {
            quantityOfDose = prescriptionData.getDosage();
            durationOfLunch = mContext.getString(R.string.after) + " " + mContext.getString(R.string.lunch);
            timeOfDosage = mContext.getString(R.string.after);
            doseQuantity = prescriptionData.getLunchA();
            showSlotLabel = mContext.getString(R.string.lunch);
        }
        if (quantityOfDose.isEmpty()) {
            holder.mHightLightAfternoonDose.setVisibility(View.GONE);
            holder.mShowAfternoonDosage.setVisibility(View.GONE);

        } else {
            holder.mHightLightAfternoonDose.setVisibility(View.VISIBLE);
            holder.mShowAfternoonDosage.setVisibility(View.VISIBLE);
        }
        //if current timeOfDosage is within durationOfLunch timeOfDosage ie. 11 am to 3 pm then durationOfLunch image highlighted with circular background
        if (mGetMealTime.equals(mContext.getString(R.string.mlunch))) {
            holder.mDosePeriod.setText(timeOfDosage);
            holder.mDoseQuantityNumber.setText(doseQuantity);
            holder.mDoseSlot.setText(showSlotLabel);
            //if dose quantity is 1/2 or 1/4 etc then change textSize of respective textview.
            if (doseQuantity.contains("/")) {
                holder.mDoseQuantityNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp18));
            } else {
                holder.mDoseQuantityNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp28));
            }
        }
        holder.mShowAfterNoonFullFormOfDose.setText(durationOfLunch);
        holder.mLunchDoseQuantity.setText(mContext.getString(R.string.opening_brace) + doseQuantity + mContext.getString(R.string.closing_brace));//PrescriptionData.getMedicineTypeAbbreviation(prescriptionData.getMedicineTypeName()) + mContext.getString(R.string.closing_brace));
        //************************************Evening************************************************
        quantityOfDose = "";
        timeOfDosage = "";
        durationOfBreakFast = "";
        showSlotLabel = "";
        durationOfLunch = "";
        durationOfDinner = "";
        durationOfEvening = "";
        doseQuantity = "";

        if (!prescriptionData.getEveningB().isEmpty()) {
            quantityOfDose = "10mg";/*prescriptionData.getDosage();*/
            durationOfEvening = mContext.getString(R.string.before) + " " + mContext.getString(R.string.snacks);
            timeOfDosage = mContext.getString(R.string.before);
            doseQuantity = prescriptionData.getEveningB();
            showSlotLabel = mContext.getString(R.string.snacks);
        }
       /* if (!prescriptionData.getEveningA().isEmpty()) {
            quantityOfDose = prescriptionData.getDosage();
            durationOfEvening = mContext.getString(R.string.after) + " " + mContext.getString(R.string.snacks);
            timeOfDosage = mContext.getString(R.string.after);
            doseQuantity = prescriptionData.getEveningA();
            showSlotLabel = mContext.getString(R.string.snacks);
        }*/
        if (quantityOfDose.isEmpty()) {
            holder.mHightLightEveningDose.setVisibility(View.GONE);
            holder.mShowEveningDosage.setVisibility(View.GONE);

        } else {

            holder.mHightLightEveningDose.setVisibility(View.VISIBLE);
            holder.mShowEveningDosage.setVisibility(View.VISIBLE);
        }
        //if current timeOfDosage is within durationOfLunch timeOfDosage ie. 11 am to 3 pm then durationOfLunch image highlighted with circular background
        if (mGetMealTime.equals(mContext.getString(R.string.msnacks))) {

            holder.mDosePeriod.setText(timeOfDosage);
            holder.mDoseQuantityNumber.setText(doseQuantity);
            holder.mDoseSlot.setText(showSlotLabel);
            //if dose quantity is 1/2 or 1/4 etc then change textSize of respective textview.
            if (doseQuantity.contains("/")) {
                holder.mDoseQuantityNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp18));
            } else {
                holder.mDoseQuantityNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp28));
            }
        }
        holder.mEveningDose.setText(durationOfEvening);
        holder.mEveningDoseQuantity.setText(mContext.getString(R.string.opening_brace) + doseQuantity + mContext.getString(R.string.closing_brace));//PrescriptionData.getMedicineTypeAbbreviation(prescriptionData.getMedicineTypeName()) + mContext.getString(R.string.closing_brace));



        // AmAAmAAmlA****Dinner********************************************

        quantityOfDose = "";
        timeOfDosage = "";
        durationOfBreakFast = "";
        showSlotLabel = "";
        durationOfLunch = "";
        durationOfDinner = "";
        doseQuantity = "";

        if (!prescriptionData.getDinnerB().isEmpty()) {
            timeOfDosage = mContext.getString(R.string.before);
            doseQuantity = prescriptionData.getDinnerB();
            showSlotLabel = mContext.getString(R.string.dinner);
            quantityOfDose = prescriptionData.getDosage();
            durationOfDinner = mContext.getString(R.string.before) + " " + mContext.getString(R.string.dinner);
        }
        if (!prescriptionData.getDinnerA().isEmpty()) {
            timeOfDosage = mContext.getString(R.string.after);
            doseQuantity = prescriptionData.getDinnerA();
            showSlotLabel = mContext.getString(R.string.dinner);
            quantityOfDose = prescriptionData.getDosage();
            durationOfDinner = mContext.getString(R.string.after) + " " + mContext.getString(R.string.dinner);
        }
        if (quantityOfDose.isEmpty()) {
            holder.mHightLightNightDose.setVisibility(View.GONE);
            holder.mShowDinnerDosage.setVisibility(View.GONE);
        } else {
            holder.mHightLightNightDose.setVisibility(View.VISIBLE);
            holder.mShowDinnerDosage.setVisibility(View.VISIBLE);
        }
        //if current timeOfDosage is within durationOfDinner timeOfDosage ie. 7 pm to 11 pm then durationOfDinner image highlighted with circular background
        if (mGetMealTime.equals(mContext.getString(R.string.mdinner))) {
            holder.mDosePeriod.setText(timeOfDosage);
            holder.mDoseQuantityNumber.setText(doseQuantity);
            holder.mDoseSlot.setText(showSlotLabel);
            //if dose quantity is 1/2 or 1/4 etc then change textSize of respective textview.
            if (doseQuantity.contains("/")) {
                holder.mDoseQuantityNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp18));
            } else {
                holder.mDoseQuantityNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp28));
            }
        }
        holder.mDinnerDoseQuantity.setText(mContext.getString(R.string.opening_brace) + doseQuantity + mContext.getString(R.string.closing_brace)); //PrescriptionData.getMedicineTypeAbbreviation(prescriptionData.getMedicineTypeName()) + mContext.getString(R.string.closing_brace));
        holder.mShowNightFullFormOfDose.setText(durationOfDinner);
    }


    @Override
    public int getItemCount() {
        return mPrescriptionData.size();
    }


}
