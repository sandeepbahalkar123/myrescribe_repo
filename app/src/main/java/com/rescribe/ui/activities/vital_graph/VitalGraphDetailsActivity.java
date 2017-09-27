package com.rescribe.ui.activities.vital_graph;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.helpers.vital_graph_helper.VitalGraphHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.vital_graph.vital_all_list.VitalGraphData;
import com.rescribe.model.vital_graph.vital_description.VitalGraphInfoBaseModel;
import com.rescribe.ui.customesViews.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VitalGraphDetailsActivity extends AppCompatActivity implements HelperResponse {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.weight)
    CustomTextView weightText;
    @BindView(R.id.date)
    TextView dateText;
    @BindView(R.id.description)
    CustomTextView mDescriptionText;
    private VitalGraphData mClickedVitalGraphData;
    private VitalGraphHelper mVitalGraphHelper;
    private VitalGraphInfoBaseModel.VitalGraphInfoDataModel mReceivedVitalGraphDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vital_graph_details);
        ButterKnife.bind(this);
        mClickedVitalGraphData = getIntent().getParcelableExtra(getString(R.string.vital_graph));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(mClickedVitalGraphData.getVitalName());
        toolbar.setTitle(mClickedVitalGraphData.getVitalName());
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
        mVitalGraphHelper.doGetPatientVitalDetail(mClickedVitalGraphData.getVitalName());

        weightText.setText(mClickedVitalGraphData.getVitalValue());
        dateText.setText(mClickedVitalGraphData.getVitalDate());
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

        VitalGraphInfoBaseModel customResponse1 = (VitalGraphInfoBaseModel) customResponse;

        if (customResponse1.getVitalGraphInfoDataModel() != null) {
            mReceivedVitalGraphDataModel = customResponse1.getVitalGraphInfoDataModel();
            mDescriptionText.setText("" + mReceivedVitalGraphDataModel.getDescription());
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
}
