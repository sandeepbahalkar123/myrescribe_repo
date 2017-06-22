package com.myrescribe.adapters;

import java.util.HashMap;
import java.util.List;
 
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.myrescribe.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowHistoryListAdapter extends BaseExpandableListAdapter {
 
    private Context mContext;
    private List<String> mListDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> mListDataChild;
 
    public ShowHistoryListAdapter(Context context, List<String> listDataHeader,
                                  HashMap<String, List<String>> listChildData) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .get(childPosititon);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.history_child_item_layout, parent, false);

            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        final String childText = (String) getChild(groupPosition, childPosition);
        childViewHolder.txtListChild.setText(childText);

        if (isLastChild) {
            childViewHolder.mDividerLine.setVisibility(View.VISIBLE);
        } else {
            childViewHolder.mDividerLine.setVisibility(View.GONE);
        }
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .size();
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this.mListDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        final GroupViewHolder groupViewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.histroy_group_item_layout, parent, false);

            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        String headerTitle = (String) getGroup(groupPosition);

        groupViewHolder.lblListHeader.setText(headerTitle);
 
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ChildViewHolder {
        //---------

        @BindView(R.id.textView_name)
        TextView txtListChild;

        @BindView(R.id.adapter_divider_bottom)
        View mDividerLine;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class GroupViewHolder {
        //---------

        @BindView(R.id.headerLabel)
        TextView lblListHeader;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}