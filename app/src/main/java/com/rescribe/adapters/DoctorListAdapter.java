package com.rescribe.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.rescribe.model.case_details.Range;
import com.rescribe.model.doctors.doctor_info.DoctorDetail;
import com.rescribe.ui.activities.SingleVisitDetailsActivity;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 11/7/17.
 */

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.ListViewHolder> {

    private final String TAG = getClass().getName();
    Context mContext;
    ArrayList<DoctorDetail> mDataList;
    private SimpleDateFormat mDateFormat;
    private int imageSize;
    private ColorGenerator mColorGenerator;


    public DoctorListAdapter(Context context, ArrayList<DoctorDetail> dataList) {
        this.mContext = context;
        mDataList = dataList;
        mDateFormat = new SimpleDateFormat(RescribeConstants.DATE_PATTERN.DD_MM_YYYY, Locale.US);
        setColumnNumber(context, 2);
        mColorGenerator = ColorGenerator.MATERIAL;

    }

    @Override
    public DoctorListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_doctor_list_layout, parent, false);
        return new DoctorListAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DoctorListAdapter.ListViewHolder holder, final int position) {

        final DoctorDetail dataObject = mDataList.get(position);

        if (dataObject.isStartElement()) {

            Date date = CommonMethods.convertStringToDate(dataObject.getDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            //----
            int day = cal.get(Calendar.DAY_OF_MONTH);

            String toDisplay = day + "<sup>" + CommonMethods.getSuffixForNumber(day) + "</sup>";
            //------
            if (dataObject.getDate().equalsIgnoreCase(mDateFormat.format(new Date()))) {
                toDisplay = toDisplay + "\n" + mContext.getString(R.string.just_now);
            }
            //------
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                holder.date.setText(Html.fromHtml(toDisplay, Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.date.setText(Html.fromHtml(toDisplay));
            }

            holder.date.setVisibility(View.VISIBLE);

            holder.circularBulletMainElement.setVisibility(View.VISIBLE);
            holder.circularBulletChildElement.setVisibility(View.GONE);

        } else {
            holder.date.setVisibility(View.INVISIBLE);
            holder.date.setVisibility(View.INVISIBLE);
            holder.circularBulletChildElement.setVisibility(View.VISIBLE);
            holder.circularBulletMainElement.setVisibility(View.GONE);

        }

        // Glide.with(mContext).load(dataObject.getDocImg()).into(holder.docProfileImage);
        holder.doctorName.setText(dataObject.getDoctorName());
        holder.doctorAddress.setText(dataObject.getAddress());
        holder.doctorType.setText(dataObject.getSpecialization());

        holder.parentDataContainer.setBackgroundColor(dataObject.getRowColor());
        holder.sideBarView.setBackgroundColor(dataObject.getSideBarViewColor());

        if (position == 0)
            holder.upperLine.setVisibility(View.INVISIBLE);
        else {
            holder.upperLine.setVisibility(View.VISIBLE);
        }

        //---
        if (position == mDataList.size() - 1)
            holder.lowerLine.setVisibility(View.INVISIBLE);
        else {
            holder.lowerLine.setVisibility(View.VISIBLE);
        }
        holder.mClickOnDoctorVisitLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeToShow = CommonMethods.formatDateTime(dataObject.getDate(), RescribeConstants.DATE_PATTERN.MMM_YYYY,
                        RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE).toLowerCase();
                Date date = CommonMethods.convertStringToDate(dataObject.getDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                timeToShow = timeToShow.substring(0, 1).toUpperCase() + timeToShow.substring(1);
                String toDisplay = cal.get(Calendar.DAY_OF_MONTH) + "<sup>" + CommonMethods.getSuffixForNumber(cal.get(Calendar.DAY_OF_MONTH)) + "</sup> " + timeToShow;
                Intent intent = new Intent(mContext, SingleVisitDetailsActivity.class);
                intent.putExtra(mContext.getString(R.string.name), dataObject.getDoctorName());
                intent.putExtra(mContext.getString(R.string.specialization), dataObject.getSpecialization());
                intent.putExtra(mContext.getString(R.string.address), dataObject.getAddress());
                intent.putExtra(mContext.getString(R.string.doctor_image),dataObject.getDocImg());

                intent.putExtra(mContext.getString(R.string.one_day_visit_date), toDisplay);
                intent.putExtra(mContext.getString(R.string.opd_id), dataObject.getOpdId());
                intent.putExtra(mContext.getString(R.string.doctor_image),dataObject.getDocImg());
                mContext.startActivity(intent);
            }
        });


        int color2 = mColorGenerator.getColor(dataObject.getDoctorName());
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // width in px
                .height(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // height in px
                .endConfig()
                .buildRound(("" + dataObject.getDoctorName().charAt(0)).toUpperCase(), color2);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        requestOptions.override(imageSize, imageSize);
        requestOptions.error(drawable);
        requestOptions.placeholder(drawable);

        Glide.with(mContext)
                .load(dataObject.getDocImg())
                .apply(requestOptions).thumbnail(0.5f)
                .into(holder.docProfileImage);



    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        CustomTextView date;

        @BindView(R.id.clickOnDoctorVisitLinearLayout)
        LinearLayout mClickOnDoctorVisitLinearLayout;

        @BindView(R.id.circularBulletChildElement)
        ImageView circularBulletChildElement;
        @BindView(R.id.docProfileImage)
        CircularImageView docProfileImage;
        @BindView(R.id.circularBulletMainElement)
        ImageView circularBulletMainElement;

        @BindView(R.id.upperLine)
        TextView upperLine;
        @BindView(R.id.lowerLine)
        TextView lowerLine;

        @BindView(R.id.doctorName)
        CustomTextView doctorName;
        @BindView(R.id.doctorType)
        TextView doctorType;
        @BindView(R.id.doctorAddress)
        TextView doctorAddress;

        @BindView(R.id.parentDataContainer)
        LinearLayout parentDataContainer;
        @BindView(R.id.sideBarView)
        TextView sideBarView;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = (widthPixels / columnNum) - CommonMethods.convertDpToPixel(30);
    }

}

