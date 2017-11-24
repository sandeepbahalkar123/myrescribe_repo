package com.rescribe.model.book_appointment.doctor_data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClinicTokenDetails implements Parcelable {
    @SerializedName("scheduledTimeStamp")
    @Expose
    private String scheduledTimeStamp;
    @SerializedName("tokenNumber")
    @Expose
    private String tokenNumber;
    @SerializedName("waitingTime")
    @Expose
    private String waitingTime;

    public ClinicTokenDetails(String scheduledTimeStamp, String tokenNumber, String waitingTime) {
        this.scheduledTimeStamp = scheduledTimeStamp;
        this.tokenNumber = tokenNumber;
        this.waitingTime = waitingTime;
    }

    public final static Parcelable.Creator<ClinicTokenDetails> CREATOR = new Creator<ClinicTokenDetails>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ClinicTokenDetails createFromParcel(Parcel in) {
            return new ClinicTokenDetails(in);
        }

        public ClinicTokenDetails[] newArray(int size) {
            return (new ClinicTokenDetails[size]);
        }

    };

    protected ClinicTokenDetails(Parcel in) {
        this.scheduledTimeStamp = ((String) in.readValue((String.class.getClassLoader())));
        this.tokenNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.waitingTime = ((String) in.readValue((String.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(scheduledTimeStamp);
        dest.writeValue(tokenNumber);
        dest.writeValue(waitingTime);
    }

    public int describeContents() {
        return 0;
    }

    public String getScheduledTimeStamp() {
        return scheduledTimeStamp;
    }

    public void setScheduledTimeStamp(String scheduledTimeStamp) {
        this.scheduledTimeStamp = scheduledTimeStamp;
    }

    public String getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public String getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(String waitingTime) {
        this.waitingTime = waitingTime;
    }
}