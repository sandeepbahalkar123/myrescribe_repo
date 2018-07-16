package com.rescribe.ui.activities.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.adapters.doctor_visit_attachmnt_adapters.DoctorAttachListAdapter;
import com.rescribe.adapters.health_offers.HealthOffersAdapter;
import com.rescribe.adapters.health_repository.HealthRepositoryAdapter;
import com.rescribe.interfaces.dashboard_menu_click.IOnMenuClickListener;
import com.rescribe.model.dashboard_api.ClickEvent;
import com.rescribe.model.dashboard_api.ClickOption;
import com.rescribe.model.dashboard_api.DashboardMenuList;
import com.rescribe.ui.activities.MyRecordsActivity;
import com.rescribe.ui.activities.saved_articles.SavedArticlesActivity;
import com.rescribe.ui.activities.vital_graph.VitalGraphActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 13/10/17.
 */

public class DoctorVisitAttachmentsListActivity extends AppCompatActivity {

    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    @BindView(R.id.titleText)
    TextView titleText;

    private Context mContext;

    private ArrayList<String> receivedArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_view_attach_list);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getBundleExtra(RescribeConstants.ITEM_DATA);
        if (extras != null) {
            receivedArrayList = extras.getStringArrayList(RescribeConstants.ITEM_DATA_VALUE);
        }
        setUpAdapter();

        titleText.setText(getString(R.string.attachments));
    }

    private void setUpAdapter() {
        listView.setLayoutManager(new GridLayoutManager(this, 3));
        listView.setAdapter(new DoctorAttachListAdapter(this, receivedArrayList));
        listView.setVisibility(View.VISIBLE);
        emptyListView.setVisibility(View.GONE);
    }

    @OnClick(R.id.backArrow)
    void onClicked() {
        finish();
    }
}

