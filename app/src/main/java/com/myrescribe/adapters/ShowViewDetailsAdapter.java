package com.myrescribe.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import com.myrescribe.model.Album;
import com.myrescribe.model.history.HistoryCommonDetails;
import com.myrescribe.model.visit_details.Diagnosi;
import com.myrescribe.util.CommonMethods;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowViewDetailsAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private Activity context;
    String[] firstRow = {
            "Weight",
            "BMI",
            "Heart Rate"
    } ;
    String[] secondRow = {
            "Blood Pressure",
            "Blood Glucose"
    };
    Integer[] firstRowImage = {
            R.drawable.weight,
            R.drawable.bmi_1,
            R.drawable.heart_rate,
    };
    Integer[] SecondRowImage = {
            R.drawable.blood_pressure,
            R.drawable.layer_10
    };
    private List<Album> albumList = new ArrayList<>();
    private static final String CHILD_TYPE_1 = "Vitals";
    private ArrayList<HistoryCommonDetails> mHistoryCommonDetailses;
    private ArrayList<String> mListDataHeader; // header titles

    // child data in format of header title, child title
    private HashMap<String, ArrayList<Diagnosi>> mListDataChild;

    public ShowViewDetailsAdapter(Context context, ArrayList<String> listDataHeader,
                                  HashMap<String, ArrayList<Diagnosi>> listChildData) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .get(childPosititon);
    }


    public ArrayList<Diagnosi> getChildList(int groupPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //final String incoming_text = (String) getChild(groupPosition, childPosition);
        final Diagnosi childObject = (Diagnosi) getChild(groupPosition, childPosition);
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

        Integer childType = getChildType(groupPosition, childPosition);
        String headerName = getGroupName(groupPosition);
        // We need to create a new "cell container"
//        if (convertView == null) {
            switch (headerName) {
                case CHILD_TYPE_1:
                    convertView = inflater.inflate(R.layout.vitals_main_activity, null);
                    convertView.setTag(headerName);
                    TableLayout tableLayout = (TableLayout) convertView.findViewById(R.id.table);
                    View divider = (View)convertView.findViewById(R.id.adapter_divider);
                    tableLayout.removeAllViews();
                    tableLayout.addView(addTableRow(3,firstRow,firstRowImage));
                    tableLayout.addView(addTableRow(2, secondRow, SecondRowImage));
                    if(isLastChild){
                        divider.setVisibility(View.VISIBLE);
                    }
                    else{
                        divider.setVisibility(View.GONE);
                    }
                    break;

                default:
                    convertView = inflater.inflate(R.layout.history_child_item_layout, null);
                    convertView.setTag(headerName);
                    TextView txtListChild = (TextView) convertView.findViewById(R.id.textView_name);
                    View dividerLine = (View)convertView.findViewById(R.id.adapter_divider_bottom);
                    txtListChild.setText(childObject.getName());

                    if(isLastChild){
                        dividerLine.setVisibility(View.VISIBLE);
                    }
                    else{
                        dividerLine.setVisibility(View.GONE);
                    }
                    break;
            }
//        }
       /* // We'll reuse the existing one
        else {
            // There is nothing to do here really we just need to set the content of view which we do in both cases
        }*/

    return convertView;
}

    private View addTableRow(int columnCount, String[] rowText, Integer[] rowImage) {
        TableRow tableRow = new TableRow(mContext);

        for (int i = 0; i < columnCount; i++) {
            View item = LayoutInflater.from(mContext)
                    .inflate(R.layout.item, tableRow, false);
            ImageView vitalImage = (ImageView)item.findViewById(R.id.vitalImage);
            TextView vital_name = (TextView)item.findViewById(R.id.vital_name) ;
            vitalImage.setImageResource(rowImage[i]);
            vital_name.setText(rowText[i]);
            tableRow.addView(item);
        }
        return tableRow;
    }

    public String getGroupName(int groupPosition) {
        String headerTitle = (String) getGroup(groupPosition);
        return headerTitle;
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
        if (isExpanded) {
            groupViewHolder.mDetailFirstPoint.setVisibility(View.GONE);
            groupViewHolder.mHeadergroupDivider.setVisibility(View.VISIBLE);
            groupViewHolder.mDivider.setVisibility(View.GONE);

        } else {
            groupViewHolder.mDetailFirstPoint.setVisibility(View.VISIBLE);
            groupViewHolder.mHeadergroupDivider.setVisibility(View.GONE);
            groupViewHolder.mDivider.setVisibility(View.VISIBLE);
        }
        groupViewHolder.lblListHeader.setText(headerTitle);
        groupViewHolder.mViewDetailIcon.setImageResource(CommonMethods.getVisitDetailsIcons(headerTitle, mContext));
        ArrayList<Diagnosi> historyCommonDetailses = getChildList(groupPosition);
        groupViewHolder.mDetailFirstPoint.setText(test(historyCommonDetailses.get(0).getName())+".......");
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

static class ChildViewHolderVitals {
    //---------

    @BindView(R.id.recycler_view)
    RecyclerView mRecycler_view;


    ChildViewHolderVitals(View view) {
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

  public String test(String t){
    String o = null;
        if(t.length() >= 30){
             o = t.substring(0,30);
            System.out.println(o);
          /*  String x = t.substring(5,t.length());
            test(x);*/
            return o;
        }
        else{
            System.out.println(t);
            return t;
        }

    }

}