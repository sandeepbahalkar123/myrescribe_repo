package com.myrescribe.ui.fragments.filter;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.myrescribe.R;
import com.myrescribe.adapters.filter.FilterDoctorsAdapter;
import com.myrescribe.model.doctors.doctor_info.DoctorDetail;
import com.myrescribe.ui.customesViews.CustomTextView;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SelectDoctorsFragment extends Fragment {

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

    public SelectDoctorsFragment() {
        // Required empty public constructor
    }

    public static SelectDoctorsFragment newInstance(ArrayList<DoctorDetail> doctorList, String title) {
        SelectDoctorsFragment fragment = new SelectDoctorsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MyRescribeConstants.DOCTORS_LIST, doctorList);
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

            doctorList = (ArrayList<DoctorDetail>) getArguments().getSerializable(MyRescribeConstants.DOCTORS_LIST);
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
                mListener.onBack();
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SelectDoctorsFragment.OnSelectDoctorInteractionListener) {
            mListener = (SelectDoctorsFragment.OnSelectDoctorInteractionListener) context;
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
