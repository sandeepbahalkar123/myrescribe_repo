package com.heinrichreimersoftware.materialdrawer.bottom_menu;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.heinrichreimersoftware.materialdrawer.R;

import java.util.ArrayList;

@SuppressWarnings("unused")
@SuppressLint("Registered")
public class BottomMenuActivity extends AppCompatActivity {

    private FrameLayout mFrame;
    private RecyclerView bottomMenuListRecyclerView;
    private BottomMenuAdapter bottomMenuAdapter;
    private int widthPixels;
    private ArrayList<BottomMenu> bottomMenus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.bottom_menu_activity);
        mFrame = (FrameLayout) findViewById(R.id.activityView);
        bottomMenuListRecyclerView = (RecyclerView) findViewById(R.id.bottomMenuListRecyclerView);
        widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        createBottomMenu();
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
    }

    public void addBottomMenu(BottomMenu bottomMenu) {
        bottomMenus.add(bottomMenu);
        bottomMenuAdapter.notifyItemInserted(bottomMenus.size());
    }
}