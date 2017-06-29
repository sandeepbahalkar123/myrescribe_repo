package com.myrescribe.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.myrescribe.R;
import com.myrescribe.adapters.ImageAdapter;
import com.myrescribe.helpers.database.AppDBHelper;
import com.myrescribe.model.investigation.DataObject;
import com.myrescribe.model.investigation.SelectedDocModel;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SeletedDocsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.uploadButton)
    Button uploadButton;

    private static final int MAX_ATTACHMENT_COUNT = 10;
    private Context mContext;
    private ArrayList<String> photoPaths = new ArrayList<>();
    //    private int media_id;
    private ImageAdapter imageAdapter;
    private ArrayList<DataObject> investigation;
    private AppDBHelper appDBHelper;

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

        mContext = SeletedDocsActivity.this;

        appDBHelper = new AppDBHelper(mContext);

//        media_id = getIntent().getIntExtra(FilePickerConst.MEDIA_ID, 0);
//        photoPaths = (ArrayList<String>) getIntent().getSerializableExtra(FilePickerConst.KEY_SELECTED_MEDIA);
        investigation = (ArrayList<DataObject>) getIntent().getSerializableExtra(MyRescribeConstants.INVESTIGATION_DATA);

            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
            layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
            recyclerView.setLayoutManager(layoutManager);

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
                SeletedDocsActivityPermissionsDispatcher.onPickPhotoWithCheck(this);
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
        else
            FilePickerBuilder.getInstance().setMaxCount(MAX_ATTACHMENT_COUNT)
                    .setSelectedFiles(photoPaths)
                    .setActivityTheme(R.style.AppTheme)
                    .enableVideoPicker(false)
                    .enableCameraSupport(true)
                    .showGifs(false)
                    .showFolderView(true)
                    .enableOrientation(true)
                    .pickPhoto(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SeletedDocsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
//            int id = data.getIntExtra(FilePickerConst.MEDIA_ID, 0);
            if (resultCode == Activity.RESULT_OK) {
                photoPaths = new ArrayList<>();
                photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                imageAdapter = new ImageAdapter(this, photoPaths);
                recyclerView.setAdapter(imageAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            } else if (resultCode == RESULT_CANCELED) {
//                media_id = id;
            }
        }
    }

    @OnClick(R.id.uploadButton)
    public void onViewClicked() {
        if (photoPaths.size() > 0 && photoPaths != null) {
            CommonMethods.showToast(mContext, "Upload Successfully");

            int selectedCount = 0;
            ArrayList<Integer> selectedInvestigationIds = new ArrayList<>();

            for (DataObject dataObject : investigation) {
                if (dataObject.isSelected() && !dataObject.isUploaded()) {
                    selectedInvestigationIds.add(dataObject.getId());
                    dataObject.setUploaded(dataObject.isSelected());
                    appDBHelper.updateInvestigationData(dataObject.getId(), dataObject.getTitle(), dataObject.isSelected());
                }
                if (dataObject.isSelected())
                    selectedCount +=1;
            }

            SelectedDocModel selectedDocModel = new SelectedDocModel();
            selectedDocModel.setSelectedDocPaths(photoPaths);
            selectedDocModel.setSelectedInvestigation(selectedInvestigationIds);

            Log.d("JSON", new Gson().toJson(selectedDocModel));

            if (selectedCount == investigation.size()){
                Intent intent = new Intent(this, HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else {
                Intent intent = new Intent();
                intent.putExtra(MyRescribeConstants.INVESTIGATION_DATA, investigation);
                intent.putExtra(FilePickerConst.KEY_SELECTED_MEDIA, photoPaths);
                setResult(RESULT_OK, intent);
            }
            finish();

        } else {
            CommonMethods.showToast(mContext, "Please select at least one document");
        }
    }
}
