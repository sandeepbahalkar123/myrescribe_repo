package com.heinrichreimersoftware.materialdrawer.bottom_menu;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.heinrichreimersoftware.materialdrawer.R;

import java.util.ArrayList;

public class BottomMenuAdapter extends RecyclerView.Adapter<BottomMenuAdapter.ListViewHolder> {

    private onBottomMenuClickListener mBottomMenuListClickListener;
    private ArrayList<BottomMenu> bottomMenus;

    BottomMenuAdapter(Context mContext, ArrayList<BottomMenu> bottomMenus) {
        this.bottomMenus = bottomMenus;

        try {
            this.mBottomMenuListClickListener = ((onBottomMenuClickListener) mContext);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement onBottomMenuListClickListener.");
        }
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bottom_menu_list_item, parent, false);

        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        int width = (widthPixels * 20) / 100;
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.width = width;

        itemView.setLayoutParams(layoutParams);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, final int position) {
        final BottomMenu bottomMenu = bottomMenus.get(position);
        holder.bottomMenuName.setText(bottomMenu.getMenuName());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomMenuListClickListener.onBottomMenuClick(bottomMenu);
            }
        });

        if (bottomMenu.isAppIcon()) {
            holder.bottomMenuName.setVisibility(View.GONE);
            holder.bottomMenuTab.setVisibility(View.INVISIBLE);
        } else {
            if (bottomMenu.isSelected())
                holder.bottomMenuTab.setVisibility(View.VISIBLE);
            else
                holder.bottomMenuTab.setVisibility(View.INVISIBLE);
        }

        RequestOptions options = new RequestOptions()
                .centerInside()
                .priority(Priority.HIGH);

        Glide.with(holder.menuBottomIcon.getContext())
                .load(bottomMenu.getMenuIcon()).apply(options)
                .into(holder.menuBottomIcon);
    }

    @Override
    public int getItemCount() {
        return bottomMenus.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView menuBottomIcon;
        TextView bottomMenuName;
        ImageView bottomMenuTab;

        View view;

        ListViewHolder(View view) {
            super(view);
            this.view = view;
            bottomMenuName = (TextView) view.findViewById(R.id.bottomMenuName);
            menuBottomIcon = (ImageView) view.findViewById(R.id.menuBottomIcon);
            bottomMenuTab = (ImageView) view.findViewById(R.id.bottomMenuTab);
        }
    }

    public interface onBottomMenuClickListener {
        void onBottomMenuClick(BottomMenu bottomMenu);
    }
}
