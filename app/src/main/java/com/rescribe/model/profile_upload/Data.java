package com.rescribe.model.profile_upload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("patImgUrl")
    @Expose
    private String patImgUrl;
    @SerializedName("modificationDate")
    @Expose
    private String modificationDate;

    public String getPatImgUrl() {
        return patImgUrl;
    }

    public void setPatImgUrl(String patImgUrl) {
        this.patImgUrl = patImgUrl;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

}