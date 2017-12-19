package com.heinrichreimersoftware.materialdrawer.bottom_menu;

/**
 * Created by ganeshshirole on 19/12/17.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class IconImage implements Parcelable {

    private String url = "";
    private String time = "";
    public final static Creator<IconImage> CREATOR = new Creator<IconImage>() {


        @SuppressWarnings({
                "unchecked"
        })
        public IconImage createFromParcel(Parcel in) {
            return new IconImage(in);
        }

        public IconImage[] newArray(int size) {
            return (new IconImage[size]);
        }

    };

    protected IconImage(Parcel in) {
        this.url = ((String) in.readValue((String.class.getClassLoader())));
        this.time = ((String) in.readValue((String.class.getClassLoader())));
    }

    public IconImage() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(url);
        dest.writeValue(time);
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return url;
    }
}