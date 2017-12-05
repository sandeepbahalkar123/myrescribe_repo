package com.heinrichreimersoftware.materialdrawer.bottom_menu;

import android.os.Parcel;
import android.os.Parcelable;

public class BottomMenu implements Parcelable {

    private String menuName = "";
    private String menuIcon = "";
    private boolean isSelected;
    private boolean isAppIcon;
    private int notificationCount;

    public final static Creator<BottomMenu> CREATOR = new Creator<BottomMenu>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BottomMenu createFromParcel(Parcel in) {
            return new BottomMenu(in);
        }

        public BottomMenu[] newArray(int size) {
            return (new BottomMenu[size]);
        }

    }
            ;

    protected BottomMenu(Parcel in) {
        this.menuName = ((String) in.readValue((String.class.getClassLoader())));
        this.menuIcon = ((String) in.readValue((String.class.getClassLoader())));
        this.isSelected = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.isAppIcon = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.notificationCount = ((int) in.readValue((boolean.class.getClassLoader())));
    }

    public BottomMenu() {
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isAppIcon() {
        return isAppIcon;
    }

    public void setAppIcon(boolean appIcon) {
        isAppIcon = appIcon;
    }

    public int getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(int notificationCount) {
        this.notificationCount = notificationCount;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(menuName);
        dest.writeValue(menuIcon);
        dest.writeValue(isSelected);
        dest.writeValue(isAppIcon);
        dest.writeValue(notificationCount);
    }

    public int describeContents() {
        return 0;
    }

}