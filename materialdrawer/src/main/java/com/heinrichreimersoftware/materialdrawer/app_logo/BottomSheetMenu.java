package com.heinrichreimersoftware.materialdrawer.app_logo;

import android.os.Parcel;
import android.os.Parcelable;

import com.heinrichreimersoftware.materialdrawer.bottom_menu.IconImage;


public class BottomSheetMenu implements Parcelable {


    private String name;
    private IconImage iconImageUrl;
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
        this.iconImageUrl = ((IconImage) in.readValue((IconImage.class.getClassLoader())));
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

    public IconImage getIconImageUrl() {
        return iconImageUrl;
    }

    public void setIconImageUrl(IconImage iconImageUrl) {
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