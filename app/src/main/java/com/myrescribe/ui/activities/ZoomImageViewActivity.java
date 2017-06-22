package com.myrescribe.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.myrescribe.R;

import java.io.File;

public class ZoomImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image_view);

        ImageView zoomView = (ImageView) findViewById(R.id.zoomView);
        Glide.with(this).load(new File(getIntent().getStringExtra("IMAGE")))
                .placeholder(droidninja.filepicker.R.drawable.image_placeholder)
                .into(zoomView);
    }
}
