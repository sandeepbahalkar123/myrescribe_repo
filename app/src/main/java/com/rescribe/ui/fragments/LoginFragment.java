package com.rescribe.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.rescribe.R;
import com.rescribe.helpers.login.LoginHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.login.LoginModel;
import com.rescribe.model.login.LoginWithOtp;
import com.rescribe.model.login.PatientDetail;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.activities.AppGlobalContainerActivity;
import com.rescribe.ui.activities.HomePageActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment implements HelperResponse {

    @BindView(R.id.editTextMobileNo)
    EditText editTextMobileNo;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.btnOtp)
    CustomTextView btnOtp;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.forgotPasswordView)
    CustomTextView forgotPasswordView;
    @BindView(R.id.btnSignUp)
    Button signup;
    Unbinder unbinder;
    @BindView(R.id.loginUpWithFacebook)
    ImageView loginUpWithFacebook;
    @BindView(R.id.loginUpWithGmail)
    ImageView loginUpWithGmail;
    private OnFragmentInteractionListener mListener;
    private final String TAG = this.getClass().getName();

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    btnOtp.setAlpha(1f);
                    btnOtp.setEnabled(true);
                } else {
                    btnOtp.setAlpha(.5f);
                    btnOtp.setEnabled(false);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btnOtp, R.id.btn_login, R.id.forgotPasswordView, R.id.btnSignUp, R.id.loginUpWithFacebook, R.id.loginUpWithGmail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnOtp:
                //input mobile no and click on otp button ,TASK_LOGIN_WITH_OTP is used
                String mobile = editTextMobileNo.getText().toString();
                if (!validatePhoneNo(mobile)) {
                    LoginHelper loginHelper = new LoginHelper(getActivity(), this);
                    loginHelper.doLoginByOTP(mobile);
                }
                break;
            case R.id.btn_login:
                // input mobileNo and password on click of Login buttton , TASK_LOGIN is used
                String mobileNo = editTextMobileNo.getText().toString();
                String password = editTextPassword.getText().toString();
                if (!validate(mobileNo, password)) {
                    LoginHelper loginHelper = new LoginHelper(getActivity(), this);
                    loginHelper.doLogin(mobileNo, password);
                }
                break;
            case R.id.forgotPasswordView:
                // on click of forgotPassword
                Intent intentObj = new Intent(getActivity(), AppGlobalContainerActivity.class);
                intentObj.putExtra(getString(R.string.type), getString(R.string.forgot_password));
                intentObj.putExtra(getString(R.string.title), getString(R.string.forgot_password_header));
                startActivity(intentObj);
                break;
            case R.id.btnSignUp:
                //on click of signup , Signup fragment is loaded here.
                SignUpFragment signupFragment = new SignUpFragment();
                FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, signupFragment);
                fragmentTransaction.addToBackStack("SignUpFragment");
                fragmentTransaction.commit();
                break;
            case R.id.loginUpWithFacebook:
                //Onclick of Facebook button logic immplemented in LoginSignupActivity
                mListener.onClickFacebook(getString(R.string.log_in));
                break;
            case R.id.loginUpWithGmail:
                //Onclick of gmail button logic immplemented in LoginSignupActivity
                mListener.onClickGoogle(getString(R.string.log_in));
                break;
        }
    }

    //validation for mobileNo and password on click of Login Button
    private boolean validatePhoneNo(String mobile) {
        String message = null;
        String enter = getString(R.string.enter);
        if (mobile.isEmpty()) {
            message = enter + getString(R.string.enter_mobile_no).toLowerCase(Locale.US);
            editTextMobileNo.setError(message);
            editTextMobileNo.requestFocus();
        } else if ((mobile.trim().length() < 10) || !(mobile.trim().startsWith("7") || mobile.trim().startsWith("8") || mobile.trim().startsWith("9"))) {
            message = getString(R.string.err_invalid_mobile_no);
            editTextMobileNo.setError(message);
            editTextMobileNo.requestFocus();
        }
        if (message != null) {
            return true;
        } else {
            return false;
        }
    }

    //validation for mobileNo on click of otp Button
    private boolean validate(String mobileNo, String password) {
        String message = null;
        String enter = getString(R.string.enter);
        if (mobileNo.isEmpty()) {
            message = enter + getString(R.string.enter_mobile_no).toLowerCase(Locale.US);
            editTextMobileNo.setError(message);
            editTextMobileNo.requestFocus();
        } else if ((mobileNo.trim().length() < 10) || !(mobileNo.trim().startsWith("7") || mobileNo.trim().startsWith("8") || mobileNo.trim().startsWith("9"))) {
            message = getString(R.string.err_invalid_mobile_no);
            editTextMobileNo.setError(message);
            editTextMobileNo.requestFocus();
        } else if (password.isEmpty()) {
            message = enter + getString(R.string.enter_password).toLowerCase(Locale.US);
            editTextPassword.setError(message);
            editTextPassword.requestFocus();
        } else if (password.trim().length() < 8) {
            message = getString(R.string.error_too_small_password);
            editTextPassword.setError(message);
            editTextPassword.requestFocus();

        }
        return message != null;
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_LOGIN)) {
            //After login user navigated to HomepageActivity
            LoginModel loginModel = (LoginModel) customResponse;
            if (loginModel.getCommon().isSuccess()) {
                CommonMethods.Log(TAG + " Token", loginModel.getLoginData().getAuthToken());

                PatientDetail patientDetail = loginModel.getLoginData().getPatientDetail();

                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.AUTHTOKEN, loginModel.getLoginData().getAuthToken(), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.LOGIN_STATUS, RescribeConstants.YES, getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, String.valueOf(patientDetail.getPatientId()), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.MOBILE_NUMBER, patientDetail.getMobileNumber(), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.PASSWORD, editTextPassword.getText().toString(), getActivity());

                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.USER_NAME, patientDetail.getPatientName(), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.PROFILE_PHOTO, patientDetail.getPatientImgUrl(), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.USER_EMAIL, patientDetail.getPatientEmail(), getActivity());

                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.AGE, patientDetail.getPatientAge(), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.USER_GENDER, patientDetail.getPatientGender(), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.SALUTATION, "" + patientDetail.getPatientSalutation(), getActivity());

                Intent intent = new Intent(getActivity(), HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(RescribeConstants.APP_OPENING_FROM_LOGIN, true);
                startActivity(intent);
                getActivity().finish();
            } else {
                CommonMethods.showToast(getActivity(), loginModel.getCommon().getStatusMessage());
            }
        } else if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_LOGIN_WITH_OTP)) {
            //After login user navigated to HomepageActivity
            LoginWithOtp loginModel = (LoginWithOtp) customResponse;
            if (loginModel.getCommon().isSuccess()) {
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.MOBILE_NUMBER, editTextMobileNo.getText().toString(), getActivity());
                Intent intent = new Intent(getActivity(), AppGlobalContainerActivity.class);
                intent.putExtra(getString(R.string.type), getString(R.string.enter_otp_for_login));
                intent.putExtra(getString(R.string.title), getString(R.string.enter_otp_for_login));
                startActivity(intent);
            } else {
                CommonMethods.showToast(getActivity(), loginModel.getCommon().getStatusMessage());
            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        CommonMethods.showToast(getActivity(), errorMessage);
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(getActivity(), serverErrorMessage);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(getActivity(), serverErrorMessage);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onClickGoogle(String login);

        void onClickFacebook(String login);
    }
}
