package com.myrescribe.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.case_details.CommonData;
import com.myrescribe.model.case_details.PatientHistory;
import com.myrescribe.model.case_details.Vital;
import com.myrescribe.util.CommonMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OneDayVisitAdapter extends BaseExpandableListAdapter {
    private int mPosition = 0;
    private Context mContext;
    private static final String CHILD_TYPE_1 = "vitals";
    private List<PatientHistory> mListDataHeader = new ArrayList<>(); // header titles
    List<CommonData> mVisitDetailList = new ArrayList<>();
    List<CommonData> mCommonDataVisitList = new ArrayList<>();

    public OneDayVisitAdapter(Context context, List<PatientHistory> listDataHeader) {
        this.mContext = context;
        for (int i = 0; i < listDataHeader.size(); i++) {
            List<CommonData> commonData = listDataHeader.get(i).getCommonData();
            List<Vital> commonDatasVitals = listDataHeader.get(i).getVitals();
            if (!(commonData == null)) {
                if (commonData.size() > 0) {
                    mListDataHeader.add(listDataHeader.get(i));
                }
            } else if (!(commonDatasVitals == null)) {
                if (commonDatasVitals.size() > 0) {
                    CommonData commonVitals = new CommonData();
                    commonVitals.setId(0);
                    commonVitals.setName(listDataHeader.get(i).getVitals().get(0).getUnitName());
                    mCommonDataVisitList.add(commonVitals);
                    listDataHeader.get(i).setCommonData(mCommonDataVisitList);
                    mListDataHeader.add(listDataHeader.get(i));
                }
            }
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return (this.mListDataHeader.get(groupPosition).getCommonData())
                .get(childPosititon);
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final List<CommonData> childObject = mListDataHeader.get(groupPosition).getCommonData();
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        String headerName = mListDataHeader.get(groupPosition).getCaseDetailName();
        switch (headerName) {
            case CHILD_TYPE_1:
                convertView = inflater.inflate(R.layout.vitals_main_activity, null);
                convertView.setTag(headerName);
                TableLayout tableLayout = (TableLayout) convertView.findViewById(R.id.table);
                View divider = convertView.findViewById(R.id.adapter_divider);
                tableLayout.removeAllViews();
                mPosition = 0;
                List<com.myrescribe.model.case_details.Vital> vital = new ArrayList<>();
                int size = mListDataHeader.get(groupPosition).getVitals().size();
                int count = 1;
                int tempSize = size - (size % 3);
                for (int i = 0; i < size; i++) {

                    vital.add(mListDataHeader.get(groupPosition).getVitals().get(i));

                    if (tempSize > i) {
                        if (count == 3) {
                            tableLayout.addView(addTableRow(vital, groupPosition));
                            vital.clear();
                            count = 1;
                        } else
                            count++;
                    } else if (count == size % 3) {
                        tableLayout.addView(addTableRow(vital, groupPosition));
                        vital.clear();
                        count = 1;
                    } else count++;
                }

                if (isLastChild) {
                    divider.setVisibility(View.VISIBLE);
                } else {
                    divider.setVisibility(View.GONE);
                }
                break;

            default:
                convertView = inflater.inflate(R.layout.history_child_item_layout, null);
                convertView.setTag(headerName);
                TextView txtListChild = (TextView) convertView.findViewById(R.id.textView_name);
                View dividerLine = convertView.findViewById(R.id.adapter_divider_bottom);
                txtListChild.setText(childObject.get(childPosition).getName());

                if (isLastChild) {
                    dividerLine.setVisibility(View.VISIBLE);
                } else {
                    dividerLine.setVisibility(View.GONE);
                }
                break;
        }

        return convertView;
    }

    private View addTableRow(final List<com.myrescribe.model.case_details.Vital> vital, final int groupPosition) {

        int i;
        TableRow tableRow = new TableRow(mContext);
        for (i = 0; i < vital.size(); i++) {
            View item = LayoutInflater.from(mContext)
                    .inflate(R.layout.vital_item_row, tableRow, false);
            LinearLayout vitalLinearlayout = (LinearLayout) item.findViewById(R.id.vitalCellLinearLayout);
            ImageView vitalImage = (ImageView) item.findViewById(R.id.vitalImage);
            TextView vital_name = (TextView) item.findViewById(R.id.vital_name);
            TextView noOfVitals = (TextView) item.findViewById(R.id.noOfVitals);
            final int finali = mPosition;
            vitalLinearlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonMethods.showVitalDialog(mContext, mListDataHeader.get(groupPosition).getVitals().get(finali).getUnitName(),
                            mListDataHeader.get(groupPosition).getVitals().get(finali).getUnitValue(),
                            mListDataHeader.get(groupPosition).getVitals().get(finali).getRanges(),
                            CommonMethods.getVitalIcons(mListDataHeader.get(groupPosition).getVitals().get(finali).getDisplayName()),
                            mListDataHeader.get(groupPosition).getVitals().get(finali).getCategory());
                }
            });
            vitalImage.setImageResource(CommonMethods.getVitalIcons(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getDisplayName()));
            vital_name.setText(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getUnitName());
            noOfVitals.setText(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getUnitValue());
            if (mListDataHeader.get(groupPosition).getVitals().get(mPosition).getCategory().equalsIgnoreCase(mContext.getResources().getString(R.string.severeRange))) {
                noOfVitals.setTextColor(ContextCompat.getColor(mContext, R.color.Red));
            } else if (mListDataHeader.get(groupPosition).getVitals().get(mPosition).getCategory().equalsIgnoreCase(mContext.getResources().getString(R.string.normalRange))) {
                noOfVitals.setTextColor(ContextCompat.getColor(mContext, R.color.range_green));
            } else if (mListDataHeader.get(groupPosition).getVitals().get(mPosition).getCategory().equalsIgnoreCase(mContext.getResources().getString(R.string.moderateRange))) {
                noOfVitals.setTextColor(ContextCompat.getColor(mContext, R.color.range_yellow));
            }
            tableRow.addView(item);
            mPosition++;
        }
        return tableRow;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (this.mListDataHeader.get(groupPosition).getCommonData())
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

        if (isExpanded) {
            groupViewHolder.mDetailFirstPoint.setVisibility(View.GONE);
            groupViewHolder.mHeadergroupDivider.setVisibility(View.VISIBLE);
            groupViewHolder.mDivider.setVisibility(View.GONE);

        } else {
            groupViewHolder.mDetailFirstPoint.setVisibility(View.VISIBLE);
            groupViewHolder.mHeadergroupDivider.setVisibility(View.GONE);
            groupViewHolder.mDivider.setVisibility(View.VISIBLE);
        }
        String s1 = mListDataHeader.get(groupPosition).getCaseDetailName();
        groupViewHolder.lblListHeader.setText(s1.substring(0, 1).toUpperCase() + s1.substring(1));
        groupViewHolder.mViewDetailIcon.setImageResource(CommonMethods.getCaseStudyIcons(mListDataHeader.get(groupPosition).getCaseDetailName()));
        if (!mListDataHeader.get(groupPosition).getCommonData().equals(null)) {
            mVisitDetailList = mListDataHeader.get(groupPosition).getCommonData();
            groupViewHolder.mDetailFirstPoint.setText(setStringLength(mVisitDetailList.get(0).getName()) + ".......");
        }
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

    public String setStringLength(String t) {
        String o = "";
        if (t.length() >= 30) {
            o = t.substring(0, 30);
            System.out.println(o);
            return o;
        } else {
            System.out.println(t);
            return t;
        }
    }
}