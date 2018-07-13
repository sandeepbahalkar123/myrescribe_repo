package com.rescribe.ui.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.rescribe.R;
import com.rescribe.helpers.login.LoginHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.login.SignUpModel;
import com.rescribe.model.requestmodel.login.SignUpRequestModel;
import com.rescribe.ui.activities.AppGlobalContainerActivity;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rescribe.ui.fragments.OTPConfirmationForSignUp.SIGN_UP_DETAILS;
import static com.rescribe.util.RescribeConstants.GENDER;
import static com.rescribe.util.RescribeConstants.SALUTATION;

public class SocialSignUpInputFragment extends Fragment implements HelperResponse {

    @BindView(R.id.editTextName)
    EditText editTextName;

    @BindView(R.id.socialLoginMobileNo)
    EditText mSocialLoginMobileNo;

    @BindView(R.id.editTextAge)
    EditText editTextAge;

    @BindView(R.id.socialLoginPassword)
    EditText mSocialLoginPassword;
    @BindView(R.id.socialLoginEmail)
    EditText mSocialLoginEmail;
    @BindView(R.id.emailLayout)
    LinearLayout mEmailLayout;
    @BindView(R.id.submitBtn)
    Button mSubmitBtn;

    @BindView(R.id.salutationSpinner)
    Spinner salutationSpinner;

    @BindView(R.id.genderSpinner)
    Spinner genderSpinner;

    private SignUpRequestModel mSignUpRequestModel;
    private int salutationValue;
    private String genderValue;

    public SocialSignUpInputFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //For Both gmail and login signup this fragment is loaded , By setting isGmailLogin or is FacebookLogin true functinality of social media login  works
        View inflate = inflater.inflate(R.layout.social_login_confirm_mobileno_password, container, false);
        ButterKnife.bind(this, inflate);
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            mSignUpRequestModel = (SignUpRequestModel) arguments.getSerializable(SIGN_UP_DETAILS);
            if (mSignUpRequestModel.getEmailId() == null || mSignUpRequestModel.getEmailId().isEmpty())
                mEmailLayout.setVisibility(View.GONE);
            else {
                mSocialLoginEmail.setText(mSignUpRequestModel.getEmailId());
                mEmailLayout.setVisibility(View.VISIBLE);
            }

            editTextName.setText(mSignUpRequestModel.getName());

            ArrayAdapter genderSpinnerAdapter = new ArrayAdapter(getContext(), R.layout.signup_social_spinner_item, GENDER);
            genderSpinnerAdapter.setDropDownViewResource(R.layout.signup_social_spinner_item_view);
            genderSpinner.setAdapter(genderSpinnerAdapter);

            genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    genderValue = GENDER[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            ArrayAdapter salutationSpinnerAdapter = new ArrayAdapter(getContext(), R.layout.signup_social_spinner_item, RescribeConstants.SALUTATION_SOCIALMEDIA);
            salutationSpinnerAdapter.setDropDownViewResource(R.layout.signup_social_spinner_item_view);
            salutationSpinner.setAdapter(salutationSpinnerAdapter);

            salutationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    salutationValue = position + 1;
                    if (salutationValue == 1)
                        genderSpinner.setSelection(0);
                    else if (salutationValue == 2 || salutationValue == 3)
                        genderSpinner.setSelection(1);
                    else if (salutationValue == 4)
                        genderSpinner.setSelection(2);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
        return inflate;
    }

    /**
     * Return true if fields empty/validation failed, else false.
     *
     * @return
     */
    private boolean validate(String mobileNo, String password, String email, String age) {
        String message = null;
        String enter = getString(R.string.enter);
        if (mobileNo.isEmpty()) {
            message = enter + getString(R.string.enter_mobile_no).toLowerCase(Locale.US);
        } else if ((mobileNo.trim().length() < 10) || !(mobileNo.trim().startsWith("7") || mobileNo.trim().startsWith("8") || mobileNo.trim().startsWith("9"))) {
            message = getString(R.string.err_invalid_mobile_no);
        } else if (password.isEmpty()) {
            message = enter + getString(R.string.enter_password).toLowerCase(Locale.US);
        } else if (password.trim().length() < 8) {
            message = getString(R.string.error_too_small_password);
        } else if (mEmailLayout.getVisibility() == View.VISIBLE) {
            if (email.isEmpty()) {
                message = enter + getString(R.string.enter_email_id).toLowerCase(Locale.US);
            } else if (!CommonMethods.isValidEmail(email)) {
                message = enter + getString(R.string.err_email_invalid);
            }
        } else if (age.isEmpty()) {
            message = "Enter " + getString(R.string.enter_age);
        }
        if (message != null) {
            CommonMethods.showToast(getActivity(), message);
            return true;
        } else {
            return false;
        }
    }

    @OnClick({R.id.submitBtn})
    public void onButtonClicked(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.submitBtn:
                String email = mSocialLoginEmail.getText().toString();
                String password = mSocialLoginPassword.getText().toString();
                String mobileNo = mSocialLoginMobileNo.getText().toString();
                String age = editTextAge.getText().toString();

                if (!validate(mobileNo, password, email, age)) {
                    LoginHelper loginHelper = new LoginHelper(getActivity(), this);
                    mSignUpRequestModel.setMobileNumber(mobileNo);
                    mSignUpRequestModel.setEmailId(email);
                    mSignUpRequestModel.setPassword(password);
                    mSignUpRequestModel.setGender(genderValue);
                    mSignUpRequestModel.setSalutation(salutationValue);
                    mSignUpRequestModel.setAge(age);
                    mSignUpRequestModel.setAuthSocialToken(email);
                    loginHelper.doSignUp(mSignUpRequestModel);
                }
                break;
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_SIGN_UP)) {

            SignUpModel loginModel = (SignUpModel) customResponse;

            if (loginModel.getCommon().isSuccess()) {
                AppGlobalContainerActivity activity = (AppGlobalContainerActivity) getActivity();
                activity.loadFragment(getString(R.string.enter_otp), mSignUpRequestModel, getString(R.string.sign_up_confirmation));

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
