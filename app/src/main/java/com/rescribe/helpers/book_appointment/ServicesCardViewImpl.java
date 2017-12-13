package com.rescribe.helpers.book_appointment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.rescribe.R;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.interfaces.services.IServicesCardViewClickListener;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.ui.activities.AppointmentActivity;
import com.rescribe.ui.activities.book_appointment.SelectSlotToBookAppointmentBaseActivity;
import com.rescribe.ui.activities.book_appointment.ServicesFilteredDoctorListActivity;
import com.rescribe.ui.activities.book_appointment.DoctorDescriptionBaseActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by riteshpandhurkar on 15/11/17.
 */

public class ServicesCardViewImpl implements IServicesCardViewClickListener {

    private Context mContext;
    private AppCompatActivity mParentActivity;
    private static ArrayList<DoctorList> mReceivedDoctorDataList;
    private static DoctorList userSelectedDoctorListDataObject;

    public ServicesCardViewImpl(Context context, AppCompatActivity parentActivity) {
        this.mContext = context;
        this.mParentActivity = parentActivity;
    }


    @Override
    public void onClickOfCardView(Bundle bundleData) {
        String value = bundleData.getString(mContext.getString(R.string.clicked_item_data_type_value));
        userSelectedDoctorListDataObject = bundleData.getParcelable(mContext.getString(R.string.clicked_item_data));
        if (value.equalsIgnoreCase(mContext.getString(R.string.my_appointments))) {
            Intent intent = new Intent(mParentActivity, AppointmentActivity.class);
            mParentActivity.startActivity(intent);
        } else {
            Intent intent = new Intent(mParentActivity, DoctorDescriptionBaseActivity.class);
            intent.putExtra(mContext.getString(R.string.clicked_item_data), userSelectedDoctorListDataObject);
            intent.putExtra(mContext.getString(R.string.toolbarTitle), value);
            mParentActivity.startActivity(intent);
        }
    }

    @Override
    public void onFavoriteIconClick(boolean isFavouriteStatus, DoctorList doctorListObject, ImageView favorite, HelperResponse helperResponse) {
        userSelectedDoctorListDataObject = doctorListObject;
        new DoctorDataHelper(mContext, helperResponse).setFavouriteDoctor(isFavouriteStatus, doctorListObject.getDocId());
    }

    @Override
    public void onClickOfTotalCount(Bundle bundleData) {
        String nameOfCategoryType = bundleData.getString(mContext.getString(R.string.clicked_item_data));

        if (nameOfCategoryType.equalsIgnoreCase(mContext.getString(R.string.my_appointments))) {
            Intent intent = new Intent(mParentActivity, ServicesFilteredDoctorListActivity.class);
            bundleData.putString(mContext.getString(R.string.toolbarTitle), mContext.getString(R.string.my_appointments));
            intent.putExtras(bundleData);
            //mParentActivity.startActivityForResult(intent, RescribeConstants.DOCTOR_DATA_REQUEST_CODE);
            mParentActivity.startActivity(intent);
        } else if (nameOfCategoryType.equalsIgnoreCase(mContext.getString(R.string.favorite))) { // favorite card name
            Intent intent = new Intent(mParentActivity, ServicesFilteredDoctorListActivity.class);
            bundleData.putString(mContext.getString(R.string.toolbarTitle), nameOfCategoryType);
            bundleData.putBoolean(mContext.getString(R.string.favorite), true);
            intent.putExtras(bundleData);
            mParentActivity.startActivity(intent);
        } else {
            // for sponcered and recent visited doctor list.
            Intent intent = new Intent(mParentActivity, ServicesFilteredDoctorListActivity.class);
            bundleData.putString(mContext.getString(R.string.toolbarTitle), nameOfCategoryType);
            intent.putExtras(bundleData);
            mParentActivity.startActivity(intent);
        }
    }

    @Override
    public void onClickedOfTokenNumber(Bundle bundleData) {
        userSelectedDoctorListDataObject = bundleData.getParcelable(mContext.getString(R.string.clicked_item_data));

        Intent intent = new Intent(mParentActivity, SelectSlotToBookAppointmentBaseActivity.class);
        bundleData.putString(mContext.getString(R.string.toolbarTitle), userSelectedDoctorListDataObject.getCategoryName());
        intent.putExtras(bundleData);
        mParentActivity.startActivity(intent);
    }

    @Override
    public void onClickedOfBookButton(Bundle bundleData) {

        userSelectedDoctorListDataObject = bundleData.getParcelable(mContext.getString(R.string.clicked_item_data));
        Intent intent = new Intent(mParentActivity, SelectSlotToBookAppointmentBaseActivity.class);
        bundleData.putString(mContext.getString(R.string.toolbarTitle), userSelectedDoctorListDataObject.getCategoryName());
        intent.putExtras(bundleData);
        mParentActivity.startActivity(intent);
    }


    public ArrayList<DoctorList> getCategoryWiseDoctorList(String categoryName, int size) {
        ArrayList<DoctorList> temp = new ArrayList<>();
        for (DoctorList docObject :
                mReceivedDoctorDataList) {
            if (docObject.getCategoryName().equalsIgnoreCase(categoryName)) {
                temp.add(docObject);
            }
        }
        if (size != -1) {
            if (temp.size() > size) {
                temp = new ArrayList<DoctorList>(temp.subList(0, size));
            }
        }
        return temp;
    }

    public ArrayList<DoctorList> getFavouriteDocList(int size) {
        HashMap<Integer, DoctorList> tempMap = new HashMap<>();
        for (DoctorList docObject :
                mReceivedDoctorDataList) {
            if (docObject.getFavourite()) {
                DoctorList doctorList = tempMap.get(docObject.getDocId());
                if (doctorList == null)
                    tempMap.put(docObject.getDocId(), docObject);
            }
        }
        ArrayList<DoctorList> temp = new ArrayList<>(tempMap.values());
        if (size != -1)
            if (temp.size() > size) {
                temp = new ArrayList<DoctorList>(temp.subList(0, size));
            }

        return temp;
    }

    public void replaceDoctorListById(String docId, DoctorList docObjectToReplace, String objectUpdateType) {
        ArrayList<DoctorList> newListToUpdateTempDoctorList = new ArrayList<>(mReceivedDoctorDataList);
        boolean isUpdated = false;
        for (int i = 0; i < mReceivedDoctorDataList.size(); i++) {
            DoctorList tempObject = mReceivedDoctorDataList.get(i);

            if (docId.equalsIgnoreCase("" + tempObject.getDocId())) {
                isUpdated = true;
                newListToUpdateTempDoctorList.set(i, docObjectToReplace);
            }
        }

        if (isUpdated) {
            mReceivedDoctorDataList.clear();
            mReceivedDoctorDataList.addAll(newListToUpdateTempDoctorList);
        }
    }

    public DoctorList findDoctorListById(String docId) {
        for (int i = 0; i < mReceivedDoctorDataList.size(); i++) {
            DoctorList tempObject = mReceivedDoctorDataList.get(i);
            if (docId.equalsIgnoreCase("" + tempObject.getDocId())) {
                return tempObject;
            }
        }
        return null;
    }

    public static boolean updateFavStatusForDoctorDataObject(DoctorList updatedObject) {
        boolean status = false;
        if (updatedObject != null) {
            for (int i = 0; i < mReceivedDoctorDataList.size(); i++) {
                DoctorList tempObject = mReceivedDoctorDataList.get(i);
                if (updatedObject.getDocId() == tempObject.getDocId()) {
                    tempObject.setFavourite(tempObject.getFavourite() ? false : true);
                    mReceivedDoctorDataList.set(i, tempObject);
                    status = true;
                }
            }
        }
        return status;
    }


    public ArrayList<DoctorList> filterDocListBySpeciality(String selectedSpeciality) {

        ArrayList<DoctorList> dataList = new ArrayList<>();
        if (selectedSpeciality == null) {
            return dataList;
        } else {
            for (DoctorList listObject :
                    mReceivedDoctorDataList) {
                if (selectedSpeciality.equalsIgnoreCase(listObject.getDocSpeciality())) {
                    dataList.add(listObject);
                }
            }
        }
        return dataList;
    }

    public static DoctorList getUserSelectedDoctorListDataObject() {
        return userSelectedDoctorListDataObject;
    }

    public static void setUserSelectedDoctorListDataObject(DoctorList userSelectedDoctorListDataObject) {
        ServicesCardViewImpl.userSelectedDoctorListDataObject = userSelectedDoctorListDataObject;
    }

    public void setReceivedDoctorDataList(ArrayList<DoctorList> list) {
        mReceivedDoctorDataList = list;
    }

    public static ArrayList<DoctorList> getReceivedDoctorDataList() {
        return mReceivedDoctorDataList;
    }
}
