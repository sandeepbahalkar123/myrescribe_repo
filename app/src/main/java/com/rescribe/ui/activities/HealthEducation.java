package com.rescribe.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.saved_article.SavedArticleHealthEducationAdapter;
import com.rescribe.helpers.dashboard.DashboardHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.saved_article.SavedArticleBaseModel;
import com.rescribe.model.saved_article.SavedArticleDataModel;
import com.rescribe.model.saved_article.SavedArticleInfo;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 8/12/17.
 */

public class HealthEducation extends AppCompatActivity implements HelperResponse, SavedArticleHealthEducationAdapter.OnArticleHealthEducationClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.listView)
    RecyclerView mSavedArticleListView;
    @BindView(R.id.emptyListView)
    RelativeLayout mEmptyListView;
    @BindView(R.id.title)
    CustomTextView title;
    private SavedArticleHealthEducationAdapter mSavedArticleListAdapter;
    private DashboardHelper mHelper;
    private ArrayList<SavedArticleInfo> mReceivedSavedArticleList;
    private int pos;
    private boolean isHealthEducationApiCalled = true;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_articles_base_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initialize();
        getSupportActionBar().setTitle("");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title.setText(extras.getString(getString(R.string.clicked_item_data)));
        }
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void initialize() {
        mContext = HealthEducation.this;
        mHelper = new DashboardHelper(this, this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isHealthEducationApiCalled) {
            mHelper.doHealthEducationGetSavedArticles();
        }
    }
    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        switch (mOldDataTag) {
            case RescribeConstants.TASK_GET_HEALTH_EDUCATION_ARTICLES:
                SavedArticleBaseModel savedArticleBaseModel = (SavedArticleBaseModel) customResponse;
                if (savedArticleBaseModel == null) {
                    isDataListViewVisible(false);
                } else {
                    SavedArticleDataModel savedArticleDataModel = savedArticleBaseModel.getSavedArticleDataModel();
                    if (savedArticleDataModel == null) {
                        isDataListViewVisible(false);
                    } else {
                        mReceivedSavedArticleList = savedArticleDataModel.getSavedArticleList();
                        if (mReceivedSavedArticleList.size() > 0) {
                            isDataListViewVisible(true);
                            isHealthEducationApiCalled = false;

                            //----------
                            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                            mSavedArticleListView.setLayoutManager(layoutManager);
                            mSavedArticleListView.setHasFixedSize(true);
                            DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                                    mSavedArticleListView.getContext(),
                                    layoutManager.getOrientation()
                            );
                            mSavedArticleListView.addItemDecoration(mDividerItemDecoration);
                            //----------
                            mSavedArticleListAdapter = new SavedArticleHealthEducationAdapter(mContext, mReceivedSavedArticleList, this);
                            mSavedArticleListView.setAdapter(mSavedArticleListAdapter);
                        } else {
                            isDataListViewVisible(false);
                        }
                    }
                }
                break;

            case RescribeConstants.TASK_SAVE_ARTICLES_TO_SERVER:
                if (customResponse != null) {
                    CommonBaseModelContainer responseFavouriteDoctorBaseModel = (CommonBaseModelContainer) customResponse;
                    //  CommonMethods.showToast(this, responseFavouriteDoctorBaseModel.getCommonRespose().getStatusMessage());
                    // mHelper.doHealthEducationGetSavedArticles();
                    if (responseFavouriteDoctorBaseModel.getCommonRespose().getStatusMessage().equals("Article saved!")) {
                        mReceivedSavedArticleList.get(pos).setIsSaved(true);
                        mSavedArticleListAdapter.notifyItemChanged(pos);
                    } else {
                        mReceivedSavedArticleList.get(pos).setIsSaved(false);
                        mSavedArticleListAdapter.notifyItemChanged(pos);
                    }


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
    public void onArticleClicked(SavedArticleInfo data) {

        Intent intent = new Intent(HealthEducation.this, SaveArticleHealthEducation.class);
        Bundle b = new Bundle();
        b.putString(getString(R.string.url), data.getArticleUrl());
        b.putString(getString(R.string.toolbarTitle), getString(R.string.health_education));
        b.putString(getString(R.string.clicked_item_data), getString(R.string.clicked_saved_articles));
        b.putBoolean(getString(R.string.save), true);
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    public void onBookMarkIconClicked(SavedArticleInfo data, int position) {
        pos = position;
        if (data.getIsSaved()) {
            mHelper.doSaveArticlesToServer(data.getArticleUrl(), false);
        } else {
            mHelper.doSaveArticlesToServer(data.getArticleUrl(), true);
        }


    }

    private void isDataListViewVisible(boolean flag) {
        if (flag) {
            mEmptyListView.setVisibility(View.GONE);
            mSavedArticleListView.setVisibility(View.VISIBLE);
        } else {
            mEmptyListView.setVisibility(View.VISIBLE);
            mSavedArticleListView.setVisibility(View.GONE);
        }
    }

}

