package com.myrescribe.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.myrescribe.R;
import com.myrescribe.adapters.InvestigationViewAdapter;
import com.myrescribe.helpers.database.AppDBHelper;
import com.myrescribe.model.investigation.DataObject;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerConst;

public class InvestigationActivity extends AppCompatActivity implements InvestigationViewAdapter.CheckedClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.selectDocsButton)
    Button selectDocsButton;

    private LinearLayoutManager mLayoutManager;
    private InvestigationViewAdapter mAdapter;
    private Context mContext;
    private ArrayList<DataObject> investigation = new ArrayList<DataObject>();
    private ArrayList<DataObject> investigationTemp = new ArrayList<DataObject>();
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
                onBackPressed();
            }
        });

        mContext = InvestigationActivity.this;
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        getDataSet();

        appDBHelper = new AppDBHelper(mContext);
        for (DataObject dataObject : investigation) {
            appDBHelper.insertInvestigationData(dataObject.getId(), dataObject.getTitle(), dataObject.isUploaded());
        }

        for (int i = 0; i < investigation.size(); i++) {
            boolean status = appDBHelper.getInvestigationData(investigation.get(i).getId()).isUploaded();
            if (!status) {
                DataObject dataObject = new DataObject(investigation.get(i).getId(), investigation.get(i).getTitle(), investigation.get(i).isSelected(), investigation.get(i).isUploaded(), new ArrayList<String>());
                investigationTemp.add(dataObject);
            }

        }

        mAdapter = new InvestigationViewAdapter(mContext, investigationTemp);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    private void getDataSet() {
        investigation.add(new DataObject(1, "CT Scan", false, false, new ArrayList<String>()));
        investigation.add(new DataObject(2, "Lipid", false, false, new ArrayList<String>()));
        investigation.add(new DataObject(3, "Liver Profile", false, false, new ArrayList<String>()));
        investigation.add(new DataObject(4, "X Ray", false, false, new ArrayList<String>()));
    }

    @Override
    public void onCheckedClick(int position) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
            /*int id = data.getIntExtra(FilePickerConst.MEDIA_ID, 0);
            ArrayList<String> photu = new ArrayList<>();
            photu.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
            investigation.get(id).setPhotos(photu);
            appDBHelper.updateInvestigationData(investigation.get(id).getId(), investigation.get(id).getTitle(), investigation.get(id).isSelected());

            if (resultCode == RESULT_CANCELED) {
                investigation.get(id).setSelected(false);
                mAdapter.notifyItemChanged(id);
            }
            CommonMethods.Log("SELECTED_PHOTOS " + id, investigation.toString());*/
            if (resultCode == RESULT_OK) {
                investigationTemp.clear();
                ArrayList<DataObject> invest = (ArrayList<DataObject>) data.getSerializableExtra(MyRescribeConstants.INVESTIGATION_DATA);
                investigationTemp.addAll(invest);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @OnClick(R.id.selectDocsButton)
    public void onViewClicked() {
        boolean flag = false;
        boolean selected = false;
        for (int i = 0; i < investigationTemp.size(); i++) {
            if (!investigationTemp.get(i).isUploaded() && investigationTemp.get(i).isSelected())
                selected = true;
            if (investigationTemp.get(i).isUploaded() && !selected)
                flag = true;
        }

        if (flag)
            CommonMethods.showToast(mContext, "Please select non Uploaded Document.");
        else {
            if (selected) {
                Intent intent = new Intent(mContext, SeletedDocsActivity.class);
                intent.putExtra(MyRescribeConstants.INVESTIGATION_DATA, investigationTemp);
                startActivityForResult(intent, FilePickerConst.REQUEST_CODE_PHOTO);
            } else
                CommonMethods.showToast(mContext, "Please select at least one Document.");
        }

    }
}
