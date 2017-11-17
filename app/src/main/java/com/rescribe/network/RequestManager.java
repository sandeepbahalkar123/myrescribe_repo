package com.rescribe.network;

/**
 * @author Sandeep Bahalkar
 */

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rescribe.R;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.Connector;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.book_appointment.complaints.ComplaintsBaseModel;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.model.book_appointment.filterdrawer.BookAppointFilterBaseModel;
import com.rescribe.model.book_appointment.reviews.ReviewListBaseModel;
import com.rescribe.model.book_appointment.search_doctors.RecentVisitedBaseModel;
import com.rescribe.model.book_appointment.select_slot_book_appointment.TimeSlotListBaseModel;
import com.rescribe.model.case_details.CaseDetailsModel;
import com.rescribe.model.chat.SendMessageModel;
import com.rescribe.model.chat.history.ChatHistoryModel;
import com.rescribe.model.dashboard_api.DashBoardBaseModel;
import com.rescribe.model.doctor_connect.DoctorConnectBaseModel;
import com.rescribe.model.doctor_connect.RecentChatDoctorModel;
import com.rescribe.model.doctor_connect_chat.DoctorConnectChatBaseModel;
import com.rescribe.model.doctor_connect_search.DoctorConnectSearchBaseModel;
import com.rescribe.model.doctors.appointments.DoctorAppointmentModel;
import com.rescribe.model.doctors.doctor_info.DoctorBaseModel;
import com.rescribe.model.doctors.filter_doctor_list.DoctorFilterModel;
import com.rescribe.model.filter.CaseDetailsListModel;
import com.rescribe.model.filter.FilterDoctorListModel;
import com.rescribe.model.filter.FilterDoctorSpecialityListModel;
import com.rescribe.model.investigation.InvestigationListModel;
import com.rescribe.model.investigation.gmail.InvestigationUploadByGmailModel;
import com.rescribe.model.investigation.uploaded.InvestigationUploadFromUploadedModel;
import com.rescribe.model.login.ActiveStatusModel;
import com.rescribe.model.login.LoginModel;
import com.rescribe.model.login.LoginWithOtp;
import com.rescribe.model.login.SignUpModel;
import com.rescribe.model.my_records.AddDoctorModel;
import com.rescribe.model.my_records.MyRecordsDoctorListModel;
import com.rescribe.model.my_records.new_pojo.NewMyRecordBaseModel;
import com.rescribe.model.notification.AppointmentsNotificationModel;
import com.rescribe.model.notification.NotificationModel;
import com.rescribe.model.prescription_response_model.PrescriptionBaseModel;
import com.rescribe.model.requestmodel.login.LoginRequestModel;
import com.rescribe.model.response_model_notification.NotificationResponseBaseModel;
import com.rescribe.model.vital_graph.vital_all_list.VitalGraphBaseModel;
import com.rescribe.model.vital_graph.vital_description.VitalGraphInfoBaseModel;
import com.rescribe.model.vital_graph.vital_tracker.VitalGraphTrackerBaseModel;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.singleton.Device;
import com.rescribe.ui.customesViews.CustomProgressDialog;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;
import com.rescribe.util.NetworkUtil;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestManager extends ConnectRequest implements Connector, RequestTimer.RequestTimerListener {
    private final String TAG = this.getClass().getName();
    private static final int CONNECTION_TIME_OUT = 1000 * 60;
    private static final int N0OF_RETRY = 0;
    private AppDBHelper dbHelper;
    private String requestTag;
    private int connectionType = Request.Method.POST;

    private String mDataTag;
    private RequestTimer requestTimer;
    private JsonObjectRequest jsonRequest;
    private StringRequest stringRequest;

    public RequestManager(Context mContext, ConnectionListener connectionListener, String dataTag, View viewById, boolean isProgressBarShown, String mOldDataTag, int connectionType, boolean isOffline) {
        super();
        this.mConnectionListener = connectionListener;
        this.mContext = mContext;
        this.mDataTag = dataTag;
        this.mViewById = viewById;
        this.isProgressBarShown = isProgressBarShown;
        this.mOldDataTag = mOldDataTag;
        this.requestTag = String.valueOf(dataTag);
        this.requestTimer = new RequestTimer();
        this.requestTimer.setListener(this);
        this.mProgressDialog = new CustomProgressDialog(mContext);
        this.connectionType = connectionType;
        this.isOffline = isOffline;
        this.dbHelper = new AppDBHelper(mContext);
    }

    @Override
    public void connect() {

        if (NetworkUtil.isInternetAvailable(mContext)) {

            RequestPool.getInstance(this.mContext).cancellAllPreviousRequestWithSameTag(requestTag);

            if (isProgressBarShown) {
                mProgressDialog.setCancelable(true);
                mProgressDialog.show();
            } else {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }

            if (mPostParams != null) {
                stringRequest(mURL, connectionType, mHeaderParams, mPostParams, false);
            } else if (customResponse != null) {
                jsonRequest(mURL, connectionType, mHeaderParams, customResponse, false);
            } else {
                jsonRequest();
            }
        } else {

            if (isOffline) {
                if (getOfflineData() != null)
                    succesResponse(getOfflineData(), false);
                else
                    mConnectionListener.onResponse(ConnectionListener.NO_INTERNET, null, mOldDataTag);
            } else {
                mConnectionListener.onResponse(ConnectionListener.NO_INTERNET, null, mOldDataTag);
            }

            if (mViewById != null)
                CommonMethods.showSnack(mViewById, mContext.getString(R.string.internet));
            else
                CommonMethods.showToast(mContext, mContext.getString(R.string.internet));
        }
    }

    private void jsonRequest(String url, int connectionType, final Map<String, String> headerParams, CustomResponse customResponse, final boolean isTokenExpired) {

        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            CommonMethods.Log(TAG, "customResponse:--" + customResponse.toString());
            String jsonString = gson.toJson(customResponse);

            CommonMethods.Log(TAG, "jsonRequest:--" + jsonString);

            if (!jsonString.equals("null"))
                jsonObject = new JSONObject(jsonString);
        } catch (JSONException | JsonSyntaxException e) {
            e.printStackTrace();
        }

        jsonRequest = new JsonObjectRequest(connectionType, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        succesResponse(response.toString(), isTokenExpired);
                        if (isOffline)
                            dbHelper.insertData(mDataTag, response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorResponse(error, isTokenExpired);
            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (headerParams == null) {
                    return Collections.emptyMap();
                } else {
                    return headerParams;
                }

            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(CONNECTION_TIME_OUT, N0OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonRequest.setTag(requestTag);
        requestTimer.start();
        RequestPool.getInstance(this.mContext).addToRequestQueue(jsonRequest);
    }


    private void jsonRequest() {

        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            String jsonString = gson.toJson(customResponse);

            CommonMethods.Log(TAG, "jsonRequest:--" + jsonString);

            if (!jsonString.equals("null"))
                jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        jsonRequest = new JsonObjectRequest(connectionType, this.mURL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        succesResponse(response.toString(), false);
                        if (isOffline)
                            dbHelper.insertData(mDataTag, response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorResponse(error, false);
            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (mHeaderParams == null) {
                    return Collections.emptyMap();
                } else {
                    return mHeaderParams;
                }

            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(CONNECTION_TIME_OUT, N0OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonRequest.setTag(requestTag);
        requestTimer.start();
        RequestPool.getInstance(this.mContext).addToRequestQueue(jsonRequest);
    }

    private void stringRequest(String url, int connectionType, final Map<String, String> headerParams, final Map<String, String> postParams, final boolean isTokenExpired) {

        // ganesh for string request and delete method with string request

        stringRequest = new StringRequest(connectionType, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        succesResponse(response, isTokenExpired);
                        if (isOffline)
                            dbHelper.insertData(mDataTag, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        errorResponse(error, isTokenExpired);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headerParams;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return postParams;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(CONNECTION_TIME_OUT, N0OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag(requestTag);
        requestTimer.start();
        RequestPool.getInstance(this.mContext).addToRequestQueue(stringRequest);
    }

    private void succesResponse(String response, boolean isTokenExpired) {
        requestTimer.cancel();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        parseJson(fixEncoding(response), isTokenExpired);
    }

    private void errorResponse(VolleyError error, boolean isTokenExpired) {

        requestTimer.cancel();

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        try {

//            VolleyError error1 = new VolleyError(new String(error.networkResponse.data));
//            error = error1;
//            CommonMethods.Log("Error Message", error.getMessage() + "\n error Localize message" + error.getLocalizedMessage());
            CommonMethods.Log(TAG, "Goes into error response condition");

            if (error instanceof TimeoutError) {

                if (error.getMessage().equalsIgnoreCase("java.io.IOException: No authentication challenges found") || error.getMessage().equalsIgnoreCase("invalid_grant")) {
                    if (!isTokenExpired) {
                        tokenRefreshRequest();
                    }
                }

//                if (error.getMessage().equalsIgnoreCase("java.io.IOException: No authentication challenges found") || error.getMessage().equalsIgnoreCase("invalid_grant")) {
//                    if (mViewById != null)
//                        CommonMethods.showSnack(mViewById, mContext.getString(R.string.authentication));
//                    else
//                        CommonMethods.showToast(mContext, mContext.getString(R.string.authentication));
//                } else if (error.getMessage().equalsIgnoreCase("javax.net.ssl.SSLHandshakeException: java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.")) {
//                    showErrorDialog("Something went wrong.");
//                }

                if (mViewById != null)
                    CommonMethods.showSnack(mViewById, mContext.getString(R.string.timeout));
                else
                    CommonMethods.showToast(mContext, mContext.getString(R.string.timeout));

            } else if (error instanceof NoConnectionError) {

                if (error.getMessage().equalsIgnoreCase("java.io.IOException: No authentication challenges found") || error.getMessage().equalsIgnoreCase("invalid_grant")) {
                    if (!isTokenExpired) {
                        tokenRefreshRequest();
                    }
                }

                if (mViewById != null)
                    CommonMethods.showSnack(mViewById, mContext.getString(R.string.internet));
                else {
                    mConnectionListener.onResponse(ConnectionListener.NO_CONNECTION_ERROR, null, mOldDataTag);
                }

            } else if (error instanceof ServerError) {
                if (isTokenExpired) {
                    // Redirect to SplashScreen then Login
//                    Intent intent = new Intent(mContext, LoginActivity.class);
//                    mContext.startActivity(intent);
//                    ((AppCompatActivity) mContext).finishAffinity();

//                    RescribePreferencesManager.clearSharedPref(mContext);
//                    Intent intent = new Intent(mContext, SplashScreenActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    mContext.startActivity(intent);

                    //  loginRequest();
                } else {
                    mConnectionListener.onResponse(ConnectionListener.SERVER_ERROR, null, mOldDataTag);
                    CommonMethods.showToast(mContext, mContext.getResources().getString(R.string.server_error));
                }
            } else if (error instanceof NetworkError) {

                if (isOffline) {
                    succesResponse(getOfflineData(), false);
                } else {
                    mConnectionListener.onResponse(ConnectionListener.NO_INTERNET, null, mOldDataTag);
                }

                if (mViewById != null)
                    CommonMethods.showSnack(mViewById, mContext.getString(R.string.internet));
                else
                    CommonMethods.showToast(mContext, mContext.getString(R.string.internet));
            } else if (error instanceof ParseError) {
                mConnectionListener.onResponse(ConnectionListener.PARSE_ERR0R, null, mOldDataTag);
            } else if (error instanceof AuthFailureError) {
                if (!isTokenExpired) {
                    tokenRefreshRequest();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getOfflineData() {
        if (dbHelper.dataTableNumberOfRows(this.mDataTag) > 0) {
            Cursor cursor = dbHelper.getData(this.mDataTag);
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(AppDBHelper.COLUMN_DATA));
        } else {
            return null;
        }
    }

    private String fixEncoding(String response) {
        try {
            byte[] u = response.getBytes("ISO-8859-1");
            response = new String(u, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    @Override
    public void parseJson(String data, boolean isTokenExpired) {
        try {
            CommonMethods.Log(TAG, data);
            Gson gson = new Gson();

            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(data);
                Common common = gson.fromJson(jsonObject.optString("common"), Common.class);
                if (!common.getStatusCode().equals(RescribeConstants.SUCCESS)) {
                    CommonMethods.showToast(mContext, common.getStatusMessage());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*MessageModel messageModel = gson.fromJson(data, MessageModel.class);
            if (!messageModel.getCommon().getStatusCode().equals(RescribeConstants.SUCCESS))
                CommonMethods.showToast(mContext, messageModel.getCommon().getStatusMessage());*/

            if (isTokenExpired) {
                // This success response is for refresh token
                // Need to Add
                LoginModel loginModel = gson.fromJson(data, LoginModel.class);
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.AUTHTOKEN, loginModel.getLoginData().getAuthToken(), mContext);
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, RescribeConstants.YES, mContext);
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, String.valueOf(loginModel.getLoginData().getPatientDetail().getPatientId()), mContext);

                mHeaderParams.put(RescribeConstants.AUTHORIZATION_TOKEN, loginModel.getLoginData().getAuthToken());

                connect();

            } else {
                // This success response is for respective api's

                switch (this.mDataTag) {

                    // Need to add

                    case RescribeConstants.TASK_PRESCRIPTION_LIST: //This is for get archived list
                        PrescriptionBaseModel ipTestResponseModel = gson.fromJson(data, PrescriptionBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, ipTestResponseModel, mOldDataTag);
                        break;

                    case RescribeConstants.TASK_LOGIN: //This is for get archived list
                        LoginModel loginModel = gson.fromJson(data, LoginModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, loginModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_ONE_DAY_VISIT: //This is for get archived list
                        CaseDetailsModel caseDetailsModel = gson.fromJson(data, CaseDetailsModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, caseDetailsModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_DOCTOR_LIST: //This is for get archived list
                        DoctorBaseModel doctorsModel = new Gson().fromJson(data, DoctorBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, doctorsModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_DOCTOR_LIST_FILTERING: //This is for get archived list
                        DoctorFilterModel doctorFilterModel = new Gson().fromJson(data, DoctorFilterModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, doctorFilterModel, mOldDataTag);
                        break;

                    case RescribeConstants.TASK_SIGN_UP: //This is for get sign-up
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, new Gson().fromJson(data, SignUpModel.class), mOldDataTag);
                        break;
                    case RescribeConstants.TASK_VERIFY_SIGN_UP_OTP: //This is for to verify sign-up otp
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, new Gson().fromJson(data, LoginModel.class), mOldDataTag);
                        break;
                    case RescribeConstants.TASK_NOTIFICATION: //This is for get archived list
                        NotificationModel notificationModel = new Gson().fromJson(data, NotificationModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, notificationModel, mOldDataTag);
                        break;


                    case RescribeConstants.FILTER_DOCTOR_LIST: //This is for get archived list
                        FilterDoctorListModel filterDoctorListModel = new Gson().fromJson(data, FilterDoctorListModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, filterDoctorListModel, mOldDataTag);
                        break;

                    case RescribeConstants.FILTER_DOCTOR_SPECIALITY_LIST: //This is for get archived list
                        FilterDoctorSpecialityListModel filterDoctorSpecialityListModel = new Gson().fromJson(data, FilterDoctorSpecialityListModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, filterDoctorSpecialityListModel, mOldDataTag);
                        break;

                    case RescribeConstants.CASE_DETAILS_LIST: //This is for get archived list
                        CaseDetailsListModel caseDetailsListModel = new Gson().fromJson(data, CaseDetailsListModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, caseDetailsListModel, mOldDataTag);
                        break;

                    case RescribeConstants.APPOINTMENT_NOTIFICATION: //This is for get archived list
                        AppointmentsNotificationModel appointmentsNotificationModel = new Gson().fromJson(data, AppointmentsNotificationModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, appointmentsNotificationModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_DOCTOR_APPOINTMENT: //This is for TASK_DOCTOR_APPOINTMENT
                        DoctorAppointmentModel doctorAppointmentModel = new Gson().fromJson(data, DoctorAppointmentModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, doctorAppointmentModel, mOldDataTag);
                        break;

                    case RescribeConstants.INVESTIGATION_LIST: //This is for INVESTIGATION_LIST
                        InvestigationListModel investigationListModel = new Gson().fromJson(data, InvestigationListModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, investigationListModel, mOldDataTag);
                        break;

                    case RescribeConstants.INVESTIGATION_UPLOAD_BY_GMAIL: //This is for INVESTIGATION_UPLOAD_BY_GMAIL
                        InvestigationUploadByGmailModel investigationUploadByGmailModel = new Gson().fromJson(data, InvestigationUploadByGmailModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, investigationUploadByGmailModel, mOldDataTag);
                        break;

                    case RescribeConstants.INVESTIGATION_UPLOAD_FROM_UPLOADED: //This is for INVESTIGATION_UPLOAD_FROM_UPLOADED
                        InvestigationUploadFromUploadedModel investigationUploadFromUploadedModel = new Gson().fromJson(data, InvestigationUploadFromUploadedModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, investigationUploadFromUploadedModel, mOldDataTag);
                        break;

                    case RescribeConstants.TASK_GET_ALL_MY_RECORDS: //This is for TASK_GET_ALL_MY_RECORDS
                        NewMyRecordBaseModel model = new Gson().fromJson(data, NewMyRecordBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, model, mOldDataTag);
                        break;

                    case RescribeConstants.TASK_LOGIN_WITH_PASSWORD: //This is for get archived list
                        LoginModel loginWithPasswordModel = new Gson().fromJson(data, LoginModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, loginWithPasswordModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_LOGIN_WITH_OTP: //This is for get archived list
                        LoginWithOtp loginWithOtpModel = new Gson().fromJson(data, LoginWithOtp.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, loginWithOtpModel, mOldDataTag);
                        break;

                    case RescribeConstants.MY_RECORDS_DOCTOR_LIST: //This is for get archived list
                        MyRecordsDoctorListModel myRecordsDoctorListModel = new Gson().fromJson(data, MyRecordsDoctorListModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, myRecordsDoctorListModel, mOldDataTag);
                        break;

                    case RescribeConstants.MY_RECORDS_ADD_DOCTOR: //This is for get archived list
                        AddDoctorModel addDoctorModel = new Gson().fromJson(data, AddDoctorModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, addDoctorModel, mOldDataTag);
                        break;

                    case RescribeConstants.TASK_DOCTOR_CONNECT_CHAT: //This is for get archived list
                        DoctorConnectChatBaseModel doctorConnectChatBaseModel = new Gson().fromJson(data, DoctorConnectChatBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, doctorConnectChatBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_DOCTOR__FILTER_DOCTOR_SPECIALITY_LIST: //This is for get archived list
                        DoctorConnectSearchBaseModel doctorConnectSearchBaseModel = new Gson().fromJson(data, DoctorConnectSearchBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, doctorConnectSearchBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_DOCTOR_CONNECT: //This is for get archived list
                        DoctorConnectBaseModel doctorConnectBaseModel = new Gson().fromJson(data, DoctorConnectBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, doctorConnectBaseModel, mOldDataTag);
                        break;

                    case RescribeConstants.CHAT_USERS: //This is for get archived list
                        RecentChatDoctorModel recentChatDoctorModel = new Gson().fromJson(data, RecentChatDoctorModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, recentChatDoctorModel, mOldDataTag);
                        break;

                    case RescribeConstants.SEND_MESSAGE: //This is for get archived list
                        SendMessageModel sendMessageModel = new Gson().fromJson(data, SendMessageModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, sendMessageModel, mOldDataTag);
                        break;

                    case RescribeConstants.CHAT_HISTORY: //This is for get archived list
                        ChatHistoryModel chatHistoryModel = new Gson().fromJson(data, ChatHistoryModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, chatHistoryModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_GET_DOCTOR_DATA://get doctor data by location
                        BookAppointmentBaseModel bookAppointmentBaseModel = new Gson().fromJson(data, BookAppointmentBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, bookAppointmentBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_GET_DOCTOR_LIST_BY_COMPLAINT://get doctor data by location
                        BookAppointmentBaseModel bookAppointmentBaseModelForComplaint = new Gson().fromJson(data, BookAppointmentBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, bookAppointmentBaseModelForComplaint, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_GET_COMPLAINTS: //This is for get archived list
                        ComplaintsBaseModel complaintsBaseModel = new Gson().fromJson(data, ComplaintsBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, complaintsBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_GET_BOOK_APPOINT_DRAWER_CONFIG: //This is for get archived list
                        BookAppointFilterBaseModel bookAppointFilterBaseModel = new Gson().fromJson(data, BookAppointFilterBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, bookAppointFilterBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_GET_PATIENT_VITAL_LIST: //This is for get vital graph list
                        VitalGraphBaseModel vitalGraphBaseModel = new Gson().fromJson(data, VitalGraphBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, vitalGraphBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_GET_PATIENT_VITAL_DETAIL: //This is for get vital graph details
                        VitalGraphInfoBaseModel vitalGraphInfoBaseModel = new Gson().fromJson(data, VitalGraphInfoBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, vitalGraphInfoBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_GET_VITAL_TRACKER_LIST: //This is for get vital graph tracker list
                        VitalGraphTrackerBaseModel vitalGraphTrackerBaseModel = new Gson().fromJson(data, VitalGraphTrackerBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, vitalGraphTrackerBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_ADD_VITAL_MANUALLY: //This is for get vital graph tracker list
                        CommonBaseModelContainer c = new Gson().fromJson(data, CommonBaseModelContainer.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, c, mOldDataTag);
                        break;

                    case RescribeConstants.ACTIVE_STATUS: //This is for get archived list
                        ActiveStatusModel activeStatusModel = new Gson().fromJson(data, ActiveStatusModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, activeStatusModel, mOldDataTag);
                        break;

                    case RescribeConstants.LOGOUT: //This is for get archived list
                        ActiveStatusModel activeStatusLogout = new Gson().fromJson(data, ActiveStatusModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, activeStatusLogout, mOldDataTag);
                        break;

                    case RescribeConstants.TASK_GET_REVIEW_LIST: //This is for get archived list
                        ReviewListBaseModel reviewListBaseModel = new Gson().fromJson(data, ReviewListBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, reviewListBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_SERVICES_DOC_LIST_FILTER: //This is for get archived list
                        BookAppointmentBaseModel dataObject = new Gson().fromJson(data, BookAppointmentBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, dataObject, mOldDataTag);
                        break;

                    case RescribeConstants.TASK_TIME_SLOT_TO_BOOK_APPOINTMENT: //This is for get archived list
                        TimeSlotListBaseModel dataTimeSlot = new Gson().fromJson(data, TimeSlotListBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, dataTimeSlot, mOldDataTag);
                        break;

                    case RescribeConstants.TASK_DASHBOARD_API: //This is for get archived list
                        DashBoardBaseModel dashboardBaseModel = new Gson().fromJson(data, DashBoardBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, dashboardBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_SET_FAVOURITE_DOCTOR: //This is for get archived list
                        CommonBaseModelContainer responseFavouriteDoctorBaseModel = new Gson().fromJson(data, CommonBaseModelContainer.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, responseFavouriteDoctorBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_RECENT_VISIT_DOCTOR_PLACES_DATA: //This is for get archived list
                        RecentVisitedBaseModel mRecentVisitedBaseModel = new Gson().fromJson(data, RecentVisitedBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mRecentVisitedBaseModel, mOldDataTag);
                        break;

                    default:
                        //This is for get PDF VisitData
                        if (mOldDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION)) {
                            NotificationResponseBaseModel responseLogNotificationModel = new Gson().fromJson(data, NotificationResponseBaseModel.class);
                            this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, responseLogNotificationModel, mOldDataTag);
                        } else if (mDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER)) {
                            NotificationResponseBaseModel responseLogNotificationModel = new Gson().fromJson(data, NotificationResponseBaseModel.class);
                            this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, responseLogNotificationModel, mOldDataTag);
                        } else if (mDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER_ADAPTER)) {
                            NotificationResponseBaseModel responseLogNotificationModel = new Gson().fromJson(data, NotificationResponseBaseModel.class);
                            this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, responseLogNotificationModel, mOldDataTag);
                        } else if (mDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION_ADAPTER)) {
                            NotificationResponseBaseModel responseLogNotificationModel = new Gson().fromJson(data, NotificationResponseBaseModel.class);
                            this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, responseLogNotificationModel, mOldDataTag);
                        }

                }
            }

        } catch (JsonSyntaxException e) {
            CommonMethods.Log(TAG, "JsonException" + e.getMessage());
            mConnectionListener.onResponse(ConnectionListener.PARSE_ERR0R, null, mOldDataTag);
        }

    }

    @Override
    public void setPostParams(CustomResponse customResponse) {
        this.customResponse = customResponse;
    }

    @Override
    public void setPostParams(Map<String, String> postParams) {
        this.mPostParams = postParams;
    }

    @Override
    public void setHeaderParams(Map<String, String> headerParams) {
        this.mHeaderParams = headerParams;
    }

    @Override
    public void abort() {
        if (jsonRequest != null)
            jsonRequest.cancel();
        if (stringRequest != null)
            stringRequest.cancel();
    }

    @Override
    public void setUrl(String url) {
        this.mURL = url;
    }

    @Override
    public void onTimeout(RequestTimer requestTimer) {
        if (mContext instanceof AppCompatActivity) {
            if (mContext != null) {
                ((AppCompatActivity) this.mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                    }
                });
            }
        }

        RequestPool.getInstance(mContext)
                .cancellAllPreviousRequestWithSameTag(requestTag);
        mConnectionListener.onTimeout(this);
    }

    public void showErrorDialog(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void tokenRefreshRequest() {
        loginRequest();
        // Commented as login API is not implemented yet.
       /* String url = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.SERVER_PATH, mContext) + Config.URL_LOGIN;
        CommonMethods.Log(TAG, "Refersh token while sending refresh token api: " + RescribePreferencesManager.getString(RescribeConstants.REFRESH_TOKEN, mContext));
        Map<String, String> headerParams = new HashMap<>();
        headerParams.putAll(mHeaderParams);
        headerParams.remove(RescribeConstants.CONTENT_TYPE);
        headerParams.put(RescribeConstants.CONTENT_TYPE, RescribeConstants.APPLICATION_URL_ENCODED);

        Map<String, String> postParams = new HashMap<>();
        postParams.put(RescribeConstants.GRANT_TYPE_KEY, RescribeConstants.REFRESH_TOKEN);
        postParams.put(RescribeConstants.REFRESH_TOKEN, RescribePreferencesManager.getString(RescribeConstants.REFRESH_TOKEN, mContext));
        postParams.put(RescribeConstants.CLIENT_ID_KEY, RescribeConstants.CLIENT_ID_VALUE);

        stringRequest(url, Request.Method.POST, headerParams, postParams, true);*/
    }

    private void loginRequest() {
        CommonMethods.Log(TAG, "Refresh token while sending refresh token api: ");
        String url = Config.BASE_URL + Config.LOGIN_URL;

        LoginRequestModel loginRequestModel = new LoginRequestModel();

        loginRequestModel.setMobileNumber(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER, mContext));
        loginRequestModel.setPassword(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PASSWORD, mContext));
        if (!(RescribeConstants.BLANK.equalsIgnoreCase(loginRequestModel.getMobileNumber()) &&
                RescribeConstants.BLANK.equalsIgnoreCase(loginRequestModel.getPassword()))) {
            Map<String, String> headerParams = new HashMap<>();
            headerParams.putAll(mHeaderParams);
            Device device = Device.getInstance(mContext);

            headerParams.put(RescribeConstants.CONTENT_TYPE, RescribeConstants.APPLICATION_JSON);
            headerParams.put(RescribeConstants.DEVICEID, device.getDeviceId());
            headerParams.put(RescribeConstants.OS, device.getOS());
            headerParams.put(RescribeConstants.OSVERSION, device.getOSVersion());
            headerParams.put(RescribeConstants.DEVICE_TYPE, device.getDeviceType());
            CommonMethods.Log(TAG, "setHeaderParams:" + headerParams.toString());
            jsonRequest(url, Request.Method.POST, headerParams, loginRequestModel, true);
        } else {
            mConnectionListener.onResponse(ConnectionListener.PARSE_ERR0R, null, mOldDataTag);
        }
    }
}