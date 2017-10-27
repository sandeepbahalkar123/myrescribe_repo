
package com.rescribe.model.dashboard_api;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardModel {

    @SerializedName("doctorList")
    @Expose
    private List<DashboardDoctorList> dashboardDoctorList = new ArrayList<DashboardDoctorList>();
    @SerializedName("cardBgImageUrlList")
    @Expose
    private List<String> cardBgImageUrlList = new ArrayList<String>();
    @SerializedName("dashboardMenuList")
    @Expose
    private List<DashboardMenuList> dashboardMenuList = new ArrayList<DashboardMenuList>();
    @SerializedName("dashboardBottomMenuList")
    @Expose
    private List<DashboardBottomMenuList> dashboardBottomMenuList = new ArrayList<DashboardBottomMenuList>();
    @SerializedName("dashboardLeftSideDrawerMenuList")
    @Expose
    private List<DashboardLeftSideDrawerMenuList> dashboardLeftSideDrawerMenuList = new ArrayList<DashboardLeftSideDrawerMenuList>();

    public List<DashboardDoctorList> getDashboardDoctorList() {
        return dashboardDoctorList;
    }

    public void setDashboardDoctorList(List<DashboardDoctorList> dashboardDoctorList) {
        this.dashboardDoctorList = dashboardDoctorList;
    }

    public List<String> getCardBgImageUrlList() {
        return cardBgImageUrlList;
    }

    public void setCardBgImageUrlList(List<String> cardBgImageUrlList) {
        this.cardBgImageUrlList = cardBgImageUrlList;
    }

    public List<DashboardMenuList> getDashboardMenuList() {
        return dashboardMenuList;
    }

    public void setDashboardMenuList(List<DashboardMenuList> dashboardMenuList) {
        this.dashboardMenuList = dashboardMenuList;
    }

    public List<DashboardBottomMenuList> getDashboardBottomMenuList() {
        return dashboardBottomMenuList;
    }

    public void setDashboardBottomMenuList(List<DashboardBottomMenuList> dashboardBottomMenuList) {
        this.dashboardBottomMenuList = dashboardBottomMenuList;
    }

    public List<DashboardLeftSideDrawerMenuList> getDashboardLeftSideDrawerMenuList() {
        return dashboardLeftSideDrawerMenuList;
    }

    public void setDashboardLeftSideDrawerMenuList(List<DashboardLeftSideDrawerMenuList> dashboardLeftSideDrawerMenuList) {
        this.dashboardLeftSideDrawerMenuList = dashboardLeftSideDrawerMenuList;
    }

}
