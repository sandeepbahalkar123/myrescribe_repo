package com.rescribe.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rescribe.R;
import com.rescribe.adapters.myrecords.SelectedRecordsAdapter;
import com.rescribe.model.investigation.Image;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import io.github.shree.fabmenu.FabSpeedDial;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SelectedRecordsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.uploadButton)
    Button uploadButton;
    @BindView(R.id.fab)
    FabSpeedDial fab;
    @BindView(R.id.coachmark)
    ImageView coachmark;

    private static final int MAX_ATTACHMENT_COUNT = 10;
    private Context mContext;
    private ArrayList<Image> imagePaths = new ArrayList<>();

    private SelectedRecordsAdapter selectedRecordsAdapter;
    private String patient_id = "";
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleted_records);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.title_activity_report_selection));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mContext = SelectedRecordsActivity.this;
        String coachMarkStatus = RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.COACHMARK, mContext);
        if (coachMarkStatus.equals(RescribeConstants.YES))
            coachmark.setVisibility(View.GONE);

        patient_id = RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, mContext);

        // Show two options for user

        dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_file_dialog);
        dialog.setCanceledOnTouchOutside(false);

        dialog.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SelectedRecordsActivityPermissionsDispatcher.onPickPhotoWithCheck(SelectedRecordsActivity.this);
            }
        });

        dialog.findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SelectedRecordsActivityPermissionsDispatcher.onPickPhotoWithCheck(SelectedRecordsActivity.this);
            }
        });

        dialog.findViewById(R.id.files).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SelectedRecordsActivityPermissionsDispatcher.onPickDocWithCheck(SelectedRecordsActivity.this);
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (imagePaths.isEmpty())
                    onBackPressed();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        // End
        // off recyclerView Animation

        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator)
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);

        selectedRecordsAdapter = new SelectedRecordsAdapter(mContext, imagePaths);
        recyclerView.setAdapter(selectedRecordsAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        recyclerView.setLayoutManager(layoutManager);

        fab.addOnStateChangeListener(new FabSpeedDial.OnStateChangeListener() {
            @Override
            public void onStateChange(boolean open) {
                if (open) {
                    fab.getMainFab().setImageResource(R.drawable.x);
                    fab.getMainFab().setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.statusbar));
                } else {
                    fab.getMainFab().setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.tagColor));
                    fab.getMainFab().setImageResource(R.drawable.fab_icon_records);
                }
            }
        });

        fab.addOnMenuItemClickListener(new FabSpeedDial.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(FloatingActionButton miniFab, @Nullable TextView label, int itemId) {
                for (Image image : imagePaths) {
                    if (image.isSelected()) {
                        if (label != null) {
                            image.setParentCaption(label.getText().toString());
                            image.setSelected(false);
                        }
                    }
                }
                selectedRecordsAdapter.notifyDataSetChanged();
            }
        });
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
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onPickPhoto() {
        if (imagePaths.size() == MAX_ATTACHMENT_COUNT)
            Toast.makeText(this, "Cannot select more than " + MAX_ATTACHMENT_COUNT + " documents", Toast.LENGTH_SHORT).show();
        else {

            ArrayList photos = new ArrayList();
            for (Image photo : imagePaths) {
                if (photo.getType() == FilePickerConst.REQUEST_CODE_PHOTO)
                photos.add(photo.getImagePath());
            }

            FilePickerBuilder.getInstance().setMaxCount(MAX_ATTACHMENT_COUNT)
                    .setSelectedFiles(photos)
                    .setActivityTheme(R.style.AppTheme)
                    .enableVideoPicker(false)
                    .enableCameraSupport(true)
                    .showGifs(false)
                    .showFolderView(true)
                    .enableOrientation(true)
                    .pickPhoto(this);
        }
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onPickDoc() {
        String[] documents = {".doc", ".docx", ".odt", ".pdf", ".xls", ".xlsx", ".ods", ".ppt", ".pptx"};
        if (imagePaths.size() == MAX_ATTACHMENT_COUNT)
            Toast.makeText(this, "Cannot select more than " + MAX_ATTACHMENT_COUNT + " documents", Toast.LENGTH_SHORT).show();
        else {
            ArrayList photos = new ArrayList();
            for (Image photo : imagePaths) {
                if (photo.getType() == FilePickerConst.REQUEST_CODE_DOC)
                    photos.add(photo.getImagePath());
            }

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
        SelectedRecordsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
                if (data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).size() == 0) {
                    if (imagePaths.isEmpty())
                        finish();
                } else
                    addFiles(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA), FilePickerConst.REQUEST_CODE_PHOTO);
            } else if (requestCode == FilePickerConst.REQUEST_CODE_DOC) {
                if (data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS).size() == 0) {
                    if (imagePaths.isEmpty())
                        finish();
                } else
                    addFiles(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS), FilePickerConst.REQUEST_CODE_DOC);
            }
        } else if (imagePaths.isEmpty())
            finish();
    }

    private void addFiles(ArrayList<String> data, int type) {
        for (String imagePath : data) {
            boolean isExist = false;
            for (Image imagePre : imagePaths) {
                if (imagePre.getImagePath().equals(imagePath))
                    isExist = true;
            }

            if (!isExist) {
                Image image = new Image();
                image.setImageId(patient_id + "_" + UUID.randomUUID().toString());
                image.setImagePath(imagePath);
                image.setType(type);
                image.setSelected(false);
                imagePaths.add(image);
            }
        }
        selectedRecordsAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.coachmark, R.id.uploadButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.coachmark:
                coachmark.setVisibility(View.GONE);
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.COACHMARK, RescribeConstants.YES, mContext);
                break;
            case R.id.uploadButton:
                if (imagePaths.size() > 0) {
                    Intent intent = new Intent(mContext, SelectedRecordsGroupActivity.class);
                    intent.putExtra(RescribeConstants.DOCTORS_ID, getIntent().getIntExtra(RescribeConstants.DOCTORS_ID, 0));
                    intent.putExtra(RescribeConstants.VISIT_DATE, getIntent().getStringExtra(RescribeConstants.VISIT_DATE));
                    intent.putExtra(RescribeConstants.OPD_ID, getIntent().getIntExtra(RescribeConstants.OPD_ID, 0));
                    intent.putExtra(RescribeConstants.DOCUMENTS, imagePaths);
                    startActivity(intent);
                } else
                    CommonMethods.showToast(mContext, "Please select at least one document");
                break;
        }
    }
}
