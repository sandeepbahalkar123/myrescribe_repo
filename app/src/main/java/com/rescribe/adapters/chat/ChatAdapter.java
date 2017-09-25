package com.rescribe.adapters.chat;

import android.content.Context;
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
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.rescribe.R;
import com.rescribe.model.chat.MQTTMessage;
import com.rescribe.services.MQTTService;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.NetworkUtil;
import com.rescribe.util.RescribeConstants;
import com.tonyodev.fetch.Fetch;
import com.tonyodev.fetch.request.Request;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ListViewHolder> {

    private final Context context;
    private final Fetch fetch;
    private TextDrawable mReceiverTextDrawable;
    private ArrayList<MQTTMessage> mqttMessages;

    public ChatAdapter(ArrayList<MQTTMessage> mqttMessages, TextDrawable mReceiverTextDrawable, Context context) {
        this.mqttMessages = mqttMessages;
        this.mReceiverTextDrawable = mReceiverTextDrawable;
        this.context = context;
        fetch = Fetch.newInstance(context);
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

            if (!message.getImageUrl().equals("")){
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.dontAnimate();
                requestOptions.override(100, 100);
                requestOptions.transform(new CircleCrop(holder.senderProfilePhoto.getContext()));
                requestOptions.placeholder(R.drawable.exercise);
                Glide.with(holder.senderProfilePhoto.getContext())
                        .load(message.getImageUrl())
                        .apply(requestOptions).thumbnail(0.5f)
                        .into(holder.senderProfilePhoto);
            }

            if (message.getFileUrl().isEmpty()) {
                holder.senderMessage.setText(message.getMsg());
                holder.senderPhotoLayout.setVisibility(View.GONE);
                holder.senderFileLayout.setVisibility(View.GONE);
                holder.senderMessage.setVisibility(View.VISIBLE);
            } else {

                holder.senderMessage.setVisibility(View.GONE);

                if (message.getFileType().equals(RescribeConstants.FILE.DOC)) {
                    holder.senderFileLayout.setVisibility(View.VISIBLE);
                    holder.senderPhotoLayout.setVisibility(View.GONE);
                    String extension = CommonMethods.getExtension(message.getFileUrl());

                    int fontSize = 26;
                    if (extension.length() > 3 && extension.length() < 5)
                        fontSize = 20;
                    else if (extension.length() > 4)
                        fontSize = 16;

                    holder.senderFileExtension.setText(message.getMsg());
                    TextDrawable fileTextDrawable = TextDrawable.builder()
                            .beginConfig()
                            .width(Math.round(holder.senderFileIcon.getResources().getDimension(R.dimen.dp34)))  // width in px
                            .height(Math.round(holder.senderFileIcon.getResources().getDimension(R.dimen.dp34))) // height in px
                            .bold()
                            .fontSize(fontSize)
                            .toUpperCase()
                            .endConfig()
                            .buildRoundRect(extension, holder.senderFileIcon.getResources().getColor(R.color.grey_500), CommonMethods.convertDpToPixel(2));
                    holder.senderFileIcon.setImageDrawable(fileTextDrawable);

                } else {

                    holder.senderPhotoLayout.setVisibility(View.VISIBLE);
                    holder.senderFileLayout.setVisibility(View.GONE);

                    holder.senderPhotoProgressLayout.setVisibility(View.VISIBLE);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();
                    requestOptions.override(300, 300);
                    requestOptions.placeholder(R.drawable.image_placeholder);
                    Glide.with(holder.senderPhotoThumb.getContext())
                            .load(message.getFileUrl())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    holder.senderPhotoProgressLayout.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    holder.senderPhotoProgressLayout.setVisibility(View.GONE);
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
            }

        } else {
            holder.receiverLayout.setVisibility(View.VISIBLE);
            holder.senderLayout.setVisibility(View.GONE);

            if (!message.getImageUrl().equals("")){
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.dontAnimate();
                requestOptions.override(100, 100);
                requestOptions.transform(new CircleCrop(holder.receiverProfilePhoto.getContext()));
                requestOptions.placeholder(R.drawable.doctor_speciality);
                Glide.with(holder.receiverProfilePhoto.getContext())
                        .load(message.getImageUrl())
                        .apply(requestOptions).thumbnail(0.5f)
                        .into(holder.receiverProfilePhoto);
            }

            if (message.getFileUrl().isEmpty()) {
                holder.receiverMessage.setText(message.getMsg());
                holder.receiverPhotoLayout.setVisibility(View.GONE);
                holder.receiverFileLayout.setVisibility(View.GONE);
                holder.receiverMessage.setVisibility(View.VISIBLE);
            } else {

                holder.receiverMessage.setVisibility(View.GONE);

                if (message.getFileType().equals(RescribeConstants.FILE.DOC)) {

//                    fileDownload();

                    holder.receiverFileLayout.setVisibility(View.VISIBLE);
                    holder.receiverPhotoLayout.setVisibility(View.GONE);

                    String extension = CommonMethods.getExtension(message.getFileUrl());

                    int fontSize = 26;
                    if (extension.length() > 3 && extension.length() < 5)
                        fontSize = 20;
                    else if (extension.length() > 4)
                        fontSize = 16;

                    holder.receiverFileExtension.setText(message.getMsg());
                    TextDrawable fileTextDrawable = TextDrawable.builder()
                            .beginConfig()
                            .width(Math.round(holder.senderFileIcon.getResources().getDimension(R.dimen.dp34)))  // width in px
                            .height(Math.round(holder.senderFileIcon.getResources().getDimension(R.dimen.dp34))) // height in px
                            .bold()
                            .fontSize(fontSize)
                            .toUpperCase()
                            .endConfig()
                            .buildRoundRect(CommonMethods.getExtension(message.getImageUrl()), holder.senderFileIcon.getResources().getColor(R.color.grey_500), CommonMethods.convertDpToPixel(3));

                    holder.receiverFileIcon.setImageDrawable(fileTextDrawable);

                } else {

                    holder.receiverPhotoLayout.setVisibility(View.VISIBLE);
                    holder.receiverFileLayout.setVisibility(View.GONE);

                    holder.receiverPhotoProgressLayout.setVisibility(View.VISIBLE);

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();
                    requestOptions.override(300, 300);
                    requestOptions.placeholder(droidninja.filepicker.R.drawable.image_placeholder);
                    Glide.with(holder.receiverPhotoThumb.getContext())
                            .load(message.getImageUrl())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    holder.receiverPhotoProgressLayout.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    holder.receiverPhotoProgressLayout.setVisibility(View.GONE);
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
        }

        //TODO, sendProfiile Image will set, added it for now
        holder.receiverProfilePhoto.setImageDrawable(mReceiverTextDrawable);

    }

    private void fileDownload(String url, String dirPath, String fileName) {
        if (NetworkUtil.getConnectivityStatusBoolean(context)) {
            Request request = new Request(url, dirPath, fileName);
            long downloadId = fetch.enqueue(request);

            if (downloadId != Fetch.ENQUEUE_ERROR_ID) {
                //Download was successfully queued for download.


            }
        } else
            CommonMethods.showToast(context, context.getResources().getString(R.string.internet));
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

        // Photo

        @BindView(R.id.senderPhotoThumb)
        ImageView senderPhotoThumb;
        @BindView(R.id.senderPhotoLayout)
        CardView senderPhotoLayout;
        @BindView(R.id.senderMessageWithImage)
        TextView senderMessageWithImage;

        @BindView(R.id.receiverPhotoThumb)
        ImageView receiverPhotoThumb;
        @BindView(R.id.receiverPhotoLayout)
        CardView receiverPhotoLayout;
        @BindView(R.id.receiverMessageWithImage)
        TextView receiverMessageWithImage;

        // File

        @BindView(R.id.senderFileIcon)
        ImageView senderFileIcon;
        @BindView(R.id.senderFileExtension)
        CustomTextView senderFileExtension;
       
        @BindView(R.id.senderFileLayout)
        RelativeLayout senderFileLayout;

        @BindView(R.id.receiverFileIcon)
        ImageView receiverFileIcon;
        @BindView(R.id.receiverFileExtension)
        CustomTextView receiverFileExtension;
       
        @BindView(R.id.receiverFileLayout)
        RelativeLayout receiverFileLayout;

        @BindView(R.id.receiverFileDownloading)
        RelativeLayout receiverFileDownloading;
        @BindView(R.id.receiverFileDownloadStopped)
        RelativeLayout receiverFileDownloadStopped;

        @BindView(R.id.senderFileUploading)
        RelativeLayout senderFileUploading;
        @BindView(R.id.senderFileUploadStopped)
        RelativeLayout senderFileUploadStopped;

        @BindView(R.id.senderFileProgressLayout)
        RelativeLayout senderFileProgressLayout;

        @BindView(R.id.senderPhotoProgressLayout)
        RelativeLayout senderPhotoProgressLayout;
        
        @BindView(R.id.receiverFileProgressLayout)
        RelativeLayout receiverFileProgressLayout;

        @BindView(R.id.receiverPhotoProgressLayout)
        RelativeLayout receiverPhotoProgressLayout;
        
        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
