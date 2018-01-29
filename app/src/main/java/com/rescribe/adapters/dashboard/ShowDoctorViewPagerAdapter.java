package com.rescribe.adapters.dashboard;

import android.content.Context;
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
import android.widget.RelativeLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
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
import java.util.Map;

/**
 * Created by jeetal on 11/10/17.
 */

public class ShowDoctorViewPagerAdapter extends PagerAdapter {
    private HelperResponse mHelperResponse;
    private boolean mIsFavAvail = false;
    private Map<String, Integer> mListSizeWithTypeMap;
    private ArrayList<DoctorList> mDataList;
    private LayoutInflater mInflater;
    private Context mContext;
    private ServicesCardViewImpl mServicesCardViewClickListener;
    private String cityname;

    public ShowDoctorViewPagerAdapter(Context context, ArrayList<DoctorList> doctorLists, ServicesCardViewImpl mOnClickOfCardOnDashboard, Map<String, Integer> dataMap, HelperResponse helperResponse) {
        this.mContext = context;
        this.mDataList = doctorLists;
        this.mServicesCardViewClickListener = mOnClickOfCardOnDashboard;
        mInflater = LayoutInflater.from(context);
        this.mListSizeWithTypeMap = dataMap;
        if (mListSizeWithTypeMap.get(mContext.getString(R.string.favorite)) > 0) {
            mIsFavAvail = true;
        }
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
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = mInflater.inflate(R.layout.dashboard_doctor_category_item, view, false);
        assert imageLayout != null;

        final CardView dashBoardCard = (CardView) imageLayout
                .findViewById(R.id.dashBoardCard);
        final CustomTextView doctorNameTextView = (CustomTextView) imageLayout
                .findViewById(R.id.doctorName);
        final CustomTextView doctorType = (CustomTextView) imageLayout
                .findViewById(R.id.doctorType);
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
                .findViewById(R.id.feesToPaidVisit);
        final CustomTextView doctorCategory = (CustomTextView) imageLayout
                .findViewById(R.id.doctorCategoryVisit);
        final ImageView favorite = (ImageView) imageLayout
                .findViewById(R.id.favorite);
        final ImageView bookAppointmentButton = (ImageView) imageLayout
                .findViewById(R.id.bookAppointmentButton);
        final CustomTextView doctorAppointmentDate = (CustomTextView) imageLayout
                .findViewById(R.id.doctorAppointmentDate);
        final ImageView imageURL = (ImageView) imageLayout
                .findViewById(R.id.imageURL);
        final LinearLayout thumbnail = (LinearLayout) imageLayout
                .findViewById(R.id.thumbnail);
        final RelativeLayout designLineLayout = (RelativeLayout) imageLayout
                .findViewById(R.id.designLineLayout);
        final CustomTextView clinicName = (CustomTextView) imageLayout
                .findViewById(R.id.clinicName);
        final RatingBar ratingBar = (RatingBar) imageLayout
                .findViewById(R.id.ratingBar);
        final ImageView tokenNo = (ImageView) imageLayout
                .findViewById(R.id.tokenNo);

        final DoctorList doctorObject = mDataList.get(position);
          if(!doctorObject.getCategorySpeciality().equalsIgnoreCase("")){
              doctorCategoryType.setText(doctorObject.getCategorySpeciality());
              doctorCategoryType.setVisibility(View.VISIBLE);
          }else{
              doctorCategoryType.setVisibility(View.INVISIBLE);
          }
        doctorCategory.setText(doctorObject.getCategoryName());
        doctorNameTextView.setText(doctorObject.getDocName());
        doctorType.setText(doctorObject.getDegree());
        if (doctorObject.getExperience() == 0) {
            doctorExperience.setVisibility(View.GONE);
        } else {
            doctorExperience.setVisibility(View.VISIBLE);
            doctorExperience.setText(doctorObject.getExperience() + " " + mContext.getString(R.string.years_experience));
        }

        //-----THIS IS DONE TO SHOW COUNT OF FAVORITE(CUSTOM CREATED CATEGORY), ASSUME IT WILL COME LAST ALWAYS ----
        int size;
        if (((position == mDataList.size() - 1) && mIsFavAvail)) {
            doctorCategory.setText(mContext.getString(R.string.favorite));
            size = mListSizeWithTypeMap.get(mContext.getString(R.string.favorite));
        } else {
            doctorCategory.setText(doctorObject.getCategoryName());
            size = mListSizeWithTypeMap.get(doctorObject.getCategoryName());
        }
        sizeOfList.setText("" + size);

        //----------------
        String doctorName = doctorObject.getDocName();
        if (doctorName.contains("Dr. ")) {
            doctorName = doctorName.replace("Dr. ", "");
        }
        TextDrawable textDrawable = CommonMethods.getTextDrawable(mContext, doctorName);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        requestOptions.placeholder(textDrawable);
        requestOptions.error(textDrawable);

        Glide.with(mContext)
                .load(doctorObject.getDoctorImageUrl())
                .apply(requestOptions)
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

        ArrayList<ClinicData> clinicDataList = doctorObject.getClinicDataList();

        if (doctorObject.getCategoryName().equals(mContext.getString(R.string.my_appointments))) {
            //this is done to show myAppointment doctor in favourite category to avail book option
            if (((position == mDataList.size() - 1) && mIsFavAvail)) {
                if (clinicDataList.size() == 1) {
                    clinicName.setVisibility(View.VISIBLE);
                    clinicName.setText(clinicDataList.get(0).getClinicName());
                    doctorAddress.setText(clinicDataList.get(0).getClinicAddress());
                    doctorAddress.setTextColor(mContext.getResources().getColor(R.color.grey_shade));

                } else {
                    if (clinicDataList.size() > 0) {
                        boolean b = checkAllClinicAddressInSameCity(clinicDataList);
                        if (b) {
                            SpannableString locationString = new SpannableString(clinicDataList.size() + " " + mContext.getString(R.string.locations)+ " " + "in" + " " + cityname);
                            locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                            doctorAddress.setText(locationString);
                            clinicName.setVisibility(View.VISIBLE);
                            clinicName.setText(clinicDataList.get(0).getClinicName());
                            doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));

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
                designLineLayout.setBackground(mContext.getResources().getDrawable(R.drawable.design_line));
                doctorAppointmentDate.setVisibility(View.GONE);
                feesToPaid.setVisibility(View.VISIBLE);
                if (clinicDataList.size() > 0) {
                    feesToPaid.setVisibility(View.VISIBLE);
                    feesToPaid.setText("" + clinicDataList.get(0).getAmount());

                } else {
                    feesToPaid.setVisibility(View.INVISIBLE);

                }

                //----------
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
            }else{
                feesToPaid.setVisibility(View.INVISIBLE);
                bookAppointmentButton.setVisibility(View.GONE);
                doctorAppointmentDate.setVisibility(View.VISIBLE);
                tokenNo.setVisibility(View.GONE);

                if (!doctorObject.getAptTime().isEmpty()) {
                    String time = CommonMethods.getFormattedDate(doctorObject.getAptTime(), RescribeConstants.DATE_PATTERN.HH_mm_ss, RescribeConstants.DATE_PATTERN.hh_mm_a).toLowerCase();
                    SpannableString content = new SpannableString(CommonMethods.getFormattedDate(doctorObject.getAptDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE_PATTERN.MMM_DD_YYYY) + ", " + time);
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    doctorAppointmentDate.setText(content);
                }

                if (clinicDataList.size() > 0) {
                    doctorAddress.setTextColor(mContext.getResources().getColor(R.color.grey_shade));
                    doctorAddress.setText(clinicDataList.get(0).getClinicAddress());
                    clinicName.setText(clinicDataList.get(0).getClinicName());
                }
                designLineLayout.setBackground(mContext.getResources().getDrawable(R.drawable.design_line));

            }

        } else if (doctorObject.getCategoryName().equals(mContext.getString(R.string.sponsored_doctor))) {

            if (clinicDataList.size() == 1) {
                clinicName.setVisibility(View.VISIBLE);
                clinicName.setText(clinicDataList.get(0).getClinicName());
                doctorAddress.setText(clinicDataList.get(0).getClinicAddress());
                doctorAddress.setTextColor(mContext.getResources().getColor(R.color.grey_shade));

            } else {
                if (clinicDataList.size() > 0) {
                    boolean b = checkAllClinicAddressInSameCity(clinicDataList);
                    if (b) {
                        SpannableString locationString = new SpannableString(clinicDataList.size() + " " + mContext.getString(R.string.locations)+ " " + "in" + " " + cityname);
                        locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                        doctorAddress.setText(locationString);
                        clinicName.setVisibility(View.VISIBLE);
                        clinicName.setText(clinicDataList.get(0).getClinicName());
                        doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));

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
            designLineLayout.setBackground(mContext.getResources().getDrawable(R.drawable.design_line));
            doctorAppointmentDate.setVisibility(View.GONE);
            feesToPaid.setVisibility(View.VISIBLE);
            if (clinicDataList.size() > 0) {
                feesToPaid.setVisibility(View.VISIBLE);
                feesToPaid.setText("" + clinicDataList.get(0).getAmount());

            } else {
                feesToPaid.setVisibility(View.INVISIBLE);

            }

            //----------
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

        } else if (doctorObject.getCategoryName().equals(mContext.getString(R.string.recently_visit_doctor))) {
            if (clinicDataList.size() == 1) {
                clinicName.setVisibility(View.VISIBLE);
                clinicName.setText(clinicDataList.get(0).getClinicName());
                doctorAddress.setText(clinicDataList.get(0).getClinicAddress());
                doctorAddress.setTextColor(mContext.getResources().getColor(R.color.grey_shade));

            } else {
                if (clinicDataList.size() > 0) {
                    boolean b = checkAllClinicAddressInSameCity(clinicDataList);
                    if (b) {
                        SpannableString locationString = new SpannableString(clinicDataList.size() + " " + mContext.getString(R.string.locations)+ " " + "in" + " " + cityname);
                        locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                        doctorAddress.setText(locationString);
                        clinicName.setVisibility(View.VISIBLE);
                        clinicName.setText(clinicDataList.get(0).getClinicName());
                        doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));
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
            designLineLayout.setBackground(mContext.getResources().getDrawable(R.drawable.desing_line_for_big_name));
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
        } else if (doctorObject.getCategoryName().equals(RescribeConstants.BLANK)) {
            if (clinicDataList.size() == 1) {
                clinicName.setVisibility(View.VISIBLE);
                clinicName.setText(clinicDataList.get(0).getClinicName());
                doctorAddress.setText(clinicDataList.get(0).getClinicAddress());
                doctorAddress.setTextColor(mContext.getResources().getColor(R.color.grey_shade));

            } else {
                if (clinicDataList.size() > 0) {
                    boolean b = checkAllClinicAddressInSameCity(clinicDataList);
                    if (b) {
                        SpannableString locationString = new SpannableString(clinicDataList.size() + " " + mContext.getString(R.string.locations)+ " " + "in" + " " + cityname);
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
            designLineLayout.setBackground(mContext.getResources().getDrawable(R.drawable.desing_line_for_big_name));
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
        //---------
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

        sizeOfList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(mContext.getString(R.string.clicked_item_data_type_value), mContext.getString(R.string.category_name));
                b.putString(mContext.getString(R.string.clicked_item_data), doctorCategory.getText().toString());
                mServicesCardViewClickListener.onClickOfTotalCount(b);

            }
        });
        doctorCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(mContext.getString(R.string.clicked_item_data_type_value), mContext.getString(R.string.category_name));
                b.putString(mContext.getString(R.string.clicked_item_data), doctorCategory.getText().toString());
                b.putString(mContext.getString(R.string.category_name),doctorObject.getCategoryName());

                mServicesCardViewClickListener.onClickOfTotalCount(b);
            }
        });


        dashBoardCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((position == mDataList.size() - 1) && mIsFavAvail)) {
                    //this is done to show myAppointment doctor in favourite category to avail book option
                    Bundle b = new Bundle();
                    if(!doctorObject.getClinicDataList().isEmpty()) {
                        doctorObject.setNameOfClinicString(doctorObject.getClinicDataList().get(0).getClinicName());
                        doctorObject.setAddressOfDoctorString(doctorObject.getClinicDataList().get(0).getClinicAddress());
                    }
                    //typeDashboard is set for cancel and reshedule flow.
                    doctorObject.setTypedashboard(true);
                    b.putString(mContext.getString(R.string.clicked_item_data_type_value), doctorCategory.getText().toString());
                    b.putParcelable(mContext.getString(R.string.clicked_item_data), doctorObject);
                    b.putString(mContext.getString(R.string.category_name),"");
                    b.putString(RescribeConstants.TYPE_OF_DOCTOR_SEARCH,"");
                    mServicesCardViewClickListener.onClickOfCardView(b);
                }else{
                    Bundle b = new Bundle();
                    if(!doctorObject.getClinicDataList().isEmpty()) {
                        doctorObject.setNameOfClinicString(doctorObject.getClinicDataList().get(0).getClinicName());
                        doctorObject.setAddressOfDoctorString(doctorObject.getClinicDataList().get(0).getClinicAddress());
                    }
                    doctorObject.setTypedashboard(true);
                    b.putString(mContext.getString(R.string.clicked_item_data_type_value), doctorCategory.getText().toString());
                    b.putParcelable(mContext.getString(R.string.clicked_item_data), doctorObject);

                    b.putString(mContext.getString(R.string.category_name),doctorObject.getCategoryName());
                    //TYPE_OF_DOCTOR_SEARCH parameter is set to avail book option for myappointment card
                    b.putString(RescribeConstants.TYPE_OF_DOCTOR_SEARCH,"");
                    mServicesCardViewClickListener.onClickOfCardView(b);
                }

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
        //-----------

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    /**
     *
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
            if (count == list.size()) {
                return true;
            }
        }
        return false;
    }

}