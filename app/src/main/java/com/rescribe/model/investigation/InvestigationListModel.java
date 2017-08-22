package com.rescribe.model.investigation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

import java.util.ArrayList;

public class InvestigationListModel implements Parcelable, CustomResponse {

    public final static Parcelable.Creator<InvestigationListModel> CREATOR = new Creator<InvestigationListModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public InvestigationListModel createFromParcel(Parcel in) {
            InvestigationListModel instance = new InvestigationListModel();
            instance.common = ((Common) in.readValue((Common.class.getClassLoader())));
            in.readList(instance.data, (InvestigationData.class.getClassLoader()));
            return instance;
        }

        public InvestigationListModel[] newArray(int size) {
            return (new InvestigationListModel[size]);
        }

    };
    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private ArrayList<InvestigationData> data = new ArrayList<InvestigationData>();

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public ArrayList<InvestigationData> getData() {
        return data;
    }

    public void setData(ArrayList<InvestigationData> data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeList(data);
    }

    public int describeContents() {
        return 0;
    }

}