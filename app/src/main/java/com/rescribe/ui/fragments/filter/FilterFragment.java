package com.rescribe.ui.fragments.filter;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.filter.FilterCaseDetailsAdapter;
import com.rescribe.model.filter.CaseDetailsData;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.MyRescribeConstants;
import com.ngapps.ganeshshirole.monthpicker.RackMonthPicker;
import com.ngapps.ganeshshirole.monthpicker.listener.DateMonthDialogListener;
import com.ngapps.ganeshshirole.monthpicker.listener.OnCancelMonthDialogListener;
import com.ngapps.ganeshshirole.monthpicker.util.MonthOfYear;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FilterFragment extends Fragment {

    @BindView(R.id.applyButton)
    Button applyButton;
    @BindView(R.id.titleTextView)
    CustomTextView titleTextView;
    @BindView(R.id.resetButton)
    Button resetButton;
    @BindView(R.id.selectDoctorLayout)
    RelativeLayout selectDoctorLayout;
    @BindView(R.id.selectSpecialityLayout)
    RelativeLayout selectSpecialityLayout;
    @BindView(R.id.selectMonthLayout)
    RelativeLayout selectMonthLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.drNameTextView)
    CustomTextView drNameTextView;
    @BindView(R.id.drSpecialityTextView)
    CustomTextView drSpecialityTextView;
    @BindView(R.id.drCalenderTextView)
    CustomTextView drCalenderTextView;
    private OnDrawerInteractionListener mListener;
    private String monthSelected;
    private RackMonthPicker rackMonthPicker;
    private ArrayList<CaseDetailsData> caseDetailsList;
    private FilterCaseDetailsAdapter filterCaseDetailsAdapter;

    private String fromDateTemp = "";
    private String toDate = "";
    private String fromDate = "";


    public FilterFragment() {
        // Required empty public constructor
    }

    public static FilterFragment newInstance(ArrayList<CaseDetailsData> caseDetailsList) {
        FilterFragment fragment = new FilterFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(MyRescribeConstants.CASE_DETAILS, caseDetailsList);
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
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (getArguments() != null) {
            caseDetailsList = getArguments().getParcelableArrayList(MyRescribeConstants.CASE_DETAILS);
        }

        // off recyclerView Animation

        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator)
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        filterCaseDetailsAdapter = new FilterCaseDetailsAdapter(getContext(), caseDetailsList);
        recyclerView.setAdapter(filterCaseDetailsAdapter);

        rackMonthPicker = new RackMonthPicker(getContext())
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel, boolean isFrom) {
                        CommonMethods.Log("MONTH YEAR", month + " " + startDate + " " + endDate + " " + year + " " + monthLabel + " " + isFrom);

                        if (isFrom) {
                            monthSelected = MonthOfYear.getMonth(month - 1) + " " + year;
                            fromDateTemp = year + "-" + month + "-" + startDate;
                        } else {
                            fromDate = fromDateTemp;
                            monthSelected += " to " + MonthOfYear.getMonth(month - 1) + " " + year;
                            toDate = year + "-" + month + "-" + endDate;
                            drCalenderTextView.setText(monthSelected);
                            CommonMethods.Log("Date", fromDate + " " + toDate);
                        }
                    }
                })
                .setNegativeButton(new OnCancelMonthDialogListener() {
                    @Override
                    public void onCancel(AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });

        return view;
    }

    public void notifyCaseDetails() {
        filterCaseDetailsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.applyButton, R.id.resetButton, R.id.selectDoctorLayout, R.id.selectSpecialityLayout, R.id.selectMonthLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.applyButton:
                mListener.onApply();
                break;
            case R.id.resetButton:
                fromDate = "";
                toDate = "";
                setDoctorName(getResources().getString(R.string.select_doctors));
                setDoctorSpeciality(getResources().getString(R.string.select_doctors_speciality));
                drCalenderTextView.setText(getResources().getString(R.string.select_month_year));
                mListener.onReset();
                break;
            case R.id.selectDoctorLayout:
                mListener.onSelectDoctors();
                break;
            case R.id.selectSpecialityLayout:
                mListener.onSelectSpeciality();
                break;
            case R.id.selectMonthLayout:
                rackMonthPicker.show();
                break;
        }
    }

    public void setDoctorName(String name) {
        drNameTextView.setText(name);
    }

   /* public String getDoctorName() {
        return drNameTextView.getText().toString();
    }*/

    public void setDoctorSpeciality(String speciality) {
        drSpecialityTextView.setText(speciality);
    }

   /* public String getDoctorSpeciality() {
        return drSpecialityTextView.getText().toString();
    }*/

    public String getFromDate(){
        return fromDate;
    }

    public String getToDate(){
        return toDate;
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
        void onSelectDoctors();
        void onSelectSpeciality();
        void onReset();
    }
}
