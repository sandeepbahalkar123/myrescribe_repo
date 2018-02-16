package com.rescribe.model.follow_up;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class FollowUpRequest implements CustomResponse{

    @SerializedName("reminderId")
    @Expose
    private int reminderId;
    @SerializedName("response")
    @Expose
    private String response;

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}