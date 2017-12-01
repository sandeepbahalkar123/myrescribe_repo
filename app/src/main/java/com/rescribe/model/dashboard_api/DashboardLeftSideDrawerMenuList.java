
package com.rescribe.model.dashboard_api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardLeftSideDrawerMenuList implements Parcelable
{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    public final static Creator<DashboardLeftSideDrawerMenuList> CREATOR = new Creator<DashboardLeftSideDrawerMenuList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DashboardLeftSideDrawerMenuList createFromParcel(Parcel in) {
            return new DashboardLeftSideDrawerMenuList(in);
        }

        public DashboardLeftSideDrawerMenuList[] newArray(int size) {
            return (new DashboardLeftSideDrawerMenuList[size]);
        }

    }
    ;

    protected DashboardLeftSideDrawerMenuList(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.imageUrl = ((String) in.readValue((String.class.getClassLoader())));
    }

    public DashboardLeftSideDrawerMenuList() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(imageUrl);
    }

    public int describeContents() {
        return  0;
    }

}
