package com.myrescribe.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.myrescribe.R;
import com.myrescribe.adapters.UploadedImageAdapter;
import com.myrescribe.model.investigation.DataObject;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        uploadedImageAdapter = new UploadedImageAdapter(mContext, photoPaths);
        recyclerView.setAdapter(uploadedImageAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        recyclerView.setLayoutManager(layoutManager);

    }
}
