
package com.rescribe.model.book_appointment.search_doctors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;


public class RecentVisitedBaseModel implements CustomResponse{

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private RecentVisitedModel recentVisitedModel;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public RecentVisitedModel getRecentVisitedModel() {
        return recentVisitedModel;
    }

    public void setRecentVisitedModel(RecentVisitedModel recentVisitedModel) {
        this.recentVisitedModel = recentVisitedModel;
    }



}
