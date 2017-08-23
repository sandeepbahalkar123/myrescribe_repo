package com.myrescribe.helpers.myrecords;

import android.content.Context;

import com.android.volley.Request;
import com.myrescribe.interfaces.ConnectionListener;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.my_records.MyRecordDataModel;
import com.myrescribe.model.my_records.MyRecordInfoAndReports;
import com.myrescribe.model.my_records.MyRecordInfoMonthContainer;
import com.myrescribe.model.my_records.new_pojo.NewMyRecordBaseModel;
import com.myrescribe.network.ConnectRequest;
import com.myrescribe.network.ConnectionFactory;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.Config;
import com.myrescribe.util.MyRescribeConstants;

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

                if (mOldDataTag.equals(MyRescribeConstants.TASK_GET_ALL_MY_RECORDS)) {
                    NewMyRecordBaseModel model = (NewMyRecordBaseModel) customResponse;

                    mHelperResponseManager.onSuccess(mOldDataTag, model);
                }else if (mOldDataTag.equals(MyRescribeConstants.MY_RECORDS_DOCTOR_LIST)) {
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

    public Map<String, Map<String, ArrayList<MyRecordInfoAndReports>>> getYearWiseSortedMyRecordInfoAndReports(MyRecordDataModel recordMainDataModel) {
        if (recordMainDataModel.getMyRecordInfoMonthContainer() != null) {
            MyRecordInfoMonthContainer myRecordInfoMonthContainer = recordMainDataModel.getMyRecordInfoMonthContainer();
            yearWiseSortedMyRecordInfoAndReports.put(myRecordInfoMonthContainer.getYear(), myRecordInfoMonthContainer.getMonthWiseSortedMyRecords());
        }

        return yearWiseSortedMyRecordInfoAndReports;
    }

    @Override
    public void onTimeout(ConnectRequest request) {

    }

    public void getDoctorList(String patientId) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, MyRescribeConstants.MY_RECORDS_DOCTOR_LIST, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.MY_RECORDS_DOCTOR_LIST + "?patientId=" + patientId);
        mConnectionFactory.createConnection(MyRescribeConstants.MY_RECORDS_DOCTOR_LIST);
    }

    public void doGetAllMyRecords() {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, MyRescribeConstants.TASK_GET_ALL_MY_RECORDS, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        String id = MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);
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
}
