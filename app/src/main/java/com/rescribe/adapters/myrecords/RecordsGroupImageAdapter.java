package com.rescribe.adapters.myrecords;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.model.investigation.Image;
import com.rescribe.util.RescribeConstants;

import java.io.File;
import java.util.List;

import static com.rescribe.ui.activities.SelectedRecordsGroupActivity.INVESTIGATIONS;

public class RecordsGroupImageAdapter extends RecyclerView.Adapter<RecordsGroupImageAdapter.MyViewHolder> {

    private final Context mContext;
    private final ItemListener itemListener;
    private int mainPosition;
    private List<Image> images;
    private int imageSize;

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
        RelativeLayout progressBarLay;
        ImageView removeCheckbox;
        TextView addCaptionText;
        ImageView scroller;
        Button retryButton;
        RelativeLayout addCaptionLayout;

        MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            progressBarLay = (RelativeLayout) view.findViewById(R.id.progress_bar_lay);
            removeCheckbox = (ImageView) view.findViewById(R.id.removeCheckbox);
            scroller = (ImageView) view.findViewById(R.id.scroller);
            addCaptionLayout = (RelativeLayout) view.findViewById(R.id.addCaptionLayout);
            removeCheckbox.setAlpha(.5f);
            addCaptionText = (TextView) view.findViewById(R.id.addCaptionText);
            retryButton = (Button) view.findViewById(R.id.retryButton);
        }
    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = widthPixels / columnNum;
    }

    public RecordsGroupImageAdapter(List<Image> images, Context context) {
        this.images = images;
        mContext = context;
        setColumnNumber(context, 2);

        try {
            this.itemListener = ((ItemListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ItemClickListener.");
        }
    }

    public void setMainPosition(int mainPosition) {
        this.mainPosition = mainPosition;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.records_image_grid_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Image image = images.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        requestOptions.override(imageSize, imageSize);
        requestOptions.placeholder(droidninja.filepicker.R.drawable.image_placeholder);

        if (image.isUploading() == RescribeConstants.UPLOADING) {
            holder.progressBarLay.setVisibility(View.VISIBLE);
            holder.removeCheckbox.setVisibility(View.GONE);
            holder.retryButton.setVisibility(View.GONE);
        } else if (image.isUploading() == RescribeConstants.FAILED) {
            holder.progressBarLay.setVisibility(View.GONE);
            holder.retryButton.setVisibility(View.VISIBLE);
            holder.removeCheckbox.setVisibility(View.GONE);
        } else if (image.isUploading() == RescribeConstants.COMPLETED) {
            holder.progressBarLay.setVisibility(View.GONE);
            holder.removeCheckbox.setVisibility(View.GONE);
            holder.retryButton.setVisibility(View.GONE);
        } else {
            holder.progressBarLay.setVisibility(View.GONE);
            holder.removeCheckbox.setVisibility(View.VISIBLE);
            holder.retryButton.setVisibility(View.GONE);
        }

        Glide.with(mContext)
                .load(new File(image.getImagePath()))
                .apply(requestOptions).thumbnail(0.5f)
                .into(holder.imageView);

        holder.addCaptionText.setText(image.getChildCaption());

        holder.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add retry code
                itemListener.uploadImage(mainPosition + "_" + position, image);
            }
        });

        holder.removeCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onRemoveClick(mainPosition, position);
            }
        });

        if (image.getParentCaption().equals(INVESTIGATIONS)) {
            holder.scroller.setVisibility(View.VISIBLE);
            holder.addCaptionLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onAddCaptionClick(mainPosition, position);
                }
            });
        } else
            holder.scroller.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public interface ItemListener {
        void onRemoveClick(int mainPosition, int position);
        void onAddCaptionClick(int mainPosition, int position);
        void uploadImage(String uploadId, Image image);
    }
}