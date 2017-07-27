package com.myrescribe.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myrescribe.R;
import com.myrescribe.interfaces.CheckIpConnection;
import com.myrescribe.interfaces.DatePickerDialogListener;
import com.myrescribe.model.util.TimePeriod;
import com.myrescribe.ui.activities.ShowMedicineDoseListActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CommonMethods {

    private static final String TAG = "MyRescribe/CommonMethods";
    private static boolean encryptionIsOn = true;
    private static String aBuffer = "";
    private static CheckIpConnection mCheckIpConnection;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private DatePickerDialogListener mDatePickerDialogListener;

    public static void showToast(Context context, String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
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
        if (emailId == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches();
        }
    }

    /**
     * Returns Message for Error codes
     *
     * @param messageCode
     * @return
     */
    public static String getResponseCodeMessage(String messageCode) {
        String strMessage = "";
        try {
            if (messageCode.length() >= 3) {
                if (messageCode.equalsIgnoreCase("900")) {
                    strMessage = "Registration Error";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strMessage;

    }

    public static void hideKeyboard(Activity cntx) {
        // Check if no view has focus:
        View view = cntx.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) cntx.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    //create the new folder in sd card & write the data in text file which is created in that folder.
    public static void MyRescribeLogWriteFile(String title, String text, boolean textAppend) {
        try {
            byte[] keyBytes = getKey("password");
            File directory = new File(Environment.getExternalStorageDirectory().getPath() + "/", MyRescribeConstants.MYRESCRIBE_LOG_FOLDER);
            if (!directory.exists()) {
                directory.mkdir();
            }
            //make a new text file in that created new directory/folder
            File file = new File(directory.getPath(), MyRescribeConstants.MYRESCRIBE_LOG_FILE);

            if (!file.exists() && directory.exists()) {
                file.createNewFile();
            }
//            OutputStreamWriter osw;
//            osw = new FileWriter(file, textAppend);
//
//            BufferedWriter out = new BufferedWriter(osw);
//            out.write("************" + getCurrentDateTime() + "************" + title + ": " + text + "\n");
//            out.close();

            OutputStreamWriter osw;
            if (encryptionIsOn) {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
                IvParameterSpec ivParameterSpec = new IvParameterSpec(keyBytes);
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

                FileOutputStream fos = new FileOutputStream(file, textAppend);
                CipherOutputStream cos = new CipherOutputStream(fos, cipher);
                osw = new OutputStreamWriter(cos, "UTF-8");
            } else    // not encryptionIsOn
                osw = new FileWriter(file, textAppend);

            BufferedWriter out = new BufferedWriter(osw);
            out.write("************" + getCurrentDateTime() + "************" + title + ": " + text + "\n");
            out.close();


        } catch (Exception e) {
            System.out.println("Encryption Exception " + e);
        }
    }

    private static byte[] getKey(String password) {
        String key = "";
        while (key.length() < 16)
            key += password;
        return key.substring(0, 16).getBytes();
    }

    // read the whole file data with previous data also
    public static String MyRescribeLogReadFile() {

        try {
            byte[] keyBytes = getKey("password");

            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/", MyRescribeConstants.MYRESCRIBE_LOG_FOLDER + "/" + MyRescribeConstants.MYRESCRIBE_LOG_FILE);
            InputStreamReader isr;
            if (encryptionIsOn) {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
                IvParameterSpec ivParameterSpec = new IvParameterSpec(keyBytes);
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

                FileInputStream fis = new FileInputStream(file);
                CipherInputStream cis = new CipherInputStream(fis, cipher);
                isr = new InputStreamReader(cis, "UTF-8");
            } else
                isr = new FileReader(file);

            BufferedReader in = new BufferedReader(isr);
            //	    		String line = in.readLine();
            StringBuffer s = new StringBuffer();
            int cr = 0;
            while ((cr = in.read()) != -1) {
                s.append((char) cr);
            }
            aBuffer = s.toString();
            CommonMethods.Log(TAG, "Text read: " + aBuffer);
            in.close();
            return aBuffer;
        } catch (Exception e) {
            System.out.println("Decryption Exception " + e);
        }
        return aBuffer;
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

    public static void showSnack(Context mContext, View mViewById, String msg) {
        Snackbar snack = Snackbar.make(mViewById, msg, Snackbar.LENGTH_SHORT);
        ViewGroup group = (ViewGroup) snack.getView();
        group.setBackgroundColor(mContext.getResources().getColor(R.color.errorColor));
        snack.show();
    }

    public static String splitToComponentTimes(int seconds) {
        int hr = seconds / 3600;
        int rem = seconds % 3600;
        int mn = rem / 60;
        int sec = rem % 60;
        String hrStr = (hr < 10 ? "0" : "") + hr;
        String mnStr = (mn < 10 ? "0" : "") + mn;
        String secStr = (sec < 10 ? "0" : "") + sec;
        return hrStr + ":" + mnStr + ":" + secStr;
    }

    public static String getHoursFromSeconds(int seconds) {
        int hr = seconds / 3600;
        int rem = seconds % 3600;
        int mn = rem / 60;
        int sec = rem % 60;
        String hrStr = (hr < 10 ? "0" : "") + hr;
        String mnStr = (mn < 10 ? "0" : "") + mn;
        String secStr = (sec < 10 ? "0" : "") + sec;
        float hour = (float) Integer.parseInt(hrStr);
        float minuite = (float) Integer.parseInt(mnStr);
        if (minuite > 0) {
            minuite = minuite / 60;
        }

        float finalvalue = hour + minuite;
        return "" + new DecimalFormat("##.##").format(finalvalue);
    }

    public static String getCurrentTimeStamp(String expectedFormat) {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat(expectedFormat, Locale.US);
            String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static int getTimeStampDifference(String startTime, String endTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        Date secondParsedDate = null;
        Date firstParsedDate = null;
        long diff = 0;
        int mDiff = 0;
        try {
            firstParsedDate = dateFormat.parse(startTime);
            secondParsedDate = dateFormat.parse(endTime);
            diff = secondParsedDate.getTime() - firstParsedDate.getTime();
            mDiff = (int) (diff / 1000) % 60;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mDiff;
    }

    public static ArrayList<TimePeriod> getMonthsWithYear(String startDate, String endDate, String dateFormat) {
        ArrayList<String> monthsWithYear = new ArrayList<>();
        try {
            Calendar startCal = Calendar.getInstance();
            Calendar endCal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
            startCal.setTime(sdf.parse(startDate));
            endCal.setTime(sdf.parse(endDate));
            while (startCal.getTimeInMillis() <= endCal.getTimeInMillis()) {
                monthsWithYear.add(String.valueOf(android.text.format.DateFormat.format("MMM-yyyy", startCal)));
                startCal.add(Calendar.MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayList<TimePeriod> timePeriods = new ArrayList<>();
        for (String data :
                monthsWithYear) {
            String[] splitValues = data.split("-");
            TimePeriod timePeriod = new TimePeriod();
            timePeriod.setMonthName(splitValues[0]);
            timePeriod.setYear(splitValues[1]);
            timePeriods.add(timePeriod);
        }
        return timePeriods;
    }

    public static void dateDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("difference : " + different);

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

    public static String getDayFromDate(String dateFormat, String date) {

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
            return "Today";
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


    public static View loadView(int resourceName, Context mActivity) {

        LayoutInflater inflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // param.gravity = Gravity.CENTER;
        View child = inflater.inflate(resourceName, null);
        LinearLayout l1 = new LinearLayout(mActivity);
        child.setLayoutParams(param);

        l1.setLayoutParams(param);
        l1.addView(child);
        return l1;
    }


    public static String getFormatedDate(String strDate, String sourceFormate,
                                         String destinyFormate) {
        SimpleDateFormat df;
        df = new SimpleDateFormat(sourceFormate, Locale.US);
        Date date = null;
        try {
            date = df.parse(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        df = new SimpleDateFormat(destinyFormate, Locale.US);
        return df.format(date);
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


        if (formatString.equalsIgnoreCase(MyRescribeConstants.TIME)) {
            // SimpleDateFormat ft = new SimpleDateFormat(MyRescribeConstants.DATE_PATTERN.HH_MM, Locale.US);
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

        else if (formatString.equalsIgnoreCase(MyRescribeConstants.DATE)) {
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

    private static boolean isValidIP(String ipAddr) {

        Pattern ptn = Pattern.compile("(\\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])\\b)\\.(\\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])\\b)\\.(\\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])\\b)\\.(\\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])\\b)\\:(\\d{1,4})$");
        Matcher mtch = ptn.matcher(ipAddr);
        return mtch.find();
    }

    public static File getCacheFile(Context context, String base64Pdf, String filename, String extension) {
        // Create a file in the Internal Storage

        byte[] pdfAsBytes = Base64.decode(base64Pdf, 0);

        File file = null;
        FileOutputStream outputStream;
        try {

            file = new File(context.getCacheDir(), filename + "." + extension);

            outputStream = new FileOutputStream(file);
            outputStream.write(pdfAsBytes);
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File storeAndGetDocument(Context context, String base64Pdf, String filename, String extension) {
        // Create a file in the Internal Storage

        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = null;

        byte[] pdfAsBytes = Base64.decode(base64Pdf, 0);

        FileOutputStream outputStream;
        try {

            file = new File(filepath + "/Android/data/" + context.getPackageName() + "/Documents");
            if (!file.exists()) {
                file.mkdirs();
            }

            file = new File(file.getAbsolutePath(), filename + "." + extension);

            outputStream = new FileOutputStream(file);
            outputStream.write(pdfAsBytes);
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void showDialog(String msg, final Context mContext) {


        final Dialog dialog = new Dialog(mContext);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ok_cancel);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        ((TextView) dialog.findViewById(R.id.textview_sucess)).setText(msg);

        dialog.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((Activity) mContext).finish();
                mContext.startActivity(new Intent(mContext, ShowMedicineDoseListActivity.class));

            }
        });

        dialog.show();
    }

    public static Dialog showAlertDialog(Context activity, String dialogHeader, CheckIpConnection checkIpConnection) {
        final Context mContext = activity;
        mCheckIpConnection = checkIpConnection;
        final Dialog dialog = new Dialog(activity);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_dialog_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        if (dialogHeader != null)
            ((TextView) dialog.findViewById(R.id.textView_dialog_heading)).setText(dialogHeader);

        dialog.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etServerPath = (EditText) dialog.findViewById(R.id.et_server_path);

                if (isValidIP(etServerPath.getText().toString())) {
                    String mServerPath = Config.HTTP + etServerPath.getText().toString() + Config.API;
                    Log.e(TAG, "SERVER PATH===" + mServerPath);
                    mCheckIpConnection.onOkButtonClickListner(mServerPath, mContext, dialog);
                } else {
                    Toast.makeText(mContext, R.string.error_in_ip, Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((Activity) mContext).finish();
            }
        });
        dialog.show();

        return dialog;
    }

    public static Dialog showVitalDialog(Context context, String unit, String unitValue, int color, String normalRange, Integer drawable) {
        final Context mContext = context;
        final Dialog dialog = new Dialog(context);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.vitals_dialog_layout);
        dialog.setCancelable(true);

        ((TextView) dialog.findViewById(R.id.vitalNameDialog)).setText(unit);
        ((TextView) dialog.findViewById(R.id.noOfVitalsDialog)).setText(unitValue);
        ((TextView) dialog.findViewById(R.id.normalRange)).setText(normalRange);
        ((TextView) dialog.findViewById(R.id.noOfVitalsDialog)).setTextColor(color);
        ((ImageView) dialog.findViewById(R.id.vitalImageDialog)).setImageResource(drawable);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        lp.gravity = Gravity.CENTER;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();

        return dialog;
    }

    public static String getMealTime(int hour, int mint, Context context) {
        //BB : 7-11,lunch : 11-3,dinner :7-11
        String time = "";
        if (hour > 7 && hour < 11) {
            time = context.getString(R.string.break_fast);
        } else if (hour >= 11 && hour < 15) {
            time = context.getString(R.string.mlunch);
        } else if (hour >= 15 && hour <= 17) {
            time = context.getString(R.string.msnacks);
        } else if (hour >= 17 && hour <= 24) {
            time = context.getString(R.string.mdinner);
        }
        CommonMethods.Log(TAG, "hour" + hour);
        CommonMethods.Log(TAG, "getMealTime" + time);
        return time;
    }

    public static Drawable getMedicalTypeIcon(String medicineTypeName, Context context) {

        Drawable abbreviation = ContextCompat.getDrawable(context, R.mipmap.highlight);
        if (medicineTypeName.equalsIgnoreCase("syrup")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.syrup_01);
        } else if (medicineTypeName.equalsIgnoreCase("Tablet")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.tablet_02);
        } else if (medicineTypeName.equalsIgnoreCase("Capsule")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.capsule_01);
        } else if (medicineTypeName.equalsIgnoreCase("injection")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.injection_01);
        } else if (medicineTypeName.equalsIgnoreCase("insulin")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.insulin_01);
        } else if (medicineTypeName.equalsIgnoreCase("Inhaler")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.inhaler_01);
        } else if (medicineTypeName.equalsIgnoreCase("liquid")) {
            abbreviation = ContextCompat.getDrawable(context, R.mipmap.highlight); // not found
        } else if (medicineTypeName.equalsIgnoreCase("tan")) {
            abbreviation = ContextCompat.getDrawable(context, R.mipmap.highlight);// not found
        } else if (medicineTypeName.equalsIgnoreCase("cream")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.cream_01);
        } else if (medicineTypeName.equalsIgnoreCase("jelly")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.jelly_02);
        } else if (medicineTypeName.equalsIgnoreCase("local application")) {
            abbreviation = ContextCompat.getDrawable(context, R.mipmap.highlight);// not found
        } else if (medicineTypeName.equalsIgnoreCase("ointment")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.ointment_01);
        } else if (medicineTypeName.equalsIgnoreCase("lotion")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.lotion_01);
        } else if (medicineTypeName.equalsIgnoreCase("drops")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.drop_01);
        } else if (medicineTypeName.equalsIgnoreCase("eye drops")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.eye_drops_02);
        } else if (medicineTypeName.equalsIgnoreCase("nasal drops")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.nasal_drop_01_01);
        } else if (medicineTypeName.equalsIgnoreCase("nasal spray")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.nasal_spray_01);
        } else if (medicineTypeName.equalsIgnoreCase("ointment/powder")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.ointment_powder_01);
        } else if (medicineTypeName.equalsIgnoreCase("respules")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.respules_01);
        } else if (medicineTypeName.equalsIgnoreCase("rotacaps")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.rotacaps_01);
        } else if (medicineTypeName.equalsIgnoreCase("sachet")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.sachet_01);
        }
        return abbreviation;
    }

    // Return medicine Icon's

    public static Drawable getMedicineTypeImage(String medicineTypeName, Context context) {

        Drawable abbreviation = ContextCompat.getDrawable(context, R.mipmap.highlight);
        if (medicineTypeName.equalsIgnoreCase("syrup")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.syrup);
        } else if (medicineTypeName.equalsIgnoreCase("Tablet")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.tablet);
        } else if (medicineTypeName.equalsIgnoreCase("Capsule")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.capsule);
        } else if (medicineTypeName.equalsIgnoreCase("injection")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.injection);
        } else if (medicineTypeName.equalsIgnoreCase("insulin")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.insulin);
        } else if (medicineTypeName.equalsIgnoreCase("Inhaler")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.inhaler);
        } else if (medicineTypeName.equalsIgnoreCase("liquid")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.liquid); // not found
        } else if (medicineTypeName.equalsIgnoreCase("tan")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.tan);// not found
        } else if (medicineTypeName.equalsIgnoreCase("cream")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.cream);
        } else if (medicineTypeName.equalsIgnoreCase("jelly")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.jelly);
        } else if (medicineTypeName.equalsIgnoreCase("local application")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.tablet);// not found
        } else if (medicineTypeName.equalsIgnoreCase("ointment")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.ointment);
        } else if (medicineTypeName.equalsIgnoreCase("lotion")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.lotion);
        } else if (medicineTypeName.equalsIgnoreCase("drops")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.drop);
        } else if (medicineTypeName.equalsIgnoreCase("eye drops")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.eye_drops);
        } else if (medicineTypeName.equalsIgnoreCase("nasal drops")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.nasal_drop);
        } else if (medicineTypeName.equalsIgnoreCase("nasal spray")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.nasal_spray);
        } else if (medicineTypeName.equalsIgnoreCase("ointment/powder")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.ointment_powder);
        } else if (medicineTypeName.equalsIgnoreCase("respules")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.respules);
        } else if (medicineTypeName.equalsIgnoreCase("rotacaps")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.rotocaps);
        } else if (medicineTypeName.equalsIgnoreCase("sachet")) {
            abbreviation = ContextCompat.getDrawable(context, R.drawable.sachet);
        }
        return abbreviation;
    }

    public static int getVisitDetailsIcons(String visitDetailName, Context context) {

        // Drawable abbreviation = ContextCompat.getDrawable(context, R.drawable.ellipse_2);
        int abbreviation = R.drawable.ellipse_2;
        if (visitDetailName.equalsIgnoreCase("complaints")) {
            abbreviation = R.drawable.complaints;
        } else if (visitDetailName.equalsIgnoreCase("vitals")) {
            abbreviation = R.drawable.vitals;
        } else if (visitDetailName.equalsIgnoreCase("remarks")) {
            abbreviation = R.drawable.remarks;
        } else if (visitDetailName.equalsIgnoreCase("diagnosis")) {
            abbreviation = R.drawable.diagnosis;
        } else if (visitDetailName.equalsIgnoreCase("prescription")) {
            abbreviation = R.drawable.prescription;
        } else if (visitDetailName.equalsIgnoreCase("investigations")) {
            abbreviation = R.drawable.investigations;
        } else if (visitDetailName.equalsIgnoreCase("advice")) {
            abbreviation = R.drawable.advice; // not found
        }
        return abbreviation;
    }


    public static int getVisitDetailsIconsAsPerID(int visitDetailId, Context context) {

        // Drawable abbreviation = ContextCompat.getDrawable(context, R.drawable.ellipse_2);
        int abbreviation = R.drawable.case_paper;
        if (visitDetailId == 11) {
            abbreviation = R.drawable.treatment_plan;
        } else if (visitDetailId == 13) {
            abbreviation = R.drawable.surgery;
        } else if (visitDetailId == 15) {
            abbreviation = R.drawable.vaccination;
        } else if (visitDetailId == 17) {
            abbreviation = R.drawable.general_precautions;
        } else if (visitDetailId == 12) {
            abbreviation = R.drawable.pre_operative_precautions;
        } else if (visitDetailId == 14) {
            abbreviation = R.drawable.post_operative_care;
        } else if (visitDetailId == 10) {
            abbreviation = R.drawable.pain_score; // not found
        } else if (visitDetailId == 16) {
            abbreviation = R.drawable.exercise; // not found
        } else if (visitDetailId == 9) {
            abbreviation = R.drawable.case_paper; // not found
        } else if (visitDetailId == 3) {
            abbreviation = R.drawable.vitals; // not found
        } else if (visitDetailId == 7) {
            abbreviation = R.drawable.investigations; // not found
        } else if (visitDetailId == 1) {
            abbreviation = R.drawable.complaints; // not found
        } else if (visitDetailId == 5) {
            abbreviation = R.drawable.prescription; // not found
        } else if (visitDetailId == 9) {
            abbreviation = R.drawable.advice; // not found
        } else if (visitDetailId == 4) {
            abbreviation = R.drawable.diagnosis; // not found
        } else if (visitDetailId == 8) {
            abbreviation = R.drawable.finding; // not found
        } else if (visitDetailId == 6) {
            abbreviation = R.drawable.remarks; // not found
        } else if (visitDetailId == 2) {
            abbreviation = R.drawable.allergy; // not found
        }
        return abbreviation;
    }

    public static int getVitalsDetails(String vitalDetailName, Context context) {

        // Drawable abbreviation = ContextCompat.getDrawable(context, R.drawable.ellipse_2);
        int abbreviation = R.drawable.ellipse_2;
        if (vitalDetailName.equalsIgnoreCase("BP Max")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("BP Min")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("Weight")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("Height")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("BMI")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("Total")) {
            abbreviation = R.drawable.vitals;
        } else if (vitalDetailName.equalsIgnoreCase("Cholesterol")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("HDL")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("LDL")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("Triglycerides")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("HDL Cholesterol")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("LDL Cholesterol")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("GFR")) {
            abbreviation = R.drawable.remarks;
        } else if (vitalDetailName.equalsIgnoreCase("BUN")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("Sr. Creatinine")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("Respiratory Rate")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("Heart Rate")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("Temperature")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("Fasting Blood Sugar")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("PP Blood Sugar")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("Oxygen Saturation")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("Platelet Count")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("ESR")) {
            abbreviation = R.drawable.complaints;
        } else if (vitalDetailName.equalsIgnoreCase("Hb")) {
            abbreviation = R.drawable.complaints;
        }
        return abbreviation;
    }


    public static Date convertStringToDate(String dateString, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat,Locale.US);
        Date date = null;

        try {
            date = formatter.parse(dateString.trim());
        } catch (ParseException e) {
            e.printStackTrace();
            CommonMethods.Log("convertStringToDate", "convertStringToDate EXCEPTION OCCURS : " + e.getMessage());
        }
        return date;
    }

    public static String getDateSelectedDoctorVisit(String visitdate, String dateFormat) {
        String yourDate = null;

        DateFormat format = new SimpleDateFormat(dateFormat,Locale.US);

        try {
            Date date = format.parse(visitdate);
            format = new SimpleDateFormat("d'th' MMM, yyyy",Locale.US);
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

    public void datePickerDialog(Context context, DatePickerDialogListener datePickerDialogListener, Date dateToSet, final Boolean isFromDateClicked, final Date date) {
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

                        if (isFromDateClicked) {
                            mDatePickerDialogListener.getSelectedDate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        } else {
                            mDatePickerDialogListener.getSelectedDate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }


                    }
                }, mYear, mMonth, mDay);
        if (isFromDateClicked) {
            datePickerDialog.getDatePicker().setCalendarViewShown(false);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        } else {
            if (date != null) {
                datePickerDialog.getDatePicker().setCalendarViewShown(false);
                datePickerDialog.getDatePicker().setMinDate(date.getTime());
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            } else {
                datePickerDialog.getDatePicker().setCalendarViewShown(false);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        }
    }


    public static String readJsonFile(Context mContext, String fileName) {
        try {
            InputStream is = mContext.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "{}";
    }
}

