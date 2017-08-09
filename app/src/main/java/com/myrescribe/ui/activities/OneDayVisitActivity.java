package com.myrescribe.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.myrescribe.R;

import android.widget.ExpandableListView;

import com.myrescribe.adapters.OneDayVisitAdapter;
import com.myrescribe.helpers.one_day_visit.OneDayVisitHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.case_details.Data;
import com.myrescribe.model.case_details.PatientHistory;
import com.myrescribe.model.case_details.Range;
import com.myrescribe.model.case_details.Vital;
import com.myrescribe.ui.customesViews.CustomTextView;
import com.myrescribe.util.CommonMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 14/6/17.
 */


public class OneDayVisitActivity extends AppCompatActivity implements HelperResponse {

    @BindView(R.id.historyExpandableListView)
    ExpandableListView mHistoryExpandableListView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.doctorSpecialization)
    CustomTextView mDoctorSpecialization;
    @BindView(R.id.doctorName)
    CustomTextView mDoctorName;
    @BindView(R.id.doctor_address)
    CustomTextView mDoctor_address;
    @BindView(R.id.dateTextView)
    CustomTextView mDateTextView;
    private int lastExpandedPosition = -1;
    Intent intent;
    private String TAG = getClass().getName();
    private OneDayVisitHelper mOneDayVisitHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_details_activity);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        intent = getIntent();
        if (getIntent().getExtras() != null) {
            mDoctorName.setText(intent.getStringExtra(getString(R.string.name)));
            mDoctorSpecialization.setText(intent.getStringExtra(getString(R.string.specialization)));
            mDoctor_address.setText(intent.getStringExtra(getString(R.string.address)));
            String stringExtra = intent.getStringExtra(getString(R.string.one_day_visit_date));

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mDateTextView.setText(Html.fromHtml(stringExtra, Html.FROM_HTML_MODE_LEGACY));
            } else {
                mDateTextView.setText(Html.fromHtml(stringExtra));
            }
        }


        mOneDayVisitHelper = new OneDayVisitHelper(this, this);
        mOneDayVisitHelper.doGetOneDayVisit(intent.getStringExtra(getString(R.string.opd_id)));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.visit_details));
        mToolbar.setNavigationIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_white_24dp, null));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        mHistoryExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    mHistoryExpandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
        mHistoryExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                mHistoryExpandableListView.collapseGroup(groupPosition);

                return false;
            }
        });

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        String bpMin = "";
        Data data = (Data) customResponse;
        List<PatientHistory> patientHistoryList = data.getPatientHistory();
        List<Vital> vitalSortedList = new ArrayList<>();

        for (int i = 0; i < patientHistoryList.size(); i++) {
            if (patientHistoryList.get(i).getVitals() != null) {
                String pos = null;

                List<Vital> vitalList = patientHistoryList.get(i).getVitals();
                for (int j = 0; j < vitalList.size(); j++) {
                    Vital dataObject = vitalList.get(j);
                    if (dataObject.getUnitName().equalsIgnoreCase(getString(R.string.bp_max)) || dataObject.getUnitName().equalsIgnoreCase(getString(R.string.bp_min))) {
                        Vital vital = new Vital();
                        if (pos == null) {
                            vital.setUnitName(getString(R.string.bp));
                            vital.setUnitValue(dataObject.getUnitValue());
                            vital.setCategory(dataObject.getCategory());
                            for(int k = 0;k<dataObject.getRanges().size();k++){
                                dataObject.getRanges().get(k).setNameOfVital(getString(R.string.bp_max));
                            }
                            vital.setRanges(dataObject.getRanges());
                            vital.setDisplayName(dataObject.getDisplayName());
                            vitalSortedList.add(vital);
                            pos = String.valueOf(j);
                        } else {
                            Vital previousData = vitalSortedList.get(Integer.parseInt(pos));
                            String unitValue = previousData.getUnitValue();
                            String unitCategory = previousData.getCategory();
                            unitCategory = unitCategory + getString(R.string.colon_sign) + dataObject.getCategory();
                            unitValue = unitValue + "/" + dataObject.getUnitValue();
                            previousData.setUnitName(getString(R.string.bp));
                            previousData.setUnitValue(unitValue);
                            previousData.setCategory(unitCategory);
                            List<Range> ranges = previousData.getRanges();
                            ranges.addAll(dataObject.getRanges());
                            vitalSortedList.set(Integer.parseInt(pos), previousData);
                        }
                    } else {
                        Vital vital = new Vital();
                        vital.setUnitName(vitalList.get(j).getUnitName());
                        vital.setUnitValue(vitalList.get(j).getUnitValue());
                        vital.setCategory(vitalList.get(j).getCategory());
                        vital.setRanges(vitalList.get(j).getRanges());
                        vital.setDisplayName(vitalList.get(j).getDisplayName());
                        vitalSortedList.add(vital);
                    }
                }
                patientHistoryList.get(i).setVitals(vitalSortedList);
            }
        }
        CommonMethods.Log(TAG, patientHistoryList.toString());
        OneDayVisitAdapter oneDayVisitAdapter = new OneDayVisitAdapter(this,patientHistoryList);
        mHistoryExpandableListView.setAdapter(oneDayVisitAdapter);


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

