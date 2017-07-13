/*
package com.myrescribe.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.Album;
import com.myrescribe.model.history.HistoryCommonDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsExpandableListAdapter extends BaseExpandableListAdapter {

    // 4 Child types
    private Context mContext;
    private AlbumsAdapter adapter;
    private List<Album> albumList;
    private ArrayList<HistoryCommonDetails> mHistoryCommonDetailses;
    private ArrayList<String> mListDataHeader; // header titles
    private static final String CHILD_TYPE_1 = "Vitals";



    // 3 Group types
    private static final int GROUP_TYPE_1 = 0;
    private static final int GROUP_TYPE_2 = 1;


    private Activity context;
    private Map<String, List<String>> comments_feed_collection;
    private List<String> group_list;
    private HashMap<String, ArrayList<HistoryCommonDetails>> mListDataChild;
    public CommentsExpandableListAdapter(Context context, ArrayList<String> listDataHeader,
                                         HashMap<String, ArrayList<HistoryCommonDetails>> listChildData) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .get(childPosition);
    }
    public ArrayList<HistoryCommonDetails> getChildList(int groupPosition){
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition));
    }
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String incoming_text = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        int childType = getChildType(groupPosition, childPosition);
         String headerName = getGroupName(groupPosition);
        // We need to create a new "cell container"
        if (convertView == null || convertView.getTag() != childType) {
            switch (headerName) {
                case CHILD_TYPE_1:
                        convertView = inflater.inflate(R.layout.vitals_layout, null);
                        convertView.setTag(childType);
                    break;
                default:
                         convertView = inflater.inflate(R.layout.history_child_item_layout, null);
                         convertView.setTag(childType);
                    break;
            }
        }
        // We'll reuse the existing one
        else {
            // There is nothing to do here really we just need to set the content of view which we do in both cases
        }

        switch (headerName) {


            case CHILD_TYPE_1:
                RecyclerView recyclerView = (RecyclerView)convertView.findViewById(R.id.recycler_view)   ;
                adapter = new AlbumsAdapter(mContext,albumList);
                recyclerView.setAdapter(adapter);
                break;
            default:
                 final HistoryCommonDetails childObject = (HistoryCommonDetails) getChild(groupPosition, childPosition);
                TextView txtListChild = (TextView) convertView.findViewById(R.id.textView_name);
                  txtListChild.setText(childObject.getName());
                  break;
        }

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .size();
    }

    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }
    public int getGroupCount() {
        return this.mListDataHeader.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final String incoming_text = (String) getGroup(groupPosition);

        int groupType = getGroupType(groupPosition);

        // We need to create a new "cell container"
        if (convertView == null || convertView.getTag() != groupType) {
            switch (groupType) {
                case GROUP_TYPE_1 :
                    convertView = inflater.inflate(R.layout.histroy_group_item_layout, null);
                    break;
                case GROUP_TYPE_2:
                    // Am using the same layout cause am lasy and don't wanna create other ones but theses should be different
                    // or the group type shouldnt exist
                    convertView = inflater.inflate(R.layout.histroy_group_item_layout, null);
                    break;
                default:
                    // Maybe we should implement a default behaviour but it should be ok we know there are 3 group types right?
                    break;
            }
        }
        // We'll reuse the existing one
        else {
            // There is nothing to do here really we just need to set the content of view which we do in both cases
        }

        switch (groupType) {
            case GROUP_TYPE_1 :
               // final HistoryCommonDetails childObject = (HistoryCommonDetails) getChild(groupPosition, childPosition);
                //TextView item = (TextView) convertView.findViewById(R.id.expandable_list_single_item_text_view_group);
               // item.setText(incoming_text);
                break;
            case GROUP_TYPE_2:
                //TODO: Define how to render the data on the GROUPE_TYPE_2 layout
                // Since i use the same layout as GROUPE_TYPE_1 i could do the same thing as above but i choose to do nothing
                break;
            default:
                // Maybe we should implement a default behaviour but it should be ok we know there are 3 group types right?
                break;
        }

        return convertView;
    }






































    public boolean hasStableIds() {
        return true;
    }




    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    @Override
    public int getChildTypeCount() {
        return 2; // I defined 4 child types (CHILD_TYPE_1, CHILD_TYPE_2, CHILD_TYPE_3, CHILD_TYPE_UNDEFINED)
    }

    @Override
    public int getGroupTypeCount() {
        return 1; // I defined 3 groups types (GROUP_TYPE_1, GROUP_TYPE_2, GROUP_TYPE_3)
    }

    @Override
    public int getGroupType(int groupPosition) {
        switch (groupPosition) {
            case 0:
                return GROUP_TYPE_1;
            default:
                return GROUP_TYPE_1;
        }
    }
        public String getGroupName(int groupPosition){
          String headerTitle = (String) getGroup(groupPosition);
            return   headerTitle;
        }
    @Override
    public int getChildType(int groupPosition, int childPosition) {
        switch (groupPosition) {
            case 0:
                switch (childPosition) {
                    case 0:
                        return CHILD_TYPE_1;
                    case 1:
                        return CHILD_TYPE_UNDEFINED;
                    case 2:
                        return CHILD_TYPE_UNDEFINED;
                }
                break;
            case 1:
                switch (childPosition) {
                    case 0:
                        return CHILD_TYPE_2;
                    case 1:
                        return CHILD_TYPE_3;
                    case 2:
                        return CHILD_TYPE_3;
                }
                break;
            default:
                return CHILD_TYPE_UNDEFINED;
        }

        return CHILD_TYPE_UNDEFINED;
    }
}*/
