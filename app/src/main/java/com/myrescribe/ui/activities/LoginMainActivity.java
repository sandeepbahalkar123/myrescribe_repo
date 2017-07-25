package com.myrescribe.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.myrescribe.R;
import com.myrescribe.ui.fragments.LoginMainTabFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 19/5/17.
 */

public class LoginMainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    private Context mContext;
    @BindView(R.id.container)
    FrameLayout mContainer;
    LoginMainTabFragment mLoginMainTabFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        ButterKnife.bind(this);
        mContext = this;
        mLoginMainTabFragment = new LoginMainTabFragment();
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, mLoginMainTabFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoginMainTabFragment.onActivityResult(requestCode, resultCode, data);
    }
}
