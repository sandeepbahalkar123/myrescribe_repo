package com.rescribe.adapters.myrecords;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.model.my_records.MyRecordDoctorInfo;
import com.rescribe.model.my_records.MyRecordInfoAndReports;
import com.rescribe.model.my_records.MyRecordReports;
import com.rescribe.ui.activities.ShowRecordsActivity;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThreeLevelListAdapter extends BaseExpandableListAdapter {

    private RequestOptions mRequestOptions;
    private ColorGenerator mColorGenerator;
    private List<MyRecordInfoAndReports> mListDataHeader;// header titles
    // child data in format of header title, child title
    private HashMap<MyRecordInfoAndReports, ArrayList<MyRecordReports>> mListDataChild;

    private ArrayList<MyRecordInfoAndReports> mOriginalList;
    private Context context;

    public ThreeLevelListAdapter(Context context, ArrayList<MyRecordInfoAndReports> mOriginalList) {
        this.context = context;
        this.mOriginalList = mOriginalList;

        this.mListDataHeader = new ArrayList<>();
        this.mListDataChild = new HashMap<>();
        mColorGenerator = ColorGenerator.MATERIAL;

        for (MyRecordInfoAndReports dataObject :
                mOriginalList) {
            mListDataHeader.add(dataObject);

            ArrayList<MyRecordReports> myRecordReportInfo = dataObject.getMyRecordReportInfo();

            mListDataChild.put(dataObject, myRecordReportInfo);
        }

        int imageSizeToLoadImage = CommonMethods.getImageSizeToLoadImage(context, 2);

        mRequestOptions = new RequestOptions();
        mRequestOptions.dontAnimate();
        mRequestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        mRequestOptions.skipMemoryCache(true);
        mRequestOptions.override(imageSizeToLoadImage, imageSizeToLoadImage);
    }


    @Override
    public int getGroupCount() {
        return mListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // no idea why this code is working
        return 1;

        //-- Below code did not work, hence commented.
//        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
//                .size();
    }

    @Override
    public MyRecordInfoAndReports getGroup(int groupPosition) {
        return mListDataHeader.get(groupPosition);
    }

    @Override
    public MyRecordReports getChild(int groupPosition, int childPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final GroupViewHolder groupViewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_doctor_list_layout, parent, false);

            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        MyRecordInfoAndReports group = getGroup(groupPosition);

        MyRecordDoctorInfo dataObject = group.getMyRecordDoctorInfo();

        groupViewHolder.doctorName.setText(dataObject.getDoctorName());
        groupViewHolder.doctorAddress.setText(dataObject.getAddress());
        groupViewHolder.doctorType.setText(dataObject.getSpecialization());

        groupViewHolder.parentDataContainer.setBackgroundColor(dataObject.getRowColor());
        groupViewHolder.sideBarView.setBackgroundColor(dataObject.getSideBarViewColor());

        //--------
        String timeToShow = CommonMethods.formatDateTime(dataObject.getDate(), RescribeConstants.DATE_PATTERN.MMM_YYYY,
                RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE).toLowerCase();
        Date date = CommonMethods.convertStringToDate(dataObject.getDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String toDisplay = cal.get(Calendar.DAY_OF_MONTH) + "<sup>" + CommonMethods.getSuffixForNumber(cal.get(Calendar.DAY_OF_MONTH)) + "</sup> ";// + timeToShow.toUpperCase(Locale.US);
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

        if (groupPosition % 2 == 1) {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.divider));
            dataObject.setRowColor(ContextCompat.getColor(context, R.color.divider));
            groupViewHolder.sideBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.darkblue));
            groupViewHolder.footerSideBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.darkblue));
        } else {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            dataObject.setRowColor(ContextCompat.getColor(context, R.color.white));

            groupViewHolder.sideBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.recentblue));
            groupViewHolder.footerSideBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.recentblue));
        }
        //-------


        if (dataObject.getDocImgURL() == null) {
            int color2 = mColorGenerator.getColor(dataObject.getDoctorName());

            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .width(Math.round(context.getResources().getDimension(R.dimen.dp40))) // width in px
                    .height(Math.round(context.getResources().getDimension(R.dimen.dp40))) // height in px
                    .endConfig()
                    .buildRound(("" + dataObject.getDoctorName().charAt(0)).toUpperCase(), color2);

            groupViewHolder.docProfileImage.setImageDrawable(drawable);
        } else {
            Glide.with(context)
                    .load(dataObject.getDocImgURL())
                    .apply(mRequestOptions).thumbnail(0.5f)
                    .into(groupViewHolder.docProfileImage);
        }

        //--------------

        return convertView;

    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final SecondLevelExpandableListView secondLevelELV = new SecondLevelExpandableListView(context);

        MyRecordInfoAndReports group = getGroup(groupPosition);

        //----------
        MyRecordDoctorInfo dataObject = group.getMyRecordDoctorInfo();

        int color;
        int bgColor = dataObject.getRowColor();
        if (groupPosition % 2 == 1) {

            color = ContextCompat.getColor(context, R.color.darkblue);
        } else {
            color = ContextCompat.getColor(context, R.color.recentblue);
        }

        //-----------

        secondLevelELV.setAdapter(new SecondLevelAdapter(context, group.getMyRecordReportInfo(), color, bgColor));

        secondLevelELV.setGroupIndicator(null);
        secondLevelELV.setChildIndicator(null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            secondLevelELV.setChildDivider(context.getDrawable(R.color.black));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            secondLevelELV.setDivider(context.getDrawable(R.color.black));
        }
        secondLevelELV.setDividerHeight(0);

        secondLevelELV.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                /* TO open single child group at a time.
                if (groupPosition != previousGroup)
                    secondLevelELV.collapseGroup(previousGroup);
                previousGroup = groupPosition;*/
                SecondLevelAdapter adapter = (SecondLevelAdapter) secondLevelELV.getExpandableListAdapter();
                MyRecordReports childGroup = adapter.getGroup(groupPosition);
                if (childGroup.getParentCaptionName().equalsIgnoreCase(context.getString(R.string.investigation)) || childGroup.getParentCaptionName().equalsIgnoreCase(context.getString(R.string.investigations))) {
                } else {
                    secondLevelELV.collapseGroup(groupPosition);
                    Intent intent = new Intent(context, ShowRecordsActivity.class);
                    ArrayList<MyRecordReports.MyRecordReportList> reportList = childGroup.getReportList();
                    MyRecordReports.MyRecordReportList myRecordReportList = reportList.get(0);
                    String caption = myRecordReportList.getChildCaptionName();
                    String[] imageList = myRecordReportList.getImageList();
                    intent.putExtra(RescribeConstants.DOCUMENTS, imageList);
                    intent.putExtra(RescribeConstants.CAPTION, caption);
                    context.startActivity(intent);
                }
            }
        });
        secondLevelELV.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                SecondLevelAdapter adapter = (SecondLevelAdapter) secondLevelELV.getExpandableListAdapter();
                MyRecordReports.MyRecordReportList child = adapter.getChild(groupPosition, childPosition);
                String[] imageList = child.getImageList();
                String caption = child.getChildCaptionName();
                Intent intent = new Intent(context, ShowRecordsActivity.class);
                intent.putExtra(RescribeConstants.DOCUMENTS, imageList);
                intent.putExtra(RescribeConstants.CAPTION, caption);
                context.startActivity(intent);
                return false;
            }
        });
        return secondLevelELV;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        @BindView(R.id.date)
        CustomTextView date;
        @BindView(R.id.docProfileImage)
        CircularImageView docProfileImage;

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
}
