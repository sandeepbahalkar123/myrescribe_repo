package com.rescribe.adapters.doctor_visit_attachmnt_adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.model.case_details.VisitCommonData;
import com.rescribe.model.filter.DoctorSpecialityData;
import com.rescribe.ui.activities.zoom_images.MultipleImageWithSwipeAndZoomActivity;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoctorAttachListAdapter extends RecyclerView.Adapter<DoctorAttachListAdapter.FileViewHolder> {

    private final ArrayList<String> listData;
    private final Context context;

    public DoctorAttachListAdapter(Context context, ArrayList<String> listData) {
        this.context = context;
        this.listData = listData;

    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.image_view, parent, false);
        return new DoctorAttachListAdapter.FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DoctorAttachListAdapter.FileViewHolder holder, final int position) {


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.error(R.drawable.ic_file);
        requestOptions.placeholder(R.drawable.ic_file);


        String url = listData.get(position);
        Glide.with(context)
                .load(url)
                .apply(requestOptions).thumbnail(0.5f)
                .into(holder.attachmentImage);


        holder.attachmentImage.setTag(url);

        holder.attachmentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do stuff here

                String tag = "" + v.getTag();
                ArrayList<VisitCommonData> list = new ArrayList<>();
                for (String data :
                        listData) {
                    VisitCommonData vData = new VisitCommonData();
                    vData.setUrl(data);
                    list.add(vData);
                }

                Intent intent = new Intent(context, MultipleImageWithSwipeAndZoomActivity.class);
                intent.putExtra(RescribeConstants.DOCUMENTS, tag);
                intent.putExtra(RescribeConstants.IS_URL, true);
                intent.putParcelableArrayListExtra(RescribeConstants.ATTACHMENTS_LIST, list);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.attachmentImage)
        ImageView attachmentImage;


        View rowView;

        FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rowView = itemView;
        }
    }

}
