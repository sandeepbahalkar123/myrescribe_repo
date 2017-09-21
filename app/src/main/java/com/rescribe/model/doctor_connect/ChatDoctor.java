
package com.rescribe.model.doctor_connect;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;


public class ChatDoctor implements Parcelable, CustomResponse {

    @SerializedName("docId")
    @Expose
    private int id;
    @SerializedName("docName")
    @Expose
    private String doctorName;
    @SerializedName("speciality")
    @Expose
    private String specialization;
    @SerializedName("onlineStatus")
    @Expose
    private String onlineStatus;
    @SerializedName("paidStatus")
    @Expose
    private int paidStatus;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("unreadMessages")
    @Expose
    private int unreadMessages;

    public final static Creator<ChatDoctor> CREATOR = new Creator<ChatDoctor>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ChatDoctor createFromParcel(Parcel in) {
            ChatDoctor instance = new ChatDoctor();
            instance.id = ((int) in.readValue((Integer.class.getClassLoader())));
            instance.doctorName = ((String) in.readValue((String.class.getClassLoader())));
            instance.specialization = ((String) in.readValue((String.class.getClassLoader())));
            instance.onlineStatus = ((String) in.readValue((String.class.getClassLoader())));
            instance.paidStatus = ((int) in.readValue((Integer.class.getClassLoader())));
            instance.imageUrl = ((String) in.readValue((String.class.getClassLoader())));
            instance.address = ((String) in.readValue((String.class.getClassLoader())));

            instance.unreadMessages = ((int) in.readValue((Integer.class.getClassLoader())));
            return instance;
        }

        public ChatDoctor[] newArray(int size) {
            return (new ChatDoctor[size]);
        }

    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public int getPaidStatus() {
        return paidStatus;
    }

    public void setPaidStatus(int paidStatus) {
        this.paidStatus = paidStatus;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(int unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(doctorName);
        dest.writeValue(specialization);
        dest.writeValue(onlineStatus);
        dest.writeValue(paidStatus);
        dest.writeValue(imageUrl);
        dest.writeValue(address);

        dest.writeValue(unreadMessages);
    }

    public int describeContents() {
        return 0;
    }

}
