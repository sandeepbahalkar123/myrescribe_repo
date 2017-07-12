package com.myrescribe.model.visit_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.model.prescription_response_model.PrescribeCommon;

/**
 * Created by jeetal on 11/7/17.
 */

public class VisitDetailsModel {

    @SerializedName("common")
    @Expose
    private PrescribeCommon common;
    @SerializedName("data")
    @Expose
    private Data data;

    public PrescribeCommon getCommon() {
        return common;
    }

    public void setCommon(PrescribeCommon common) {
        this.common = common;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
