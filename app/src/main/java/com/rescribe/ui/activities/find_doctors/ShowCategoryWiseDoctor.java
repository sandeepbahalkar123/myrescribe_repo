package com.rescribe.ui.activities.find_doctors;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.book_appointment.BookAppointFilteredDocList;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.helpers.book_appointment.ServicesCardViewImpl;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 27/11/17.
 */

public class ShowCategoryWiseDoctor extends AppCompatActivity implements HelperResponse {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.showDoctorList)
    RecyclerView showDoctorList;
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    private BookAppointFilteredDocList mBookAppointFilteredDocListAdapter;
    private ServicesCardViewImpl mServicesCardViewImpl;
    private Context mContext;
    private ArrayList<DoctorList> mDoctorCategoryList;
    private String locationReceived = "";
    private DoctorDataHelper doctorDataHelper;
    private String mReceivedTitle;
    private String mClickedItemDataTypeValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_doctors_show_doctor_list);
        ButterKnife.bind(this);
        mDoctorCategoryList = getIntent().getExtras().getParcelableArrayList(getString(R.string.clicked_item_data));
        setSupportActionBar(toolbar);
        mContext = ShowCategoryWiseDoctor.this;

        mReceivedTitle = getIntent().getExtras().getString(getString(R.string.toolbarTitle));
        mClickedItemDataTypeValue = getIntent().getExtras().getString(getString(R.string.clicked_item_data_type_value));

        // toolbar title showing type of doctors as per requirement.
        getSupportActionBar().setTitle(mClickedItemDataTypeValue);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initialize();
    }

    private void initialize() {
        setUpList();

    }

    private void setUpList() {
        mServicesCardViewImpl = new ServicesCardViewImpl(mContext, (ShowCategoryWiseDoctor) mContext);
        if (getString(R.string.favorite).equalsIgnoreCase(mClickedItemDataTypeValue)) {
            mDoctorCategoryList = mServicesCardViewImpl.getFavouriteDocList(-1);
            if(mDoctorCategoryList.size()!=0) {
                emptyListView.setVisibility(View.GONE);
                mBookAppointFilteredDocListAdapter = new BookAppointFilteredDocList(this, mDoctorCategoryList, mServicesCardViewImpl, this, mReceivedTitle);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                showDoctorList.setLayoutManager(layoutManager);
                showDoctorList.setHasFixedSize(true);
                showDoctorList.setAdapter(mBookAppointFilteredDocListAdapter);
            }else{
                emptyListView.setVisibility(View.VISIBLE);
            }
        } else if (getString(R.string.recently_visit_doctor).equalsIgnoreCase(mClickedItemDataTypeValue)) {
            mDoctorCategoryList = mServicesCardViewImpl.getCategoryWiseDoctorList(mClickedItemDataTypeValue, -1);
            if(mDoctorCategoryList.size()!=0) {
                emptyListView.setVisibility(View.GONE);
                mBookAppointFilteredDocListAdapter = new BookAppointFilteredDocList(this, mDoctorCategoryList, mServicesCardViewImpl, this, mReceivedTitle);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                showDoctorList.setLayoutManager(layoutManager);
                showDoctorList.setHasFixedSize(true);
                showDoctorList.setAdapter(mBookAppointFilteredDocListAdapter);
            }else{
                emptyListView.setVisibility(View.VISIBLE);
            }
        } else if (getString(R.string.sponsored_doctor).equalsIgnoreCase(mClickedItemDataTypeValue)) {
            mDoctorCategoryList = mServicesCardViewImpl.getCategoryWiseDoctorList(mClickedItemDataTypeValue, -1);
            if(mDoctorCategoryList.size()!=0) {
                mBookAppointFilteredDocListAdapter = new BookAppointFilteredDocList(this, mDoctorCategoryList, mServicesCardViewImpl, this, mReceivedTitle);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                showDoctorList.setLayoutManager(layoutManager);
                showDoctorList.setHasFixedSize(true);
                showDoctorList.setAdapter(mBookAppointFilteredDocListAdapter);
            } else{
                emptyListView.setVisibility(View.VISIBLE);
            }
        } else if (getIntent().getExtras().getString(getString(R.string.toolbarTitle)).equalsIgnoreCase(getString(R.string.complaints))) {
           /* HashMap<String, String> userSelectedLocationInfo = RescribeApplication.getUserSelectedLocationInfo();
            locationReceived = userSelectedLocationInfo.get(getString(R.string.location));
            String[] split = locationReceived.split(",");
            doctorDataHelper = new DoctorDataHelper(this,this);
            doctorDataHelper.doGetDoctorListByComplaint(split[1].trim(), split[0].trim(), selectIdComplaint1, selectIdComplaint2);*/

        }
    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        switch (mOldDataTag) {
            case RescribeConstants.TASK_SET_FAVOURITE_DOCTOR:
                CommonBaseModelContainer temp = (CommonBaseModelContainer) customResponse;
                //  CommonMethods.showToast(this, temp.getCommonRespose().getStatusMessage());
                if (temp.getCommonRespose().isSuccess()) {
                    //--------
                    ServicesCardViewImpl.updateFavStatusForDoctorDataObject(ServicesCardViewImpl.getUserSelectedDoctorListDataObject());
                    //--------
                    mBookAppointFilteredDocListAdapter.updateClickedItemFavImage();

                }
                break;
        }

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


    @Override
    protected void onResume() {
        super.onResume();
        setUpList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
