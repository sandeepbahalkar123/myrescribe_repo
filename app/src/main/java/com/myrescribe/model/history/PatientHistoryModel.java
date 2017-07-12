
package com.myrescribe.model.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.model.Common;

public class PatientHistoryModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private PatientHistoryData historyDataInstance;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public PatientHistoryData getHistoryDataInstance() {
        return historyDataInstance;
    }

    public void setHistoryDataInstance(PatientHistoryData historyDataInstance) {
        this.historyDataInstance = historyDataInstance;
    }
}
