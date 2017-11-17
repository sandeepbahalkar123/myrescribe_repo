package com.rescribe.model.chat.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserOnlineStatus {
        @SerializedName("user2id")
        @Expose
        private String user2ID;
        @SerializedName("onlineStatus")
        @Expose
        private String onlineStatus;

        public String getUser2ID() {
            return user2ID;
        }

        public void setUser2ID(String user2ID) {
            this.user2ID = user2ID;
        }

        public String getOnlineStatus() {
            return onlineStatus;
        }

        public void setOnlineStatus(String onlineStatus) {
            this.onlineStatus = onlineStatus;
        }
    }