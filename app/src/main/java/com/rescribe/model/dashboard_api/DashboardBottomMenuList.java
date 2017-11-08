
package com.rescribe.model.dashboard_api;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardBottomMenuList implements Parcelable
{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    public final static Creator<DashboardBottomMenuList> CREATOR = new Creator<DashboardBottomMenuList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DashboardBottomMenuList createFromParcel(Parcel in) {
            return new DashboardBottomMenuList(in);
        }

        public DashboardBottomMenuList[] newArray(int size) {
            return (new DashboardBottomMenuList[size]);
        }

    }
    ;

    protected DashboardBottomMenuList(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.imageUrl = ((String) in.readValue((String.class.getClassLoader())));
    }

    public DashboardBottomMenuList() {
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
