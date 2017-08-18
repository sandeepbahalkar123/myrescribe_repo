package com.myrescribe.helpers.my_record;

import android.content.Context;
import android.support.v4.content.ContextCompat;

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
import com.myrescribe.model.filter.filter_request.DrFilterRequestModel;
import com.myrescribe.model.my_records.MyRecordBaseModel;
import com.myrescribe.model.my_records.MyRecordDataModel;
import com.myrescribe.model.my_records.MyRecordInfoAndReports;
import com.myrescribe.model.my_records.MyRecordInfoMonthContainer;
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
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by riteshpandhurkar on 1/3/17.
 */

public class MyRecordHelper implements ConnectionListener {

    String TAG = this.getClass().getName();
    Context mContext;
    HelperResponse mHelperResponseManager;
    private Map<String, Map<String, ArrayList<MyRecordInfoAndReports>>> yearWiseSortedMyRecordInfoAndReports = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public MyRecordHelper(Context context, HelperResponse loginActivity) {
        this.mContext = context;
        this.mHelperResponseManager = loginActivity;
    }

    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag == MyRescribeConstants.TASK_GET_ALL_MY_RECORDS) {
                    MyRecordBaseModel model = (MyRecordBaseModel) customResponse;
                    MyRecordDataModel recordMainDataModel = model.getRecordMainDataModel();
                    if (recordMainDataModel.getMyRecordInfoMonthContainer() != null) {
                        MyRecordInfoMonthContainer myRecordInfoMonthContainer = recordMainDataModel.getMyRecordInfoMonthContainer();
                        yearWiseSortedMyRecordInfoAndReports.put(myRecordInfoMonthContainer.getYear(), myRecordInfoMonthContainer.getMonthWiseSortedMyRecords());
                    }
                    mHelperResponseManager.onSuccess(mOldDataTag, model);
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


    public Map<String, Map<String, ArrayList<MyRecordInfoAndReports>>> getYearWiseSortedMyRecordInfoAndReports() {
        return yearWiseSortedMyRecordInfoAndReports;
    }

/*
    public ArrayList<DoctorDetail> getFormattedMyRecords(String month, Map<String, ArrayList<MyRecordInfoAndReports>> monthList) {

        ArrayList<MyRecordInfoAndReports> doctorDetails = monthList.get(month);
        ArrayList<DoctorDetail> map = new ArrayList<>();

        if (doctorDetails != null) {
            //-----------
            TreeSet<String> dateHashSet = new TreeSet<String>(new DateWiseComparator());
            for (MyRecordInfoAndReports data :
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
    }*/


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


    public void doGetAllMyRecords(String year) {
         ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, MyRescribeConstants.TASK_GET_ALL_MY_RECORDS, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        String id = MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATEINT_ID, mContext);
        mConnectionFactory.setUrl(Config.LIST_ALL_MY_RECORD + id);
        mConnectionFactory.createConnection(MyRescribeConstants.TASK_GET_ALL_MY_RECORDS);

       /* // TODO : HARDCODED JSON STRING PARSING FROM assets foler
        try {
            InputStream is = mContext.getAssets().open("my_record_home_screen_new.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            CommonMethods.Log(TAG, "doGetAllMyRecords" + json);

            MyRecordBaseModel model = new Gson().fromJson(json, MyRecordBaseModel.class);
            onResponse(ConnectionListener.RESPONSE_OK, model, MyRescribeConstants.TASK_GET_ALL_MY_RECORDS);

        } catch (IOException ex) {
            ex.printStackTrace();
        }*/
    }
}
