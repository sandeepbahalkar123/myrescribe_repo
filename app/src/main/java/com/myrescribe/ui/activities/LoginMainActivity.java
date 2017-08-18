package com.myrescribe.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.myrescribe.R;
import com.myrescribe.ui.fragments.LoginMainTabFragment;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by jeetal on 19/5/17.
 */

@RuntimePermissions
public class LoginMainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    private Context mContext;
    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.mainParentScrollView)
    ScrollView mMainParentScrollView;
    Intent intent;
    LoginMainTabFragment mLoginMainTabFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        ButterKnife.bind(this);
        mContext = this;
        intent = getIntent();
        mLoginMainTabFragment = new LoginMainTabFragment();
        Bundle args = new Bundle();
        args.putString(getString(R.string.type),intent.getStringExtra(getString(R.string.type)));
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        mLoginMainTabFragment.setArguments(args);
        fragmentTransaction.replace(R.id.container, mLoginMainTabFragment);
        fragmentTransaction.commit();
        LoginMainActivityPermissionsDispatcher.askToReadMessageWithCheck(LoginMainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoginMainTabFragment.onActivityResult(requestCode, resultCode, data);
    }

    @NeedsPermission(Manifest.permission.READ_SMS)
    public void askToReadMessage() {
        //Do nothing
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LoginMainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied({Manifest.permission.READ_SMS})
    void deniedReadSms() {
        //Do nothing
    }
}
