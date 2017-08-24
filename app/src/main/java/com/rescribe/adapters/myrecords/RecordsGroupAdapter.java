package com.rescribe.adapters.myrecords;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.myrecords.Group;

import java.util.List;

public class RecordsGroupAdapter extends RecyclerView.Adapter<RecordsGroupAdapter.MyViewHolder> {

    private final Context context;
    private List<Group> groups;

    class MyViewHolder extends RecyclerView.ViewHolder {
        RecyclerView gridView;
        TextView groupName;
        RecordsGroupImageAdapter mAdapter;

        MyViewHolder(View view) {
            super(view);

            gridView = (RecyclerView) view.findViewById(R.id.gridView);
            groupName = (TextView) view.findViewById(R.id.groupName);

            RecyclerView.ItemAnimator animator = gridView.getItemAnimator();
            if (animator instanceof SimpleItemAnimator)
                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(), 2);
            gridView.setLayoutManager(mLayoutManager);
            gridView.setHasFixedSize(true);
        }
    }


    public RecordsGroupAdapter(List<Group> patients, Context context) {
        this.groups = patients;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.records_group_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Group group = groups.get(position);

        holder.mAdapter = new RecordsGroupImageAdapter(group.getImages(), context);
        holder.gridView.setAdapter(holder.mAdapter);
        holder.mAdapter.setMainPosition(position);
        holder.groupName.setText(group.getGroupname());
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}