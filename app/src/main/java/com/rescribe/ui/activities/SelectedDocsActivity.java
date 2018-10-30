package com.rescribe.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.adapters.SelectedImageAdapter;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.investigation.Image;
import com.rescribe.model.investigation.Images;
import com.rescribe.model.investigation.InvestigationData;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.singleton.Device;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.customesViews.CustomProgressDialog;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.NetworkUtil;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SelectedDocsActivity extends AppCompatActivity {

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
    private String patientId = "";
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

        patientId = RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, mContext);

        investigation = getIntent().getParcelableArrayListExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_DATA);

        for (int i = 0; i < investigation.size(); i++) {
            if (investigation.get(i).isSelected() && !investigation.get(i).isUploaded() && investigation.get(i).getPhotos().size() > 0) {
                media_id = i;
                break;
            }
        }

        if (media_id == -1) {
            showPickerDialog();
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
                // Show two options for user
                showPickerDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showPickerDialog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_file_dialog);
        dialog.setCanceledOnTouchOutside(false);

        dialog.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SelectedDocsActivityPermissionsDispatcher.onPickCameraPhotoWithCheck(SelectedDocsActivity.this);
            }
        });

        dialog.findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SelectedDocsActivityPermissionsDispatcher.onPickPhotoWithCheck(SelectedDocsActivity.this);
            }
        });

        dialog.findViewById(R.id.files).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SelectedDocsActivityPermissionsDispatcher.onPickDocWithCheck(SelectedDocsActivity.this);
            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
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

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onPickCameraPhoto() {
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
                    .openCameraDirect(true)
                    .showGifs(false)
                    .showFolderView(true)
                    .enableOrientation(true)
                    .pickPhoto(this);
        }
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onPickDoc() {

        String[] documents = {".doc", ".docx", ".odt", ".pdf", ".xls", ".xlsx", ".ods", ".ppt", ".pptx"};

        if (photoPaths.size() == MAX_ATTACHMENT_COUNT)
            Toast.makeText(this, "Cannot select more than " + MAX_ATTACHMENT_COUNT + " documents", Toast.LENGTH_SHORT).show();
        else {

            ArrayList photos = new ArrayList();
            for (Image photo : photoPaths)
                photos.add(photo.getImagePath());

            FilePickerBuilder.getInstance().setMaxCount(MAX_ATTACHMENT_COUNT)
                    .setSelectedFiles(photos)
                    .setActivityTheme(R.style.AppTheme)
                    .addFileSupport(documents)
                    .enableDocSupport(false)
                    .enableOrientation(true)
                    .pickFile(this);
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
        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                photoPaths.clear();
                for (String imagePath : data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)) {
                    Image image = new Image();
                    image.setImageId(patientId + "_" + UUID.randomUUID().toString());
                    image.setImagePath(imagePath);
                    image.setSelected(false);
                    photoPaths.add(image);
                }
                selectedImageAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == FilePickerConst.REQUEST_CODE_DOC) {
            if (resultCode == Activity.RESULT_OK) {
                photoPaths.clear();
                for (String imagePath : data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS)) {
                    Image image = new Image();
                    image.setImageId(patientId + "_" + UUID.randomUUID().toString());
                    image.setImagePath(imagePath);
                    image.setSelected(false);
                    photoPaths.add(image);
                }
                selectedImageAdapter.notifyDataSetChanged();
            }
        }
    }

    @OnClick(R.id.uploadButton)
    public void onViewClicked() {

        if (NetworkUtil.isInternetAvailable(mContext)) {
            if (!photoPaths.isEmpty()) {
                customProgressDialog.show();
                StringBuilder investigationIds = new StringBuilder();
                StringBuilder investigationTypes = new StringBuilder();
                StringBuilder opdIds = new StringBuilder();

                imageUploadedCount = 0;
                imageUploadFailedCount = 0;

                for (int index = 0; index < investigation.size(); index++) {
                    InvestigationData dataObject = investigation.get(index);
                    if (dataObject.isSelected() && !dataObject.isUploaded()) {
                        investigationIds.append(dataObject.getId());
                        opdIds.append(dataObject.getOpdId());
                        if (dataObject.getInvestigationType() != null)
                            investigationTypes.append(dataObject.getInvestigationType());

                        investigationIds.append(",");
                        opdIds.append(",");
                        investigationTypes.append(",");
                    }
                }

                investigationIds.deleteCharAt(investigationIds.length() - 1);
                opdIds.deleteCharAt(opdIds.length() - 1);
                investigationTypes.deleteCharAt(investigationTypes.length() - 1);


                for (Image image : photoPaths) {
                    Device device = Device.getInstance(mContext);
                    String authorizationString = RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.AUTHTOKEN, mContext);
                    HashMap<String, String> mapHeaders = new HashMap<>();

                    mapHeaders.put(RescribeConstants.AUTHORIZATION_TOKEN, authorizationString);
                    mapHeaders.put(RescribeConstants.DEVICEID, device.getDeviceId());
                    mapHeaders.put(RescribeConstants.OS, device.getOS());
                    mapHeaders.put(RescribeConstants.OSVERSION, device.getOSVersion());
                    mapHeaders.put(RescribeConstants.DEVICE_TYPE, device.getDeviceType());

                    mapHeaders.put(RescribeConstants.INVESTIGATION_KEYS.IMAGE_ID, image.getImageId());
                    mapHeaders.put(RescribeConstants.INVESTIGATION_KEYS.INV_ID, investigationIds.toString());
                    mapHeaders.put(RescribeConstants.INVESTIGATION_KEYS.INV_TYPES, investigationTypes.toString());
                    mapHeaders.put(RescribeConstants.INVESTIGATION_KEYS.OPD_ID, opdIds.toString());
                    mapHeaders.put(RescribeConstants.INVESTIGATION_KEYS.PATIENT_ID, patientId);

                    SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, Config.BASE_URL + Config.INVESTIGATION_UPLOAD,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("Response", response);
                                    publishResults(response);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            String msg = "{\"common\":{\"success\":false,\"statusCode\":400,\"statusMessage\": \"Server Error \"} }";
                            publishResults(msg);
                        }
                    });
                    smr.setHeaders(mapHeaders);
                    smr.addFile("investigationDoc", image.getImagePath());
                    RescribeApplication.getInstance().addToRequestQueue(smr);

                }
            } else
                CommonMethods.showToast(mContext, "Please select at least one document");
        } else
            CommonMethods.showToast(mContext, getResources().getString(R.string.internet));

    }

    private void publishResults(String response) {
        Gson gson = new Gson();
        CommonBaseModelContainer common = gson.fromJson(response, CommonBaseModelContainer.class);
        Toast.makeText(this, common.getCommonRespose().getStatusMessage(), Toast.LENGTH_SHORT).show();

        if (common.getCommonRespose().isSuccess()) {
            imageUploadedCount++;

        CommonMethods.Log("Status", imageUploadedCount + " Completed "  + response);

        if ((imageUploadedCount + imageUploadFailedCount) == photoPaths.size())
            allUploaded();

        } else {
            imageUploadFailedCount++;
            if (imageUploadFailedCount == photoPaths.size()) {
            CommonMethods.showToast(mContext, "Uploading Failed");
            customProgressDialog.dismiss();
        } else if ((imageUploadedCount + imageUploadFailedCount) == photoPaths.size())
            allUploaded();
        }


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
