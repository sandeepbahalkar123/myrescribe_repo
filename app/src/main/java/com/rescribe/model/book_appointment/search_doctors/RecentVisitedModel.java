
package com.rescribe.model.book_appointment.search_doctors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RecentVisitedModel {


    @SerializedName("areaList")
    @Expose
    private ArrayList<AreaList> areaList = new ArrayList<AreaList>();
    @SerializedName("recentlyVisitedAreaList")
    @Expose
    private ArrayList<RecentlyVisitedAreaList> recentlyVisitedAreaList = new ArrayList<RecentlyVisitedAreaList>();

    public ArrayList<AreaList> getAreaList() {
        return areaList;
    }

    public void setAreaList(ArrayList<AreaList> areaList) {
        this.areaList = areaList;
    }

    public ArrayList<RecentlyVisitedAreaList> getRecentlyVisitedAreaList() {
        return recentlyVisitedAreaList;
    }

    public void setRecentlyVisitedAreaList(ArrayList<RecentlyVisitedAreaList> recentlyVisitedAreaList) {
        this.recentlyVisitedAreaList = recentlyVisitedAreaList;
    }

}
