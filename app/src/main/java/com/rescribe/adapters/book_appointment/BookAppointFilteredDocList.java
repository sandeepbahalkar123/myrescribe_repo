package com.rescribe.adapters.book_appointment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;

import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;

import com.rescribe.ui.activities.book_appointment.SelectSlotToBookAppointmentBaseActivity;

import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookAppointFilteredDocList extends RecyclerView.Adapter<BookAppointFilteredDocList.ListViewHolder> {

    private Context mContext;
    private ArrayList<DoctorList> mDataList;
    private int mImageSize;
    private OnFilterDocListClickListener mOnFilterDocListClickListener;

    private ColorGenerator mColorGenerator;

    public BookAppointFilteredDocList(Context mContext, ArrayList<DoctorList> dataList, OnFilterDocListClickListener mOnFilterDocListClickListener, Fragment m) {
        this.mDataList = dataList;
        this.mContext = mContext;
        this.mOnFilterDocListClickListener = mOnFilterDocListClickListener;
        mColorGenerator = ColorGenerator.MATERIAL;
        setColumnNumber(mContext, 2);
    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        mImageSize = (widthPixels / columnNum) - CommonMethods.convertDpToPixel(30);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_appointment_doctor_list, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        final DoctorList doctorObject = mDataList.get(position);
        holder.doctorName.setText(doctorObject.getDocName());
        holder.doctorExperience.setText(doctorObject.getExperience() + mContext.getString(R.string.space) + mContext.getString(R.string.years_experience));
        holder.doctorCategoryType.setText(doctorObject.getCategorySpeciality());
        holder.aboutDoctor.setText(doctorObject.getDegree());
/////
        if (doctorObject.getCategoryName().equals(mContext.getString(R.string.my_appointments))) {
            holder.ruppessIcon.setVisibility(View.INVISIBLE);
            holder.doctorFee.setVisibility(View.INVISIBLE);
            holder.bookAppointmentButton.setVisibility(View.INVISIBLE);
            holder.appointmentDate.setVisibility(View.VISIBLE);
            holder.tokenNo.setVisibility(View.INVISIBLE);
            SpannableString content = new SpannableString(CommonMethods.getFormattedDate(doctorObject.getAptDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE_PATTERN.MMM_DD_YYYY) + ", " + CommonMethods.getFormattedDate(doctorObject.getAptTime(), RescribeConstants.DATE_PATTERN.HH_mm_ss, RescribeConstants.DATE_PATTERN.hh_mm_a));
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            holder.appointmentDate.setText(content);
            if (doctorObject.getClinicDataList().size() > 0) {
                holder.doctorAddress.setText(doctorObject.getClinicDataList().get(0).getClinicAddress());
                holder.clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());
            }
        } else if (doctorObject.getCategoryName().equals(mContext.getString(R.string.sponsered_doctor))) {

            if (doctorObject.getClinicDataList().size() == 1) {
                holder.clinicName.setVisibility(View.VISIBLE);
                holder.clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());
                holder.doctorAddress.setText(doctorObject.getClinicDataList().get(0).getClinicAddress());

            } else {
                if (doctorObject.getClinicDataList().size() > 0) {
                    SpannableString locationString = new SpannableString(doctorObject.getClinicDataList().size() + mContext.getString(R.string.space) + mContext.getString(R.string.locations));
                    locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                    holder.doctorAddress.setText(locationString);
                    holder.clinicName.setVisibility(View.GONE);
                }
            }
            holder.bookAppointmentButton.setVisibility(View.VISIBLE);
            holder.appointmentDate.setVisibility(View.INVISIBLE);

            if (doctorObject.getClinicDataList().size() > 0) {
                holder.ruppessIcon.setVisibility(View.VISIBLE);
                holder.doctorFee.setVisibility(View.VISIBLE);
                holder.doctorFee.setText("" + doctorObject.getClinicDataList().get(0).getAmount());
            } else {
                holder.doctorFee.setVisibility(View.INVISIBLE);
                holder.ruppessIcon.setVisibility(View.INVISIBLE);

            }
            if (doctorObject.getTokenNo().equals("")) {
                holder.bookAppointmentButton.setVisibility(View.VISIBLE);
                holder.tokenNo.setVisibility(View.INVISIBLE);
            } else {
                holder.tokenNo.setVisibility(View.VISIBLE);
                holder.bookAppointmentButton.setVisibility(View.INVISIBLE);
            }

        } else if (doctorObject.getCategoryName().equals(mContext.getString(R.string.recently_visit_doctor))) {
            if (doctorObject.getClinicDataList().size() == 1) {
                holder.clinicName.setVisibility(View.VISIBLE);
                holder.clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());
                holder.doctorAddress.setText(doctorObject.getClinicDataList().get(0).getClinicAddress());

            } else {
                if (doctorObject.getClinicDataList().size() > 0) {
                    SpannableString locationString = new SpannableString(doctorObject.getClinicDataList().size() + mContext.getString(R.string.space) + mContext.getString(R.string.locations));
                    locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                    holder.doctorAddress.setText(locationString);
                    holder.clinicName.setVisibility(View.GONE);
                }
            }
            holder.bookAppointmentButton.setVisibility(View.VISIBLE);
            holder.appointmentDate.setVisibility(View.INVISIBLE);

            if (doctorObject.getClinicDataList().size() > 0) {
                holder.doctorFee.setVisibility(View.VISIBLE);
                holder.ruppessIcon.setVisibility(View.VISIBLE);
                holder.doctorFee.setText("" + doctorObject.getClinicDataList().get(0).getAmount());
            } else {
                holder.doctorFee.setVisibility(View.INVISIBLE);
                holder.ruppessIcon.setVisibility(View.INVISIBLE);

            }
            if (doctorObject.getTokenNo().equals("")) {
                holder.bookAppointmentButton.setVisibility(View.VISIBLE);
                holder.tokenNo.setVisibility(View.INVISIBLE);
            } else {
                holder.tokenNo.setVisibility(View.VISIBLE);
                holder.bookAppointmentButton.setVisibility(View.INVISIBLE);
            }
        } else if (doctorObject.getCategoryName().equals("")) {
            if (doctorObject.getClinicDataList().size() == 1) {
                holder.clinicName.setVisibility(View.VISIBLE);
                holder.clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());
                holder.doctorAddress.setText(doctorObject.getClinicDataList().get(0).getClinicAddress());

            } else {
                if (doctorObject.getClinicDataList().size() > 0) {
                    SpannableString locationString = new SpannableString(doctorObject.getClinicDataList().size() + mContext.getString(R.string.space) + mContext.getString(R.string.locations));
                    locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                    holder.doctorAddress.setText(locationString);
                    holder.clinicName.setVisibility(View.GONE);
                }
            }
            holder.bookAppointmentButton.setVisibility(View.VISIBLE);
            holder.appointmentDate.setVisibility(View.INVISIBLE);

            if (doctorObject.getClinicDataList().size() > 0) {
                holder.doctorFee.setVisibility(View.VISIBLE);
                holder.ruppessIcon.setVisibility(View.VISIBLE);
                holder.doctorFee.setText("" + doctorObject.getClinicDataList().get(0).getAmount());
            } else {
                holder.doctorFee.setVisibility(View.INVISIBLE);
                holder.ruppessIcon.setVisibility(View.INVISIBLE);

            }
            if (doctorObject.getTokenNo().equals("")) {
                holder.bookAppointmentButton.setVisibility(View.VISIBLE);
                holder.tokenNo.setVisibility(View.INVISIBLE);
            } else {
                holder.tokenNo.setVisibility(View.VISIBLE);
                holder.bookAppointmentButton.setVisibility(View.INVISIBLE);
            }
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
        if (doctorObject.getDoctorImageUrl().equals(RescribeConstants.BLANK)) {
            String doctorName = doctorObject.getDocName();
            if (doctorName.contains("Dr. ")) {
                doctorName = doctorName.replace("Dr. ", "");
            }

            int color2 = mColorGenerator.getColor(doctorName);
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .width(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // width in px
                    .height(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // height in px
                    .endConfig()
                    .buildRound(("" + doctorName.charAt(0)).toUpperCase(), color2);
            holder.imageURL.setImageDrawable(drawable);

        } else {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.dontAnimate();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.skipMemoryCache(true);
            requestOptions.override(mImageSize, mImageSize);

            Glide.with(mContext)
                    .load(doctorObject.getDoctorImageUrl())
                    .apply(requestOptions).thumbnail(0.5f)
                    .into(holder.imageURL);
            //--------------
        }
        //-----------

        holder.dataLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putParcelable(mContext.getString(R.string.clicked_item_data), doctorObject);
                b.putString(mContext.getString(R.string.do_operation), mContext.getString(R.string.doctor_details));

                mOnFilterDocListClickListener.onClickOfDoctorRowItem(b);
            }
        });


        holder.favoriteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putParcelable(mContext.getString(R.string.clicked_item_data), doctorObject);
                b.putString(mContext.getString(R.string.do_operation), mContext.getString(R.string.favorite));
                mOnFilterDocListClickListener.onClickOfDoctorRowItem(b);
            }
        });
        holder.doctorlistCardLinearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mOnCardOfAppointmentClickListener.onClickOfCard(doctorObject.getCategoryName());
            }
        });
        holder.bookAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectSlotToBookAppointmentBaseActivity.class);
                intent.putExtra(mContext.getString(R.string.clicked_item_data), doctorObject);
                intent.putExtra(mContext.getString(R.string.toolbarTitle), doctorObject.getCategoryName());
                mContext.startActivity(intent);
            }
        });
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

    public interface OnFilterDocListClickListener {
        void onClickOfDoctorRowItem(Bundle bundleData);
    }

}