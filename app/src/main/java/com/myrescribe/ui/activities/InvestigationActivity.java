package com.myrescribe.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.myrescribe.R;
import com.myrescribe.adapters.InvestigationViewAdapter;
import com.myrescribe.model.DataObject;
import com.myrescribe.util.CommonMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import droidninja.filepicker.FilePickerConst;

public class InvestigationActivity extends AppCompatActivity implements InvestigationViewAdapter.CheckedClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;
    private InvestigationViewAdapter mAdapter;
    private Context mContext;
    private ArrayList<DataObject> investigation = new ArrayList<DataObject>();

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
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new InvestigationViewAdapter(mContext, getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    private ArrayList<DataObject> getDataSet() {
        investigation.add(new DataObject("CT Scan", false, new ArrayList<String>()));
        investigation.add(new DataObject("Lipid", false, new ArrayList<String>()));
        investigation.add(new DataObject("Liver Profile", false, new ArrayList<String>()));
        return investigation;
    }

    @Override
    public void onCheckedClick(int position) {
        Intent intent = new Intent(mContext, SeletedDocsActivity.class);
        intent.putExtra(FilePickerConst.MEDIA_ID, position);
        startActivityForResult(intent, FilePickerConst.REQUEST_CODE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
            int id = data.getIntExtra(FilePickerConst.MEDIA_ID, 0);

            if (resultCode == Activity.RESULT_OK) {
                ArrayList<String> photu = new ArrayList<>();
                photu.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                investigation.get(id).setPhotos(photu);
            } else if (resultCode == RESULT_CANCELED) {
                investigation.get(id).setUploaded(false);
                mAdapter.notifyItemChanged(id);
            }
            CommonMethods.Log("SELECTED_PHOTOS " + id, investigation.toString());
        }
    }
}
