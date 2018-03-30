package com.rescribe.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.model.doctors.appointments.AptList;
import com.rescribe.ui.activities.MapsActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.NetworkUtil;
import com.rescribe.util.RescribeConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ListViewHolder> {


    private String mAppointmentType;
    private Context mContext;
    private ArrayList<AptList> appointmentsList;
    private int imageSize;
    private Bundle bundleData;
    private OnClickOfAppointmentClickListener mOnClickOfAppointmentClickListener;


    public AppointmentAdapter(final Context mContext, ArrayList<AptList> appointmentsList, String appointmentType, OnClickOfAppointmentClickListener mOnClickOfAppointmentClickListener) {

        this.mAppointmentType = appointmentType;
        this.appointmentsList = appointmentsList;
        this.mContext = mContext;
        this.mOnClickOfAppointmentClickListener = mOnClickOfAppointmentClickListener;
        setColumnNumber(mContext, 2);
    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = (widthPixels / columnNum) - mContext.getResources().getDimensionPixelSize(R.dimen.dp30);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment_list, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        final AptList appointment = appointmentsList.get(position);

        holder.doctorName.setText("Dr. " + appointment.getDoctorName());
        holder.doctorType.setText(appointment.getSpecialization());

        //--- For address
        if (!mAppointmentType.equalsIgnoreCase(mContext.getString(R.string.completed))) {
            Date timeStamp = CommonMethods.convertStringToDate(appointment.getAptDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
            Calendar cal = Calendar.getInstance();
            cal.setTime(timeStamp);
            String toDisplay = cal.get(Calendar.DAY_OF_MONTH) + "<sup>" + CommonMethods.getSuffixForNumber(cal.get(Calendar.DAY_OF_MONTH)) + "</sup> " + new SimpleDateFormat("MMM yy", Locale.US).format(cal.getTime());
            //------
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.doctorAddress.setText(Html.fromHtml(toDisplay + ", " + appointment.getArea_name() + ", " + appointment.getCity_name(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.doctorAddress.setText(Html.fromHtml(toDisplay + ", " + appointment.getArea_name() + ", " + appointment.getCity_name()));
            }
        } else {
            holder.doctorAddress.setText(appointment.getArea_name() + ", " + appointment.getCity_name());
        }
        //--- For address

        //---For Date
        String timeToShow = CommonMethods.formatDateTime(appointment.getAptDate(), RescribeConstants.DATE_PATTERN.hh_mm_a,
                RescribeConstants.DATE_PATTERN.YYYY_MM_DD_hh_mm_a, RescribeConstants.DATE).toLowerCase();
        if (mAppointmentType.equalsIgnoreCase(mContext.getString(R.string.completed))) {
            String timeStamp = CommonMethods.formatDateTime(appointment.getAptDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD,
                    RescribeConstants.DATE_PATTERN.YYYY_MM_DD_hh_mm_a, RescribeConstants.DATE);

            DateFormat dateFormat = new SimpleDateFormat(RescribeConstants.DATE_PATTERN.YYYY_MM_DD, Locale.US);
            String mCurrentDate = dateFormat.format(new Date());

            if (mCurrentDate.equalsIgnoreCase(timeStamp)) {// for Current date
                holder.appointmentsTimeStamp.setText(timeToShow);
            } else if (CommonMethods.getCalculatedDate(RescribeConstants.DATE_PATTERN.YYYY_MM_DD, -1).equals(timeStamp)) { // for Yesterday date
                holder.appointmentsTimeStamp.setText("Yest");
            } else if (getWeekDays().contains(timeStamp)) { // for date before yesterday
                holder.appointmentsTimeStamp.setText(android.text.format.DateFormat.format(RescribeConstants.DATE_PATTERN.EEE, CommonMethods.convertStringToDate(appointment.getAptDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD_hh_mm_a)));
            } else {
                holder.appointmentsTimeStamp.setText(CommonMethods.formatDateTime(appointment.getAptDate(), RescribeConstants.DATE_PATTERN.DD_MM_YY,
                        RescribeConstants.DATE_PATTERN.YYYY_MM_DD_hh_mm_a, RescribeConstants.DATE));
            }
        } else {
            holder.appointmentsTimeStamp.setText("" + timeToShow);
        }

        holder.mGmapLocationView.setTag("" + position);
        holder.mGmapLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.isInternetAvailable(mContext)) {
                    AptList appointment1 = appointmentsList.get(Integer.parseInt("" + v.getTag()));
                    Intent intent = new Intent(mContext, MapsActivity.class);
                    intent.putExtra(mContext.getString(R.string.address), appointment1.getClinicAddress());
                    intent.putExtra(RescribeConstants.DOCTOR_NAME, appointment1.getDoctorName());
                    intent.putExtra(RescribeConstants.RATING, appointment1.getRating());
                    mContext.startActivity(intent);
                    //intent.putExtra(mContext.getString(R.string.longitude), appointment1.getLosngitude());

                } else {
                    CommonMethods.showToast(mContext, mContext.getString(R.string.internet));
                }
            }
        });

        //-------Load image-------
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        requestOptions.override(imageSize, imageSize);

        Glide.with(mContext)
                .load(appointment.getImageUrl())
                .apply(requestOptions).thumbnail(0.5f)
                .into(holder.mImageURL);
        //--------------
        if (mAppointmentType.equalsIgnoreCase(mContext.getString(R.string.upcoming))) {
            holder.appointmentItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickOfAppointmentClickListener.setOnClickOfAppointmentLayout(appointment);

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.doctorName)
        CustomTextView doctorName;
        @BindView(R.id.doctorType)
        TextView doctorType;
        @BindView(R.id.doctorAddress)
        TextView doctorAddress;
        @BindView(R.id.gMapLocationView)
        ImageView mGmapLocationView;
        @BindView(R.id.imageURL)
        ImageView mImageURL;
        @BindView(R.id.appointmentsTimeStamp)
        TextView appointmentsTimeStamp;
        @BindView(R.id.appointmentItemLayout)
        LinearLayout appointmentItemLayout;
        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    private ArrayList<String> getWeekDays() {
        ArrayList<String> preWeekDays = new ArrayList<>();
        for (int i = -2; i > -6; i--)
            preWeekDays.add(CommonMethods.getCalculatedDate(RescribeConstants.DATE_PATTERN.YYYY_MM_DD, i));
        return preWeekDays;
    }

    public interface OnClickOfAppointmentClickListener {
        void setOnClickOfAppointmentLayout(AptList mAptListObject);
    }

}