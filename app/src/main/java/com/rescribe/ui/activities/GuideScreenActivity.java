package com.rescribe.ui.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.rescribe.R;
import com.rescribe.adapters.guide_pager.SliderAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GuideScreenActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout indicator;
    Button skipButton;
    Button gettingStartedButton;

    private List<Integer> images;
    private List<String> descriptions;
    private List<String> title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_screen);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (TabLayout) findViewById(R.id.indicator);
        skipButton = (Button) findViewById(R.id.skipButton);
        gettingStartedButton = (Button) findViewById(R.id.gettingStartedButton);
        images = new ArrayList<>();
        images.add(R.drawable.guide_calendar);
        images.add(R.drawable.guide_reminder);
        images.add(R.drawable.guide_repository);
        images.add(R.drawable.guide_vital_graph);
        images.add(R.drawable.guide_doctor_connect);

        title = new ArrayList<>();
        title.add(getString(R.string.book_appointment));
        title.add(getString(R.string.health_reminder));
        title.add(getString(R.string.health_repository));
        title.add(getString(R.string.vital_graph));
        title.add(getString(R.string.doctor_connect));

        descriptions = new ArrayList<>();
        descriptions.add(getString(R.string.guide_book_appointment));
        descriptions.add(getString(R.string.guide_health_reminder));
        descriptions.add(getString(R.string.guide_health_repository));
        descriptions.add(getString(R.string.guide_vital_graph));
        descriptions.add(getString(R.string.guide_doctor_connect));

        viewPager.setAdapter(new SliderAdapter(this, images, title, descriptions));
        indicator.setupWithViewPager(viewPager, true);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == title.size() - 1){
                    gettingStartedButton.setVisibility(View.VISIBLE);
                    skipButton.setVisibility(View.GONE);
                } else {
                    gettingStartedButton.setVisibility(View.GONE);
                    skipButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gettingStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            GuideScreenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < images.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }

    }
}
