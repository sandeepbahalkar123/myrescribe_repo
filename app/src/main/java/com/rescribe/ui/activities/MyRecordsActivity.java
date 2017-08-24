package com.rescribe.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.rescribe.R;
import com.rescribe.ui.fragments.my_record.MyRecordListFragmentContainer;
import com.rescribe.util.RescribeConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 31/7/17.
 */

public class MyRecordsActivity extends AppCompatActivity {
    // Filter Start
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.nav_view)
    FrameLayout nav_view;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_record_activity);
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();
        loadFragment(MyRecordListFragmentContainer.newInstance(), false);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyRecordsActivity.this, HomePageActivity.class);
        intent.putExtra(RescribeConstants.ALERT, false);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
    }

    private void loadFragment(Fragment fragment, boolean requiredBackStack) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.myRecordViewContainer, fragment);
        if (requiredBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
}
