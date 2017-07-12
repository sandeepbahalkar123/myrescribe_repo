package com.myrescribe.ui.activities;

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
import com.myrescribe.helpers.one_day_visit.OneDayVisitHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.visit_details.Data;
import com.myrescribe.model.visit_details.Diagnosi;
import com.myrescribe.model.visit_details.PatientHistory;

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
    private HashMap<String, ArrayList<Diagnosi>> mHistoryDataList;
    private OneDayVisitHelper mOneDayVisitHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_details_activity);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        mOneDayVisitHelper = new OneDayVisitHelper(this, this);
        mOneDayVisitHelper.doGetOneDayVisit();
       /* mHistoryHelper = new HistoryHelper(this, this);
        mHistoryHelper.doGetHistory();*/
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.visit_details));
        mToolbar.setNavigationIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_white_24dp, null));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
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
        Data data = (Data) customResponse;
        formatResponseDataForAdapter(data.getPatientHistory());
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
    private void formatResponseDataForAdapter(List<PatientHistory> dataList) {
        mHeaderList = new ArrayList<>();
        mHistoryDataList = new HashMap<>();


        for (PatientHistory listObject :
                dataList) {
            if (listObject.getComplaints() != null) {
               /* if (listObject.getComplaints().size() != 0) {*/
                    mHeaderList.add("Complaints");
                ArrayList<Diagnosi> diagnosis = new ArrayList<>();
                Diagnosi mDiagnosiList = new Diagnosi();
                mDiagnosiList.setName("Medicine not available");
                diagnosis.add(mDiagnosiList);
                mHistoryDataList.put("Complaints", diagnosis);

            } else if (listObject.getDiagnosis() != null) {

                    mHeaderList.add("Diagnosis");
                    mHistoryDataList.put("Diagnosis", listObject.getDiagnosis());

            } else if (listObject.getPrescriptions() != null) {

                    mHeaderList.add("Prescription");
                    mHistoryDataList.put("Prescription", listObject.getPrescriptions());

            } else if (listObject.getInvestigations() != null) {

                    mHeaderList.add("Investigations");
                ArrayList<Diagnosi> diagnosis = new ArrayList<>();
                Diagnosi mDiagnosiList = new Diagnosi();
                mDiagnosiList.setName("Need to investigate");
                diagnosis.add(mDiagnosiList);
                mHistoryDataList.put("Investigations", diagnosis);

            }else if (listObject.getVitals() != null) {
                mHeaderList.add("Vitals");
                ArrayList<Diagnosi> diagnosis = new ArrayList<>();
                Diagnosi mDiagnosiList = new Diagnosi();
                mDiagnosiList.setName("vitals");
                diagnosis.add(mDiagnosiList);
                mHistoryDataList.put("Vitals", diagnosis);

            }
            else if (listObject.getRemarks() != null) {
                    mHeaderList.add("Remarks");
                ArrayList<Diagnosi> diagnosis = new ArrayList<>();
                Diagnosi mDiagnosiList = new Diagnosi();
                mDiagnosiList.setName("Please drink water");
                diagnosis.add(mDiagnosiList);
                    mHistoryDataList.put("Remarks", diagnosis);
            }
           else if (listObject.getAdvice() != null) {
                if (!listObject.getAdvice().equals(null)) {
                    mHeaderList.add("Advice");
                    ArrayList<Diagnosi> diagnosis = new ArrayList<>();
                    Diagnosi mDiagnosiList = new Diagnosi();
                    mDiagnosiList.setName(listObject.getAdvice());
                    diagnosis.add(mDiagnosiList);
                 //  diagnosis.add(1,listObject.getAdvice());  listObject.getAdvice();
                    mHistoryDataList.put("Advice", diagnosis);
                }
            }

        }
    }

}

