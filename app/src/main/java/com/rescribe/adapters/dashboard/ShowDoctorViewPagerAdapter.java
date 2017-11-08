package com.rescribe.adapters.dashboard;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

/**
 * Created by jeetal on 11/10/17.
 */

public class ShowDoctorViewPagerAdapter extends PagerAdapter {

    private ArrayList<DoctorList> mDataList;
    private LayoutInflater mInflater;
    private Context mContext;
    private int mImageSize;
    private OnClickOfCardOnDashboard mOnClickOfCardOnDashboard;
    private ColorGenerator mColorGenerator;


    public ShowDoctorViewPagerAdapter(Context context, ArrayList<DoctorList> doctorLists, OnClickOfCardOnDashboard mOnClickOfCardOnDashboard) {
        this.mContext = context;
        this.mDataList = doctorLists;
        mColorGenerator = ColorGenerator.MATERIAL;
        setColumnNumber(mContext, 2);
        this.mOnClickOfCardOnDashboard = mOnClickOfCardOnDashboard;
        mInflater = LayoutInflater.from(context);
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
        View imageLayout = mInflater.inflate(R.layout.doctor_details_view_item, view, false);
        assert imageLayout != null;


        final LinearLayout dashBoardCard = (LinearLayout) imageLayout
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
        final CircularImageView imageURL = (CircularImageView) imageLayout
                .findViewById(R.id.imageURL);
        final LinearLayout thumbnail = (LinearLayout) imageLayout
                .findViewById(R.id.thumbnail);

        final DoctorList doctorObject = mDataList.get(position);
        if (doctorObject.getDoctorImageUrl().equals("")) {
            String doctorName = doctorObject.getDocName();
            if (doctorName.contains("Dr. ")) {
                doctorName = doctorName.replace("Dr. ", "");
            }

            if (doctorName != null) {
                int color2 = mColorGenerator.getColor(doctorName);
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .width(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // width in px
                        .height(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // height in px
                        .endConfig()
                        .buildRound(("" + doctorName.charAt(0)).toUpperCase(), color2);
                imageURL.setImageDrawable(drawable);
            }
        } else {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.dontAnimate();
            requestOptions.override(mImageSize, mImageSize);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.skipMemoryCache(true);

            Glide.with(mContext)
                    .load(doctorObject.getDoctorImageUrl())
                    .apply(requestOptions).thumbnail(0.5f)
                    .into(imageURL);
        }

        doctorNameTextView.setText(doctorObject.getDocName());
        doctorType.setText(doctorObject.getDegree());
        doctorExperience.setText(doctorObject.getExperience() + mContext.getString(R.string.space) + mContext.getString(R.string.years_experience));
        if (doctorObject.getClinicDataList().size() == 1) {
            doctorAddress.setText(doctorObject.getClinicDataList().get(0).getClinicAddress());
        } else {
            doctorAddress.setText(doctorObject.getClinicDataList().size() + mContext.getString(R.string.space) + mContext.getString(R.string.locations));
        }
        doctorCategory.setText(doctorObject.getCategoryName());
        if(doctorObject.getRating()==0){
            doctorRating.setVisibility(View.INVISIBLE);
        }else {
            doctorRating.setVisibility(View.VISIBLE);
            doctorRating.setText("" + doctorObject.getRating());

        }
        doctorCategoryType.setText(doctorObject.getCategorySpeciality());
        sizeOfList.setText(""+doctorObject.getSizeOfList());
        sizeOfList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(doctorObject.getCategoryName().equals(mContext.getString(R.string.my_appointments))) {
                    mOnClickOfCardOnDashboard.onClickOfCount(mContext.getString(R.string.my_appointments));
                }else if(doctorObject.getCategoryName().equals(mContext.getString(R.string.sponsered_doctor))) {
                    mOnClickOfCardOnDashboard.onClickOfCount(mContext.getString(R.string.sponsered_doctor));
                }else if(doctorObject.getCategoryName().equals(mContext.getString(R.string.recently_visit_doctor))) {
                    mOnClickOfCardOnDashboard.onClickOfCount(mContext.getString(R.string.recently_visit_doctor));
                }
            }
        });
        dashBoardCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(doctorObject.getCategoryName().equals(mContext.getString(R.string.my_appointments))) {
                    mOnClickOfCardOnDashboard.onClickOfDashboardDoctorItem(mContext.getString(R.string.my_appointments));
                }else if(doctorObject.getCategoryName().equals(mContext.getString(R.string.sponsered_doctor))) {
                    mOnClickOfCardOnDashboard.onClickOfDashboardDoctorItem(mContext.getString(R.string.sponsered_doctor));
                }else if(doctorObject.getCategoryName().equals(mContext.getString(R.string.recently_visit_doctor))) {
                    mOnClickOfCardOnDashboard.onClickOfDashboardDoctorItem(mContext.getString(R.string.recently_visit_doctor));
                }
            }
        });
        if (doctorObject.getCategoryName().equals(mContext.getString(R.string.my_appointments))) {
            bookAppointmentButton.setVisibility(View.INVISIBLE);
            doctorAppointmentDate.setVisibility(View.VISIBLE);
            SpannableString content = new SpannableString(CommonMethods.getFormattedDate(doctorObject.getAptDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE_PATTERN.MMM_DD_YYYY) + ", " + CommonMethods.getFormattedDate(doctorObject.getAptTime(), RescribeConstants.DATE_PATTERN.HH_mm_ss, RescribeConstants.DATE_PATTERN.hh_mm_a));
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            doctorAppointmentDate.setText(content);

        } else {
            if (doctorObject.getClinicDataList().size() > 0) {
                feesToPaid.setVisibility(View.VISIBLE);
                feesToPaid.setText("" + doctorObject.getClinicDataList().get(0).getAmt());

            } else {
                feesToPaid.setVisibility(View.INVISIBLE);
            }

            bookAppointmentButton.setVisibility(View.VISIBLE);
            doctorAppointmentDate.setVisibility(View.INVISIBLE);
        }

      /*  if (doctorObject.getRecentlyVisited()) {
            recentVisit.setVisibility(View.VISIBLE);
        } else {
            recentVisit.setVisibility(View.GONE);
        }*/
        if (doctorObject.getFavourite()) {
            favorite.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dashboard_heart_fav));

        } else {
            favorite.setImageDrawable(mContext.getResources().getDrawable(R.drawable.result_line_heart_fav));
        }
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickOfCardOnDashboard.onClickOfFavourite(doctorObject.getFavourite(),doctorObject.getDocId());
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

    public interface OnClickOfCardOnDashboard {
        void onClickOfDashboardDoctorItem(String nameOfClickOnItem);
        void onClickOfCount(String nameOfCategoryType);
        void onClickOfFavourite(boolean isFavourite,int docId);
    }

}