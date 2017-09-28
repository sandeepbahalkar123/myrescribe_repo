
package com.rescribe.model.book_appointment.complaints;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

public class ComplaintsBaseModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private ComplaintsModel complaintsModel;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public ComplaintsModel getComplaintsModel() {
        return complaintsModel;
    }

    public void setComplaintsModel(ComplaintsModel complaintsModel) {
        this.complaintsModel = complaintsModel;
    }

}
