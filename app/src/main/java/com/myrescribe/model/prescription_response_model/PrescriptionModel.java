package com.myrescribe.model.prescription_response_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;


import java.util.List;

public class PrescriptionModel  implements CustomResponse {

@SerializedName("common")
@Expose
private PrescribeCommon common;
@SerializedName("data")
@Expose
private List<PrescriptionD> data = null;

public PrescribeCommon getCommon() {
return common;
}

public void setCommon(PrescribeCommon common) {
this.common = common;
}

public List<PrescriptionD> getData() {
return data;
}

public void setData(List<PrescriptionD> data) {
this.data = data;
}

}