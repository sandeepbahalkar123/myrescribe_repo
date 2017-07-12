package com.myrescribe.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.myrescribe.R;
import com.myrescribe.ui.activities.ZoomImageViewActivity;
import com.myrescribe.util.MyRescribeConstants;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UploadedImageAdapter extends RecyclerView.Adapter<UploadedImageAdapter.FileViewHolder> {

    private final ArrayList<String> paths;
    private final Context context;
    private int imageSize;

    public UploadedImageAdapter(Context context, ArrayList<String> paths) {
        this.context = context;
        this.paths = paths;
        setColumnNumber(context, 2);
    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = widthPixels / columnNum;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.uploded_image_item_layout, parent, false);
        return new UploadedImageAdapter.FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UploadedImageAdapter.FileViewHolder holder, final int position) {
        final String path = paths.get(position);
        Glide.with(context).load(new File(path))
                .centerCrop()
                .dontAnimate()
                .thumbnail(0.5f)
                .override(imageSize, imageSize)
                .placeholder(droidninja.filepicker.R.drawable.image_placeholder)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ZoomImageViewActivity.class);
                intent.putExtra(MyRescribeConstants.DOCUMENTS, path);
                context.startActivity(intent);
            }
        });

        holder.selectCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_photo)
        ImageView imageView;

        @BindView(R.id.removeCheckbox)
        CheckBox selectCheckbox;

        FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
