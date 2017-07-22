package com.myrescribe.helpers.doctor;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.myrescribe.R;
import com.myrescribe.interfaces.ConnectionListener;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.doctors.appointments.DoctorAppointmentModel;
import com.myrescribe.model.doctors.doctor_info.DoctorDetail;
import com.myrescribe.model.doctors.doctor_info.DoctorInfoMonthContainer;
import com.myrescribe.model.doctors.doctor_info.DoctorModel;
import com.myrescribe.model.doctors.filter_doctor_list.DoctorFilterModel;
import com.myrescribe.network.ConnectRequest;
import com.myrescribe.network.ConnectionFactory;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.Config;
import com.myrescribe.util.MyRescribeConstants;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
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

    public DoctorHelper(Context context, HelperResponse activity) {
        this.mContext = context;
        this.mHelperResponseManager = activity;
    }


    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag == MyRescribeConstants.TASK_DOCTOR_LIST) {
                    DoctorModel model = (DoctorModel) customResponse;
                    if (model.getDoctorInfoMonthContainer() != null) {
                        DoctorInfoMonthContainer doctorInfoMonthContainer = model.getDoctorInfoMonthContainer();
                        yearWiseSortedDoctorList.put(doctorInfoMonthContainer.getYear(), doctorInfoMonthContainer.getMonthWiseSortedDoctorList());
                    }
                    mHelperResponseManager.onSuccess(mOldDataTag, model);
                } else if (mOldDataTag == MyRescribeConstants.TASK_DOCTOR_APPOINTMENT) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                } else if (mOldDataTag == MyRescribeConstants.TASK_DOCTOR_LIST_FILTERING) {
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
            //  String s = CommonMethods.formatDateTime(m1, MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD, MyRescribeConstants.DATE_PATTERN.UTC_PATTERN, MyRescribeConstants.DATE);
            Date m1Date = CommonMethods.convertStringToDate(m1, MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD);
            Date m2Date = CommonMethods.convertStringToDate(m2, MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD);
            int i = m2Date.compareTo(m1Date);
            return i;
        }
    }

    public void doGetDoctorList(String year) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, MyRescribeConstants.TASK_DOCTOR_LIST, Request.Method.GET, true);
        Map<String, String> testParams = new HashMap<String, String>();

        testParams.put(MyRescribeConstants.AUTHORIZATION_TOKEN, MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.AUTHTOKEN, mContext));
        testParams.put(MyRescribeConstants.CONTENT_TYPE, MyRescribeConstants.APPLICATION_JSON);

        mConnectionFactory.setHeaderParams(testParams);
        // mConnectionFactory.setPostParams(testParams);
        mConnectionFactory.setUrl(Config.DOCTOR_LIST_URL + year);
        mConnectionFactory.createConnection(MyRescribeConstants.TASK_DOCTOR_LIST);

    }

    public void doGetDoctorAppointment() {
      /*  ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, MyRescribeConstants.TASK_DOCTOR_LIST, Request.Method.GET, true);
        Map<String, String> testParams = new HashMap<String, String>();

        testParams.put(MyRescribeConstants.AUTHORIZATION_TOKEN, MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.AUTHTOKEN, mContext));
        testParams.put(MyRescribeConstants.CONTENT_TYPE, MyRescribeConstants.APPLICATION_JSON);

        mConnectionFactory.setHeaderParams(testParams);
        // mConnectionFactory.setPostParams(testParams);
        mConnectionFactory.setUrl(Config.DOCTOR_LIST_URL + year);
        mConnectionFactory.createConnection(MyRescribeConstants.TASK_DOCTOR_LIST);
*/

        // TODO : HARDCODED JSON STRING PARSING FROM assets folder, will get remove
        try {
            InputStream is = mContext.getAssets().open("appointments.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Log.e(TAG, "doGetDoctorAppointment" + json);

            DoctorAppointmentModel model = new Gson().fromJson(json, DoctorAppointmentModel.class);

            CommonMethods.Log("doGetDoctorAppointment", "" + model.toString());
            onResponse(ConnectionListener.RESPONSE_OK, model, MyRescribeConstants.TASK_DOCTOR_APPOINTMENT);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void doFilterDoctorList() {
      /*  ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, MyRescribeConstants.TASK_DOCTOR_LIST, Request.Method.GET, true);
        Map<String, String> testParams = new HashMap<String, String>();

        testParams.put(MyRescribeConstants.AUTHORIZATION_TOKEN, MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.AUTHTOKEN, mContext));
        testParams.put(MyRescribeConstants.CONTENT_TYPE, MyRescribeConstants.APPLICATION_JSON);

        mConnectionFactory.setHeaderParams(testParams);
        // mConnectionFactory.setPostParams(testParams);
        mConnectionFactory.setUrl(Config.DOCTOR_LIST_URL + year);
        mConnectionFactory.createConnection(MyRescribeConstants.TASK_DOCTOR_LIST);
*/

        // TODO : HARDCODED JSON STRING PARSING FROM assets folder, will get remove
        try {
            InputStream is = mContext.getAssets().open("doctor_list_filter.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Log.e(TAG, "doFilterDoctorList" + json);

            DoctorFilterModel model = new Gson().fromJson(json, DoctorFilterModel.class);

            CommonMethods.Log("doFilterDoctorList", "" + model.toString());
            onResponse(ConnectionListener.RESPONSE_OK, model, MyRescribeConstants.TASK_DOCTOR_LIST_FILTERING);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
