package com.myrescribe.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.ui.activities.PhoneNoActivity;
import com.myrescribe.util.MyRescribeConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LogInApp.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LogInApp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInApp extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final String TAG = this.getClass().getName();

    Context mContext;
    private GoogleApiClient mGoogleApiClient;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.editTextMobileNo)
    EditText mMobileNo;
    @BindView(R.id.editTextPassword)
    EditText mPassword;

    private static final int RC_SIGN_IN = 007;
    private CallbackManager mCallbackManager;

    public LogInApp() {
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
    public static LogInApp newInstance(String param1, String param2) {
        LogInApp fragment = new LogInApp();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.log_in, container, false);
        ButterKnife.bind(this, inflate);
        mContext = this.getContext();

        googleInitialize();
        facebookInitialize();
        return inflate;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void googleInitialize() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        }
//    }


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
        Log.e(TAG, "handleSignInResult:" + result.isSuccess());
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


    @OnClick({R.id.btn_login, R.id.buttonLoginFacebook, R.id.buttonLoginGPlus})
    public void onButtonClicked(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.buttonLoginFacebook:
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("user_friends", "email", "public_profile"));
//                FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.container, new EnterPhoneNoToGenerateOTP());
//                fragmentTransaction.commit();
                break;
            case R.id.buttonLoginGPlus:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                getActivity().startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.btn_login:
                String userName = mMobileNo.getText().toString();
                String emailId = mPassword.getText().toString();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
                        try {
                            Log.e("requestUserInfo", json.toString());

                            String name = json.getString("name");
                            Log.e("name", name);
                            MyRescribePreferencesManager.putString(MyRescribeConstants.USERNAME, name, mContext);
//                            Intent intent = new Intent(getActivity(), PhoneNoActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);

                            FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
//                            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,
//                                    R.anim.slide_out_left);
                            fragmentTransaction.replace(R.id.container, new EnterPhoneNoToGenerateOTP());
                            fragmentTransaction.commit();

                            //renderView(json);
                        } catch (JSONException e) {
                            e.printStackTrace();


                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString(FIELDS, REQUEST_FIELDS);
        request.setParameters(parameters);
        request.executeAsync();
    }

}
