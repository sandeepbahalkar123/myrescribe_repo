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
    private ArrayList<UnreadNotificationMessageData> mListToBeUsed;

    public UnreadAppointmentNotificationAlert(Context context, ArrayList<UnreadNotificationMessageData> list) {
        this.mContext = context;
        this.mOriginalReceivedList = list;

        mListToBeUsed = new ArrayList<>();
        mListToBeUsed.add(mOriginalReceivedList.get(0));

    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_appointment_notification_layout, parent, false);
        return new UnreadAppointmentNotificationAlert.FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UnreadAppointmentNotificationAlert.FileViewHolder holder, final int position) {
        UnreadNotificationMessageData unreadNotificationMessageData = mListToBeUsed.get(position);
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

        //--- TO show more load button or not
        if (position == 0 & mOriginalReceivedList.size() > 1) {
            holder.loadMoreItems.setVisibility(View.VISIBLE);
        } else {
            holder.loadMoreItems.setVisibility(View.GONE);
        }
        //--- TO show more load button or not

        holder.rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.loadMoreItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mOriginalReceivedList.size();
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textMessage)
        CustomTextView text;
        @BindView(R.id.loadMoreItems)
        CustomTextView loadMoreItems;
        @BindView(R.id.imageIcon)
        ImageView imageIcon;

        View rowView;

        FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rowView = itemView;
        }
    }


}
