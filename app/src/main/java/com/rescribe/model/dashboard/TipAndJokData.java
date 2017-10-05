package com.rescribe.model.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TipAndJokData {

    @SerializedName("imgURL")
    @Expose
    private String imgURL;
    @SerializedName("type")
    @Expose
    private String type;

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
