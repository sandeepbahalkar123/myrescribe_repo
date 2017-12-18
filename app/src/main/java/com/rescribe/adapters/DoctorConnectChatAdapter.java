package com.rescribe.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.model.doctor_connect.ChatDoctor;
import com.rescribe.ui.activities.ChatActivity;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rescribe.ui.activities.DoctorConnectActivity.PAID;
import static com.rescribe.util.RescribeConstants.USER_STATUS.IDLE;
import static com.rescribe.util.RescribeConstants.USER_STATUS.OFFLINE;
import static com.rescribe.util.RescribeConstants.USER_STATUS.ONLINE;


/**
 * Created by jeetal on 5/9/17.
 */
public class DoctorConnectChatAdapter extends RecyclerView.Adapter<DoctorConnectChatAdapter.ListViewHolder> {

    private final AppDBHelper appDBHelper;
    private ColorGenerator mColorGenerator;
    private Context mContext;
    private ArrayList<ChatDoctor> chatLists;


    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.doctorName)
        CustomTextView doctorName;
        @BindView(R.id.doctorType)
        TextView doctorType;
        @BindView(R.id.onlineStatusTextView)
        TextView onlineStatusTextView;

        @BindView(R.id.onlineStatusIcon)
        ImageView onlineStatusIcon;

        @BindView(R.id.imageOfDoctor)
        CircularImageView imageOfDoctor;
        @BindView(R.id.paidStatusTextView)
        TextView paidStatusTextView;
        @BindView(R.id.badgeView)
        TextView badgeView;
        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }


    public DoctorConnectChatAdapter(Context mContext, ArrayList<ChatDoctor> chatList) {
        this.chatLists = chatList;
        this.mContext = mContext;
        mColorGenerator = ColorGenerator.MATERIAL;
        appDBHelper = new AppDBHelper(mContext);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_connect_chats_row_item, parent, false);

        return new ListViewHolder(itemView);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        final ChatDoctor doctorConnectChatModel = chatLists.get(position);

        holder.doctorName.setText(doctorConnectChatModel.getDoctorName());
        holder.doctorType.setText(doctorConnectChatModel.getSpecialization());
        holder.onlineStatusTextView.setText(doctorConnectChatModel.getOnlineStatus());

        //-----------

        holder.onlineStatusIcon.setVisibility(View.GONE);

        if (doctorConnectChatModel.getOnlineStatus().equalsIgnoreCase(ONLINE)) {
            holder.onlineStatusIcon.setVisibility(View.VISIBLE);
            holder.onlineStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.green_light));
        } else if (doctorConnectChatModel.getOnlineStatus().equalsIgnoreCase(IDLE)) {
            holder.onlineStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.range_yellow));
        } else if (doctorConnectChatModel.getOnlineStatus().equalsIgnoreCase(OFFLINE)) {
            holder.onlineStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.grey_500));
        }
        // Removed Dr. from doctor name to get starting letter of doctorName to set to image icon.

        String doctorName = doctorConnectChatModel.getDoctorName();
        if (doctorName.contains("Dr. ")) {
            doctorName = doctorName.replace("Dr. ", "");
        }
        //---------------
        TextDrawable textDrawable = CommonMethods.getTextDrawable(mContext, doctorName);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        requestOptions.override(CommonMethods.convertDpToPixel(40), CommonMethods.convertDpToPixel(40));
        requestOptions.placeholder(textDrawable);
        requestOptions.error(textDrawable);

        Glide.with(mContext)
                .load(doctorConnectChatModel.getImageUrl())
                .apply(requestOptions).thumbnail(0.5f)
                .into(holder.imageOfDoctor);
        //---------------

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doctorConnectChatModel.getPaidStatus() != PAID) {
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra(RescribeConstants.DOCTORS_INFO, doctorConnectChatModel);
                    mContext.startActivity(intent);
                } else
                    CommonMethods.showInfoDialog(mContext.getResources().getString(R.string.paid_doctor_message), mContext, false);
            }
        });

        String time = CommonMethods.formatDateTime(doctorConnectChatModel.getLastChatTime(), RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.TIME);
        holder.paidStatusTextView.setText(time);

        int count = appDBHelper.unreadMessageCountById(doctorConnectChatModel.getId());
        doctorConnectChatModel.setUnreadMessages(count);

        if (doctorConnectChatModel.getUnreadMessages() > 0) {
            holder.badgeView.setVisibility(View.VISIBLE);
            holder.badgeView.setText(String.valueOf(doctorConnectChatModel.getUnreadMessages()));
        } else {
            holder.badgeView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }


}