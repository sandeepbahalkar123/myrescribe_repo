
package com.rescribe.model.dashboard_api;

import java.util.ArrayList;
import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardModel {

    @SerializedName("doctorList")
    @Expose
    private ArrayList<DashboardDoctorList> dashboardDoctorList = new ArrayList<DashboardDoctorList>();
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

    public ArrayList<DashboardDoctorList> getDashboardDoctorList() {
        return dashboardDoctorList;
    }

    public void setDashboardDoctorList(ArrayList<DashboardDoctorList> dashboardDoctorList) {
        this.dashboardDoctorList = dashboardDoctorList;
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

}
