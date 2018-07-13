package com.rescribe.singleton;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.maps.model.LatLng;
import com.rescribe.R;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.model.dashboard_api.unread_notification_message_list.UnreadSavedNotificationMessageData;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.TreeSet;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Sandeep Bahalkar
 */
public class RescribeApplication extends MultiDexApplication {
    public final String TAG = this.getClass().getName();
    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    private static HashMap<String, String> userSelectedLocationInfo = new HashMap<>();
    private static HashMap<String, String> previousUserSelectedLocationInfo = new HashMap<>();

    public static ArrayList<UnreadSavedNotificationMessageData> appUnreadNotificationMessageList = new ArrayList<>();
    private static AppDBHelper appDBHelper;

    public static Typeface get(Context c, String name) {
        synchronized (cache) {
            if (!cache.containsKey(name)) {
                Typeface t = Typeface.createFromAsset(c.getAssets(), "fonts/"
                        + name);
                cache.put(name, t);
            }
            return cache.get(name);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        //------------
        MultiDex.install(this);
        AppDBHelper instance = AppDBHelper.getInstance(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        instance.doReadAllUnreadMessages();
        appDBHelper = new AppDBHelper(this);
        //--------------
    }

    public static void setUserSelectedLocationInfo(Context ctx, LatLng data, String locationText) {
        RescribePreferencesManager.putString(ctx.getString(R.string.location), locationText, ctx);
        RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.LAST_UPDATED, "", ctx);
        userSelectedLocationInfo.put(ctx.getString(R.string.location), locationText);
        if (data != null) {
            RescribePreferencesManager.putString(ctx.getString(R.string.latitude), "" + data.latitude, ctx);
            RescribePreferencesManager.putString(ctx.getString(R.string.longitude), "" + data.longitude, ctx);
            userSelectedLocationInfo.put(ctx.getString(R.string.latitude), "" + data.latitude);
            userSelectedLocationInfo.put(ctx.getString(R.string.longitude), "" + data.longitude);
        }
    }

    public static HashMap<String, String> getUserSelectedLocationInfo() {
        return userSelectedLocationInfo;
    }

    public static HashMap<String, String> getPreviousUserSelectedLocationInfo() {
        return previousUserSelectedLocationInfo;
    }

    public static void setPreviousUserSelectedLocationInfo(Context ctx, LatLng data, String locationText) {
        previousUserSelectedLocationInfo.put(ctx.getString(R.string.location), locationText);
        if (data != null) {
            previousUserSelectedLocationInfo.put(ctx.getString(R.string.latitude), "" + data.latitude);
            previousUserSelectedLocationInfo.put(ctx.getString(R.string.longitude), "" + data.longitude);
        }
    }

    public static ArrayList<UnreadSavedNotificationMessageData> getAppUnreadNotificationMessageList() {
        return appUnreadNotificationMessageList;
    }

    public static void setAppUnreadNotificationMessageList(Context mContext, ArrayList<UnreadSavedNotificationMessageData> appUnreadNotificationMessageList) {
        RescribeApplication.appUnreadNotificationMessageList = appUnreadNotificationMessageList;
        mContext.sendBroadcast(new Intent(mContext.getString(R.string.unread_notification_update_received)));
    }

    public static ArrayList<UnreadSavedNotificationMessageData> doFindUnreadNotificationMessageByType(String notificationType) {
        ArrayList<UnreadSavedNotificationMessageData> receivedNotificationMessageList = new ArrayList<>();
        //String : id|messageType|message
        for (UnreadSavedNotificationMessageData object :
                appUnreadNotificationMessageList) {
            if (object.getNotificationMessageType().equalsIgnoreCase(notificationType)) {
                receivedNotificationMessageList.add(object);
            }
        }
        return receivedNotificationMessageList;
    }

    public static int doGetUnreadNotificationCount(String notificationType) {

        ArrayList<UnreadSavedNotificationMessageData> objectToRemove = new ArrayList<>();

        ArrayList<UnreadSavedNotificationMessageData> receivedNotificationMessageList = new ArrayList<>();
        int size;
        if (notificationType.equalsIgnoreCase(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT)) {
            TreeSet<String> listDataGroup = new TreeSet<>();
            //--- set header data
            for (UnreadSavedNotificationMessageData dataObject :
                    appUnreadNotificationMessageList) {
                if (dataObject.getNotificationMessageType().equalsIgnoreCase(notificationType)) {

                    String dateText = CommonMethods.getFormattedDate(dataObject.getNotificationTimeStamp(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY + " " + RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.DATE_PATTERN.DD_MM_YYYY);
                    String today = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.DD_MM_YYYY);

                    if (dateText.equals(today))
                        listDataGroup.add(dataObject.getNotificationMessage());
                    else
                        objectToRemove.add(dataObject);
                }
            }
            size = listDataGroup.size();
        } else {
            //String : id|messageType|message
            for (UnreadSavedNotificationMessageData dataObject :
                    appUnreadNotificationMessageList) {
                if (dataObject.getNotificationMessageType().equalsIgnoreCase(notificationType)) {

                    String dateText = CommonMethods.getFormattedDate(dataObject.getNotificationTimeStamp(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY + " " + RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.DATE_PATTERN.DD_MM_YYYY);
                    String today = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.DD_MM_YYYY);

                    if (dateText.equals(today))
                        receivedNotificationMessageList.add(dataObject);
                    else
                        objectToRemove.add(dataObject);
                }
            }
            size = receivedNotificationMessageList.size();
        }

        for (UnreadSavedNotificationMessageData dataObject : objectToRemove) {
            appUnreadNotificationMessageList.remove(dataObject);
            appDBHelper.deleteUnreadReceivedNotificationMessage(dataObject.getId(), dataObject.getNotificationMessageType());
        }

        return size;
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void clearNotification(Context context, String notification_tag, String notificationId) {
        try {
            final NotificationManager nm = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(notification_tag, Integer.parseInt(notificationId));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}