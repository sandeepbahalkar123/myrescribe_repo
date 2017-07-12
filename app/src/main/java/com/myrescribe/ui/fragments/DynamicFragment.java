package com.myrescribe.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.myrescribe.R;
import com.myrescribe.adapters.DoctorAdapter;
import com.myrescribe.adapters.DoctorListAdapter;
import com.myrescribe.helpers.doctor.DoctorHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.doctors.DoctorDetail;
import com.myrescribe.model.doctors.DoctorsModel;
import java.util.ArrayList;
import java.util.HashMap;


public class DynamicFragment extends Fragment implements HelperResponse,View.OnClickListener{

    private static final String COUNT = "column-count";
    private static final String VALUE = "VALUE";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    RecyclerView mDoctorListView;
    DoctorAdapter showDoctorListAdapter;
    private DoctorHelper mDoctorHelper;

    public DynamicFragment() {
        // Required empty public constructor
    }
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(COUNT);
        }
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_all_view_doctor_history, container, false);
        init(rootView);

        return rootView;
    }
    public static DynamicFragment createNewFragment(String dataString) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putString(VALUE, dataString);
       fragment.setArguments(args);
        return fragment;
    }

    private void init(View view) {
        mDoctorListView = (RecyclerView) view.findViewById(R.id.doctorListView);
        mDoctorHelper = new DoctorHelper(getActivity(), this);
        mDoctorHelper.doGetDoctorList();

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        DoctorsModel data = (DoctorsModel) customResponse;
        ArrayList<DoctorDetail> doctorDetails = formatResponseDataForAdapter(data.getDoctorList());
        showDoctorListAdapter = new DoctorAdapter(getActivity(), doctorDetails);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mDoctorListView.setLayoutManager(layoutManager);
        mDoctorListView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mDoctorListView.getContext(),
                layoutManager.getOrientation());
        mDoctorListView.addItemDecoration(dividerItemDecoration);
        mDoctorListView.setAdapter(showDoctorListAdapter);

    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {

    }
    private ArrayList<DoctorDetail> formatResponseDataForAdapter(HashMap<String, ArrayList<DoctorDetail>> dataList) {
        ArrayList<DoctorDetail> formattedDoctorList = new ArrayList<>();
        for (String key : dataList.keySet()) {
            boolean flag = true;
            System.out.println(key);
            ArrayList<DoctorDetail> doctorDetails = dataList.get(key);
            for (DoctorDetail dataObject : doctorDetails) {
                if (flag) {
                    dataObject.setIsStartElement(true);
                    flag = false;
                }
                dataObject.setRespectiveDate(key);
                formattedDoctorList.add(dataObject);
            }
        }
        return formattedDoctorList;
    }
}
