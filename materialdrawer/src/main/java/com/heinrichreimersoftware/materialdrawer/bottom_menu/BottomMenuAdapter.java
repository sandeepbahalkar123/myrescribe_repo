package com.heinrichreimersoftware.materialdrawer.bottom_menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.heinrichreimersoftware.materialdrawer.R;
import com.heinrichreimersoftware.materialdrawer.app_logo.BottomSheetMenu;

import java.util.ArrayList;

public class BottomMenuAdapter extends RecyclerView.Adapter<BottomMenuAdapter.ListViewHolder> {

    private OnBottomMenuClickListener mBottomMenuListClickListener;
    private ArrayList<BottomMenu> bottomMenus;
    static int appIconIndex;
    static int connectIndex;
    static final String CONNECT = "Connect";

    BottomMenuAdapter(Context mContext, ArrayList<BottomMenu> bottomMenus) {
        this.bottomMenus = bottomMenus;

        try {
            this.mBottomMenuListClickListener = ((OnBottomMenuClickListener) mContext);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement onBottomMenuListClickListener.");
        }
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bottom_menu_item, parent, false);

        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        int width = (widthPixels * 20) / 100;
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.width = width;

        itemView.setLayoutParams(layoutParams);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final BottomMenu bottomMenu = bottomMenus.get(position);
        holder.bottomMenuName.setText(bottomMenu.getMenuName());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomMenuListClickListener.onBottomMenuClick(bottomMenu);
            }
        });

        holder.menuBottomIcon.setImageDrawable(bottomMenu.getMenuIcon());

//for app logo

        if (bottomMenu.isAppIcon()) {
            holder.bottomMenuName.setVisibility(View.GONE);
            holder.bottomMenuTab.setVisibility(View.GONE);
            holder.showConnectCountTextView.setVisibility(View.GONE);

            appIconIndex = position;
            if (bottomMenu.getNotificationCount() > 0) {
                holder.showCount.setText(String.valueOf(bottomMenu.getNotificationCount()));
                holder.showCount.setVisibility(View.VISIBLE);
            } else
                holder.showCount.setVisibility(View.GONE);

        } else {

            holder.showCount.setVisibility(View.GONE);

            if (bottomMenu.getMenuName().equalsIgnoreCase(CONNECT)) {

                connectIndex = position;
                if (bottomMenu.getNotificationCount() > 0) {
                    holder.showConnectCountTextView.setText(String.valueOf(bottomMenu.getNotificationCount()));
                    holder.showConnectCountTextView.setVisibility(View.VISIBLE);
                } else
                    holder.showConnectCountTextView.setVisibility(View.GONE);
            } else holder.showConnectCountTextView.setVisibility(View.GONE);

            if (bottomMenu.isSelected()) {
                holder.bottomMenuTab.setVisibility(View.VISIBLE);
                holder.bottomMenuName.setTextColor(holder.bottomMenuName.getContext().getResources().getColor(R.color.tagColor));
                holder.menuBottomIcon.setColorFilter(ContextCompat.getColor(holder.menuBottomIcon.getContext(), R.color.tagColor), android.graphics.PorterDuff.Mode.MULTIPLY);
            } else {
                holder.bottomMenuTab.setVisibility(View.INVISIBLE);
                holder.bottomMenuName.setTextColor(holder.bottomMenuName.getContext().getResources().getColor(R.color.grey));
                holder.menuBottomIcon.setColorFilter(ContextCompat.getColor(holder.menuBottomIcon.getContext(), R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        }

    }

    @Override
    public int getItemCount() {
        return bottomMenus.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView menuBottomIcon;
        TextView bottomMenuName;
        TextView showCount;
        TextView showConnectCountTextView;
        View spaceView;
        TextView bottomMenuTab;

        View view;

        ListViewHolder(View view) {
            super(view);
            this.view = view;
            bottomMenuName = (TextView) view.findViewById(R.id.bottomMenuName);
            menuBottomIcon = (ImageView) view.findViewById(R.id.menuBottomIcon);
            bottomMenuTab = (TextView) view.findViewById(R.id.bottomMenuTab);
            spaceView = (View) view.findViewById(R.id.spaceView);
            showCount = (TextView) view.findViewById(R.id.showCountTextView);
            showConnectCountTextView = (TextView) view.findViewById(R.id.showConnectCountTextView);
        }
    }

    public interface OnBottomMenuClickListener {
        void onBottomSheetMenuClick(BottomSheetMenu bottomMenu);
        void onBottomMenuClick(BottomMenu bottomMenu);
        void onProfileImageClick();
    }

}
