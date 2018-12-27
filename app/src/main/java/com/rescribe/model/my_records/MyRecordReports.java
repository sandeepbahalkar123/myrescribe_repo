package com.rescribe.model.my_records;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by riteshpandhurkar on 15/6/17.
 */

public class MyRecordReports implements CustomResponse, Serializable {

    @SerializedName("type")
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public class MyRecordReportList implements CustomResponse, Serializable {
        @SerializedName("childCaptionName")
        private String childCaptionName;
        @SerializedName("imageList")
        @Expose
        private ArrayList<ImageListData> imageList;

        public String getChildCaptionName() {
            return childCaptionName;
        }

        public void setChildCaptionName(String childCaptionName) {
            this.childCaptionName = childCaptionName;
        }

        public ArrayList<ImageListData> getImageList() {
            return imageList;
        }

        public void setImageList(ArrayList<ImageListData> imageList) {
            this.imageList = imageList;
        }
    }

    public class ImageListData implements CustomResponse ,Serializable{
        @SerializedName("id")
        private String imageId;
        @SerializedName("url")
        @Expose
        private String imageUrl;

        boolean isChecked;

        public String getImageId() {
            return imageId;
        }

        public void setImageId(String imageId) {
            this.imageId = imageId;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }
}
