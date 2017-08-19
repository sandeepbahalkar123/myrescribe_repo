package com.myrescribe.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;

public class MessageModel implements CustomResponse {
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