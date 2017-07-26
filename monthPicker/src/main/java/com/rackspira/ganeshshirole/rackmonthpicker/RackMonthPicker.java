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

        builder.validateNextYearMonth();
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

    private class Builder implements MonthButtonListener {

        private final ImageView next;
        private final ImageView previous;
        private MonthRadioButton monthRadioButton;
        private TextView mLabel;
        private TextView mTitle;
        private TextView mYear;
        private int fromYear = -1;
        private int fromMonth = -1;
        private AlertDialog.Builder alertBuilder;
        private View contentView;
        private int year = 2017;

        private Builder() {
            alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setCancelable(false);

            contentView = LayoutInflater.from(context).inflate(R.layout.date_month_dialog_view, null);
            contentView.setFocusable(true);
            contentView.setFocusableInTouchMode(true);

            mLabel = (TextView) contentView.findViewById(R.id.label);
            mTitle = (TextView) contentView.findViewById(R.id.title);
            mYear = (TextView) contentView.findViewById(R.id.text_year);
            mYear.setText(year + "");

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
                    mYear.setText(year + "");
                    mTitle.setText(monthRadioButton.getMonth() + ", " + year);

                    boolean isDisableNext = true;
                    for (int month = 0; month < monthRadioButtonList.size(); month++) {

                        monthRadioButtonList.get(month).setEnabled(true);

                        if (!isFrom) {

                            if (monthRadioButtonList.get(month).isChecked())
                                isDisableNext = false;

                            monthRadioButtonList.get(month).setButtonDrawable(MonthOfYear.getIcons(month));
                            if (isDisableNext)
                                monthRadioButtonList.get(month).setButtonDrawable(MonthOfYear.getActiveIcons(month));

                        }
                    }

                    validateNextYearMonth();

                    previous.setAlpha(1f);
                }
            };
        }

        public View.OnClickListener previousButtonClick() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fromYear < year) {
                        year--;
                        mYear.setText(year + "");
                        mTitle.setText(monthRadioButton.getMonth() + ", " + year);
                        if (fromYear == year) {
                            boolean isChecked = false;
                            for (int month = 0; month < (fromMonth - 1); month++) {

                                if (monthRadioButtonList.get(month).isChecked())
                                    isChecked = true;

                                monthRadioButtonList.get(month).setEnabled(false);
                                monthRadioButtonList.get(month).setButtonDrawable(MonthOfYear.getIcons(month));
                                monthRadioButtonList.get(month).setChecked(false);
                                previous.setAlpha(.4f);

                                if (isChecked)
                                    monthRadioButtonList.get(month + 1).setChecked(true);
                            }
                        }
                    } else if (fromYear > year) {
                        for (int month = 0; month < monthRadioButtonList.size(); month++) {
                            monthRadioButtonList.get(month).setEnabled(true);
                        }
                    }

                    validateNextYearMonth();
                }
            };
        }

        private void validateNextYearMonth() {
            if (year >= (Calendar.getInstance().get(Calendar.YEAR))) {
                next.setEnabled(false);
                next.setAlpha(0.4f);

                for (int month = (Calendar.getInstance().get(Calendar.MONTH)) + 1; month < monthRadioButtonList.size(); month++) {
                    monthRadioButtonList.get(month).setButtonDrawable(MonthOfYear.getIcons(month));
                        monthRadioButtonList.get(month).setEnabled(false);
                        if (monthRadioButtonList.get(month).isChecked()) {
                            monthRadioButtonList.get(month).setChecked(false);
                            monthRadioButtonList.get((Calendar.getInstance().get(Calendar.MONTH))).setButtonDrawable(MonthOfYear.getIcons((Calendar.getInstance().get(Calendar.MONTH))));
                            monthRadioButtonList.get((Calendar.getInstance().get(Calendar.MONTH))).setChecked(true);
                        }
                }
            } else if (year == (Calendar.getInstance().get(Calendar.YEAR)) - 1) {
                next.setEnabled(true);
                next.setAlpha(1f);

                for (int month = 0; month < monthRadioButtonList.size(); month++)
                    monthRadioButtonList.get(month).setEnabled(true);
            }
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
                        fromYear = year;

                        for (int month = 0; month < monthRadioButtonList.size(); month++) {
                            if (month < (fromMonth - 1))
                                monthRadioButtonList.get(month).setEnabled(false);
                        }
                        previous.setAlpha(.4f);
                        isFrom = false;
                        mPositiveButton.setText(context.getResources().getString(R.string.done));
                    } else {
                        dateMonthDialogListener.onDateMonth(
                                monthRadioButton.getIdMonth(),
                                monthRadioButton.getStartDate(),
                                monthRadioButton.getEndDate(),
                                year, mTitle.getText().toString(), isFrom);
                        mLabel.setText(FROM);
                        isFrom = true;
                        fromMonth = -1;
                        fromYear = -1;
                        previous.setAlpha(1f);

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
                    fromYear = -1;
                    previous.setAlpha(1f);
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
    }
}
