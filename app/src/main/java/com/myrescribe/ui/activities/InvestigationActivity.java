package com.myrescribe.ui.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.myrescribe.R;
import com.myrescribe.adapters.InvestigationViewAdapter;
import com.myrescribe.helpers.database.AppDBHelper;
import com.myrescribe.model.investigation.DataObject;
import com.myrescribe.model.investigation.Image;
import com.myrescribe.model.investigation.Images;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerConst;

public class InvestigationActivity extends AppCompatActivity implements InvestigationViewAdapter.CheckedClickListener {

    private static final int UPLOADED_DOCS = 121;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.selectDocsButton)
    Button selectDocsButton;
    @BindView(R.id.selectUploadedButton)
    Button selectUploadedButton;
    @BindView(R.id.gmailButton)
    Button gmailButton;
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
            Images images = new Images();
            images.setImageArray(dataObject.getPhotos());
            appDBHelper.insertInvestigationData(dataObject.getId(), dataObject.getTitle(), dataObject.isUploaded(), new Gson().toJson(images));
        }

        int isAlreadyUploadedButtonVisible = View.GONE;

        for (int i = 0; i < investigation.size(); i++) {
            DataObject data = appDBHelper.getInvestigationData(investigation.get(i).getId());
            boolean status = data.isUploaded();
            ArrayList<Image> imageArray = data.getPhotos();
            if (!status) {
                DataObject dataObject = new DataObject(investigation.get(i).getId(), investigation.get(i).getTitle(), investigation.get(i).isSelected(), investigation.get(i).isUploaded(), imageArray);
                investigationTemp.add(dataObject);
            } else {
                isAlreadyUploadedButtonVisible = View.VISIBLE;
                investigation.get(i).setPhotos(data.getPhotos());
            }
        }

        buttonManage(isAlreadyUploadedButtonVisible);

        mAdapter = new InvestigationViewAdapter(mContext, investigationTemp);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void buttonManage(int isAlreadyUploadedButtonVisible) {
        if (isAlreadyUploadedButtonVisible == View.VISIBLE)
            selectDocsButton.setText(getResources().getString(R.string.new_document));
        else selectDocsButton.setText(getResources().getString(R.string.upload));
        selectUploadedButton.setVisibility(isAlreadyUploadedButtonVisible);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(InvestigationActivity.this, HomePageActivity.class);
        intent.putExtra(MyRescribeConstants.ALERT, false);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
    }

    private void getDataSet() {
        investigation.add(new DataObject(1, "CT Scan", false, false, new ArrayList<Image>()));
        investigation.add(new DataObject(2, "Lipid", false, false, new ArrayList<Image>()));
        investigation.add(new DataObject(3, "Liver Profile", false, false, new ArrayList<Image>()));
        investigation.add(new DataObject(4, "X Ray", false, false, new ArrayList<Image>()));
    }

    @Override
    public void onCheckedClick(int position) {
        buttonEnable();
    }

    private void buttonEnable() {
        boolean uploadButton = false;
        for (DataObject dataObject : investigationTemp) {
            if (dataObject.isSelected() && !dataObject.isUploaded()) {
                uploadButton = true;
                break;
            }
        }

        selectDocsButton.setEnabled(uploadButton);
        selectUploadedButton.setEnabled(uploadButton);
        gmailButton.setEnabled(uploadButton);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO || requestCode == UPLOADED_DOCS) {
            if (resultCode == RESULT_OK) {
                investigationTemp.clear();
                ArrayList<DataObject> invest = (ArrayList<DataObject>) data.getSerializableExtra(MyRescribeConstants.INVESTIGATION_DATA);
                changeOriginalData(invest);
                investigationTemp.addAll(invest);
                mAdapter.notifyDataSetChanged();
                buttonEnable();
                buttonManage(View.VISIBLE);
            }
        }
    }

    private void changeOriginalData(ArrayList<DataObject> invest) {
        for (int i = 0; i < investigation.size(); i++) {
            for (DataObject objectTemp : invest) {
                if (investigation.get(i).getId() == objectTemp.getId()) {
                    investigation.set(i, objectTemp);
                }
            }
        }
    }

    @OnClick({R.id.selectUploadedButton, R.id.gmailButton, R.id.selectDocsButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.selectDocsButton:
                boolean selected = false;
                for (int i = 0; i < investigationTemp.size(); i++) {
                    if (!investigationTemp.get(i).isUploaded() && investigationTemp.get(i).isSelected())
                        selected = true;
                }
                if (selected) {
                    Intent intent = new Intent(mContext, SeletedDocsActivity.class);
                    intent.putExtra(MyRescribeConstants.INVESTIGATION_DATA, investigationTemp);
                    startActivityForResult(intent, FilePickerConst.REQUEST_CODE_PHOTO);
                } else
                    CommonMethods.showToast(mContext, "Please select at least one Document.");
                break;
            case R.id.selectUploadedButton:
                Intent intent = new Intent(mContext, UploadedDocsActivity.class);
                intent.putExtra(MyRescribeConstants.INVESTIGATION_DATA, investigation);
                startActivityForResult(intent, UPLOADED_DOCS);
                break;
            case R.id.gmailButton:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("email", "dr.shah@gmail.com");
                clipboard.setPrimaryClip(clip);

                if (openApp("com.google.android.gm")) {
                    for (DataObject dataObject : investigationTemp) {
                        if (dataObject.isSelected()) {
                            dataObject.setUploaded(dataObject.isSelected());
                        }
                    }
                    changeOriginalData(investigationTemp);
                    buttonEnable();
                    buttonManage(View.VISIBLE);
                    Toast.makeText(InvestigationActivity.this, "dr.shah@gmail.com email Id copied.", Toast.LENGTH_SHORT).show();
                } else {
                    CommonMethods.showToast(mContext, "Gmail application not found");
                }
                break;
        }
    }

    public boolean openApp(String packageName) {
        PackageManager manager = getPackageManager();
        Intent i = manager.getLaunchIntentForPackage(packageName);
        if (i == null) {
            return false;
        }
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(i);
        return true;
    }
}
