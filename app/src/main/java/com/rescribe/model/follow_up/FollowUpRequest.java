package com.rescribe.model.follow_up;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class FollowUpRequest implements CustomResponse{

    @SerializedName("notificationId")
    @Expose
    private int notificationId;
    @SerializedName("response")
    @Expose
    private String response;

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int reminderId) {
        this.notificationId = reminderId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}