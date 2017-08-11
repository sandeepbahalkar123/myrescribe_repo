package com.myrescribe.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.myrescribe.R;
import com.myrescribe.model.investigation.Image;
import com.myrescribe.util.CommonMethods;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedRecordsAdapter extends RecyclerView.Adapter<SelectedRecordsAdapter.FileViewHolder> {

    private final ArrayList<Image> paths;
    private final Context context;
    private int imageSize;

    public SelectedRecordsAdapter(Context context, ArrayList<Image> paths) {
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
                .inflate(R.layout.selected_records_item_layout, parent, false);
        return new SelectedRecordsAdapter.FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SelectedRecordsAdapter.FileViewHolder holder, final int position) {
        final Image image = paths.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        requestOptions.dontAnimate();
        requestOptions.override(imageSize, imageSize);
        requestOptions.placeholder(droidninja.filepicker.R.drawable.image_placeholder);

        Glide.with(context)
                .load(new File(image.getImagePath()))
                .apply(requestOptions).thumbnail(0.5f)
                .into(holder.imageView);

        if (image.isSelected()) {
            holder.itemLayout.setAlpha(0.5f);
        } else {
            holder.itemLayout.setAlpha(1f);
        }

        holder.addCaptionText.setText(image.getCaption());

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image.isSelected())
                    image.setSelected(false);
                else
                    image.setSelected(true);

                notifyItemChanged(position);
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

        @BindView(R.id.item_layout)
        RelativeLayout itemLayout;

        @BindView(R.id.iv_photo)
        ImageView imageView;

        @BindView(R.id.addCaptionText)
        TextView addCaptionText;

        @BindView(R.id.removeCheckbox)
        ImageView removeCheckbox;

        FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
