package com.rescribe.model.dashboard_new_api;

import java.util.ArrayList;
import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClickEvent implements Parcelable
{

@SerializedName("bgImageUrl")
@Expose
private String bgImageUrl;
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

}
;

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