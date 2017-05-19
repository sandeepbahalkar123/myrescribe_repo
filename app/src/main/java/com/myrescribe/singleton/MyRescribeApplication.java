package com.myrescribe.singleton;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.myrescribe.helpers.database.AppDBHelper;

import java.util.Hashtable;

//import android.support.multidex.MultiDexApplication;
/*import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;*/

/**
 * Created by Sandeep Bahalkar
 */
public class MyRescribeApplication extends Application /*MultiDexApplication*/ {
    public static final String TAG = MyRescribeApplication.class
            .getSimpleName();

    private static MyRescribeApplication singleton;

    public static MyRescribeApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //------------
        AppDBHelper.getInstance(this);
        //--------------
    }

    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

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
}