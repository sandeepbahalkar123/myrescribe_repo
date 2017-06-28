package com.myrescribe.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.doctors.DoctorDetail;
import com.myrescribe.ui.customesViews.CircularImageView;
import com.myrescribe.ui.customesViews.ReadMoreTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by root on 22/6/16.
 */
public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.ListViewHolder> {
    private final String TAG = getClass().getName();
    Context mContext;
    int layoutResourceId;
    ArrayList<DoctorDetail> mDataList;

    public DoctorListAdapter(Context context, int layoutResourceId, ArrayList<DoctorDetail> dataList) {

        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        mDataList = dataList;
    }


   /* public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ListViewHolder holder = null;

        if (row == null) {
            holder = new DataHolder();
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder.date = (TextView) row.findViewById(R.id.date);
            holder.doctorName = (ReadMoreTextView) row.findViewById(R.id.doctorName);
            holder.doctorID = (TextView) row.findViewById(R.id.doctorID);
            holder.circularBullet = (CircularImageView) row.findViewById(R.id.circularBullet);
            // holder.buttonMoreOrLess = (TextView) row.findViewById(R.id.button_toggle);

            row.setTag(holder);
        } else {
            holder = (DataHolder) row.getTag();
        }

        DoctorDetail dataObject = mDataList.get(position);

        holder.doctorName.setText(dataObject.getName());
        holder.doctorID.setText("" + dataObject.getId());
        holder.doctorName.setTrimLength(5);

        if (dataObject.getIsStartElement()) {
            holder.date.setText(dataObject.getRespectiveDate());
            holder.date.setVisibility(View.VISIBLE);
            holder.circularBullet.setVisibility(View.VISIBLE);

        } else {
            holder.date.setVisibility(View.INVISIBLE);
        //    holder.circularBullet.setVisibility(View.GONE);
        }

        return row;
    }*/

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_doctor_list, parent, false);
        return new DoctorListAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        DoctorDetail dataObject = mDataList.get(position);
        holder.doctorName.setText(dataObject.getName());
        holder.doctorID.setText("" + dataObject.getId());
        holder.doctorName.setTrimLength(5);

        if (dataObject.getIsStartElement()) {
            holder.date.setText(dataObject.getRespectiveDate());
            holder.date.setVisibility(View.VISIBLE);
            holder.circularBullet.setVisibility(View.VISIBLE);

        } else {
            holder.date.setVisibility(View.INVISIBLE);
            //    holder.circularBullet.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return 0;
    }


    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.circularBullet)
        CircularImageView circularBullet;
        @BindView(R.id.doctorName)
        ReadMoreTextView doctorName;
        @BindView(R.id.doctorID)
        TextView doctorID;
        View view;


        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }


}

