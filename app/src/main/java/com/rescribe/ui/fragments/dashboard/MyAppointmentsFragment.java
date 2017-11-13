package com.rescribe.ui.fragments.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rescribe.R;
import com.rescribe.adapters.book_appointment.BookAppointFilteredDocList;
import com.rescribe.adapters.dashboard.MyAppointmentDashBoardAdapter;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.book_appointment.doctor_data.DoctorServicesModel;
import com.rescribe.ui.activities.AppointmentActivity;
import com.rescribe.ui.activities.HomePageActivity;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.activities.dashboard.DashboardShowCategoryNameByListBaseActivity;
import com.rescribe.ui.activities.dashboard.DoctorDescriptionBaseActivity;
import com.rescribe.ui.fragments.book_appointment.BookAppointDoctorDescriptionFragment;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.rescribe.util.RescribeConstants.DOCTOR_DATA_REQUEST_CODE;

/**
 * Created by jeetal on 7/11/17.
 */

public class MyAppointmentsFragment extends Fragment implements BookAppointFilteredDocList.OnFilterDocListClickListener, HelperResponse {

    Unbinder unbinder;
    private static Bundle args;
    @BindView(R.id.myAppoinmentRecyclerView)
    RecyclerView myAppoinmentRecyclerView;
    private View mRootView;
    ArrayList<DoctorList> dashboardDoctorLists;
    private DividerItemDecoration mDividerItemDecoration;
    private BookAppointFilteredDocList mMyAppointmentAdapter;
    private int mClickedDocIdToUpdateFavoriteStatus;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyAppointmentsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MyAppointmentsFragment newInstance(Bundle data) {

        MyAppointmentsFragment fragment = new MyAppointmentsFragment();
        args = new Bundle();
        args = data;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.show_myappointment_layout, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        init();
        return mRootView;
    }

    private void init() {
        dashboardDoctorLists = args.getParcelableArrayList(getString(R.string.clicked_item_data));
        if (dashboardDoctorLists != null) {
            // mMyAppointmentAdapter = new MyAppointmentDashBoardAdapter(getActivity(), dashboardDoctorLists,this);
            mMyAppointmentAdapter = new BookAppointFilteredDocList(getActivity(), dashboardDoctorLists, this, this);
            LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            myAppoinmentRecyclerView.setLayoutManager(linearlayoutManager);
            myAppoinmentRecyclerView.setHasFixedSize(true);
            myAppoinmentRecyclerView.setAdapter(mMyAppointmentAdapter);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        //  setDoctorListAdapter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onClickOfDoctorRowItem(Bundle bundleData) {
        if (bundleData.getString(getString(R.string.do_operation)).equalsIgnoreCase(getString(R.string.doctor_details))) {
            DoctorList mClickedDoctorObject = bundleData.getParcelable(getString(R.string.clicked_item_data));

            if (mClickedDoctorObject.getCategoryName().equalsIgnoreCase(getString(R.string.my_appointments))) {
                Intent intent = new Intent(getActivity(), AppointmentActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), DoctorDescriptionBaseActivity.class);
                intent.putExtra(getString(R.string.toolbarTitle), mClickedDoctorObject.getCategoryName());
                intent.putExtra(getString(R.string.clicked_item_data), mClickedDoctorObject);
                startActivityForResult(intent, DOCTOR_DATA_REQUEST_CODE);
            }

        } else if (bundleData.getString(getString(R.string.do_operation)).equalsIgnoreCase(getString(R.string.favorite))) {
            DoctorList mClickedDoctorObject = bundleData.getParcelable(getString(R.string.clicked_item_data));
            mClickedDocIdToUpdateFavoriteStatus = mClickedDoctorObject.getDocId();
            boolean status = mClickedDoctorObject.getFavourite() ? false : true;
            new DoctorDataHelper(this.getContext(), this).setFavouriteDoctor(status, mClickedDoctorObject.getDocId());
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        switch (mOldDataTag) {
            case RescribeConstants.TASK_SET_FAVOURITE_DOCTOR:
                CommonBaseModelContainer temp = (CommonBaseModelContainer) customResponse;
                if (temp.getCommonRespose().isSuccess()) {
                    DashboardShowCategoryNameByListBaseActivity activity = (DashboardShowCategoryNameByListBaseActivity) getActivity();

                    for (int i = 0; i < dashboardDoctorLists.size(); i++) {
                        DoctorList tempObject = dashboardDoctorLists.get(i);
                        if (tempObject.getDocId() == mClickedDocIdToUpdateFavoriteStatus) {
                            tempObject.setFavourite(tempObject.getFavourite() ? false : true);
                            activity.replaceDoctorListById(tempObject.getDocId(), tempObject, getString(R.string.object_update_common_to_doc));
                        }
                    }
                    mMyAppointmentAdapter.notifyDataSetChanged();
                }
                CommonMethods.showToast(getActivity(), temp.getCommonRespose().getStatusMessage());
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

}