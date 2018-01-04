package com.rescribe.ui.fragments.book_appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.book_appointment.BookAppointFilteredDocList;
import com.rescribe.adapters.book_appointment.SortByClinicAndDoctorNameAdapter;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.helpers.book_appointment.ServicesCardViewImpl;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.book_appointment.doctor_data.DoctorServicesModel;
import com.rescribe.model.book_appointment.filterdrawer.request_model.BookAppointFilterRequestModel;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.activities.book_appointment.BookAppointListOnLocationSelection;
import com.rescribe.ui.activities.book_appointment.MapActivityPlotNearByDoctor;
import com.rescribe.ui.activities.book_appointment.ServicesFilteredDoctorListActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.customesViews.EditTextWithDeleteButton;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class BookAppointListOnLocationSelectionFragment extends Fragment implements HelperResponse, SortByClinicAndDoctorNameAdapter.OnDataListViewVisible {


    @BindView(R.id.listView)
    RecyclerView mDoctorListView;
    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.emptyListView)
    RelativeLayout mEmptyListView;
    @BindView(R.id.rightFab)
    FloatingActionButton mFilterFab;
    @BindView(R.id.leftFab)
    FloatingActionButton mLocationFab;
    @BindView(R.id.searchView)
    EditTextWithDeleteButton mSearchView;
    Unbinder unbinder;
    private View mRootView;
    private ArrayList<DoctorList> mReceivedList = new ArrayList<>();
    private DoctorDataHelper mDoctorDataHelper;
    private String mClickedItemDataTypeValue;
    private String mReceivedTitle;
    private String mClickedItemDataValue;
    private boolean mIsFavoriteList;
    private String mUserSelectedLocation;
    private ServicesCardViewImpl mServicesCardViewImpl;
    private HashMap<String, String> mComplaintHashMap;
    private SortByClinicAndDoctorNameAdapter mSortByClinicAndDoctorNameAdapter;

    public BookAppointListOnLocationSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.book_appoint_list_on_location_selection, container, false);
        unbinder = ButterKnife.bind(this, mRootView);

        init(getArguments());
        return mRootView;
    }

    public static BookAppointListOnLocationSelectionFragment newInstance(Bundle b) {
        BookAppointListOnLocationSelectionFragment fragment = new BookAppointListOnLocationSelectionFragment();
        Bundle args = b;
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    private void init(Bundle args) {
        mDoctorListView.setNestedScrollingEnabled(false);
        if (args != null) {
            mClickedItemDataTypeValue = args.getString(getString(R.string.clicked_item_data_type_value));
            mReceivedTitle = args.getString(getString(R.string.toolbarTitle));
            title.setText("" + mReceivedTitle);
            mClickedItemDataValue = args.getString(getString(R.string.clicked_item_data));
            mIsFavoriteList = args.getBoolean(getString(R.string.favorite));

            mComplaintHashMap = (HashMap<String, String>) args.getSerializable(getString(R.string.complaints));
        }
        mDoctorDataHelper = new DoctorDataHelper(getContext(), this);
        mServicesCardViewImpl = new ServicesCardViewImpl(this.getContext(), (BookAppointListOnLocationSelection) getActivity());

        mSearchView.addTextChangedListener(new EditTextWithDeleteButton.TextChangedListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()) {
                    mSortByClinicAndDoctorNameAdapter.getFilter().filter(s);
                } else {
                    mSortByClinicAndDoctorNameAdapter.getFilter().filter("");
                    doConfigureDataListViewVisibility(false, false);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        doGetLatestDoctorListOnLocationChange(null);
    }

    public void onBackPressed() {
        BookAppointListOnLocationSelection activity = (BookAppointListOnLocationSelection) getActivity();
        if (activity.getActivityDrawerLayout().isDrawerOpen(GravityCompat.END)) {
            activity.getActivityDrawerLayout().closeDrawer(GravityCompat.END);
        } else {
            activity.onBackPressed();
        }
    }

    private void setDoctorListAdapter() {

        if (mReceivedList.size() == 0) {
            doConfigureDataListViewVisibility(true, true);
        } else {
            doConfigureDataListViewVisibility(true, false);

            ArrayList<DoctorList> receivedDoctorDataList = ServicesCardViewImpl.getDoctorListByUniqueDocIDs(ServicesCardViewImpl.getReceivedDoctorDataList());

            mSortByClinicAndDoctorNameAdapter = new SortByClinicAndDoctorNameAdapter(getActivity(), receivedDoctorDataList, mServicesCardViewImpl, this, this, mDoctorListView);
            LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mDoctorListView.setLayoutManager(linearlayoutManager);
            mDoctorListView.setNestedScrollingEnabled(false);
            mDoctorListView.setAdapter(mSortByClinicAndDoctorNameAdapter);
            mDoctorListView.setHasFixedSize(true);
            // off recyclerView Animation
            RecyclerView.ItemAnimator animator = mDoctorListView.getItemAnimator();
            if (animator instanceof SimpleItemAnimator)
                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
            mDoctorListView.setAdapter(mSortByClinicAndDoctorNameAdapter);
        }
    }

    public boolean doGetLatestDoctorListOnLocationChange(HashMap<String, String> mComplaintsUserSearchFor) {
        HashMap<String, String> userSelectedLocationInfo = RescribeApplication.getUserSelectedLocationInfo();
        String selectedLocation = userSelectedLocationInfo.get(getString(R.string.location));
        if (selectedLocation != null) {
            if (selectedLocation.equalsIgnoreCase(mUserSelectedLocation)) {
                mUserSelectedLocation = selectedLocation;
            } else {
                mUserSelectedLocation = selectedLocation;
                String[] split = mUserSelectedLocation.split(",");
                if (split.length == 2) {
                    mDoctorDataHelper.doGetDoctorData(split[1], split[0], mComplaintsUserSearchFor);
                    return true;
                } else {
                    mDoctorDataHelper.doGetDoctorData("", "", mComplaintsUserSearchFor);
                    return true;

                }
            }
        }
        return false;
    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

        switch (mOldDataTag) {
            case RescribeConstants.TASK_SET_FAVOURITE_DOCTOR:
                CommonBaseModelContainer temp = (CommonBaseModelContainer) customResponse;
                // CommonMethods.showToast(getActivity(), temp.getCommonRespose().getStatusMessage());
                if (temp.getCommonRespose().isSuccess()) {
                    //--------
                    ServicesCardViewImpl.updateFavStatusForDoctorDataObject(ServicesCardViewImpl.getUserSelectedDoctorListDataObject());
                    //--------
                    mSortByClinicAndDoctorNameAdapter.updateClickedItemFavImage();
                }
                break;
            case RescribeConstants.TASK_SERVICES_DOC_LIST_FILTER:
                BookAppointmentBaseModel received = (BookAppointmentBaseModel) customResponse;
                if (received != null) {
                    DoctorServicesModel doctorServices = received.getDoctorServicesModel();
                    if (doctorServices != null) {
                        new ServicesCardViewImpl(this.getContext(), (BookAppointListOnLocationSelection) getActivity()).setReceivedDoctorDataList(doctorServices.getDoctorList());
                        // mReceivedList = doctorServices.filterDocListBySpeciality(mReceivedTitle);
                    }
                }
                setDoctorListAdapter();
                break;
            case RescribeConstants.TASK_GET_DOCTOR_DATA:
                DoctorServicesModel receivedDoctorServicesModel = DoctorDataHelper.getReceivedDoctorServicesModel();
                if (receivedDoctorServicesModel != null) {
                    mReceivedList = receivedDoctorServicesModel.getDoctorList();
                    new ServicesCardViewImpl(this.getContext(), (BookAppointListOnLocationSelection) getActivity()).setReceivedDoctorDataList(mReceivedList);
                    setDoctorListAdapter();
                }
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


    @OnClick({R.id.rightFab, R.id.leftFab, R.id.bookAppointmentBackButton})
    public void onViewClicked(View view) {
        BookAppointListOnLocationSelection activity;
        switch (view.getId()) {
            case R.id.rightFab:
                activity = (BookAppointListOnLocationSelection) getActivity();
                activity.getActivityDrawerLayout().openDrawer(GravityCompat.END);
                break;
            case R.id.bookAppointmentBackButton:
                onBackPressed();
                break;
            case R.id.leftFab:
                //this list is sorted for plotting map for each clinic location, the values of clinicName and doctorAddress are set in string here, which are coming from arraylist.
                ArrayList<DoctorList> doctorListByClinics = new ArrayList<>();
                for (int i = 0; i < mReceivedList.size(); i++) {
                    if (mReceivedList.get(i).getClinicDataList().size() > 0) {
                        DoctorList doctorList = mReceivedList.get(i);
                        for (int j = 0; j < mReceivedList.get(i).getClinicDataList().size(); j++) {
                            DoctorList doctorListByClinic = new DoctorList();
                            doctorListByClinic = doctorList;
                            doctorListByClinic.setNameOfClinicString(mReceivedList.get(i).getClinicDataList().get(j).getClinicName());
                            doctorListByClinic.setAddressOfDoctorString(mReceivedList.get(i).getClinicDataList().get(j).getClinicAddress());
                            doctorListByClinics.add(doctorListByClinic);
                        }
                    }
                }
                Intent intent = new Intent(getActivity(), MapActivityPlotNearByDoctor.class);
                intent.putParcelableArrayListExtra(getString(R.string.doctor_data), doctorListByClinics);
                intent.putExtra(getString(R.string.clicked_item_data_type_value), getString(R.string.filter));
                intent.putExtra(getString(R.string.toolbarTitle), mReceivedTitle);
                startActivityForResult(intent, RescribeConstants.DOCTOR_LOCATION_CHANGE_FROM_MAP_REQUEST_CODE);
                break;
        }
    }

    public void onApplyClicked(Bundle data) {
        BookAppointFilterRequestModel requestModel = data.getParcelable(getString(R.string.filter));

        mDoctorDataHelper.doFilteringOnSelectedConfig(requestModel,null);
    }

    public void onResetClicked() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // It is implemented for SortByClinicAndDoctorNameAdapter and for local level view manage.
    @Override
    public void doConfigureDataListViewVisibility(boolean flag, boolean isShowEmptyListView) {
        if (flag) {
            mLocationFab.setVisibility(View.VISIBLE);
            mFilterFab.setVisibility(View.VISIBLE);
            mDoctorListView.setVisibility(View.VISIBLE);
            mEmptyListView.setVisibility(View.GONE);
            if (isShowEmptyListView) {
                mLocationFab.setVisibility(View.GONE);
                mFilterFab.setVisibility(View.GONE);
                mEmptyListView.setVisibility(View.VISIBLE);
                mDoctorListView.setVisibility(View.GONE);
            }
        } else {
            mLocationFab.setVisibility(View.GONE);
            mFilterFab.setVisibility(View.GONE);
            mEmptyListView.setVisibility(View.GONE);
            mDoctorListView.setVisibility(View.GONE);
        }

    }

}
