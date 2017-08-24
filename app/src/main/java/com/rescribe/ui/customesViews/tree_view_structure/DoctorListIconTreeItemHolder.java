/*
package com.rescribe.ui.customesViews.tree_view_structure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.doctors.doctor_info.DoctorDetail;
import com.unnamed.b.atv.model.TreeNode;

*/
/**
 * Created by Bogdan Melnychuk on 2/12/15.
 *//*

public class DoctorListIconTreeItemHolder extends TreeNode.BaseNodeViewHolder<DoctorListIconTreeItemHolder.IconTreeItem> {
    private Context mContext;
    TextView date;

    ImageView circularBulletChildElement, circularBulletMainElement;
    TextView upperLine, lowerLine;
     TextView doctorName;
    LinearLayout parentDataContainer;

    public DoctorListIconTreeItemHolder(Context context) {
        super(context);
        this.mContext = context;

    }

    @Override
    public View createNodeView(final TreeNode node, IconTreeItem value) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.item_doctor_list_layout, null, false);

        date = (TextView) view.findViewById(R.id.date);
        doctorName = (TextView) view.findViewById(R.id.doctorName);

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
    public void toggle(boolean active) {
        //  arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
    }

    public static class IconTreeItem {
        public int icon;
        public DoctorDetail mDataObject;

        public IconTreeItem(int icon, DoctorDetail dataObject) {
            this.icon = icon;
            this.mDataObject = dataObject;
        }
    }

}
*/
