package com.rescribe.model.book_appointment.doctor_data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClinicTokenData implements Parcelable {

    @SerializedName("tokenDetails")
    @Expose
    private TokenDetails tokenDetails;
    @SerializedName("isTokenTaken")
    @Expose
    private int isTokenTaken;
    public final static Parcelable.Creator<ClinicTokenData> CREATOR = new Creator<ClinicTokenData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ClinicTokenData createFromParcel(Parcel in) {
            return new ClinicTokenData(in);
        }

        public ClinicTokenData[] newArray(int size) {
            return (new ClinicTokenData[size]);
        }

    };

    protected ClinicTokenData(Parcel in) {
        this.tokenDetails = ((TokenDetails) in.readValue((TokenDetails.class.getClassLoader())));
        this.isTokenTaken = ((int) in.readValue((int.class.getClassLoader())));
    }

    public ClinicTokenData() {
    }

    public TokenDetails getTokenDetails() {
        return tokenDetails;
    }

    public void setTokenDetails(TokenDetails tokenDetails) {
        this.tokenDetails = tokenDetails;
    }

    public int isTokenTaken() {
        return isTokenTaken;
    }

    public void setTokenTaken(int isTokenTaken) {
        this.isTokenTaken = isTokenTaken;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(tokenDetails);
        dest.writeValue(isTokenTaken);
    }

    public int describeContents() {
        return 0;
    }

}