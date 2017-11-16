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
import com.rescribe.helpers.book_appointment.ServicesCardViewImpl;
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
import com.rescribe.ui.activities.book_appointment.ServicesFilteredDoctorListActivity;
import com.rescribe.ui.activities.dashboard.DoctorDescriptionBaseActivity;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class BookAppointFilteredDoctorListFragment extends Fragment implements View.OnClickListener, HelperResponse, BookAppointFilteredDocList.OnFilterDocListClickListener {


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
    private ArrayList<DoctorList> mReceivedList = new ArrayList<>();

    private DoctorDataHelper mDoctorDataHelper;
    private String mClickedItemDataTypeValue;
    private String mReceivedTitle;
    private DoctorList mClickedDocListToUpdateFavStatus;

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
            mReceivedList = args.getParcelableArrayList(getString(R.string.clicked_item_data));
            if (mReceivedList == null) {
                mReceivedList = new ArrayList<>();
            }
            mClickedItemDataTypeValue = args.getString(getString(R.string.clicked_item_data_type_value));
            mReceivedTitle = args.getString(getString(R.string.title));
            //  BookAppointDoctorListBaseActivity.setToolBarTitle(mReceivedTitle, true);
            if (getString(R.string.doctors_speciality).equalsIgnoreCase(mClickedItemDataTypeValue)) {
                mLocationFab.setVisibility(View.VISIBLE);
                mFilterFab.setVisibility(View.VISIBLE);
            } else {
                mLocationFab.setVisibility(View.GONE);
                mFilterFab.setVisibility(View.GONE);
            }
        }

        mDoctorDataHelper = new DoctorDataHelper(getContext(), this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
        setDoctorListAdapter();
    }

    private void setDoctorListAdapter() {

        if (mReceivedList.size() == 0) {
            isDataListViewVisible(false);
        } else {
            isDataListViewVisible(true);
            mBookAppointFilteredDocListAdapter = new BookAppointFilteredDocList(getActivity(), mReceivedList, this, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mDoctorListView.setLayoutManager(layoutManager);
            mDoctorListView.setHasFixedSize(true);
            mDoctorListView.setAdapter(mBookAppointFilteredDocListAdapter);
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
                if (temp.getCommonRespose().isSuccess()) {
                    //--------
                    ServicesCardViewImpl.updateFavStatusForDoctorDataObject(mClickedDocListToUpdateFavStatus);
                    //--------
                    for (DoctorList dataObject :
                            mReceivedList) {
                        if (dataObject.getDocId() == mClickedDocListToUpdateFavStatus.getDocId()) {
                            dataObject.setFavourite(dataObject.getFavourite() ? false : true);
                        }
                    }
                    mBookAppointFilteredDocListAdapter.notifyDataSetChanged();
                }
                break;
            case RescribeConstants.TASK_SERVICES_DOC_LIST_FILTER:
                BookAppointmentBaseModel received = (BookAppointmentBaseModel) customResponse;
                if (received != null) {
                    DoctorServicesModel doctorServices = received.getDoctorServicesModel();
                    if (doctorServices != null) {
                        mReceivedList = doctorServices.filterDocListBySpeciality(mReceivedTitle);
                    }
                }
                setDoctorListAdapter();
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
                //   intent.putParcelableArrayListExtra(getString(R.string.doctor_data), new ArrayList<DoctorList>());
                intent.putExtra(getString(R.string.toolbarTitle), mReceivedTitle);
                startActivity(intent);
                break;
        }
    }

    // TODO: NEED TO ADD SAME IN RECENT VISIT FILTER
    @Override
    public void onClickOfDoctorRowItem(Bundle bundleData) {
        if (bundleData.getString(getString(R.string.do_operation)).equalsIgnoreCase(getString(R.string.doctor_details))) {
            DoctorList mClickedDoctorObject = bundleData.getParcelable(getString(R.string.clicked_item_data));

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
            mClickedDocListToUpdateFavStatus = bundleData.getParcelable(getString(R.string.clicked_item_data));
            boolean status = mClickedDocListToUpdateFavStatus.getFavourite() ? false : true;
            mDoctorDataHelper.setFavouriteDoctor(status, mClickedDocListToUpdateFavStatus.getDocId());
        }
    }

    public void onApplyClicked(Bundle data) {
        BookAppointFilterRequestModel requestModel = data.getParcelable(getString(R.string.filter));

        mDoctorDataHelper.doFilteringOnSelectedConfig(requestModel);
    }

    public void onResetClicked() {

    }
}
