package com.rescribe.ui.activities.find_doctors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.adapters.find_doctors.FindDoctorCategoryAdapter;
import com.rescribe.adapters.find_doctors.FindDoctorsMenuListAdapter;
import com.rescribe.adapters.find_doctors.ShowDoctorComplaints;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.helpers.book_appointment.ServicesCardViewImpl;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.interfaces.dashboard_menu_click.IOnMenuClickListener;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.book_appointment.complaints.ComplaintsBaseModel;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.dashboard_api.ClickEvent;
import com.rescribe.model.dashboard_api.ClickOption;
import com.rescribe.model.dashboard_api.DashboardMenuList;
import com.rescribe.ui.activities.ConnectSplashActivity;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.activities.book_appointment.ServicesFilteredDoctorListActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 13/10/17.
 */

public class FindDoctorsActivity extends AppCompatActivity implements HelperResponse, IOnMenuClickListener {

    private static final String FOLDER_PATH = "images/dashboard/menu/finddoctors/android/";
    private String density;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_group_photo)
    ImageView imgGroupPhoto;

    @BindView(R.id.backgroundImage)
    ImageView backgroundImage;
    @BindView(R.id.bottomFrame)
    RelativeLayout bottomFrame;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.listView)
    RecyclerView listView;

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
    @BindView(R.id.viewAllRecentVisited)
    CustomTextView viewAllRecentVisited;
    @BindView(R.id.viewAllSponsered)
    CustomTextView viewAllSponsered;
    @BindView(R.id.viewAllFavorite)
    CustomTextView viewAllFavorite;
    @BindView(R.id.complaintsTextView)
    AutoCompleteTextView complaintsTextView;
    @BindView(R.id.complaintsImageView)
    ImageView complaintsImageView;
    @BindView(R.id.recentlyvisitedTextView)
    CustomTextView recentlyvisitedTextView;
    @BindView(R.id.sponsoredDoctors)
    CustomTextView sponsoredDoctors;
    @BindView(R.id.favouriteDoctors)
    CustomTextView favouriteDoctors;
    @BindView(R.id.title)
    CustomTextView title;

    private Context mContext;
    private int pager_padding;
    private int pager_margin;
    private FindDoctorCategoryAdapter mRecentlyVisitedDoctors;
    private ServicesCardViewImpl mServicesCardViewImpl;
    private DashboardMenuList mReceivedDashboardMenuListData;
    private AppDBHelper appDBHelper;
    private ArrayList<DoctorList> sponsered;
    private ArrayList<DoctorList> recently_visit_doctor;
    private ArrayList<DoctorList> favoriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_doctors_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mContext = FindDoctorsActivity.this;
        appDBHelper = new AppDBHelper(mContext);
        density = CommonMethods.getDeviceResolution(mContext) + "/";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mReceivedDashboardMenuListData = extras.getParcelable(RescribeConstants.ITEM_DATA);
            String value = extras.getString(RescribeConstants.ITEM_DATA_VALUE);

            if (mReceivedDashboardMenuListData != null) {
                title.setText(mReceivedDashboardMenuListData.getName());
            } else if (value != null)
                title.setText(value);
        }

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

    @SuppressLint("CheckResult")
    private void initialize() {
        mServicesCardViewImpl = new ServicesCardViewImpl(mContext, (FindDoctorsActivity) mContext);
        recentlyvisitedTextView.setText(getString(R.string.recently_visit_doctor).toUpperCase());
        sponsoredDoctors.setText(getString(R.string.sponsored_doctor).toUpperCase());
        favouriteDoctors.setText(getString(R.string.favorite).toUpperCase());

        //------Load background image : START------
        ClickEvent clickEvent1 = mReceivedDashboardMenuListData.getClickEvent();
        if (clickEvent1 != null) {
            if (clickEvent1.getBgImageUrl() != null) {

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.dontAnimate();
                requestOptions.centerCrop();

                String imageURL = Config.BASE_URL + FOLDER_PATH + density + clickEvent1.getBgImageUrl();

                Glide.with(this)
                        .load(imageURL)
                        .apply(requestOptions)
                        .into(imgGroupPhoto);
            }
        }
        //------Load background image : END------


        //------------
        pager_padding = getResources().getDimensionPixelSize(R.dimen.dp28);
        pager_margin = getResources().getDimensionPixelSize(R.dimen.pager_margin);
        DoctorDataHelper mDoctorDataHelper = new DoctorDataHelper(this, this);
        mDoctorDataHelper.doGetComplaintsList();
        SpannableString contentViewAllFavorite = new SpannableString(viewAllFavorite.getText().toString());
        contentViewAllFavorite.setSpan(new UnderlineSpan(), 0, contentViewAllFavorite.length(), 0);
        viewAllFavorite.setText(contentViewAllFavorite);
        SpannableString contentViewAllRecentVisited = new SpannableString(viewAllRecentVisited.getText().toString());
        contentViewAllRecentVisited.setSpan(new UnderlineSpan(), 0, contentViewAllRecentVisited.length(), 0);
        viewAllRecentVisited.setText(contentViewAllRecentVisited);
        SpannableString contentViewAllSponsered = new SpannableString(viewAllSponsered.getText().toString());
        contentViewAllSponsered.setSpan(new UnderlineSpan(), 0, contentViewAllSponsered.length(), 0);
        viewAllSponsered.setText(contentViewAllSponsered);

        //----------------
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());

        //-------------
        ClickEvent clickEvent = mReceivedDashboardMenuListData.getClickEvent();
        if (clickEvent != null) {
            FindDoctorsMenuListAdapter mFindDoctorsAdapter = new FindDoctorsMenuListAdapter(mContext, clickEvent.getClickOptions(), this);
            listView.setAdapter(mFindDoctorsAdapter);
        }

        ViewTreeObserver vto = bottomFrame.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                bottomFrame.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = bottomFrame.getMeasuredWidth();
                int height = bottomFrame.getMeasuredHeight();
                RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(width, height);
                backgroundImage.setImageResource(R.drawable.bg_overlay);
                backgroundImage.setLayoutParams(parms);
            }
        });
    }

    private void setUpViewPager() {
        viewAllFavorite.setVisibility(View.GONE);
        viewAllSponsered.setVisibility(View.GONE);
        viewAllRecentVisited.setVisibility(View.GONE);

        sponsered = mServicesCardViewImpl.getCategoryWiseDoctorList(getString(R.string.sponsored_doctor), 3);
        recently_visit_doctor = mServicesCardViewImpl.getCategoryWiseDoctorList(getString(R.string.recently_visit_doctor), 3);
        favoriteList = mServicesCardViewImpl.getFavouriteDocList(3);
        setRecentlyVisitedPager();
        setSponseredPager();
        setFavoritePager();
    }

    private void setRecentlyVisitedPager() {
        if (!recently_visit_doctor.isEmpty()) {
            recentlyVisitedFindDoctorLayout.setVisibility(View.VISIBLE);
            mRecentlyVisitedDoctors = new FindDoctorCategoryAdapter(this, recently_visit_doctor, mServicesCardViewImpl, this);
            recentlyViewPager.setAdapter(mRecentlyVisitedDoctors);
            recentlyViewPager.setClipToPadding(false);
            recentlyViewPager.setPadding(pager_padding, 0, pager_padding, 0);
            recentlyViewPager.setPageMargin(pager_margin);
        } else
            recentlyVisitedFindDoctorLayout.setVisibility(View.GONE);
    }

    private void setSponseredPager() {
        if (!sponsered.isEmpty()) {
            sponseredFindDoctorLayout.setVisibility(View.VISIBLE);
            mRecentlyVisitedDoctors = new FindDoctorCategoryAdapter(this, sponsered, mServicesCardViewImpl, this);
            sponseredViewPager.setAdapter(mRecentlyVisitedDoctors);
            sponseredViewPager.setClipToPadding(false);
            sponseredViewPager.setPadding(pager_padding, 0, pager_padding, 0);
            sponseredViewPager.setPageMargin(pager_margin);
        } else
            sponseredFindDoctorLayout.setVisibility(View.GONE);
    }

    private void setFavoritePager() {
        if (!favoriteList.isEmpty()) {
            favoriteFindDoctorLayout.setVisibility(View.VISIBLE);
            mRecentlyVisitedDoctors = new FindDoctorCategoryAdapter(this, favoriteList, mServicesCardViewImpl, this);
            favoriteViewPager.setAdapter(mRecentlyVisitedDoctors);
            favoriteViewPager.setClipToPadding(false);
            favoriteViewPager.setPadding(pager_padding, 0, pager_padding, 0);
            favoriteViewPager.setPageMargin(pager_margin);
        } else
            favoriteFindDoctorLayout.setVisibility(View.GONE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpViewPager();
    }

    @OnClick({R.id.complaintsImageView, R.id.favouriteDoctors, R.id.recentlyvisitedTextView, R.id.sponsoredDoctors})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.complaintsImageView:
                String completeText = complaintsTextView.getText().toString().trim();
                if (!completeText.isEmpty()) {
                    Bundle bundleData = new Bundle();
                    bundleData.putString(RescribeConstants.TITLE, complaintsTextView.getText().toString());
                    bundleData.putString(RescribeConstants.CATEGORY, complaintsTextView.getText().toString());
                    Intent intentComplaint = new Intent(FindDoctorsActivity.this, ServicesFilteredDoctorListActivity.class);
                    intentComplaint.putExtras(bundleData);
                    startActivity(intentComplaint);
                }
                break;
            case R.id.favouriteDoctors:
                Intent viewAllFavorite = new Intent(mContext, ShowCategoryWiseDoctorActivity.class);
                viewAllFavorite.putExtra(RescribeConstants.TITLE, getString(R.string.doctorss));
                viewAllFavorite.putExtra(RescribeConstants.ITEM_DATA_VALUE, mContext.getString(R.string.favorite));
                startActivity(viewAllFavorite);
                break;
            case R.id.recentlyvisitedTextView:
                Intent viewAllRecentVisited = new Intent(mContext, ShowCategoryWiseDoctorActivity.class);
                viewAllRecentVisited.putExtra(RescribeConstants.TITLE, getString(R.string.doctorss));
                viewAllRecentVisited.putExtra(RescribeConstants.ITEM_DATA_VALUE, mContext.getString(R.string.recently_visit_doctor));
                startActivity(viewAllRecentVisited);
                break;
            case R.id.sponsoredDoctors:
                Intent viewAllSponsered = new Intent(mContext, ShowCategoryWiseDoctorActivity.class);
                viewAllSponsered.putExtra(RescribeConstants.TITLE, getString(R.string.doctorss));
                viewAllSponsered.putExtra(RescribeConstants.ITEM_DATA_VALUE, mContext.getString(R.string.sponsored_doctor));
                startActivity(viewAllSponsered);
                break;
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_GET_COMPLAINTS)) {
            if (customResponse != null) {
                ComplaintsBaseModel complaintsBaseModel = (ComplaintsBaseModel) customResponse;
                ShowDoctorComplaints adapter = new ShowDoctorComplaints(this, R.layout.find_doctors_layout, R.id.custom_spinner_txt_view_Id, complaintsBaseModel.getComplaintsModel().getComplaintList());
                complaintsTextView.setAdapter(adapter);
            }
        } else if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_SET_FAVOURITE_DOCTOR)) {
            CommonBaseModelContainer temp = (CommonBaseModelContainer) customResponse;
            CommonMethods.showToast(this, temp.getCommonRespose().getStatusMessage());
            if (temp.getCommonRespose().isSuccess()) {
                ServicesCardViewImpl.updateFavStatusForDoctorDataObject(ServicesCardViewImpl.getUserSelectedDoctorListDataObject(), appDBHelper);
                setUpViewPager();
            }

        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onMenuClick(ClickOption data) {
        if (data.getName().equalsIgnoreCase(getString(R.string.book_appointment))) {
            Intent intent = new Intent(mContext, BookAppointDoctorListBaseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(RescribeConstants.ITEM_DATA, getString(R.string.doctorss));
            bundle.putString(RescribeConstants.CALL_FROM_DASHBOARD, "");
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (data.getName().equalsIgnoreCase(getString(R.string.doctor_connect))) {
            Intent intent = new Intent(mContext, ConnectSplashActivity.class);
            startActivity(intent);
        }
    }
}
