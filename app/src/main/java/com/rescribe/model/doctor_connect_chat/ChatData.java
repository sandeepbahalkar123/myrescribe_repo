
package com.rescribe.model.doctor_connect_chat;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;


import java.util.ArrayList;

public class ChatData implements Parcelable,CustomResponse {

    @SerializedName("doctorList")
    @Expose
    private ArrayList<ChatList> chatList = null;
    public final static Creator<ChatData> CREATOR = new Creator<ChatData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ChatData createFromParcel(Parcel in) {
            ChatData instance = new ChatData();
            in.readList(instance.chatList, (ChatList.class.getClassLoader()));
            return instance;
        }

        public ChatData[] newArray(int size) {
            return (new ChatData[size]);
        }

    };

    public ArrayList<ChatList> getChatList() {
        return chatList;
    }

    public void setChatList(ArrayList<ChatList> chatList) {
        this.chatList = chatList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(chatList);
    }

    public int describeContents() {
        return 0;
    }

}
