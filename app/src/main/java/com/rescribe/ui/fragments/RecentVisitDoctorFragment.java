package com.rescribe.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.rescribe.R;
import com.rescribe.adapters.DoctorConnectSearchAdapter;
import com.rescribe.adapters.DoctorSpecialistBookAppointmentAdapter;
import com.rescribe.adapters.ShowRecentVisitedDoctorPagerAdapter;
import com.rescribe.model.book_appointment.doctor_data.DoctorServicesModel;
import com.rescribe.ui.customesViews.CircleIndicator;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import droidninja.filepicker.utils.GridSpacingItemDecoration;

import static com.facebook.FacebookSdk.getApplicationContext;


public class RecentVisitDoctorFragment extends Fragment implements DoctorSpecialistBookAppointmentAdapter.OnSpecialityClickListener {
    ArrayList<String> arrlist = new ArrayList<String>(5);
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.circleIndicator)
    CircleIndicator circleIndicator;
    @BindView(R.id.pickSpeciality)
    CustomTextView pickSpeciality;
    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    @BindView(R.id.fragmentContainer)
    RelativeLayout fragmentContainer;
    @BindView(R.id.doubtMessage)
    RelativeLayout doubtMessage;
    private View mRootView;
    Unbinder unbinder;
    DoctorServicesModel mDoctorServicesModel = new DoctorServicesModel();
    DoctorSpecialistBookAppointmentAdapter mDoctorConnectSearchAdapter;


    public RecentVisitDoctorFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.recent_visit_doctor, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mDoctorServicesModel = getArguments().getParcelable(RescribeConstants.DOCTOR_DATA_REQUEST);
        }
        init(mRootView);
        return mRootView;
/*
        return inflater.inflate(R.layout.recent_visit_doctor, container, false);
*/

    }

    private void init(View mRootView) {
        if(mDoctorServicesModel==null){
            pickSpeciality.setVisibility(View.GONE);
            doubtMessage.setVisibility(View.GONE);
            emptyListView.setVisibility(View.VISIBLE);
        }else {
            emptyListView.setVisibility(View.GONE);
            pickSpeciality.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            doubtMessage.setVisibility(View.VISIBLE);
            arrlist.add("Sunita Phadke");
            arrlist.add("Amruta Nikam");
            arrlist.add("Gunjan Gangwar");
            arrlist.add("Tejaswini Patil");
            ViewPager viewpager = (ViewPager) mRootView.findViewById(R.id.viewpager);
            CircleIndicator indicator = (CircleIndicator) mRootView.findViewById(R.id.circleIndicator);
            viewpager.setAdapter(new ShowRecentVisitedDoctorPagerAdapter(getActivity(), arrlist));
            indicator.setViewPager(viewpager);
            listView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
            listView.setLayoutManager(layoutManager);
            listView.setItemAnimator(new DefaultItemAnimator());
            int spanCount = 3; // 3 columns
            int spacing = 50; // 50px
            boolean includeEdge = true;
            listView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
            mDoctorConnectSearchAdapter = new DoctorSpecialistBookAppointmentAdapter(getActivity(),this,mDoctorServicesModel.getDoctorSpecialities());
            listView.setAdapter(mDoctorConnectSearchAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public static RecentVisitDoctorFragment newInstance(DoctorServicesModel doctorServicesModel) {
        RecentVisitDoctorFragment fragment = new RecentVisitDoctorFragment();
        Bundle args = new Bundle();
        args.putParcelable(RescribeConstants.DOCTOR_DATA_REQUEST,doctorServicesModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setOnClickOfDoctorSpeciality(Bundle bundleData) {

    }
}
