package com.rescribe.ui.activities.vital_graph;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;
import com.rescribe.R;
import com.rescribe.adapters.vital_graph.AddTrackerAdapter;
import com.rescribe.adapters.vital_graph.VitalGraphAdapter;
import com.rescribe.helpers.vital_graph_helper.VitalGraphHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.DatePickerDialogListener;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.Common;
import com.rescribe.model.vital_graph.vital_all_list.VitalGraphBaseModel;
import com.rescribe.model.vital_graph.vital_all_list.VitalGraphData;
import com.rescribe.model.vital_graph.vital_all_list.VitalGraphList;
import com.rescribe.model.vital_graph.vital_tracker.VitalGraphAddNewTrackerRequestModel;
import com.rescribe.model.vital_graph.vital_tracker.VitalGraphTracker;
import com.rescribe.model.vital_graph.vital_tracker.VitalGraphTrackerBaseModel;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.customesViews.EditTextWithDeleteButton;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.data;
import static android.R.attr.mode;

public class AddTrackerActivity extends AppCompatActivity implements AddTrackerAdapter.ItemClickListener, HelperResponse {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.listView)
    RecyclerView mRecyclerView;
    @BindView(R.id.emptyListView)
    RelativeLayout mEmptyListView;
    @BindView(R.id.searchView)
    EditTextWithDeleteButton mSearchView;
    private VitalGraphHelper mVitalGraphHelper;
    private ArrayList<VitalGraphTracker> mReceivedTrackerList;
    private AddTrackerAdapter mVitalGraphTrackerAdapter;

    String mSelectedTrackerDateToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tracker);
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
        mVitalGraphHelper = new VitalGraphHelper(this, this);
        mVitalGraphHelper.doGetPatientVitalTrackerList();

        mSearchView.addClearTextButtonListener(new EditTextWithDeleteButton.OnClearButtonClickedInEditTextListener() {
            @Override
            public void onClearButtonClicked() {
                isDataListViewVisible(true);
                mVitalGraphTrackerAdapter.notifyDataSetChanged();
            }
        });
        mSearchView.addTextChangedListener(new EditTextWithDeleteButton.TextChangedListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                mVitalGraphTrackerAdapter.getFilter().filter(s);

            }
        });
    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

        switch (mOldDataTag) {
            case RescribeConstants.TASK_GET_VITAL_TRACKER_LIST:
                VitalGraphTrackerBaseModel temp = (VitalGraphTrackerBaseModel) customResponse;
                setTrackerListAdapter(temp);
                break;
            case RescribeConstants.TASK_ADD_VITAL_MANUALLY:
                Common common = (Common) customResponse;
                CommonMethods.showToast(this, "" + "Vital added successfully.");
                break;
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

    public void isDataListViewVisible(boolean flag) {
        if (flag) {
            mEmptyListView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mEmptyListView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }


    private void setTrackerListAdapter(VitalGraphTrackerBaseModel vitalGraphTrackerBaseModel) {
        if (vitalGraphTrackerBaseModel == null) {
            isDataListViewVisible(false);
        } else {
            VitalGraphTrackerBaseModel.VitalGraphTrackerDataModel data = vitalGraphTrackerBaseModel.getVitalGraphTrakcerDataModel();
            if (data == null) {
                isDataListViewVisible(false);
            } else {
                ArrayList<VitalGraphTracker> trackerList = data.getVitalGraphTrackersList();
                if (trackerList.size() == 0) {
                    isDataListViewVisible(false);
                } else {
                    isDataListViewVisible(true);
                    mReceivedTrackerList = trackerList;
                    mVitalGraphTrackerAdapter = new AddTrackerAdapter(this, mReceivedTrackerList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                            DividerItemDecoration.VERTICAL);
                    mRecyclerView.addItemDecoration(dividerItemDecoration);
                    mRecyclerView.setAdapter(mVitalGraphTrackerAdapter);
                }
            }
        }
    }


    @Override
    public void onTrackerClick(VitalGraphTracker vitalList) {
        openAddTrackerDialog(vitalList);
    }


    public void openAddTrackerDialog(final VitalGraphTracker vitalList) {
        final View modalbottomsheet = getLayoutInflater().inflate(R.layout.add_new_tracker_dialog, null);
        final CustomTextView addTrackerDate = (CustomTextView) modalbottomsheet.findViewById(R.id.addTrackerDate);
        CustomTextView header = (CustomTextView) modalbottomsheet.findViewById(R.id.header);
        header.setText(vitalList.getVitalName());
        final EditText reading = (EditText) modalbottomsheet.findViewById(R.id.reading);

        //------
        final LinearLayout bloodPressureReadingLayout = (LinearLayout) modalbottomsheet.findViewById(R.id.bloodPressureReadingLayout);
        final EditText systolic = (EditText) modalbottomsheet.findViewById(R.id.systolic);
        final EditText dystolic = (EditText) modalbottomsheet.findViewById(R.id.dystolic);
        //------
        Button addTrackerButton = (Button) modalbottomsheet.findViewById(R.id.addTrackerButton);
        //---------


        //-------------
        if (vitalList.getVitalName().equalsIgnoreCase("Blood Pressure")) {
            bloodPressureReadingLayout.setVisibility(View.VISIBLE);
            reading.setVisibility(View.GONE);
        } else {
            bloodPressureReadingLayout.setVisibility(View.GONE);
            reading.setVisibility(View.VISIBLE);
        }

        //-------------

        final BottomSheetDialog dialog = new BottomSheetDialog(this);

        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        dialog.show();

        addTrackerDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
                                addTrackerDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                mSelectedTrackerDateToSend = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setAccentColor(getResources().getColor(R.color.tagColor));
                datePickerDialog.show(getSupportFragmentManager(), getResources().getString(R.string.enter_reading));
            }
        });

        addTrackerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = addTrackerDate.getText().toString();
                String readingData = reading.getText().toString();
                String systolicData = systolic.getText().toString();
                String dystolicData = dystolic.getText().toString();
                if (date.trim().length() == 0) {
                    CommonMethods.showToast(AddTrackerActivity.this, getString(R.string.enter_date));
                } else if (readingData.trim().length() == 0 && reading.getVisibility() == View.VISIBLE) {
                    CommonMethods.showToast(AddTrackerActivity.this, getString(R.string.enter_reading));
                } else if (bloodPressureReadingLayout.getVisibility() == View.VISIBLE && systolicData.trim().length() == 0) {
                    CommonMethods.showToast(AddTrackerActivity.this, getString(R.string.enter_systolic));
                } else if (bloodPressureReadingLayout.getVisibility() == View.VISIBLE && dystolicData.trim().length() == 0) {
                    CommonMethods.showToast(AddTrackerActivity.this, getString(R.string.enter_diastolic));
                } else {
                    VitalGraphAddNewTrackerRequestModel model = new VitalGraphAddNewTrackerRequestModel();

                    model.setCheckDate(mSelectedTrackerDateToSend);
                    model.setVitalName(vitalList.getVitalName());
                    if (bloodPressureReadingLayout.getVisibility() == View.VISIBLE) {
                        model.setVitalValue(systolic.getText().toString() + "/" + dystolic.getText().toString());
                    } else {
                        model.setVitalValue(reading.getText().toString());
                    }
                    mVitalGraphHelper.doAddNewVitalGraphTracker(model);
                    dialog.dismiss();
                }
            }
        });

    }

}
