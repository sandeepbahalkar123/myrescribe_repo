package com.myrescribe.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import com.myrescribe.R;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.util.MyRescribeConstants;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 30/5/17.
 */

public class PhoneNoActivity extends AppCompatActivity implements View.OnClickListener {
    public final String TAG = getClass().getName();
    Context mContext;

    @BindView(R.id.editTextUserPhoneNumber)
    EditText mEditTextUserPhoneNumber;

    @BindView(R.id.buttonOk)
    AppCompatButton mButtonOk;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_no_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        initialize();

    }

    private void initialize() {
        initializeVariables();
        bindView();
    }

    private void initializeVariables() {
        mContext = PhoneNoActivity.this;

    }

    private void bindView() {
        mButtonOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.buttonOk:
                String phoneNumber = mEditTextUserPhoneNumber.getText().toString();
                MyRescribePreferencesManager.putString(MyRescribeConstants.PHONE, phoneNumber, mContext);
                Intent intent = new Intent(PhoneNoActivity.this, ShowMedicineDoseListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }

    }
}
