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

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;

/**
 * Created by jeetal on 11/10/17.
 */

public class ShowBackgroundViewPagerAdapter extends PagerAdapter {

    private ArrayList<String> mDataList;
    private LayoutInflater mInflater;
    private Context mContext;
    private int mImageSize;
    private ColorGenerator mColorGenerator;

    public ShowBackgroundViewPagerAdapter(Context context, ArrayList<String> doctorLists) {
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
        View imageLayout = mInflater.inflate(R.layout.background_item, view, false);
        assert imageLayout != null;

        final ImageView dashboardBackgroundLayout = (ImageView) imageLayout
                .findViewById(R.id.dashboardBackgroundLayout);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);

        Glide.with(mContext)
                .load(mDataList.get(position))
                .into(dashboardBackgroundLayout);

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

}