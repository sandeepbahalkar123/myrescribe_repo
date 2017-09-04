package com.rescribe.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.adapters.SingleVisitAdapter;
import com.rescribe.helpers.single_visit_details.SingleVisitDetailHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.case_details.PatientHistory;
import com.rescribe.model.case_details.Range;
import com.rescribe.model.case_details.VisitData;
import com.rescribe.model.case_details.Vital;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 14/6/17.
 */


public class SingleVisitDetailsActivity extends AppCompatActivity implements HelperResponse {

    @BindView(R.id.historyExpandableListView)
    ExpandableListView mHistoryExpandableListView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.doctorSpecialization)
    CustomTextView mDoctorSpecialization;
    @BindView(R.id.doctorName)
    CustomTextView mDoctorName;
    @BindView(R.id.doctor_address)
    CustomTextView mDoctor_address;
    @BindView(R.id.dateTextView)
    CustomTextView mDateTextView;
    @BindView(R.id.noRecordAvailable)
    RelativeLayout mNoRecordAvailable;
    @BindView(R.id.doctorImg)
    CircularImageView doctorImg;
    private int mLastExpandedPosition = -1;
    Intent mIntent;
    private String TAG = getClass().getName();
    private SingleVisitDetailHelper mSingleVisitDetailHelper;
    private int imageSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_details_activity);
        ButterKnife.bind(this);
        setColumnNumber(this, 2);

        initialize();
    }

    private void initialize() {
        mIntent = getIntent();
        if (getIntent().getExtras() != null) {
            mDoctorName.setText(mIntent.getStringExtra(getString(R.string.name)));
            mDoctorSpecialization.setText(mIntent.getStringExtra(getString(R.string.specialization)));
            mDoctor_address.setText(mIntent.getStringExtra(getString(R.string.address)));
            String stringExtra = mIntent.getStringExtra(getString(R.string.one_day_visit_date));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mDateTextView.setText(Html.fromHtml(stringExtra, Html.FROM_HTML_MODE_LEGACY));
            } else {
                mDateTextView.setText(Html.fromHtml(stringExtra));
            }
        }

        //---
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.override(imageSize, imageSize);
        requestOptions.placeholder(droidninja.filepicker.R.drawable.image_placeholder);

        Glide.with(this)
                .load(mIntent.getStringExtra(getString(R.string.doctor_image)))
                .apply(requestOptions).thumbnail(0.5f)
                .into(doctorImg);

        //--


        mSingleVisitDetailHelper = new SingleVisitDetailHelper(this, this);
        mSingleVisitDetailHelper.doGetOneDayVisit(mIntent.getStringExtra(getString(R.string.opd_id)));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.visit_details));
        mToolbar.setNavigationIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_white_24dp, null));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        mHistoryExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (mLastExpandedPosition != -1
                        && groupPosition != mLastExpandedPosition) {
                    mHistoryExpandableListView.collapseGroup(mLastExpandedPosition);
                }
                mLastExpandedPosition = groupPosition;
            }
        });
        mHistoryExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                mHistoryExpandableListView.collapseGroup(groupPosition);

                return false;
            }
        });

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        String bpMin = "";
        VisitData visitData = (VisitData) customResponse;
        if (visitData != null) {
            mHistoryExpandableListView.setVisibility(View.VISIBLE);
            mNoRecordAvailable.setVisibility(View.GONE);
        } else {
            mHistoryExpandableListView.setVisibility(View.GONE);
            mNoRecordAvailable.setVisibility(View.VISIBLE);
        }
        List<PatientHistory> patientHistoryList = visitData.getPatientHistory();
        List<Vital> vitalSortedList = new ArrayList<>();
        // Bpmin and Bpmax is clubed together as Bp in vitals
        for (int i = 0; i < patientHistoryList.size(); i++) {
            if (patientHistoryList.get(i).getVitals() != null) {
                String pos = null;

                List<Vital> vitalList = patientHistoryList.get(i).getVitals();
                for (int j = 0; j < vitalList.size(); j++) {
                    Vital dataObject = vitalList.get(j);
                    if (dataObject.getUnitName().equalsIgnoreCase(getString(R.string.bp_max)) || dataObject.getUnitName().equalsIgnoreCase(getString(R.string.bp_min))) {
                        Vital vital = new Vital();
                        if (pos == null) {
                            vital.setUnitName(getString(R.string.bp));
                            vital.setUnitValue(dataObject.getUnitValue());
                            vital.setCategory(dataObject.getCategory());
                            vital.setIcon(dataObject.getIcon());
                            for (int k = 0; k < dataObject.getRanges().size(); k++) {
                                dataObject.getRanges().get(k).setNameOfVital(getString(R.string.bp_max));
                            }
                            vital.setRanges(dataObject.getRanges());
                            vital.setDisplayName(dataObject.getDisplayName());
                            vitalSortedList.add(vital);
                            pos = String.valueOf(j);
                        } else {
                            Vital previousData = vitalSortedList.get(Integer.parseInt(pos));
                            String unitValue = previousData.getUnitValue();
                            String unitCategory = previousData.getCategory();
                            unitCategory = unitCategory + getString(R.string.colon_sign) + dataObject.getCategory();
                            unitValue = unitValue + "/" + dataObject.getUnitValue();
                            previousData.setUnitName(getString(R.string.bp));
                            previousData.setUnitValue(unitValue);
                            previousData.setCategory(unitCategory);
                            List<Range> ranges = previousData.getRanges();
                            ranges.addAll(dataObject.getRanges());
                            vitalSortedList.set(Integer.parseInt(pos), previousData);
                        }
                    } else {
                        Vital vital = new Vital();
                        vital.setUnitName(vitalList.get(j).getUnitName());
                        vital.setUnitValue(vitalList.get(j).getUnitValue());
                        vital.setCategory(vitalList.get(j).getCategory());
                        vital.setRanges(vitalList.get(j).getRanges());
                        vital.setIcon(vitalList.get(j).getIcon());
                        vital.setDisplayName(vitalList.get(j).getDisplayName());
                        vitalSortedList.add(vital);
                    }
                }
                patientHistoryList.get(i).setVitals(vitalSortedList);
            }
        }


        SingleVisitAdapter singleVisitAdapter = new SingleVisitAdapter(this, patientHistoryList);
        mHistoryExpandableListView.setAdapter(singleVisitAdapter);


    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        mHistoryExpandableListView.setVisibility(View.GONE);
        mNoRecordAvailable.setVisibility(View.VISIBLE);
    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = (widthPixels / columnNum) - CommonMethods.convertDpToPixel(30);
    }

}

