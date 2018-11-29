package com.rescribe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.devsupport.interfaces.DevOptionHandler;
import com.facebook.react.devsupport.interfaces.DevSupportManager;
import com.rescribe.singleton.RescribeApplication;

public class MainActivity extends ReactActivity {
    public static final String LAUNCH_SCREEN = "launchscreen";
    private String launchingScreen = "NavigatorPage";

    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {
        return new ReactActivityDelegate(this, getMainComponentName()) {
            @Nullable
            @Override
            protected Bundle getLaunchOptions() {
                Intent intent = getIntent();
                if (intent != null) {
                    String data = intent.getStringExtra(LAUNCH_SCREEN);
                    if (data != null)
                        launchingScreen = data;
                }

                Toast.makeText(MainActivity.this, "Start Screen " + launchingScreen, Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString(LAUNCH_SCREEN, launchingScreen);
                return bundle;
            }
        };
    }

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "Patient Rescribe";
    }

    /**
     * Demonstrates how to add a custom option to the dev menu.
     * https://stackoverflow.com/a/44882371/3968276
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RescribeApplication application = (RescribeApplication) getApplication();
        ReactNativeHost reactNativeHost = application.getReactNativeHost();
        ReactInstanceManager reactInstanceManager = reactNativeHost.getReactInstanceManager();
        DevSupportManager devSupportManager = reactInstanceManager.getDevSupportManager();
        devSupportManager.addCustomDevOption("Custom dev option", new DevOptionHandler() {
            @Override
            public void onOptionSelected() {
                Toast.makeText(MainActivity.this, "Hello from custom dev option", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(this, "Test " + requestCode + " " + resultCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String data = intent.getStringExtra(LAUNCH_SCREEN);
            if (data != null)
                launchingScreen = data;
        }
        Toast.makeText(this, "New Intent " + launchingScreen, Toast.LENGTH_SHORT).show();
    }
}
