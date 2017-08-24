package com.rescribe.model.prescription_response_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;


import java.util.List;

public class PrescriptionModel  implements CustomResponse {

@SerializedName("common")
@Expose
private Common common;
@SerializedName("data")
@Expose
private List<PrescriptionData> data = null;

public Common getCommon() {
return common;
}

public void setCommon(Common common) {
this.common = common;
}

public List<PrescriptionData> getData() {
return data;
}

public void setData(List<PrescriptionData> data) {
this.data = data;
}

}