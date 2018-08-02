package com.rescribe.helpers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.model.chat.MQTTData;
import com.rescribe.model.chat.MQTTMessage;
import com.rescribe.model.dashboard_api.card_data.CategoryList;
import com.rescribe.model.dashboard_api.card_data.DocDetail;
import com.rescribe.model.dashboard_api.doctors.ClinicList;
import com.rescribe.model.dashboard_api.doctors.DoctorList;
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

    public static final String INV_ID = "inv_id";
    public static final String INV_NAME = "inv_name";
    public static final String INV_NAME_KEY = "inv_key";
    public static final String INV_OPD_ID = "inv_opd_id";
    public static final String INV_TYPE = "inv_type";
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
    private static final int DB_VERSION = 2;
    public static final String APP_DATA_TABLE = "PrescriptionData";
    public static final String COLUMN_ID = "dataId";
    public static final String COLUMN_DATA = "data";

    public static final String USER_ID = "userId";
    public static final String BREAKFAST_TIME = "breakfastTime";
    public static final String LUNCH_TIME = "lunchTime";
    public static final String DINNER_TIME = "dinnerTime";
    public static final String SNACKS_TIME = "snacksTime";

    public static final String NOTIFICATION_MSG_TYPE = "notification_msg_type";
    public static final String TIME_STAMP = "time_stamp";

    public interface DOC_DATA {

        String CARDVIEW_DATA_TABLE = "CardViewTable";
        String CARDS_BACKGROUND_TABLE = "CardsBackground";
        String DOCTOR_DATA_TABLE = "Doctor";
        String CLINIC_DATA_TABLE = "Clinic";
        String DOCOTORVSCLINIC_DATA_TABLE = "DoctorVsClinic";
        String APPOINTMENT_DATA_TABLE = "Appointment";

        String CARD_TYPE = "cardType";
        String IMAGE_URL = "imageUrl";

        String DOC_ID = "doctorId";
        String DOC_NAME = "doctorName";
        String PHONE_NUMBER = "phoneNumber";
        String ICON_URL = "iconURL";
        String ABOUT_DOCTOR = "aboutDoctor";
        String SPECIALITY_ID = "specialityId";
        String SPECIALITY = "speciality";
        String RATING = "rating";
        String IS_PREMIUM = "isPremium";
        String DOC_DEGREE = "docDegree";
        String EXPERIANCE = "experience";
        String PAID_STATUS = "isPaidStatus";
        String IS_FAVORITE = "isFavorite";
        String CATEGORY = "category";
        String DOCTOR_GENDER = "doctorGender";

        String CLINIC_ID = "clinicId";
        String CLINIC_NAME = "clinicName";
        String CLINIC_LATITUDE = "clinicLatitude";
        String CLINIC_LONGITUDE = "clinicLongitude";
        String CLINIC_ADDRESS = "clinicAddress";
        String CLINIC_AREA_NAME = "clinicAreaName";
        String CLINIC_CITY_NAME = "clinicCityName";
        String MODIFIED_NDATE = "modifiedDate";
        String CREATED_DATE = "createdDate";

        String CLINIC_FEES = "clinicFees";
        String APPOINTMENT_SCHEDULE_LIMIT_DAYS = "appointmentScheduleLimitDays";
        String CLINIC_APPOINTMENT_TYPE = "clinicAppointmentType";
        String CLINIC_SERVICE = "clinicServices";

        String APPOINTMENT_ID = "appointmentId";
        String APPOINTMENT_DATE = "appointmentDate";
        String APPOINTMENT_TIME = "appointmentTime";
        String APPOINTMENT_TYPE = "appointmentType";
        String APPOINTMENT_STATUS = "appointmentStatus";
        String TOKEN_NUMBER = "tokenNumber";
        String WAITING_PATIENT_TIME = "waitingPatientTime";
        String WAITING_PATIENT_COUNT = "waitingPatientCount";
    }

    public static AppDBHelper instance = null;
    private Context mContext;

    public AppDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.mContext = context;
        checkDatabase();
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // db.execSQL("CREATE TABLE IF NOT EXISTS " + APP_DATA_TABLE + "(dataId integer, data text)");
        // db.execSQL("CREATE TABLE IF NOT EXISTS " + PREFERENCES_TABLE + "(userId integer, breakfastTime text, lunchTime text, dinnerTime text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "Updating table from " + oldVersion + " to " + newVersion);
        // You will not need to modify this unless you need to do some android specific things.
        // When upgrading the database, all you need to do is add a file to the assets folder and name it:
        // from_1_to_2.sql with the version that you are upgrading to as the last version.
        for (int i = oldVersion; i < newVersion; ++i) {
            String migrationName = String.format("from_%d_to_%d.sql", i, (i + 1));
            Log.e(TAG, "Looking for migration file: " + migrationName);
            if (!isFieldExist(db, INVESTIGATION_TABLE, INV_TYPE))
                db.execSQL("ALTER TABLE `investigation_table` ADD `inv_type` TEXT");
        }
    }

    private boolean isFieldExist(SQLiteDatabase db, String tableName, String fieldName) {
        boolean isExist = false;
        Cursor res = null;
        try {
            res = db.rawQuery("Select * from " + tableName + " limit 1", null);
            int colIndex = res.getColumnIndex(fieldName);
            if (colIndex != -1) {
                isExist = true;
            }

        } finally {
            if (res != null) {
                res.close();
            }
        }
        return isExist;
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
        // delete all tables data
        SQLiteDatabase db = getWritableDatabase();
        db.delete(INVESTIGATION_TABLE, null, null);
        db.delete(MESSAGE_TABLE, null, null);
        db.delete(APP_DATA_TABLE, null, null);
        db.delete(MESSAGE_UPLOAD_TABLE, null, null);
        db.delete(MY_RECORDS_TABLE, null, null);
        db.delete(NOTIFICATION_MESSAGE_TABLE, null, null);

        db.delete(DOC_DATA.APPOINTMENT_DATA_TABLE, null, null);
        db.delete(DOC_DATA.DOCOTORVSCLINIC_DATA_TABLE, null, null);
        db.delete(DOC_DATA.CARDS_BACKGROUND_TABLE, null, null);
        db.delete(DOC_DATA.CLINIC_DATA_TABLE, null, null);
        db.delete(DOC_DATA.CARDVIEW_DATA_TABLE, null, null);
        db.delete(DOC_DATA.DOCTOR_DATA_TABLE, null, null);
        db.close();
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

    // investigation

    public void insertInvestigationData(ArrayList<InvestigationData> investigation) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        db.beginTransaction();
        for (InvestigationData dataObject : investigation) {
            if (investigationDataTableNumberOfRows(db, dataObject.getId()) == 0) {

                Images images = new Images();
                images.setImageArray(dataObject.getPhotos());

                contentValues.put(INV_ID, dataObject.getId());
                contentValues.put(INV_NAME, dataObject.getTitle());
                contentValues.put(INV_NAME_KEY, dataObject.getInvestigationKey());

                contentValues.put(INV_DR_NAME, dataObject.getDoctorName());
                contentValues.put(INV_OPD_ID, dataObject.getOpdId());

                contentValues.put(INV_TYPE, dataObject.getInvestigationType());

                contentValues.put(INV_UPLOAD_STATUS, dataObject.isUploaded() ? 1 : 0);
                contentValues.put(INV_UPLOADED_IMAGES, new Gson().toJson(images));

                db.insert(INVESTIGATION_TABLE, null, contentValues);
            }
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private int investigationDataTableNumberOfRows(SQLiteDatabase db, int id) {
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
                dataObject.setInvestigationType(cursor.getString(cursor.getColumnIndex(AppDBHelper.INV_TYPE)));

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

                chatDoctors.add(unreadNotificationMessageData);
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
    //----- Notification storing : END

    public void addDoctors(List<DoctorList> doctorList) {

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        ContentValues contentValuesDoc = new ContentValues();
        ContentValues contentValuesClinic = new ContentValues();
        ContentValues contentValuesClinicVSDoc = new ContentValues();

        for (DoctorList doctor : doctorList) {

            if (doctor.getDocInfoFlag().equalsIgnoreCase(RescribeConstants.DOC_STATUS.REMOVE)) {
                // delete doctor related data
                db.delete(DOC_DATA.DOCTOR_DATA_TABLE,
                        DOC_DATA.DOC_ID + " = ? ",
                        new String[]{String.valueOf(doctor.getDocId())});

                // delete doc clinic relation
                db.delete(DOC_DATA.DOCOTORVSCLINIC_DATA_TABLE,
                        DOC_DATA.DOC_ID + " = ? ",
                        new String[]{String.valueOf(doctor.getDocId())});

            } else {

                // insert doc data
                contentValuesDoc.put(DOC_DATA.DOC_ID, doctor.getDocId());
                contentValuesDoc.put(DOC_DATA.DOC_NAME, doctor.getDocName());
                contentValuesDoc.put(DOC_DATA.PHONE_NUMBER, doctor.getDocPhone());
                contentValuesDoc.put(DOC_DATA.ICON_URL, doctor.getDoctorImageUrl());
                contentValuesDoc.put(DOC_DATA.ABOUT_DOCTOR, doctor.getAboutDoctor());
                contentValuesDoc.put(DOC_DATA.SPECIALITY_ID, doctor.getSpecialityId());
                contentValuesDoc.put(DOC_DATA.SPECIALITY, doctor.getSpeciality());
                contentValuesDoc.put(DOC_DATA.RATING, doctor.getRating());
                contentValuesDoc.put(DOC_DATA.IS_PREMIUM, doctor.getCategorySpeciality());
                contentValuesDoc.put(DOC_DATA.DOC_DEGREE, doctor.getDegree());
                contentValuesDoc.put(DOC_DATA.EXPERIANCE, doctor.getExperience());
                contentValuesDoc.put(DOC_DATA.PAID_STATUS, doctor.getPaidStatus());
                contentValuesDoc.put(DOC_DATA.CATEGORY, doctor.getCategoryName());
                contentValuesDoc.put(DOC_DATA.DOCTOR_GENDER, doctor.getGender());
                contentValuesDoc.put(DOC_DATA.IS_FAVORITE, doctor.isFavorite() ? 1 : 0);
                contentValuesDoc.put(DOC_DATA.MODIFIED_NDATE, CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.UTC_PATTERN));

                if (doctor.getDocInfoFlag().equalsIgnoreCase(RescribeConstants.DOC_STATUS.ADD)) {
                    contentValuesDoc.put(DOC_DATA.CREATED_DATE, CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.UTC_PATTERN));
                    long isInserted = db.insert(DOC_DATA.DOCTOR_DATA_TABLE, null, contentValuesDoc);
                    Log.d(TAG, "DOCTOR_TABLE_ROW " + isInserted);
                } else if (doctor.getDocInfoFlag().equalsIgnoreCase(RescribeConstants.DOC_STATUS.UPDATE)) {
                    // update doctor related info
                    db.update(DOC_DATA.DOCTOR_DATA_TABLE, contentValuesDoc, DOC_DATA.DOC_ID + " = ? ", new String[]{String.valueOf(doctor.getDocId())});
                }

                addClinics(db, contentValuesClinic, contentValuesClinicVSDoc, doctor);
            }
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private void addClinics(SQLiteDatabase db, ContentValues contentValuesClinic, ContentValues contentValuesClinicVSDoc, DoctorList doctor) {
        for (ClinicList clinic : doctor.getClinicList()) {

            // insert clinic data
            contentValuesClinic.put(DOC_DATA.CLINIC_ID, clinic.getLocationId());
            contentValuesClinic.put(DOC_DATA.CLINIC_NAME, clinic.getClinicName());
            contentValuesClinic.put(DOC_DATA.CLINIC_LATITUDE, clinic.getLocationLat());
            contentValuesClinic.put(DOC_DATA.CLINIC_LONGITUDE, clinic.getLocationLong());
            contentValuesClinic.put(DOC_DATA.CLINIC_ADDRESS, clinic.getClinicAddress());
            contentValuesClinic.put(DOC_DATA.CLINIC_AREA_NAME, clinic.getAreaName());
            contentValuesClinic.put(DOC_DATA.CLINIC_CITY_NAME, clinic.getCityName());
            contentValuesClinic.put(DOC_DATA.CREATED_DATE, CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.UTC_PATTERN));
            contentValuesClinic.put(DOC_DATA.MODIFIED_NDATE, CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.UTC_PATTERN));

            // if clinic exist then update else insert
            long isInserted = db.insertWithOnConflict(DOC_DATA.CLINIC_DATA_TABLE, null, contentValuesClinic, SQLiteDatabase.CONFLICT_IGNORE);
            Log.d(TAG, "CLINIC_TABLE_ROW " + isInserted);
            if (isInserted == -1) {
                int isUpdated = db.update(DOC_DATA.CLINIC_DATA_TABLE, contentValuesClinic, DOC_DATA.CLINIC_ID + " = ?", new String[]{String.valueOf(clinic.getLocationId())});
                Log.d(TAG, "CLINIC_TABLE_ROW_UPDATE " + isUpdated);
            }

            // insert clinic vs doctors
            contentValuesClinicVSDoc.put(DOC_DATA.CLINIC_ID, clinic.getLocationId());
            contentValuesClinicVSDoc.put(DOC_DATA.DOC_ID, doctor.getDocId());
            contentValuesClinicVSDoc.put(DOC_DATA.CLINIC_FEES, clinic.getAmount());
            contentValuesClinicVSDoc.put(DOC_DATA.APPOINTMENT_SCHEDULE_LIMIT_DAYS, clinic.getApptScheduleLmtDays());
            contentValuesClinicVSDoc.put(DOC_DATA.CLINIC_APPOINTMENT_TYPE, clinic.getAppointmentType());
            contentValuesClinicVSDoc.put(DOC_DATA.CLINIC_SERVICE, clinic.getServices().isEmpty() ? "" : CommonMethods.listToString(clinic.getServices(), ","));

            // if clinic and doc exist then update else insert
            Cursor checkCursor = db.rawQuery("select * from " + DOC_DATA.DOCOTORVSCLINIC_DATA_TABLE + " where " + DOC_DATA.DOC_ID + " = " + doctor.getDocId() + " AND " + DOC_DATA.CLINIC_ID + " = " + clinic.getLocationId(), null);
            if (checkCursor.moveToFirst())
                db.update(DOC_DATA.DOCOTORVSCLINIC_DATA_TABLE, contentValuesClinicVSDoc, DOC_DATA.DOC_ID + " = ? AND " + DOC_DATA.CLINIC_ID + " = ?", new String[]{String.valueOf(doctor.getDocId()), String.valueOf(clinic.getLocationId())});
            else
                db.insert(DOC_DATA.DOCOTORVSCLINIC_DATA_TABLE, null, contentValuesClinicVSDoc);
            checkCursor.close();
        }
    }

    public void addCardDoctors(List<CategoryList> categoryList) {

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        ContentValues contentValuesCard = new ContentValues();
        ContentValues contentValuesCardsBackground = new ContentValues();
        ContentValues contentValuesAppoint = new ContentValues();

        for (CategoryList category : categoryList) {
            for (DocDetail docDetail : category.getDocDetails()) {
                // insert card data
                contentValuesCard.put(DOC_DATA.DOC_ID, docDetail.getDocId());
                contentValuesCard.put(DOC_DATA.CARD_TYPE, category.getCategoryName());
                db.insert(DOC_DATA.CARDVIEW_DATA_TABLE, null, contentValuesCard);

                contentValuesCardsBackground.put(DOC_DATA.CARD_TYPE, category.getCategoryName());
                contentValuesCardsBackground.put(DOC_DATA.IMAGE_URL, category.getUrl());
                long isInserted = db.insertWithOnConflict(DOC_DATA.CARDS_BACKGROUND_TABLE, null, contentValuesCardsBackground, SQLiteDatabase.CONFLICT_IGNORE);
                Log.d(TAG, "CARDS_BACKGROUND_TABLE_ROW " + isInserted);

                // insert appointment data
                if (category.getCategoryName().equalsIgnoreCase(mContext.getString(R.string.my_appointments))) {
                    contentValuesAppoint.put(DOC_DATA.APPOINTMENT_ID, docDetail.getBookId());
                    contentValuesAppoint.put(DOC_DATA.DOC_ID, docDetail.getDocId());
                    contentValuesAppoint.put(DOC_DATA.CLINIC_ID, docDetail.getLocationId());
                    contentValuesAppoint.put(DOC_DATA.APPOINTMENT_DATE, docDetail.getAptDate());
                    contentValuesAppoint.put(DOC_DATA.APPOINTMENT_TYPE, docDetail.getBookType());
                    contentValuesAppoint.put(DOC_DATA.APPOINTMENT_STATUS, docDetail.getPaidStatus());
                    contentValuesAppoint.put(DOC_DATA.TOKEN_NUMBER, docDetail.getTokenNumber());
                    contentValuesAppoint.put(DOC_DATA.WAITING_PATIENT_TIME, docDetail.getWaitingPatientTime());
                    contentValuesAppoint.put(DOC_DATA.WAITING_PATIENT_COUNT, docDetail.getWaitingPatientCount());
                    contentValuesAppoint.put(DOC_DATA.APPOINTMENT_TIME, docDetail.getAptTime());

                    db.insert(DOC_DATA.APPOINTMENT_DATA_TABLE, null, contentValuesAppoint);
                }
            }
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public Cursor getCardDoctorsData() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select * from " + DOC_DATA.CARDVIEW_DATA_TABLE, null);
    }

    public Cursor getCardDoctorData(int doctorId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select * from " + DOC_DATA.CARDVIEW_DATA_TABLE + " where " + DOC_DATA.DOC_ID + " = " + doctorId, null);
    }

    public Cursor getAppointmentByDoctor(int doctorId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select * from " + DOC_DATA.APPOINTMENT_DATA_TABLE + " where " + DOC_DATA.DOC_ID + " = " + doctorId, null);
    }

    public Cursor getDoctorsSpecialities() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select DISTINCT " + DOC_DATA.SPECIALITY + ", " + DOC_DATA.SPECIALITY_ID + " from " + DOC_DATA.DOCTOR_DATA_TABLE + " WHERE NOT (" + DOC_DATA.SPECIALITY + " = '') ", null);
    }

    public Cursor getDoctor(int doctorId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select * from " + DOC_DATA.DOCTOR_DATA_TABLE + " where " + DOC_DATA.DOC_ID + " = " + doctorId, null);
    }

    public Cursor getDoctorsBySpeciality(String speciality) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select * from " + DOC_DATA.DOCTOR_DATA_TABLE + " where " + DOC_DATA.SPECIALITY + " = '" + speciality + "'", null);
    }

    public Cursor getDoctors() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select * from " + DOC_DATA.DOCTOR_DATA_TABLE, null);
    }

    public Cursor getDoctorVsClinicById(int doctorId, int clinicId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select * from " + DOC_DATA.DOCOTORVSCLINIC_DATA_TABLE + " where " + DOC_DATA.DOC_ID + " = " + doctorId + " AND " + DOC_DATA.CLINIC_ID + " = " + clinicId, null);
    }

    public Cursor getAllClinicsByDoctor(int doctorId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select c.*, dc.doctorId, dc.clinicFees, dc.appointmentScheduleLimitDays, dc.clinicAppointmentType, dc.clinicServices from " + DOC_DATA.CLINIC_DATA_TABLE + " c inner join " + DOC_DATA.DOCOTORVSCLINIC_DATA_TABLE + " dc on dc.clinicId = c.clinicId where dc.doctorId = " + doctorId, null);
    }

    public String getCardsBackground(String cardType) {
        String cardBack = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DOC_DATA.CARDS_BACKGROUND_TABLE + " where " + DOC_DATA.CARD_TYPE + " = '" + cardType + "'", null);
        if (cursor.moveToFirst())
            cardBack = cursor.getString(cursor.getColumnIndex(DOC_DATA.IMAGE_URL));
        return cardBack;
    }

    public void updateCardTable(com.rescribe.model.book_appointment.doctor_data.DoctorList doctor, String categoryName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValuesDoc = new ContentValues();
        ContentValues contentValuesCard = new ContentValues();
        int isFav = doctor.getFavourite() ? 1 : 0;

        db.beginTransaction();
        contentValuesDoc.put(DOC_DATA.IS_FAVORITE, isFav);
        db.update(DOC_DATA.DOCTOR_DATA_TABLE, contentValuesDoc, DOC_DATA.DOC_ID + " = ?", new String[]{String.valueOf(doctor.getDocId())});

        if (isFav == 1) {
            contentValuesCard.put(DOC_DATA.DOC_ID, doctor.getDocId());
            contentValuesCard.put(DOC_DATA.CARD_TYPE, categoryName);
            db.insert(DOC_DATA.CARDVIEW_DATA_TABLE, null, contentValuesCard);
        } else
            db.delete(DOC_DATA.CARDVIEW_DATA_TABLE, DOC_DATA.DOC_ID + " = ? AND " + DOC_DATA.CARD_TYPE + " = ?", new String[]{String.valueOf(doctor.getDocId()), categoryName});

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
}