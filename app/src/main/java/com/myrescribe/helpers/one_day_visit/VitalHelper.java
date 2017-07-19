package com.myrescribe.helpers.one_day_visit;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.myrescribe.R;
import com.myrescribe.interfaces.ConnectionListener;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.visit_details.Vital;
import com.myrescribe.model.visit_details.VitalsList;
import com.myrescribe.network.ConnectRequest;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeetal on 18/7/17.
 */

public class VitalHelper implements ConnectionListener {
    String TAG = this.getClass().getName();
    static Context mContext;
    HelperResponse mHelperResponseManager;

    public VitalHelper(Context context, HelperResponse vitalsActivity) {
        this.mContext = context;
        this.mHelperResponseManager = vitalsActivity;
    }

    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
               /* if (mOldDataTag == MyRescribeConstants.VITALS_LIST) {
                    VitalsList model = (VitalsList) customResponse;
                    mHelperResponseManager.onSuccess(mOldDataTag, model);
                }*/
                break;
            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG, "parse error");
                mHelperResponseManager.onParseError(mOldDataTag, "parse error");
                break;
            case ConnectionListener.SERVER_ERROR:
                CommonMethods.Log(TAG, "server error");
                mHelperResponseManager.onServerError(mOldDataTag, "server error");

                break;
            case ConnectionListener.NO_CONNECTION_ERROR:
                CommonMethods.Log(TAG, "no connection error");
                mHelperResponseManager.onNoConnectionError(mOldDataTag, "no connection error");

                break;
            default:
                CommonMethods.Log(TAG, "default error");
                break;
        }
    }

    @Override
    public void onTimeout(ConnectRequest request) {

    }

    public List<Vital> doGetVitalsList() {

        List<Vital> vitalsList = null;

        // TODO : HARDCODED JSON STRING PARSING FROM assets foler
        try {
            InputStream is = mContext.getAssets().open("vitals.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            CommonMethods.Log(TAG, "doGetHistory" + json);

            VitalsList vitalsModel = new Gson().fromJson(json, VitalsList.class);
            if (vitalsModel.getVitals() != null) {
                vitalsList = vitalsModel.getVitals();
                // yearWiseSortedDoctorList.put(doctorInfoMonthContainer.getYear(), doctorInfoMonthContainer.getMonthWiseSortedDoctorList());
            }

            CommonMethods.Log("doGetDoctorList", "" + vitalsModel.toString());
            onResponse(ConnectionListener.RESPONSE_OK, vitalsModel, MyRescribeConstants.VITALS_LIST);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return vitalsList;
    }

    public static boolean isBetween(int value, int min, int max) {
        return ((value > min) && (value < max));
    }

/*
    public static int getColorForVital(Context mContext,String vitalName,int value,int min,int max){
        int abbreviation = R.color.black;
        if (vitalName.equalsIgnoreCase("BP Max")) {
            if(isBetween(value,80,120)) {
                abbreviation = R.color.parrot;
            }else if(isBetween(value,121,140)){
                abbreviation = R.color.yellow;
            }
        } else if (vitalName.equalsIgnoreCase("BP Min")) {
            if(isBetween(value,60,80)) {
                abbreviation = R.color.parrot;
            }else if(isBetween(value,81,90)){
                abbreviation = R.color.yellow;
            }
        } else if (vitalName.equalsIgnoreCase("Weight")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalName.equalsIgnoreCase("Height")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalName.equalsIgnoreCase("BMI")) {
            if(isBetween(value,18,24)) {
                abbreviation = R.color.parrot;
            }else if(isBetween(value,25,29)){
                abbreviation = R.color.yellow;
            }
        } else if (vitalName.equalsIgnoreCase("Total")) {
            abbreviation = R.drawable.vitals;
        } else if (vitalName.equalsIgnoreCase("Cholesterol")) {
            if(isBetween(value,200,250)) {
                abbreviation = R.color.parrot;
            }
                abbreviation = R.color.black;

        } else if (vitalName.equalsIgnoreCase("HDL")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalName.equalsIgnoreCase("LDL")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalName.equalsIgnoreCase("Triglycerides")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalName.equalsIgnoreCase("HDL Cholesterol")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalName.equalsIgnoreCase("LDL Cholesterol")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalName.equalsIgnoreCase("GFR")) {
            abbreviation = R.drawable.remarks;
        } else if (vitalName.equalsIgnoreCase("BUN")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalName.equalsIgnoreCase("Sr. Creatinine")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalName.equalsIgnoreCase("Respiratory Rate")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalName.equalsIgnoreCase("Heart Rate")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalName.equalsIgnoreCase("Temperature")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalName.equalsIgnoreCase("Fasting Blood Sugar")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalName.equalsIgnoreCase("PP Blood Sugar")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalName.equalsIgnoreCase("Oxygen Saturation")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalName.equalsIgnoreCase("Platelet Count")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalName.equalsIgnoreCase("ESR")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalName.equalsIgnoreCase("Hb")) {
            abbreviation = R.drawable.complaints;
        }
        return abbreviation;
    }
*/

}
