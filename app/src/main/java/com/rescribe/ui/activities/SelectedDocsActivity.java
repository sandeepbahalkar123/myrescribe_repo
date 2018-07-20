package com.rescribe.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.adapters.SelectedImageAdapter;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.model.investigation.Image;
import com.rescribe.model.investigation.Images;
import com.rescribe.model.investigation.InvestigationData;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.singleton.Device;
import com.rescribe.ui.customesViews.CustomProgressDialog;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.NetworkUtil;
import com.rescribe.util.RescribeConstants;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SelectedDocsActivity extends AppCompatActivity implements UploadStatusDelegate {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.uploadButton)
    Button uploadButton;

    private int imageUploadedCount = 0;
    private int imageUploadFailedCount = 0;
    private CustomProgressDialog customProgressDialog;

    private static final int MAX_ATTACHMENT_COUNT = 10;
    private Context mContext;
    private ArrayList<Image> photoPaths = new ArrayList<>();
    private int media_id = -1;
    private SelectedImageAdapter selectedImageAdapter;
    private ArrayList<InvestigationData> investigation;
    private AppDBHelper appDBHelper;
    private String patient_id = "";
    private String mUnreadInvestigationMsgID;

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

        mContext = SelectedDocsActivity.this;
        appDBHelper = new AppDBHelper(mContext);
        customProgressDialog = new CustomProgressDialog(mContext);
        customProgressDialog.setCancelable(false);

        patient_id = RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, mContext);

        investigation = getIntent().getParcelableArrayListExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_DATA);

        for (int i = 0; i < investigation.size(); i++) {
            if (investigation.get(i).isSelected() && !investigation.get(i).isUploaded() && investigation.get(i).getPhotos().size() > 0) {
                media_id = i;
                break;
            }
        }

        if (media_id == -1) {
            SelectedDocsActivityPermissionsDispatcher.onPickPhotoWithCheck(SelectedDocsActivity.this);
            photoPaths = new ArrayList<>();
        } else {
            photoPaths = investigation.get(media_id).getPhotos();
        }

        selectedImageAdapter = new SelectedImageAdapter(mContext, photoPaths);
        recyclerView.setAdapter(selectedImageAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        recyclerView.setLayoutManager(layoutManager);

        mUnreadInvestigationMsgID = getIntent().getStringExtra(RescribeConstants.NOTIFICATION_ID);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_docs_menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_docs:
                SelectedDocsActivityPermissionsDispatcher.onPickPhotoWithCheck(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(FilePickerConst.KEY_SELECTED_MEDIA, photoPaths);
        setResult(RESULT_CANCELED, intent);
        super.onBackPressed();
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onPickPhoto() {
        if (photoPaths.size() == MAX_ATTACHMENT_COUNT)
            Toast.makeText(this, "Cannot select more than " + MAX_ATTACHMENT_COUNT + " documents", Toast.LENGTH_SHORT).show();
        else {

            ArrayList photos = new ArrayList();
            for (Image photo : photoPaths)
                photos.add(photo.getImagePath());

            FilePickerBuilder.getInstance().setMaxCount(MAX_ATTACHMENT_COUNT)
                    .setSelectedFiles(photos)
                    .setActivityTheme(R.style.AppTheme)
                    .enableVideoPicker(false)
                    .enableCameraSupport(true)
                    .enableCameraMultiplePhotos(false)
                    .openCameraDirect(false)
                    .showGifs(false)
                    .showFolderView(true)
                    .enableOrientation(true)
                    .pickPhoto(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SelectedDocsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA) == null)
            finish();
        else if (data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).size() == 0)
            finish();
        else {
            if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
//            int id = data.getIntExtra(FilePickerConst.MEDIA_ID, 0);
                if (resultCode == Activity.RESULT_OK) {
                    photoPaths.clear();
                    for (String imagePath : data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)) {
                        Image image = new Image();
                        image.setImageId(patient_id + "_" + UUID.randomUUID().toString());
                        image.setImagePath(imagePath);
                        image.setSelected(false);
                        photoPaths.add(image);
                    }
                    selectedImageAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @OnClick(R.id.uploadButton)
    public void onViewClicked() {

        if (NetworkUtil.isInternetAvailable(mContext)) {
            if (photoPaths.size() > 0 && photoPaths != null) {
                customProgressDialog.show();
                String investigationIds = "";
                String investigationTypes = "";

                imageUploadedCount = 0;
                imageUploadFailedCount = 0;

                for (InvestigationData dataObject : investigation) {
                    if (dataObject.isSelected() && !dataObject.isUploaded()) {
                        investigationIds = investigationIds + "," + dataObject.getId();
                        investigationTypes = investigationTypes + "," + dataObject.getInvestigationType();
                    }
                }

                for (Image image : photoPaths) {
                    try {
                        Device device = Device.getInstance(mContext);
                        String authorizationString = RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.AUTHTOKEN, mContext);

                        String uploadId = new MultipartUploadRequest(SelectedDocsActivity.this, Config.BASE_URL + Config.INVESTIGATION_UPLOAD)
                                .setMaxRetries(RescribeConstants.MAX_RETRIES)

                                .addHeader(RescribeConstants.AUTHORIZATION_TOKEN, authorizationString)
                                .addHeader(RescribeConstants.DEVICEID, device.getDeviceId())
                                .addHeader(RescribeConstants.OS, device.getOS())
                                .addHeader(RescribeConstants.OSVERSION, device.getOSVersion())
                                .addHeader(RescribeConstants.DEVICE_TYPE, device.getDeviceType())

                                .addHeader("imgId", image.getImageId())
                                .addHeader("invIds", investigationIds)
                                .addHeader("types", investigationTypes)
                                .addFileToUpload(image.getImagePath(), "investigationDoc")
                                .setDelegate(SelectedDocsActivity.this)
                                .startUpload();

                        CommonMethods.Log("ImagedUploadId", uploadId);
                    } catch (Exception exc) {
                        CommonMethods.Log("AndroidUploadService", exc.getMessage());
                    }
                }
            } else
                CommonMethods.showToast(mContext, "Please select at least one document");
        } else
            CommonMethods.showToast(mContext, getResources().getString(R.string.internet));

    }

    @Override
    public void onProgress(Context context, UploadInfo uploadInfo) {
        CommonMethods.Log("Status", uploadInfo.getProgressPercent() + " " + uploadInfo.getUploadId());
    }

    @Override
    public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
        imageUploadFailedCount++;
        CommonMethods.Log("Status", imageUploadFailedCount + " Error " + uploadInfo.getUploadId());
        if (imageUploadFailedCount == photoPaths.size()) {
            CommonMethods.showToast(mContext, "Uploading Failed");
            customProgressDialog.dismiss();
        } else if ((imageUploadedCount + imageUploadFailedCount) == photoPaths.size())
            allUploaded();
    }

    @Override
    public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
        imageUploadedCount++;

        CommonMethods.Log("Status", imageUploadedCount + " Completed " + uploadInfo.getUploadId());

        if ((imageUploadedCount + imageUploadFailedCount) == photoPaths.size())
            allUploaded();
    }

    @Override
    public void onCancelled(Context context, UploadInfo uploadInfo) {
        CommonMethods.Log("Status", "Cancelled");
    }

    void allUploaded() {
        customProgressDialog.dismiss();
        CommonMethods.showToast(mContext, getResources().getString(R.string.documents_uploaded_successfully));
        imageUploadedCount = 0;
        int selectedCount = 0;
        for (InvestigationData dataObject : investigation) {
            if (dataObject.isSelected() && !dataObject.isUploaded()) {
                dataObject.setUploaded(dataObject.isSelected());
                Images images = new Images();
                images.setImageArray(photoPaths);
                dataObject.setPhotos(photoPaths);
                appDBHelper.updateInvestigationData(dataObject.getId(), dataObject.isUploaded(), new Gson().toJson(images));
            }
            if (dataObject.isSelected())
                selectedCount += 1;
        }

        if (selectedCount == investigation.size()) {
            AppDBHelper.getInstance(this).deleteUnreadReceivedNotificationMessage(mUnreadInvestigationMsgID, RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT);

            Intent intent = new Intent(this, HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else {
            Intent intent = new Intent();
            intent.putExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_DATA, investigation);
//                intent.putExtra(FilePickerConst.KEY_SELECTED_MEDIA, photoPaths);
            setResult(RESULT_OK, intent);
        }

        finish();
    }
}
