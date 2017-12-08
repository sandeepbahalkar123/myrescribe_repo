package com.rescribe.ui.activities.book_appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rescribe.R;
import com.rescribe.ui.fragments.book_appointment.BookAppointDoctorDescriptionFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 8/12/17.
 */

public class CoachMarkGetTokenActivity extends AppCompatActivity {
    @BindView(R.id.coachmark)
    ImageView coachmark;
    @BindView(R.id.coachmarkLayout)
    LinearLayout coachmarkLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_mark_activity);
        ButterKnife.bind(this);
        initialize();

    }

    private void initialize() {

    }

    @OnClick({R.id.coachmark, R.id.coachmarkLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.coachmark:
                break;
            case R.id.coachmarkLayout:
                Intent intent = new Intent(CoachMarkGetTokenActivity.this,DoctorDescriptionBaseActivity.class);
                startActivity(intent);
                break;
        }
    }
}
