package com.rescribe.model.dashboard_api.cardviewdatamodel;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryList implements Parcelable {

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
    public final static Parcelable.Creator<CategoryList> CREATOR = new Creator<CategoryList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CategoryList createFromParcel(Parcel in) {
            return new CategoryList(in);
        }

        public CategoryList[] newArray(int size) {
            return (new CategoryList[size]);
        }

    };

    protected CategoryList(Parcel in) {
        this.categoryName = ((String) in.readValue((String.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
        this.time = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.docDetails, (com.rescribe.model.dashboard_api.cardviewdatamodel.DocDetail.class.getClassLoader()));
    }

    public CategoryList() {
    }

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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(categoryName);
        dest.writeValue(url);
        dest.writeValue(time);
        dest.writeList(docDetails);
    }

    public int describeContents() {
        return 0;
    }

}