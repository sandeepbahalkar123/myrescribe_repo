package com.rescribe.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rescribe.R;
import com.rescribe.adapters.myrecords.ShowRecordsAdapter;
import com.rescribe.helpers.myrecords.MyRecordsHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.my_records.DeleteRecordDetailsModel;
import com.rescribe.model.my_records.DeleteRecordModel;
import com.rescribe.model.my_records.DeleteRecordResponseModel;
import com.rescribe.model.my_records.MyRecordReports;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowRecordsActivity extends AppCompatActivity implements ShowRecordsAdapter.OnRecordsListener, HelperResponse {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.deleteButton)
    Button deleteButton;

    private Context mContext;
    String caption;
    String type;
    ShowRecordsAdapter showRecordsAdapter;
    MyRecordsHelper myRecordsHelper;
    ArrayList<MyRecordReports.ImageListData> imageArrayExtra;
    boolean isRecordDeleted=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_records);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.title_activity_reports));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mContext = ShowRecordsActivity.this;
        myRecordsHelper = new MyRecordsHelper(mContext, this);
        imageArrayExtra = (ArrayList<MyRecordReports.ImageListData>) getIntent().getSerializableExtra(RescribeConstants.DOCUMENTS);

        caption = getIntent().getStringExtra(RescribeConstants.CAPTION);
        type = getIntent().getStringExtra(RescribeConstants.TYPE);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (imageArrayExtra != null) {
            if (imageArrayExtra.size() != 0) {
                // off recyclerView Animation
                RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
                if (animator instanceof SimpleItemAnimator)
                    ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);

                showRecordsAdapter = new ShowRecordsAdapter(mContext, imageArrayExtra, caption, this);
                recyclerView.setAdapter(showRecordsAdapter);
                GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
                recyclerView.setLayoutManager(layoutManager);
            }
        }


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<DeleteRecordDetailsModel> recordDetailsModels = new ArrayList<>();

                for (MyRecordReports.ImageListData listData : imageArrayExtra) {
                    if (listData.isChecked()) {

                        DeleteRecordDetailsModel detailsModel = new DeleteRecordDetailsModel(listData.getImageId(), type);
                        recordDetailsModels.add(detailsModel);
                    }

                }
                String patientId = RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, mContext);
                DeleteRecordModel deleteRecordModel = new DeleteRecordModel(patientId, recordDetailsModels);

                if (recordDetailsModels.size() != 0)
                    myRecordsHelper.deleteMyRecord(deleteRecordModel);
                else
                    CommonMethods.showToast(mContext,"Select record to delete.");

            }
        });
    }

    @Override
    public void showDeleteButton(boolean isShowButton) {
        if (isShowButton)
            deleteButton.setVisibility(View.VISIBLE);
        else
            deleteButton.setVisibility(View.GONE);

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        Log.e("customResponse", "--" + customResponse);
        DeleteRecordResponseModel recordResponseModel = (DeleteRecordResponseModel) customResponse;
        if (recordResponseModel.getCommon().isSuccess()) {
            showRecordsAdapter.RemoveDeletedRecord();
            isRecordDeleted=true;
            if (showRecordsAdapter.isEmpty()) {
                Intent intent = new Intent();
                intent.putExtra(RescribeConstants.DOCUMENTS, imageArrayExtra);
                intent.putExtra(RescribeConstants.RECORD_DELETED, isRecordDeleted);
                setResult(Activity.RESULT_OK, intent);

                onBackPressed();
            }
            CommonMethods.showToast(mContext, recordResponseModel.getCommon().getStatusMessage());
        } else {
            CommonMethods.showToast(mContext, recordResponseModel.getCommon().getStatusMessage());
        }


    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra(RescribeConstants.DOCUMENTS, imageArrayExtra);
        intent.putExtra(RescribeConstants.RECORD_DELETED, isRecordDeleted);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
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
