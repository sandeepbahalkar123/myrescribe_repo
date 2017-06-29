package com.myrescribe.ui.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.myrescribe.R;
import com.myrescribe.adapters.InvestigationViewAdapter;
import com.myrescribe.helpers.database.AppDBHelper;
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
    private AppDBHelper appDBHelper;

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
                finish();
            }
        });

        mContext = InvestigationActivity.this;
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ArrayList<DataObject> data = getDataSet();

        appDBHelper = new AppDBHelper(mContext);
        for (DataObject dataObject:data){
            appDBHelper.insertInvestigationData(dataObject.getId(), dataObject.getTitle(), dataObject.isUploaded());
        }

        for (DataObject dataObject:data){
            boolean status = appDBHelper.getInvestigationData(dataObject.getId()).isUploaded();
            dataObject.setUploaded(status);
        }

        mAdapter = new InvestigationViewAdapter(mContext, data);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    private ArrayList<DataObject> getDataSet() {
        investigation.add(new DataObject(1, "CT Scan", false, new ArrayList<String>()));
        investigation.add(new DataObject(2, "Lipid", false, new ArrayList<String>()));
        investigation.add(new DataObject(3, "Liver Profile", false, new ArrayList<String>()));
        return investigation;
    }

    @Override
    public void onCheckedClick(int position) {
        Intent intent = new Intent(mContext, SeletedDocsActivity.class);
        intent.putExtra(FilePickerConst.MEDIA_ID, position);
        intent.putExtra(FilePickerConst.KEY_SELECTED_MEDIA, investigation.get(position).getPhotos());
        startActivityForResult(intent, FilePickerConst.REQUEST_CODE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
            int id = data.getIntExtra(FilePickerConst.MEDIA_ID, 0);

            ArrayList<String> photu = new ArrayList<>();
            photu.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
            investigation.get(id).setPhotos(photu);
            appDBHelper.updateInvestigationData(investigation.get(id).getId(), investigation.get(id).getTitle(), investigation.get(id).isUploaded());

            if (resultCode == RESULT_CANCELED) {
                investigation.get(id).setUploaded(false);
                mAdapter.notifyItemChanged(id);
            }
            CommonMethods.Log("SELECTED_PHOTOS " + id, investigation.toString());
        }
    }
}
