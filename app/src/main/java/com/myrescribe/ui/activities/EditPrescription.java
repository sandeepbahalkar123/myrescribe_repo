package com.myrescribe.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.adapters.CustomSpinAdapter;
import com.myrescribe.util.CommonMethods;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 11/5/17.
 */

public class EditPrescription extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private CustomSpinAdapter mCustomSpinAdapter;
    private String[] mArrayId;

    @BindView(R.id.spinnerType)
    Spinner mSpinnerType;

    @BindView(R.id.apply)
    TextView mApply;
    @BindView(R.id.reset)
    TextView mReset;

    @BindView(R.id.spinnerBreakfastDose)
    Spinner mSpinnerBreakfastDose;

    @BindView(R.id.spinnerLunchDose)
    Spinner mSpinnerLunchDose;

    @BindView(R.id.spinnerDinnerDose)
    Spinner mSpinnerDinnerDose;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_prescription_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        initialize();

    }

    private void initialize() {
        initializeVariables();
        bindView();
    }

    private void initializeVariables() {
        mContext = EditPrescription.this;
        mArrayId = getResources().getStringArray(R.array.ids);
    }

    private void bindView() {
        mApply.setOnClickListener(this);
        mReset.setOnClickListener(this);
        mSpinnerType.setOnItemSelectedListener(this);

        mCustomSpinAdapter = new CustomSpinAdapter(this, mArrayId, getResources().getStringArray(R.array.type));
        mSpinnerType.setAdapter(mCustomSpinAdapter);

        mCustomSpinAdapter = new CustomSpinAdapter(this, mArrayId, getResources().getStringArray(R.array.dose));
        mSpinnerBreakfastDose.setAdapter(mCustomSpinAdapter);

        mCustomSpinAdapter = new CustomSpinAdapter(this, mArrayId, getResources().getStringArray(R.array.dose));
        mSpinnerLunchDose.setAdapter(mCustomSpinAdapter);

        mCustomSpinAdapter = new CustomSpinAdapter(this, mArrayId, getResources().getStringArray(R.array.dose));
        mSpinnerDinnerDose.setAdapter(mCustomSpinAdapter);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //onclick on floating button
            case R.id.apply:
                CommonMethods.showDialog(getString(R.string.data_saved), mContext);
                break;
            case R.id.reset:
                CommonMethods.showDialog(getString(R.string.data__not_saved), mContext);
                break;
        }
    }


}
