package com.rescribe.ui.activities.dashboard;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.heinrichreimersoftware.materialdrawer.DrawerActivity;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.heinrichreimersoftware.materialdrawer.theme.DrawerTheme;
import com.rescribe.R;
import com.rescribe.preference.RescribePreferencesManager;

import butterknife.ButterKnife;

/**
 * Created by jeetal on 3/11/17.
 */

public class ProfileActivity extends DrawerActivity {

    private static final long MANAGE_ACCOUNT = 121;
    private static final long ADD_ACCOUNT = 122;
    private static final String TAG = "ProfilePage";
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        mContext = ProfileActivity.this;
        drawerConfiguration();
        openDrawer();
    }

    private void drawerConfiguration() {
        setDrawerTheme(
                new DrawerTheme(this)
                        .setBackgroundColorRes(R.color.drawer_bg)
                        .setTextColorPrimaryRes(R.color.black)
                        .setTextColorSecondaryRes(R.color.grey_shade)
        );

        // TODO : HARDEDCODED will get remove once done with APIs.
        addProfile(new DrawerProfile()
                .setId(1)
                .setRoundedAvatar((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.profile))
                .setBackground(ContextCompat.getDrawable(this, R.drawable.group_2))
                .setName(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.USER_NAME, mContext))
                .setDescription(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER, mContext))
                .setExtraText("Age: 62 years")
        );

        addProfile(new DrawerProfile()
                .setId(2)
                .setRoundedAvatar((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.profile))
                .setBackground(ContextCompat.getDrawable(this, R.drawable.group_2))
                .setName("Mr. Sandeep Deshmukh ")
                .setDescription("8201888897")
                .setExtraText("Age: 32 years")
        );

        addProfile(new DrawerProfile()
                .setId(ADD_ACCOUNT)
                .setRoundedAvatar((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.profile_addaccount_icon))
                .setBackground(ContextCompat.getDrawable(this, R.drawable.group_2))
                .setDescription("Add Patient").setProfile(false) // for fixed item set profile false
        );

        addProfile(new DrawerProfile()
                .setId(MANAGE_ACCOUNT)
                .setRoundedAvatar((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.profile_manageaccount_icon))
                .setBackground(ContextCompat.getDrawable(this, R.drawable.group_2))
                .setDescription("Manage Profile").setProfile(false) // for fixed item set profile false
        );

        setOnNonProfileClickListener(new DrawerProfile.OnNonProfileClickListener() {
            @Override
            public void onProfileItemClick(DrawerProfile profile, long id) {
                if (id == ADD_ACCOUNT) {

                    // Do stuff here

                   /* addProfile(new DrawerProfile()
                            .setId(3)
                            .setRoundedAvatar((BitmapDrawable) ContextCompat.getDrawable(mContext, R.drawable.profile))
                            .setBackground(ContextCompat.getDrawable(mContext, R.drawable.coacmark_gettoken))
                            .setName("Mr.Ganesh Deshmukh")
                            .setDescription("ganesh_deshmukh@gmail.com")
                    );*/
//                    CommonMethods.showToast(mContext, "Profile Added");

                } else if (id == MANAGE_ACCOUNT) {
                    // Do stuff here
//                    CommonMethods.showToast(mContext, profile.getDescription());
                }

            }
        });

        setOnProfileSwitchListener(new DrawerProfile.OnProfileSwitchListener() {
            @Override
            public void onSwitch(DrawerProfile oldProfile, long oldId, DrawerProfile newProfile, long newId) {
                // do stuff here
//                CommonMethods.showToast(mContext, "Welcome " + newProfile.getName());
            }
        });
    }
}
