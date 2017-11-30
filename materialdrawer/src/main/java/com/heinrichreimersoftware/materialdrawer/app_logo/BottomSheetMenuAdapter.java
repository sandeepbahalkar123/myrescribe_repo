package com.heinrichreimersoftware.materialdrawer.app_logo;

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

/**
 * Created by jeetal on 29/11/17.
 */

public class BottomSheetMenuAdapter extends RecyclerView.Adapter<BottomSheetMenuAdapter.ListViewHolder> {

    private onBottomSheetMenuClickListener mBottomMenuListClickListener;
    private ArrayList<ClickOption> bottomMenus;

    public BottomSheetMenuAdapter(Context mContext, ArrayList<ClickOption> bottomMenus) {
        this.bottomMenus = bottomMenus;

        try {
            this.mBottomMenuListClickListener = ((onBottomSheetMenuClickListener) mContext);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement onBottomSheetMenuListClickListener.");
        }
    }

    @Override
    public BottomSheetMenuAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bottom_sheet_menu_item_list, parent, false);

        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        int width = (widthPixels * 20) / 100;
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.width = width;

        itemView.setLayoutParams(layoutParams);

        return new BottomSheetMenuAdapter.ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BottomSheetMenuAdapter.ListViewHolder holder, final int position) {
        final ClickOption bottomMenu = bottomMenus.get(position);
       holder.bottomMenuName.setText(bottomMenu.getName());

        RequestOptions options = new RequestOptions()
                .centerInside()
                .priority(Priority.HIGH);

        Glide.with(holder.menuBottomIcon.getContext())
                .load(bottomMenu.getIconImageUrl()).apply(options)
                .into(holder.menuBottomIcon);
       /* holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomMenuListClickListener.onBottomMenuClick(bottomMenu);
            }
        });

        RequestOptions options = new RequestOptions()
                .centerInside()
                .priority(Priority.HIGH);

        Glide.with(holder.menuBottomIcon.getContext())
                .load(bottomMenu.getMenuIcon()).apply(options)
                .into(holder.menuBottomIcon);
//for app logo
        if (bottomMenu.isAppIcon()) {
            holder.bottomMenuName.setVisibility(View.GONE);
            holder.bottomMenuTab.setVisibility(View.GONE);
        } else {
            if (bottomMenu.isSelected()) {
                holder.bottomMenuTab.setVisibility(View.VISIBLE);
                holder.bottomMenuName.setTextColor(holder.bottomMenuName.getContext().getResources().getColor(R.color.tagColor));
                holder.menuBottomIcon.setColorFilter(ContextCompat.getColor(holder.menuBottomIcon.getContext(), R.color.tagColor), android.graphics.PorterDuff.Mode.MULTIPLY);
            } else {
                holder.bottomMenuTab.setVisibility(View.INVISIBLE);
                holder.bottomMenuName.setTextColor(holder.bottomMenuName.getContext().getResources().getColor(R.color.grey));
                holder.menuBottomIcon.setColorFilter(ContextCompat.getColor(holder.menuBottomIcon.getContext(), R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        }*/
    }

    @Override
    public int getItemCount() {
        return bottomMenus.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView menuBottomIcon;
        TextView bottomMenuName;


        View view;

        ListViewHolder(View view) {
            super(view);
            this.view = view;
            bottomMenuName = (TextView) view.findViewById(R.id.menuName);
            menuBottomIcon = (ImageView) view.findViewById(R.id.menuImage);
        }
    }

    public interface onBottomSheetMenuClickListener {
        void onBottomSheetMenuClick(ClickOption bottomMenu);
    }
}
