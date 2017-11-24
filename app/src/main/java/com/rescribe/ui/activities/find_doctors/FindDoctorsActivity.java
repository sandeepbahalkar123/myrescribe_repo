package com.rescribe.ui.activities.find_doctors;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.book_appointment.ServicesAdapter;
import com.rescribe.adapters.find_doctors.FindDoctorsAdapter;
import com.rescribe.ui.customesViews.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 13/10/17.
 */

public class FindDoctorsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_group_photo)
    ImageView imgGroupPhoto;
    @BindView(R.id.toolbarTitle)
    CustomTextView toolbarTitle;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.listView)
    RecyclerView listView;

    /*@BindView(R.id.bookAppointmentLayout)
    LinearLayout bookAppointmentLayout;
    @BindView(R.id.consultOnline)
    LinearLayout consultOnline;*/
    @BindView(R.id.scroll)
    NestedScrollView scroll;
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    private FindDoctorsAdapter mFindDoctorsAdapter;
    private Context mContext;

    /*    @BindView(R.id.bookAppointmentLayout)
        LinearLayout bookAppointment;*/
 /*   @BindView(R.id.consultOnline)
    LinearLayout consultOnline;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_doctors_layout);
        ButterKnife.bind(this);
        mContext = FindDoctorsActivity.this;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbarTitle.setText(getIntent().getStringExtra(getString(R.string.toolbarTitle)));
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
/*
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(final AppBarLayout appBarLayout, int verticalOffset) {
                int i = verticalOffset + 400;
                listView.setPadding(0, i, 0, 0);
            }
        });*/
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
//            int spanCount = 2; // 3 columns
//            int spacing = 20; // 50px
//            boolean includeEdge = true;
//            listView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        mFindDoctorsAdapter = new FindDoctorsAdapter(mContext);
        listView.setAdapter(mFindDoctorsAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    //    @OnClick({/*R.id.bookAppointmentLayout,*//* R.id.consultOnline*/})
    public void onViewClicked(View view) {
        switch (view.getId()) {
           /* case R.id.bookAppointmentLayout:

                break;*/
           /* case R.id.consultOnline:
                Intent intent = new Intent(FindDoctorsActivity.this, DoctorConnectActivity.class);
                startActivity(intent);
                break;*/
        }
    }
}
