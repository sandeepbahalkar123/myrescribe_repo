package com.rescribe.model.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

public class SendMessageModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
   /* @SerializedName("data")
    @Expose
    private String data;*/

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    /*public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }*/

}