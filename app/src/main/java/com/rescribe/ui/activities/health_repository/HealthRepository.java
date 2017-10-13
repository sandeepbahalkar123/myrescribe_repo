package com.rescribe.ui.activities.health_repository;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.rescribe.R;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.database.MyRecordsData;
import com.rescribe.model.investigation.Image;
import com.rescribe.ui.activities.MyRecordsActivity;
import com.rescribe.ui.activities.SelectedRecordsGroupActivity;
import com.rescribe.ui.activities.doctor.DoctorListActivity;
import com.rescribe.ui.activities.vital_graph.VitalGraphActivity;
import com.rescribe.util.RescribeConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 13/10/17.
 */

public class HealthRepository extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.myRecordsLayout)
    LinearLayout myRecordsLayout;
    @BindView(R.id.vitalGraphsLayout)
    LinearLayout vitalGraphsLayout;
    @BindView(R.id.doctorVisitsLayout)
    LinearLayout doctorVisitsLayout;
    @BindView(R.id.savedArticlesLayout)
    LinearLayout savedArticlesLayout;
    private AppDBHelper appDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_respository_base_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.health_repository));
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        initialize();
    }

    private void initialize() {
        appDBHelper = new AppDBHelper(HealthRepository.this);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick({R.id.myRecordsLayout, R.id.vitalGraphsLayout, R.id.doctorVisitsLayout, R.id.savedArticlesLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.myRecordsLayout:
                MyRecordsData myRecordsData = appDBHelper.getMyRecordsData();
                int completeCount = 0;
                for (Image image : myRecordsData.getImageArrayList()) {
                    if (image.isUploading() == RescribeConstants.COMPLETED)
                        completeCount++;
                }
                Intent intent;
                if (completeCount == myRecordsData.getImageArrayList().size()) {
                    appDBHelper.deleteMyRecords();
                    intent = new Intent(HealthRepository.this, MyRecordsActivity.class);
                } else {
                    intent = new Intent(HealthRepository.this, SelectedRecordsGroupActivity.class);
                    intent.putExtra(RescribeConstants.UPLOADING_STATUS, true);
                    intent.putExtra(RescribeConstants.VISIT_DATE, myRecordsData.getVisitDate());
                    intent.putExtra(RescribeConstants.OPD_ID, myRecordsData.getDocId());
                    intent.putExtra(RescribeConstants.DOCTORS_ID, myRecordsData.getDocId());
                    intent.putExtra(RescribeConstants.DOCUMENTS, myRecordsData.getImageArrayList());
                }
                startActivity(intent);
                break;
            case R.id.vitalGraphsLayout:
                Intent intentVital = new Intent(HealthRepository.this, VitalGraphActivity.class);
                startActivity(intentVital);
                break;
            case R.id.doctorVisitsLayout:
                Intent intentDoctorVisit = new Intent(HealthRepository.this, DoctorListActivity.class);
                startActivity(intentDoctorVisit);
                break;
            case R.id.savedArticlesLayout:
                break;
        }
    }
}

