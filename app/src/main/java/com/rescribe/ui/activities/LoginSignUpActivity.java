package com.rescribe.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.FrameLayout;

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
import com.rescribe.model.login.LoginModel;
import com.rescribe.model.login.PatientDetail;
import com.rescribe.model.login.SocialLoginRequestModel;
import com.rescribe.model.requestmodel.login.SignUpRequestModel;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.fragments.LoginFragment;
import com.rescribe.ui.fragments.SignUpFragment;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

import static com.rescribe.ui.fragments.OTPConfirmationForSignUp.SIGN_UP_DETAILS;
import static com.rescribe.util.RescribeConstants.FACEBOOK;
import static com.rescribe.util.RescribeConstants.GMAIL;

/**
 * Created by jeetal on 18/8/17.
 */
@RuntimePermissions
public class LoginSignUpActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, LoginFragment.OnFragmentInteractionListener, SignUpFragment.OnFragmentInteractionListener, HelperResponse {
    private final String TAG = this.getClass().getName();
    @BindView(R.id.container)
    FrameLayout container;
    private Context mContext;
    Intent intent;
    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    private LoginFragment loginFragment;
    private SignUpRequestModel signUpRequestSocial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup_layout);
        ButterKnife.bind(this);
        LoginSignUpActivityPermissionsDispatcher.askToReadMessageWithCheck(LoginSignUpActivity.this);
        String key = CommonMethods.printKeyHash(LoginSignUpActivity.this);
        CommonMethods.Log(TAG, key);
        // Code for facebook and gmail login for both signup and login fragment is written in LoginSignUpActivity
        googleInitialize();
        facebookInitialize();
        init();
    }

    private void init() {
        mContext = LoginSignUpActivity.this;
        //Fragment  login is loaded in LoginSignUpActivity , Facebook and google Login click is handled in LoginSignUpActivity
        loginFragment = new LoginFragment();
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, loginFragment);
        fragmentTransaction.commit();
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

    //Request to facebook for user info
    private void requestUserInfo(final AccessToken accessToken) {
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

                        // logout facebook session
                        LoginManager.getInstance().logOut();

                        JSONObject json = response.getJSONObject();

                        if (object != null)
                            CommonMethods.Log("requestUserInfo", object.toString());
                        CommonMethods.Log("requestUserInfo", json.toString() + "\n" + accessToken.getToken() + "\n" + accessToken.getUserId() + "\n" + accessToken.getApplicationId());

                        signUpRequestSocial = new SignUpRequestModel();
                        signUpRequestSocial.setMobileNumber(null);
                        signUpRequestSocial.setName(json.optString("name"));
                        signUpRequestSocial.setEmailId(json.optString("email"));
                        signUpRequestSocial.setPassword(null);
                        signUpRequestSocial.setAuthSocialType(FACEBOOK);
                        String gender = json.optString("gender").toUpperCase();
                        signUpRequestSocial.setGender(gender);

                        if (RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.LOGIN_OR_SIGNUP, mContext).equalsIgnoreCase(getString(R.string.sign_up))) {

                            Intent intentObj = new Intent(mContext, AppGlobalContainerActivity.class);
                            intentObj.putExtra(getString(R.string.type), getString(R.string.login_with_facebook));
                            intentObj.putExtra(SIGN_UP_DETAILS, signUpRequestSocial);
                            intentObj.putExtra(getString(R.string.title), getString(R.string.sign_up_confirmation));
                            startActivity(intentObj);

                        } else if (RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.LOGIN_OR_SIGNUP, mContext).equalsIgnoreCase(getString(R.string.log_in))) {

                            SocialLoginRequestModel signUpRequest = new SocialLoginRequestModel();
                            signUpRequest.setAuthSocialToken(json.optString("email"));
                            signUpRequest.setAuthSocialType(FACEBOOK);
                            signUpRequest.setName(json.optString("name"));

                            LoginHelper loginHelper = new LoginHelper(mContext, LoginSignUpActivity.this);
                            loginHelper.doLoginBySocial(signUpRequest);

                        }

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
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Request to Google for user info
    private void handleSignInResult(GoogleSignInResult result) {
        CommonMethods.Log(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            CommonMethods.Log(TAG, "display name: " + acct.getDisplayName());

            signUpRequestSocial = new SignUpRequestModel();
            signUpRequestSocial.setMobileNumber(null);
            signUpRequestSocial.setName(acct.getDisplayName());
            signUpRequestSocial.setEmailId(acct.getEmail());
            signUpRequestSocial.setPassword(null);
            signUpRequestSocial.setAuthSocialType(GMAIL);
            signUpRequestSocial.setAuthSocialToken(acct.getEmail());

            if (RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.LOGIN_OR_SIGNUP, mContext).equalsIgnoreCase(getString(R.string.sign_up))) {
                //-----------
                Intent intentObj = new Intent(mContext, AppGlobalContainerActivity.class);
                intentObj.putExtra(getString(R.string.type), getString(R.string.login_with_gmail));
                intentObj.putExtra(SIGN_UP_DETAILS, signUpRequestSocial);
                intentObj.putExtra(getString(R.string.title), getString(R.string.sign_up_confirmation));
                startActivity(intentObj);

            } else if (RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.LOGIN_OR_SIGNUP, mContext).equalsIgnoreCase(getString(R.string.log_in))) {
                // call api for social login auth from our server

                SocialLoginRequestModel signUpRequest = new SocialLoginRequestModel();
                signUpRequest.setAuthSocialToken(acct.getEmail());
                signUpRequest.setAuthSocialType(GMAIL);
                signUpRequest.setName(acct.getDisplayName());

                LoginHelper loginHelper = new LoginHelper(mContext, this);
                loginHelper.doLoginBySocial(signUpRequest);
            }


        } else {
            // Signed out, show unauthenticated UI.

        }

        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onClickGoogle(String loginOrSignup) {
        RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.LOGIN_OR_SIGNUP, loginOrSignup, mContext);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClickFacebook(String loginOrSignup) {
        RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.LOGIN_OR_SIGNUP, loginOrSignup, mContext);
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_friends", "email", "public_profile"));
    }

    @NeedsPermission(Manifest.permission.READ_SMS)
    public void askToReadMessage() {
        //Do nothing
    }

    @OnPermissionDenied({Manifest.permission.READ_SMS})
    void deniedReadSms() {
        //Do nothing
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LoginSignUpActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        // User can login through gmail or facebook
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_LOGIN)) {

            LoginModel loginModel = (LoginModel) customResponse;
            if (loginModel.getCommon().isSuccess()) {
                CommonMethods.Log(TAG + " Token", loginModel.getLoginData().getAuthToken());
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.AUTHTOKEN, loginModel.getLoginData().getAuthToken(), mContext);

                PatientDetail patientDetail = loginModel.getLoginData().getPatientDetail();

                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.LOGIN_STATUS, RescribeConstants.YES, mContext);
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, String.valueOf(patientDetail.getPatientId()), mContext);
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.MOBILE_NUMBER, patientDetail.getMobileNumber(), mContext);

                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.USER_NAME, patientDetail.getPatientName(), mContext);
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.PROFILE_PHOTO, patientDetail.getPatientImgUrl(), mContext);
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.USER_EMAIL, patientDetail.getPatientEmail(), mContext);

                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.AGE, patientDetail.getPatientAge(), mContext);
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.USER_GENDER, patientDetail.getPatientGender(), mContext);
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.SALUTATION, ""+patientDetail.getPatientSalutation(), mContext);

                if (RescribePreferencesManager.getString(RescribeConstants.TYPE_OF_LOGIN, mContext).equalsIgnoreCase(getString(R.string.login_with_facebook))) {
                    RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.MOBILE_NUMBER, RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.MOBILE_NUMBER_FACEBOOK, mContext), mContext);
                    RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.PASSWORD, RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PASSWORD_FACEBOOK, mContext), mContext);
                }
                if (RescribePreferencesManager.getString(RescribeConstants.TYPE_OF_LOGIN, mContext).equalsIgnoreCase(getString(R.string.login_with_gmail))) {
                    RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.MOBILE_NUMBER, RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.MOBILE_NUMBER_GMAIL, mContext), mContext);
                    RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.PASSWORD, RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PASSWORD_GMAIL, mContext), mContext);
                }

                Intent intent = new Intent(this, HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                CommonMethods.showToast(mContext, loginModel.getCommon().getStatusMessage());
            }
        } else if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_LOGIN_WITH_SOCIAL)) {
            LoginModel loginModel = (LoginModel) customResponse;
            if (loginModel.getCommon().isSuccess()) {
                CommonMethods.Log(TAG + " Token", loginModel.getLoginData().getAuthToken());
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.AUTHTOKEN, loginModel.getLoginData().getAuthToken(), mContext);

                PatientDetail patientDetail = loginModel.getLoginData().getPatientDetail();

                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.LOGIN_STATUS, RescribeConstants.YES, mContext);
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, String.valueOf(patientDetail.getPatientId()), mContext);
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.MOBILE_NUMBER, patientDetail.getMobileNumber(), mContext);

                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.USER_NAME, patientDetail.getPatientName(), mContext);
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.PROFILE_PHOTO, patientDetail.getPatientImgUrl(), mContext);
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.USER_EMAIL, patientDetail.getPatientEmail(), mContext);

                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.AGE, patientDetail.getPatientAge(), mContext);
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.USER_GENDER, patientDetail.getPatientGender(), mContext);
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.SALUTATION, ""+patientDetail.getPatientSalutation(), mContext);

                if (RescribePreferencesManager.getString(RescribeConstants.TYPE_OF_LOGIN, mContext).equalsIgnoreCase(getString(R.string.login_with_facebook))) {
                    RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.MOBILE_NUMBER, RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.MOBILE_NUMBER_FACEBOOK, mContext), mContext);
                    RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.PASSWORD, RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PASSWORD_FACEBOOK, mContext), mContext);
                }
                if (RescribePreferencesManager.getString(RescribeConstants.TYPE_OF_LOGIN, mContext).equalsIgnoreCase(getString(R.string.login_with_gmail))) {
                    RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.MOBILE_NUMBER, RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.MOBILE_NUMBER_GMAIL, mContext), mContext);
                    RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.PASSWORD, RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PASSWORD_GMAIL, mContext), mContext);
                }
                Intent intent = new Intent(this, HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(RescribeConstants.APP_OPENING_FROM_LOGIN, true);
                startActivity(intent);
                finish();
            } else if (loginModel.getCommon().getStatusCode().equals(404)) {
                // signUp call

                Intent intentObj = new Intent(mContext, AppGlobalContainerActivity.class);
                intentObj.putExtra(getString(R.string.type), signUpRequestSocial.getAuthSocialType().equalsIgnoreCase(GMAIL) ? getString(R.string.login_with_gmail) : getString(R.string.login_with_facebook));
                intentObj.putExtra(SIGN_UP_DETAILS, signUpRequestSocial);
                intentObj.putExtra(getString(R.string.title), getString(R.string.sign_up_confirmation));
                startActivity(intentObj);

            } else {
                CommonMethods.showToast(mContext, loginModel.getCommon().getStatusMessage());
            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        CommonMethods.Log(TAG, errorMessage);

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.Log(TAG, serverErrorMessage);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.Log(TAG, serverErrorMessage);
    }
}
