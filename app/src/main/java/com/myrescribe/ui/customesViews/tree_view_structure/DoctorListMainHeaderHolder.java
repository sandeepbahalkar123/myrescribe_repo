package com.myrescribe.ui.customesViews.tree_view_structure;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myrescribe.R;
import com.unnamed.b.atv.model.TreeNode;
import com.myrescribe.R;
import com.myrescribe.ui.customesViews.CustomTextView;

/**
 * Created by Bogdan Melnychuk on 2/15/15.
 */
public class DoctorListMainHeaderHolder extends TreeNode.BaseNodeViewHolder<DoctorListIconTreeItemHolder.IconTreeItem> {
    private Context mContext;
    TextView date;

    ImageView circularBulletChildElement, circularBulletMainElement;
    TextView upperLine, lowerLine;
    TextView doctorName;
    LinearLayout parentDataContainer;

  /*  public DoctorListMainHeaderHolder(Context context, boolean isDefaultExpanded) {
        this(context, isDefaultExpanded, (int) (context.getResources().getDimension(R.dimen.dp10) / context.getResources().getDisplayMetrics().density));
    }*/

    public DoctorListMainHeaderHolder(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public View createNodeView(final TreeNode node, DoctorListIconTreeItemHolder.IconTreeItem value) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.item_doctor_list_layout, null, false);

        date = (TextView) view.findViewById(R.id.date);
        doctorName = ( TextView) view.findViewById(R.id.doctorName);

        circularBulletMainElement = (ImageView) view.findViewById(R.id.circularBulletMainElement);
        circularBulletChildElement = (ImageView) view.findViewById(R.id.circularBulletChildElement);

        parentDataContainer = (LinearLayout) view.findViewById(R.id.parentDataContainer);
        upperLine = (TextView) view.findViewById(R.id.upperLine);
        lowerLine = (TextView) view.findViewById(R.id.lowerLine);

        doctorName.setText(value.mDataObject.getDoctorName());

        if (value.mDataObject.isStartElement()) {
            date.setText(value.mDataObject.getDate());
            date.setVisibility(View.VISIBLE);

            circularBulletMainElement.setVisibility(View.VISIBLE);
            circularBulletChildElement.setVisibility(View.GONE);

        } else {
            date.setVisibility(View.INVISIBLE);
            date.setVisibility(View.INVISIBLE);
            circularBulletChildElement.setVisibility(View.VISIBLE);
            circularBulletMainElement.setVisibility(View.GONE);

        }

        return view;
    }

    @Override
    public void toggleSelectionMode(boolean editModeEnabled) {
        //    nodeSelector.setVisibility(editModeEnabled ? View.VISIBLE : View.GONE);
        //  nodeSelector.setChecked(mNode.isSelected());
    }
}
