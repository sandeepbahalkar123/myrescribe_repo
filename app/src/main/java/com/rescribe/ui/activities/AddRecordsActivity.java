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
import com.rescribe.R;
import com.rescribe.adapters.DoctorSpinnerAdapter;
import com.rescribe.helpers.myrecords.MyRecordsHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.myrecords.MyRecordsDoctorListModel;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;

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
    private int doctorId;
    private int mSelectedId;

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

        MyRecordsHelper myRecordsHelper = new MyRecordsHelper(mContext, this);
        myRecordsHelper.getDoctorList(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext));
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

    /*MyRecordsDoctorListModel getFilteredDoctorList() {
        try {
            InputStream is = mContext.getAssets().open("spinner_doctor.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            return new Gson().fromJson(json, MyRecordsDoctorListModel.class);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }*/

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
                mSelectDoctorString = "";
                mSelectDateString = "Select Date ";
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
                        CommonMethods.showToast(mContext, "Please enter Doctor Name");
                        return;
                    }
                    if (selectDate.getText().length() == 0) {
                        CommonMethods.showToast(mContext, "Please enter Date");
                        return;
                    }
                    if (selectAddressText.getText().length() == 0) {
                        CommonMethods.showToast(mContext, "Please enter Doctor Address");
                        return;
                    }
                    visitDate = selectDate.getText().toString();
                    doctorId = -1; // needs to generate
                } else {
                    doctorId = mSelectedId;
                    if (isDatesThere) {
                        if (mSelectDoctorString.length() == 0) {
                            CommonMethods.showToast(mContext, "Please enter Doctor Name");
                            return;
                        }
                        if (mSelectDateString.equals("Select Date")) {
                            CommonMethods.showToast(mContext, "Please enter Date");
                            return;
                        }
                        visitDate = mSelectDateString;
                    } else {
                        if (mSelectDoctorString.length() == 0) {
                            CommonMethods.showToast(mContext, "Please enter Doctor Name");
                            return;
                        }
                        if (selectDate.getText().length() == 0) {
                            CommonMethods.showToast(mContext, "Please enter Date");
                            return;
                        }
                        visitDate = selectDate.getText().toString();
                    }
                }
                Intent intent = new Intent(mContext, SelectedRecordsActivity.class);
                intent.putExtra(RescribeConstants.DOCTORS_ID, doctorId);
                intent.putExtra(RescribeConstants.VISIT_DATE, visitDate);
                startActivityForResult(intent, FilePickerConst.REQUEST_CODE_PHOTO);
                break;
            case R.id.searchButton:
                mSelectDoctorName.setText("");
                break;
            case R.id.addressIcon:
                AddRecordsActivityPermissionsDispatcher.callPickPlaceWithCheck(this);
                break;
        }
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
        selectDate.setText(dayOfMonth + "-" + monthOfYear + "-" + year);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

        MyRecordsDoctorListModel myRecordsDoctorListModel = (MyRecordsDoctorListModel) customResponse;

        if (myRecordsDoctorListModel.getCommon().getStatusCode().equals(RescribeConstants.SUCCESS)) {
            mSelectDoctorName.setThreshold(1);
            doctorSpinnerAdapter = new DoctorSpinnerAdapter(AddRecordsActivity.this, R.layout.activity_add_records, R.id.doctorName, myRecordsDoctorListModel.getDoctors());
            mSelectDoctorName.setAdapter(doctorSpinnerAdapter);

            mSelectDoctorName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final ArrayList<String> spinnerList = new ArrayList<String>();
                    mSelectDateString = "Select Date";
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

                    spinnerList.add("Select Date");
                    for (String date : doctorSpinnerAdapter.getDoctor(position).getDates())
                        spinnerList.add(CommonMethods.getFormatedDate(date, RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DD_MM_YYYY));

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddRecordsActivity.this, R.layout.simple_spinner_item, spinnerList);
                    selectDateSpinner.setAdapter(arrayAdapter);
                    selectDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            mSelectDateString = spinnerList.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            });
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
