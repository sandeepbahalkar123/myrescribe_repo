package com.myrescribe.singleton;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.myrescribe.helpers.database.AppDBHelper;

import java.util.Hashtable;

/**
 * Created by Sandeep Bahalkar
 */
public class MyRescribeApplication extends Application /*MultiDexApplication*/ {
    public static final String TAG = "MyRescribe/MyRescribeApplication";
    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();
    private static MyRescribeApplication singleton;

    public static MyRescribeApplication getInstance() {
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
//        MultiDex.install(this);
        AppDBHelper.getInstance(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        //--------------
    }
}