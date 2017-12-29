package com.rescribe.ui.fragments.doctor;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.rescribe.R;
import com.rescribe.adapters.CustomSpinnerAdapter;
import com.rescribe.helpers.doctor.DoctorHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.doctors.doctor_info.DoctorBaseModel;
import com.rescribe.model.doctors.doctor_info.DoctorDataModel;
import com.rescribe.model.doctors.doctor_info.DoctorDetail;
import com.rescribe.model.login.Year;
import com.rescribe.ui.activities.doctor.DoctorListActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DoctorListFragmentContainer extends Fragment implements HelperResponse {


    @BindView(R.id.backArrow)
    ImageView mBackArrow;
    @BindView(R.id.tabFragment)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private CustomSpinnerAdapter mCustomSpinAdapter;
    @BindView(R.id.year)
    Spinner mYearSpinnerView;
    @BindView(R.id.yearSingleItem)
    CustomTextView mYearSpinnerSingleItem;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.noRecords)
    ImageView noRecords;
    private ArrayList<String> mYearList = new ArrayList<>();
    private ArrayList<Year> mTimePeriodList = new ArrayList<>();
    private Year mCurrentSelectedTimePeriodTab;
    private DoctorHelper mDoctorHelper;
    private ViewPagerAdapter mViewPagerAdapter;
    private HashSet<String> mGeneratedRequestForYearList = new HashSet<>();
    private DoctorListActivity mParentActivity;
    private DrawerLayout mDrawer;
    private Context mContext;

    public DoctorListFragmentContainer() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mRootView = inflater.inflate(R.layout.doctor_list_fragment_container, container, false);
        ButterKnife.bind(this, mRootView);

        mParentActivity = (DoctorListActivity) getActivity();
        mContext = inflater.getContext();

        mDrawer = mParentActivity.getActivityDrawer();
        initialize();
        return mRootView;
    }

    public static DoctorListFragmentContainer newInstance() {
        DoctorListFragmentContainer fragment = new DoctorListFragmentContainer();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void initialize() {
        mYearList = CommonMethods.getYearForDoctorList();

        //------------

        mCustomSpinAdapter = new CustomSpinnerAdapter(mParentActivity, mYearList, ContextCompat.getColor(getActivity(), R.color.white));
        mYearSpinnerView.setAdapter(mCustomSpinAdapter);
        YearSpinnerInteractionListener listener = new YearSpinnerInteractionListener();
        mYearSpinnerView.setOnTouchListener(listener);
        mYearSpinnerView.setOnItemSelectedListener(listener);
        mYearSpinnerView.setVisibility(View.GONE);
        mYearSpinnerSingleItem.setVisibility(View.GONE);


        //-------
        mDoctorHelper = new DoctorHelper(mContext, this);
        //-------
        mCurrentSelectedTimePeriodTab = new Year();
        mCurrentSelectedTimePeriodTab.setMonthName(new SimpleDateFormat("MMM", Locale.US).format(new Date()));
        mCurrentSelectedTimePeriodTab.setYear(new SimpleDateFormat("yyyy", Locale.US).format(new Date()));
    }

    @OnClick({R.id.backArrow, R.id.fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                mParentActivity.finish();
                break;
            case R.id.fab:
                if (mDrawer.isDrawerOpen(GravityCompat.END)) {
                    mDrawer.closeDrawer(GravityCompat.END);
                } else {
                    mDrawer.openDrawer(GravityCompat.END);
                }
                break;
        }
    }


    private void setupViewPager() {
        mViewPagerAdapter.mFragmentList.clear();
        mViewPagerAdapter.mFragmentTitleList.clear();
        for (Year data :
                mTimePeriodList) {
            Fragment fragment = DoctorListFragment.createNewFragment(data); // pass data here
            mViewPagerAdapter.addFragment(fragment, data); // pass title here
        }
        mViewpager.setOffscreenPageLimit(0);
        mViewpager.setAdapter(mViewPagerAdapter);

        //------------
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                DoctorListFragment item = (DoctorListFragment) mViewPagerAdapter.getItem(position);
                Bundle arguments = item.getArguments();
                String month = arguments.getString(RescribeConstants.MONTH);
                String year = arguments.getString(RescribeConstants.YEAR);
                CommonMethods.Log("onPageSelected", month + " " + year);
                mCurrentSelectedTimePeriodTab.setMonthName(month);
                mCurrentSelectedTimePeriodTab.setYear(year);

                for (int i = 0; i < mYearList.size(); i++) {
                    if (mYearList.get(i).equalsIgnoreCase(year)) {
                        mYearSpinnerView.setSelection(i);
                        break;
                    }
                }

                if (mYearList.size() == 1) {
                    mYearSpinnerView.setVisibility(View.GONE);
                    mYearSpinnerSingleItem.setVisibility(View.VISIBLE);
                    SpannableString contentViewAllFavorite = new SpannableString(mYearList.get(0).toString());
                    contentViewAllFavorite.setSpan(new UnderlineSpan(), 0, contentViewAllFavorite.length(), 0);
                    mYearSpinnerSingleItem.setText(contentViewAllFavorite);
                } else {
                    mYearSpinnerSingleItem.setVisibility(View.GONE);
                    mYearSpinnerView.setVisibility(View.VISIBLE);
                }

                //-----THis condition calls API only once for that specific year.----
                if (!mGeneratedRequestForYearList.contains(year)) {
                    Map<String, Map<String, ArrayList<DoctorDetail>>> yearWiseSortedDoctorList = mDoctorHelper.getYearWiseSortedDoctorList();
                    if (yearWiseSortedDoctorList.get(year) == null) {
                        mGeneratedRequestForYearList.add(year);
                        mDoctorHelper.doGetDoctorList(year);
                    }
                }
                //---------
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean flag = false;
                for (int i = 0; i < mTimePeriodList.size(); i++) {
                    Year temp = mTimePeriodList.get(i);
                    if (temp.getYear().equalsIgnoreCase(mCurrentSelectedTimePeriodTab.getYear()) &&
                            temp.getMonthName().equalsIgnoreCase(mCurrentSelectedTimePeriodTab.getMonthName())) {
                        mViewpager.setCurrentItem(i);
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    mViewpager.setCurrentItem(mTimePeriodList.size());
                }
            }
        }, 0);
        //---------
    }

    //---------------
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<Year> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
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

        public void addFragment(Fragment fragment, Year title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position).getMonthName();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    private class YearSpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

        boolean mYearSpinnerConfigChange = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mYearSpinnerConfigChange = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (mYearSpinnerConfigChange) {
                // Your selection handling code here
                mYearSpinnerConfigChange = false;
                if (parent.getId() == R.id.year && !mYearSpinnerConfigChange) {
                    String selectedYear = mYearList.get(parent.getSelectedItemPosition());
                    for (int i = 0; i < mTimePeriodList.size(); i++) {
                        if (mTimePeriodList.get(i).getYear().equalsIgnoreCase("" + selectedYear)) {
                            mCurrentSelectedTimePeriodTab = mTimePeriodList.get(i);
                            mViewpager.setCurrentItem(i);
                            break;
                        }
                    }
                } else {
                    mYearSpinnerConfigChange = false;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    //---------------
    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        DoctorBaseModel baseModel = (DoctorBaseModel) customResponse;
        if (baseModel != null) {
            DoctorDataModel doctorDataModel = baseModel.getDoctorDataModel();
            if (doctorDataModel != null) {
                mTimePeriodList = doctorDataModel.getFormattedYearList();
                if (mViewPagerAdapter == null) {
                    mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
                    mTabLayout.setupWithViewPager(mViewpager);
                    mYearList = doctorDataModel.getUniqueYears();
                    mCustomSpinAdapter = new CustomSpinnerAdapter(mParentActivity, mYearList, ContextCompat.getColor(getActivity(), R.color.white));
                    mYearSpinnerView.setAdapter(mCustomSpinAdapter);
                }
                if (doctorDataModel.getReceivedYearMap().isEmpty()) {

                    mYearSpinnerView.setVisibility(View.GONE);
                    mYearSpinnerSingleItem.setVisibility(View.GONE);
                    mTabLayout.setVisibility(View.GONE);
                    noRecords.setVisibility(View.VISIBLE);
                } else {
                    noRecords.setVisibility(View.GONE);
                    if(mYearList.size()==1){
                        mYearSpinnerView.setVisibility(View.GONE);
                        mYearSpinnerSingleItem.setVisibility(View.VISIBLE);
                        mTabLayout.setVisibility(View.VISIBLE);
                        SpannableString contentViewAllFavorite = new SpannableString(mYearList.get(0).toString());
                        contentViewAllFavorite.setSpan(new UnderlineSpan(), 0, contentViewAllFavorite.length(), 0);
                        mYearSpinnerSingleItem.setText(contentViewAllFavorite);
                    }else {
                        mYearSpinnerView.setVisibility(View.VISIBLE);
                        mTabLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
        setupViewPager();
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        setupViewPager();
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        noRecords.setVisibility(View.VISIBLE);
        mYearSpinnerView.setVisibility(View.GONE);
        mTabLayout.setVisibility(View.GONE);

    }
    //---------------


    @Override
    public void onResume() {
        super.onResume();
        if (!mGeneratedRequestForYearList.contains(mCurrentSelectedTimePeriodTab.getYear())) {
            Map<String, Map<String, ArrayList<DoctorDetail>>> yearWiseSortedDoctorList = mDoctorHelper.getYearWiseSortedDoctorList();
            if (yearWiseSortedDoctorList.get(mCurrentSelectedTimePeriodTab.getYear()) == null) {
                mDoctorHelper.doGetDoctorList(mCurrentSelectedTimePeriodTab.getYear());
                mGeneratedRequestForYearList.add(mCurrentSelectedTimePeriodTab.getYear());
            }
        }
    }

    public DoctorHelper getParentDoctorHelper() {
        return mDoctorHelper;
    }

}
