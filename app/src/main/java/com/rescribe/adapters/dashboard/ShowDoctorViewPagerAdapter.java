package com.rescribe.adapters.dashboard;

import android.content.Context;
import android.os.Parcelable;
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
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.dashboard.DoctorData;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by jeetal on 11/10/17.
 */

public class ShowDoctorViewPagerAdapter extends PagerAdapter {

    private ArrayList<DoctorData> mDataList;
    private LayoutInflater mInflater;
    private Context mContext;
    private int mImageSize;
    private ColorGenerator mColorGenerator;


    public ShowDoctorViewPagerAdapter(Context context, ArrayList<DoctorData> doctorLists) {
        this.mContext = context;
        this.mDataList = doctorLists;
        mColorGenerator = ColorGenerator.MATERIAL;
        setColumnNumber(mContext, 2);
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
        View imageLayout = mInflater.inflate(R.layout.doctor_list_item_dashboard, view, false);
        assert imageLayout != null;


        final CustomTextView doctorNameTextView = (CustomTextView) imageLayout
                .findViewById(R.id.doctorName);
        final CustomTextView doctorType = (CustomTextView) imageLayout
                .findViewById(R.id.doctorType);
        final CustomTextView doctorExperience = (CustomTextView) imageLayout
                .findViewById(R.id.doctorExperience);
        final CustomTextView doctorAddress = (CustomTextView) imageLayout
                .findViewById(R.id.doctorAddress);
        final CustomTextView feesToPaid = (CustomTextView) imageLayout
                .findViewById(R.id.feesToPaidVisit);
        final CustomTextView doctorCategory = (CustomTextView) imageLayout
                .findViewById(R.id.doctorCategoryVisit);
        final ImageView favorite = (ImageView) imageLayout
                .findViewById(R.id.favorite);
        final CustomTextView recentVisit = (CustomTextView) imageLayout
                .findViewById(R.id.recentVisit);
        final CircularImageView imageURL = (CircularImageView) imageLayout
                .findViewById(R.id.imageURL);
        final LinearLayout thumbnail = (LinearLayout) imageLayout
                .findViewById(R.id.thumbnail);

        final DoctorData doctorObject = mDataList.get(position);
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
            requestOptions.placeholder(R.drawable.layer_12);

            Glide.with(mContext)
                    .load(doctorObject.getDoctorImageUrl())
                    .apply(requestOptions).thumbnail(0.5f)
                    .into(imageURL);
        }
        doctorNameTextView.setText(doctorObject.getDocName());
        doctorType.setText(doctorObject.getDegree());
        doctorExperience.setText(doctorObject.getExperience() + mContext.getString(R.string.space) + mContext.getString(R.string.years_experience));
        doctorAddress.setText(doctorObject.getDoctorAddress());
        doctorCategory.setText(doctorObject.getCategoryName());
        feesToPaid.setText(doctorObject.getAmount());
        if (doctorObject.getRecentlyVisited()) {
            recentVisit.setVisibility(View.VISIBLE);
        } else {
            recentVisit.setVisibility(View.GONE);
        }
        if (doctorObject.getFavourite()) {
            favorite.setVisibility(View.VISIBLE);
        } else {
            favorite.setVisibility(View.INVISIBLE);
        }
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

    @Override
    public float getPageWidth(int position) {
        return 0.9f;
    }
}