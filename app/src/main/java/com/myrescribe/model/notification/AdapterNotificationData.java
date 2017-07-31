package com.myrescribe.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.model.Common;

import java.util.List;

/**
 * Created by jeetal on 27/7/17.
 */

public class AdapterNotificationData  implements CustomResponse{
    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private List<AdapterNotificationModel> data = null;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public List<AdapterNotificationModel> getData() {
        return data;
    }

    public void setData(List<AdapterNotificationModel> data) {
        this.data = data;
    }
}
