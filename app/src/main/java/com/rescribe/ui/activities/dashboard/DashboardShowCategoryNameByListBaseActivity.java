package com.rescribe.ui.activities.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.rescribe.R;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.book_appointment.doctor_data.DoctorServicesModel;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.book_appointment.BookAppointFilteredDoctorListFragment;
import com.rescribe.ui.fragments.dashboard.MyAppointmentsFragment;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rescribe.util.RescribeConstants.DOCTOR_DATA;
import static com.rescribe.util.RescribeConstants.DOCTOR_DATA_REQUEST_CODE;

/**
 * Created by jeetal on 6/11/17.
 */

public class DashboardShowCategoryNameByListBaseActivity extends AppCompatActivity {

    @BindView(R.id.bookAppointmentBackButton)
    ImageView bookAppointmentBackButton;
    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.locationTextView)
    CustomTextView locationTextView;
    @BindView(R.id.showlocation)
    CustomTextView showlocation;
    @BindView(R.id.viewContainer)
    FrameLayout viewContainer;
    ArrayList<DoctorList> doctorList;
    HashMap<String, String> userSelectedLocationInfo;
    private MyAppointmentsFragment mMyAppointmentsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_slot_book_app_layout);
        ButterKnife.bind(this);
        initialize();

    }

    private void initialize() {
        doctorList = new ArrayList<>();
        doctorList = getIntent().getExtras().getParcelableArrayList(getString(R.string.clicked_item_data));
        showlocation.setVisibility(View.VISIBLE);
        locationTextView.setVisibility(View.GONE);
        title.setText(getIntent().getStringExtra(getString(R.string.toolbarTitle)));
        userSelectedLocationInfo = DoctorDataHelper.getUserSelectedLocationInfo();
        showlocation.setText(userSelectedLocationInfo.get(getString(R.string.location)));
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(getString(R.string.clicked_item_data), doctorList);
        bundle.putString(getString(R.string.toolbarTitle), getIntent().getStringExtra(getString(R.string.toolbarTitle)));
        // mMyAppointmentsFragment = BookAppointFilteredDoctorListFragment.newInstance(bundle);
        mMyAppointmentsFragment = MyAppointmentsFragment.newInstance(bundle);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewContainer, mMyAppointmentsFragment);
        fragmentTransaction.commit();

    }

    @OnClick({R.id.bookAppointmentBackButton, R.id.locationTextView, R.id.showlocation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookAppointmentBackButton:
                onBackPressed();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if (doctorList != null) {
            Intent intent = new Intent();
            intent.putExtra(DOCTOR_DATA, doctorList);
            setResult(DOCTOR_DATA_REQUEST_CODE, intent);
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RescribeConstants.DOCTOR_DATA_REQUEST_CODE && data != null) {
            ArrayList<DoctorList> receivedList = data.getParcelableArrayListExtra(DOCTOR_DATA);
            if (receivedList.size() > 0) {
                DoctorList docObject = receivedList.get(0);
                replaceDoctorListById(docObject.getDocId(), docObject, getString(R.string.object_update_common_to_doc));
            }
            mMyAppointmentsFragment.setAdapter(doctorList);
        }
    }

    public void replaceDoctorListById(int docId, DoctorList docObjectToReplace, String objectUpdateType) {
        ArrayList<DoctorList> tempDoctorList = doctorList;
        for (int i = 0; i < tempDoctorList.size(); i++) {
            DoctorList tempObject = tempDoctorList.get(i);
            if (docId == tempObject.getDocId()) {
                doctorList.set(i, docObjectToReplace);
            }
        }
    }
}