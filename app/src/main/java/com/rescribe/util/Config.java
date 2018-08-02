package com.rescribe.util;

import android.content.Context;

/**
 * Created by Sandeep Bahalkar
 */
public class Config {
    public static final String HTTP = "http://";
    public static final String API = "/api/";
    public static final String TOKEN_TYPE = "Bearer";
    public static final String LOGIN_URL = "authApi/authenticate/patientLogin";
    public static final String VERIFY_SIGN_UP_OTP = "authApi/authenticate/verifyPatientOTP";

    public static final String VERIFY_FORGET_PASSWORD_OTP = "authApi/authenticate/verifyOtpByNumber"; // new
    public static final String RESET_PASSWORD = "authApi/authenticate/resetpassword"; // new

    public static final String SOCIAL_SIGN_IN_URL = "authApi/authenticate/patientSocialLogin";

    public static final String SIGN_UP_URL = "authApi/authenticate/patientSignUp";
    public static final String DOCTOR_LIST_BY_LOCATION = "api/doctors/getDoctorListByLocation";
    public static final String REVIEW_URL = "api/doctors/getReviewsForDoctor?docId=10";
    public static final String GET_DOCTOR_LIST_BY_COMPLAINT = "api/doctors/getDocListByCityAreaComplaint";

    //    public static final String GET_DASHBOARD_DATA = "api/dashboard/getDashboardDetails?patientId=";
    public static final String GET_DASHBOARD_DATA = "api/dashboard/getDocListByCategory?patientId=";
    public static final String GET_DOCTORLIST_DATA = "api/dashboard/getDoctorDetails?patientId=";

    public static final String GET_TASK_RECENT_VISIT_DOCTOR_PLACES_DATA = "api/doctors/getDocCountByArea?patientId=";
    public static final String CONFIRM_APPOINTMENT = "api/patient/bookAppointment ";
    public static final String CANCEL_RESCHDULE_APPOINTMENT = "api/patient/updateAppointmentStatus";
    public static final String CANCEL_TOKEN_NUMBER = "api/doctors/cancelTokenNo";
    public static final String FOLLOW_UP_RESPONSE = "api/patient/notificationResponse";
    public static final String PROFILE_UPLOAD = "api/upload/uploadPatientImage";

    public static boolean DEV_BUILD = true;

    // QA

//    public static final String BROKER = "tcp://drrescribe.com:1883"; // Dr Rescribe IP
//    public static String BASE_URL = "http://drrescribe.com:3003/";

    // Live

    public static final String BROKER = "tcp://rescribe.in:1883"; // Dr Rescribe IP
    public static String BASE_URL = "http://rescribe.in:3003/";


    //---------------------
    public Context mContext;
    //Declared all URL used in app here
    public static final String LOGIN_WITH_OTP_URL = "authApi/authenticate/patientOtpLogin";

    public static final String CHANGE_PASS_WITH_OTP = "authApi/authenticate/forgotpassword";
    public static final String TERMS_AND_CONDITIONS_URL = "https://rescribe.in/terms-and-conditions.html";
    public static final String PRIVACY_POLICY = "https://rescribe.in/privacy-policy.html";

    public static final String PRESCRIPTION_URL = "api/patient/getPatientPrescriptions?patientId=";
    public static final String DOCTOR_LIST_URL = "api/patient/getDoctorList?patientId=";
    public static final String ONE_DAY_VISIT_URL = "api/patient/getPatientOneDayVisit?opdId=";
    public static final String DOCTOR_LIST_FILTER_URL = "api/patient/searchDoctors";
    public static final String NOTIFICATION_URL = "api/patient/getPrescriptionNotifications?patientId=";
    public static final String RESPOND_TO_NOTIFICATION_URL = "api/patient/logMedicinesTakenInfo";
    public static final String FILTER_DOCTOR_LIST = "api/patient/getDoctorNameList";
    public static final String FILTER_DOCTOR_SPECIALIST_LIST = "api/patient/getDoctorSpeciality";
    public static final String CASE_DETAILS_LIST = "api/patient/getVitalCaseHeadings";
    public static final String APPOINTMENTS = "api/patient/appointments";

    public static final String MY_RECORDS_DOCTOR_LIST = "api/doctors/getDoctorsWithPatientVisits";
    public static final String LIST_ALL_MY_RECORD = "api/records/getRecordList?patientId=";

    public static final String MY_RECORDS_UPLOAD = "api/upload/myRecords";
    public static final String MY_RECORDS_ADD_DOCTOR = "api/doctors/addDoctor";

    public static final String SEND_MSG_TO_DOCTOR = "api/chat/sendMsgToDoctor";
    public static final String CHAT_HISTORY = "api/chat/getChatHistory?";

    public static final String DOCTOR_CHAT_LIST_URL = "api/doctors/getChatDoctorList";

    public static final String CHAT_USERS = "api/chat/getChatTabUsers";

    public static final String CHAT_FILE_UPLOAD = "api/upload/chatDoc";

    public static final String SET_FAVOURITE_URL = "api/doctors/setFavoriteDoctor";
    public static final String GET_COMPLAINTS_LIST = "api/doctors/getComplaintList";

    public static final String SERVICES_DOC_LIST_FILTER_URL = "api/doctors/filterDoctors";
    public static final String TIME_SLOT_TO_BOOK_APPOINTMENT = "api/doctors/getDocOpenTimeSlots?";

    //------ BOOK_APPONINT_TOKEN
    public static final String GET_TOKEN_NUMBER_OTHER_DETAILS = "api/doctors/getTokenDetails?";
    public static final String TO_SET_TOKEN_NOTIFICATION_REMAINDER_ALERT = "api/patient/addNotificationReminder";
    public static final String TO_REJECT_RECEIVED_TOKEN_NOTIFICATION_REMAINDER = "api/patient/rejectTokenNo";
    public static final String TO_GET_TOKEN_REMAINDER_UNREAD_NOTIFICATIONS = "api/patient/getTokenUnreadNotifications";
    public static final String TO_UNREAD_TOKEN_REMAINDER_CONFIRMATION = "api/doctors/confirmTokenNo";
    //------ BOOK_APPONINT_TOKEN

    //----- vital graph-------------
    public static final String TASK_GET_PATIENT_VITAL_LIST = "api/patient/getPatientVitalList";
    public static final String TASK_GET_PATIENT_VITAL_DETAIL = "api/patient/getPatientVitalDetail";
    public static final String TASK_PATIENT_ADD_VITAL_MANUALLY = "api/patient/addVitalsManually";
    public static final String TASK_GET_PATIENT_VITAL_TRACKER_LIST = "api/patient/getVitalTrackerList";
    public static final String GET_BOOK_APPOINTMENT_FILTER_APPOINTMENT_DATA = "api/doctors/getFilterCriteriaByCity?cityName=";
    //----- vital graph-------------
    public static final String LOGOUT = "api/patient/logPatientSignOut";
    public static final String ACTIVE = "api/patient/logPatientActivity";
    //-----------SAVED ARTICLE-----------------------
    public static final String TO_GET_SAVED_ARTICLES = "api/patient/getSavedArticleList?patientId=";
    public static final String TO_GET_HEALTH_EDUCATION_SAVED_ARTICLES = "api/patient/getArticleList?patientId=";

    public static final String TO_SAVE_ARTICLE_TO_SERVER = "api/patient/saveArticle";
    //-----------INVESTIGATIONS -----------------------

    public static final String INVESTIGATION_LIST = "api/patient/getInvestigationNotifications";
    public static final String INVESTIGATION_UPLOAD_BY_GMAIL = "api/patient/updateGmailInvestigationUploadStatus";
    public static final String INVESTIGATION_UPLOAD = "api/upload/InvestigationDocs";
    public static final String DO_SKIP_INVESTIGATION = "api/patient/skipInvestigationNotification";

    //-----------------------------------------------


}



