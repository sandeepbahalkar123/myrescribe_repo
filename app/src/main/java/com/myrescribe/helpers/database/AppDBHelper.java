package com.myrescribe.helpers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.myrescribe.util.CommonMethods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AppDBHelper extends SQLiteOpenHelper {

    private static final String PREFERENCES_TABLE = "preferences_table";

    private final String TAG = this.getClass().getName();

    private static final String DATABASE_NAME = "MyRescribe.sqlite";
    private static final String DB_PATH_SUFFIX = "/data/data/com.myrescribe/databases/";
    private static final int DBVERSION = 1;

    public static final String APP_DATA_TABLE = "PrescriptionData";
    public static final String COLUMN_ID = "dataId";
    public static final String COLUMN_DATA = "data";

    public static final String USER_ID = "userId";
    public static final String BREAKFAST_TIME = "breakfastTime";
    public static final String LUNCH_TIME = "lunchTime";
    public static final String DINNER_TIME = "dinnerTime";

    static AppDBHelper instance = null;
    private Context mContext;

    public AppDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DBVERSION);
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
        db.execSQL("DROP TABLE IF EXISTS " + APP_DATA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PREFERENCES_TABLE);
        onCreate(db);
    }

    public static synchronized AppDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new AppDBHelper(context);
        }
        return instance;
    }

    public boolean insertData(String dataId, String data) {
        if (dataTableNumberOfRows(dataId) == 0) {
            SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + APP_DATA_TABLE + " where dataId=" + dataId + "", null);
    }

    public int dataTableNumberOfRows(String dataId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, APP_DATA_TABLE, "dataId = ? ", new String[]{dataId});
    }

    public boolean updateData(String dataId, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("data", data);

        db.update(APP_DATA_TABLE, contentValues, "dataId = ? ", new String[]{dataId});
        return true;
    }

    public Integer deleteData(Integer dataId) {
        SQLiteDatabase db = this.getWritableDatabase();
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

    public boolean insertPreferences(String userId, String breakfastTime, String lunchTime, String dinnerTime) {
        if (preferencesTableNumberOfRows(userId) == 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(USER_ID, userId);
            contentValues.put(BREAKFAST_TIME, breakfastTime);
            contentValues.put(LUNCH_TIME, lunchTime);
            contentValues.put(DINNER_TIME, dinnerTime);

            db.insert(PREFERENCES_TABLE, null, contentValues);
        } else {
            updatePreferences(userId, breakfastTime, lunchTime, dinnerTime);
        }
        return true;
    }

    public int preferencesTableNumberOfRows(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, PREFERENCES_TABLE, USER_ID + " = ? ", new String[]{userId});
    }

    public Cursor getPreferences(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + PREFERENCES_TABLE + " where " + USER_ID + "=" + userId + "", null);
    }

    private boolean updatePreferences(String userId, String breakfastTime, String lunchTime, String dinnerTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BREAKFAST_TIME, breakfastTime);
        contentValues.put(LUNCH_TIME, lunchTime);
        contentValues.put(DINNER_TIME, dinnerTime);

        db.update(PREFERENCES_TABLE, contentValues, USER_ID + " = ? ", new String[]{userId});
        return true;
    }

    private int deletePreferences(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PREFERENCES_TABLE,
                USER_ID + " = ? ",
                new String[]{userId});
    }

}