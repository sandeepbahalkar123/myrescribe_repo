package com.rescribe.model.vital_graph.vital_tracker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;
import com.rescribe.model.vital_graph.vital_description.VitalGraphDetails;

import java.util.ArrayList;

public class VitalGraphTrackerBaseModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private VitalGraphTrackerDataModel vitalGraphTrakcerDataModel;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public VitalGraphTrackerDataModel getVitalGraphTrakcerDataModel() {
        return vitalGraphTrakcerDataModel;
    }

    public void setVitalGraphTrakcerDataModel(VitalGraphTrackerDataModel vitalGraphTrakcerDataModel) {
        this.vitalGraphTrakcerDataModel = vitalGraphTrakcerDataModel;
    }

    public class VitalGraphTrackerDataModel {
        @SerializedName("vitalTrackerlist")
        @Expose
        private ArrayList<VitalGraphTracker> vitalGraphTrackersList = new ArrayList<>();

        public ArrayList<VitalGraphTracker> getVitalGraphTrackersList() {
            return vitalGraphTrackersList;
        }

        public void setVitalGraphTrackersList(ArrayList<VitalGraphTracker> vitalGraphTrackersList) {
            this.vitalGraphTrackersList = vitalGraphTrackersList;
        }
    }

}