package com.rescribe.ui.fragments.health_offers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.health_offers.HealthOffersAdapter;

/**
 * Created by jeetal on 14/11/17.
 */

public class HealthOffersFragment  extends Fragment {

    private static final String DATA = "DATA";
    private View mRootView;
    private RecyclerView mAppointmentListView;
    private RelativeLayout mEmptyListView;
    private String mAppointmentTypeName;
    private HealthOffersAdapter mHealthOffersAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HealthOffersFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HealthOffersFragment newInstance(String data) {
        HealthOffersFragment fragment = new HealthOffersFragment();
        Bundle args = new Bundle();
        args.putString(DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.health_offers_item_layout, container, false);

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
        mAppointmentListView.setVisibility(View.GONE);
        mEmptyListView.setVisibility(View.VISIBLE);
//        setDoctorListAdapter();
    }

    private void setDoctorListAdapter() {
        mHealthOffersAdapter = new HealthOffersAdapter();
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mAppointmentListView.setLayoutManager(linearlayoutManager);
        mAppointmentListView.setHasFixedSize(true);

        mAppointmentListView.setAdapter(mHealthOffersAdapter);
    }


}