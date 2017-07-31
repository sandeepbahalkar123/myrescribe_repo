package com.myrescribe.network;

/**
 * @author Sandeep Bahalkar
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.myrescribe.R;
import com.myrescribe.helpers.database.AppDBHelper;
import com.myrescribe.interfaces.ConnectionListener;
import com.myrescribe.interfaces.Connector;
import com.myrescribe.interfaces.CustomResponse;

import com.myrescribe.model.doctors.doctor_info.DoctorModel;

import com.myrescribe.model.filter.CaseDetailsListModel;
import com.myrescribe.model.filter.FilterDoctorSpecialityListModel;
import com.myrescribe.model.filter.FilterDoctorListModel;
import com.myrescribe.model.login.SignUpModel;

import com.myrescribe.model.notification.AppointmentsNotificationData;
import com.myrescribe.model.notification.AppointmentsNotificationModel;
import com.myrescribe.model.notification.NotificationModel;

import com.myrescribe.model.prescription_response_model.PrescriptionModel;

import com.myrescribe.model.login.LoginModel;
import com.myrescribe.model.requestmodel.login.LoginRequestModel;
import com.myrescribe.model.response_model_notification.ResponseLogNotificationModel;
import com.myrescribe.model.visit_details.VisitDetailsModel;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.ui.activities.SplashScreenActivity;
import com.myrescribe.ui.customesViews.CustomProgressDialog;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.Config;
import com.myrescribe.util.MyRescribeConstants;
import com.myrescribe.util.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestManager extends ConnectRequest implements Connector, RequestTimer.RequestTimerListener {
    private static final String TAG = "MyRescribe/RequestManager";
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
                jsonRequest();
            } else {
                jsonRequest();
            }
        } else {

            if (isOffline) {
                succesResponse(getOfflineData(), false);
            } else {
                mConnectionListener.onResponse(ConnectionListener.NO_INTERNET, null, mOldDataTag);
            }

            if (mViewById != null)
                CommonMethods.showSnack(mViewById, mContext.getString(R.string.internet));
            else
                CommonMethods.showToast(mContext, mContext.getString(R.string.internet));
        }
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
        jsonRequest.setRetryPolicy(new

                DefaultRetryPolicy(CONNECTION_TIME_OUT, N0OF_RETRY,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );
        jsonRequest.setTag(requestTag);
        requestTimer.start();
        RequestPool.getInstance(this.mContext).

                addToRequestQueue(jsonRequest);


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

        stringRequest.setRetryPolicy(new

                DefaultRetryPolicy(CONNECTION_TIME_OUT, N0OF_RETRY,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );
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

//                    MyRescribePreferencesManager.clearSharedPref(mContext);
//                    Intent intent = new Intent(mContext, SplashScreenActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    mContext.startActivity(intent);

                    loginRequest();
                } else
                    mConnectionListener.onResponse(ConnectionListener.SERVER_ERROR, null, mOldDataTag);
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
            return "";
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

            Log.e(TAG, data);

            Gson gson = new Gson();

            if (isTokenExpired) {
                // This success response is for refresh token

                // Need to Add

                LoginModel loginModel = gson.fromJson(data, LoginModel.class);
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.AUTHTOKEN, loginModel.getAuthToken(), mContext);
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, MyRescribeConstants.YES, mContext);
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATEINTID, loginModel.getPatientId(), mContext);

                mHeaderParams.put(MyRescribeConstants.AUTHORIZATION_TOKEN, loginModel.getAuthToken());

                connect();

            } else {
                // This success response is for respective api's

                switch (this.mDataTag) {

                    // Need to add

                    case MyRescribeConstants.TASK_PRESCRIPTION_LIST: //This is for get archived list
                        PrescriptionModel ipTestResponseModel = gson.fromJson(data, PrescriptionModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, ipTestResponseModel, mOldDataTag);
                        break;

                    case MyRescribeConstants.TASK_LOGIN: //This is for get archived list
                        LoginModel loginModel = gson.fromJson(data, LoginModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, loginModel, mOldDataTag);
                        break;
                    case MyRescribeConstants.TASK_ONE_DAY_VISIT: //This is for get archived list
                        VisitDetailsModel visitDetailsModel = gson.fromJson(data, VisitDetailsModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, visitDetailsModel, mOldDataTag);
                        break;
                    case MyRescribeConstants.TASK_DOCTOR_LIST: //This is for get archived list
                        DoctorModel doctorsModel = new Gson().fromJson(data, DoctorModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, doctorsModel, mOldDataTag);
                        break;

                    case MyRescribeConstants.TASK_SIGN_UP: //This is for get sign-up
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, new Gson().fromJson(data, SignUpModel.class), mOldDataTag);
                        break;
                    case MyRescribeConstants.TASK_VERIFY_SIGN_UP_OTP: //This is for to verify sign-up otp
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, new Gson().fromJson(data, LoginModel.class), mOldDataTag);
                        break;
                    case MyRescribeConstants.TASK_NOTIFICATION: //This is for get archived list
                        NotificationModel notificationModel = new Gson().fromJson(data, NotificationModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, notificationModel, mOldDataTag);
                        break;
                    case MyRescribeConstants.TASK_RESPOND_NOTIFICATION: //This is for get archived list
                        ResponseLogNotificationModel responseLogNotificationModel = new Gson().fromJson(data, ResponseLogNotificationModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, responseLogNotificationModel, mOldDataTag);
                        break;

                    case MyRescribeConstants.FILTER_DOCTOR_LIST: //This is for get archived list
                        FilterDoctorListModel filterDoctorListModel = new Gson().fromJson(data, FilterDoctorListModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, filterDoctorListModel, mOldDataTag);
                        break;

                    case MyRescribeConstants.FILTER_DOCTOR_SPECIALITY_LIST: //This is for get archived list
                        FilterDoctorSpecialityListModel filterDoctorSpecialityListModel = new Gson().fromJson(data, FilterDoctorSpecialityListModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, filterDoctorSpecialityListModel, mOldDataTag);
                        break;

                    case MyRescribeConstants.CASE_DETAILS_LIST: //This is for get archived list
                        CaseDetailsListModel caseDetailsListModel = new Gson().fromJson(data, CaseDetailsListModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, caseDetailsListModel, mOldDataTag);
                        break;

                    case MyRescribeConstants.APPOINTMENT_NOTIFICATION: //This is for get archived list
                        AppointmentsNotificationModel appointmentsNotificationModel = new Gson().fromJson(data, AppointmentsNotificationModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, appointmentsNotificationModel, mOldDataTag);
                        break;
                    /*
                    default:
                        //This is for get PDF Data
                        if (mOldDataTag.startsWith(MyRescribeConstants.TASK_GET_PDF_DATA)) {
                            GetPdfDataResponseModel getPdfDataResponseModel = gson.fromJson(data, GetPdfDataResponseModel.class);
                            this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, getPdfDataResponseModel, mOldDataTag);
                        }*/

                }
            }

        } catch (JsonSyntaxException e) {
            Log.d(TAG, "JsonException" + e.getMessage());
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
        // Commented as login API is not implemented yet.
       /* String url = MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.SERVER_PATH, mContext) + Config.URL_LOGIN;
        CommonMethods.Log(TAG, "Refersh token while sending refresh token api: " + MyRescribePreferencesManager.getString(MyRescribeConstants.REFRESH_TOKEN, mContext));
        Map<String, String> headerParams = new HashMap<>();
        headerParams.putAll(mHeaderParams);
        headerParams.remove(MyRescribeConstants.CONTENT_TYPE);
        headerParams.put(MyRescribeConstants.CONTENT_TYPE, MyRescribeConstants.APPLICATION_URL_ENCODED);

        Map<String, String> postParams = new HashMap<>();
        postParams.put(MyRescribeConstants.GRANT_TYPE_KEY, MyRescribeConstants.REFRESH_TOKEN);
        postParams.put(MyRescribeConstants.REFRESH_TOKEN, MyRescribePreferencesManager.getString(MyRescribeConstants.REFRESH_TOKEN, mContext));
        postParams.put(MyRescribeConstants.CLIENT_ID_KEY, MyRescribeConstants.CLIENT_ID_VALUE);

        stringRequest(url, Request.Method.POST, headerParams, postParams, true);*/
    }

    private void loginRequest() {
        CommonMethods.Log(TAG, "Refresh token while sending refresh token api: ");

        Map<String, String> headerParams = new HashMap<>();
        headerParams.putAll(mHeaderParams);
        Map<String, String> postParams = new HashMap<String, String>();
        postParams.put(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER, MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER, mContext));
        postParams.put(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PASSWORD, MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PASSWORD, mContext));
        String url = Config.LOGIN_URL + MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.SERVER_PATH, mContext);
        stringRequest(url, Request.Method.POST, headerParams, postParams, true);
    }
}