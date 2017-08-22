package com.rescribe.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.helpers.notification.RespondToNotificationHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.listeners.SwipeDismissTouchListener;
import com.rescribe.model.notification.AdapterNotificationModel;
import com.rescribe.model.notification.Medication;
import com.rescribe.model.notification.SlotModel;
import com.rescribe.model.response_model_notification.ResponseLogNotificationModel;
import com.rescribe.preference.MyRescribePreferencesManager;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.MyRescribeConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 31/7/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ListViewHolder> implements HelperResponse {

    private static final String DINNER = "dinner";
    private static final String LUNCH = "lunch";
    private static final String BREAK_FAST = "breakfast";
    private static final String SNACKS = "snacks";
    public int preExpandedPos = -1;
    String notificationDate = null;
    private List<AdapterNotificationModel> mDataSet;
    private RespondToNotificationHelper mRespondToNotificationHelper;
    private Context mContext;
    int mPos;
    int mCount;
    List<Medication> mMedicationListAdapter;
    ViewGroup mSlotCardParent;
    View mView;
    View mViewForHeader;
    int mHeaderPosition;
    String mSlotTypeForHeader;
    private NotificationAdapter.OnHeaderClickListener mOnHeaderClickListener;
    private Integer mMedicineID = null;
    private SlotModel mSlotModel = null;
    private ViewGroup mparentHeader;
    private String mSlotType;
    private View mSlotCardView;


    public NotificationAdapter(Context context, List<AdapterNotificationModel> dataSet, String time[]) {

        this.mDataSet = dataSet;
        this.mContext = context;
        mRespondToNotificationHelper = new RespondToNotificationHelper(mContext, this);

        try {
            this.mOnHeaderClickListener = ((NotificationAdapter.OnHeaderClickListener) mContext);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnHeaderClickListener.");
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

        notificationDate = mDataSet.get(position).getPrescriptionDate();
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
                        slotTimeTextView.setText(CommonMethods.getDayFromDate(MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, mDataSet.get(position).getPrescriptionDate()));
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
                        selectView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mViewForHeader = view;
                                mHeaderPosition = position;
                                mSlotTypeForHeader = slotType;
                                mparentHeader = parent;
                                mRespondToNotificationHelper.doRespondToNotificationForHeader(Integer.valueOf(MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext)), mContext.getString(R.string.smallcasedinner), mMedicineID, CommonMethods.formatDateTime(mDataSet.get(position).getPrescriptionDate(), MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD, MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, MyRescribeConstants.DATE), 1, MyRescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER + "_" + mHeaderPosition);
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
                                    mOnHeaderClickListener.onHeaderCollapse();
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
                        slotTimeTextView.setText(CommonMethods.getDayFromDate(MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, mDataSet.get(position).getPrescriptionDate()));
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
                        selectView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mViewForHeader = view;
                                mHeaderPosition = position;
                                mSlotTypeForHeader = slotType;
                                mparentHeader = parent;
                                mRespondToNotificationHelper.doRespondToNotificationForHeader(Integer.valueOf(MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext)), mContext.getString(R.string.smallcaselunch), mMedicineID, CommonMethods.formatDateTime(mDataSet.get(position).getPrescriptionDate(), MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD, MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, MyRescribeConstants.DATE), 1, MyRescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER + "_" + mHeaderPosition);

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
                                    mOnHeaderClickListener.onHeaderCollapse();
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
                        slotTimeTextView.setText(CommonMethods.getDayFromDate(MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, mDataSet.get(position).getPrescriptionDate()));
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
                        selectView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mViewForHeader = view;
                                mHeaderPosition = position;
                                mSlotTypeForHeader = slotType;
                                mparentHeader = parent;
                                mRespondToNotificationHelper.doRespondToNotificationForHeader(Integer.valueOf(MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext)), mContext.getString(R.string.smallcasebreakfast), mMedicineID, CommonMethods.formatDateTime(mDataSet.get(position).getPrescriptionDate(), MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD, MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, MyRescribeConstants.DATE), 1, MyRescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER + "_" + mHeaderPosition);

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
                                    mOnHeaderClickListener.onHeaderCollapse();
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
                        slotTimeTextView.setText(CommonMethods.getDayFromDate(MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, mDataSet.get(position).getPrescriptionDate()));
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
                        selectView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mViewForHeader = view;
                                mHeaderPosition = position;
                                mSlotTypeForHeader = slotType;
                                mparentHeader = parent;
                                mRespondToNotificationHelper.doRespondToNotificationForHeader(Integer.valueOf(MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext)), mContext.getString(R.string.smallcasesnacks), mMedicineID, CommonMethods.formatDateTime(mDataSet.get(position).getPrescriptionDate(), MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD, MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, MyRescribeConstants.DATE), 1, MyRescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER + "_" + mHeaderPosition);


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
                                    mOnHeaderClickListener.onHeaderCollapse();
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
            mOnHeaderClickListener.onHeaderCollapse();
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
            tabTypeView.setImageDrawable(CommonMethods.getMedicalTypeIcon(mMedicationListAdapter.get(i).getMedicineTypeName(), mContext));
            tabNameTextView.setText(mMedicationListAdapter.get(i).getMedicineName());
            selectViewTab.setEnabled(mMedicationListAdapter.get(i).isTabWebService());
            final int finalI = i;
            mCount = finalI;
            selectViewTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSlotType = slotType;
                    mPos = position;
                    mView = view;
                    mSlotCardView = slotCardView;
                    mSlotCardParent = slotCardParent;
                    mRespondToNotificationHelper.doRespondToNotification(Integer.valueOf(MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext)), medicationList.get(finalI).getMedicinSlot(), medicationList.get(finalI).getMedicineId(), CommonMethods.formatDateTime(medicationList.get(finalI).getDate(), MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD, MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, MyRescribeConstants.DATE), 0, MyRescribeConstants.TASK_RESPOND_NOTIFICATION + "_" + finalI);

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

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.startsWith(MyRescribeConstants.TASK_RESPOND_NOTIFICATION)) {
            ResponseLogNotificationModel responseLogNotificationModel = (ResponseLogNotificationModel) customResponse;
            String position = mOldDataTag;

            String[] count = position.split("_");
            String counter = count[1];
            if (responseLogNotificationModel.getCommon().isSuccess()) {
                CommonMethods.showToast(mContext, responseLogNotificationModel.getCommon().getStatusMessage());
                if (mSlotType.equals(BREAK_FAST)) {
                    mDataSet.get(mPos).getMedication().getBreakfast().get(Integer.parseInt(counter)).setTabSelected(true);
                    mDataSet.get(mPos).getMedication().getBreakfast().get(Integer.parseInt(counter)).setTabWebService(false);
                    mView.findViewById(R.id.selectViewTab).setEnabled(false);

                    if (getSelectedCount(mDataSet.get(mPos).getMedication().getBreakfast()) == mDataSet.get(mPos).getMedication().getBreakfast().size()) {
                        if (mView.getTag().equals(BREAK_FAST)) {
                            mDataSet.get(mPos).setBreakThere(false);
                            mSlotCardParent.removeView(mSlotCardView);
                        }
                    }

                } else if (mSlotType.equals(LUNCH)) {
                    mDataSet.get(mPos).getMedication().getLunch().get(Integer.parseInt(counter)).setTabSelected(true);
                    mDataSet.get(mPos).getMedication().getLunch().get(Integer.parseInt(counter)).setTabWebService(false);
                    mView.findViewById(R.id.selectViewTab).setEnabled(false);

                    if (getSelectedCount(mDataSet.get(mPos).getMedication().getLunch()) == mDataSet.get(mPos).getMedication().getLunch().size()) {
                        mDataSet.get(mPos).setLunchThere(false);
                        mSlotCardParent.removeView(mSlotCardView);
                    }

                } else if (mSlotType.equals(SNACKS)) {
                    mDataSet.get(mPos).getMedication().getSnacks().get(Integer.parseInt(counter)).setTabSelected(true);
                    mDataSet.get(mPos).getMedication().getSnacks().get(Integer.parseInt(counter)).setTabWebService(false);
                    mView.findViewById(R.id.selectViewTab).setEnabled(false);

                    if (getSelectedCount(mDataSet.get(mPos).getMedication().getSnacks()) == mDataSet.get(mPos).getMedication().getSnacks().size()) {
                        mDataSet.get(mPos).setSnacksThere(false);
                        mSlotCardParent.removeView(mSlotCardView);
                    }

                } else if (mSlotType.equals(DINNER)) {
                    mDataSet.get(mPos).getMedication().getDinner().get(Integer.parseInt(counter)).setTabSelected(true);
                    mDataSet.get(mPos).getMedication().getDinner().get(Integer.parseInt(counter)).setTabWebService(false);
                    mView.findViewById(R.id.selectViewTab).setEnabled(false);

                    if (getSelectedCount(mDataSet.get(mPos).getMedication().getDinner()) == mDataSet.get(mPos).getMedication().getDinner().size()) {
                        mDataSet.get(mPos).setDinnerThere(false);
                        mSlotCardParent.removeView(mSlotCardView);
                    }
                }
            }
        } else if (mOldDataTag.startsWith(MyRescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER)) {
            ResponseLogNotificationModel responseLogNotificationModel = (ResponseLogNotificationModel) customResponse;
            String position = mOldDataTag;
            String[] count = position.split("_");
            String counter = count[1];
            if (responseLogNotificationModel.getCommon().isSuccess()) {
                CommonMethods.showToast(mContext, responseLogNotificationModel.getCommon().getStatusMessage());
                if (mSlotTypeForHeader.equals(DINNER)) {
                    mDataSet.get(Integer.parseInt(counter)).setDinnerThere(false);
                    mparentHeader.removeView(mViewForHeader);
                    if (!mDataSet.get(Integer.parseInt(counter)).isDinnerThere() && !mDataSet.get(Integer.parseInt(counter)).isLunchThere() && !mDataSet.get(Integer.parseInt(counter)).isBreakThere() && !mDataSet.get(Integer.parseInt(counter)).isSnacksThere()) {
                        mDataSet.remove(Integer.parseInt(counter));
                        notifyDataSetChanged();
                    } else {
                        notifyItemChanged(Integer.parseInt(counter));
                    }
                } else if (mSlotTypeForHeader.equals(SNACKS)) {
                    mDataSet.get(Integer.parseInt(counter)).setSnacksThere(false);
                    mparentHeader.removeView(mViewForHeader);

                    if (!mDataSet.get(Integer.parseInt(counter)).isDinnerThere() && !mDataSet.get(Integer.parseInt(counter)).isLunchThere() && !mDataSet.get(Integer.parseInt(counter)).isBreakThere() && !mDataSet.get(Integer.parseInt(counter)).isSnacksThere()) {
                        mDataSet.remove(Integer.parseInt(counter));
                        notifyDataSetChanged();
                    } else {
                        notifyItemChanged(Integer.parseInt(counter));
                    }

                } else if (mSlotTypeForHeader.equals(LUNCH)) {
                    mDataSet.get(Integer.parseInt(counter)).setLunchThere(false);
                    mparentHeader.removeView(mViewForHeader);

                    if (!mDataSet.get(Integer.parseInt(counter)).isDinnerThere() && !mDataSet.get(Integer.parseInt(counter)).isLunchThere() && !mDataSet.get(Integer.parseInt(counter)).isBreakThere() && !mDataSet.get(Integer.parseInt(counter)).isSnacksThere()) {
                        mDataSet.remove(Integer.parseInt(counter));
                        notifyDataSetChanged();
                    } else {
                        notifyItemChanged(Integer.parseInt(counter));
                    }

                } else if (mSlotTypeForHeader.equals(BREAK_FAST)) {
                    mDataSet.get(Integer.parseInt(counter)).setBreakThere(false);
                    mparentHeader.removeView(mViewForHeader);

                    if (!mDataSet.get(Integer.parseInt(counter)).isDinnerThere() && !mDataSet.get(Integer.parseInt(counter)).isLunchThere() && !mDataSet.get(Integer.parseInt(counter)).isBreakThere() && !mDataSet.get(Integer.parseInt(counter)).isSnacksThere()) {
                        mDataSet.remove(Integer.parseInt(counter));
                        notifyDataSetChanged();
                    } else {
                        notifyItemChanged(Integer.parseInt(counter));
                    }
                }
            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {


    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        if (mOldDataTag.startsWith(MyRescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER)) {
            String position = mOldDataTag;
            String[] count = position.split("_");
            String counter = count[1];
            CheckBox headerCheckBox = (CheckBox) mViewForHeader.findViewById(R.id.selectView);
            mDataSet.get(Integer.parseInt(counter)).setTabSelected(false);
            headerCheckBox.setEnabled(true);
            headerCheckBox.setChecked(false);
        } else if (mOldDataTag.startsWith(MyRescribeConstants.TASK_RESPOND_NOTIFICATION)) {
            String position = mOldDataTag;
            String[] count = position.split("_");
            String counter = count[1];
            CheckBox mChecbox = (CheckBox) mView.findViewById(R.id.selectViewTab);
            if (mSlotType.equals(BREAK_FAST)) {
                mDataSet.get(mPos).getMedication().getBreakfast().get(Integer.parseInt(counter)).setTabSelected(false);
                mDataSet.get(mPos).getMedication().getBreakfast().get(Integer.parseInt(counter)).setTabWebService(true);
                mView.findViewById(R.id.selectViewTab).setEnabled(true);
                mView.findViewById(R.id.selectViewTab).setSelected(false);
                mChecbox.setChecked(false);

            } else if (mSlotType.equals(LUNCH)) {
                mDataSet.get(mPos).getMedication().getLunch().get(Integer.parseInt(counter)).setTabSelected(false);
                mDataSet.get(mPos).getMedication().getLunch().get(Integer.parseInt(counter)).setTabWebService(true);
                mView.findViewById(R.id.selectViewTab).setEnabled(true);
                mView.findViewById(R.id.selectViewTab).setSelected(false);
                mChecbox.setChecked(false);

            } else if (mSlotType.equals(SNACKS)) {
                mDataSet.get(mPos).getMedication().getSnacks().get(Integer.parseInt(counter)).setTabSelected(false);
                mDataSet.get(mPos).getMedication().getSnacks().get(Integer.parseInt(counter)).setTabWebService(true);
                mView.findViewById(R.id.selectViewTab).setEnabled(true);
                mView.findViewById(R.id.selectViewTab).setSelected(false);
                mChecbox.setChecked(false);

            } else if (mSlotType.equals(DINNER)) {
                mDataSet.get(mPos).getMedication().getDinner().get(Integer.parseInt(counter)).setTabSelected(false);
                mDataSet.get(mPos).getMedication().getDinner().get(Integer.parseInt(counter)).setTabWebService(true);
                mView.findViewById(R.id.selectViewTab).setEnabled(true);
                mView.findViewById(R.id.selectViewTab).setSelected(false);
                mChecbox.setChecked(false);
            }
        }
    }

    public interface OnHeaderClickListener {
        void onHeaderCollapse();
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
