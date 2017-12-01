package com.rescribe.ui.activities.saved_articles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.health_repository.HealthRepositoryAdapter;
import com.rescribe.adapters.saved_article.SavedArticleListAdapter;
import com.rescribe.helpers.dashboard.DashboardHelper;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.interfaces.dashboard_menu_click.IOnMenuClickListener;
import com.rescribe.model.dashboard_api.ClickOption;
import com.rescribe.model.saved_article.SavedArticleBaseModel;
import com.rescribe.model.saved_article.SavedArticleDataModel;
import com.rescribe.model.saved_article.SavedArticleInfo;
import com.rescribe.ui.activities.health_repository.HealthRepository;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 27/11/17.
 */

public class SavedArticles extends AppCompatActivity implements HelperResponse, SavedArticleListAdapter.OnArticleClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.listView)
    RecyclerView mSavedArticleListView;
    @BindView(R.id.emptyListView)
    RelativeLayout mEmptyListView;

    private SavedArticleListAdapter mSavedArticleListAdapter;

    private DashboardHelper mHelper;
    private ArrayList<SavedArticleInfo> mReceivedSavedArticleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_articles_base_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            getSupportActionBar().setTitle(extras.getString(getString(R.string.clicked_item_data)));
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
        mHelper = new DashboardHelper(this, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mHelper.doGetSavedArticles();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

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

                    //----------
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    mSavedArticleListView.setLayoutManager(layoutManager);
                    mSavedArticleListView.setHasFixedSize(true);
                    DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                            mSavedArticleListView.getContext(),
                            layoutManager.getOrientation()
                    );
                    mSavedArticleListView.addItemDecoration(mDividerItemDecoration);
                    //----------
                    mSavedArticleListAdapter = new SavedArticleListAdapter(this, mReceivedSavedArticleList, this);
                    mSavedArticleListView.setAdapter(mSavedArticleListAdapter);
                } else {
                    isDataListViewVisible(false);
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

        Intent intent = new Intent(this, SaveArticleWebViewActivity.class);
        Bundle b = new Bundle();
        b.putString(getString(R.string.url), data.getArticleUrl());
        b.putString(getString(R.string.toolbarTitle), getString(R.string.saved_articles));
        b.putString(getString(R.string.clicked_item_data), getString(R.string.clicked_saved_articles));
        b.putBoolean(getString(R.string.save), true);
        intent.putExtras(b);
        startActivity(intent);
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

