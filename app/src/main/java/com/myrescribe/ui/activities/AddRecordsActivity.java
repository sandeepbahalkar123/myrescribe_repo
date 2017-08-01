package com.myrescribe.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.adapters.DoctorSpinnerAdapter;
import com.myrescribe.model.doctors.doctor_info.DoctorDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 31/7/17.
 */

public class AddRecordsActivity extends AppCompatActivity {
    @BindView(R.id.addRecordsToolbar)
    Toolbar mToolbar;
    @BindView(R.id.selectDoctorName)
    AutoCompleteTextView mSelectDoctorName;
    @BindView(R.id.clearButton)
    ImageView clearButton;
    @BindView(R.id.selectDate)
    TextView selectDate;
    private Context mContext;
    ArrayList<DoctorDetail> mList;
    DoctorSpinnerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_records);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        mContext = AddRecordsActivity.this;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.addrecords));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mList = retrievePeople();
        mSelectDoctorName.setThreshold(1);
        adapter = new DoctorSpinnerAdapter(this, R.layout.activity_add_records, R.id.doctorName, mList);
        mSelectDoctorName.setAdapter(adapter);
    }

    private ArrayList<DoctorDetail> retrievePeople() {
        ArrayList<DoctorDetail> list = new ArrayList<DoctorDetail>();
        for (int i = 0; i < 10; i++) {
            DoctorDetail doctorDetail = new DoctorDetail();
            doctorDetail.setDoctorName(Character.toString((char) (i + 65)) + " Sitaramanjaneyulu Rajasekhara Srinivasulu Laxminarayana Siva ");
            doctorDetail.setSpecialization(Character.toString((char) (i + 65)) + " Speciality");
            doctorDetail.setAddress(Character.toString((char) (i + 65)) + " Pune");
            list.add(doctorDetail);
        }
        return list;
    }

    @OnClick({R.id.clearButton, R.id.selectDate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.clearButton:
                mSelectDoctorName.setText("");
                break;
            case R.id.selectDate:
                break;
        }
    }
}
