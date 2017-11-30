package com.rescribe.ui.activities.find_doctors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.adapters.find_doctors.FindDoctorsMenuListAdapter;
import com.rescribe.adapters.find_doctors.FindDoctorCategoryAdapter;
import com.rescribe.adapters.find_doctors.ShowDoctorComplaints;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.helpers.book_appointment.ServicesCardViewImpl;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.interfaces.dashboard_menu_click.IOnMenuClickListener;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.book_appointment.complaints.ComplaintsBaseModel;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.dashboard_api.ClickEvent;
import com.rescribe.model.dashboard_api.ClickOption;
import com.rescribe.model.dashboard_api.DashboardMenuList;
import com.rescribe.ui.activities.DoctorConnectActivity;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 13/10/17.
 */

public class FindDoctorsActivity extends AppCompatActivity implements HelperResponse, IOnMenuClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_group_photo)
    ImageView imgGroupPhoto;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.listView)
    RecyclerView listView;
    ArrayList<DoctorList> sponsered;
    ArrayList<DoctorList> recently_visit_doctor;
    ArrayList<DoctorList> favoriteList;
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
    @BindView(R.id.viewAllRecentVisited)
    CustomTextView viewAllRecentVisited;
    @BindView(R.id.viewAllSponsered)
    CustomTextView viewAllSponsered;
    @BindView(R.id.viewAllFavorite)
    CustomTextView viewAllFavorite;
    @BindView(R.id.complaintsTextView)
    AutoCompleteTextView complaintsTextView;
    @BindView(R.id.bookAppointmentButton)
    FrameLayout bookAppointmentButton;
    @BindView(R.id.complaintsImageView)
    ImageView complaintsImageView;
    private FindDoctorsMenuListAdapter mFindDoctorsAdapter;
    private Context mContext;
    int pager_padding;
    int pager_margin;
    private FindDoctorCategoryAdapter mRecentlyVisitedDoctors;
    private ServicesCardViewImpl mServicesCardViewImpl;
    private DoctorDataHelper mDoctorDataHelper;
    private DashboardMenuList mReceivedDashboardMenuListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_doctors_layout);
        ButterKnife.bind(this);
        mContext = FindDoctorsActivity.this;
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mReceivedDashboardMenuListData = extras.getParcelable(getString(R.string.clicked_item_data));
            getSupportActionBar().setTitle(mReceivedDashboardMenuListData.getName());
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

    private void initialize() {

        //------Load background image : START------
        ClickEvent clickEvent1 = mReceivedDashboardMenuListData.getClickEvent();
        if (clickEvent1 != null) {
            if (clickEvent1.getBgImageUrl() != null) {
                int imageSizeToLoadImage = CommonMethods.getImageSizeToLoadImage(this, 2);

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.dontAnimate();
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                requestOptions.skipMemoryCache(true);
                requestOptions.override(imageSizeToLoadImage, imageSizeToLoadImage);

                Glide.with(this)
                        .load(clickEvent1.getBgImageUrl())
                        .apply(requestOptions).thumbnail(0.5f)
                        .into(imgGroupPhoto);
            }
        }
        //------Load background image : END------


        //------------
        pager_padding = getResources().getDimensionPixelSize(R.dimen.pager_padding);
        pager_margin = getResources().getDimensionPixelSize(R.dimen.pager_margin);
        mDoctorDataHelper = new DoctorDataHelper(this, this);
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

//            int spanCount = 2; // 3 columns
//            int spacing = 20; // 50px
//            boolean includeEdge = true;
//            listView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        //-------------
        ClickEvent clickEvent = mReceivedDashboardMenuListData.getClickEvent();
        if (clickEvent != null) {
            mFindDoctorsAdapter = new FindDoctorsMenuListAdapter(mContext, clickEvent.getClickOptions(), this);
            listView.setAdapter(mFindDoctorsAdapter);

        }
        //-------------

        //----------------

        setUpViewPager();
    }

    private void setUpViewPager() {
        mServicesCardViewImpl = new ServicesCardViewImpl(mContext, (FindDoctorsActivity) mContext);
        sponsered = mServicesCardViewImpl.getCategoryWiseDoctorList(getString(R.string.sponsered_doctor), 3);
        recently_visit_doctor = mServicesCardViewImpl.getCategoryWiseDoctorList(getString(R.string.recently_visit_doctor), 3);
        favoriteList = mServicesCardViewImpl.getFavouriteDocList(3);
        setRecentlyVisitedPager();
        setSponseredPager();
        setFavoritePager();
    }

    private void setRecentlyVisitedPager() {
        if(recently_visit_doctor.size()>0) {
            recentlyVisitedFindDoctorLayout.setVisibility(View.VISIBLE);
            mRecentlyVisitedDoctors = new FindDoctorCategoryAdapter(this, recently_visit_doctor, mServicesCardViewImpl, this);
            recentlyViewPager.setAdapter(mRecentlyVisitedDoctors);
            recentlyViewPager.setClipToPadding(false);
            recentlyViewPager.setPadding(pager_padding, 0, pager_padding, 0);
            recentlyViewPager.setPageMargin(pager_margin);
        }else{
            recentlyVisitedFindDoctorLayout.setVisibility(View.GONE);
        }
    }

    private void setSponseredPager() {
        if(sponsered.size()>0) {
            sponseredFindDoctorLayout.setVisibility(View.VISIBLE);
            mRecentlyVisitedDoctors = new FindDoctorCategoryAdapter(this, sponsered, mServicesCardViewImpl, this);
            sponseredViewPager.setAdapter(mRecentlyVisitedDoctors);
            sponseredViewPager.setClipToPadding(false);
            sponseredViewPager.setPadding(pager_padding, 0, pager_padding, 0);
            sponseredViewPager.setPageMargin(pager_margin);
        }else{
            sponseredFindDoctorLayout.setVisibility(View.GONE);
        }
    }

    private void setFavoritePager() {
        if(favoriteList.size()>0) {
            favoriteFindDoctorLayout.setVisibility(View.VISIBLE);
            mRecentlyVisitedDoctors = new FindDoctorCategoryAdapter(this, favoriteList, mServicesCardViewImpl, this);
            favoriteViewPager.setAdapter(mRecentlyVisitedDoctors);
            favoriteViewPager.setClipToPadding(false);
            favoriteViewPager.setPadding(pager_padding, 0, pager_padding, 0);
            favoriteViewPager.setPageMargin(pager_margin);
        }else{
            favoriteFindDoctorLayout.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpViewPager();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @OnClick({R.id.viewAllFavorite, R.id.viewAllRecentVisited, R.id.viewAllSponsered})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.viewAllFavorite:
                Intent viewAllFavorite = new Intent(mContext, ShowCategoryWiseDoctor.class);
                viewAllFavorite.putExtra(getString(R.string.toolbarTitle), getString(R.string.favorite));
                startActivity(viewAllFavorite);
                break;
            case R.id.viewAllRecentVisited:
                Intent viewAllRecentVisited = new Intent(mContext, ShowCategoryWiseDoctor.class);
                viewAllRecentVisited.putExtra(getString(R.string.toolbarTitle), getString(R.string.recently_visit_doctor));
                startActivity(viewAllRecentVisited);
                break;
            case R.id.viewAllSponsered:
                Intent viewAllSponsered = new Intent(mContext, ShowCategoryWiseDoctor.class);
                viewAllSponsered.putExtra(getString(R.string.toolbarTitle), getString(R.string.sponsered_doctor));
                startActivity(viewAllSponsered);
                break;
            case R.id.complaintsImageView:
             Intent intentComplaint = new Intent(FindDoctorsActivity.this,ShowCategoryWiseDoctor.class);
                intentComplaint.putExtra(getString(R.string.toolbarTitle),getString(R.string.complaints));
              /*  intentComplaint.putExtra(getString(R.string.))*/
                startActivity(intentComplaint);
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
                //--------
                ServicesCardViewImpl.updateFavStatusForDoctorDataObject(ServicesCardViewImpl.getUserSelectedDoctorListDataObject());
                //--------
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
            // TODO, THIS IS ADDED FOR NOW, OPEN ONLY IF clicked value == DOCTOR
            bundle.putString(getString(R.string.clicked_item_data), getString(R.string.doctorss));
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (data.getName().equalsIgnoreCase(getString(R.string.consult_online))) {
            Intent intent = new Intent(mContext, DoctorConnectActivity.class);
            startActivity(intent);
        }
    }
}
