package com.rescribe.ui.fragments.book_appointment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.rescribe.ui.activities.AppointmentActivity;
import com.rescribe.ui.activities.HomePageActivity;
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
    private String mClickedItemDataTypeValue;
    private int mClickedDocIdToUpdateFavoriteStatus;

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
            mClickedItemDataTypeValue = args.getString(getString(R.string.clicked_item_data_type_value));
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
                //----This is done to filter list based on speciality selected---
                ArrayList<DoctorList> doctorList;
                //---category name like myAppointment,Sponserced doctor, recently visited doctor,[custom]favorite
                if (getString(R.string.category_name).equalsIgnoreCase(mClickedItemDataTypeValue)) {
                    //--clicked_item_data is favorite.
                    if (getString(R.string.favorite).equalsIgnoreCase(mSelectedSpeciality)) {
                        doctorList = doctorServicesModel.getFavouriteDocList();
                    } else {
                        //--clicked_item_data is categoryName like myAppointment,Sponserced doctor, recently visited doctor.
                        doctorList = doctorServicesModel.getCategoryWiseDoctorList(mSelectedSpeciality);
                    }
                } else {
                    //--- filter based on speciality of doctor.
                    doctorList = doctorServicesModel.filterDocListBySpeciality(mSelectedSpeciality);
                }
                //-------
                if (doctorList.size() == 0) {
                    isDataListViewVisible(false);
                } else {
                    mReceivedList = doctorList;

                    isDataListViewVisible(true);
                    mBookAppointFilteredDocListAdapter = new BookAppointFilteredDocList(getActivity(), mReceivedList, this, this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    mDoctorListView.setLayoutManager(layoutManager);
                    mDoctorListView.setHasFixedSize(true);
                    mDoctorListView.setAdapter(mBookAppointFilteredDocListAdapter);
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
                BookAppointDoctorListBaseActivity baseActivity = (BookAppointDoctorListBaseActivity) getActivity();
                CommonBaseModelContainer temp = (CommonBaseModelContainer) customResponse;
                CommonMethods.showToast(baseActivity, temp.getCommonRespose().getStatusMessage());
                for (DoctorList dataObject :
                        mReceivedList) {
                    if (dataObject.getDocId() == mClickedDocIdToUpdateFavoriteStatus) {
                        dataObject.setFavourite(dataObject.getFavourite() ? false : true);
                        baseActivity.replaceDoctorListById( mClickedDocIdToUpdateFavoriteStatus, dataObject, getString(R.string.object_update_common_to_doc));
                    }
                }
                mBookAppointFilteredDocListAdapter.notifyDataSetChanged();
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
            mLocationFab.setVisibility(View.VISIBLE);
            mFilterFab.setVisibility(View.VISIBLE);
        } else {
            mLocationFab.setVisibility(View.GONE);
            mFilterFab.setVisibility(View.GONE);
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
             //   intent.putParcelableArrayListExtra(getString(R.string.doctor_data), new ArrayList<DoctorList>());
                intent.putExtra(getString(R.string.toolbarTitle), mSelectedSpeciality);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onClickOfDoctorRowItem(Bundle bundleData) {
        if (bundleData.getString(getString(R.string.do_operation)).equalsIgnoreCase(getString(R.string.doctor_details))) {
            DoctorList mClickedDoctorObject = bundleData.getParcelable(getString(R.string.clicked_item_data));

            if (mClickedDoctorObject.getCategoryName().equalsIgnoreCase(getString(R.string.my_appointments))) {
                Intent intent = new Intent(getActivity(), AppointmentActivity.class);
                startActivity(intent);
            } else {
                bundleData.putString(getString(R.string.toolbarTitle), mSelectedSpeciality);
                BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
                activity.loadFragment(BookAppointDoctorDescriptionFragment.newInstance(bundleData), false);
            }

        } else if (bundleData.getString(getString(R.string.do_operation)).equalsIgnoreCase(getString(R.string.favorite))) {
            DoctorList mClickedDoctorObject = bundleData.getParcelable(getString(R.string.clicked_item_data));
            mClickedDocIdToUpdateFavoriteStatus = mClickedDoctorObject.getDocId();
            boolean status = mClickedDoctorObject.getFavourite() ? false : true;
            mDoctorDataHelper.setFavouriteDoctor(status, mClickedDoctorObject.getDocId());
        }
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
