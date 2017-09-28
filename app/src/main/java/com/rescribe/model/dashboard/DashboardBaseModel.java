
package com.rescribe.model.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

public class DashboardBaseModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private DashboardDataModel dashboardDashModel;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public DashboardDataModel getDashboardDataModel() {
        return dashboardDashModel;
    }

    public void setDashboardDataModel(DashboardDataModel dashboardDashModel) {
        this.dashboardDashModel = dashboardDashModel;
    }
}
