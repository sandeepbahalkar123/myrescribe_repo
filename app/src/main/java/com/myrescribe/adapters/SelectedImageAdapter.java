package com.myrescribe.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.myrescribe.R;
import com.myrescribe.model.investigation.Image;
import com.myrescribe.ui.activities.ZoomImageViewActivity;
import com.myrescribe.util.MyRescribeConstants;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedImageAdapter extends RecyclerView.Adapter<SelectedImageAdapter.FileViewHolder> {

    private final ArrayList<Image> paths;
    private final Context context;
    private int imageSize;

    public SelectedImageAdapter(Context context, ArrayList<Image> paths) {
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
                .inflate(R.layout.selected_image_item_layout, parent, false);
        return new SelectedImageAdapter.FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SelectedImageAdapter.FileViewHolder holder, final int position) {
        final Image path = paths.get(position);
        Glide.with(context).load(new File(path.getImagePath()))
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
                intent.putExtra(MyRescribeConstants.DOCUMENTS, path.getImagePath());
                context.startActivity(intent);
            }
        });

        holder.removeCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paths.remove(position);
                notifyDataSetChanged();
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
        ImageView removeCheckbox;

        FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
