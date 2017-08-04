package com.myrescribe.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import com.myrescribe.R;
import android.widget.ExpandableListView;
import com.myrescribe.adapters.OneDayVisitAdapter;
import com.myrescribe.helpers.one_day_visit.OneDayVisitHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.case_details.Data;
import com.myrescribe.ui.customesViews.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 14/6/17.
 */


public class ViewDetailsActivity extends AppCompatActivity implements HelperResponse {

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
    private int lastExpandedPosition = -1;
    Intent intent;
    private OneDayVisitHelper mOneDayVisitHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_details_activity);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        intent = getIntent();
        if (getIntent().getExtras() != null) {
            mDoctorName.setText(intent.getStringExtra(getString(R.string.name)));
            mDoctorSpecialization.setText(intent.getStringExtra(getString(R.string.specialization)));
            mDoctor_address.setText(intent.getStringExtra(getString(R.string.address)));
            String stringExtra = intent.getStringExtra(getString(R.string.one_day_visit_date));

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mDateTextView.setText(Html.fromHtml(stringExtra, Html.FROM_HTML_MODE_LEGACY));
            } else {
                mDateTextView.setText(Html.fromHtml(stringExtra));
            }
        }


        mOneDayVisitHelper = new OneDayVisitHelper(this, this);
        mOneDayVisitHelper.doGetOneDayVisit(intent.getStringExtra(getString(R.string.opd_id)));

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
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    mHistoryExpandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
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

        Data data = (Data) customResponse;
        OneDayVisitAdapter oneDayVisitAdapter = new OneDayVisitAdapter(this, data.getPatientHistory());
        mHistoryExpandableListView.setAdapter(oneDayVisitAdapter);


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

