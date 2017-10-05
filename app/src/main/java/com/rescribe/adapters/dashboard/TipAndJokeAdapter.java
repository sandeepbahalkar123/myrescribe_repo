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
import com.rescribe.model.dashboard.HealthBlogData;
import com.rescribe.model.dashboard.TipAndJokData;
import com.rescribe.ui.activities.WebViewActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 28/9/17.
 */

public class TipAndJokeAdapter extends RecyclerView.Adapter<TipAndJokeAdapter.ListViewHolder> {


    private ArrayList<TipAndJokData> mTipAndJokDataList;
    private Context mContext;
    Integer[] imageId = {
            R.drawable.tip_of_the_day,
            R.drawable.joke_of_the_day
    };

    public TipAndJokeAdapter(Context mContext, ArrayList<TipAndJokData> list) {
        this.mContext = mContext;
        this.mTipAndJokDataList = list;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tip_and_joke_item_dashboard, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        TipAndJokData tipAndJokData = mTipAndJokDataList.get(position);

        holder.image.setImageResource(imageId[position]);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mTipAndJokDataList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

}
