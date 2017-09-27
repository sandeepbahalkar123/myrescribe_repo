package com.rescribe.model.vital_graph.vital_all_list;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VitalGraphList implements Parcelable {

    @SerializedName("vitalList")
    @Expose
    private ArrayList<VitalGraphData> vitalList = new ArrayList<VitalGraphData>();
    public final static Parcelable.Creator<VitalGraphList> CREATOR = new Creator<VitalGraphList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public VitalGraphList createFromParcel(Parcel in) {
            VitalGraphList instance = new VitalGraphList();
            in.readList(instance.vitalList, (VitalGraphData.class.getClassLoader()));
            return instance;
        }

        public VitalGraphList[] newArray(int size) {
            return (new VitalGraphList[size]);
        }

    };

    public ArrayList<VitalGraphData> getVitalList() {
        return vitalList;
    }

    public void setVitalList(ArrayList<VitalGraphData> vitalList) {
        this.vitalList = vitalList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(vitalList);
    }

    public int describeContents() {
        return 0;
    }

}