package com.myrescribe.singleton;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.myrescribe.R;
import com.myrescribe.util.Constants;

/**
 * Created by Sandeep Bahalkar
 */

public class Device {

    private static final String TAG = "Device";
    private Context context;
    private WindowManager windowManager;

    /**
     * Create a static method to get instance.
     */

    public static Device getInstance(Context context){

        return new Device(context);
    }

    public static Device getInstance(WindowManager windowManager){

        return new Device(windowManager);
    }

    public Device(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    public Device(Context context) {
        this.context = context;
    }

    public String getDensity() {

        String density = Constants.HDPI;

        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        switch (metrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                density = Constants.LDPI;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                density = Constants.MDPI;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                density = Constants.HDPI;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                density = Constants.XHDPI;
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                density = Constants.XXHDPI;
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                density = Constants.XXXHDPI;
                break;
        }

        Log.d(TAG, density);

        return density;
    }

    public String getDeviceType() {
        String what = "";
        boolean tabletSize = context.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            what = Constants.TABLET;
        } else {
            what = Constants.PHONE;
        }
        return what;
    }

    public String getDeviceId() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    public String getOS() {
        return "Android (" + Build.BRAND + ")";
    }


}
