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
import com.rescribe.model.token.FCMTokenData;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.activities.HomePageActivity;

import java.util.HashMap;

import static com.rescribe.util.RescribeConstants.TOKEN_NOTIFICATION_TAG;

public class FCMService extends FirebaseMessagingService {
    
    private static final String TAG = "MyFirebaseMsgService";
    public static final String TOKEN_DATA = "token_data";

    public static final String TOKEN_DATA_ACTION = "token_data_action";
    public static final String FCM_BODY = "body";

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

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
     *  @param messageBody FCM message body received.
     * @param data
     */
    private void sendNotification(String messageBody, HashMap<String, String> data) {

        for (String name : data.keySet()) {
            String value = data.get(name);
            Log.d(TAG, name + " " + value);
        }

        String dataText = data.get(FCM_BODY);
        Gson gson = new Gson();
        FCMTokenData fcmTokenData = gson.fromJson(dataText, FCMTokenData.class);

        Intent intent = new Intent(this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(TOKEN_DATA, fcmTokenData);
        intent.setAction(TOKEN_DATA_ACTION);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                                R.drawable.ic_notification_gettoken))
                        .setSmallIcon(R.drawable.logosmall)
                        .setContentTitle("Rescribe")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (fcmTokenData != null) {
            notificationManager.notify(TOKEN_NOTIFICATION_TAG, fcmTokenData.getTokenNumber(), notificationBuilder.build());
        }
    }
}