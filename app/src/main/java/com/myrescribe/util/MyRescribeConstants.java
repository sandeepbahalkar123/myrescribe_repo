package com.myrescribe.util;


/**
 * @author Sandeep Bahalkar
 */
public class MyRescribeConstants {

    public static final String MYRESCRIBE_LOG_FOLDER = "MyRescribe_LOG";
    public static final String MYRESCRIBE_LOG_FILE = "MyRescribe_LOG_FILE.txt";
    public static final String MEDICINE_NAME = "MEDICINE_NAME";
    //This is for bydefault textcolor,headercolor,buttoncolor etc.
    public static String HEADER_COLOR = "#E4422C";
    public static String BUTTON_TEXT_COLOR = "#FFFFFF";
    public static String TEXT_COLOR = "#000000";
    public static final String ID = "_id";
    public static final String USER_ID = "User-ID";

    public static class PRESCRIPTION_LIST_PARAMS {
        public static final String PATIENT_NAME = "User-ID";
        public static final String FILE_TYPE = "fileType";
        public static final String DATE_TYPE = "dateType";
        public static final String FROM_DATE = "fromDate";
        public static final String TO_DATE = "toDate";
        public static final String ANNOTATION_TEXT = "annotationText";
        public static final String DOC_TYPE_ID = "DocTypeId";
    }

    public static final String DEVICEID = "Device-Id";
    public static final String OS = "OS";
    public static final String OSVERSION = "OS-Version";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String TOKEN_TYPE = "token_type";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String LDPI = "/LDPI/";
    public static final String MDPI = "/MDPI/";
    public static final String HDPI = "/HDPI/";
    public static final String XHDPI = "/XHDPI/";
    public static final String XXHDPI = "/XXHDPI/";
    public static final String XXXHDPI = "/XXXHDPI/";
    public static final String TABLET = "Tablet";
    public static final String PHONE = "Phone";


    public static final String CONTENT_TYPE = "Content-Type";
    public static final String AUTHORIZATION = "Authorization";
    public static final String AUTHORIZATION_TOKEN = "Authorization-Token";
    public static final String AUTH_KEY = "Auth-Key";
    public static final String CLIENT_SERVICE = "Client-Service";
    public static final String GRANT_TYPE_KEY = "grant_type";
    public static final String APPLICATION_URL_ENCODED = "application/x-www-form-urlencoded; charset=UTF-8";
    public static final String APPLICATION_JSON = "application/json; charset=utf-8";

    //--- Request Params
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String LOGIN_SUCCESS = "LOGIN_SUCCESS";
    public static final String CLIENT_ID_KEY = "client_id";
    public static final String CLIENT_ID_VALUE = "334a059d82304f4e9892ee5932f81425";
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    //Click codes
    public static final String CLICK_DELETE = MyRescribeConstants.BLANK + 0;
    public static final String CLICK_EXPAND_VIEW = MyRescribeConstants.BLANK + 1;
    public static final String CLICK_EDIT = MyRescribeConstants.BLANK + 2;

    // Connection codes

    public static final String TASK_PRESCRIPTION_LIST = MyRescribeConstants.BLANK + 1;

    public static class DATE_PATTERN {
        public static String YYYY_MM_DD = "yyyy-MM-dd";
        public static String YYYY_MM_DD_hh_mm_ss = "yyyy-MM-dd hh:mm:ss";
        public static String DD_MM_YYYY_hh_mm = "dd/MM/yyyy hh:mm aa";

        public static String HH_MM = "hh:mm";
        public static String EEE_MMM_DD_MMM = "EEEE dd MMM yyyy HH:mm";
        public final static String yyyy_MM_dd = "yyyy-MM-dd";
        public final static String DD_MM_YYYY = "dd/MM/yyyy";
        public final static String EEEE_dd_MMM_yyyy_hh_mm_a = "EEEE dd MMM yyyy | hh:mm a";
    }

    public static final String TIME = "time";
    public static final String DATE = "date";
    public static final String BLANK = "";
    public static final String DATA = "DATA";

    public static class TIME_STAMPS {
        public static int ONE_SECONDS = 1000;
        public static int TWO_SECONDS = 2000;
        public static int THREE_SECONDS = 3000;
    }

    public static final Integer SUCCESS = 200;

}
