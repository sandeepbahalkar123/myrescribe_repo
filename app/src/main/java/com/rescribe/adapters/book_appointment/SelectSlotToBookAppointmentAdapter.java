package com.rescribe.adapters.book_appointment;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.book_appointment.select_slot_book_appointment.SlotList;

import java.util.ArrayList;

import droidninja.filepicker.utils.GridSpacingItemDecoration;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by jeetal on 31/10/17.
 */

public class SelectSlotToBookAppointmentAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<SlotList> expandableListTitle;
    private ShowTimingsBookAppointmentDoctor mShowTimingsBookAppointmentDoctor;


    public SelectSlotToBookAppointmentAdapter(Context context, ArrayList<SlotList> expandableListTitle) {
        this.mContext = context;
        this.expandableListTitle = expandableListTitle;

    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ArrayList<String> childListOfTimings = expandableListTitle.get(listPosition).getSlotTimingsList();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.select_slot_recycler_child_layout, null);
        }
        RecyclerView slotRecyclerView = (RecyclerView) convertView.findViewById(R.id.slotRecyclerView);
        mShowTimingsBookAppointmentDoctor = new ShowTimingsBookAppointmentDoctor(mContext, expandableListTitle.get(listPosition).getSlotTimingsList());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 4);
        slotRecyclerView.setLayoutManager(layoutManager);
        slotRecyclerView.setItemAnimator(new DefaultItemAnimator());
        /*int spanCount = 3; // 3 columns
        int spacing = 30; // 50px
        boolean includeEdge = true;
        slotRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));*/
        slotRecyclerView.setAdapter(mShowTimingsBookAppointmentDoctor);

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final SlotList slotList = expandableListTitle.get(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.book_appointment_select_slot_groupitem, null);
        }
        TextView slotName = (TextView) convertView.findViewById(R.id.slotName);
        TextView slotTime = (TextView) convertView.findViewById(R.id.slotTime);
        ImageView slotImage = (ImageView) convertView.findViewById(R.id.slotImage);
        if (slotList.getSlotName().equals("Morning")) {
            slotImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.breakfast_normal));
        }
        if (slotList.getSlotName().equals("Afternoon")) {
            slotImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.lunch_highlighted));
        }
        if (slotList.getSlotName().equals("Evening")) {
            slotImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.snacks));
        }
        if (slotList.getSlotName().equals("Night")) {
            slotImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.night_dinner));
        }
        slotTime.setText(slotList.getSlotTime());
        slotName.setText(slotList.getSlotName());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
