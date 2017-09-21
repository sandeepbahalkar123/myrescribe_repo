package com.rescribe.adapters.chat;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.rescribe.R;
import com.rescribe.model.chat.MQTTMessage;
import com.rescribe.services.MQTTService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ListViewHolder> {

    private final RequestOptions requestOptions;
    private TextDrawable mReceiverTextDrawable;
    private ArrayList<MQTTMessage> mqttMessages;

    public ChatAdapter(ArrayList<MQTTMessage> mqttMessages, TextDrawable mReceiverTextDrawable) {
        this.mqttMessages = mqttMessages;
        this.mReceiverTextDrawable = mReceiverTextDrawable;
        requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.override(300, 300);
        requestOptions.placeholder(droidninja.filepicker.R.drawable.image_placeholder);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        MQTTMessage message = mqttMessages.get(position);

        if (mqttMessages.get(position).getSender().equals(MQTTService.PATIENT)) {
            holder.receiverLayout.setVisibility(View.GONE);
            holder.senderLayout.setVisibility(View.VISIBLE);
            holder.senderMessage.setText(message.getMsg());

            if (message.getImageUrl().isEmpty()) {
                holder.isSenderPhoto.setVisibility(View.GONE);
                holder.senderMessage.setVisibility(View.VISIBLE);
            } else {
                holder.senderMessage.setVisibility(View.GONE);
                holder.isSenderPhoto.setVisibility(View.VISIBLE);

                holder.senderProgressBar.setVisibility(View.VISIBLE);
                Glide.with(holder.senderPhotoThumb.getContext())
                        .load(message.getImageUrl())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                holder.senderProgressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.senderProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .apply(requestOptions).thumbnail(0.5f)
                        .into(holder.senderPhotoThumb);

                if (message.getMsg().isEmpty())
                    holder.senderMessageWithImage.setVisibility(View.GONE);
                else {
                    holder.senderMessageWithImage.setVisibility(View.VISIBLE);
                    holder.senderMessageWithImage.setText(message.getMsg());
                }
            }

        } else {
            holder.receiverLayout.setVisibility(View.VISIBLE);
            holder.senderLayout.setVisibility(View.GONE);
            holder.receiverMessage.setText(message.getMsg());

            if (message.getImageUrl().isEmpty()) {
                holder.isReceiverPhoto.setVisibility(View.GONE);
                holder.receiverMessage.setVisibility(View.VISIBLE);
            } else {
                holder.isReceiverPhoto.setVisibility(View.VISIBLE);
                holder.receiverMessage.setVisibility(View.GONE);

                holder.receiverProgressBar.setVisibility(View.VISIBLE);
                Glide.with(holder.receiverPhotoThumb.getContext())
                        .load(message.getImageUrl())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                holder.receiverProgressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.receiverProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .apply(requestOptions).thumbnail(0.1f)
                        .into(holder.receiverPhotoThumb);

                if (message.getMsg().isEmpty())
                    holder.receiverMessageWithImage.setVisibility(View.GONE);
                else {
                    holder.receiverMessageWithImage.setVisibility(View.VISIBLE);
                    holder.receiverMessageWithImage.setText(message.getMsg());
                }
            }
        }

        //TODO, sendProfiile Image will set, added it for now
        holder.receiverProfilePhoto.setImageDrawable(mReceiverTextDrawable);

    }

    @Override
    public int getItemCount() {
        return mqttMessages.size();
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

        @BindView(R.id.senderPhotoThumb)
        ImageView senderPhotoThumb;
        @BindView(R.id.isSenderPhoto)
        CardView isSenderPhoto;
        @BindView(R.id.senderMessageWithImage)
        TextView senderMessageWithImage;
        @BindView(R.id.senderProgressBar)
        ProgressBar senderProgressBar;

        @BindView(R.id.receiverPhotoThumb)
        ImageView receiverPhotoThumb;
        @BindView(R.id.isReceiverPhoto)
        CardView isReceiverPhoto;
        @BindView(R.id.receiverMessageWithImage)
        TextView receiverMessageWithImage;
        @BindView(R.id.receiverProgressBar)
        ProgressBar receiverProgressBar;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
