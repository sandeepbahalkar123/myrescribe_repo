
package com.rescribe.model.doctor_connect;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;


import java.util.ArrayList;

public class DoctorConnectDataModel implements Parcelable, CustomResponse {

    @SerializedName("doctorList")
    @Expose
    private ArrayList<ChatDoctor> chatDoctor = new ArrayList<>();
    public final static Creator<DoctorConnectDataModel> CREATOR = new Creator<DoctorConnectDataModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DoctorConnectDataModel createFromParcel(Parcel in) {
            DoctorConnectDataModel instance = new DoctorConnectDataModel();
            in.readList(instance.chatDoctor, (ChatDoctor.class.getClassLoader()));
            return instance;
        }

        public DoctorConnectDataModel[] newArray(int size) {
            return (new DoctorConnectDataModel[size]);
        }

    };

    public ArrayList<ChatDoctor> getChatDoctor() {
        return chatDoctor;
    }

    public void setChatDoctor(ArrayList<ChatDoctor> chatDoctor) {
        this.chatDoctor = chatDoctor;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(chatDoctor);
    }

    public int describeContents() {
        return 0;
    }

}
