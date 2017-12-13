
package com.rescribe.model.book_appointment.unread_token_notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

import java.util.ArrayList;


public class UnreadBookAppointTokenNotificationBaseModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private UnreadTokenNotificationDataModel unreadTokenNotificationDataModel;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public UnreadTokenNotificationDataModel getUnreadTokenNotificationDataModel() {
        return unreadTokenNotificationDataModel;
    }

    public void setUnreadTokenNotificationDataModel(UnreadTokenNotificationDataModel unreadTokenNotificationDataModel) {
        this.unreadTokenNotificationDataModel = unreadTokenNotificationDataModel;
    }

    public class UnreadTokenNotificationDataModel implements CustomResponse {

        @SerializedName("notifications")
        @Expose
        private ArrayList<UnreadBookAppointTokenNotificationData> unreadTokenNotificationDataList;

        public ArrayList<UnreadBookAppointTokenNotificationData> getUnreadTokenNotificationDataList() {
            return unreadTokenNotificationDataList;
        }

        public void setUnreadTokenNotificationDataList(ArrayList<UnreadBookAppointTokenNotificationData> unreadTokenNotificationDataList) {
            this.unreadTokenNotificationDataList = unreadTokenNotificationDataList;
        }
    }

}
