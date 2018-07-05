package com.rescribe.model.dashboard_api.card_data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CategoryList {

    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("docDetails")
    @Expose
    private List<DocDetail> docDetails = new ArrayList<DocDetail>();

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<DocDetail> getDocDetails() {
        return docDetails;
    }

    public void setDocDetails(List<DocDetail> docDetails) {
        this.docDetails = docDetails;
    }

}