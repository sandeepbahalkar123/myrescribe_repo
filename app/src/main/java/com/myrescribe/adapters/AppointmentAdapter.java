package com.myrescribe.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.doctors.appointments.DoctorAppointment;
import com.myrescribe.ui.activities.MapsActivity;
import com.myrescribe.ui.customesViews.CustomTextView;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

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
    private ArrayList<DoctorAppointment> appointmentsList;


    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.doctorName)
        CustomTextView doctorName;
        @BindView(R.id.doctorType)
        TextView doctorType;
        @BindView(R.id.doctorAddress)
        TextView doctorAddress;

        @BindView(R.id.gMapLocationView)
        ImageView mGmapLocationView;
        @BindView(R.id.appointmentsTimeStamp)
        TextView appointmentsTimeStamp;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }


    public AppointmentAdapter(Context mContext, ArrayList<DoctorAppointment> appointmentsList, String appointmentType) {
        this.mAppointmentType = appointmentType;
        this.appointmentsList = appointmentsList;
        this.mContext = mContext;
        DateFormat dateFormat = new SimpleDateFormat(MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD, Locale.US);
        mCurrentDate = dateFormat.format(new Date());
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment_list, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        DoctorAppointment appointment = appointmentsList.get(position);

        holder.doctorName.setText(appointment.getDoctorName());
        holder.doctorType.setText(appointment.getSpecialization());

        //--- For address
        if (mAppointmentType.equalsIgnoreCase(mContext.getString(R.string.upcoming))) {
            Date timeStamp = CommonMethods.convertStringToDate(appointment.getTimeStamp(), MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD);
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
        String timeToShow = CommonMethods.formatDateTime(appointment.getTimeStamp(), MyRescribeConstants.DATE_PATTERN.hh_mm_a,
                MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD_hh_mm_a, MyRescribeConstants.DATE).toLowerCase();
        if (mAppointmentType.equalsIgnoreCase(mContext.getString(R.string.completed))) {
            String timeStamp = CommonMethods.formatDateTime(appointment.getTimeStamp(), MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD,
                    MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD_hh_mm_a, MyRescribeConstants.DATE);
            String dayFromDate = CommonMethods.getDayFromDate(MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD, timeStamp);
            if (mCurrentDate.equalsIgnoreCase(timeStamp)) {// for Current date
                holder.appointmentsTimeStamp.setText(timeToShow);
            } else if (dayFromDate.equalsIgnoreCase("Yesterday")) { // for Yesterday date
                holder.appointmentsTimeStamp.setText("" + android.text.format.DateFormat.format("EEE", CommonMethods.convertStringToDate(appointment.getTimeStamp(), MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD_hh_mm_a)));
            } else { // for date before yesterday
                holder.appointmentsTimeStamp.setText("" + CommonMethods.formatDateTime(appointment.getTimeStamp(), MyRescribeConstants.DATE_PATTERN.DD_MM,
                        MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD_hh_mm_a, MyRescribeConstants.DATE));
            }
        } else {
            holder.appointmentsTimeStamp.setText("" + timeToShow);
        }

        holder.mGmapLocationView.setTag("" + position);
        holder.mGmapLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorAppointment appointment1 = appointmentsList.get(Integer.parseInt("" + v.getTag()));
                Intent intent = new Intent(mContext, MapsActivity.class);
                intent.putExtra(mContext.getString(R.string.latitude), appointment1.getLatitude());
                intent.putExtra(mContext.getString(R.string.longitude), appointment1.getLongitude());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }
}