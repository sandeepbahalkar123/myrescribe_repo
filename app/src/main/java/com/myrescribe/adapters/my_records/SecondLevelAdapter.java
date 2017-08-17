package com.myrescribe.adapters.my_records;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.my_records.MyRecordReports;
import com.myrescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SecondLevelAdapter extends BaseExpandableListAdapter {

    private final int mColor;
    private ArrayList<MyRecordReports> mChildOriginalList;
    private ArrayList<MyRecordReports> mChildListDataHeader;
    private HashMap<MyRecordReports, ArrayList<MyRecordReports.MyRecordReportList>> mChildListDataChild;

    private Context context;

    public SecondLevelAdapter(Context context, ArrayList<MyRecordReports> mOriginalList, int color) {
        this.context = context;
        this.mChildOriginalList = mOriginalList;
        this.mColor = color;

        this.mChildListDataHeader = new ArrayList<>();
        this.mChildListDataChild = new HashMap<>();

        for (MyRecordReports dataObject :
                mOriginalList) {
            mChildListDataHeader.add(dataObject);

            ArrayList<MyRecordReports.MyRecordReportList> reportList = dataObject.getReportList();

            mChildListDataChild.put(dataObject, reportList);
        }
    }

    @Override
    public int getGroupCount() {
        return mChildListDataHeader.size();
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public MyRecordReports getGroup(int groupPosition) {
        return mChildListDataHeader.get(groupPosition);
    }

    @Override
    public MyRecordReports.MyRecordReportList getChild(int groupPosition, int childPosition) {
        return this.mChildListDataChild.get(this.mChildListDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final ChildGroupViewHolder childGroupViewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_my_record_second_level, parent, false);

            childGroupViewHolder = new ChildGroupViewHolder(convertView);
            convertView.setTag(childGroupViewHolder);
        } else {
            childGroupViewHolder = (ChildGroupViewHolder) convertView.getTag();
        }

        MyRecordReports group = getGroup(groupPosition);

        childGroupViewHolder.headerName.setText(group.getParentCaptionName());
        if (isExpanded) {
            childGroupViewHolder.headerName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.up_arrow, 0, 0, 0);
        } else {
            childGroupViewHolder.headerName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0);
        }
        childGroupViewHolder.headerName.setCompoundDrawablePadding(Math.round(context.getResources().getDimension(R.dimen.dp4)));
        childGroupViewHolder.childSideBarView.setBackgroundColor(mColor);

        return convertView;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final ChildViewHolder childViewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_my_record_third_level, parent, false);

            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        MyRecordReports.MyRecordReportList child = getChild(groupPosition, childPosition);

        childViewHolder.childContent.setText(child.getChildCaptionName());
        childViewHolder.childSideBarView.setBackgroundColor(mColor);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // no idea why this code is working
        //   return 1;
        return this.mChildListDataChild.get(this.mChildListDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ChildGroupViewHolder {
        //---------

        @BindView(R.id.headerName)
        CustomTextView headerName;

        @BindView(R.id.childSideBarView)
        TextView childSideBarView;

        ChildGroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        //---------

        @BindView(R.id.childContent)
        CustomTextView childContent;
        @BindView(R.id.cardDetailsBullet)
        ImageView cardDetailsBullet;
        @BindView(R.id.childSideBarView)
        TextView childSideBarView;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}