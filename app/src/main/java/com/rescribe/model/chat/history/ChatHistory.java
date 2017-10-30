package com.rescribe.model.chat.history;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatHistory implements Parcelable {

    @SerializedName("chat_id")
    @Expose
    private String chatId = "";
    @SerializedName("user_1_id")
    @Expose
    private int user1Id;
    @SerializedName("user_2_id")
    @Expose
    private int user2Id;
    @SerializedName("sender")
    @Expose
    private String sender;
    @SerializedName("msg")
    @Expose
    private String msg = "";
    @SerializedName("msgTime")
    @Expose
    private String msgTime;

    // Added

    @SerializedName("senderName")
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
    @SerializedName("senderImgUrl")
    @Expose
    private String imageUrl = "";
    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("fileType")
    @Expose
    private String fileType;

    @SerializedName("fileUrl")
    @Expose
    private String fileUrl;

    // Added End

    public final static Creator<ChatHistory> CREATOR = new Creator<ChatHistory>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ChatHistory createFromParcel(Parcel in) {
            ChatHistory instance = new ChatHistory();
            instance.chatId = ((String) in.readValue((int.class.getClassLoader())));
            instance.user1Id = ((int) in.readValue((int.class.getClassLoader())));
            instance.user2Id = ((int) in.readValue((int.class.getClassLoader())));
            instance.sender = ((String) in.readValue((String.class.getClassLoader())));
            instance.msg = ((String) in.readValue((String.class.getClassLoader())));
            instance.msgTime = ((String) in.readValue((String.class.getClassLoader())));

            instance.paidStatus = ((int) in.readValue((int.class.getClassLoader())));

            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.specialization = ((String) in.readValue((String.class.getClassLoader())));
            instance.onlineStatus = ((String) in.readValue((String.class.getClassLoader())));
            instance.imageUrl = ((String) in.readValue((String.class.getClassLoader())));
            instance.address = ((String) in.readValue((String.class.getClassLoader())));

            instance.fileType = ((String) in.readValue((String.class.getClassLoader())));
            instance.fileUrl = ((String) in.readValue((String.class.getClassLoader())));

            return instance;
        }

        public ChatHistory[] newArray(int size) {
            return (new ChatHistory[size]);
        }

    };

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public int getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(int user1Id) {
        this.user1Id = user1Id;
    }

    public int getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(int user2Id) {
        this.user2Id = user2Id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    // Added

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    // End Added

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(chatId);
        dest.writeValue(user1Id);
        dest.writeValue(user2Id);
        dest.writeValue(sender);
        dest.writeValue(msg);
        dest.writeValue(msgTime);

        dest.writeValue(paidStatus);
        dest.writeValue(name);
        dest.writeValue(specialization);
        dest.writeValue(onlineStatus);
        dest.writeValue(imageUrl);
        dest.writeValue(address);

        dest.writeValue(fileType);
        dest.writeValue(fileUrl);
    }

    public int describeContents() {
        return 0;
    }
}