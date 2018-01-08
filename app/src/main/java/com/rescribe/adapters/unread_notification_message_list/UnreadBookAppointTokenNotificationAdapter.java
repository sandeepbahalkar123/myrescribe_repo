package com.rescribe.adapters.unread_notification_message_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rescribe.R;
import com.rescribe.model.book_appointment.unread_token_notification.UnreadBookAppointTokenNotificationData;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.activities.dashboard.UnreadNotificationMessageActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnreadBookAppointTokenNotificationAdapter extends RecyclerView.Adapter<UnreadBookAppointTokenNotificationAdapter.FileViewHolder> {

    private final ArrayList<UnreadBookAppointTokenNotificationData> mOriginalReceivedList;
    private final Context mContext;
    private final OnUnreadTokenNotificationItemClicked listener;
    private UnreadNotificationMessageActivity parentActivity;
    private ArrayList<UnreadBookAppointTokenNotificationData> mListToBeUsed;

    public UnreadBookAppointTokenNotificationAdapter(Context context, ArrayList<UnreadBookAppointTokenNotificationData> list, OnUnreadTokenNotificationItemClicked listener) {
        this.mContext = context;
        this.mOriginalReceivedList = list;
        this.parentActivity = (UnreadNotificationMessageActivity) context;

        mListToBeUsed = new ArrayList<>();
        addSingleElementToList();
        this.listener = listener;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_get_token_notification_layout, parent, false);
        return new UnreadBookAppointTokenNotificationAdapter.FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UnreadBookAppointTokenNotificationAdapter.FileViewHolder holder, final int position) {
        final UnreadBookAppointTokenNotificationData unreadNotificationMessageData = mListToBeUsed.get(position);
        holder.text.setText(unreadNotificationMessageData.getNotificationMsg());

        //---- To show icon for first element based on notification type----
        if (position == 0) {
            holder.imageIcon.setVisibility(View.VISIBLE);
        } else {
            holder.imageIcon.setVisibility(View.INVISIBLE);
        }
        //--------

        int showFirstMessageTimeStamp = parentActivity.isShowFirstMessageTimeStamp(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.TOKEN_ALERT_COUNT);


        //--- TO show more load button or not on appointment_type_notification.
        if (position == 0 && (mListToBeUsed.size() == 1 && mOriginalReceivedList.size() > 1)) {
            holder.loadMoreUnreadTokenMessage.setVisibility(View.VISIBLE);
            holder.tokenMessageTimeStamp.setVisibility(View.INVISIBLE);
        } else {
            holder.loadMoreUnreadTokenMessage.setVisibility(View.INVISIBLE);
            if (showFirstMessageTimeStamp == View.VISIBLE) {
                holder.tokenMessageTimeStamp.setVisibility(View.GONE);
            } else {
                holder.tokenMessageTimeStamp.setVisibility(View.VISIBLE);
            }
        }
        //----------
        if (holder.tokenMessageTimeStamp.getVisibility() == View.VISIBLE) {

            String formattedDate = CommonMethods.getFormattedDate(unreadNotificationMessageData.getCreatedDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.DD_MM_YYYY);
            String time = CommonMethods.formatDateTime(unreadNotificationMessageData.getCreatedDate(), RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.DATE_PATTERN.DD_MM_YYYY + " " + RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.TIME);

            String dayFromDate = CommonMethods.getDayFromDate(RescribeConstants.DATE_PATTERN.DD_MM_YYYY, formattedDate);

            if (mContext.getString(R.string.today).equalsIgnoreCase(dayFromDate)) {
                holder.tokenMessageTimeStamp.setText(time);
            } else {
                holder.tokenMessageTimeStamp.setText(dayFromDate + " " + time);
            }
        }
        //--------------


        //--- TO show more load button or not
        holder.rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNotificationRowClicked(unreadNotificationMessageData);
            }
        });

        holder.loadMoreUnreadTokenMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTokenMoreButtonClicked(unreadNotificationMessageData);
            }
        });

        holder.yesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTextView button = (CustomTextView) v;
                listener.onButtonClicked(button.getText().toString(), unreadNotificationMessageData);
            }
        });
        holder.noTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTextView button = (CustomTextView) v;
                listener.onButtonClicked(button.getText().toString(), unreadNotificationMessageData);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListToBeUsed.size();
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tokenMessage)
        CustomTextView text;
        @BindView(R.id.tokenMessageTimeStamp)
        CustomTextView tokenMessageTimeStamp;
        @BindView(R.id.loadMoreUnreadTokenMessage)
        CustomTextView loadMoreUnreadTokenMessage;
        @BindView(R.id.yesTextView)
        CustomTextView yesTextView;
        @BindView(R.id.noTextView)
        CustomTextView noTextView;

        @BindView(R.id.imageIcon)
        ImageView imageIcon;

        View rowView;

        FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rowView = itemView;
        }
    }

    public interface OnUnreadTokenNotificationItemClicked {
        public void onTokenMoreButtonClicked(UnreadBookAppointTokenNotificationData unreadNotificationMessageData);

        public void onButtonClicked(String type, UnreadBookAppointTokenNotificationData unreadNotificationMessageData);

        public void onNotificationRowClicked(UnreadBookAppointTokenNotificationData unreadNotificationMessageData);
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
