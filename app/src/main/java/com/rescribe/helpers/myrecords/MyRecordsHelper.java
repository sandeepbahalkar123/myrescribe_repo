package com.rescribe.helpers.myrecords;

import android.content.Context;
import android.view.View;

import com.android.volley.Request;
import com.rescribe.R;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.my_records.MyRecordBaseModel;
import com.rescribe.model.my_records.MyRecordDataModel;
import com.rescribe.model.my_records.MyRecordInfoAndReports;
import com.rescribe.model.my_records.MyRecordInfoMonthContainer;
import com.rescribe.model.my_records.RequestAddDoctorModel;
import com.rescribe.model.my_records.new_pojo.NewMonth;
import com.rescribe.model.my_records.new_pojo.NewMyRecordBaseModel;
import com.rescribe.model.my_records.new_pojo.NewMyRecordDataModel;
import com.rescribe.model.my_records.new_pojo.NewOriginalData;
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

                if (customResponse instanceof NewMyRecordBaseModel){

                    NewMyRecordBaseModel newModel = (NewMyRecordBaseModel) customResponse;
                    MyRecordBaseModel model = new MyRecordBaseModel();
                    MyRecordDataModel myRecordDataModel = new MyRecordDataModel();
                    NewMyRecordDataModel newRecordMainDataModel = newModel.getData();
                    model.setCommon(newModel.getCommon());
                    model.setRecordMainDataModel(myRecordDataModel);
                    myRecordDataModel.setReceivedYearMap(newRecordMainDataModel.getYearsMonthsData());
                    MyRecordInfoMonthContainer myRecordInfoMonthContainerNew = new MyRecordInfoMonthContainer();
                    myRecordInfoMonthContainerNew.setYear(String.valueOf(newRecordMainDataModel.getOriginalData().getYear()));
                    NewOriginalData newOriginalData = newRecordMainDataModel.getOriginalData();

                    TreeMap<String, ArrayList<MyRecordInfoAndReports>> monthWiseSortedMyRecords = new TreeMap<String, ArrayList<MyRecordInfoAndReports>>(String.CASE_INSENSITIVE_ORDER);

                    for (NewMonth newMonth : newOriginalData.getMonths()) {
                        ArrayList<MyRecordInfoAndReports> docVisits = newMonth.getDocVisits();
                        String month = newMonth.getMonth();
                        monthWiseSortedMyRecords.put(month, docVisits);
                    }

                    myRecordInfoMonthContainerNew.setMonthWiseSortedMyRecords(monthWiseSortedMyRecords);

                    myRecordDataModel.setMyRecordInfoMonthContainer(myRecordInfoMonthContainerNew);

                    MyRecordDataModel recordMainDataModel = model.getRecordMainDataModel();
                    if (recordMainDataModel.getMyRecordInfoMonthContainer() != null) {
                        MyRecordInfoMonthContainer myRecordInfoMonthContainer = recordMainDataModel.getMyRecordInfoMonthContainer();
                        yearWiseSortedMyRecordInfoAndReports.put(myRecordInfoMonthContainer.getYear(), myRecordInfoMonthContainer.getMonthWiseSortedMyRecords());
                    }
                }

                mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                break;
            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG,  mContext.getString(R.string.parse_error));
                mHelperResponseManager.onParseError(mOldDataTag, mContext.getString(R.string.parse_error));
                break;
            case ConnectionListener.SERVER_ERROR:
                CommonMethods.Log(TAG, mContext.getString(R.string.server_error));
                mHelperResponseManager.onServerError(mOldDataTag, mContext.getString(R.string.server_error));
                break;
            case ConnectionListener.NO_CONNECTION_ERROR:
                CommonMethods.Log(TAG,mContext.getString(R.string.no_connection_error));
                mHelperResponseManager.onNoConnectionError(mOldDataTag,mContext.getString(R.string.no_connection_error));
                break;
            default:
                CommonMethods.Log(TAG, mContext.getString(R.string.default_error));
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
    
    public void addDoctor(RequestAddDoctorModel requestAddDoctorModel) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.MY_RECORDS_ADD_DOCTOR, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(requestAddDoctorModel);
        mConnectionFactory.setUrl(Config.MY_RECORDS_ADD_DOCTOR);
        mConnectionFactory.createConnection(RescribeConstants.MY_RECORDS_ADD_DOCTOR);
    }

    public void doGetAllMyRecords(String year) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_GET_ALL_MY_RECORDS, Request.Method.GET, false);
        mConnectionFactory.setHeaderParams();
        String id = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);
        mConnectionFactory.setUrl(Config.LIST_ALL_MY_RECORD + id + "&year=" + year);
        mConnectionFactory.createConnection(RescribeConstants.TASK_GET_ALL_MY_RECORDS);
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
