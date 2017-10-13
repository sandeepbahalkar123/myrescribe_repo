package com.rescribe.adapters.book_appointment;

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
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;

public class ShowRecentVisitedDoctorPagerAdapter extends PagerAdapter {

    private ArrayList<DoctorList> mDoctorLists;
    private LayoutInflater mInflater;
    private Context mContext;
    private int mImageSize;
    private ColorGenerator mColorGenerator;


    public ShowRecentVisitedDoctorPagerAdapter(Context context, ArrayList<DoctorList> doctorLists) {
        this.mContext = context;
        this.mDoctorLists = doctorLists;
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
        return mDoctorLists.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = mInflater.inflate(R.layout.recently_visit_doctors_layout, view, false);
        assert imageLayout != null;

        final TextView doctorNameTextView = (TextView) imageLayout
                .findViewById(R.id.doctorName);
        final TextView doctorType = (TextView) imageLayout
                .findViewById(R.id.doctorType);
        final TextView doctorExperience = (TextView) imageLayout
                .findViewById(R.id.doctorExperience);
        final TextView doctorAddress = (TextView) imageLayout
                .findViewById(R.id.doctorAddress);
        final TextView doctorFees = (TextView) imageLayout
                .findViewById(R.id.doctorFees);
        final TextView kilometers = (TextView) imageLayout
                .findViewById(R.id.kilometers);
        final ImageView favoriteView = (ImageView) imageLayout
                .findViewById(R.id.favoriteView);
        final TextView recentVisitView = (TextView) imageLayout
                .findViewById(R.id.recentVisitView);
        final CircularImageView imageURL = (CircularImageView) imageLayout
                .findViewById(R.id.imageURL);
        if(!mDoctorLists.get(position).getRecentlyVisited()){
            recentVisitView.setVisibility(View.INVISIBLE);
        }else if(!mDoctorLists.get(position).getFavourite()){
            favoriteView.setVisibility(View.INVISIBLE);
        }else if(!mDoctorLists.get(position).getRecentlyVisited()&&!mDoctorLists.get(position).getFavourite()){
            favoriteView.setVisibility(View.INVISIBLE);
            recentVisitView.setVisibility(View.INVISIBLE);
        }else{
            favoriteView.setVisibility(View.VISIBLE);
            recentVisitView.setVisibility(View.VISIBLE);
        }

        if (mDoctorLists.get(position).getDoctorImageUrl().equals("")) {
            String doctorName = mDoctorLists.get(position).getDocName();
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

        doctorNameTextView.setText(mDoctorLists.get(position).getDocName());
        doctorType.setText(mDoctorLists.get(position).getDegree());
        doctorExperience.setText(""+ mDoctorLists.get(position).getExperience()+mContext.getString(R.string.space)+mContext.getString(R.string.years_experience));
        doctorAddress.setText(mDoctorLists.get(position).getDoctorAddress());
        doctorFees.setText(""+ mDoctorLists.get(position).getAmount());
        SpannableString content = new SpannableString(""+ mDoctorLists.get(position).getDistance());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        kilometers.setText(content);
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


   /* public void addItem() {
        mSize++;
        notifyDataSetChanged();
    }

    public void removeItem() {
        mSize--;
        mSize = mSize < 0 ? 0 : mSize;

        notifyDataSetChanged();
    }*/
}