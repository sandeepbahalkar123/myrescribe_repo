package com.myrescribe.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
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
import com.myrescribe.ui.customesViews.CustomTextView;
import com.myrescribe.ui.customesViews.ReadMoreTextView;

import java.util.ArrayList;


/**
 * Created by root on 22/6/16.
 */
public class DoctorListAdapter extends ArrayAdapter<DoctorDetail> {
    private final String TAG = getClass().getName();
    private int mParentDataContainerBackground;
    Context mContext;
    int layoutResourceId;
    ArrayList<DoctorDetail> mDataList;

    public DoctorListAdapter(Context context, int layoutResourceId, ArrayList<DoctorDetail> dataList) {
        super(context, layoutResourceId, dataList);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        mDataList = dataList;
        mParentDataContainerBackground = ContextCompat.getColor(mContext, R.color.gray_93);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DataHolder holder = null;

        if (row == null) {
            holder = new DataHolder();
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder.date = (CustomTextView) row.findViewById(R.id.date);
            holder.doctorName = (CustomTextView) row.findViewById(R.id.doctorName);
            holder.circularBullet = (ImageView) row.findViewById(R.id.circularBullet);
            holder.parentDataContainer = (LinearLayout) row.findViewById(R.id.parentDataContainer);
            // holder.buttonMoreOrLess = (TextView) row.findViewById(R.id.button_toggle);

            row.setTag(holder);
        } else {
            holder = (DataHolder) row.getTag();
        }

        DoctorDetail dataObject = mDataList.get(position);

        holder.doctorName.setText(dataObject.getName());

        if (dataObject.getIsStartElement()) {
            holder.date.setText(dataObject.getRespectiveDate());
            holder.date.setVisibility(View.VISIBLE);
            holder.circularBullet.setVisibility(View.VISIBLE);
            holder.circularBullet.setImageResource(R.drawable.blue_dot);

        } else {
            holder.date.setVisibility(View.INVISIBLE);
            holder.circularBullet.setImageResource(R.drawable.small_blue_dot);
            holder.circularBullet.setVisibility(View.VISIBLE);
            //    holder.circularBullet.setVisibility(View.GONE);
        }

       /* if (holder.parentDataContainer.getTag() == null) {
            holder.parentDataContainer.setTag((Integer) mParentDataContainerBackground);
            holder.parentDataContainer.setBackgroundColor(mParentDataContainerBackground);
        } else {
            holder.parentDataContainer.setBackgroundColor((Integer) holder.parentDataContainer.getTag());
        }
*/
        return row;
    }

    static class DataHolder {
        CustomTextView date;
        ImageView circularBullet;
        CustomTextView doctorName;
        LinearLayout parentDataContainer;
        //   TextView buttonMoreOrLess;
    }


}

