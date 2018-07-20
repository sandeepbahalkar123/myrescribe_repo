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

import static com.amulyakhare.textdrawable.util.ColorGenerator.MATERIAL;
import static com.rescribe.adapters.DoctorListAdapter.DOCTOR_ID;
import static com.rescribe.adapters.SingleVisitAdapter.CHILD_TYPE_ATTACHMENTS;
import static com.rescribe.adapters.SingleVisitAdapter.CHILD_TYPE_VITALS;
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
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_details_activity);
        ButterKnife.bind(this);
        initialize();
    }


    private void initialize() {

        mContext = SingleVisitDetailsActivity.this;
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
            setDoctorImage(mDocName, mIntent.getStringExtra(getString(R.string.doctor_image)));

            String stringExtra = mIntent.getStringExtra(getString(R.string.one_day_visit_date));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mDateTextView.setText(Html.fromHtml(stringExtra, Html.FROM_HTML_MODE_LEGACY));
            } else {
                mDateTextView.setText(Html.fromHtml(stringExtra));
            }
        }

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

                if (childObject.size() == 1) {

                    boolean flag = true;
                    if (listDataList.get(groupPosition).getCaseDetailName().toLowerCase().contains(CHILD_TYPE_ATTACHMENTS) || listDataList.get(groupPosition).getCaseDetailName().toLowerCase().contains(CHILD_TYPE_VITALS))
                        flag = false;

                    if (flag) {

                        if(!listDataList.get(groupPosition).getCaseDetailName().contains("allergie")){
                            if (childObject.get(0).getName().length() <= TEXT_LIMIT)
                                mHistoryExpandableListView.collapseGroup(groupPosition);

                        }else{
                            String textToShow = "";
                            String name = childObject.get(0).getName();
                            String medicineName = childObject.get(0).getMedicinename();
                            String remarks = childObject.get(0).getRemarks();

                            if(!name.isEmpty()){
                                if(name.contains("food")){
                                    textToShow += name;
                                    if (!remarks.isEmpty())
                                        textToShow += "-" +remarks;
                                }else{
                                    textToShow += name;
                                    if (!medicineName.isEmpty())
                                        textToShow += "-" +medicineName;
                                    if (!remarks.isEmpty())
                                        textToShow += "-" +remarks;
                                }
                            }else{
                                textToShow += name;
                            }

                            if (textToShow.length() <= TEXT_LIMIT)
                                mHistoryExpandableListView.collapseGroup(groupPosition);
                        }

                    }
                }

                collapseOther(groupPosition);
            }

            private void collapseOther(int groupPosition) {
                if (mLastExpandedPosition != -1 && mLastExpandedPosition != groupPosition)
                    mHistoryExpandableListView.collapseGroup(mLastExpandedPosition);
                mLastExpandedPosition = groupPosition;
            }
        });

        mHistoryExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {
                mHistoryExpandableListView.collapseGroup(groupPosition);
                return false;
            }
        });

    }

    private void setDoctorImage(String docName, String docImage) {

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // width in px
                .height(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // height in px
                .endConfig()
                .buildRound("" + docName.charAt(0), MATERIAL.getColor(docName));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        requestOptions.placeholder(drawable);
        requestOptions.error(drawable);

        Glide.with(this)
                .load(docImage)
                .apply(requestOptions)
                .into(mDoctorImg);
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        VisitData visitData = (VisitData) customResponse;
        List<PatientHistory> patientHistoryList = visitData.getPatientHistory();
        List<Vital> vitalSortedList = new ArrayList<>();

        boolean isEmpty = true;

        // Bpmin and Bpmax is clubed together as Bp in vitals
        for (int i = 0; i < patientHistoryList.size(); i++) {
            if (patientHistoryList.get(i).getVitals() != null) {
                String pos = null;

                List<Vital> vitalList = patientHistoryList.get(i).getVitals();
                if (!vitalList.isEmpty())
                    isEmpty = false;

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
                                if(!dataObject.getCategory().isEmpty())
                                    vital.setCategory(dataObject.getCategory());
                                else
                                    vital.setCategory(dataObject.getUnitName());
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
                                String unitCategory="";
                                if(!previousData.getCategory().isEmpty()) {
                                    unitCategory = previousData.getCategory();
                                    unitCategory = unitCategory + getString(R.string.colon_sign) + dataObject.getCategory();
                                }else {
                                    unitCategory = previousData.getUnitName();
                                    unitCategory = unitCategory + getString(R.string.colon_sign) + dataObject.getUnitName();

                                }for (int k = 0; k < dataObject.getRanges().size(); k++) {
                                    dataObject.getRanges().get(k).setNameOfVital(getString(R.string.bp_min));
                                }

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
                            if(!vitalList.get(j).getCategory().isEmpty())
                                vital.setCategory(vitalList.get(j).getCategory());
                                else
                                vital.setCategory(vitalList.get(j).getUnitName());
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
            } else if (!patientHistoryList.get(i).getCommonData().isEmpty())
                isEmpty = false;
        }

        if (isEmpty) {
            mHistoryExpandableListView.setVisibility(View.GONE);
            mNoRecordAvailable.setVisibility(View.VISIBLE);
        } else {
            mHistoryExpandableListView.setVisibility(View.VISIBLE);
            mNoRecordAvailable.setVisibility(View.GONE);
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

