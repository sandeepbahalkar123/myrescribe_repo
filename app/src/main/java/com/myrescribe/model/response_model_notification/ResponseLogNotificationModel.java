package com.myrescribe.model.response_model_notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.model.notification.Common;

public class ResponseLogNotificationModel implements CustomResponse{

@SerializedName("common")
@Expose
private Common common;

public Common getCommon() {
return common;
}

public void setCommon(Common common) {
this.common = common;
}

}