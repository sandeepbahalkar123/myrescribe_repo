
package com.rescribe.model.doctor_connect;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;


public class RecentChatDoctorModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private RecentChatDoctorData doctorConnectDataModel;
    public final static Creator<RecentChatDoctorModel> CREATOR = new Creator<RecentChatDoctorModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RecentChatDoctorModel createFromParcel(Parcel in) {
            RecentChatDoctorModel instance = new RecentChatDoctorModel();
            instance.common = ((Common) in.readValue((Common.class.getClassLoader())));
            instance.doctorConnectDataModel = ((RecentChatDoctorData) in.readValue((DoctorConnectDataModel.class.getClassLoader())));
            return instance;
        }

        public RecentChatDoctorModel[] newArray(int size) {
            return (new RecentChatDoctorModel[size]);
        }

    };

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public RecentChatDoctorData getDoctorConnectDataModel() {
        return doctorConnectDataModel;
    }

    public void setDoctorConnectDataModel(RecentChatDoctorData doctorConnectDataModel) {
        this.doctorConnectDataModel = doctorConnectDataModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(doctorConnectDataModel);
    }

    public int describeContents() {
        return 0;
    }

}
