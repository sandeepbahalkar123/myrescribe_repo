package com.rescribe.model.book_appointment.doctor_data.add_to_favourite;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.model.Common;

public class ResponseFavouriteDoctorBaseModel {

@SerializedName("common")
@Expose
private Common common;
@SerializedName("responseAddToFavourite")
@Expose
private ResponseAddToFavourite responseAddToFavourite;

public Common getCommon() {
return common;
}

public void setCommon(Common common) {
this.common = common;
}

public ResponseAddToFavourite getResponseAddToFavourite() {
return responseAddToFavourite;
}

public void setResponseAddToFavourite(ResponseAddToFavourite responseAddToFavourite) {
this.responseAddToFavourite = responseAddToFavourite;
}

}