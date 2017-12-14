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
import com.rescribe.helpers.book_appointment.ServicesCardViewImpl;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.ClinicData;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.singleton.RescribeApplication;
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

    private final HelperResponse mHelperResponse;
    private Fragment mFragment;
    private Context mContext;
    private ArrayList<DoctorList> mDataList;
    private int mImageSize;
    private ArrayList<DoctorList> mArrayList;
    private ServicesCardViewImpl mOnClinicAndDoctorNameSearchRowItem;
    private String mSearchString;
    private ColorGenerator mColorGenerator;
    private String mSearchClinicNameString;
    private boolean isListByClinicName;
    private String cityname;


    public SortByClinicAndDoctorNameAdapter(Context mContext, ArrayList<DoctorList> dataList, ServicesCardViewImpl mOnClinicAndDoctorNameSearchRowItem, Fragment m, HelperResponse mHelperResponse) {
        this.mDataList = dataList;
        this.mContext = mContext;
        this.mArrayList = dataList;
        this.mOnClinicAndDoctorNameSearchRowItem = mOnClinicAndDoctorNameSearchRowItem;
        this.mFragment = m;
        this.mHelperResponse = mHelperResponse;
        String cityNameString = RescribeApplication.getUserSelectedLocationInfo().get(mContext.getString(R.string.location));
        if (cityNameString != null) {
            String[] split = cityNameString.split(",");
            cityname = split[1].trim();
        }
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
    public void onBindViewHolder(SortByClinicAndDoctorNameAdapter.ListViewHolder holder, final int position) {
        final DoctorList doctorObject = mDataList.get(position);
        if(doctorObject.getExperience()==0) {
            holder.doctorExperience.setVisibility(View.GONE);
        }else{
            holder.doctorExperience.setVisibility(View.VISIBLE);
            holder.doctorExperience.setText("" + doctorObject.getExperience() + mContext.getString(R.string.space) + mContext.getString(R.string.years_experience));

        }
        holder.doctorCategoryType.setText(doctorObject.getCategorySpeciality());
        holder.aboutDoctor.setText(doctorObject.getDegree());
        //------------
        if (doctorObject.getRating() == 0) {
            holder.doctorRating.setVisibility(View.GONE);
            holder.ratingBar.setVisibility(View.GONE);
        } else {
            holder.doctorRating.setVisibility(View.VISIBLE);
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.doctorRating.setText("" + doctorObject.getRating());
            holder.ratingBar.setRating((float) doctorObject.getRating());
        }
        //------------

        //if only one clinic is available then only show doctoraddress otherwise show total locations available
        if (doctorObject.getClinicDataList().size() == 1) {
            if(doctorObject.getClinicDataList().get(0).getAmount()==0){
                holder.doctorFee.setVisibility(View.INVISIBLE);
                holder.ruppessIcon.setVisibility(View.INVISIBLE);
            }else{
                holder.doctorFee.setVisibility(View.VISIBLE);
                holder.ruppessIcon.setVisibility(View.VISIBLE);
                holder.doctorFee.setText(""+doctorObject.getClinicDataList().get(0).getAmount());
            }
            holder.doctorAddress.setText(doctorObject.getClinicDataList().get(0).getClinicAddress());
            holder.doctorAddress.setTextColor(mContext.getResources().getColor(R.color.dose_completed));

        } else {
            holder.doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));
            SpannableString locationString = new SpannableString( doctorObject.getClinicDataList().size() + mContext.getString(R.string.space) + mContext.getString(R.string.locations)+mContext.getString(R.string.space)+"in"+mContext.getString(R.string.space)+cityname);
            locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
            holder.doctorAddress.setText(locationString);
            if(doctorObject.getClinicDataList().get(0).getAmount()==0){
                holder.doctorFee.setVisibility(View.INVISIBLE);
                holder.ruppessIcon.setVisibility(View.INVISIBLE);
            }else{
                holder.doctorFee.setVisibility(View.VISIBLE);
                holder.ruppessIcon.setVisibility(View.VISIBLE);
                holder.doctorFee.setText(""+doctorObject.getClinicDataList().get(0).getAmount());
            }

        }
        //------------

        // holder.doctorFee.setText("" + doctorObject.getAmount());
      /*  SpannableString content = new SpannableString(doctorObject.getDistance());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        holder.distance.setText(content);*/

        //-------Load image-------

        if (doctorObject.getDoctorImageUrl() !=null) {
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


            RequestOptions requestOptions = new RequestOptions();
            requestOptions.dontAnimate();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.skipMemoryCache(true);
            requestOptions.override(mImageSize, mImageSize);
            requestOptions.placeholder(drawable);
            requestOptions.error(drawable);

            Glide.with(mContext)
                    .load(doctorObject.getDoctorImageUrl())
                    .apply(requestOptions).thumbnail(0.5f)
                    .into(holder.imageURL);
            //--------------
        }


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

            if (!doctorObject.getNameOfClinicString().equals("")) {
                holder.clinicName.setVisibility(View.VISIBLE);
                if (doctorObject.getNameOfClinicString().toLowerCase().startsWith(mSearchClinicNameString.toLowerCase())) {
                    spannableClinicNameString = new SpannableString(doctorObject.getNameOfClinicString());
                    Pattern pattern = Pattern.compile(mSearchClinicNameString, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(doctorObject.getNameOfClinicString());
                    while (matcher.find()) {
                        spannableClinicNameString.setSpan(new ForegroundColorSpan(
                                        ContextCompat.getColor(mContext, R.color.tagColor)),
                                0, 0 + mSearchClinicNameString.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        holder.clinicName.setText(spannableClinicNameString);

                    }
                } else {
                    holder.clinicName.setVisibility(View.GONE);
                    /*holder.clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());*/
                   if (doctorObject.getClinicDataList().size() == 1) {
                        holder.clinicName.setVisibility(View.VISIBLE);
                        holder.clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());
                    } else {
                        holder.clinicName.setVisibility(View.GONE);
                        holder.clinicName.setText("");
                    }
                }
            }
        }

        //--------------
        if (doctorObject.getClinicDataList().size() > 0) {
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

        //---------------
        if (doctorObject.getFavourite()) {
            holder.favoriteView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.result_heart_fav));
        } else {
            holder.favoriteView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.result_line_heart_fav));
        }

        //----*********-------------
        holder.favoriteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView) v;
                boolean status = !doctorObject.getFavourite();
                mOnClinicAndDoctorNameSearchRowItem.onFavoriteIconClick(status, doctorObject, imageView, mHelperResponse);
            }
        });

        holder.dataLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(mContext.getString(R.string.clicked_item_data_type_value), mContext.getString(R.string.doctor));
                b.putParcelable(mContext.getString(R.string.clicked_item_data), doctorObject);
                mOnClinicAndDoctorNameSearchRowItem.onClickOfCardView(b);
            }
        });

        holder.bookAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(mContext.getString(R.string.clicked_item_data_type_value), mContext.getString(R.string.book_appointment));
                b.putParcelable(mContext.getString(R.string.clicked_item_data), doctorObject);
                b.putInt(mContext.getString(R.string.selected_clinic_data_position), 0);

                mOnClinicAndDoctorNameSearchRowItem.onClickedOfBookButton(b);
            }
        });

        holder.tokenNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(mContext.getString(R.string.clicked_item_data_type_value), mContext.getString(R.string.token_number));
                b.putInt(mContext.getString(R.string.selected_clinic_data_position), 0);
                b.putParcelable(mContext.getString(R.string.clicked_item_data), doctorObject);
                mOnClinicAndDoctorNameSearchRowItem.onClickedOfTokenNumber(b);
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
        @BindView(R.id.bookAppointmentButton)
        ImageView bookAppointmentButton;
        @BindView(R.id.ruppessIcon)
        ImageView ruppessIcon;
        @BindView(R.id.favoriteView)
        ImageView favoriteView;
        @BindView(R.id.imageURL)
        CircularImageView imageURL;
        @BindView(R.id.dataLayout)
        LinearLayout dataLayout;
        @BindView(R.id.doctorCategoryType)
        CustomTextView doctorCategoryType;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
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

                        if (doctorConnectModel.getDocName().toLowerCase().startsWith(mContext.getString(R.string.dr).toLowerCase()+mContext.getString(R.string.space)+charString.toLowerCase())) {
                            filteredList.add(doctorConnectModel);
                            setListByClinicName(false);
                        } else {
                            int i = 0;
                            for (ClinicData dataObj :
                                    doctorConnectModel.getClinicDataList()) {
                                if (dataObj.getClinicName().toLowerCase().startsWith(charString.toLowerCase())) {
                                    doctorConnectModel.setNameOfClinicString(dataObj.getClinicName());
                                    setListByClinicName(true);
                                    doctorConnectModel.setAddressOfDoctorString(dataObj.getClinicAddress());
                                    filteredList.add(doctorConnectModel);
                                    i++;
                                }
                            }
                        }

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

    public ArrayList<DoctorList> getSortedListByClinicNameOrDoctorName() {
        return mDataList;
    }

    public boolean isListByClinicName() {
        return isListByClinicName;
    }

    public void setListByClinicName(boolean listByClinicName) {
        isListByClinicName = listByClinicName;
    }

    public interface OnClinicAndDoctorNameSearchRowItem {
        void onClickOfDoctorRowItem(Bundle bundleData);
    }

}
