package com.rescribe.interfaces.services;

import android.os.Bundle;
import android.widget.ImageView;

import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;

/**
 * @author Ritesh D. Pandhurkar
 */
public interface IServicesCardViewClickListener {

    void onClickOfCardView(Bundle bundleData);

    void onFavoriteIconClick(boolean isFavouriteStatus, DoctorList doctorListObject, ImageView favorite, HelperResponse helperResponse);

    void onClickOfTotalCount(Bundle bundleData);

    void onClickedOfTokenNumber(Bundle bundleData);

    void onClickedOfBookButton(Bundle bundleData);
}
