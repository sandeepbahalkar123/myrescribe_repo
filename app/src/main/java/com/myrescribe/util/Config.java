package com.myrescribe.util;

import android.content.Context;

/**
 * Created by Sandeep Bahalkar
 */
public class Config {
    private static final String TAG = "MyRescribe/Config";
    public static final String HTTP = "http://";
    public static final String API = "/api/";
    public static final String TOKEN_TYPE = "Bearer";
    public static final String LOGIN_URL = "authApi/authenticate/login";
    public static boolean DEV_BUILD = true;
    public static String BASE_URL = "http://192.168.0.182:3003/";
    public Context mContext;
    //Declared all URL used in app here

    public static final String PRESCRIPTION_URL = "api/patient/getPatientPrescriptions/4092";

    public static final String ONE_DAY_VISIT_URL = "api/patient/getPatientOneDayVisit/4092/10/08-06-2017";
    public static final String DOCTOR_LIST_URL = "api/patient/getDoctorList?patientId=4092&year=";

}



