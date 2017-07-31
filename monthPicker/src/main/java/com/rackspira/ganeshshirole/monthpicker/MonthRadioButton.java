package com.rackspira.ganeshshirole.monthpicker;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;

import com.rackspira.ganeshshirole.monthpicker.listener.MonthButtonListener;

/**
 * Created by ganeshshirole on 31/12/16.
 */

public class MonthRadioButton extends AppCompatRadioButton {
    private int idMonth;
    private int startDate = 1;
    private int endDate = 31;
    private String month;
    private MonthButtonListener listener;

    public MonthRadioButton(Context context) {
        super(context);
    }

    public void setMonthListener(final MonthButtonListener listener, final MonthRadioButton monthRadioButton){
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.monthClick(monthRadioButton);
            }
        });
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled)
            setAlpha(1f);
        else setAlpha(.4f);
        super.setEnabled(enabled);
    }

    public int getIdMonth() {
        return idMonth;
    }

    public void setIdMonth(int idMonth) {
        this.idMonth = idMonth;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
