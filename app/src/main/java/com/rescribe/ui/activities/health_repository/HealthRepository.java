package com.rescribe.ui.activities.health_repository;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.adapters.health_repository.HealthRepositoryAdapter;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.interfaces.dashboard_menu_click.IOnMenuClickListener;
import com.rescribe.model.dashboard_api.ClickEvent;
import com.rescribe.model.dashboard_api.ClickOption;
import com.rescribe.model.dashboard_api.DashboardMenuList;
import com.rescribe.ui.activities.MyRecordsActivity;
import com.rescribe.ui.activities.doctor.DoctorListActivity;
import com.rescribe.ui.activities.saved_articles.SavedArticles;
import com.rescribe.ui.activities.vital_graph.VitalGraphActivity;
import com.rescribe.util.CommonMethods;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 13/10/17.
 */

public class HealthRepository extends AppCompatActivity implements IOnMenuClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_group_photo)
    ImageView imgGroupPhoto;
    @BindView(R.id.healthRepositoryListView)
    RecyclerView healthRepositoryListView;
    private AppDBHelper appDBHelper;
    private Context mContext;
    private HealthRepositoryAdapter mHealthRepositoryAdapter;
    private DashboardMenuList mReceivedDashboardMenuListData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_respository_base_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mReceivedDashboardMenuListData = extras.getParcelable(getString(R.string.clicked_item_data));
            String value = extras.getString(getString(R.string.clicked_item_data_type_value));

            if (mReceivedDashboardMenuListData != null)
                getSupportActionBar().setTitle(mReceivedDashboardMenuListData.getName());
            else if (value != null)
                getSupportActionBar().setTitle(value);
        }

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
        //------Load background image : START------
        ClickEvent clickEvent1 = mReceivedDashboardMenuListData.getClickEvent();
        if (clickEvent1 != null) {
            if (clickEvent1.getBgImageUrl() != null) {
                int imageSizeToLoadImage = CommonMethods.getImageSizeToLoadImage(this, 2);

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.dontAnimate();
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                requestOptions.skipMemoryCache(true);
                    requestOptions.override(imageSizeToLoadImage, imageSizeToLoadImage);

                Glide.with(this)
                        .load(clickEvent1.getBgImageUrl())
                        .apply(requestOptions).thumbnail(0.5f)
                        .into(imgGroupPhoto);
            }
        }
        //------Load background image : END------

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        healthRepositoryListView.setLayoutManager(layoutManager);
        healthRepositoryListView.setItemAnimator(new DefaultItemAnimator());
//            int spanCount = 2; // 3 columns
//            int spacing = 20; // 50px
//            boolean includeEdge = true;
//            listView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        //-------------
        ClickEvent clickEvent = mReceivedDashboardMenuListData.getClickEvent();
        if (clickEvent != null) {
            mHealthRepositoryAdapter = new HealthRepositoryAdapter(mContext, clickEvent.getClickOptions(), this);
            healthRepositoryListView.setAdapter(mHealthRepositoryAdapter);
            healthRepositoryListView.setNestedScrollingEnabled(false);
        }
        //-------------
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onMenuClick(ClickOption data) {
        if (data.getName().equalsIgnoreCase(getString(R.string.vital_graph))) {
            Intent intent = new Intent(mContext, VitalGraphActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.clicked_item_data), data.getName());
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (data.getName().equalsIgnoreCase(getString(R.string.doctor_visit))) {
            Intent intent = new Intent(mContext, DoctorListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.clicked_item_data), data.getName());
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (data.getName().equalsIgnoreCase(getString(R.string.my_records))) {
            Intent intent = new Intent(mContext, MyRecordsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.clicked_item_data), data.getName());
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (data.getName().equalsIgnoreCase(getString(R.string.saved_articles))) {
            Intent intent = new Intent(mContext, SavedArticles.class);
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.clicked_item_data), data.getName());
            intent.putExtras(bundle);
            startActivity(intent);
        }
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

