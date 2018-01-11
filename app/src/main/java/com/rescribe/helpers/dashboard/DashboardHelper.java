package com.rescribe.helpers.dashboard;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;

import com.rescribe.model.dashboard_api.unread_notification_message_list.UnreadSavedNotificationMessageData;

import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.dashboard_api.DashBoardBaseModel;
import com.rescribe.model.dashboard_api.DashboardDataModel;

import com.rescribe.model.saved_article.SavedArticleBaseModel;
import com.rescribe.model.saved_article.SavedArticleInfo;
import com.rescribe.model.saved_article.request_model.ArticleToSaveReqModel;
import com.rescribe.network.ConnectRequest;
import com.rescribe.network.ConnectionFactory;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by riteshpandhurkar on 1/3/17.
 */

public class DashboardHelper implements ConnectionListener {

    String TAG = this.getClass().getName();
    Context mContext;
    HelperResponse mHelperResponseManager;
    private DashboardDataModel mDashboardDataModel = null;

    public DashboardHelper(Context context, HelperResponse loginActivity) {
        this.mContext = context;
        this.mHelperResponseManager = loginActivity;
    }

    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_DASHBOARD_API)) {
                    DashBoardBaseModel dashBoardBaseModel = (DashBoardBaseModel) customResponse;
                    if (dashBoardBaseModel != null) {
                        DashboardDataModel dashboardDataModel = dashBoardBaseModel.getDashboardModel();
                        if (dashboardDataModel != null) {
                            mDashboardDataModel = dashboardDataModel;
                            for (int i = 0; i < mDashboardDataModel.getDoctorList().size(); i++) {
                                DoctorList doctorList = mDashboardDataModel.getDoctorList().get(i);
                                if (!doctorList.getDocName().toLowerCase().contains("dr.")) {
                                    doctorList.setDocName("Dr. " + doctorList.getDocName());
                                }
                                if (doctorList.getCategoryName().equalsIgnoreCase(mContext.getString(R.string.sponsered))) {
                                    doctorList.setCategoryName(mContext.getString(R.string.sponsored_doctor));
                                }
                            }
                        }
                    }
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                } else if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_GET_SAVED_ARTICLES)) {
                    SavedArticleBaseModel savedArticleBaseModel = (SavedArticleBaseModel) customResponse;
                    if (savedArticleBaseModel != null) {
                        for (int i = 0; i < savedArticleBaseModel.getSavedArticleDataModel().getSavedArticleList().size(); i++) {
                            SavedArticleInfo savedArticleInfo = savedArticleBaseModel.getSavedArticleDataModel().getSavedArticleList().get(i);
                            if (!savedArticleInfo.getAuthorName().toLowerCase().contains("dr.")) {
                                savedArticleInfo.setAuthorName("Dr. " + savedArticleInfo.getAuthorName());
                            }
                        }
                    }

                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                } else if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_SAVE_ARTICLES_TO_SERVER)) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                } else if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_GET_HEALTH_EDUCATION_ARTICLES)) {
                    SavedArticleBaseModel savedArticleBaseModel = (SavedArticleBaseModel) customResponse;
                    if (savedArticleBaseModel != null) {
                        for (int i = 0; i < savedArticleBaseModel.getSavedArticleDataModel().getSavedArticleList().size(); i++) {
                            SavedArticleInfo savedArticleInfo = savedArticleBaseModel.getSavedArticleDataModel().getSavedArticleList().get(i);
                            if (!savedArticleInfo.getAuthorName().toLowerCase().contains("dr.")) {
                                savedArticleInfo.setAuthorName("Dr. " + savedArticleInfo.getAuthorName());
                            }
                        }
                    }

                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                }
                break;
            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG, mContext.getString(R.string.parse_error));
                mHelperResponseManager.onParseError(mOldDataTag, mContext.getString(R.string.parse_error));
                break;
            case ConnectionListener.SERVER_ERROR:
                CommonMethods.Log(TAG, mContext.getString(R.string.server_error));
                mHelperResponseManager.onServerError(mOldDataTag, mContext.getString(R.string.server_error));

                break;
            case ConnectionListener.NO_INTERNET:
                CommonMethods.Log(TAG, mContext.getString(R.string.no_connection_error));
                mHelperResponseManager.onNoConnectionError(mOldDataTag, mContext.getString(R.string.no_connection_error));

                break;
            case ConnectionListener.NO_CONNECTION_ERROR:
                CommonMethods.Log(TAG, mContext.getString(R.string.no_connection_error));
                mHelperResponseManager.onNoConnectionError(mOldDataTag, mContext.getString(R.string.no_connection_error));

                break;
            default:
                CommonMethods.Log(TAG, mContext.getString(R.string.default_error));
                break;
        }
    }

    @Override
    public void onTimeout(ConnectRequest request) {

    }

    public void doGetDashboard(String currentCity) {


        String screenResolutionValue = CommonMethods.getDeviceResolution(mContext);
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, false, RescribeConstants.TASK_DASHBOARD_API, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();

        String url = Config.GET_DASHBOARD_DATA + RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext) + mContext.getString(R.string.platform) + mContext.getString(R.string.android) + mContext.getString(R.string.screen_resolution) + screenResolutionValue;
        if (currentCity != null) {
            String dateAndTime = CommonMethods.getFormattedDate(CommonMethods.getCurrentDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.YYYY_MM_DD) + "&time=" + CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.HH_mm);
            url = url + mContext.getString(R.string.city) + currentCity.trim() + "&date=" + dateAndTime;
        }
        mConnectionFactory.setUrl(url);
        mConnectionFactory.createConnection(RescribeConstants.TASK_DASHBOARD_API);


        //---
        /*try {
            InputStream is = mContext.getAssets().open("temp_dashboard_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Log.e(TAG, "temp_dashboard_data" + json);

            DashBoardBaseModel slotListBaseModel = new Gson().fromJson(json, DashBoardBaseModel.class);
            onResponse(ConnectionListener.RESPONSE_OK, slotListBaseModel, RescribeConstants.TASK_DASHBOARD_API);

        } catch (IOException ex) {
            ex.printStackTrace();
        }*/
        //---
    }

    public void doGetSavedArticles() {

        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_GET_SAVED_ARTICLES, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        String url = Config.TO_GET_SAVED_ARTICLES + RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);
        mConnectionFactory.setUrl(url);
        mConnectionFactory.createConnection(RescribeConstants.TASK_GET_SAVED_ARTICLES);
    }


    public void doHealthEducationGetSavedArticles() {

        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_GET_HEALTH_EDUCATION_ARTICLES, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();

        String url = Config.TO_GET_HEALTH_EDUCATION_SAVED_ARTICLES + RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);

        mConnectionFactory.setUrl(url);
        mConnectionFactory.createConnection(RescribeConstants.TASK_GET_HEALTH_EDUCATION_ARTICLES);
    }

    public void doSaveArticlesToServer(String url, boolean isBookMarked) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_SAVE_ARTICLES_TO_SERVER, Request.Method.POST, true);
        mConnectionFactory.setHeaderParams();

        ArticleToSaveReqModel reqModel = new ArticleToSaveReqModel();
        reqModel.setUrl(url);
        reqModel.setPatientId(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext)));

        int value = isBookMarked ? 0 : 1;
        reqModel.setRemove(value);
        mConnectionFactory.setPostParams(reqModel);

        mConnectionFactory.setUrl(Config.TO_SAVE_ARTICLE_TO_SERVER);
        mConnectionFactory.createConnection(RescribeConstants.TASK_SAVE_ARTICLES_TO_SERVER);
    }

}
