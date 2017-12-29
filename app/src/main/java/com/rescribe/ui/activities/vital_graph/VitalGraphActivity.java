package com.rescribe.ui.activities.vital_graph;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.vital_graph.VitalGraphAdapter;
import com.rescribe.helpers.vital_graph_helper.VitalGraphHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.vital_graph.vital_all_list.VitalGraphBaseModel;
import com.rescribe.model.vital_graph.vital_all_list.VitalGraphData;
import com.rescribe.model.vital_graph.vital_all_list.VitalGraphList;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VitalGraphActivity extends AppCompatActivity implements VitalGraphAdapter.ItemClickListener, HelperResponse {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.listView)
    RecyclerView mRecyclerView;
    @BindView(R.id.emptyListView)
    RelativeLayout mEmptyListView;
    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private VitalGraphHelper mVitalGraphHelper;
    private VitalGraphBaseModel mReceivedVitalGraphBaseModel;
    private ArrayList<VitalGraphData> mReceivedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vital_graph);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText(getString(R.string.vital_graph));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        init();
    }

    private void init() {

        mVitalGraphHelper = new VitalGraphHelper(this, this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        mVitalGraphHelper.doGetPatientVitalList();
    }

    @Override
    public void onVitalClick(VitalGraphData vitalList) {
        Intent intent = new Intent(VitalGraphActivity.this, VitalGraphDetailsActivity.class);
        intent.putExtra(getString(R.string.vital_graph), vitalList);
        startActivity(intent);
    }

    @Override
    public void onAddTrackerClick() {
      /*  Intent intent = new Intent(VitalGraphActivity.this, AddTrackerActivity.class);
        startActivity(intent);*/
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

        switch (mOldDataTag) {
            case RescribeConstants.TASK_GET_PATIENT_VITAL_LIST:
                mReceivedVitalGraphBaseModel = (VitalGraphBaseModel) customResponse;
                setDoctorListAdapter(mReceivedVitalGraphBaseModel);
                break;
        }

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

    public void isDataListViewVisible(boolean flag) {
        if (flag) {
            mEmptyListView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mEmptyListView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }


    private void setDoctorListAdapter(VitalGraphBaseModel receivedBookAppointmentBaseModel) {
        if (receivedBookAppointmentBaseModel == null) {
            isDataListViewVisible(false);
        } else {
            VitalGraphList data = receivedBookAppointmentBaseModel.getData();
            if (data == null) {
                isDataListViewVisible(false);
            } else {
                ArrayList<VitalGraphData> graphList = data.getVitalList();
                if (graphList.size() == 0) {
                    isDataListViewVisible(false);
                } else {
                    isDataListViewVisible(true);
                    mReceivedList = graphList;
                    VitalGraphAdapter vitalGraphAdapter = new VitalGraphAdapter(this, mReceivedList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(VitalGraphActivity.this);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                            DividerItemDecoration.VERTICAL);
                    mRecyclerView.addItemDecoration(dividerItemDecoration);
                    mRecyclerView.setAdapter(vitalGraphAdapter);
                }
            }
        }
    }

    @OnClick({R.id.fab, R.id.emptyListView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab:
                Intent intent = new Intent(VitalGraphActivity.this, AddTrackerActivity.class);
                startActivity(intent);
                break;
            case R.id.emptyListView:
                break;
        }
    }
}
