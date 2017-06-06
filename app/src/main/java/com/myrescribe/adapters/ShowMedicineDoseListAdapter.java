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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.prescription_response_model.PrescriptionData;
import com.myrescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by jeetal on 10/5/17.
 */

public class ShowMedicineDoseListAdapter extends RecyclerView.Adapter<ShowMedicineDoseListAdapter.ListViewHolder> {

    private ArrayList<PrescriptionData> mPrescriptionData;
    RowClickListener mRowClickListener;
    Context mContext;
    Boolean isPatientLogin;
    String mGetMealTime;
    private List<PrescriptionData> mSearchListByMedicineName;


    public ShowMedicineDoseListAdapter(Context context, ArrayList<PrescriptionData> dataSet, Boolean isPatientLogin, String mMealTime) {
        this.mPrescriptionData = dataSet;
        this.mContext = context;
        this.isPatientLogin = isPatientLogin;
        this.mGetMealTime = mMealTime;
        this.mSearchListByMedicineName = new ArrayList<>();
        this.mSearchListByMedicineName.addAll(mPrescriptionData);
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
        TextView mDays;
        TextView mDosePeriod, mDoseQuantityNumber, mDoseSlot;
        LinearLayout mHighlightedInstructionView;
        CustomTextView mTextViewhightlightInstructions;
        TextView mShowMorningFullFormOfDose, mShowAfterNoonFullFormOfDose, mShowNightFullFormOfDose;
        TextView mMorningDoseQuanity, mLunchDoseQuantity, mDinnerDoseQuantity;
        ImageView mHightLightMorningDose, mHightLightAfternoonDose, mHightLightNightDose;

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
        final PrescriptionData prescriptionDataObject = mPrescriptionData.get(position);
        if (position == 0) {
            holder.mCardViewLayout.setBackground(mContext.getDrawable(R.color.show_recent_prescription_bgcolor));
        } else {
            holder.mCardViewLayout.setBackground(mContext.getDrawable(R.color.prescription_bgcolor));
        }
        if (prescriptionDataObject.getExpanded()) {
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
        holder.mTextviewNameOfMedicine.setText("" + prescriptionDataObject.getMedicineName());
        holder.mTextviewTabletLabel.setText("" + prescriptionDataObject.getMedicineTypeName() + " (" + prescriptionDataObject.getDosage() + ")");
        if (prescriptionDataObject.getInstruction().equals("")) {
            holder.mDetailedInstructions.setVisibility(View.GONE);
            holder.mHighlightedInstructionView.setVisibility(View.GONE);
        } else {
            holder.mDetailedInstructions.setVisibility(View.VISIBLE);
            holder.mTextViewInstructions.setText("" + prescriptionDataObject.getInstruction());
            holder.mTextViewhightlightInstructions.setText("" + prescriptionDataObject.getInstruction());
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
            durationOfBreakFast = mContext.getString(R.string.before_break_fast);
            timeOfDosage = mContext.getString(R.string.before);
            doseQuantity = prescriptionData.getMorningB();
            showBreakFastLabel = mContext.getString(R.string.break_fast);
        }
        if (!prescriptionData.getMorningA().isEmpty()) {
            quantityOfDose = prescriptionData.getDosage();
            durationOfBreakFast = mContext.getString(R.string.after_break_fast);
            timeOfDosage = mContext.getString(R.string.after);
            doseQuantity = prescriptionData.getMorningA();
            showBreakFastLabel = mContext.getString(R.string.break_fast);
        }
        if (quantityOfDose.isEmpty()) {
            holder.mRelativeLayoutShowBreakfastIcon.setVisibility(View.GONE);
            holder.mShowMorningFullFormOfDose.setVisibility(View.GONE);
            holder.mMorningDoseQuanity.setVisibility(View.GONE);
        } else {
            holder.mRelativeLayoutShowBreakfastIcon.setVisibility(View.VISIBLE);
            holder.mShowMorningFullFormOfDose.setVisibility(View.VISIBLE);
            holder.mMorningDoseQuanity.setVisibility(View.VISIBLE);
        }
        //if current timeOfDosage is within breakfast timeOfDosage ie. 7 am to 11 am then breakfast image highlighted with circular background
        if (mGetMealTime.equals(mContext.getString(R.string.break_fast))) {
            holder.mHightLightMorningDose.setBackground(mContext.getResources().getDrawable(R.mipmap.highlight));
            holder.mDosePeriod.setText(timeOfDosage);
            holder.mDoseQuantityNumber.setText(doseQuantity);
            holder.mDoseSlot.setText(showBreakFastLabel);
            //if dose quantity is 1/2 or 1/4 etc then change textSize of respective textview.
            if (doseQuantity.contains("/")) {
                holder.mDoseQuantityNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp18));
            } else {
                holder.mDoseQuantityNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp40));
            }
        }
        //if dose quantity is 1/2 or 1/4 etc then change textSize of respective textview.
        if (doseQuantity.contains("/")) {
            holder.mMorningDoseQuanity.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp12));
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
            durationOfLunch = mContext.getString(R.string.before_lunch);
            timeOfDosage = mContext.getString(R.string.before);
            doseQuantity = prescriptionData.getLunchB();
            showBreakFastLabel = mContext.getString(R.string.lunch);
        }
        if (!prescriptionData.getLunchA().isEmpty()) {
            quantityOfDose = prescriptionData.getDosage();
            durationOfLunch = mContext.getString(R.string.after_lunch);
            timeOfDosage = mContext.getString(R.string.after);
            doseQuantity = prescriptionData.getLunchA();
            showBreakFastLabel = mContext.getString(R.string.lunch);
        }
        if (quantityOfDose.isEmpty()) {
            holder.mRelativeLayoutShowLunchIcon.setVisibility(View.GONE);
            holder.mShowAfterNoonFullFormOfDose.setVisibility(View.GONE);
            holder.mLunchDoseQuantity.setVisibility(View.GONE);

        } else {

            holder.mRelativeLayoutShowLunchIcon.setVisibility(View.VISIBLE);
            holder.mLunchDoseQuantity.setVisibility(View.VISIBLE);
            holder.mShowAfterNoonFullFormOfDose.setVisibility(View.VISIBLE);
        }
        //if current timeOfDosage is within durationOfLunch timeOfDosage ie. 11 am to 3 pm then durationOfLunch image highlighted with circular background
        if (mGetMealTime.equals(mContext.getString(R.string.mlunch))) {
            holder.mHightLightAfternoonDose.setBackground(mContext.getResources().getDrawable(R.mipmap.highlight));
            holder.mDosePeriod.setText(timeOfDosage);
            holder.mDoseQuantityNumber.setText(doseQuantity);
            holder.mDoseSlot.setText(showBreakFastLabel);
            //if dose quantity is 1/2 or 1/4 etc then change textSize of respective textview.
            if (doseQuantity.contains("/")) {
                holder.mDoseQuantityNumber.setTextSize(mContext.getResources().getDimension(R.dimen.sp18));
            } else {
                holder.mDoseQuantityNumber.setTextSize(mContext.getResources().getDimension(R.dimen.sp40));
            }
        }
        //if dose quantity is 1/2 or 1/4 etc then change textSize of respective textview.
        if (doseQuantity.contains("/")) {
            holder.mLunchDoseQuantity.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp12));
        } else {
            holder.mLunchDoseQuantity.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp16));
        }

        holder.mShowAfterNoonFullFormOfDose.setText(durationOfLunch);
        holder.mLunchDoseQuantity.setText(mContext.getString(R.string.opening_brace) + doseQuantity + " " + mContext.getString(R.string.tablet) + mContext.getString(R.string.closing_brace));

        //****************************************Dinner********************************************

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
            durationOfDinner = mContext.getString(R.string.before_dinner);
        }
        if (!prescriptionData.getDinnerA().isEmpty()) {
            timeOfDosage = mContext.getString(R.string.after);
            doseQuantity = prescriptionData.getDinnerA();
            showBreakFastLabel = mContext.getString(R.string.dinner);
            quantityOfDose = prescriptionData.getDosage();
            durationOfDinner = mContext.getString(R.string.after_dinner);
        }
        if (quantityOfDose.isEmpty()) {
            holder.mRelativeLayoutShowDinnerIcon.setVisibility(View.GONE);
            holder.mDinnerDoseQuantity.setVisibility(View.GONE);
            holder.mShowNightFullFormOfDose.setVisibility(View.GONE);
        } else {
            holder.mShowNightFullFormOfDose.setVisibility(View.VISIBLE);
            holder.mDinnerDoseQuantity.setVisibility(View.VISIBLE);
            holder.mRelativeLayoutShowDinnerIcon.setVisibility(View.VISIBLE);
            holder.mShowNightFullFormOfDose.setVisibility(View.VISIBLE);
        }
        //if current timeOfDosage is within durationOfDinner timeOfDosage ie. 7 pm to 11 pm then durationOfDinner image highlighted with circular background
        if (mGetMealTime.equals(mContext.getString(R.string.mdinner))) {
            holder.mHightLightNightDose.setBackground(mContext.getResources().getDrawable(R.mipmap.highlight));
            holder.mDosePeriod.setText(timeOfDosage);
            holder.mDoseQuantityNumber.setText(doseQuantity);
            holder.mDoseSlot.setText(showBreakFastLabel);
            //if dose quantity is 1/2 or 1/4 etc then change textSize of respective textview.
            if (doseQuantity.contains("/")) {
                holder.mDoseQuantityNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp18));
            } else {
                holder.mDoseQuantityNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp40));
            }
        }
        //if dose quantity is 1/2 or 1/4 etc then change textSize of respective textview.
        if (doseQuantity.contains("/")) {
            holder.mDinnerDoseQuantity.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp12));
        } else {
            holder.mDinnerDoseQuantity.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp16));
        }

        holder.mDinnerDoseQuantity.setText(mContext.getString(R.string.opening_brace) + doseQuantity + " " + mContext.getString(R.string.tablet) + mContext.getString(R.string.closing_brace));
        holder.mShowNightFullFormOfDose.setText(durationOfDinner);
    }

    // search function to search particular medicine .
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mPrescriptionData.clear();
        if (charText.length() == 0) {
            mPrescriptionData.addAll(mSearchListByMedicineName);
        } else {
            for (PrescriptionData s : mSearchListByMedicineName) {
                if (s.getMedicineName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mPrescriptionData.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mPrescriptionData.size();
    }

    public interface RowClickListener {
        void onRowClicked(ArrayList<PrescriptionData> dataObjects, int position, View v, String mClickCodes);
    }

    public void setRowClickListener(RowClickListener mRowClickListener) {
        this.mRowClickListener = mRowClickListener;
    }


}
