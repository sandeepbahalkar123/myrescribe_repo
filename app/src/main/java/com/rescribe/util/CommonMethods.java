package com.rescribe.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.rescribe.R;
import com.rescribe.interfaces.CheckIpConnection;
import com.rescribe.interfaces.DatePickerDialogListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CommonMethods {

    private static final String TAG = "Rescribe/CommonMethods";
    public static final String TODAY = "Today";
    private static boolean encryptionIsOn = true;
    private static String aBuffer = "";
    private static CheckIpConnection mCheckIpConnection;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private DatePickerDialogListener mDatePickerDialogListener;

    public static void showToast(Context context, String error) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
    }

    // 1ˢᵗ, 2ⁿᵈ, 3ʳᵈ, 4ᵗʰ
    public static String ordinal(int i) {
        String[] sufixes = new String[]{"ᵗʰ", "ˢᵗ", "ⁿᵈ", "ʳᵈ", "ᵗʰ", "ᵗʰ", "ᵗʰ", "ᵗʰ", "ᵗʰ", "ᵗʰ"};
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "ᵗʰ";
            default:
                return i + sufixes[i % 10];

        }
    }

    public static void setBackground(View v, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            v.setBackground(drawable);
        else
            v.setBackgroundDrawable(drawable);
    }

    public static int getDocumentIconByExtension(String extension) {
        if (extension.contains(".doc") || extension.contains(".odt")) {
            return R.drawable.word;
        } else if (extension.contains(".ppt") || extension.contains(".odp")) {
            return R.drawable.ppt;
        } else if (extension.contains(".xls") || extension.contains(".ods")) {
            return R.drawable.excel;
        } else if (extension.contains(".pdf")) {
            return R.drawable.pdf;
        } else return droidninja.filepicker.R.drawable.image_placeholder;
    }

    public static void showSnack(View mViewById, String msg) {
        if (mViewById != null) {
            Snackbar.make(mViewById, msg, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            Log.d(TAG, "null snacbar view" + msg);
        }
    }


    /**
     * Email validator
     *
     * @param emailId
     * @return
     */
    public final static boolean isValidEmail(CharSequence emailId) {
        return emailId != null && android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches();
    }


    public static void hideKeyboard(Activity cntx) {
        // Check if no view has focus:
        View view = cntx.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) cntx.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static String getCurrentDateTime() // for enrollmentId
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);

        String Year = String.valueOf(year);
        StringBuffer dString = new StringBuffer();
        dString.append((date > 9) ? String.valueOf(date) : ("0" + date));
        dString.append("-");
        dString.append((month > 9) ? String.valueOf(month) : ("0" + month));
        dString.append("-");
        dString.append(year);
        return dString.toString();
    }
    public static String getCurrentDateWithSlash() // for enrollmentId
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);
        String Year = String.valueOf(year);
        StringBuffer dString = new StringBuffer();
        dString.append(date);
        dString.append("/");
        dString.append(month);
        dString.append("/");
        dString.append(year);
        return dString.toString();
    }

    public static String getCurrentDate() // for enrollmentId
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(RescribeConstants.DD_MM_YYYY, Locale.US);
        return df.format(c.getTime());
    }

    public static void showSnack(Context mContext, View mViewById, String msg) {
        Snackbar snack = Snackbar.make(mViewById, msg, Snackbar.LENGTH_SHORT);
        ViewGroup group = (ViewGroup) snack.getView();
        group.setBackgroundColor(mContext.getResources().getColor(R.color.errorColor));
        snack.show();
    }


    public static String getCurrentTimeStamp(String expectedFormat) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(expectedFormat, Locale.US);
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }


    public static void dateDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);

    }


    public static String getCalculatedDate(String inFormat, int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, days);
        Date date = new Date(cal.getTimeInMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(inFormat, Locale.US);
        return dateFormat.format(date);
    }

    public static int getAge(int year, int month, int day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, noofyears;

        y = cal.get(Calendar.YEAR);// current year ,
        m = cal.get(Calendar.MONTH);// current month
        d = cal.get(Calendar.DAY_OF_MONTH);//current day
        cal.set(year, month, day);// here ur date
        noofyears = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --noofyears;
        }
        if (noofyears < 0)
            throw new IllegalArgumentException("age < 0");
        System.out.println(noofyears);
        return noofyears;
    }

    public static String getDayFromDateTime(String dateText, String originalDateFormat, String expectedDateFormat) {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date yesterday = calendar.getTime();

        DateFormat expectedFormat = new SimpleDateFormat(expectedDateFormat, Locale.US);

        SimpleDateFormat originalFormat = new SimpleDateFormat(originalDateFormat, Locale.US);
        Date date;
        try {
            date = originalFormat.parse(dateText);
        } catch (ParseException ex) {
            return "";
        }

        String originalDateAsString = expectedFormat.format(date);

        String todayAsString = expectedFormat.format(today);
        String yesterdayAsString = expectedFormat.format(yesterday);

        if (todayAsString.equals(originalDateAsString))
            return "Today";

        if (yesterdayAsString.equals(originalDateAsString))
            return "Yesterday";

        return originalDateAsString;
    }

    public static String getDayFromDate(String dateFormat, String date) {

        try {
            date = date.trim();
            Date currentDate = new Date();
            String timeString = new SimpleDateFormat(dateFormat + " HH:mm:ss", Locale.US).format(currentDate).substring(10);

            SimpleDateFormat mainDateFormat = new SimpleDateFormat(dateFormat + " HH:mm:ss", Locale.US);
            Date formattedInputDate = null;
            try {
                formattedInputDate = mainDateFormat.parse(date + timeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (date.trim().equalsIgnoreCase(new SimpleDateFormat(dateFormat, Locale.US).format(currentDate).trim())) {
                return TODAY;
            }
            //-----------
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            Date yesterdayDate = cal.getTime();
            String sDate = new SimpleDateFormat(dateFormat, Locale.US).format(yesterdayDate);
            try {
                yesterdayDate = mainDateFormat.parse(sDate + timeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (formattedInputDate.getTime() == yesterdayDate.getTime()) {
                return "Yesterday";
            }
            //-----------
            cal = Calendar.getInstance();
            cal.add(Calendar.DATE, +1);
            Date tomorrowDate = cal.getTime();
            sDate = new SimpleDateFormat(dateFormat, Locale.US).format(tomorrowDate);
            try {
                tomorrowDate = mainDateFormat.parse(sDate + timeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (formattedInputDate.getTime() == tomorrowDate.getTime()) {
                return "Tomorrow";
            } else {
                DateFormat f = new SimpleDateFormat("EEEE", Locale.US);
                try {
                    return f.format(formattedInputDate);
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }
        } catch (NullPointerException e) {
            return "";
        }
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    public static void Log(String tag, String message) {
        Log.e(tag, "" + message);
    }


    public static String getFormattedDate(String strDate, String sourceFormat, String destinyFormat) {

        if (!strDate.equals("")) {
            SimpleDateFormat df;
            df = new SimpleDateFormat(sourceFormat, Locale.US);
            Date date;
            try {
                date = df.parse(strDate);
                df = new SimpleDateFormat(destinyFormat, Locale.US);
                return df.format(date);
            } catch (ParseException e) {
                return "";
            }
        } else return "";
    }

    /**
     * The method will return the date and time in requested format
     *
     * @param selectedDateTime to be converted to requested format
     * @param requestedFormat  the format in which the provided datetime needs to be changed
     * @param formatString     differentiate parameter to format date or time
     * @return formated date or time
     */
    public static String formatDateTime(String selectedDateTime, String requestedFormat, String currentDateFormat, String formatString) {

        if (formatString.equalsIgnoreCase(RescribeConstants.TIME)) {
            // SimpleDateFormat ft = new SimpleDateFormat(RescribeConstants.DATE_PATTERN.HH_MM, Locale.US);
            SimpleDateFormat ft = new SimpleDateFormat(currentDateFormat, Locale.US);

            Date dateObj = null;

            try {
                dateObj = ft.parse(selectedDateTime);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            long millis = dateObj.getTime();
            SimpleDateFormat simpleDateFormatObj = new SimpleDateFormat(requestedFormat, Locale.US);
            return simpleDateFormatObj.format(millis);

        }//if

        else if (formatString.equalsIgnoreCase(RescribeConstants.DATE)) {
            SimpleDateFormat ft = new SimpleDateFormat(currentDateFormat, Locale.US);

            Date dateObj = null;

            try {
                dateObj = ft.parse(selectedDateTime);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            SimpleDateFormat simpleDateFormatObj = new SimpleDateFormat(requestedFormat, Locale.US);
            return simpleDateFormatObj.format(dateObj);


        }
        return null;

    }

    public static int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static void showInfoDialog(String msg, final Context mContext, final boolean closeActivity) {

        final Dialog dialog = new Dialog(mContext);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ok);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        ((TextView) dialog.findViewById(R.id.textview_sucess)).setText(msg);

        dialog.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (closeActivity)
                    ((AppCompatActivity) mContext).finish();
            }
        });

        dialog.show();
    }

    public static String getMealTime(int hour, int mint, Context context) {
        //BB : 7-11,lunch : 11-3,dinner :7-11
        String time = "";
        if (hour > 7 && hour < 11)
            time = context.getString(R.string.break_fast);
        else if (hour >= 11 && hour < 15)
            time = context.getString(R.string.mlunch);
        else if (hour >= 15 && hour < 17)
            time = context.getString(R.string.msnacks);
        else if (hour >= 17 && hour <= 23)
            time = context.getString(R.string.mdinner);

        CommonMethods.Log(TAG, "hour" + hour);
        CommonMethods.Log(TAG, "getMealTime" + time);
        return time;
    }

    public static String getMealTimeForLocalNotification(int hour, Context context) {
        //BB : 7-11,lunch : 11-3,dinner :7-11
        String time = "";
        if (hour >= 8 && hour < 11)
            time = context.getString(R.string.break_fast);
        else if (hour >= 14 && hour < 16)
            time = context.getString(R.string.mlunch);
        else if (hour >= 17 && hour < 19)
            time = context.getString(R.string.msnacks);
        else if (hour >= 20 && hour <= 23)
            time = context.getString(R.string.mdinner);

        CommonMethods.Log(TAG, "hour" + hour);
        CommonMethods.Log(TAG, "getMealTime" + time);
        return time;
    }

    // Return medicine Icon's

    public static Drawable getMedicineTypeImage(String medicineTypeName, Context context, int colorCodeForUnread) {

        boolean isReqUnread = ContextCompat.getColor(context, R.color.white) == colorCodeForUnread;

        Drawable abbreviation = ContextCompat.getDrawable(context, R.drawable.defaulticon);
        if (medicineTypeName.equalsIgnoreCase("syrup")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.syrup);
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_syrup);
            }
        } else if (medicineTypeName.equalsIgnoreCase("Tablet")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.tablet);
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_tablet);
            }
        } else if (medicineTypeName.equalsIgnoreCase("Capsule")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.capsule);
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_capsule);
            }
        } else if (medicineTypeName.equalsIgnoreCase("injection")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.injection);
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_injection);
            }
        } else if (medicineTypeName.equalsIgnoreCase("insulin")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.insulin);
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_insulin);
            }
        } else if (medicineTypeName.equalsIgnoreCase("Inhaler")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.inhaler);
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_inhaler);
            }
        } else if (medicineTypeName.equalsIgnoreCase("liquid")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.liquid); // not found
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_liquid);
            }
        } else if (medicineTypeName.equalsIgnoreCase("tan")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.tan);// not found
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_tan);
            }
        } else if (medicineTypeName.equalsIgnoreCase("cream")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.cream);
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_cream);
            }
        } else if (medicineTypeName.equalsIgnoreCase("jelly")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.jelly);
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_jelly);
            }
        } else if (medicineTypeName.equalsIgnoreCase("local application")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.defaulticon);// not found
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_jelly);
            }
        } else if (medicineTypeName.equalsIgnoreCase("ointment")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.ointment);
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_ointment);
            }
        } else if (medicineTypeName.equalsIgnoreCase("lotion")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.lotion);
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_lotion);
            }
        } else if (medicineTypeName.equalsIgnoreCase("drops")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.drop);
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_drop);
            }
        } else if (medicineTypeName.equalsIgnoreCase("eye drops")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.eyedrops);
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_eyedrops);
            }
        } else if (medicineTypeName.equalsIgnoreCase("nasal drops")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.nasaldrop);
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_nasaldrop);
            }
        } else if (medicineTypeName.equalsIgnoreCase("nasal spray")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.nasalspray);
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_nasalspray);
            }
        } else if (medicineTypeName.equalsIgnoreCase("powder")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.ointmentpowder);
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_ointmentpowder);
            }
        } else if (medicineTypeName.equalsIgnoreCase("respules")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.respules);
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_respules);
            }
        } else if (medicineTypeName.equalsIgnoreCase("rotacaps")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.rotocaps);
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_rotocaps);
            }
        } else if (medicineTypeName.equalsIgnoreCase("sachet")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.sachet);
            if (isReqUnread) {
                abbreviation = ContextCompat.getDrawable(context, R.drawable.unread_notification_sachet);
            }
        }

        return abbreviation;
    }

    public static int getCaseStudyIcons(String caseStudyName) {

        // Drawable abbreviation = ContextCompat.getDrawable(context, R.drawable.ellipse_2);
        int abbreviation = R.drawable.commonicon;
        if (caseStudyName.contains("complaint"))
            abbreviation = R.drawable.complaints;
        else if (caseStudyName.contains("vital"))
            abbreviation = R.drawable.vitals;
        else if (caseStudyName.contains("remark"))
            abbreviation = R.drawable.remarks;
        else if (caseStudyName.contains("diagnosis"))
            abbreviation = R.drawable.diagnosis;
        else if (caseStudyName.contains("prescription"))
            abbreviation = R.drawable.prescription;
        else if (caseStudyName.contains("investigation"))
            abbreviation = R.drawable.investigations;
        else if (caseStudyName.contains("advice"))
            abbreviation = R.drawable.advice; // not found
        else if (caseStudyName.contains("treatment plan"))
            abbreviation = R.drawable.treatment_plan; // not found
        else if (caseStudyName.contains("surgery"))
            abbreviation = R.drawable.surgery; // not found
        else if (caseStudyName.contains("vaccination"))
            abbreviation = R.drawable.vaccination; // not found
        else if (caseStudyName.contains("general precaution"))
            abbreviation = R.drawable.generalprecautions; // not found
        else if (caseStudyName.contains("pre-operative precaution"))
            abbreviation = R.drawable.preoperativeprecautions; // not found
        else if (caseStudyName.contains("post-operative care"))
            abbreviation = R.drawable.postoperativecare; // not found
        else if (caseStudyName.contains("pain score"))
            abbreviation = R.drawable.painscore; // not found
        else if (caseStudyName.contains("exercise"))
            abbreviation = R.drawable.exercise; // not found
        else if (caseStudyName.contains("finding"))
            abbreviation = R.drawable.finding; // not found
        else if (caseStudyName.contains("allergie"))
            abbreviation = R.drawable.allergy; // not found

        return abbreviation;
    }

    public static int getVitalIcons(String vitalDetailName) {

        // Drawable abbreviation = ContextCompat.getDrawable(context, R.drawable.ellipse_2);
        int abbreviation = R.drawable.defaulticon;
        if (vitalDetailName.equalsIgnoreCase("bp"))
            abbreviation = R.drawable.bp;
        else if (vitalDetailName.equalsIgnoreCase("weight"))
            abbreviation = R.drawable.weight;
        else if (vitalDetailName.equalsIgnoreCase("height"))
            abbreviation = R.drawable.height;
        else if (vitalDetailName.equalsIgnoreCase("bmi"))
            abbreviation = R.drawable.bmi;
        else if (vitalDetailName.equalsIgnoreCase("totalcholesterolhdlcholesterol"))
            abbreviation = R.drawable.totalcholesterolhdlcholesterol;
        else if (vitalDetailName.equalsIgnoreCase("ldlhdl"))
            abbreviation = R.drawable.ldlhdl;
        else if (vitalDetailName.equalsIgnoreCase("triglycerides"))
            abbreviation = R.drawable.triglycerides;
        else if (vitalDetailName.equalsIgnoreCase("hdlcholesterol"))
            abbreviation = R.drawable.hdlcholesterol;
        else if (vitalDetailName.equalsIgnoreCase("ldlcholesterol"))
            abbreviation = R.drawable.ldlcholesterol;
        else if (vitalDetailName.equalsIgnoreCase("totalcholesterol"))
            abbreviation = R.drawable.totalcholesterol;
        else if (vitalDetailName.equalsIgnoreCase("gfr"))
            abbreviation = R.drawable.gfr;
        else if (vitalDetailName.equalsIgnoreCase("bun"))
            abbreviation = R.drawable.bun;
        else if (vitalDetailName.equalsIgnoreCase("creatinine"))
            abbreviation = R.drawable.creatinine;
        else if (vitalDetailName.equalsIgnoreCase("respiratoryrate"))
            abbreviation = R.drawable.respiratoryrate;
        else if (vitalDetailName.equalsIgnoreCase("heartrate"))
            abbreviation = R.drawable.heartrate;
        else if (vitalDetailName.equalsIgnoreCase("temperature"))
            abbreviation = R.drawable.temperature;
        else if (vitalDetailName.equalsIgnoreCase("fbs"))
            abbreviation = R.drawable.fbs;
        else if (vitalDetailName.equalsIgnoreCase("ppbs"))
            abbreviation = R.drawable.ppbs;
        else if (vitalDetailName.equalsIgnoreCase("spo_2"))
            abbreviation = R.drawable.spo_2;
        else if (vitalDetailName.equalsIgnoreCase("platelet"))
            abbreviation = R.drawable.platelet;
        else if (vitalDetailName.equalsIgnoreCase("esr"))
            abbreviation = R.drawable.esr;
        else if (vitalDetailName.equalsIgnoreCase("hb"))
            abbreviation = R.drawable.hb;

        return abbreviation;
    }


    public static int getDoctorSpecialistIcons(String caseStudyName, Context mContext) {

        // Drawable abbreviation = ContextCompat.getDrawable(context, R.drawable.ellipse_2);
        int abbreviation = R.drawable.endocrinologist;
        if (caseStudyName.equalsIgnoreCase(mContext.getString(R.string.cardiologist)))
            abbreviation = R.drawable.cardiologist;
        else if (caseStudyName.equalsIgnoreCase(mContext.getString(R.string.ophthalmologist)))
            abbreviation = R.drawable.ophthalmologist;
        else if (caseStudyName.equalsIgnoreCase(mContext.getString(R.string.gastro)))
            abbreviation = R.drawable.gastro;
        else if (caseStudyName.equalsIgnoreCase(mContext.getString(R.string.physiotherapist)))
            abbreviation = R.drawable.physiotherapist;
        else if (caseStudyName.equalsIgnoreCase(mContext.getString(R.string.orthopaedic)))
            abbreviation = R.drawable.orthopaedic;
        else if (caseStudyName.equalsIgnoreCase(mContext.getString(R.string.ent)))
            abbreviation = R.drawable.ent;
        else if (caseStudyName.equalsIgnoreCase(mContext.getString(R.string.dentist)))
            abbreviation = R.drawable.dentist;
        else if (caseStudyName.equalsIgnoreCase(mContext.getString(R.string.gynecologist)))
            abbreviation = R.drawable.gynecologist;
        else if (caseStudyName.equalsIgnoreCase(mContext.getString(R.string.paediatric)))
            abbreviation = R.drawable.paediatric;
        else if (caseStudyName.equalsIgnoreCase(mContext.getString(R.string.dermatologist)))
            abbreviation = R.drawable.dermatologist;
        else if (caseStudyName.equalsIgnoreCase(mContext.getString(R.string.neurologist)))
            abbreviation = R.drawable.neurologist;
        else if (caseStudyName.equalsIgnoreCase(mContext.getString(R.string.physician)))
            abbreviation = R.drawable.physician;
        else if (caseStudyName.equalsIgnoreCase(mContext.getString(R.string.psychiatrist)))
            abbreviation = R.drawable.psychiatrist;
        else if (caseStudyName.equalsIgnoreCase(mContext.getString(R.string.oncologist)))
            abbreviation = R.drawable.oncologist;
        else if (caseStudyName.equalsIgnoreCase(mContext.getString(R.string.urologist)))
            abbreviation = R.drawable.urologist;
        else if (caseStudyName.equalsIgnoreCase(mContext.getString(R.string.nephrologist)))
            abbreviation = R.drawable.nephrologist;
        else if (caseStudyName.equalsIgnoreCase(mContext.getString(R.string.surgeon)))
            abbreviation = R.drawable.surgeon;
        else if (caseStudyName.equalsIgnoreCase(mContext.getString(R.string.endocrinologist)))
            abbreviation = R.drawable.endocrinologist;

        return abbreviation;
    }

    /*public static int getServices(String caseStudyName, Context mContext) {

        // Drawable abbreviation = ContextCompat.getDrawable(context, R.drawable.ellipse_2);
        int abbreviation = R.drawable.gynecologist;
        if (caseStudyName.equalsIgnoreCase("Doctors"))
            abbreviation = R.drawable.doctor;
        else if (caseStudyName.equalsIgnoreCase("Hospitals"))
            abbreviation = R.drawable.hospital_practices;
        else if (caseStudyName.equalsIgnoreCase("Laboratories"))
            abbreviation = R.drawable.laboratories;
        else if (caseStudyName.equalsIgnoreCase("Pharmacy"))
            abbreviation = R.drawable.layer_9;
        else if (caseStudyName.equalsIgnoreCase("Diagnostic Centers"))
            abbreviation = R.drawable.investigation_servcies;
        else if (caseStudyName.equalsIgnoreCase("Blood Bank"))
            abbreviation = R.drawable.bloodbank;
        else if (caseStudyName.equalsIgnoreCase("Ambulance"))
            abbreviation = R.drawable.ambulance;

        return abbreviation;
    }*/


    public static Date convertStringToDate(String dateString, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.US);
        Date date = null;

        try {
            date = formatter.parse(dateString.trim());
        } catch (ParseException e) {
            e.printStackTrace();
            CommonMethods.Log("convertStringToDate", "convertStringToDate EXCEPTION OCCURS : " + e.getMessage());
        }
        return date;
    }

    public static String toCamelCase(String input) {
        StringBuilder result = new StringBuilder();
        char firstChar = input.charAt(0);
        result.append(Character.toUpperCase(firstChar));
        for (int i = 1; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            char previousChar = input.charAt(i - 1);
            if (previousChar == ' ')
                result.append(Character.toUpperCase(currentChar));
            else
                result.append(Character.toLowerCase(currentChar));
        }
        return result.toString();
    }

    public static String getDateSelectedDoctorVisit(String visitdate, String dateFormat) {
        String yourDate = null;

        DateFormat format = new SimpleDateFormat(dateFormat, Locale.US);

        try {
            Date date = format.parse(visitdate);
            format = new SimpleDateFormat("d'th' MMM, yyyy", Locale.US);
            yourDate = format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return yourDate;

    }


    public static String getSuffixForNumber(final int n) {
        //  checkArgument(n >= 1 && n <= 31, "illegal day of month: " + n);
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }

    }

    //TODO : this is done for temp
    public static ArrayList<String> getYearForDoctorList() {
        ArrayList<String> a = new ArrayList<>();
        a.add("2017");
        return a;
    }

    public void datePickerDialog(Context context, DatePickerDialogListener datePickerDialogListener, Date dateToSet) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();

        if (dateToSet != null) {
            c.setTime(dateToSet);
        }

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mDatePickerDialogListener = datePickerDialogListener;
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mDatePickerDialogListener.getSelectedDate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    public static String getExtension(String filePath) {
        return filePath.substring(filePath.lastIndexOf(".") + 1);
    }

    public static String getFileNameFromPath(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    public static String getFilePath(String filePath) {
        return filePath.substring(0, filePath.lastIndexOf("/") + 1);
    }

    public static float spToPx(int sp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static String getDeviceResolution(Context mContext) {
        String resolution;
        int density = mContext.getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_LOW:
                resolution = "ldpi";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                resolution = "mdpi";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                resolution = "hdpi";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                resolution = "xhdpi";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
            case DisplayMetrics.DENSITY_420:
                resolution = "xxhdpi";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                resolution = "xxxhdpi";
                break;
            default:
                resolution = "xxhdpi";
                break;
        }
        return resolution;
    }

    public static SpannableString addTextToStringAtLast(String text, int wordSize, String addLastString, int addLastStringColor) {
        boolean isMultiWordText = false;
        int spaceCount = 0;
        int lastIndex = 0;
        String[] stringSplitted = new String[wordSize];//assuming the sentence has 100 words or less, you can change the value to Integer.MAX_VALUE instead of 10

        String finalString = "";
        int stringLength = 0;//this will give the character count in the string to be split

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {   //check whether the character is a space, if yes then count the words
                spaceCount++;// increment the count as you have encountered a word

            }
            if (spaceCount == wordSize) {
                isMultiWordText = true;
                //after encountering 10 words split the sentence from lastIndex to the 10th word. For the first time lastIndex would be zero that is starting position of the string
                stringSplitted[stringLength++] = text.substring(lastIndex, i);
                lastIndex = i;// to get the next part of the sentence, set the last index to 10th word
                spaceCount = 0;//set the number of spaces to zero to starting counting the next 10 words
                Log.e("addReadMoreTextToString", stringSplitted[0]);
            }
        }

        if (isMultiWordText)
            stringSplitted[stringLength++] = text.substring(lastIndex, text.length() - 1);//If the sentence has 14 words, only 10 words would be copied to stringSplitted array, this would copy rest of the 4 words into the string splitted array
        else
            stringSplitted[stringLength++] = text;

        for (int i = 0; i < stringSplitted.length; i++) {
            if (stringSplitted[i] != null) {

                if (isMultiWordText) {
                    finalString = stringSplitted[i] + addLastString;
                } else {
                    finalString = stringSplitted[i];
                }

                SpannableString modifiedText = new SpannableString(finalString);
                modifiedText.setSpan(new ForegroundColorSpan(addLastStringColor),
                        stringSplitted[i].length(), finalString.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                Log.e("addReadMoreTextToString", "" + stringSplitted[i]);//Print the splitted strings here
                return modifiedText;
            }
        }
        return null;
    }

    public static TextDrawable getTextDrawable(Context context, String name) {
        ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
        int color2 = mColorGenerator.getColor(name);
        return TextDrawable.builder()
                .beginConfig()
                .width(Math.round(context.getResources().getDimension(R.dimen.dp52))) // width in px
                .height(Math.round(context.getResources().getDimension(R.dimen.dp52))) // height in px
                .endConfig()
                .buildRound(("" + name.charAt(0)).toUpperCase(), color2);
    }

    //creation of marker on map
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels,
                displayMetrics.heightPixels);

        view.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        view.buildDrawingCache(false);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}

