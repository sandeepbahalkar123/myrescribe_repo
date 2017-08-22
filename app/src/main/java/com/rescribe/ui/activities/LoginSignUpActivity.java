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
import android.util.Log;
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
import com.rescribe.model.requestmodel.login.SignUpRequestModel;
import com.rescribe.ui.fragments.LoginFragment;
import com.rescribe.ui.fragments.SignUpFragment;
import com.rescribe.util.CommonMethods;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by jeetal on 18/8/17.
 */
@RuntimePermissions
public class LoginSignUpActivity extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener, LoginFragment.OnFragmentInteractionListener, SignUpFragment.OnFragmentInteractionListener {
    private final String TAG = this.getClass().getName();
    @BindView(R.id.container)
    FrameLayout container;
    private Context mContext;
    Intent intent;
    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    private SignUpRequestModel mSignUpRequestModel;
    private static final int RC_SIGN_IN = 007;
    private static final int REQUEST_PERMISSIONS = 001;
    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup_layout);
        ButterKnife.bind(this);
        LoginSignUpActivityPermissionsDispatcher.askToReadMessageWithCheck(LoginSignUpActivity.this);

        googleInitialize();
        facebookInitialize();
        init();
    }

    private void init() {
        mContext = LoginSignUpActivity.this;
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
        } else if (requestCode == REQUEST_PERMISSIONS) {

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

    @Override
    public void onClickGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClickFacebook() {
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


}
