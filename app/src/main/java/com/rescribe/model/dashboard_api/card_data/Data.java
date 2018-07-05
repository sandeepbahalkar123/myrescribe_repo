package com.rescribe.model.dashboard_api.card_data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Data {

    @SerializedName("categoryList")
    @Expose
    private List<CategoryList> categoryList = new ArrayList<CategoryList>();
    @SerializedName("isDocUpdated")
    @Expose
    private boolean isDocUpdated;

    public List<CategoryList> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryList> categoryList) {
        this.categoryList = categoryList;
    }

    public boolean isIsDocUpdated() {
        return isDocUpdated;
    }

    public void setIsDocUpdated(boolean isDocUpdated) {
        this.isDocUpdated = isDocUpdated;
    }

}