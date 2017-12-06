package com.rescribe.model.dashboard_api.unread_notification_message_list;

public class UnreadNotificationMessageData  {


    private String id;
    private String notificationMessageType;
    private String notificationData;
    private String notificationTimeStamp;

    public UnreadNotificationMessageData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotificationMessageType() {
        return notificationMessageType;
    }

    public void setNotificationMessageType(String notificationMessageType) {
        this.notificationMessageType = notificationMessageType;
    }

    public String getNotificationData() {
        return notificationData;
    }

    public void setNotificationData(String notificationData) {
        this.notificationData = notificationData;
    }

    public String getNotificationTimeStamp() {
        return notificationTimeStamp;
    }

    public void setNotificationTimeStamp(String notificationTimeStamp) {
        this.notificationTimeStamp = notificationTimeStamp;
    }
}