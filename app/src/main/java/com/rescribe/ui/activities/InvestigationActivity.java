package com.rescribe.ui.activities;

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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.adapters.InvestigationViewAdapter;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.investigation.InvestigationHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.investigation.Image;
import com.rescribe.model.investigation.Images;
import com.rescribe.model.investigation.InvestigationData;
import com.rescribe.model.investigation.InvestigationListModel;
import com.rescribe.model.investigation.gmail.InvestigationUploadByGmailModel;
import com.rescribe.model.investigation.request.InvestigationUploadByGmailRequest;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerConst;

public class InvestigationActivity extends AppCompatActivity implements InvestigationViewAdapter.CheckedClickListener, HelperResponse {

    private boolean isCompareDialogCollapsed = true;
    private static final long ANIMATION_DURATION = 300; // in milliseconds
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

        String patientIdString = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);

        patientId = Integer.parseInt(patientIdString.equals("") ? "0" : patientIdString);

        investigationHelper = new InvestigationHelper(mContext);
        getIntentData();
//        investigationHelper.getInvestigationList(true);

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
    public void onBackPressed() {
        Intent intent = new Intent(InvestigationActivity.this, HomePageActivity.class);
        intent.putExtra(RescribeConstants.ALERT, false);
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
                ArrayList<InvestigationData> invest = data.getParcelableArrayListExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_DATA);
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
                    intent.putExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_DATA, investigationTemp);
                    startActivityForResult(intent, FilePickerConst.REQUEST_CODE_PHOTO);
                } else
                    CommonMethods.showToast(mContext, getResources().getString(R.string.please_select_at_least_one_document));
                break;
            case R.id.selectUploadedButton:
                Intent intent = new Intent(mContext, UploadedDocsActivity.class);
                intent.putExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_DATA, investigation);
                intent.putExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_TEMP_DATA, investigationTemp);
                startActivityForResult(intent, UPLOADED_DOCS);
                break;
            case R.id.gmailButton:
                if (isAppAvailable(RescribeConstants.GMAIL_PACKAGE)) {
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
                    CommonMethods.showToast(mContext, getResources().getString(R.string.gmail_application_not_found));
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
        if (customResponse instanceof InvestigationUploadByGmailModel) {
            InvestigationUploadByGmailModel investigationUploadByGmailModel = (InvestigationUploadByGmailModel) customResponse;
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            String email = "";
            if (investigationUploadByGmailModel.getData() != null) {
                email = investigationUploadByGmailModel.getData().getEmailId();
                ClipData clip = ClipData.newPlainText(RescribeConstants.EMAIL, email);
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

    private void getIntentData() {

        investigation = getIntent().getParcelableArrayListExtra(RescribeConstants.INVESTIGATION_LIST);

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
        } else
            CommonMethods.showInfoDialog(getResources().getString(R.string.no_investigation), mContext, true);
    }
}
