package com.rescribe.interfaces;

import android.os.Bundle;
import android.widget.ImageView;

import com.rescribe.model.book_appointment.doctor_data.DoctorList;

/**
 * @author Ritesh D. Pandhurkar
 */
public interface IServicesCardViewClickListener {

    void onClickOfCardView(Bundle bundleData);

    void onFavoriteIconClick(boolean isFavouriteStatus, DoctorList doctorListObject, ImageView favorite);

    void onClickOfTotalCount(Bundle bundleData);
}
