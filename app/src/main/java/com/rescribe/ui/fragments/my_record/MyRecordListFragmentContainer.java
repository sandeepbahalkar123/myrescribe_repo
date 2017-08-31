package com.rescribe.ui.fragments.my_record;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.rescribe.R;
import com.rescribe.adapters.CustomSpinnerAdapter;
import com.rescribe.helpers.myrecords.MyRecordsHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.login.Year;
import com.rescribe.model.my_records.MyRecordBaseModel;
import com.rescribe.model.my_records.MyRecordDataModel;
import com.rescribe.model.my_records.MyRecordInfoAndReports;
import com.rescribe.model.my_records.MyRecordInfoMonthContainer;
import com.rescribe.model.my_records.new_pojo.NewMonth;
import com.rescribe.model.my_records.new_pojo.NewMyRecordBaseModel;
import com.rescribe.model.my_records.new_pojo.NewMyRecordDataModel;
import com.rescribe.model.my_records.new_pojo.NewOriginalData;
import com.rescribe.ui.activities.AddRecordsActivity;
import com.rescribe.ui.activities.MyRecordsActivity;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MyRecordListFragmentContainer extends Fragment implements HelperResponse {

    @BindView(R.id.backArrow)
    ImageView mBackArrow;
    @BindView(R.id.tabFragment)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private CustomSpinnerAdapter mCustomSpinAdapter;
    @BindView(R.id.year)
    Spinner mYearSpinnerView;
    @BindView(R.id.addRecordButton)
    Button addRecordButton;
    @BindView(R.id.noRecords)
    ImageView noRecords;
    private ArrayList<String> mYearList = new ArrayList<>();
    private ArrayList<Year> mTimePeriodList = new ArrayList<>();
    private Year mCurrentSelectedTimePeriodTab;
    private MyRecordsHelper mMyRecordHelper;
    private ViewPagerAdapter mViewPagerAdapter = null;
    private HashSet<String> mGeneratedRequestForYearList = new HashSet<>();
    private MyRecordsActivity mParentActivity;
    private Context mContext;
    private TreeMap<String, ArrayList<MyRecordInfoAndReports>> monthWiseSortedMyRecords = monthWiseSortedMyRecords = new TreeMap<String, ArrayList<MyRecordInfoAndReports>>(String.CASE_INSENSITIVE_ORDER);
    private MyRecordDataModel myRecordDataModel = new MyRecordDataModel();
    ;
    private NewMyRecordDataModel newRecordMainDataModel;
    private MyRecordBaseModel model = new MyRecordBaseModel();

    public MyRecordListFragmentContainer() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mRootView = inflater.inflate(R.layout.my_record_list_fragment_container, container, false);
        ButterKnife.bind(this, mRootView);

        mParentActivity = (MyRecordsActivity) getActivity();
        mContext = inflater.getContext();

        initialize();
        return mRootView;
    }

    public static MyRecordListFragmentContainer newInstance() {
        MyRecordListFragmentContainer fragment = new MyRecordListFragmentContainer();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void initialize() {

        YearSpinnerInteractionListener listener = new YearSpinnerInteractionListener();
        mYearSpinnerView.setOnTouchListener(listener);
        mYearSpinnerView.setOnItemSelectedListener(listener);
        //-------
        mMyRecordHelper = new MyRecordsHelper(mContext, this);
        //-------
        mCurrentSelectedTimePeriodTab = new Year();
        mCurrentSelectedTimePeriodTab.setMonthName(new SimpleDateFormat("MMM", Locale.US).format(new Date()));
        mCurrentSelectedTimePeriodTab.setYear(new SimpleDateFormat("yyyy", Locale.US).format(new Date()));
        //-------
        //----

        /*AppDBHelper appDBHelper = new AppDBHelper(mParentActivity);

        if (appDBHelper.dataTableNumberOfRows(RescribeConstants.TASK_GET_ALL_MY_RECORDS) > 0) {
            Cursor cursor = appDBHelper.getData(RescribeConstants.TASK_GET_ALL_MY_RECORDS);
            cursor.moveToFirst();
            String loginData = cursor.getString(cursor.getColumnIndex(AppDBHelper.COLUMN_DATA));
            Gson gson = new Gson();

            MyRecordBaseModel model = gson.fromJson(loginData, MyRecordBaseModel.class);
            MyRecordDataModel recordMainDataModel = model.getRecordMainDataModel();
            mYearList = recordMainDataModel.getUniqueYears();
            mCustomSpinAdapter = new CustomSpinnerAdapter(mParentActivity, mYearList);
            mYearSpinnerView.setAdapter(mCustomSpinAdapter);
            mTimePeriodList = recordMainDataModel.getFormattedYearList();
        }*/

        //---------

    }

    @OnClick({R.id.backArrow, R.id.addRecordButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                mParentActivity.finish();
                break;
            case R.id.addRecordButton:
                Intent intent = new Intent(mContext, AddRecordsActivity.class);
                mContext.startActivity(intent);
                break;
        }
    }


    private void setupViewPager() {
        mViewPagerAdapter.mFragmentList.clear();
        mViewPagerAdapter.mFragmentTitleList.clear();
        for (Year data :
                mTimePeriodList) {
            Fragment fragment = MyRecordListFragment.createNewFragment(data, myRecordDataModel); // pass data here
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

                MyRecordListFragment item = (MyRecordListFragment) mViewPagerAdapter.getItem(position);
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

                //-----THis condition calls API only once for that specific year.----
                if (!mGeneratedRequestForYearList.contains(year)) {
                    Map<String, Map<String, ArrayList<MyRecordInfoAndReports>>> yearWiseSortedMyRecordInfoAndReports = mMyRecordHelper.getYearWiseSortedMyRecordInfoAndReports(myRecordDataModel);
                    if (yearWiseSortedMyRecordInfoAndReports.get(year) == null) {
                        mGeneratedRequestForYearList.add(year);
//                        mMyRecordHelper.doGetAllMyRecords(year);
                        if (newRecordMainDataModel != null && !newRecordMainDataModel.getYearsMonthsData().isEmpty())
                            getYearData(Integer.parseInt(year));
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
                for (int i = 0; i < mTimePeriodList.size(); i++) {
                    Year temp = mTimePeriodList.get(i);
                    if (temp.getYear().equalsIgnoreCase(mCurrentSelectedTimePeriodTab.getYear()) &&
                            temp.getMonthName().equalsIgnoreCase(mCurrentSelectedTimePeriodTab.getMonthName())) {
                        mViewpager.setCurrentItem(i);
                        break;
                    } else if (temp.getYear().equalsIgnoreCase(mCurrentSelectedTimePeriodTab.getYear())) {
                        mViewpager.setCurrentItem(i);
                        break;
                    }
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

                // YearListener

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
        NewMyRecordBaseModel newModel = (NewMyRecordBaseModel) customResponse;

        newRecordMainDataModel = newModel.getData();
        model.setCommon(newModel.getCommon());
        model.setRecordMainDataModel(myRecordDataModel);
        myRecordDataModel.setReceivedYearMap(newRecordMainDataModel.getYearsMonthsData());

        if (newRecordMainDataModel == null || newRecordMainDataModel.getYearsMonthsData().isEmpty()) {
            noRecords.setVisibility(View.VISIBLE);
            mYearSpinnerView.setVisibility(View.GONE);
            mTabLayout.setVisibility(View.GONE);
        } else {
            noRecords.setVisibility(View.GONE);
            mYearSpinnerView.setVisibility(View.VISIBLE);
            mTabLayout.setVisibility(View.VISIBLE);
            getYearData(newRecordMainDataModel.getYearsMonthsData().get(0).getYear());
        }
    }

    private void getYearData(int year) {
        int yearPos = 0;
        for (int pos = 0; pos < newRecordMainDataModel.getYearsMonthsData().size(); pos++) {
            if (year == newRecordMainDataModel.getYearsMonthsData().get(pos).getYear()) {
                yearPos = pos;
                break;
            }
        }

        MyRecordInfoMonthContainer myRecordInfoMonthContainerNew = new MyRecordInfoMonthContainer();

        myRecordInfoMonthContainerNew.setYear(String.valueOf(newRecordMainDataModel.getOriginalData().get(yearPos).getYear()));
        NewOriginalData newOriginalData = newRecordMainDataModel.getOriginalData().get(yearPos);

        //monthWiseSortedMyRecords = new TreeMap<String, ArrayList<MyRecordInfoAndReports>>(String.CASE_INSENSITIVE_ORDER);
        for (NewMonth newMonth : newOriginalData.getMonths()) {
            ArrayList<MyRecordInfoAndReports> docVisits = newMonth.getDocVisits();
            String month = newMonth.getMonth();
            monthWiseSortedMyRecords.put(month, docVisits);
        }

        myRecordInfoMonthContainerNew.setMonthWiseSortedMyRecords(monthWiseSortedMyRecords);

        myRecordDataModel.setMyRecordInfoMonthContainer(myRecordInfoMonthContainerNew);

        MyRecordDataModel recordMainDataModel = model.getRecordMainDataModel();
        mTimePeriodList = recordMainDataModel.getFormattedYearList();
        if (mViewPagerAdapter == null) {
            mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
            mTabLayout.setupWithViewPager(mViewpager);
            mYearList = recordMainDataModel.getUniqueYears();
            mCustomSpinAdapter = new CustomSpinnerAdapter(mParentActivity, mYearList);
            mYearSpinnerView.setAdapter(mCustomSpinAdapter);
        }
        setupViewPager();

        if (mTimePeriodList.size() < 6) {
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
            mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        } else {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
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

        setupViewPager();

    }
    //---------------

    @Override
    public void onResume() {
        super.onResume();
        if (!mGeneratedRequestForYearList.contains(mCurrentSelectedTimePeriodTab.getYear())) {
            Map<String, Map<String, ArrayList<MyRecordInfoAndReports>>> yearWiseSortedMyRecordInfoAndReports = mMyRecordHelper.getYearWiseSortedMyRecordInfoAndReports(myRecordDataModel);
            if (yearWiseSortedMyRecordInfoAndReports.get(mCurrentSelectedTimePeriodTab.getYear()) == null) {
                mMyRecordHelper.doGetAllMyRecords();
//                getYearData(mCurrentSelectedTimePeriodTab.getYear());
                mGeneratedRequestForYearList.add(mCurrentSelectedTimePeriodTab.getYear());
            }
        }
    }

    public MyRecordsHelper getParentMyRecordHelper() {
        return mMyRecordHelper;
    }

}
