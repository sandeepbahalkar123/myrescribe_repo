package com.rescribe.ui.activities.book_appointment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.adapters.book_appointment.ServicesAdapter;
import com.rescribe.interfaces.dashboard_menu_click.IOnMenuClickListener;
import com.rescribe.model.dashboard_api.ClickEvent;
import com.rescribe.model.dashboard_api.ClickOption;
import com.rescribe.model.dashboard_api.DashboardMenuList;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rescribe.util.RescribeConstants.DOCTOR_DATA_REQUEST_CODE;

/**
 * Created by jeetal on 15/9/17.
 */

public class BookAppointmentServices extends AppCompatActivity implements IOnMenuClickListener {

    private static final String FOLDER_PATH = "images/dashboard/menu/healthservices/android/";
    private String density;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_group_photo)
    ImageView imgGroupPhoto;
    @BindView(R.id.listView)
    RecyclerView listView;
    ServicesAdapter mServicesAdapter;
    @BindView(R.id.scroll)
    NestedScrollView scroll;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.title)
    CustomTextView title;
    private Context mContext;
    private int PLACE_PICKER_REQUEST = 10;
    String latitude = "";
    String longitude = "";
    String address;
    private DashboardMenuList mReceivedDashboardMenuListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_layout_trial);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mReceivedDashboardMenuListData = extras.getParcelable(getString(R.string.clicked_item_data));
            String value = extras.getString(getString(R.string.clicked_item_data_type_value));

            if (mReceivedDashboardMenuListData != null)
                title.setText(mReceivedDashboardMenuListData.getName());
            else if (value != null)
                title.setText(value);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initialize();
    }

    private void initialize() {
        mContext = BookAppointmentServices.this;

        density = CommonMethods.getDeviceResolution(mContext) + "/";

        //------Load background image : START------
        ClickEvent clickEvent1 = mReceivedDashboardMenuListData.getClickEvent();
        if (clickEvent1 != null) {
            if (clickEvent1.getBgImageUrl() != null) {

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.dontAnimate();
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                requestOptions.skipMemoryCache(true);

                String imageURL = Config.BASE_URL + FOLDER_PATH + density + clickEvent1.getBgImageUrl();

                Glide.with(this)
                        .load(imageURL)
                        .apply(requestOptions)
                        .into(imgGroupPhoto);
            }
        }
        //------Load background image : END------

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());

        //-------------
        ClickEvent clickEvent = mReceivedDashboardMenuListData.getClickEvent();
        if (clickEvent != null) {
            mServicesAdapter = new ServicesAdapter(mContext, clickEvent.getClickOptions(), this);
            listView.setAdapter(mServicesAdapter);
            listView.setNestedScrollingEnabled(false);
        }
        //-------------
    }

    @Override
    public void onMenuClick(ClickOption data) {
        // TODO, THIS IS ADDED FOR NOW, OPEN ONLY IF clicked value == DOCTOR
        if (data.getName().equalsIgnoreCase(getString(R.string.doctorss))) {
            Intent intent = new Intent(BookAppointmentServices.this, BookAppointDoctorListBaseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(RescribeConstants.CALL_FROM_DASHBOARD,"");
            bundle.putString(getString(R.string.location_address), address);
            bundle.putString(getString(R.string.latitude), latitude);
            bundle.putString(getString(R.string.longitude), longitude);
            bundle.putString(getString(R.string.location), "");
            bundle.putString(getString(R.string.clicked_item_data), data.getName());
            intent.putExtras(bundle);
            startActivityForResult(intent, DOCTOR_DATA_REQUEST_CODE);
        }
    }
}
