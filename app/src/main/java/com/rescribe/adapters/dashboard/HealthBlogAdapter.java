package com.rescribe.adapters.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rescribe.R;
import com.rescribe.model.book_appointment.doctor_data.ReviewList;
import com.rescribe.model.dashboard.HealthBlogData;
import com.rescribe.ui.activities.HomePageActivity;
import com.rescribe.ui.activities.WebViewActivity;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 28/9/17.
 */

public class HealthBlogAdapter extends RecyclerView.Adapter<HealthBlogAdapter.ListViewHolder> {


    private ArrayList<HealthBlogData> mHealthBlogList;
    private Fragment mFragment;
    private Context mContext;
    Integer[] imageId = {
            R.drawable.diabetes_and_weightloss,
            R.drawable.preventing_treating,
            R.drawable.tips_to_getting_sound_sleep,
            R.drawable.myths_about_cancer

    };

    public HealthBlogAdapter(Context mContext, ArrayList<HealthBlogData> healthBlogList) {
        this.mContext = mContext;
        this.mHealthBlogList = healthBlogList;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.healthblog_item_dashboard, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        final HealthBlogData healthBlogData = mHealthBlogList.get(position);
        holder.diabetes.setImageResource(imageId[position]);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(mContext.getString(R.string.title_activity_selected_docs), healthBlogData.getHealthBlogImgUrl());
                intent.putExtra(mContext.getString(R.string.title), healthBlogData.getHealthBlogName());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.diabetes)
        ImageView diabetes;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public interface OnFilterDocListClickListener {
        void onClickOfDoctorRowItem(Bundle bundleData);
    }
}
