package com.rescribe.helpers.dashboard;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;

import com.rescribe.model.dashboard_api.DoctorlistModel.Doctorlistmodel;
import com.rescribe.model.dashboard_api.cardviewdatamodel.CardViewDataModel;
import com.rescribe.model.dashboard_api.cardviewdatamodel.CategoryList;
import com.rescribe.model.dashboard_api.cardviewdatamodel.Data;
import com.rescribe.model.dashboard_api.cardviewdatamodel.DocDetail;
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

    private String TAG = this.getClass().getName();
    private Context mContext;
    private HelperResponse mHelperResponseManager;
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
                if(mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_DASHBOARD_API)){
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                }else if(mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_DOCTORLIST_API)){

                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                    Doctorlistmodel doctorlistmodel = (Doctorlistmodel) customResponse;
                    if(doctorlistmodel!=null){
                      com.rescribe.model.dashboard_api.DoctorlistModel.Data data=doctorlistmodel.getData();

                      if(data!=null){

                          appDBHelper.deleteDoctor();
                          appDBHelper.deleteClinic();
                          appDBHelper.deleteCardViewData();
                          for(int i=0;i<data.getDoctorList().size();i++){

                              String dateAndTime = CommonMethods.getFormattedDate(CommonMethods.getCurrentDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.YYYY_MM_DD) + "&time=" + CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.HH_mm);
                              com.rescribe.model.dashboard_api.DoctorlistModel.DoctorList doctorList=data.getDoctorList().get(i);

                              appDBHelper.insertDocotorListData(doctorList.getDocId(),doctorList.getDocName(),doctorList.getDocPhone(),doctorList.getDoctorImageUrl(),doctorList.getAboutDoctor(),
                                      doctorList.getSpecialityId(),doctorList.getSpeciality(),doctorList.getRating(),doctorList.getCategorySpeciality(),doctorList.getDegree(),
                                      doctorList.getExperience(),doctorList.getPaidStatus(),doctorList.getCategoryName(),doctorList.getGender(),dateAndTime);

                             for(int j=0;j<doctorList.getClinicList().size();j++){

                                 appDBHelper.insertClinicData(doctorList.getClinicList().get(j).getLocationId(),doctorList.getDocId(),doctorList.getClinicList().get(j).getClinicName(),
                                         doctorList.getClinicList().get(j).getLocationLat(),doctorList.getClinicList().get(j).getLocationLong(),
                                         doctorList.getClinicList().get(j).getClinicAddress(),doctorList.getClinicList().get(j).getAreaName(),doctorList.getClinicList().get(j).getCityName());


                                 appDBHelper.insertDoctorvsClinicData(doctorList.getDocId(),doctorList.getClinicList().get(j).getLocationId(),doctorList.getClinicList().get(j).getAmount(),doctorList.getClinicList().get(j).getApptScheduleLmtDays(),
                                         doctorList.getClinicList().get(j).getAppointmentType(),"");
                             }
                          }
                      }
                    }


                }else if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_GET_SAVED_ARTICLES)) {
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
            url = url + mContext.getString(R.string.city) + currentCity.trim() + "&date=" + dateAndTime+ "&lastUpdatedDate=" + "2018-07-04T18:50:32.729Z";

        }
        mConnectionFactory.setUrl(url);
        mConnectionFactory.createConnection(RescribeConstants.TASK_DASHBOARD_API);
    }
    public void GetDoctorlist(String currentcity){
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, false, RescribeConstants.TASK_DOCTORLIST_API, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();

        String url = Config.GET_DOCTORLIST_DATA + RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);
        if(currentcity!=null){
            url = url + mContext.getString(R.string.city) + currentcity.trim();

        }
        mConnectionFactory.setUrl(url);
        mConnectionFactory.createConnection(RescribeConstants.TASK_DOCTORLIST_API);
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
