package com.myrescribe.ui.fragments.filter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.myrescribe.R;
import com.myrescribe.model.visit_details.Data;
import com.myrescribe.ui.customesViews.CustomTextView;
import com.myrescribe.util.MyRescribeConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SelectDoctorsorSpecialityFragment extends Fragment {

    @BindView(R.id.closeButton)
    Button closeButton;
    @BindView(R.id.titleTextView)
    CustomTextView titleTextView;
    @BindView(R.id.resetButton)
    Button resetButton;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    Unbinder unbinder;

    public SelectDoctorsorSpecialityFragment() {
        // Required empty public constructor
    }

    public static SelectDoctorsorSpecialityFragment newInstance(String title) {
        SelectDoctorsorSpecialityFragment fragment = new SelectDoctorsorSpecialityFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MyRescribeConstants.TITLE, title);
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
        View view = inflater.inflate(R.layout.fragment_select_doctors_speciality, container, false);
        unbinder = ButterKnife.bind(this, view);



        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.closeButton, R.id.resetButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.closeButton:
                break;
            case R.id.resetButton:
                break;
        }
    }
}
