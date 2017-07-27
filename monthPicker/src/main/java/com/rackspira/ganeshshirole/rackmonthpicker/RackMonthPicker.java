package com.rackspira.ganeshshirole.rackmonthpicker;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rackspira.ganeshshirole.rackmonthpicker.listener.DateMonthDialogListener;
import com.rackspira.ganeshshirole.rackmonthpicker.listener.MonthButtonListener;
import com.rackspira.ganeshshirole.rackmonthpicker.listener.OnCancelMonthDialogListener;
import com.rackspira.ganeshshirole.rackmonthpicker.util.MonthOfYear;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ganeshshirole on 31/12/16.
 */

public class RackMonthPicker {

    private static final String FROM = "From";
    private static final String TO = "To";

    private AlertDialog mAlertDialog;
    private RackMonthPicker.Builder builder;
    private Context context;
    private Button mPositiveButton;
    private Button mNegativeButton;
    private DateMonthDialogListener dateMonthDialogListener;
    private OnCancelMonthDialogListener onCancelMonthDialogListener;
    private List<MonthRadioButton> monthRadioButtonList;
    private boolean isBuild = false;
    private boolean isFrom = true;

    public RackMonthPicker(Context context) {
        this.context = context;
        monthRadioButtonList = new ArrayList<>();
        builder = new Builder();
    }

    public void show() {
        if (isBuild) {
            mAlertDialog.show();
        } else {
            builder.build();
            isBuild = true;
        }

        builder.validation();
    }

    public RackMonthPicker setPositiveButton(DateMonthDialogListener dateMonthDialogListener) {
        this.dateMonthDialogListener = dateMonthDialogListener;
        mPositiveButton.setOnClickListener(builder.positiveButtonClick());
        return this;
    }

    public RackMonthPicker setNegativeButton(OnCancelMonthDialogListener onCancelMonthDialogListener) {
        this.onCancelMonthDialogListener = onCancelMonthDialogListener;
        mNegativeButton.setOnClickListener(builder.negativeButtonClick());
        return this;
    }

    /*public RackMonthPicker setPositiveText(String text){
        mPositiveButton.setText(text);
        return this;
    }

    public RackMonthPicker setNegativeText(String text){
        mNegativeButton.setText(text);
        return this;
    }

    public void dismiss() {
        mAlertDialog.dismiss();
    }*/

    private class Builder implements MonthButtonListener, HorizontalPicker.OnItemSelected {

        private final ImageView next;
        private final ImageView previous;
        private final ArrayList<Object> yearArray;
        private MonthRadioButton monthRadioButton;
        private TextView mLabel;
        private TextView mTitle;
        private HorizontalPicker horizontalPicker;
        private int fromMonth = -1;
        private AlertDialog.Builder alertBuilder;
        private View contentView;
        private int year = 2000;
        private static final int MIN_LIMIT = 1990;
        private int min_limit_year = MIN_LIMIT;

        private Builder() {
            alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setCancelable(false);

            contentView = LayoutInflater.from(context).inflate(R.layout.date_month_dialog_view, null);
            contentView.setFocusable(true);
            contentView.setFocusableInTouchMode(true);

            mLabel = (TextView) contentView.findViewById(R.id.label);
            mTitle = (TextView) contentView.findViewById(R.id.title);

            horizontalPicker = (HorizontalPicker) contentView.findViewById(R.id.picker);
            horizontalPicker.setOnItemSelectedListener(this);
            yearArray = new ArrayList<>();

            setHorizontalPickerValues();

            next = (ImageView) contentView.findViewById(R.id.btn_next);
            next.setOnClickListener(nextButtonClick());

            previous = (ImageView) contentView.findViewById(R.id.btn_previous);
            previous.setOnClickListener(previousButtonClick());

            mPositiveButton = (Button) contentView.findViewById(R.id.btn_p);
            mNegativeButton = (Button) contentView.findViewById(R.id.btn_n);

            GridLayout gridLayout = (GridLayout) contentView.findViewById(R.id.radiogroup);
            for (int i = 1; i < 13; i++) {
                MonthRadioButton radioButton = new MonthRadioButton(context);
                radioButton.setIdMonth(i);
                radioButton.setMonth(MonthOfYear.getMonth(i - 1));
                radioButton.setButtonDrawable(MonthOfYear.getIcons(i - 1));
                if (i == 1) {
                    monthRadioButton = radioButton;
                    radioButton.setChecked(true);
                    mTitle.setText(MonthOfYear.getMonth(i - 1) + ", " + year);
                    if (isFrom) mLabel.setText(FROM);
                    else mLabel.setText(TO);
                }

                if (i == 2) {
                    radioButton.setEndDate(28);
                }

                if (i == 4 || i == 6 || i == 9 || i == 11) {
                    radioButton.setEndDate(30);
                }

                radioButton.setMonthListener(this, radioButton);
                monthRadioButtonList.add(radioButton);
                gridLayout.addView(radioButton);
            }
        }

        private void setHorizontalPickerValues() {
            for (int year = min_limit_year; year <= (Calendar.getInstance().get(Calendar.YEAR)); year++)
                yearArray.add(String.valueOf(year));
            horizontalPicker.setValues(yearArray.toArray(new CharSequence[yearArray.size()]));
            horizontalPicker.setSelectedItem(yearArray.indexOf(String.valueOf(year)));
        }

        public void build() {
            mAlertDialog = alertBuilder.create();
            mAlertDialog.show();
            mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            mAlertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_STATE);
            mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mAlertDialog.getWindow().setBackgroundDrawableResource(R.drawable.material_dialog_window);
            mAlertDialog.getWindow().setContentView(contentView);
        }

        public View.OnClickListener nextButtonClick() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    year++;
                    horizontalPicker.setSelectedItem(yearArray.indexOf(String.valueOf(year)));

                    validation();
                }
            };
        }

        private void validation() {

            boolean isInRange = true;
            boolean isPreSelected = false;
            boolean isNextSelected = false;
            boolean isMinYearAndYearSameSelected = true;
            for (int month = 0; month < monthRadioButtonList.size(); month++) {

                // Next Validation
                if (year == (Calendar.getInstance().get(Calendar.YEAR))) {
                    if (month > Calendar.getInstance().get(Calendar.MONTH)) {

                        if (monthRadioButtonList.get(month).isChecked())
                            isNextSelected = true;

                        monthRadioButtonList.get(month).setButtonDrawable(MonthOfYear.getIcons(month));
                        monthRadioButtonList.get(month).setEnabled(false);
                        monthRadioButtonList.get(month).setChecked(false);

                        if (isNextSelected) {
                            monthRadioButton = monthRadioButtonList.get((Calendar.getInstance().get(Calendar.MONTH)));
                            monthRadioButton.setButtonDrawable(MonthOfYear.getIcons((Calendar.getInstance().get(Calendar.MONTH))));
                            monthRadioButton.setChecked(true);
                        }
                    }
                } else if (year < (Calendar.getInstance().get(Calendar.YEAR)) && year != min_limit_year) {
                    monthRadioButtonList.get(month).setButtonDrawable(MonthOfYear.getIcons(month));
                    monthRadioButtonList.get(month).setEnabled(true);
                }

                if (year == min_limit_year) {
                    if (month < (fromMonth - 1)) {
                        if (monthRadioButtonList.get(month).isChecked())
                            isPreSelected = true;
                        monthRadioButtonList.get(month).setButtonDrawable(MonthOfYear.getIcons(month));
                        monthRadioButtonList.get(month).setEnabled(false);
                        monthRadioButtonList.get(month).setChecked(false);
                        if (isPreSelected) {
                            monthRadioButton = monthRadioButtonList.get((fromMonth - 1));
                            monthRadioButton.setChecked(true);
                        }
                    } else {
                        if (!isFrom) {

                            if (min_limit_year == (Calendar.getInstance().get(Calendar.YEAR))) {
                                if (month <= (Calendar.getInstance().get(Calendar.MONTH))) {

                                    // Code A

                                    if (monthRadioButtonList.get(month).isChecked())
                                        isMinYearAndYearSameSelected = false;

                                    if (isMinYearAndYearSameSelected) {
                                        monthRadioButtonList.get(month).setButtonDrawable(MonthOfYear.getActiveIcons(month));
                                        monthRadioButtonList.get(month).setEnabled(true);
                                    } else {
                                        monthRadioButtonList.get(month).setButtonDrawable(MonthOfYear.getIcons(month));
                                        monthRadioButtonList.get(month).setEnabled(true);
                                    }
                                }
                            } else {

                                // Code A

                                if (monthRadioButtonList.get(month).isChecked())
                                    isMinYearAndYearSameSelected = false;

                                if (isMinYearAndYearSameSelected) {
                                    monthRadioButtonList.get(month).setButtonDrawable(MonthOfYear.getActiveIcons(month));
                                    monthRadioButtonList.get(month).setEnabled(true);
                                } else {
                                    monthRadioButtonList.get(month).setButtonDrawable(MonthOfYear.getIcons(month));
                                    monthRadioButtonList.get(month).setEnabled(true);
                                }
                            }
                        }
                    }
                } else if (year < min_limit_year) {
                    monthRadioButtonList.get(month).setButtonDrawable(MonthOfYear.getIcons(month));
                    monthRadioButtonList.get(month).setEnabled(false);
                } else if (year > min_limit_year && year < (Calendar.getInstance().get(Calendar.YEAR))) {
                    monthRadioButtonList.get(month).setEnabled(true);
                    if (!isFrom) {
                        if (monthRadioButtonList.get(month).isChecked())
                            isInRange = false;
                        monthRadioButtonList.get(month).setButtonDrawable(MonthOfYear.getIcons(month));
                        if (isInRange)
                            monthRadioButtonList.get(month).setButtonDrawable(MonthOfYear.getActiveIcons(month));
                    }
                }
            }

            manageDoneButton();

            if (year <= min_limit_year) {
                previous.setEnabled(false);
                previous.setAlpha(.4f);
            } else {
                previous.setEnabled(true);
                previous.setAlpha(1f);
            }

            if (year >= (Calendar.getInstance().get(Calendar.YEAR))) {
                next.setEnabled(false);
                next.setAlpha(.4f);
            } else {
                next.setEnabled(true);
                next.setAlpha(1f);
            }

            mTitle.setText(monthRadioButton.getMonth() + ", " + year);
        }

        private void manageDoneButton() {
            if (!isFrom) {
                if (year < min_limit_year) {
                    mPositiveButton.setAlpha(.4f);
                    mPositiveButton.setEnabled(false);
                } else {
                    mPositiveButton.setAlpha(1f);
                    mPositiveButton.setEnabled(true);
                }
            }
        }

        public View.OnClickListener previousButtonClick() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    year--;
                    horizontalPicker.setSelectedItem(yearArray.indexOf(String.valueOf(year)));
                    validation();
                }
            };
        }

        public View.OnClickListener positiveButtonClick() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isFrom) {
                        dateMonthDialogListener.onDateMonth(
                                monthRadioButton.getIdMonth(),
                                monthRadioButton.getStartDate(),
                                monthRadioButton.getEndDate(),
                                year, mTitle.getText().toString(), isFrom);
                        mLabel.setText(TO);
                        fromMonth = monthRadioButton.getIdMonth();

                        for (int month = 0; month < (fromMonth - 1); month++)
                                monthRadioButtonList.get(month).setEnabled(false);

                        manageDoneButton();

                        previous.setAlpha(.4f);
                        isFrom = false;
                        mPositiveButton.setText(context.getResources().getString(R.string.done));
                        min_limit_year = year;
                    } else {
                        dateMonthDialogListener.onDateMonth(
                                monthRadioButton.getIdMonth(),
                                monthRadioButton.getStartDate(),
                                monthRadioButton.getEndDate(),
                                year, mTitle.getText().toString(), isFrom);
                        mLabel.setText(FROM);
                        isFrom = true;
                        fromMonth = -1;
                        previous.setAlpha(1f);
                        min_limit_year = MIN_LIMIT;
                        for (int month = 0; month < monthRadioButtonList.size(); month++) {
                            monthRadioButtonList.get(month).setEnabled(true);
                            monthRadioButtonList.get(month).setButtonDrawable(MonthOfYear.getIcons(month));
                        }
                        mPositiveButton.setText(context.getResources().getString(R.string.next));
                        mAlertDialog.dismiss();
                    }
                }
            };
        }

        public View.OnClickListener negativeButtonClick() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mLabel.setText(FROM);
                    isFrom = true;
                    fromMonth = -1;
                    previous.setAlpha(1f);

                    mPositiveButton.setAlpha(1f);
                    mPositiveButton.setEnabled(true);

                    min_limit_year = MIN_LIMIT;
                    mPositiveButton.setText(context.getResources().getString(R.string.next));
                    for (int month = 0; month < monthRadioButtonList.size(); month++) {
                        monthRadioButtonList.get(month).setEnabled(true);
                        monthRadioButtonList.get(month).setButtonDrawable(MonthOfYear.getIcons(month));
                    }
                    onCancelMonthDialogListener.onCancel(mAlertDialog);
                }
            };
        }

        @Override
        public void monthClick(MonthRadioButton objectMonth) {
            mTitle.setText(objectMonth.getMonth() + ", " + year);
            monthRadioButton = objectMonth;

            boolean isDisableNext = true;
            for (int i = 0; i < 12; i++) {

                if (monthRadioButtonList.get(i) != objectMonth) {
                    monthRadioButtonList.get(i).setChecked(false);
                } else isDisableNext = false;

                if (!isFrom && monthRadioButtonList.get(i).isEnabled()) {
                    monthRadioButtonList.get(i).setButtonDrawable(MonthOfYear.getIcons(i));
                    if (isDisableNext)
                        monthRadioButtonList.get(i).setButtonDrawable(MonthOfYear.getActiveIcons(i));
                }
            }
        }

        @Override
        public void onItemSelected(int index) {
            year = Integer.parseInt((String) yearArray.get(index));
            validation();
        }
    }
}
