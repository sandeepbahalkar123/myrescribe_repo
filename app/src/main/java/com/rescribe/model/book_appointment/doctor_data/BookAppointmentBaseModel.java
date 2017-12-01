
package com.rescribe.model.book_appointment.doctor_data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

public class BookAppointmentBaseModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private DoctorServicesModel doctorServicesModel;
    public final static Creator<BookAppointmentBaseModel> CREATOR = new Creator<BookAppointmentBaseModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BookAppointmentBaseModel createFromParcel(Parcel in) {
            BookAppointmentBaseModel instance = new BookAppointmentBaseModel();
            instance.common = ((Common) in.readValue((Common.class.getClassLoader())));
            instance.doctorServicesModel = ((DoctorServicesModel) in.readValue((DoctorServicesModel.class.getClassLoader())));
            return instance;
        }

        public BookAppointmentBaseModel[] newArray(int size) {
            return (new BookAppointmentBaseModel[size]);
        }

    };

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public DoctorServicesModel getDoctorServicesModel() {
        return doctorServicesModel;
    }

    public void setDoctorServicesModel(DoctorServicesModel doctorServicesModel) {
        this.doctorServicesModel = doctorServicesModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(doctorServicesModel);
    }

    public int describeContents() {
        return 0;
    }

}
