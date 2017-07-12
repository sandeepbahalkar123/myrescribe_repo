package com.myrescribe.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.myrescribe.R;
import com.myrescribe.adapters.DoctorListAdapter;
import com.myrescribe.helpers.doctor.DoctorHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.doctors.DoctorDetail;
import com.myrescribe.model.doctors.DoctorModel;
import com.myrescribe.ui.customesViews.tree_view_structure.DoctorListIconTreeItemHolder;
import com.myrescribe.ui.customesViews.tree_view_structure.DoctorListMainHeaderHolder;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.HashMap;


public class DoctorListFragment extends Fragment implements HelperResponse, View.OnClickListener {

    private static final String COUNT = "column-count";
    private static final String VALUE = "VALUE";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    ListView mDoctorListView;
    DoctorListAdapter showDoctorListAdapter;
    private DoctorHelper mDoctorHelper;
    private RelativeLayout mTreeViewContainer;

    public DoctorListFragment() {
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

    public static DoctorListFragment createNewFragment(String dataString) {
        DoctorListFragment fragment = new DoctorListFragment();
        Bundle args = new Bundle();
        args.putString(VALUE, dataString);
        fragment.setArguments(args);
        return fragment;
    }

    private void init(View view) {
        mDoctorListView = (ListView) view.findViewById(R.id.doctorListView);
        mTreeViewContainer = (RelativeLayout) view.findViewById(R.id.treeViewContainer);
        mDoctorHelper = new DoctorHelper(getActivity(), this);
        mDoctorHelper.doGetDoctorList();

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        DoctorModel data = (DoctorModel) customResponse;
        //ArrayList<DoctorDetail> doctorDetails = formatResponseDataForAdapter(data.getDoctorList());
        //   showDoctorListAdapter = new DoctorListAdapter(getActivity(), R.layout.item_doctor_list_layout, doctorDetails);
        // mDoctorListView.setAdapter(showDoctorListAdapter);
        //createTreeStructure(doctorDetails);
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

    private void createTreeStructure(ArrayList<DoctorDetail> doctorDetails) {
        TreeNode root = TreeNode.root();
        for (DoctorDetail data : doctorDetails) {
            TreeNode mainListObjectItemNode = new TreeNode(new DoctorListIconTreeItemHolder.IconTreeItem
                    (R.drawable.cross_icon, data)).setViewHolder(new DoctorListMainHeaderHolder(getActivity()));
            root.addChildren(mainListObjectItemNode);
        }
        AndroidTreeView mAndroidTreeView = new AndroidTreeView(this.getContext(), root);
        mAndroidTreeView.setDefaultAnimation(true);
        mAndroidTreeView.setUse2dScroll(true);
        mAndroidTreeView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
        View view = mAndroidTreeView.getView();
        mTreeViewContainer.addView(view);
    }

}
