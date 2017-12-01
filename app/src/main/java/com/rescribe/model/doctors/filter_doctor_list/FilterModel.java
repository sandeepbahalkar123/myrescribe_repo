package com.rescribe.model.doctors.filter_doctor_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FilterModel {

@SerializedName("docVisits")
@Expose
private ArrayList<DoctorFilteredInfoAndCaseDetails> docVisits = null;

public ArrayList<DoctorFilteredInfoAndCaseDetails> getDocVisits() {
return docVisits;
}

public void setDocVisits(ArrayList<DoctorFilteredInfoAndCaseDetails> docVisits) {
this.docVisits = docVisits;
}

}
