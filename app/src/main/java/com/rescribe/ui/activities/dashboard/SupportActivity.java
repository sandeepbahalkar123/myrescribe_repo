package com.rescribe.ui.activities.dashboard;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuActivity;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuAdapter;
import com.rescribe.R;
import com.rescribe.ui.customesViews.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.rescribe.util.RescribeConstants.TITLE;

/**
 * Created by jeetal on 3/11/17.
 */

@RuntimePermissions
public class SupportActivity extends BottomMenuActivity implements BottomMenuAdapter.OnBottomMenuClickListener {

    @BindView(R.id.callTextView)
    CustomTextView callTextView;
    @BindView(R.id.emailtextView)
    CustomTextView emailtextView;
    @BindView(R.id.title)
    CustomTextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_base_layout);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        title.setText(getIntent().getStringExtra(TITLE));
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    void doCallSupport() {
        callSupport();
    }

    private void callSupport() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:123456789"));
        startActivity(callIntent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SupportActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnClick({R.id.callTextView, R.id.emailtextView, R.id.backButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                onBackPressed();
                break;
            case R.id.callTextView:
                SupportActivityPermissionsDispatcher.doCallSupportWithCheck(this);
                break;
            case R.id.emailtextView:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "your_email"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
                    intent.putExtra(Intent.EXTRA_TEXT, "your_text");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    //TODO smth
                }
                break;
        }
    }
}
