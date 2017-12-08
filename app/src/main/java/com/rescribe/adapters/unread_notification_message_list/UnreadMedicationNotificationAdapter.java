package com.rescribe.adapters.unread_notification_message_list;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.model.dashboard_api.unread_notification_message_list.UnreadNotificationMessageData;
import com.rescribe.model.notification.Medication;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

// Eclipse wanted me to use a sparse array instead of my hashmaps, I just suppressed that suggestion
@SuppressLint("UseSparseArrays")
public class UnreadMedicationNotificationAdapter extends BaseExpandableListAdapter {

    // Define activity context
    private Context mContext;

    //---------
    private ArrayList<UnreadNotificationMessageData> mOriginalReceivedList;
    //--------

    /*
     * Here we have a Hashmap containing a String key
     * (can be Integer or other type but I was testing
     * with contacts so I used contact name as the key)
    */
    private HashMap<String, ArrayList<Medication>> mListDataChild;

    // ArrayList that is what each key in the above
    // hashmap points to
    private ArrayList<String> mListDataGroup;

    // Hashmap for keeping track of our checkbox check states
    private HashMap<Integer, boolean[]> mChildCheckStates;

    // Our getChildView & getGroupView use the viewholder patter
    // Here are the viewholders defined, the inner classes are
    // at the bottom
    private ChildViewHolder childViewHolder;
    private GroupViewHolder groupViewHolder;

    /*
          *  For the purpose of this document, I'm only using a single
     *	textview in the group (parent) and child, but you're limited only
     *	by your XML view for each group item :)
    */
    private String groupText;
    private Medication childText;

    /*  Here's the constructor we'll use to pass in our calling
     *  activity's context, group items, and child items
    */
    public UnreadMedicationNotificationAdapter(Context context, ArrayList<UnreadNotificationMessageData> dataArrayList) {

        mContext = context;

        this.mOriginalReceivedList = dataArrayList;

        mListDataGroup = new ArrayList<>();
        mListDataChild = new HashMap<>();
        addSingleElementToList();

        // Initialize our hashmap containing our check states here
        mChildCheckStates = new HashMap<Integer, boolean[]>();
    }

    @Override
    public int getGroupCount() {
        return mListDataGroup.size();
    }

    /*
     * This defaults to "public object getGroup" if you auto import the methods
     * I've always make a point to change it from "object" to whatever item
     * I passed through the constructor
    */
    @Override
    public String getGroup(int groupPosition) {
        return mListDataGroup.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        //  I passed a text string into an activity holding a getter/setter
        //  which I passed in through "ExpListGroupItems".
        //  Here is where I call the getter to get that text
        groupText = getGroup(groupPosition);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tablet_notification_item_header, null);

            // Initialize the GroupViewHolder defined at the bottom of this document
            groupViewHolder = new GroupViewHolder();

            groupViewHolder.mGroupText = (TextView) convertView.findViewById(R.id.tabNameTextView);

            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        SpannableString modifiedText = new SpannableString(groupText);
        modifiedText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.Gray)),
                0, 14,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        groupViewHolder.mGroupText.setText(modifiedText);

        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String s = mListDataGroup.get(groupPosition);
        int size = mListDataChild.get(s).size();
        return size;
    }

    /*
     * This defaults to "public object getChild" if you auto import the methods
     * I've always make a point to change it from "object" to whatever item
     * I passed through the constructor
    */
    @Override
    public Medication getChild(int groupPosition, int childPosition) {
        return mListDataChild.get(mListDataGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final int mGroupPosition = groupPosition;
        final int mChildPosition = childPosition;

        //  I passed a text string into an activity holding a getter/setter
        //  which I passed in through "ExpListChildItems".
        //  Here is where I call the getter to get that text
        childText = getChild(mGroupPosition, mChildPosition);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tablet_notification_item, null);

            childViewHolder = new ChildViewHolder();

            childViewHolder.tabNameTextView = (CustomTextView) convertView
                    .findViewById(R.id.tabNameTextView);

            childViewHolder.selectViewTab = (CheckBox) convertView
                    .findViewById(R.id.selectViewTab);
            childViewHolder.tabImageView = (ImageView) convertView
                    .findViewById(R.id.tabImageView);
            childViewHolder.loadMoreMedications = (CustomTextView) convertView
                    .findViewById(R.id.loadMoreMedication);

            convertView.setTag(R.layout.tablet_notification_item, childViewHolder);

        } else {

            childViewHolder = (ChildViewHolder) convertView
                    .getTag(R.layout.tablet_notification_item);
        }

        childViewHolder.tabNameTextView.setText(childText.getMedicineName());
        childViewHolder.tabImageView.setImageDrawable(CommonMethods.getMedicineTypeImage(childText.getMedicineTypeName(), mContext));

		/*
         * You have to set the onCheckChangedListener to null
		 * before restoring check states because each call to
		 * "setChecked" is accompanied by a call to the
		 * onCheckChangedListener
		*/
        childViewHolder.selectViewTab.setOnCheckedChangeListener(null);

        if (mChildCheckStates.containsKey(mGroupPosition)) {
            /*
             * if the hashmap mChildCheckStates<Integer, Boolean[]> contains
			 * the value of the parent view (group) of this child (aka, the key),
			 * then retrive the boolean array getChecked[]
			*/
            boolean getChecked[] = mChildCheckStates.get(mGroupPosition);

            // set the check state of this position's checkbox based on the
            // boolean value of getChecked[position]
            childViewHolder.selectViewTab.setChecked(getChecked[mChildPosition]);

        } else {

			/*
             * if the hashmap mChildCheckStates<Integer, Boolean[]> does not
			 * contain the value of the parent view (group) of this child (aka, the key),
			 * (aka, the key), then initialize getChecked[] as a new boolean array
			 *  and set it's size to the total number of children associated with
			 *  the parent group
			*/
            boolean getChecked[] = new boolean[getChildrenCount(mGroupPosition)];

            // add getChecked[] to the mChildCheckStates hashmap using mGroupPosition as the key
            mChildCheckStates.put(mGroupPosition, getChecked);

            // set the check state of this position's checkbox based on the
            // boolean value of getChecked[position]
            childViewHolder.selectViewTab.setChecked(false);
        }

        childViewHolder.selectViewTab.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                    getChecked[mChildPosition] = isChecked;
                    mChildCheckStates.put(mGroupPosition, getChecked);

                } else {

                    boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                    getChecked[mChildPosition] = isChecked;
                    mChildCheckStates.put(mGroupPosition, getChecked);
                }
            }
        });

        if (isLastChild) {
            childViewHolder.loadMoreMedications.setVisibility(View.VISIBLE);
        } else {
            childViewHolder.loadMoreMedications.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public final class GroupViewHolder {

        TextView mGroupText;
    }

    public final class ChildViewHolder {

        CustomTextView tabNameTextView;
        CustomTextView loadMoreMedications;
        CheckBox selectViewTab;
        ImageView tabImageView;
    }

    public void addAllElementToList() {
        mListDataChild.clear();
        mListDataGroup.clear();
        HashSet<String> strings = configureHeaderMapList(mOriginalReceivedList, false);
        mListDataGroup = new ArrayList<String>(strings);
        mListDataChild = configureGroupChildMapList(strings, mOriginalReceivedList);
    }

    public void addSingleElementToList() {
        mListDataChild.clear();
        mListDataGroup.clear();

        HashSet<String> strings = configureHeaderMapList(mOriginalReceivedList, true);
        ArrayList<String> temp = new ArrayList<String>(strings);
        mListDataGroup.add(temp.get(0));
        mListDataChild = configureGroupChildMapList(strings, mOriginalReceivedList);

    }

    private HashSet<String> configureHeaderMapList(ArrayList<UnreadNotificationMessageData> dataArrayList, boolean isSingleSize) {

        HashSet<String> listDataGroup = new HashSet<>();

        //--- set header data
        for (UnreadNotificationMessageData dataObject :
                dataArrayList) {
            String[] notificationData = dataObject.getNotificationData().split("\\|");
            listDataGroup.add(notificationData[0]);
            if (isSingleSize)
                break;
        }

        return listDataGroup;

    }

    private HashMap<String, ArrayList<Medication>> configureGroupChildMapList(HashSet<String> listDataGroup, ArrayList<UnreadNotificationMessageData> dataArrayList) {
        Gson gson = new Gson();
        HashMap<String, ArrayList<Medication>> listDataChild = new HashMap<>();
        //-- set child data
        for (String groupName :
                listDataGroup) {
            ArrayList<Medication> temp = new ArrayList<>();
            for (UnreadNotificationMessageData dataObject :
                    dataArrayList) {
                String[] notificationData = dataObject.getNotificationData().split("\\|");
                if (notificationData[0].equalsIgnoreCase(groupName)) {
                    Medication medication = gson.fromJson(notificationData[1], Medication.class);
                    temp.add(medication);
                }
            }
            listDataChild.put(groupName, temp);
        }
        //------
        return listDataChild;
    }


}