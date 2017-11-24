package com.rescribe.ui.activities.find_doctors;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
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
import com.rescribe.adapters.find_doctors.FindDoctorsAdapter;
import com.rescribe.adapters.find_doctors.RecentlyVisitedDoctors;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.dashboard_api.DashBoardBaseModel;
import com.rescribe.model.dashboard_api.DashboardDataModel;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;

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
    ArrayList<DoctorList> sponsered;
    ArrayList<DoctorList> recently_visit_doctor;
    ArrayList<DoctorList> favoriteList;

    /*@BindView(R.id.bookAppointmentLayout)
    LinearLayout bookAppointmentLayout;
    @BindView(R.id.consultOnline)
    LinearLayout consultOnline;*/

    @BindView(R.id.serviceNameTextView)
    CustomTextView serviceNameTextView;
    @BindView(R.id.showVitalUnitNameIconLayout)
    LinearLayout showVitalUnitNameIconLayout;
    @BindView(R.id.serviceIcon)
    ImageView serviceIcon;
    @BindView(R.id.bookAppointmentLayout)
    LinearLayout bookAppointmentLayout;
    @BindView(R.id.recentlyViewPager)
    ViewPager recentlyViewPager;
    @BindView(R.id.recentlyVisitedFindDoctorLayout)
    LinearLayout recentlyVisitedFindDoctorLayout;
    @BindView(R.id.sponseredViewPager)
    ViewPager sponseredViewPager;
    @BindView(R.id.sponseredFindDoctorLayout)
    LinearLayout sponseredFindDoctorLayout;
    @BindView(R.id.favoriteViewPager)
    ViewPager favoriteViewPager;
    @BindView(R.id.favoriteFindDoctorLayout)
    LinearLayout favoriteFindDoctorLayout;
    @BindView(R.id.scroll)
    NestedScrollView scroll;
    private FindDoctorsAdapter mFindDoctorsAdapter;
    private Context mContext;
    DashboardDataModel mDashboardDataModel;
    private RecentlyVisitedDoctors mRecentlyVisitedDoctors;
    private ArrayList<DoctorList> mSponseredAdapterList;
    private ArrayList<DoctorList> mRecentVisitAdapterList;
    private ArrayList<DoctorList> mFavouriteAdapterList;

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

        mDashboardDataModel = getIntent().getExtras().getParcelable(getString(R.string.doctor_data));
        sponsered = getIntent().getExtras().getParcelableArrayList(getString(R.string.sponsered_doctor));
        recently_visit_doctor = getIntent().getExtras().getParcelableArrayList(getString(R.string.recently_visit_doctor));
        favoriteList = getIntent().getExtras()
                .getParcelableArrayList(getString(R.string.favorite));
        toolbarTitle.setText(getIntent().getStringExtra(getString(R.string.toolbarTitle)));
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
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
        setUpViewPager();
    }

    private void setUpViewPager() {
        int pager_padding = getResources().getDimensionPixelSize(R.dimen.pager_padding);
        int pager_margin = getResources().getDimensionPixelSize(R.dimen.pager_margin);
///This done because atleast 3 objects should be visible on FindDoctors Page.
        if(sponsered.size()>3){
            mSponseredAdapterList = new ArrayList<>();
            for(int i = 0;i<3;i++){
                mSponseredAdapterList.add(sponsered.get(i));
            }
            mRecentlyVisitedDoctors = new RecentlyVisitedDoctors(this,mSponseredAdapterList);
            sponseredViewPager.setAdapter(mRecentlyVisitedDoctors);
            sponseredViewPager.setClipToPadding(false);
            sponseredViewPager.setPadding(pager_padding, 0, pager_padding, 0);
            sponseredViewPager.setPageMargin(pager_margin);
        }else{
            mRecentlyVisitedDoctors = new RecentlyVisitedDoctors(this,sponsered);
            sponseredViewPager.setAdapter(mRecentlyVisitedDoctors);
            sponseredViewPager.setClipToPadding(false);
            sponseredViewPager.setPadding(pager_padding, 0, pager_padding, 0);
            sponseredViewPager.setPageMargin(pager_margin);
        }

        if(recently_visit_doctor.size()>3){
            mRecentVisitAdapterList = new ArrayList<>();
            for(int i = 0;i<3;i++){
                mRecentVisitAdapterList.add(recently_visit_doctor.get(i));
            }
            mRecentlyVisitedDoctors = new RecentlyVisitedDoctors(this,mRecentVisitAdapterList);
            recentlyViewPager.setAdapter(mRecentlyVisitedDoctors);
            recentlyViewPager.setClipToPadding(false);
            recentlyViewPager.setPadding(pager_padding, 0, pager_padding, 0);
            recentlyViewPager.setPageMargin(pager_margin);
        }else{
            mRecentlyVisitedDoctors = new RecentlyVisitedDoctors(this,recently_visit_doctor);
            recentlyViewPager.setAdapter(mRecentlyVisitedDoctors);
            recentlyViewPager.setClipToPadding(false);
            recentlyViewPager.setPadding(pager_padding, 0, pager_padding, 0);
            recentlyViewPager.setPageMargin(pager_margin);
        }

        if(favoriteList.size()>3){
            mFavouriteAdapterList = new ArrayList<>();
            for(int i = 0;i<3;i++){
                mFavouriteAdapterList.add(favoriteList.get(i));
            }
            mRecentlyVisitedDoctors = new RecentlyVisitedDoctors(this,mFavouriteAdapterList);
            favoriteViewPager.setAdapter(mRecentlyVisitedDoctors);
            favoriteViewPager.setClipToPadding(false);
            favoriteViewPager.setPadding(pager_padding, 0, pager_padding, 0);
            favoriteViewPager.setPageMargin(pager_margin);
        }else{
            mRecentlyVisitedDoctors = new RecentlyVisitedDoctors(this,favoriteList);
            favoriteViewPager.setAdapter(mRecentlyVisitedDoctors);
            favoriteViewPager.setClipToPadding(false);
            favoriteViewPager.setPadding(pager_padding, 0, pager_padding, 0);
            favoriteViewPager.setPageMargin(pager_margin);
        }
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
