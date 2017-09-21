
package com.rescribe.model.doctor_connect;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

import java.util.ArrayList;

public class RecentChatDoctorData implements Parcelable, CustomResponse {

    @SerializedName("chatUsers")
    @Expose
    private ArrayList<ChatDoctor> chatDoctor = null;
    public final static Creator<RecentChatDoctorData> CREATOR = new Creator<RecentChatDoctorData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RecentChatDoctorData createFromParcel(Parcel in) {
            RecentChatDoctorData instance = new RecentChatDoctorData();
            in.readList(instance.chatDoctor, (ChatDoctor.class.getClassLoader()));
            return instance;
        }

        public RecentChatDoctorData[] newArray(int size) {
            return (new RecentChatDoctorData[size]);
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
