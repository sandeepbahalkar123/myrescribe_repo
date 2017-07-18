package com.myrescribe.ui.activities;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.gson.Gson;
import com.myrescribe.R;
import com.myrescribe.adapters.SelectedImageAdapter;
import com.myrescribe.helpers.database.AppDBHelper;
import com.myrescribe.model.investigation.DataObject;
import com.myrescribe.model.investigation.Image;
import com.myrescribe.model.investigation.Images;
import com.myrescribe.model.investigation.SelectedDocModel;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;
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
public class SeletedDocsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.uploadButton)
    Button uploadButton;

    private static final int MAX_ATTACHMENT_COUNT = 10;
    private Context mContext;
    private ArrayList<Image> photoPaths = new ArrayList<>();
    private int media_id = -1;
    private SelectedImageAdapter selectedImageAdapter;
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

        investigation = (ArrayList<DataObject>) getIntent().getSerializableExtra(MyRescribeConstants.INVESTIGATION_DATA);

        for (int i = 0; i < investigation.size(); i++) {
            if (investigation.get(i).isSelected() && !investigation.get(i).isUploaded() && investigation.get(i).getPhotos().size() > 0) {
                media_id = i;
                break;
            }
        }

        if (media_id == -1) {
            SeletedDocsActivityPermissionsDispatcher.onPickPhotoWithCheck(SeletedDocsActivity.this);
            photoPaths = new ArrayList<>();
        }else {
            photoPaths = investigation.get(media_id).getPhotos();
        }

        selectedImageAdapter = new SelectedImageAdapter(mContext, photoPaths);
        recyclerView.setAdapter(selectedImageAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
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
        SeletedDocsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA) == null)
            finish();
        else if (data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).size() == 0)
            finish(); else {
            if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
//            int id = data.getIntExtra(FilePickerConst.MEDIA_ID, 0);
                if (resultCode == Activity.RESULT_OK) {
                    photoPaths.clear();
                    for (String imagePath :data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA))
                        photoPaths.add(new Image(UUID.randomUUID().toString(), imagePath, false));
                    selectedImageAdapter.notifyDataSetChanged();
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

            for (DataObject dataObject : investigation) {
                if (dataObject.isSelected() && !dataObject.isUploaded()) {
                    selectedInvestigationIds.add(dataObject.getId());
                    dataObject.setUploaded(dataObject.isSelected());
                    Images images = new Images();
                    images.setImageArray(photoPaths);
                    dataObject.setPhotos(photoPaths);
                    appDBHelper.updateInvestigationData(dataObject.getId(), dataObject.isSelected(), new Gson().toJson(images));
                }
                if (dataObject.isSelected())
                    selectedCount += 1;
            }

            SelectedDocModel selectedDocModel = new SelectedDocModel();
            selectedDocModel.setSelectedDocPaths(photoPaths);
            selectedDocModel.setSelectedInvestigation(selectedInvestigationIds);

            Log.d("JSON", new Gson().toJson(selectedDocModel));

            if (selectedCount == investigation.size()) {
                Intent intent = new Intent(this, HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent();
                intent.putExtra(MyRescribeConstants.INVESTIGATION_DATA, investigation);
//                intent.putExtra(FilePickerConst.KEY_SELECTED_MEDIA, photoPaths);
                setResult(RESULT_OK, intent);
            }
            finish();

        } else {
            CommonMethods.showToast(mContext, "Please select at least one document");
        }
    }
}
