package com.rescribe.model.dashboard_api.unread_notification_message_list;

public class UnreadSavedNotificationMessageData {


    private String id;
    private String notificationMessageType;
    private String notificationData;
    private String notificationMessage;
    private String notificationTimeStamp;

    private String buttonToVisible;

    public UnreadSavedNotificationMessageData() {
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

    public String getButtonToVisible() {
        return buttonToVisible;
    }

    public void setButtonToVisible(String buttonToVisible) {
        this.buttonToVisible = buttonToVisible;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }
}