package com.rescribe.model.dashboard_api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.IconImage;

import java.util.ArrayList;

public class ClickEvent implements Parcelable {

    @SerializedName("bgImageUrl")
    @Expose
    private IconImage bgImageUrl;
    @SerializedName("clickOptions")
    @Expose
    private ArrayList<ClickOption> clickOptions = new ArrayList<ClickOption>();
    public final static Parcelable.Creator<ClickEvent> CREATOR = new Creator<ClickEvent>() {


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
        this.bgImageUrl = ((IconImage) in.readValue((IconImage.class.getClassLoader())));
        in.readList(this.clickOptions, (ClickOption.class.getClassLoader()));
    }

    public ClickEvent() {
    }

    public IconImage getBgImageUrl() {
        return bgImageUrl;
    }

    public void setBgImageUrl(IconImage bgImageUrl) {
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