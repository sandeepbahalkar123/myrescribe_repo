package com.myrescribe.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.history.HistoryCommonDetails;
import com.myrescribe.model.prescription_response_model.PrescriptionData;
import com.myrescribe.util.CommonMethods;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowViewDetailsAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<HistoryCommonDetails> mHistoryCommonDetailses;
    private ArrayList<String> mListDataHeader; // header titles

    // child data in format of header title, child title
    private HashMap<String, ArrayList<HistoryCommonDetails>> mListDataChild;

    public ShowViewDetailsAdapter(Context context, ArrayList<String> listDataHeader,
                                  HashMap<String, ArrayList<HistoryCommonDetails>> listChildData) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .get(childPosititon);
    }


    public ArrayList<HistoryCommonDetails> getChildList(int groupPosition){
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition));
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

        final HistoryCommonDetails childObject = (HistoryCommonDetails) getChild(groupPosition, childPosition);
        childViewHolder.txtListChild.setText(childObject.getName());

  /*      childViewHolder.mExpandVisitDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (childViewHolder.mExpandVisitDetailsLayout.getVisibility() == View.VISIBLE) {
                    childViewHolder.mExpandVisitDetailsLayout.setVisibility(View.GONE);
                    childObject.setExpanded(false);

                } else {
                    childViewHolder.mExpandVisitDetailsLayout.setVisibility(View.VISIBLE);
                    childObject.setExpanded(true);
                }
                notifyDataSetChanged();
            }
        });*/
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
        int i = 0;
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
        if(isExpanded){
            groupViewHolder.mDetailFirstPoint.setVisibility(View.GONE);
            groupViewHolder.mHeadergroupDivider.setVisibility(View.VISIBLE);
            groupViewHolder.mDivider.setVisibility(View.GONE);

        }else{
            groupViewHolder.mDetailFirstPoint.setVisibility(View.VISIBLE);
            groupViewHolder.mHeadergroupDivider.setVisibility(View.GONE);
            groupViewHolder.mDivider.setVisibility(View.VISIBLE);
        }
        groupViewHolder.lblListHeader.setText(headerTitle);
        groupViewHolder.mViewDetailIcon.setImageResource(CommonMethods.getVisitDetailsIcons(headerTitle,mContext));
        ArrayList<HistoryCommonDetails> historyCommonDetailses = getChildList(groupPosition);
        groupViewHolder.mDetailFirstPoint.setText(historyCommonDetailses.get(0).getName());
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

        @BindView(R.id.expandVisitDetailsLayout)
        LinearLayout mExpandVisitDetailsLayout;


        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class GroupViewHolder {
        //---------

        @BindView(R.id.viewDetailHeaderLabel)
        TextView lblListHeader;
        @BindView(R.id.viewDetailIcon)
        ImageView mViewDetailIcon;
        @BindView(R.id.headergroupDivider)
        View mHeadergroupDivider;
        @BindView(R.id.adapter_divider_top)
        View mDivider;


        @BindView(R.id.detailFirstPoint)
        TextView mDetailFirstPoint;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}