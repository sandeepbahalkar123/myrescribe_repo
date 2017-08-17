package com.myrescribe.model.my_records;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by riteshpandhurkar on 15/6/17.
 */

public class MyRecordReports implements CustomResponse {

    @SerializedName("parentCaptionName")
    private String parentCaptionName;
    @SerializedName("reportDetails")
    private ArrayList<MyRecordReportList> reportList;

    public String getParentCaptionName() {
        return parentCaptionName;
    }

    public void setParentCaptionName(String parentCaptionName) {
        this.parentCaptionName = parentCaptionName;
    }

    public ArrayList<MyRecordReportList> getReportList() {
        return reportList;
    }

    public void setReportList(ArrayList<MyRecordReportList> reportList) {
        this.reportList = reportList;
    }

    public class MyRecordReportList implements CustomResponse {
        @SerializedName("childCaptionName")
        private String childCaptionName;
        @SerializedName("imageList")
        @Expose
        private String[] imageList;

        public String getChildCaptionName() {
            return childCaptionName;
        }

        public void setChildCaptionName(String childCaptionName) {
            this.childCaptionName = childCaptionName;
        }

        public String[] getImageList() {
            return imageList;
        }

        public void setImageList(String[] imageList) {
            this.imageList = imageList;
        }
    }
}
