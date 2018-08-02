package com.rescribe.helpers.dashboard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.Request;
import com.rescribe.R;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.dashboard_api.card_data.CategoryList;
import com.rescribe.model.dashboard_api.card_data.DashboardModel;
import com.rescribe.model.dashboard_api.doctors.DoctorListModel;
import com.rescribe.model.saved_article.SavedArticleBaseModel;
import com.rescribe.model.saved_article.SavedArticleInfo;
import com.rescribe.model.saved_article.request_model.ArticleToSaveReqModel;
import com.rescribe.network.ConnectRequest;
import com.rescribe.network.ConnectionFactory;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;

import java.util.List;

import static com.rescribe.util.RescribeConstants.SUCCESS;

/**
 * Created by riteshpandhurkar on 1/3/17.
 */

public class DashboardHelper implements ConnectionListener {

    private String TAG = this.getClass().getName();
    private Context mContext;
    private HelperResponse mHelperResponseManager;
//    private String currentCity = "";
    private AppDBHelper appDBHelper;

    public DashboardHelper(Context context, HelperResponse loginActivity) {
        this.mContext = context;
        this.mHelperResponseManager = loginActivity;
        appDBHelper = new AppDBHelper(mContext);
    }

    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_DASHBOARD_API)) {
                    DashboardModel dashboardModel = (DashboardModel) customResponse;
                    if (dashboardModel.getCommon().getStatusCode().equals(SUCCESS)) {
                        // inset card doctors details in database
                        List<CategoryList> categoryList = dashboardModel.getData().getCategoryList();

                        SQLiteDatabase writableDatabase = appDBHelper.getWritableDatabase();

//                        if (RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.LAST_UPDATED, mContext).isEmpty()) {
//                            int isDelete1 = writableDatabase.delete(AppDBHelper.DOC_DATA.DOCTOR_DATA_TABLE, "1", null);
//                            int isDelete2 = writableDatabase.delete(AppDBHelper.DOC_DATA.DOCOTORVSCLINIC_DATA_TABLE, "1", null);
//                            int isDelete3 = writableDatabase.delete(AppDBHelper.DOC_DATA.CLINIC_DATA_TABLE, "1", null);
//                            Log.d("DOCTOR_DELETED_ITEMS", isDelete1 + " " + isDelete2 + " " + isDelete3);
//                        }

                        int isDelete1 = writableDatabase.delete(AppDBHelper.DOC_DATA.CARDVIEW_DATA_TABLE, "1", null);
                        int isDelete2 = writableDatabase.delete(AppDBHelper.DOC_DATA.APPOINTMENT_DATA_TABLE, "1", null);
                        Log.d("CARD_DELETED_ITEMS", isDelete1 + " " + isDelete2);

                        writableDatabase.close();

                        appDBHelper.addCardDoctors(categoryList);
                        getDoctorList();
                    }
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);

                } else if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_DOCTORLIST_API)) {
                    DoctorListModel doctorListModel = (DoctorListModel) customResponse;
                    if (doctorListModel.getCommon().getStatusCode().equals(SUCCESS)) {
                        // insert doctor data in database and show
                        appDBHelper.addDoctors(doctorListModel.getData().getDoctorList());
                        RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.LAST_UPDATED, CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.UTC_PATTERN), mContext);
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
//        this.currentCity = currentCity;
        String screenResolutionValue = CommonMethods.getDeviceResolution(mContext);
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, false, RescribeConstants.TASK_DASHBOARD_API, Request.Method.GET, false);
        mConnectionFactory.setHeaderParams();

        String url = Config.GET_DASHBOARD_DATA + RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, mContext) + mContext.getString(R.string.platform) + mContext.getString(R.string.android) + mContext.getString(R.string.screen_resolution) + screenResolutionValue;
        if (currentCity != null) {
            String dateAndTime = CommonMethods.getFormattedDate(CommonMethods.getCurrentDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.YYYY_MM_DD) + "&time=" + CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.HH_mm);
            url = url + mContext.getString(R.string.city) + currentCity.trim() + "&date=" + dateAndTime + "&lastUpdatedDate=" + RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.LAST_UPDATED, mContext) + "&appName=" + RescribeConstants.PATIENT;
        }
        mConnectionFactory.setUrl(url);
        mConnectionFactory.createConnection(RescribeConstants.TASK_DASHBOARD_API);
    }

    public void getDoctorList() {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, false, RescribeConstants.TASK_DOCTORLIST_API, Request.Method.GET, false);
        mConnectionFactory.setHeaderParams();

        String url = Config.GET_DOCTORLIST_DATA + RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, mContext) + "&lastUpdatedDate=" + RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.LAST_UPDATED, mContext);
//        if (currentCity != null) {
//            url = url + mContext.getString(R.string.city) + currentCity.trim();
//        }
        mConnectionFactory.setUrl(url);
        mConnectionFactory.createConnection(RescribeConstants.TASK_DOCTORLIST_API);
    }

    public void doGetSavedArticles() {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_GET_SAVED_ARTICLES, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        String url = Config.TO_GET_SAVED_ARTICLES + RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, mContext);
        mConnectionFactory.setUrl(url);
        mConnectionFactory.createConnection(RescribeConstants.TASK_GET_SAVED_ARTICLES);
    }


    public void doHealthEducationGetSavedArticles() {

        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_GET_HEALTH_EDUCATION_ARTICLES, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();

        String url = Config.TO_GET_HEALTH_EDUCATION_SAVED_ARTICLES + RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, mContext);

        mConnectionFactory.setUrl(url);
        mConnectionFactory.createConnection(RescribeConstants.TASK_GET_HEALTH_EDUCATION_ARTICLES);
    }

    public void doSaveArticlesToServer(String url, boolean isBookMarked) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_SAVE_ARTICLES_TO_SERVER, Request.Method.POST, true);
        mConnectionFactory.setHeaderParams();

        ArticleToSaveReqModel reqModel = new ArticleToSaveReqModel();
        reqModel.setUrl(url);
        reqModel.setPatientId(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, mContext)));
        reqModel.setRemove(isBookMarked ? 0 : 1);
        mConnectionFactory.setPostParams(reqModel);

        mConnectionFactory.setUrl(Config.TO_SAVE_ARTICLE_TO_SERVER);
        mConnectionFactory.createConnection(RescribeConstants.TASK_SAVE_ARTICLES_TO_SERVER);
    }

}
