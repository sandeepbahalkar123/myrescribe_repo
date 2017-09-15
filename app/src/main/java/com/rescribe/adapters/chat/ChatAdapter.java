package com.rescribe.adapters.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.rescribe.R;
import com.rescribe.model.chat.MessageList;
import com.rescribe.services.MQTTService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ListViewHolder> {

    private TextDrawable mReceiverTextDrawable;
    private ArrayList<MessageList> messageList;

    public ChatAdapter(ArrayList<MessageList> messageList, TextDrawable mReceiverTextDrawable) {
        this.messageList = messageList;
        this.mReceiverTextDrawable = mReceiverTextDrawable;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        MessageList message = messageList.get(position);

        if (messageList.get(position).getSender().equals(MQTTService.PATIENT)) {
            holder.receiverLayout.setVisibility(View.GONE);
            holder.senderLayout.setVisibility(View.VISIBLE);
            holder.senderMessage.setText(message.getMsg());
        } else {
            holder.receiverLayout.setVisibility(View.VISIBLE);
            holder.senderLayout.setVisibility(View.GONE);
            holder.receiverMessage.setText(message.getMsg());
        }

        //TODO, sendProfiile Image will set, added it for now
        holder.senderProfilePhoto.setImageDrawable(mReceiverTextDrawable);

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.senderMessage)
        TextView senderMessage;
        @BindView(R.id.senderProfilePhoto)
        ImageView senderProfilePhoto;
        @BindView(R.id.senderLayout)
        RelativeLayout senderLayout;
        @BindView(R.id.receiverProfilePhoto)
        ImageView receiverProfilePhoto;
        @BindView(R.id.receiverMessage)
        TextView receiverMessage;
        @BindView(R.id.receiverLayout)
        RelativeLayout receiverLayout;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

