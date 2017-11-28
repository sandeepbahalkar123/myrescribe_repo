package com.rescribe.ui.activities.health_repository;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.rescribe.R;
import com.rescribe.adapters.find_doctors.FindDoctorsAdapter;
import com.rescribe.adapters.health_offers.HealthOffersAdapter;
import com.rescribe.adapters.health_repository.HealthRepositoryAdapter;
import com.rescribe.helpers.database.AppDBHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 13/10/17.
 */

public class HealthRepository extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.healthRepositoryListView)
    RecyclerView healthRepositoryListView;
    /*  @BindView(R.id.myRecordsLayout)
      LinearLayout myRecordsLayout;
      @BindView(R.id.vitalGraphsLayout)
      LinearLayout vitalGraphsLayout;
      @BindView(R.id.doctorVisitsLayout)
      LinearLayout doctorVisitsLayout;
      @BindView(R.id.savedArticlesLayout)
      LinearLayout savedArticlesLayout;*/
    private AppDBHelper appDBHelper;
    private Context mContext;
    private HealthRepositoryAdapter mHealthRepositoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_respository_base_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra(getString(R.string.toolbarTitle)));
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
        mContext = HealthRepository.this;
        appDBHelper = new AppDBHelper(HealthRepository.this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        healthRepositoryListView.setLayoutManager(layoutManager);
        healthRepositoryListView.setItemAnimator(new DefaultItemAnimator());
//            int spanCount = 2; // 3 columns
//            int spacing = 20; // 50px
//            boolean includeEdge = true;
//            listView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        mHealthRepositoryAdapter = new HealthRepositoryAdapter(mContext);
        healthRepositoryListView.setAdapter(mHealthRepositoryAdapter);
        healthRepositoryListView.setNestedScrollingEnabled(false);


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*@OnClick({R.id.myRecordsLayout, R.id.vitalGraphsLayout, R.id.doctorVisitsLayout, R.id.savedArticlesLayout})
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
    }*/
}

