package com.heinrichreimersoftware.materialdrawer.app_logo;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;

public class ClickEvent implements Parcelable {


    private String bgImageUrl;

    private ArrayList<ClickOption> clickOptions = new ArrayList<ClickOption>();
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
        in.readList(this.clickOptions, (ClickOption.class.getClassLoader()));
    }

    public ClickEvent() {
    }

    public String getBgImageUrl() {
        return bgImageUrl;
    }

    public void setBgImageUrl(String bgImageUrl) {
        this.bgImageUrl = bgImageUrl;
    }

    public ArrayList<ClickOption> getClickOptions() {
        return clickOptions;
    }

    public void setClickOptions(ArrayList<ClickOption> clickOptions) {
        this.clickOptions = clickOptions;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(bgImageUrl);
        dest.writeList(clickOptions);
    }

    public int describeContents() {
        return 0;
    }


}