package com.myrescribe.model.investigation.uploaded;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.model.Common;
import com.myrescribe.model.investigation.gmail.GmailData;

public class InvestigationUploadFromUploadedModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private String data;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}