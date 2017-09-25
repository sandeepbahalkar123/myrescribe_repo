package com.rescribe.ui.fragments.book_appointment;

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
import com.rescribe.model.doctors.appointments.AptList;
import com.rescribe.ui.activities.AppointmentActivity;
import java.util.ArrayList;

/**
 * Created by jeetal on 25/9/17.
 */

public class ComplaintsFragment extends Fragment {

    private static final String DATA = "DATA";
    private View mRootView;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ComplaintsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ComplaintsFragment newInstance(Bundle data) {
        ComplaintsFragment fragment = new ComplaintsFragment();
        Bundle args = new Bundle();
       // args.putString(DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.still_in_doubt, container, false);

      //  mParentActivity = (AppointmentActivity) getActivity();

        Bundle arguments = getArguments();
        /*if (arguments != null) {
            mAppointmentTypeName = arguments.getString(DATA);
        }*/
        init();
        return mRootView;
    }

    private void init() {

    }

    @Override
    public void onResume() {
        super.onResume();
      //  setDoctorListAdapter();
    }


}
