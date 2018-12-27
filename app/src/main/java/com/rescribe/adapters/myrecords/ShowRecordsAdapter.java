package com.rescribe.adapters.myrecords;

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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.model.my_records.MyRecordReports;
import com.rescribe.ui.activities.WebViewActivity;
import com.rescribe.ui.activities.zoom_images.ZoomImageViewActivity;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowRecordsAdapter extends RecyclerView.Adapter<ShowRecordsAdapter.FileViewHolder> {

    // private final String[] paths;
    private  ArrayList<MyRecordReports.ImageListData> imageListData;
    private final Context context;
    private final String caption;
    private int imageSize;
    private boolean mShowDeleteCheckbox = false;
    private OnRecordsListener onRecordsListener;
    // private HashSet<DeleteRecordModel> mSelectedRecordToDelete = new HashSet<>();

    public ShowRecordsAdapter(Context context, ArrayList<MyRecordReports.ImageListData> imageListData, String caption, OnRecordsListener onRecordsListener) {
        this.context = context;
        this.imageListData = imageListData;
        this.caption = caption;
        this.onRecordsListener = onRecordsListener;
        setColumnNumber(context, 2);
    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = (widthPixels / columnNum) - context.getResources().getDimensionPixelSize(R.dimen.dp30);
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.show_records_item_layout, parent, false);
        return new ShowRecordsAdapter.FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShowRecordsAdapter.FileViewHolder holder, final int position) {


        final String urlString = imageListData.get(position).getImageUrl();

        String fileExtension = urlString.substring(urlString.lastIndexOf("."));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
       // requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
       // requestOptions.skipMemoryCache(true);
        requestOptions.override(imageSize, imageSize);
        requestOptions.placeholder(CommonMethods.getDocumentIconByExtension(fileExtension));
        requestOptions.error(CommonMethods.getDocumentIconByExtension(fileExtension));

        Glide.with(context)
                .load(urlString)
                .apply(requestOptions).thumbnail(0.5f)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = "" + urlString;
                String fileExtension = tag.substring(tag.lastIndexOf("."));

                if (fileExtension.contains(".doc") || fileExtension.contains(".odt") || fileExtension.contains(".ppt") || fileExtension.contains(".odp") || fileExtension.contains(".xls") || fileExtension.contains(".ods") || fileExtension.contains(".pdf")) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra(context.getString(R.string.title_activity_selected_docs), urlString);
                    intent.putExtra(context.getString(R.string.file_extension), fileExtension);
                    context.startActivity(intent);
                } else {
                    // do stuff here
                    Intent intent = new Intent(context, ZoomImageViewActivity.class);
                    intent.putExtra(RescribeConstants.DOCUMENTS, urlString);
                    intent.putExtra(RescribeConstants.IS_URL, true);
                    context.startActivity(intent);
                }

            }
        });

        holder.removeCheckbox.setChecked(imageListData.get(position).isChecked());
        if (mShowDeleteCheckbox)
            holder.removeCheckbox.setVisibility(View.VISIBLE);
         else
            holder.removeCheckbox.setVisibility(View.GONE);

        holder.removeCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox c = (CheckBox) v;
                 imageListData.get(position).setChecked(c.isChecked());
            }
        });


        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mShowDeleteCheckbox = !mShowDeleteCheckbox;
                onRecordsListener.showDeleteButton(mShowDeleteCheckbox);
                if (!mShowDeleteCheckbox) {
                    for (MyRecordReports.ImageListData imageList : imageListData) {
                        imageList.setChecked(false);
                    }
                }
                notifyDataSetChanged();
                return false;
            }
        });


        holder.addCaptionText.setText(imageListData.size() > 1 ? caption + "_" + (position + 1) : caption);
    }

    @Override
    public int getItemCount() {
        return imageListData.size();
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_photo)
        ImageView imageView;
        @BindView(R.id.addCaptionText)
        TextView addCaptionText;
        @BindView(R.id.removeCheckbox)
        CheckBox removeCheckbox;

        FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface OnRecordsListener {
        void showDeleteButton(boolean isShowButton);
    }


    public boolean isEmpty(){
        if (imageListData.size()==0)
            return true;
        else
            return false;
    }
    public void RemoveDeletedRecord() {

        ArrayList< MyRecordReports.ImageListData> imageListDataArrayList = new ArrayList<>();
        for (int i = 0; i <imageListData.size(); i++) {
            MyRecordReports.ImageListData listData = imageListData.get(i);
            if (listData.isChecked()) {
                imageListDataArrayList.add(listData);
            }
        }
        imageListData.removeAll(imageListDataArrayList);
        mShowDeleteCheckbox = false;
        notifyDataSetChanged();

    }


}
