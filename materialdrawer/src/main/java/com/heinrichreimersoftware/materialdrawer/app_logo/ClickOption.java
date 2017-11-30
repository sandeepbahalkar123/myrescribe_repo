package com.heinrichreimersoftware.materialdrawer.app_logo;

import android.os.Parcel;
import android.os.Parcelable;


public class ClickOption implements Parcelable {


    private String name;

    private String iconImageUrl;

   // private ClickEvent clickEvent;
    public final static Creator<ClickOption> CREATOR = new Creator<ClickOption>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ClickOption createFromParcel(Parcel in) {
            return new ClickOption(in);
        }

        public ClickOption[] newArray(int size) {
            return (new ClickOption[size]);
        }

    };

    protected ClickOption(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.iconImageUrl = ((String) in.readValue((String.class.getClassLoader())));
        //this.clickEvent = ((ClickEvent) in.readValue((ClickEvent.class.getClassLoader())));
    }

    public ClickOption() {
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

   /* public ClickEvent getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }*/

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(iconImageUrl);
      //  dest.writeValue(clickEvent);
    }

    public int describeContents() {
        return 0;
    }

}