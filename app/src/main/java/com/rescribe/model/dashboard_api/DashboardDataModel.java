
package com.rescribe.model.dashboard_api;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;

public class DashboardDataModel implements Parcelable {

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
    @SerializedName("dashboardLeftSideDrawerMenuList")
    @Expose
    private ArrayList<DashboardLeftSideDrawerMenuList> dashboardLeftSideDrawerMenuList = new ArrayList<DashboardLeftSideDrawerMenuList>();
    public final static Creator<DashboardDataModel> CREATOR = new Creator<DashboardDataModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DashboardDataModel createFromParcel(Parcel in) {
            return new DashboardDataModel(in);
        }

        public DashboardDataModel[] newArray(int size) {
            return (new DashboardDataModel[size]);
        }

    };

    protected DashboardDataModel(Parcel in) {
        in.readList(this.doctorList, (DoctorList.class.getClassLoader()));
        in.readList(this.cardBgImageUrlList, (String.class.getClassLoader()));
        in.readList(this.dashboardMenuList, (DashboardMenuList.class.getClassLoader()));
        in.readList(this.dashboardBottomMenuList, (DashboardBottomMenuList.class.getClassLoader()));
        in.readList(this.dashboardLeftSideDrawerMenuList, (DashboardLeftSideDrawerMenuList.class.getClassLoader()));
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

    public ArrayList<DashboardLeftSideDrawerMenuList> getDashboardLeftSideDrawerMenuList() {
        return dashboardLeftSideDrawerMenuList;
    }

    public void setDashboardLeftSideDrawerMenuList(ArrayList<DashboardLeftSideDrawerMenuList> dashboardLeftSideDrawerMenuList) {
        this.dashboardLeftSideDrawerMenuList = dashboardLeftSideDrawerMenuList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(doctorList);
        dest.writeList(cardBgImageUrlList);
        dest.writeList(dashboardMenuList);
        dest.writeList(dashboardBottomMenuList);
        dest.writeList(dashboardLeftSideDrawerMenuList);
    }

    public int describeContents() {
        return 0;
    }


    public ArrayList<DoctorList> getCategoryWiseDoctorList(String categoryName) {
        ArrayList<DoctorList> temp = new ArrayList<>();
        for (DoctorList docObject :
                doctorList) {
            if (categoryName.equalsIgnoreCase(docObject.getCategoryName())) {
                temp.add(docObject);
            }
        }
    /*    //-------
        int size = temp.size();
        for (DoctorList docObject :
                temp) {
            docObject.setSizeOfList(size);
        }
        //-------*/
        return temp;
    }

    public ArrayList<DoctorList> getFavouriteDocList() {
        ArrayList<DoctorList> temp = new ArrayList<>();
        for (DoctorList docObject :
                doctorList) {
            if (docObject.getFavourite()) {
                temp.add(docObject);
            }
        }
        return temp;
    }

    public void replaceDoctorListById(String docId, DoctorList docObjectToReplace, String objectUpdateType) {
        ArrayList<DoctorList> newListToUpdateTempDoctorList = new ArrayList<>(doctorList);
        boolean isUpdated = false;
        for (int i = 0; i < doctorList.size(); i++) {
            DoctorList tempObject = doctorList.get(i);
            if (docId.equalsIgnoreCase("" + tempObject.getDocId())) {
                isUpdated = true;
                newListToUpdateTempDoctorList.set(i, docObjectToReplace);
            }
        }

        if (isUpdated) {
            setDoctorList(newListToUpdateTempDoctorList);
        }
    }

    public DoctorList findDoctorListById(String docId) {
        for (int i = 0; i < doctorList.size(); i++) {
            DoctorList tempObject = doctorList.get(i);
            if (docId.equalsIgnoreCase("" + tempObject.getDocId())) {
                return tempObject;
            }
        }
        return null;
    }
}
