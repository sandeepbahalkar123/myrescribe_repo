package com.myrescribe.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.helpers.notification.RespondToNotificationHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.listeners.SwipeDismissTouchListener;
import com.myrescribe.model.notification.AdapterNotificationModel;
import com.myrescribe.model.notification.Medication;
import com.myrescribe.model.notification.SlotModel;
import com.myrescribe.model.response_model_notification.ResponseLogNotificationModel;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.ui.customesViews.CustomTextView;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;
import com.myrescribe.util.NetworkUtil;

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
    private static final String TAG = "NotificationAdapter";
    public int preExpandedPos = -1;
    String notificationDate = null;
    private List<AdapterNotificationModel> mDataSet;
    private RespondToNotificationHelper mRespondToNotificationHelper;
    private Context mContext;
    private NotificationAdapter.OnHeaderClickListener onHeaderClickListener;
    List<SlotModel> medicationList = null;
    private Integer medicineID = null;
    private SlotModel slotModel = null;


    public NotificationAdapter(Context context, List<AdapterNotificationModel> dataSet, String time[]) {

        this.mDataSet = dataSet;
        this.mContext = context;
        mRespondToNotificationHelper = new RespondToNotificationHelper(mContext, this);
       /* for(int i = 0;i<mDataSet.size();i++){
            medicationList = mDataSet.get(i).getMedication();

        }*/

        try {
            this.onHeaderClickListener = ((NotificationAdapter.OnHeaderClickListener) mContext);
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
        slotModel = new SlotModel();

        notificationDate = mDataSet.get(position).getPrescriptionDate();
        if (!mDataSet.get(position).getMedication().equals(0)) {
            slotModel = mDataSet.get(position).getMedication();
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

    // Added Slots

    private void addSlotCards(final ViewGroup parent, final String slotType, final int position) {
        final View view = LayoutInflater.from(mContext)
                .inflate(R.layout.notification_slot_card, parent, false);
        List<Medication> dinnerMedicationList = null;
        List<Medication> lunchMedicationList = null;
        List<Medication> snacksMedicationList = null;
        List<Medication> breakFastMedicationList = null;
        String tabletName = null;
        String tabletQuantity = null;
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
                if (!slotModel.getDinner().isEmpty()) {
                    if (slotModel.getDinner().size() != 0) {
                        slotCard.setVisibility(View.VISIBLE);
                        slotTextView.setText(mContext.getResources().getString(R.string.dinner_medication));
                        slotTimeTextView.setText(CommonMethods.getDayFromDate(MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, mDataSet.get(position).getPrescriptionDate()));
                        addTabletView(slotTabletListLayout, position, parent, view, DINNER, slotModel.getDinner());
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
                                if (NetworkUtil.isInternetAvailable(mContext)) {
                                    mDataSet.get(position).setDinnerThere(false);
                                    mRespondToNotificationHelper.doRespondToNotification(Integer.valueOf(MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATEINT_ID, mContext)), mContext.getString(R.string.smallcasedinner), medicineID, CommonMethods.formatDateTime(mDataSet.get(position).getPrescriptionDate(), MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD, MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, MyRescribeConstants.DATE), 1);
                                    parent.removeView(view);

                                    if (!mDataSet.get(position).isDinnerThere() && !mDataSet.get(position).isLunchThere() && !mDataSet.get(position).isBreakThere() && !mDataSet.get(position).isSnacksThere()) {
                                        mDataSet.remove(position);
                                        notifyDataSetChanged();
                                    } else {
                                        notifyItemChanged(position);
                                    }
                                } else {
                                    CommonMethods.showToast(mContext, mContext.getString(R.string.internet));
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
                    }
                }

                break;
            case LUNCH:
                if (!slotModel.getLunch().isEmpty()) {
                    if (slotModel.getLunch().size() != 0) {
                        slotCard.setVisibility(View.VISIBLE);
                        slotTextView.setText(mContext.getResources().getString(R.string.lunch_medication));
                        slotTimeTextView.setText(CommonMethods.getDayFromDate(MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, mDataSet.get(position).getPrescriptionDate()));
                        addTabletView(slotTabletListLayout, position, parent, view, LUNCH, slotModel.getLunch());
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
                                if (NetworkUtil.isInternetAvailable(mContext)) {
                                    mDataSet.get(position).setLunchThere(false);
                                    parent.removeView(view);
                                    mRespondToNotificationHelper.doRespondToNotification(Integer.valueOf(MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATEINT_ID, mContext)), mContext.getString(R.string.smallcaselunch), medicineID, CommonMethods.formatDateTime(mDataSet.get(position).getPrescriptionDate(), MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD, MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, MyRescribeConstants.DATE), 1);

                                    if (!mDataSet.get(position).isDinnerThere() && !mDataSet.get(position).isLunchThere() && !mDataSet.get(position).isBreakThere() && !mDataSet.get(position).isSnacksThere()) {
                                        mDataSet.remove(position);
                                        notifyDataSetChanged();
                                    } else {
                                        notifyItemChanged(position);
                                    }
                                } else {
                                    CommonMethods.showToast(mContext, mContext.getString(R.string.internet));
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
                    }
                }

                break;
            case BREAK_FAST:
                if (!slotModel.getBreakfast().isEmpty()) {
                    if (slotModel.getBreakfast().size() != 0) {
                        slotCard.setVisibility(View.VISIBLE);
                        slotTextView.setText(mContext.getResources().getString(R.string.breakfast_medication));
                        slotTimeTextView.setText(CommonMethods.getDayFromDate(MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, mDataSet.get(position).getPrescriptionDate()));
                        addTabletView(slotTabletListLayout, position, parent, view, BREAK_FAST, slotModel.getBreakfast());
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
                                if (NetworkUtil.isInternetAvailable(mContext)) {
                                    mDataSet.get(position).setBreakThere(false);
                                    parent.removeView(view);
                                    mRespondToNotificationHelper.doRespondToNotification(Integer.valueOf(MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATEINT_ID, mContext)), mContext.getString(R.string.smallcasebreakfast), medicineID, CommonMethods.formatDateTime(mDataSet.get(position).getPrescriptionDate(), MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD, MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, MyRescribeConstants.DATE), 1);
                                    if (!mDataSet.get(position).isDinnerThere() && !mDataSet.get(position).isLunchThere() && !mDataSet.get(position).isBreakThere() && !mDataSet.get(position).isSnacksThere()) {
                                        mDataSet.remove(position);
                                        notifyDataSetChanged();
                                    } else {
                                        notifyItemChanged(position);
                                    }
                                } else {
                                    CommonMethods.showToast(mContext, mContext.getString(R.string.internet));
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
                    }
                }

                break;
            case SNACKS:
                if (!slotModel.getSnacks().isEmpty()) {
                    if (slotModel.getSnacks().size() != 0) {
                        slotCard.setVisibility(View.VISIBLE);
                        slotTextView.setText(mContext.getResources().getString(R.string.snacks_medication));
                        slotTimeTextView.setText(CommonMethods.getDayFromDate(MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, mDataSet.get(position).getPrescriptionDate()));
                        addTabletView(slotTabletListLayout, position, parent, view, SNACKS, slotModel.getSnacks());

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
                                if (NetworkUtil.isInternetAvailable(mContext)) {
                                    mDataSet.get(position).setSnacksThere(false);
                                    parent.removeView(view);

                                    mRespondToNotificationHelper.doRespondToNotification(Integer.valueOf(MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATEINT_ID, mContext)), mContext.getString(R.string.smallcasesnacks), medicineID, CommonMethods.formatDateTime(mDataSet.get(position).getPrescriptionDate(), MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD, MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, MyRescribeConstants.DATE), 1);

                                    if (!mDataSet.get(position).isDinnerThere() && !mDataSet.get(position).isLunchThere() && !mDataSet.get(position).isBreakThere() && !mDataSet.get(position).isSnacksThere()) {
                                        mDataSet.remove(position);
                                        notifyDataSetChanged();
                                    } else {
                                        notifyItemChanged(position);
                                    }
                                } else {
                                    CommonMethods.showToast(mContext, mContext.getString(R.string.internet));
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
                    }
                }

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

    private void addTabletView(final ViewGroup parent, final int position, final ViewGroup slotCardParent, final View slotCardView, String slotType, final List<Medication> medicationList) {

        for (int i = 0; i < medicationList.size(); i++) {
            final View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.tablet_list, parent, false);

            final CheckBox selectViewTab = (CheckBox) view.findViewById(R.id.selectViewTab);
            selectViewTab.setChecked(medicationList.get(i).isTabSelected());
            ImageView tabTypeView = (ImageView) view.findViewById(R.id.tabTypeView);
            TextView tabNameTextView = (TextView) view.findViewById(R.id.tabNameTextView);
            TextView tabCountTextView = (TextView) view.findViewById(R.id.tabCountTextView);
            tabCountTextView.setText(medicationList.get(i).getQuantity());
            tabTypeView.setImageDrawable(CommonMethods.getMedicalTypeIcon(medicationList.get(i).getMedicineTypeName(), mContext));
            tabNameTextView.setText(medicationList.get(i).getMedicineName());
            selectViewTab.setEnabled(medicationList.get(i).isTabWebService());

            final int finalI = i;
            selectViewTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkUtil.isInternetAvailable(mContext)) {
                        if (selectViewTab.isChecked()) {
                            medicationList.get(finalI).setTabSelected(true);
                            medicationList.get(finalI).setTabWebService(false);
                            selectViewTab.setEnabled(false);
                            mRespondToNotificationHelper.doRespondToNotification(Integer.valueOf(MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATEINT_ID, mContext)),
                                    medicationList.get(finalI).getMedicinSlot(), medicationList.get(finalI).getMedicineId(),
                                    CommonMethods.formatDateTime(medicationList.get(finalI).getDate(),
                                            MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD, MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY,
                                            MyRescribeConstants.DATE), 0);
                            if (getSelectedCount(medicationList) == medicationList.size()) {
                                if (view.getTag().equals(DINNER)) {
                                    mDataSet.get(position).setDinnerThere(false);
                                    slotCardParent.removeView(slotCardView);

                                } else if (view.getTag().equals(SNACKS)) {
                                    mDataSet.get(position).setSnacksThere(false);
                                    slotCardParent.removeView(slotCardView);

                                } else if (view.getTag().equals(LUNCH)) {
                                    mDataSet.get(position).setLunchThere(false);
                                    slotCardParent.removeView(slotCardView);

                                } else if (view.getTag().equals(BREAK_FAST)) {
                                    mDataSet.get(position).setBreakThere(false);
                                    slotCardParent.removeView(slotCardView);


                                }
                            }
                       /* if (!mDataSet.get(position).isDinnerThere() && !mDataSet.get(position).isLunchThere() && !mDataSet.get(position).isBreakThere() && !mDataSet.get(position).isSnacksThere()) {
                            mDataSet.remove(position);
                            notifyDataSetChanged();
                        } else {
                            slotCardParent.removeView(slotCardView);
                            notifyItemChanged(position);
                        }*/
                        } else {
                            medicationList.get(finalI).setTabSelected(false);


                        }
                    } else {
                        CommonMethods.showToast(mContext, mContext.getString(R.string.internet));
                    }
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
        ResponseLogNotificationModel responseLogNotificationModel = (ResponseLogNotificationModel) customResponse;
        if (responseLogNotificationModel.getCommon().isSuccess()) {
            CommonMethods.showToast(mContext, responseLogNotificationModel.getCommon().getStatusMessage());
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
