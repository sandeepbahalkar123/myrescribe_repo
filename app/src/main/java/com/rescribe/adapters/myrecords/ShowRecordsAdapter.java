package com.rescribe.adapters.myrecords;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.util.CommonMethods;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowRecordsAdapter extends RecyclerView.Adapter<ShowRecordsAdapter.FileViewHolder> {

    private final String[] paths;
    private final Context context;
    private int imageSize;

    public ShowRecordsAdapter(Context context, String[] paths) {
        this.context = context;
        this.paths = paths;
        setColumnNumber(context, 2);
    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = (widthPixels / columnNum) - CommonMethods.convertDpToPixel(30);
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.show_records_item_layout, parent, false);
        return new ShowRecordsAdapter.FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShowRecordsAdapter.FileViewHolder holder, final int position) {
        final String image = paths[position];

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.override(imageSize, imageSize);
        requestOptions.placeholder(droidninja.filepicker.R.drawable.image_placeholder);

        Glide.with(context)
                .load(new File(image))
                .apply(requestOptions).thumbnail(0.5f)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return paths.length;
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_photo)
        ImageView imageView;

        FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
