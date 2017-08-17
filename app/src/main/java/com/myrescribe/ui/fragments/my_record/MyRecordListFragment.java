package com.myrescribe.ui.fragments.my_record;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.myrescribe.R;
import com.myrescribe.adapters.my_records.ThreeLevelListAdapter;
import com.myrescribe.helpers.my_record.MyRecordHelper;
import com.myrescribe.model.login.Year;
import com.myrescribe.model.my_records.MyRecordInfoAndReports;
import com.myrescribe.ui.activities.MyRecordsActivity;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyRecordListFragment extends Fragment {

    private MyRecordHelper mMyRecordHelper;
    @BindView(R.id.expandMyRecordListView)
    ExpandableListView mExpandMyRecordListView;
    @BindView(R.id.emptyListView)
    RelativeLayout mEmptyListView;

    private Context mContext;
    private MyRecordsActivity mParentActivity;
    private String mMonthName;
    private String mYear;

    public MyRecordListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mRootView = inflater.inflate(R.layout.my_record_expandable_listiew, container, false);
        ButterKnife.bind(this, mRootView);
        mContext = inflater.getContext();
        Bundle arguments = getArguments();
        if (arguments != null) {
            mMonthName = arguments.getString(MyRescribeConstants.MONTH);
            mYear = arguments.getString(MyRescribeConstants.YEAR);
        }

        return mRootView;
    }

    public static MyRecordListFragment createNewFragment(Year dataString) {
        MyRecordListFragment fragment = new MyRecordListFragment();
        Bundle args = new Bundle();
        args.putString(MyRescribeConstants.MONTH, dataString.getMonthName());
        args.putString(MyRescribeConstants.YEAR, dataString.getYear());
        fragment.setArguments(args);
        return fragment;
    }

    private void setListAdapter() {
        MyRecordListFragmentContainer parentFragment = (MyRecordListFragmentContainer) this.getParentFragment();
        MyRecordHelper parentMyRecordHelper = parentFragment.getParentMyRecordHelper();
        if (parentMyRecordHelper != null) {

            Map<String, Map<String, ArrayList<MyRecordInfoAndReports>>> yearWiseSortedMyRecordInfoAndReports = parentMyRecordHelper.getYearWiseSortedMyRecordInfoAndReports();
            if (yearWiseSortedMyRecordInfoAndReports.size() != 0) {
                Map<String, ArrayList<MyRecordInfoAndReports>> monthArrayListHashMap = yearWiseSortedMyRecordInfoAndReports.get(mYear);
                if (monthArrayListHashMap != null) {
                    // ArrayList<MyRecordInfoAndReports> formattedDoctorList = parentMyRecordHelper.getFormattedMyRecords(mMonthName, monthArrayListHashMap);
                    ArrayList<MyRecordInfoAndReports> formattedDoctorList = monthArrayListHashMap.get(mMonthName);
                    if (formattedDoctorList != null) {
                        if (formattedDoctorList.size() == 0) {
                            mExpandMyRecordListView.setVisibility(View.GONE);
                            mEmptyListView.setVisibility(View.VISIBLE);
                        } else {
                            mEmptyListView.setVisibility(View.GONE);
                            mExpandMyRecordListView.setVisibility(View.VISIBLE);
                            ThreeLevelListAdapter adapter = new ThreeLevelListAdapter(mContext, formattedDoctorList);
                            mExpandMyRecordListView.setAdapter(adapter);
                        }
                    } else {
                        mExpandMyRecordListView.setVisibility(View.GONE);
                        mEmptyListView.setVisibility(View.VISIBLE);
                    }

                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setListAdapter();
    }
}