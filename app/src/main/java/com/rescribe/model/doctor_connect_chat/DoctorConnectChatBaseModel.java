
package com.rescribe.model.doctor_connect_chat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;


public class DoctorConnectChatBaseModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private ChatData data;
    public final static Creator<DoctorConnectChatBaseModel> CREATOR = new Creator<DoctorConnectChatBaseModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DoctorConnectChatBaseModel createFromParcel(Parcel in) {
            DoctorConnectChatBaseModel instance = new DoctorConnectChatBaseModel();
            instance.common = ((Common) in.readValue((Common.class.getClassLoader())));
            instance.data = ((ChatData) in.readValue((ChatData.class.getClassLoader())));
            return instance;
        }

        public DoctorConnectChatBaseModel[] newArray(int size) {
            return (new DoctorConnectChatBaseModel[size]);
        }

    };

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public ChatData getData() {
        return data;
    }

    public void setData(ChatData data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(data);
    }

    public int describeContents() {
        return 0;
    }

}
