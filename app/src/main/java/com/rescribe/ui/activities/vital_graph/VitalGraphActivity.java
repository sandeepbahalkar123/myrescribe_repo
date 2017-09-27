package com.rescribe.ui.activities.vital_graph;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.adapters.vital_graph.VitalGraphAdapter;
import com.rescribe.model.vital_graph.VitalGraphModel;
import com.rescribe.model.vital_graph.VitalList;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VitalGraphActivity extends AppCompatActivity implements VitalGraphAdapter.ItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.listView)
    RecyclerView mRecyclerView;

    private ArrayList<VitalList> vitalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vital_graph);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        init();
    }

    private void init() {
        String data = "{ \"common\": { \"success\": true, \"statusCode\": 200, \"statusMessage\": \"USER AUTHENTICATED\" }, \"data\": { \"vitalList\": [ { \"vitalId\": 1, \"vitalName\": \"Blood Pressure\", \"vitalWeight\": \"130/88 mm Hg\", \"vitalDate\": \"1ˢᵗ Aug’17 \" }, { \"vitalId\": 2, \"vitalName\": \"Total Cholesterol\", \"vitalWeight\": \"200 mg/dl\", \"vitalDate\": \"2ⁿᵈ Aug’17 \" }, { \"vitalId\": 3, \"vitalName\": \"Weight\", \"vitalWeight\": \"68 Kgs\", \"vitalDate\": \"3ʳᵈ Aug’17 \" }, { \"vitalId\": 4, \"vitalName\": \"Temperature\", \"vitalWeight\": \"103 F\", \"vitalDate\": \"4ᵗʰ Aug’17 \" } ] } }";
        Gson gson = new Gson();
        VitalGraphModel vitalGraphModel = gson.fromJson(data, VitalGraphModel.class);
        vitalList.addAll(vitalGraphModel.getData().getVitalList());

        VitalGraphAdapter vitalGraphAdapter = new VitalGraphAdapter(VitalGraphActivity.this, vitalList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(VitalGraphActivity.this);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(vitalGraphAdapter);
    }

    @Override
    public void onVitalClick(VitalList vitalList) {
        Intent intent = new Intent(VitalGraphActivity.this, VitalGraphDetailsActivity.class);
        intent.putExtra(RescribeConstants.VITALS_LIST, vitalList);
        startActivity(intent);
    }

    @Override
    public void onAddTrackerClick() {
        Intent intent = new Intent(VitalGraphActivity.this, AddTrackerActivity.class);
        startActivity(intent);
    }
}
