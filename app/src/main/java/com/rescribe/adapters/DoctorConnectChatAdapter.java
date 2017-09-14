package com.rescribe.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.rescribe.R;
import com.rescribe.model.doctor_connect_chat.ChatList;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by jeetal on 5/9/17.
 */
public class DoctorConnectChatAdapter extends RecyclerView.Adapter<DoctorConnectChatAdapter.ListViewHolder> {

    private ColorGenerator mColorGenerator;
    private Context mContext;
    private ArrayList<ChatList> chatLists;
    private String mIdle, mOnline, mOffline;


    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.doctorName)
        CustomTextView doctorName;
        @BindView(R.id.doctorType)
        TextView doctorType;
        @BindView(R.id.onlineStatusTextView)
        TextView onlineStatusTextView;
        @BindView(R.id.imageOfDoctor)
        ImageView imageOfDoctor;
        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }


    public DoctorConnectChatAdapter(Context mContext, ArrayList<ChatList> chatList) {
        this.chatLists = chatList;
        this.mContext = mContext;
        mColorGenerator = ColorGenerator.MATERIAL;
        mOnline = mContext.getString(R.string.online);
        mOffline = mContext.getString(R.string.offline);
        mIdle = mContext.getString(R.string.idle);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_connect_chats_row_item, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        ChatList doctorConnectChatModel = chatLists.get(position);

        holder.doctorName.setText(doctorConnectChatModel.getDoctorName());
        holder.doctorType.setText(doctorConnectChatModel.getSpecialization());
        holder.onlineStatusTextView.setText(doctorConnectChatModel.getOnlineStatus());
        //-----------
        if (doctorConnectChatModel.getOnlineStatus().equalsIgnoreCase(mOnline)) {
            holder.onlineStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.green_light));
        } else if (doctorConnectChatModel.getOnlineStatus().equalsIgnoreCase(mIdle)) {
            holder.onlineStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.range_yellow));
        } else if (doctorConnectChatModel.getOnlineStatus().equalsIgnoreCase(mOffline)) {
            holder.onlineStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.grey_500));
        } else {
            holder.onlineStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.tagColor));
        }
        //-----------

        String doctorName = doctorConnectChatModel.getDoctorName();
        doctorName = doctorName.replace("Dr. ", "");

        if (doctorName != null) {
            int color2 = mColorGenerator.getColor(doctorName);
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .width(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // width in px
                    .height(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // height in px
                    .endConfig()
                    .buildRound(("" + doctorName.charAt(0)).toUpperCase(), color2);
            holder.imageOfDoctor.setImageDrawable(drawable);
        }

    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }


}