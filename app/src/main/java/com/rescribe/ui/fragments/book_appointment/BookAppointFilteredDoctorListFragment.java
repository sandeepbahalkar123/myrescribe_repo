package com.rescribe.ui.fragments.book_appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.book_appointment.BookAppointFilteredDocListAdapter;
import com.rescribe.adapters.dashboard.ShowDoctorViewPagerAdapter;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.helpers.book_appointment.ServicesCardViewImpl;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.book_appointment.doctor_data.DoctorServicesModel;
import com.rescribe.model.book_appointment.filterdrawer.request_model.BookAppointFilterRequestModel;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.activities.book_appointment.MapActivityPlotNearByDoctor;
import com.rescribe.ui.activities.book_appointment.ServicesFilteredDoctorListActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class BookAppointFilteredDoctorListFragment extends Fragment implements HelperResponse{


    @BindView(R.id.listView)
    RecyclerView mDoctorListView;
    @BindView(R.id.emptyListView)
    RelativeLayout mEmptyListView;
    @BindView(R.id.rightFab)
    FloatingActionButton mFilterFab;
    @BindView(R.id.leftFab)
    FloatingActionButton mLocationFab;
    Unbinder unbinder;
    BookAppointFilteredDocListAdapter mBookAppointFilteredDocListAdapterAdapter;
    private ArrayList<DoctorList> mReceivedList = new ArrayList<>();
    private DoctorDataHelper mDoctorDataHelper;
    private String mClickedItemDataTypeValue;
    private String mReceivedTitle;
    private String mClickedItemDataValue;
    private boolean mIsFavoriteList;
    private String mUserSelectedLocation;
    private ServicesCardViewImpl mServicesCardViewImpl;
    private HashMap<String, String> mComplaintHashMap;
    private ArrayList<DoctorList> mReceivedPreviousDoctorList;
    private AppDBHelper appDBHelper;
    private ImageView favoriteIcon;
    private DoctorList favoriteDoctor;

    public BookAppointFilteredDoctorListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mRootView = inflater.inflate(R.layout.filtered_doc_viewlist_with_bottom_fab_margin, container, false);
        unbinder = ButterKnife.bind(this, mRootView);

        init(getArguments());
        return mRootView;
    }

    public static BookAppointFilteredDoctorListFragment newInstance(Bundle b) {
        BookAppointFilteredDoctorListFragment fragment = new BookAppointFilteredDoctorListFragment();
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
            mClickedItemDataValue = args.getString(getString(R.string.clicked_item_data));
            mIsFavoriteList = args.getBoolean(getString(R.string.favorite));

            mComplaintHashMap = (HashMap<String, String>) args.getSerializable(getString(R.string.complaints));
        }
        // OnBackPressed of this page original list of doctor should be shown , thats why that list is set here and accessed in ServicesFilteredDoctorListActivity which is base of this fragment.
        setReceivedPreviousDoctorList(ServicesCardViewImpl.getReceivedDoctorDataList());
        mDoctorDataHelper = new DoctorDataHelper(getContext(), this);
        mServicesCardViewImpl = new ServicesCardViewImpl(this.getContext(), (ServicesFilteredDoctorListActivity) getActivity());

        ////////////////////// onResume

        doGetReceivedListBasedOnClickedItemData();
        setDoctorListAdapter();
        ServicesFilteredDoctorListActivity activity = (ServicesFilteredDoctorListActivity) getActivity();

        //*****************
        // THIS IS HACK, TO CALL API IN CASE OF COMAPINT-MAP!=NULL
        if (mComplaintHashMap != null) {
            activity.setLocationChangeViewClicked(true);
            mLocationFab.setVisibility(View.VISIBLE);
            mFilterFab.setVisibility(View.VISIBLE);
        }
        //*****************

        if (activity.isLocationChangeViewClicked()) {
            doGetLatestDoctorListOnLocationChange(mComplaintHashMap);
            activity.setLocationChangeViewClicked(false);
        }
    }

    private void doGetReceivedListBasedOnClickedItemData() {
        //------------

        if (getString(R.string.doctors_speciality).equalsIgnoreCase(mClickedItemDataTypeValue)) {
            mReceivedList = mServicesCardViewImpl.filterDocListBySpeciality(mClickedItemDataValue);
            mLocationFab.setVisibility(View.VISIBLE);
            mFilterFab.setVisibility(View.VISIBLE);
        } else {
            mLocationFab.setVisibility(View.GONE);
            mFilterFab.setVisibility(View.GONE);
            if (mIsFavoriteList) {
                mReceivedList = mServicesCardViewImpl.getFavouriteDocList(-1);
            } else if (mClickedItemDataValue != null) {
                mReceivedList = mServicesCardViewImpl.getCategoryWiseDoctorList(mClickedItemDataValue, -1);
            } else if (mComplaintHashMap != null) {
                // THIS IS HACK, TO CALL API IN CASE OF COMAPINT-MAP!=NULL
                mReceivedList = ServicesCardViewImpl.getDoctorListByUniqueDocIDs(ServicesCardViewImpl.getReceivedDoctorDataList());
            } else {
                mReceivedList = ServicesCardViewImpl.getReceivedDoctorDataList();
            }
        }
        //------------
        if (mReceivedList == null) {
            mReceivedList = new ArrayList<>();
        }
        //-----------
    }

    public void doGetLatestDoctorListOnLocationChange(HashMap<String, String> mComplaintsUserSearchFor) {
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
                } else {
                    mDoctorDataHelper.doGetDoctorData("", "", mComplaintsUserSearchFor);
                }
            }
        }
    }


    private void setDoctorListAdapter() {

        if (mReceivedList.size() == 0) {
            isDataListViewVisible(false);
        } else {
            isDataListViewVisible(true);
            mBookAppointFilteredDocListAdapterAdapter = new BookAppointFilteredDocListAdapter(getActivity(), mReceivedList, mServicesCardViewImpl, this, mClickedItemDataTypeValue, mReceivedTitle);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mDoctorListView.setLayoutManager(layoutManager);
            mDoctorListView.setHasFixedSize(true);
            // off recyclerView Animation
            RecyclerView.ItemAnimator animator = mDoctorListView.getItemAnimator();
            if (animator instanceof SimpleItemAnimator)
                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
            mDoctorListView.setAdapter(mBookAppointFilteredDocListAdapterAdapter);
        }
    }


    public ArrayList<DoctorList> getReceivedPreviousDoctorList() {
        return mReceivedPreviousDoctorList;
    }

    public void setReceivedPreviousDoctorList(ArrayList<DoctorList> mReceivedPreviousDoctorList) {
        if (this.mReceivedPreviousDoctorList == null)
            this.mReceivedPreviousDoctorList = mReceivedPreviousDoctorList;
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
                    mBookAppointFilteredDocListAdapterAdapter.updateClickedItemFavImage();
                   /* favoriteDoctor.setFavourite(!favoriteDoctor.getFavourite());
                    if (favoriteDoctor.getFavourite())
                        favoriteIcon.setImageResource(R.drawable.favourite_icon);
                    else favoriteIcon.setImageResource(R.drawable.favourite_line_icon);

                    // update in database
                    appDBHelper.insertfavoriteData(String.valueOf(favoriteDoctor.getDocId()),favoriteDoctor.getFavourite());*/
                }
                break;
            case RescribeConstants.TASK_SERVICES_DOC_LIST_FILTER:
                BookAppointmentBaseModel received = (BookAppointmentBaseModel) customResponse;
                if (received != null) {
                    DoctorServicesModel doctorServices = received.getDoctorServicesModel();
                    if (doctorServices != null) {
                        new ServicesCardViewImpl(this.getContext(), (ServicesFilteredDoctorListActivity) getActivity()).setReceivedDoctorDataList(doctorServices.getDoctorList());
                        // mReceivedList = doctorServices.filterDocListBySpeciality(mReceivedTitle);
                        doGetReceivedListBasedOnClickedItemData();
                    }
                }
                setDoctorListAdapter();
                break;
            case RescribeConstants.TASK_GET_DOCTOR_DATA:
                DoctorServicesModel receivedDoctorServicesModel = DoctorDataHelper.getReceivedDoctorServicesModel();

                if (receivedDoctorServicesModel != null) {

                    //   new ServicesCardViewImpl(this.getContext(), (ServicesFilteredDoctorListActivity) getActivity()).setReceivedDoctorDataList(receivedDoctorServicesModel.getDoctorList());
                    mServicesCardViewImpl.setReceivedDoctorDataList(receivedDoctorServicesModel.getDoctorList());
                    doGetReceivedListBasedOnClickedItemData();
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

    public void isDataListViewVisible(boolean flag) {
        if (flag) {
            mEmptyListView.setVisibility(View.GONE);
            mDoctorListView.setVisibility(View.VISIBLE);
            mLocationFab.setVisibility(View.VISIBLE);
            mFilterFab.setVisibility(View.VISIBLE);
        } else {
            mLocationFab.setVisibility(View.GONE);
            mFilterFab.setVisibility(View.GONE);
            mEmptyListView.setVisibility(View.VISIBLE);
            mDoctorListView.setVisibility(View.GONE);
        }

        //--- invisible left & right button if mClickedItemDataTypeValue!=doctor's speciality
        if (!getString(R.string.doctors_speciality).equalsIgnoreCase(mClickedItemDataTypeValue)) {
            mLocationFab.setVisibility(View.GONE);
            mFilterFab.setVisibility(View.GONE);
        }

        // THIS IS HACK, TO CALL API IN CASE OF COMAPINT-MAP!=NULL
        if (mComplaintHashMap != null) {
            mLocationFab.setVisibility(View.VISIBLE);
            mFilterFab.setVisibility(View.VISIBLE);
        }
        //*****************
    }

    @OnClick({R.id.rightFab, R.id.leftFab})
    public void onViewClicked(View view) {
        ServicesFilteredDoctorListActivity activity;
        switch (view.getId()) {
            case R.id.rightFab:
                activity = (ServicesFilteredDoctorListActivity) getActivity();
                activity.getActivityDrawerLayout().openDrawer(GravityCompat.END);
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
                //intent.putExtra(getString(R.string.clicked_item_data_type_value), getString(R.string.filter));
                intent.putExtra(getString(R.string.toolbarTitle), mReceivedTitle);
                startActivityForResult(intent, RescribeConstants.DOCTOR_LOCATION_CHANGE_FROM_MAP_REQUEST_CODE);
                break;
        }
    }

    public void onApplyClicked(Bundle data) {
        BookAppointFilterRequestModel requestModel = data.getParcelable(getString(R.string.filter));
        mDoctorDataHelper.doFilteringOnSelectedConfig(requestModel, mComplaintHashMap);
    }

    public void onResetClicked() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
/*
    @Override
    public void onFavoriteClick(DoctorList doctorList, ImageView favorite, CustomTextView sizeOfList) {
        boolean status = !doctorList.getFavourite();
        new DoctorDataHelper(getContext(), this).setFavouriteDoctor(status, doctorList.getDocId());
        favoriteIcon = favorite;
        favoriteDoctor = doctorList;
    }*/


   /* // TODO: NEED TO ADD SAME IN RECENT VISIT FILTER
    @Override
    public void onClickOfDoctorRowItem(Bundle bundleData) {
        DoctorList mClickedDoctorObject = bundleData.getParcelable(getString(R.string.clicked_item_data));
        ServicesCardViewImpl.setUserSelectedDoctorListDataObject(mClickedDoctorObject);
        //------------
        if (bundleData.getString(getString(R.string.do_operation)).equalsIgnoreCase(getString(R.string.doctor_details))) {

            if (mClickedDoctorObject.getCategoryName().equalsIgnoreCase(getString(R.string.my_appointments))) {
                Intent intent = new Intent(getActivity(), AppointmentActivity.class);
                startActivity(intent);
            } else {
                bundleData.putString(getString(R.string.toolbarTitle), mReceivedTitle);
                Intent intent = new Intent(getActivity(), DoctorDescriptionBaseActivity.class);
                intent.putExtras(bundleData);
                startActivity(intent);
            }
        } else if (bundleData.getString(getString(R.string.do_operation)).equalsIgnoreCase(getString(R.string.favorite))) {
            boolean status = mClickedDoctorObject.getFavourite() ? false : true;
            mDoctorDataHelper.setFavouriteDoctor(status, mClickedDoctorObject.getDocId());
        }
    }
*/
}
