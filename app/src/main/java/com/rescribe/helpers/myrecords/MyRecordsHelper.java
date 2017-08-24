package com.rescribe.helpers.myrecords;

import android.content.Context;

import com.android.volley.Request;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.my_records.MyRecordBaseModel;
import com.rescribe.model.my_records.MyRecordDataModel;
import com.rescribe.model.my_records.MyRecordInfoAndReports;
import com.rescribe.model.my_records.MyRecordInfoMonthContainer;
import com.rescribe.network.ConnectRequest;
import com.rescribe.network.ConnectionFactory;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by riteshpandhurkar on 1/3/17.
 */

public class MyRecordsHelper implements ConnectionListener {

    String TAG = this.getClass().getName();
    Context mContext;
    HelperResponse mHelperResponseManager;
    private Map<String, Map<String, ArrayList<MyRecordInfoAndReports>>> yearWiseSortedMyRecordInfoAndReports = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);


    public MyRecordsHelper(Context context, HelperResponse mHelperResponseManager) {
        this.mContext = context;
        this.mHelperResponseManager = mHelperResponseManager;
    }


    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                CommonMethods.Log(TAG, customResponse.getClass() + " success");

                if (mOldDataTag.equals(RescribeConstants.TASK_GET_ALL_MY_RECORDS)) {
                    MyRecordBaseModel model = (MyRecordBaseModel) customResponse;
                    MyRecordDataModel recordMainDataModel = model.getRecordMainDataModel();
                    if (recordMainDataModel.getMyRecordInfoMonthContainer() != null) {
                        MyRecordInfoMonthContainer myRecordInfoMonthContainer = recordMainDataModel.getMyRecordInfoMonthContainer();
                        yearWiseSortedMyRecordInfoAndReports.put(myRecordInfoMonthContainer.getYear(), myRecordInfoMonthContainer.getMonthWiseSortedMyRecords());
                    }
                    mHelperResponseManager.onSuccess(mOldDataTag, model);
                }else if (mOldDataTag.equals(RescribeConstants.MY_RECORDS_DOCTOR_LIST)) {
                    ((HelperResponse) mContext).onSuccess(mOldDataTag, customResponse);
                }

                break;
            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG, "parse error");
                ((HelperResponse) mContext).onParseError(mOldDataTag, "parse error");
                break;
            case ConnectionListener.SERVER_ERROR:
                CommonMethods.Log(TAG, "server error");
                ((HelperResponse) mContext).onServerError(mOldDataTag, "server error");
                break;
            case ConnectionListener.NO_CONNECTION_ERROR:
                CommonMethods.Log(TAG, "no connection error");
                ((HelperResponse) mContext).onNoConnectionError(mOldDataTag, "no connection error");
                break;
            default:
                CommonMethods.Log(TAG, "default error");
                break;
        }
    }

    public Map<String, Map<String, ArrayList<MyRecordInfoAndReports>>> getYearWiseSortedMyRecordInfoAndReports() {
        return yearWiseSortedMyRecordInfoAndReports;
    }

    @Override
    public void onTimeout(ConnectRequest request) {

    }

    public void getDoctorList(String patientId) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.MY_RECORDS_DOCTOR_LIST, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.MY_RECORDS_DOCTOR_LIST + "?patientId=" + patientId);
        mConnectionFactory.createConnection(RescribeConstants.MY_RECORDS_DOCTOR_LIST);
    }

    public void doGetAllMyRecords(String year) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_GET_ALL_MY_RECORDS, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        String id = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);
        mConnectionFactory.setUrl(Config.LIST_ALL_MY_RECORD + id);
        mConnectionFactory.createConnection(RescribeConstants.TASK_GET_ALL_MY_RECORDS);

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
            onResponse(ConnectionListener.RESPONSE_OK, model, RescribeConstants.TASK_GET_ALL_MY_RECORDS);

        } catch (IOException ex) {
            ex.printStackTrace();
        }*/
    }

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
}
