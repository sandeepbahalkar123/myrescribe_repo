package com.rescribe.model.my_records;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisitDate implements Parcelable {

    @SerializedName("opdDate")
    @Expose
    private String opdDate;
    @SerializedName("opdId")
    @Expose
    private int opdId;
    public final static Creator<VisitDate> CREATOR = new Creator<VisitDate>() {


        @SuppressWarnings({
                "unchecked"
        })
        public VisitDate createFromParcel(Parcel in) {
            VisitDate instance = new VisitDate();
            instance.opdDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.opdId = ((int) in.readValue((int.class.getClassLoader())));
            return instance;
        }

        public VisitDate[] newArray(int size) {
            return (new VisitDate[size]);
        }

    };

    public String getOpdDate() {
        return opdDate;
    }

    public void setOpdDate(String opdDate) {
        this.opdDate = opdDate;
    }

    public int getOpdId() {
        return opdId;
    }

    public void setOpdId(int opdId) {
        this.opdId = opdId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(opdDate);
        dest.writeValue(opdId);
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return opdDate;
    }
}