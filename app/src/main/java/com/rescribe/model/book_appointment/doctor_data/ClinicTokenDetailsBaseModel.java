
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
    private ClinicTokenData clinicTokenData;
    public final static Creator<ClinicTokenDetailsBaseModel> CREATOR = new Creator<ClinicTokenDetailsBaseModel>() {

        @SuppressWarnings({
                "unchecked"
        })
        public ClinicTokenDetailsBaseModel createFromParcel(Parcel in) {
            ClinicTokenDetailsBaseModel instance = new ClinicTokenDetailsBaseModel();
            instance.common = ((Common) in.readValue((Common.class.getClassLoader())));
            instance.clinicTokenData = ((ClinicTokenData) in.readValue((ClinicTokenData.class.getClassLoader())));
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

    public ClinicTokenData getClinicTokenDetails() {
        return clinicTokenData;
    }

    public void setClinicTokenDetails(ClinicTokenData clinicTokenDetails) {
        this.clinicTokenData = clinicTokenDetails;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(clinicTokenData);
    }

    public int describeContents() {
        return 0;
    }
}
