package com.myrescribe.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.myrescribe.R;
import com.myrescribe.model.doctors.filter_doctor_list.DoctorFilteredInfoAndCaseDetails;
import com.myrescribe.model.doctors.filter_doctor_list.DoctorFilteredInfo;
import com.myrescribe.ui.customesViews.CustomTextView;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by riteshpandhurkar on 24/2/17.
 */

public class DoctorFilteredExpandableList extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<DoctorFilteredInfoAndCaseDetails> mOriginalList;
    private List<DoctorFilteredInfoAndCaseDetails> mListDataHeader;// header titles
    // child data in format of header title, child title
    private HashMap<DoctorFilteredInfoAndCaseDetails, ArrayList<String>> mListDataChild;

    public DoctorFilteredExpandableList(Context context, ArrayList<DoctorFilteredInfoAndCaseDetails> mOriginalList) {
        this.context = context;
        this.mOriginalList = mOriginalList;

        this.mListDataHeader = new ArrayList<>();
        this.mListDataChild = new HashMap<>();

        for (DoctorFilteredInfoAndCaseDetails dataObject :
                mOriginalList) {
            mListDataHeader.add(dataObject);

            HashMap<String, String[]> caseDetailList = dataObject.getCaseDetailList();
            ArrayList<String> listToInsert = new ArrayList();
            for (Map.Entry<String, String[]> pair : caseDetailList.entrySet()) {
                //--- Add case name as header in list
                String data = "-1|" + pair.getKey() + "|" + MyRescribeConstants.TRUE;// this is set for header only, id|NAME|TRUE
                listToInsert.add(data);
                //---
                listToInsert.addAll(new ArrayList<String>(Arrays.asList(pair.getValue())));
            }
            mListDataChild.put(dataObject, listToInsert);
        }
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {

        final ChildViewHolder childViewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_filtered_doctor_child, parent, false);

            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        String child = getChild(groupPosition, childPosition);

        if (child.contains("|") && child.endsWith(MyRescribeConstants.TRUE)) {
            String[] split = child.split("\\|");
            childViewHolder.headerName.setText(split[1]);
            childViewHolder.headerName.setVisibility(View.VISIBLE);
            childViewHolder.childContent.setVisibility(View.GONE);
            childViewHolder.cardDetailsBullet.setVisibility(View.INVISIBLE);
        } else {
            childViewHolder.cardDetailsBullet.setVisibility(View.VISIBLE);
            childViewHolder.childContent.setText(child);
            childViewHolder.headerName.setVisibility(View.GONE);
            childViewHolder.childContent.setVisibility(View.VISIBLE);
        }

        if (groupPosition % 2 == 1) {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.divider));
            childViewHolder.childSideBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.darkblue));
        } else {
            childViewHolder.childSideBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.recentblue));
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public DoctorFilteredInfoAndCaseDetails getGroup(int groupPosition) {
        return mListDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mListDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {

        final GroupViewHolder groupViewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_doctor_list_layout, parent, false);

            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        DoctorFilteredInfoAndCaseDetails doctorFilteredInfoAndCaseDetails = getGroup(groupPosition);

        DoctorFilteredInfo dataObject = doctorFilteredInfoAndCaseDetails.getDoctorFilteredInfo();

        groupViewHolder.doctorName.setText(dataObject.getDoctorName());
        groupViewHolder.doctorAddress.setText(dataObject.getAddress());
        groupViewHolder.doctorType.setText(dataObject.getSpecialization());

        groupViewHolder.parentDataContainer.setBackgroundColor(dataObject.getRowColor());
        groupViewHolder.sideBarView.setBackgroundColor(dataObject.getSideBarViewColor());

        //--------
        String timeToShow = CommonMethods.formatDateTime(dataObject.getDate(), MyRescribeConstants.DATE_PATTERN.MMM_YYYY,
                MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD, MyRescribeConstants.DATE).toLowerCase();
        Date date = CommonMethods.convertStringToDate(dataObject.getDate(), MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String toDisplay = cal.get(Calendar.DAY_OF_MONTH) + "<sup>" + CommonMethods.getSuffixForNumber(cal.get(Calendar.DAY_OF_MONTH)) + "</sup> " + timeToShow.toUpperCase(Locale.US);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            groupViewHolder.date.setText(Html.fromHtml(toDisplay, Html.FROM_HTML_MODE_LEGACY));
        } else {
            groupViewHolder.date.setText(Html.fromHtml(toDisplay));
        }

        //---------
        groupViewHolder.footerBarLayout.setVisibility(View.VISIBLE);
        groupViewHolder.circularBulletChildElement.setVisibility(View.GONE);
        if (groupPosition == 0)
            groupViewHolder.upperLine.setVisibility(View.INVISIBLE);
        else {
            groupViewHolder.upperLine.setVisibility(View.VISIBLE);
        }
        //----------

        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);
        //----------
        if (groupPosition % 2 == 1) {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.divider));
            groupViewHolder.sideBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.darkblue));
            groupViewHolder.footerSideBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.darkblue));
        } else {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            groupViewHolder.sideBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.recentblue));
            groupViewHolder.footerSideBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.recentblue));
        }
        //-------

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        @BindView(R.id.date)
        CustomTextView date;

        @BindView(R.id.clickOnDoctorVisitLinearLayout)
        LinearLayout mClickOnDoctorVisitLinearLayout;

        @BindView(R.id.circularBulletChildElement)
        ImageView circularBulletChildElement;
        @BindView(R.id.circularBulletMainElement)
        ImageView circularBulletMainElement;

        @BindView(R.id.upperLine)
        TextView upperLine;
        @BindView(R.id.lowerLine)
        TextView lowerLine;

        @BindView(R.id.doctorName)
        CustomTextView doctorName;
        @BindView(R.id.doctorType)
        TextView doctorType;
        @BindView(R.id.doctorAddress)
        TextView doctorAddress;

        @BindView(R.id.parentDataContainer)
        LinearLayout parentDataContainer;
        @BindView(R.id.sideBarView)
        TextView sideBarView;
        @BindView(R.id.footerSideBarView)
        TextView footerSideBarView;
        @BindView(R.id.footerBarLayout)
        LinearLayout footerBarLayout;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        //---------

        @BindView(R.id.headerName)
        CustomTextView headerName;
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