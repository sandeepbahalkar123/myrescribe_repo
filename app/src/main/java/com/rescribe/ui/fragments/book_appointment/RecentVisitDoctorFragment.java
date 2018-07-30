package com.rescribe.ui.fragments.book_appointment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.adapters.DoctorSpecialistBookAppointmentAdapter;
import com.rescribe.adapters.book_appointment.SortByClinicAndDoctorNameAdapter;
import com.rescribe.adapters.dashboard.ShowDoctorViewPagerAdapter;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.helpers.book_appointment.ServicesCardViewImpl;
import com.rescribe.helpers.dashboard.DashboardHelper;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.book_appointment.doctor_data.DoctorServicesModel;
import com.rescribe.model.book_appointment.filterdrawer.request_model.BookAppointFilterRequestModel;
import com.rescribe.model.dashboard_api.doctors.DoctorListModel;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.activities.book_appointment.MapActivityPlotNearByDoctor;
import com.rescribe.ui.activities.book_appointment.ServicesFilteredDoctorListActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.customesViews.EditTextWithDeleteButton;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.rescribe.util.RescribeConstants.SUCCESS;
import static com.rescribe.util.RescribeConstants.TASK_DOCTORLIST_API;


public class RecentVisitDoctorFragment extends Fragment implements DoctorSpecialistBookAppointmentAdapter.OnSpecialityClickListener, HelperResponse, SortByClinicAndDoctorNameAdapter.OnDataListViewVisible {

    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.filterListLayout)
    LinearLayout mFilterListLayout;
    @BindView(R.id.pickSpeciality)
    CustomTextView pickSpeciality;
    @BindView(R.id.emptyListView)
    RelativeLayout mFilterDocListEmptyListView;
    @BindView(R.id.specialityEmptyListView)
    RelativeLayout mSpecialityEmptyListView;
    @BindView(R.id.fragmentContainer)
    RelativeLayout fragmentContainer;
    @BindView(R.id.recyclerViewLinearLayout)
    LinearLayout recyclerViewLinearLayout;
    @BindView(R.id.searchView)
    EditTextWithDeleteButton searchView;
    @BindView(R.id.searchBarLinearLayout)
    LinearLayout searchBarLinearLayout;
    @BindView(R.id.listView)
    RecyclerView showDoctorsRecyclerView;
    @BindView(R.id.recentDoctorLayout)
    LinearLayout recentDoctorLayout;
    @BindView(R.id.prevBtn)
    ImageView prevBtn;
    @BindView(R.id.nextBtn)
    ImageView nextBtn;
    @BindView(R.id.bookAppointSpecialityListView)
    RecyclerView mBookAppointSpecialityListView;
    @BindView(R.id.whiteUnderLine)
    TextView whiteUnderLine;
    @BindView(R.id.rightFab)
    FloatingActionButton rightFab;
    @BindView(R.id.leftFab)
    FloatingActionButton leftFab;
    @BindView(R.id.viewDoctorPager)
    LinearLayout viewDoctorPager;
    Unbinder unbinder;

    DoctorSpecialistBookAppointmentAdapter mDoctorConnectSearchAdapter;
    private SortByClinicAndDoctorNameAdapter mSortByClinicAndDoctorNameAdapter;
    private ArrayList<DoctorList> doctorListByClinics;
    private DoctorDataHelper mDoctorDataHelper;
    private ServicesCardViewImpl mServiceCardDataViewBuilder;
    private DoctorServicesModel mReceivedDoctorServicesModel;
    private ShowDoctorViewPagerAdapter mRecentVisitedDoctorPagerAdapter;
    private String mUserSelectedLocation;
    private boolean isLocationChanged;
    private ArrayList<DoctorList> mPreviousLoadedDocList;
    private boolean isFilterApplied = false;
    private String mReceivedTitle = "";
    private AppDBHelper appDBHelper;
    private DashboardHelper mDashboardHelper;

    public RecentVisitDoctorFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.recent_visit_doctor, container, false);
        //  hideSoftKeyboard();
        unbinder = ButterKnife.bind(this, mRootView);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        init();
        mPreviousLoadedDocList = ServicesCardViewImpl.getReceivedDoctorDataList();
        return mRootView;

    }

    private void init() {
        mReceivedDoctorServicesModel = new DoctorServicesModel();
        appDBHelper = new AppDBHelper(getContext());
        mDashboardHelper = new DashboardHelper(getContext(), this);
//        pickSpeciality.setVisibility(View.INVISIBLE);
//        recyclerViewLinearLayout.setVisibility(View.INVISIBLE);
        recentDoctorLayout.setVisibility(View.INVISIBLE);
//        viewDoctorPager.setVisibility(View.INVISIBLE);
        doConfigureDataListViewVisibility(false, false);
        Bundle arguments = getArguments();
        if (arguments != null) mReceivedTitle = arguments.getString(getString(R.string.title));

        mServiceCardDataViewBuilder = new ServicesCardViewImpl(this.getContext(), (BookAppointDoctorListBaseActivity) getActivity());
        mDoctorDataHelper = new DoctorDataHelper(this.getContext(), this);

        searchView.addTextChangedListener(new EditTextWithDeleteButton.TextChangedListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()) {
                    mSortByClinicAndDoctorNameAdapter.getFilter().filter(s);
                } else {
                    doConfigureDataListViewVisibility(false, false);
                    recentDoctorLayout.setVisibility(View.VISIBLE);
                    mFilterListLayout.setVisibility(View.VISIBLE);
                    if (isFilterApplied) {
                        mServiceCardDataViewBuilder.setReceivedDoctorDataList(mPreviousLoadedDocList);
                        setDoctorListAdapter(false);
                    }
                }
            }
        });
    }

    public static RecentVisitDoctorFragment newInstance(Bundle b) {
        RecentVisitDoctorFragment fragment = new RecentVisitDoctorFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @OnClick({R.id.prevBtn, R.id.nextBtn, R.id.rightFab, R.id.leftFab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rightFab:
                BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
                activity.getActivityDrawerLayout().openDrawer(GravityCompat.END);
                break;
            case R.id.leftFab:
                if (mSortByClinicAndDoctorNameAdapter.isListByClinicName()) {
                    Intent intent = new Intent(getActivity(), MapActivityPlotNearByDoctor.class);
                    intent.putExtra(getString(R.string.doctor_data), mSortByClinicAndDoctorNameAdapter.getSortedListByClinicNameOrDoctorName());
                    intent.putExtra(RescribeConstants.TITLE, mReceivedTitle);
                    startActivity(intent);
                } else {
                    // this list is sorted for plotting map for each clinic location, the values of clinicName and doctorAddress are set in string here, which are coming from arraylist.
                    doctorListByClinics = new ArrayList<>();
                    ArrayList<DoctorList> sortedListByClinicNameOrDoctorName = mSortByClinicAndDoctorNameAdapter.getSortedListByClinicNameOrDoctorName();
                    for (int i = 0; i < sortedListByClinicNameOrDoctorName.size(); i++) {
                        DoctorList doctorList = sortedListByClinicNameOrDoctorName.get(i);
                        if (doctorList.getClinicDataList().size() > 0) {
                            for (int j = 0; j < doctorList.getClinicDataList().size(); j++) {
                                DoctorList doctorListByClinic = new DoctorList();
                                doctorListByClinic = doctorList;
                                doctorListByClinic.setNameOfClinicString(doctorList.getClinicDataList().get(j).getClinicName());
                                doctorListByClinic.setAddressOfDoctorString(doctorList.getClinicDataList().get(j).getClinicAddress());
                                doctorListByClinics.add(doctorListByClinic);
                            }
                        }
                    }
                    Intent intent = new Intent(getActivity(), MapActivityPlotNearByDoctor.class);
                    intent.putExtra(getString(R.string.doctor_data), doctorListByClinics);
                    intent.putExtra(RescribeConstants.TITLE, mReceivedTitle);
                    startActivity(intent);
                }
                break;
        }
    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        switch (mOldDataTag) {
            case RescribeConstants.TASK_SET_FAVOURITE_DOCTOR:
                if (customResponse != null) {
                    CommonBaseModelContainer responseFavouriteDoctorBaseModel = (CommonBaseModelContainer) customResponse;
                    if (responseFavouriteDoctorBaseModel.getCommonRespose().isSuccess()) {
                        mServiceCardDataViewBuilder.updateFavStatusForDoctorDataObject(ServicesCardViewImpl.getUserSelectedDoctorListDataObject(), appDBHelper);
                        setUpViewPager(true);
                        mSortByClinicAndDoctorNameAdapter.updateClickedItemFavImage();
                    }
                }
                break;

            case TASK_DOCTORLIST_API:
                DoctorListModel doctorListModel = (DoctorListModel) customResponse;
                if (doctorListModel.getCommon().getStatusCode().equals(SUCCESS)) {
                    setUpViewPager(false);
                }
                break;
            case RescribeConstants.TASK_SERVICES_DOC_LIST_FILTER:
                BookAppointmentBaseModel received = (BookAppointmentBaseModel) customResponse;
                if (received != null) {
                    DoctorServicesModel doctorServices = received.getDoctorServicesModel();
                    if (doctorServices != null) {
                        mReceivedDoctorServicesModel = doctorServices;
                        mServiceCardDataViewBuilder.setReceivedDoctorDataList(doctorServices.getDoctorList());
                        setDoctorListAdapter(isFilterApplied);
                    }
                }

                break;
        }

    }

    private void setDoctorListAdapter(boolean isShowSortByClinicAndDoctorNameAdapter) {
        mSortByClinicAndDoctorNameAdapter = new SortByClinicAndDoctorNameAdapter(getActivity(), ServicesCardViewImpl.getReceivedDoctorDataList(), mServiceCardDataViewBuilder, RecentVisitDoctorFragment.this, this);
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        showDoctorsRecyclerView.setLayoutManager(linearlayoutManager);
        showDoctorsRecyclerView.setNestedScrollingEnabled(false);
        // off recyclerView Animation
        RecyclerView.ItemAnimator animator = showDoctorsRecyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator)
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        showDoctorsRecyclerView.setAdapter(mSortByClinicAndDoctorNameAdapter);

        if (mReceivedDoctorServicesModel.getDoctorSpecialities().isEmpty()) {
            pickSpeciality.setVisibility(View.GONE);
            mSpecialityEmptyListView.setVisibility(View.VISIBLE);
            prevBtn.setVisibility(View.INVISIBLE);
            nextBtn.setVisibility(View.INVISIBLE);
            viewDoctorPager.setVisibility(View.INVISIBLE);
            mBookAppointSpecialityListView.setVisibility(View.GONE);
        } else {
            recyclerViewLinearLayout.setVisibility(View.VISIBLE);
            mBookAppointSpecialityListView.setVisibility(View.VISIBLE);
            whiteUnderLine.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);
            mSpecialityEmptyListView.setVisibility(View.GONE);

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
            mBookAppointSpecialityListView.setLayoutManager(layoutManager);
            mBookAppointSpecialityListView.setItemAnimator(new DefaultItemAnimator());

            mBookAppointSpecialityListView.setNestedScrollingEnabled(false);

            mDoctorConnectSearchAdapter = new DoctorSpecialistBookAppointmentAdapter(getActivity(), this, mReceivedDoctorServicesModel.getDoctorSpecialities());
            mBookAppointSpecialityListView.setAdapter(mDoctorConnectSearchAdapter);
            pickSpeciality.setVisibility(View.VISIBLE);
            viewDoctorPager.setVisibility(View.VISIBLE);
        }
        //---set data ---------

        //------manage sorted_listview visibility---
        if (isShowSortByClinicAndDoctorNameAdapter) {
            mSortByClinicAndDoctorNameAdapter.getFilter().filter(searchView.getText().toString());
            doConfigureDataListViewVisibility(true, mReceivedDoctorServicesModel.getDoctorList().isEmpty());
        } else {
            doConfigureDataListViewVisibility(false, false);
        }
    }

    private void setUpViewPager(boolean isFavorite) {
        if (mViewpager != null) {
            mServiceCardDataViewBuilder.setReceivedDoctorDataList(CommonMethods.getCategoryDoctorsFromDb(appDBHelper));
            ArrayList<String> specialities = new ArrayList<>();
            // get all specialities
            Cursor specialitiesCursor = appDBHelper.getDoctorsSpecialities();
            if (specialitiesCursor.moveToFirst()) {
                do {
                    specialities.add(specialitiesCursor.getString(specialitiesCursor.getColumnIndex(AppDBHelper.DOC_DATA.SPECIALITY)));
                } while (specialitiesCursor.moveToNext());
            }
            mReceivedDoctorServicesModel.setDoctorSpecialities(specialities);

            int currentItem = mViewpager.getCurrentItem();

            Map<String, Integer> dataMap = new LinkedHashMap<>();
            ArrayList<DoctorList> myAppoint = mServiceCardDataViewBuilder.getCategoryWiseDoctorList(getString(R.string.my_appointments), -1);
            ArrayList<DoctorList> sponsered = mServiceCardDataViewBuilder.getCategoryWiseDoctorList(getString(R.string.sponsored_doctor), -1);
            ArrayList<DoctorList> recently_visit_doctor = mServiceCardDataViewBuilder.getCategoryWiseDoctorList(getString(R.string.recently_visited_doctor), -1);
            ArrayList<DoctorList> favoriteList = mServiceCardDataViewBuilder.getFavouriteDocList(-1);

            dataMap.put(getString(R.string.my_appointments), myAppoint.size());
            dataMap.put(getString(R.string.sponsored_doctor), sponsered.size());
            dataMap.put(getString(R.string.recently_visited_doctor), recently_visit_doctor.size());
            dataMap.put(getString(R.string.favorite), favoriteList.size());

            ArrayList<DoctorList> mergeList = new ArrayList<>();

            if (!myAppoint.isEmpty())
                mergeList.add(myAppoint.get(0));

            if (!sponsered.isEmpty())
                mergeList.add(sponsered.get(0));

            if (!recently_visit_doctor.isEmpty())
                mergeList.add(recently_visit_doctor.get(0));

            if (!favoriteList.isEmpty())
                mergeList.add(favoriteList.get(0));

            if (mergeList.isEmpty()) {
                mViewpager.setVisibility(View.GONE);
            } else {
                if (!isFavorite)
                    mViewpager.setVisibility(View.VISIBLE);

                mRecentVisitedDoctorPagerAdapter = new ShowDoctorViewPagerAdapter(getActivity(), mergeList, mServiceCardDataViewBuilder, dataMap, this);
                mViewpager.setAdapter(mRecentVisitedDoctorPagerAdapter);
                mViewpager.setClipToPadding(false);
                //------
                int pager_padding = getResources().getDimensionPixelSize(R.dimen.pager_padding);
                mViewpager.setPadding(pager_padding, 0, pager_padding, 0);
                int pager_margin = getResources().getDimensionPixelSize(R.dimen.pager_margin);
                mViewpager.setPageMargin(pager_margin);
            }

            // set pre state
            mViewpager.setCurrentItem(currentItem);
            if (showDoctorsRecyclerView.getVisibility() != View.VISIBLE)
                setDoctorListAdapter(false);
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        isLocationChanged = doGetLatestDoctorListOnLocationChange();
        if (!isLocationChanged) {
            if (mReceivedDoctorServicesModel != null) {
                if (showDoctorsRecyclerView.getVisibility() == View.VISIBLE && mSortByClinicAndDoctorNameAdapter != null) {
                    mSortByClinicAndDoctorNameAdapter.notifyDataSetChanged();
                }
            }
        }
        if (searchView != null) {
            if (searchView.getText().toString().isEmpty()) {
                setUpViewPager(false);
            }
        }
    }


    @Override
    public void setOnClickOfDoctorSpeciality(Bundle bundleData) {
        String specialityName = bundleData.getString(RescribeConstants.ITEM_DATA);
        Intent intent = new Intent(getActivity(), ServicesFilteredDoctorListActivity.class);
        bundleData.putString(RescribeConstants.TITLE, specialityName);
        intent.putExtra(RescribeConstants.PICK_SPECAILITY, RescribeConstants.SORT_BY_SPECIALITY);
        intent.putExtras(bundleData);
        startActivity(intent);
    }

    public boolean doGetLatestDoctorListOnLocationChange() {
        HashMap<String, String> userSelectedLocationInfo = RescribeApplication.getUserSelectedLocationInfo();
        String selectedLocation = userSelectedLocationInfo.get(getString(R.string.location));
        if (selectedLocation != null) {
            if (selectedLocation.equalsIgnoreCase(mUserSelectedLocation)) {
                mUserSelectedLocation = selectedLocation;
            } else {
                setUpViewPager(false);
                mUserSelectedLocation = selectedLocation;
                String[] split = mUserSelectedLocation.split(",");
                if (split.length == 2) {
//                    mDoctorDataHelper.doGetDoctorData(split[1], split[0], mComplaintsUserSearchFor);
                    mDashboardHelper.doGetDashboard(split[1]);
                    return true;
                } else {
//                    mDoctorDataHelper.doGetDoctorData("", "", mComplaintsUserSearchFor);
                    mDashboardHelper.doGetDashboard("");
                    return true;
                }
            }
        }
        return false;
    }

    public void onApplyClicked(Bundle data) {
        isFilterApplied = true;
        BookAppointFilterRequestModel requestModel = data.getParcelable(getString(R.string.filter));
        mDoctorDataHelper.doFilteringOnSelectedConfig(requestModel, null);
    }

    public void onResetClicked() {

    }

    // It is implemented for SortByClinicAndDoctorNameAdapter and for local level view manage.
    @Override
    public void doConfigureDataListViewVisibility(boolean flag, boolean isShowEmptyListView) {
        if (flag) {
            recentDoctorLayout.setVisibility(View.GONE);
            leftFab.setVisibility(View.VISIBLE);
            rightFab.setVisibility(View.VISIBLE);
            showDoctorsRecyclerView.setVisibility(View.VISIBLE);
            mFilterDocListEmptyListView.setVisibility(View.GONE);

            if (isShowEmptyListView) {
                leftFab.setVisibility(View.GONE);
                rightFab.setVisibility(View.GONE);
                mFilterDocListEmptyListView.setVisibility(View.VISIBLE);
                showDoctorsRecyclerView.setVisibility(View.GONE);
            }
        } else {
            leftFab.setVisibility(View.GONE);
            rightFab.setVisibility(View.GONE);
            mFilterDocListEmptyListView.setVisibility(View.GONE);
            recentDoctorLayout.setVisibility(View.VISIBLE);
            showDoctorsRecyclerView.setVisibility(View.GONE);
        }

    }

}

