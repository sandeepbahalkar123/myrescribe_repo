package com.rescribe.adapters.unread_notification_message_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rescribe.R;
import com.rescribe.model.dashboard_api.unread_notification_message_list.UnreadNotificationMessageData;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnreadAppointmentNotificationAlert extends RecyclerView.Adapter<UnreadAppointmentNotificationAlert.FileViewHolder> {

    private final ArrayList<UnreadNotificationMessageData> mOriginalReceivedList;
    private final Context mContext;
    private final OnNotificationItemClicked listener;
    private ArrayList<UnreadNotificationMessageData> mListToBeUsed;

    public UnreadAppointmentNotificationAlert(Context context, ArrayList<UnreadNotificationMessageData> list, OnNotificationItemClicked listener) {
        this.mContext = context;
        this.mOriginalReceivedList = list;

        mListToBeUsed = new ArrayList<>();
        addSingleElementToList();
        this.listener = listener;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_appointment_notification_layout, parent, false);
        return new UnreadAppointmentNotificationAlert.FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UnreadAppointmentNotificationAlert.FileViewHolder holder, final int position) {
        final UnreadNotificationMessageData unreadNotificationMessageData = mListToBeUsed.get(position);
        holder.text.setText(unreadNotificationMessageData.getNotificationData());

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

        //--- TO show more load button or not on appointment_type_notification.
        if (unreadNotificationMessageData.getNotificationMessageType().equalsIgnoreCase(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT)) {
            if (position == 0 && (mListToBeUsed.size() == 1 && mOriginalReceivedList.size() > 1)) {
                holder.loadMoreItems.setVisibility(View.VISIBLE);
                holder.loadLessItems.setVisibility(View.GONE);
            } else if (position == 0 && mListToBeUsed.size() == mOriginalReceivedList.size()) {
                holder.loadMoreItems.setVisibility(View.GONE);
                holder.loadLessItems.setVisibility(View.VISIBLE);
            } else {
                holder.loadMoreItems.setVisibility(View.GONE);
                holder.loadLessItems.setVisibility(View.GONE);
            }
        } else if (unreadNotificationMessageData.getNotificationMessageType().equalsIgnoreCase(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT)) {
            holder.loadMoreItems.setVisibility(View.GONE);
            holder.loadLessItems.setVisibility(View.GONE);
            holder.skipItems.setVisibility(View.VISIBLE);
        }
        //--- TO show more load button or not

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
        holder.loadLessItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLessClicked(unreadNotificationMessageData);
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
        @BindView(R.id.loadMoreItems)
        CustomTextView loadMoreItems;
        @BindView(R.id.loadLessItems)
        CustomTextView loadLessItems;
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
        public void onMoreClicked(UnreadNotificationMessageData unreadNotificationMessageData);

        public void onLessClicked(UnreadNotificationMessageData unreadNotificationMessageData);

        public void onSkipClicked();

        public void onNotificationRowClicked(UnreadNotificationMessageData unreadNotificationMessageData);
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
