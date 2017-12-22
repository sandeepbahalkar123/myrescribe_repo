package com.rescribe.adapters.book_appointment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookAppointFilteredDocList extends RecyclerView.Adapter<BookAppointFilteredDocList.ListViewHolder> {

    private String mReceivedTitleForView;
    private String mClickedItemDataTypeValue;

    private String cityname = "";

    private HelperResponse mHelperResponse;
    private Context mContext;
    private ArrayList<DoctorList> mDataList;
    private ServicesCardViewImpl mOnFilterDocListClickListener;
    private ImageView mClickedItemFavImageView;

    public BookAppointFilteredDocList(Context mContext, ArrayList<DoctorList> dataList, ServicesCardViewImpl mOnFilterDocListClickListener, HelperResponse helperResponse, String mClickedItemDataValue, String mReceivedTitleForView) {
        this.mDataList = dataList;
        this.mContext = mContext;
        this.mOnFilterDocListClickListener = mOnFilterDocListClickListener;
        this.mHelperResponse = helperResponse;
        this.mClickedItemDataTypeValue = mClickedItemDataValue;
        this.mReceivedTitleForView = mReceivedTitleForView;
        String cityNameString = RescribeApplication.getUserSelectedLocationInfo().get(mContext.getString(R.string.location));
        if (cityNameString != null) {
            String[] split = cityNameString.split(",");
            cityname = split[1].trim();
        }
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_appointment_doctor_list, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {
        final DoctorList doctorObject = mDataList.get(position);
        holder.doctorName.setText(doctorObject.getDocName());
        if (doctorObject.getExperience() == 0) {
            holder.doctorExperience.setVisibility(View.GONE);
        } else {
            holder.doctorExperience.setVisibility(View.VISIBLE);
            holder.doctorExperience.setText("" + doctorObject.getExperience() + " " + mContext.getString(R.string.years_experience));

        }
        holder.doctorCategoryType.setText(doctorObject.getCategorySpeciality());
        holder.aboutDoctor.setText(doctorObject.getDegree());
/////
        //-------------
        ArrayList<ClinicData> clinicDataList = doctorObject.getClinicDataList();
        /// MyAppointment Category
        if (doctorObject.getCategoryName().equals(mContext.getString(R.string.my_appointments))) {
            holder.ruppessIcon.setVisibility(View.INVISIBLE);
            holder.doctorFee.setVisibility(View.INVISIBLE);
            holder.bookAppointmentButton.setVisibility(View.INVISIBLE);
            holder.appointmentDate.setVisibility(View.VISIBLE);
            holder.tokenNo.setVisibility(View.INVISIBLE);
            SpannableString content = new SpannableString(CommonMethods.getFormattedDate(doctorObject.getAptDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE_PATTERN.MMM_DD_YYYY) + ", " + CommonMethods.getFormattedDate(doctorObject.getAptTime(), RescribeConstants.DATE_PATTERN.HH_mm_ss, RescribeConstants.DATE_PATTERN.hh_mm_a));
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            holder.appointmentDate.setText(content);
            if (clinicDataList.size() > 0) {
                holder.clinicName.setVisibility(View.VISIBLE);
                holder.doctorAddress.setTextColor(mContext.getResources().getColor(R.color.dose_completed));
                holder.doctorAddress.setText(clinicDataList.get(0).getClinicAddress());
                holder.clinicName.setText(clinicDataList.get(0).getClinicName());
            } else {
                holder.clinicName.setVisibility(View.GONE);
            }
            //Sponsered Doctors
        } else if (doctorObject.getCategoryName().equals(mContext.getString(R.string.sponsored_doctor))) {

            if (clinicDataList.size() == 1) {
                holder.clinicName.setVisibility(View.VISIBLE);
                holder.clinicName.setText(clinicDataList.get(0).getClinicName());
                holder.doctorAddress.setTextColor(mContext.getResources().getColor(R.color.dose_completed));
                holder.doctorAddress.setText(clinicDataList.get(0).getClinicAddress());

            } else {
                if (clinicDataList.size() > 0) {
                    boolean b = checkAllClinicAddressInSameCity(clinicDataList);
                    if (b) {
                        SpannableString locationString = new SpannableString(clinicDataList.size() + " " + mContext.getString(R.string.locations) + " " + "in" + " " + cityname);
                        locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                        holder.doctorAddress.setText(locationString);
                        holder.doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));
                        holder.clinicName.setVisibility(View.GONE);
                    } else {
                        SpannableString locationString = new SpannableString(clinicDataList.size() + " " + mContext.getString(R.string.locations));
                        locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                        holder.doctorAddress.setText(locationString);
                        holder.doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));
                        holder.clinicName.setVisibility(View.GONE);

                    }
                }
            }
            holder.bookAppointmentButton.setVisibility(View.VISIBLE);
            holder.appointmentDate.setVisibility(View.INVISIBLE);

            if (clinicDataList.size() > 0) {
                holder.ruppessIcon.setVisibility(View.VISIBLE);
                holder.doctorFee.setVisibility(View.VISIBLE);
                holder.doctorFee.setText("" + clinicDataList.get(0).getAmount());
            } else {
                holder.doctorFee.setVisibility(View.INVISIBLE);
                holder.ruppessIcon.setVisibility(View.INVISIBLE);
            }

            //--------------
            if (clinicDataList.size() > 0) {
                String appointmentType = doctorObject.getClinicDataList().get(0).getAppointmentType();
                if (mContext.getString(R.string.token).equalsIgnoreCase(appointmentType) || mContext.getString(R.string.mixed).equalsIgnoreCase(appointmentType)) {
                    holder.bookAppointmentButton.setVisibility(View.INVISIBLE);
                    holder.tokenNo.setVisibility(View.VISIBLE);
                } else if (doctorObject.getClinicDataList().get(0).getAppointmentType().equalsIgnoreCase(mContext.getString(R.string.book))) {
                    holder.bookAppointmentButton.setVisibility(View.VISIBLE);
                    holder.tokenNo.setVisibility(View.INVISIBLE);
                }
            }
            //---------------Recently Visited Category

        } else if (doctorObject.getCategoryName().equals(mContext.getString(R.string.recently_visit_doctor))) {
            if (clinicDataList.size() == 1) {
                holder.clinicName.setVisibility(View.VISIBLE);
                holder.clinicName.setText(clinicDataList.get(0).getClinicName());
                holder.doctorAddress.setText(clinicDataList.get(0).getClinicAddress());
                holder.doctorAddress.setTextColor(mContext.getResources().getColor(R.color.dose_completed));

            } else {
                if (clinicDataList.size() > 0) {


                    boolean b = checkAllClinicAddressInSameCity(clinicDataList);
                    if (b) {
                        SpannableString locationString = new SpannableString(clinicDataList.size() + " " + mContext.getString(R.string.locations) + " " + "in" + " " + cityname);
                        locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                        holder.doctorAddress.setText(locationString);
                        holder.doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));
                        holder.clinicName.setVisibility(View.GONE);
                    } else {
                        SpannableString locationString = new SpannableString(clinicDataList.size() + " " + mContext.getString(R.string.locations));
                        locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                        holder.doctorAddress.setText(locationString);
                        holder.doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));
                        holder.clinicName.setVisibility(View.GONE);

                    }

                }
            }
            holder.bookAppointmentButton.setVisibility(View.VISIBLE);
            holder.appointmentDate.setVisibility(View.INVISIBLE);

            //----------
            if (clinicDataList.size() > 0) {
                holder.doctorFee.setVisibility(View.VISIBLE);
                holder.ruppessIcon.setVisibility(View.VISIBLE);
                holder.doctorFee.setText("" + clinicDataList.get(0).getAmount());
            } else {
                holder.doctorFee.setVisibility(View.INVISIBLE);
                holder.ruppessIcon.setVisibility(View.INVISIBLE);
            }
            //----------
            if (clinicDataList.size() > 0) {
                String appointmentType = doctorObject.getClinicDataList().get(0).getAppointmentType();
                if (mContext.getString(R.string.token).equalsIgnoreCase(appointmentType) || mContext.getString(R.string.mixed).equalsIgnoreCase(appointmentType)) {
                    holder.bookAppointmentButton.setVisibility(View.INVISIBLE);
                    holder.tokenNo.setVisibility(View.VISIBLE);
                } else if (doctorObject.getClinicDataList().get(0).getAppointmentType().equalsIgnoreCase(mContext.getString(R.string.book))) {
                    holder.bookAppointmentButton.setVisibility(View.VISIBLE);
                    holder.tokenNo.setVisibility(View.INVISIBLE);
                }
            }
            //---------------if Doctor Doesnt belong to any Category
        } else if (doctorObject.getCategoryName().equals("")) {
            if (clinicDataList.size() == 1) {
                holder.clinicName.setVisibility(View.VISIBLE);
                holder.clinicName.setText(clinicDataList.get(0).getClinicName());
                holder.doctorAddress.setText(clinicDataList.get(0).getClinicAddress());
                holder.doctorAddress.setTextColor(mContext.getResources().getColor(R.color.dose_completed));

            } else {
                if (doctorObject.getClinicDataList().size() > 0) {

                    boolean b = checkAllClinicAddressInSameCity(clinicDataList);
                    if (b) {
                        SpannableString locationString = new SpannableString(clinicDataList.size() + " " + mContext.getString(R.string.locations) + " " + "in" + " " + cityname);
                        locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                        holder.doctorAddress.setText(locationString);
                        holder.doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));
                        holder.clinicName.setVisibility(View.GONE);
                    } else {
                        SpannableString locationString = new SpannableString(clinicDataList.size() + " " + mContext.getString(R.string.locations));
                        locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                        holder.doctorAddress.setText(locationString);
                        holder.doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));
                        holder.clinicName.setVisibility(View.GONE);

                    }

                }
            }
            holder.bookAppointmentButton.setVisibility(View.VISIBLE);
            holder.appointmentDate.setVisibility(View.INVISIBLE);

            if (clinicDataList.size() > 0) {
                holder.doctorFee.setVisibility(View.VISIBLE);
                holder.ruppessIcon.setVisibility(View.VISIBLE);
                holder.doctorFee.setText("" + clinicDataList.get(0).getAmount());
            } else {
                holder.doctorFee.setVisibility(View.INVISIBLE);
                holder.ruppessIcon.setVisibility(View.INVISIBLE);

            }

            //----------
            if (clinicDataList.size() > 0) {
                String appointmentType = doctorObject.getClinicDataList().get(0).getAppointmentType();
                if (mContext.getString(R.string.token).equalsIgnoreCase(appointmentType) || mContext.getString(R.string.mixed).equalsIgnoreCase(appointmentType)) {
                    holder.bookAppointmentButton.setVisibility(View.INVISIBLE);
                    holder.tokenNo.setVisibility(View.VISIBLE);
                } else if (doctorObject.getClinicDataList().get(0).getAppointmentType().equalsIgnoreCase(mContext.getString(R.string.book))) {
                    holder.bookAppointmentButton.setVisibility(View.VISIBLE);
                    holder.tokenNo.setVisibility(View.INVISIBLE);
                }
            }
            //---------------
        }

        ////-------------------
        if (doctorObject.getRating() == 0) {
            holder.ratingBar.setVisibility(View.INVISIBLE);
            holder.doctorRating.setVisibility(View.INVISIBLE);
        } else {
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.doctorRating.setVisibility(View.VISIBLE);
            holder.ratingBar.setRating((float) doctorObject.getRating());
            holder.doctorRating.setText("" + doctorObject.getRating());
        }
        ////-------------------


        //----------
        if (doctorObject.getFavourite()) {
            holder.favoriteView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.favourite_icon));
        } else {
            holder.favoriteView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.favourite_line_icon));
        }
        //-----------
        String doctorName = doctorObject.getDocName();
        if (doctorName.contains("Dr. ")) {
            doctorName = doctorName.replace("Dr. ", "");
        }
        //----------
        TextDrawable textDrawable = CommonMethods.getTextDrawable(mContext, doctorName);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        requestOptions.placeholder(textDrawable);
        requestOptions.error(textDrawable);

        Glide.with(mContext)
                .load(doctorObject.getDoctorImageUrl())
                .apply(requestOptions).thumbnail(0.5f)
                .into(holder.imageURL);
        //--------------

        //-----------

        holder.favoriteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView) v;
                boolean status = !doctorObject.getFavourite();
                mClickedItemFavImageView = imageView;
                mOnFilterDocListClickListener.onFavoriteIconClick(status, doctorObject, imageView, mHelperResponse);
            }
        });

        holder.dataLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();

                b.putString(mContext.getString(R.string.clicked_item_data_type_value), doctorObject.getDocSpeciality());
                b.putParcelable(mContext.getString(R.string.clicked_item_data), doctorObject);
                b.putString(mContext.getString(R.string.opening_mode), mClickedItemDataTypeValue);
                b.putString(mContext.getString(R.string.toolbarTitle), mReceivedTitleForView);

                mOnFilterDocListClickListener.onClickOfCardView(b);
            }
        });

        holder.bookAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(mContext.getString(R.string.clicked_item_data_type_value), mContext.getString(R.string.book_appointment));
                b.putParcelable(mContext.getString(R.string.clicked_item_data), doctorObject);

                b.putInt(mContext.getString(R.string.selected_clinic_data_position), 0);
                b.putString(mContext.getString(R.string.opening_mode), mClickedItemDataTypeValue);
                b.putString(mContext.getString(R.string.toolbarTitle), mReceivedTitleForView);

                mOnFilterDocListClickListener.onClickedOfBookButton(b);
            }
        });

        holder.tokenNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(mContext.getString(R.string.clicked_item_data_type_value), mContext.getString(R.string.token_number));
                b.putInt(mContext.getString(R.string.selected_clinic_data_position), 0);
                b.putParcelable(mContext.getString(R.string.clicked_item_data), doctorObject);
                b.putString(mContext.getString(R.string.opening_mode), mClickedItemDataTypeValue);
                b.putString(mContext.getString(R.string.toolbarTitle), mReceivedTitleForView);

                mOnFilterDocListClickListener.onClickedOfTokenNumber(b);
            }
        });

        //----*********-------------
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.doctorName)
        CustomTextView doctorName;
        @BindView(R.id.doctorExperience)
        CustomTextView doctorExperience;
        @BindView(R.id.doctorAddress)
        CustomTextView doctorAddress;
        @BindView(R.id.doctorFee)
        CustomTextView doctorFee;
        @BindView(R.id.distance)
        CustomTextView distance;
        @BindView(R.id.doctorRating)
        CustomTextView doctorRating;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        @BindView(R.id.aboutDoctor)
        CustomTextView aboutDoctor;
        @BindView(R.id.clinicName)
        CustomTextView clinicName;
        @BindView(R.id.tokenNo)
        ImageView tokenNo;
        @BindView(R.id.favoriteView)
        ImageView favoriteView;
        @BindView(R.id.imageURL)
        CircularImageView imageURL;
        @BindView(R.id.dataLayout)
        LinearLayout dataLayout;
        @BindView(R.id.bookAppointmentButton)
        ImageView bookAppointmentButton;
        @BindView(R.id.appointmentDate)
        CustomTextView appointmentDate;
        @BindView(R.id.doctorlistCardLinearlayout)
        LinearLayout doctorlistCardLinearlayout;
        @BindView(R.id.ruppessIcon)
        ImageView ruppessIcon;
        @BindView(R.id.doctorCategoryType)
        CustomTextView doctorCategoryType;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public void updateClickedItemFavImage() {
        DoctorList userSelectedDoctorListDataObject = ServicesCardViewImpl.getUserSelectedDoctorListDataObject();
        if (userSelectedDoctorListDataObject.getFavourite()) {
            mClickedItemFavImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.favourite_icon));
        } else {
            mClickedItemFavImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.favourite_line_icon));
        }
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
            if (count == list.size()) {
                return true;
            }
        }
        return false;
    }
}