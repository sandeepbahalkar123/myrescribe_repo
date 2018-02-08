package com.rescribe.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.adapters.SingleVisitAdapter;
import com.rescribe.helpers.single_visit_details.SingleVisitDetailHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.case_details.PatientHistory;
import com.rescribe.model.case_details.Range;
import com.rescribe.model.case_details.VisitCommonData;
import com.rescribe.model.case_details.VisitData;
import com.rescribe.model.case_details.Vital;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rescribe.adapters.DoctorListAdapter.DOCTOR_ID;
import static com.rescribe.adapters.SingleVisitAdapter.TEXT_LIMIT;

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
    CircularImageView mDoctorImg;
    @BindView(R.id.title)
    CustomTextView title;
    private int mLastExpandedPosition = -1;
    Intent mIntent;
    private SingleVisitAdapter mSingleVisitAdapter;
    private boolean isBpMin = false;
    private boolean isBpMax = false;
    private String mDocId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_details_activity);
        ButterKnife.bind(this);
        initialize();
    }


    private void initialize() {

        Context mContext = SingleVisitDetailsActivity.this;
        ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
        mIntent = getIntent();
        if (getIntent().getExtras() != null) {
            String mDocName = mIntent.getStringExtra(getString(R.string.name));
            String doctorName = "";
            if (mIntent.getStringExtra(getString(R.string.name)).contains("Dr.")) {
                doctorName = mIntent.getStringExtra(getString(R.string.name));
            } else {
                doctorName = "Dr. " + mIntent.getStringExtra(getString(R.string.name));
            }

            mDocId = mIntent.getStringExtra(DOCTOR_ID);

            mDoctorName.setText(doctorName);
            mDoctorSpecialization.setText(mIntent.getStringExtra(getString(R.string.specialization)));
            mDoctor_address.setText(mIntent.getStringExtra(getString(R.string.address)));
            int color2 = mColorGenerator.getColor(mIntent.getStringExtra(getString(R.string.name)));
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .width(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // width in px
                    .height(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // height in px
                    .endConfig()
                    .buildRound("" + mDocName.charAt(0), color2);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.dontAnimate();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.skipMemoryCache(true);
            requestOptions.placeholder(drawable);
            requestOptions.error(drawable);

            Glide.with(this)
                    .load(mIntent.getStringExtra(getString(R.string.doctor_image)))
                    .apply(requestOptions).thumbnail(0.5f)
                    .into(mDoctorImg);


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
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        requestOptions.placeholder(droidninja.filepicker.R.drawable.image_placeholder);
        requestOptions.error(droidninja.filepicker.R.drawable.image_placeholder);

        Glide.with(this)
                .load(mIntent.getStringExtra(getString(R.string.doctor_image)))
                .apply(requestOptions)
                .into(mDoctorImg);

        SingleVisitDetailHelper mSingleVisitDetailHelper = new SingleVisitDetailHelper(this, this);
        mSingleVisitDetailHelper.doGetOneDayVisit(mIntent.getStringExtra(getString(R.string.opd_id)), mDocId);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        title.setText(getString(R.string.visit_details));
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

                // this is done because if single element in child list , groupPosition will not expand, it will expand on advice even if it has only one element ,vitals will also expand
                List<PatientHistory> listDataList = mSingleVisitAdapter.getListDataList();
                List<VisitCommonData> childObject = listDataList.get(groupPosition).getCommonData();

                if (mSingleVisitAdapter.getListDataList().get(groupPosition).getCaseDetailName().equalsIgnoreCase("vitals")) {
                    if (mSingleVisitAdapter.getListDataList().get(groupPosition).getVitals().isEmpty()) {
                        mHistoryExpandableListView.collapseGroup(groupPosition);
                    }
                } else if (childObject.size() == 1) {
                    if (childObject.get(0).getName().length() <= TEXT_LIMIT)
                        mHistoryExpandableListView.collapseGroup(groupPosition);
                }

                collapseOther(groupPosition);
            }

            private void collapseOther(int groupPosition) {
                if (mLastExpandedPosition != -1 && mLastExpandedPosition != groupPosition)
                    mHistoryExpandableListView.collapseGroup(mLastExpandedPosition);
                mLastExpandedPosition = groupPosition;
            }
        });

        mHistoryExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()

        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {

                mHistoryExpandableListView.collapseGroup(groupPosition);

                return false;
            }
        });

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
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
                    if (dataObject.getUnitName().contains(getString(R.string.bp_max))) {
                        setBpMax(true);
                    }
                    if (dataObject.getUnitName().contains(getString(R.string.bp_min))) {
                        setBpMin(true);
                    }
                }

                for (int j = 0; j < vitalList.size(); j++) {
                    Vital dataObject = vitalList.get(j);
                    if (isBpMax() && isBpMin()) {
                        if (dataObject.getUnitName().contains(getString(R.string.bp_max)) || dataObject.getUnitName().contains(getString(R.string.bp_min))) {
                            Vital vital = new Vital();
                            if (pos == null) {
                                vital.setUnitName(getString(R.string.bp) + " " + dataObject.getUnitValue());
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

                    } else {
                        Vital vital;
                        if (dataObject.getUnitName().contains(getString(R.string.bp_max))) {
                            vital = vitalList.get(j);
                            vital.setUnitName("Systolic BP" + " " + vital.getUnitValue());
                            vital.setDisplayName("Systolic BP");
                            vitalSortedList.add(vital);
                        } else if (dataObject.getUnitName().contains(getString(R.string.bp_min))) {
                            vital = vitalList.get(j);
                            vital.setUnitName("Diastolic BP" + " " + vital.getUnitValue());
                            vital.setDisplayName("Diastolic BP");
                            vitalSortedList.add(vital);
                        } else {
                            vital = vitalList.get(j);
                            vitalSortedList.add(vital);
                        }
                    }
                }
                patientHistoryList.get(i).setVitals(vitalSortedList);
            }
        }


        mSingleVisitAdapter = new SingleVisitAdapter(this, patientHistoryList);
        mHistoryExpandableListView.setAdapter(mSingleVisitAdapter);


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

    public boolean isBpMin() {
        return isBpMin;
    }

    public void setBpMin(boolean bpMin) {
        isBpMin = bpMin;
    }

    public boolean isBpMax() {
        return isBpMax;
    }

    public void setBpMax(boolean bpMax) {
        isBpMax = bpMax;
    }
}

