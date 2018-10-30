package com.rescribe.services.add_record_upload_Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.UploadStatus;
import com.rescribe.model.investigation.Image;
import com.rescribe.singleton.RescribeApplication;

import java.util.ArrayList;
import java.util.HashMap;

import static com.rescribe.ui.activities.SelectedRecordsGroupActivity.FILELIST;
import static com.rescribe.util.RescribeConstants.COMPLETED;
import static com.rescribe.util.RescribeConstants.FAILED;
import static com.rescribe.util.RescribeConstants.UPLOADING;

public class AddRecordService extends Service {
    public static final String ATTATCHMENT_DOC_UPLOAD = "com.rescribe.ATTATCHMENT_DOC_UPLOAD";

    public static final String RESULT = "result";
    private static final String ADD_RECORD_CHANNEL = "addrecord";
    private static final int ADD_RECORD_FOREGROUND_ID = 1634;
    public static final String UPLOAD_ID = "upload_id";
    private String BASE_URL;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;
    ArrayList<UploadStatus> uploadIdList = new ArrayList<>();
    private AppDBHelper appDBHelper;
    public int index = 0;
    ArrayList<String> documents = new ArrayList<String>() {{
        add("png");
        add("jpeg");
        add("jpg");
    }};

    @Override
    public void onCreate() {
        super.onCreate();
        if (appDBHelper == null)
            appDBHelper = new AppDBHelper(this);
        getNotificationManager();
        createChannel();
        Intent notificationIntent = new Intent();
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Create the channel object with the unique ID ADD_RECORD_CHANNEL
            NotificationChannel connectUploadChannel = new NotificationChannel(
                    ADD_RECORD_CHANNEL, "Add Records File Upload",
                    NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the channel's initial settings
            connectUploadChannel.setLightColor(Color.GREEN);
            connectUploadChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            // Submit the mBuilder channel object to the mBuilder manager
            mNotificationManager.createNotificationChannel(connectUploadChannel);
        }

        mBuilder = new NotificationCompat.Builder(this, ADD_RECORD_CHANNEL);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.logosmall);

        Notification notification = mBuilder.setContentTitle("Add Records Uploading")
                .setTicker("Uploading")
                .setSmallIcon(R.drawable.logosmall)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent).build();

        startForeground(ADD_RECORD_FOREGROUND_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        ArrayList<UploadStatus> uploadStatusArrayList = intent.getParcelableArrayListExtra(FILELIST);
        uploadIdList.addAll(uploadStatusArrayList);
        BASE_URL = intent.getStringExtra("URL");

        mBuilder.setContentText("Uploading " + uploadIdList.size() + (uploadIdList.size() == 1 ? " File" : " Files"));
        // Removes the progress bar
//                        .setProgress(connectUploads.size(), uploadInfo.getProgressPercent(), false);
        mNotificationManager.notify(ADD_RECORD_FOREGROUND_ID, mBuilder.build());
        Gson gson = new Gson();
        if (index == 0) {
            UploadStatus file = uploadIdList.get(index);

            String imageJson = file.getData();
            Image image = gson.fromJson(imageJson, Image.class);
            String path = image.getImagePath();

            imageUpload(path, uploadIdList.get(index).getHeaderMap(), file.getOpdId());
        }

        return Service.START_STICKY;
    }


    public void createChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Create the channel object with the unique ID CONNECT_CHANNEL
            NotificationChannel connectChannel = new NotificationChannel(
                    ADD_RECORD_CHANNEL, "Add Record",
                    NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the channel's initial settings
            connectChannel.setLightColor(Color.GREEN);
            connectChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            // Submit the mBuilder channel object to the mBuilder manager
            getNotificationManager().createNotificationChannel(connectChannel);
        }
    }


    public NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }


    private void imageUpload(String imagePath, HashMap<String, String> mapHeaders, int OpdId) {

        appDBHelper.updateMyRecordsData(uploadIdList.get(index).getUploadId(), UPLOADING);

        Log.e("imagePath122--", "-" + imagePath);
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response);
                        publishResults(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                String msg = "{\"common\":{\"success\":false,\"statusCode\":400,\"statusMessage\": \"Server Error \"} }";
                publishResults(msg);
            }
        });
        smr.setHeaders(mapHeaders);

        if (OpdId != 0)
            smr.addStringParam("opdId", String.valueOf(OpdId));
        smr.addFile("myRecord", imagePath);
        RescribeApplication.getInstance().addToRequestQueue(smr);

    }

    private void publishResults(String result) {
        Gson gson = new Gson();
        CommonBaseModelContainer common = gson.fromJson(result, CommonBaseModelContainer.class);
        Toast.makeText(this, common.getCommonRespose().getStatusMessage(), Toast.LENGTH_SHORT).show();

        if (common.getCommonRespose().isSuccess()) {


            mBuilder.setContentText("Uploading " + (uploadIdList.size() - index) + ((uploadIdList.size() - index) == 1 ? " File" : " Files"));
            // Removes the progress bar
//                        .setProgress(connectUploads.size(), uploadInfo.getProgressPercent(), false);
            mNotificationManager.notify(ADD_RECORD_FOREGROUND_ID, mBuilder.build());

            Log.e("index sucess", "" + index);
            appDBHelper.updateMyRecordsData(uploadIdList.get(index).getUploadId(), COMPLETED);

            Intent intent = new Intent(ATTATCHMENT_DOC_UPLOAD);
            intent.putExtra(RESULT, common);
            intent.putExtra(UPLOAD_ID, uploadIdList.get(index).getUploadId());
            sendBroadcast(intent);

            if (uploadIdList.size() == (index + 1)) {

                mBuilder.setContentText("File Uploaded Successfully ");
                // Removes the progress bar
//                        .setProgress(connectUploads.size(), uploadInfo.getProgressPercent(), false);
                mNotificationManager.notify(ADD_RECORD_FOREGROUND_ID, mBuilder.build());
                intent = new Intent(ATTATCHMENT_DOC_UPLOAD);
                intent.putExtra(RESULT, common);
                intent.putExtra(UPLOAD_ID, uploadIdList.get(index).getUploadId());
                sendBroadcast(intent);
                stopSelf();
            } else {
                index += 1;

                UploadStatus file = uploadIdList.get(index);
                String imageJson = file.getData();
                Image image = gson.fromJson(imageJson, Image.class);
                String path = image.getImagePath();

                imageUpload(path, uploadIdList.get(index).getHeaderMap(), file.getOpdId());
            }


        } else {

            mBuilder.setContentText("Uploading " + (uploadIdList.size() - index) + ((uploadIdList.size() - index) == 1 ? " File" : " Files"));
            // Removes the progress bar
//                        .setProgress(connectUploads.size(), uploadInfo.getProgressPercent(), false);
            mNotificationManager.notify(ADD_RECORD_FOREGROUND_ID, mBuilder.build());

            Log.e("index fail", "" + index);
            appDBHelper.updateMyRecordsData(uploadIdList.get(index).getUploadId(), FAILED);

            Intent intent = new Intent(ATTATCHMENT_DOC_UPLOAD);
            intent.putExtra(RESULT, common);
            intent.putExtra(UPLOAD_ID, uploadIdList.get(index).getUploadId());
            sendBroadcast(intent);

            if (uploadIdList.size() == (index + 1)) {

                mBuilder.setContentText("File Uploaded Successfully");
                // Removes the progress bar
//                        .setProgress(connectUploads.size(), uploadInfo.getProgressPercent(), false);
                mNotificationManager.notify(ADD_RECORD_FOREGROUND_ID, mBuilder.build());
                intent = new Intent(ATTATCHMENT_DOC_UPLOAD);
                intent.putExtra(RESULT, common);
                intent.putExtra(UPLOAD_ID, uploadIdList.get(index).getUploadId());
                sendBroadcast(intent);
                stopSelf();
            } else {
                index += 1;

                UploadStatus file = uploadIdList.get(index);
                String imageJson = file.getData();
                Image image = gson.fromJson(imageJson, Image.class);
                String path = image.getImagePath();

                imageUpload(path, uploadIdList.get(index).getHeaderMap(), file.getOpdId());

            }


        }
    }

}