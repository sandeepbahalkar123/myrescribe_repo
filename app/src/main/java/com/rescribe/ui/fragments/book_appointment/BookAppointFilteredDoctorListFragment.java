package com.rescribe.ui.fragments.book_appointment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.activities.book_appointment.ShowNearByDoctorsOnMapFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class BookAppointFilteredDoctorListFragment extends Fragment implements View.OnClickListener, HelperResponse {


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
    public static Bundle args;
    BookAppointFilteredDocList mBookAppointFilteredDocListAdapter;

    public BookAppointFilteredDoctorListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.global_recycle_view_list, container, false);
        unbinder = ButterKnife.bind(this, mRootView);

        init();
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

    private void init() {
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
        BookAppointmentBaseModel receivedBookAppointmentBaseModel = activity.getReceivedBookAppointmentBaseModel();
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
                    mBookAppointFilteredDocListAdapter = new BookAppointFilteredDocList(getActivity(), doctorList);
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
        switch (view.getId()) {
            case R.id.rightFab:

                break;
            case R.id.leftFab:
                BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
                activity.loadFragment(ShowNearByDoctorsOnMapFragment.newInstance(args));
                break;
        }
    }


}
