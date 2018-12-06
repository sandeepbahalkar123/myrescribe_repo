package com.rescribe.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.adapters.myrecords.RecordsGroupAdapter;
import com.rescribe.adapters.myrecords.RecordsGroupImageAdapter;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.database.MyRecordsData;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.UploadStatus;
import com.rescribe.model.investigation.Image;
import com.rescribe.model.my_records.Group;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.services.add_record_upload_Service.AddRecordService;
import com.rescribe.singleton.Device;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.NetworkUtil;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rescribe.services.add_record_upload_Service.AddRecordService.ATTATCHMENT_DOC_UPLOAD;
import static com.rescribe.services.add_record_upload_Service.AddRecordService.RESULT;
import static com.rescribe.services.add_record_upload_Service.AddRecordService.UPLOAD_ID;

public class SelectedRecordsGroupActivity extends AppCompatActivity implements RecordsGroupImageAdapter.ItemListener {

    public static final String CASE_ENCOUNTER = "Case Encounter";
    public static final String INVESTIGATIONS = "Investigations";
    public static final String PRESCRIPTIONS = "Prescriptions";
    public static final String BILL_INVOICES = "Bill Invoices";
    public static final String OTHERS = "Others";
    private static final String IMAGEDUPLOADID = "ImagedUploadId";
    public static final String FILELIST = "FileList";
    ArrayList<Image> imageArrayList;
    ArrayList<Group> groups = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.uploadButton)
    Button uploadButton;
    private RecordsGroupAdapter mAdapter;

    // Caption Dialog
    private AlertDialog alertDialog;
    private ListView captionListView;
    final String childCaptions[] = {"Blood Sugar", "CT Scan", "Haemogram", "Lipid Profile", "Liver Profile", "Serum Bilirubin", "Serum Creatinine", "Sonography", "Sputum",
            "Thyroid", "Urine Culture", "Urine Routine", "X-Ray", "Others"};
    private String visitDate;
    private int docId;
    private AppDBHelper appDBHelper;
    private String authorizationString;
    private Device device;
    private String Url;
    private String patientId;

    private int opdId;
    ArrayList<UploadStatus> uploadDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_records_group);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        imageArrayList = getIntent().getParcelableArrayListExtra(RescribeConstants.DOCUMENTS);

        opdId = getIntent().getIntExtra(RescribeConstants.OPD_ID, 0);

        visitDate = getIntent().getStringExtra(RescribeConstants.VISIT_DATE);

        docId = getIntent().getIntExtra(RescribeConstants.DOCTORS_ID, 0);

        patientId = RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, SelectedRecordsGroupActivity.this);

        boolean isUploading = getIntent().getBooleanExtra(RescribeConstants.UPLOADING_STATUS, false);
        if (isUploading)
            uploadButton.setEnabled(false);

        appDBHelper = new AppDBHelper(SelectedRecordsGroupActivity.this);

        createGroup();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.title_activity_report_selection));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RecyclerView.ItemAnimator animator = recyclerview.getItemAnimator();
        if (animator instanceof SimpleItemAnimator)
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(mLayoutManager);

        mAdapter = new RecordsGroupAdapter(groups, SelectedRecordsGroupActivity.this);
        recyclerview.setAdapter(mAdapter);

        // Caption Dialog
        alertDialog = new AlertDialog.Builder(SelectedRecordsGroupActivity.this).create();
        View convertView = getLayoutInflater().inflate(R.layout.child_caption, null);
        alertDialog.setView(convertView);

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        captionListView = (ListView) convertView.findViewById(R.id.captionListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, childCaptions);
        captionListView.setAdapter(adapter);

        // Uploading

        device = Device.getInstance(SelectedRecordsGroupActivity.this);

        Url = Config.BASE_URL + Config.MY_RECORDS_UPLOAD;
//        Url = "http://192.168.0.115:8000/" + Config.MY_RECORDS_UPLOAD;

        authorizationString = RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.AUTHTOKEN, SelectedRecordsGroupActivity.this);

    }

    private void createGroup() {

        ArrayList<Image> caseEncounter = new ArrayList<>();
        ArrayList<Image> investigations = new ArrayList<>();
        ArrayList<Image> prescription = new ArrayList<>();
        ArrayList<Image> billInvoices = new ArrayList<>();
        ArrayList<Image> others = new ArrayList<>();

        for (int index = 0; index < imageArrayList.size(); index++) {
            Image image = imageArrayList.get(index);
            String caption = image.getParentCaption();
            switch (caption) {
                case CASE_ENCOUNTER:
                    caseEncounter.add(image);
                    image.setChildCaption(CASE_ENCOUNTER + "_" + caseEncounter.size());
                    break;
                case INVESTIGATIONS:
                    investigations.add(image);
                    break;
                case PRESCRIPTIONS:
                    prescription.add(image);
                    image.setChildCaption(PRESCRIPTIONS + "_" + prescription.size());
                    break;
                case BILL_INVOICES:
                    billInvoices.add(image);
                    image.setChildCaption(BILL_INVOICES + "_" + billInvoices.size());
                    break;
                default:
                    others.add(image);
                    image.setChildCaption(OTHERS + "_" + others.size());
                    image.setParentCaption(OTHERS);
                    break;
            }
        }

        if (!caseEncounter.isEmpty()) {
            Group group = new Group();
            group.setImages(caseEncounter);
            group.setGroupname(CASE_ENCOUNTER);
            groups.add(group);
        }

        if (!investigations.isEmpty()) {
            Group group = new Group();
            group.setImages(investigations);
            group.setGroupname(INVESTIGATIONS);
            groups.add(group);
        }

        if (!prescription.isEmpty()) {
            Group group = new Group();
            group.setImages(prescription);
            group.setGroupname(PRESCRIPTIONS);
            groups.add(group);
        }

        if (!billInvoices.isEmpty()) {
            Group group = new Group();
            group.setImages(billInvoices);
            group.setGroupname(BILL_INVOICES);
            groups.add(group);
        }

        if (!others.isEmpty()) {
            Group group = new Group();
            group.setImages(others);
            group.setGroupname(OTHERS);
            groups.add(group);
        }
    }

    @OnClick(R.id.uploadButton)
    public void onViewClicked() {
        if (groups.isEmpty()) {
            CommonMethods.showToast(SelectedRecordsGroupActivity.this, getResources().getString(R.string.select_report));
        } else {
            if (NetworkUtil.isInternetAvailable(SelectedRecordsGroupActivity.this)) {
                uploadButton.setEnabled(false);


                uploadDataList = new ArrayList<>();

                for (int parentIndex = 0; parentIndex < groups.size(); parentIndex++) {

                    List<Image> images = groups.get(parentIndex).getImages();

                    for (int childIndex = 0; childIndex < images.size(); childIndex++) {
                        // uploadImage(parentIndex + "_" + childIndex, images.get(childIndex));


                        Image image = images.get(childIndex);

                        if (image.getChildCaption() == null || image.getChildCaption().equals(""))
                            image.setChildCaption(OTHERS);

                        String childCaptionName;
                        if (image.getParentCaption().equals(INVESTIGATIONS))
                            childCaptionName = image.getChildCaption();
                        else
                            childCaptionName = image.getParentCaption();

                        String visitDateToPass = CommonMethods.getFormattedDate(visitDate, RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.YYYY_MM_DD);

                        HashMap<String, String> headers = new HashMap<>();
                        headers.put(RescribeConstants.AUTHORIZATION_TOKEN, authorizationString);
                        headers.put(RescribeConstants.DEVICEID, device.getDeviceId());
                        headers.put(RescribeConstants.OS, device.getOS());
                        headers.put(RescribeConstants.OSVERSION, device.getOSVersion());
                        headers.put(RescribeConstants.DEVICE_TYPE, device.getDeviceType());

                        headers.put("patientId", patientId);
                        headers.put("docId", String.valueOf(docId));
                        headers.put("visitDate", visitDateToPass);
                        headers.put("imageId", image.getImageId());
                        headers.put("parentCaptionName", image.getParentCaption());
                        headers.put("childCaptionName", childCaptionName);

                        appDBHelper.insertMyRecordsData(parentIndex + "_" + childIndex, RescribeConstants.UPLOADING, new Gson().toJson(image), docId, opdId, visitDate);
                        UploadStatus uploadStatus = new UploadStatus(parentIndex + "_" + childIndex, RescribeConstants.UPLOADING, new Gson().toJson(image), docId, opdId, visitDate, headers);
                        uploadDataList.add(uploadStatus);

                    }


                }

                uploadImage(uploadDataList);


            } else
                CommonMethods.showToast(SelectedRecordsGroupActivity.this, getResources().getString(R.string.internet));
        }
    }


    public void uploadImage(ArrayList<UploadStatus> images) {

        Intent i = new Intent(SelectedRecordsGroupActivity.this, AddRecordService.class);
        i.putParcelableArrayListExtra(FILELIST, images);
        i.putExtra("URL", Url);
        ContextCompat.startForegroundService(SelectedRecordsGroupActivity.this, i);

    }

    // Uploading

    @Override
    public void onBackPressed() {
        if (!uploadButton.isEnabled()) {
            Intent intent = new Intent(SelectedRecordsGroupActivity.this, HomePageActivity.class);
            intent.putExtra(RescribeConstants.ALERT, false);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        super.onBackPressed();
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals(ATTATCHMENT_DOC_UPLOAD)) {

                    CommonBaseModelContainer commonBaseModelContainer = intent.getParcelableExtra(RESULT);
                    String upload_id = intent.getStringExtra(UPLOAD_ID);

                    if (commonBaseModelContainer.getCommonRespose().isSuccess()) {

                        String pos[] = upload_id.split("_");
                        int finalI = Integer.parseInt(pos[0]);
                        int finalJ = Integer.parseInt(pos[1]);

                        groups.get(finalI).getImages().get(finalJ).setUploading(RescribeConstants.COMPLETED);
                        mAdapter.notifyItemChanged(finalI);

                        navigate();
                    } else {


                        String pos[] = upload_id.split("_");
                        int finalI = Integer.parseInt(pos[0]);
                        int finalJ = Integer.parseInt(pos[1]);

                        groups.get(finalI).getImages().get(finalJ).setUploading(RescribeConstants.FAILED);
                        mAdapter.notifyItemChanged(finalI);

                        navigate();
                    }


                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ATTATCHMENT_DOC_UPLOAD);
        registerReceiver(receiver, intentFilter);
    }

//    private UploadServiceBroadcastReceiver broadcastReceiver = new UploadServiceBroadcastReceiver() {
//        @Override
//        public void onProgress(Context context, UploadInfo uploadInfo) {
//            String pos[] = uploadInfo.getUploadId().split("_");
//            int finalI = Integer.parseInt(pos[0]);
//            int finalJ = Integer.parseInt(pos[1]);
//
//            if (groups.get(finalI).getImages().get(finalJ).isUploading() != RescribeConstants.UPLOADING) {
//                groups.get(finalI).getImages().get(finalJ).setUploading(RescribeConstants.UPLOADING);
//                uploadButton.setEnabled(false);
//                mAdapter.notifyDataSetChanged();
//            }
//
//            CommonMethods.Log(IMAGEDUPLOADID, uploadInfo.getUploadId() + " onProgress " + uploadInfo.getProgressPercent());
//        }
//
//        @Override
//        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
//
//            appDBHelper.updateMyRecordsData(uploadInfo.getUploadId(), RescribeConstants.FAILED);
//
//            String pos[] = uploadInfo.getUploadId().split("_");
//            int finalI = Integer.parseInt(pos[0]);
//            int finalJ = Integer.parseInt(pos[1]);
//
//            groups.get(finalI).getImages().get(finalJ).setUploading(RescribeConstants.FAILED);
//            mAdapter.notifyItemChanged(finalI);
//            CommonMethods.Log(IMAGEDUPLOADID, uploadInfo.getUploadId() + " onError");
//
//            navigate();
//        }
//
//        @Override
//        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
//
//            appDBHelper.updateMyRecordsData(uploadInfo.getUploadId(), RescribeConstants.COMPLETED);
//
//            String pos[] = uploadInfo.getUploadId().split("_");
//            int finalI = Integer.parseInt(pos[0]);
//            int finalJ = Integer.parseInt(pos[1]);
//
//            groups.get(finalI).getImages().get(finalJ).setUploading(RescribeConstants.COMPLETED);
//            mAdapter.notifyItemChanged(finalI);
//
//            CommonMethods.Log(IMAGEDUPLOADID, uploadInfo.getUploadId() + " onCompleted " + serverResponse.getBodyAsString());
//
//            navigate();
//        }
//
//        @Override
//        public void onCancelled(Context context, UploadInfo uploadInfo) {
//            CommonMethods.Log(IMAGEDUPLOADID, uploadInfo.getUploadId() + " onCancelled");
//        }
//    };

    private void navigate() {

        // Navigate
        MyRecordsData myRecordsData = appDBHelper.getMyRecordsData();
        int completeCount = 0;
        for (Image image : myRecordsData.getImageArrayList()) {
            if (image.isUploading() == RescribeConstants.COMPLETED)
                completeCount++;
        }
        if (completeCount == myRecordsData.getImageArrayList().size()) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(SelectedRecordsGroupActivity.this, MyRecordsActivity.class);
                    intent.putExtra(RescribeConstants.ALERT, false);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }, 2000);
        }
        // End Navigate
    }

    @Override
    public void onRemoveClick(int mainPosition, int position) {
        groups.get(mainPosition).getImages().remove(position);
        if (groups.get(mainPosition).getImages().isEmpty()) {
            groups.remove(mainPosition);
            mAdapter.notifyDataSetChanged();
        } else
            mAdapter.notifyItemChanged(mainPosition);
    }

    @Override
    public void onAddCaptionClick(final int mainPosition, final int position) {
        captionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                groups.get(mainPosition).getImages().get(position).setChildCaption(childCaptions[pos]);
                mAdapter.notifyItemChanged(mainPosition);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void uploadImage(String uploadId, Image image) {
        ArrayList<UploadStatus> images = new ArrayList<>();

        if (image.getChildCaption() == null || image.getChildCaption().equals(""))
            image.setChildCaption(OTHERS);

        String childCaptionName;
        if (image.getParentCaption().equals(INVESTIGATIONS))
            childCaptionName = image.getChildCaption();
        else
            childCaptionName = image.getParentCaption();

        String visitDateToPass = CommonMethods.getFormattedDate(visitDate, RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.YYYY_MM_DD);

        HashMap<String, String> headers = new HashMap<>();
        headers.put(RescribeConstants.AUTHORIZATION_TOKEN, authorizationString);
        headers.put(RescribeConstants.DEVICEID, device.getDeviceId());
        headers.put(RescribeConstants.OS, device.getOS());
        headers.put(RescribeConstants.OSVERSION, device.getOSVersion());
        headers.put(RescribeConstants.DEVICE_TYPE, device.getDeviceType());

        headers.put("patientId", patientId);
        headers.put("docId", String.valueOf(docId));
        headers.put("visitDate", visitDateToPass);
        headers.put("imageId", image.getImageId());
        headers.put("parentCaptionName", image.getParentCaption());
        headers.put("childCaptionName", childCaptionName);

        appDBHelper.insertMyRecordsData(uploadId, RescribeConstants.UPLOADING, new Gson().toJson(image), docId, opdId, visitDate);
        UploadStatus uploadStatus = new UploadStatus(uploadId, RescribeConstants.UPLOADING, new Gson().toJson(image), docId, opdId, visitDate, headers);
        images.add(uploadStatus);

        Intent i = new Intent(SelectedRecordsGroupActivity.this, AddRecordService.class);
        i.putParcelableArrayListExtra(FILELIST, images);
        i.putExtra("URL", Url);
        ContextCompat.startForegroundService(SelectedRecordsGroupActivity.this, i);

    }
}
