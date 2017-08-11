package com.myrescribe.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.case_details.VisitCommonData;
import com.myrescribe.model.case_details.PatientHistory;
import com.myrescribe.model.case_details.Range;
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
    List<VisitCommonData> mVisitDetailList = new ArrayList<>();
    List<VisitCommonData> mCommonDataVisitList = new ArrayList<>();

    public OneDayVisitAdapter(Context context, List<PatientHistory> listDataHeader) {
        this.mContext = context;
        for (int i = 0; i < listDataHeader.size(); i++) {
            List<VisitCommonData> commonData = listDataHeader.get(i).getCommonData();
            List<Vital> commonDatasVitals = listDataHeader.get(i).getVitals();
            if (!(commonData == null)) {
                if (commonData.size() > 0) {
                    mListDataHeader.add(listDataHeader.get(i));
                }
            } else if (!(commonDatasVitals == null)) {
                if (commonDatasVitals.size() > 0) {
                    VisitCommonData commonVitals = new VisitCommonData();
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
        final List<VisitCommonData> childObject = mListDataHeader.get(groupPosition).getCommonData();
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
            LinearLayout unitValuesLayout = (LinearLayout)item.findViewById(R.id.unitValuesLayout);
            final int finali = mPosition;

            vitalLinearlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showVitalDialog(mContext, mListDataHeader.get(groupPosition).getVitals().get(finali).getUnitName(),
                            mListDataHeader.get(groupPosition).getVitals().get(finali).getUnitValue(),
                            mListDataHeader.get(groupPosition).getVitals().get(finali).getRanges(),
                            CommonMethods.getVitalIcons(mListDataHeader.get(groupPosition).getVitals().get(finali).getUnitName()),
                            mListDataHeader.get(groupPosition).getVitals().get(finali).getCategory());
                }
            });

            if(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getUnitName().equals(mContext.getString(R.string.bp))){
                String category = mListDataHeader.get(groupPosition).getVitals().get(mPosition).getCategory();
                String[] categoryForBp = category.split(":");
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0.7f
                );
                unitValuesLayout.setLayoutParams(param);
                String categoryForBpMax = categoryForBp[0];
                String categoryForBpMin = categoryForBp[1];
                String unit = mListDataHeader.get(groupPosition).getVitals().get(mPosition).getUnitValue();
                String[] unitForBp = unit.split("/");
                String unitForBpMax = unitForBp[0];
                String unitForBpMin = unitForBp[1];
                vitalImage.setImageResource(CommonMethods.getVitalIcons(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getUnitName()));
                vital_name.setText(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getUnitName());
                noOfVitals.setText(Html.fromHtml(getUnitValueforBp(categoryForBpMin,categoryForBpMax,unitForBpMin,unitForBpMax)));

            }else {
                vitalImage.setImageResource(CommonMethods.getVitalIcons(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getUnitName()));
                vital_name.setText(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getUnitName());
              noOfVitals.setText(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getUnitValue());
                if (mListDataHeader.get(groupPosition).getVitals().get(mPosition).getCategory().equalsIgnoreCase(mContext.getResources().getString(R.string.severeRange))) {
                    noOfVitals.setTextColor(ContextCompat.getColor(mContext, R.color.Red));
                } else if (mListDataHeader.get(groupPosition).getVitals().get(mPosition).getCategory().equalsIgnoreCase(mContext.getResources().getString(R.string.normalRange))) {
                    noOfVitals.setTextColor(ContextCompat.getColor(mContext, R.color.range_green));
                } else if (mListDataHeader.get(groupPosition).getVitals().get(mPosition).getCategory().equalsIgnoreCase(mContext.getResources().getString(R.string.moderateRange))) {
                    noOfVitals.setTextColor(ContextCompat.getColor(mContext, R.color.range_yellow));
                }
            }
            tableRow.addView(item);
            mPosition++;
        }
        return tableRow;
    }

    private String getUnitValueforBp( String categoryForBpMin, String categoryForBpMax,String forBpMin, String forBpMax) {
       String unitValue = "";
        if(categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.severeRange))&&categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.severeRange))){
            String bpMax = "<font color='#FF0000'>"+forBpMax+"</font>";
            String bpMin = "<font color='#FF0000'>"+forBpMin+"</font>";
            String slash = "<font color='#737373'>"+"/"+"</font>";
            unitValue = bpMax+slash+bpMin;


        }else if(categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.severeRange))&&categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.moderateRange))){
            String bpMax = "<font color='#FF0000'>"+forBpMax+"</font>";
            String bpMin = "<font color='#ff9500'>"+forBpMin+"</font>";
            String slash = "<font color='#737373'>"+"/"+"</font>";
            unitValue = bpMax+slash+bpMin;

        }else if(categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.severeRange))&&categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.normalRange))){
            String bpMax = "<font color='#FF0000'>"+forBpMax+"</font>";
            String bpMin = "<font color='#178a00'>"+forBpMin+"</font>";
            String slash = "<font color='#737373'>"+"/"+"</font>";
            unitValue = bpMax+slash+bpMin;

        }else  if(categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.normalRange))&&categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.severeRange))){
            String bpMax = "<font color='#178a00'>"+forBpMax+"</font>";
            String bpMin = "<font color='#FF0000'>"+forBpMin+"</font>";
            String slash = "<font color='#737373'>"+"/"+"</font>";
            unitValue = bpMax+slash+bpMin;

        } else if(categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.normalRange))&&categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.moderateRange))){
            String bpMax = "<font color='#178a00'>"+forBpMax+"</font>";
            String bpMin = "<font color='#ff9500'>"+forBpMin+"</font>";
            String slash = "<font color='#737373'>"+"/"+"</font>";
            unitValue = bpMax+slash+bpMin;

        }else if(categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.normalRange))&&categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.normalRange))){
            String bpMax = "<font color='#178a00'>"+forBpMax+"</font>";
            String bpMin = "<font color='#178a00'>"+forBpMin+"</font>";
            String slash = "<font color='#737373'>"+"/"+"</font>";
            unitValue = bpMax+slash+bpMin;

        }else  if(categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.moderateRange))&&categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.severeRange))){
            String bpMax = "<font color='#ff9500'>"+forBpMax+"</font>";
            String bpMin = "<font color='#FF0000'>"+forBpMin+"</font>";
            String slash = "<font color='#737373'>"+"/"+"</font>";
            unitValue = bpMax+slash+bpMin;

        }else if(categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.moderateRange))&&categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.moderateRange))){
            String bpMax = "<font color='#ff9500'>"+forBpMax+"</font>";
            String bpMin = "<font color='#ff9500'>"+forBpMin+"</font>";
            String slash = "<font color='#737373'>"+"/"+"</font>";
            unitValue = bpMax+slash+bpMin;

        }else if(categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.moderateRange))&&categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.normalRange))){
            String bpMax = "<font color='#ff9500'>"+forBpMax+"</font>";
            String bpMin = "<font color='#178a00'>"+forBpMin+"</font>";
            String slash = "<font color='#737373'>"+"/"+"</font>";
            unitValue = bpMax+slash+bpMin;

        }
        return unitValue;
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

    public Dialog showVitalDialog(Context context, String unitName, String unitValue, List<Range> rangeList, Integer drawable, String category) {

        final Context mContext = context;
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.vitals_dialog_layout);
        dialog.setCancelable(true);
        String normal = "";
        String moderate = "";
        String severe = "";
        LinearLayout normalBpMaxRangeLayout = (LinearLayout) dialog.findViewById(R.id.normalRangeLayout);
        LinearLayout moderateBpMaxRangeLayout = (LinearLayout) dialog.findViewById(R.id.moderateRangeLayout);
        LinearLayout severeBpMaxRangeLayout = (LinearLayout) dialog.findViewById(R.id.severeRangeLayout);
        LinearLayout showVitalNameLayout = (LinearLayout) dialog.findViewById(R.id.showVitalNameLayout);
        TextView normalBpMaxRange = (TextView) dialog.findViewById(R.id.normalRange);
        TextView moderateBpMaxRange = (TextView) dialog.findViewById(R.id.moderateRange);
        TextView severeBpMaxRange = (TextView) dialog.findViewById(R.id.severeRange);
        LinearLayout showVitalRangeLayout = (LinearLayout) dialog.findViewById(R.id.showVitalRangeLayout);
        TextView vitalTypeNameDialog = (TextView) dialog.findViewById(R.id.vitalTypeNameDialog);
        TextView noOfVitalsTypeDialog = (TextView) dialog.findViewById(R.id.noOfVitalsTypeDialog);
        TextView normalRange = (TextView) dialog.findViewById(R.id.normalSubTypeRange);
        TextView moderateRange = (TextView) dialog.findViewById(R.id.moderateSubTypeRange);
        TextView severeRange = (TextView) dialog.findViewById(R.id.severeSubTypeRange);
        TextView noOfVitalsDialog = (TextView) dialog.findViewById(R.id.noOfVitalsDialog);
        TextView vitalName = (TextView) dialog.findViewById(R.id.vitalNameDialog);
        LinearLayout bpMinLayout = (LinearLayout) dialog.findViewById(R.id.bpMinLayout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        for (int i = 0; i < rangeList.size(); i++) {
            if (rangeList.get(i).getNameOfVital() != null) {
            if (rangeList.get(i).getNameOfVital().equalsIgnoreCase(mContext.getString(R.string.bp_max))) {
                if(rangeList.get(i).getCategory().equalsIgnoreCase(mContext.getString(R.string.normalRange))) {
                    if (normal.equals("")) {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(),rangeList.get(i).getValue(),rangeList.get(i).getMin(),rangeList.get(i).getMax());
                        normal += finalString;
                        normalBpMaxRange.setText(normal);
                    } else {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(),rangeList.get(i).getValue(),rangeList.get(i).getMin(),rangeList.get(i).getMax());
                        normal += "," + finalString;
                        normalBpMaxRange.setText(normal);
                    }
                }else if(rangeList.get(i).getCategory().equalsIgnoreCase(mContext.getString(R.string.moderateRange))){
                    if (moderate.equals("")) {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(),rangeList.get(i).getValue(),rangeList.get(i).getMin(),rangeList.get(i).getMax());
                        moderate += finalString;
                        moderateBpMaxRange.setText(moderate);
                    } else {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(),rangeList.get(i).getValue(),rangeList.get(i).getMin(),rangeList.get(i).getMax());
                        moderate += "," + finalString;
                        moderateBpMaxRange.setText(moderate);
                    }
                }
                else if(rangeList.get(i).getCategory().equalsIgnoreCase(mContext.getString(R.string.severeRange))){
                    if (severe.equals("")) {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(),rangeList.get(i).getValue(),rangeList.get(i).getMin(),rangeList.get(i).getMax());
                        severe += finalString;
                        severeBpMaxRange.setText(severe);
                    } else {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(),rangeList.get(i).getValue(),rangeList.get(i).getMin(),rangeList.get(i).getMax());
                        severe += "," + finalString;
                        severeBpMaxRange.setText(severe);

                    }
                }
            } else if (rangeList.get(i).getNameOfVital().equalsIgnoreCase(mContext.getString(R.string.bp_min))) {

                if(rangeList.get(i).getCategory().equalsIgnoreCase(mContext.getString(R.string.normalRange))) {
                    if (normal.equals("")) {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(),rangeList.get(i).getValue(),rangeList.get(i).getMin(),rangeList.get(i).getMax());
                        normal += finalString;
                        normalRange.setText(normal);
                    } else {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(),rangeList.get(i).getValue(),rangeList.get(i).getMin(),rangeList.get(i).getMax());
                        normal += "," + finalString;
                        normalRange.setText(normal);
                    }
                }else if(rangeList.get(i).getCategory().equalsIgnoreCase(mContext.getString(R.string.moderateRange))){
                    if (moderate.equals("")) {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(),rangeList.get(i).getValue(),rangeList.get(i).getMin(),rangeList.get(i).getMax());
                        moderate += finalString;
                        moderateRange.setText(moderate);
                    } else {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(),rangeList.get(i).getValue(),rangeList.get(i).getMin(),rangeList.get(i).getMax());
                        moderate += "," + finalString;
                        moderateRange.setText(moderate);
                    }
                }
                else if(rangeList.get(i).getCategory().equalsIgnoreCase(mContext.getString(R.string.severeRange))){
                    if (severe.equals("")) {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(),rangeList.get(i).getValue(),rangeList.get(i).getMin(),rangeList.get(i).getMax());
                        severe += finalString;
                        severeRange.setText(severe);
                    } else {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(),rangeList.get(i).getValue(),rangeList.get(i).getMin(),rangeList.get(i).getMax());
                        severe += "," + finalString;
                        severeRange.setText(severe);

                    }
                }

            }
        }else if(rangeList.get(i).getNameOfVital()==null){
                if(rangeList.get(i).getCategory().equalsIgnoreCase(mContext.getString(R.string.normalRange))) {
                    if (normal.equals("")) {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(),rangeList.get(i).getValue(),rangeList.get(i).getMin(),rangeList.get(i).getMax());
                        normal += finalString;
                        normalRange.setText(normal);
                    } else {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(),rangeList.get(i).getValue(),rangeList.get(i).getMin(),rangeList.get(i).getMax());
                        normal += "," + finalString;
                        normalRange.setText(normal);
                    }
                }else if(rangeList.get(i).getCategory().equalsIgnoreCase(mContext.getString(R.string.moderateRange))){
                    if (moderate.equals("")) {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(),rangeList.get(i).getValue(),rangeList.get(i).getMin(),rangeList.get(i).getMax());
                        moderate += finalString;
                        moderateRange.setText(moderate);
                    } else {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(),rangeList.get(i).getValue(),rangeList.get(i).getMin(),rangeList.get(i).getMax());
                        moderate += "," + finalString;
                        moderateRange.setText(moderate);
                    }
                }
                else if(rangeList.get(i).getCategory().equalsIgnoreCase(mContext.getString(R.string.severeRange))){
                    if (severe.equals("")) {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(),rangeList.get(i).getValue(),rangeList.get(i).getMin(),rangeList.get(i).getMax());
                        severe += finalString;
                        severeRange.setText(severe);
                    } else {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(),rangeList.get(i).getValue(),rangeList.get(i).getMin(),rangeList.get(i).getMax());
                        severe += "," + finalString;
                        severeRange.setText(severe);

                    }
                }
            }
        }
        if (unitName.equals(mContext.getString(R.string.bp))) {
            String unitData = unitValue;
            String[] unitDataObject = unitData.split("/");
            String unitBpMax = unitDataObject[0];
            String unitBpMin = unitDataObject[1];
            showVitalNameLayout.setVisibility(View.VISIBLE);
            showVitalRangeLayout.setVisibility(View.VISIBLE);
            vitalName.setText(mContext.getString(R.string.systolic_pressure));
            String[] categoryForBp = category.split(":");
            String categoryForBpMax = categoryForBp[0];
            String categoryBpMin = categoryForBp[1];
            if (categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.severeRange))) {
                noOfVitalsDialog.setTextColor(ContextCompat.getColor(mContext, R.color.Red));
            } else if (categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.normalRange))) {
                noOfVitalsDialog.setTextColor(ContextCompat.getColor(mContext, R.color.range_green));
            } else if (categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.moderateRange))) {
                noOfVitalsDialog.setTextColor(ContextCompat.getColor(mContext, R.color.range_yellow));
            }
            if (categoryBpMin.equalsIgnoreCase(mContext.getString(R.string.severeRange))) {
                noOfVitalsTypeDialog.setTextColor(ContextCompat.getColor(mContext, R.color.Red));
            } else if (categoryBpMin.equalsIgnoreCase(mContext.getString(R.string.normalRange))) {
                noOfVitalsTypeDialog.setTextColor(ContextCompat.getColor(mContext, R.color.range_green));
            } else if (categoryBpMin.equalsIgnoreCase(mContext.getString(R.string.moderateRange))) {
                noOfVitalsTypeDialog.setTextColor(ContextCompat.getColor(mContext, R.color.range_yellow));
            }
            noOfVitalsDialog.setText(unitBpMax);
            noOfVitalsTypeDialog.setText(unitBpMin);
            vitalTypeNameDialog.setText(mContext.getString(R.string.diastolic_pressure));
            if (normalBpMaxRange.getText().toString().trim().length() == 0) {
                normalBpMaxRangeLayout.setVisibility(View.GONE);
            }
            if (moderateBpMaxRange.getText().toString().trim().length() == 0) {
                moderateBpMaxRangeLayout.setVisibility(View.GONE);
            }
            if (severeBpMaxRange.getText().toString().trim().length() == 0) {
                severeBpMaxRangeLayout.setVisibility(View.GONE);
            }
            if(normalBpMaxRange.getText().toString().trim().length() == 0&&moderateBpMaxRange.getText().toString().trim().length() == 0&&severeBpMaxRange.getText().toString().trim().length() == 0){
                showVitalRangeLayout.setVisibility(View.GONE);
                showVitalNameLayout.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
                bpMinLayout.setVisibility(View.VISIBLE);
                bpMinLayout.setBackground(mContext.getDrawable(R.drawable.vitals_curve_grey_bg_bottom_curves));

            }

        } else {
            showVitalNameLayout.setVisibility(View.GONE);
            showVitalRangeLayout.setVisibility(View.GONE);
            noOfVitalsDialog.setText(unitValue);
            vitalName.setText(unitName);
        }

        if (category.equalsIgnoreCase(mContext.getString(R.string.severeRange))) {
            noOfVitalsDialog.setTextColor(ContextCompat.getColor(mContext, R.color.Red));
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.normalRange))) {
            noOfVitalsDialog.setTextColor(ContextCompat.getColor(mContext, R.color.range_green));
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.moderateRange))) {
            noOfVitalsDialog.setTextColor(ContextCompat.getColor(mContext, R.color.range_yellow));
        }

        //----Manage visibility----
        if (normalRange.getText().toString().trim().length() == 0) {
            LinearLayout normalRangeLayout = (LinearLayout) dialog.findViewById(R.id.normalSubTypeRangeLayout);
            normalRangeLayout.setVisibility(View.GONE);
        }
        if (moderateRange.getText().toString().trim().length() == 0) {
            LinearLayout moderateRangeLayout = (LinearLayout) dialog.findViewById(R.id.moderateSubTypeRangeLayout);
            moderateRangeLayout.setVisibility(View.GONE);
        }
        if (severeRange.getText().toString().trim().length() == 0) {
            LinearLayout severeRangeLayout = (LinearLayout) dialog.findViewById(R.id.severeSubTypeRangeLayout);
            severeRangeLayout.setVisibility(View.GONE);
        }
        if(normalRange.getText().toString().trim().length() == 0&&moderateRange.getText().toString().trim().length() == 0&&severeRange.getText().toString().trim().length() == 0){
            bpMinLayout.setVisibility(View.GONE);
            showVitalNameLayout.setBackground(mContext.getDrawable(R.drawable.vitals_curve_grey_bg_bottom_curves));


        }
        //--------
        ((ImageView) dialog.findViewById(R.id.vitalImageDialog)).setImageResource(drawable);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        lp.gravity = Gravity.CENTER;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        return dialog;
    }

    private String getSortedRangeValues(String category, String operator , String value , String min, String max) {
        String range = "";
        if (category.equalsIgnoreCase(mContext.getString(R.string.normalRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.less))) {
            range = mContext.getString(R.string.less_than_sign) + value;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.normalRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.equal))) {
            range= min + mContext.getString(R.string.dash) +max;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.normalRange)) &&operator.equalsIgnoreCase(mContext.getString(R.string.greater))) {
            range =    mContext.getString(R.string.greater_than_sign) + value;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.moderateRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.less))) {
            range =  mContext.getString(R.string.less_than_sign) + value;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.moderateRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.equal))) {
            range =  min + mContext.getString(R.string.dash) + max;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.moderateRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.greater))) {
            range = mContext.getString(R.string.greater_than_sign) + value;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.severeRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.less))) {
            range =   mContext.getString(R.string.less_than_sign) + value;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.severeRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.equal))) {
            range = min + mContext.getString(R.string.dash) + max;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.severeRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.greater))) {
            range =  mContext.getString(R.string.greater_than_sign) +value;
        }
        return range;
    }




}