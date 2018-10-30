package com.rescribe.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class UploadStatus implements Parcelable {
    private String uploadId;
    private int status;
    private  String data;
    private int docId;
    private int opdId;
    String visitDate;
    private HashMap<String, String> headerMap = new HashMap<>();


    public UploadStatus(String uploadId, int status, String data, int docId, int opdId, String visitDate, HashMap<String, String> headerMap) {
        this.uploadId = uploadId;
        this.status = status;
        this.data = data;
        this.docId = docId;
        this.opdId = opdId;
        this.visitDate = visitDate;
        this.headerMap = headerMap;
    }

    protected UploadStatus(Parcel in) {
        uploadId = in.readString();
        status = in.readInt();
        data = in.readString();
        docId = in.readInt();
        opdId = in.readInt();
        visitDate = in.readString();
        this.headerMap = (HashMap<String, String>) in.readSerializable();
    }

    public static final Creator<UploadStatus> CREATOR = new Creator<UploadStatus>() {
        @Override
        public UploadStatus createFromParcel(Parcel in) {
            return new UploadStatus(in);
        }

        @Override
        public UploadStatus[] newArray(int size) {
            return new UploadStatus[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uploadId);
        parcel.writeInt(status);
        parcel.writeString(data);
        parcel.writeInt(docId);
        parcel.writeInt(opdId);
        parcel.writeString(visitDate);
        parcel.writeSerializable(this.headerMap);
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public int getOpdId() {
        return opdId;
    }

    public void setOpdId(int opdId) {
        this.opdId = opdId;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public HashMap<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(HashMap<String, String> headerMap) {
        this.headerMap = headerMap;
    }
}
