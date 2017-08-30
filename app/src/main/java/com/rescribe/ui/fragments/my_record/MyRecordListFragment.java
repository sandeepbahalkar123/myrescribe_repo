package com.rescribe.ui.fragments.my_record;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import com.rescribe.R;
import com.rescribe.adapters.myrecords.ThreeLevelListAdapter;
import com.rescribe.helpers.myrecords.MyRecordsHelper;
import com.rescribe.model.login.Year;
import com.rescribe.model.my_records.MyRecordDataModel;
import com.rescribe.model.my_records.MyRecordInfoAndReports;
import com.rescribe.ui.activities.MyRecordsActivity;
import com.rescribe.util.RescribeConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyRecordListFragment extends Fragment {

    private MyRecordsHelper mMyRecordHelper;
    @BindView(R.id.expandMyRecordListView)
    ExpandableListView mExpandMyRecordListView;
    @BindView(R.id.emptyListView)
    RelativeLayout mEmptyListView;

    private Context mContext;
    private MyRecordsActivity mParentActivity;
    private String mMonthName;
    private String mYear;
    private ThreeLevelListAdapter mAdapter;
    private String mInvestigationText;

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
            mMonthName = arguments.getString(RescribeConstants.MONTH);
            mYear = arguments.getString(RescribeConstants.YEAR);
        }
        mInvestigationText = getString(R.string.investigation);

        return mRootView;
    }

    public static MyRecordListFragment createNewFragment(Year dataString) {
        MyRecordListFragment fragment = new MyRecordListFragment();
        Bundle args = new Bundle();
        args.putString(RescribeConstants.MONTH, dataString.getMonthName());
        args.putString(RescribeConstants.YEAR, dataString.getYear());
        fragment.setArguments(args);
        return fragment;
    }

    private void setListAdapter() {
        MyRecordListFragmentContainer parentFragment = (MyRecordListFragmentContainer) this.getParentFragment();
        MyRecordsHelper parentMyRecordHelper = parentFragment.getParentMyRecordHelper();
        if (parentMyRecordHelper != null) {

            Map<String, Map<String, ArrayList<MyRecordInfoAndReports>>> yearWiseSortedMyRecordInfoAndReports = parentMyRecordHelper.getYearWiseSortedMyRecordInfoAndReports();
            if (yearWiseSortedMyRecordInfoAndReports.size() != 0) {
                Map<String, ArrayList<MyRecordInfoAndReports>> monthArrayListHashMap = yearWiseSortedMyRecordInfoAndReports.get(mYear);
                if (monthArrayListHashMap != null) {
                    ArrayList<MyRecordInfoAndReports> formattedDoctorList = monthArrayListHashMap.get(mMonthName);
                    if (formattedDoctorList != null) {
                        if (formattedDoctorList.size() == 0) {
                            mExpandMyRecordListView.setVisibility(View.GONE);
                            mEmptyListView.setVisibility(View.VISIBLE);
                        } else {
                            mEmptyListView.setVisibility(View.GONE);
                            mExpandMyRecordListView.setVisibility(View.VISIBLE);
                            mAdapter = new ThreeLevelListAdapter(mContext, formattedDoctorList);
                            mExpandMyRecordListView.setAdapter(mAdapter);
                        }
                    } else {
                        mExpandMyRecordListView.setVisibility(View.GONE);
                        mEmptyListView.setVisibility(View.VISIBLE);
                    }
                } else {
                    mExpandMyRecordListView.setVisibility(View.GONE);
                    mEmptyListView.setVisibility(View.VISIBLE);
                }
            } else {
                mExpandMyRecordListView.setVisibility(View.GONE);
                mEmptyListView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setListAdapter();
    }
}