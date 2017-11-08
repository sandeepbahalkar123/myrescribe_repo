
package com.rescribe.model.dashboard_api;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardMenuList implements Parcelable
{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    public final static Creator<DashboardMenuList> CREATOR = new Creator<DashboardMenuList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DashboardMenuList createFromParcel(Parcel in) {
            return new DashboardMenuList(in);
        }

        public DashboardMenuList[] newArray(int size) {
            return (new DashboardMenuList[size]);
        }

    }
    ;

    protected DashboardMenuList(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.imageUrl = ((String) in.readValue((String.class.getClassLoader())));
    }

    public DashboardMenuList() {
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
