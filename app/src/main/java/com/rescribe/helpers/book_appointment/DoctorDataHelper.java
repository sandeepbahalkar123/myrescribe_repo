package com.rescribe.helpers.book_appointment;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.ServicesModel;
import com.rescribe.model.book_appointment.complaints.request_complaints.DoctorListByComplaintModel;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.book_appointment.doctor_data.DoctorServicesModel;
import com.rescribe.model.book_appointment.doctor_data.request_model.BookAppointTokenNotifyAlertRequestModel;
import com.rescribe.model.book_appointment.doctor_data.request_model.RequestDoctorListBaseModel;
import com.rescribe.model.book_appointment.doctor_data.request_model.RequestFavouriteDoctorModel;
import com.rescribe.model.book_appointment.filterdrawer.request_model.BookAppointFilterRequestModel;
import com.rescribe.model.dashboard_api.DashBoardBaseModel;
import com.rescribe.model.dashboard_api.DashboardDataModel;
import com.rescribe.network.ConnectRequest;
import com.rescribe.network.ConnectionFactory;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by jeetal on 19/9/17.
 */

public class DoctorDataHelper implements ConnectionListener {

    String TAG = this.getClass().getName();
    Context mContext;
    HelperResponse mHelperResponseManager;

    private static DoctorServicesModel receivedDoctorServicesModel = null;
    private static DashboardDataModel mDashboardDataModel = null;

    public DoctorDataHelper(Context context, HelperResponse servicesActivity) {
        this.mContext = context;
        this.mHelperResponseManager = servicesActivity;
    }

    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag == RescribeConstants.TASK_GET_DOCTOR_DATA) {
                    BookAppointmentBaseModel receivedBookAppointmentBaseModel = (BookAppointmentBaseModel) customResponse;
                    if (receivedBookAppointmentBaseModel != null) {
                        DoctorServicesModel mReceivedDoctorServicesModel = receivedBookAppointmentBaseModel.getDoctorServicesModel();
                        if (mReceivedDoctorServicesModel != null) {
                            receivedDoctorServicesModel = mReceivedDoctorServicesModel;
                            for (int i = 0; i < receivedDoctorServicesModel.getDoctorList().size(); i++) {
                                DoctorList doctorList = receivedDoctorServicesModel.getDoctorList().get(i);
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
                } else if (mOldDataTag == RescribeConstants.TASK_GET_COMPLAINTS) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                } else if (mOldDataTag == RescribeConstants.TASK_GET_BOOK_APPOINT_DRAWER_CONFIG) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                } else if (mOldDataTag == RescribeConstants.TASK_GET_REVIEW_LIST) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                } else if (mOldDataTag == RescribeConstants.TASK_BOOK_APPOINTMENT_SERVICES) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                } else if (mOldDataTag == RescribeConstants.TASK_GET_DOCTOR_LIST_BY_COMPLAINT) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                } else if (mOldDataTag == RescribeConstants.TASK_SERVICES_DOC_LIST_FILTER) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                } else if (mOldDataTag == RescribeConstants.TASK_TIME_SLOT_TO_BOOK_APPOINTMENT || mOldDataTag == RescribeConstants.TASK_TIME_SLOT_TO_BOOK_APPOINTMENT_WITH_DOCTOR_DETAILS) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                } else if (mOldDataTag == RescribeConstants.TASK_SET_FAVOURITE_DOCTOR) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                } else if (mOldDataTag == RescribeConstants.TASK_DASHBOARD_API) {

                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                } else if (mOldDataTag == RescribeConstants.TASK_RECENT_VISIT_DOCTOR_PLACES_DATA) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                } else if (mOldDataTag == RescribeConstants.TASK_GET_TOKEN_NUMBER_OTHER_DETAILS) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                } else {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                }
                break;
            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG, mContext.getString(R.string.parse_error));
                ((HelperResponse) mContext).onParseError(mOldDataTag, mContext.getString(R.string.parse_error));
                break;
            case ConnectionListener.SERVER_ERROR:
                CommonMethods.Log(TAG, mContext.getString(R.string.server_error));
                ((HelperResponse) mContext).onServerError(mOldDataTag, mContext.getString(R.string.server_error));

                break;
            case ConnectionListener.NO_CONNECTION_ERROR:
                CommonMethods.Log(TAG, mContext.getString(R.string.no_connection_error));
                ((HelperResponse) mContext).onNoConnectionError(mOldDataTag, mContext.getString(R.string.no_connection_error));
                break;
            case ConnectionListener.NO_INTERNET:
                CommonMethods.Log(TAG, mContext.getString(R.string.no_connection_error));
                ((HelperResponse) mContext).onNoConnectionError(mOldDataTag, mContext.getString(R.string.no_connection_error));
                break;
            default:
                CommonMethods.Log(TAG, mContext.getString(R.string.default_error));
                break;
        }
    }

    @Override
    public void onTimeout(ConnectRequest request) {

    }

    public void doGetDoctorData(String city, String address, HashMap<String, String> mReceivedComplaintHashMap) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_GET_DOCTOR_DATA, Request.Method.POST, true);
        mConnectionFactory.setHeaderParams();

        String url = Config.DOCTOR_LIST_BY_LOCATION;
        RequestDoctorListBaseModel requestDoctorListBaseModel = new RequestDoctorListBaseModel();
        requestDoctorListBaseModel.setArea(address.trim());
        requestDoctorListBaseModel.setCityName(city.trim());
        requestDoctorListBaseModel.setDate(CommonMethods.getFormattedDate(CommonMethods.getCurrentDate(),RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.YYYY_MM_DD));
        requestDoctorListBaseModel.setTime(CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.HH_mm));
        requestDoctorListBaseModel.setPatientId(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext)));
        //--------In case of complaint added by user from ComplaintFragment.java---
        if (mReceivedComplaintHashMap != null) {
            if (mReceivedComplaintHashMap.size() > 0) {
                url = Config.GET_DOCTOR_LIST_BY_COMPLAINT;
                requestDoctorListBaseModel.setComplaint(mReceivedComplaintHashMap.get(mContext.getString(R.string.complaint1)));
                //requestDoctorListBaseModel.setComplaint2(mReceivedComplaintHashMap.get(mContext.getString(R.string.complaint2)));
            }
        }
        //-----------
        mConnectionFactory.setPostParams(requestDoctorListBaseModel);
        mConnectionFactory.setUrl(url);
        mConnectionFactory.createConnection(RescribeConstants.TASK_GET_DOCTOR_DATA);
        /*try {
            InputStream is = mContext.getAssets().open("doctor_data_22_nov_2017.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Log.e(TAG, "doctor_data_22_nov_2017" + json);

            Gson gson = new Gson();
            BookAppointmentBaseModel bookAppointmentBaseModel = gson.fromJson(json, BookAppointmentBaseModel.class);
            onResponse(ConnectionListener.RESPONSE_OK, bookAppointmentBaseModel, RescribeConstants.TASK_GET_DOCTOR_DATA);

        } catch (IOException ex) {
            ex.printStackTrace();
        }*/
    }

    public void doGetDrawerFilterConfigurationData(String cityName) {
/*        try {
            InputStream is = mContext.getAssets().open("book_appointment_filter_drawer.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Log.e(TAG, "book_appointment_filter_drawer" + json);

            BookAppointFilterBaseModel bookAppointmentBaseModel = new Gson().fromJson(json, BookAppointFilterBaseModel.class);
            onResponse(ConnectionListener.RESPONSE_OK, bookAppointmentBaseModel, RescribeConstants.TASK_GET_BOOK_APPOINT_DRAWER_CONFIG);

        } catch (IOException ex) {
            ex.printStackTrace();
        }*/

        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_GET_BOOK_APPOINT_DRAWER_CONFIG, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.GET_BOOK_APPOINTMENT_FILTER_APPOINTMENT_DATA + cityName);
        mConnectionFactory.createConnection(RescribeConstants.TASK_GET_BOOK_APPOINT_DRAWER_CONFIG);
    }

    public void doGetComplaintsList() {

        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_GET_COMPLAINTS, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.GET_COMPLAINTS_LIST);
        mConnectionFactory.createConnection(RescribeConstants.TASK_GET_COMPLAINTS);

       /* try {
            InputStream is = mContext.getAssets().open("complaint_list");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Log.e(TAG, "complaint_list" + json);

            ComplaintsBaseModel complaintsBaseModel = new Gson().fromJson(json, ComplaintsBaseModel.class);
            onResponse(ConnectionListener.RESPONSE_OK, complaintsBaseModel, RescribeConstants.TASK_GET_COMPLAINTS);

        } catch (IOException ex) {
            ex.printStackTrace();
        }*/
    }

    public void doGetReviewsList(String docId) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_GET_REVIEW_LIST, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.REVIEW_URL);
        mConnectionFactory.createConnection(RescribeConstants.TASK_GET_REVIEW_LIST);
    }

    public void doGetDoctorListByComplaint(String cityName, String area, String complaint1, String complaint2) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_GET_DOCTOR_LIST_BY_COMPLAINT, Request.Method.POST, true);
        DoctorListByComplaintModel doctorListByComplaintModel = new DoctorListByComplaintModel();
        doctorListByComplaintModel.setCityName(cityName);
        doctorListByComplaintModel.setArea(area);
        doctorListByComplaintModel.setPatientId(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext)));
        doctorListByComplaintModel.setComplaint1(complaint1);
        doctorListByComplaintModel.setComplaint2(complaint2);
        mConnectionFactory.setPostParams(doctorListByComplaintModel);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.GET_DOCTOR_LIST_BY_COMPLAINT);
        mConnectionFactory.createConnection(RescribeConstants.TASK_GET_DOCTOR_LIST_BY_COMPLAINT);
    }

    public void setFavouriteDoctor(Boolean isFavourite, int docId) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_SET_FAVOURITE_DOCTOR, Request.Method.POST, true);
        RequestFavouriteDoctorModel requestFavouriteDoctorModel = new RequestFavouriteDoctorModel();
        requestFavouriteDoctorModel.setPatientId(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext)));
        requestFavouriteDoctorModel.setFavouriteflag(isFavourite);
        requestFavouriteDoctorModel.setDoctorId(docId);
        mConnectionFactory.setPostParams(requestFavouriteDoctorModel);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.SET_FAVOURITE_URL);
        mConnectionFactory.createConnection(RescribeConstants.TASK_SET_FAVOURITE_DOCTOR);
    }

    public void doFilteringOnSelectedConfig(BookAppointFilterRequestModel requestModel, HashMap<String, String> mReceivedComplaintHashMap) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_SERVICES_DOC_LIST_FILTER, Request.Method.POST, true);

        //---------
        String s = RescribeApplication.getUserSelectedLocationInfo().get(mContext.getString(R.string.location));
        if (s != null) {
            String[] split = s.split(",");
            requestModel.setCityName(split[1].trim());
            requestModel.setArea(split[0].trim());
        }
        requestModel.setPatientId(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext)));
        //---------
        //--------In case of complaint added by user from ComplaintFragment.java---
        if (mReceivedComplaintHashMap != null) {
            if (mReceivedComplaintHashMap.size() > 0) {
                requestModel.setComplaint(mReceivedComplaintHashMap.get(mContext.getString(R.string.complaint1)));
             }
        }
        //-----------

        mConnectionFactory.setPostParams(requestModel);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.SERVICES_DOC_LIST_FILTER_URL);
        mConnectionFactory.createConnection(RescribeConstants.TASK_SERVICES_DOC_LIST_FILTER);
    }

    public void getTimeSlotToBookAppointmentWithDoctor(String docId, String locationID, String date, boolean isReqDoctorData, String taskID) {

        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, taskID, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();

        String currentTimeStamp = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.HH_mm);

        String url = Config.TIME_SLOT_TO_BOOK_APPOINTMENT + "docId=" + docId + "&locationId=" + locationID + "&date=" + date + "&time=" + currentTimeStamp;

        if (isReqDoctorData) {
            url = url + "&docDetailReq=" + isReqDoctorData + "&patientId=" + RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);
        }

        mConnectionFactory.setUrl(url);
        mConnectionFactory.createConnection(taskID);//RescribeConstants.TASK_TIME_SLOT_TO_BOOK_APPOINTMENT

       /* try {
            InputStream is = mContext.getAssets().open("book_appointment_select_time");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Log.e(TAG, "book_appointment_select_time" + json);

            TimeSlotListBaseModel slotListBaseModel = new Gson().fromJson(json, TimeSlotListBaseModel.class);
            onResponse(ConnectionListener.RESPONSE_OK, slotListBaseModel, RescribeConstants.TASK_SLOT_TIME_TO_BOOK_APPOINTMENT);

        } catch (IOException ex) {
            ex.printStackTrace();
        }*/
    }

    public void doGetRecentlyVisitedDoctorPlacesData() {

        HashMap<String, String> userSelectedLocationInfo = RescribeApplication.getUserSelectedLocationInfo();
        if (userSelectedLocationInfo.get(mContext.getString(R.string.location)) != null) {
            String locationReceived = userSelectedLocationInfo.get(mContext.getString(R.string.location));
            String[] locationArea = locationReceived.split(",");
            ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_RECENT_VISIT_DOCTOR_PLACES_DATA, Request.Method.GET, true);
            mConnectionFactory.setHeaderParams();
            mConnectionFactory.setUrl(Config.GET_TASK_RECENT_VISIT_DOCTOR_PLACES_DATA + RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext) + mContext.getString(R.string.city) + locationArea[1].trim());
            mConnectionFactory.createConnection(RescribeConstants.TASK_RECENT_VISIT_DOCTOR_PLACES_DATA);
        }
    }


    public void getTokenNumberDetails(String docId, int locationID, String selectedTimeStamp) {

        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_GET_TOKEN_NUMBER_OTHER_DETAILS, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();

        String url = Config.GET_TOKEN_NUMBER_OTHER_DETAILS + "docId=" + docId + "&locationId=" + locationID + "&patientId=" + RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);

        if (selectedTimeStamp != null) {
            url = url + "&time=" + selectedTimeStamp;
        }

        mConnectionFactory.setUrl(url);
        mConnectionFactory.createConnection(RescribeConstants.TASK_GET_TOKEN_NUMBER_OTHER_DETAILS);

        /*try {
            InputStream is = mContext.getAssets().open("get_token_api_result.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Log.e(TAG, "get_token_api_result" + json);

            ClinicTokenDetailsBaseModel slotListBaseModel = new Gson().fromJson(json, ClinicTokenDetailsBaseModel.class);
            onResponse(ConnectionListener.RESPONSE_OK, slotListBaseModel, RescribeConstants.TASK_GET_TOKEN_NUMBER_OTHER_DETAILS);

        } catch (IOException ex) {
            ex.printStackTrace();
        }*/
    }

    public void doSetTokenNotificationReminder(String time, int docId, int locationID) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_TO_SET_TOKEN_NOTIFICATION_REMAINDER, Request.Method.POST, true);
        mConnectionFactory.setHeaderParams();

        BookAppointTokenNotifyAlertRequestModel requestDoctorListBaseModel = new BookAppointTokenNotifyAlertRequestModel();
        requestDoctorListBaseModel.setDocId(docId);
        requestDoctorListBaseModel.setOs("android");
        requestDoctorListBaseModel.setTime(time);
        requestDoctorListBaseModel.setLocationId(locationID);
        requestDoctorListBaseModel.setPatientId(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext)));

        mConnectionFactory.setPostParams(requestDoctorListBaseModel);
        mConnectionFactory.setUrl(Config.TO_SET_TOKEN_NOTIFICATION_REMAINDER_ALERT);
        mConnectionFactory.createConnection(RescribeConstants.TASK_TO_SET_TOKEN_NOTIFICATION_REMAINDER);

    }

    public static DoctorServicesModel getReceivedDoctorServicesModel() {
        return receivedDoctorServicesModel;
    }

    public static void setReceivedDoctorServicesModel(DoctorServicesModel receivedDoctorServicesModel) {
        DoctorDataHelper.receivedDoctorServicesModel = receivedDoctorServicesModel;
    }

    public void doGetTokenUnreadNotification() {

        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_TO_GET_TOKEN_REMAINDER_UNREAD_NOTIFICATIONS, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();

        String url = Config.TO_GET_TOKEN_REMAINDER_UNREAD_NOTIFICATIONS + "?patientId=" + RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);

        mConnectionFactory.setUrl(url);
        mConnectionFactory.createConnection(RescribeConstants.TASK_TO_GET_TOKEN_REMAINDER_UNREAD_NOTIFICATIONS);

    }

    public void doRejectBookAppointReceivedToken(String time, int docId, int locationID) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_TO_REJECT_RECEIVED_TOKEN_NOTIFICATION_REMAINDER, Request.Method.POST, true);
        mConnectionFactory.setHeaderParams();

        BookAppointTokenNotifyAlertRequestModel requestDoctorListBaseModel = new BookAppointTokenNotifyAlertRequestModel();
        requestDoctorListBaseModel.setDocId(docId);
        requestDoctorListBaseModel.setTime(time);
        requestDoctorListBaseModel.setLocationId(locationID);
        requestDoctorListBaseModel.setPatientId(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext)));

        mConnectionFactory.setPostParams(requestDoctorListBaseModel);
        mConnectionFactory.setUrl(Config.TO_REJECT_RECEIVED_TOKEN_NOTIFICATION_REMAINDER);
        mConnectionFactory.createConnection(RescribeConstants.TASK_TO_REJECT_RECEIVED_TOKEN_NOTIFICATION_REMAINDER);

    }

    public void doConfirmBookAppointReceivedToken(String time, int docId, int locationID, int tokenNumber) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_TO_UNREAD_TOKEN_REMAINDER_CONFIRMATION, Request.Method.POST, true);
        mConnectionFactory.setHeaderParams();

        BookAppointTokenNotifyAlertRequestModel requestDoctorListBaseModel = new BookAppointTokenNotifyAlertRequestModel();
        requestDoctorListBaseModel.setDocId(docId);
        requestDoctorListBaseModel.setTime(time);
        requestDoctorListBaseModel.setLocationId(locationID);
        requestDoctorListBaseModel.setTokenNumber(tokenNumber);
        requestDoctorListBaseModel.setPatientId(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext)));

        mConnectionFactory.setPostParams(requestDoctorListBaseModel);
        mConnectionFactory.setUrl(Config.TO_UNREAD_TOKEN_REMAINDER_CONFIRMATION);
        mConnectionFactory.createConnection(RescribeConstants.TASK_TO_UNREAD_TOKEN_REMAINDER_CONFIRMATION);

    }

}
