package com.rescribe.ui.fragments.book_appointment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.book_appointment.doctor_data.DoctorServicesModel;
import com.rescribe.model.book_appointment.filterdrawer.request_model.BookAppointFilterRequestModel;
import com.rescribe.model.doctor_connect.ChatDoctor;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.activities.book_appointment.MapActivityPlotNearByDoctor;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class BookAppointFilteredDoctorListFragment extends Fragment implements View.OnClickListener, HelperResponse, BookAppointFilteredDocList.OnFilterDocListClickListener, BookAppointDoctorListBaseActivity.OnActivityDrawerListener {


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

    public BookAppointFilteredDoctorListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.global_recycle_view_list, container, false);
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
        if (args != null) {
            BookAppointDoctorListBaseActivity.setToolBarTitle(args.getString(getString(R.string.clicked_item_data)), true);
            mSelectedSpeciality = args.getString(getString(R.string.clicked_item_data));
        }

        mLocationFab.setVisibility(View.VISIBLE);
        mFilterFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
        BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
        receivedBookAppointmentBaseModel = activity.getReceivedBookAppointmentBaseModel();
        setDoctorListAdapter(receivedBookAppointmentBaseModel);
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
                    mReceivedList = doctorList;
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
                intent.putParcelableArrayListExtra(getString(R.string.doctor_data),receivedBookAppointmentBaseModel.getDoctorServicesModel().getDoctorList());
                startActivity(intent);
                /*activity = (BookAppointDoctorListBaseActivity) getActivity();
                activity.loadFragment(ShowNearByDoctorsOnMapFragment.newInstance(new Bundle()), false);*/
                break;
        }
    }

    @Override
    public void onClickOfDoctorRowItem(Bundle bundleData) {
        bundleData.putString(getString(R.string.toolbarTitle), mSelectedSpeciality);
        BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
        activity.loadFragment(BookAppointDoctorDescriptionFragment.newInstance(bundleData), false);
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
        //TODO, API IS NOT IMPLEMENTED YET, CALL API FROM HERE
        CommonMethods.Log("onApplyClicked", "" + requestModel.toString());
    }

    @Override
    public void onResetClicked() {

    }
}
