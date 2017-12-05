package com.heinrichreimersoftware.materialdrawer.app_logo;

import android.os.Parcel;
import android.os.Parcelable;


public class BottomSheetMenu implements Parcelable {


    private String name;
    private String iconImageUrl;
    private int notificationCount;

   // private ClickEvent clickEvent;
    public final static Creator<BottomSheetMenu> CREATOR = new Creator<BottomSheetMenu>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BottomSheetMenu createFromParcel(Parcel in) {
            return new BottomSheetMenu(in);
        }

        public BottomSheetMenu[] newArray(int size) {
            return (new BottomSheetMenu[size]);
        }

    };

    protected BottomSheetMenu(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.iconImageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.notificationCount = ((int) in.readValue((String.class.getClassLoader())));
        //this.clickEvent = ((ClickEvent) in.readValue((ClickEvent.class.getClassLoader())));
    }

    public BottomSheetMenu() {
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

    public int getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(int notificationCount) {
        this.notificationCount = notificationCount;
    }

    /* public ClickEvent getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }*/

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(iconImageUrl);
        dest.writeValue(notificationCount);
      //  dest.writeValue(clickEvent);
    }

    public int describeContents() {
        return 0;
    }

}