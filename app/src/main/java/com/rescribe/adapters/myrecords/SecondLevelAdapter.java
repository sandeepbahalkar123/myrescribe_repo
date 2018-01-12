package com.rescribe.adapters.myrecords;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.my_records.MyRecordReports;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SecondLevelAdapter extends BaseExpandableListAdapter {

    private final int mColor;
    private final String mInvestigationText;
    private int mBgColor;
    private ArrayList<MyRecordReports> mChildListDataHeader;
    private HashMap<MyRecordReports, ArrayList<MyRecordReports.MyRecordReportList>> mChildListDataChild;

    public SecondLevelAdapter(Context context, ArrayList<MyRecordReports> mOriginalList, int color, int bgColor) {
        this.mColor = color;
        this.mBgColor = bgColor;

        this.mChildListDataHeader = new ArrayList<>();
        this.mChildListDataChild = new HashMap<>();

        mInvestigationText = context.getString(R.string.investigation);

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
        if (group.getParentCaptionName().startsWith(mInvestigationText)) {
            childGroupViewHolder.secondLevelAttachmentIcon.setVisibility(View.GONE);
            childGroupViewHolder.secondLevelUpDownArrow.setVisibility(View.VISIBLE);

            if (isExpanded) {
                childGroupViewHolder.secondLevelUpDownArrow.setImageResource(R.drawable.spinner_icon_down);
            } else {
                childGroupViewHolder.secondLevelUpDownArrow.setImageResource(R.drawable.down_arrow);
            }
        } else {
            childGroupViewHolder.secondLevelUpDownArrow.setVisibility(View.INVISIBLE);
            childGroupViewHolder.secondLevelAttachmentIcon.setVisibility(View.VISIBLE);
        }
        childGroupViewHolder.childSideBarView.setBackgroundColor(mColor);
        childGroupViewHolder.clickOnDoctorVisitLinearLayout.setBackgroundColor(mBgColor);
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
        childViewHolder.childClickOnDoctorVisitLinearLayout.setBackgroundColor(mBgColor);

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
        @BindView(R.id.clickOnDoctorVisitLinearLayout)
        LinearLayout clickOnDoctorVisitLinearLayout;
        @BindView(R.id.secondLevelAttachmentIcon)
        ImageView secondLevelAttachmentIcon;
        @BindView(R.id.secondLevelUpDownArrow)
        ImageView secondLevelUpDownArrow;

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
        @BindView(R.id.clickOnDoctorVisitLinearLayout)
        LinearLayout childClickOnDoctorVisitLinearLayout;
        @BindView(R.id.cardDetailsBullet)
        ImageView cardDetailsBullet;
        @BindView(R.id.childSideBarView)
        TextView childSideBarView;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}