package com.rescribe.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class CommonBaseModelContainer implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common commonRespose;

    public Common getCommonRespose() {
        return commonRespose;
    }

    public void setCommonRespose(Common commonRespose) {
        this.commonRespose = commonRespose;
    }
}