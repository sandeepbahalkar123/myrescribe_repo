package com.rescribe.model.notification;

import android.app.Notification;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class NotificationPrescriptionModel implements CustomResponse{

@SerializedName("presriptionNotification")
@Expose
private List<NotificationData> presriptionNotification = null;

    public List<NotificationData> getPresriptionNotification() {
        return presriptionNotification;
    }

    public void setPresriptionNotification(List<NotificationData> presriptionNotification) {
        this.presriptionNotification = presriptionNotification;
    }
}