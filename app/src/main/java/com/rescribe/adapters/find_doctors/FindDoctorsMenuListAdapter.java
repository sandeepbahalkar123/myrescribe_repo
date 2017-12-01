package com.rescribe.adapters.find_doctors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.interfaces.dashboard_menu_click.IOnMenuClickListener;
import com.rescribe.model.dashboard_api.ClickOption;
import com.rescribe.ui.activities.find_doctors.FindDoctorsActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 24/11/17.
 */

public class FindDoctorsMenuListAdapter extends RecyclerView.Adapter<FindDoctorsMenuListAdapter.ListViewHolder> {

    private IOnMenuClickListener onMenuClickListener;
    private ArrayList<ClickOption> mClickOptionList;
    private Context mContext;

    public FindDoctorsMenuListAdapter(Context mContext, ArrayList<ClickOption> clickOptions, IOnMenuClickListener onMenuClickListener) {
        this.mContext = mContext;
        this.mClickOptionList = clickOptions;
        this.onMenuClickListener = onMenuClickListener;
    }

    @Override
    public FindDoctorsMenuListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.services_item_layout, parent, false);

        return new FindDoctorsMenuListAdapter.ListViewHolder(itemView);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(FindDoctorsMenuListAdapter.ListViewHolder holder, int position) {

        final ClickOption clickOption = mClickOptionList.get(position);
        holder.serviceNameTextView.setText(clickOption.getName());
        holder.recyclerViewClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenuClickListener.onMenuClick(clickOption);
            }
        });

        //------------
        if (clickOption.getIconImageUrl() != null) {
            int imageSizeToLoadImage = CommonMethods.getImageSizeToLoadImage(mContext, 2);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.dontAnimate();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.skipMemoryCache(true);
            requestOptions.override(imageSizeToLoadImage, imageSizeToLoadImage);

            Glide.with(mContext)
                    .load(clickOption.getIconImageUrl())
                    .apply(requestOptions).thumbnail(0.5f)
                    .into(holder.serviceIcon);
        }
        //--------------
    }

    @Override
    public int getItemCount() {
        return mClickOptionList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.serviceNameTextView)
        CustomTextView serviceNameTextView;
        @BindView(R.id.serviceIcon)
        ImageView serviceIcon;
        @BindView(R.id.recyclerViewClick)
        LinearLayout recyclerViewClick;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

}
