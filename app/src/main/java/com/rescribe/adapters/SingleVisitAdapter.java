package com.rescribe.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.model.case_details.PatientHistory;
import com.rescribe.model.case_details.Range;
import com.rescribe.model.case_details.VisitCommonData;
import com.rescribe.model.case_details.Vital;
import com.rescribe.ui.activities.WebViewActivity;
import com.rescribe.ui.activities.zoom_images.MultipleImageWithSwipeAndZoomActivity;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingleVisitAdapter extends BaseExpandableListAdapter {
    private int mPosition = 0;
    private Context mContext;

    public static final String CHILD_TYPE_VITALS = "vital";
    public static final String CHILD_TYPE_ATTACHMENTS = "attachment";
    private static final String CHILD_TYPE_ALLERGIES = "allergie";
    private static final String CHILD_TYPE_PRESCRIPTIONS = "prescription";
    private static final String CHILD_TYPE_IMAGE_ANNOTATIONS = "image annotations";
    private static final String CHILD_TYPE_MEDICAL_HISTORY = "medical history";

    private List<PatientHistory> mListDataHeader = new ArrayList<>(); // header titles
    List<VisitCommonData> mVisitDetailList = new ArrayList<>();
    List<VisitCommonData> mCommonDataVisitList = new ArrayList<>();
    public static final int TEXT_LIMIT = 33;
    private List<VisitCommonData> mListDataHeaderAllAttachmentCommonDataList;

    public SingleVisitAdapter(Context context, List<PatientHistory> listDataHeader) {
        this.mContext = context;
        //  case details with no data are not added in the list
        for (int i = 0; i < listDataHeader.size(); i++) {
            List<VisitCommonData> commonData = listDataHeader.get(i).getCommonData();
            List<Vital> commonDatasVitals = listDataHeader.get(i).getVitals();
            if (!(commonData == null)) {
                if (!commonData.isEmpty()) {
                    mListDataHeader.add(listDataHeader.get(i));
                } else if (listDataHeader.get(i).getCaseDetailName().toLowerCase().contains(CHILD_TYPE_MEDICAL_HISTORY)) {
                    if (!listDataHeader.get(i).getComment().equals(""))
                        mListDataHeader.add(listDataHeader.get(i));
                }


//                else if (listDataHeader.get(i).getCaseDetailName().toLowerCase().contains(CHILD_TYPE_ALLERGIES)) {
//                    List<VisitCommonData> common = new ArrayList<>();
//                    VisitCommonData visitCommonData = new VisitCommonData();
//                    visitCommonData.setDosage("");
//                    visitCommonData.setId(0);
//                    visitCommonData.setMedicineTypeName("");
//                    visitCommonData.setMedicinename("");
//                    visitCommonData.setName("No Allergies Found");
//                    visitCommonData.setRemarks("");
//                    visitCommonData.setVitalValue("");
//                    visitCommonData.setUrl("");
//                    common.add(visitCommonData);
//                    listDataHeader.get(i).setCommonData(common);
//                    mListDataHeader.add(listDataHeader.get(i));
//                }
            } else if (!(commonDatasVitals == null)) {
                if (commonDatasVitals.size() > 0) {
                    VisitCommonData commonVitals = new VisitCommonData();
                    commonVitals.setId(0);
                    commonVitals.setVitalValue(listDataHeader.get(i).getVitals().get(0).getUnitValue());
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

        final ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.visit_details_child_item_layout, parent, false);

            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        // Onclick of vitals UI is different from those of other case details
        String headerName = mListDataHeader.get(groupPosition).getCaseDetailName().toLowerCase();
        if (headerName.contains(CHILD_TYPE_VITALS)) {
            //set data and UI for vitals

            childViewHolder.tableLayout.removeAllViews();
            childViewHolder.textView_comment.setVisibility(View.GONE);
            childViewHolder.tableLayout.setVisibility(View.VISIBLE);
            childViewHolder.itemsLayout.setVisibility(View.GONE);

            mPosition = 0;
            List<Vital> vital = new ArrayList<>();
            int size = mListDataHeader.get(groupPosition).getVitals().size();
            int count = 1;
            int tempSize = size - (size % 3);
            for (int i = 0; i < size; i++) {
                vital.add(mListDataHeader.get(groupPosition).getVitals().get(i));
                if (tempSize > i) {
                    if (count == 3) {
                        childViewHolder.tableLayout.addView(addVitalsTableRow(vital, groupPosition));
                        vital.clear();
                        count = 1;
                    } else
                        count++;
                } else if (count == size % 3) {
                    childViewHolder.tableLayout.addView(addVitalsTableRow(vital, groupPosition));
                    vital.clear();
                    count = 1;
                } else count++;
            }

        } else if (headerName.contains(CHILD_TYPE_ATTACHMENTS)) {

            childViewHolder.tableLayout.setVisibility(View.VISIBLE);
            childViewHolder.itemsLayout.setVisibility(View.GONE);
            childViewHolder.textView_comment.setVisibility(View.GONE);
            childViewHolder.tableLayout.removeAllViews();

            List<VisitCommonData> attachments = new ArrayList<>();
            int size1 = mListDataHeader.get(groupPosition).getCommonData().size();
            mListDataHeaderAllAttachmentCommonDataList = mListDataHeader.get(groupPosition).getCommonData();

            for (int i = 0; i < size1; i++) {
                attachments.add(mListDataHeader.get(groupPosition).getCommonData().get(i));
                int check = i + 1;
                if (check % 3 == 0 || check == size1) {
                    childViewHolder.tableLayout.addView(addAttachmentsTableRow(attachments));
                    attachments.clear();
                }
            }

        } else if (headerName.contains(CHILD_TYPE_IMAGE_ANNOTATIONS)) {

            childViewHolder.tableLayout.setVisibility(View.VISIBLE);
            childViewHolder.itemsLayout.setVisibility(View.GONE);
            childViewHolder.textView_comment.setVisibility(View.GONE);

            childViewHolder.tableLayout.removeAllViews();

            List<VisitCommonData> attachments = new ArrayList<>();
            int size1 = mListDataHeader.get(groupPosition).getCommonData().size();
            mListDataHeaderAllAttachmentCommonDataList = mListDataHeader.get(groupPosition).getCommonData();

            for (int i = 0; i < size1; i++) {
                attachments.add(mListDataHeader.get(groupPosition).getCommonData().get(i));
                int check = i + 1;
                if (check % 3 == 0 || check == size1) {
                    childViewHolder.tableLayout.addView(addAttachmentsTableRow(attachments));
                    attachments.clear();
                }
            }

        } else if (headerName.contains(CHILD_TYPE_MEDICAL_HISTORY)) {

            childViewHolder.tableLayout.setVisibility(View.VISIBLE);
            childViewHolder.itemsLayout.setVisibility(View.GONE);
            if (!mListDataHeader.get(groupPosition).getComment().equals(""))
                childViewHolder.textView_comment.setVisibility(View.VISIBLE);

            childViewHolder.tableLayout.removeAllViews();
            childViewHolder.textView_comment.setText(mListDataHeader.get(groupPosition).getComment());
            List<VisitCommonData> attachments = new ArrayList<>();

            if (!mListDataHeader.get(groupPosition).getCommonData().isEmpty()) {
                int size1 = mListDataHeader.get(groupPosition).getCommonData().size();
                mListDataHeaderAllAttachmentCommonDataList = mListDataHeader.get(groupPosition).getCommonData();

                for (int i = 0; i < size1; i++) {
                    attachments.add(mListDataHeader.get(groupPosition).getCommonData().get(i));
                    int check = i + 1;
                    if (check % 3 == 0 || check == size1) {
                        childViewHolder.tableLayout.addView(addAttachmentsTableRow(attachments));
                        attachments.clear();
                    }
                }
            }
        } else if (headerName.contains(CHILD_TYPE_PRESCRIPTIONS)) {
            childViewHolder.tableLayout.setVisibility(View.GONE);
            childViewHolder.itemsLayout.setVisibility(View.VISIBLE);
            childViewHolder.textView_comment.setVisibility(View.GONE);

            final List<VisitCommonData> childObject = mListDataHeader.get(groupPosition).getCommonData();

            String textToShow = "";

            String medicineTypeName = childObject.get(childPosition).getMedicineTypeName();
            String name = childObject.get(childPosition).getName();
            String dosage = childObject.get(childPosition).getDosage();

            if (!medicineTypeName.isEmpty())
                textToShow += medicineTypeName.charAt(0) + ". ";
            if (!name.isEmpty())
                textToShow += name + " ";
            if (!dosage.isEmpty())
                textToShow += dosage;

            childViewHolder.textView_name.setText(textToShow);
        } else {
            // set data and UI for other case study

            final List<VisitCommonData> childObject = mListDataHeader.get(groupPosition).getCommonData();
            childViewHolder.tableLayout.setVisibility(View.GONE);
            childViewHolder.itemsLayout.setVisibility(View.VISIBLE);
            childViewHolder.textView_comment.setVisibility(View.GONE);

            String textToShow = "";
            if (headerName.contains(CHILD_TYPE_ALLERGIES)) {
                if (!childObject.get(childPosition).getName().isEmpty()) {
                    if (childObject.get(childPosition).getName().contains("food")) {
                        textToShow = childObject.get(childPosition).getName();
                        if (!childObject.get(childPosition).getRemarks().isEmpty())
                            textToShow += "-" + childObject.get(childPosition).getRemarks();
                    } else {
                        textToShow = childObject.get(childPosition).getName();
                        if (!childObject.get(childPosition).getMedicinename().isEmpty())
                            textToShow += "-" + childObject.get(childPosition).getMedicinename();
                        if (!childObject.get(childPosition).getRemarks().isEmpty())
                            textToShow += "-" + childObject.get(childPosition).getRemarks();
                    }
                } else {
                    textToShow = childObject.get(childPosition).getName();
                }

            } else textToShow = childObject.get(childPosition).getName();

            childViewHolder.textView_name.setText(textToShow);

        }

        if (isLastChild)
            childViewHolder.divider.setVisibility(View.VISIBLE);
        else
            childViewHolder.divider.setVisibility(View.GONE);

        return convertView;
    }

    class ChildViewHolder {

        @BindView(R.id.divider)
        View divider;

        @BindView(R.id.table)
        LinearLayout tableLayout;

        @BindView(R.id.textView_name)
        TextView textView_name;

        @BindView(R.id.textView_comment)
        TextView textView_comment;


        @BindView(R.id.items_layout)
        LinearLayout itemsLayout;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    // Created dynamic grid function to list of  vitals
    private View addVitalsTableRow(final List<Vital> vital, final int groupPosition) {
        int i;
        String categoryForBpMax = "";
        String categoryForBpMin = "";
        LinearLayout tableRow = new LinearLayout(mContext);
        for (i = 0; i < vital.size(); i++) {

            View item = LayoutInflater.from(mContext)
                    .inflate(R.layout.vital_item_row, tableRow, false);
            LinearLayout vitalLinearlayout = (LinearLayout) item.findViewById(R.id.vitalCellLinearLayout);
            ImageView vitalImage = (ImageView) item.findViewById(R.id.vitalImage);
            TextView vital_name = (TextView) item.findViewById(R.id.vital_name);
            TextView noOfVitals = (TextView) item.findViewById(R.id.noOfVitals);

            final int finali = mPosition;
            //dialog is opened to see info of vitals , Note : BpMin and BpMax is together shown as Bp
            vitalLinearlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Vital vitalClickedObject = mListDataHeader.get(groupPosition).getVitals().get(finali);
                    showVitalDialog(mContext, vitalClickedObject.getUnitName(),
                            vitalClickedObject.getUnitValue(),
                            vitalClickedObject.getRanges(),
                            CommonMethods.getVitalIcons(vitalClickedObject.getIcon()),
                            vitalClickedObject.getCategory(),
                            vitalClickedObject.getDisplayName());
                }
            });

            if (mListDataHeader.get(groupPosition).getVitals().get(mPosition).getUnitName().equals(mContext.getString(R.string.bp))) {
                String category = mListDataHeader.get(groupPosition).getVitals().get(mPosition).getCategory();
                String[] categoryForBp = category.split(":");
                categoryForBpMax = categoryForBp[0];
                categoryForBpMin = categoryForBp.length == 2 ? categoryForBp[1] : "";

                String unit = mListDataHeader.get(groupPosition).getVitals().get(mPosition).getUnitValue();
                String[] unitForBp = unit.split("/");
                String unitForBpMax = unitForBp[0];
                String unitForBpMin = unitForBp.length > 1 ? unitForBp[1] : "";
                vitalImage.setImageResource(CommonMethods.getVitalIcons(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getIcon()));

                // Find Unit
                String unitString = "";
                if (unitForBpMax.contains(" "))
                    unitString = unitForBpMax.substring(unitForBpMax.indexOf(" "), unitForBpMax.length());

                // Find Digits
                String digitSystolic = unitForBpMax.split(" ")[0];
                String digitDiastolic = unitForBpMin.split(" ")[0];

                vital_name.setText(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getUnitName());

                noOfVitals.setText(Html.fromHtml(getUnitValueforBp(categoryForBpMin, categoryForBpMax, digitDiastolic, digitSystolic) + " <font color='#737373'>" + unitString + "</font>"));

            } else {
                //TextColor of vital unit is set according to category
                vitalImage.setImageResource(CommonMethods.getVitalIcons(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getIcon()));

                //---*************** Show vaital_display_name instead of unitName (EXCEPT BP CASE) : START
                //  vital_name.setText(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getUnitName());
                vital_name.setText(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getDisplayName());

                noOfVitals.setText(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getUnitValue());
                if (mListDataHeader.get(groupPosition).getVitals().get(mPosition).getCategory().equalsIgnoreCase(mContext.getResources().getString(R.string.severeRange))) {
                    noOfVitals.setTextColor(ContextCompat.getColor(mContext, R.color.Red));
                } else if (mListDataHeader.get(groupPosition).getVitals().get(mPosition).getCategory().equalsIgnoreCase(mContext.getResources().getString(R.string.normalRange))) {
                    noOfVitals.setTextColor(ContextCompat.getColor(mContext, R.color.range_green));
                } else if (mListDataHeader.get(groupPosition).getVitals().get(mPosition).getCategory().equalsIgnoreCase(mContext.getResources().getString(R.string.moderateRange))) {
                    noOfVitals.setTextColor(ContextCompat.getColor(mContext, R.color.range_yellow));
                } else {
                    noOfVitals.setTextColor(ContextCompat.getColor(mContext, R.color.view_detail_color));
                }
            }

            tableRow.addView(item);
            mPosition++;
        }
        return tableRow;
    }

    // Created dynamic grid function to list of  vitals
    @SuppressLint("CheckResult")
    private View addAttachmentsTableRow(final List<VisitCommonData> attachments) {

        LinearLayout tableRow = new LinearLayout(mContext);

        for (int i = 0; i < attachments.size(); i++) {

            View item = LayoutInflater.from(mContext)
                    .inflate(R.layout.attachment_item_row, tableRow, false);

            item.setTag(attachments.get(i));
            ImageView attachmentImage = (ImageView) item.findViewById(R.id.attachmentImage);
            TextView titleText = (TextView) item.findViewById(R.id.titleText);

            titleText.setText(attachments.get(i).getName());

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.dontAnimate();
            requestOptions.error(R.drawable.ic_file);
            requestOptions.placeholder(R.drawable.ic_file);

            Glide.with(mContext)
                    .load(attachments.get(i).getUrl())
                    .apply(requestOptions).thumbnail(0.5f)
                    .into(attachmentImage);

            //dialog is opened to see info of vitals , Note : BpMin and BpMax is together shown as Bp
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show Image or WebView.

                    String tag = ((VisitCommonData) v.getTag()).getUrl();
                    String fileExtension = tag.substring(tag.lastIndexOf("."));

                    if (fileExtension.contains(".doc") || fileExtension.contains(".odt") || fileExtension.contains(".ppt") || fileExtension.contains(".odp") || fileExtension.contains(".xls") || fileExtension.contains(".ods") || fileExtension.contains(".pdf")) {
                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra(mContext.getString(R.string.title_activity_selected_docs), tag);
                        intent.putExtra(mContext.getString(R.string.file_extension), fileExtension);
                        mContext.startActivity(intent);
                    } else {
                        // do stuff here
                        Intent intent = new Intent(mContext, MultipleImageWithSwipeAndZoomActivity.class);
                        intent.putExtra(RescribeConstants.DOCUMENTS, tag);
                        intent.putExtra(RescribeConstants.IS_URL, true);
                        intent.putParcelableArrayListExtra(RescribeConstants.ATTACHMENTS_LIST, new ArrayList<VisitCommonData>(mListDataHeaderAllAttachmentCommonDataList));
                        mContext.startActivity(intent);
                    }
                }
            });

            tableRow.addView(item);
        }
        return tableRow;
    }

    //set Unit Value of Bp according to category
    private String getUnitValueforBp(String categoryForBpMin, String categoryForBpMax, String forBpMin, String forBpMax) {
        String unitValue = "";
        if (categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.severeRange)) && categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.severeRange))) {
            String bpMax = "<font color='#FF0000'>" + forBpMax + "</font>";
            String bpMin = "<font color='#FF0000'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;


        } else if (categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.severeRange)) && categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.moderateRange))) {
            String bpMax = "<font color='#ff9500'>" + forBpMax + "</font>";
            String bpMin = "<font color='#FF0000'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.severeRange)) && categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.normalRange))) {
            String bpMax = "<font color='#178a00'>" + forBpMax + "</font>";
            String bpMin = "<font color='#FF0000'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.normalRange)) && categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.severeRange))) {
            String bpMax = "<font color='#FF0000'>" + forBpMax + "</font>";
            String bpMin = "<font color='#178a00'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.normalRange)) && categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.moderateRange))) {
            String bpMax = "<font color='#ff9500'>" + forBpMax + "</font>";
            String bpMin = "<font color='#178a00'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.normalRange)) && categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.normalRange))) {
            String bpMax = "<font color='#178a00'>" + forBpMax + "</font>";
            String bpMin = "<font color='#178a00'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.moderateRange)) && categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.severeRange))) {
            String bpMax = "<font color='#FF0000'>" + forBpMax + "</font>";
            String bpMin = "<font color='#ff9500'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.moderateRange)) && categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.moderateRange))) {
            String bpMax = "<font color='#ff9500'>" + forBpMax + "</font>";
            String bpMin = "<font color='#ff9500'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.moderateRange)) && categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.normalRange))) {
            String bpMax = "<font color='#178a00'>" + forBpMax + "</font>";
            String bpMin = "<font color='#ff9500'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.severeRange)) && categoryForBpMin.equalsIgnoreCase("")) {
            String bpMax = "<font color='#FF0000'>" + forBpMax + "</font>";
            String bpMin = "<font color='#737373'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.moderateRange)) && categoryForBpMin.equalsIgnoreCase("")) {
            String bpMax = "<font color='#ff9500'>" + forBpMax + "</font>";
            String bpMin = "<font color='#737373'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.normalRange)) && categoryForBpMin.equalsIgnoreCase("")) {
            String bpMax = "<font color='#178a00'>" + forBpMax + "</font>";
            String bpMin = "<font color='#737373'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMax.equalsIgnoreCase("") && categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.severeRange))) {
            String bpMax = "<font color='#737373'>" + forBpMax + "</font>";
            String bpMin = "<font color='#FF0000'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMax.equalsIgnoreCase("") && categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.moderateRange))) {
            String bpMax = "<font color='#737373'>" + forBpMax + "</font>";
            String bpMin = "<font color='#ff9500'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMax.equalsIgnoreCase("") && categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.normalRange))) {
            String bpMax = "<font color='#737373'>" + forBpMax + "</font>";
            String bpMin = "<font color='#178a00'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else {
            String bpMax = "<font color='#737373'>" + forBpMax + "</font>";
            String bpMin = "<font color='#737373'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;
        }
        return unitValue;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String caseName = this.mListDataHeader.get(groupPosition).getCaseDetailName().toLowerCase();
        if (caseName.contains(CHILD_TYPE_ATTACHMENTS) || caseName.contains(CHILD_TYPE_VITALS) || caseName.contains(CHILD_TYPE_IMAGE_ANNOTATIONS) || caseName.contains(CHILD_TYPE_MEDICAL_HISTORY))
            return 1;
        else
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
                    .inflate(R.layout.history_group_item_layout, parent, false);

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

        if (mListDataHeader.get(groupPosition).getCaseDetailName() != null) {
            groupViewHolder.lblListHeader.setText(CommonMethods.toCamelCase(mListDataHeader.get(groupPosition).getCaseDetailName()));
            groupViewHolder.mViewDetailIcon.setImageResource(CommonMethods.getCaseStudyIcons(mListDataHeader.get(groupPosition).getCaseDetailName()));

            if (mListDataHeader.get(groupPosition).getCommonData() != null) {
                mVisitDetailList = mListDataHeader.get(groupPosition).getCommonData();

                String caseDetailName = mListDataHeader.get(groupPosition).getCaseDetailName().toLowerCase();

                if (caseDetailName.contains(CHILD_TYPE_PRESCRIPTIONS)) {

                    if (!mListDataHeader.get(groupPosition).getCommonData().isEmpty()) {

                        String textToShow = "";

                        String medicineTypeName = mListDataHeader.get(groupPosition).getCommonData().get(0).getMedicineTypeName();
                        String name = mListDataHeader.get(groupPosition).getCommonData().get(0).getName();
                        String dosage = mListDataHeader.get(groupPosition).getCommonData().get(0).getDosage();

                        if (!medicineTypeName.isEmpty())
                            textToShow += medicineTypeName.charAt(0) + ". ";
                        if (!name.isEmpty())
                            textToShow += name + " ";
                        if (!dosage.isEmpty())
                            textToShow += dosage;

                        if (mListDataHeader.get(groupPosition).getCommonData().size() > 1)
                            textToShow += "...";
                        groupViewHolder.mDetailFirstPoint.setText(textToShow);

                    }

                } else if (caseDetailName.contains(CHILD_TYPE_VITALS)) {
                    if (!mListDataHeader.get(groupPosition).getVitals().isEmpty())
                        if (mVisitDetailList.get(0).getName().equalsIgnoreCase("bp")) {
                            String bpValue = mVisitDetailList.get(0).getVitalValue();

                            String[] unitDataObject = bpValue.split("/");
                            String unitBpMax = unitDataObject[0];
                            String unitBpMin = unitDataObject.length > 1 ? unitDataObject[1] : "";

                            // Find Unit
                            String unitString = "";
                            if (unitBpMax.contains(" "))
                                unitString = unitBpMax.substring(unitBpMax.indexOf(" "), unitBpMax.length());

                            // Find Digits
                            String digitSystolic = unitBpMax.split(" ")[0];
                            String digitDiastolic = unitBpMin.split(" ")[0];

                            // Final String
                            String finalString = digitSystolic + "/ " + digitDiastolic + " " + unitString + "...";

                            groupViewHolder.mDetailFirstPoint.setText(mVisitDetailList.get(0).getName() + " - " + finalString);

                        } else {
                            groupViewHolder.mDetailFirstPoint.setText(mVisitDetailList.get(0).getName() + "...");
                        }
                } else if (caseDetailName.contains(CHILD_TYPE_ALLERGIES)) {

                    if (!mListDataHeader.get(groupPosition).getCommonData().isEmpty()) {

                        String textToShow = "";

                        String name = mListDataHeader.get(groupPosition).getCommonData().get(0).getName();
                        String medicineName = mListDataHeader.get(groupPosition).getCommonData().get(0).getMedicinename();
                        String remarks = mListDataHeader.get(groupPosition).getCommonData().get(0).getRemarks();

                        if (!name.isEmpty()) {
                            if (name.contains("food")) {
                                textToShow += name;
                                if (!remarks.isEmpty())
                                    textToShow += "-" + remarks;
                            } else {
                                textToShow += name;
                                if (!medicineName.isEmpty())
                                    textToShow += "-" + medicineName;
                                if (!remarks.isEmpty())
                                    textToShow += "-" + remarks;
                            }
                        } else {
                            textToShow += name;
                        }

                        if (textToShow.length() > TEXT_LIMIT)
                            groupViewHolder.mDetailFirstPoint.setText(textToShow.substring(0, TEXT_LIMIT - 1) + "...");
                        else groupViewHolder.mDetailFirstPoint.setText(textToShow);

                    }

                } else if (caseDetailName.contains(CHILD_TYPE_MEDICAL_HISTORY)) {
                    if (mListDataHeader.get(groupPosition).getCommonData().isEmpty()) {
                        String text = mListDataHeader.get(groupPosition).getComment();
                        if (text.length() > TEXT_LIMIT)
                            groupViewHolder.mDetailFirstPoint.setText(text.substring(0, TEXT_LIMIT - 1) + "...");
                        else groupViewHolder.mDetailFirstPoint.setText(text);
                    } else {

                        if (mListDataHeader.get(groupPosition).getComment().equals("")) {
                            if (mVisitDetailList.size() > 1) {
                                int length = mVisitDetailList.get(0).getName().length();
                                String text = mVisitDetailList.get(0).getName().substring(0, length < TEXT_LIMIT ? length - 1 : TEXT_LIMIT - 1) + "...";
                                groupViewHolder.mDetailFirstPoint.setText(text);
                            } else {
                                String text = mVisitDetailList.get(0).getName();
                                String remarks = mVisitDetailList.get(0).getRemarks();
                                String combineString = remarks.isEmpty() ? text : text + " - " + remarks;
                                if (combineString.length() > TEXT_LIMIT)
                                    groupViewHolder.mDetailFirstPoint.setText(combineString.substring(0, TEXT_LIMIT - 1) + "...");
                                else groupViewHolder.mDetailFirstPoint.setText(combineString);
                            }
                        } else {
                            String text = mListDataHeader.get(groupPosition).getComment();
                            if (text.length() > TEXT_LIMIT)
                                groupViewHolder.mDetailFirstPoint.setText(text.substring(0, TEXT_LIMIT - 1) + "...");
                            else groupViewHolder.mDetailFirstPoint.setText(text);
                        }


                    }
                } else if (mVisitDetailList.size() > 1) {
                    int length = mVisitDetailList.get(0).getName().length();
                    String text = mVisitDetailList.get(0).getName().substring(0, length < TEXT_LIMIT ? length - 1 : TEXT_LIMIT - 1) + "...";
                    groupViewHolder.mDetailFirstPoint.setText(text);
                } else {

                    String text = mVisitDetailList.get(0).getName();

                    String remarks = mVisitDetailList.get(0).getRemarks();
                    String combineString = remarks.isEmpty() ? text : text + " - " + remarks;

                    if (combineString.length() > TEXT_LIMIT)
                        groupViewHolder.mDetailFirstPoint.setText(combineString.substring(0, TEXT_LIMIT - 1) + "...");
                    else groupViewHolder.mDetailFirstPoint.setText(combineString);


                }
            }
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

    class GroupViewHolder {
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

    public Dialog showVitalDialog(Context context, String unitName, String unitValue, List<Range> rangeList, Integer drawable, String category, String vitalDisplayName) {

        final Dialog dialog = new Dialog(context);
        String categoryForBpMax = "";
        String categoryBpMin = "";
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.vitals_dialog_layout);
        String normal = "";
        String moderate = "";
        String severe = "";
        String noncategory = "";
        String normalBpmin = "";
        String moderateBpmin = "";
        String severeBpmin = "";
        String noncategoryBpMin = "";
        String normalBpNull = "";
        String moderateBpNull = "";
        String severeBpNull = "";
        String noncategoryBpNull = "";


        LinearLayout vitalsDialogLayout = (LinearLayout) dialog.findViewById(R.id.vitalsDialogLayout);
        LinearLayout normalBpMaxRangeLayout = (LinearLayout) dialog.findViewById(R.id.normalRangeLayout);
        LinearLayout showVitalUnitNameIconLayout = (LinearLayout) dialog.findViewById(R.id.showVitalUnitNameIconLayout);
        LinearLayout moderateBpMaxRangeLayout = (LinearLayout) dialog.findViewById(R.id.moderateRangeLayout);
        LinearLayout severeBpMaxRangeLayout = (LinearLayout) dialog.findViewById(R.id.severeRangeLayout);
        LinearLayout nonCategoryRangeLayout = (LinearLayout) dialog.findViewById(R.id.nonCategoryRangeLayout);

        LinearLayout showVitalNameLayout = (LinearLayout) dialog.findViewById(R.id.showVitalNameLayout);
        TextView normalBpMaxRange = (TextView) dialog.findViewById(R.id.normalRange);
        TextView moderateBpMaxRange = (TextView) dialog.findViewById(R.id.moderateRange);
        TextView severeBpMaxRange = (TextView) dialog.findViewById(R.id.severeRange);
        TextView nonCategoryBpMaxRange = (TextView) dialog.findViewById(R.id.nonCategoryRange);

        LinearLayout showVitalRangeLayout = (LinearLayout) dialog.findViewById(R.id.showVitalRangeLayout);
        TextView vitalTypeNameDialog = (TextView) dialog.findViewById(R.id.vitalTypeNameDialog);
        TextView noOfVitalsTypeDialog = (TextView) dialog.findViewById(R.id.noOfVitalsTypeDialog);
        TextView normalSubTypeRange = (TextView) dialog.findViewById(R.id.normalSubTypeRange);
        TextView moderateSubTypeRange = (TextView) dialog.findViewById(R.id.moderateSubTypeRange);
        TextView severeSubTypeRange = (TextView) dialog.findViewById(R.id.severeSubTypeRange);
        TextView nonCategorySubTypeRange = (TextView) dialog.findViewById(R.id.nonCategorySubTypeRange);


        TextView noOfVitalsDialog = (TextView) dialog.findViewById(R.id.noOfVitalsDialog);
        TextView vitalName = (TextView) dialog.findViewById(R.id.vitalNameDialog);
        LinearLayout bpMinLayout = (LinearLayout) dialog.findViewById(R.id.bpMinLayout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        for (int i = 0; i < rangeList.size(); i++) {
            if (rangeList.get(i).getNameOfVital() != null) {
                if (rangeList.get(i).getNameOfVital().equalsIgnoreCase(context.getString(R.string.bp_max))) {
                    if (rangeList.get(i).getCategory().equalsIgnoreCase(context.getString(R.string.normalRange))) {
                        if (normal.equals("")) {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            normal += finalString;
                            normalBpMaxRange.setText(normal);
                        } else {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            normal += ", " + finalString;
                            normalBpMaxRange.setText(normal);
                        }
                    } else if (rangeList.get(i).getCategory().equalsIgnoreCase(context.getString(R.string.moderateRange))) {
                        if (moderate.equals("")) {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            moderate += finalString;
                            moderateBpMaxRange.setText(moderate);
                        } else {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            moderate += ", " + finalString;
                            moderateBpMaxRange.setText(moderate);
                        }
                    } else if (rangeList.get(i).getCategory().equalsIgnoreCase(context.getString(R.string.severeRange))) {
                        if (severe.equals("")) {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            severe += finalString;
                            severeBpMaxRange.setText(severe);
                        } else {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            severe += ", " + finalString;
                            severeBpMaxRange.setText(severe);

                        }
                    } else {
                        if (noncategory.equals("")) {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            noncategory += finalString;
                            nonCategoryBpMaxRange.setText(noncategory);
                        } else {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            noncategory += ", " + finalString;
                            nonCategoryBpMaxRange.setText(noncategory);

                        }
                    }
                } else if (rangeList.get(i).getNameOfVital().equalsIgnoreCase(context.getString(R.string.bp_min))) {

                    if (rangeList.get(i).getCategory().equalsIgnoreCase(context.getString(R.string.normalRange))) {
                        if (normalBpmin.equals("")) {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            normalBpmin += finalString;
                            normalSubTypeRange.setText(normalBpmin);
                        } else {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            normalBpmin += ", " + finalString;
                            normalSubTypeRange.setText(normalBpmin);
                        }
                    } else if (rangeList.get(i).getCategory().equalsIgnoreCase(context.getString(R.string.moderateRange))) {
                        if (moderateBpmin.equals("")) {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            moderateBpmin += finalString;
                            moderateSubTypeRange.setText(moderateBpmin);
                        } else {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            moderateBpmin += ", " + finalString;
                            moderateSubTypeRange.setText(moderateBpmin);
                        }
                    } else if (rangeList.get(i).getCategory().equalsIgnoreCase(context.getString(R.string.severeRange))) {
                        if (severeBpmin.equals("")) {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            severeBpmin += finalString;
                            severeSubTypeRange.setText(severeBpmin);
                        } else {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            severeBpmin += ", " + finalString;
                            severeSubTypeRange.setText(severeBpmin);

                        }
                    } else {
                        if (noncategoryBpMin.equals("")) {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            noncategoryBpMin += finalString;
                            nonCategorySubTypeRange.setText(noncategoryBpMin);
                        } else {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            noncategoryBpMin += ", " + finalString;
                            nonCategorySubTypeRange.setText(noncategoryBpMin);

                        }
                    }

                }
            } else if (rangeList.get(i).getNameOfVital() == null) {
                if (rangeList.get(i).getCategory().equalsIgnoreCase(context.getString(R.string.normalRange))) {
                    if (normalBpNull.equals("")) {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                        normalBpNull += finalString;
                        normalSubTypeRange.setText(normalBpNull);
                    } else {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                        normalBpNull += ", " + finalString;
                        normalSubTypeRange.setText(normalBpNull);
                    }
                } else if (rangeList.get(i).getCategory().equalsIgnoreCase(context.getString(R.string.moderateRange))) {
                    if (moderateBpNull.equals("")) {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                        moderateBpNull += finalString;
                        moderateSubTypeRange.setText(moderateBpNull);
                    } else {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                        moderateBpNull += ", " + finalString;
                        moderateSubTypeRange.setText(moderateBpNull);
                    }
                } else if (rangeList.get(i).getCategory().equalsIgnoreCase(context.getString(R.string.severeRange))) {
                    if (severeBpNull.equals("")) {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                        severeBpNull += finalString;
                        severeSubTypeRange.setText(severeBpNull);
                    } else {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                        severeBpNull += ", " + finalString;
                        severeSubTypeRange.setText(severeBpNull);

                    }
                } else {
                    if (noncategoryBpNull.equals("")) {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                        noncategoryBpNull += finalString;
                        nonCategorySubTypeRange.setText(noncategoryBpNull);
                    } else {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                        noncategoryBpNull += ", " + finalString;
                        nonCategorySubTypeRange.setText(noncategoryBpNull);

                    }
                }
            }
        }
        if (unitName.equals(context.getString(R.string.bp))) {
            String[] unitDataObject = unitValue.split("/");
            String unitBpMax = unitDataObject[0];
            String unitBpMin = unitDataObject.length == 2 ? unitDataObject[1] : "";
            showVitalNameLayout.setVisibility(View.VISIBLE);
            showVitalRangeLayout.setVisibility(View.VISIBLE);

            vitalName.setText(context.getString(R.string.systolic_pressure));
            if (!category.equals(":")) {
                String[] categoryForBp = category.split(":");
                categoryForBpMax = categoryForBp[0];
                categoryBpMin = categoryForBp.length == 2 ? categoryForBp[1] : "";
            }
            if (categoryForBpMax.equalsIgnoreCase(context.getString(R.string.severeRange))) {
                noOfVitalsDialog.setTextColor(ContextCompat.getColor(context, R.color.Red));
            } else if (categoryForBpMax.equalsIgnoreCase(context.getString(R.string.normalRange))) {
                noOfVitalsDialog.setTextColor(ContextCompat.getColor(context, R.color.range_green));
            } else if (categoryForBpMax.equalsIgnoreCase(context.getString(R.string.moderateRange))) {
                noOfVitalsDialog.setTextColor(ContextCompat.getColor(context, R.color.range_yellow));
            } else {
                noOfVitalsDialog.setTextColor(ContextCompat.getColor(context, R.color.view_detail_color));
            }

            if (categoryBpMin.equalsIgnoreCase(context.getString(R.string.severeRange))) {
                noOfVitalsTypeDialog.setTextColor(ContextCompat.getColor(context, R.color.Red));
            } else if (categoryBpMin.equalsIgnoreCase(context.getString(R.string.normalRange))) {
                noOfVitalsTypeDialog.setTextColor(ContextCompat.getColor(context, R.color.range_green));
            } else if (categoryBpMin.equalsIgnoreCase(context.getString(R.string.moderateRange))) {
                noOfVitalsTypeDialog.setTextColor(ContextCompat.getColor(context, R.color.range_yellow));
            } else {
                noOfVitalsTypeDialog.setTextColor(ContextCompat.getColor(context, R.color.view_detail_color));
            }
            noOfVitalsDialog.setText(unitBpMax);
            noOfVitalsTypeDialog.setText(unitBpMin);
            vitalTypeNameDialog.setText(context.getString(R.string.diastolic_pressure));
            if (normalBpMaxRange.getText().toString().trim().length() == 0) {
                normalBpMaxRangeLayout.setVisibility(View.GONE);
            }
            if (moderateBpMaxRange.getText().toString().trim().length() == 0) {
                moderateBpMaxRangeLayout.setVisibility(View.GONE);
            }
            if (severeBpMaxRange.getText().toString().trim().length() == 0) {
                severeBpMaxRangeLayout.setVisibility(View.GONE);
            }
            if (nonCategoryBpMaxRange.getText().toString().trim().length() == 0) {
                nonCategoryRangeLayout.setVisibility(View.GONE);
            }
            if (normalBpMaxRange.getText().toString().trim().length() == 0 && moderateBpMaxRange.getText().toString().trim().length() == 0 && severeBpMaxRange.getText().toString().trim().length() == 0 && nonCategoryBpMaxRange.getText().toString().trim().length() == 0) {
                showVitalRangeLayout.setVisibility(View.GONE);
                showVitalNameLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.vitals_curve_white_bg));
                bpMinLayout.setVisibility(View.GONE);
                showVitalUnitNameIconLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.vitals_curve_grey_bg));
            }
            if (normalSubTypeRange.getText().toString().trim().length() == 0) {
                LinearLayout normalRangeLayout = (LinearLayout) dialog.findViewById(R.id.normalSubTypeRangeLayout);
                normalRangeLayout.setVisibility(View.GONE);
            }
            if (moderateSubTypeRange.getText().toString().trim().length() == 0) {
                LinearLayout moderateRangeLayout = (LinearLayout) dialog.findViewById(R.id.moderateSubTypeRangeLayout);
                moderateRangeLayout.setVisibility(View.GONE);
            }
            if (severeSubTypeRange.getText().toString().trim().length() == 0) {
                LinearLayout severeRangeLayout = (LinearLayout) dialog.findViewById(R.id.severeSubTypeRangeLayout);
                severeRangeLayout.setVisibility(View.GONE);
            }
            if (nonCategorySubTypeRange.getText().toString().trim().length() == 0) {
                LinearLayout noncategorySubTypeRangeLayout = (LinearLayout) dialog.findViewById(R.id.noncategorySubTypeRangeLayout);
                noncategorySubTypeRangeLayout.setVisibility(View.GONE);
            }
            if (normalSubTypeRange.getText().toString().trim().length() == 0 && moderateSubTypeRange.getText().toString().trim().length() == 0 && severeSubTypeRange.getText().toString().trim().length() == 0 && nonCategorySubTypeRange.getText().toString().trim().length() == 0) {
                bpMinLayout.setVisibility(View.GONE);
                showVitalUnitNameIconLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.vitals_curve_grey_bg));
            }

        } else {

            showVitalNameLayout.setVisibility(View.GONE);
            showVitalRangeLayout.setVisibility(View.GONE);
            noOfVitalsDialog.setText(unitValue);
            //---*************** Show vaital_display_name instead of unitName : START
            //vitalName.setText(unitName);
            vitalName.setText(vitalDisplayName);
            //---*************** Show vaital_display_name instead of unitName : END
            if (category.equalsIgnoreCase(context.getString(R.string.severeRange))) {
                noOfVitalsDialog.setTextColor(ContextCompat.getColor(context, R.color.Red));
            } else if (category.equalsIgnoreCase(context.getString(R.string.normalRange))) {
                noOfVitalsDialog.setTextColor(ContextCompat.getColor(context, R.color.range_green));
            } else if (category.equalsIgnoreCase(context.getString(R.string.moderateRange))) {
                noOfVitalsDialog.setTextColor(ContextCompat.getColor(context, R.color.range_yellow));
            }
            if (normalSubTypeRange.getText().toString().trim().length() == 0) {
                LinearLayout normalRangeLayout = (LinearLayout) dialog.findViewById(R.id.normalSubTypeRangeLayout);
                normalRangeLayout.setVisibility(View.GONE);
            }
            if (moderateSubTypeRange.getText().toString().trim().length() == 0) {
                LinearLayout moderateRangeLayout = (LinearLayout) dialog.findViewById(R.id.moderateSubTypeRangeLayout);
                moderateRangeLayout.setVisibility(View.GONE);
            }
            if (severeSubTypeRange.getText().toString().trim().length() == 0) {
                LinearLayout severeRangeLayout = (LinearLayout) dialog.findViewById(R.id.severeSubTypeRangeLayout);
                severeRangeLayout.setVisibility(View.GONE);
            }
            if (nonCategorySubTypeRange.getText().toString().trim().length() == 0) {
                LinearLayout noncategorySubTypeRangeLayout = (LinearLayout) dialog.findViewById(R.id.noncategorySubTypeRangeLayout);
                noncategorySubTypeRangeLayout.setVisibility(View.GONE);
            }
            if (normalSubTypeRange.getText().toString().trim().length() == 0 && moderateSubTypeRange.getText().toString().trim().length() == 0 && severeSubTypeRange.getText().toString().trim().length() == 0) {
                bpMinLayout.setVisibility(View.GONE);
                showVitalUnitNameIconLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.vital_curve_allcorners_grey));
            }
        }

        ((ImageView) dialog.findViewById(R.id.vitalImageDialog)).setImageResource(drawable);

        vitalsDialogLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(true);
        dialog.show();

        return dialog;
    }

    private String getSortedRangeValues(String category, String operator, String value, String min, String max) {
        String range = "";
        if (category.equalsIgnoreCase(mContext.getString(R.string.normalRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.less))) {
            range = mContext.getString(R.string.less_than_sign) + value;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.normalRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.equal))) {
            range = min + mContext.getString(R.string.dash) + max;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.normalRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.greater))) {
            range = mContext.getString(R.string.greater_than_sign) + value;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.moderateRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.less))) {
            range = mContext.getString(R.string.less_than_sign) + value;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.moderateRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.equal))) {
            range = min + mContext.getString(R.string.dash) + max;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.moderateRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.greater))) {
            range = mContext.getString(R.string.greater_than_sign) + value;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.severeRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.less))) {
            range = mContext.getString(R.string.less_than_sign) + value;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.severeRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.equal))) {
            range = min + mContext.getString(R.string.dash) + max;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.severeRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.greater))) {
            range = mContext.getString(R.string.greater_than_sign) + value;
        } else if ((!category.equalsIgnoreCase(mContext.getString(R.string.normalRange)) || !category.equalsIgnoreCase(mContext.getString(R.string.moderateRange)) || !category.equalsIgnoreCase(mContext.getString(R.string.severeRange))) && operator.equalsIgnoreCase(mContext.getString(R.string.less))) {
            range = mContext.getString(R.string.less_than_sign) + value;
        } else if ((!category.equalsIgnoreCase(mContext.getString(R.string.normalRange)) || !category.equalsIgnoreCase(mContext.getString(R.string.moderateRange)) || !category.equalsIgnoreCase(mContext.getString(R.string.severeRange))) && operator.equalsIgnoreCase(mContext.getString(R.string.equal))) {
            range = min + mContext.getString(R.string.dash) + max;
        } else if ((!category.equalsIgnoreCase(mContext.getString(R.string.normalRange)) || !category.equalsIgnoreCase(mContext.getString(R.string.moderateRange)) || !category.equalsIgnoreCase(mContext.getString(R.string.severeRange))) && operator.equalsIgnoreCase(mContext.getString(R.string.greater))) {
            range = mContext.getString(R.string.greater_than_sign) + value;
        }

        return range;
    }

    public List<PatientHistory> getListDataList() {
        return mListDataHeader;
    }

}