package com.rescribe.helpers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.rescribe.model.chat.MQTTData;
import com.rescribe.model.chat.MQTTMessage;
import com.rescribe.model.investigation.Image;
import com.rescribe.model.investigation.Images;
import com.rescribe.model.investigation.InvestigationData;
import com.rescribe.util.CommonMethods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class AppDBHelper extends SQLiteOpenHelper {

    private final String TAG = "Rescribe/AppDBHelper";

    private static final String MESSAGE_UPLOAD_ID = "message_upload_id";
    private static final String MESSAGE_STATUS = "message_status";
    private static final String MESSAGE_FILE_DATA = "message_file_data";
    private static final String MY_MESSAGE_TABLE = "my_message_table";

    public static final String CHAT_USER_ID = "user_id";
    public static final String MESSAGE = "message";
    public static final String MESSAGE_TABLE = "unread_messages";

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

    public static final String USER_ID = "userId";
    public static final String BREAKFAST_TIME = "breakfastTime";
    public static final String LUNCH_TIME = "lunchTime";
    public static final String DINNER_TIME = "dinnerTime";
    public static final String SNACKS_TIME = "snacksTime";

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

    public Cursor getAllInvestigationData() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select * from " + INVESTIGATION_TABLE, null);
    }

    public int deleteInvestigation(String id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(INVESTIGATION_TABLE,
                INV_ID + " = ? ",
                new String[]{id});
    }

    // MyRecords

    public boolean insertMyRecordsData(String id, int status, String data, int docId,int opdId, String visitDate) {
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

    public Cursor getAllMyRecordsData() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select from " + MY_RECORDS_TABLE, null);
    }

    public int deleteMyRecords() {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(MY_RECORDS_TABLE, null, null);
    }

    public boolean deleteUnreadMessage(int id) {
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

    /*public int unreadMessageCount() {
        // Return Total Count
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "select * from " + MESSAGE_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }*/

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

        return chatDoctors;
    }

    /*public int unreadMessageUsersCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + MESSAGE_TABLE + " group by " + CHAT_USER_ID, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }*/

    // Chat Data

    public boolean insertMessageData(String id, int status, String data) {
        if (messageDataTableNumberOfRows(id) == 0) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(MESSAGE_UPLOAD_ID, id);
            contentValues.put(MESSAGE_STATUS, status);
            contentValues.put(MESSAGE_FILE_DATA, data);

            db.insert(MY_MESSAGE_TABLE, null, contentValues);
        }
        return true;
    }

    private int messageDataTableNumberOfRows(String id) {
        SQLiteDatabase db = getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, MY_MESSAGE_TABLE, MESSAGE_UPLOAD_ID + " = ? ", new String[]{id});
    }

    public int updateMessageData(String id, int isUploaded) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE_STATUS, isUploaded);

        return db.update(MY_MESSAGE_TABLE, contentValues, MESSAGE_UPLOAD_ID + " = ? ", new String[]{id});
    }

    public MQTTData getMessageData() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + MY_MESSAGE_TABLE, null);

        MQTTData myMessageData = new MQTTData();
        ArrayList<MQTTMessage> mqttMessages = new ArrayList<>();

        Gson gson = new Gson();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String imageJson = cursor.getString(cursor.getColumnIndex(MESSAGE_FILE_DATA));
                MQTTMessage mqttMessage = gson.fromJson(imageJson, MQTTMessage.class);
                mqttMessage.setUploadStatus(cursor.getInt(cursor.getColumnIndex(MESSAGE_STATUS)));
                mqttMessages.add(mqttMessage);
                cursor.moveToNext();
            }
        }
        cursor.close();

        myMessageData.setMqttMessages(mqttMessages);

        return myMessageData;
    }

    public MQTTMessage getMessageDataById(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "select * from " + MY_MESSAGE_TABLE + " where " + MESSAGE_UPLOAD_ID + " = '" + id + "'";
        Cursor cursor = db.rawQuery(countQuery, null);

        Gson gson = new Gson();
        MQTTMessage mqttMessage = null;
        if (cursor.moveToFirst()) {
            String imageJson = cursor.getString(cursor.getColumnIndex(MESSAGE_FILE_DATA));
            mqttMessage = gson.fromJson(imageJson, MQTTMessage.class);
            mqttMessage.setUploadStatus(cursor.getInt(cursor.getColumnIndex(MESSAGE_STATUS)));
        }
        cursor.close();

        return mqttMessage;
    }

    public boolean deleteUploadedMessage(String id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(MY_MESSAGE_TABLE, MESSAGE_UPLOAD_ID + "='" + id + "'", null) > 0;
    }
}