package com.myrescribe.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.myrescribe.R;
import com.myrescribe.helpers.login.LoginHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.login.SignUpModel;
import com.myrescribe.model.requestmodel.login.SignUpRequestModel;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.ui.activities.AppLoginConfirmationActivity;
import com.myrescribe.ui.activities.PhoneNoActivity;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SignUp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUp extends Fragment implements HelperResponse, GoogleApiClient.OnConnectionFailedListener {

    private Context mContext;

    private final String TAG = this.getClass().getName();
    @BindView(R.id.editTextFullName)
    EditText mFullName;
    @BindView(R.id.editTextEmailId)
    EditText mEmailId;
    @BindView(R.id.editTextPassword)
    EditText mPassword;
    @BindView(R.id.editTextMobileNo)
    EditText mMobileNo;
    @BindView(R.id.signUpFacebookLogin)
    ImageView mFacebookLogin;
    @BindView(R.id.signUpGMailLogin)
    ImageView mGMailLogin;
    private SignUpRequestModel mSignUpRequestModel;

    //----
    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;

    //----
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.sign_up, container, false);
        ButterKnife.bind(this, inflate);
        this.mContext = getActivity();
        googleInitialize();
        facebookInitialize();
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
        } else if (password.trim().length() < 8) {
            message = getString(R.string.error_too_small_password);
        } else if (mobileNo.isEmpty()) {
            message = enter + getString(R.string.enter_mobile_no).toLowerCase(Locale.US);
        } else if (mobileNo.trim().length() < 10) {
            message = getString(R.string.err_invalid_mobile_no);
        }
        if (message != null) {
            CommonMethods.showSnack(mMobileNo, message);
            return true;
        } else {
            return false;
        }
    }

    @OnClick({R.id.btn_SignUp, R.id.signUpFacebookLogin, R.id.signUpGMailLogin})
    public void onButtonClicked(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.signUpFacebookLogin:
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("user_friends", "email", "public_profile"));
                break;
            case R.id.signUpGMailLogin:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                getActivity().startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.btn_SignUp:
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

                    loginHelper.doSignUp(mSignUpRequestModel);

                }
                break;
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

    //-------
    private void googleInitialize() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void facebookInitialize() {
        FacebookSdk.sdkInitialize(this.getContext());
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
                        Intent intentObj = new Intent(mContext, AppLoginConfirmationActivity.class);
                        intentObj.putExtra(getString(R.string.type), getString(R.string.login_social_media));
                        intentObj.putExtra(getString(R.string.details), signUpRequest);
                        startActivity(intentObj);
                        getActivity().finish();

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
            String personName = acct.getDisplayName();

            MyRescribePreferencesManager.putString(MyRescribeConstants.USERNAME, personName, mContext);
            Intent intent = new Intent(getActivity(), PhoneNoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else {
            // Signed out, show unauthenticated UI.

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }
    //-------
}
