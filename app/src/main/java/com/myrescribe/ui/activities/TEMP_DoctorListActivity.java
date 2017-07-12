package com.myrescribe.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.myrescribe.util.CommonMethods;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;

/**
 * Created by jeetal on 14/6/17.
 */

public class TEMP_DoctorListActivity extends AppCompatActivity implements HelperResponse, View.OnClickListener {

    private static final String COUNT = "column-count";
    private static final String VALUE = "VALUE";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    ListView mDoctorListView;
    DoctorListAdapter showDoctorListAdapter;
    private DoctorHelper mDoctorHelper;
    private RelativeLayout mTreeViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_all_view_doctor_history);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mDoctorListView = (ListView) findViewById(R.id.doctorListView);
        mTreeViewContainer = (RelativeLayout) findViewById(R.id.treeViewContainer);
        mDoctorHelper = new DoctorHelper(this, this);
        mDoctorHelper.doGetDoctorList();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        DoctorModel data = (DoctorModel) customResponse;
        CommonMethods.Log("onSuccess", data.toString());
        ArrayList<DoctorDetail> jan = data.getDoctorInfoMonthContainer().getFormattedDoctorList("JAN", this);
        showDoctorListAdapter = new DoctorListAdapter(this, R.layout.item_doctor_list_layout, jan);
        mDoctorListView.setAdapter(showDoctorListAdapter);
        //  createTreeStructure(doctorDetails1);
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
                    (R.drawable.cross_icon, data)).setViewHolder(new DoctorListMainHeaderHolder(this));
            root.addChildren(mainListObjectItemNode);
        }
        AndroidTreeView mAndroidTreeView = new AndroidTreeView(this, root);
        mAndroidTreeView.setDefaultAnimation(true);
        mAndroidTreeView.setUse2dScroll(true);
        mAndroidTreeView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
        View view = mAndroidTreeView.getView();
        mTreeViewContainer.addView(view);
    }
}