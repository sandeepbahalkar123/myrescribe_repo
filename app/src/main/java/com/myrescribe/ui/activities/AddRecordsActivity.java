package com.myrescribe.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.myrescribe.R;
import com.myrescribe.adapters.DoctorSpinnerAdapter;
import com.myrescribe.model.records.SpinnerDoctorListModel;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerConst;

/**
 * Created by jeetal on 31/7/17.
 */

public class AddRecordsActivity extends AppCompatActivity implements DoctorSpinnerAdapter.TextEnterListener {
    @BindView(R.id.addRecordsToolbar)
    Toolbar mToolbar;
    @BindView(R.id.selectDoctorName)
    AutoCompleteTextView mSelectDoctorName;
    @BindView(R.id.clearButton)
    ImageView clearButton;
    @BindView(R.id.selectDateTextView)
    TextView selectDate;
    @BindView(R.id.uploadButton)
    Button uploadButton;
    @BindView(R.id.doctorImage)
    ImageView doctorImage;
    @BindView(R.id.doctorSpecialist)
    TextView doctorSpecialist;
    @BindView(R.id.doctorName)
    TextView doctorName;
    @BindView(R.id.doctorAddress)
    TextView doctorAddress;
    @BindView(R.id.dropdownLayout)
    RelativeLayout dropdownLayout;
    @BindView(R.id.searchButton)
    ImageView searchButton;
    @BindView(R.id.autocompleteLayout)
    RelativeLayout autocompleteLayout;
    @BindView(R.id.selectDateSpinner)
    Spinner selectDateSpinner;
    @BindView(R.id.dateSpinnerLayout)
    RelativeLayout dateSpinnerLayout;
    @BindView(R.id.dateIcon)
    ImageView dateIcon;
    @BindView(R.id.selectDateLayout)
    RelativeLayout selectDateLayout;
    @BindView(R.id.selectAddressLayout)
    RelativeLayout selectAddressLayout;

    @BindView(R.id.selectAddressText)
    AutoCompleteTextView selectAddressText;

    private Context mContext;
    DoctorSpinnerAdapter doctorSpinnerAdapter;

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

        mSelectDoctorName.setThreshold(1);
        doctorSpinnerAdapter = new DoctorSpinnerAdapter(AddRecordsActivity.this, R.layout.activity_add_records, R.id.doctorName, getFilteredDoctorList().getDoctors());
        mSelectDoctorName.setAdapter(doctorSpinnerAdapter);

        mSelectDoctorName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dropdownLayout.setVisibility(View.VISIBLE);
                autocompleteLayout.setVisibility(View.GONE);
                doctorName.setText(doctorSpinnerAdapter.getDoctor(position).getDoctorName());
                doctorSpecialist.setText(doctorSpinnerAdapter.getDoctor(position).getSpecialization());
                doctorAddress.setText(doctorSpinnerAdapter.getDoctor(position).getAddress());

                dateSpinnerLayout.setVisibility(View.VISIBLE);
                selectDateLayout.setVisibility(View.GONE);

                selectAddressLayout.setVisibility(View.GONE);

                ArrayList<String> spinnerList = new ArrayList<String>();
                spinnerList.add("Select Date");
                for (String date : doctorSpinnerAdapter.getDoctor(position).getDates())
                    spinnerList.add(CommonMethods.getFormatedDate(date, MyRescribeConstants.DATE_PATTERN.UTC_PATTERN, MyRescribeConstants.DD_MM_YYYY));

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddRecordsActivity.this, R.layout.simple_spinner_item, spinnerList);
                selectDateSpinner.setAdapter(arrayAdapter);
            }
        });
    }

    SpinnerDoctorListModel getFilteredDoctorList() {
        try {
            InputStream is = mContext.getAssets().open("spinner_doctor.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            return new Gson().fromJson(json, SpinnerDoctorListModel.class);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @OnClick({R.id.clearButton, R.id.selectDateTextView, R.id.uploadButton, R.id.searchButton, R.id.selectDateLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.clearButton:
                mSelectDoctorName.setText("");
                dropdownLayout.setVisibility(View.GONE);
                autocompleteLayout.setVisibility(View.VISIBLE);

                dateSpinnerLayout.setVisibility(View.GONE);
                selectDateLayout.setVisibility(View.VISIBLE);
                selectAddressLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.selectDateTextView:

                break;
            case R.id.uploadButton:
                Intent intent = new Intent(mContext, SelectedRecordsActivity.class);
                startActivityForResult(intent, FilePickerConst.REQUEST_CODE_PHOTO);
                break;
            case R.id.searchButton:
                mSelectDoctorName.setText("");
                break;
            case R.id.selectDateLayout:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onTextEnter(boolean isEntered) {
        if (isEntered)
            searchButton.setImageResource(R.drawable.del);
        else searchButton.setImageResource(R.drawable.magnifying_glass);
    }
}
