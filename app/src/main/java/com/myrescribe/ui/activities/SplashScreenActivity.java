package com.myrescribe.ui.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.myrescribe.R;
import com.myrescribe.interfaces.CheckIpConnection;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

public class SplashScreenActivity extends AppCompatActivity {
    Context mContext;
    Dialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mContext = SplashScreenActivity.this;
        doLogin();
      //  doAppCheckLogin();

    }

    private void doLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                 if(MyRescribePreferencesManager.getString(MyRescribeConstants.USERNAME,mContext).equals("")&&MyRescribePreferencesManager.getString(MyRescribeConstants.PHONE,mContext).equals("")) {
                     Intent intentObj = new Intent(SplashScreenActivity.this, LoginActivity.class);
                     intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                     intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     startActivity(intentObj);
                 }else if(!MyRescribePreferencesManager.getString(MyRescribeConstants.USERNAME,mContext).equals("")&&MyRescribePreferencesManager.getString(MyRescribeConstants.PHONE,mContext).equals("")){
                     Intent intentObj = new Intent(SplashScreenActivity.this, PhoneNoActivity.class);
                     intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                     intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     startActivity(intentObj);
                 }
                else if(!MyRescribePreferencesManager.getString(MyRescribeConstants.USERNAME,mContext).equals("") && (!MyRescribePreferencesManager.getString(MyRescribeConstants.PHONE,mContext).equals(""))){
                     Intent intentObj = new Intent(SplashScreenActivity.this, ShowMedicineDoseListActivity.class);
                     intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                     intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     startActivity(intentObj);

                 }
            }
        }, MyRescribeConstants.TIME_STAMPS.THREE_SECONDS);

    }

    private void doAppCheckLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                String userName = MyRescribePreferencesManager.getString(MyRescribeConstants.USERNAME, mContext);
                String password = MyRescribePreferencesManager.getString(MyRescribeConstants.PASSWORD, mContext);

                Intent intentObj = null;

                if (MyRescribeConstants.BLANK.equalsIgnoreCase(userName) || MyRescribeConstants.BLANK.equalsIgnoreCase(password)) {
                    if (!MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.IS_VALID_IP_CONFIG, mContext).equals(MyRescribeConstants.TRUE)) {
                        //alert dialog for serverpath
                        CommonMethods.showAlertDialog(SplashScreenActivity.this, getString(R.string.server_path) + "\n" + getString(R.string.for_example_server_path), new CheckIpConnection() {
                            @Override
                            public void onOkButtonClickListner(String serverPath, Context context, Dialog dialog) {
                                mDialog = dialog;
                                mContext = context;
                                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.SERVER_PATH, serverPath, context);
                               // mLoginHelper.checkConnectionToServer(serverPath);


                            }
                        });
                    } else {
                        intentObj = new Intent(mContext, LoginActivity.class);
                        intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentObj);

                        finish();
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                    }
                } else {
                    //------Check Remember ME first , then only move on next screen.
                    intentObj = new Intent(mContext, ShowMedicineDoseListActivity.class);
                    intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentObj);

                    finish();
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }


            }
        }, MyRescribeConstants.TIME_STAMPS.THREE_SECONDS);

    }

}
