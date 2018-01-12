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
import com.rescribe.adapters.filter.FilterDoctorSpecialitiesAdapter;
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

public class SelectSpecialityFragment extends Fragment {

    @BindView(R.id.closeButton)
    Button closeButton;
    @BindView(R.id.titleTextView)
    CustomTextView titleTextView;
    @BindView(R.id.resetButton)
    Button resetButton;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private OnSelectSpecialityInteractionListener mListener;
    private FilterDoctorSpecialitiesAdapter filterDoctorSpecialitiesAdapter;
    private ArrayList<DoctorSpecialityData> doctorSpecialityList;

    public SelectSpecialityFragment() {
        // Required empty public constructor
    }

    public static SelectSpecialityFragment newInstance(ArrayList<DoctorSpecialityData> doctorSpecialityDataArrayList, String title) {
        SelectSpecialityFragment fragment = new SelectSpecialityFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(RescribeConstants.DOCTORS_LIST, doctorSpecialityDataArrayList);
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
            doctorSpecialityList = getArguments().getParcelableArrayList(RescribeConstants.DOCTORS_LIST);
        }
        Comparator<DoctorSpecialityData> comparator = new Comparator<DoctorSpecialityData>() {
            @Override
            public int compare(DoctorSpecialityData o1, DoctorSpecialityData o2) {
                String m1 = o1.getSpeciality() ;
                String m2 = o2.getSpeciality();
                return m2.compareTo(m1);
            }
        };

        Collections.sort(doctorSpecialityList, Collections.reverseOrder(comparator));

        // off recyclerView Animation

        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator)
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        filterDoctorSpecialitiesAdapter = new FilterDoctorSpecialitiesAdapter(getContext(), doctorSpecialityList);
        recyclerView.setAdapter(filterDoctorSpecialitiesAdapter);
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
                for (DoctorSpecialityData doctorSpecialityData : doctorSpecialityList) {
                    doctorSpecialityData.setSelected(false);
                }
                filterDoctorSpecialitiesAdapter.notifyDataSetChanged();
                mListener.setDoctorSpeciality(getResources().getString(R.string.select_doctors_speciality));
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SelectSpecialityFragment.OnSelectSpecialityInteractionListener) {
            mListener = (SelectSpecialityFragment.OnSelectSpecialityInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSelectSpecialityInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSelectSpecialityInteractionListener {
        void onFragmentBack();
        void setDoctorSpeciality(String speciality);
    }
}
