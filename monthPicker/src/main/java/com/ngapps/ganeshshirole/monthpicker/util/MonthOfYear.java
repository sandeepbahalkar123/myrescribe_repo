package com.ngapps.ganeshshirole.monthpicker.util;

import com.ngapps.ganeshshirole.monthpicker.R;

/**
 * Created by ganeshshirole on 31/12/16.
 */

public class MonthOfYear {
    private static String months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private static int icons[] = {
            R.drawable.radio_button_background_jan,
            R.drawable.radio_button_background_feb,
            R.drawable.radio_button_background_mar,
            R.drawable.radio_button_background_apr,
            R.drawable.radio_button_background_may,
            R.drawable.radio_button_background_jun,
            R.drawable.radio_button_background_jul,
            R.drawable.radio_button_background_aug,
            R.drawable.radio_button_background_sep,
            R.drawable.radio_button_background_oct,
            R.drawable.radio_button_background_nov,
            R.drawable.radio_button_background_dec
    };

    private static int activeIcons[] = {
            R.drawable.jan_active,
            R.drawable.feb_active,
            R.drawable.mar_active,
            R.drawable.apr_active,
            R.drawable.may_active,
            R.drawable.jun_active,
            R.drawable.jul_active,
            R.drawable.aug_active,
            R.drawable.sep_active,
            R.drawable.oct_active,
            R.drawable.nov_active,
            R.drawable.dec_active
    };

    public static String getMonth(int idx) {
        return months[idx];
    }

    public static int getIcons(int idx) {
        return icons[idx];
    }

    public static int getActiveIcons(int idx) {
        return activeIcons[idx];
    }
}
