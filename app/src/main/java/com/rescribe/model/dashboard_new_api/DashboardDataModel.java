package com.rescribe.model.dashboard_new_api;

import java.util.ArrayList;
import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;

public class DashboardDataModel implements Parcelable
{

@SerializedName("doctorList")
@Expose
private ArrayList<DoctorList> doctorList = new ArrayList<DoctorList>();
@SerializedName("cardBgImageUrlList")
@Expose
private ArrayList<String> cardBgImageUrlList = new ArrayList<String>();
@SerializedName("dashboardMenuList")
@Expose
private ArrayList<DashboardMenuList> dashboardMenuList = new ArrayList<DashboardMenuList>();
@SerializedName("dashboardBottomMenuList")
@Expose
private ArrayList<DashboardBottomMenuList> dashboardBottomMenuList = new ArrayList<DashboardBottomMenuList>();
public final static Parcelable.Creator<DashboardDataModel> CREATOR = new Creator<DashboardDataModel>() {


@SuppressWarnings({
"unchecked"
})
public DashboardDataModel createFromParcel(Parcel in) {
return new DashboardDataModel(in);
}

public DashboardDataModel[] newArray(int size) {
return (new DashboardDataModel[size]);
}

}
;

protected DashboardDataModel(Parcel in) {
in.readList(this.doctorList, (DoctorList.class.getClassLoader()));
in.readList(this.cardBgImageUrlList, (String.class.getClassLoader()));
in.readList(this.dashboardMenuList, (DashboardMenuList.class.getClassLoader()));
in.readList(this.dashboardBottomMenuList, (DashboardBottomMenuList.class.getClassLoader()));
}

public DashboardDataModel() {
}

public ArrayList<DoctorList> getDoctorList() {
return doctorList;
}

public void setDoctorList(ArrayList<DoctorList> doctorList) {
this.doctorList = doctorList;
}

public ArrayList<String> getCardBgImageUrlList() {
return cardBgImageUrlList;
}

public void setCardBgImageUrlList(ArrayList<String> cardBgImageUrlList) {
this.cardBgImageUrlList = cardBgImageUrlList;
}

public ArrayList<DashboardMenuList> getDashboardMenuList() {
return dashboardMenuList;
}

public void setDashboardMenuList(ArrayList<DashboardMenuList> dashboardMenuList) {
this.dashboardMenuList = dashboardMenuList;
}

public ArrayList<DashboardBottomMenuList> getDashboardBottomMenuList() {
return dashboardBottomMenuList;
}

public void setDashboardBottomMenuList(ArrayList<DashboardBottomMenuList> dashboardBottomMenuList) {
this.dashboardBottomMenuList = dashboardBottomMenuList;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeList(doctorList);
dest.writeList(cardBgImageUrlList);
dest.writeList(dashboardMenuList);
dest.writeList(dashboardBottomMenuList);
}

public int describeContents() {
return 0;
}

}