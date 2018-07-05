package com.rescribe.model.dashboard_api.cardviewdatamodel;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class CardViewDataModel implements Parcelable,CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private Data data;
    public final static Parcelable.Creator<CardViewDataModel> CREATOR = new Creator<CardViewDataModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CardViewDataModel createFromParcel(Parcel in) {
            return new CardViewDataModel(in);
        }

        public CardViewDataModel[] newArray(int size) {
            return (new CardViewDataModel[size]);
        }

    };

    protected CardViewDataModel(Parcel in) {
        this.common = ((Common) in.readValue((Common.class.getClassLoader())));
        this.data = ((Data) in.readValue((Data.class.getClassLoader())));
    }

    public CardViewDataModel() {
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(data);
    }

    public int describeContents() {
        return 0;
    }

}