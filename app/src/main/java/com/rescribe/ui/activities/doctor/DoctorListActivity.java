package com.rescribe.ui.activities.doctor;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.adapters.filter.FilterCaseDetailsAdapter;
import com.rescribe.adapters.filter.FilterDoctorSpecialitiesAdapter;
import com.rescribe.adapters.filter.FilterDoctorsAdapter;
import com.rescribe.helpers.filter.FilterHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.filter.CaseDetailsData;
import com.rescribe.model.filter.CaseDetailsListModel;
import com.rescribe.model.filter.DoctorData;
import com.rescribe.model.filter.DoctorSpecialityData;
import com.rescribe.model.filter.FilterDoctorListModel;
import com.rescribe.model.filter.FilterDoctorSpecialityListModel;
import com.rescribe.model.filter.filter_request.DrFilterRequestModel;
import com.rescribe.network.RequestPool;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.fragments.doctor.DoctorFilteredListFragment;
import com.rescribe.ui.fragments.doctor.DoctorListFragmentContainer;
import com.rescribe.ui.fragments.filter.FilterFragment;
import com.rescribe.ui.fragments.filter.SelectDoctorsFragment;
import com.rescribe.ui.fragments.filter.SelectSpecialityFragment;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 14/6/17.
 */

public class DoctorListActivity extends AppCompatActivity implements HelperResponse, FilterFragment.OnDrawerInteractionListener, SelectDoctorsFragment.OnSelectDoctorInteractionListener, SelectSpecialityFragment.OnSelectSpecialityInteractionListener, FilterDoctorsAdapter.ItemClickListener, FilterDoctorSpecialitiesAdapter.ItemClickListener, FilterCaseDetailsAdapter.ItemClickListener {
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    private static final String FILTERED_FRAGMENT_TAG = "filtered_fragment";
    // Filter Start
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.nav_view)
    FrameLayout nav_view;
    //   boolean isOnApplyFilterCalledBefore = false; // this is added for maintaining stack of filter result
    // Filter End
    private FragmentManager mFragmentManager;
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
        setContentView(R.layout.global_drawer_layout_container);
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.viewContainer, DoctorListFragmentContainer.newInstance(), BACK_STACK_ROOT_TAG);
        fragmentTransaction.commit();

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
        } else {
            if (mFragmentManager.getBackStackEntryCount() > 0) {
                mFragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                RequestPool.getInstance(this).cancellAllPreviousRequestWithSameTag(RescribeConstants.CASE_DETAILS_LIST);
                super.onBackPressed();
            }
        }
    }

    public DrawerLayout getActivityDrawer() {
        return mDrawer;
    }

// Filter Start

    @Override
    public void onApply() {

        DrFilterRequestModel drFilterRequestModel = new DrFilterRequestModel();
        drFilterRequestModel.setPatientId(Integer.parseInt(RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, DoctorListActivity.this)));
        drFilterRequestModel.setDocIds(docIdList);
        drFilterRequestModel.setDocSpecialities(doctorSpecialityList);
        drFilterRequestModel.setStartDate(filterFragment.getFromDate());
        drFilterRequestModel.setEndDate(filterFragment.getToDate());
        drFilterRequestModel.setCases(caseList);

        Gson gson = new Gson();
        CommonMethods.Log("FilterRequest", gson.toJson(drFilterRequestModel, DrFilterRequestModel.class));
        mDrawer.closeDrawer(GravityCompat.END);

        if (mFragmentManager.getBackStackEntryCount() > 0)
            mFragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        DoctorFilteredListFragment doctorFilteredListFragment = DoctorFilteredListFragment.newInstance(drFilterRequestModel);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.viewContainer, doctorFilteredListFragment, FILTERED_FRAGMENT_TAG);
        fragmentTransaction.addToBackStack(BACK_STACK_ROOT_TAG);
        fragmentTransaction.commit();

    }

    @Override
    public void onSelectDoctors() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        SelectDoctorsFragment selectDoctorsFragment = SelectDoctorsFragment.newInstance(filterDoctorListModel.getDoctorNameModel().getDoctorNames(), getResources().getString(R.string.doctors));
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.add(R.id.nav_view, selectDoctorsFragment, getResources().getString(R.string.doctors));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onSelectSpeciality() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        SelectSpecialityFragment selectSpecialityFragment = SelectSpecialityFragment.newInstance(filterDoctorSpecialityListModel.getDoctorSpecialitiesModel().getDoctorSpecialities(), getResources().getString(R.string.doctors_speciality));
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

        for (DoctorData doctorDetail : filterDoctorListModel.getDoctorNameModel().getDoctorNames()) {
            doctorDetail.setSelected(false);
        }

        for (DoctorSpecialityData doctorSpecialityData : filterDoctorSpecialityListModel.getDoctorSpecialitiesModel().getDoctorSpecialities()) {
            doctorSpecialityData.setSelected(false);
        }

        for (CaseDetailsData caseDetails : caseDetailsListModel.getCaseHeadingsModel().getCaseHeadings())
            caseDetails.setSelected(false);

        filterFragment.notifyCaseDetails();
    }

    @Override
    public void onFragmentBack() {
        mFragmentManager.popBackStack();
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
        for (DoctorData doctorDetail : filterDoctorListModel.getDoctorNameModel().getDoctorNames()) {
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
        for (DoctorSpecialityData doctorSpecialityData : filterDoctorSpecialityListModel.getDoctorSpecialitiesModel().getDoctorSpecialities()) {
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

        for (CaseDetailsData caseDetailsData : caseDetailsListModel.getCaseHeadingsModel().getCaseHeadings()) {
            if (caseDetailsData.isSelected())
                caseList.add(caseDetailsData.getName());
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (customResponse instanceof FilterDoctorListModel) {
            filterDoctorListModel = (FilterDoctorListModel) customResponse;
        } else if (customResponse instanceof FilterDoctorSpecialityListModel) {
            filterDoctorSpecialityListModel = (FilterDoctorSpecialityListModel) customResponse;
        } else if (customResponse instanceof CaseDetailsListModel) {
            caseDetailsListModel = (CaseDetailsListModel) customResponse;

            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            filterFragment = FilterFragment.newInstance(caseDetailsListModel.getCaseHeadingsModel().getCaseHeadings());
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
}