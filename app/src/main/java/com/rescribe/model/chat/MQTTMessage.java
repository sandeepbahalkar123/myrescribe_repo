package com.rescribe.model.chat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MQTTMessage implements Parcelable {

    @SerializedName("msgId")
    @Expose
    private int msgId;
    @SerializedName("topic")
    @Expose
    private String topic;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("msgTime")
    @Expose
    private String msgTime;
    @SerializedName("sender")
    @Expose
    private String sender;
    @SerializedName("user2id")
    @Expose
    private int patId;
    @SerializedName("user1id")
    @Expose
    private int docId;

    // Added

    @SerializedName("name")
    @Expose
    private String name;
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

    // Added End

    // For File
    @SerializedName("isFile")
    @Expose
    private boolean isFile;

    public final static Creator<MQTTMessage> CREATOR = new Creator<MQTTMessage>() {

        @SuppressWarnings({
                "unchecked"
        })
        public MQTTMessage createFromParcel(Parcel in) {
            MQTTMessage instance = new MQTTMessage();
            instance.msgId = ((int) in.readValue((int.class.getClassLoader())));
            instance.topic = ((String) in.readValue((String.class.getClassLoader())));
            instance.msg = ((String) in.readValue((String.class.getClassLoader())));
            instance.msgTime = ((String) in.readValue((String.class.getClassLoader())));
            instance.sender = ((String) in.readValue((String.class.getClassLoader())));
            instance.docId = ((int) in.readValue((int.class.getClassLoader())));
            instance.patId = ((int) in.readValue((int.class.getClassLoader())));

            instance.paidStatus = ((int) in.readValue((int.class.getClassLoader())));

            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.specialization = ((String) in.readValue((String.class.getClassLoader())));
            instance.onlineStatus = ((String) in.readValue((String.class.getClassLoader())));
            instance.imageUrl = ((String) in.readValue((String.class.getClassLoader())));
            instance.address = ((String) in.readValue((String.class.getClassLoader())));

            instance.isFile = ((boolean) in.readValue((boolean.class.getClassLoader())));

            return instance;
        }

        public MQTTMessage[] newArray(int size) {
            return (new MQTTMessage[size]);
        }

    };

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public int getPatId() {
        return patId;
    }

    public void setPatId(int patId) {
        this.patId = patId;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    public void setPaidStatus(Integer paidStatus) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPaidStatus(int paidStatus) {
        this.paidStatus = paidStatus;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(msgId);
        dest.writeValue(topic);
        dest.writeValue(msg);
        dest.writeValue(msgTime);
        dest.writeValue(sender);
        dest.writeValue(docId);
        dest.writeValue(patId);

        dest.writeValue(paidStatus);
        dest.writeValue(name);
        dest.writeValue(specialization);
        dest.writeValue(onlineStatus);
        dest.writeValue(imageUrl);
        dest.writeValue(address);

        dest.writeValue(isFile);
    }

    public int describeContents() {
        return 0;
    }

}