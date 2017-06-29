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

            holder.circularBulletMainElement = (ImageView) row.findViewById(R.id.circularBulletMainElement);
            holder.circularBulletChildElement = (ImageView) row.findViewById(R.id.circularBulletChildElement);

            holder.parentDataContainer = (LinearLayout) row.findViewById(R.id.parentDataContainer);
            holder.upperLine = (TextView) row.findViewById(R.id.upperLine);
            holder.lowerLine = (TextView) row.findViewById(R.id.lowerLine);
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

            holder.circularBulletMainElement.setVisibility(View.VISIBLE);
            holder.circularBulletChildElement.setVisibility(View.GONE);
        } else {
            holder.date.setVisibility(View.INVISIBLE);
            holder.date.setVisibility(View.INVISIBLE);
            holder.circularBulletChildElement.setVisibility(View.VISIBLE);
            holder.circularBulletMainElement.setVisibility(View.GONE);
            //---
            if (position == mDataList.size() - 1)
                holder.lowerLine.setVisibility(View.INVISIBLE);
            else {
                holder.lowerLine.setVisibility(View.VISIBLE);
            }
        }

        if (position == 0)
            holder.upperLine.setVisibility(View.INVISIBLE);
        else {
            holder.upperLine.setVisibility(View.VISIBLE);
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

        ImageView circularBulletChildElement, circularBulletMainElement;
        TextView upperLine, lowerLine;
        CustomTextView doctorName;
        LinearLayout parentDataContainer;
        //   TextView buttonMoreOrLess;
    }


}

