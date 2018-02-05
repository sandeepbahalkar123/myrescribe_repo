package com.rescribe.model.book_appointment.doctor_data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

public class ClinicTokenDetails implements Parcelable {
    @SerializedName("apmtTime")
    @Expose
    private String scheduledTimeStamp;
    @SerializedName("tokenNo")
    @Expose
    private int tokenNumber;
    @SerializedName("waitingTime")
    @Expose
    private int waitingTime;

    @SerializedName("waitingPatientCount")
    @Expose
    private int waitingPatientCount;

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
        this.tokenNumber = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.waitingTime = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.waitingPatientCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(scheduledTimeStamp);
        dest.writeValue(tokenNumber);
        dest.writeValue(waitingTime);
        dest.writeValue(waitingPatientCount);
    }

    public int describeContents() {
        return 0;
    }

    public String getScheduledTimeStamp() {
        if (scheduledTimeStamp.contains("T")) {
            String date[] = scheduledTimeStamp.split("T");
            if (date[1].contains(":")) {
                String[] split = date[1].split(":");
                scheduledTimeStamp = split[0] + ":" + split[1] + ":00";
                scheduledTimeStamp = CommonMethods.formatDateTime(scheduledTimeStamp, RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.DATE_PATTERN.hh_mm, RescribeConstants.TIME);
            }
        }
        return scheduledTimeStamp;
    }

    public void setScheduledTimeStamp(String scheduledTimeStamp) {
        this.scheduledTimeStamp = scheduledTimeStamp;
    }

    public int getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(int tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getWaitingPatientCount() {
        return waitingPatientCount;
    }

    public void setWaitingPatientCount(int waitingPatientCount) {
        this.waitingPatientCount = waitingPatientCount;
    }
}