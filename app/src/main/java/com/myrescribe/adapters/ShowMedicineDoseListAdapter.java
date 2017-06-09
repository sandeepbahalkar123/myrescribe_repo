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
import com.myrescribe.ui.customesViews.CustomTextView;
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

        @BindView(R.id.imageViewUpArrow)
        ImageView mUpIcon;
        @BindView(R.id.imageViewdownArrow)
        ImageView mdownIcon;
        @BindView(R.id.tv_instructions)
        TextView mTextViewInstructions;
        @BindView(R.id.detailedInstructions)
        LinearLayout mDetailedInstructions;
        @BindView(R.id.medicineName)
        TextView mTextviewNameOfMedicine;
        @BindView(R.id.textviewTabletLabel)
        TextView mTextviewTabletLabel;
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
        CardView mCardViewLayout;
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
        @BindView(R.id.showEveningDosage)
        LinearLayout mShowEveningDosage;
        View view;

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
            holder.mUpIcon.setVisibility(View.VISIBLE);
            holder.mdownIcon.setVisibility(View.INVISIBLE);
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
        holder.mTextviewNameOfMedicine.setText( prescriptionDataObject.getMedicineName());
        holder.mTextviewTabletLabel.setText(prescriptionDataObject.getMedicineTypeName() + " (" + prescriptionDataObject.getDosage() + ")");
        if (prescriptionDataObject.getInstruction().equals("")) {
            holder.mDetailedInstructions.setVisibility(View.GONE);
            holder.mHighlightedInstructionView.setVisibility(View.GONE);
        } else {
            holder.mdownIcon.setVisibility(View.INVISIBLE);
            holder.mDetailedInstructions.setVisibility(View.VISIBLE);
            holder.mTextViewInstructions.setText(prescriptionDataObject.getInstruction());
            holder.mTextViewhightlightInstructions.setText(prescriptionDataObject.getInstruction());
        }

        if (Integer.parseInt(prescriptionDataObject.getDays()) > 1) {
            holder.mDays.setText("" + prescriptionDataObject.getDays() + " days");
        } else {
            holder.mDays.setText("" + prescriptionDataObject.getDays() + " day");
        }

        setPrescriptionDosageData(holder, position);
    }

    private void setPrescriptionDosageData(ListViewHolder holder, int position) {
        final PrescriptionData prescriptionData = mPrescriptionData.get(position);
        String quantityOfDose = "";
        String timeOfDosage = "";
        String durationOfBreakFast = "";
        String showBreakFastLabel = "";
        String durationOfLunch = "";
        String durationOfDinner = "";
        String doseQuantity = "";

        //  **************************BreakFast********************************************
        if (!prescriptionData.getMorningB().isEmpty()) {
            quantityOfDose = prescriptionData.getDosage();
            durationOfBreakFast = mContext.getString(R.string.before)+" "+mContext.getString(R.string.breakfast);
            timeOfDosage = mContext.getString(R.string.before);
            doseQuantity = prescriptionData.getMorningB();
            showBreakFastLabel = mContext.getString(R.string.breakfast);
        }
        if (!prescriptionData.getMorningA().isEmpty()) {
            quantityOfDose = prescriptionData.getDosage();
            durationOfBreakFast = mContext.getString(R.string.after)+" "+mContext.getString(R.string.breakfast);
            timeOfDosage = mContext.getString(R.string.after);
            doseQuantity = prescriptionData.getMorningA();
            showBreakFastLabel = mContext.getString(R.string.breakfast);
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
            holder.mHightLightMorningDose.setImageResource(R.mipmap.breakfast_highlighted);
            holder.mDosePeriod.setText(timeOfDosage);
            holder.mDoseQuantityNumber.setText(doseQuantity);
            holder.mDoseSlot.setText(showBreakFastLabel);
            //if dose quantity is 1/2 or 1/4 etc then change textSize of respective textview.
            if (doseQuantity.contains("/")) {
                holder.mDoseQuantityNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp18));
            } else {
                holder.mDoseQuantityNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp40));
            }
        }else{
            holder.mHightLightMorningDose.setImageResource(R.mipmap.breakfast);
        }
        //if dose quantity is 1/2 or 1/4 etc then change textSize of respective textview.
        if (doseQuantity.contains("/")) {
            holder.mMorningDoseQuanity.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp16));
        } else {
            holder.mMorningDoseQuanity.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp16));
        }

        holder.mShowMorningFullFormOfDose.setText(durationOfBreakFast);
        holder.mMorningDoseQuanity.setText(mContext.getString(R.string.opening_brace) + doseQuantity + " " + mContext.getString(R.string.tablet) + mContext.getString(R.string.closing_brace));

        //***************************************Lunch*****************************************************

        quantityOfDose = "";
        timeOfDosage = "";
        durationOfBreakFast = "";
        showBreakFastLabel = "";
        durationOfLunch = "";
        durationOfDinner = "";
        doseQuantity = "";

        if (!prescriptionData.getLunchB().isEmpty()) {
            quantityOfDose = prescriptionData.getDosage();
            durationOfLunch = mContext.getString(R.string.before)+" "+mContext.getString(R.string.lunch);
            timeOfDosage = mContext.getString(R.string.before);
            doseQuantity = prescriptionData.getLunchB();
            showBreakFastLabel = mContext.getString(R.string.lunch);
        }
        if (!prescriptionData.getLunchA().isEmpty()) {
            quantityOfDose = prescriptionData.getDosage();
            durationOfLunch = mContext.getString(R.string.after)+" "+mContext.getString(R.string.lunch);
            timeOfDosage = mContext.getString(R.string.after);
            doseQuantity = prescriptionData.getLunchA();
            showBreakFastLabel = mContext.getString(R.string.lunch);
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
            holder.mHightLightAfternoonDose.setImageResource(R.mipmap.lunch_highlighted);
            holder.mDosePeriod.setText(timeOfDosage);
            holder.mDoseQuantityNumber.setText(doseQuantity);
            holder.mDoseSlot.setText(showBreakFastLabel);
            //if dose quantity is 1/2 or 1/4 etc then change textSize of respective textview.
            if (doseQuantity.contains("/")) {
                holder.mDoseQuantityNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX,mContext.getResources().getDimension(R.dimen.sp18));
            } else {
                holder.mDoseQuantityNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX,mContext.getResources().getDimension(R.dimen.sp40));
            }
        }else{
            holder.mHightLightAfternoonDose.setImageResource(R.mipmap.lunch);
        }
        //if dose quantity is 1/2 or 1/4 etc then change textSize of respective textview.
        if (doseQuantity.contains("/")) {
            holder.mLunchDoseQuantity.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp16));
        } else {
            holder.mLunchDoseQuantity.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp16));
        }

        holder.mShowAfterNoonFullFormOfDose.setText(durationOfLunch);
        holder.mLunchDoseQuantity.setText(mContext.getString(R.string.opening_brace) + doseQuantity + " " + mContext.getString(R.string.tablet) + mContext.getString(R.string.closing_brace));

        //************************************phora@146
        // AmAAmAAmlA****Dinner********************************************

        quantityOfDose = "";
        timeOfDosage = "";
        durationOfBreakFast = "";
        showBreakFastLabel = "";
        durationOfLunch = "";
        durationOfDinner = "";
        doseQuantity = "";

        if (!prescriptionData.getDinnerB().isEmpty()) {
            timeOfDosage = mContext.getString(R.string.before);
            doseQuantity = prescriptionData.getDinnerB();
            showBreakFastLabel = mContext.getString(R.string.dinner);
            quantityOfDose = prescriptionData.getDosage();
            durationOfDinner = mContext.getString(R.string.before)+" "+mContext.getString(R.string.dinner);
        }
        if (!prescriptionData.getDinnerA().isEmpty()) {
            timeOfDosage = mContext.getString(R.string.after);
            doseQuantity = prescriptionData.getDinnerA();
            showBreakFastLabel = mContext.getString(R.string.dinner);
            quantityOfDose = prescriptionData.getDosage();
            durationOfDinner = mContext.getString(R.string.after)+" "+mContext.getString(R.string.dinner);
        }
        if (quantityOfDose.isEmpty()) {
            holder.mHightLightNightDose.setVisibility(View.GONE);
            holder.mShowEveningDosage.setVisibility(View.GONE);
        } else {
            holder.mHightLightNightDose.setVisibility(View.VISIBLE);
            holder.mShowEveningDosage.setVisibility(View.VISIBLE);
        }
        //if current timeOfDosage is within durationOfDinner timeOfDosage ie. 7 pm to 11 pm then durationOfDinner image highlighted with circular background
        if (mGetMealTime.equals(mContext.getString(R.string.mdinner))) {
            holder.mHightLightNightDose.setImageResource(R.mipmap.dinner_highlighted);
            holder.mDosePeriod.setText(timeOfDosage);
            holder.mDoseQuantityNumber.setText(doseQuantity);
            holder.mDoseSlot.setText(showBreakFastLabel);
            //if dose quantity is 1/2 or 1/4 etc then change textSize of respective textview.
            if (doseQuantity.contains("/")) {
                holder.mDoseQuantityNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp18));
            } else {
                holder.mDoseQuantityNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp40));
            }
        }else{
            holder.mHightLightNightDose.setImageResource(R.mipmap.night);
        }
        //if dose quantity is 1/2 or 1/4 etc then change textSize of respective textview.
        if (doseQuantity.contains("/")) {
            holder.mDinnerDoseQuantity.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp16));
        } else {
            holder.mDinnerDoseQuantity.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp16));
        }

        holder.mDinnerDoseQuantity.setText(mContext.getString(R.string.opening_brace) + doseQuantity + " " + mContext.getString(R.string.tablet) + mContext.getString(R.string.closing_brace));
        holder.mShowNightFullFormOfDose.setText(durationOfDinner);
    }


    @Override
    public int getItemCount() {
        return mPrescriptionData.size();
    }


}
