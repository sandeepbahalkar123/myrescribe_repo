package com.rescribe.model.book_appointment.doctor_data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenDetails implements Parcelable {

    @SerializedName("waitingPatientCount")
    @Expose
    private int waitingPatientCount;
    @SerializedName("waitingTime")
    @Expose
    private int waitingTime;
    @SerializedName("apmtTime")
    @Expose
    private String apmtTime;
    public final static Parcelable.Creator<TokenDetails> CREATOR = new Creator<TokenDetails>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TokenDetails createFromParcel(Parcel in) {
            return new TokenDetails(in);
        }

        public TokenDetails[] newArray(int size) {
            return (new TokenDetails[size]);
        }

    };

    protected TokenDetails(Parcel in) {
        this.waitingPatientCount = ((int) in.readValue((int.class.getClassLoader())));
        this.waitingTime = ((int) in.readValue((int.class.getClassLoader())));
        this.apmtTime = ((String) in.readValue((String.class.getClassLoader())));
    }

    public TokenDetails() {
    }

    public int getWaitingPatientCount() {
        return waitingPatientCount;
    }

    public void setWaitingPatientCount(int waitingPatientCount) {
        this.waitingPatientCount = waitingPatientCount;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public String getApmtTime() {
        return apmtTime;
    }

    public void setApmtTime(String apmtTime) {
        this.apmtTime = apmtTime;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(waitingPatientCount);
        dest.writeValue(waitingTime);
        dest.writeValue(apmtTime);
    }

    public int describeContents() {
        return 0;
    }

}