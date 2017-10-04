package com.rescribe.model.book_appointment.doctor_data.add_to_favourite;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseAddToFavourite {

@SerializedName("statusMessage")
@Expose
private String statusMessage;

public String getStatusMessage() {
return statusMessage;
}

public void setStatusMessage(String statusMessage) {
this.statusMessage = statusMessage;
}

}