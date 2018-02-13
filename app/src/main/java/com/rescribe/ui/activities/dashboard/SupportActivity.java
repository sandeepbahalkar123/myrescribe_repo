package com.rescribe.ui.activities.dashboard;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.heinrichreimersoftware.materialdrawer.app_logo.BottomSheetMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuActivity;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuAdapter;
import com.rescribe.BuildConfig;
import com.rescribe.R;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.database.MyRecordsData;
import com.rescribe.model.dashboard_api.DashboardBottomMenuList;
import com.rescribe.model.investigation.Image;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.activities.AppointmentActivity;
import com.rescribe.ui.activities.MyRecordsActivity;
import com.rescribe.ui.activities.NotificationActivity;
import com.rescribe.ui.activities.PrescriptionActivity;
import com.rescribe.ui.activities.SelectedRecordsGroupActivity;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.activities.doctor.DoctorListActivity;
import com.rescribe.ui.activities.saved_articles.SavedArticles;
import com.rescribe.ui.activities.vital_graph.VitalGraphActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.rescribe.util.RescribeConstants.BOTTOM_MENUS;
import static com.rescribe.util.RescribeConstants.DRAWABLE;
import static com.rescribe.util.RescribeConstants.SALUTATION;
import static com.rescribe.util.RescribeConstants.TITLE;

/**
 * Created by jeetal on 3/11/17.
 */

@RuntimePermissions
public class SupportActivity extends BottomMenuActivity implements BottomMenuAdapter.OnBottomMenuClickListener {
    private static final String TAG = "SupportActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ArrayList<DashboardBottomMenuList> dashboardBottomMenuLists;
    @BindView(R.id.callTextView)
    CustomTextView callTextView;
    @BindView(R.id.emailtextView)
    CustomTextView emailtextView;
    @BindView(R.id.title)
    CustomTextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_base_layout);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        title.setText(getIntent().getStringExtra(TITLE));
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    void doCallSupport() {
        callSupport();
    }

    private void callSupport() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:123456789"));
        startActivity(callIntent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SupportActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnClick({R.id.callTextView, R.id.emailtextView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.callTextView:
                SupportActivityPermissionsDispatcher.doCallSupportWithCheck(this);
                break;
            case R.id.emailtextView:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "your_email"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
                    intent.putExtra(Intent.EXTRA_TEXT, "your_text");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    //TODO smth
                }

                break;
        }
    }
}
