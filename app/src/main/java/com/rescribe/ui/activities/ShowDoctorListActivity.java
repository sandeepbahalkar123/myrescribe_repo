package com.rescribe.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.RecentVisitDoctorFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 15/9/17.
 */
public class ShowDoctorListActivity extends AppCompatActivity implements HelperResponse {

    @BindView(R.id.doctorToolbar)
    ImageView doctorToolbar;
    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.locationTextView)
    CustomTextView locationTextView;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    Intent intent;
    private RecentVisitDoctorFragment mChangeColorFragment;
    private DoctorDataHelper mDoctorDataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_doctors);
        intent = getIntent();
      /*  if(getIntent()!=null){
            locationTextView.setText(intent.getStringExtra(getString(R.string.title)));
        }*/
        ButterKnife.bind(this);
        initialize();

    }

    private void initialize() {
        mDoctorDataHelper = new DoctorDataHelper(this, this);
        mDoctorDataHelper.doGetDoctorData();

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        emptyListView.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
        BookAppointmentBaseModel bookAppointmentBaseModel = (BookAppointmentBaseModel) customResponse;
        mChangeColorFragment = RecentVisitDoctorFragment.newInstance(bookAppointmentBaseModel.getDoctorServicesModel());
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, mChangeColorFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        container.setVisibility(View.GONE);
        emptyListView.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.doctorToolbar, R.id.title, R.id.locationTextView, R.id.container, R.id.emptyListView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.doctorToolbar:
                onBackPressed();
                break;
            case R.id.title:
                break;
            case R.id.locationTextView:
                break;
            case R.id.container:
                break;
            case R.id.emptyListView:
                break;
        }
    }
}
