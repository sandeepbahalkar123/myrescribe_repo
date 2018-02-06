package com.heinrichreimersoftware.materialdrawer.app_logo;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.heinrichreimersoftware.materialdrawer.R;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeetal on 29/11/17.
 */

public class BottomSheetMenuAdapter extends RecyclerView.Adapter<BottomSheetMenuAdapter.ListViewHolder> {

    private onBottomSheetMenuClickListener mBottomMenuListClickListener;
    private ArrayList<BottomSheetMenu> bottomMenus;
    private int mPosition;
    private Context mContext;

    public BottomSheetMenuAdapter(Context mContext, ArrayList<BottomSheetMenu> bottomMenus) {
        this.bottomMenus = bottomMenus;
        this.mContext = mContext;

        try {
            this.mBottomMenuListClickListener = ((onBottomSheetMenuClickListener) mContext);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement onBottomSheetMenuListClickListener.");
        }
    }

    @Override
    public BottomSheetMenuAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item, parent, false);

        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        int width = (widthPixels * 20) / 100;
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.width = width;

        itemView.setLayoutParams(layoutParams);

        return new BottomSheetMenuAdapter.ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BottomSheetMenuAdapter.ListViewHolder holder, final int position) {
        final BottomSheetMenu bottomMenu = bottomMenus.get(position);
        holder.tableLayout.removeAllViews();

        mPosition = 0;
        List<BottomSheetMenu> bottomSheetMenus = new ArrayList<>();
        int size = bottomMenus.size();
        int count = 1;
        int tempSize = size - (size % 3);
        for (int i = 0; i < size; i++) {
            bottomSheetMenus.add(bottomMenus.get(position));
            if (tempSize > i) {
                if (count == 3) {
                    holder.tableLayout.addView(addTableRow(bottomSheetMenus, position));
                    bottomSheetMenus.clear();
                    count = 1;
                } else
                    count++;
            } else if (count == size % 3) {
                holder.tableLayout.addView(addTableRow(bottomSheetMenus, position));
                bottomSheetMenus.clear();
                count = 1;
            } else count++;
        }

    }

    @Override
    public int getItemCount() {
        return bottomMenus.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
       /* ImageView menuBottomIcon;
        TextView bottomMenuName;
*/
       TableLayout tableLayout;

        View view;

        ListViewHolder(View view) {
            super(view);
            this.view = view;
           tableLayout = (TableLayout) view.findViewById(R.id.table);

        }
    }

    public interface onBottomSheetMenuClickListener {
        void onBottomSheetMenuClick(BottomSheetMenu bottomMenu);
    }
    private View addTableRow(final List<BottomSheetMenu> bottomSheetMenus, final int groupPosition) {
        int i;
        String categoryForBpMax = "";
        String categoryForBpMin = "";
        TableRow tableRow = new TableRow(mContext);
        for (i = 0; i < bottomSheetMenus.size(); i++) {
            View item = LayoutInflater.from(mContext)
                    .inflate(R.layout.bottom_sheet_menu_item_list, tableRow, false);
            TextView bottomMenuName = (TextView) item.findViewById(R.id.menuName);
            ImageView menuBottomIcon = (ImageView) item.findViewById(R.id.menuImage);

            final int finali = mPosition;
              bottomMenuName.setText(bottomSheetMenus.get(i).getName());

            RequestOptions options = new RequestOptions()
                    .centerInside()
                    .priority(Priority.HIGH);

            Glide.with(menuBottomIcon.getContext())
                    .load(bottomSheetMenus.get(i).getIconImageUrl()).apply(options)
                    .into(menuBottomIcon);
            tableRow.addView(item);
            mPosition++;
        }
        return tableRow;
    }

}
