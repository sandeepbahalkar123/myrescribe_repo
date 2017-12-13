package com.rescribe.adapters.find_doctors;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.helpers.book_appointment.ServicesCardViewImpl;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.ui.activities.book_appointment.SelectSlotToBookAppointmentBaseActivity;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by jeetal on 23/11/17.
 */

public class FindDoctorCategoryAdapter extends PagerAdapter {
    private HelperResponse mHelperResponse;
    private boolean mIsFavAvail = false;
    private Map<String, Integer> mListSizeWithTypeMap;
    private ArrayList<DoctorList> mDataList;
    private LayoutInflater mInflater;
    private Context mContext;
    private int mImageSize;
    private ServicesCardViewImpl mServicesCardViewClickListener;


    public FindDoctorCategoryAdapter(Context context, ArrayList<DoctorList> doctorLists, ServicesCardViewImpl mOnClickOfCardOnDashboard, HelperResponse helperResponse) {
        this.mContext = context;
        this.mDataList = doctorLists;
        setColumnNumber(mContext, 2);
        this.mServicesCardViewClickListener = mOnClickOfCardOnDashboard;
         /* this.mListSizeWithTypeMap = dataMap;*/

        mInflater = LayoutInflater.from(context);
       /* if (mListSizeWithTypeMap.get(mContext.getString(R.string.favorite)) > 0) {
            mIsFavAvail = true;
        }*/
        this.mHelperResponse = helperResponse;


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = mInflater.inflate(R.layout.item_book_appointment_doctor_list, view, false);
        assert imageLayout != null;

        final CardView dashBoardCard = (CardView) imageLayout
                .findViewById(R.id.dashBoardCard);
        final CustomTextView doctorNameTextView = (CustomTextView) imageLayout
                .findViewById(R.id.doctorName);
       /* final CustomTextView doctorType = (CustomTextView) imageLayout
                .findViewById(R.id.doctorType);*/
        final CustomTextView sizeOfList = (CustomTextView) imageLayout
                .findViewById(R.id.sizeOfList);
        final CustomTextView doctorExperience = (CustomTextView) imageLayout
                .findViewById(R.id.doctorExperience);
        final CustomTextView doctorRating = (CustomTextView) imageLayout
                .findViewById(R.id.doctorRating);
        final CustomTextView doctorAddress = (CustomTextView) imageLayout
                .findViewById(R.id.doctorAddress);
        final CustomTextView doctorCategoryType = (CustomTextView) imageLayout
                .findViewById(R.id.doctorCategoryType);
        final CustomTextView feesToPaid = (CustomTextView) imageLayout
                .findViewById(R.id.doctorFee);
       /* final CustomTextView doctorCategory = (CustomTextView) imageLayout
                .findViewById(R.id.doctorCategoryVisit);*/
        final ImageView favorite = (ImageView) imageLayout
                .findViewById(R.id.favoriteView);
        final ImageView bookAppointmentButton = (ImageView) imageLayout
                .findViewById(R.id.bookAppointmentButton);
        final CustomTextView doctorAppointmentDate = (CustomTextView) imageLayout
                .findViewById(R.id.appointmentDate);
        final CircularImageView imageURL = (CircularImageView) imageLayout
                .findViewById(R.id.imageURL);
        final LinearLayout thumbnail = (LinearLayout) imageLayout
                .findViewById(R.id.thumbnail);
        final CustomTextView clinicName = (CustomTextView) imageLayout
                .findViewById(R.id.clinicName);
        final RatingBar ratingBar = (RatingBar) imageLayout
                .findViewById(R.id.ratingBar);
        final ImageView tokenNo = (ImageView) imageLayout
                .findViewById(R.id.tokenNo);
        final LinearLayout dataLayout = (LinearLayout) imageLayout
                .findViewById(R.id.dataLayout);
        final CustomTextView aboutDoctor = (CustomTextView) imageLayout
                .findViewById(R.id.aboutDoctor);


        final DoctorList doctorObject = mDataList.get(position);

        doctorCategoryType.setText(doctorObject.getCategorySpeciality());
        //doctorCategory.setText(doctorObject.getCategoryName());
        doctorNameTextView.setText(doctorObject.getDocName());
        //  doctorType.setText(doctorObject.getDegree());
        doctorExperience.setText(doctorObject.getExperience() + mContext.getString(R.string.space) + mContext.getString(R.string.years_experience));
        aboutDoctor.setText(doctorObject.getDegree() + "");
        //-----THIS IS DONE TO SHOW COUNT OF FAVORITE(CUSTOM CREATED CATEGORY), ASSUME IT WILL COME LAST ALWAYS ----
      /*  int size;
        if (((position == mDataList.size() - 1) && mIsFavAvail)) {
            doctorCategory.setText(mContext.getString(R.string.favorite));
            size = mListSizeWithTypeMap.get(mContext.getString(R.string.favorite));
        } else {
            doctorCategory.setText(doctorObject.getCategoryName());
            size = mListSizeWithTypeMap.get(doctorObject.getCategoryName());
        }
        sizeOfList.setText("" + size);
*/
        //-----------

        //---------------
        String doctorName = doctorObject.getDocName();
        if (doctorName.contains("Dr. ")) {
            doctorName = doctorName.replace("Dr. ", "");
        }
        TextDrawable textDrawable = CommonMethods.getTextDrawable(mContext, doctorName);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.override(mImageSize, mImageSize);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        requestOptions.placeholder(textDrawable);
        requestOptions.error(textDrawable);

        Glide.with(mContext)
                .load(doctorObject.getDoctorImageUrl())
                .apply(requestOptions).thumbnail(0.5f)
                .into(imageURL);
        //---------------

        if (doctorObject.getRating() == 0) {
            doctorRating.setVisibility(View.INVISIBLE);
            ratingBar.setVisibility(View.INVISIBLE);
        } else {
            doctorRating.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.VISIBLE);
            ratingBar.setRating((float) doctorObject.getRating());
            doctorRating.setText("" + doctorObject.getRating());

        }


        if (doctorObject.getCategoryName().equals(mContext.getString(R.string.my_appointments))) {
            feesToPaid.setVisibility(View.INVISIBLE);
            bookAppointmentButton.setVisibility(View.GONE);
            doctorAppointmentDate.setVisibility(View.VISIBLE);
            tokenNo.setVisibility(View.GONE);
            SpannableString content = new SpannableString(CommonMethods.getFormattedDate(doctorObject.getAptDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE_PATTERN.MMM_DD_YYYY) + ", " + CommonMethods.getFormattedDate(doctorObject.getAptTime(), RescribeConstants.DATE_PATTERN.HH_mm_ss, RescribeConstants.DATE_PATTERN.hh_mm_a));
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            doctorAppointmentDate.setText(content);
            if (doctorObject.getClinicDataList().size() > 0) {
                doctorAddress.setText(doctorObject.getClinicDataList().get(0).getClinicAddress());
                clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());
            }
            //     designLineLayout.setBackground(mContext.getResources().getDrawable(R.drawable.design_line));

        } else if (doctorObject.getCategoryName().equals(mContext.getString(R.string.sponsered_doctor))) {

            if (doctorObject.getClinicDataList().size() == 1) {
                clinicName.setVisibility(View.VISIBLE);
                clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());
                doctorAddress.setText(doctorObject.getClinicDataList().get(0).getClinicAddress());

            } else {
                if (doctorObject.getClinicDataList().size() > 0) {
                    SpannableString locationString = new SpannableString(doctorObject.getClinicDataList().size() + mContext.getString(R.string.space) + mContext.getString(R.string.locations));
                    locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                    doctorAddress.setText(locationString);
                    clinicName.setVisibility(View.INVISIBLE);
                }
            }
            //  designLineLayout.setBackground(mContext.getResources().getDrawable(R.drawable.design_line));
            doctorAppointmentDate.setVisibility(View.GONE);
            feesToPaid.setVisibility(View.VISIBLE);
            if (doctorObject.getClinicDataList().size() > 0) {
                feesToPaid.setVisibility(View.VISIBLE);
                feesToPaid.setText("" + doctorObject.getClinicDataList().get(0).getAmount());
            } else {
                feesToPaid.setVisibility(View.INVISIBLE);
            }

            //----------
            if (doctorObject.getClinicDataList().size() > 0) {
                if (doctorObject.getClinicDataList().get(0).getAppointmentType().equalsIgnoreCase(mContext.getString(R.string.token))) {
                    bookAppointmentButton.setVisibility(View.INVISIBLE);
                    tokenNo.setVisibility(View.VISIBLE);
                } else if (doctorObject.getClinicDataList().get(0).getAppointmentType().equalsIgnoreCase(mContext.getString(R.string.book))) {
                    bookAppointmentButton.setVisibility(View.VISIBLE);
                    tokenNo.setVisibility(View.INVISIBLE);
                }
            }
            //---------------


        } else if (doctorObject.getCategoryName().equals(mContext.getString(R.string.recently_visit_doctor))) {
            if (doctorObject.getClinicDataList().size() == 1) {
                clinicName.setVisibility(View.VISIBLE);
                clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());
                doctorAddress.setText(doctorObject.getClinicDataList().get(0).getClinicAddress());

            } else {
                if (doctorObject.getClinicDataList().size() > 0) {
                    SpannableString locationString = new SpannableString(doctorObject.getClinicDataList().size() + mContext.getString(R.string.space) + mContext.getString(R.string.locations));
                    locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                    doctorAddress.setText(locationString);
                    clinicName.setVisibility(View.INVISIBLE);
                }

            }
            // designLineLayout.setBackground(mContext.getResources().getDrawable(R.drawable.desing_line_for_big_name));
            doctorAppointmentDate.setVisibility(View.GONE);

            if (doctorObject.getClinicDataList().size() > 0) {
                feesToPaid.setVisibility(View.VISIBLE);

                feesToPaid.setText("" + doctorObject.getClinicDataList().get(0).getAmount());

            } else {
                feesToPaid.setVisibility(View.INVISIBLE);

            }

            //----------
            if (doctorObject.getClinicDataList().size() > 0) {
                if (doctorObject.getClinicDataList().get(0).getAppointmentType().equalsIgnoreCase(mContext.getString(R.string.token))) {
                    bookAppointmentButton.setVisibility(View.INVISIBLE);
                    tokenNo.setVisibility(View.VISIBLE);
                } else if (doctorObject.getClinicDataList().get(0).getAppointmentType().equalsIgnoreCase(mContext.getString(R.string.book))) {
                    bookAppointmentButton.setVisibility(View.VISIBLE);
                    tokenNo.setVisibility(View.INVISIBLE);
                }
            }

        }


        if (doctorObject.getFavourite()) {
            favorite.setImageDrawable(mContext.getResources().getDrawable(R.drawable.favourite_icon));
        } else {
            favorite.setImageDrawable(mContext.getResources().getDrawable(R.drawable.favourite_line_icon));
        }
        //---------
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status = !doctorObject.getFavourite();
                mServicesCardViewClickListener.onFavoriteIconClick(status, doctorObject, favorite, mHelperResponse);
            }
        });


        dashBoardCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(mContext.getString(R.string.clicked_item_data_type_value), doctorObject.getCategoryName());
                b.putParcelable(mContext.getString(R.string.clicked_item_data), doctorObject);
                mServicesCardViewClickListener.onClickOfCardView(b);
            }
        });

        bookAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(mContext.getString(R.string.clicked_item_data_type_value), mContext.getString(R.string.book_appointment));
                b.putParcelable(mContext.getString(R.string.clicked_item_data), doctorObject);
                b.putInt(mContext.getString(R.string.selected_clinic_data_position), 0);
                mServicesCardViewClickListener.onClickedOfBookButton(b);
            }
        });
        tokenNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(mContext.getString(R.string.clicked_item_data_type_value), mContext.getString(R.string.token_number));
                b.putInt(mContext.getString(R.string.selected_clinic_data_position), 0);
                b.putParcelable(mContext.getString(R.string.clicked_item_data), doctorObject);
                mServicesCardViewClickListener.onClickedOfTokenNumber(b);
            }
        });


        view.addView(imageLayout, 0);

        return imageLayout;
    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        mImageSize = (widthPixels / columnNum) - CommonMethods.convertDpToPixel(30);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


}
