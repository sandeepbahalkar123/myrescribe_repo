package com.rescribe.adapters.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.rescribe.R;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;

import java.util.ArrayList;

/**
 * Created by jeetal on 11/10/17.
 */

public class ShowBackgroundViewPagerAdapter extends PagerAdapter {

    private final String activityCreatedTimeStamp;
    //    private final int widthPixelOfBanner;
    private ArrayList<String> cardsBack;
    private LayoutInflater mInflater;
    private Context mContext;

    public ShowBackgroundViewPagerAdapter(Context context, String activityCreatedTimeStamp, ArrayList<String> cardsBack) {
        this.mContext = context;
        this.cardsBack = cardsBack;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
//        widthPixelOfBanner = metrics.widthPixels;
        mInflater = LayoutInflater.from(context);
        this.activityCreatedTimeStamp = activityCreatedTimeStamp;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return cardsBack.size();
    }

    @SuppressLint("CheckResult")
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = mInflater.inflate(R.layout.background_item, view, false);

        final ImageView dashboardBackgroundLayout = (ImageView) imageLayout
                .findViewById(R.id.dashboardBackgroundLayout);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.signature(new ObjectKey(activityCreatedTimeStamp + cardsBack.get(position)));

        Glide.with(mContext)
                .load(cardsBack.get(position))
                .apply(requestOptions)
                .into(dashboardBackgroundLayout);

        view.addView(imageLayout, 0);

        return imageLayout;
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

}