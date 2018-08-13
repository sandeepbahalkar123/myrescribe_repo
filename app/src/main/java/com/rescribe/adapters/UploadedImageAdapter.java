package com.rescribe.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.model.investigation.Image;
import com.rescribe.ui.activities.zoom_images.ZoomImageViewActivity;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rescribe.ui.activities.ChatActivity.RESCRIBE_FILES;

public class UploadedImageAdapter extends RecyclerView.Adapter<UploadedImageAdapter.FileViewHolder> {

    private static final String TAG = "UploadedImageAdapter";
    private final ArrayList<Image> paths;
    private final Context context;
    private final String filesFolder;
    private int imageSize;

    public UploadedImageAdapter(Context context, ArrayList<Image> paths) {
        this.context = context;
        this.paths = paths;
        setColumnNumber(context, 2);

        File sdCard = Environment.getExternalStorageDirectory();
        filesFolder = sdCard.getAbsolutePath() + RESCRIBE_FILES;
        File dirFilesFolder = new File(filesFolder);
        if (!dirFilesFolder.exists()) {
            if (dirFilesFolder.mkdirs()) {
                Log.i(TAG, filesFolder + " Directory Created");
            }
        }
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

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(final UploadedImageAdapter.FileViewHolder holder, final int position) {
        final Image path = paths.get(position);

        if (CommonMethods.isDoc(CommonMethods.getExtension(path.getImagePath())))
            holder.imageView.setImageResource(R.drawable.ic_file);
        else {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.centerCrop();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.skipMemoryCache(true);
            requestOptions.dontAnimate();
            requestOptions.override(imageSize, imageSize);
            requestOptions.placeholder(droidninja.filepicker.R.drawable.image_placeholder);

            Glide.with(context)
                    .load(new File(path.getImagePath()))
                    .apply(requestOptions).thumbnail(0.5f)
                    .into(holder.imageView);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonMethods.isDoc(CommonMethods.getExtension(path.getImagePath()))) {
                    // open document
                    CommonMethods.openDoc(context, path.getImagePath(), filesFolder);
                } else {
                    Intent intent = new Intent(context, ZoomImageViewActivity.class);
                    intent.putExtra(RescribeConstants.DOCUMENTS, path.getImagePath());
                    context.startActivity(intent);
                }
            }
        });

        holder.selectCheckbox.setChecked(paths.get(position).isSelected());
        holder.selectCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paths.get(position).setSelected(holder.selectCheckbox.isChecked());
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
