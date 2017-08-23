package com.rescribe.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.rescribe.R;
import com.rescribe.adapters.myrecords.ShowRecordsAdapter;
import com.rescribe.util.RescribeConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowRecordsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private Context mContext;

    ShowRecordsAdapter showRecordsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_records);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.title_activity_reports));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String[] imageArrayExtra = getIntent().getStringArrayExtra(RescribeConstants.DOCUMENTS);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mContext = ShowRecordsActivity.this;
        if (imageArrayExtra != null) {
            if (imageArrayExtra.length != 0) {
                // off recyclerView Animation
                RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
                if (animator instanceof SimpleItemAnimator)
                    ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);

                showRecordsAdapter = new ShowRecordsAdapter(mContext, imageArrayExtra);
                recyclerView.setAdapter(showRecordsAdapter);
                GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
                recyclerView.setLayoutManager(layoutManager);
            }
        }
    }
}
