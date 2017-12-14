package com.rescribe.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.helpers.dashboard.DashboardHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 8/12/17.
 */

public class SaveArticleHealthEducation extends AppCompatActivity implements HelperResponse {

    @BindView(R.id.webViewLayout)
    WebView mWebViewObject;
    @BindView(R.id.webViewTitle)
    TextView mWebViewTitle;
    @BindView(R.id.bookMarkIcon)
    ImageView mBookMarkIcon;
    String mUrl;
    private boolean mIsSaved = false;
    private DashboardHelper mDashBoardHelper;
    private String mViewOpeningFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mUrl = extras.getString(getString(R.string.url));
            mViewOpeningFrom = extras.getString(getString(R.string.clicked_item_data));
            //-------SET BOOKMARK ICON ------
            mIsSaved = extras.getBoolean(getString(R.string.save), false);
            if (mIsSaved) {
                mBookMarkIcon.setImageResource(R.drawable.ic_action_bookmark);
            } else {
                mBookMarkIcon.setImageResource(R.drawable.ic_action_bookmark_border);
            }
            //--------------
            String title = extras.getString(getString(R.string.toolbarTitle));

            if (!(RescribeConstants.BLANK.equalsIgnoreCase(title) || title == null))
                mWebViewTitle.setText("" + title);

        }


        mDashBoardHelper = new DashboardHelper(this, this);

        // Hardcoded
//        String url = "http://che.org.il/wp-content/uploads/2016/12/pdf-sample.pdf";

        loadWebViewData(mUrl);
    }

    @OnClick({R.id.backButton, R.id.bookMarkIcon})
    public void back(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                onBackPressed();
                break;
            case R.id.bookMarkIcon:
                boolean localSaved = mIsSaved ? false : true;

                Log.e("bookMarkIcon", "getUrl:" + mWebViewObject.getUrl());
                Log.e("bookMarkIcon", "getOriginalUrl : " + mWebViewObject.getOriginalUrl());
                Log.e("bookMarkIcon", "getTitle" + mWebViewObject.getTitle());
                mDashBoardHelper.doSaveArticlesToServer(mWebViewObject.getUrl(), localSaved);
                break;
        }

    }

    private void loadWebViewData(String url) {
        if (url != null) {
            mWebViewObject.setVisibility(View.VISIBLE);

            // Let's display the progress in the activity title bar, like the
            // browser app does.

            mWebViewObject.getSettings().setJavaScriptEnabled(true);
            mWebViewObject.getSettings().setSupportZoom(true);
            mWebViewObject.getSettings().setBuiltInZoomControls(true);


            mWebViewObject.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    mBookMarkIcon.setVisibility(View.GONE);
                }

                public void onPageFinished(WebView view, String url) {
                    // do your stuff here
                    if (getString(R.string.saved_articles).equalsIgnoreCase(mViewOpeningFrom)) {
                        if (mWebViewObject.canGoBack()) {
                            mBookMarkIcon.setVisibility(View.GONE);
                        } else {
                            mBookMarkIcon.setVisibility(View.GONE);
                            mIsSaved = false;
                            mBookMarkIcon.setImageResource(R.drawable.ic_action_bookmark_border);
                        }
                    } else if (getString(R.string.clicked_saved_articles).equalsIgnoreCase(mViewOpeningFrom)) {
                        mBookMarkIcon.setVisibility(View.GONE);
                    }

                }
            });
            mWebViewObject.loadUrl(url);
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebViewObject.canGoBack()) {
            mWebViewObject.goBack();
        } else
            super.onBackPressed();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (customResponse != null) {
            CommonBaseModelContainer responseFavouriteDoctorBaseModel = (CommonBaseModelContainer) customResponse;
           // CommonMethods.showToast(this, responseFavouriteDoctorBaseModel.getCommonRespose().getStatusMessage());

            if (responseFavouriteDoctorBaseModel.getCommonRespose().isSuccess()) {
                if (mIsSaved) {
                    mBookMarkIcon.setImageResource(R.drawable.ic_action_bookmark_border);
                    mIsSaved = false;
                } else {
                    mBookMarkIcon.setImageResource(R.drawable.ic_action_bookmark);
                    mIsSaved = true;
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
}
