
package com.rescribe.model.book_appointment.doctor_data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

public class ClinicTokenDetailsBaseModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private ClinicTokenDataModel clinicTokenDataModel;
    public final static Creator<ClinicTokenDetailsBaseModel> CREATOR = new Creator<ClinicTokenDetailsBaseModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ClinicTokenDetailsBaseModel createFromParcel(Parcel in) {
            ClinicTokenDetailsBaseModel instance = new ClinicTokenDetailsBaseModel();
            instance.common = ((Common) in.readValue((Common.class.getClassLoader())));
            instance.clinicTokenDataModel = ((ClinicTokenDataModel) in.readValue((ClinicTokenDataModel.class.getClassLoader())));
            return instance;
        }

        public ClinicTokenDetailsBaseModel[] newArray(int size) {
            return (new ClinicTokenDetailsBaseModel[size]);
        }

    };

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public ClinicTokenDataModel getClinicTokenDataModel() {
        return clinicTokenDataModel;
    }

    public void setClinicTokenDataModel(ClinicTokenDataModel clinicTokenDataModel) {
        this.clinicTokenDataModel = clinicTokenDataModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(clinicTokenDataModel);
    }

    public int describeContents() {
        return 0;
    }


    public class ClinicTokenDataModel implements Parcelable {

        @SerializedName("tokenDetails")
        @Expose
        private ClinicTokenDetails clinicTokenDetails;

        public final Creator<ClinicTokenDataModel> CREATOR = new Creator<ClinicTokenDataModel>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public ClinicTokenDataModel createFromParcel(Parcel in) {
                ClinicTokenDataModel instance = new ClinicTokenDataModel();
                instance.clinicTokenDetails = ((ClinicTokenDetails) in.readValue((ClinicTokenDetails.class.getClassLoader())));
                return instance;
            }

            public ClinicTokenDataModel[] newArray(int size) {
                return (new ClinicTokenDataModel[size]);
            }

        };

        public ClinicTokenDetails getClinicTokenDetails() {
            return clinicTokenDetails;
        }

        public void setClinicTokenDetails(ClinicTokenDetails clinicTokenDetails) {
            this.clinicTokenDetails = clinicTokenDetails;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(clinicTokenDetails);
        }
    }
}
