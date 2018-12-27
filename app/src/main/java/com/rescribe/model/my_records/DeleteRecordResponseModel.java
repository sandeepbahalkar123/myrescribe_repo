package com.rescribe.model.my_records;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

public class DeleteRecordResponseModel implements CustomResponse {

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
