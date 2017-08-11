package com.myrescribe.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.myrescribe.R;
import com.myrescribe.adapters.SelectedRecordsAdapter;
import com.myrescribe.model.investigation.Image;
import com.myrescribe.model.investigation.Images;
import com.myrescribe.model.investigation.InvestigationData;
import com.myrescribe.model.investigation.SelectedDocModel;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

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

    private static final int MAX_ATTACHMENT_COUNT = 10;
    private Context mContext;
    private ArrayList<Image> photoPaths = new ArrayList<>();
    private int media_id = -1;
    private SelectedRecordsAdapter selectedRecordsAdapter;
    private ArrayList<InvestigationData> investigation = new ArrayList<>();
    //    private AppDBHelper appDBHelper;
    private String patient_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleted_records);
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

        mContext = SelectedRecordsActivity.this;
//        appDBHelper = new AppDBHelper(mContext);

        patient_id = MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);

        for (int i = 0; i < investigation.size(); i++) {
            if (investigation.get(i).isSelected() && !investigation.get(i).isUploaded() && investigation.get(i).getPhotos().size() > 0) {
                media_id = i;
                break;
            }
        }

        if (media_id == -1) {
            SelectedRecordsActivityPermissionsDispatcher.onPickPhotoWithCheck(SelectedRecordsActivity.this);
            photoPaths = new ArrayList<>();
        } else {
            photoPaths = investigation.get(media_id).getPhotos();
        }

        // off recyclerView Animation

        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator)
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);

        selectedRecordsAdapter = new SelectedRecordsAdapter(mContext, photoPaths);
        recyclerView.setAdapter(selectedRecordsAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        recyclerView.setLayoutManager(layoutManager);

        fab.addOnMenuItemClickListener(new FabSpeedDial.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(FloatingActionButton miniFab, @Nullable TextView label, int itemId) {
                for (Image image : photoPaths) {
                    if (image.isSelected()) {
                        if (label != null) {
                            image.setCaption(label.getText().toString());
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
                SelectedRecordsActivityPermissionsDispatcher.onPickPhotoWithCheck(this);
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
                    .showGifs(false)
                    .showFolderView(true)
                    .enableOrientation(true)
                    .pickPhoto(this);
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

        if (data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA) == null)
            finish();
        else if (data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).size() == 0)
            finish();
        else {
            if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
//            int id = data.getIntExtra(FilePickerConst.MEDIA_ID, 0);
                if (resultCode == Activity.RESULT_OK) {
//                    photoPaths.clear();

                    for (String imagePath : data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)) {
                        boolean isExist = false;
                        for (Image imagePre : photoPaths) {
                            if (imagePre.getImagePath().equals(imagePath))
                                isExist = true;
                        }

                        if (!isExist) {
                            Image image = new Image();
                            image.setImageId(patient_id + "_" + UUID.randomUUID().toString());
                            image.setImagePath(imagePath);
                            image.setSelected(false);
                            photoPaths.add(image);
                        }
                    }
                    selectedRecordsAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @OnClick(R.id.uploadButton)
    public void onViewClicked() {
        if (photoPaths.size() > 0 && photoPaths != null) {
            CommonMethods.showToast(mContext, "Upload Successfully");

            int selectedCount = 0;
            ArrayList<Integer> selectedInvestigationIds = new ArrayList<>();

            for (InvestigationData dataObject : investigation) {
                if (dataObject.isSelected() && !dataObject.isUploaded()) {
                    selectedInvestigationIds.add(dataObject.getId());
                    dataObject.setUploaded(dataObject.isSelected());
                    Images images = new Images();
                    images.setImageArray(photoPaths);
                    dataObject.setPhotos(photoPaths);
//                    appDBHelper.updateInvestigationData(dataObject.getId(), dataObject.isUploaded(), new Gson().toJson(images));
                }
                if (dataObject.isSelected())
                    selectedCount += 1;
            }

            SelectedDocModel selectedDocModel = new SelectedDocModel();
            selectedDocModel.setSelectedDocPaths(photoPaths);
            selectedDocModel.setSelectedInvestigation(selectedInvestigationIds);

            Log.d("JSON", new Gson().toJson(selectedDocModel));

            Intent intent = new Intent();
            intent.putExtra(MyRescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_DATA, investigation);
//            intent.putExtra(FilePickerConst.KEY_SELECTED_MEDIA, photoPaths);
            setResult(RESULT_OK, intent);

            finish();

        } else {
            CommonMethods.showToast(mContext, "Please select at least one document");
        }
    }
}
