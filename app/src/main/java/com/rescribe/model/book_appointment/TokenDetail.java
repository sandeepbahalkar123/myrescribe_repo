package com.rescribe.model.book_appointment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenDetail {

    @SerializedName("tokenNumber")
    @Expose
    private int tokenNumber;

    @SerializedName("waitingPatientCount")
    @Expose
    private String waitingPatientCount;

    @SerializedName("waitingPatientTime")
    @Expose
    private String waitingPatientTime;

    public int getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(int tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public String getWaitingPatientCount() {
        return waitingPatientCount;
    }

    public void setWaitingPatientCount(String waitingPatientCount) {
        this.waitingPatientCount = waitingPatientCount;
    }

    public String getWaitingPatientTime() {
        return waitingPatientTime;
    }

    public void setWaitingPatientTime(String waitingPatientTime) {
        this.waitingPatientTime = waitingPatientTime;
    }
}