
package com.rescribe.model.dashboard_api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

public class DashBoardBaseModel implements CustomResponse{

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private DashboardModel dashboardModel;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public DashboardModel getDashboardModel() {
        return dashboardModel;
    }

    public void setDashboardModel(DashboardModel dashboardModel) {
        this.dashboardModel = dashboardModel;
    }

}
