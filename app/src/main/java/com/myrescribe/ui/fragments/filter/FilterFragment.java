package com.myrescribe.ui.fragments.filter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.myrescribe.R;
import com.myrescribe.ui.customesViews.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FilterFragment extends Fragment {

    @BindView(R.id.closeButton)
    Button closeButton;
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

    public FilterFragment() {
        // Required empty public constructor
    }

    public static FilterFragment newInstance() {
        FilterFragment fragment = new FilterFragment();
        Bundle bundle = new Bundle();
        // put data
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



        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.closeButton, R.id.titleTextView, R.id.resetButton, R.id.selectDoctorLayout, R.id.selectSpecialityLayout, R.id.selectMonthLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.closeButton:
                break;
            case R.id.titleTextView:
                break;
            case R.id.resetButton:
                break;
            case R.id.selectDoctorLayout:
                break;
            case R.id.selectSpecialityLayout:
                break;
            case R.id.selectMonthLayout:
                break;
        }
    }
}
