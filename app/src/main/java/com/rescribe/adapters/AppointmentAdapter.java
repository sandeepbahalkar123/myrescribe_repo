package com.rescribe.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
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

    private String mCurrentDate;
    private String mAppointmentType;
    private Context mContext;
    private ArrayList<AptList> appointmentsList;
    private int imageSize;


    public AppointmentAdapter(final Context mContext, ArrayList<AptList> appointmentsList, String appointmentType) {

        this.mAppointmentType = appointmentType;
        this.appointmentsList = appointmentsList;
        this.mContext = mContext;
        DateFormat dateFormat = new SimpleDateFormat(RescribeConstants.DATE_PATTERN.YYYY_MM_DD, Locale.US);
        mCurrentDate = dateFormat.format(new Date());
        setColumnNumber(mContext, 2);
    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = (widthPixels / columnNum) - CommonMethods.convertDpToPixel(30);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment_list, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        AptList appointment = appointmentsList.get(position);

        holder.doctorName.setText(appointment.getDoctorName());
        holder.doctorType.setText(appointment.getSpecialization());

        //--- For address
        if (!mAppointmentType.equalsIgnoreCase(mContext.getString(R.string.completed))) {
            Date timeStamp = CommonMethods.convertStringToDate(appointment.getAptDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
            Calendar cal = Calendar.getInstance();
            cal.setTime(timeStamp);
            String toDisplay = cal.get(Calendar.DAY_OF_MONTH) + "<sup>" + CommonMethods.getSuffixForNumber(cal.get(Calendar.DAY_OF_MONTH)) + "</sup>" + new SimpleDateFormat("MMM").format(cal.getTime());
            //------
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                holder.doctorAddress.setText(Html.fromHtml(toDisplay + ", " + appointment.getAddress(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.doctorAddress.setText(Html.fromHtml(toDisplay + ", " + appointment.getAddress()));
            }
        } else {
            holder.doctorAddress.setText(appointment.getAddress());
        }
        //--- For address

        //---For Date
        String timeToShow = CommonMethods.formatDateTime(appointment.getAptDate(), RescribeConstants.DATE_PATTERN.hh_mm_a,
                RescribeConstants.DATE_PATTERN.YYYY_MM_DD_hh_mm_a, RescribeConstants.DATE).toLowerCase();
        if (mAppointmentType.equalsIgnoreCase(mContext.getString(R.string.completed))) {
            String timeStamp = CommonMethods.formatDateTime(appointment.getAptDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD,
                    RescribeConstants.DATE_PATTERN.YYYY_MM_DD_hh_mm_a, RescribeConstants.DATE);
            String dayFromDate = CommonMethods.getDayFromDate(RescribeConstants.DATE_PATTERN.YYYY_MM_DD, timeStamp);
            if (mCurrentDate.equalsIgnoreCase(timeStamp)) {// for Current date
                holder.appointmentsTimeStamp.setText(timeToShow);
            } else if (dayFromDate.equalsIgnoreCase("Yesterday")) { // for Yesterday date
                holder.appointmentsTimeStamp.setText("" + android.text.format.DateFormat.format("EEE", CommonMethods.convertStringToDate(appointment.getAptDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD_hh_mm_a)));
            } else { // for date before yesterday
                holder.appointmentsTimeStamp.setText("" + CommonMethods.formatDateTime(appointment.getAptDate(), RescribeConstants.DATE_PATTERN.DD_MM,
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
                    intent.putExtra(mContext.getString(R.string.address), appointment1.getAddress());
                    //intent.putExtra(mContext.getString(R.string.longitude), appointment1.getLongitude());
                    mContext.startActivity(intent);
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
        requestOptions.placeholder(R.drawable.layer_12);

        Glide.with(mContext)
                .load(appointment.getImageUrl())
                .apply(requestOptions).thumbnail(0.5f)
                .into(holder.mImageURL);
        //--------------

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

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }


}