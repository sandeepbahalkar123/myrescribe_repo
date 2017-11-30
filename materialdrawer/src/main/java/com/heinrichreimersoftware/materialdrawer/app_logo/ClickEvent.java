package com.heinrichreimersoftware.materialdrawer.app_logo;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;

public class ClickEvent implements Parcelable {


    private String bgImageUrl;

    private ArrayList<BottomSheetMenu> bottomSheetMenus = new ArrayList<BottomSheetMenu>();
    public final static Creator<ClickEvent> CREATOR = new Creator<ClickEvent>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ClickEvent createFromParcel(Parcel in) {
            return new ClickEvent(in);
        }

        public ClickEvent[] newArray(int size) {
            return (new ClickEvent[size]);
        }

    };

    protected ClickEvent(Parcel in) {
        this.bgImageUrl = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.bottomSheetMenus, (BottomSheetMenu.class.getClassLoader()));
    }

    public ClickEvent() {
    }

    public String getBgImageUrl() {
        return bgImageUrl;
    }

    public void setBgImageUrl(String bgImageUrl) {
        this.bgImageUrl = bgImageUrl;
    }

    public ArrayList<BottomSheetMenu> getBottomSheetMenus() {
        return bottomSheetMenus;
    }

    public void setBottomSheetMenus(ArrayList<BottomSheetMenu> bottomSheetMenus) {
        this.bottomSheetMenus = bottomSheetMenus;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(bgImageUrl);
        dest.writeList(bottomSheetMenus);
    }

    public int describeContents() {
        return 0;
    }


}