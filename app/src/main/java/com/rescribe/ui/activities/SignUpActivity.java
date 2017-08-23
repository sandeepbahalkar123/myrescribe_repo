package com.rescribe.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.rescribe.R;
import com.rescribe.helpers.login.LoginHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.login.SignUpModel;
import com.rescribe.model.requestmodel.login.SignUpRequestModel;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 18/8/17.
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, HelperResponse, GoogleApiClient.OnConnectionFailedListener {
    private final String TAG = this.getClass().getName();
    @BindView(R.id.editTextName)
    EditText editTextName;
    @BindView(R.id.editTextEmailID)
    EditText editTextEmailID;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.editTextMobileNo)
    EditText editTextMobileNo;
    @BindView(R.id.btnSignUp)
    Button btnSignUp;
    @BindView(R.id.login)
    CustomTextView login;
    @BindView(R.id.signUpWithFacebook)
    ImageView signUpWithFacebook;
    @BindView(R.id.signUpWithGoogle)
    ImageView signUpWithGoogle;
    private Context mContext;
    Intent intent;
    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    private SignUpRequestModel mSignUpRequestModel;
    private static final int RC_SIGN_IN = 007;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        ButterKnife.bind(this);
        googleInitialize();
        facebookInitialize();
        init();

    }

    private void init() {
        mContext = SignUpActivity.this;
    }

    private boolean validate(String name, String email, String password, String mobileNo) {
        String message = null;
        String enter = getString(R.string.enter);
        if (name.isEmpty()) {
            message = enter + getString(R.string.enter_full_name).toLowerCase(Locale.US);
            editTextName.setError(message);
            editTextName.requestFocus();
        } else if (email.isEmpty()) {
            message = enter + getString(R.string.enter_email_id).toLowerCase(Locale.US);
            editTextEmailID.setError(message);
            editTextEmailID.requestFocus();

        } else if (!CommonMethods.isValidEmail(email)) {
            message = getString(R.string.err_email_invalid);
            editTextEmailID.setError(message);
            editTextEmailID.requestFocus();

        } else if (password.isEmpty()) {
            message = enter + getString(R.string.enter_password).toLowerCase(Locale.US);
            editTextPassword.setError(message);
            editTextPassword.requestFocus();

        } else if (password.trim().length() < 8) {
            message = getString(R.string.error_too_small_password);
            editTextPassword.setError(message);
            editTextPassword.requestFocus();

        } else if (mobileNo.isEmpty()) {
            message = enter + getString(R.string.enter_mobile_no).toLowerCase(Locale.US);
            editTextMobileNo.setError(message);
            editTextMobileNo.requestFocus();

        } else if ((mobileNo.trim().length() < 10) || !(mobileNo.trim().startsWith("7") || mobileNo.trim().startsWith("8") || mobileNo.trim().startsWith("9"))) {

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
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_SIGN_UP)) {

            SignUpModel loginModel = (SignUpModel) customResponse;

            if (loginModel.getCommon().isSuccess()) {
                if (loginModel.getCommon().getStatusMessage().equalsIgnoreCase(getString(R.string.profile_exists))) {
                    CommonMethods.showToast(this, loginModel.getCommon().getStatusMessage());
                } else {
                    Intent intentObj = new Intent(mContext, AppGlobalContainerActivity.class);
                    intentObj.putExtra(getString(R.string.type), getString(R.string.enter_otp));
                    intentObj.putExtra(getString(R.string.title), getString(R.string.sign_up_confirmation));
                    intentObj.putExtra(getString(R.string.details), mSignUpRequestModel);
                    startActivity(intentObj);
                }
            } else {
                CommonMethods.showToast(this, loginModel.getCommon().getStatusMessage());
            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        CommonMethods.showToast(this, errorMessage);

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(this, serverErrorMessage);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(this, serverErrorMessage);
    }

    @OnClick(R.id.btnSignUp)
    public void onViewClicked() {
        String name = editTextName.getText().toString();
        String email = editTextEmailID.getText().toString();
        String password = editTextPassword.getText().toString();
        String mobileNo = editTextMobileNo.getText().toString();
        if (!validate(name, email, password, mobileNo)) {
            LoginHelper loginHelper = new LoginHelper(this, this);
            mSignUpRequestModel = new SignUpRequestModel();
            mSignUpRequestModel.setMobileNumber(mobileNo);
            mSignUpRequestModel.setName(name);
            mSignUpRequestModel.setEmailId(email);
            mSignUpRequestModel.setPassword(password);
            loginHelper.doSignUp(mSignUpRequestModel);
        }
    }

    @OnClick(R.id.login)
    public void onLoginClicked() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @OnClick({R.id.signUpWithFacebook, R.id.signUpWithGoogle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.signUpWithFacebook:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_friends", "email", "public_profile"));
                break;
            case R.id.signUpWithGoogle:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
        }
    }

    private void facebookInitialize() {
        FacebookSdk.sdkInitialize(this);
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(
                mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        requestUserInfo(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        //Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        // TODO Auto-generated method stub
                    }
                });
    }

    private void googleInitialize() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void requestUserInfo(AccessToken accessToken) {
        String FIELDS = "fields";
        String ID = "id";
        String NAME = "name";
        String PICTURE = "picture";
        String EMAIL = "email";
        String BIRTHDAY = "birthday";
        String GENDER = "gender";
        String KEY_USERNAME = "email_address";
        String KEY_PASSWORD = "password";
        String REQUEST_FIELDS = TextUtils.join(",", new String[]{ID, NAME, PICTURE, EMAIL, BIRTHDAY, GENDER});

        GraphRequest request = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {
                        // TODO Auto-generated method stub

                        JSONObject json = response.getJSONObject();
                        CommonMethods.Log("requestUserInfo", json.toString());

                        //-----------
                        SignUpRequestModel signUpRequest = new SignUpRequestModel();
                        signUpRequest.setMobileNumber(null);
                        signUpRequest.setName(json.optString("name"));
                        signUpRequest.setEmailId(json.optString("email"));
                        signUpRequest.setPassword(null);
                        //-----------
                        Intent intentObj = new Intent(mContext, AppGlobalContainerActivity.class);
                        intentObj.putExtra(getString(R.string.type), getString(R.string.login_social_media));
                        intentObj.putExtra(getString(R.string.details), signUpRequest);
                        intentObj.putExtra(getString(R.string.title), getString(R.string.sign_up_confirmation));
                        startActivity(intentObj);
                        finish();

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString(FIELDS, REQUEST_FIELDS);
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if (requestCode != RC_SIGN_IN) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        CommonMethods.Log(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            //-----------
            SignUpRequestModel signUpRequest = new SignUpRequestModel();
            signUpRequest.setMobileNumber(null);
            signUpRequest.setName(acct.getDisplayName());
            signUpRequest.setEmailId(acct.getEmail());
            signUpRequest.setPassword(null);
            //-----------
            Intent intentObj = new Intent(mContext, AppGlobalContainerActivity.class);
            intentObj.putExtra(getString(R.string.type), getString(R.string.login_social_media));
            intentObj.putExtra(getString(R.string.details), signUpRequest);
            intentObj.putExtra(getString(R.string.title), getString(R.string.sign_up_confirmation));
            startActivity(intentObj);
            finish();

        } else {
            // Signed out, show unauthenticated UI.

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }
}
