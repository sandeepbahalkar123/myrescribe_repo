package com.rescribe.ui.fragments.filter;


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

import com.rescribe.R;
import com.rescribe.adapters.filter.FilterDoctorsAdapter;
import com.rescribe.model.filter.DoctorData;
import com.rescribe.model.filter.DoctorSpecialityData;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    private ArrayList<DoctorData> doctorList;
    private FilterDoctorsAdapter filterDoctorsAdapter;

    public SelectDoctorsFragment() {
        // Required empty public constructor
    }

    public static SelectDoctorsFragment newInstance(ArrayList<DoctorData> doctorList, String title) {
        SelectDoctorsFragment fragment = new SelectDoctorsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(RescribeConstants.DOCTORS_LIST, doctorList);
        bundle.putString(RescribeConstants.TITLE, title);
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
            String title = getArguments().getString(RescribeConstants.TITLE);
            titleTextView.setText(title);

            doctorList = getArguments().getParcelableArrayList(RescribeConstants.DOCTORS_LIST);
        }
        Comparator<DoctorData> comparator = new Comparator<DoctorData>() {
            @Override
            public int compare(DoctorData o1, DoctorData o2) {
                String m1 = o1.getDoctorName() ;
                String m2 = o2.getDoctorName();
                return m2.compareTo(m1);
            }
        };

       // Collections.sort(doctorList, Collections.reverseOrder(comparator));
        // off recyclerView Animation

        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator)
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        filterDoctorsAdapter = new FilterDoctorsAdapter(getContext(), doctorList);
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
                mListener.onFragmentBack();
                break;
            case R.id.resetButton:
                for (DoctorData doctorDetail : doctorList) {
                    doctorDetail.setSelected(false);
                }
                filterDoctorsAdapter.notifyDataSetChanged();
                mListener.setDoctorName(getResources().getString(R.string.select_doctors));
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
        void onFragmentBack();
        void setDoctorName(String name);
    }
}
