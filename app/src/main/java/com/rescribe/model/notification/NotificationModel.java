
package com.rescribe.model.notification;

import java.util.List;
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
    private List<NotificationData> data = null;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public List<NotificationData> getData() {
        return data;
    }

    public void setData(List<NotificationData> data) {
        this.data = data;
    }

}
