package com.rescribe.model.chat.history;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class HistoryData implements Parcelable {

    @SerializedName("chatHistory")
    @Expose
    private List<ChatHistory> chatHistory = new ArrayList<ChatHistory>();
    @SerializedName("onlineStatus")
    @Expose
    private UserOnlineStatus userOnlineStatus;

    public final static Parcelable.Creator<HistoryData> CREATOR = new Creator<HistoryData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public HistoryData createFromParcel(Parcel in) {
            HistoryData instance = new HistoryData();
            in.readList(instance.chatHistory, (ChatHistory.class.getClassLoader()));
            instance.userOnlineStatus = ((UserOnlineStatus) in.readValue((UserOnlineStatus.class.getClassLoader())));

            return instance;
        }

        public HistoryData[] newArray(int size) {
            return (new HistoryData[size]);
        }

    };

    public List<ChatHistory> getChatHistory() {
        return chatHistory;
    }

    public void setChatHistory(List<ChatHistory> chatHistory) {
        this.chatHistory = chatHistory;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(chatHistory);
        dest.writeValue(userOnlineStatus);
    }

    public int describeContents() {
        return 0;
    }

    public UserOnlineStatus getUserOnlineStatus() {
        return userOnlineStatus;
    }

    public void setUserOnlineStatus(UserOnlineStatus userOnlineStatus) {
        this.userOnlineStatus = userOnlineStatus;
    }
}