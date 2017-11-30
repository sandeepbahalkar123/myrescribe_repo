package com.heinrichreimersoftware.materialdrawer.bottom_menu;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.heinrichreimersoftware.materialdrawer.R;
import com.heinrichreimersoftware.materialdrawer.app_logo.BottomSheetMenuAdapter;
import com.heinrichreimersoftware.materialdrawer.app_logo.ClickEvent;
import com.heinrichreimersoftware.materialdrawer.app_logo.ClickOption;

import java.util.ArrayList;

@SuppressWarnings("unused")
@SuppressLint("Registered")
public class BottomMenuActivity extends AppCompatActivity implements BottomMenuAdapter.onBottomMenuClickListener {

    private FrameLayout mFrame;
    private RecyclerView bottomMenuListRecyclerView;
    private BottomMenuAdapter bottomMenuAdapter;
    private int widthPixels;
    private ArrayList<BottomMenu> bottomMenus = new ArrayList<>();
    private ArrayList<ClickOption> mClickOptions = new ArrayList<>();
    private RelativeLayout bottomSheetMenu;
    private FrameLayout bottomSheetMenuLayout;
    private BottomSheetMenuAdapter mBottomSheetMenuAdapter;
    private RecyclerView mBottomSheetMenuListRecyclerView;
    public boolean isOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.bottom_menu_activity);
        mFrame = (FrameLayout) findViewById(R.id.activityView);

        bottomSheetMenuLayout = (FrameLayout) findViewById(R.id.bottomSheetMenuLayout);
        bottomSheetMenu = (RelativeLayout) findViewById(R.id.bottomSheetMenu);
        bottomMenuListRecyclerView = (RecyclerView) findViewById(R.id.bottomMenuListRecyclerView);
        mBottomSheetMenuListRecyclerView = (RecyclerView)findViewById(R.id.bottomSheetMenuListRecyclerView);
        widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        createBottomMenu();

        bottomSheetMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSheet();
            }
        });
    }

    public void openSheet() {

        bottomSheetMenu.setVisibility(View.VISIBLE);
        bottomSheetMenuLayout.setVisibility(View.VISIBLE);

        AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(300);
        bottomSheetMenu.startAnimation(animation1);

        Animation slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up_animation);
        bottomSheetMenuLayout.startAnimation(slideUpAnimation);
        isOpen = true;
    }

    public void closeSheet() {
        Animation slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down_animation);

        AlphaAnimation animation1 = new AlphaAnimation(1.0f, 0.0f);
        animation1.setDuration(300);
        bottomSheetMenu.startAnimation(animation1);

        bottomSheetMenuLayout.startAnimation(slideDownAnimation);

        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bottomSheetMenu.setVisibility(View.GONE);
                bottomSheetMenuLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        isOpen = false;
    }

    @Override
    public void setContentView(int layoutResID) {
        mFrame.removeAllViews();
        View.inflate(this, layoutResID, mFrame);
    }

    @Override
    public void setContentView(View view) {
        mFrame.removeAllViews();
        mFrame.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mFrame.removeAllViews();
        mFrame.addView(view, params);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        mFrame.addView(view, params);
    }

    public void setMenuVisible(int visibility) {
        bottomMenuListRecyclerView.setVisibility(visibility);
    }

    public void createBottomMenu() {
        bottomMenuAdapter = new BottomMenuAdapter(this, bottomMenus);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

//        set padding BottomMenuListRecyclerView
//        int padding = Math.round((widthPixels * 2.5f) / 100);
//        bottomMenuListRecyclerView.setPadding(padding, 0, padding, 0);

        bottomMenuListRecyclerView.setLayoutManager(layoutManager);
        bottomMenuListRecyclerView.setHasFixedSize(true);
        bottomMenuListRecyclerView.setAdapter(bottomMenuAdapter);

      /*  mBottomSheetMenuAdapter = new BottomSheetMenuAdapter(this,mClickOptions);
        LinearLayoutManager bottomSheetlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mBottomSheetMenuListRecyclerView.setLayoutManager(bottomSheetlayoutManager);
        mBottomSheetMenuListRecyclerView.setHasFixedSize(true);
        mBottomSheetMenuListRecyclerView.setAdapter(mBottomSheetMenuAdapter);*/

    }

    public void addBottomMenu(BottomMenu bottomMenu) {
        bottomMenus.add(bottomMenu);
        bottomMenuAdapter.notifyItemInserted(bottomMenus.size());
    }

    public void addBottomSheetMenu(ClickOption clickOption){

       /* mClickOptions.add(clickOption);
        mBottomSheetMenuAdapter.notifyItemInserted(mClickOptions.size());
*/
    }

    @Override
    public void onBackPressed() {
        if (isOpen)
            closeSheet();
        else
        super.onBackPressed();
    }

    @Override
    public void onBottomMenuClick(BottomMenu bottomMenu) {


    }
}