package com.rescribe.ui.activities.book_appointment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.book_appointment.ShowReviewsAdapter;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.reviews.ReviewListBaseModel;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 5/10/17.
 */

public class ShowReviewListActivity extends AppCompatActivity implements HelperResponse {
    @BindView(R.id.listView)
    RecyclerView listView;
    ShowReviewsAdapter showReviewsAdapter;
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    @BindView(R.id.bookAppointmentBackButton)
    ImageView bookAppointmentBackButton;
    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.locationTextView)
    CustomTextView locationTextView;
    @BindView(R.id.showlocation)
    CustomTextView showlocation;
    private DoctorDataHelper doctorDataHelper;
    private static ReviewListBaseModel mReviewListBaseModel;
    private DividerItemDecoration mDividerItemDecoration;
    HashMap<String, String> userSelectedLocationInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_layout);
        ButterKnife.bind(this);
        initialize();

    }

    private void initialize() {
        userSelectedLocationInfo = RescribeApplication.getUserSelectedLocationInfo();
        bookAppointmentBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        title.setText(getString(R.string.reviews));
        showlocation.setVisibility(View.GONE);
        locationTextView.setVisibility(View.GONE);
        doctorDataHelper = new DoctorDataHelper(this, this);
        doctorDataHelper.doGetReviewsList(getIntent().getStringExtra(getString(R.string.doctorId)));
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (customResponse != null) ;
        mReviewListBaseModel = (ReviewListBaseModel) customResponse;
        setAdapter();
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        listView.setVisibility(View.GONE);
        emptyListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        listView.setVisibility(View.GONE);
        emptyListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        listView.setVisibility(View.GONE);
        emptyListView.setVisibility(View.VISIBLE);
    }

    private void setAdapter() {
        if (mReviewListBaseModel.getReviewList().getReviews().getReviews().size() != 0) {
            listView.setVisibility(View.VISIBLE);
            emptyListView.setVisibility(View.GONE);
            showReviewsAdapter = new ShowReviewsAdapter(this, mReviewListBaseModel.getReviewList().getReviews().getReviews());
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            listView.setLayoutManager(layoutManager);
            listView.setHasFixedSize(true);
            mDividerItemDecoration = new DividerItemDecoration(
                    listView.getContext(),
                    layoutManager.getOrientation()
            );
            listView.addItemDecoration(mDividerItemDecoration);
            listView.setAdapter(showReviewsAdapter);
        } else {
            listView.setVisibility(View.GONE);
            emptyListView.setVisibility(View.VISIBLE);
        }
    }
}
