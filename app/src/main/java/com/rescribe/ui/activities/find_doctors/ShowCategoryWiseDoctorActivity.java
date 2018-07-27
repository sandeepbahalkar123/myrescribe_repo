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
import com.rescribe.adapters.book_appointment.BookAppointFilteredDocListAdapter;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.helpers.book_appointment.ServicesCardViewImpl;
import com.rescribe.helpers.database.AppDBHelper;
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

public class ShowCategoryWiseDoctorActivity extends AppCompatActivity implements HelperResponse {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.showDoctorList)
    RecyclerView showDoctorList;
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    private BookAppointFilteredDocListAdapter mBookAppointFilteredDocListAdapterAdapter;
    private Context mContext;
    private ArrayList<DoctorList> mDoctorCategoryList;
    private String locationReceived = "";
    private DoctorDataHelper doctorDataHelper;
    private String mReceivedTitle;
    private String mClickedItemDataTypeValue;
    private AppDBHelper appDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_doctors_show_doctor_list);
        ButterKnife.bind(this);
        mDoctorCategoryList = getIntent().getExtras().getParcelableArrayList(RescribeConstants.ITEM_DATA);
        setSupportActionBar(toolbar);
        mContext = ShowCategoryWiseDoctorActivity.this;
        appDBHelper = new AppDBHelper(mContext);

        mReceivedTitle = getIntent().getExtras().getString(RescribeConstants.TITLE);
        mClickedItemDataTypeValue = getIntent().getExtras().getString(RescribeConstants.ITEM_DATA_VALUE);

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
        ServicesCardViewImpl mServicesCardViewImpl = new ServicesCardViewImpl(mContext, (ShowCategoryWiseDoctorActivity) mContext);
        if (getString(R.string.favorite).equalsIgnoreCase(mClickedItemDataTypeValue)) {
            mDoctorCategoryList = mServicesCardViewImpl.getFavouriteDocList(-1);

            if (mDoctorCategoryList.size() != 0) {
                emptyListView.setVisibility(View.GONE);
                mBookAppointFilteredDocListAdapterAdapter = new BookAppointFilteredDocListAdapter(this, mDoctorCategoryList, mServicesCardViewImpl, this, mReceivedTitle, mReceivedTitle);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                showDoctorList.setLayoutManager(layoutManager);
                showDoctorList.setHasFixedSize(true);
                showDoctorList.setAdapter(mBookAppointFilteredDocListAdapterAdapter);
            } else
                emptyListView.setVisibility(View.VISIBLE);

        } else if (getString(R.string.recently_visit_doctor).equalsIgnoreCase(mClickedItemDataTypeValue)) {
            mDoctorCategoryList = mServicesCardViewImpl.getCategoryWiseDoctorList(mClickedItemDataTypeValue, -1);
            mBookAppointFilteredDocListAdapterAdapter = new BookAppointFilteredDocListAdapter(this, mDoctorCategoryList, mServicesCardViewImpl, this, mReceivedTitle, mReceivedTitle);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            showDoctorList.setLayoutManager(layoutManager);
            showDoctorList.setHasFixedSize(true);
            showDoctorList.setAdapter(mBookAppointFilteredDocListAdapterAdapter);
        }  else if (getString(R.string.sponsored_doctor).equalsIgnoreCase(mClickedItemDataTypeValue)) {
            mDoctorCategoryList = mServicesCardViewImpl.getCategoryWiseDoctorList(mClickedItemDataTypeValue, -1);
            if (mDoctorCategoryList.size() != 0) {
                mBookAppointFilteredDocListAdapterAdapter = new BookAppointFilteredDocListAdapter(this, mDoctorCategoryList, mServicesCardViewImpl, this, mReceivedTitle, mReceivedTitle);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                showDoctorList.setLayoutManager(layoutManager);
                showDoctorList.setHasFixedSize(true);
                showDoctorList.setAdapter(mBookAppointFilteredDocListAdapterAdapter);
            } else
                emptyListView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        switch (mOldDataTag) {
            case RescribeConstants.TASK_SET_FAVOURITE_DOCTOR:
                CommonBaseModelContainer temp = (CommonBaseModelContainer) customResponse;
                //  CommonMethods.showToast(this, temp.getCommonRespose().getStatusMessage());
                if (temp.getCommonRespose().isSuccess()) {
                    ServicesCardViewImpl.updateFavStatusForDoctorDataObject(ServicesCardViewImpl.getUserSelectedDoctorListDataObject(), appDBHelper);
                    mBookAppointFilteredDocListAdapterAdapter.updateClickedItemFavImage();
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
        initialize();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
