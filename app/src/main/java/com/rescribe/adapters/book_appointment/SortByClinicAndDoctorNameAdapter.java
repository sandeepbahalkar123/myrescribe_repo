package com.rescribe.adapters.book_appointment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.book_appointment.RecentVisitDoctorFragment;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 26/10/17.
 */

public class SortByClinicAndDoctorNameAdapter extends RecyclerView.Adapter<SortByClinicAndDoctorNameAdapter.ListViewHolder> implements Filterable {


    @BindView(R.id.blueLine)
    ImageView blueLine;
    @BindView(R.id.doctorCategoryType)
    CustomTextView doctorCategoryType;
    @BindView(R.id.doctorFee)
    CustomTextView doctorFee;
    @BindView(R.id.imageURL)
    CircularImageView imageURL;
    @BindView(R.id.thumbnail)
    LinearLayout thumbnail;
    @BindView(R.id.doctorName)
    CustomTextView doctorName;
    @BindView(R.id.doctorExperience)
    CustomTextView doctorExperience;
    @BindView(R.id.doctorAddress)
    CustomTextView doctorAddress;
    @BindView(R.id.dataLayout)
    LinearLayout dataLayout;
    @BindView(R.id.favoriteView)
    ImageView favoriteView;
    @BindView(R.id.gMapLocationView)
    ImageView gMapLocationView;
    @BindView(R.id.distance)
    CustomTextView distance;
    @BindView(R.id.doctorRating)
    CustomTextView doctorRating;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    private Fragment mFragment;
    private Context mContext;
    private ArrayList<DoctorList> mDataList;
    private int mImageSize;
    private ArrayList<DoctorList> mArrayList;
    private OnClinicAndDoctorNameSearchRowItem mOnClinicAndDoctorNameSearchRowItem;
    private String mSearchString;
    private ColorGenerator mColorGenerator;
    private String mSearchClinicNameString;

    public SortByClinicAndDoctorNameAdapter(Context mContext, ArrayList<DoctorList> dataList, OnClinicAndDoctorNameSearchRowItem mOnClinicAndDoctorNameSearchRowItem, Fragment m) {
        this.mDataList = dataList;
        this.mContext = mContext;
        this.mArrayList = dataList;
        this.mOnClinicAndDoctorNameSearchRowItem = mOnClinicAndDoctorNameSearchRowItem;
        this.mFragment = m;
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
    public SortByClinicAndDoctorNameAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_appointment_doctor_list, parent, false);

        return new SortByClinicAndDoctorNameAdapter.ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SortByClinicAndDoctorNameAdapter.ListViewHolder holder, int position) {
        final DoctorList doctorObject = mDataList.get(position);

        // holder.waitingTime.setText("" + mContext.getString(R.string.waiting_for) + mContext.getString(R.string.space) + doctorObject.getWaitingTime());
        //holder.tokenNo.setText(mContext.getString(R.string.token_no_available));

        holder.aboutDoctor.setText(doctorObject.getDegree());
        if (doctorObject.getRating().equals("NA")) {
            holder.doctorRating.setVisibility(View.INVISIBLE);
            holder.ratingBar.setVisibility(View.INVISIBLE);
        } else {
            holder.doctorRating.setVisibility(View.VISIBLE);
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.doctorRating.setText("" + doctorObject.getRating());
            holder.ratingBar.setRating(Float.parseFloat(doctorObject.getRating()));
        }


        holder.doctorExperience.setText("" + doctorObject.getExperience() + mContext.getString(R.string.space) + mContext.getString(R.string.years_experience));
        holder.doctorAddress.setText(doctorObject.getAddressOfDoctor());
        holder.doctorFee.setText("" + doctorObject.getAmount());
        SpannableString content = new SpannableString(doctorObject.getDistance());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        holder.distance.setText(content);

        //-------Load image-------
        if (doctorObject.getDoctorImageUrl().equals("")) {
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
            requestOptions.placeholder(R.drawable.layer_12);

            Glide.with(mContext)
                    .load(doctorObject.getDoctorImageUrl())
                    .apply(requestOptions).thumbnail(0.5f)
                    .into(holder.imageURL);
            //--------------
        }

        holder.dataLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putParcelable(mContext.getString(R.string.clicked_item_data), doctorObject);
                b.putString(mContext.getString(R.string.do_operation), mContext.getString(R.string.doctor_details));
                mOnClinicAndDoctorNameSearchRowItem.onClinicAndDoctorNameSearchRowItem(b);
            }
        });
        SpannableString spannableStringSearch = null;
        SpannableString spannableClinicNameString = null;
        if ((mSearchString != null) && (!mSearchString.isEmpty())) {

            if (doctorObject.getDocName().toLowerCase().startsWith(mContext.getString(R.string.dr).toLowerCase() + mContext.getString(R.string.space) + mSearchString.toLowerCase())) {
                spannableStringSearch = new SpannableString(doctorObject.getDocName());

                spannableStringSearch.setSpan(new ForegroundColorSpan(
                                ContextCompat.getColor(mContext, R.color.tagColor)),
                        4, 4 + mSearchString.length(),//hightlight mSearchString
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.doctorName.setText(spannableStringSearch);
            } else {

                holder.doctorName.setText(doctorObject.getDocName());
            }
            //----------------------------------
            if (doctorObject.getNameOfClinic().startsWith(mSearchString)) {
                spannableClinicNameString = new SpannableString(doctorObject.getNameOfClinic());
                Pattern pattern = Pattern.compile(mSearchClinicNameString, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(doctorObject.getNameOfClinic());
                while (matcher.find()) {
                    spannableClinicNameString.setSpan(new ForegroundColorSpan(
                                    ContextCompat.getColor(mContext, R.color.tagColor)),
                           0, 0+mSearchString.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.clinicName.setText(spannableClinicNameString);
                }

            } else {
                holder.clinicName.setText(doctorObject.getNameOfClinic());
            }

        }
     /*   if ((mSearchClinicNameString != null) && (!mSearchClinicNameString.isEmpty())) {

           spannableClinicNameString = new SpannableString(doctorObject.getClinicName());
            Pattern pattern = Pattern.compile(mSearchClinicNameString, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(doctorObject.getClinicName());
            while (matcher.find()) {
                spannableClinicNameString.setSpan(new ForegroundColorSpan(
                                ContextCompat.getColor(mContext, R.color.tagColor)),
                        matcher.start(), matcher.end(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }*/


        if (doctorObject.getFavourite()) {
            holder.favoriteView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.result_heart_fav));
        } else {
            holder.favoriteView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.result_line_heart_fav));
        }

        // holder.clinicName.setVisibility(View.VISIBLE);


        holder.favoriteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putParcelable(mContext.getString(R.string.clicked_item_data), doctorObject);
                b.putString(mContext.getString(R.string.do_operation), mContext.getString(R.string.favorite));
                mOnClinicAndDoctorNameSearchRowItem.onClinicAndDoctorNameSearchRowItem(b);
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
        /*  @BindView(R.id.waitingTime)
          CustomTextView waitingTime;
          @BindView(R.id.tokenNo)
          CustomTextView tokenNo;*/
        @BindView(R.id.favoriteView)
        ImageView favoriteView;
        @BindView(R.id.imageURL)
        CircularImageView imageURL;
        @BindView(R.id.dataLayout)
        LinearLayout dataLayout;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public interface OnClinicAndDoctorNameSearchRowItem {
        void onClinicAndDoctorNameSearchRowItem(Bundle bundleData);
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                mSearchString = charString;
                mSearchClinicNameString = charString;
                if (charString.isEmpty()) {
                    mDataList = mArrayList;

                } else {

                    ArrayList<DoctorList> filteredList = new ArrayList<>();

                    for (DoctorList doctorConnectModel : mArrayList) {

                        if (doctorConnectModel.getDocName().toLowerCase().startsWith(mContext.getString(R.string.dr).toLowerCase() + mContext.getString(R.string.space) + charString.toLowerCase()) || doctorConnectModel.getNameOfClinic().toLowerCase().startsWith(charString.toLowerCase())) {
                            filteredList.add(doctorConnectModel);
                        }/*else{
                            for (String name :
                                    doctorConnectModel.getClinicName()) {
                                if (name.toLowerCase().startsWith(charString.toLowerCase())) {
                                    filteredList.add(doctorConnectModel);
                                    break;
                                }
                            }
                        }*/

                    }
                    mDataList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mDataList = (ArrayList<DoctorList>) filterResults.values;
                if (mDataList.size() == 0) {
                    RecentVisitDoctorFragment temp = (RecentVisitDoctorFragment) mFragment;
                    temp.isDataListViewVisible(true, true);
                } else {
                    RecentVisitDoctorFragment temp = (RecentVisitDoctorFragment) mFragment;
                    temp.isDataListViewVisible(true, false);
                }
                notifyDataSetChanged();
            }
        };
    }
}