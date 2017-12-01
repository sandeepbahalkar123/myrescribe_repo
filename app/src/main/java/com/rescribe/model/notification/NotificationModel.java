
package com.rescribe.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

public class NotificationModel implements CustomResponse{

    @SerializedName("common")
    @Expose
    private Common common;

    @SerializedName("data")
    @Expose
    private NotificationPrescriptionModel notificationPrescriptionModel;

    public NotificationPrescriptionModel getNotificationPrescriptionModel() {
        return notificationPrescriptionModel;
    }

    public void setNotificationPrescriptionModel(NotificationPrescriptionModel notificationPrescriptionModel) {
        this.notificationPrescriptionModel = notificationPrescriptionModel;
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

}
