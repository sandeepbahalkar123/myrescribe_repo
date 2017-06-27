package com.myrescribe.model.doctors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.model.history.PatientHistoryMain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by riteshpandhurkar on 15/6/17.
 */

public class DoctorListMain implements CustomResponse {

    private HashMap<String,ArrayList<DoctorDetail>> doctorList = null;

}
