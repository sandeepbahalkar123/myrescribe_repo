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
    private static final String SNACKS = "snacks";

    public int preExpandedPos = -1;

    private final String time[];

    private ArrayList<PrescriptionData> mDataSet;
    private Context mContext;

    private OnHeaderClickListener onHeaderClickListener;

    public NotificationListAdapter(Context context, ArrayList<PrescriptionData> dataSet, String time[]) {
        this.mDataSet = dataSet;
        this.mContext = context;
        this.time = time;

        try {
            this.onHeaderClickListener = ((OnHeaderClickListener) mContext);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnHeaderClickListener.");
        }
    }

    @Override
    public NotificationListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.notification_list_item, parent, false);
        return new NotificationListAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotificationListAdapter.ListViewHolder holder, final int position) {

        holder.dateTextView.setText(mDataSet.get(position).getDate());
        holder.titleTextView.setText(CommonMethods.getDayFromDate(MyRescribeConstants.DD_MM_YYYY, mDataSet.get(position).getDate()));

        holder.slotLayout.removeAllViews();
        if (mDataSet.get(position).isDinnerThere()) {
            addSlotCards(holder.slotLayout, DINNER, position);
        }
        if (mDataSet.get(position).isLunchThere()) {
            addSlotCards(holder.slotLayout, LUNCH, position);
        }
        if (mDataSet.get(position).isSnacksThere()) {
            addSlotCards(holder.slotLayout, SNACKS, position);
        }
        if (mDataSet.get(position).isBreakThere()) {
            addSlotCards(holder.slotLayout, BREAK_FAST, position);
        }

    }

    // Added Slots

    private void addSlotCards(final ViewGroup parent, final String slotType, final int position) {
        final View view = LayoutInflater.from(mContext)
                .inflate(R.layout.notification_slot_card, parent, false);

        CustomTextView slotTextView = (CustomTextView) view.findViewById(R.id.slotTextView);
        View dividerLine = (View) view.findViewById(R.id.dividerLineInHeader);
        CustomTextView slotTimeTextView = (CustomTextView) view.findViewById(R.id.slotTimeTextView);
        CustomTextView slotQuestionTextView = (CustomTextView) view.findViewById(R.id.slotQuestionTextView);
        CheckBox selectView = (CheckBox) view.findViewById(R.id.selectView);
        LinearLayout slotTabletListLayout = (LinearLayout) view.findViewById(R.id.slotTabletListLayout);
        LinearLayout slotCard = (LinearLayout) view.findViewById(R.id.slotCard);

        slotTabletListLayout.setTag(slotType);

        switch (slotType) {
            case DINNER:
                slotTextView.setText(mContext.getResources().getString(R.string.dinner_medication));
                slotTimeTextView.setText(CommonMethods.getDayFromDate(MyRescribeConstants.DD_MM_YYYY, mDataSet.get(position).getDate()));
                addTabletView(slotTabletListLayout, position, parent, view);
                if (mDataSet.get(position).isDinnerExpanded()) {
                    slotTabletListLayout.setVisibility(View.VISIBLE);
                    dividerLine.setVisibility(View.VISIBLE);
                    selectView.setVisibility(View.INVISIBLE);
                } else {
                    slotTabletListLayout.setVisibility(View.GONE);
                    dividerLine.setVisibility(View.GONE);
                    selectView.setVisibility(View.VISIBLE);
                }

                selectView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDataSet.get(position).setDinnerThere(false);
                        parent.removeView(view);

                        if (!mDataSet.get(position).isDinnerThere() && !mDataSet.get(position).isLunchThere() && !mDataSet.get(position).isBreakThere() && !mDataSet.get(position).isSnacksThere()) {
                            mDataSet.remove(position);
                            notifyDataSetChanged();
                        } else {
                            notifyItemChanged(position);
                        }
                    }
                });

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDataSet.get(position).isDinnerExpanded()) {
                            mDataSet.get(position).setDinnerExpanded(false);
                            notifyItemChanged(position);
                        } else {
                            collapseAll();
                            onHeaderClickListener.onHeaderCollapse();
                            mDataSet.get(position).setDinnerExpanded(true);
                            notifyItemChanged(preExpandedPos);
                            if (preExpandedPos != position)
                                notifyItemChanged(position);
                            preExpandedPos = position;
                        }
//                        CommonMethods.showToast(mContext, "Clicked Card " + slotType);
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

                                if (!mDataSet.get(position).isDinnerThere() && !mDataSet.get(position).isLunchThere() && !mDataSet.get(position).isBreakThere() && !mDataSet.get(position).isSnacksThere()) {
                                    mDataSet.remove(position);
                                    notifyDataSetChanged();
                                } else {
                                    notifyItemChanged(position);
                                }

                                CommonMethods.showToast(mContext, "Removed " + slotType);
                            }
                        });

                view.setOnTouchListener(swipeDismissTouchListener);
                break;
            case LUNCH:
                slotTextView.setText(mContext.getResources().getString(R.string.lunch_medication));
                slotTimeTextView.setText(CommonMethods.getDayFromDate(MyRescribeConstants.DD_MM_YYYY, mDataSet.get(position).getDate()));
                addTabletView(slotTabletListLayout, position, parent, view);
                if (mDataSet.get(position).isLunchExpanded()) {
                    slotTabletListLayout.setVisibility(View.VISIBLE);
                    dividerLine.setVisibility(View.VISIBLE);
                    selectView.setVisibility(View.INVISIBLE);
                } else {
                    slotTabletListLayout.setVisibility(View.GONE);
                    dividerLine.setVisibility(View.GONE);
                    selectView.setVisibility(View.VISIBLE);
                }

                selectView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDataSet.get(position).setLunchThere(false);
                        parent.removeView(view);

                        if (!mDataSet.get(position).isDinnerThere() && !mDataSet.get(position).isLunchThere() && !mDataSet.get(position).isBreakThere() && !mDataSet.get(position).isSnacksThere()) {
                            mDataSet.remove(position);
                            notifyDataSetChanged();
                        } else {
                            notifyItemChanged(position);
                        }
                    }
                });

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDataSet.get(position).isLunchExpanded()) {
                            mDataSet.get(position).setLunchExpanded(false);
                            notifyItemChanged(position);
                        } else {
                            collapseAll();
                            onHeaderClickListener.onHeaderCollapse();
                            mDataSet.get(position).setLunchExpanded(true);
                            notifyItemChanged(preExpandedPos);
                            if (preExpandedPos != position)
                                notifyItemChanged(position);
                            preExpandedPos = position;
                        }
//                        CommonMethods.showToast(mContext, "Clicked Card " + slotType);
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

                                if (!mDataSet.get(position).isDinnerThere() && !mDataSet.get(position).isLunchThere() && !mDataSet.get(position).isBreakThere() && !mDataSet.get(position).isSnacksThere()) {
                                    mDataSet.remove(position);
                                    notifyDataSetChanged();
                                } else {
                                    notifyItemChanged(position);
                                }

                                CommonMethods.showToast(mContext, "Removed " + slotType);
                            }
                        }));
                break;
            case BREAK_FAST:
                slotTextView.setText(mContext.getResources().getString(R.string.breakfast_medication));
                slotTimeTextView.setText(CommonMethods.getDayFromDate(MyRescribeConstants.DD_MM_YYYY, mDataSet.get(position).getDate()));
                addTabletView(slotTabletListLayout, position, parent, view);
                if (mDataSet.get(position).isBreakFastExpanded()) {
                    slotTabletListLayout.setVisibility(View.VISIBLE);
                    dividerLine.setVisibility(View.VISIBLE);
                    selectView.setVisibility(View.INVISIBLE);
                } else {
                    dividerLine.setVisibility(View.GONE);
                    slotTabletListLayout.setVisibility(View.GONE);
                    selectView.setVisibility(View.VISIBLE);
                }

                selectView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDataSet.get(position).setBreakThere(false);
                        parent.removeView(view);
                        if (!mDataSet.get(position).isDinnerThere() && !mDataSet.get(position).isLunchThere() && !mDataSet.get(position).isBreakThere() && !mDataSet.get(position).isSnacksThere()) {
                            mDataSet.remove(position);
                            notifyDataSetChanged();
                        } else {
                            notifyItemChanged(position);
                        }
                    }
                });

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDataSet.get(position).isBreakFastExpanded()) {
                            mDataSet.get(position).setBreakFastExpanded(false);
                            notifyItemChanged(position);
                        } else {
                            collapseAll();
                            onHeaderClickListener.onHeaderCollapse();
                            mDataSet.get(position).setBreakFastExpanded(true);
                            notifyItemChanged(preExpandedPos);
                            if (preExpandedPos != position)
                                notifyItemChanged(position);
                            preExpandedPos = position;
                        }
//                        CommonMethods.showToast(mContext, "Clicked Card " + slotType);
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

                                if (!mDataSet.get(position).isDinnerThere() && !mDataSet.get(position).isLunchThere() && !mDataSet.get(position).isBreakThere() && !mDataSet.get(position).isSnacksThere()) {
                                    mDataSet.remove(position);
                                    notifyDataSetChanged();
                                } else {
                                    notifyItemChanged(position);
                                }

                                CommonMethods.showToast(mContext, "Removed " + slotType);
                            }
                        }));
                break;
            case SNACKS:
                slotTextView.setText(mContext.getResources().getString(R.string.snacks_medication));
                slotTimeTextView.setText(CommonMethods.getDayFromDate(MyRescribeConstants.DD_MM_YYYY, mDataSet.get(position).getDate()));
                addTabletView(slotTabletListLayout, position, parent, view);
                if (mDataSet.get(position).isSnacksExpanded()) {
                    slotTabletListLayout.setVisibility(View.VISIBLE);
                    selectView.setVisibility(View.INVISIBLE);
                    dividerLine.setVisibility(View.VISIBLE);

                } else {
                    slotTabletListLayout.setVisibility(View.GONE);
                    selectView.setVisibility(View.VISIBLE);
                    dividerLine.setVisibility(View.GONE);
                }

                selectView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDataSet.get(position).setSnacksThere(false);
                        parent.removeView(view);
                        if (!mDataSet.get(position).isDinnerThere() && !mDataSet.get(position).isLunchThere() && !mDataSet.get(position).isBreakThere()  && !mDataSet.get(position).isSnacksThere()) {
                            mDataSet.remove(position);
                            notifyDataSetChanged();
                        } else {
                            notifyItemChanged(position);
                        }
                    }
                });

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDataSet.get(position).isSnacksExpanded()) {
                            mDataSet.get(position).setSnacksExpanded(false);
                            notifyItemChanged(position);
                        } else {
                            collapseAll();
                            onHeaderClickListener.onHeaderCollapse();
                            mDataSet.get(position).setSnacksExpanded(true);
                            notifyItemChanged(preExpandedPos);
                            if (preExpandedPos != position)
                                notifyItemChanged(position);
                            preExpandedPos = position;
                        }
//                        CommonMethods.showToast(mContext, "Clicked Card " + slotType);
                    }
                });

                view.setOnTouchListener(new SwipeDismissTouchListener(
                        view,
                        null,
                        new SwipeDismissTouchListener.OnDismissCallback() {
                            @Override
                            public void onDismiss(View view, Object token) {

                                mDataSet.get(position).setSnacksThere(false);
                                parent.removeView(view);

                                if (!mDataSet.get(position).isDinnerThere() && !mDataSet.get(position).isLunchThere() && !mDataSet.get(position).isBreakThere() && !mDataSet.get(position).isSnacksThere()) {
                                    mDataSet.remove(position);
                                    notifyDataSetChanged();
                                } else {
                                    notifyItemChanged(position);
                                }

                                CommonMethods.showToast(mContext, "Removed " + slotType);
                            }
                        }));
                break;
        }

        parent.addView(view);
    }

    public void collapseAll() {
        if (preExpandedPos == -1)
            onHeaderClickListener.onHeaderCollapse();
        else {
            mDataSet.get(preExpandedPos).setBreakFastExpanded(false);
            mDataSet.get(preExpandedPos).setLunchExpanded(false);
            mDataSet.get(preExpandedPos).setDinnerExpanded(false);
            mDataSet.get(preExpandedPos).setSnacksExpanded(false);
        }
    }

    // Added Tablet View

    private void addTabletView(final ViewGroup parent, final int position, final ViewGroup slotCardParent, final View slotCardView) {

        final ArrayList<PrescriptionData> prescription = new ArrayList<>();
        prescription.addAll(mDataSet);

        for (int i = 0; i < prescription.size(); i++) {
            final View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.tablet_list, parent, false);

            final CheckBox selectViewTab = (CheckBox) view.findViewById(R.id.selectViewTab);
            selectViewTab.setChecked(prescription.get(i).isTabSelected());
            ImageView tabTypeView = (ImageView) view.findViewById(R.id.tabTypeView);
            TextView tabNameTextView = (TextView) view.findViewById(R.id.tabNameTextView);
            TextView tabCountTextView = (TextView) view.findViewById(R.id.tabCountTextView);

            tabCountTextView.setText(prescription.get(i).getDinnerA() + prescription.get(i).getDinnerB());
            tabTypeView.setImageDrawable(CommonMethods.getMedicalTypeIcon(prescription.get(i).getMedicineTypeName(), mContext));
            tabNameTextView.setText(prescription.get(i).getMedicineName());

            final int finalI = i;
            selectViewTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectViewTab.isChecked()) {
                        prescription.get(finalI).setTabSelected(true);
                        if (getSelectedCount(prescription) == prescription.size()) {

                            if (view.getTag().equals(DINNER))
                                mDataSet.get(position).setDinnerThere(false);
                            else if (view.getTag().equals(LUNCH))
                                mDataSet.get(position).setLunchThere(false);
                            else if(view.getTag().equals(BREAK_FAST))
                                mDataSet.get(position).setBreakThere(false);
                            else if(view.getTag().equals(SNACKS))
                                mDataSet.get(position).setSnacksThere(false);

                            if (!mDataSet.get(position).isDinnerThere() && !mDataSet.get(position).isLunchThere() && !mDataSet.get(position).isBreakThere() && !mDataSet.get(position).isSnacksThere()) {
                                mDataSet.remove(position);
                                notifyDataSetChanged();
                            } else {
                                slotCardParent.removeView(slotCardView);
                                notifyItemChanged(position);
                            }
                        }
                    } else {
                        prescription.get(finalI).setTabSelected(false);
                    }
//                    CommonMethods.showToast(mContext, "Checked in " + position + " " + selectViewTab.isChecked());
                }
            });
            view.setTag(parent.getTag());
            parent.addView(view);
        }
    }

    public int getSelectedCount(ArrayList<PrescriptionData> data) {
        int count = 0;
        for (PrescriptionData prescriptionData : data) {
            if (prescriptionData.isTabSelected())
                count += 1;
        }
        return count;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

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

    public interface OnHeaderClickListener {
        void onHeaderCollapse();
    }
}
