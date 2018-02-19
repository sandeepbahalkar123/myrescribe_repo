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

public class UnreadFollowUpNotificationAdapter extends RecyclerView.Adapter<UnreadFollowUpNotificationAdapter.FileViewHolder> {

    private final ArrayList<UnreadBookAppointTokenNotificationData> mOriginalReceivedList;
    private final Context mContext;
    private final OnUnreadFollowUpNotificationItemClicked listener;
    private UnreadNotificationMessageActivity parentActivity;
    private ArrayList<UnreadBookAppointTokenNotificationData> mListToBeUsed;

    public UnreadFollowUpNotificationAdapter(Context context, ArrayList<UnreadBookAppointTokenNotificationData> list, OnUnreadFollowUpNotificationItemClicked listener) {
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
                .inflate(R.layout.item_follow_up_notification, parent, false);
        return new UnreadFollowUpNotificationAdapter.FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UnreadFollowUpNotificationAdapter.FileViewHolder holder, final int position) {
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
                holder.tokenMessageTimeStamp.setVisibility(View.INVISIBLE);
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

        holder.loadMoreUnreadTokenMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFollowUpMoreButtonClicked(unreadNotificationMessageData);
            }
        });

        holder.yesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTextView button = (CustomTextView) v;
                listener.onFollowUpButtonClicked(button.getText().toString(), unreadNotificationMessageData);
            }
        });
        holder.noTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTextView button = (CustomTextView) v;
                listener.onFollowUpButtonClicked(button.getText().toString(), unreadNotificationMessageData);
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
        CustomTextView tokenMessageTimeStamp;
        @BindView(R.id.loadMoreUnreadTokenMessage)
        CustomTextView loadMoreUnreadTokenMessage;
        @BindView(R.id.yesTextView)
        CustomTextView yesTextView;
        @BindView(R.id.noTextView)
        CustomTextView noTextView;

        @BindView(R.id.imageIcon)
        ImageView imageIcon;

        FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnUnreadFollowUpNotificationItemClicked {
        public void onFollowUpMoreButtonClicked(UnreadBookAppointTokenNotificationData unreadNotificationMessageData);
        public void onFollowUpButtonClicked(String type, UnreadBookAppointTokenNotificationData unreadNotificationMessageData);
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
