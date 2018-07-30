package com.rescribe.adapters.find_doctors;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.rescribe.model.book_appointment.doctor_data.ClinicData;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

/**
 * Created by jeetal on 23/11/17.
 */

public class FindDoctorCategoryAdapter extends PagerAdapter {
    private HelperResponse mHelperResponse;
    private ArrayList<DoctorList> mDataList;
    private LayoutInflater mInflater;
    private Context mContext;
    private int mImageSize;
    private ServicesCardViewImpl mServicesCardViewClickListener;
    private ColorGenerator mColorGenerator;
    private String cityname;

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
        String cityNameString = RescribeApplication.getUserSelectedLocationInfo().get(mContext.getString(R.string.location));
        if (cityNameString != null) {
            String[] split = cityNameString.split(",");
            cityname = split[1].trim();
        }

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
        final ImageView ruppessIcon = (ImageView) imageLayout.findViewById(R.id.ruppessIcon);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dashBoardCard.setBackground(mContext.getDrawable(R.drawable.result_white_box));
        }
        if (!doctorObject.getCategorySpeciality().equalsIgnoreCase("")) {
            doctorCategoryType.setText(doctorObject.getCategorySpeciality());
            doctorCategoryType.setVisibility(View.VISIBLE);
        } else {
            doctorCategoryType.setVisibility(View.INVISIBLE);
        }

        String drName = doctorObject.getDocName().contains("Dr.") ? doctorObject.getDocName() : "Dr. " + doctorObject.getDocName();
        doctorNameTextView.setText(drName);

        doctorExperience.setText(doctorObject.getExperience() + " " + mContext.getString(R.string.years_experience));
        aboutDoctor.setText(doctorObject.getDegree() + "");

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

        if (doctorObject.getCategoryName().equals(mContext.getString(R.string.sponsored_doctor)) || doctorObject.getCategoryName().equals(mContext.getString(R.string.my_appointments))) {

            if (doctorObject.getClinicDataList().size() == 1) {
                clinicName.setVisibility(View.VISIBLE);
                clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());

                String areaCity;
                if (doctorObject.getClinicDataList().get(0).getAreaName().isEmpty())
                    areaCity = CommonMethods.toCamelCase(doctorObject.getClinicDataList().get(0).getCityName());
                else
                    areaCity = CommonMethods.toCamelCase(doctorObject.getClinicDataList().get(0).getAreaName()) + ", " + CommonMethods.toCamelCase(doctorObject.getClinicDataList().get(0).getCityName());
                doctorAddress.setText(areaCity);

            } else {
                if (!doctorObject.getClinicDataList().isEmpty()) {
                    boolean b = checkAllClinicAddressInSameCity(doctorObject.getClinicDataList());
                    if (b) {
                        SpannableString locationString = new SpannableString(doctorObject.getClinicDataList().size() + " " + mContext.getString(R.string.locations) + " " + "in" + " " + cityname);
                        locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                        doctorAddress.setText(locationString);
                        doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));
                        clinicName.setVisibility(View.VISIBLE);
                        clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());

                    } else {
                        SpannableString locationString = new SpannableString(doctorObject.getClinicDataList().size() + " " + mContext.getString(R.string.locations));
                        locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                        doctorAddress.setText(locationString);
                        doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));
                        clinicName.setVisibility(View.VISIBLE);
                        clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());


                    }
                }
            }
            //  designLineLayout.setBackground(mContext.getResources().getDrawable(R.drawable.design_line));
            doctorAppointmentDate.setVisibility(View.GONE);

            if (!doctorObject.getClinicDataList().isEmpty()) {
                feesToPaid.setVisibility(View.VISIBLE);
                ruppessIcon.setVisibility(View.VISIBLE);
                feesToPaid.setText("" + doctorObject.getClinicDataList().get(0).getAmount());
            } else {
                feesToPaid.setVisibility(View.INVISIBLE);
                ruppessIcon.setVisibility(View.INVISIBLE);
            }

            //----------
            if (!doctorObject.getClinicDataList().isEmpty()) {
                String appointmentType = doctorObject.getClinicDataList().get(0).getAppointmentType();
                if (doctorObject.getCategoryName().equals(mContext.getString(R.string.sponsored_doctor))
                        && (mContext.getString(R.string.token).equalsIgnoreCase(appointmentType) ||
                        mContext.getString(R.string.mixed).equalsIgnoreCase(appointmentType))) {
                    bookAppointmentButton.setVisibility(View.GONE);
                    tokenNo.setVisibility(View.VISIBLE);
                } else if (appointmentType.equalsIgnoreCase(mContext.getString(R.string.token))) {
                    bookAppointmentButton.setVisibility(View.INVISIBLE);
                    tokenNo.setVisibility(View.VISIBLE);
                } else if (doctorObject.getClinicDataList().get(0).getAppointmentType().equalsIgnoreCase(mContext.getString(R.string.book))) {
                    bookAppointmentButton.setVisibility(View.VISIBLE);
                    bookAppointmentButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.book));
                    tokenNo.setVisibility(View.INVISIBLE);
                }
            }
            //---------------

        } else if (doctorObject.getCategoryName().equals(mContext.getString(R.string.recently_visit_doctor))) {
            if (doctorObject.getClinicDataList().size() == 1) {
                clinicName.setVisibility(View.VISIBLE);
                clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());

                String areaCity;
                if (doctorObject.getClinicDataList().get(0).getAreaName().isEmpty())
                    areaCity = CommonMethods.toCamelCase(doctorObject.getClinicDataList().get(0).getCityName());
                else
                    areaCity = CommonMethods.toCamelCase(doctorObject.getClinicDataList().get(0).getAreaName()) + ", " + CommonMethods.toCamelCase(doctorObject.getClinicDataList().get(0).getCityName());
                doctorAddress.setText(areaCity);

            } else {
                if (doctorObject.getClinicDataList().size() > 0) {

                    boolean b = checkAllClinicAddressInSameCity(doctorObject.getClinicDataList());
                    if (b) {
                        SpannableString locationString = new SpannableString(doctorObject.getClinicDataList().size() + " " + mContext.getString(R.string.locations) + " " + "in" + " " + cityname);
                        locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                        doctorAddress.setText(locationString);
                        doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));
                        clinicName.setVisibility(View.VISIBLE);
                        clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());

                    } else {
                        SpannableString locationString = new SpannableString(doctorObject.getClinicDataList().size() + " " + mContext.getString(R.string.locations));
                        locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                        doctorAddress.setText(locationString);
                        doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));
                        clinicName.setVisibility(View.VISIBLE);
                        clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());

                    }
                }

            }
            // designLineLayout.setBackground(mContext.getResources().getDrawable(R.drawable.desing_line_for_big_name));
            doctorAppointmentDate.setVisibility(View.GONE);

            if (doctorObject.getClinicDataList().size() > 0) {
                feesToPaid.setVisibility(View.VISIBLE);
                ruppessIcon.setVisibility(View.VISIBLE);
                feesToPaid.setText("" + doctorObject.getClinicDataList().get(0).getAmount());

            } else {
                feesToPaid.setVisibility(View.INVISIBLE);
                ruppessIcon.setVisibility(View.INVISIBLE);

            }

            //----------
            if (doctorObject.getClinicDataList().size() > 0) {
                String appointmentType = doctorObject.getClinicDataList().get(0).getAppointmentType();
                if (appointmentType.equalsIgnoreCase(mContext.getString(R.string.token))
                        || mContext.getString(R.string.mixed).equalsIgnoreCase(appointmentType)) {
                    bookAppointmentButton.setVisibility(View.INVISIBLE);
                    tokenNo.setVisibility(View.VISIBLE);
                } else if (doctorObject.getClinicDataList().get(0).getAppointmentType().equalsIgnoreCase(mContext.getString(R.string.book))) {
                    bookAppointmentButton.setVisibility(View.VISIBLE);
                    bookAppointmentButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.book));
                    tokenNo.setVisibility(View.INVISIBLE);
                }
            }

        } else if (doctorObject.getCategoryName().equals(mContext.getString(R.string.favorite))) {
            if (doctorObject.getClinicDataList().size() == 1) {
                clinicName.setVisibility(View.VISIBLE);
                clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());

                String areaCity;
                if (doctorObject.getClinicDataList().get(0).getAreaName().isEmpty())
                    areaCity = CommonMethods.toCamelCase(doctorObject.getClinicDataList().get(0).getCityName());
                else
                    areaCity = CommonMethods.toCamelCase(doctorObject.getClinicDataList().get(0).getAreaName()) + ", " + CommonMethods.toCamelCase(doctorObject.getClinicDataList().get(0).getCityName());
                doctorAddress.setText(areaCity);

            } else {
                if (doctorObject.getClinicDataList().size() > 0) {

                    boolean b = checkAllClinicAddressInSameCity(doctorObject.getClinicDataList());
                    if (b) {
                        SpannableString locationString = new SpannableString(doctorObject.getClinicDataList().size() + " " + mContext.getString(R.string.locations) + " " + "in" + " " + cityname);
                        locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                        doctorAddress.setText(locationString);
                        doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));
                        clinicName.setVisibility(View.VISIBLE);
                        clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());

                    } else {
                        SpannableString locationString = new SpannableString(doctorObject.getClinicDataList().size() + " " + mContext.getString(R.string.locations));
                        locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                        doctorAddress.setText(locationString);
                        doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));
                        clinicName.setVisibility(View.VISIBLE);
                        clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());

                    }
                }

            }
            // designLineLayout.setBackground(mContext.getResources().getDrawable(R.drawable.desing_line_for_big_name));
            doctorAppointmentDate.setVisibility(View.GONE);

            if (doctorObject.getClinicDataList().size() > 0) {
                feesToPaid.setVisibility(View.VISIBLE);
                ruppessIcon.setVisibility(View.VISIBLE);
                feesToPaid.setText("" + doctorObject.getClinicDataList().get(0).getAmount());

            } else {
                feesToPaid.setVisibility(View.INVISIBLE);
                ruppessIcon.setVisibility(View.INVISIBLE);

            }

            //----------
            if (doctorObject.getClinicDataList().size() > 0) {
                String appointmentType = doctorObject.getClinicDataList().get(0).getAppointmentType();
                if (appointmentType.equalsIgnoreCase(mContext.getString(R.string.token))
                        || mContext.getString(R.string.mixed).equalsIgnoreCase(appointmentType)) {
                    bookAppointmentButton.setVisibility(View.INVISIBLE);
                    tokenNo.setVisibility(View.VISIBLE);
                } else if (doctorObject.getClinicDataList().get(0).getAppointmentType().equalsIgnoreCase(mContext.getString(R.string.book))) {
                    bookAppointmentButton.setVisibility(View.VISIBLE);
                    bookAppointmentButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.book));
                    tokenNo.setVisibility(View.INVISIBLE);
                }
            }

        }
        // ritesh added , if caterogyName & categorySpeciality is BLANK
        //----************************** STARTED ----
        else if (doctorObject.getCategoryName().equals(RescribeConstants.BLANK)) {
            ArrayList<ClinicData> clinicDataList = doctorObject.getClinicDataList();
            if (clinicDataList.size() == 1) {
                clinicName.setVisibility(View.VISIBLE);
                clinicName.setText(clinicDataList.get(0).getClinicName());

                String areaCity;
                if (doctorObject.getClinicDataList().get(0).getAreaName().isEmpty())
                    areaCity = CommonMethods.toCamelCase(doctorObject.getClinicDataList().get(0).getCityName());
                else
                    areaCity = CommonMethods.toCamelCase(doctorObject.getClinicDataList().get(0).getAreaName()) + ", " + CommonMethods.toCamelCase(doctorObject.getClinicDataList().get(0).getCityName());
                doctorAddress.setText(areaCity);

            } else {
                if (clinicDataList.size() > 0) {
                    boolean b = checkAllClinicAddressInSameCity(clinicDataList);
                    if (b) {
                        SpannableString locationString = new SpannableString(clinicDataList.size() + " " + mContext.getString(R.string.locations) + " " + "in" + " " + cityname);
                        locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                        doctorAddress.setText(locationString);
                        doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));
                        clinicName.setVisibility(View.VISIBLE);
                        clinicName.setText(clinicDataList.get(0).getClinicName());

                    } else {
                        SpannableString locationString = new SpannableString(clinicDataList.size() + " " + mContext.getString(R.string.locations));
                        locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                        doctorAddress.setText(locationString);
                        doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));
                        clinicName.setVisibility(View.VISIBLE);
                        clinicName.setText(clinicDataList.get(0).getClinicName());

                    }
                }
            }
            doctorAppointmentDate.setVisibility(View.GONE);

            //----------------
            if (clinicDataList.size() > 0) {
                feesToPaid.setVisibility(View.VISIBLE);
                feesToPaid.setText("" + clinicDataList.get(0).getAmount());
            } else {
                feesToPaid.setVisibility(View.INVISIBLE);
            }
            //---------------

            if (clinicDataList.size() > 0) {
                String appointmentType = doctorObject.getClinicDataList().get(0).getAppointmentType();
                if (mContext.getString(R.string.token).equalsIgnoreCase(appointmentType) || mContext.getString(R.string.mixed).equalsIgnoreCase(appointmentType)) {
                    bookAppointmentButton.setVisibility(View.GONE);
                    tokenNo.setVisibility(View.VISIBLE);
                } else if (doctorObject.getClinicDataList().get(0).getAppointmentType().equalsIgnoreCase(mContext.getString(R.string.book))) {
                    bookAppointmentButton.setVisibility(View.VISIBLE);
                    tokenNo.setVisibility(View.GONE);
                }
            }
            //---------------
        }
        //----************************** END----


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
                b.putString(RescribeConstants.ITEM_DATA_VALUE, doctorObject.getCategoryName());
                b.putParcelable(RescribeConstants.ITEM_DATA, doctorObject);
                b.putString(RescribeConstants.CATEGORY, doctorObject.getCategoryName());
                b.putString(RescribeConstants.TYPE_OF_DOCTOR_SEARCH, RescribeConstants.SEARCH_DOCTORS);
                mServicesCardViewClickListener.onClickOfCardView(b);
            }
        });

        bookAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(RescribeConstants.ITEM_DATA_VALUE, mContext.getString(R.string.book_appointment));
                b.putParcelable(RescribeConstants.ITEM_DATA, doctorObject);
                b.putInt(mContext.getString(R.string.selected_clinic_data_position), 0);
                mServicesCardViewClickListener.onClickedOfBookButton(b);
            }
        });

        tokenNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(RescribeConstants.ITEM_DATA_VALUE, mContext.getString(R.string.token_number));
                b.putInt(mContext.getString(R.string.selected_clinic_data_position), 0);
                b.putParcelable(RescribeConstants.ITEM_DATA, doctorObject);
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

    /**
     * @param list
     * @return true incase all clinic adrress are same, else false.
     * (Considered all address ends with city name)
     */
    private boolean checkAllClinicAddressInSameCity(ArrayList<ClinicData> list) {

        if (list.size() > 1) {
            int count = 0;
            String[] clinicAddress = list.get(0).getClinicAddress().split(",");
            for (ClinicData innerDataObject :
                    list) {
                String innerClinicAddress = innerDataObject.getClinicAddress();
                if (innerClinicAddress.endsWith(clinicAddress[clinicAddress.length - 1])) {
                    count = count + 1;
                }
            }
            return count == list.size();
        }
        return false;
    }

}
