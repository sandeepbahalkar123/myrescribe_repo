
package com.rescribe.model.dashboard_api;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

public class DashBoardBaseModel implements Parcelable, CustomResponse
{

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private DashboardModel dashboardModel;
    public final static Creator<DashBoardBaseModel> CREATOR = new Creator<DashBoardBaseModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DashBoardBaseModel createFromParcel(Parcel in) {
            return new DashBoardBaseModel(in);
        }

        public DashBoardBaseModel[] newArray(int size) {
            return (new DashBoardBaseModel[size]);
        }

    }
    ;

    protected DashBoardBaseModel(Parcel in) {
        this.common = ((Common) in.readValue((Common.class.getClassLoader())));
        this.dashboardModel = ((DashboardModel) in.readValue((DashboardModel.class.getClassLoader())));
    }

    public DashBoardBaseModel() {
    }

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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(dashboardModel);
    }

    public int describeContents() {
        return  0;
    }

}
