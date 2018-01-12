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
import com.rescribe.ui.activities.dashboard.UnreadNotificationMessageActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnreadAppointmentNotificationAlert extends RecyclerView.Adapter<UnreadAppointmentNotificationAlert.FileViewHolder> {

    private final ArrayList<UnreadSavedNotificationMessageData> mOriginalReceivedList;
    private final Context mContext;
    private final OnNotificationItemClicked listener;
    private final String userName;
    private UnreadNotificationMessageActivity parentActivity;
    private ArrayList<UnreadSavedNotificationMessageData> mListToBeUsed;

    public UnreadAppointmentNotificationAlert(Context context, ArrayList<UnreadSavedNotificationMessageData> list, OnNotificationItemClicked listener) {
        this.mContext = context;
        this.parentActivity = (UnreadNotificationMessageActivity) context;
        this.mOriginalReceivedList = list;

        mListToBeUsed = new ArrayList<>();
        addSingleElementToList();
        this.listener = listener;

        userName = "Dear " + CommonMethods.toCamelCase(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.USER_NAME, mContext)) + ", ";
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_appointment_notification_layout, parent, false);
        return new UnreadAppointmentNotificationAlert.FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UnreadAppointmentNotificationAlert.FileViewHolder holder, final int position) {
        final UnreadSavedNotificationMessageData unreadNotificationMessageData = mListToBeUsed.get(position);


        //---- To show icon for first element based on notification type----
        if (position == 0) {
            if (unreadNotificationMessageData.getNotificationMessageType().equalsIgnoreCase(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT)) {
                holder.imageIcon.setImageResource(R.drawable.my_appointments_icon);
            } else if (unreadNotificationMessageData.getNotificationMessageType().equalsIgnoreCase(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT)) {
                holder.imageIcon.setImageResource(R.drawable.investigation_icon);
            }
            holder.imageIcon.setVisibility(View.VISIBLE);
        } else {
            holder.imageIcon.setVisibility(View.INVISIBLE);
        }
        //--------

        int showFirstMessageTimeStamp = parentActivity.isShowFirstMessageTimeStamp(unreadNotificationMessageData.getNotificationMessageType());

        //--- TO show more load button or not on appointment_type_notification.
        if (unreadNotificationMessageData.getNotificationMessageType().equalsIgnoreCase(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT)) {
            if (position == 0 && (mListToBeUsed.size() == 1 && mOriginalReceivedList.size() > 1)) {
                holder.loadMoreItems.setVisibility(View.VISIBLE);
                holder.textMessageTimeStamp.setVisibility(View.INVISIBLE);
            } else {
                if (showFirstMessageTimeStamp == View.VISIBLE) {
                    holder.textMessageTimeStamp.setVisibility(View.GONE);
                } else {
                    holder.textMessageTimeStamp.setVisibility(View.VISIBLE);
                }
                holder.loadMoreItems.setVisibility(View.GONE);
            }
        } else if (unreadNotificationMessageData.getNotificationMessageType().equalsIgnoreCase(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT)) {
            if (position == 0 && (mListToBeUsed.size() == 1 && mOriginalReceivedList.size() > 1)) {
                holder.loadMoreItems.setVisibility(View.VISIBLE);
                holder.skipItems.setVisibility(View.VISIBLE);
                holder.textMessageTimeStamp.setVisibility(View.INVISIBLE);

            } else {
                if (showFirstMessageTimeStamp == View.VISIBLE) {
                    holder.textMessageTimeStamp.setVisibility(View.GONE);
                } else {
                    holder.textMessageTimeStamp.setVisibility(View.VISIBLE);
                }
                holder.loadMoreItems.setVisibility(View.GONE);
                holder.skipItems.setVisibility(View.VISIBLE);
            }
        }
        //----------

        if (unreadNotificationMessageData.getNotificationMessageType().equalsIgnoreCase(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT)) {
            holder.text.setText(userName + unreadNotificationMessageData.getNotificationMessage());
        } else if (unreadNotificationMessageData.getNotificationMessageType().equalsIgnoreCase(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT)) {
            holder.text.setText(unreadNotificationMessageData.getNotificationMessage());
        }
        //------------

        if (holder.textMessageTimeStamp.getVisibility() == View.VISIBLE) {

            String formattedDate = CommonMethods.getFormattedDate(unreadNotificationMessageData.getNotificationTimeStamp(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.DD_MM_YYYY);
            String time = CommonMethods.formatDateTime(unreadNotificationMessageData.getNotificationTimeStamp(), RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.DATE_PATTERN.DD_MM_YYYY + " " + RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.TIME);

            String dayFromDate = CommonMethods.getDayFromDate(RescribeConstants.DATE_PATTERN.DD_MM_YYYY, formattedDate);

            if (mContext.getString(R.string.today).equalsIgnoreCase(dayFromDate)) {
                holder.textMessageTimeStamp.setText(time);
            } else {
                holder.textMessageTimeStamp.setText(dayFromDate + " " + time);
            }
        }

        holder.rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNotificationRowClicked(unreadNotificationMessageData);
            }
        });

        holder.loadMoreItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMoreClicked(unreadNotificationMessageData);
            }
        });
        holder.skipItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSkipClicked(unreadNotificationMessageData);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListToBeUsed.size();
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textMessage)
        CustomTextView text;
        @BindView(R.id.textMessageTimeStamp)
        CustomTextView textMessageTimeStamp;
        @BindView(R.id.loadMoreItems)
        CustomTextView loadMoreItems;
        @BindView(R.id.skipItems)
        CustomTextView skipItems;
        @BindView(R.id.imageIcon)
        ImageView imageIcon;

        View rowView;

        FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rowView = itemView;
        }
    }

    public interface OnNotificationItemClicked {
        public void onMoreClicked(UnreadSavedNotificationMessageData unreadNotificationMessageData);

        public void onSkipClicked(UnreadSavedNotificationMessageData unreadNotificationMessageData);

        public void onNotificationRowClicked(UnreadSavedNotificationMessageData unreadNotificationMessageData);
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
