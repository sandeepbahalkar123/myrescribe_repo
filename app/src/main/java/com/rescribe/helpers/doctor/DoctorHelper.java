package com.rescribe.helpers.doctor;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.doctors.appointments.DoctorAppointmentModel;
import com.rescribe.model.doctors.doctor_info.DoctorDetail;
import com.rescribe.model.doctors.doctor_info.DoctorInfoMonthContainer;
import com.rescribe.model.doctors.doctor_info.DoctorModel;
import com.rescribe.model.filter.filter_request.DrFilterRequestModel;
import com.rescribe.network.ConnectRequest;
import com.rescribe.network.ConnectionFactory;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by riteshpandhurkar on 1/3/17.
 */

public class DoctorHelper implements ConnectionListener {

    String TAG = this.getClass().getName();
    Context mContext;
    HelperResponse mHelperResponseManager;
    private Map<String, Map<String, ArrayList<DoctorDetail>>> yearWiseSortedDoctorList = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public DoctorHelper(Context context) {
        this.mContext = context;
        this.mHelperResponseManager = (HelperResponse) context;
    }

    public DoctorHelper(Context context, HelperResponse loginActivity) {
        this.mContext = context;
        this.mHelperResponseManager = loginActivity;
    }


    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag == RescribeConstants.TASK_DOCTOR_LIST) {
                    DoctorModel model = (DoctorModel) customResponse;
                    if (model.getDoctorInfoMonthContainer() != null) {
                        DoctorInfoMonthContainer doctorInfoMonthContainer = model.getDoctorInfoMonthContainer();
                        yearWiseSortedDoctorList.put(doctorInfoMonthContainer.getYear(), doctorInfoMonthContainer.getMonthWiseSortedDoctorList());
                    }
                    mHelperResponseManager.onSuccess(mOldDataTag, model);
                } else if (mOldDataTag == RescribeConstants.TASK_DOCTOR_APPOINTMENT) {
                    DoctorAppointmentModel doctorAppointmentModel = (DoctorAppointmentModel) customResponse;
                    mHelperResponseManager.onSuccess(mOldDataTag, doctorAppointmentModel);
                } else if (mOldDataTag == RescribeConstants.TASK_DOCTOR_LIST_FILTERING) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                }
                break;
            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG, "parse error");
                mHelperResponseManager.onParseError(mOldDataTag, "parse error");
                break;
            case ConnectionListener.SERVER_ERROR:
                CommonMethods.Log(TAG, "server error");
                mHelperResponseManager.onServerError(mOldDataTag, "server error");

                break;
            case ConnectionListener.NO_INTERNET:
                CommonMethods.Log(TAG, "no connection error");
                mHelperResponseManager.onNoConnectionError(mOldDataTag, "no connection error");

                break;
            case ConnectionListener.NO_CONNECTION_ERROR:
                CommonMethods.Log(TAG, "no connection error");
                mHelperResponseManager.onNoConnectionError(mOldDataTag, "no connection error");

                break;
            default:
                CommonMethods.Log(TAG, "default error");
                break;
        }
    }

    @Override
    public void onTimeout(ConnectRequest request) {

    }


    public Map<String, Map<String, ArrayList<DoctorDetail>>> getYearWiseSortedDoctorList() {
        return yearWiseSortedDoctorList;
    }

    public ArrayList<DoctorDetail> getFormattedDoctorList(String month, Map<String, ArrayList<DoctorDetail>> monthList) {

        ArrayList<DoctorDetail> doctorDetails = monthList.get(month);
        ArrayList<DoctorDetail> map = new ArrayList<>();

        if (doctorDetails != null) {
            //-----------
            TreeSet<String> dateHashSet = new TreeSet<String>(new DateWiseComparator());
            for (DoctorDetail data :
                    doctorDetails) {
                dateHashSet.add(data.getDate());
            }
            //-----------
            int color = ContextCompat.getColor(mContext, R.color.white);
            int previousColor = color;
            int sideViewColor = ContextCompat.getColor(mContext, R.color.recentblue);
            int previousSideViewCColor = sideViewColor;
            for (String dateString :
                    dateHashSet) {
                boolean flag = true;
                for (DoctorDetail data :
                        doctorDetails) {
                    if (dateString.equalsIgnoreCase(data.getDate())) {
                        if (flag) {
                            data.setRowColor(color);
                            data.setSideBarViewColor(sideViewColor);
                            //--background color---
                            if (color == ContextCompat.getColor(mContext, R.color.white)) {
                                previousColor = color;
                                color = ContextCompat.getColor(mContext, R.color.divider);
                            } else if (color == ContextCompat.getColor(mContext, R.color.divider)) {
                                previousColor = color;
                                color = ContextCompat.getColor(mContext, R.color.white);
                            }
                            //-----
                            //--sideView color---
                            if (sideViewColor == ContextCompat.getColor(mContext, R.color.recentblue)) {
                                previousSideViewCColor = sideViewColor;
                                sideViewColor = ContextCompat.getColor(mContext, R.color.darkblue);
                            } else if (sideViewColor == ContextCompat.getColor(mContext, R.color.darkblue)) {
                                previousSideViewCColor = sideViewColor;
                                sideViewColor = ContextCompat.getColor(mContext, R.color.recentblue);
                            }
                            data.setStartElement(true);
                            flag = false;
                        } else if (!flag) {
                            data.setRowColor(previousColor);
                            data.setSideBarViewColor(previousSideViewCColor);
                        }
                        map.add(data);
                    }
                }
            }
        }
        //----------
        return map;
    }

    //-- Sort date in descending order, copied from SRDaoImplManager.java
    private class DateWiseComparator implements Comparator<String> {

        public int compare(String m1, String m2) {

            //possibly check for nulls to avoid NullPointerException
            //  String s = CommonMethods.formatDateTime(m1, RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE);
            Date m1Date = CommonMethods.convertStringToDate(m1, RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
            Date m2Date = CommonMethods.convertStringToDate(m2, RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
            int i = m2Date.compareTo(m1Date);
            return i;
        }
    }

    public void doGetDoctorList(String year) {
        //ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_DOCTOR_LIST, Request.Method.GET, true);
       // mConnectionFactory.setHeaderParams();
      //  String id = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);
       // mConnectionFactory.setUrl(Config.DOCTOR_LIST_URL + id + "&year=" + year);
      //  mConnectionFactory.createConnection(RescribeConstants.TASK_DOCTOR_LIST);

            // TODO : HARDCODED JSON STRING PARSING FROM assets foler
        try {
            InputStream is = mContext.getAssets().open("doctor_list_update.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            CommonMethods.Log(TAG, "doGetAllMyRecords" + json);

            DoctorModel model = new Gson().fromJson(json, DoctorModel.class);
            onResponse(ConnectionListener.RESPONSE_OK, model, RescribeConstants.TASK_DOCTOR_LIST);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void doGetDoctorAppointment() {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_DOCTOR_APPOINTMENT, Request.Method.GET, true);

        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.APPOINTMENTS_DETAILS_URL + RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext));
        mConnectionFactory.createConnection(RescribeConstants.TASK_DOCTOR_APPOINTMENT);

    }

    public void doFilterDoctorList(DrFilterRequestModel mRequestedFilterRequestModel) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_DOCTOR_LIST_FILTERING, Request.Method.POST, true);
        mConnectionFactory.setHeaderParams();

        mConnectionFactory.setPostParams(mRequestedFilterRequestModel);
        mConnectionFactory.setUrl(Config.DOCTOR_LIST_FILTER_URL);
        mConnectionFactory.createConnection(RescribeConstants.TASK_DOCTOR_LIST_FILTERING);
    }
}
