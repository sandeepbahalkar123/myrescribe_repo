package com.rescribe.adapters.book_appointment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 26/10/17.
 */

public class SortByClinicAndDoctorNameAdapter extends RecyclerView.Adapter<SortByClinicAndDoctorNameAdapter.ListViewHolder> implements Filterable {

    private final HelperResponse mHelperResponse;
    private SortByClinicAndDoctorNameAdapter.OnDataListViewVisible mOnDataListViewVisibleListener;
    private Context mContext;
    private ArrayList<DoctorList> mDataList;
    private ArrayList<DoctorList> mArrayList;
    private ServicesCardViewImpl mOnClinicAndDoctorNameSearchRowItem;
    private String mSearchString;
    private ColorGenerator mColorGenerator;
    private boolean isListByClinicName;
    private ImageView mClickedItemFavImageView;
    private String cityname;
    private RecyclerView recyclerView;


    public SortByClinicAndDoctorNameAdapter(Context mContext, ArrayList<DoctorList> dataList, ServicesCardViewImpl mOnClinicAndDoctorNameSearchRowItem, SortByClinicAndDoctorNameAdapter.OnDataListViewVisible m, HelperResponse mHelperResponse, RecyclerView recyclerView) {
        this.mDataList = dataList;
        this.mContext = mContext;
        this.mArrayList = dataList;
        this.mOnClinicAndDoctorNameSearchRowItem = mOnClinicAndDoctorNameSearchRowItem;
        this.mOnDataListViewVisibleListener = m;
        this.mHelperResponse = mHelperResponse;
        this.recyclerView = recyclerView;

        String cityNameString = RescribeApplication.getUserSelectedLocationInfo().get(mContext.getString(R.string.location));
        if (cityNameString != null) {
            String[] split = cityNameString.split(",");
            cityname = split[1].trim();
        }
        mColorGenerator = ColorGenerator.MATERIAL;
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
        String doctorName = doctorObject.getDocName();
        if (doctorName.contains("Dr. ")) {
            doctorName = doctorObject.getDocName();
        } else {
            doctorName = "Dr. " + doctorObject.getDocName();
        }
        holder.clinicName.setVisibility(View.VISIBLE);
        if (doctorObject.getExperience() == 0) {
            holder.doctorExperience.setVisibility(View.GONE);
        } else {
            holder.doctorExperience.setVisibility(View.VISIBLE);
            holder.doctorExperience.setText("" + doctorObject.getExperience() + " " + mContext.getString(R.string.years_experience));

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
            if (doctorObject.getClinicDataList().get(0).getAmount() == 0) {
                holder.doctorFee.setVisibility(View.INVISIBLE);
                holder.ruppessIcon.setVisibility(View.INVISIBLE);
            } else {
                holder.doctorFee.setVisibility(View.VISIBLE);
                holder.ruppessIcon.setVisibility(View.VISIBLE);
                holder.doctorFee.setText("" + doctorObject.getClinicDataList().get(0).getAmount());
            }
            holder.doctorAddress.setText(doctorObject.getClinicDataList().get(0).getClinicAddress());
            holder.doctorAddress.setTextColor(mContext.getResources().getColor(R.color.dose_completed));

        } else {
            holder.doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));
            boolean b = checkAllClinicAddressInSameCity(doctorObject.getClinicDataList());
            if (b) {
                SpannableString locationString = new SpannableString(doctorObject.getClinicDataList().size() + " " + mContext.getString(R.string.locations) + " " + "in" + " " + cityname);
                locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                holder.doctorAddress.setText(locationString);
                holder.doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));
            } else {
                SpannableString locationString = new SpannableString(doctorObject.getClinicDataList().size() + " " + mContext.getString(R.string.locations));
                locationString.setSpan(new UnderlineSpan(), 0, locationString.length(), 0);
                holder.doctorAddress.setText(locationString);
                holder.doctorAddress.setTextColor(mContext.getResources().getColor(R.color.black));

            }
            if (doctorObject.getClinicDataList().get(0).getAmount() == 0) {
                holder.doctorFee.setVisibility(View.INVISIBLE);
                holder.ruppessIcon.setVisibility(View.INVISIBLE);
            } else {
                holder.doctorFee.setVisibility(View.VISIBLE);
                holder.ruppessIcon.setVisibility(View.VISIBLE);
                holder.doctorFee.setText("" + doctorObject.getClinicDataList().get(0).getAmount());
            }

        }
        //------------


        TextDrawable textDrawable = CommonMethods.getTextDrawable(mContext, doctorName);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        requestOptions.placeholder(textDrawable);
        requestOptions.error(textDrawable);

        Glide.with(mContext)
                .load(doctorObject.getDoctorImageUrl())
                .apply(requestOptions).thumbnail(0.5f)
                .into(holder.imageURL);
        //---------------

        SpannableString spannableStringSearch = null;
        SpannableString spannableClinicNameString = null;
        if ((mSearchString != null) && (!mSearchString.isEmpty())) {
            if (doctorObject.getNameOfClinicString().equals("")) {

                //--Removed Dr. from spannable string -
                String replacedDrNameString = doctorName;
                if (replacedDrNameString.contains("Dr. ")) {
                    replacedDrNameString = replacedDrNameString.replace("Dr. ", "");
                }
                //---

                holder.clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());

                if (replacedDrNameString.toLowerCase().contains(/*mContext.getString(R.string.dr).toLowerCase() + " " +*/ mSearchString.toLowerCase())) {
                    spannableStringSearch = new SpannableString(replacedDrNameString);
                    Pattern pattern = Pattern.compile(mSearchString, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(replacedDrNameString);
                    while (matcher.find()) {

                        spannableStringSearch.setSpan(new ForegroundColorSpan(
                                        ContextCompat.getColor(mContext, R.color.tagColor)),
                                matcher.start(), matcher.end(),//hightlight mSearchString
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    //-------
                    holder.doctorName.setText(TextUtils.concat("Dr. ", spannableStringSearch));
                    //-------
                } else {
                    holder.doctorName.setText(doctorName);
                }
                //----------------------------------

            } else {
                holder.doctorName.setText(doctorName);
                if (doctorObject.getNameOfClinicString().toLowerCase().contains(mSearchString.toLowerCase())) {
                    spannableClinicNameString = new SpannableString(doctorObject.getNameOfClinicString());
                    Pattern pattern = Pattern.compile(mSearchString, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(doctorObject.getNameOfClinicString());
                    while (matcher.find()) {
                        spannableClinicNameString.setSpan(new ForegroundColorSpan(
                                        ContextCompat.getColor(mContext, R.color.tagColor)),
                                matcher.start(), matcher.end(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        holder.clinicName.setText(spannableClinicNameString);

                    }
                } else {
                    holder.clinicName.setText(doctorObject.getNameOfClinicString());
                  /*  holder.clinicName.setVisibility(View.GONE);
                    *//*holder.clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());*//*
                    if (doctorObject.getClinicDataList().size() == 1) {
                        holder.clinicName.setVisibility(View.VISIBLE);
                        holder.clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());
                    } else {
                        holder.clinicName.setVisibility(View.GONE);
                        holder.clinicName.setText("");
                    }*/
                }
            }
        } else {
            //--THIS IS DONE IF, ADAPTER OPEND FROM FIND_LOCATION(RECENT_VISITOR.java location selection)
            holder.doctorName.setText(doctorName);
            holder.clinicName.setText(doctorObject.getClinicDataList().get(0).getClinicName());
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
                mClickedItemFavImageView = imageView;

                mOnClinicAndDoctorNameSearchRowItem.onFavoriteIconClick(status, doctorObject, imageView, mHelperResponse);
            }
        });

        holder.dataLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(mContext.getString(R.string.clicked_item_data_type_value), mContext.getString(R.string.doctor));
                b.putParcelable(mContext.getString(R.string.clicked_item_data), doctorObject);
                b.putString(mContext.getString(R.string.category_name), doctorObject.getCategoryName());
                b.putString(RescribeConstants.TYPE_OF_DOCTOR_SEARCH, RescribeConstants.SEARCH_DOCTORS);
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
                if (charString.isEmpty()) {
                    mDataList = mArrayList;
                } else {
                    ArrayList<DoctorList> filteredList = new ArrayList<>();

                    for (DoctorList doctorConnectModel : mArrayList) {
                        String docName = doctorConnectModel.getDocName();
                        //---
                        if (docName.contains("Dr. ")) {
                            docName = docName.replace("Dr. ", "");
                        }
                        //---
                        if (docName.toLowerCase().contains(/*mContext.getString(R.string.dr).toLowerCase() + " " + */ charString.toLowerCase())) {
                            filteredList.add(doctorConnectModel);
                        } else {
                            for (ClinicData dataObj :
                                    doctorConnectModel.getClinicDataList()) {
                                if (dataObj.getClinicName().toLowerCase().contains(charString.toLowerCase())) {
                                    doctorConnectModel.setNameOfClinicString(dataObj.getClinicName());
                                    doctorConnectModel.setAddressOfDoctorString(dataObj.getClinicAddress());
                                    filteredList.add(doctorConnectModel);
                                }
                            }
                        }

                    }
                    //--removed duplicate doctors based on docID--
                    if (!filteredList.isEmpty()) {

                        Set set = new TreeSet(new Comparator() {
                            @Override
                            public int compare(Object o1, Object o2) {
                                if(((DoctorList)o1).getDocId() == ((DoctorList)o2).getDocId()){
                                    return 0;
                                }
                                return 1;
                            }
                        });
                        set.addAll(filteredList);
                        mDataList.clear();
                        mDataList.addAll(set);

                    } else {
                        mDataList = filteredList;
                    }
                    //----
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mDataList = (ArrayList<DoctorList>) filterResults.values;
                if (mDataList.size() == 0) {
                    mOnDataListViewVisibleListener.doConfigureDataListViewVisibility(true, true);
                } else {
                    mOnDataListViewVisibleListener.doConfigureDataListViewVisibility(true, false);
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

    public void updateClickedItemFavImage() {
        DoctorList userSelectedDoctorListDataObject = ServicesCardViewImpl.getUserSelectedDoctorListDataObject();
        if (userSelectedDoctorListDataObject.getFavourite()) {
            mClickedItemFavImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.favourite_icon));
        } else {
            mClickedItemFavImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.favourite_line_icon));
        }
    }

    public interface OnDataListViewVisible {
        public void doConfigureDataListViewVisibility(boolean flag, boolean isShowEmptyListView);
    }

    private boolean checkAllClinicAddressInSameCity(ArrayList<ClinicData> list) {

        if (list.size() > 1) {
            int count = 0;
            String[] clinicAddress = list.get(0).getClinicAddress().split(",");
            for (ClinicData innerDataObject :
                    list) {
                String innerClinicAddress = innerDataObject.getClinicAddress();
                if (innerClinicAddress.endsWith(clinicAddress[clinicAddress.length - 1])) {
                    count = count + 1;
                }
            }
            if (count == list.size()) {
                return true;
            }
        }
        return false;
    }
}

