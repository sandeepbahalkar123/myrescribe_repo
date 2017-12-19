package com.rescribe.model.dashboard_api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.IconImage;

public class ClickOption implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("iconImageUrl")
    @Expose
    private IconImage iconImageUrl;
    @SerializedName("clickEvent")
    @Expose
    private ClickEvent clickEvent;
    public final static Parcelable.Creator<ClickOption> CREATOR = new Creator<ClickOption>() {


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
        this.iconImageUrl = ((IconImage) in.readValue((IconImage.class.getClassLoader())));
        this.clickEvent = ((ClickEvent) in.readValue((ClickEvent.class.getClassLoader())));
    }

    public ClickOption() {
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

    public ClickEvent getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(iconImageUrl);
        dest.writeValue(clickEvent);
    }

    public int describeContents() {
        return 0;
    }

}