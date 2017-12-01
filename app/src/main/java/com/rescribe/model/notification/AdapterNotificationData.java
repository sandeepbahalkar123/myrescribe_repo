package com.rescribe.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

/**
 * Created by jeetal on 27/7/17.
 */

public class AdapterNotificationData  implements CustomResponse{
    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private AdapterNotificationPrescriptionModel adapterNotificationPrescriptionModel;


    public Common getCommon() {
        return common;
    }

    public AdapterNotificationPrescriptionModel getAdapterNotificationPrescriptionModel() {
        return adapterNotificationPrescriptionModel;
    }

    public void setAdapterNotificationPrescriptionModel(AdapterNotificationPrescriptionModel adapterNotificationPrescriptionModel) {
        this.adapterNotificationPrescriptionModel = adapterNotificationPrescriptionModel;
    }

    public void setCommon(Common common) {

        this.common = common;
    }


}
