package com.rescribe.model.dashboard_new_api;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardMenuList implements Parcelable
{

@SerializedName("name")
@Expose
private String name;
@SerializedName("iconImageUrl")
@Expose
private String iconImageUrl;
@SerializedName("clickEvent")
@Expose
private ClickEvent clickEvent;
@SerializedName("healthEducationUrl")
@Expose
private String healthEducationUrl;
public final static Parcelable.Creator<DashboardMenuList> CREATOR = new Creator<DashboardMenuList>() {


@SuppressWarnings({
"unchecked"
})
public DashboardMenuList createFromParcel(Parcel in) {
return new DashboardMenuList(in);
}

public DashboardMenuList[] newArray(int size) {
return (new DashboardMenuList[size]);
}

}
;

protected DashboardMenuList(Parcel in) {
this.name = ((String) in.readValue((String.class.getClassLoader())));
this.iconImageUrl = ((String) in.readValue((String.class.getClassLoader())));
this.clickEvent = ((ClickEvent) in.readValue((ClickEvent.class.getClassLoader())));
this.healthEducationUrl = ((String) in.readValue((String.class.getClassLoader())));
}

public DashboardMenuList() {
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

public ClickEvent getClickEvent() {
return clickEvent;
}

public void setClickEvent(ClickEvent clickEvent) {
this.clickEvent = clickEvent;
}

public String getHealthEducationUrl() {
return healthEducationUrl;
}

public void setHealthEducationUrl(String healthEducationUrl) {
this.healthEducationUrl = healthEducationUrl;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(name);
dest.writeValue(iconImageUrl);
dest.writeValue(clickEvent);
dest.writeValue(healthEducationUrl);
}

public int describeContents() {
return 0;
}

}