package com.myrescribe.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.prescription_response_model.PrescriptionData;
import com.myrescribe.ui.activities.NotificationActivity;
import com.myrescribe.ui.customesViews.CustomTextView;
import com.myrescribe.util.CommonMethods;
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
    private final String medicineSlot;
    private final String date;
    private final String time;
    private final String medicineName;

    private ArrayList<PrescriptionData> mDataSet;
    NotificationActivity mRowClickListener;
    Context mContext;
    Boolean isPatientLogin;
    String mGetMealTime;

    public NotificationListAdapter(Context context, ArrayList<PrescriptionData> dataSet, Boolean isPatientLogin, String mGetMealTime, String medicineSlot, String date, String time, String medicineName) {
        this.mDataSet = dataSet;
        this.mContext = context;
        this.isPatientLogin = isPatientLogin;
        this.mGetMealTime = mGetMealTime;

        this.medicineSlot = medicineSlot;
        this.date = date;
        this.time = time;
        this.medicineName = medicineName;

    }

    @Override
    public NotificationListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.notification_list_item, parent, false);
        return new NotificationListAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotificationListAdapter.ListViewHolder holder, final int position) {

        if (position == 0) {
            holder.headerLayout.removeAllViews();
            addHeader(holder.headerLayout, position);
        } else {
            holder.headerLayout.removeAllViews();
        }

        holder.slotLayout.removeAllViews();
        if (mDataSet.get(position).isDinnerThere())
            addSlotCards(holder.slotLayout, DINNER, position);
        if (mDataSet.get(position).isLunchThere())
            addSlotCards(holder.slotLayout, LUNCH, position);
        if (mDataSet.get(position).isBreakThere())
            addSlotCards(holder.slotLayout, BREAK_FAST, position);

    }

    // Added Slots

    private void addSlotCards(final ViewGroup parent, final String slotType, final int position) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.notification_slot_card, parent, false);

        CustomTextView slotTextView = (CustomTextView) view.findViewById(R.id.slotTextView);
        CustomTextView slotTimeTextView = (CustomTextView) view.findViewById(R.id.slotTimeTextView);
        CustomTextView slotQuestionTextView = (CustomTextView) view.findViewById(R.id.slotQuestionTextView);
        CheckBox slotSelectView = (CheckBox) view.findViewById(R.id.slotSelectView);
        LinearLayout slotTabletListLayout = (LinearLayout) view.findViewById(R.id.slotTabletListLayout);
        CardView slotCard = (CardView) view.findViewById(R.id.slotCard);

        switch (slotType) {
            case DINNER:
                slotTextView.setText(mContext.getResources().getString(R.string.dinner_medication));

                addTabletView(slotTabletListLayout, position);
                if (mDataSet.get(position).isDinnerExpanded()) {
                    slotTabletListLayout.setVisibility(View.VISIBLE);
                    slotSelectView.setVisibility(View.GONE);
                } else {
                    slotTabletListLayout.setVisibility(View.GONE);
                    slotSelectView.setVisibility(View.VISIBLE);
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
                                mDataSet.get(position).setDinnerThere(false);
                                parent.removeView(view);
                                CommonMethods.showToast(mContext, "Removed " + slotType);
                                notifyItemChanged(position);
                            }
                        });

                view.setOnTouchListener(swipeDismissTouchListener);
                break;
            case LUNCH:
                slotTextView.setText(mContext.getResources().getString(R.string.lunch_medication));

                addTabletView(slotTabletListLayout, position);
                if (mDataSet.get(position).isLunchExpanded()) {
                    slotTabletListLayout.setVisibility(View.VISIBLE);
                    slotSelectView.setVisibility(View.GONE);
                } else {
                    slotTabletListLayout.setVisibility(View.GONE);
                    slotSelectView.setVisibility(View.VISIBLE);
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
                                mDataSet.get(position).setLunchThere(false);
                                parent.removeView(view);
                                CommonMethods.showToast(mContext, "Removed " + slotType);
                                notifyItemChanged(position);
                            }
                        }));
                break;
            case BREAK_FAST:
                slotTextView.setText(mContext.getResources().getString(R.string.breakfast_medication));

                addTabletView(slotTabletListLayout, position);
                if (mDataSet.get(position).isBreakFastExpanded()) {
                    slotTabletListLayout.setVisibility(View.VISIBLE);
                    slotSelectView.setVisibility(View.GONE);
                } else {
                    slotTabletListLayout.setVisibility(View.GONE);
                    slotSelectView.setVisibility(View.VISIBLE);
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
                                mDataSet.get(position).setBreakThere(false);
                                parent.removeView(view);
                                CommonMethods.showToast(mContext, "Removed " + slotType);
                                notifyItemChanged(position);
                            }
                        }));
                break;
        }
        parent.addView(view);
    }

    // Added Header

    private void addHeader(ViewGroup parent, int position) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.notification_header, parent, false);

        TextView slotTextView = (TextView) view.findViewById(R.id.slotTextView);
        TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
        TextView dateTextView = (TextView) view.findViewById(R.id.dateTextView);

        slotTextView.setText(medicineSlot);
        timeTextView.setText(time);
        dateTextView.setText(date);

        addTabletView((LinearLayout) view.findViewById(R.id.tabletListLayout), position);
        parent.addView(view);
    }

    // Added Tablet View

    private void addTabletView(final ViewGroup parent, final int position) {
        for (int i = 0; i < 2; i++) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.tablet_list, parent, false);

            final CheckBox selectViewTab = (CheckBox) view.findViewById(R.id.selectViewTab);
            ImageView tabTypeView = (ImageView) view.findViewById(R.id.tabTypeView);
            TextView tabNameTextView = (TextView) view.findViewById(R.id.tabNameTextView);

            if (i % 2 == 0) {
                tabTypeView.setImageResource(R.drawable.tablet);
                tabNameTextView.setText("Metrogyl");
            }

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

        @BindView(R.id.headerLayout)
        LinearLayout headerLayout;

        @BindView(R.id.slotLayout)
        LinearLayout slotLayout;

        @BindView(R.id.titleTextView)
        CustomTextView titleTextView;
        @BindView(R.id.dateTextView)
        CustomTextView dateTextView;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
