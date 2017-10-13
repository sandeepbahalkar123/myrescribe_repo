package com.rescribe.ui.activities.find_doctors;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.rescribe.R;
import com.rescribe.ui.activities.DoctorConnectActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 13/10/17.
 */

public class FindDoctorsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bookAppointmentLayout)
    LinearLayout bookAppointment;
    @BindView(R.id.consultOnline)
    LinearLayout consultOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_doctors_base_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.find_doctors));
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

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @OnClick({R.id.bookAppointmentLayout, R.id.consultOnline})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookAppointmentLayout:

                break;
            case R.id.consultOnline:
                Intent intent = new Intent(FindDoctorsActivity.this, DoctorConnectActivity.class);
                startActivity(intent);
                break;
        }
    }
}
