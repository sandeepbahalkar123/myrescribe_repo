
package com.rescribe.model.book_appointment.unread_token_notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;
import com.rescribe.model.book_appointment.search_doctors.RecentVisitedModel;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;


public class UnreadBookAppointTokenNotificationData implements CustomResponse {

    @SerializedName("reminderId")
    @Expose
    private Integer reminderId;
    @SerializedName("patientId")
    @Expose
    private Integer patientId;
    @SerializedName("locationId")
    @Expose
    private Integer locationId;
    @SerializedName("docId")
    @Expose
    private Integer docId;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("os")
    @Expose
    private String os;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("read_status")
    @Expose
    private Integer readStatus;
    @SerializedName("notification_msg")
    @Expose
    private String notificationMsg;
    @SerializedName("token_number")
    @Expose
    private Integer tokenNumber;

    public Integer getReminderId() {
        return reminderId;
    }

    public void setReminderId(Integer reminderId) {
        this.reminderId = reminderId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getCreatedDate() {
        if (createdDate != null) {
            if (createdDate.contains("T")) {
                createdDate = CommonMethods.formatDateTime(createdDate, RescribeConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm_a, RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE);
            }
        }
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public String getNotificationMsg() {
        return notificationMsg;
    }

    public void setNotificationMsg(String notificationMsg) {
        this.notificationMsg = notificationMsg;
    }

    public Integer getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(Integer tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

}
