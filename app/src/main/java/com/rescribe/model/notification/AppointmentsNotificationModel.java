package com.rescribe.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsNotificationModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private List<AppointmentsNotificationData> data = new ArrayList<>();

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public List<AppointmentsNotificationData> getData() {
        return data;
    }

    public void setData(List<AppointmentsNotificationData> data) {
        this.data = data;
    }

}