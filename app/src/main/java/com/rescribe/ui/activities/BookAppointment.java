package com.rescribe.ui.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.rescribe.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 15/9/17.
 */

public class BookAppointment  extends AppCompatActivity {
    @BindView(R.id.bookAppointmentToolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.book_appointment));
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initialize();
    }

    private void initialize() {

    }
}
