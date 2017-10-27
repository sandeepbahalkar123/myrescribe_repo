package com.rescribe.ui.fragments.book_appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.book_appointment.BookAppointFilteredDocList;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.book_appointment.doctor_data.DoctorServicesModel;
import com.rescribe.model.book_appointment.filterdrawer.request_model.BookAppointFilterRequestModel;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.activities.book_appointment.MapActivityPlotNearByDoctor;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class BookAppointFilteredDoctorListFragment extends Fragment implements View.OnClickListener, HelperResponse, BookAppointFilteredDocList.OnFilterDocListClickListener, BookAppointDoctorListBaseActivity.OnActivityDrawerListener, BookAppointDoctorListBaseActivity.AddUpdateViewDataListener {


    @BindView(R.id.listView)
    RecyclerView mDoctorListView;
    @BindView(R.id.emptyListView)
    RelativeLayout mEmptyListView;
    @BindView(R.id.rightFab)
    FloatingActionButton mFilterFab;
    @BindView(R.id.leftFab)
    FloatingActionButton mLocationFab;
    Unbinder unbinder;
    private View mRootView;
    BookAppointFilteredDocList mBookAppointFilteredDocListAdapter;
    private String mSelectedSpeciality;
    BookAppointmentBaseModel receivedBookAppointmentBaseModel;
    private ArrayList<DoctorList> mReceivedList;
    private static Bundle args;

    private DoctorDataHelper mDoctorDataHelper;

    public BookAppointFilteredDoctorListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.filtered_doc_viewlist_with_bottom_fab_margin, container, false);
        unbinder = ButterKnife.bind(this, mRootView);

        init(getArguments());
        return mRootView;
    }

    public static BookAppointFilteredDoctorListFragment newInstance(Bundle b) {
        BookAppointFilteredDoctorListFragment fragment = new BookAppointFilteredDoctorListFragment();
        args = b;
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    private void init(Bundle args) {
        mDoctorListView.setNestedScrollingEnabled(false);
        if (args != null) {
            mSelectedSpeciality = args.getString(getString(R.string.clicked_item_data));
            BookAppointDoctorListBaseActivity.setToolBarTitle(mSelectedSpeciality, true);
        }

        mLocationFab.setVisibility(View.VISIBLE);
        mFilterFab.setVisibility(View.VISIBLE);
        mDoctorDataHelper = new DoctorDataHelper(getContext(), this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
        updateViewData();
    }

    private void setDoctorListAdapter(BookAppointmentBaseModel receivedBookAppointmentBaseModel) {
        if (receivedBookAppointmentBaseModel == null) {
            isDataListViewVisible(false);
        } else {
            DoctorServicesModel doctorServicesModel = receivedBookAppointmentBaseModel.getDoctorServicesModel();
            if (doctorServicesModel == null) {
                isDataListViewVisible(false);
            } else {
                ArrayList<DoctorList> doctorList = doctorServicesModel.getDoctorList();
                if (doctorList.size() == 0) {
                    isDataListViewVisible(false);
                } else {
                    isDataListViewVisible(true);
                    ArrayList<DoctorList> doctorListByClinics = new ArrayList<>();
                    for(int i =0;i<doctorList.size();i++){
                        if(doctorList.get(i).getClinicName().size()>0){
                            for(int j = 0;j<doctorList.get(i).getClinicName().size();j++){
                                DoctorList doctorListByClinic = new DoctorList();
                                doctorListByClinic.setDocId(doctorList.get(i).getDocId());
                                doctorListByClinic.setDocName(doctorList.get(i).getDocName());
                                doctorListByClinic.setDoctorImageUrl(doctorList.get(i).getDoctorImageUrl());
                                doctorListByClinic.setExperience(doctorList.get(i).getExperience());
                                doctorListByClinic.setSpeciality(doctorList.get(i).getSpeciality());
                                doctorListByClinic.setAmount(doctorList.get(i).getAmount());
                                doctorListByClinic.setDistance(doctorList.get(i).getDistance());
                                doctorListByClinic.setRecentlyVisited(doctorList.get(i).getRecentlyVisited());
                                doctorListByClinic.setFavourite(doctorList.get(i).getFavourite());
                                doctorListByClinic.setAboutDoctor(doctorList.get(i).getAboutDoctor());
                                doctorListByClinic.setDegree(doctorList.get(i).getDegree());
                                doctorListByClinic.setWaitingTime(doctorList.get(i).getWaitingTime());
                                doctorListByClinic.setRating(doctorList.get(i).getRating());
                                doctorListByClinic.setTokenNo(doctorList.get(i).getTokenNo());
                                doctorListByClinic.setPracticePlaceInfos(doctorList.get(i).getPracticePlaceInfos());
                                doctorListByClinic.setAvailableTimeSlots(doctorList.get(i).getAvailableTimeSlots());
                                doctorListByClinic.setOpenToday(doctorList.get(i).getOpenToday());
                                doctorListByClinic.setTotalReview(doctorList.get(i).getTotalReview());
                                doctorListByClinic.setNameOfClinic(doctorList.get(i).getClinicName().get(j));
                                doctorListByClinic.setAddressOfDoctor(doctorList.get(i).getDoctorAddress().get(j));
                                doctorListByClinics.add(doctorListByClinic);
                            }
                        }
                    }
                    mReceivedList = doctorListByClinics;
                    if (filterDataOnDocSpeciality().size() == 0) {
                        isDataListViewVisible(false);
                        mLocationFab.setVisibility(View.GONE);
                        mFilterFab.setVisibility(View.GONE);
                    } else {
                        mBookAppointFilteredDocListAdapter = new BookAppointFilteredDocList(getActivity(), filterDataOnDocSpeciality(), this, this);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        mDoctorListView.setLayoutManager(layoutManager);
                        mDoctorListView.setHasFixedSize(true);
                        mDoctorListView.setAdapter(mBookAppointFilteredDocListAdapter);
                    }
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

        switch (mOldDataTag) {
            case RescribeConstants.TASK_SET_FAVOURITE_DOCTOR:
                CommonBaseModelContainer temp = (CommonBaseModelContainer) customResponse;
                CommonMethods.showToast(getActivity(), temp.getCommonRespose().getStatusMessage());
                break;
            case RescribeConstants.TASK_SERVICES_DOC_LIST_FILTER:
                BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
                activity.setReceivedBookAppointmentBaseModel((BookAppointmentBaseModel) customResponse);
                updateViewData();
                break;
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

    public void isDataListViewVisible(boolean flag) {
        if (flag) {
            mEmptyListView.setVisibility(View.GONE);
            mDoctorListView.setVisibility(View.VISIBLE);
        } else {
            mEmptyListView.setVisibility(View.VISIBLE);
            mDoctorListView.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.rightFab, R.id.leftFab})
    public void onViewClicked(View view) {
        BookAppointDoctorListBaseActivity activity;
        switch (view.getId()) {
            case R.id.rightFab:
                activity = (BookAppointDoctorListBaseActivity) getActivity();
                activity.getActivityDrawerLayout().openDrawer(GravityCompat.END);
                break;
            case R.id.leftFab:
                Intent intent = new Intent(getActivity(), MapActivityPlotNearByDoctor.class);
                intent.putParcelableArrayListExtra(getString(R.string.doctor_data), mReceivedList);
                intent.putExtra(getString(R.string.toolbarTitle), mSelectedSpeciality);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onClickOfDoctorRowItem(Bundle bundleData) {
        if (bundleData.getString(getString(R.string.do_operation)).equalsIgnoreCase(getString(R.string.doctor_details))) {
            bundleData.putString(getString(R.string.toolbarTitle), mSelectedSpeciality);
            BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
            activity.loadFragment(BookAppointDoctorDescriptionFragment.newInstance(bundleData), false);
        } else if (bundleData.getString(getString(R.string.do_operation)).equalsIgnoreCase(getString(R.string.favorite))) {
            DoctorList mClickedDoctorObject = bundleData.getParcelable(getString(R.string.clicked_item_data));

            boolean status = mClickedDoctorObject.getFavourite() ? false : true;
            mDoctorDataHelper.setFavouriteDoctor(status, "" + mClickedDoctorObject.getDocId());
        }
    }

    private ArrayList<DoctorList> filterDataOnDocSpeciality() {

        ArrayList<DoctorList> doctors = this.mReceivedList;

        ArrayList<DoctorList> dataList = new ArrayList<>();
        if (mSelectedSpeciality == null) {
            return doctors;
        } else {
            for (DoctorList listObject :
                    doctors) {
                if (mSelectedSpeciality.equalsIgnoreCase(listObject.getSpeciality())) {
                    dataList.add(listObject);
                }
            }
        }
        return dataList;
    }

    @Override
    public void onApplyClicked(Bundle data) {
        BookAppointFilterRequestModel requestModel = data.getParcelable(getString(R.string.filter));

        mDoctorDataHelper.doFilteringOnSelectedConfig(requestModel);
    }

    @Override
    public void onResetClicked() {

    }

    @Override
    public void updateViewData() {
        BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
        receivedBookAppointmentBaseModel = activity.getReceivedBookAppointmentBaseModel();
        setDoctorListAdapter(receivedBookAppointmentBaseModel);
        if (RescribeConstants.BLANK.equalsIgnoreCase(args.getString(getString(R.string.clicked_item_data))) || args.getString(getString(R.string.clicked_item_data)) == null) {
            BookAppointDoctorListBaseActivity.setToolBarTitle(getString(R.string.doctorss), true);
        } else {
            BookAppointDoctorListBaseActivity.setToolBarTitle(args.getString(getString(R.string.clicked_item_data)), true);
        }


        HashMap<String, String> userSelectedLocationInfo = DoctorDataHelper.getUserSelectedLocationInfo();
        String s = userSelectedLocationInfo.get(getString(R.string.location));
        if (s != null) {
            BookAppointDoctorListBaseActivity.setSelectedLocationText(s);
        }
    }
}
