package com.rescribe.model.filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

public class FilterDoctorListModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private DoctorNameModel doctorNameModel;
    public final static Parcelable.Creator<FilterDoctorListModel> CREATOR = new Creator<FilterDoctorListModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public FilterDoctorListModel createFromParcel(Parcel in) {
            FilterDoctorListModel instance = new FilterDoctorListModel();
            instance.common = ((Common) in.readValue((Common.class.getClassLoader())));
            instance.doctorNameModel = ((DoctorNameModel) in.readValue((DoctorNameModel.class.getClassLoader())));
            return instance;
        }

        public FilterDoctorListModel[] newArray(int size) {
            return (new FilterDoctorListModel[size]);
        }

    }
            ;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public DoctorNameModel getDoctorNameModel() {
        return doctorNameModel;
    }

    public void setDoctorNameModel(DoctorNameModel doctorNameModel) {
        this.doctorNameModel = doctorNameModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(doctorNameModel);
    }

    public int describeContents() {
        return 0;
    }
}