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
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.book_appointment.BookAppointDoctorDescriptionFragment;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rescribe.util.RescribeConstants.DOCTOR_DATA;
import static com.rescribe.util.RescribeConstants.DOCTOR_DATA_REQUEST_CODE;

/**
 * Created by jeetal on 8/11/17.
 */

public class DoctorDescriptionBaseActivity extends AppCompatActivity {

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
    DoctorList doctorObject;
    private BookAppointDoctorDescriptionFragment mBookAppointDoctorDescriptionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_slot_book_app_layout);
        ButterKnife.bind(this);
        initialize();

    }

    private void initialize() {
        doctorList = new ArrayList<>();
        doctorObject = getIntent().getExtras().getParcelable(getString(R.string.clicked_item_data));
        showlocation.setVisibility(View.VISIBLE);
        locationTextView.setVisibility(View.GONE);
        title.setText(getIntent().getStringExtra(getString(R.string.toolbarTitle)));
        userSelectedLocationInfo = DoctorDataHelper.getUserSelectedLocationInfo();
        showlocation.setText(userSelectedLocationInfo.get(getString(R.string.location)));
      /*  if (doctorList != null)
            for (int i = 0; i < doctorList.size(); i++) {
                doctorObject = doctorList.get(i);
            }*/

        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.clicked_item_data), doctorObject);
        bundle.putString(getString(R.string.toolbarTitle), getIntent().getStringExtra(getString(R.string.toolbarTitle)));
        mBookAppointDoctorDescriptionFragment = BookAppointDoctorDescriptionFragment.newInstance(bundle);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewContainer, mBookAppointDoctorDescriptionFragment);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RescribeConstants.DOCTOR_DATA_REQUEST_CODE && data != null) {
            ArrayList<DoctorList> doctorLists = data.getParcelableArrayListExtra(DOCTOR_DATA);

            if (!doctorLists.isEmpty()) {
                doctorObject.setFavourite(doctorLists.get(0).getFavourite());
                if (doctorList.isEmpty())
                    doctorList.add(doctorLists.get(0));
                else doctorList.get(0).setFavourite(doctorLists.get(0).getFavourite());

                mBookAppointDoctorDescriptionFragment.setFavorite(doctorLists.get(0).getFavourite());
                // update ui
            }
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

    public void replaceDoctorListById(String docId, DoctorList mClickedDoctorObject) {
        doctorObject.setFavourite(mClickedDoctorObject.getFavourite());
        if (doctorList.isEmpty())
            doctorList.add(doctorObject);
        else doctorList.get(0).setFavourite(mClickedDoctorObject.getFavourite());
    }
}