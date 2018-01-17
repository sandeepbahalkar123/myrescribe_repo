package com.rescribe.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.listeners.SwipeDismissTouchListener;
import com.rescribe.model.notification.AdapterNotificationModel;
import com.rescribe.model.notification.Medication;
import com.rescribe.model.notification.SlotModel;
import com.rescribe.model.response_model_notification.NotificationResponseBaseModel;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 31/7/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ListViewHolder> {

    private static final String DINNER = "dinner";
    private static final String LUNCH = "lunch";
    private static final String BREAK_FAST = "breakfast";
    private static final String SNACKS = "snacks";
    public int preExpandedPos = -1;
    private List<AdapterNotificationModel> mDataSet;
    private Context mContext;
    private int mPos;
    private List<Medication> mMedicationListAdapter;
    private ViewGroup mSlotCardParent;
    private View mView;
    private View mViewForHeader;
    private int mHeaderPosition;
    private String mSlotTypeForHeader;
    private OnNotificationClickListener mOnNotificationClickListener;
    private Integer mMedicineID = null;
    private SlotModel mSlotModel = null;
    private ViewGroup mparentHeader;
    private String mSlotType;
    private View mSlotCardView;

    public NotificationAdapter(Context context, List<AdapterNotificationModel> dataSet, String time[]) {

        this.mDataSet = dataSet;
        this.mContext = context;

        try {
            this.mOnNotificationClickListener = ((OnNotificationClickListener) mContext);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnNotificationClickListener.");
        }
    }

    @Override
    public NotificationAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.notification_list_item, parent, false);
        return new NotificationAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotificationAdapter.ListViewHolder holder, final int position) {
        mSlotModel = new SlotModel();

        if (!mDataSet.get(position).getMedication().equals(0)) {
            mSlotModel = mDataSet.get(position).getMedication();
        }

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

    private void addSlotCards(final ViewGroup parent, final String slotType, final int position) {
        // in each row four slots of dinner ,breakfast, lunch and snacks are added to show respective dosage of medicine of user
        final View view = LayoutInflater.from(mContext)
                .inflate(R.layout.notification_slot_card, parent, false);
        CustomTextView slotTextView = (CustomTextView) view.findViewById(R.id.slotTextView);
        View dividerLine = (View) view.findViewById(R.id.dividerLineInHeader);
        CustomTextView slotTimeTextView = (CustomTextView) view.findViewById(R.id.slotTimeTextView);
        CustomTextView slotQuestionTextView = (CustomTextView) view.findViewById(R.id.slotQuestionTextView);
        final CheckBox selectView = (CheckBox) view.findViewById(R.id.selectView);
        LinearLayout slotTabletListLayout = (LinearLayout) view.findViewById(R.id.slotTabletListLayout);
        LinearLayout slotCard = (LinearLayout) view.findViewById(R.id.slotCard);
        slotTabletListLayout.setTag(slotType);

        switch (slotType) {
            case DINNER:
                if (!mSlotModel.getDinner().isEmpty()) {
                    if (mSlotModel.getDinner().size() != 0) {
                        slotCard.setVisibility(View.VISIBLE);
                        slotTextView.setText(mContext.getResources().getString(R.string.dinner_medication));
                        slotTimeTextView.setText(CommonMethods.getDayFromDate(RescribeConstants.DATE_PATTERN.DD_MM_YYYY, mDataSet.get(position).getPrescriptionDate()));
                        //no of medicines to be taken in dinner slot is shown
                        addTabletView(slotTabletListLayout, position, parent, view, DINNER, mSlotModel.getDinner());
                        if (mDataSet.get(position).isDinnerExpanded()) {
                            slotTabletListLayout.setVisibility(View.VISIBLE);
                            dividerLine.setVisibility(View.VISIBLE);
                            selectView.setVisibility(View.INVISIBLE);
                        } else {
                            slotTabletListLayout.setVisibility(View.GONE);
                            dividerLine.setVisibility(View.GONE);
                            selectView.setVisibility(View.VISIBLE);
                        }

                        //Onclick of checkbox of dinner slot
                        selectView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mViewForHeader = view;
                                mHeaderPosition = position;
                                mSlotTypeForHeader = slotType;
                                mparentHeader = parent;
                                mOnNotificationClickListener.setOnClickCheckBoxListener(mViewForHeader, mHeaderPosition, mContext.getString(R.string.smallcasedinner), mparentHeader, mMedicineID, CommonMethods.formatDateTime(mDataSet.get(position).getPrescriptionDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE), 1, RescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER_ADAPTER + "_" + mHeaderPosition, true);
                            }
                        });
                        //expand and collapse for each slot in one row
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mDataSet.get(position).isDinnerExpanded()) {
                                    mDataSet.get(position).setDinnerExpanded(false);
                                    notifyItemChanged(position);
                                } else {
                                    collapseAll();
                                    mOnNotificationClickListener.onHeaderCollapse();
                                    mDataSet.get(position).setDinnerExpanded(true);
                                    notifyItemChanged(preExpandedPos);
                                    if (preExpandedPos != position)
                                        notifyItemChanged(position);
                                    preExpandedPos = position;
                                }

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

                                        mOnNotificationClickListener.onSwiped(slotType);
                                    }
                                });

                        view.setOnTouchListener(swipeDismissTouchListener);
                    }
                }
                break;
            case LUNCH:
                if (!mSlotModel.getLunch().isEmpty()) {
                    if (mSlotModel.getLunch().size() != 0) {
                        slotCard.setVisibility(View.VISIBLE);
                        slotTextView.setText(mContext.getResources().getString(R.string.lunch_medication));
                        slotTimeTextView.setText(CommonMethods.getDayFromDate(RescribeConstants.DATE_PATTERN.DD_MM_YYYY, mDataSet.get(position).getPrescriptionDate()));
                        //no of medicines to be taken in lunch slot is shown
                        addTabletView(slotTabletListLayout, position, parent, view, LUNCH, mSlotModel.getLunch());
                        if (mDataSet.get(position).isLunchExpanded()) {
                            slotTabletListLayout.setVisibility(View.VISIBLE);
                            dividerLine.setVisibility(View.VISIBLE);
                            selectView.setVisibility(View.INVISIBLE);
                        } else {
                            slotTabletListLayout.setVisibility(View.GONE);
                            dividerLine.setVisibility(View.GONE);
                            selectView.setVisibility(View.VISIBLE);
                        }

                        //Onclick of checkbox of lunch slot
                        selectView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mViewForHeader = view;
                                mHeaderPosition = position;
                                mSlotTypeForHeader = slotType;
                                mparentHeader = parent;
                                mOnNotificationClickListener.setOnClickCheckBoxListener(mViewForHeader, mHeaderPosition, mContext.getString(R.string.smallcaselunch), mparentHeader, mMedicineID, CommonMethods.formatDateTime(mDataSet.get(position).getPrescriptionDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE), 1, RescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER_ADAPTER + "_" + mHeaderPosition, true);

                            }
                        });
                        //expand and collapse for each slot in one row
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mDataSet.get(position).isLunchExpanded()) {
                                    mDataSet.get(position).setLunchExpanded(false);
                                    notifyItemChanged(position);
                                } else {
                                    collapseAll();
                                    mOnNotificationClickListener.onHeaderCollapse();
                                    mDataSet.get(position).setLunchExpanded(true);
                                    notifyItemChanged(preExpandedPos);
                                    if (preExpandedPos != position)
                                        notifyItemChanged(position);
                                    preExpandedPos = position;
                                }
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

                                        mOnNotificationClickListener.onSwiped(slotType);
                                    }
                                }));
                    }
                }

                break;
            case BREAK_FAST:
                if (!mSlotModel.getBreakfast().isEmpty()) {
                    if (mSlotModel.getBreakfast().size() != 0) {
                        slotCard.setVisibility(View.VISIBLE);
                        slotTextView.setText(mContext.getResources().getString(R.string.breakfast_medication));
                        slotTimeTextView.setText(CommonMethods.getDayFromDate(RescribeConstants.DATE_PATTERN.DD_MM_YYYY, mDataSet.get(position).getPrescriptionDate()));
                        //no of medicines to be taken in breakfast slot is shown
                        addTabletView(slotTabletListLayout, position, parent, view, BREAK_FAST, mSlotModel.getBreakfast());
                        if (mDataSet.get(position).isBreakFastExpanded()) {
                            slotTabletListLayout.setVisibility(View.VISIBLE);
                            dividerLine.setVisibility(View.VISIBLE);
                            selectView.setVisibility(View.INVISIBLE);
                        } else {
                            dividerLine.setVisibility(View.GONE);
                            slotTabletListLayout.setVisibility(View.GONE);
                            selectView.setVisibility(View.VISIBLE);
                        }

                        //Onclick of checkbox of breakfast slot
                        selectView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mViewForHeader = view;
                                mHeaderPosition = position;
                                mSlotTypeForHeader = slotType;
                                mparentHeader = parent;
                                mOnNotificationClickListener.setOnClickCheckBoxListener(mViewForHeader, mHeaderPosition, mContext.getString(R.string.smallcasebreakfast), mparentHeader, mMedicineID, CommonMethods.formatDateTime(mDataSet.get(position).getPrescriptionDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE), 1, RescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER_ADAPTER + "_" + mHeaderPosition, true);

                            }
                        });
                        //expand and collapse for each slot in one row
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mDataSet.get(position).isBreakFastExpanded()) {
                                    mDataSet.get(position).setBreakFastExpanded(false);
                                    notifyItemChanged(position);
                                } else {
                                    collapseAll();
                                    mOnNotificationClickListener.onHeaderCollapse();
                                    mDataSet.get(position).setBreakFastExpanded(true);
                                    notifyItemChanged(preExpandedPos);
                                    if (preExpandedPos != position)
                                        notifyItemChanged(position);
                                    preExpandedPos = position;
                                }
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

                                        mOnNotificationClickListener.onSwiped(slotType);
                                    }
                                }));
                    }
                }
                break;
            case SNACKS:
                if (!mSlotModel.getSnacks().isEmpty()) {
                    if (mSlotModel.getSnacks().size() != 0) {
                        slotCard.setVisibility(View.VISIBLE);
                        slotTextView.setText(mContext.getResources().getString(R.string.snacks_medication));
                        slotTimeTextView.setText(CommonMethods.getDayFromDate(RescribeConstants.DATE_PATTERN.DD_MM_YYYY, mDataSet.get(position).getPrescriptionDate()));
                        //no of medicines to be taken in snacks slot is shown

                        addTabletView(slotTabletListLayout, position, parent, view, SNACKS, mSlotModel.getSnacks());

                        if (mDataSet.get(position).isSnacksExpanded()) {
                            slotTabletListLayout.setVisibility(View.VISIBLE);
                            selectView.setVisibility(View.INVISIBLE);
                            dividerLine.setVisibility(View.VISIBLE);

                        } else {
                            slotTabletListLayout.setVisibility(View.GONE);
                            selectView.setVisibility(View.VISIBLE);
                            dividerLine.setVisibility(View.GONE);
                        }
                        //Onclick of checkbox of snacks slot
                        selectView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mViewForHeader = view;
                                mHeaderPosition = position;
                                mSlotTypeForHeader = slotType;
                                mparentHeader = parent;
                                mOnNotificationClickListener.setOnClickCheckBoxListener(mViewForHeader, mHeaderPosition, mContext.getString(R.string.smallcasesnacks), mparentHeader, mMedicineID, CommonMethods.formatDateTime(mDataSet.get(position).getPrescriptionDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE), 1, RescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER_ADAPTER + "_" + mHeaderPosition, true);

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
                                    mOnNotificationClickListener.onHeaderCollapse();
                                    mDataSet.get(position).setSnacksExpanded(true);
                                    notifyItemChanged(preExpandedPos);
                                    if (preExpandedPos != position)
                                        notifyItemChanged(position);
                                    preExpandedPos = position;
                                }
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

                                        mOnNotificationClickListener.onSwiped(slotType);
                                    }
                                }));
                    }
                }

                break;
        }
        parent.addView(view);
    }

    public void collapseAll() {
        if (preExpandedPos == -1)
            mOnNotificationClickListener.onHeaderCollapse();
        else {
            mDataSet.get(preExpandedPos).setBreakFastExpanded(false);
            mDataSet.get(preExpandedPos).setLunchExpanded(false);
            mDataSet.get(preExpandedPos).setDinnerExpanded(false);
            mDataSet.get(preExpandedPos).setSnacksExpanded(false);
        }
    }

    // Added Tablet View

    private void addTabletView(final ViewGroup parent, final int position, final ViewGroup slotCardParent, final View slotCardView, final String slotType, final List<Medication> medicationList) {
        mMedicationListAdapter = new ArrayList<>();
        mMedicationListAdapter.addAll(medicationList);
        for (int i = 0; i < mMedicationListAdapter.size(); i++) {
            final View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.tablet_list, parent, false);

            final CheckBox selectViewTab = (CheckBox) view.findViewById(R.id.selectViewTab);
            selectViewTab.setChecked(mMedicationListAdapter.get(i).isTabSelected());
            ImageView tabTypeView = (ImageView) view.findViewById(R.id.tabTypeView);
            TextView tabNameTextView = (TextView) view.findViewById(R.id.tabNameTextView);
            TextView tabCountTextView = (TextView) view.findViewById(R.id.tabCountTextView);
            tabCountTextView.setText(mMedicationListAdapter.get(i).getQuantity());
            tabTypeView.setImageDrawable(CommonMethods.getMedicineTypeImage(mMedicationListAdapter.get(i).getMedicineTypeName(), mContext, ContextCompat.getColor(mContext, R.color.tagColor)));
            tabNameTextView.setText(mMedicationListAdapter.get(i).getMedicineName());
            selectViewTab.setEnabled(mMedicationListAdapter.get(i).isTabWebService());
            final int finalI = i;
            selectViewTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSlotType = slotType;
                    mPos = position;
                    mView = view;
                    mSlotCardView = slotCardView;
                    mSlotCardParent = slotCardParent;
                    mOnNotificationClickListener.setOnClickCheckBoxListener(mView, mPos, medicationList.get(finalI).getMedicinSlot(), mSlotCardParent, medicationList.get(finalI).getMedicineId(), CommonMethods.formatDateTime(medicationList.get(finalI).getDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE), 0, RescribeConstants.TASK_RESPOND_NOTIFICATION_ADAPTER + "_" + finalI, false);
                }
            });
            view.setTag(parent.getTag());
            parent.addView(view);
        }
    }

    public int getSelectedCount(List<Medication> data) {
        int count = 0;
        for (Medication medicationData : data) {
            if (medicationData.isTabSelected())
                count += 1;
        }
        return count;
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    // this method is called from NotificationActivity Onsuccess of TASK_RESPOND_NOTIFICATION_ADAPTER or TASK_RESPOND_NOTIFICATION_FOR_HEADER_ADAPTER
    public void onSuccessOfNotificationCheckBoxClick(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION_ADAPTER)) {
            NotificationResponseBaseModel responseLogNotificationModel = (NotificationResponseBaseModel) customResponse;

            String[] count = mOldDataTag.split("_");
            String counter = count[1];
            if (responseLogNotificationModel.getCommon().isSuccess()) {
                // CommonMethods.showToast(mContext, responseLogNotificationModel.getNotificationResponseModel().getMsg());
                switch (mSlotType) {
                    case BREAK_FAST:
                        mDataSet.get(mPos).getMedication().getBreakfast().get(Integer.parseInt(counter)).setTabSelected(true);
                        mDataSet.get(mPos).getMedication().getBreakfast().get(Integer.parseInt(counter)).setTabWebService(false);
                        mView.findViewById(R.id.selectViewTab).setEnabled(false);

                        if (getSelectedCount(mDataSet.get(mPos).getMedication().getBreakfast()) == mDataSet.get(mPos).getMedication().getBreakfast().size()) {
                            if (mView.getTag().equals(BREAK_FAST)) {
                                mDataSet.get(mPos).setBreakThere(false);
                                mSlotCardParent.removeView(mSlotCardView);
                            }
                        }

                        break;
                    case LUNCH:
                        mDataSet.get(mPos).getMedication().getLunch().get(Integer.parseInt(counter)).setTabSelected(true);
                        mDataSet.get(mPos).getMedication().getLunch().get(Integer.parseInt(counter)).setTabWebService(false);
                        mView.findViewById(R.id.selectViewTab).setEnabled(false);

                        if (getSelectedCount(mDataSet.get(mPos).getMedication().getLunch()) == mDataSet.get(mPos).getMedication().getLunch().size()) {
                            mDataSet.get(mPos).setLunchThere(false);
                            mSlotCardParent.removeView(mSlotCardView);
                        }

                        break;
                    case SNACKS:
                        mDataSet.get(mPos).getMedication().getSnacks().get(Integer.parseInt(counter)).setTabSelected(true);
                        mDataSet.get(mPos).getMedication().getSnacks().get(Integer.parseInt(counter)).setTabWebService(false);
                        mView.findViewById(R.id.selectViewTab).setEnabled(false);

                        if (getSelectedCount(mDataSet.get(mPos).getMedication().getSnacks()) == mDataSet.get(mPos).getMedication().getSnacks().size()) {
                            mDataSet.get(mPos).setSnacksThere(false);
                            mSlotCardParent.removeView(mSlotCardView);
                        }

                        break;
                    case DINNER:
                        mDataSet.get(mPos).getMedication().getDinner().get(Integer.parseInt(counter)).setTabSelected(true);
                        mDataSet.get(mPos).getMedication().getDinner().get(Integer.parseInt(counter)).setTabWebService(false);
                        mView.findViewById(R.id.selectViewTab).setEnabled(false);

                        if (getSelectedCount(mDataSet.get(mPos).getMedication().getDinner()) == mDataSet.get(mPos).getMedication().getDinner().size()) {
                            mDataSet.get(mPos).setDinnerThere(false);
                            mSlotCardParent.removeView(mSlotCardView);
                        }
                        break;
                }
            }
        } else if (mOldDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER_ADAPTER)) {
            NotificationResponseBaseModel responseLogNotificationModel = (NotificationResponseBaseModel) customResponse;
            String[] count = mOldDataTag.split("_");
            String counter = count[1];
            if (responseLogNotificationModel.getCommon().isSuccess()) {
                //  CommonMethods.showToast(mContext, responseLogNotificationModel.getNotificationResponseModel().getMsg());
                switch (mSlotTypeForHeader) {
                    case DINNER:
                        mDataSet.get(Integer.parseInt(counter)).setDinnerThere(false);
                        mparentHeader.removeView(mViewForHeader);
                        if (!mDataSet.get(Integer.parseInt(counter)).isDinnerThere() && !mDataSet.get(Integer.parseInt(counter)).isLunchThere() && !mDataSet.get(Integer.parseInt(counter)).isBreakThere() && !mDataSet.get(Integer.parseInt(counter)).isSnacksThere()) {
                            mDataSet.remove(Integer.parseInt(counter));
                            notifyDataSetChanged();
                        } else {
                            notifyItemChanged(Integer.parseInt(counter));
                        }
                        break;
                    case SNACKS:
                        mDataSet.get(Integer.parseInt(counter)).setSnacksThere(false);
                        mparentHeader.removeView(mViewForHeader);

                        if (!mDataSet.get(Integer.parseInt(counter)).isDinnerThere() && !mDataSet.get(Integer.parseInt(counter)).isLunchThere() && !mDataSet.get(Integer.parseInt(counter)).isBreakThere() && !mDataSet.get(Integer.parseInt(counter)).isSnacksThere()) {
                            mDataSet.remove(Integer.parseInt(counter));
                            notifyDataSetChanged();
                        } else {
                            notifyItemChanged(Integer.parseInt(counter));
                        }

                        break;
                    case LUNCH:
                        mDataSet.get(Integer.parseInt(counter)).setLunchThere(false);
                        mparentHeader.removeView(mViewForHeader);

                        if (!mDataSet.get(Integer.parseInt(counter)).isDinnerThere() && !mDataSet.get(Integer.parseInt(counter)).isLunchThere() && !mDataSet.get(Integer.parseInt(counter)).isBreakThere() && !mDataSet.get(Integer.parseInt(counter)).isSnacksThere()) {
                            mDataSet.remove(Integer.parseInt(counter));
                            notifyDataSetChanged();
                        } else {
                            notifyItemChanged(Integer.parseInt(counter));
                        }

                        break;
                    case BREAK_FAST:
                        mDataSet.get(Integer.parseInt(counter)).setBreakThere(false);
                        mparentHeader.removeView(mViewForHeader);

                        if (!mDataSet.get(Integer.parseInt(counter)).isDinnerThere() && !mDataSet.get(Integer.parseInt(counter)).isLunchThere() && !mDataSet.get(Integer.parseInt(counter)).isBreakThere() && !mDataSet.get(Integer.parseInt(counter)).isSnacksThere()) {
                            mDataSet.remove(Integer.parseInt(counter));
                            notifyDataSetChanged();
                        } else {
                            notifyItemChanged(Integer.parseInt(counter));
                        }
                        break;
                }
            }
        }

    }

    //this method is called from Notification Activity when there when no connection error occurs , on click of checkbox
    public void onNoConnectionOfNotificationCheckBoxClick(String mOldDataTag, String errorMessage) {
        if (mOldDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER_ADAPTER)) {
            String[] count = mOldDataTag.split("_");
            String counter = count[1];
            CheckBox headerCheckBox = (CheckBox) mViewForHeader.findViewById(R.id.selectView);
            mDataSet.get(Integer.parseInt(counter)).setTabSelected(false);
            headerCheckBox.setEnabled(true);
            headerCheckBox.setChecked(false);
        } else if (mOldDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION_ADAPTER)) {
            String[] count = mOldDataTag.split("_");
            String counter = count[1];
            CheckBox mChecbox = (CheckBox) mView.findViewById(R.id.selectViewTab);
            switch (mSlotType) {
                case BREAK_FAST:
                    mDataSet.get(mPos).getMedication().getBreakfast().get(Integer.parseInt(counter)).setTabSelected(false);
                    mDataSet.get(mPos).getMedication().getBreakfast().get(Integer.parseInt(counter)).setTabWebService(true);
                    mView.findViewById(R.id.selectViewTab).setEnabled(true);
                    mView.findViewById(R.id.selectViewTab).setSelected(false);
                    mChecbox.setChecked(false);

                    break;
                case LUNCH:
                    mDataSet.get(mPos).getMedication().getLunch().get(Integer.parseInt(counter)).setTabSelected(false);
                    mDataSet.get(mPos).getMedication().getLunch().get(Integer.parseInt(counter)).setTabWebService(true);
                    mView.findViewById(R.id.selectViewTab).setEnabled(true);
                    mView.findViewById(R.id.selectViewTab).setSelected(false);
                    mChecbox.setChecked(false);

                    break;
                case SNACKS:
                    mDataSet.get(mPos).getMedication().getSnacks().get(Integer.parseInt(counter)).setTabSelected(false);
                    mDataSet.get(mPos).getMedication().getSnacks().get(Integer.parseInt(counter)).setTabWebService(true);
                    mView.findViewById(R.id.selectViewTab).setEnabled(true);
                    mView.findViewById(R.id.selectViewTab).setSelected(false);
                    mChecbox.setChecked(false);

                    break;
                case DINNER:
                    mDataSet.get(mPos).getMedication().getDinner().get(Integer.parseInt(counter)).setTabSelected(false);
                    mDataSet.get(mPos).getMedication().getDinner().get(Integer.parseInt(counter)).setTabWebService(true);
                    mView.findViewById(R.id.selectViewTab).setEnabled(true);
                    mView.findViewById(R.id.selectViewTab).setSelected(false);
                    mChecbox.setChecked(false);
                    break;
            }
        }
    }

    public interface OnNotificationClickListener {
        void onHeaderCollapse();
        void setOnClickCheckBoxListener(View mViewForHeader, int pos, String slotType, ViewGroup viewGroup, Integer medicineId, String takenDate, Integer bundleValue, String taskName, boolean isHeaderCheckboxClick);
        void onSwiped(String slotType);
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

}
