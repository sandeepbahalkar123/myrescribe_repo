package com.rescribe.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;
import com.rescribe.R;
import com.rescribe.adapters.DoctorSpinnerAdapter;
import com.rescribe.helpers.myrecords.MyRecordsHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.my_records.AddDoctorModel;
import com.rescribe.model.my_records.MyRecordsDoctorListModel;
import com.rescribe.model.my_records.RequestAddDoctorModel;
import com.rescribe.model.my_records.VisitDate;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerConst;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by jeetal on 31/7/17.
 */

@RuntimePermissions
public class AddRecordsActivity extends AppCompatActivity implements DoctorSpinnerAdapter.TextEnterListener, DatePickerDialog.OnDateSetListener, GoogleApiClient.OnConnectionFailedListener, HelperResponse {
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
    EditText selectAddressText;
    @BindView(R.id.addressIcon)
    ImageView addressIcon;

    private int PLACE_PICKER_REQUEST = 1;

    private boolean isManual = true;

    private Context mContext;
    private DoctorSpinnerAdapter doctorSpinnerAdapter;
    private DatePickerDialog datePickerDialog;
    private String mSelectDoctorString = "";
    private String mSelectDateString = "Select Date";
    private boolean isDatesThere = false;
    private String visitDate;
    private int doctorId = -1;
    private int opdId;
    private int mSelectedId;
    private MyRecordsHelper myRecordsHelper;

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

        myRecordsHelper = new MyRecordsHelper(mContext, this);
        myRecordsHelper.getDoctorList(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext));
        // HardCoded
//        myRecordsHelper.getDoctorList("4092");

        Calendar now = Calendar.getInstance();
// As of version 2.3.0, `BottomSheetDatePickerDialog` is deprecated.
        datePickerDialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setAccentColor(getResources().getColor(R.color.tagColor));

        // Places

        new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

    }

    @OnClick({R.id.clearButton, R.id.selectDateTextView, R.id.dateIcon, R.id.uploadButton, R.id.searchButton, R.id.addressIcon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.clearButton:
                mSelectDoctorName.setText("");
                selectDate.setText("");

                dropdownLayout.setVisibility(View.GONE);
                autocompleteLayout.setVisibility(View.VISIBLE);
                dateSpinnerLayout.setVisibility(View.GONE);
                selectDateLayout.setVisibility(View.VISIBLE);
                selectAddressLayout.setVisibility(View.VISIBLE);

                isManual = true;
                mSelectedId = -1;
                doctorId = -1;
                opdId = 0;
                mSelectDoctorString = "";
                mSelectDateString = getResources().getString(R.string.select_date_text);
                break;
            case R.id.selectDateTextView:
                datePickerDialog.show(getSupportFragmentManager(), getResources().getString(R.string.select_date_text));
                break;
            case R.id.dateIcon:
                datePickerDialog.show(getSupportFragmentManager(), getResources().getString(R.string.select_date_text));
                break;
            case R.id.uploadButton:
                if (isManual) {
                    if (mSelectDoctorName.getText().length() == 0) {
                        CommonMethods.showToast(mContext, getResources().getString(R.string.please_select_doctor_name));
                        return;
                    }
                    if (selectDate.getText().length() == 0) {
                        CommonMethods.showToast(mContext, getResources().getString(R.string.please_enter_date));
                        return;
                    }
                    if (selectAddressText.getText().length() == 0) {
                        CommonMethods.showToast(mContext, getResources().getString(R.string.please_enter_doctor_address));
                        return;
                    }
                    visitDate = selectDate.getText().toString();
                } else {
                    doctorId = mSelectedId;
                    if (isDatesThere) {
                        if (mSelectDoctorString.length() == 0) {
                            CommonMethods.showToast(mContext, getResources().getString(R.string.please_select_doctor_name));
                            return;
                        }
                        if (mSelectDateString.equals(getResources().getString(R.string.select_date_text))) {
                            CommonMethods.showToast(mContext, getResources().getString(R.string.please_enter_date));
                            return;
                        }
                        visitDate = mSelectDateString;
                    } else {
                        if (mSelectDoctorString.length() == 0) {
                            CommonMethods.showToast(mContext, getResources().getString(R.string.please_select_doctor_name));
                            return;
                        }
                        if (selectDate.getText().length() == 0) {
                            CommonMethods.showToast(mContext, getResources().getString(R.string.please_enter_date));
                            return;
                        }
                        visitDate = selectDate.getText().toString();
                    }
                }

                if (doctorId != -1) {
                    callRecordsActivity();
                } else {
                    RequestAddDoctorModel requestAddDoctorModel = new RequestAddDoctorModel();
                    requestAddDoctorModel.setAddress(selectAddressText.getText().toString());
                    requestAddDoctorModel.setName(mSelectDoctorName.getText().toString());
                    myRecordsHelper.addDoctor(requestAddDoctorModel);
                }
                break;
            case R.id.searchButton:
                mSelectDoctorName.setText("");
                break;
            case R.id.addressIcon:
                AddRecordsActivityPermissionsDispatcher.callPickPlaceWithCheck(this);
                break;
        }
    }

    private void callRecordsActivity() {
        Intent intent = new Intent(mContext, SelectedRecordsActivity.class);
        intent.putExtra(RescribeConstants.DOCTORS_ID, doctorId);
        intent.putExtra(RescribeConstants.OPD_ID, opdId);
        intent.putExtra(RescribeConstants.VISIT_DATE, visitDate);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AddRecordsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION})
    public void callPickPlace() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            Intent intentPlace = builder.build(AddRecordsActivity.this);
            startActivityForResult(intentPlace, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append("Name: ");
                stBuilder.append(placename);
                stBuilder.append("\n");
                stBuilder.append("Latitude: ");
                stBuilder.append(latitude);
                stBuilder.append("\n");
                stBuilder.append("Logitude: ");
                stBuilder.append(longitude);
                stBuilder.append("\n");
                stBuilder.append("Address: ");
                stBuilder.append(address);

                CommonMethods.Log("Address: ", stBuilder.toString());

                selectAddressText.setText(address);
            }
        }
    }

    @Override
    public void onTextEnter(boolean isEntered) {
        if (isEntered)
            searchButton.setImageResource(R.drawable.del);
        else searchButton.setImageResource(R.drawable.magnifying_glass);
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        selectDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

        if (customResponse instanceof MyRecordsDoctorListModel) {
            MyRecordsDoctorListModel myRecordsDoctorListModel = (MyRecordsDoctorListModel) customResponse;
            if (myRecordsDoctorListModel.getCommon().getStatusCode().equals(RescribeConstants.SUCCESS)) {
                mSelectDoctorName.setThreshold(1);
                doctorSpinnerAdapter = new DoctorSpinnerAdapter(AddRecordsActivity.this, R.layout.activity_add_records, R.id.doctorName, myRecordsDoctorListModel.getDoctors());
                mSelectDoctorName.setAdapter(doctorSpinnerAdapter);

                mSelectDoctorName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        final ArrayList<VisitDate> spinnerList = new ArrayList<VisitDate>();
                        mSelectDateString = getResources().getString(R.string.select_date_text);
                        mSelectDoctorString = doctorSpinnerAdapter.getDoctor(position).getDoctorName();

                        mSelectedId = doctorSpinnerAdapter.getDoctor(position).getId();
                        if (!doctorSpinnerAdapter.getDoctor(position).getDates().isEmpty()) {
                            isDatesThere = true;

                            dropdownLayout.setVisibility(View.VISIBLE);
                            autocompleteLayout.setVisibility(View.GONE);
                            doctorName.setText(doctorSpinnerAdapter.getDoctor(position).getDoctorName());
                            doctorSpecialist.setText(doctorSpinnerAdapter.getDoctor(position).getSpecialization());
                            doctorAddress.setText(doctorSpinnerAdapter.getDoctor(position).getAddress());

                            dateSpinnerLayout.setVisibility(View.VISIBLE);
                            selectDateLayout.setVisibility(View.GONE);

                            selectAddressLayout.setVisibility(View.GONE);
                        } else {
                            isDatesThere = false;

                            mSelectDoctorName.setText("");
                            dropdownLayout.setVisibility(View.VISIBLE);
                            autocompleteLayout.setVisibility(View.GONE);
                            doctorName.setText(doctorSpinnerAdapter.getDoctor(position).getDoctorName());
                            doctorSpecialist.setText(doctorSpinnerAdapter.getDoctor(position).getSpecialization());
                            doctorAddress.setText(doctorSpinnerAdapter.getDoctor(position).getAddress());

                            dateSpinnerLayout.setVisibility(View.GONE);
                            selectDateLayout.setVisibility(View.VISIBLE);

                            selectAddressLayout.setVisibility(View.GONE);
                        }

                        isManual = false;

                        VisitDate visitDate = new VisitDate();
                        visitDate.setOpdDate(getResources().getString(R.string.select_date_text));
                        visitDate.setOpdId(0);
                        spinnerList.add(visitDate);
                        for (VisitDate date : doctorSpinnerAdapter.getDoctor(position).getDates()) {
                            String formatedDate = CommonMethods.getFormatedDate(date.getOpdDate(), RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DD_MM_YYYY);

                            VisitDate visitD = new VisitDate();
                            visitD.setOpdDate(formatedDate);
                            visitD.setOpdId(date.getOpdId());
                            spinnerList.add(visitD);
                        }

                        ArrayAdapter<VisitDate> arrayAdapter = new ArrayAdapter<>(AddRecordsActivity.this, R.layout.simple_spinner_item, spinnerList);
                        selectDateSpinner.setAdapter(arrayAdapter);
                        selectDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mSelectDateString = spinnerList.get(position).getOpdDate();
                                opdId = spinnerList.get(position).getOpdId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });
            }
        } else if (customResponse instanceof AddDoctorModel) {
            AddDoctorModel addDoctorModel = (AddDoctorModel) customResponse;
            if (addDoctorModel.getCommon().getStatusCode().equals(RescribeConstants.SUCCESS)) {
                doctorId = addDoctorModel.getData().getDocId();
                callRecordsActivity();
            }
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