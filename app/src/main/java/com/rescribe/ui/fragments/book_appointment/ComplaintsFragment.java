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
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;

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
    @BindView(R.id.spinnerComplaint2)
    Spinner spinnerComplaint2;
    @BindView(R.id.editTextComplaint2)
    EditText editTextComplaint2;
    @BindView(R.id.showEditText2)
    LinearLayout showEditText2;
    private View mRootView;
    DoctorDataHelper doctorDataHelper;
    private ArrayList<ComplaintList> mArrayId;
    private ComplaintsSpinnerAdapter mComplaintsSpinnerAdapter;
    private ComplaintsBaseModel mComplaintsBaseModel;
    String selectIdComplaint1;
    String selectIdComplaint2;
    private static Bundle args;


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
        args = data;
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
        spinnerComplaint2.setOnItemSelectedListener(this);
        mComplaintsSpinnerAdapter = new ComplaintsSpinnerAdapter(getActivity(), mComplaintsBaseModel.getComplaintsModel().getComplaintList());
        spinnerComplaint1.setAdapter(mComplaintsSpinnerAdapter);
        spinnerComplaint2.setAdapter(mComplaintsSpinnerAdapter);
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

    @OnClick({R.id.editTextComplaint1, R.id.showEditText, R.id.okButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.editTextComplaint1:
                break;
            case R.id.showEditText:
                break;
            case R.id.okButton:
                if (showEditText2.getVisibility() == View.GONE && showEditText.getVisibility() == View.GONE) {
                    if (selectIdComplaint1.equals("Select") && selectIdComplaint2.equals("Select")) {
                        Toast.makeText(getActivity(), "Please select valid option", Toast.LENGTH_SHORT).show();

                    }else{
                        BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
                        activity.loadFragment(BookAppointFilteredDoctorListFragment.newInstance(args), true);

                    }
                } else if (showEditText2.getVisibility() == View.VISIBLE && showEditText.getVisibility() == View.VISIBLE) {
                    if (editTextComplaint1.getText().toString().equals("")&& editTextComplaint2.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Please enter text", Toast.LENGTH_SHORT).show();
                    }else{
                        BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
                        activity.loadFragment(BookAppointFilteredDoctorListFragment.newInstance(args), true);

                    }
                }

                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinnerComplaint1) {
            int selected = (int) parent.getItemAtPosition(position);
            selectIdComplaint1 = mComplaintsBaseModel.getComplaintsModel().getComplaintList().get(selected).getComplaint();
            if (selectIdComplaint1.equals("Others")) {
                showEditText.setVisibility(View.VISIBLE);
            } else {
                showEditText.setVisibility(View.GONE);
            }
        } else if (parent.getId() == R.id.spinnerComplaint2) {
            int selected = (int) parent.getItemAtPosition(position);
            selectIdComplaint2 = mComplaintsBaseModel.getComplaintsModel().getComplaintList().get(selected).getComplaint();
            if (selectIdComplaint2.equals("Others")) {
                showEditText2.setVisibility(View.VISIBLE);
            } else {
                showEditText2.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
