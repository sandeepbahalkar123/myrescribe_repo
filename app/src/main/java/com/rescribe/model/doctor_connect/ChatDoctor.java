
package com.rescribe.model.doctor_connect;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.util.CommonMethods;


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
    @SerializedName("appointmentType")
    @Expose
    private String docAppointmentType;
    @SerializedName("onlineStatus")
    @Expose
    private String onlineStatus = "";
    @SerializedName("paidStatus")
    @Expose
    private int paidStatus;
    @SerializedName("doctorImageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("doctorAddress")
    @Expose
    private String address;
    @SerializedName("lastChatTime")
    @Expose
    private String lastChatTime;
    @SerializedName("unreadMessages")
    @Expose
    private int unreadMessages;
    @SerializedName("amount")
    @Expose
    private int amount;

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
            instance.lastChatTime = ((String) in.readValue((String.class.getClassLoader())));
            instance.unreadMessages = ((int) in.readValue((Integer.class.getClassLoader())));
            instance.docAppointmentType = ((String) in.readValue((String.class.getClassLoader())));
            instance.amount = ((int) in.readValue((Integer.class.getClassLoader())));

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
        return CommonMethods.toCamelCase(onlineStatus);
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

    public String getLastChatTime() {
        return lastChatTime;
    }

    public void setLastChatTime(String lastChatTime) {
        this.lastChatTime = lastChatTime;
    }

    public String getDocAppointmentType() {
        return docAppointmentType;
    }

    public void setDocAppointmentType(String docAppointmentType) {
        this.docAppointmentType = docAppointmentType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(doctorName);
        dest.writeValue(specialization);
        dest.writeValue(onlineStatus);
        dest.writeValue(paidStatus);
        dest.writeValue(imageUrl);
        dest.writeValue(address);
        dest.writeValue(lastChatTime);
        dest.writeValue(unreadMessages);
        dest.writeValue(docAppointmentType);
        dest.writeValue(amount);
    }

    public int describeContents() {
        return 0;
    }

}
