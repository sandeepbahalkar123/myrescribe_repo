package com.rescribe.ui.fragments.book_appointment;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.rescribe.R;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DrawerForFilterDoctorBookAppointment extends Fragment {

    @BindView(R.id.applyButton)
    Button applyButton;
    @BindView(R.id.titleTextView)
    CustomTextView titleTextView;
    @BindView(R.id.resetButton)
    Button resetButton;

    Unbinder unbinder;
    @BindView(R.id.genderHeaderView)
    LinearLayout mGenderHeaderView;
    @BindView(R.id.genderContentView)
    LinearLayout mGenderContentView;
    @BindView(R.id.clinicFeesHeaderView)
    LinearLayout mClinicFeesHeaderView;

    @BindView(R.id.clinicFeesContentView)
    LinearLayout mClinicFeesContentView;
    @BindView(R.id.availabilityHeaderView)
    LinearLayout mAvailabilityHeaderView;
    @BindView(R.id.availabilityContentView)
    LinearLayout mAvailabilityContentView;
    @BindView(R.id.locationHeaderView)
    LinearLayout mLocationHeaderView;
    @BindView(R.id.locationContentRecycleView)
    RecyclerView mLocationContentRecycleView;
    @BindView(R.id.locationContentView)
    LinearLayout mLocationContentView;
    @BindView(R.id.genderMaleIcon)
    ImageView mGenderMaleIcon;
    @BindView(R.id.genderMaleText)
    CustomTextView mGenderMaleText;
    @BindView(R.id.genderMaleLayout)
    LinearLayout mGenderMaleLayout;
    @BindView(R.id.genderFemaleIcon)
    ImageView mGenderFemaleIcon;
    @BindView(R.id.genderFemaleText)
    CustomTextView mGenderFemaleText;
    @BindView(R.id.genderFemaleLayout)
    LinearLayout mGenderFemaleLayout;
    @BindView(R.id.distanceHeaderView)
    LinearLayout distanceHeaderView;
    @BindView(R.id.distanceSeekBar)
    SeekBar distanceSeekBar;
    @BindView(R.id.distanceSeekBarValueIndicator)
    CustomTextView mDistanceSeekBarValueIndicator;
    @BindView(R.id.distanceContentView)
    LinearLayout distanceContentView;
    @BindView(R.id.availSunday)
    CustomTextView mAvailSunday;
    @BindView(R.id.availMonday)
    CustomTextView mAvailMonday;
    @BindView(R.id.availTues)
    CustomTextView mAvailTues;
    @BindView(R.id.availWed)
    CustomTextView mAvailWed;
    @BindView(R.id.availThurs)
    CustomTextView mAvailThurs;
    @BindView(R.id.availFri)
    CustomTextView mAvailFri;
    @BindView(R.id.availSat)
    CustomTextView mAvailSat;

    //-----
    @BindView(R.id.clinicFeesSeekBar)
    CrystalRangeSeekbar mClinicFeesSeekBar;

    private OnDrawerInteractionListener mListener;

    //--------
    private String mSelectedGender;
    private HashMap<String, Boolean> mSelectedDays;
    //--------
    View mLeftThumbView, mRightThumbView;
    //--------

    public DrawerForFilterDoctorBookAppointment() {
        // Required empty public constructor
    }

    public static DrawerForFilterDoctorBookAppointment newInstance() {
        DrawerForFilterDoctorBookAppointment fragment = new DrawerForFilterDoctorBookAppointment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.book_appointment_drawer_filter, container, false);
        unbinder = ButterKnife.bind(this, view);
        initialize();
        return view;
    }

    private void initialize() {
        configureClinicFeesSeekBar();
        mSelectedDays = new HashMap<>();
        //---------
        mSelectedDays.put(getString(R.string.weekday_sun), false);
        mSelectedDays.put(getString(R.string.weekday_mon), false);
        mSelectedDays.put(getString(R.string.weekday_tues), false);
        mSelectedDays.put(getString(R.string.weekday_wed), false);
        mSelectedDays.put(getString(R.string.weekday_thurs), false);
        mSelectedDays.put(getString(R.string.weekday_fri), false);
        mSelectedDays.put(getString(R.string.weekday_sat), false);
        //---------
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDrawerInteractionListener) {
            mListener = (OnDrawerInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDrawerInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void configureClinicFeesSeekBar() {

        //---------- Clinic Seek Bar : START ----------
        mLeftThumbView = LayoutInflater.from(getActivity()).inflate(R.layout.seekbar_progress_thumb_layout, null, false);
        mRightThumbView = LayoutInflater.from(getActivity()).inflate(R.layout.seekbar_progress_thumb_layout, null, false);
        mLeftThumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mRightThumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        //----- Left thumb view------
        Bitmap leftBitmap = null;
        if (mLeftThumbView != null) {
            mLeftThumbView.setDrawingCacheEnabled(true);
            setThumbValue(mLeftThumbView, "" + 1);
        }
        //-----------
        // ----- Right thumb view------
        Bitmap rightBitmap = null;
        if (mRightThumbView != null) {
            mRightThumbView.setDrawingCacheEnabled(true);
            setThumbValue(mRightThumbView, "" + 250);
        }
        //-----------

        // set properties
        mClinicFeesSeekBar
                .setCornerRadius(10f)
                .setBarColor(ContextCompat.getColor(getActivity(), R.color.seek_bar_default))
                .setBarHighlightColor(ContextCompat.getColor(getActivity(), R.color.seek_bar_progress))
                .setSteps(1)
                .setLeftThumbBitmap(leftBitmap)
                .setLeftThumbHighlightBitmap(leftBitmap)
                .setRightThumbBitmap(rightBitmap)
                .setRightThumbHighlightBitmap(rightBitmap)
                .setDataType(CrystalRangeSeekbar.DataType.INTEGER)
                .apply();

        // set listener
        mClinicFeesSeekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {

                if (mLeftThumbView != null) {
                    Bitmap bitmap = setThumbValue(mLeftThumbView, String.valueOf(minValue));
                    mClinicFeesSeekBar.setLeftThumbBitmap(bitmap);
                    mClinicFeesSeekBar.setLeftThumbHighlightBitmap(bitmap);
                }
                if (mRightThumbView != null) {
                    Bitmap bitmap = setThumbValue(mRightThumbView, String.valueOf(maxValue));
                    mClinicFeesSeekBar.setRightThumbBitmap(bitmap);
                    mClinicFeesSeekBar.setRightThumbHighlightBitmap(bitmap);
                }
            }
        });

        //-------Clinic seek bar : END---------------

        //-------Distance seek bar : END---------------

        distanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Rect tr = distanceSeekBar.getThumb().getBounds();

                mDistanceSeekBarValueIndicator.setText("" + i);

                if (String.valueOf(i).length() > 2) {
                    mDistanceSeekBarValueIndicator.setX(tr.left);
                } else {
                    mDistanceSeekBarValueIndicator.setX(tr.exactCenterX());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //-------Distance seek bar : END---------------

        // TODO unCOMMENT THIS
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // To set min/max value of thumbs of clinic fees seek bar
                setThumbValue(mLeftThumbView, "" + 1);
                setThumbValue(mRightThumbView, "" + 250);

                mClinicFeesSeekBar.setMinValue(1).setMaxValue(250).setMinStartValue(1).setMaxStartValue(250).apply();
                //--------------
                // To set min/max value of thumbs of distance fees seek bar
                Rect tr = distanceSeekBar.getThumb().getBounds();
                mDistanceSeekBarValueIndicator.setX(tr.exactCenterX());
                mDistanceSeekBarValueIndicator.setText(distanceSeekBar.getProgress() + "");
                //------------
            }
        }, 200);
    }

    public interface OnDrawerInteractionListener {
        void onApply(boolean drawerRequired);

        void onReset(boolean drawerRequired);
    }

    @OnClick({R.id.resetButton, R.id.applyButton})
    public void onButtonClicked(View v) {
        switch (v.getId()) {
            case R.id.resetButton:
                mListener.onReset(true);
                break;
            case R.id.applyButton:
                mListener.onApply(true);
                break;
        }
    }

  /*  @OnClick({R.id.genderHeaderView, R.id.locationHeaderView, R.id.clinicFeesHeaderView, R.id.availabilityHeaderView})
    public void onHeaderViewClicked(LinearLayout layout) {
        switch (layout.getId()) {
            case R.id.genderHeaderView:
                if (mGenderContentView.getVisibility() == View.VISIBLE) {
                    mGenderContentView.setVisibility(View.GONE);
                } else {
                    mGenderContentView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.locationHeaderView:
                if (mLocationContentView.getVisibility() == View.VISIBLE) {
                    mLocationContentView.setVisibility(View.GONE);
                } else {
                    mLocationContentView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.clinicFeesHeaderView:
                if (mClinicFeesContentView.getVisibility() == View.VISIBLE) {
                    mClinicFeesContentView.setVisibility(View.GONE);
                } else {
                    mClinicFeesContentView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.availabilityHeaderView:
                if (mAvailabilityContentView.getVisibility() == View.VISIBLE) {
                    mAvailabilityContentView.setVisibility(View.GONE);
                } else {
                    mAvailabilityContentView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }*/


    public Bitmap setThumbValue(View view, String valueToSet) {
        TextView tvRightProgressValue = (TextView) view.findViewById(R.id.tvProgress);
        Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        view.layout(0, 0, view.getRight(), view.getBottom());
        view.draw(c);
        tvRightProgressValue.setText(valueToSet);
        return b;
    }

    @OnClick({R.id.genderMaleLayout, R.id.genderFemaleLayout})
    public void onGenderClicked(View view) {
        switch (view.getId()) {
            case R.id.genderMaleLayout:
                mSelectedGender = mGenderMaleText.getText().toString();
                mGenderMaleIcon.setImageResource(R.drawable.icon_male_after_click);
                mGenderMaleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                mGenderFemaleIcon.setImageResource(R.drawable.icon_female_default);
                mGenderFemaleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.gender_drawer));
                break;
            case R.id.genderFemaleLayout:
                mSelectedGender = mGenderFemaleText.getText().toString();
                mGenderFemaleIcon.setImageResource(R.drawable.icon_female_after_click);
                mGenderFemaleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.female_select));
                mGenderMaleIcon.setImageResource(R.drawable.icon_male_default);
                mGenderMaleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.gender_drawer));
                break;
        }
    }

    @OnClick({R.id.availSunday, R.id.availMonday, R.id.availTues, R.id.availWed, R.id.availThurs, R.id.availFri, R.id.availSat})
    public void onDaysAvailabilityClicked(View view) {
        boolean data;
        switch (view.getId()) {
            case R.id.availSunday:
                data = mSelectedDays.get(getString(R.string.weekday_sun));
                if (data) {
                    mSelectedDays.put(getString(R.string.weekday_sun), false);
                    mAvailSunday.setBackgroundResource(R.drawable.select_days_circle_default);
                    mAvailSunday.setTextColor(ContextCompat.getColor(getActivity(), R.color.tagColor));
                } else {
                    mSelectedDays.put(getString(R.string.weekday_sun), true);
                    mAvailSunday.setBackgroundResource(R.drawable.select_days_circle);
                    mAvailSunday.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                }
                break;
            case R.id.availMonday:
                data = mSelectedDays.get(getString(R.string.weekday_mon));

                if (data) {
                    mSelectedDays.put(getString(R.string.weekday_mon), false);
                    mAvailMonday.setBackgroundResource(R.drawable.select_days_circle_default);
                    mAvailMonday.setTextColor(ContextCompat.getColor(getActivity(), R.color.tagColor));

                } else {
                    mSelectedDays.put(getString(R.string.weekday_mon), true);
                    mAvailMonday.setBackgroundResource(R.drawable.select_days_circle);
                    mAvailMonday.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                }
                break;
            case R.id.availTues:
                data = mSelectedDays.get(getString(R.string.weekday_tues));

                if (data) {
                    mSelectedDays.put(getString(R.string.weekday_tues), false);
                    mAvailTues.setBackgroundResource(R.drawable.select_days_circle_default);
                    mAvailTues.setTextColor(ContextCompat.getColor(getActivity(), R.color.tagColor));

                } else {
                    mSelectedDays.put(getString(R.string.weekday_tues), true);
                    mAvailTues.setBackgroundResource(R.drawable.select_days_circle);
                    mAvailTues.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                }
                break;
            case R.id.availWed:
                data = mSelectedDays.get(getString(R.string.weekday_wed));

                if (data) {
                    mSelectedDays.put(getString(R.string.weekday_wed), false);
                    mAvailWed.setBackgroundResource(R.drawable.select_days_circle_default);
                    mAvailWed.setTextColor(ContextCompat.getColor(getActivity(), R.color.tagColor));

                } else {
                    mSelectedDays.put(getString(R.string.weekday_wed), true);
                    mAvailWed.setBackgroundResource(R.drawable.select_days_circle);
                    mAvailWed.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                }
                break;
            case R.id.availThurs:
                data = mSelectedDays.get(getString(R.string.weekday_thurs));

                if (data) {
                    mSelectedDays.put(getString(R.string.weekday_thurs), false);
                    mAvailThurs.setBackgroundResource(R.drawable.select_days_circle_default);
                    mAvailThurs.setTextColor(ContextCompat.getColor(getActivity(), R.color.tagColor));

                } else {
                    mSelectedDays.put(getString(R.string.weekday_thurs), true);
                    mAvailThurs.setBackgroundResource(R.drawable.select_days_circle);
                    mAvailThurs.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                }
                break;
            case R.id.availFri:
                data = mSelectedDays.get(getString(R.string.weekday_fri));

                if (data) {
                    mSelectedDays.put(getString(R.string.weekday_fri), false);
                    mAvailFri.setBackgroundResource(R.drawable.select_days_circle_default);
                    mAvailFri.setTextColor(ContextCompat.getColor(getActivity(), R.color.tagColor));

                } else {
                    mSelectedDays.put(getString(R.string.weekday_fri), true);
                    mAvailFri.setBackgroundResource(R.drawable.select_days_circle);
                    mAvailFri.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                }
                break;
            case R.id.availSat:
                data = mSelectedDays.get(getString(R.string.weekday_sat));

                if (data) {
                    mSelectedDays.put(getString(R.string.weekday_sat), false);
                    mAvailSat.setBackgroundResource(R.drawable.select_days_circle_default);
                    mAvailSat.setTextColor(ContextCompat.getColor(getActivity(), R.color.tagColor));

                } else {
                    mSelectedDays.put(getString(R.string.weekday_sat), true);
                    mAvailSat.setBackgroundResource(R.drawable.select_days_circle);
                    mAvailSat.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                }
                break;
        }
    }


}
