package com.myrescribe.ui.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.myrescribe.R;
import com.myrescribe.adapters.ShowHistoryListAdapter;
import com.myrescribe.helpers.history.HistoryHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.history.HistoryCommonDetails;
import com.myrescribe.model.history.PatientHistoryData;
import com.myrescribe.model.history.PatientHistoryMain;
import com.myrescribe.model.history.PatientHistoryModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 14/6/17.
 */

public class HistoryActivity extends AppCompatActivity implements HelperResponse {

    @BindView(R.id.historyExpandableListView)
    ExpandableListView mHistoryExpandableListView;

    private LinearLayout mToolbar;

    private String[] strings = {"All", "Complaints", "Findings", "Vitals", "Diagnosis", "Prescription"};
    private HistoryHelper mHistoryHelper;
    private ArrayList<String> mHeaderList;
    private HashMap<String, ArrayList<HistoryCommonDetails>> mHistoryDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);
        ButterKnife.bind(this);

        mToolbar = (LinearLayout) findViewById(R.id.toolbar);
        initialize();
    }

    private void initialize() {
        mHistoryHelper = new HistoryHelper(this, this);
        mHistoryHelper.doGetHistory();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        PatientHistoryData data = (PatientHistoryData) customResponse;
        formatResponseDataForAdapter(data.getPatientHistoryList());
        ShowHistoryListAdapter showHistoryListAdapter = new ShowHistoryListAdapter(this, mHeaderList, mHistoryDataList);
        mHistoryExpandableListView.setAdapter(showHistoryListAdapter);
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

    //TODO : Here main lengthy parsing begins to format list properly.
    private void formatResponseDataForAdapter(List<PatientHistoryMain> dataList) {
        mHeaderList = new ArrayList<>();
        mHistoryDataList = new HashMap<>();
        for (PatientHistoryMain listObject :
                dataList) {
            if (listObject.getComplaints() != null) {
                if (listObject.getComplaints().size() != 0) {
                    mHeaderList.add("Complaints");
                    mHistoryDataList.put("Complaints", listObject.getComplaints());
                }
            } else if (listObject.getDiagnosis() != null) {
                if (listObject.getDiagnosis().size() != 0) {
                    mHeaderList.add("Diagnosis");
                    mHistoryDataList.put("Diagnosis", listObject.getDiagnosis());
                }
            } else if (listObject.getPrescriptions() != null) {
                if (listObject.getPrescriptions().size() != 0) {
                    mHeaderList.add("Prescriptions");
                    mHistoryDataList.put("Prescriptions", listObject.getPrescriptions());
                }
            } else if (listObject.getInvestigations() != null) {
                if (listObject.getInvestigations().size() != 0) {
                    mHeaderList.add("Investigations");
                    mHistoryDataList.put("Investigations", listObject.getInvestigations());
                }
            }

        }
    }

}
