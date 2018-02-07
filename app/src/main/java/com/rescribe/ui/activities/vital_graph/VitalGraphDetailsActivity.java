package com.rescribe.ui.activities.vital_graph;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.gson.Gson;
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;
import com.rescribe.R;
import com.rescribe.helpers.vital_graph_helper.VitalGraphHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.vital_graph.vital_all_list.VitalGraphData;
import com.rescribe.model.vital_graph.vital_description.VitalGraphDetails;
import com.rescribe.model.vital_graph.vital_description.VitalGraphInfoBaseModel;
import com.rescribe.model.vital_graph.vital_tracker.VitalGraphAddNewTrackerRequestModel;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VitalGraphDetailsActivity extends AppCompatActivity implements HelperResponse {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.weight)
    CustomTextView weightText;
    @BindView(R.id.date)
    TextView dateText;
    @BindView(R.id.description)
    CustomTextView mDescriptionText;
    @BindView(R.id.graphCard)
    LineChart mGraphCard;
    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.legendsDrawn)
    LinearLayout legendsDrawn;
    private VitalGraphData mClickedVitalGraphData;
    private VitalGraphHelper mVitalGraphHelper;
    private VitalGraphInfoBaseModel.VitalGraphInfoDataModel mReceivedVitalGraphDataModel;
    String mSelectedTrackerDateToSend;
    private boolean isVitalAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vital_graph_details);
        ButterKnife.bind(this);
        mClickedVitalGraphData = getIntent().getParcelableExtra(getString(R.string.vital_graph));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText(mClickedVitalGraphData.getVitalName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        init();
    }

    private void init() {
        mVitalGraphHelper = new VitalGraphHelper(this, this);
        mVitalGraphHelper.doGetPatientVitalDetail(mClickedVitalGraphData.getVitalName());
        weightText.setText(mClickedVitalGraphData.getVitalValue() + " " + mClickedVitalGraphData.getVitalUnit());
        if (mClickedVitalGraphData.getCategory().equalsIgnoreCase(getString(R.string.normalRange))) {
            weightText.setTextColor(ContextCompat.getColor(this, R.color.range_green));
        } else if (mClickedVitalGraphData.getCategory().equalsIgnoreCase(getString(R.string.moderateRange))) {
            weightText.setTextColor(ContextCompat.getColor(this, R.color.range_yellow));
        } else if (mClickedVitalGraphData.getCategory().equalsIgnoreCase(getString(R.string.severeRange))) {
            weightText.setTextColor(ContextCompat.getColor(this, R.color.Red));
        } else {
            weightText.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        if (mClickedVitalGraphData.getVitalDate() != null) {
            Date timeStamp = CommonMethods.convertStringToDate(mClickedVitalGraphData.getVitalDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
            Calendar cal = Calendar.getInstance();
            cal.setTime(timeStamp);
            String toDisplay = cal.get(Calendar.DAY_OF_MONTH) + "<sup>" + "" + CommonMethods.getSuffixForNumber(cal.get(Calendar.DAY_OF_MONTH)) + "</sup>" + " " + new SimpleDateFormat("MMM yy").format(cal.getTime());
            //------
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dateText.setText(Html.fromHtml(toDisplay, Html.FROM_HTML_MODE_LEGACY));
            } else {
                dateText.setText(Html.fromHtml(toDisplay));
            }
        }


    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        switch (mOldDataTag) {
            case RescribeConstants.TASK_GET_PATIENT_VITAL_DETAIL:
                // VitalGraphInfoBaseModel customResponse1 = test();
                VitalGraphInfoBaseModel customResponse1 = (VitalGraphInfoBaseModel) customResponse;

                if (customResponse1.getVitalGraphInfoDataModel() != null) {
                    mReceivedVitalGraphDataModel = customResponse1.getVitalGraphInfoDataModel();
                    // mDescriptionText.setText("" + mReceivedVitalGraphDataModel.getDescription());
                    // plotVitalGraph();
                    mGraphCard.invalidate();
                    if (mClickedVitalGraphData.getVitalName().equalsIgnoreCase("Blood Pressure")) {
                        plotVitalGraphUsingMpChartForBloodPressure();
                        legendsDrawn.setVisibility(View.GONE);

                    } else {
                        plotVitalGraphUsingMpChart();
                        legendsDrawn.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case RescribeConstants.TASK_ADD_VITAL_MANUALLY:
                CommonBaseModelContainer common = (CommonBaseModelContainer) customResponse;
                CommonMethods.showToast(this, "" + common.getCommonRespose().getStatusMessage());
                mVitalGraphHelper.doGetPatientVitalDetail(mClickedVitalGraphData.getVitalName());
                isVitalAdded = true;
                break;
        }
    }

    private VitalGraphInfoBaseModel test() {
        try {
            InputStream is = getAssets().open("vitals_graph.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Log.e("test", "doctor_data_22_nov_2017" + json);

            Gson gson = new Gson();
            VitalGraphInfoBaseModel bookAppointmentBaseModel = gson.fromJson(json, VitalGraphInfoBaseModel.class);
            return bookAppointmentBaseModel;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void plotVitalGraphUsingMpChart() {

        ArrayList<VitalGraphDetails> vitalGraphDetailList = mReceivedVitalGraphDataModel.getVitalGraphDetailList();
        //  ArrayList<VitalGraphDetails> vitalGraphDetailList = mReceivedVitalGraphDataModel.getVitalGraphDetailListBySize(5);
        if (vitalGraphDetailList.size() != 0) {

            // soring for ascending list
            //Collections.sort(vitalGraphDetailList, new DateWiseComparator());
            //------
            DateFormat requiredDateFormat = new SimpleDateFormat(RescribeConstants.DATE_PATTERN.DD_MMM, Locale.US);
            final ArrayList<Entry> tempEntries = new ArrayList<>();
            final HashSet<String> tempLabels = new HashSet<>();
            final ArrayList<String> tempLabelsArrayList = new ArrayList<>();
            //To add circle color
            List<Integer> circleColors = new ArrayList<>();

            for (int i = 0; i < vitalGraphDetailList.size(); i++) {

                VitalGraphDetails data = vitalGraphDetailList.get(i);

                circleColors.add(data.getSelfTrackerFlag() ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));

                //----
                Date date = CommonMethods.convertStringToDate(data.getCreationDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY);
                String formattedDate = requiredDateFormat.format(date);
                tempLabels.add(formattedDate);
                tempLabelsArrayList.add(formattedDate);
                //----
                if (!(RescribeConstants.BLANK.equalsIgnoreCase(data.getVitalValue()))) {
                    //To add text color
                    Bitmap bitmapView = createBitmapFromLayoutWithText(data.getVitalValue(), data.getSelfTrackerFlag() ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                    Drawable drawable = new BitmapDrawable(getResources(), bitmapView);

                    tempEntries.add(new Entry(i, Float.parseFloat(data.getVitalValue()), drawable));
//                    tempEntries.add(new Entry(i, Float.parseFloat(data.getVitalValue()), getResources().getDrawable( R.drawable.blue_dot)));
                }
            }

            if (tempEntries.size() > 0) {
                LineDataSet dataset = new LineDataSet(tempEntries, "");

                LineData data = new LineData();
                data.addDataSet(dataset);
                dataset.setDrawCircleHole(false);
                dataset.setColor(Color.WHITE);
                dataset.setValueTextColor(Color.TRANSPARENT);
//                dataset.setValueTextColors(circleColors);
//                dataset.setCircleColor(Color.TRANSPARENT);
                dataset.setCircleColors(circleColors);
                dataset.setValueTextSize(10); // It's a DP value.
                //----
                mGraphCard.getLegend().setEnabled(false);
                mGraphCard.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                mGraphCard.setData(data);
                mGraphCard.animateY(1000);
                //--------
                mGraphCard.getAxisLeft().setTextColor(Color.WHITE); // left y-axis
                mGraphCard.getXAxis().setTextColor(Color.WHITE);
                mGraphCard.getAxisRight().setTextColor(Color.TRANSPARENT);
                //--------
                mGraphCard.getAxisLeft().setGridColor(Color.TRANSPARENT);
                mGraphCard.getXAxis().setGridColor(Color.TRANSPARENT);
                mGraphCard.getXAxis().setAxisLineWidth(2.0f);
                mGraphCard.getAxisRight().setAxisLineWidth(2.0f);
                mGraphCard.getAxisLeft().setAxisLineWidth(2.0f);
                mGraphCard.getAxisLeft().setAxisLineColor(ContextCompat.getColor(this, R.color.graph_color));
                mGraphCard.getAxisRight().setAxisLineColor(ContextCompat.getColor(this, R.color.graph_color));
                mGraphCard.getXAxis().setAxisLineColor(ContextCompat.getColor(this, R.color.graph_color));
                mGraphCard.getAxisRight().setGridColor(Color.TRANSPARENT); // left y-axis
                //--------
                mGraphCard.getDescription().setEnabled(false);
                //---------
                mGraphCard.setScrollContainer(true);
                mGraphCard.setHorizontalScrollBarEnabled(true);
                mGraphCard.setScaleXEnabled(true);
                mGraphCard.setVisibleXRangeMaximum(5);
                mGraphCard.moveViewToX(0);
                //---------
                IAxisValueFormatter formatter = new IAxisValueFormatter() {

                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {

                        System.out.println(value);

                        if ((int) value == -1) {
                            return "";
                        } else {
                            if (((int) value) < tempLabelsArrayList.size()) {
                                return (tempLabelsArrayList.get((int) value));
                            } else {
                                return "";
                            }
                        }
                        //  return tempLabelsArrayList.get((int) value);
                    }
                };

                XAxis xAxis = mGraphCard.getXAxis();
                xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
                xAxis.setValueFormatter(formatter);

                //---------

            }
        }
    }


    private void plotVitalGraphUsingMpChartForBloodPressure() {

        ArrayList<VitalGraphDetails> vitalGraphDetailList = mReceivedVitalGraphDataModel.getVitalGraphDetailList();
        // ArrayList<VitalGraphDetails> vitalGraphDetailList = mReceivedVitalGraphDataModel.getVitalGraphDetailListBySize(5);
        if (vitalGraphDetailList.size() != 0) {

            // soring for ascending list
            //Collections.sort(vitalGraphDetailList, new DateWiseComparator());
            //------
            DateFormat requiredDateFormat = new SimpleDateFormat(RescribeConstants.DATE_PATTERN.DD_MMM, Locale.US);
            final ArrayList<Entry> maxTempEntries = new ArrayList<>();
            final ArrayList<Entry> minTempEntries = new ArrayList<>();
            final HashSet<String> tempLabels = new HashSet<>();
            final ArrayList<String> tempLabelsArrayList = new ArrayList<>();

            List<Integer> circleColorsSystolic = new ArrayList<>();
            List<Integer> circleColorsDiastolic = new ArrayList<>();


            for (int i = 0; i < vitalGraphDetailList.size(); i++) {

                VitalGraphDetails data = vitalGraphDetailList.get(i);

                circleColorsSystolic.add(data.getSelfTrackerFlag() ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                circleColorsDiastolic.add(data.getSelfTrackerFlag() ? getResources().getColor(R.color.black) : Color.YELLOW);

                //----
                Date date = CommonMethods.convertStringToDate(data.getCreationDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY);
                String formattedDate = requiredDateFormat.format(date);
                tempLabels.add(formattedDate);
                tempLabelsArrayList.add(formattedDate);
                //----
                String vitalValue = data.getVitalValue();
                vitalValue = vitalValue.replaceAll("[^0-9/.]", "");
                if (vitalValue.contains("/")) {
                    String[] split = vitalValue.split("/");
                    Bitmap bitmapViewSystolic = createBitmapFromLayoutWithText(split[0], data.getSelfTrackerFlag() ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                    Drawable drawableSystolic = new BitmapDrawable(getResources(), bitmapViewSystolic);
                    Bitmap bitmapViewDiasystolic = createBitmapFromLayoutWithText(split[1], data.getSelfTrackerFlag() ? getResources().getColor(R.color.black) : Color.YELLOW);
                    Drawable drawableDiasystolic = new BitmapDrawable(getResources(), bitmapViewDiasystolic);

                    maxTempEntries.add(new Entry(i, Float.parseFloat(split[0]), drawableSystolic));
                    minTempEntries.add(new Entry(i, Float.parseFloat(split[1]), drawableDiasystolic));
                }
            }

            if (maxTempEntries.size() > 0) {

                LineDataSet maxTempEntriesDataset = new LineDataSet(maxTempEntries, "SYSTOLIC");
                LineDataSet minTempEntriesDataset = new LineDataSet(minTempEntries, "DIASTOLIC");

                LineData data = new LineData();
                data.addDataSet(maxTempEntriesDataset);
                data.addDataSet(minTempEntriesDataset);
                data.setValueTextSize(10); // It's DP value.

                //----******for MAX/Systolic*****------
                maxTempEntriesDataset.setDrawCircleHole(false);
                maxTempEntriesDataset.setDrawFilled(true);
                maxTempEntriesDataset.setColor(Color.WHITE);
                maxTempEntriesDataset.setCircleColors(circleColorsSystolic);
                maxTempEntriesDataset.setFillColor(Color.WHITE);
                maxTempEntriesDataset.setFillAlpha(10);
                maxTempEntriesDataset.setValueTextColor(Color.TRANSPARENT);
                //----*******for MIN/Diastolic****------
                minTempEntriesDataset.setDrawCircleHole(false);
                minTempEntriesDataset.setDrawFilled(true);
                minTempEntriesDataset.setCircleColors(circleColorsDiastolic);
                minTempEntriesDataset.setFillColor(Color.YELLOW);
                minTempEntriesDataset.setFillAlpha(10);
                minTempEntriesDataset.setColor(Color.YELLOW);
                minTempEntriesDataset.setValueTextColor(Color.TRANSPARENT);
                //----
                mGraphCard.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                mGraphCard.setData(data);
                mGraphCard.animateY(1000);
                //--------
                mGraphCard.getAxisLeft().setTextColor(Color.WHITE); // left y-axis
                mGraphCard.getXAxis().setTextColor(Color.WHITE);
                mGraphCard.getAxisRight().setTextColor(Color.TRANSPARENT);
                //--------
                mGraphCard.getXAxis().setAxisLineWidth(2.0f);
                mGraphCard.getAxisRight().setAxisLineWidth(2.0f);
                mGraphCard.getAxisLeft().setAxisLineWidth(2.0f);
                mGraphCard.getAxisLeft().setAxisLineColor(ContextCompat.getColor(this, R.color.graph_color));
                mGraphCard.getAxisRight().setAxisLineColor(ContextCompat.getColor(this, R.color.graph_color));
                mGraphCard.getXAxis().setAxisLineColor(ContextCompat.getColor(this, R.color.graph_color));
                mGraphCard.getAxisLeft().setGridColor(Color.TRANSPARENT);
                mGraphCard.getAxisRight().setGridColor(Color.TRANSPARENT);
                mGraphCard.getXAxis().setGridColor(Color.TRANSPARENT);
                //--------
                mGraphCard.getDescription().setEnabled(false);
                //---------
                mGraphCard.setScrollContainer(true);
                mGraphCard.setHorizontalScrollBarEnabled(true);
                mGraphCard.setScaleXEnabled(true);
                mGraphCard.setVisibleXRangeMaximum(4);
                mGraphCard.moveViewToX(0);
                //---------
                IAxisValueFormatter formatter = new IAxisValueFormatter() {

                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        System.out.println(value);
                        if ((int) value == -1) {
                            return "";
                        } else {
                            if (((int) value) < tempLabelsArrayList.size()) {
                                return (tempLabelsArrayList.get((int) value));
                            } else {
                                return "";
                            }
                        }
                        //  return tempLabelsArrayList.get((int) value);
                    }
                };

                XAxis xAxis = mGraphCard.getXAxis();
                xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
                xAxis.setValueFormatter(formatter);
                //---------

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

    }

    //-- Sort date in ascending order
    private class DateWiseComparator implements Comparator<VitalGraphDetails> {

        public int compare(VitalGraphDetails m1, VitalGraphDetails m2) {

            //possibly check for nulls to avoid NullPointerException
            //  String s = CommonMethods.formatDateTime(m1, RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE);
            Date m1Date = CommonMethods.convertStringToDate(m1.getCreationDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY);
            Date m2Date = CommonMethods.convertStringToDate(m2.getCreationDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY);
            int i = m1Date.compareTo(m2Date);
            return i;
        }
    }


    @OnClick(R.id.addNewTracker)
    /**
     *  Add Tracker copied from addTrackerActivity.java
     *  In chang over here, also replace with addTrackerActivity.Java
     */
    public void openAddTrackerDialog() {
        final View modalbottomsheet = getLayoutInflater().inflate(R.layout.add_new_tracker_dialog, null);
        final CustomTextView addTrackerDate = (CustomTextView) modalbottomsheet.findViewById(R.id.addTrackerDate);
        CustomTextView header = (CustomTextView) modalbottomsheet.findViewById(R.id.header);
        header.setText(mClickedVitalGraphData.getVitalName());
        final EditText reading = (EditText) modalbottomsheet.findViewById(R.id.reading);

        //------
        final LinearLayout bloodPressureReadingLayout = (LinearLayout) modalbottomsheet.findViewById(R.id.bloodPressureReadingLayout);
        final EditText systolic = (EditText) modalbottomsheet.findViewById(R.id.systolic);
        final EditText dystolic = (EditText) modalbottomsheet.findViewById(R.id.dystolic);
        //------
        Button addTrackerButton = (Button) modalbottomsheet.findViewById(R.id.addTrackerButton);
        //---------
        addTrackerDate.setHint(CommonMethods.getCurrentDateWithSlash());

        //-------------
        if (mClickedVitalGraphData.getVitalName().equalsIgnoreCase("Blood Pressure")) {
            bloodPressureReadingLayout.setVisibility(View.VISIBLE);
            dystolic.setHint(RescribeConstants.DIASTOLIC_HINT);
            systolic.setHint(RescribeConstants.SYSTOLIC_HINT);
            reading.setVisibility(View.GONE);
        } else {
            bloodPressureReadingLayout.setVisibility(View.GONE);
            reading.setHint(RescribeConstants.OTHER_VITALS_HINT);
            reading.setVisibility(View.VISIBLE);
        }

        //-------------

        final BottomSheetDialog dialog = new BottomSheetDialog(this);

        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        dialog.show();

        addTrackerDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
                                addTrackerDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                mSelectedTrackerDateToSend = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setAccentColor(getResources().getColor(R.color.tagColor));
                datePickerDialog.show(getSupportFragmentManager(), getResources().getString(R.string.enter_reading));
            }
        });

        addTrackerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentDate = new Date();
                String date = addTrackerDate.getText().toString();
                String readingData = reading.getText().toString();
                String systolicData = systolic.getText().toString();
                String dystolicData = dystolic.getText().toString();
                boolean isError = false;
                if (date.trim().length() == 0) {
                    CommonMethods.showToast(VitalGraphDetailsActivity.this, getString(R.string.enter_date));
                    isError = true;
                } else if (date.trim().length() != 0) {
                    Date enteredDate = CommonMethods.convertStringToDate(mSelectedTrackerDateToSend, RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
                    if (enteredDate.getTime() > currentDate.getTime()) {
                        isError = true;
                        CommonMethods.showToast(VitalGraphDetailsActivity.this, getString(R.string.err_reading_date));
                    }
                } else if (readingData.trim().length() == 0 && reading.getVisibility() == View.VISIBLE) {
                    isError = true;
                    CommonMethods.showToast(VitalGraphDetailsActivity.this, getString(R.string.enter_reading));
                } else if (bloodPressureReadingLayout.getVisibility() == View.VISIBLE && systolicData.trim().length() == 0) {
                    isError = true;
                    CommonMethods.showToast(VitalGraphDetailsActivity.this, getString(R.string.enter_systolic));
                } else if (bloodPressureReadingLayout.getVisibility() == View.VISIBLE && dystolicData.trim().length() == 0) {
                    isError = true;
                    CommonMethods.showToast(VitalGraphDetailsActivity.this, getString(R.string.enter_diastolic));
                }

                if (!isError) {
                    VitalGraphAddNewTrackerRequestModel model = new VitalGraphAddNewTrackerRequestModel();

                    //-----
                    DateFormat writeFormat = new SimpleDateFormat(RescribeConstants.DATE_PATTERN.HH_mm_ss);
                    model.setCheckDate(mSelectedTrackerDateToSend + " " + writeFormat.format(new Date()));
                    //-----
                    model.setVitalName(mClickedVitalGraphData.getVitalName());
                    if (bloodPressureReadingLayout.getVisibility() == View.VISIBLE) {
                        model.setVitalValue(systolic.getText().toString() + "/" + dystolic.getText().toString());
                    } else {
                        model.setVitalValue(reading.getText().toString());
                    }
                    mVitalGraphHelper.doAddNewVitalGraphTracker(model);
                    dialog.dismiss();
                }
            }
        });

    }

    public Bitmap createBitmapFromLayoutWithText(String text, int color) {
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = mInflater.inflate(R.layout.custom_point, null);
        TextView tv = (TextView) view.findViewById(R.id.valueTextView);
        tv.setTextColor(color);
        tv.setText(text);

        //Pre-measure the view so that height and width don't remain null.
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        //--------------
        int dpPerChar = getResources().getDimensionPixelSize(R.dimen.dp24);
        int bitmapSize = getResources().getDimensionPixelSize(R.dimen.dp34);
        if (text.length() > 4) {
            dpPerChar = CommonMethods.convertDpToPixel(text.length() * 6); // Assumed each char takes 6dp
            bitmapSize = CommonMethods.convertDpToPixel((text.length() * 6) + 6);// Assumed each char takes 6dp
        }
        //--------------

        //Assign a size and position to the view and all of its descendants
        view.layout(0, 0, dpPerChar, dpPerChar);

        //Create the bitmap
        Bitmap bitmap = Bitmap.createBitmap(bitmapSize,
                bitmapSize,
                Bitmap.Config.ARGB_8888);
        //Create a canvas with the specified bitmap to draw into
        Canvas c = new Canvas(bitmap);

        //Render this view (and all of its children) to the given Canvas
        view.draw(c);
        return bitmap;
    }

    @Override
    public void onBackPressed() {
        if (isVitalAdded) {
            Intent intent = new Intent(this, VitalGraphActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
