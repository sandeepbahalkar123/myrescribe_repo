package com.myrescribe.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.activities.EditPrescription;
import com.myrescribe.model.DataObject;
import com.myrescribe.util.Constants;

import java.util.ArrayList;

/**
 * Created by jeetal on 10/5/17.
 */

public class ShowMedicineDoseListAdapter extends RecyclerView.Adapter<ShowMedicineDoseListAdapter.MyViewHolder> {

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObject> mDataset;
    MyClickListner customListner;
    Context mContext;
    Boolean isPatientLogin;
    private LinearLayout lv;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTextviewNameOfTablet;
        TextView mTextviewTabletLabel;
        TextView mButtonDelete;
        View mExpandLayout;
        CardView mCardViewLayout;
        TextView mButtonEdit;
        TextView textviewDisplayDates;
        LinearLayout mLayoutshowOnlytoDoctor;



        public MyViewHolder(View view) {
            super(view);
            textviewDisplayDates = (TextView) view.findViewById(R.id.textviewDisplayDates);
            mButtonEdit = (TextView) view.findViewById(R.id.buttonEdit);
            mTextviewNameOfTablet = (TextView) view.findViewById(R.id.textviewNameOfTablet);
            mTextviewTabletLabel = (TextView) view.findViewById(R.id.textviewTabletLabel);
            mButtonDelete = (TextView) view.findViewById(R.id.buttonDelete);
            mExpandLayout = (View) view.findViewById(R.id.expandPrescriptionView);
            mCardViewLayout = (CardView)view.findViewById(R.id.card_view);
            mLayoutshowOnlytoDoctor = (LinearLayout)view.findViewById(R.id.linearlayout_showOnlytoDoctor);

        }
    }


    public ShowMedicineDoseListAdapter(Context context, ArrayList<DataObject> myDataset, Boolean isPatientLogin) {
        mDataset = myDataset;
        this.mContext = context;
        this.isPatientLogin = isPatientLogin;

    }

    @Override
    public ShowMedicineDoseListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.medicine_prescribtion_activity, parent, false);


        return new ShowMedicineDoseListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShowMedicineDoseListAdapter.MyViewHolder holder, final int position) {
        final DataObject movie = mDataset.get(position);
        /*holder.mTextviewNameOfTablet.setText(movie.getmText1());*/

        if(isPatientLogin){
            holder.mLayoutshowOnlytoDoctor.setVisibility(View.GONE);
        }else{
            holder.mLayoutshowOnlytoDoctor.setVisibility(View.VISIBLE);
        }

        if (movie.getExpanded()) {
            holder.mExpandLayout.setVisibility(View.VISIBLE);
            holder.textviewDisplayDates.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.uparrow, 0);
        } else {
            holder.mExpandLayout.setVisibility(View.GONE);
            holder.textviewDisplayDates.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down, 0);
        }

        holder.mCardViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (DataObject object : mDataset) {
                    object.setExpanded(false);

                }
                if(holder.mExpandLayout.getVisibility()==View.GONE){
                    holder.mExpandLayout.setVisibility(View.VISIBLE);
                    movie.setExpanded(true);
                }else {
                    holder.mExpandLayout.setVisibility(View.GONE);
                    movie.setExpanded(false);
                }
               notifyDataSetChanged();

            }
        });
        holder.mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonClick(mDataset,position,v,Constants.CLICK_EDIT);
                }
            }
        });

      holder.mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonClick(mDataset,position,v,Constants.CLICK_DELETE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListner {
        void onButtonClick(ArrayList<DataObject> dataObjects, int position, View v, String mClickCodes);
        }

    public void setMyClickListner(MyClickListner listener) {
        this.customListner = listener;
        }
     }
