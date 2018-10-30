package com.rescribe.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class CommonBaseModelContainer implements CustomResponse, Parcelable {

    @SerializedName("common")
    @Expose
    private Common commonRespose;

    public Common getCommonRespose() {
        return commonRespose;
    }

    public void setCommonRespose(Common commonRespose) {
        this.commonRespose = commonRespose;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.commonRespose, flags);
    }

    public CommonBaseModelContainer() {
    }

    protected CommonBaseModelContainer(Parcel in) {
        this.commonRespose = in.readParcelable(Common.class.getClassLoader());
    }

    public static final Parcelable.Creator<CommonBaseModelContainer> CREATOR = new Parcelable.Creator<CommonBaseModelContainer>() {
        @Override
        public CommonBaseModelContainer createFromParcel(Parcel source) {
            return new CommonBaseModelContainer(source);
        }

        @Override
        public CommonBaseModelContainer[] newArray(int size) {
            return new CommonBaseModelContainer[size];
        }
    };
}