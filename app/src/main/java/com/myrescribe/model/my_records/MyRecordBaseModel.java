
package com.myrescribe.model.my_records;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.model.Common;

public class MyRecordBaseModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private MyRecordDataModel recordMainDataModel;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public MyRecordDataModel getRecordMainDataModel() {
        return recordMainDataModel;
    }

    public void setRecordMainDataModel(MyRecordDataModel recordMainDataModel) {
        this.recordMainDataModel = recordMainDataModel;
    }

    @Override
    public String toString() {
        return "DoctorModel{" +
                "common=" + common +
                ", recordMainDataModel=" + recordMainDataModel +
                '}';
    }
}
