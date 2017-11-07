
package com.rescribe.model.dashboard_api;

import java.util.ArrayList;
import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardModel implements Parcelable
{

    @SerializedName("doctorList")
    @Expose
    private ArrayList<DashboardDoctorList> doctorList = new ArrayList<DashboardDoctorList>();
    @SerializedName("cardBgImageUrlList")
    @Expose
    private ArrayList<String> cardBgImageUrlList = new ArrayList<String>();
    @SerializedName("dashboardMenuList")
    @Expose
    private ArrayList<DashboardMenuList> dashboardMenuList = new ArrayList<DashboardMenuList>();
    @SerializedName("dashboardBottomMenuList")
    @Expose
    private ArrayList<DashboardBottomMenuList> dashboardBottomMenuList = new ArrayList<DashboardBottomMenuList>();
    @SerializedName("dashboardLeftSideDrawerMenuList")
    @Expose
    private ArrayList<DashboardLeftSideDrawerMenuList> dashboardLeftSideDrawerMenuList = new ArrayList<DashboardLeftSideDrawerMenuList>();
    public final static Creator<DashboardModel> CREATOR = new Creator<DashboardModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DashboardModel createFromParcel(Parcel in) {
            return new DashboardModel(in);
        }

        public DashboardModel[] newArray(int size) {
            return (new DashboardModel[size]);
        }

    }
    ;

    protected DashboardModel(Parcel in) {
        in.readList(this.doctorList, (DashboardDoctorList.class.getClassLoader()));
        in.readList(this.cardBgImageUrlList, (String.class.getClassLoader()));
        in.readList(this.dashboardMenuList, (DashboardMenuList.class.getClassLoader()));
        in.readList(this.dashboardBottomMenuList, (DashboardBottomMenuList.class.getClassLoader()));
        in.readList(this.dashboardLeftSideDrawerMenuList, (DashboardLeftSideDrawerMenuList.class.getClassLoader()));
    }

    public DashboardModel() {
    }

    public ArrayList<DashboardDoctorList> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(ArrayList<DashboardDoctorList> doctorList) {
        this.doctorList = doctorList;
    }

    public ArrayList<String> getCardBgImageUrlList() {
        return cardBgImageUrlList;
    }

    public void setCardBgImageUrlList(ArrayList<String> cardBgImageUrlList) {
        this.cardBgImageUrlList = cardBgImageUrlList;
    }

    public ArrayList<DashboardMenuList> getDashboardMenuList() {
        return dashboardMenuList;
    }

    public void setDashboardMenuList(ArrayList<DashboardMenuList> dashboardMenuList) {
        this.dashboardMenuList = dashboardMenuList;
    }

    public ArrayList<DashboardBottomMenuList> getDashboardBottomMenuList() {
        return dashboardBottomMenuList;
    }

    public void setDashboardBottomMenuList(ArrayList<DashboardBottomMenuList> dashboardBottomMenuList) {
        this.dashboardBottomMenuList = dashboardBottomMenuList;
    }

    public ArrayList<DashboardLeftSideDrawerMenuList> getDashboardLeftSideDrawerMenuList() {
        return dashboardLeftSideDrawerMenuList;
    }

    public void setDashboardLeftSideDrawerMenuList(ArrayList<DashboardLeftSideDrawerMenuList> dashboardLeftSideDrawerMenuList) {
        this.dashboardLeftSideDrawerMenuList = dashboardLeftSideDrawerMenuList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(doctorList);
        dest.writeList(cardBgImageUrlList);
        dest.writeList(dashboardMenuList);
        dest.writeList(dashboardBottomMenuList);
        dest.writeList(dashboardLeftSideDrawerMenuList);
    }

    public int describeContents() {
        return  0;
    }

}
