package com.rescribe.adapters.book_appointment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.book_appointment.doctor_data.ClinicData;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Map;

public class ShowRecentVisitedDoctorPagerAdapter extends PagerAdapter {

    private OnViewPagerItemClickListener mOnViewPagerItemClickListener;
    private Map<String, Integer> mListSizeWithTypeMap;
    private ArrayList<DoctorList> mDoctorLists;
    private LayoutInflater mInflater;
    private Context mContext;
    private int mImageSize;
    private ColorGenerator mColorGenerator;


    public ShowRecentVisitedDoctorPagerAdapter(Context context, ArrayList<DoctorList> doctorLists, Map<String, Integer> dataMap, ShowRecentVisitedDoctorPagerAdapter.OnViewPagerItemClickListener listener) {
        this.mContext = context;
        this.mDoctorLists = doctorLists;
        mColorGenerator = ColorGenerator.MATERIAL;
        setColumnNumber(mContext, 2);
        mInflater = LayoutInflater.from(context);
        this.mListSizeWithTypeMap = dataMap;
        mOnViewPagerItemClickListener = listener;

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mDoctorLists.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = mInflater.inflate(R.layout.recently_visited_pager_item, view, false);
        assert imageLayout != null;

        //---------
        final TextView doctorNameTextView = (TextView) imageLayout
                .findViewById(R.id.doctorName);
        final TextView doctorCategory = (TextView) imageLayout
                .findViewById(R.id.doctorCategoryVisit);
        final TextView doctorType = (TextView) imageLayout
                .findViewById(R.id.doctorType);
        final TextView doctorExperience = (TextView) imageLayout
                .findViewById(R.id.doctorExperience);
        final TextView doctorAddress = (TextView) imageLayout
                .findViewById(R.id.doctorAddress);
        final TextView doctorFees = (TextView) imageLayout
                .findViewById(R.id.feesToPaidVisit);
        final TextView doctorRating = (TextView) imageLayout
                .findViewById(R.id.doctorRating);
        final ImageView bookAppointmentButton = (ImageView) imageLayout
                .findViewById(R.id.bookAppointmentButton);

        final CircularImageView imageURL = (CircularImageView) imageLayout
                .findViewById(R.id.imageURL);
        final CustomTextView doctorAppointmentDate = (CustomTextView) imageLayout
                .findViewById(R.id.doctorAppointmentDate);
        final ImageView favorite = (ImageView) imageLayout
                .findViewById(R.id.favorite);
        final CustomTextView doctorCategoryType = (CustomTextView) imageLayout
                .findViewById(R.id.doctorCategoryType);
        //---------
        DoctorList doctorListObject = mDoctorLists.get(position);

        if (doctorListObject.getDoctorImageUrl().equals(RescribeConstants.BLANK)) {
            String doctorName = doctorListObject.getDocName();
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
            imageURL.setImageDrawable(drawable);

        } else {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.dontAnimate();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.skipMemoryCache(true);
            requestOptions.override(mImageSize, mImageSize);
            requestOptions.placeholder(R.drawable.layer_12);

            Glide.with(mContext)
                    .load(mDoctorLists.get(position).getDoctorImageUrl())
                    .apply(requestOptions).thumbnail(0.5f)
                    .into(imageURL);
            //--------------
        }

        //---------
        doctorNameTextView.setText(doctorListObject.getDocName());
        doctorType.setText(doctorListObject.getDegree());
        doctorExperience.setText("" + doctorListObject.getExperience() + mContext.getString(R.string.space) + mContext.getString(R.string.years_experience));

        //---------
        if (doctorListObject.getFavourite()) {
            favorite.setVisibility(View.VISIBLE);
        } else {
            favorite.setVisibility(View.GONE);
        }
        //-----------
        if (doctorListObject.getRating() == 0) {
            doctorRating.setVisibility(View.INVISIBLE);
        } else {
            doctorRating.setVisibility(View.VISIBLE);
            doctorRating.setText("" + doctorListObject.getRating());
        }
        //---------

        //---------
        if (doctorListObject.getCategorySpeciality() != null) {
            doctorCategoryType.setText(doctorListObject.getCategorySpeciality());
            doctorCategoryType.setVisibility(View.VISIBLE);
        } else {
            doctorCategoryType.setVisibility(View.INVISIBLE);
        }
        //-----THIS IS DONE TO SHOW COUNT OF FAVORITE(CUSTOM CREATED CATEGORY), ASSUME IT WILL COME LAST ALWAYS ----
        int size;
        if (position == mDoctorLists.size() - 1) {
            doctorCategory.setText(mContext.getString(R.string.favorite));
            size = mListSizeWithTypeMap.get(mContext.getString(R.string.favorite));
        } else {
            doctorCategory.setText(doctorListObject.getCategoryName());
            size = mListSizeWithTypeMap.get(doctorListObject.getCategoryName());
        }
        //  sizeOfList.setText("" + size); // TOD ADDED YET

        //-----------

        //---------
        if (mContext.getString(R.string.recently_visited_doctor).equalsIgnoreCase(doctorListObject.getCategoryName())) {
            doctorFees.setVisibility(View.VISIBLE);
            ArrayList<ClinicData> clinicDataList = doctorListObject.getClinicDataList();
            if (clinicDataList.size() > 0) {
                doctorAddress.setText(clinicDataList.get(0).getClinicAddress());
                doctorFees.setText("" + clinicDataList.get(0).getAmt());
            }
            doctorAppointmentDate.setVisibility(View.INVISIBLE);
            bookAppointmentButton.setVisibility(View.VISIBLE);
        } else if (mContext.getString(R.string.my_appointments).equalsIgnoreCase(doctorListObject.getCategoryName())) {
            doctorAppointmentDate.setVisibility(View.VISIBLE);
            SpannableString content = new SpannableString(doctorListObject.getAptDate() + " " + doctorListObject.getAptTime());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            doctorAppointmentDate.setText(content);
            bookAppointmentButton.setVisibility(View.INVISIBLE);
            doctorFees.setVisibility(View.INVISIBLE);
        } else if (mContext.getString(R.string.sponsored_doctor).equalsIgnoreCase(doctorListObject.getCategoryName())) {
            doctorFees.setVisibility(View.VISIBLE);
            ArrayList<ClinicData> clinicDataList = doctorListObject.getClinicDataList();
            if (clinicDataList.size() > 0) {
                doctorAddress.setText(clinicDataList.get(0).getClinicAddress());
                doctorFees.setText("" + clinicDataList.get(0).getAmt());
            }
            doctorAppointmentDate.setVisibility(View.INVISIBLE);
            bookAppointmentButton.setVisibility(View.VISIBLE);
        }
        //---------

        //-- TODO: NEED CLICK OF COUNT
        doctorCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(mContext.getString(R.string.clicked_item_data_type_value), doctorCategory.getText().toString());
                mOnViewPagerItemClickListener.setOnClickedOfViewPagerItem(b);
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

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public interface OnViewPagerItemClickListener {
        void setOnClickedOfViewPagerItem(Bundle bundleData);
    }
}