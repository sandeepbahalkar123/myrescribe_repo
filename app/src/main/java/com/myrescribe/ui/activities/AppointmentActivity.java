package com.myrescribe.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.myrescribe.R;
import com.myrescribe.helpers.doctor.DoctorHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.doctors.appointments.DoctorAppointment;
import com.myrescribe.model.doctors.appointments.DoctorAppointmentModel;
import com.myrescribe.ui.fragments.AppointmentFragment;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppointmentActivity extends AppCompatActivity implements HelperResponse {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appointmentViewpager)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    String[] mFragmentTitleList = new String[3];

    private DoctorHelper mDoctorHelper;
    private ArrayList<DoctorAppointment> mAppointmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoinment);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.my_appointments));
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mFragmentTitleList[0] = getString(R.string.upcoming);
        mFragmentTitleList[1] = getString(R.string.completed);
        mFragmentTitleList[2] = getString(R.string.cancelled);
        setupViewPager();

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);


        initialize();
    }

    private void initialize() {
        mDoctorHelper = new DoctorHelper(this);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < mFragmentTitleList.length; i++) {
            Fragment fragment = AppointmentFragment.newInstance(mFragmentTitleList[i]); // pass data here
            adapter.addFragment(fragment, mFragmentTitleList[i]); // pass title here
        }
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        DoctorAppointmentModel model = (DoctorAppointmentModel) customResponse;
        if (model != null) {
            ArrayList<DoctorAppointment> doctorAppointmentList = model.getDoctorAppointmentList();
            if (doctorAppointmentList != null) {
                mAppointmentList = doctorAppointmentList;
                setupViewPager();
            }
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

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mDoctorHelper.doGetDoctorAppointment();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AppointmentActivity.this, HomePageActivity.class);
        intent.putExtra(MyRescribeConstants.ALERT, false);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
    }

    public ArrayList<DoctorAppointment> getAppointmentList(String type) {
        ArrayList<DoctorAppointment> tempList = new ArrayList<>();
        if (mAppointmentList != null) {
            for (DoctorAppointment dataObject :
                    mAppointmentList) {
                if (dataObject.getAppointmentType().equalsIgnoreCase(type)) {
                    tempList.add(dataObject);
                }
            }
        }
        Collections.sort(tempList, new Comparator<DoctorAppointment>() {
            @Override
            public int compare(DoctorAppointment o1, DoctorAppointment o2) {
                Date m1Date = CommonMethods.convertStringToDate(o1.getAptDate(), MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD_hh_mm_a);
                Date m2Date = CommonMethods.convertStringToDate(o2.getAptDate(), MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD_hh_mm_a);

                return m2Date.compareTo(m1Date);
            }
        });
        return tempList;
    }

}
