package com.rescribe.ui.fragments.doctor_connect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.DoctorSearchByNameAdapter;
import com.rescribe.model.doctor_connect.ChatDoctor;
import com.rescribe.ui.activities.DoctorConnectActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by jeetal on 8/9/17.
 */

public class SearchDoctorByNameFragment extends Fragment implements DoctorConnectActivity.OnClickOfSearchBar {
    @BindView(R.id.listView)
    RecyclerView mRecyclerView;
    @BindView(R.id.noDataAvailable)
    RelativeLayout noDataAvailable;
    Unbinder unbinder;
    @BindView(R.id.displayNote)
    CustomTextView displayNote;

    @BindView(R.id.fragmentContainer)
    RelativeLayout fragmentContainer;

    private ArrayList<ChatDoctor> mReceivedList;
    private DoctorSearchByNameAdapter doctorSearchByNameAdapter;
    private String mClickedSpecialityOfDoctor;


    public SearchDoctorByNameFragment() {
    }

    public static SearchDoctorByNameFragment newInstance(ArrayList<ChatDoctor> chatDoctors, Bundle bundleData) {
        SearchDoctorByNameFragment fragment = new SearchDoctorByNameFragment();
        if (bundleData == null) {
            bundleData = new Bundle();
        }
        bundleData.putParcelableArrayList(RescribeConstants.CONNECT_REQUEST, chatDoctors);
        fragment.setArguments(bundleData);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mRootView = inflater.inflate(R.layout.doctor_connect_search_layout, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mClickedSpecialityOfDoctor = arguments.getString(RescribeConstants.ITEM_DATA);
            mReceivedList = getArguments().getParcelableArrayList(RescribeConstants.CONNECT_REQUEST);
        }

        unbinder = ButterKnife.bind(this, mRootView);
        init();

        return mRootView;
    }

    private void init() {
        if (mReceivedList == null) {
            mRecyclerView.setVisibility(View.GONE);
            noDataAvailable.setVisibility(View.VISIBLE);
        } else {

            for (int i = 0; i < filterDataOnDocSpeciality().size(); i++) {
                String doctorName = filterDataOnDocSpeciality().get(i).getDoctorName();
                if (doctorName.startsWith("DR. ")) {
                    String drName = doctorName.replace("DR. ", "Dr. ");
                    filterDataOnDocSpeciality().get(i).setDoctorName(drName);
                } else if (doctorName.startsWith("DR.")) {
                    String drName = doctorName.replace("DR.", "Dr. ");
                    filterDataOnDocSpeciality().get(i).setDoctorName(drName);
                } else if (doctorName.startsWith("Dr. ")) {
                    String drName = doctorName.replace("Dr. ", "Dr. ");
                    filterDataOnDocSpeciality().get(i).setDoctorName(drName);
                } else {
                    filterDataOnDocSpeciality().get(i).setDoctorName("Dr. " + doctorName);
                }
            }

            ArrayList<ChatDoctor> docList = filterDataOnDocSpeciality();
            if (docList.isEmpty()) {
                mRecyclerView.setVisibility(View.GONE);
                noDataAvailable.setVisibility(View.VISIBLE);
            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                noDataAvailable.setVisibility(View.GONE);

                doctorSearchByNameAdapter = new DoctorSearchByNameAdapter(getActivity(), docList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                        DividerItemDecoration.VERTICAL);
                mRecyclerView.addItemDecoration(dividerItemDecoration);
                mRecyclerView.setAdapter(doctorSearchByNameAdapter);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setOnClickOfSearchBar(String searchText) {
        if (doctorSearchByNameAdapter != null)
            doctorSearchByNameAdapter.getFilter().filter(searchText);
    }

    private ArrayList<ChatDoctor> filterDataOnDocSpeciality() {
        ArrayList<ChatDoctor> chatDoctors = this.mReceivedList;
        ArrayList<ChatDoctor> dataList = new ArrayList<>();
        if (mClickedSpecialityOfDoctor == null) {
            return chatDoctors;
        } else {
            for (ChatDoctor listObject :
                    chatDoctors) {
                if (mClickedSpecialityOfDoctor.equalsIgnoreCase(listObject.getSpecialization())) {
                    dataList.add(listObject);
                }
            }
        }
        return dataList;
    }
}
