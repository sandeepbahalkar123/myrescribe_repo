package com.myrescribe.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.doctors.DoctorDetail;
import com.myrescribe.ui.customesViews.CircularImageView;
import com.myrescribe.ui.customesViews.ReadMoreTextView;

import java.util.ArrayList;


/**
 * Created by root on 22/6/16.
 */
public class DoctorListAdapter extends ArrayAdapter<DoctorDetail> {
    private final String TAG = getClass().getName();
    Context mContext;
    int layoutResourceId;
    ArrayList<DoctorDetail> mDataList;

    public DoctorListAdapter(Context context, int layoutResourceId, ArrayList<DoctorDetail> dataList) {
        super(context, layoutResourceId, dataList);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        mDataList = dataList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DataHolder holder = null;

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

            holder.circularBullet.setVisibility(View.GONE);

            //    holder.circularBullet.setVisibility(View.GONE);

        }

        return row;
    }

    static class DataHolder {
        TextView date;
        CircularImageView circularBullet;
        ReadMoreTextView doctorName;
        TextView doctorID;
        //   TextView buttonMoreOrLess;
    }


}

