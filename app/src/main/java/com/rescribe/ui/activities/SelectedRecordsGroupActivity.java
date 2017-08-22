package com.rescribe.ui.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.rescribe.R;
import com.rescribe.adapters.myrecords.RecordsGroupAdapter;
import com.rescribe.adapters.myrecords.RecordsGroupImageAdapter;
import com.rescribe.model.investigation.Image;
import com.rescribe.model.myrecords.Group;
import com.rescribe.preference.MyRescribePreferencesManager;
import com.rescribe.singleton.Device;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.MyRescribeConstants;
import com.rescribe.util.NetworkUtil;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectedRecordsGroupActivity extends AppCompatActivity implements RecordsGroupImageAdapter.ItemListener {

    public static final String CASE_ENCOUNTER = "Case Encounter";
    public static final String INVESTIGATIONS = "Investigations";
    public static final String PRESCRIPTIONS = "Prescriptions";
    public static final String BILL_INVOICES = "Bill Invoices";
    public static final String OTHERS = "Others";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_records_group);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        imageArrayList = getIntent().getParcelableArrayListExtra(MyRescribeConstants.DOCUMENTS);
        visitDate = getIntent().getStringExtra(MyRescribeConstants.VISIT_DATE);
        docId = getIntent().getIntExtra(MyRescribeConstants.DOCTORS_ID, 0);
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
//        alertDialog.setTitle("Captions");
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        captionListView = (ListView) convertView.findViewById(R.id.captionListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, childCaptions);
        captionListView.setAdapter(adapter);
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
                try {
                    Device device = Device.getInstance(SelectedRecordsGroupActivity.this);
                    String baseUrl = MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.SERVER_PATH, SelectedRecordsGroupActivity.this);
                    String authorizationString = MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.AUTHTOKEN, SelectedRecordsGroupActivity.this);

                    UploadNotificationConfig uploadNotificationConfig = new UploadNotificationConfig();
                    uploadNotificationConfig.setTitleForAllStatuses("Document Uploading");
                    uploadNotificationConfig.setIconColorForAllStatuses(Color.parseColor("#04abdf"));

                    uploadButton.setEnabled(false);

//                    String Url = "http://192.168.0.115:8000/api/upload/myRecords";

                    for (int i = 0; i < groups.size(); i++) {

                        List<Image> images = groups.get(i).getImages();

                        for (int j = 0; j < images.size(); j++) {

                            new MultipartUploadRequest(SelectedRecordsGroupActivity.this, i + "_" + j, baseUrl + Config.MY_RECORDS_UPLOAD)
//                            new MultipartUploadRequest(SelectedRecordsGroupActivity.this, i + "_" + j, Url)
                                    .setNotificationConfig(uploadNotificationConfig)
                                    .setMaxRetries(0)

                                    .addHeader(MyRescribeConstants.AUTHORIZATION_TOKEN, authorizationString)
                                    .addHeader(MyRescribeConstants.DEVICEID, device.getDeviceId())
                                    .addHeader(MyRescribeConstants.OS, device.getOS())
                                    .addHeader(MyRescribeConstants.OSVERSION, device.getOSVersion())
                                    .addHeader(MyRescribeConstants.DEVICE_TYPE, device.getDeviceType())

                                    .addHeader("docId", String.valueOf(docId))
                                    .addHeader("visitDate", visitDate)
                                    .addHeader("imageId", images.get(j).getImageId())
                                    .addHeader("parentCaptionName", groups.get(i).getGroupname())
                                    .addHeader("childCaptionName", images.get(j).getChildCaption())

                                    .addFileToUpload(images.get(j).getImagePath(), "myRecord")
//                                    .setDelegate(MainActivity.this)
                                    .startUpload();
                        }
                    }

                } catch (Exception exc) {
                    Log.e("AndroidUploadService", exc.getMessage(), exc);
                }
            } else
                CommonMethods.showToast(SelectedRecordsGroupActivity.this, getResources().getString(R.string.internet));
        }
    }

    // Uploading

    @Override
    protected void onResume() {
        super.onResume();
        broadcastReceiver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        broadcastReceiver.unregister(this);
    }

    private UploadServiceBroadcastReceiver broadcastReceiver = new UploadServiceBroadcastReceiver() {
        @Override
        public void onProgress(Context context, UploadInfo uploadInfo) {
            String pos[] = uploadInfo.getUploadId().split("_");
            int finalI = Integer.parseInt(pos[0]);
            int finalJ = Integer.parseInt(pos[1]);

            if (!groups.get(finalI).getImages().get(finalJ).isUploading()) {
                groups.get(finalI).getImages().get(finalJ).setUploading(true);
                uploadButton.setEnabled(false);
                mAdapter.notifyDataSetChanged();
            }

            Log.d("ImagedUploadId", uploadInfo.getUploadId() + " onProgress " + uploadInfo.getProgressPercent());
        }

        @Override
        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
            String pos[] = uploadInfo.getUploadId().split("_");
            int finalI = Integer.parseInt(pos[0]);
            int finalJ = Integer.parseInt(pos[1]);

            groups.get(finalI).getImages().get(finalJ).setUploading(false);
            mAdapter.notifyItemChanged(finalI);
            Log.d("ImagedUploadId", uploadInfo.getUploadId() + " onError");
        }

        @Override
        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
            String pos[] = uploadInfo.getUploadId().split("_");
            int finalI = Integer.parseInt(pos[0]);
            int finalJ = Integer.parseInt(pos[1]);

            groups.get(finalI).getImages().get(finalJ).setUploading(false);
            mAdapter.notifyItemChanged(finalI);
            Log.d("ImagedUploadId", uploadInfo.getUploadId() + " onCompleted");
        }

        @Override
        public void onCancelled(Context context, UploadInfo uploadInfo) {
            String pos[] = uploadInfo.getUploadId().split("_");
            int finalI = Integer.parseInt(pos[0]);
            int finalJ = Integer.parseInt(pos[1]);

            groups.get(finalI).getImages().get(finalJ).setUploading(false);
            mAdapter.notifyItemChanged(finalI);
            Log.d("ImagedUploadId", uploadInfo.getUploadId() + " onCancelled");
        }
    };

    @Override
    public void onRemoveClick(int mainPosition, int position) {
        groups.get(mainPosition).getImages().remove(position);
        if (groups.get(mainPosition).getImages().isEmpty()) {
            groups.remove(mainPosition);
            mAdapter.notifyDataSetChanged();
        } else
            mAdapter.notifyItemChanged(mainPosition);

//        if (groups.isEmpty())
//            uploadButton.setEnabled(false);
    }

    @Override
    public void onAddCaptionClick(final int mainPosition, final int position) {
        if (groups.get(mainPosition).getGroupname().equals(INVESTIGATIONS)) {
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
    }
}
