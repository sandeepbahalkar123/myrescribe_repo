package com.myrescribe.ui.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.myrescribe.R;
import com.myrescribe.ui.customesViews.zoomview.ZoomageView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ZoomImageViewActivity extends AppCompatActivity {

    @BindView(R.id.zoomView)
    ZoomageView zoomView;
    @BindView(R.id.backButton)
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getColor(R.color.black));
        setContentView(R.layout.activity_zoom_image_view);
        ButterKnife.bind(this);

        Glide.with(this).load(new File(getIntent().getStringExtra("IMAGE")))
                .placeholder(droidninja.filepicker.R.drawable.image_placeholder)
                .into(zoomView);
    }

    @OnClick(R.id.backButton)
    public void onViewClicked() {
        onBackPressed();
    }
}
