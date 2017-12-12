package com.rescribe.adapters;

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
import com.rescribe.model.doctor_connect.ChatDoctor;
import com.rescribe.ui.activities.ChatActivity;
import com.rescribe.ui.activities.DoctorConnectActivity;
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
 * Created by jeetal on 6/9/17.
 */

public class DoctorConnectAdapter extends RecyclerView.Adapter<DoctorConnectAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<ChatDoctor> chatDoctors;
    private ColorGenerator mColorGenerator;

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.doctorName)
        CustomTextView doctorName;
        @BindView(R.id.doctorType)
        TextView doctorType;
        @BindView(R.id.onlineStatusTextView)
        TextView onlineStatusTextView;

        @BindView(R.id.onlineStatusIcon)
        ImageView onlineStatusIcon;

        @BindView(R.id.paidStatusTextView)
        TextView paidStatusTextView;
        @BindView(R.id.imageOfDoctor)
        ImageView imageOfDoctor;
        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }


    public DoctorConnectAdapter(Context mContext, ArrayList<ChatDoctor> chatDoctors) {
        this.chatDoctors = chatDoctors;
        this.mContext = mContext;
        mColorGenerator = ColorGenerator.MATERIAL;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_connect_chats_row_item, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        final ChatDoctor chatDoctor = chatDoctors.get(position);
        holder.doctorType.setText(chatDoctor.getSpecialization());

        //-----------

        holder.onlineStatusIcon.setVisibility(View.GONE);

        if (chatDoctor.getOnlineStatus().equalsIgnoreCase(ONLINE)) {
            holder.onlineStatusIcon.setVisibility(View.VISIBLE);
            holder.onlineStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.green_light));
        } else if (chatDoctor.getOnlineStatus().equalsIgnoreCase(IDLE)) {
            holder.onlineStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.range_yellow));
        } else if (chatDoctor.getOnlineStatus().equalsIgnoreCase(OFFLINE)) {
            holder.onlineStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.grey_500));
        }

        //-----------

        holder.onlineStatusTextView.setText(chatDoctor.getOnlineStatus());
        holder.paidStatusTextView.setText(chatDoctor.getPaidStatus() == DoctorConnectActivity.PAID ? "Rs 255/-" : "FREE");

        String doctorName = chatDoctor.getDoctorName();
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
        holder.imageOfDoctor.setImageDrawable(drawable);

        if (chatDoctor.getImageUrl() != null) {
            if (!chatDoctor.getImageUrl().equals("")) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.dontAnimate();
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                requestOptions.skipMemoryCache(true);
                requestOptions.override(CommonMethods.convertDpToPixel(40), CommonMethods.convertDpToPixel(40));
                requestOptions.placeholder(drawable);
                requestOptions.error(drawable);

                Glide.with(mContext)
                        .load(chatDoctor.getImageUrl())
                        .apply(requestOptions).thumbnail(0.5f)
                        .into(holder.imageOfDoctor);

            }
        }

        holder.doctorName.setText(chatDoctor.getDoctorName());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatDoctor.getPaidStatus() != PAID) {
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra(RescribeConstants.DOCTORS_INFO, chatDoctor);
                    ((DoctorConnectActivity) mContext).startActivityForResult(intent, 1111);
                } else
                    CommonMethods.showInfoDialog(mContext.getResources().getString(R.string.paid_doctor_message), mContext, false);

            }
        });

    }

    @Override
    public int getItemCount() {
        return chatDoctors.size();
    }

}

