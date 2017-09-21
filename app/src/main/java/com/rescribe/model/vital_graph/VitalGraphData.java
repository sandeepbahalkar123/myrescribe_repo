package com.rescribe.model.vital_graph;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.ArrayList;

public class VitalGraphData implements Parcelable {

    @SerializedName("vitalList")
    @Expose
    private ArrayList<VitalList> vitalList = new ArrayList<VitalList>();
    public final static Parcelable.Creator<VitalGraphData> CREATOR = new Creator<VitalGraphData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public VitalGraphData createFromParcel(Parcel in) {
            VitalGraphData instance = new VitalGraphData();
            in.readList(instance.vitalList, (VitalList.class.getClassLoader()));
            return instance;
        }

        public VitalGraphData[] newArray(int size) {
            return (new VitalGraphData[size]);
        }

    };

    public ArrayList<VitalList> getVitalList() {
        return vitalList;
    }

    public void setVitalList(ArrayList<VitalList> vitalList) {
        this.vitalList = vitalList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(vitalList);
    }

    public int describeContents() {
        return 0;
    }

}