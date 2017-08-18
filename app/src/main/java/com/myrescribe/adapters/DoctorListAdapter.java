package com.myrescribe.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.doctors.doctor_info.DoctorDetail;
import com.myrescribe.ui.activities.OneDayVisitActivity;
import com.myrescribe.ui.customesViews.CircularImageView;
import com.myrescribe.ui.customesViews.CustomTextView;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 11/7/17.
 */

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.ListViewHolder> {

    private final String TAG = getClass().getName();

    Context mContext;
    ArrayList<DoctorDetail> mDataList;
    private SimpleDateFormat mDateFormat;


    public DoctorListAdapter(Context context, ArrayList<DoctorDetail> dataList) {

        this.mContext = context;
        mDataList = dataList;
        mDateFormat = new SimpleDateFormat(MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, Locale.US);

    }

    @Override
    public DoctorListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_doctor_list_layout, parent, false);
        return new DoctorListAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DoctorListAdapter.ListViewHolder holder, final int position) {

        final DoctorDetail dataObject = mDataList.get(position);

        if (dataObject.isStartElement()) {

            Date date = CommonMethods.convertStringToDate(dataObject.getDate(), MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD);

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

        // Glide.with(mContext).load(dataObject.getDocImg()).into(holder.docProfileImage);
        holder.doctorName.setText(dataObject.getDoctorName());
        holder.doctorAddress.setText(dataObject.getAddress());
        holder.doctorType.setText(dataObject.getSpecialization());

        holder.parentDataContainer.setBackgroundColor(dataObject.getRowColor());
        holder.sideBarView.setBackgroundColor(dataObject.getSideBarViewColor());

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
        holder.mClickOnDoctorVisitLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeToShow = CommonMethods.formatDateTime(dataObject.getDate(), MyRescribeConstants.DATE_PATTERN.MMM_YYYY,
                        MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD, MyRescribeConstants.DATE).toLowerCase();
                Date date = CommonMethods.convertStringToDate(dataObject.getDate(), MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                timeToShow = timeToShow.substring(0, 1).toUpperCase() + timeToShow.substring(1);
                String toDisplay = cal.get(Calendar.DAY_OF_MONTH) + "<sup>" + CommonMethods.getSuffixForNumber(cal.get(Calendar.DAY_OF_MONTH)) + "</sup> " + timeToShow;
                Intent intent = new Intent(mContext, OneDayVisitActivity.class);
                intent.putExtra(mContext.getString(R.string.name), dataObject.getDoctorName());
                intent.putExtra(mContext.getString(R.string.specialization), dataObject.getSpecialization());
                intent.putExtra(mContext.getString(R.string.address), dataObject.getAddress());
                intent.putExtra(mContext.getString(R.string.one_day_visit_date), toDisplay);
                intent.putExtra(mContext.getString(R.string.opd_id), dataObject.getOpdId());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        CustomTextView date;

        @BindView(R.id.clickOnDoctorVisitLinearLayout)
        LinearLayout mClickOnDoctorVisitLinearLayout;

        @BindView(R.id.circularBulletChildElement)
        ImageView circularBulletChildElement;
        @BindView(R.id.docProfileImage)
        CircularImageView docProfileImage;
        @BindView(R.id.circularBulletMainElement)
        ImageView circularBulletMainElement;

        @BindView(R.id.upperLine)
        TextView upperLine;
        @BindView(R.id.lowerLine)
        TextView lowerLine;

        @BindView(R.id.doctorName)
        CustomTextView doctorName;
        @BindView(R.id.doctorType)
        TextView doctorType;
        @BindView(R.id.doctorAddress)
        TextView doctorAddress;

        @BindView(R.id.parentDataContainer)
        LinearLayout parentDataContainer;
        @BindView(R.id.sideBarView)
        TextView sideBarView;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

}

