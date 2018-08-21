package com.rescribe.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.content.ContextCompat;

import com.rescribe.R;
import com.rescribe.model.chat.MQTTMessage;
import com.rescribe.services.MQTTService;
import com.rescribe.ui.activities.ChatActivity;
import com.rescribe.ui.activities.DoctorConnectActivity;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import static com.rescribe.broadcast_receivers.ReplayBroadcastReceiver.MESSAGE_LIST;
import static com.rescribe.helpers.notification.NotificationHelper.CONNECT_CHANNEL;
import static com.rescribe.services.MQTTService.REPLY_ACTION;
import static com.rescribe.util.RescribeConstants.FILE.AUD;
import static com.rescribe.util.RescribeConstants.FILE.DOC;
import static com.rescribe.util.RescribeConstants.FILE.IMG;
import static com.rescribe.util.RescribeConstants.FILE.LOC;
import static com.rescribe.util.RescribeConstants.FILE.VID;

/**
 * Helper class for showing and canceling new message
 * notifications.
 * <p>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class MessageNotification extends ContextWrapper {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "RescribeMessage";
    private static final String GROUP = "RescribeMessages";
    private final Context context;
    private NotificationManager mNotificationManager;

    public MessageNotification(Context base) {
        super(base);
        context = getBaseContext();
        createChannel();
    }

    public void notify(final ArrayList<MQTTMessage> messageContent,
                              String userName, Bitmap picture, final int unread, PendingIntent replyPendingIntent, final int notificationId) {

        MQTTMessage lastMessage = messageContent.get(messageContent.size() - 1);
        String content = getContent(lastMessage);

        if (!userName.contains("Dr. "))
            userName = "Dr. " + userName;

        String title;
        if (unread > 1)
            title = userName + " (" + unread + " messages)";
        else title = userName;

        Intent resultIntent = new Intent(context, ChatActivity.class);
        resultIntent.setAction(REPLY_ACTION);
        resultIntent.putExtra(MESSAGE_LIST, lastMessage);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        lastMessage.getDocId(),
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                .setBigContentTitle(title)
                .setSummaryText("New Message");

        for (MQTTMessage message : messageContent)
            inboxStyle.addLine(getContent(message));

// Create the RemoteInput specifying above key
        RemoteInput remoteInput = new RemoteInput.Builder(MQTTService.KEY_REPLY)
                .setLabel("Reply")
                .build();

        // Add to your action, enabling Direct Reply
        NotificationCompat.Action mAction =
                new NotificationCompat.Action.Builder(R.drawable.ic_action_stat_reply, "Reply", replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .setAllowGeneratedReplies(true)
                        .build();

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CONNECT_CHANNEL)

                // Set appropriate defaults for the notification light, sound,
                // and vibration.
                .setDefaults(Notification.DEFAULT_ALL)

                // Bundle Notification

                .setGroupSummary(true)
                .setGroup(GROUP)

                // Set required fields, including the small icon, the
                // notification title, and text.
                .setSmallIcon(R.drawable.logosmall)
                .setContentTitle(title)
                .setContentText(content)

                // Set Color
                .setColor(ContextCompat.getColor(context, R.color.tagColor))

                // All fields below this line are optional.

                // Use a default priority (recognized on devices running Android
                // 4.1 or later)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

                // Provide a large icon, shown with the notification in the
                // notification drawer on devices running Android 3.0 or later.
                .setLargeIcon(picture)

                // Set ticker text (preview) information for this notification.
                .setTicker(title)

                // Show a number. This is useful when stacking notifications of
                // a single type.
                .setNumber(unread)

                // Set Style and Action
                .setStyle(inboxStyle)
                .addAction(mAction)

                // Click Event on notification
                .setContentIntent(resultPendingIntent)

                // Automatically dismiss the notification when it is touched.
                .setAutoCancel(true);

        notify(context, builder.build(), notificationId);
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification, int notificationId) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_TAG, notificationId, notification);
    }


    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context, int notificationId) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_TAG, notificationId);
    }

    private String getContent(MQTTMessage mqttMessage) {
        String content;

        if (mqttMessage.getFileType() != null) {
            switch (mqttMessage.getFileType()) {
                case DOC:
                    content = RescribeConstants.FILE_EMOJI.DOC_FILE;
                    break;
                case AUD:
                    content = RescribeConstants.FILE_EMOJI.AUD_FILE;
                    break;
                case VID:
                    content = RescribeConstants.FILE_EMOJI.VID_FILE;
                    break;
                case LOC:
                    content = RescribeConstants.FILE_EMOJI.LOC_FILE;
                    break;
                case IMG:
                    content = RescribeConstants.FILE_EMOJI.IMG_FILE;
                    break;
                default:
                    content = mqttMessage.getMsg();
                    break;
            }
        } else content = mqttMessage.getMsg();

        return content;
    }

    public void createChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Create the channel object with the unique ID CONNECT_CHANNEL
            NotificationChannel connectChannel = new NotificationChannel(
                    CONNECT_CHANNEL, "DrConnect",
                    NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the channel's initial settings
            connectChannel.setLightColor(Color.GREEN);
            connectChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            // Submit the notification channel object to the notification manager
             getNotificationManager().createNotificationChannel(connectChannel);
        }
    }

    /**
     * Get the notification mNotificationManager.
     * <p>
     * <p>Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    public NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }
}
