package com.rescribe.model.response_model_notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class NotificationResponseModel implements CustomResponse{

@SerializedName("msg")
@Expose
private String msg;

public String getMsg() {
return msg;
}

public void setMsg(String msg) {
this.msg = msg;
}

}