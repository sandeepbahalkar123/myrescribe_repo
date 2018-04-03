/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rescribe.services.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.helpers.book_appointment.ServicesCardViewImpl;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.token.FCMData;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.activities.book_appointment.SelectSlotToBookAppointmentBaseActivity;
import com.rescribe.ui.activities.dashboard.UnreadNotificationMessageActivity;

import java.util.HashMap;

import static com.rescribe.util.RescribeConstants.FOLLOW_UP_NOTIFICATION_TAG;
import static com.rescribe.util.RescribeConstants.TOKEN_NOTIFICATION_TAG;

public class FCMService extends FirebaseMessagingService {



    private static final String TAG = "MyFirebaseMsgService";
    public static final String FCM_DATA = "fcm_data";

    public static final String TOKEN_DATA_ACTION = "token_data_action";
    public static final String FOLLOW_UP_DATA_ACTION = "followupNotification";
    public static final String FCM_BODY = "body";

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        int preCount = RescribePreferencesManager.getInt(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.TOKEN_ALERT_COUNT, FCMService.this);
        RescribePreferencesManager.putInt(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.TOKEN_ALERT_COUNT, preCount + 1, FCMService.this);

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

       /* // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
            scheduleJob();

        }*/

        if (remoteMessage.getData().size() > 0)
            sendNotification(remoteMessage.getNotification().getBody(), new HashMap<String, String>(remoteMessage.getData()));

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    /*private void scheduleJob() {
        // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("rescribe_job")
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }*/

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     * @param data
     */
    private void sendNotification(String messageBody, HashMap<String, String> data) {

        for (String name : data.keySet()) {
            String value = data.get(name);
            Log.d(TAG, name + " " + value);
        }

        String dataText = data.get(FCM_BODY);
        Gson gson = new Gson();
        FCMData fcmTokenData = gson.fromJson(dataText, FCMData.class);

        int icNotificationIcon;
        Intent intent;
        String tag;

        if (fcmTokenData.getIdentifier().equalsIgnoreCase(FOLLOW_UP_DATA_ACTION)) {

            tag = FOLLOW_UP_NOTIFICATION_TAG;

            intent = new Intent(this, UnreadNotificationMessageActivity.class);
            intent.putExtra(FCM_DATA, fcmTokenData);
            intent.setAction(FOLLOW_UP_DATA_ACTION);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            icNotificationIcon = R.drawable.ic_notification_gettoken;
        } else {

            tag = TOKEN_NOTIFICATION_TAG;

            intent = new Intent(this, SelectSlotToBookAppointmentBaseActivity.class);
            intent.putExtra(FCM_DATA, fcmTokenData);
            intent.setAction(TOKEN_DATA_ACTION);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            icNotificationIcon = R.drawable.ic_notification_appoinments;

            // call book appointment
            intent.putExtra(getString(R.string.clicked_item_data_type_value), getString(R.string.chats));
            intent.putExtra(getString(R.string.toolbarTitle), getString(R.string.book_appointment));

            DoctorList doctorListData1 = new DoctorList();
            doctorListData1.setDocId(fcmTokenData.getDocId());
            doctorListData1.setLocationId(fcmTokenData.getLocationId());
            ServicesCardViewImpl.setUserSelectedDoctorListDataObject(doctorListData1);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                                icNotificationIcon))
                        .setSmallIcon(R.drawable.logosmall)
                        .setContentTitle("Rescribe")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(tag, fcmTokenData.getDocId(), notificationBuilder.build());
    }
}