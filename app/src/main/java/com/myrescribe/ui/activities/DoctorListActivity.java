package com.myrescribe.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.myrescribe.R;
import com.myrescribe.adapters.filter.FilterCaseDetailsAdapter;
import com.myrescribe.adapters.filter.FilterDoctorSpecialitiesAdapter;
import com.myrescribe.adapters.filter.FilterDoctorsAdapter;
import com.myrescribe.helpers.filter.FilterHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.filter.CaseDetailsData;
import com.myrescribe.model.filter.CaseDetailsListModel;
import com.myrescribe.model.filter.DoctorData;
import com.myrescribe.model.filter.DoctorSpecialityData;
import com.myrescribe.model.filter.FilterDoctorListModel;
import com.myrescribe.model.filter.FilterDoctorSpecialityListModel;
import com.myrescribe.model.filter.filter_request.DrFilterRequestModel;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.ui.fragments.doctor.DoctorFilteredListFragment;
import com.myrescribe.ui.fragments.doctor.DoctorListFragmentContainer;
import com.myrescribe.ui.fragments.filter.FilterFragment;
import com.myrescribe.ui.fragments.filter.SelectDoctorsFragment;
import com.myrescribe.ui.fragments.filter.SelectSpecialityFragment;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 14/6/17.
 */

public class DoctorListActivity extends AppCompatActivity implements HelperResponse, FilterFragment.OnDrawerInteractionListener, SelectDoctorsFragment.OnSelectDoctorInteractionListener, SelectSpecialityFragment.OnSelectSpecialityInteractionListener, FilterDoctorsAdapter.ItemClickListener, FilterDoctorSpecialitiesAdapter.ItemClickListener, FilterCaseDetailsAdapter.ItemClickListener {


    // Filter Start

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.nav_view)
    FrameLayout nav_view;


    // Filter End

    private FragmentManager fragmentManager;
    private FilterFragment filterFragment;
    private FilterDoctorListModel filterDoctorListModel = new FilterDoctorListModel();
    private FilterDoctorSpecialityListModel filterDoctorSpecialityListModel = new FilterDoctorSpecialityListModel();
    private CaseDetailsListModel caseDetailsListModel = new CaseDetailsListModel();

    private ArrayList<String> caseList = new ArrayList<>();
    private ArrayList<String> doctorSpecialityList = new ArrayList<>();
    private ArrayList<Integer> docIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        loadFragment(DoctorListFragmentContainer.newInstance(), false);

        //--- Filter Start
        FilterHelper filterHelper = new FilterHelper(this);
        filterHelper.getDoctorList();
        filterHelper.getDoctorSpecialityList();
        filterHelper.getCaseDetailsList();
        //--- Filter End
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.END)) {
            mDrawer.closeDrawer(GravityCompat.END);
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public DrawerLayout getActivityDrawer() {
        return mDrawer;
    }

// Filter Start

    @Override
    public void onApply() {

        DrFilterRequestModel drFilterRequestModel = new DrFilterRequestModel();
        drFilterRequestModel.setPatientId(Integer.parseInt(MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATEINT_ID, DoctorListActivity.this)));
        drFilterRequestModel.setDocIds(docIdList);
        drFilterRequestModel.setDocSpecialities(doctorSpecialityList);
        drFilterRequestModel.setStartDate(filterFragment.getFromDate());
        drFilterRequestModel.setEndDate(filterFragment.getToDate());
        drFilterRequestModel.setCases(caseList);

        Gson gson = new Gson();
        CommonMethods.Log("FilterRequest", gson.toJson(drFilterRequestModel, DrFilterRequestModel.class));

        mDrawer.closeDrawer(GravityCompat.END);
        loadFragment(DoctorFilteredListFragment.newInstance(drFilterRequestModel), true);
    }

    @Override
    public void onSelectDoctors() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SelectDoctorsFragment selectDoctorsFragment = SelectDoctorsFragment.newInstance(filterDoctorListModel.getData(), getResources().getString(R.string.doctors));
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.add(R.id.nav_view, selectDoctorsFragment, getResources().getString(R.string.doctors));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onSelectSpeciality() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SelectSpecialityFragment selectSpecialityFragment = SelectSpecialityFragment.newInstance(filterDoctorSpecialityListModel.getDoctorSpecialityData(), getResources().getString(R.string.doctors_speciality));
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.add(R.id.nav_view, selectSpecialityFragment, getResources().getString(R.string.doctors_speciality));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onReset() {

        docIdList.clear();
        doctorSpecialityList.clear();
        caseList.clear();

        for (DoctorData doctorDetail : filterDoctorListModel.getData()) {
            doctorDetail.setSelected(false);
        }

        for (DoctorSpecialityData doctorSpecialityData : filterDoctorSpecialityListModel.getDoctorSpecialityData()) {
            doctorSpecialityData.setSelected(false);
        }

        for (CaseDetailsData caseDetails : caseDetailsListModel.getCaseDetailsDatas())
            caseDetails.setSelected(false);

        filterFragment.notifyCaseDetails();
    }

    @Override
    public void onFragmentBack() {
        fragmentManager.popBackStack();
    }

    @Override
    public void setDoctorSpeciality(String speciality) {
        filterFragment.setDoctorSpeciality(speciality);
    }

    @Override
    public void setDoctorName(String name) {
        filterFragment.setDoctorName(name);
    }

    @Override
    public void onDoctorClick() {

        docIdList.clear();

        String doctorName = getResources().getString(R.string.select_doctors);
        int count = 0;
        for (DoctorData doctorDetail : filterDoctorListModel.getData()) {
            if (doctorDetail.isSelected()) {

                docIdList.add(doctorDetail.getId());

                count++;
                if (count == 1)
                    doctorName = doctorDetail.getDoctorName();
            }

        }
        if (count > 1)
            filterFragment.setDoctorName(doctorName + " + " + (count - 1));
        else if (count == 1) filterFragment.setDoctorName(doctorName);
        else filterFragment.setDoctorName(doctorName);
    }

    @Override
    public void onDoctorSpecialityClick() {

        doctorSpecialityList.clear();

        String doctorSpeciality = getResources().getString(R.string.select_doctors_speciality);
        int count = 0;
        for (DoctorSpecialityData doctorSpecialityData : filterDoctorSpecialityListModel.getDoctorSpecialityData()) {
            if (doctorSpecialityData.isSelected()) {

                doctorSpecialityList.add(doctorSpecialityData.getSpeciality());

                count++;
                if (count == 1)
                    doctorSpeciality = doctorSpecialityData.getSpeciality();
            }

        }
        if (count > 1)
            filterFragment.setDoctorSpeciality(doctorSpeciality + " + " + (count - 1));
        else if (count == 1) filterFragment.setDoctorSpeciality(doctorSpeciality);
        else filterFragment.setDoctorSpeciality(doctorSpeciality);
    }

    @Override
    public void onCaseClick() {

        caseList.clear();

        for (CaseDetailsData caseDetailsData : caseDetailsListModel.getCaseDetailsDatas()) {
            if (caseDetailsData.isSelected())
                caseList.add(caseDetailsData.getName());
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        //  mViewPagerAdapter.notifyDataSetChanged();
        if (customResponse instanceof FilterDoctorListModel) {
            filterDoctorListModel = (FilterDoctorListModel) customResponse;

        } else if (customResponse instanceof FilterDoctorSpecialityListModel) {
            filterDoctorSpecialityListModel = (FilterDoctorSpecialityListModel) customResponse;
        } else if (customResponse instanceof CaseDetailsListModel) {
            caseDetailsListModel = (CaseDetailsListModel) customResponse;

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            filterFragment = FilterFragment.newInstance(caseDetailsListModel.getCaseDetailsDatas());
            fragmentTransaction.add(R.id.nav_view, filterFragment, "Filter");
            fragmentTransaction.commit();

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

    // Filter End

    private void loadFragment(Fragment fragment, boolean requiredBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.doctorViewContainer, fragment);
        if (requiredBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
}