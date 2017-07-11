package com.myrescribe.ui.activities;

import android.Manifest;
import android.content.Context;
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

import com.myrescribe.R;
import com.myrescribe.adapters.SelectedImageAdapter;
import com.myrescribe.adapters.UploadedImageAdapter;
import com.myrescribe.model.investigation.DataObject;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import droidninja.filepicker.FilePickerBuilder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

//@RuntimePermissions
public class UploadedDocsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.uploadButton)
    Button uploadButton;

    private static final int MAX_ATTACHMENT_COUNT = 10;
    private Context mContext;
    private UploadedImageAdapter selectedImageAdapter;
    private ArrayList<DataObject> investigation;
    private ArrayList<String> photoPaths = new ArrayList<>();

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

        investigation = (ArrayList<DataObject>) getIntent().getSerializableExtra(MyRescribeConstants.INVESTIGATION_DATA);

        for (DataObject dataObject : investigation)
            photoPaths.addAll(dataObject.getPhotos());

        selectedImageAdapter = new UploadedImageAdapter(mContext, photoPaths);
        recyclerView.setAdapter(selectedImageAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        recyclerView.setLayoutManager(layoutManager);

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_docs_menu, menu);//Menu Resource, Menu
        return true;
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
        UploadedDocsActivityPermissionsDispatcher.onRequestPermissionsResult(UploadedDocsActivity.this, requestCode, grantResults);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_docs:
                UploadedDocsActivityPermissionsDispatcher.onPickPhotoWithCheck(UploadedDocsActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}
