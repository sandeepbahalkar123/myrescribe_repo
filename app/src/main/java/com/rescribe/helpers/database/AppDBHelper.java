package com.rescribe.helpers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.rescribe.R;

import com.rescribe.model.chat.MQTTData;
import com.rescribe.model.chat.MQTTMessage;
import com.rescribe.model.dashboard_api.DoctorlistModel.DoctorList;
import com.rescribe.model.dashboard_api.cardviewdatamodel.CategoryList;
import com.rescribe.model.dashboard_api.unread_notification_message_list.UnreadSavedNotificationMessageData;
import com.rescribe.model.investigation.Image;
import com.rescribe.model.investigation.Images;
import com.rescribe.model.investigation.InvestigationData;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AppDBHelper extends SQLiteOpenHelper {

    private final String TAG = "Rescribe/AppDBHelper";

    private static final String MESSAGE_UPLOAD_ID = "message_upload_id";
    private static final String MESSAGE_UPLOAD_STATUS = "message_status";
    private static final String MESSAGE_FILE_UPLOAD = "message_file_data";
    private static final String MESSAGE_UPLOAD_TABLE = "my_message_table";
    private static final String NOTIFICATION_MESSAGE_TABLE = "notification_message_table";

    public static final String CHAT_USER_ID = "user_id";
    public static final String MESSAGE = "message";
    public static final String MESSAGE_TABLE = "unread_messages";

    // Skip Function
    public static final String SKIP_INV_TABLE = "skip_investigation";
    public static final String SKIP_INV_DOCTOR_ID = "skip_inv_doctor_id";

    public static final String INV_ID = "inv_id";
    public static final String INV_NAME = "inv_name";
    public static final String INV_NAME_KEY = "inv_key";
    public static final String INV_OPD_ID = "inv_opd_id";
    public static final String INV_DR_NAME = "inv_dr_name";
    public static final String INV_UPLOAD_STATUS = "upload_status";
    public static final String INV_UPLOADED_IMAGES = "uploaded_images";
    public static final String INVESTIGATION_TABLE = "investigation_table";

    public static final String RECORDS_DOC_ID = "records_doc_id";
    public static final String RECORDS_VISIT_DATE = "records_visit_date";
    public static final String RECORDS_OPDID = "records_opdid";
    public static final String RECORDS_UPLOAD_ID = "records_upload_id";
    public static final String RECORDS_STATUS = "records_upload_status";
    public static final String RECORDS_IMAGE_DATA = "records_image_data";
    public static final String MY_RECORDS_TABLE = "MyRecords";

    private static final String PREFERENCES_TABLE = "preferences_table";
    private static final String DATABASE_NAME = "MyRescribe.sqlite";
    private static final String DB_PATH_SUFFIX = "/data/data/com.rescribe/databases/";
    private static final int DB_VERSION = 1;
    public static final String APP_DATA_TABLE = "PrescriptionData";
    public static final String COLUMN_ID = "dataId";
    public static final String COLUMN_DATA = "data";

    public static String DOC_ID = "cardDocId";
    public static final String FAVOURITE_DATA = "cardType";



    public static final String USER_ID = "userId";
    public static final String BREAKFAST_TIME = "breakfastTime";
    public static final String LUNCH_TIME = "lunchTime";
    public static final String DINNER_TIME = "dinnerTime";
    public static final String SNACKS_TIME = "snacksTime";

    public static final String CARDID = "cardDocId";
    public static final String CARDTYPE = "cardType";

    public static final String DOCID = "doctorId";
    public static final String DOCNAME= "doctorName";
    public static final String PHONENUMBER = "phoneNumber";
    public static final String ICONURL= "iconURL";
    public static final String ABOUTDOCTOR = "aboutDoctor";
    public static final String SPECIALITYID= "specialityId";
    public static final String SPECIALITY = "speciality";
    public static final String RATING= "rating";
    public static final String ISPREMIUM = "isPremium";
    public static final String DOCDEGREE= "docDegree";
    public static final String EXPERIANCE = "experience";
    public static final String ISPAIDSTATUS= "isPaidStatus";
    public static final String CATEGORY = "category";
    public static final String DOCTORGENDER= "doctorGender";
    public static final String MODIFICATIONDATE = "modificationDate";

    public static final String CLINICID = "clinicId";
    public static final String CLINICDOCTORID = "clinicDoctorId";
    public static final String CLINICNAME = "clinicName";
    public static final String CLINICLATITUDE = "clinicLatitude";
    public static final String CLINICLONGITUDE = "clinicLongitude";
    public static final String CLINICADDRESS = "clinicAddress";
    public static final String CLINICAREANAME = "clinicAreaName";
    public static final String CLINICCITYNAME = "clinicCityName";


    public static final String COMMONDOCID ="commonDocId";
    public static final String COMMONCLINICID = "commonClinicId";
    public static final String CLINICFEES = "clinicFees";
    public static final String APPOINTMENTSHEDULELIMITDAYS = "appointmentScheduleLimitDays";
    public static final String CLINICAPPOINTMENTTYPE = "clinicAppointmentType";
    public static final String CLINICSERVICE = "clinicServices";

    public static final String APPOINTMENTID = "appointmentId";
    public static final String APPOINTMENTDOCOTRID = "appointmentDoctorId";
    public static final String APPOINTMENTCLINICID = "appointmentClinicId";
    public static final String APPOINTMENTDATETIME = "appointmentDateTime";
    public static final String APPOINTMETIME = "appointmentTime";
    public static final String APPOINTMENTTYPE = "appointmentType";
    public static final String APPOINTMENTSTATUS = "appointmentStatus";
    public static final String TOKENNUMBER = "tokenNumber";
    public static final String WAITINGPATIENTTIME = "waitingPatientTime";
    public static final String WAITINGPATIENTCOUT = "waitingPatientCount";


    //---
    public static final String NOTIFICATION_MSG_TYPE = "notification_msg_type";
    public static final String TIME_STAMP = "time_stamp";

    //---
    public static final String CARDVIEW_DATA_TABLE = "CardViewTable";
    //---
    public static final String DOCTORLIST_DATA_TABLE = "Doctor";
    //---
    public static final String CLINI_DATA_TABLE = "Clinic";
    //---
    public static final String DOCOTORVSCLINIC_DATA_TABLE = "DoctorVsClinic";
    //---
    public static final String APPOINTMENT_DATA_TABLE = "Appointment";




    static AppDBHelper instance = null;
    private Context mContext;

    public AppDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.mContext = context;
        checkDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        // db.execSQL("CREATE TABLE IF NOT EXISTS " + APP_DATA_TABLE + "(dataId integer, data text)");
        // db.execSQL("CREATE TABLE IF NOT EXISTS " + PREFERENCES_TABLE + "(userId integer, breakfastTime text, lunchTime text, dinnerTime text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
//        db.execSQL("DROP TABLE IF EXISTS " + APP_DATA_TABLE);
//        db.execSQL("DROP TABLE IF EXISTS " + PREFERENCES_TABLE);
        deleteDatabase();
        copyDataBase();
//        onCreate(db);
    }

    public static synchronized AppDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new AppDBHelper(context);
        }
        return instance;
    }

    public boolean insertData(String dataId, String data) {
        if (dataTableNumberOfRows(dataId) == 0) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put("dataId", dataId);
            contentValues.put("data", data);

            db.insert(APP_DATA_TABLE, null, contentValues);
        } else {
            updateData(dataId, data);
        }
        return true;
    }





    public boolean insertCardViewData(List categoryList) {
        CategoryList category;
        SQLiteDatabase db = getWritableDatabase();

        db.delete(CARDVIEW_DATA_TABLE, null, null);

        db.beginTransaction();
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < categoryList.size(); i++) {
            category = (CategoryList) categoryList.get(i);
            for (int j = 0; j < category.getDocDetails().size(); j++){
                contentValues.put(CARDID, category.getDocDetails().get(j).getDocId());
                contentValues.put(CARDTYPE, category.getCategoryName().toLowerCase());

                db.insert(CARDVIEW_DATA_TABLE, null, contentValues);

                if(category.getCategoryName().equals(R.string.my_appointments)){

                    insertAppointmentData(category.getDocDetails().get(j).getAptId(),category.getDocDetails().get(j).getDocId(),category.getDocDetails().get(j).getLocationId(),
                            category.getDocDetails().get(j).getAptDate(),category.getDocDetails().get(j).getType(),"upcoming",
                            category.getDocDetails().get(j).getTokenNumber(),category.getDocDetails().get(j).getWaitingPatientTime(),category.getDocDetails().get(j).getWaitingPatientCount(),category.getDocDetails().get(j).getAptTime());

                }
            }

        }
        db.setTransactionSuccessful();
        db.endTransaction();


        return true;
    }



    public int cardTableNumberOfRows(int carddocId, String cardType) {
        SQLiteDatabase db = getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, CARDVIEW_DATA_TABLE, "carddocId = ? AND cardType=? ", new String[]{String.valueOf(carddocId),cardType} );
    }

    public boolean insertDocotorListData(List doctorList) {
        DoctorList doctors;
        SQLiteDatabase db = getWritableDatabase();

        db.delete(DOCTORLIST_DATA_TABLE, null, null);
        db.delete(CLINI_DATA_TABLE, null, null);
        db.delete(DOCOTORVSCLINIC_DATA_TABLE, null, null);

        db.beginTransaction();

        ContentValues contentValues = new ContentValues();
        String dateAndTime = CommonMethods.getFormattedDate(CommonMethods.getCurrentDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.YYYY_MM_DD) + "&time=" + CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.HH_mm);
        for(int i=0;i<doctorList.size();i++){
            doctors= (DoctorList) doctorList.get(i);

            contentValues.put(DOCID, doctors.getDocId());
            contentValues.put(DOCNAME, doctors.getDocName());
            contentValues.put(PHONENUMBER, doctors.getDocPhone());
            contentValues.put(ICONURL, doctors.getDoctorImageUrl());
            contentValues.put(ABOUTDOCTOR, doctors.getAboutDoctor());
            contentValues.put(SPECIALITYID, doctors.getSpecialityId());
            contentValues.put(SPECIALITY, doctors.getSpeciality());
            contentValues.put(RATING, doctors.getRating());
            contentValues.put(ISPREMIUM, doctors.getCategorySpeciality());
            contentValues.put(DOCDEGREE, doctors.getDegree());
            contentValues.put(EXPERIANCE, doctors.getExperience());
            contentValues.put(ISPAIDSTATUS, doctors.getPaidStatus());
            contentValues.put(CATEGORY, doctors.getCategoryName());
            contentValues.put(DOCTORGENDER, doctors.getGender());
            contentValues.put(MODIFICATIONDATE, dateAndTime);

            db.insert(DOCTORLIST_DATA_TABLE, null, contentValues);

            for(int j=0;j<doctors.getClinicList().size();j++){

                insertClinicData(doctors.getClinicList().get(j).getLocationId(),doctors.getDocId(),doctors.getClinicList().get(j).getClinicName(),
                        doctors.getClinicList().get(j).getLocationLat(),doctors.getClinicList().get(j).getLocationLong(),
                        doctors.getClinicList().get(j).getClinicAddress(),doctors.getClinicList().get(j).getAreaName(),doctors.getClinicList().get(j).getCityName());

                insertDoctorvsClinicData(doctors.getDocId(),doctors.getClinicList().get(j).getLocationId(),doctors.getClinicList().get(j).getAmount(),doctors.getClinicList().get(j).getApptScheduleLmtDays(),
                        doctors.getClinicList().get(j).getAppointmentType(),"");
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();

        return true;
    }

    public int doctorlistTableNumberOfRows(int doctorId, String doctorName,String phoneNumber,String iconURL,String aboutDoctor,
                                           int specialityId,String speciality,String rating,String isPremium,String docDegree,
                                           int experience,int isPaidStatus,String category,String doctorGender,String modificationDate) {
        SQLiteDatabase db = getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, DOCTORLIST_DATA_TABLE, "doctorId = ? ", new String[]{String.valueOf(doctorId)} );
    }

    private boolean updateDoctorListData(int doctorId, String doctorName,String phoneNumber,String iconURL,String aboutDoctor,
                                         int specialityId,String speciality,String rating,String isPremium,String docDegree,
                                         int experience,int isPaidStatus,String category,String doctorGender,String modificationDate){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DOCID, doctorId);
        contentValues.put(DOCNAME, doctorName);
        contentValues.put(PHONENUMBER, phoneNumber);
        contentValues.put(ICONURL, iconURL);
        contentValues.put(ABOUTDOCTOR, aboutDoctor);
        contentValues.put(SPECIALITYID, specialityId);
        contentValues.put(SPECIALITY, speciality);
        contentValues.put(RATING, rating);
        contentValues.put(ISPREMIUM, isPremium);
        contentValues.put(DOCDEGREE, docDegree);
        contentValues.put(EXPERIANCE, experience);
        contentValues.put(ISPAIDSTATUS, isPaidStatus);
        contentValues.put(CATEGORY, category);
        contentValues.put(DOCTORGENDER, doctorGender);
        contentValues.put(MODIFICATIONDATE, modificationDate);



        db.update(DOCTORLIST_DATA_TABLE, contentValues, "doctorId = ?",new String[]{String.valueOf(doctorId)});
        return true;
    }



    public boolean insertClinicData(int clinicId, int clinicDoctorId,String clinicName,String clinicLatitude,String clinicLongitude,
                                    String clinicAddress,String clinicAreaName,String cliniccityname) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CLINICID, clinicId);
        contentValues.put(CLINICDOCTORID, clinicDoctorId);
        contentValues.put(CLINICNAME, clinicName);
        contentValues.put(CLINICLATITUDE, clinicLatitude);
        contentValues.put(CLINICLONGITUDE, clinicLongitude);
        contentValues.put(CLINICADDRESS, clinicAddress);
        contentValues.put(CLINICAREANAME, clinicAreaName);
        contentValues.put(CLINICCITYNAME, cliniccityname);

        db.insert(CLINI_DATA_TABLE, null, contentValues);

        db.setTransactionSuccessful();
        db.endTransaction();

        return true;
    }

    public int clinicNumberOfRows(int clinicId, int clinicDoctorId,String clinicName,String clinicLatitude,String clinicLongitude,
                                  String clinicAddress,String clinicAreaName,String cliniccityname ) {
        SQLiteDatabase db = getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, CLINI_DATA_TABLE, "clinicId = ? AND clinicName=?", new String[]{String.valueOf(clinicId),clinicName});
    }

    private boolean updateClinic(int clinicId, int clinicDoctorId,String clinicName,String clinicLatitude,String clinicLongitude,
                                 String clinicAddress,String clinicAreaName,String cliniccityname){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CLINICID, clinicId);
        contentValues.put(CLINICDOCTORID, clinicDoctorId);
        contentValues.put(CLINICNAME, clinicName);
        contentValues.put(CLINICLATITUDE, clinicLatitude);
        contentValues.put(CLINICLONGITUDE, clinicLongitude);
        contentValues.put(CLINICADDRESS, clinicAddress);
        contentValues.put(CLINICAREANAME, clinicAreaName);
        contentValues.put(CLINICCITYNAME, cliniccityname);


        db.update(CLINI_DATA_TABLE, contentValues, "clinicId = ?  AND clinicName=?", new String[]{String.valueOf(clinicId),clinicName});
        return true;
    }

    public boolean insertDoctorvsClinicData(int commonDocId, int commonClinicId,int clinicFees,int appointmentScheduleLimitDays,
                                            String clinicAppointmentType,String clinicServices) {

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COMMONDOCID, commonDocId);
        contentValues.put(COMMONCLINICID, commonClinicId);
        contentValues.put(CLINICFEES, clinicFees);
        contentValues.put(APPOINTMENTSHEDULELIMITDAYS, appointmentScheduleLimitDays);
        contentValues.put(CLINICAPPOINTMENTTYPE, clinicAppointmentType);
        contentValues.put(CLINICSERVICE, clinicServices);


        db.insert(DOCOTORVSCLINIC_DATA_TABLE, null, contentValues);

        db.setTransactionSuccessful();
        db.endTransaction();

        return true;
    }

    public int doctorvsclinicNumberOfRows(int commonDocId, int commonClinicId,int clinicFees,int appointmentScheduleLimitDays,
                                          String clinicAppointmentType,String clinicServices) {

        SQLiteDatabase db = getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, DOCOTORVSCLINIC_DATA_TABLE, "commonDocId = ? AND commonClinicId=? ", new String[]{String.valueOf(commonDocId),String.valueOf(commonClinicId)});
    }

    private boolean updateDoctorvsClinic(int commonDocId, int commonClinicId,int clinicFees,int appointmentScheduleLimitDays,
                                         String clinicAppointmentType,String clinicServices){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COMMONDOCID, commonDocId);
        contentValues.put(COMMONCLINICID, commonClinicId);
        contentValues.put(CLINICFEES, clinicFees);
        contentValues.put(APPOINTMENTSHEDULELIMITDAYS, appointmentScheduleLimitDays);
        contentValues.put(CLINICAPPOINTMENTTYPE, clinicAppointmentType);
        contentValues.put(CLINICSERVICE, clinicServices);

        db.update(DOCOTORVSCLINIC_DATA_TABLE, contentValues, "commonDocId = ? AND commonClinicId=? ", new String[]{String.valueOf(commonDocId),String.valueOf(commonClinicId)});

        return true;
    }

    public boolean insertAppointmentData(String appointmentId, int appointmentDoctorId,int appointmentClinicId,String appointmentDateTime,
                                            String appointmentType,String appointmentStatus,String tokenNumber,String waitingPatientTime,String waitingPatientCount,String appointmentTime) {

        SQLiteDatabase db = getWritableDatabase();
        db.delete(APPOINTMENT_DATA_TABLE, null, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put(APPOINTMENTID, appointmentId);
        contentValues.put(APPOINTMENTDOCOTRID, appointmentDoctorId);
        contentValues.put(APPOINTMENTCLINICID, appointmentClinicId);
        contentValues.put(APPOINTMENTDATETIME, appointmentDateTime);
        contentValues.put(APPOINTMENTTYPE, appointmentType);
        contentValues.put(APPOINTMENTSTATUS, appointmentStatus);
        contentValues.put(TOKENNUMBER, tokenNumber);
        contentValues.put(APPOINTMENTTYPE, appointmentType);
        contentValues.put(WAITINGPATIENTTIME, waitingPatientTime);
        contentValues.put(WAITINGPATIENTCOUT, waitingPatientCount);
        contentValues.put(APPOINTMETIME, appointmentTime);


        db.insert(APPOINTMENT_DATA_TABLE, null, contentValues);

        return true;
    }
    public int appointmentNumberOfRows(String appointmentId) {
        SQLiteDatabase db = getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, APPOINTMENT_DATA_TABLE, "appointmentId = ?", new String[]{appointmentId});
    }

    private boolean updateappointment(String appointmentId, int appointmentDoctorId,int appointmentClinicId,String appointmentDateTime,
                                      String appointmentType,String appointmentStatus,String tokenNumber,String waitingPatientTime,String waitingPatientCount,String appointmentTime){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(APPOINTMENTID, appointmentId);
        contentValues.put(APPOINTMENTDOCOTRID, appointmentDoctorId);
        contentValues.put(APPOINTMENTCLINICID, appointmentClinicId);
        contentValues.put(APPOINTMENTDATETIME, appointmentDateTime);
        contentValues.put(APPOINTMENTTYPE, appointmentType);
        contentValues.put(APPOINTMENTSTATUS, appointmentStatus);
        contentValues.put(TOKENNUMBER, tokenNumber);
        contentValues.put(APPOINTMENTTYPE, appointmentType);
        contentValues.put(WAITINGPATIENTTIME, waitingPatientTime);
        contentValues.put(WAITINGPATIENTCOUT, waitingPatientCount);
        contentValues.put(APPOINTMETIME, appointmentTime);

        db.update(APPOINTMENT_DATA_TABLE, contentValues, "appointmentId = ? ", new String[]{appointmentId});

        return true;
    }

    public Cursor getCardviewData() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select * from " + CARDVIEW_DATA_TABLE , null);
    }

    public Cursor getDoctorData(String carddocId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select * from " + DOCTORLIST_DATA_TABLE + " where doctorId='" + carddocId + "'", null);
    }

    public Cursor getAllData() {
        SQLiteDatabase db = getReadableDatabase();

       // String str = "SELECT d.*, c.*, dc.*, a.* FROM " + CLINI_DATA_TABLE + " c LEFT JOIN "+DOCOTORVSCLINIC_DATA_TABLE+" dc ON dc.commonClinicId = c.clinicId LEFT JOIN "+DOCTORLIST_DATA_TABLE+" d ON d.doctorId = dc.commonDocId LEFT JOIN "+APPOINTMENT_DATA_TABLE+" a ON a.appointmentDoctorId = d.doctorId WHERE d.doctorId IN (select cardDocId from "+CARDVIEW_DATA_TABLE+" cd WHERE cd.cardType = cardtype and a.appointmentStatus = 'upcoming')";
        String str = "SELECT d.*, c.*, dc.*, a.* FROM " + CLINI_DATA_TABLE + " c LEFT JOIN "+DOCOTORVSCLINIC_DATA_TABLE+" dc ON dc.commonClinicId = c.clinicId LEFT JOIN "+DOCTORLIST_DATA_TABLE+" d ON d.doctorId = dc.commonDocId LEFT JOIN "+APPOINTMENT_DATA_TABLE+" a ON a.appointmentDoctorId = d.doctorId ";
        return  db.rawQuery(str,null);

    }

    public Cursor getData(String dataId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select * from " + APP_DATA_TABLE + " where dataId=" + dataId + "", null);
    }

    public int dataTableNumberOfRows(String dataId) {
        SQLiteDatabase db = getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, APP_DATA_TABLE, "dataId = ? ", new String[]{dataId});
    }

    private boolean updateData(String dataId, String data) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("data", data);

        db.update(APP_DATA_TABLE, contentValues, "dataId = ? ", new String[]{dataId});
        return true;
    }

    public boolean insertfavoriteData(String doctorId, boolean isfavorite) {

        SQLiteDatabase db = getWritableDatabase();

        if(isfavorite){
            ContentValues contentValues = new ContentValues();
            contentValues.put("cardDocId", doctorId);
            contentValues.put("cardType", "Favorite Doctors");
            db.insert(CARDVIEW_DATA_TABLE, null, contentValues);
        }else{
            updatefavoriteData(doctorId,isfavorite);
        }
        return true;
    }

    private void updatefavoriteData(String doctorId, boolean isfavorite) {
        SQLiteDatabase db = getWritableDatabase();
        if (isfavorite) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("cardDocId", doctorId);
            contentValues.put("cardType", "Favorite Doctors");
            db.insert(CARDVIEW_DATA_TABLE, null, contentValues);
        }

    }


    public Integer deleteFavoriteData(String cardDocId) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(CARDVIEW_DATA_TABLE,
                "cardDocId = ? AND cardType=? ",
                new String[]{String.valueOf(cardDocId), "Favorite Doctors"});
    }
    public Integer deleteCardViewData() {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(CARDVIEW_DATA_TABLE,
                null, null);
    }

    public Integer deleteDoctorData(String doctorId) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(DOCTORLIST_DATA_TABLE,
                "doctorId = ? ",
                new String[]{String.valueOf(doctorId)});
    }
    public Integer deleteDoctor() {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(DOCTORLIST_DATA_TABLE,
                null,null);
    }
    public Integer deleteClinic() {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(CLINI_DATA_TABLE,
                null,null);
    }
    public Integer deleteClinicData(String clinicId) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(CLINI_DATA_TABLE,
                "clinicId = ? ",
                new String[]{String.valueOf(clinicId)});
    }


    public Integer deleteData(Integer dataId) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(APP_DATA_TABLE,
                "dataId = ? ",
                new String[]{Integer.toString(dataId)});
    }


    private void copyDataBase() {
        CommonMethods.Log(TAG,
                "New database is being copied to device!");
        byte[] buffer = new byte[1024];
        OutputStream myOutput = null;
        int length;
        // Open your local db as the input stream
        InputStream myInput = null;
        try {
            myInput = mContext.getAssets().open(DATABASE_NAME);
            // transfer bytes from the inputfile to the
            // outputfile

            // check if databases folder exists, if not create one and its subfolders
            File databaseFile = new File(DB_PATH_SUFFIX);
            if (!databaseFile.exists()) {
                databaseFile.mkdir();
            }

            String path = databaseFile.getAbsolutePath() + "/" + DATABASE_NAME;

            myOutput = new FileOutputStream(path);

            CommonMethods.Log(TAG,
                    "New database is being copied to device!" + path);
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();
            CommonMethods.Log(TAG,
                    "New database has been copied to device!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkDatabase() {
        File dbFile = mContext.getDatabasePath(DATABASE_NAME);
        CommonMethods.Log(TAG, "FILENAME " + dbFile.getAbsolutePath() + "|" + dbFile.getName());
        if (!dbFile.exists()) {
            copyDataBase();
        }
    }

    public void deleteDatabase() {
        File dbFile = mContext.getDatabasePath(DATABASE_NAME);
        dbFile.delete();

        CommonMethods.Log("DeletedOfflineDatabase", "APP_DATA , PREFERENCES TABLE, INVESTIGATION");
    }

    public boolean insertPreferences(String userId, String breakfastTime, String lunchTime, String snacksTime, String dinnerTime) {
        if (preferencesTableNumberOfRows(userId) == 0) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(USER_ID, userId);
            contentValues.put(BREAKFAST_TIME, breakfastTime);
            contentValues.put(LUNCH_TIME, lunchTime);
            contentValues.put(DINNER_TIME, dinnerTime);
            contentValues.put(SNACKS_TIME, snacksTime);

            db.insert(PREFERENCES_TABLE, null, contentValues);
        } else {
            updatePreferences(userId, breakfastTime, lunchTime, snacksTime, dinnerTime);
        }
        return true;
    }

    private int preferencesTableNumberOfRows(String userId) {
        SQLiteDatabase db = getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, PREFERENCES_TABLE, USER_ID + " = ? ", new String[]{userId});
    }

    public Cursor getPreferences(String userId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select * from " + PREFERENCES_TABLE + " where " + USER_ID + "=" + userId + "", null);
    }

    private boolean updatePreferences(String userId, String breakfastTime, String lunchTime, String snacksTime, String dinnerTime) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BREAKFAST_TIME, breakfastTime);
        contentValues.put(LUNCH_TIME, lunchTime);
        contentValues.put(DINNER_TIME, dinnerTime);
        contentValues.put(SNACKS_TIME, snacksTime);

        db.update(PREFERENCES_TABLE, contentValues, USER_ID + " = ? ", new String[]{userId});
        return true;
    }

    private int deletePreferences(String userId) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(PREFERENCES_TABLE,
                USER_ID + " = ? ",
                new String[]{userId});
    }

    // investigation

    public boolean insertInvestigationData(int id, String name, String key, String dr_name, int opd_id, boolean isUploaded, String imageJson) {
        if (investigationDataTableNumberOfRows(id) == 0) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(INV_ID, id);
            contentValues.put(INV_NAME, name);
            contentValues.put(INV_NAME_KEY, key);

            contentValues.put(INV_DR_NAME, dr_name);
            contentValues.put(INV_OPD_ID, opd_id);

            contentValues.put(INV_UPLOAD_STATUS, isUploaded ? 1 : 0);
            contentValues.put(INV_UPLOADED_IMAGES, imageJson);

            db.insert(INVESTIGATION_TABLE, null, contentValues);
        }
        return true;
    }

    private int investigationDataTableNumberOfRows(int id) {
        SQLiteDatabase db = getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, INVESTIGATION_TABLE, INV_ID + " = ? ", new String[]{String.valueOf(id)});
    }

    public int updateInvestigationData(int id, boolean isUploaded, String imageJson) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INV_UPLOAD_STATUS, isUploaded ? 1 : 0);
        contentValues.put(INV_UPLOADED_IMAGES, imageJson);

        return db.update(INVESTIGATION_TABLE, contentValues, INV_ID + " = ? ", new String[]{String.valueOf(id)});
    }

    public InvestigationData getInvestigationData(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + INVESTIGATION_TABLE + " where " + INV_ID + "=" + id + "", null);

        InvestigationData dataObject = new InvestigationData();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ArrayList<Image> imageArray;
                if (cursor.getString(cursor.getColumnIndex(INV_UPLOADED_IMAGES)).equals(""))
                    imageArray = new ArrayList<>();
                else
                    imageArray = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(INV_UPLOADED_IMAGES)), Images.class).getImageArray();

                dataObject.setId(cursor.getInt(cursor.getColumnIndex(AppDBHelper.INV_ID)));
                dataObject.setTitle(cursor.getString(cursor.getColumnIndex(AppDBHelper.INV_NAME)));

                dataObject.setDoctorName(cursor.getString(cursor.getColumnIndex(AppDBHelper.INV_DR_NAME)));
                dataObject.setOpdId(cursor.getInt(cursor.getColumnIndex(AppDBHelper.INV_OPD_ID)));

                dataObject.setInvestigationKey(cursor.getString(cursor.getColumnIndex(AppDBHelper.INV_NAME_KEY)));
                dataObject.setSelected(cursor.getInt(cursor.getColumnIndex(AppDBHelper.INV_UPLOAD_STATUS)) == 1);
                dataObject.setUploaded(cursor.getInt(cursor.getColumnIndex(AppDBHelper.INV_UPLOAD_STATUS)) == 1);
                dataObject.setPhotos(imageArray);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return dataObject;
    }

    // MyRecords
    public boolean insertMyRecordsData(String id, int status, String data, int docId, int opdId, String visitDate) {
        if (MyRecordsDataTableNumberOfRows(id) == 0) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(RECORDS_DOC_ID, docId);
            contentValues.put(RECORDS_OPDID, opdId);
            contentValues.put(RECORDS_VISIT_DATE, visitDate);

            contentValues.put(RECORDS_UPLOAD_ID, id);
            contentValues.put(RECORDS_STATUS, status);
            contentValues.put(RECORDS_IMAGE_DATA, data);

            db.insert(MY_RECORDS_TABLE, null, contentValues);
        }
        return true;
    }

    private int MyRecordsDataTableNumberOfRows(String id) {
        SQLiteDatabase db = getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, MY_RECORDS_TABLE, RECORDS_UPLOAD_ID + " = ? ", new String[]{id});
    }

    public int updateMyRecordsData(String id, int isUploaded) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(RECORDS_STATUS, isUploaded);

        return db.update(MY_RECORDS_TABLE, contentValues, RECORDS_UPLOAD_ID + " = ? ", new String[]{id});
    }

    public MyRecordsData getMyRecordsData() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + MY_RECORDS_TABLE, null);

        MyRecordsData myRecordsData = new MyRecordsData();

        int docId = 0;
        int opdId = 0;
        String visitDate = null;
        ArrayList<Image> imageArrayList = new ArrayList<>();

        Gson gson = new Gson();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String imageJson = cursor.getString(cursor.getColumnIndex(RECORDS_IMAGE_DATA));
                Image image = gson.fromJson(imageJson, Image.class);
                image.setUploading(cursor.getInt(cursor.getColumnIndex(RECORDS_STATUS)));
                imageArrayList.add(image);
                docId = cursor.getInt(cursor.getColumnIndex(RECORDS_DOC_ID));
                opdId = cursor.getInt(cursor.getColumnIndex(RECORDS_OPDID));
                visitDate = cursor.getString(cursor.getColumnIndex(RECORDS_VISIT_DATE));
                cursor.moveToNext();
            }
        }
        cursor.close();

        myRecordsData.setDocId(docId);
        myRecordsData.setOpdId(opdId);
        myRecordsData.setVisitDate(visitDate);
        myRecordsData.setImageArrayList(imageArrayList);

        return myRecordsData;
    }

    public int deleteMyRecords() {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(MY_RECORDS_TABLE, null, null);
    }

    public boolean deleteUnreadMessage(String id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(MESSAGE_TABLE, CHAT_USER_ID + "=" + id, null) > 0;
    }

    public ArrayList<MQTTMessage> insertUnreadMessage(int id, String message) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CHAT_USER_ID, id);
        contentValues.put(MESSAGE, message);

        db.insert(MESSAGE_TABLE, null, contentValues);

        return getUnreadMessagesById(id);
    }

    public int unreadMessageCountById(int id) {
        // Return Total Count
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "select * from " + MESSAGE_TABLE + " where " + CHAT_USER_ID + " = " + id;
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public int unreadMessageCount() {
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "select * from " + MESSAGE_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public ArrayList<MQTTMessage> getUnreadMessagesById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "select * from " + MESSAGE_TABLE + " where " + CHAT_USER_ID + " = " + id;
        Cursor cursor = db.rawQuery(countQuery, null);
        ArrayList<MQTTMessage> chatDoctors = new ArrayList<>();
        Gson gson = new Gson();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String messageJson = cursor.getString(cursor.getColumnIndex(MESSAGE));
                MQTTMessage MQTTMessage = gson.fromJson(messageJson, MQTTMessage.class);
                chatDoctors.add(MQTTMessage);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        doReadAllUnreadMessages();

        return chatDoctors;
    }


    // Chat Upload Data

    public boolean insertMessageUpload(String id, int status, String data) {
        if (messageDataTableNumberOfRows(id) == 0) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(MESSAGE_UPLOAD_ID, id);
            contentValues.put(MESSAGE_UPLOAD_STATUS, status);
            contentValues.put(MESSAGE_FILE_UPLOAD, data);

            db.insert(MESSAGE_UPLOAD_TABLE, null, contentValues);
        }
        return true;
    }

    private int messageDataTableNumberOfRows(String id) {
        SQLiteDatabase db = getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, MESSAGE_UPLOAD_TABLE, MESSAGE_UPLOAD_ID + " = ? ", new String[]{id});
    }

    public int updateMessageUpload(String id, int isUploaded) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE_UPLOAD_STATUS, isUploaded);

        return db.update(MESSAGE_UPLOAD_TABLE, contentValues, MESSAGE_UPLOAD_ID + " = ? ", new String[]{id});
    }

    public MQTTData getMessageUpload() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + MESSAGE_UPLOAD_TABLE, null);

        MQTTData myMessageData = new MQTTData();
        ArrayList<MQTTMessage> mqttMessages = new ArrayList<>();

        Gson gson = new Gson();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String imageJson = cursor.getString(cursor.getColumnIndex(MESSAGE_FILE_UPLOAD));
                MQTTMessage mqttMessage = gson.fromJson(imageJson, MQTTMessage.class);
                mqttMessage.setUploadStatus(cursor.getInt(cursor.getColumnIndex(MESSAGE_UPLOAD_STATUS)));
                mqttMessages.add(mqttMessage);
                cursor.moveToNext();
            }
        }
        cursor.close();

        myMessageData.setMqttMessages(mqttMessages);

        return myMessageData;
    }

    public MQTTMessage getMessageUploadById(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "select * from " + MESSAGE_UPLOAD_TABLE + " where " + MESSAGE_UPLOAD_ID + " = '" + id + "'";
        Cursor cursor = db.rawQuery(countQuery, null);

        Gson gson = new Gson();
        MQTTMessage mqttMessage = null;
        if (cursor.moveToFirst()) {
            String imageJson = cursor.getString(cursor.getColumnIndex(MESSAGE_FILE_UPLOAD));
            mqttMessage = gson.fromJson(imageJson, MQTTMessage.class);
            mqttMessage.setUploadStatus(cursor.getInt(cursor.getColumnIndex(MESSAGE_UPLOAD_STATUS)));
        }
        cursor.close();

        return mqttMessage;
    }

    // Chat Upload End

    //----- Notification Storing : START

    public boolean insertUnreadReceivedNotificationMessage(String id, String type, String message, String jsonDataObject, String timeStamp, boolean isFromNotification) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ID, id);
        contentValues.put(NOTIFICATION_MSG_TYPE, type);
        contentValues.put(MESSAGE, message);

        contentValues.put(COLUMN_DATA, jsonDataObject);
        contentValues.put(TIME_STAMP, timeStamp);

        if (isExist(id, type))
            db.update(NOTIFICATION_MESSAGE_TABLE, contentValues, COLUMN_ID + " = ? AND " + NOTIFICATION_MSG_TYPE + " = ? ", new String[]{id, type});
        else {
            db.insert(NOTIFICATION_MESSAGE_TABLE, null, contentValues);
            if (isFromNotification) {
                int anInt = RescribePreferencesManager.getInt(RescribeConstants.NOTIFICATION_COUNT, mContext) + 1;
                RescribePreferencesManager.putInt(RescribeConstants.NOTIFICATION_COUNT, anInt, mContext);
            }
        }

        doReadAllUnreadMessages();

        return true;
    }

    private boolean isExist(String id, String type) {
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "select * from " + NOTIFICATION_MESSAGE_TABLE + " where " + COLUMN_ID + " = " + id + " AND " + NOTIFICATION_MSG_TYPE + " = '" + type + "'";
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public void doReadAllUnreadMessages() {
        ArrayList<UnreadSavedNotificationMessageData> mainList = RescribeApplication.getAppUnreadNotificationMessageList();
        mainList.clear();
        mainList.addAll(doGetAppUnreadReceivedNotificationMessage());
        RescribeApplication.setAppUnreadNotificationMessageList(mContext, mainList);
    }

    public ArrayList<UnreadSavedNotificationMessageData> doGetAppUnreadReceivedNotificationMessage() {
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "select * from " + NOTIFICATION_MESSAGE_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        ArrayList<UnreadSavedNotificationMessageData> chatDoctors = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                UnreadSavedNotificationMessageData unreadNotificationMessageData = new UnreadSavedNotificationMessageData();
                unreadNotificationMessageData.setId(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                unreadNotificationMessageData.setNotificationMessageType(cursor.getString(cursor.getColumnIndex(NOTIFICATION_MSG_TYPE)));
                unreadNotificationMessageData.setNotificationMessage(cursor.getString(cursor.getColumnIndex(MESSAGE)));
                unreadNotificationMessageData.setNotificationData(cursor.getString(cursor.getColumnIndex(COLUMN_DATA)));
                unreadNotificationMessageData.setNotificationTimeStamp(cursor.getString(cursor.getColumnIndex(TIME_STAMP)));

//                String dateText = CommonMethods.getFormattedDate(unreadNotificationMessageData.getNotificationTimeStamp(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY + " " + RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.DATE_PATTERN.DD_MM_YYYY);
//                String today = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.DD_MM_YYYY);

//                if (dateText.equals(today))
                chatDoctors.add(unreadNotificationMessageData);
//                else
//                    deleteUnreadReceivedNotificationMessage(Integer.parseInt(unreadNotificationMessageData.getId()), unreadNotificationMessageData.getNotificationMessageType());

                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return chatDoctors;
    }

    public int deleteUnreadReceivedNotificationMessage() {
        SQLiteDatabase db = getWritableDatabase();

        int deletedOther = db.delete(NOTIFICATION_MESSAGE_TABLE, null,
                null);

        int deletedChat = db.delete(MESSAGE_TABLE, null,
                null);

        return deletedChat + deletedOther;
    }

    public int deleteUnreadReceivedNotificationMessage(String id, String notificationType) {
        SQLiteDatabase db = getWritableDatabase();

        int delete = db.delete(NOTIFICATION_MESSAGE_TABLE, COLUMN_ID + " = ? AND " + NOTIFICATION_MSG_TYPE + " = ? ",
                new String[]{id, notificationType});

        doReadAllUnreadMessages();
        return getUnreadNotificationCount();
    }

    private int getUnreadNotificationCount() {
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "select * from " + NOTIFICATION_MESSAGE_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }


    public boolean updateUnreadReceivedNotificationMessage(String dataId, String notificationType, String data) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATA, data);

        db.update(NOTIFICATION_MESSAGE_TABLE, contentValues, COLUMN_ID + " = ? AND " + NOTIFICATION_MSG_TYPE + " = ? ", new String[]{dataId, notificationType});
        doReadAllUnreadMessages();

        return true;
    }

    public void updatefavoritecard(int docId, boolean favourite, String categoryName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if(favourite){

            contentValues.put("cardDocId", docId);
            contentValues.put("cardType", "Favorite Doctors");
            db.insert(CARDVIEW_DATA_TABLE, null, contentValues);

        }else{
           deletefavoriteData(docId,favourite,categoryName);
        }

    }

    private int deletefavoriteData(int docId, boolean favourite,String categoryName ) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(CARDVIEW_DATA_TABLE,
                DOC_ID + " =? AND "+CARDTYPE+ " = ? ",
                new String[]{String.valueOf(docId),categoryName});
    }

    /*public ArrayList<UnreadSavedNotificationMessageData> unreadChatMessagesList() {
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "select * from " + MESSAGE_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        ArrayList<UnreadSavedNotificationMessageData> chatDoctors = new ArrayList<>();
        Gson gson = new Gson();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String messageJson = cursor.getString(cursor.getColumnIndex(MESSAGE));
                UnreadSavedNotificationMessageData unreadNotificationMessageData = new UnreadSavedNotificationMessageData();

                MQTTMessage messageObject = gson.fromJson(messageJson, MQTTMessage.class);

                unreadNotificationMessageData.setId(String.valueOf(messageObject.getDocId()));
                unreadNotificationMessageData.setNotificationMessageType(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.CHAT_ALERT_COUNT);
                unreadNotificationMessageData.setNotificationMessage(mContext.getString(R.string.message_from) + " " + messageObject.getName());
                unreadNotificationMessageData.setNotificationData(messageObject.getMsg());
                unreadNotificationMessageData.setNotificationTimeStamp(messageObject.getMsgTime());
                chatDoctors.add(unreadNotificationMessageData);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return chatDoctors;
    }*/
    //----- Notification storing : END
}