package com.rescribe.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.AppointmentAdapter;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.doctors.appointments.AptList;
import com.rescribe.ui.activities.AppointmentActivity;
import com.rescribe.ui.activities.book_appointment.confirmation_type_activities.ConfirmAppointmentActivity;
import com.rescribe.ui.activities.book_appointment.confirmation_type_activities.ConfirmTokenInfoActivity;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import static com.rescribe.ui.fragments.book_appointment.SelectSlotTimeToBookAppointmentFragment.CONFIRM_REQUESTCODE;

/**
 * Created by jeetal on 19/7/17.
 */

public class AppointmentFragment extends Fragment implements AppointmentAdapter.OnClickOfAppointmentClickListener {

    private static final String DATA = "DATA";
    private AppointmentActivity mParentActivity;
    private View mRootView;
    private RecyclerView mAppointmentListView;
    private RelativeLayout mEmptyListView;
    private String mAppointmentTypeName;
    //    private AppointmentCancel mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AppointmentFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AppointmentFragment newInstance(String data) {
        AppointmentFragment fragment = new AppointmentFragment();
        Bundle args = new Bundle();
        args.putString(DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.globle_recycle_viewlist, container, false);

        mParentActivity = (AppointmentActivity) getActivity();

        Bundle arguments = getArguments();
        if (arguments != null) {
            mAppointmentTypeName = arguments.getString(DATA);
        }
        init();
        return mRootView;
    }

    private void init() {
        mAppointmentListView = (RecyclerView) mRootView.findViewById(R.id.listView);
        mEmptyListView = (RelativeLayout) mRootView.findViewById(R.id.emptyListView);
    }

    @Override
    public void onResume() {
        super.onResume();
        setDoctorListAdapter();
    }

    private void setDoctorListAdapter() {
        ArrayList<AptList> appointmentList = mParentActivity.getAppointmentList(mAppointmentTypeName);
        if (appointmentList != null) {
            if (appointmentList.size() == 0) {
                mAppointmentListView.setVisibility(View.GONE);
                mEmptyListView.setVisibility(View.VISIBLE);
            } else {
                mEmptyListView.setVisibility(View.GONE);
                mAppointmentListView.setVisibility(View.VISIBLE);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                mAppointmentListView.setLayoutManager(layoutManager);
                mAppointmentListView.setHasFixedSize(true);
                mAppointmentListView.setAdapter(new AppointmentAdapter(getActivity(), appointmentList, mAppointmentTypeName, this));
            }
        }
    }

    @Override
    public void setOnClickOfAppointmentLayout(AptList mAptListObject) {

        Bundle bundleData = new Bundle();
        DoctorList mDoctorList = new DoctorList();
        mDoctorList.setDocName(mAptListObject.getDoctorName());
        mDoctorList.setAptDate(mAptListObject.getAptDate());
        mDoctorList.setAptTime(mAptListObject.getAptTime());
        mDoctorList.setDegree(mAptListObject.getDoctorDegree());
        mDoctorList.setAptId(mAptListObject.getId());
        mDoctorList.setDocId(mAptListObject.getDoc_id());
        mDoctorList.setNameOfClinicString(mAptListObject.getClinic_name());
//        mDoctorList.setAddressOfDoctorString(mAptListObject.getAddress());
        mDoctorList.setDocPhone(mAptListObject.getDocPhone());
        mDoctorList.setRating(mAptListObject.getRating());
        mDoctorList.setTypedashboard(true);
        mDoctorList.setClinicAddress(mAptListObject.getClinicAddress());
        Intent intent;
        if (mAptListObject.getConfirmationType().equalsIgnoreCase(getString(R.string.token))) {
            intent = new Intent(getActivity(), ConfirmTokenInfoActivity.class);
            bundleData.putString(RescribeConstants.TOKEN_NO, mAptListObject.getTokenNumber());
            bundleData.putString(RescribeConstants.WAITING_TIME, mAptListObject.getWaitingPatientTime());
            bundleData.putString(RescribeConstants.WAITING_COUNT, mAptListObject.getWaitingPatientCount());
        } else {
            bundleData.putString(RescribeConstants.TOKEN_NO, "" + 0);
            intent = new Intent(getActivity(), ConfirmAppointmentActivity.class);
        }
        bundleData.putString(RescribeConstants.LOCATION_ID, mAptListObject.getLocationId());

        bundleData.putParcelable(getString(R.string.clicked_item_data), mDoctorList);
        intent.putExtras(bundleData);
        getActivity().startActivityForResult(intent, CONFIRM_REQUESTCODE);
    }


}
