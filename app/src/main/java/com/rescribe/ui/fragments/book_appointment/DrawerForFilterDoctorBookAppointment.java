package com.rescribe.ui.fragments.book_appointment;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.ui.customesViews.CustomTextView;

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
    @BindView(R.id.clinicFeesSeekBar)
    SeekBar mClinicFeesSeekBar;
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
    private OnDrawerInteractionListener mListener;
    private View mThumbView;

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
        mThumbView = LayoutInflater.from(getActivity()).inflate(R.layout.seekbar_progress_thumb_layout, null, false);

        mClinicFeesSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBar.setThumb(getThumb(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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

    public interface OnDrawerInteractionListener {
        void onApply();

        void onReset();
    }

    @OnClick({R.id.genderHeaderView, R.id.locationHeaderView, R.id.clinicFeesHeaderView, R.id.availabilityHeaderView})
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
    }


    public Drawable getThumb(int progress) {
        ((TextView) mThumbView.findViewById(R.id.tvProgress)).setText(progress + "");
        mThumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(mThumbView.getMeasuredWidth(), mThumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mThumbView.layout(0, 0, mThumbView.getMeasuredWidth(), mThumbView.getMeasuredHeight());
        mThumbView.draw(canvas);
        return new BitmapDrawable(getResources(), bitmap);
    }
}
