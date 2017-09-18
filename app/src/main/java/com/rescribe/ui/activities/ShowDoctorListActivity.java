package com.rescribe.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.rescribe.R;

import butterknife.ButterKnife;

/**
 * Created by jeetal on 15/9/17.
 */
public class ShowDoctorListActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
    }
}
