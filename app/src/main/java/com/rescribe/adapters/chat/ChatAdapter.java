package com.rescribe.adapters.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.rescribe.R;
import com.rescribe.model.chat.MQTTMessage;
import com.rescribe.ui.activities.ZoomImageViewActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rescribe.services.MQTTService.PATIENT;
import static com.rescribe.util.RescribeConstants.COMPLETED;
import static com.rescribe.util.RescribeConstants.DOWNLOADING;
import static com.rescribe.util.RescribeConstants.FAILED;
import static com.rescribe.util.RescribeConstants.FILE.LOC;
import static com.rescribe.util.RescribeConstants.MESSAGE_STATUS.REACHED;
import static com.rescribe.util.RescribeConstants.MESSAGE_STATUS.SEEN;
import static com.rescribe.util.RescribeConstants.UPLOADING;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ListViewHolder> {

    private final Context context;
    private final ItemListener itemListener;
    private TextDrawable mReceiverTextDrawable;
    private TextDrawable mSelfTextDrawable;
    private ArrayList<MQTTMessage> mqttMessages;

    public ChatAdapter(ArrayList<MQTTMessage> mqttMessages, TextDrawable mSelfTextDrawable, TextDrawable mReceiverTextDrawable, Context context) {
        this.mqttMessages = mqttMessages;
        this.mSelfTextDrawable = mSelfTextDrawable;
        this.mReceiverTextDrawable = mReceiverTextDrawable;
        this.context = context;

        try {
            this.itemListener = ((ItemListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ItemClickListener.");
        }
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);

        return new ListViewHolder(itemView);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {
        final MQTTMessage message = mqttMessages.get(position);

        String timeText = CommonMethods.getFormattedDate(message.getMsgTime(), RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE_PATTERN.hh_mm_a);
        String dateText = CommonMethods.getDayFromDateTime(message.getMsgTime(), RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE_PATTERN.DD_MMMM_YYYY);

        holder.dateTextView.setText(dateText);

        if (position > 0) {
            String preDate = CommonMethods.getDayFromDateTime(mqttMessages.get(position - 1).getMsgTime(), RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE_PATTERN.DD_MMMM_YYYY);
            message.setDateVisible(!preDate.equals(dateText));
        }

        if (message.isDateVisible())
            holder.dateTextView.setVisibility(View.VISIBLE);
        else holder.dateTextView.setVisibility(View.GONE);

        String urlWithLatLong = "";
        if (message.getFileType().equals(LOC))
            urlWithLatLong = "https://maps.googleapis.com/maps/api/staticmap?center=" + message.getFileUrl() + "&markers=color:red%7Clabel:" + (message.getSender().equals(PATIENT) ? "P" : "D") + "%7C" + message.getFileUrl() + "&zoom=14&size=300x300";

        if (message.getSender().equals(PATIENT)) {

            // set Time
            holder.senderTimeTextView.setText(timeText);

            switch (message.getMsgStatus()) {
                case REACHED:
                    holder.senderTickImageView.setImageResource(R.drawable.ic_reached);
                    break;
                case SEEN:
                    holder.senderTickImageView.setImageResource(R.drawable.ic_seen);
                    break;
                default:
                    holder.senderTickImageView.setImageResource(R.drawable.ic_sent);
                    break;
            }

            // reset margin
            RelativeLayout.LayoutParams resetSenderPhotoLayoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            resetSenderPhotoLayoutParams.setMargins(0, 0, 0, 0);
            resetSenderPhotoLayoutParams.addRule(RelativeLayout.LEFT_OF, R.id.senderProfilePhoto);
            holder.senderLayoutChild.setLayoutParams(resetSenderPhotoLayoutParams);

            holder.receiverLayout.setVisibility(View.GONE);
            holder.senderLayout.setVisibility(View.VISIBLE);

            if (!message.getImageUrl().equals("")) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.dontAnimate();
                requestOptions.override(100, 100);
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                requestOptions.skipMemoryCache(true);
                requestOptions.transform(new CircleCrop(holder.senderProfilePhoto.getContext()));
                requestOptions.placeholder(mSelfTextDrawable);
                Glide.with(holder.senderProfilePhoto.getContext())
                        .load(message.getImageUrl())
                        .apply(requestOptions).thumbnail(0.5f)
                        .into(holder.senderProfilePhoto);
            } else {
                holder.senderProfilePhoto.setImageDrawable(mSelfTextDrawable);
            }

            if (message.getFileUrl().isEmpty()) {
                holder.senderMessage.setText(message.getMsg());
                holder.senderPhotoLayout.setVisibility(View.GONE);
                holder.senderFileLayout.setVisibility(View.GONE);
                holder.senderMessage.setVisibility(View.VISIBLE);
            } else {

                holder.senderMessage.setVisibility(View.GONE);

                switch (message.getFileType()) {
                    case RescribeConstants.FILE.AUD:

                        holder.senderFileLayout.setVisibility(View.VISIBLE);

                        if (message.getUploadStatus() == RescribeConstants.UPLOADING) {
                            holder.senderFileProgressLayout.setVisibility(View.VISIBLE);
                            holder.senderFileUploadStopped.setVisibility(View.GONE);
                            holder.senderFileUploading.setVisibility(View.VISIBLE);
                        } else if (message.getUploadStatus() == FAILED) {
                            holder.senderFileProgressLayout.setVisibility(View.VISIBLE);
                            holder.senderFileUploadStopped.setVisibility(View.VISIBLE);
                            holder.senderFileUploading.setVisibility(View.GONE);
                        } else if (message.getUploadStatus() == RescribeConstants.COMPLETED) {
                            holder.senderFileProgressLayout.setVisibility(View.GONE);
                            holder.senderFileUploadStopped.setVisibility(View.GONE);
                            holder.senderFileUploading.setVisibility(View.GONE);
                        }

                        holder.senderPhotoLayout.setVisibility(View.GONE);
                        holder.senderFileExtension.setText(message.getMsg());
                        holder.senderFileIcon.setImageResource(R.drawable.ic_play_arrow_white_24dp);

                        holder.senderFileLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (message.getUploadStatus() == FAILED) {
                                    itemListener.uploadFile(message);
                                    message.setUploadStatus(UPLOADING);
                                    notifyItemChanged(position);
                                } else if (message.getUploadStatus() == COMPLETED) {
                                    // do file open stuff here
                                    try {
                                        itemListener.openFile(message, holder.senderFileIcon);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

                        break;
                    case RescribeConstants.FILE.DOC:
                        holder.senderFileLayout.setVisibility(View.VISIBLE);

                        if (message.getUploadStatus() == RescribeConstants.UPLOADING) {
                            holder.senderFileProgressLayout.setVisibility(View.VISIBLE);
                            holder.senderFileUploadStopped.setVisibility(View.GONE);
                            holder.senderFileUploading.setVisibility(View.VISIBLE);
                        } else if (message.getUploadStatus() == FAILED) {
                            holder.senderFileProgressLayout.setVisibility(View.VISIBLE);
                            holder.senderFileUploadStopped.setVisibility(View.VISIBLE);
                            holder.senderFileUploading.setVisibility(View.GONE);
                        } else if (message.getUploadStatus() == RescribeConstants.COMPLETED) {
                            holder.senderFileProgressLayout.setVisibility(View.GONE);
                            holder.senderFileUploadStopped.setVisibility(View.GONE);
                            holder.senderFileUploading.setVisibility(View.GONE);
                        }

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

                        holder.senderFileLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (message.getUploadStatus() == FAILED) {
                                    itemListener.uploadFile(message);
                                    message.setUploadStatus(UPLOADING);
                                    notifyItemChanged(position);
                                } else if (message.getUploadStatus() == COMPLETED) {
                                    // do file open stuff here
                                    try {
                                        itemListener.openFile(message, holder.senderFileIcon);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

                        break;
                    default:

                        // set left margin
                        RelativeLayout.LayoutParams senderPhotoLayoutParams = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);

                        senderPhotoLayoutParams.setMargins(context.getResources().getDimensionPixelOffset(R.dimen.margin_imageview), 0, 0, 0);
                        senderPhotoLayoutParams.addRule(RelativeLayout.LEFT_OF, R.id.senderProfilePhoto);
                        holder.senderLayoutChild.setLayoutParams(senderPhotoLayoutParams);

                        holder.senderPhotoLayout.setVisibility(View.VISIBLE);
                        holder.senderFileLayout.setVisibility(View.GONE);

                        RequestOptions requestOptions = new RequestOptions();

                        if (message.getFileType().equals(LOC)) {
                            // set placeholder for mapview
                            requestOptions.placeholder(R.drawable.staticmap);
                            requestOptions.error(R.drawable.staticmap);
                            requestOptions.override(300, 300);

                            message.setUploadStatus(COMPLETED);
                            holder.senderMessageWithImage.setVisibility(View.GONE);

                            Glide.with(holder.senderPhotoThumb.getContext())
                                    .load(urlWithLatLong)
                                    .apply(requestOptions)
                                    .into(holder.senderPhotoThumb);

                            holder.senderPhotoLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Uri gmmIntentUri = Uri.parse("geo:" + message.getFileUrl() + "?q=(" + (message.getSender().equals(PATIENT) ? "Patient Location" : "Doctor Location") + ")@" + message.getFileUrl());
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                                        context.startActivity(mapIntent);
                                    } else {
                                        CommonMethods.showToast(context, "GoogleMap application not installed on your device.");
                                    }
                                }
                            });

                        } else {
                            requestOptions.dontAnimate();
                            requestOptions.placeholder(R.drawable.image_placeholder);
                            requestOptions.error(R.drawable.image_placeholder);

                            if (message.getMsg().isEmpty())
                                holder.senderMessageWithImage.setVisibility(View.GONE);
                            else {
                                holder.senderMessageWithImage.setVisibility(View.VISIBLE);
                                holder.senderMessageWithImage.setText(message.getMsg());
                            }

                            final boolean isUrl;
                            String filePath = message.getFileUrl().substring(0, 4);
                            if (filePath.equals("http")) {
                                isUrl = true;
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
                            } else {

                                isUrl = false;

                                Glide.with(holder.senderPhotoThumb.getContext())
                                        .load(new File(message.getFileUrl()))
                                        .apply(requestOptions).thumbnail(0.5f)
                                        .into(holder.senderPhotoThumb);
                            }

                            holder.senderPhotoLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (message.getUploadStatus() == FAILED) {
                                        itemListener.uploadFile(message);
                                        message.setUploadStatus(UPLOADING);
                                        notifyItemChanged(position);
                                    } else if (message.getUploadStatus() == COMPLETED) {
                                        Intent intent = new Intent(context, ZoomImageViewActivity.class);
                                        intent.putExtra(RescribeConstants.DOCUMENTS, message.getFileUrl());
                                        intent.putExtra(RescribeConstants.IS_URL, isUrl);
                                        context.startActivity(intent);
                                    }
                                }
                            });
                        }

                        if (message.getUploadStatus() == RescribeConstants.UPLOADING) {
                            holder.senderPhotoProgressLayout.setVisibility(View.VISIBLE);
                            holder.senderPhotoUploading.setVisibility(View.VISIBLE);
                            holder.senderPhotoUploadStopped.setVisibility(View.GONE);
                        } else if (message.getUploadStatus() == FAILED) {
                            holder.senderPhotoProgressLayout.setVisibility(View.VISIBLE);
                            holder.senderPhotoUploading.setVisibility(View.GONE);
                            holder.senderPhotoUploadStopped.setVisibility(View.VISIBLE);
                        } else if (message.getUploadStatus() == RescribeConstants.COMPLETED) {
                            holder.senderPhotoProgressLayout.setVisibility(View.GONE);
                            holder.senderPhotoUploading.setVisibility(View.GONE);
                            holder.senderPhotoUploadStopped.setVisibility(View.GONE);
                        }

                        break;
                }
            }

        } else {

            // set Time
            holder.receiverTimeTextView.setText(timeText);

            // reset margin
            RelativeLayout.LayoutParams resetReceiverPhotoLayoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            resetReceiverPhotoLayoutParams.setMargins(0, 0, 0, 0);
            resetReceiverPhotoLayoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.receiverProfilePhoto);
            holder.receiverLayoutChild.setLayoutParams(resetReceiverPhotoLayoutParams);

            holder.receiverLayout.setVisibility(View.VISIBLE);
            holder.senderLayout.setVisibility(View.GONE);

            if (!message.getImageUrl().equals("")) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.dontAnimate();
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                requestOptions.skipMemoryCache(true);
                requestOptions.override(100, 100);
                requestOptions.transform(new CircleCrop(holder.receiverProfilePhoto.getContext()));
                requestOptions.placeholder(mReceiverTextDrawable);
                Glide.with(holder.receiverProfilePhoto.getContext())
                        .load(message.getImageUrl())
                        .apply(requestOptions).thumbnail(0.5f)
                        .into(holder.receiverProfilePhoto);
            } else {
                holder.receiverProfilePhoto.setImageDrawable(mReceiverTextDrawable);
            }

            if (message.getFileUrl().isEmpty()) {
                holder.receiverMessage.setText(message.getMsg());
                holder.receiverPhotoLayout.setVisibility(View.GONE);
                holder.receiverFileLayout.setVisibility(View.GONE);
                holder.receiverMessage.setVisibility(View.VISIBLE);
            } else {

                holder.receiverMessage.setVisibility(View.GONE);
                switch (message.getFileType()) {
                    case RescribeConstants.FILE.AUD: {

                        holder.receiverFileLayout.setVisibility(View.VISIBLE);
                        holder.receiverPhotoLayout.setVisibility(View.GONE);

                        if (message.getDownloadStatus() == FAILED) {
                            holder.receiverFileProgressLayout.setVisibility(View.VISIBLE);
                            holder.receiverFileDownloadStopped.setVisibility(View.VISIBLE);
                            holder.receiverFileDownloading.setVisibility(View.GONE);
                        } else if (message.getDownloadStatus() == COMPLETED) {
                            holder.receiverFileProgressLayout.setVisibility(View.GONE);
                            holder.receiverFileDownloadStopped.setVisibility(View.GONE);
                            holder.receiverFileDownloading.setVisibility(View.GONE);
                        } else if (message.getDownloadStatus() == UPLOADING) {
                            holder.receiverFileProgressLayout.setVisibility(View.VISIBLE);
                            holder.receiverFileDownloadStopped.setVisibility(View.GONE);
                            holder.receiverFileDownloading.setVisibility(View.VISIBLE);
                        }

                        holder.receiverFileExtension.setText(message.getMsg());
                        holder.receiverFileIcon.setImageResource(R.drawable.ic_play_arrow_white_24dp);

                        holder.receiverFileLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (message.getDownloadStatus() == FAILED) {
                                    itemListener.downloadFile(message);
                                    message.setDownloadStatus(DOWNLOADING);
                                    notifyItemChanged(position);
                                } else if (message.getDownloadStatus() == COMPLETED) {
                                    // open File in Viewer
                                    try {
                                        itemListener.openFile(message, holder.receiverFileIcon);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

                        break;
                    }
                    case RescribeConstants.FILE.DOC: {

                        holder.receiverFileLayout.setVisibility(View.VISIBLE);
                        holder.receiverPhotoLayout.setVisibility(View.GONE);

                        if (message.getDownloadStatus() == FAILED) {
                            holder.receiverFileProgressLayout.setVisibility(View.VISIBLE);
                            holder.receiverFileDownloadStopped.setVisibility(View.VISIBLE);
                            holder.receiverFileDownloading.setVisibility(View.GONE);
                        } else if (message.getDownloadStatus() == COMPLETED) {
                            holder.receiverFileProgressLayout.setVisibility(View.GONE);
                            holder.receiverFileDownloadStopped.setVisibility(View.GONE);
                            holder.receiverFileDownloading.setVisibility(View.GONE);
                        } else if (message.getDownloadStatus() == UPLOADING) {
                            holder.receiverFileProgressLayout.setVisibility(View.VISIBLE);
                            holder.receiverFileDownloadStopped.setVisibility(View.GONE);
                            holder.receiverFileDownloading.setVisibility(View.VISIBLE);
                        }

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
                                .buildRoundRect(extension, holder.senderFileIcon.getResources().getColor(R.color.grey_500), CommonMethods.convertDpToPixel(3));

                        holder.receiverFileIcon.setImageDrawable(fileTextDrawable);

                        holder.receiverFileLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (message.getDownloadStatus() == FAILED) {
                                    itemListener.downloadFile(message);
                                    message.setDownloadStatus(DOWNLOADING);
                                    notifyItemChanged(position);
                                } else if (message.getDownloadStatus() == COMPLETED) {
                                    // open File in Viewer
                                    try {
                                        itemListener.openFile(message, holder.receiverFileIcon);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

                        break;
                    }
                    default:

                        holder.receiverPhotoLayout.setVisibility(View.VISIBLE);
                        holder.receiverFileLayout.setVisibility(View.GONE);

                        // set left margin
                        RelativeLayout.LayoutParams receiverPhotoLayoutParams = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);

                        receiverPhotoLayoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.receiverProfilePhoto);
                        receiverPhotoLayoutParams.setMargins(0, 0, context.getResources().getDimensionPixelOffset(R.dimen.margin_imageview), 0);
                        holder.receiverLayoutChild.setLayoutParams(receiverPhotoLayoutParams);

                        if (message.getMsg().isEmpty())
                            holder.receiverMessageWithImage.setVisibility(View.GONE);
                        else {
                            holder.receiverMessageWithImage.setVisibility(View.VISIBLE);
                            holder.receiverMessageWithImage.setText(message.getMsg());
                        }

                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.override(300, 300);

                        if (message.getFileType().equals(LOC)) {
                            // set placeholder for mapview
                            requestOptions.placeholder(R.drawable.staticmap);
                            requestOptions.error(R.drawable.staticmap);
                            holder.receiverPhotoProgressLayout.setVisibility(View.GONE);

                            Glide.with(holder.receiverPhotoThumb.getContext())
                                    .load(urlWithLatLong)
                                    .apply(requestOptions)
                                    .into(holder.receiverPhotoThumb);

                        } else {
                            holder.receiverPhotoProgressLayout.setVisibility(View.VISIBLE);
                            holder.receiverPhotoDownloading.setVisibility(View.VISIBLE);
                            holder.receiverPhotoDownloadStopped.setVisibility(View.GONE);

                            requestOptions.dontAnimate();
                            requestOptions.placeholder(droidninja.filepicker.R.drawable.image_placeholder);
                            requestOptions.error(droidninja.filepicker.R.drawable.image_placeholder);

                            Glide.with(holder.receiverPhotoThumb.getContext())
                                    .load(message.getFileUrl())
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            holder.receiverPhotoProgressLayout.setVisibility(View.VISIBLE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            holder.receiverPhotoProgressLayout.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .apply(requestOptions).thumbnail(0.5f)
                                    .into(holder.receiverPhotoThumb);
                        }

                        holder.receiverPhotoLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (message.getFileType().equals(LOC)) {
                                    Uri gmmIntentUri = Uri.parse("geo:" + message.getFileUrl() + "?q=(" + (message.getSender().equals(PATIENT) ? "Patient Location" : "Doctor Location") + ")@" + message.getFileUrl());
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                                        context.startActivity(mapIntent);
                                    } else {
                                        CommonMethods.showToast(context, "GoogleMap application not installed on your device.");
                                    }
                                } else {
                                    Intent intent = new Intent(context, ZoomImageViewActivity.class);
                                    intent.putExtra(RescribeConstants.DOCUMENTS, message.getFileUrl());
                                    intent.putExtra(RescribeConstants.IS_URL, true);
                                    context.startActivity(intent);
                                }
                            }
                        });

                        break;
                }
            }
        }

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

        @BindView(R.id.receiverPhotoDownloading)
        RelativeLayout receiverPhotoDownloading;
        @BindView(R.id.receiverPhotoDownloadStopped)
        RelativeLayout receiverPhotoDownloadStopped;

        @BindView(R.id.senderPhotoUploading)
        RelativeLayout senderPhotoUploading;
        @BindView(R.id.senderPhotoUploadStopped)
        RelativeLayout senderPhotoUploadStopped;

        @BindView(R.id.receiverLayoutChild)
        LinearLayout receiverLayoutChild;
        @BindView(R.id.senderLayoutChild)
        LinearLayout senderLayoutChild;

        // Time and Message Status
        @BindView(R.id.receiverTimeTextView)
        TextView receiverTimeTextView;
        @BindView(R.id.senderTimeTextView)
        TextView senderTimeTextView;
        @BindView(R.id.senderTickImageView)
        ImageView senderTickImageView;

        @BindView(R.id.dateTextView)
        TextView dateTextView;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface ItemListener {
        void uploadFile(MQTTMessage mqttMessage);
        long downloadFile(MQTTMessage mqttMessage);
        void openFile(MQTTMessage message, ImageView senderFileIcon) throws IOException;
    }
}
