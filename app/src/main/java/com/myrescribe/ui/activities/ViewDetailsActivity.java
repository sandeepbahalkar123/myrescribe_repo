package com.myrescribe.ui.activities;

import android.content.Intent;
import android.nfc.Tag;
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
import com.myrescribe.helpers.one_day_visit.VitalHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.visit_details.Data;
import com.myrescribe.model.visit_details.Diagnosi;
import com.myrescribe.model.visit_details.PatientHistory;
import com.myrescribe.model.visit_details.Vital;
import com.myrescribe.ui.customesViews.CustomTextView;
import com.myrescribe.util.CommonMethods;

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

    @BindView(R.id.doctorSpecialization)
    CustomTextView mDoctorSpecialization;

    @BindView(R.id.doctorName)
    CustomTextView mDoctorName;

    @BindView(R.id.doctor_address)
    CustomTextView mDoctor_address;
    @BindView(R.id.dateTextView)
    CustomTextView mDateTextView;


    private int lastExpandedPosition = -1;
    private ArrayList<String> mHeaderList;
    Intent intent;
    private HashMap<String, ArrayList<Diagnosi>> mHistoryDataList;
    private OneDayVisitHelper mOneDayVisitHelper;
    private List<Vital> mVitalList;
    private VitalHelper mVitalHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_details_activity);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        intent = getIntent();
        if (intent != null) {
            mDoctorName.setText(intent.getStringExtra(getString(R.string.name)));
            mDoctorSpecialization.setText(intent.getStringExtra(getString(R.string.specialization)));
            mDoctor_address.setText(intent.getStringExtra(getString(R.string.address)));
            mDateTextView.setText(intent.getStringExtra(getString(R.string.one_day_visit_date)));

        }else{
            mDoctorName.setText("Ritesh Deshmukh ");
            mDoctorSpecialization.setText("Cardiologist");
            mDoctor_address.setText("Aundh, Pune");
            mDateTextView.setText("17th Jul 2017");
        }


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
        mVitalHelper = new VitalHelper(this,this);
        mVitalList = mVitalHelper.doGetVitalsList();
        Data data = (Data) customResponse;
        formatResponseDataForAdapter(data.getPatientHistory());
        ShowViewDetailsAdapter showHistoryListAdapter = new ShowViewDetailsAdapter(this, mHeaderList, mHistoryDataList,mVitalList);
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
                mHeaderList.add(getString(R.string.compalints));
                ArrayList<Diagnosi> diagnosis = new ArrayList<>();
                Diagnosi mDiagnosiList = new Diagnosi();
                mDiagnosiList.setName("Medicine not available");
                diagnosis.add(mDiagnosiList);
                mHistoryDataList.put(getString(R.string.compalints), diagnosis);

            } else if (listObject.getDiagnosis() != null) {

                mHeaderList.add(getString(R.string.diagnosis));
                mHistoryDataList.put(getString(R.string.diagnosis), listObject.getDiagnosis());

            } else if (listObject.getPrescriptions() != null) {

                mHeaderList.add(getString(R.string.prescription));
                mHistoryDataList.put(getString(R.string.prescription), listObject.getPrescriptions());

            } else if (listObject.getInvestigations() != null) {

                mHeaderList.add(getString(R.string.investigations));
                ArrayList<Diagnosi> diagnosis = new ArrayList<>();
                Diagnosi mDiagnosiList = new Diagnosi();
                mDiagnosiList.setName("Need to investigate");
                diagnosis.add(mDiagnosiList);
                mHistoryDataList.put(getString(R.string.investigations), diagnosis);

            } else if (listObject.getVitals() != null) {
                mHeaderList.add(getString(R.string.vitals));
                ArrayList<Diagnosi> diagnosis = new ArrayList<>();
                Diagnosi mDiagnosiList = new Diagnosi();
                mDiagnosiList.setName("BP-130/86 mm of hg");
                diagnosis.add(mDiagnosiList);
                mHistoryDataList.put(getString(R.string.vitals), diagnosis);

            } else if (listObject.getRemarks() != null) {
                mHeaderList.add(getString(R.string.remarks));
                ArrayList<Diagnosi> diagnosis = new ArrayList<>();
                Diagnosi mDiagnosiList = new Diagnosi();
                mDiagnosiList.setName("Please drink water");
                diagnosis.add(mDiagnosiList);
                mHistoryDataList.put(getString(R.string.remarks), diagnosis);
            } else if (listObject.getAdvice() != null) {
                if (!listObject.getAdvice().equals(null)) {
                    mHeaderList.add(getString(R.string.advice));
                    mHistoryDataList.put(getString(R.string.advice), listObject.getAdvice());
                }
            }

        }
    }

}

