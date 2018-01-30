package com.rescribe.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.rescribe.R;
import com.rescribe.helpers.login.LoginHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.login.ForgetPassPatientDetail;
import com.rescribe.model.login.ForgetPasswordModel;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.activities.AppGlobalContainerActivity;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rescribe.ui.activities.AppGlobalContainerActivity.FORGET_PASSWORD;
import static com.rescribe.util.RescribeConstants.FROM;

public class ForgotPassword extends Fragment implements HelperResponse {

    @BindView(R.id.editTextMobileNo)
    EditText editTextMobileNo;

    public ForgotPassword() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.forgot_password, container, false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    /**
     * Return true if fields empty/validation failed, else false.
     *
     * @return
     */
    private boolean validate(String email) {
        String message = null;
        String enter = getString(R.string.enter);

        if (email.isEmpty()) {
            message = enter + getString(R.string.enter_email_id).toLowerCase(Locale.US);
        } else if (!CommonMethods.isValidEmail(email)) {
            message = getString(R.string.err_email_invalid);
        }

        if (message != null) {
            CommonMethods.showToast(getActivity(), message);
            return true;
        } else {
            return false;
        }
    }

    @OnClick({R.id.forgotPasswordSubmit})
    public void onButtonClicked(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.forgotPasswordSubmit:
                //input mobile no and click on otp button ,TASK_LOGIN_WITH_OTP is used
                String mobile = editTextMobileNo.getText().toString();
                if (!validatePhoneNo(mobile)) {
                    LoginHelper loginHelper = new LoginHelper(getContext(), this);
                    loginHelper.forgetPassword(mobile);
                }
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

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_FORGOT_PASS_WITH_OTP)) {
            //After login user navigated to HomepageActivity
            ForgetPasswordModel forgetPasswordModel = (ForgetPasswordModel) customResponse;
            if (forgetPasswordModel.getCommon().isSuccess()) {

                ForgetPassPatientDetail patientDetail = forgetPasswordModel.getData().getPatientDetail();

                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER, patientDetail.getPatientPhon(), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, String.valueOf(patientDetail.getId()), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PASSWORD, patientDetail.getPassword(), getActivity());

                Intent intent = new Intent(getActivity(), AppGlobalContainerActivity.class);
                intent.putExtra(getString(R.string.type), getString(R.string.enter_otp_for_login));
                intent.putExtra(getString(R.string.title), getString(R.string.enter_otp_for_login));
                intent.putExtra(FROM, FORGET_PASSWORD);
                startActivity(intent);
            } else {
                CommonMethods.showToast(getActivity(), forgetPasswordModel.getCommon().getStatusMessage());
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
