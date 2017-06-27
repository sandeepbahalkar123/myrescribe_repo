package com.myrescribe.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.myrescribe.R;
import java.util.ArrayList;
import java.util.List;

import android.widget.ExpandableListView;

import com.myrescribe.adapters.ShowViewDetailsAdapter;
import com.myrescribe.helpers.history.HistoryHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.history.HistoryCommonDetails;
import com.myrescribe.model.history.PatientHistoryData;
import com.myrescribe.model.history.PatientHistoryMain;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 14/6/17.
 */


public class ViewDetailsActivity extends AppCompatActivity implements HelperResponse {

    @BindView(R.id.historyExpandableListView)
    ExpandableListView mHistoryExpandableListView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private int lastExpandedPosition = -1;
    private int lastChildExpandedPosition = -1;
    private HistoryHelper mHistoryHelper;
    private ArrayList<String> mHeaderList;
    private HashMap<String, ArrayList<HistoryCommonDetails>> mHistoryDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_details_activity);
        ButterKnife.bind(this);

        initialize();
    }

    private void initialize() {
        mHistoryHelper = new HistoryHelper(this, this);
        mHistoryHelper.doGetHistory();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.visit_details));
        mToolbar.setNavigationIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_white_24dp, null));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                Intent intent = new Intent(ViewDetailsActivity.this, ShowMedicineDoseListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
        PatientHistoryData data = (PatientHistoryData) customResponse;
        formatResponseDataForAdapter(data.getPatientHistoryList());
        ShowViewDetailsAdapter showHistoryListAdapter = new ShowViewDetailsAdapter(this, mHeaderList, mHistoryDataList);
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
                    mHeaderList.add("Prescription");
                    mHistoryDataList.put("Prescription", listObject.getPrescriptions());
                }
            } else if (listObject.getInvestigations() != null) {
                if (listObject.getInvestigations().size() != 0) {
                    mHeaderList.add("Investigations");
                    mHistoryDataList.put("Investigations", listObject.getInvestigations());
                }
            }else if (listObject.getVitals() != null) {
                if (listObject.getVitals().size() != 0) {
                    mHeaderList.add("Vitals");
                    mHistoryDataList.put("Vitals", listObject.getVitals());
                }
            }
            else if (listObject.getRemarks() != null) {
                if (listObject.getRemarks().size() != 0) {
                    mHeaderList.add("Remarks");
                    mHistoryDataList.put("Remarks", listObject.getRemarks());
                }
            }
           else if (listObject.getAdvice() != null) {
                if (!listObject.getAdvice().equals(null)) {
                    mHeaderList.add("Advice");
                    mHistoryDataList.put("Advice", listObject.getAdvice());
                }
            }

        }
    }

}

