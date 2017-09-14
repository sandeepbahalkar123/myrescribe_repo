package com.rescribe.model.response_model_notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

public class NotificationResponseBaseModel implements CustomResponse{

@SerializedName("common")
@Expose
private Common common;
@SerializedName("notificationResponseModel")
@Expose
private NotificationResponseModel notificationResponseModel;

public Common getCommon() {
return common;
}

public void setCommon(Common common) {
this.common = common;
}

public NotificationResponseModel getNotificationResponseModel() {
return notificationResponseModel;
}

public void setNotificationResponseModel(NotificationResponseModel notificationResponseModel) {
this.notificationResponseModel = notificationResponseModel;
}

}