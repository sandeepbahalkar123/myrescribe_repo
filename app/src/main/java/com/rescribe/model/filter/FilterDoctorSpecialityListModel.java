package com.rescribe.model.filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

public class FilterDoctorSpecialityListModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private DoctorSpecialitiesModel doctorSpecialitiesModel;
    public final static Parcelable.Creator<FilterDoctorSpecialityListModel> CREATOR = new Creator<FilterDoctorSpecialityListModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public FilterDoctorSpecialityListModel createFromParcel(Parcel in) {
            FilterDoctorSpecialityListModel instance = new FilterDoctorSpecialityListModel();
            instance.common = ((Common) in.readValue((Common.class.getClassLoader())));
            instance.doctorSpecialitiesModel = ((DoctorSpecialitiesModel) in.readValue((DoctorSpecialitiesModel.class.getClassLoader())));
            return instance;
        }

        public FilterDoctorSpecialityListModel[] newArray(int size) {
            return (new FilterDoctorSpecialityListModel[size]);
        }

    }
            ;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public DoctorSpecialitiesModel getDoctorSpecialitiesModel() {
        return doctorSpecialitiesModel;
    }

    public void setDoctorSpecialitiesModel(DoctorSpecialitiesModel doctorSpecialitiesModel) {
        this.doctorSpecialitiesModel = doctorSpecialitiesModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(doctorSpecialitiesModel);
    }

    public int describeContents() {
        return 0;
    }

}