package com.rescribe.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

import java.util.List;

/**
 * Created by jeetal on 12/9/17.
 */

public class AdapterNotificationPrescriptionModel implements CustomResponse {
    @SerializedName("presriptionNotification")
    @Expose
    private List<AdapterNotificationModel> presriptionNotification = null;

    public List<AdapterNotificationModel> getPresriptionNotification() {
        return presriptionNotification;
    }

    public void setPresriptionNotification(List<AdapterNotificationModel> presriptionNotification) {
        this.presriptionNotification = presriptionNotification;
    }
}
