package com.myrescribe.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.doctors.DoctorDetail;
import com.myrescribe.ui.customesViews.CustomTextView;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by root on 22/6/16.
 */
public class DoctorListAdapter extends ArrayAdapter<DoctorDetail> {
    private final String TAG = getClass().getName();
    private SimpleDateFormat mDateFormat;
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
        mDateFormat = new SimpleDateFormat(MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY);

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

        holder.doctorName.setText(dataObject.getDoctorName());

        if (dataObject.isStartElement()) {
            //----
            Date date = CommonMethods.convertStringToDate(dataObject.getDate(), MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            //----
            int day = cal.get(Calendar.DAY_OF_MONTH);

            String toDisplay = day + "<sup>" + CommonMethods.getSuffixForNumber(day) + "</sup>";
            //------
            if (dataObject.getDate().equalsIgnoreCase(mDateFormat.format(new Date()))) {
                toDisplay = toDisplay + "\n" + mContext.getString(R.string.just_now);
            }
            //------
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                holder.date.setText(Html.fromHtml(toDisplay, Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.date.setText(Html.fromHtml(toDisplay));
            }

            holder.date.setVisibility(View.VISIBLE);

            holder.circularBulletMainElement.setVisibility(View.VISIBLE);
            holder.circularBulletChildElement.setVisibility(View.GONE);

        } else {
            holder.date.setVisibility(View.INVISIBLE);
            holder.date.setVisibility(View.INVISIBLE);
            holder.circularBulletChildElement.setVisibility(View.VISIBLE);
            holder.circularBulletMainElement.setVisibility(View.GONE);

        }

        holder.parentDataContainer.setBackgroundColor(dataObject.getRowColor());

        if (position == 0)
            holder.upperLine.setVisibility(View.INVISIBLE);
        else {
            holder.upperLine.setVisibility(View.VISIBLE);
        }

        //---
        if (position == mDataList.size() - 1)
            holder.lowerLine.setVisibility(View.INVISIBLE);
        else {
            holder.lowerLine.setVisibility(View.VISIBLE);
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

