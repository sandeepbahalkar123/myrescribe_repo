package com.myrescribe.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.DataObject;
import com.myrescribe.model.prescription_response_model.PrescriptionData;
import com.myrescribe.util.Constants;

import java.util.ArrayList;

/**
 * Created by jeetal on 10/5/17.
 */

public class ShowMedicineDoseListAdapter extends RecyclerView.Adapter<ShowMedicineDoseListAdapter.ListViewHolder> {

    private ArrayList<PrescriptionData> mDataSet;
    RowClickListener mRowClickListener;
    Context mContext;
    Boolean isPatientLogin;
    private LinearLayout lv;


    public ShowMedicineDoseListAdapter(Context context, ArrayList<PrescriptionData> dataSet, Boolean isPatientLogin) {
        this.mDataSet = dataSet;
        this.mContext = context;
        this.isPatientLogin = isPatientLogin;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView mTextViewInstructions;
        TextView mTextviewNameOfMedicine;
        TextView mTextviewTabletLabel;
        TextView mButtonDelete;
        View mExpandLayout;
        CardView mCardViewLayout;
        TextView mButtonEdit;
        TextView textviewDisplayDates;
        LinearLayout mLayoutShowOnlyToDoctor;

        TextView mMorningTimeDose, mLunchTimeDose, mDinnerTimeDose;
        TextView mDays;


        public ListViewHolder(View view) {
            super(view);
            textviewDisplayDates = (TextView) view.findViewById(R.id.textviewDisplayDates);
            mButtonEdit = (TextView) view.findViewById(R.id.buttonEdit);
            mTextViewInstructions = (TextView) view.findViewById(R.id.tv_instructions);
            mTextviewNameOfMedicine = (TextView) view.findViewById(R.id.medicineName);
            mTextviewTabletLabel = (TextView) view.findViewById(R.id.textviewTabletLabel);
            mButtonDelete = (TextView) view.findViewById(R.id.buttonDelete);
            mExpandLayout = (View) view.findViewById(R.id.expandPrescriptionView);
            mCardViewLayout = (CardView) view.findViewById(R.id.card_view);
            mLayoutShowOnlyToDoctor = (LinearLayout) view.findViewById(R.id.linearlayout_showOnlytoDoctor);

            // Dose Time
            mMorningTimeDose = (TextView) view.findViewById(R.id.morningTimeDose);
            mLunchTimeDose = (TextView) view.findViewById(R.id.lunchTimeDose);
            mDinnerTimeDose = (TextView) view.findViewById(R.id.dinnerTimeDose);
            // Dose Time
            mDays = (TextView) view.findViewById(R.id.days);


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
        /*holder.mTextviewNameOfTablet.setText(movie.getmText1());*/

        if (isPatientLogin) {
            holder.mLayoutShowOnlyToDoctor.setVisibility(View.GONE);
        } else {
            holder.mLayoutShowOnlyToDoctor.setVisibility(View.VISIBLE);
        }

        if (dataObject.getExpanded()) {
            holder.mExpandLayout.setVisibility(View.VISIBLE);
            holder.textviewDisplayDates.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.uparrow, 0);
        } else {
            holder.mExpandLayout.setVisibility(View.GONE);
            holder.textviewDisplayDates.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down, 0);
        }

        holder.mCardViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (PrescriptionData object : mDataSet) {
                    object.setExpanded(false);
                }
                if (holder.mExpandLayout.getVisibility() == View.GONE) {
                    holder.mExpandLayout.setVisibility(View.VISIBLE);
                    dataObject.setExpanded(true);
                } else {
                    holder.mExpandLayout.setVisibility(View.GONE);
                    dataObject.setExpanded(false);
                }
                notifyDataSetChanged();

            }
        });
        holder.mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRowClickListener != null) {
                    mRowClickListener.onRowClicked(mDataSet, position, v, Constants.CLICK_EDIT);
                }
            }
        });

        holder.mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRowClickListener != null) {
                    mRowClickListener.onRowClicked(mDataSet, position, v, Constants.CLICK_DELETE);
                }
            }
        });

        //----- set data
        holder.mTextviewNameOfMedicine.setText("" + dataObject.getMedicineName() + " (" + dataObject.getDosage() + ")");
        holder.mTextViewInstructions.setText("" + dataObject.getInstruction());

        if (Integer.parseInt(dataObject.getDays()) > 1) {
            holder.mDays.setText("" + dataObject.getDays() + " days");
        } else {
            holder.mDays.setText("" + dataObject.getDays() + " day");
        }
        //+++++++++
        String data = "";
        //::::::::::::::::::::::
        if (!dataObject.getMorningB().isEmpty()) {
            data = data.concat(dataObject.getMorningB() + "BB");
        }
        if (!dataObject.getMorningA().isEmpty()) {
            data = data.concat(dataObject.getMorningA() + "AB");
        }
        if (data.isEmpty()) {
            holder.mMorningTimeDose.setVisibility(View.GONE);
        } else {
            holder.mMorningTimeDose.setVisibility(View.VISIBLE);
        }
        holder.mMorningTimeDose.setText(data);
        //::::::::::::::::::::::
        data = "";
        //::::::::::::::::::::::
        if (!dataObject.getLunchB().isEmpty()) {
            data = data.concat(dataObject.getLunchB() + "BL");
        }
        if (!dataObject.getLunchA().isEmpty()) {
            data = data.concat(" " + dataObject.getLunchA() + "AL");
        }
        if (data.isEmpty()) {
            holder.mLunchTimeDose.setVisibility(View.GONE);
        } else {
            holder.mLunchTimeDose.setVisibility(View.VISIBLE);
        }
        holder.mLunchTimeDose.setText(data);
        //::::::::::::::::::::::
        data = "";
        //::::::::::::::::::::::
        if (!dataObject.getDinnerB().isEmpty()) {
            data = data.concat(dataObject.getDinnerB() + "BD");
        }
        if (!dataObject.getDinnerA().isEmpty()) {
            data = data.concat(dataObject.getDinnerA() + "AD");
        }
        if (data.isEmpty()) {
            holder.mDinnerTimeDose.setVisibility(View.GONE);
        } else {
            holder.mDinnerTimeDose.setVisibility(View.VISIBLE);
        }
        holder.mDinnerTimeDose.setText(data);
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
