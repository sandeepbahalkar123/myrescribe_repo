package com.rescribe.singleton;

import android.content.Context;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.maps.model.LatLng;
import com.rescribe.R;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.model.dashboard_api.unread_notification_message_list.UnreadSavedNotificationMessageData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by Sandeep Bahalkar
 */
public class RescribeApplication extends MultiDexApplication {
    public final String TAG = this.getClass().getName();
    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();
    private static RescribeApplication singleton;

    private static HashMap<String, String> userSelectedLocationInfo = new HashMap<>();
    private static HashMap<String, String> previousUserSelectedLocationInfo = new HashMap<>();

    private static ArrayList<UnreadSavedNotificationMessageData> appUnreadNotificationMessageList = new ArrayList<>();

    public static RescribeApplication getInstance() {
        return singleton;
    }

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
        //------------
        MultiDex.install(this);
        AppDBHelper instance = AppDBHelper.getInstance(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        instance.doGetAppUnreadReceivedNotificationMessage();
        //--------------
    }

    public static void setUserSelectedLocationInfo(Context ctx, LatLng data, String locationText) {
        userSelectedLocationInfo.put(ctx.getString(R.string.location), locationText);
        if (data != null) {
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

    public static void setAppUnreadNotificationMessageList(ArrayList<UnreadSavedNotificationMessageData> appUnreadNotificationMessageList) {
        RescribeApplication.appUnreadNotificationMessageList = appUnreadNotificationMessageList;
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

}