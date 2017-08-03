
package com.myrescribe.model.case_details;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;

public class PatientHistory implements CustomResponse{

    @SerializedName("caseDetailId")
    @Expose
    private Integer caseDetailId;
    @SerializedName("caseDetailName")
    @Expose
    private String caseDetailName;
    @SerializedName("caseDetailData")
    @Expose
    private List<CommonData>  caseDetailData = null;
    @SerializedName("vitals")
    @Expose
    private List<Vital> vitals = null;

    public Integer getCaseDetailId() {
        return caseDetailId;
    }

    public void setCaseDetailId(Integer caseDetailId) {
        this.caseDetailId = caseDetailId;
    }

    public String getCaseDetailName() {
        return caseDetailName;
    }

    public void setCaseDetailName(String caseDetailName) {
        this.caseDetailName = caseDetailName;
    }

    public List<CommonData> getCommonData() {
        return  caseDetailData;
    }

    public void setCommonData(List<CommonData>  caseDetailData) {
        this. caseDetailData =  caseDetailData;
    }

    public List<Vital> getVitals() {
        return vitals;
    }

    public void setVitals(List<Vital> vitals) {
        this.vitals = vitals;
    }

}
