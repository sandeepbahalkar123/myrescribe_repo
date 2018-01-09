package com.heinrichreimersoftware.materialdrawer.app_logo;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;


public class BottomSheetMenu implements Parcelable {


    private String name;
    private Drawable iconImageUrl;
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
        this.iconImageUrl = ((Drawable) in.readValue((String.class.getClassLoader())));
        this.notificationCount = ((int) in.readValue((String.class.getClassLoader())));
    }

    public BottomSheetMenu() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIconImageUrl() {
        return iconImageUrl;
    }

    public void setIconImageUrl(Drawable iconImageUrl) {
        this.iconImageUrl = iconImageUrl;
    }

    public int getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(int notificationCount) {
        this.notificationCount = notificationCount;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(iconImageUrl);
        dest.writeValue(notificationCount);
    }

    public int describeContents() {
        return 0;
    }

}