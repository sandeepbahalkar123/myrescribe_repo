package com.rescribe.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

import java.util.List;

public class NotificationPrescriptionModel implements CustomResponse{

@SerializedName("prescriptionNotification")
@Expose
private List<NotificationData> presriptionNotification = null;

    public List<NotificationData> getPresriptionNotification() {
        return presriptionNotification;
    }

    public void setPresriptionNotification(List<NotificationData> presriptionNotification) {
        this.presriptionNotification = presriptionNotification;
    }
}