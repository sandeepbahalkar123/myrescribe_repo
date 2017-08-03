package com.myrescribe.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.myrescribe.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 31/7/17.
 */

public class MyRecordsActivity extends AppCompatActivity {
    @BindView(R.id.recordsToolbar)
    Toolbar mToolbar;
    @BindView(R.id.addRecord)
    Button mAddRecord;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        mContext = MyRecordsActivity.this;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.records));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @OnClick(R.id.addRecord)
    public void onViewClicked() {
        Intent intent = new Intent(MyRecordsActivity.this, AddRecordsActivity.class);
        startActivity(intent);
    }
}
