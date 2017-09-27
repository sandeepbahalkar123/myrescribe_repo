package com.rescribe.ui.activities.vital_graph;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.vital_graph.VitalList;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.RescribeConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VitalGraphDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.weight)
    CustomTextView weightText;
    @BindView(R.id.date)
    TextView dateText;
    @BindView(R.id.description)
    CustomTextView descriptionText;
    private VitalList vitalGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vital_graph_details);
        ButterKnife.bind(this);
        vitalGraph = getIntent().getParcelableExtra(RescribeConstants.VITALS_LIST);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(vitalGraph.getVitalName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        init();
    }

    private void init() {
        weightText.setText(vitalGraph.getVitalWeight());
        dateText.setText(vitalGraph.getVitalDate());
    }

}
