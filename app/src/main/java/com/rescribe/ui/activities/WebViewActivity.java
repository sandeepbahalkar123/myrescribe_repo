package com.rescribe.ui.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.ui.customesViews.zoomview.ZoomageView;
import com.rescribe.util.RescribeConstants;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.webViewLayout)
    WebView mWebViewObject;
    @BindView(R.id.backButton)
    AppCompatImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        ButterKnife.bind(this);
        loadWebViewData(getIntent().getStringExtra(getString(R.string.title_activity_selected_docs)));
    }

    @OnClick(R.id.backButton)
    public void back() {
        finish();
    }

    private void loadWebViewData(String url) {
        if (url != null) {
            mWebViewObject.setVisibility(View.VISIBLE);
            mWebViewObject.getSettings().setBuiltInZoomControls(true);
            mWebViewObject.getSettings().setJavaScriptEnabled(true);
            mWebViewObject.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
        }
    }
}
