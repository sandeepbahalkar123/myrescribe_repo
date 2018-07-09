package com.rescribe.helpers.book_appointment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.rescribe.R;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.interfaces.services.IServicesCardViewClickListener;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.ui.activities.AppointmentActivity;
import com.rescribe.ui.activities.book_appointment.DoctorDescriptionBaseActivity;
import com.rescribe.ui.activities.book_appointment.SelectSlotToBookAppointmentBaseActivity;
import com.rescribe.ui.activities.book_appointment.ServicesFilteredDoctorListActivity;
import com.rescribe.ui.activities.book_appointment.confirmation_type_activities.ConfirmAppointmentActivity;
import com.rescribe.ui.activities.book_appointment.confirmation_type_activities.ConfirmTokenInfoActivity;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by riteshpandhurkar on 15/11/17.
 */

public class ServicesCardViewImpl implements IServicesCardViewClickListener {

    /**
     * THIS IS DONE AS NOW REQ THIS TITLE FOR ALL SCENARIO EXPECT FOR COMPLAINT & SPECIALITY CASE.
     */
    private final String mTitle;
    private Context mContext;
    private AppCompatActivity mParentActivity;
    private static ArrayList<DoctorList> mReceivedDoctorDataList;
    private static DoctorList userSelectedDoctorListDataObject;

    public ServicesCardViewImpl(Context context, AppCompatActivity parentActivity) {
        this.mContext = context;
        this.mParentActivity = parentActivity;
        mTitle = mContext.getString(R.string.doctor);
    }

    //onClick of whole card view // searchType Doctor then myappointment doctors will be shown to book appointment
    @Override
    public void onClickOfCardView(Bundle bundleData) {
        String searchType = bundleData.getString(RescribeConstants.TYPE_OF_DOCTOR_SEARCH);
        String value = bundleData.getString(mContext.getString(R.string.clicked_item_data_type_value));
        String category = bundleData.getString(mContext.getString(R.string.category_name));
        String openingMode = bundleData.getString(mContext.getString(R.string.opening_mode));
        String title = bundleData.getString(mContext.getString(R.string.toolbarTitle));
        userSelectedDoctorListDataObject = bundleData.getParcelable(mContext.getString(R.string.clicked_item_data));
        if (category.equalsIgnoreCase(mContext.getString(R.string.my_appointments))) {
            if (searchType.equalsIgnoreCase(RescribeConstants.SEARCH_DOCTORS)) {
                Intent intent = new Intent(mParentActivity, DoctorDescriptionBaseActivity.class);
                intent.putExtra(mContext.getString(R.string.clicked_item_data), userSelectedDoctorListDataObject);
                if (mContext.getString(R.string.complaints).equalsIgnoreCase(openingMode) ||
                        mContext.getString(R.string.doctors_speciality).equalsIgnoreCase(openingMode)) {
                    intent.putExtra(mContext.getString(R.string.toolbarTitle), title);
                } else {
                    intent.putExtra(mContext.getString(R.string.toolbarTitle), mTitle);
                    bundleData.putString(mContext.getString(R.string.toolbarTitle), mTitle);
                }
                intent.putExtras(bundleData);
                mParentActivity.startActivity(intent);
            } else {
                Intent intent;
                //for MyAppointment category doctor confirmAppointment page will open
                if (userSelectedDoctorListDataObject.getType().equalsIgnoreCase(mContext.getString(R.string.token))) {
                    intent = new Intent(mContext, ConfirmTokenInfoActivity.class);
                    bundleData.putString(RescribeConstants.LOCATION_ID, "" + userSelectedDoctorListDataObject.getClinicDataList().get(0).getLocationId());
                    bundleData.putString(RescribeConstants.TOKEN_NO, userSelectedDoctorListDataObject.getTokenNumber());
                    bundleData.putString(RescribeConstants.WAITING_TIME, userSelectedDoctorListDataObject.getWaitingPatientTime());
                    bundleData.putString(RescribeConstants.WAITING_COUNT, userSelectedDoctorListDataObject.getWaitingPatientCount());
                } else {
                    intent = new Intent(mParentActivity, ConfirmAppointmentActivity.class);
                    bundleData.putString(RescribeConstants.LOCATION_ID, "" + 0);
                    bundleData.putString(RescribeConstants.TOKEN_NO, "" + 0);
                }
                intent.putExtras(bundleData);
                mParentActivity.startActivity(intent);
            }
        } else {
            // for category other than MyAppointment Detail page will open
            Intent intent = new Intent(mParentActivity, DoctorDescriptionBaseActivity.class);
            intent.putExtra(mContext.getString(R.string.clicked_item_data), userSelectedDoctorListDataObject);
            if (mContext.getString(R.string.complaints).equalsIgnoreCase(openingMode) ||
                    mContext.getString(R.string.doctors_speciality).equalsIgnoreCase(openingMode)) {
                intent.putExtra(mContext.getString(R.string.toolbarTitle), title);
            } else {
                intent.putExtra(mContext.getString(R.string.toolbarTitle), mTitle);
                bundleData.putString(mContext.getString(R.string.toolbarTitle), mTitle);
            }
            intent.putExtras(bundleData);
            mParentActivity.startActivity(intent);
        }
    }

    // All favourite clicks managed here
    @Override
    public void onFavoriteIconClick(boolean isFavouriteStatus, DoctorList doctorListObject, ImageView favorite, HelperResponse helperResponse) {
        userSelectedDoctorListDataObject = doctorListObject;
        new DoctorDataHelper(mContext, helperResponse).setFavouriteDoctor(isFavouriteStatus, doctorListObject.getDocId());
    }

    @Override
    public void onClickOfTotalCount(Bundle bundleData) {
        String nameOfCategoryType = bundleData.getString(mContext.getString(R.string.clicked_item_data));
// for MyAppointment doctor from dashboard and book appointment horizontal list
        if (nameOfCategoryType.equalsIgnoreCase(mContext.getString(R.string.my_appointments))) {
            Intent intent = new Intent(mParentActivity, AppointmentActivity.class);
            intent.putExtra(RescribeConstants.CALL_FROM_DASHBOARD, "");
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
            // for sponsered and recent visited doctor list.
            Intent intent = new Intent(mParentActivity, ServicesFilteredDoctorListActivity.class);
            bundleData.putString(mContext.getString(R.string.toolbarTitle), nameOfCategoryType);

            intent.putExtras(bundleData);
            mParentActivity.startActivity(intent);
        }
    }

    //onclick of GetToken button
    @Override
    public void onClickedOfTokenNumber(Bundle bundleData) {
        userSelectedDoctorListDataObject = bundleData.getParcelable(mContext.getString(R.string.clicked_item_data));
        Intent intent = new Intent(mParentActivity, SelectSlotToBookAppointmentBaseActivity.class);

        //---
        String openingMode = bundleData.getString(mContext.getString(R.string.opening_mode));
        String title = bundleData.getString(mContext.getString(R.string.toolbarTitle));
        if (mContext.getString(R.string.complaints).equalsIgnoreCase(openingMode) ||
                mContext.getString(R.string.doctors_speciality).equalsIgnoreCase(openingMode)) {
            intent.putExtra(mContext.getString(R.string.toolbarTitle), title);
        } else {
            bundleData.putString(mContext.getString(R.string.toolbarTitle), mTitle);
        }


        intent.putExtras(bundleData);
        mParentActivity.startActivity(intent);
    }

    //onClick of BookAppointment button
    @Override
    public void onClickedOfBookButton(Bundle bundleData) {

        userSelectedDoctorListDataObject = bundleData.getParcelable(mContext.getString(R.string.clicked_item_data));
        Intent intent = new Intent(mParentActivity, SelectSlotToBookAppointmentBaseActivity.class);

        //---
        String openingMode = bundleData.getString(mContext.getString(R.string.opening_mode));
        String title = bundleData.getString(mContext.getString(R.string.toolbarTitle));
        if (mContext.getString(R.string.complaints).equalsIgnoreCase(openingMode) ||
                mContext.getString(R.string.doctors_speciality).equalsIgnoreCase(openingMode)) {
            intent.putExtra(mContext.getString(R.string.toolbarTitle), title);
        } else {
            bundleData.putString(mContext.getString(R.string.toolbarTitle), mTitle);
        }

        intent.putExtras(bundleData);
        mParentActivity.startActivity(intent);
    }

    //Sort doctorList CategoryWise
    public ArrayList<DoctorList> getCategoryWiseDoctorList(String categoryName, int size) {
        ArrayList<DoctorList> temp = new ArrayList<>();
        if (mReceivedDoctorDataList != null)
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
        if (mReceivedDoctorDataList != null)
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

    public static boolean updateFavStatusForDoctorDataObject(DoctorList updatedObject, AppDBHelper appDBHelper) {
        boolean status = false;
        if (updatedObject != null) {
            for (int i = 0; i < mReceivedDoctorDataList.size(); i++) {
                DoctorList tempObject = mReceivedDoctorDataList.get(i);
                if (updatedObject.getDocId() == tempObject.getDocId()) {
                    tempObject.setFavourite(!tempObject.getFavourite());
                    mReceivedDoctorDataList.set(i, tempObject);
                    appDBHelper.updateCardTable(tempObject.getDocId(), tempObject.getFavourite() ? 1 : 0, tempObject.getCategoryName());
                    status = true;
                }
            }
        }
        return status;
    }

    // function to sort out doctors from pick speciality from bookAppointment page
    public ArrayList<DoctorList> filterDocListBySpeciality(String selectedSpeciality) {

        ArrayList<DoctorList> dataList = new ArrayList<>();
        if (selectedSpeciality == null) {
            return dataList;
        } else {
            for (DoctorList listObject :
                    mReceivedDoctorDataList) {
                //Added condition for my_appointment, Not required to add getCategoryName = my_appointment
                if (selectedSpeciality.equalsIgnoreCase(listObject.getDocSpeciality()) &&
                        !(mContext.getString(R.string.my_appointments).equalsIgnoreCase(listObject.getCategoryName()))) {

                    if (dataList.size() > 0) {
                        boolean isAddItemToList = true;
                        for (DoctorList prevAddedObject :
                                dataList) {
                            if (listObject.getDocId() == prevAddedObject.getDocId()) {
                                isAddItemToList = false;
                                break;
                            }
                        }
                        if (isAddItemToList) {
                            dataList.add(listObject);
                        }
                    } else {
                        dataList.add(listObject);
                    }
                }
            }
        }

        return dataList;
    }

    // get whole doctor list using this function
    public static DoctorList getUserSelectedDoctorListDataObject() {
        return userSelectedDoctorListDataObject;
    }

    //set doctor list using this function
    public static void setUserSelectedDoctorListDataObject(DoctorList userSelectedDoctorListDataObject) {
        ServicesCardViewImpl.userSelectedDoctorListDataObject = userSelectedDoctorListDataObject;
    }

    public void setReceivedDoctorDataList(ArrayList<DoctorList> list) {
        mReceivedDoctorDataList = list;
    }

    public static ArrayList<DoctorList> getReceivedDoctorDataList() {
        return mReceivedDoctorDataList;
    }

    //Sort Unique doctors by docId
    public static ArrayList<DoctorList> getDoctorListByUniqueDocIDs(ArrayList<DoctorList> mReceivedDoctorDataList) {
        ArrayList<DoctorList> dataList = new ArrayList<>();
        for (DoctorList listObject :
                mReceivedDoctorDataList) {
            if (dataList.size() > 0) {
                boolean isAddItemToList = true;
                for (DoctorList prevAddedObject :
                        dataList) {
                    if (listObject.getDocId() == prevAddedObject.getDocId()) {
                        isAddItemToList = false;
                        break;
                    }
                }
                if (isAddItemToList) {
                    dataList.add(listObject);
                }
            } else {
                dataList.add(listObject);
            }
        }
        return dataList;
    }
}
