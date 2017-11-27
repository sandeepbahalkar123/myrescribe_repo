package com.rescribe.model.dashboard_new_api;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

public class DashBoardBaseModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("DashboardDataModel")
    @Expose
    private DashboardDataModel dashboardDataModel;
    public final static Parcelable.Creator<DashBoardBaseModel> CREATOR = new Creator<DashBoardBaseModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DashBoardBaseModel createFromParcel(Parcel in) {
            return new DashBoardBaseModel(in);
        }

        public DashBoardBaseModel[] newArray(int size) {
            return (new DashBoardBaseModel[size]);
        }

    };

    protected DashBoardBaseModel(Parcel in) {
        this.common = ((Common) in.readValue((Common.class.getClassLoader())));
        this.dashboardDataModel = ((DashboardDataModel) in.readValue((DashboardDataModel.class.getClassLoader())));
    }

    public DashBoardBaseModel() {
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public DashboardDataModel getDashboardDataModel() {
        return dashboardDataModel;
    }

    public void setDashboardDataModel(DashboardDataModel dashboardDataModel) {
        this.dashboardDataModel = dashboardDataModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(dashboardDataModel);
    }

    public int describeContents() {
        return 0;
    }

}
