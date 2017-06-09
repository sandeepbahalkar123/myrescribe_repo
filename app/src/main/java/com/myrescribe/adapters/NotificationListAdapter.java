package com.myrescribe.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.Medicine;
import com.myrescribe.model.prescription_response_model.PrescriptionData;
import com.myrescribe.ui.activities.NotificationActivity;
import com.myrescribe.ui.customesViews.CustomTextView;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;
import com.myrescribe.util.SwipeDismissTouchListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ganesh on 10/5/17.
 */

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ListViewHolder> {

    private static final String DINNER = "dinner";
    private static final String LUNCH = "lunch";
    private static final String BREAK_FAST = "breakfast";
   /* private final String medicineSlot;
    private final String date;*/
    private final String time[];
    private final ArrayList<Medicine> medicines;

    private ArrayList<PrescriptionData> mDataSet;
    NotificationActivity mRowClickListener;
    Context mContext;
    Boolean isPatientLogin;
    String mGetMealTime;

//    private boolean isHeaderExpand = true;

    public NotificationListAdapter(Context context, ArrayList<PrescriptionData> dataSet, Boolean isPatientLogin, String mGetMealTime, String medicineSlot, String date, String time[], ArrayList<Medicine> medicines) {
        this.mDataSet = dataSet;
        this.mContext = context;
        this.isPatientLogin = isPatientLogin;
        this.mGetMealTime = mGetMealTime;

        /*this.medicineSlot = medicineSlot;
        this.date = date;*/
        this.time = time;
        this.medicines = medicines;
    }

    @Override
    public NotificationListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.notification_list_item, parent, false);
        return new NotificationListAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotificationListAdapter.ListViewHolder holder, final int position) {

        /*if (position == 0) {
            holder.headerLayout.removeAllViews();
            addHeader(holder.headerLayout);
        } else {
            holder.headerLayout.removeAllViews();
        }*/

        holder.dateTextView.setText(mDataSet.get(position).getDate());
        holder.titleTextView.setText(CommonMethods.getDayFromDate("dd-MM-yyyy", mDataSet.get(position).getDate()));

        boolean isItemHeader = false;

        holder.slotLayout.removeAllViews();
        if (mDataSet.get(position).isDinnerThere()) {
            addSlotCards(holder.slotLayout, DINNER, position);
            isItemHeader = true;
        }
        if (mDataSet.get(position).isLunchThere()) {
            addSlotCards(holder.slotLayout, LUNCH, position);
            isItemHeader = true;
        }
        if (mDataSet.get(position).isBreakThere()) {
            addSlotCards(holder.slotLayout, BREAK_FAST, position);
            isItemHeader = true;
        }

        if (position == 0) {
            if (isItemHeader)
                holder.list_item.setVisibility(View.VISIBLE);
            else holder.list_item.setVisibility(View.GONE);
        }
    }

    // Added Slots

    private void addSlotCards(final ViewGroup parent, final String slotType, final int position) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.notification_slot_card, parent, false);

        CustomTextView slotTextView = (CustomTextView) view.findViewById(R.id.slotTextView);
        CustomTextView slotTimeTextView = (CustomTextView) view.findViewById(R.id.slotTimeTextView);
        CustomTextView slotQuestionTextView = (CustomTextView) view.findViewById(R.id.slotQuestionTextView);
        CheckBox selectView = (CheckBox) view.findViewById(R.id.selectView);
        LinearLayout slotTabletListLayout = (LinearLayout) view.findViewById(R.id.slotTabletListLayout);
        CardView slotCard = (CardView) view.findViewById(R.id.slotCard);

        ImageView trangleIconBottom = (ImageView) view.findViewById(R.id.trangleIconBottom);
        ImageView trangleIconTop = (ImageView) view.findViewById(R.id.trangleIconTop);

        switch (slotType) {
            case DINNER:
                slotTextView.setText(mContext.getResources().getString(R.string.dinner_medication));
                slotTimeTextView.setText(time[0]);
                addTabletView(slotTabletListLayout, position);
                if (mDataSet.get(position).isDinnerExpanded()) {
                    slotTabletListLayout.setVisibility(View.VISIBLE);
                    selectView.setVisibility(View.INVISIBLE);
                    trangleIconBottom.setVisibility(View.INVISIBLE);
                    trangleIconTop.setVisibility(View.VISIBLE);
                } else {
                    slotTabletListLayout.setVisibility(View.GONE);
                    selectView.setVisibility(View.VISIBLE);
                    trangleIconBottom.setVisibility(View.VISIBLE);
                    trangleIconTop.setVisibility(View.INVISIBLE);
                }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDataSet.get(position).isDinnerExpanded())
                            mDataSet.get(position).setDinnerExpanded(false);
                        else mDataSet.get(position).setDinnerExpanded(true);
//                        CommonMethods.showToast(mContext, "Clicked Card " + slotType);
                        notifyItemChanged(position);
                    }
                });

                SwipeDismissTouchListener swipeDismissTouchListener = new SwipeDismissTouchListener(
                        view,
                        null,
                        new SwipeDismissTouchListener.OnDismissCallback() {
                            @Override
                            public void onDismiss(View view, Object token) {
                                if (mDataSet.size() > position)
                                mDataSet.get(position).setDinnerThere(false);
                                parent.removeView(view);
                                if (position != 0) {
                                    if (!mDataSet.get(position).isDinnerThere() && !mDataSet.get(position).isLunchThere() && !mDataSet.get(position).isBreakThere()) {
                                        mDataSet.remove(position);
                                        notifyDataSetChanged();
                                    }
                                } else notifyItemChanged(position);
                                CommonMethods.showToast(mContext, "Removed " + slotType);
                            }
                        });

                view.setOnTouchListener(swipeDismissTouchListener);
                break;
            case LUNCH:
                slotTextView.setText(mContext.getResources().getString(R.string.lunch_medication));
                slotTimeTextView.setText(time[1]);
                addTabletView(slotTabletListLayout, position);
                if (mDataSet.get(position).isLunchExpanded()) {
                    slotTabletListLayout.setVisibility(View.VISIBLE);
                    selectView.setVisibility(View.INVISIBLE);
                    trangleIconBottom.setVisibility(View.INVISIBLE);
                    trangleIconTop.setVisibility(View.VISIBLE);

                } else {
                    slotTabletListLayout.setVisibility(View.GONE);
                    selectView.setVisibility(View.VISIBLE);
                    trangleIconBottom.setVisibility(View.VISIBLE);
                    trangleIconTop.setVisibility(View.INVISIBLE);
                }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDataSet.get(position).isLunchExpanded())
                            mDataSet.get(position).setLunchExpanded(false);
                        else mDataSet.get(position).setLunchExpanded(true);
//                        CommonMethods.showToast(mContext, "Clicked Card " + slotType);
                        notifyItemChanged(position);
                    }
                });

                view.setOnTouchListener(new SwipeDismissTouchListener(
                        view,
                        null,
                        new SwipeDismissTouchListener.OnDismissCallback() {
                            @Override
                            public void onDismiss(View view, Object token) {
                                if (mDataSet.size() > position)
                                mDataSet.get(position).setLunchThere(false);
                                parent.removeView(view);
                                if (position != 0) {
                                    if (!mDataSet.get(position).isDinnerThere() && !mDataSet.get(position).isLunchThere() && !mDataSet.get(position).isBreakThere()) {
                                        mDataSet.remove(position);
                                        notifyDataSetChanged();
                                    }
                                } else notifyItemChanged(position);
                                CommonMethods.showToast(mContext, "Removed " + slotType);
                            }
                        }));
                break;
            case BREAK_FAST:
                slotTextView.setText(mContext.getResources().getString(R.string.breakfast_medication));
                slotTimeTextView.setText(time[2]);
                addTabletView(slotTabletListLayout, position);
                if (mDataSet.get(position).isBreakFastExpanded()) {
                    slotTabletListLayout.setVisibility(View.VISIBLE);
                    selectView.setVisibility(View.INVISIBLE);
                    trangleIconBottom.setVisibility(View.INVISIBLE);
                    trangleIconTop.setVisibility(View.VISIBLE);
                } else {
                    slotTabletListLayout.setVisibility(View.GONE);
                    selectView.setVisibility(View.VISIBLE);
                    trangleIconBottom.setVisibility(View.VISIBLE);
                    trangleIconTop.setVisibility(View.INVISIBLE);
                }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDataSet.get(position).isBreakFastExpanded())
                            mDataSet.get(position).setBreakFastExpanded(false);
                        else mDataSet.get(position).setBreakFastExpanded(true);
//                        CommonMethods.showToast(mContext, "Clicked Card " + slotType);
                        notifyItemChanged(position);
                    }
                });

                view.setOnTouchListener(new SwipeDismissTouchListener(
                        view,
                        null,
                        new SwipeDismissTouchListener.OnDismissCallback() {
                            @Override
                            public void onDismiss(View view, Object token) {
                                if (mDataSet.size() > position)
                                mDataSet.get(position).setBreakThere(false);
                                parent.removeView(view);
                                if (position != 0) {
                                    if (!mDataSet.get(position).isDinnerThere() && !mDataSet.get(position).isLunchThere() && !mDataSet.get(position).isBreakThere()) {
                                        mDataSet.remove(position);
                                        notifyDataSetChanged();
                                    }
                                } else notifyItemChanged(position);
                                CommonMethods.showToast(mContext, "Removed " + slotType);
                            }
                        }));
                break;
        }
        parent.addView(view);
    }

    // Added Tablet View

    private void addTabletView(final ViewGroup parent, int position) {
        for (int i = 0; i < medicines.size(); i++) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.tablet_list, parent, false);

            CheckBox selectViewTab = (CheckBox) view.findViewById(R.id.selectViewTab);
            ImageView tabTypeView = (ImageView) view.findViewById(R.id.tabTypeView);
            TextView tabNameTextView = (TextView) view.findViewById(R.id.tabNameTextView);
            TextView tabCountTextView = (TextView) view.findViewById(R.id.tabCountTextView);

            switch (medicines.get(i).getMedicineType()) {
                case MyRescribeConstants.MT_SYRUP:
                    tabCountTextView.setText(medicines.get(i).getMedicineCount());
                    tabTypeView.setImageResource(R.drawable.syrup_01);
                    break;

                case MyRescribeConstants.MT_TABLET:
                    tabCountTextView.setText(medicines.get(i).getMedicineCount());
                    tabTypeView.setImageResource(R.drawable.tablet);
                    break;
            }

            tabNameTextView.setText(medicines.get(i).getMedicineName());

            selectViewTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    CommonMethods.showToast(mContext, "Checked in " + position + " " + selectViewTab.isChecked());
                }
            });
            parent.addView(view);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface RowClickListener {
        void onRowClicked(ArrayList<PrescriptionData> dataObjects, int position, View v, String mClickCodes);
    }

    public void setRowClickListener(NotificationActivity mRowClickListener) {
        this.mRowClickListener = mRowClickListener;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        /*@BindView(R.id.headerLayout)
        LinearLayout headerLayout;*/

        @BindView(R.id.list_item)
        LinearLayout list_item;

        @BindView(R.id.slotLayout)
        LinearLayout slotLayout;

        @BindView(R.id.titleTextView)
        CustomTextView titleTextView;
        @BindView(R.id.dateTextView)
        CustomTextView dateTextView;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
