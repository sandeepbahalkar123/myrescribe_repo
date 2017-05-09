package com.myrescribe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyRescribe.db";
    public static final String TABLE_NAME = "APPDATA";
    public static final String COLUMN_ID = "dataId";
    public static final String COLUMN_DATA = "data";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table APPDATA(dataId integer, data text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS APPDATA");
        onCreate(db);
    }

    public boolean insertData(String dataId, String data) {
        if (numberOfRows(dataId) == 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put("dataId", dataId);
            contentValues.put("data", data);

            db.insert("APPDATA", null, contentValues);
        } else {
            updateData(dataId, data);
        }
        return true;
    }

    public Cursor getData(String dataId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from APPDATA where dataId=" + dataId + "", null);
    }

    public int numberOfRows(String dataId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME, "dataId = ? ", new String[]{dataId});
    }

    public boolean updateData(String dataId, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("data", data);

        db.update("APPDATA", contentValues, "dataId = ? ", new String[]{dataId});
        return true;
    }

    public Integer deleteData(Integer dataId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("APPDATA",
                "dataId = ? ",
                new String[]{Integer.toString(dataId)});
    }

}