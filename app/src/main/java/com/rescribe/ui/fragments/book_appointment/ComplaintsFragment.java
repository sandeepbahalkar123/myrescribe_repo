package com.rescribe.ui.fragments.book_appointment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.rescribe.R;
import com.rescribe.adapters.book_appointment.ComplaintsSpinnerAdapter;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.complaints.ComplaintList;
import com.rescribe.model.book_appointment.complaints.ComplaintsBaseModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by jeetal on 25/9/17.
 */

public class ComplaintsFragment extends Fragment implements HelperResponse, AdapterView.OnItemSelectedListener {

    private static final String DATA = "DATA";
    @BindView(R.id.spinnerComplaint1)
    Spinner spinnerComplaint1;
    @BindView(R.id.editTextComplaint1)
    EditText editTextComplaint1;
    @BindView(R.id.showEditText)
    LinearLayout showEditText;
    Unbinder unbinder;
    @BindView(R.id.okButton)
    Button okButton;
    private View mRootView;
    DoctorDataHelper doctorDataHelper;
    private ArrayList<ComplaintList> mArrayId;
    private ComplaintsSpinnerAdapter mComplaintsSpinnerAdapter;
    private ComplaintsBaseModel mComplaintsBaseModel;
    String selectId;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ComplaintsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ComplaintsFragment newInstance(Bundle data) {
        ComplaintsFragment fragment = new ComplaintsFragment();
        Bundle args = new Bundle();
        // args.putString(DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.still_in_doubt, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        init();
        return mRootView;
    }

    private void init() {
        doctorDataHelper = new DoctorDataHelper(getActivity(), this);
        doctorDataHelper.doGetComplaintsList();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void setComplaintSpinnerListAdapter() {
        spinnerComplaint1.setOnItemSelectedListener(this);
        mComplaintsSpinnerAdapter = new ComplaintsSpinnerAdapter(getActivity(), mComplaintsBaseModel.getComplaintsModel().getComplaintList());
        spinnerComplaint1.setAdapter(mComplaintsSpinnerAdapter);
    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (customResponse != null) {
            mComplaintsBaseModel = (ComplaintsBaseModel) customResponse;
            setComplaintSpinnerListAdapter();

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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.editTextComplaint1, R.id.showEditText,R.id.okButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.editTextComplaint1:
                break;
            case R.id.showEditText:
                break;
            case R.id.okButton:
                if(editTextComplaint1.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Please enter valid option", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "Complaint posted successfully", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinnerComplaint1) {
            int selected = (int) parent.getItemAtPosition(position);
            selectId = mComplaintsBaseModel.getComplaintsModel().getComplaintList().get(selected).getComplaint();
            if (selectId.equals("Others")) {
                showEditText.setVisibility(View.VISIBLE);
            } else {
                showEditText.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
