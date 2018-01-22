package com.rescribe.adapters.dashboard;

import android.content.Context;
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

import java.util.ArrayList;

/**
 * Created by jeetal on 11/10/17.
 */

public class ShowBackgroundViewPagerAdapter extends PagerAdapter {

    private final String activityCreatedTimeStamp;
    //    private final int widthPixelOfBanner;
    private ArrayList<String> mDataList;
    private LayoutInflater mInflater;
    private Context mContext;

    public ShowBackgroundViewPagerAdapter(Context context, ArrayList<String> doctorLists, String activityCreatedTimeStamp) {
        this.mContext = context;
        this.mDataList = doctorLists;

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
        return mDataList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = mInflater.inflate(R.layout.background_item, view, false);

        final ImageView dashboardBackgroundLayout = (ImageView) imageLayout
                .findViewById(R.id.dashboardBackgroundLayout);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.signature(new ObjectKey(activityCreatedTimeStamp));
//        requestOptions.override(widthPixelOfBanner - dashboardBackgroundLayout.getContext().getResources().getDimensionPixelSize(R.dimen.dp10));
//        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
//        requestOptions.skipMemoryCache(true);

        Glide.with(mContext)
                .load(mDataList.get(position))
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