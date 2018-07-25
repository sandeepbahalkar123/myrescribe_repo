package com.rescribe.ui.activities.book_appointment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.rescribe.R;
import com.rescribe.ui.fragments.book_appointment.BookAppointListOnLocationSelectionFragment;
import com.rescribe.ui.fragments.book_appointment.DrawerForFilterBookAppointmentFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by jeetal on 6/11/17.
 */

public class BookAppointListOnLocationSelection extends AppCompatActivity implements DrawerForFilterBookAppointmentFragment.OnDrawerInteractionListener {

    @BindView(R.id.nav_view)
    FrameLayout mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.viewContainer)
    FrameLayout viewContainer;
    private FragmentManager mFragmentManager;
    private DrawerForFilterBookAppointmentFragment mDrawerLoadedFragment;
    private BookAppointListOnLocationSelectionFragment mLoadedFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_drawer_layout_container);
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();

        //------
        Bundle extras = getIntent().getExtras();
        mLoadedFragment = BookAppointListOnLocationSelectionFragment.newInstance(extras);
        getSupportFragmentManager().beginTransaction().replace(R.id.viewContainer, mLoadedFragment).commit();
        //-----------
        //------
        mDrawerLoadedFragment = DrawerForFilterBookAppointmentFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_view, mDrawerLoadedFragment).commit();

    }

    @Override
    public void onApply(Bundle b, boolean drawerRequired) {
        mDrawerLayout.closeDrawers();
        mLoadedFragment.onApplyClicked(b);
    }

    @Override
    public void onReset(boolean drawerRequired) {

    }

    public DrawerLayout getActivityDrawerLayout() {
        return mDrawerLayout;
    }
}