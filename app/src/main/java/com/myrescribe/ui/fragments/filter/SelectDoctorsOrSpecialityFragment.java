package com.myrescribe.ui.fragments.filter;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.myrescribe.R;
import com.myrescribe.adapters.FilterDoctorsAdapter;
import com.myrescribe.model.doctors.doctor_info.DoctorDetail;
import com.myrescribe.model.filter.FilterDoctorModel;
import com.myrescribe.ui.customesViews.CustomTextView;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SelectDoctorsOrSpecialityFragment extends Fragment {

    @BindView(R.id.closeButton)
    Button closeButton;
    @BindView(R.id.titleTextView)
    CustomTextView titleTextView;
    @BindView(R.id.resetButton)
    Button resetButton;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private OnSelectDoctorInteractionListener mListener;
    private ArrayList<DoctorDetail> doctorList;

    public SelectDoctorsOrSpecialityFragment() {
        // Required empty public constructor
    }

    public static SelectDoctorsOrSpecialityFragment newInstance(String title) {
        SelectDoctorsOrSpecialityFragment fragment = new SelectDoctorsOrSpecialityFragment();
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
        if (getArguments() != null) {
            String title = getArguments().getString(MyRescribeConstants.TITLE);
            titleTextView.setText(title);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        String doctorListJson = CommonMethods.readJsonFile(getActivity(), "filterdoctor.json");
        Gson gson = new Gson();
        FilterDoctorModel filterDoctorModel = gson.fromJson(doctorListJson, FilterDoctorModel.class);
        doctorList = filterDoctorModel.getDoctors();
        FilterDoctorsAdapter filterDoctorsAdapter = new FilterDoctorsAdapter(getContext(), doctorList);
        recyclerView.setAdapter(filterDoctorsAdapter);
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
                mListener.onBack();
                break;
            case R.id.resetButton:
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SelectDoctorsOrSpecialityFragment.OnSelectDoctorInteractionListener) {
            mListener = (SelectDoctorsOrSpecialityFragment.OnSelectDoctorInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSelectDoctorInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSelectDoctorInteractionListener {
        void onBack();
    }
}
