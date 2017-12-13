package com.rescribe.adapters.unread_notification_message_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rescribe.R;
import com.rescribe.model.dashboard_api.unread_notification_message_list.UnreadSavedNotificationMessageData;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnreadChatNotificationList extends RecyclerView.Adapter<UnreadChatNotificationList.FileViewHolder> {

    private final ArrayList<UnreadSavedNotificationMessageData> mOriginalReceivedList;
    private final Context mContext;
    private final OnDocConnectItemClicked listener;
    private ArrayList<UnreadSavedNotificationMessageData> mListToBeUsed;

    public UnreadChatNotificationList(Context context, ArrayList<UnreadSavedNotificationMessageData> list, OnDocConnectItemClicked listener) {
        this.mContext = context;
        this.mOriginalReceivedList = list;

        mListToBeUsed = new ArrayList<>();
        addSingleElementToList();
        this.listener = listener;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_doctor_connect_notification_layout, parent, false);
        return new UnreadChatNotificationList.FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UnreadChatNotificationList.FileViewHolder holder, final int position) {
        final UnreadSavedNotificationMessageData unreadNotificationMessageData = mListToBeUsed.get(position);


        //---- To show icon for first element based on notification type----
        if (position == 0) {
            holder.imageIcon.setImageResource(R.drawable.doctor_connect);
            holder.imageIcon.setVisibility(View.VISIBLE);
        } else {
            holder.imageIcon.setVisibility(View.INVISIBLE);
        }
        //--------

        //--- TO show more load button or not on appointment_type_notification.
        if (position == 0 && (mListToBeUsed.size() == 1 && mOriginalReceivedList.size() > 1)) {
            holder.docConnectLoadMoreMessage.setVisibility(View.VISIBLE);

            holder.docConnectTimeStamp.setVisibility(View.GONE);

        } else {
            holder.docConnectLoadMoreMessage.setVisibility(View.INVISIBLE);
            String formattedDate = CommonMethods.getFormattedDate(unreadNotificationMessageData.getNotificationTimeStamp(), RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE_PATTERN.DD_MM_YYYY);
            String time = CommonMethods.formatDateTime(unreadNotificationMessageData.getNotificationTimeStamp(), RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.TIME);

            String dayFromDate = CommonMethods.getDayFromDate(RescribeConstants.DATE_PATTERN.DD_MM_YYYY, formattedDate);

            if (mContext.getString(R.string.today).equalsIgnoreCase(dayFromDate)) {
                holder.docConnectTimeStamp.setText(time);
            } else {
                holder.docConnectTimeStamp.setText(dayFromDate + " " + time);
            }
            holder.docConnectTimeStamp.setVisibility(View.VISIBLE);
        }

        //----------
        holder.docConnectSenderName.setText(unreadNotificationMessageData.getNotificationMessage());
        holder.docConnectReceivedMessage.setText(unreadNotificationMessageData.getNotificationData());

        holder.rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDocConnectRowClicked(unreadNotificationMessageData);
            }
        });

        holder.docConnectLoadMoreMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDocConnectMoreClicked(unreadNotificationMessageData);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListToBeUsed.size();
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.docConnectSenderName)
        CustomTextView docConnectSenderName;
        @BindView(R.id.docConnectReceivedMessage)
        CustomTextView docConnectReceivedMessage;
        @BindView(R.id.docConnectTimeStamp)
        CustomTextView docConnectTimeStamp;
        @BindView(R.id.docConnectLoadMoreMessage)
        CustomTextView docConnectLoadMoreMessage;
        @BindView(R.id.imageIcon)
        ImageView imageIcon;

        View rowView;

        FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rowView = itemView;
        }
    }

    public interface OnDocConnectItemClicked {
        public void onDocConnectMoreClicked(UnreadSavedNotificationMessageData unreadNotificationMessageData);

        public void onDocConnectRowClicked(UnreadSavedNotificationMessageData unreadNotificationMessageData);
    }

    public void addAllElementToList() {
        mListToBeUsed.clear();
        mListToBeUsed.addAll(mOriginalReceivedList);
    }

    public void addSingleElementToList() {
        mListToBeUsed.clear();
        mListToBeUsed.add(mOriginalReceivedList.get(0));
    }

}
