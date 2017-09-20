package com.rescribe.ui.activities.book_appointment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.rescribe.R;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.book_appointment.DrawerForFilterDoctorBookAppointment;
import com.rescribe.ui.fragments.book_appointment.RecentVisitDoctorFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 15/9/17.
 */
public class BookAppointDoctorListBaseActivity extends AppCompatActivity implements HelperResponse, DrawerForFilterDoctorBookAppointment.OnDrawerInteractionListener {

    @BindView(R.id.bookAppointmentBackButton)
    ImageView bookAppointmentBackButton;
    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.locationTextView)
    CustomTextView locationTextView;

    private DoctorDataHelper mDoctorDataHelper;
    private Fragment currentlyLoadedFragment; //TODO, fragmentById is not working hence hold this object.
    private BookAppointmentBaseModel mReceivedBookAppointmentBaseModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appoint_doc_base_list);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        RecentVisitDoctorFragment recentVisitDoctorFragment = RecentVisitDoctorFragment.newInstance(new Bundle());
        fragmentTransaction.replace(R.id.viewContainer, recentVisitDoctorFragment);
        fragmentTransaction.commit();
        this.currentlyLoadedFragment = recentVisitDoctorFragment;

        fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_view, DrawerForFilterDoctorBookAppointment.newInstance());
        fragmentTransaction.commit();

        mDoctorDataHelper = new DoctorDataHelper(this, this);
        mDoctorDataHelper.doGetDoctorData();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        mReceivedBookAppointmentBaseModel = (BookAppointmentBaseModel) customResponse;
        if (currentlyLoadedFragment instanceof RecentVisitDoctorFragment) {
            RecentVisitDoctorFragment tempFrag = (RecentVisitDoctorFragment) currentlyLoadedFragment;
            tempFrag.onSuccess(mOldDataTag, customResponse);
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onApply() {

    }

    @Override
    public void onReset() {

    }

    public BookAppointmentBaseModel getReceivedBookAppointmentBaseModel() {
        return mReceivedBookAppointmentBaseModel;
    }
}
