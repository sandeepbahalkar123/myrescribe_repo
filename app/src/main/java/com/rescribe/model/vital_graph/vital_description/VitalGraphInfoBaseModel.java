package com.rescribe.model.vital_graph.vital_description;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

import java.util.ArrayList;

public class VitalGraphInfoBaseModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private VitalGraphInfoDataModel vitalGraphInfoDataModel;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }


    public VitalGraphInfoDataModel getVitalGraphInfoDataModel() {
        return vitalGraphInfoDataModel;
    }

    public void setVitalGraphInfoDataModel(VitalGraphInfoDataModel vitalGraphInfoDataModel) {
        this.vitalGraphInfoDataModel = vitalGraphInfoDataModel;
    }

    public class VitalGraphInfoDataModel {
        @SerializedName("doAndDonts")
        @Expose
        private String description;
        @SerializedName("vitalDetail")
        @Expose
        private ArrayList<VitalGraphDetails> vitalGraphDetailList = new ArrayList<>();

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public ArrayList<VitalGraphDetails> getVitalGraphDetailList() {
            return vitalGraphDetailList;
        }

        public void setVitalGraphDetailList(ArrayList<VitalGraphDetails> vitalGraphDetailList) {
            this.vitalGraphDetailList = vitalGraphDetailList;
        }
    }

}