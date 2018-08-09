package com.rescribe.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.rescribe.R;
import com.rescribe.adapters.UploadedImageAdapter;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.investigation.InvestigationHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.investigation.Image;
import com.rescribe.model.investigation.InvestigationData;
import com.rescribe.model.investigation.uploaded.InvestigationUploadFromUploadedModel;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadedDocsActivity extends AppCompatActivity implements HelperResponse {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.uploadButton)
    Button uploadButton;

    private Context mContext;
    private ArrayList<Image> photoPaths = new ArrayList<>();
    private AppDBHelper appDBHelper;
    private ArrayList<InvestigationData> investigationTemp;
    private InvestigationHelper investigationHelper;
    private String mUnreadInvestigationMsgID;
    private String patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleted_docs);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mContext = UploadedDocsActivity.this;
        appDBHelper = new AppDBHelper(mContext);
        patientId = RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, mContext);
        investigationHelper = new InvestigationHelper(mContext, this);

        ArrayList<InvestigationData> investigation = getIntent().getParcelableArrayListExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_DATA);
        investigationTemp = getIntent().getParcelableArrayListExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_TEMP_DATA);

        for (InvestigationData dataObject : investigation)
            photoPaths.addAll(dataObject.getPhotos());

        UploadedImageAdapter uploadedImageAdapter = new UploadedImageAdapter(mContext, photoPaths);
        recyclerView.setAdapter(uploadedImageAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        recyclerView.setLayoutManager(layoutManager);

        mUnreadInvestigationMsgID = getIntent().getStringExtra(RescribeConstants.NOTIFICATION_ID);

    }

    @OnClick(R.id.uploadButton)
    public void onViewClicked() {

        int selectedImageCount = 0;
        StringBuilder imageIds = new StringBuilder();
        StringBuilder invIds = new StringBuilder();
        StringBuilder invTypes = new StringBuilder();

        for (Image image : photoPaths) {
            if (image.isSelected()) {
                selectedImageCount++;
                imageIds.append(",").append(imageIds);
            }
        }

        // Update server status with image id

        if (selectedImageCount > 0) {
            for (InvestigationData dataObject : investigationTemp) {
                if (dataObject.isSelected() && !dataObject.isUploaded())
                    invIds.append(",").append(dataObject.getId());
                invTypes.append(",").append(dataObject.getInvestigationType());
            }

            investigationHelper.uploadFromAlreadyUploaded(imageIds.toString(), invIds.toString(), invTypes.toString(), patientId);

        } else {
            CommonMethods.showToast(mContext, "Please select at least one document");
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (customResponse instanceof InvestigationUploadFromUploadedModel) {

            int selectedCount = 0;

            CommonMethods.showToast(mContext, "Uploaded Successfully");
            for (InvestigationData dataObject : investigationTemp) {
                if (dataObject.isSelected() && !dataObject.isUploaded()) {
                    dataObject.setUploaded(dataObject.isSelected());
                    appDBHelper.updateInvestigationData(dataObject.getId(), dataObject.isSelected(), "");
                }

                if (dataObject.isSelected())
                    selectedCount += 1;
            }

            if (selectedCount == investigationTemp.size()) {
                AppDBHelper.getInstance(this).deleteUnreadReceivedNotificationMessage(mUnreadInvestigationMsgID, RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT);

                Intent intent = new Intent(this, HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent();
                intent.putExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_DATA, investigationTemp);
                setResult(RESULT_OK, intent);
            }
            finish();
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
