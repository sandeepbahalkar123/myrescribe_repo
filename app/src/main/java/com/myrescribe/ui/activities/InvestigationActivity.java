package com.myrescribe.ui.activities;

import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.myrescribe.R;
import com.myrescribe.adapters.InvestigationViewAdapter;
import com.myrescribe.helpers.database.AppDBHelper;
import com.myrescribe.helpers.investigation.InvestigationHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.investigation.Image;
import com.myrescribe.model.investigation.Images;
import com.myrescribe.model.investigation.InvestigationData;
import com.myrescribe.model.investigation.InvestigationListModel;
import com.myrescribe.model.investigation.gmail.InvestigationUploadByGmailModel;
import com.myrescribe.model.investigation.request.InvestigationUploadByGmailRequest;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerConst;

public class InvestigationActivity extends AppCompatActivity implements InvestigationViewAdapter.CheckedClickListener, HelperResponse {

    private boolean isCompareDialogCollapsed = true;
    private static final long ANIMATION_DURATION = 400; // in milliseconds
    private static final int ANIMATION_LAYOUT_MAX_HEIGHT = 152; // in milliseconds
    private static final int ANIMATION_LAYOUT_MIN_HEIGHT = 0; // in milliseconds

    private static final int UPLOADED_DOCS = 121;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.selectDocsButton)
    Button selectDocsButton;
    @BindView(R.id.selectUploadedButton)
    Button selectUploadedButton;
    @BindView(R.id.gmailButton)
    Button gmailButton;

    @BindView(R.id.buttonLayout)
    LinearLayout buttonLayout;

    private LinearLayoutManager mLayoutManager;
    private InvestigationViewAdapter mAdapter;
    private Context mContext;
    private ArrayList<InvestigationData> investigation = new ArrayList<InvestigationData>();
    private ArrayList<InvestigationData> investigationTemp = new ArrayList<InvestigationData>();
    private AppDBHelper appDBHelper;
    private InvestigationHelper investigationHelper;
    private int patientId;
    private Intent gmailIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investigation);
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

        mContext = InvestigationActivity.this;
        appDBHelper = new AppDBHelper(mContext);

        patientId = Integer.parseInt(MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext));

        investigationHelper = new InvestigationHelper(mContext);
        investigationHelper.getInvestigationList();

        // off recyclerView Animation

        RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator)
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    public void collapseCompareDialog() {
        if (!isCompareDialogCollapsed) {
            isCompareDialogCollapsed = true;
            ValueAnimator valueAnimator = ValueAnimator.ofInt(ANIMATION_LAYOUT_MAX_HEIGHT, ANIMATION_LAYOUT_MIN_HEIGHT);
            valueAnimator.setDuration(ANIMATION_DURATION);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) buttonLayout.getLayoutParams();
                    params.height = CommonMethods.convertDpToPixel(value.intValue());
                    buttonLayout.setLayoutParams(params);
                }
            });

            valueAnimator.start();
        }
    }

    public void expandCompareDialog() {
        if (isCompareDialogCollapsed) {
            isCompareDialogCollapsed = false;
            ValueAnimator valueAnimator = ValueAnimator.ofInt(ANIMATION_LAYOUT_MIN_HEIGHT, ANIMATION_LAYOUT_MAX_HEIGHT);
            valueAnimator.setDuration(ANIMATION_DURATION);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) buttonLayout.getLayoutParams();
                    params.height = CommonMethods.convertDpToPixel(value.intValue());
                    buttonLayout.setLayoutParams(params);
                }
            });

            valueAnimator.start();
        }
    }

    private void buttonManage(int isAlreadyUploadedButtonVisible) {
        if (isAlreadyUploadedButtonVisible == View.VISIBLE)
            selectDocsButton.setText(getResources().getString(R.string.new_document));
        else selectDocsButton.setText(getResources().getString(R.string.upload));
        selectUploadedButton.setVisibility(isAlreadyUploadedButtonVisible);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Restart", "onRestart");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(InvestigationActivity.this, HomePageActivity.class);
        intent.putExtra(MyRescribeConstants.ALERT, false);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    public void onCheckedClick(int position) {
        buttonEnable();
    }

    private void buttonEnable() {
        boolean uploadButton = false;
        for (InvestigationData dataObject : investigationTemp) {
            if (dataObject.isSelected() && !dataObject.isUploaded()) {
                uploadButton = true;
                break;
            }
        }

        if (uploadButton) {
            expandCompareDialog();
        } else {
            collapseCompareDialog();
        }

        selectDocsButton.setEnabled(uploadButton);
        selectUploadedButton.setEnabled(uploadButton);
        gmailButton.setEnabled(uploadButton);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO || requestCode == UPLOADED_DOCS) {
            if (resultCode == RESULT_OK) {
                investigationTemp.clear();
                ArrayList<InvestigationData> invest = data.getParcelableArrayListExtra(MyRescribeConstants.INVESTIGATION_DATA);
                changeOriginalData(invest);
                investigationTemp.addAll(invest);
                mAdapter.notifyDataSetChanged();
                buttonEnable();
                buttonManage(View.VISIBLE);
            }
        }
    }

    private void changeOriginalData(ArrayList<InvestigationData> invest) {
        for (int i = 0; i < investigation.size(); i++) {
            for (InvestigationData objectTemp : invest) {
                if (investigation.get(i).getId() == objectTemp.getId()) {
                    investigation.set(i, objectTemp);
                }
            }
        }
    }

    @OnClick({R.id.selectUploadedButton, R.id.gmailButton, R.id.selectDocsButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.selectDocsButton:
                boolean selected = false;
                for (int i = 0; i < investigationTemp.size(); i++) {
                    if (!investigationTemp.get(i).isUploaded() && investigationTemp.get(i).isSelected())
                        selected = true;
                }
                if (selected) {
                    Intent intent = new Intent(mContext, SelectedDocsActivity.class);
                    intent.putExtra(MyRescribeConstants.INVESTIGATION_DATA, investigationTemp);
                    startActivityForResult(intent, FilePickerConst.REQUEST_CODE_PHOTO);
                } else
                    CommonMethods.showToast(mContext, "Please select at least one Document.");
                break;
            case R.id.selectUploadedButton:
                Intent intent = new Intent(mContext, UploadedDocsActivity.class);
                intent.putExtra(MyRescribeConstants.INVESTIGATION_DATA, investigation);
                intent.putExtra(MyRescribeConstants.INVESTIGATION_TEMP_DATA, investigationTemp);
                startActivityForResult(intent, UPLOADED_DOCS);
                break;
            case R.id.gmailButton:
                if (isAppAvailable("com.google.android.gm")) {
                    ArrayList<Integer> investigationId = new ArrayList<>();
                    for (InvestigationData dataObject : investigationTemp) {
                        if (dataObject.isSelected() && !dataObject.isUploaded())
                            investigationId.add(dataObject.getId());
                    }

                    InvestigationUploadByGmailRequest investigationUploadByGmailRequest = new InvestigationUploadByGmailRequest();
                    investigationUploadByGmailRequest.setPatientId(patientId);
                    investigationUploadByGmailRequest.setInvestigationId(investigationId);
                    investigationHelper.uploadByGmail(investigationUploadByGmailRequest);
                } else {
                    CommonMethods.showToast(mContext, "Gmail application not found");
                }
                break;
        }
    }

    public boolean isAppAvailable(String packageName) {
        PackageManager manager = getPackageManager();
        gmailIntent = manager.getLaunchIntentForPackage(packageName);
        if (gmailIntent == null) {
            return false;
        }
        gmailIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        return true;
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

        if (customResponse instanceof InvestigationListModel) {

            InvestigationListModel investigationListModel = (InvestigationListModel) customResponse;

            investigation = investigationListModel.getData();

            if (investigation.size() > 0) {

                for (InvestigationData dataObject : investigation) {
                    Images images = new Images();
                    images.setImageArray(dataObject.getPhotos());
                    appDBHelper.insertInvestigationData(dataObject.getId(), dataObject.getTitle(), dataObject.getInvestigationKey(), dataObject.getDoctorName(), dataObject.getOpdId(), dataObject.isUploaded(), new Gson().toJson(images));
                }

                int isAlreadyUploadedButtonVisible = View.GONE;

                for (int i = 0; i < investigation.size(); i++) {
                    InvestigationData data = appDBHelper.getInvestigationData(investigation.get(i).getId());
                    boolean status = data.isUploaded();
                    ArrayList<Image> imageArray = data.getPhotos();
                    if (!status) {
                        InvestigationData dataObject = new InvestigationData();
                        dataObject.setId(investigation.get(i).getId());
                        dataObject.setTitle(investigation.get(i).getTitle());
                        dataObject.setInvestigationKey(investigation.get(i).getInvestigationKey());
                        dataObject.setDoctorName(investigation.get(i).getDoctorName());
                        dataObject.setOpdId(investigation.get(i).getOpdId());
                        dataObject.setSelected(investigation.get(i).isSelected());
                        dataObject.setUploaded(investigation.get(i).isUploaded());
                        dataObject.setPhotos(imageArray);
                        investigationTemp.add(dataObject);
                    } else {
                        isAlreadyUploadedButtonVisible = View.VISIBLE;
                        investigation.get(i).setSelected(true);
                        investigation.get(i).setUploaded(true);
                        investigation.get(i).setPhotos(data.getPhotos());
                    }
                }

                buttonManage(isAlreadyUploadedButtonVisible);

                mAdapter = new InvestigationViewAdapter(mContext, investigationTemp);
                mRecyclerView.setAdapter(mAdapter);
            } else CommonMethods.showInfoDialog("Investigation not available", mContext, true);
        } else if (customResponse instanceof InvestigationUploadByGmailModel) {
            InvestigationUploadByGmailModel investigationUploadByGmailModel = (InvestigationUploadByGmailModel) customResponse;
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("email", investigationUploadByGmailModel.getData().getEmailId());
            clipboard.setPrimaryClip(clip);

            for (InvestigationData dataObject : investigationTemp) {
                if (dataObject.isSelected() && !dataObject.isUploaded()) {
                    dataObject.setUploaded(dataObject.isSelected());
                    appDBHelper.updateInvestigationData(dataObject.getId(), dataObject.isUploaded(), "");
                }
            }
            changeOriginalData(investigationTemp);
            buttonEnable();
            buttonManage(View.VISIBLE);
            startActivity(gmailIntent);
            CommonMethods.showToast(mContext, investigationUploadByGmailModel.getData().getEmailId());
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
