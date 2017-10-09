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

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.book_appointment.RecentVisitDoctorFragment;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookAppointFilteredDocList extends RecyclerView.Adapter<BookAppointFilteredDocList.ListViewHolder> implements Filterable {


    private Fragment mFragment;
    private Context mContext;
    private ArrayList<DoctorList> mDataList;
    private int imageSize;
    private ArrayList<DoctorList> mArrayList;
    private OnFilterDocListClickListener mOnFilterDocListClickListener;
    private String searchString;
    private ColorGenerator mColorGenerator;


    public BookAppointFilteredDocList(Context mContext, ArrayList<DoctorList> dataList, OnFilterDocListClickListener mOnFilterDocListClickListener, Fragment m) {
        this.mDataList = dataList;
        this.mContext = mContext;
        this.mArrayList = dataList;
        this.mOnFilterDocListClickListener = mOnFilterDocListClickListener;
        this.mFragment = m;
        mColorGenerator = ColorGenerator.MATERIAL;
        setColumnNumber(mContext, 2);
    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = (widthPixels / columnNum) - CommonMethods.convertDpToPixel(30);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_appointment_filtered_doctor, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        final DoctorList doctorObject = mDataList.get(position);

        holder.doctorName.setText(doctorObject.getDocName());
        holder.doctorType.setText(doctorObject.getSpeciality());
        if (doctorObject.getFavourite()) {
            // holder.favoriteView.setImageResource();
        }
        holder.doctorExperience.setText("" + doctorObject.getExperience() + mContext.getString(R.string.space) + mContext.getString(R.string.years_experience));
        holder.doctorAddress.setText(doctorObject.getDoctorAddress());
        holder.doctorFee.setText("" + mContext.getString(R.string.rupee_symbol) + doctorObject.getAmount());
        SpannableString content = new SpannableString(doctorObject.getDistance());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        holder.distance.setText(content);
        holder.waitingTime.setText("" + mContext.getString(R.string.waiting_for) + mContext.getString(R.string.space) + doctorObject.getWaitingTime());
        holder.tokenNo.setText(mContext.getString(R.string.token_no_available));

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
            requestOptions.override(imageSize, imageSize);
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

                mOnFilterDocListClickListener.onClickOfDoctorRowItem(b);
            }
        });
        SpannableString spannableStringSearch = null;
        if ((searchString != null) && (!searchString.isEmpty())) {

            spannableStringSearch = new SpannableString(doctorObject.getDocName());

            spannableStringSearch.setSpan(new ForegroundColorSpan(
                            ContextCompat.getColor(mContext, R.color.tagColor)),
                    4, 4 + searchString.length(),//hightlight searchString
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }        if (spannableStringSearch != null) {
            holder.doctorName.setText(spannableStringSearch);
        } else {
            holder.doctorName.setText(doctorObject.getDocName());
        }

        if (doctorObject.getFavourite()) {
            holder.favoriteView.setVisibility(View.VISIBLE);
        } else {
            holder.favoriteView.setVisibility(View.INVISIBLE);
        }


        holder.favoriteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putParcelable(mContext.getString(R.string.clicked_item_data), doctorObject);
                b.putString(mContext.getString(R.string.do_operation), mContext.getString(R.string.favorite));
                mOnFilterDocListClickListener.onClickOfDoctorRowItem(b);
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
        @BindView(R.id.doctorType)
        CustomTextView doctorType;
        @BindView(R.id.doctorExperience)
        CustomTextView doctorExperience;
        @BindView(R.id.doctorAddress)
        CustomTextView doctorAddress;
        @BindView(R.id.doctorFee)
        CustomTextView doctorFee;
        @BindView(R.id.distance)
        CustomTextView distance;
        @BindView(R.id.waitingTime)
        CustomTextView waitingTime;
        @BindView(R.id.tokenNo)
        CustomTextView tokenNo;
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

    public interface OnFilterDocListClickListener {
        void onClickOfDoctorRowItem(Bundle bundleData);
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                searchString = charString;
                if (charString.isEmpty()) {

                    mDataList = mArrayList;
                } else {

                    ArrayList<DoctorList> filteredList = new ArrayList<>();

                    for (DoctorList doctorConnectModel : mArrayList) {

                        if (doctorConnectModel.getDocName().toLowerCase().startsWith(mContext.getString(R.string.dr).toLowerCase() + mContext.getString(R.string.space) + charString.toLowerCase())) {

                            filteredList.add(doctorConnectModel);
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
}