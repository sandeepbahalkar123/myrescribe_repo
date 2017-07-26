package com.myrescribe.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.helpers.login.LoginHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.login.SignUpModel;
import com.myrescribe.model.requestmodel.login.SignUpRequestModel;
import com.myrescribe.ui.activities.AppLoginConfirmationActivity;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUp.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUp extends Fragment implements HelperResponse {

    private Context mContext;

    @BindView(R.id.editTextFullName)
    EditText mFullName;
    @BindView(R.id.editTextEmailId)
    EditText mEmailId;
    @BindView(R.id.editTextPassword)
    EditText mPassword;
    @BindView(R.id.editTextMobileNo)
    EditText mMobileNo;
    private SignUpRequestModel mSignUpRequestModel;

    public SignUp() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUp.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUp newInstance(String param1, String param2) {
        SignUp fragment = new SignUp();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.sign_up, container, false);
        ButterKnife.bind(this, inflate);
        mContext = this.getContext();
        return inflate;
    }

    /**
     * Return true if fields empty/validation failed, else false.
     *
     * @return
     */
    private boolean validate(String name, String email, String password, String mobileNo) {
        String message = null;
        String enter = getString(R.string.enter);
        if (name.isEmpty()) {
            message = enter + getString(R.string.enter_full_name).toLowerCase(Locale.US);
        } else if (email.isEmpty()) {
            message = enter + getString(R.string.enter_email_id).toLowerCase(Locale.US);
        } else if (!CommonMethods.isValidEmail(email)) {
            message = getString(R.string.err_email_invalid);
        } else if (password.isEmpty()) {
            message = enter + getString(R.string.enter_password).toLowerCase(Locale.US);
        } else if (email.isEmpty()) {
            message = enter + getString(R.string.enter_mobile_no).toLowerCase(Locale.US);
        }
        if (message != null) {
            CommonMethods.showSnack(mMobileNo, message);
            return true;
        } else {
            return false;
        }
    }

    @OnClick(R.id.btn_SignUp)
    public void onSignUp() {
        String name = mFullName.getText().toString();
        String email = mEmailId.getText().toString();
        String password = mPassword.getText().toString();
        String mobileNo = mMobileNo.getText().toString();
        if (!validate(name, email, password, mobileNo)) {
            LoginHelper loginHelper = new LoginHelper(getActivity(), this);
            mSignUpRequestModel = new SignUpRequestModel();
            mSignUpRequestModel.setMobileNumber(mobileNo);
            mSignUpRequestModel.setName(name);
            mSignUpRequestModel.setEmailId(email);
            mSignUpRequestModel.setPassword(password);

            //---- TODO: added for testing
            loginHelper.doSignUp(mSignUpRequestModel);
            /*Intent intentObj = new Intent(mContext, AppLoginConfirmationActivity.class);
            intentObj.putExtra(getString(R.string.type), getString(R.string.enter_otp));
            intentObj.putExtra(getString(R.string.details), signUpRequestModel);
            startActivity(intentObj);
            getActivity().finish();*/
            //----------
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(MyRescribeConstants.TASK_SIGN_UP)) {

            SignUpModel loginModel = (SignUpModel) customResponse;

            if (loginModel.getCommon().isSuccess()) {
                Intent intentObj = new Intent(mContext, AppLoginConfirmationActivity.class);
                intentObj.putExtra(getString(R.string.type), getString(R.string.enter_otp));
                intentObj.putExtra(getString(R.string.details), mSignUpRequestModel);
                startActivity(intentObj);
                getActivity().finish();
            } else {
                CommonMethods.showToast(getActivity(), loginModel.getCommon().getStatusMessage());
            }

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
}
