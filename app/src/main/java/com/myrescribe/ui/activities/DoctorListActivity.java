package com.myrescribe.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.facebook.login.LoginManager;
import com.myrescribe.R;
import com.myrescribe.adapters.DoctorListAdapter;
import com.myrescribe.helpers.doctor.DoctorHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.doctors.DoctorDetail;
import com.myrescribe.model.doctors.DoctorsModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 14/6/17.
 */

public class DoctorListActivity extends AppCompatActivity implements HelperResponse,View.OnClickListener {

    @BindView(R.id.doctorListView)
    ListView mDoctorListView;
    @BindView(R.id.backArrow)
    ImageView mBackArrow;

    private LinearLayout mToolbar;

    private DoctorHelper mDoctorHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity);
        ButterKnife.bind(this);

        mToolbar = (LinearLayout) findViewById(R.id.toolbar);
        initialize();
    }

    private void initialize() {
        mDoctorHelper = new DoctorHelper(this, this);
        mDoctorHelper.doGetDoctorList();
        mBackArrow.setOnClickListener(this);
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        DoctorsModel data = (DoctorsModel) customResponse;
        ArrayList<DoctorDetail> doctorDetails = formatResponseDataForAdapter(data.getDoctorList());
        DoctorListAdapter showDoctorListAdapter = new DoctorListAdapter(this, R.layout.item_doctor_list, doctorDetails);
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

    //TODO : Here main lengthy parsing begins to format list properly.
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.backArrow:
                finish();
                break;

        }
    }
}
