package com.myrescribe.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.myrescribe.R;
import com.myrescribe.adapters.UploadedImageAdapter;
import com.myrescribe.helpers.database.AppDBHelper;
import com.myrescribe.model.investigation.DataObject;
import com.myrescribe.model.investigation.Image;
import com.myrescribe.model.investigation.Images;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadedDocsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.uploadButton)
    Button uploadButton;

    private Context mContext;
    private UploadedImageAdapter uploadedImageAdapter;
    private ArrayList<DataObject> investigation;
//    private Set<Image> photoSet = new HashSet<>();
    private ArrayList<Image> photoPaths = new ArrayList<>();
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

        mContext = UploadedDocsActivity.this;
        appDBHelper = new AppDBHelper(mContext);

        investigation = (ArrayList<DataObject>) getIntent().getSerializableExtra(MyRescribeConstants.INVESTIGATION_DATA);

        for (DataObject dataObject : investigation) {
           /* photoSet.addAll(dataObject.getPhotos());*/
            photoPaths.addAll(dataObject.getPhotos());
        }

        uploadedImageAdapter = new UploadedImageAdapter(mContext, photoPaths);
        recyclerView.setAdapter(uploadedImageAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        recyclerView.setLayoutManager(layoutManager);

    }

    @OnClick(R.id.uploadButton)
    public void onViewClicked() {

        int selectedCount = 0;
        int selectedImageCount = 0;
        ArrayList<Image> photos = new ArrayList<>();

        for (Image image : photoPaths) {
            if (image.isSelected()) {
                photos.add(image);
                selectedImageCount++;
            }
        }

        // Update server status with image id

        if (selectedImageCount > 0) {
            for (DataObject dataObject : investigation) {
                if (dataObject.isSelected() && !dataObject.isUploaded()) {
                    dataObject.setUploaded(dataObject.isSelected());
                    appDBHelper.updateInvestigationData(dataObject.getId(), dataObject.isSelected(), "");
                }

                if (dataObject.isSelected())
                    selectedCount += 1;
            }

            if (selectedCount == investigation.size()) {
                Intent intent = new Intent(this, HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent();
                intent.putExtra(MyRescribeConstants.INVESTIGATION_DATA, investigation);
                setResult(RESULT_OK, intent);
            }
            finish();

        } else {
            CommonMethods.showToast(mContext, "Please select at least one document");
        }
    }
}
