
package com.rescribe.model.dashboard_api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardBottomMenuList implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("iconImageUrl")
    @Expose
    private String iconImageUrl;
    @SerializedName("selectedIconImageUrl")
    @Expose
    private String selectedIconImageUrl;
    @SerializedName("clickEvent")
    @Expose
    private ClickEvent clickEvent;
    public final static Parcelable.Creator<DashboardBottomMenuList> CREATOR = new Creator<DashboardBottomMenuList>() {

        public DashboardBottomMenuList createFromParcel(Parcel in) {
            return new DashboardBottomMenuList(in);
        }

        public DashboardBottomMenuList[] newArray(int size) {
            return (new DashboardBottomMenuList[size]);
        }

    };

    protected DashboardBottomMenuList(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.iconImageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.selectedIconImageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.clickEvent = ((ClickEvent) in.readValue((ClickEvent.class.getClassLoader())));
    }

    public DashboardBottomMenuList() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconImageUrl() {
        return iconImageUrl;
    }

    public void setIconImageUrl(String iconImageUrl) {
        this.iconImageUrl = iconImageUrl;
    }

    public String getSelectedIconImageUrl() {
        return selectedIconImageUrl;
    }

    public void setSelectedIconImageUrl(String selectedIconImageUrl) {
        this.selectedIconImageUrl = selectedIconImageUrl;
    }

    public ClickEvent getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(iconImageUrl);
        dest.writeValue(selectedIconImageUrl);
        dest.writeValue(clickEvent);
    }

    public int describeContents() {
        return 0;
    }

}
