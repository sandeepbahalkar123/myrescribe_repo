package com.rescribe.model.dashboard_api.cardviewdatamodel;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable {

    @SerializedName("categoryList")
    @Expose
    private List<CategoryList> categoryList = new ArrayList<CategoryList>();
    @SerializedName("isDocUpdated")
    @Expose
    private boolean isDocUpdated;
    public final static Parcelable.Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    };

    protected Data(Parcel in) {
        in.readList(this.categoryList, (com.rescribe.model.dashboard_api.cardviewdatamodel.CategoryList.class.getClassLoader()));
        this.isDocUpdated = ((boolean) in.readValue((boolean.class.getClassLoader())));
    }

    public Data() {
    }

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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(categoryList);
        dest.writeValue(isDocUpdated);
    }

    public int describeContents() {
        return 0;
    }

}