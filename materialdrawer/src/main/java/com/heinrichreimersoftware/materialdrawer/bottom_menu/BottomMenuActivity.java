package com.heinrichreimersoftware.materialdrawer.bottom_menu;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.heinrichreimersoftware.materialdrawer.R;
import com.heinrichreimersoftware.materialdrawer.app_logo.BottomSheetMenu;
import java.util.ArrayList;
import java.util.List;

import static com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuAdapter.appIconIndex;

@SuppressWarnings("unused")
@SuppressLint("Registered")
public class BottomMenuActivity extends AppCompatActivity implements BottomMenuAdapter.OnBottomMenuClickListener {

    private static final long ANIMATION_DUR = 300;
    private FrameLayout mFrame;
    private RecyclerView bottomMenuListRecyclerView;
    private BottomMenuAdapter bottomMenuAdapter;

    public ArrayList<BottomMenu> bottomMenus = new ArrayList<>();
    public ArrayList<BottomSheetMenu> bottomSheetMenus = new ArrayList<>();

    private RelativeLayout bottomSheetMenu;
    private FrameLayout bottomSheetMenuLayout;
    public boolean isOpen;
    private LinearLayout linearTableLayout;
    private int mPosition;
    private CircularImageView imageUrl;
    private TextView mPatientName;
    private TextView mMobileNumber;
    private ColorGenerator mColorGenerator;
    private TextView bottomSheetBadgeView;
    private TextView bottomMenuBadgeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.bottom_menu_activity);

        mFrame = (FrameLayout) findViewById(R.id.activityView);
        linearTableLayout = (LinearLayout) findViewById(R.id.table);
        bottomSheetMenuLayout = (FrameLayout) findViewById(R.id.bottomSheetMenuLayout);
        bottomSheetMenu = (RelativeLayout) findViewById(R.id.bottomSheetMenu);
        mPatientName = (TextView) findViewById(R.id.patientName);
        mMobileNumber = (TextView) findViewById(R.id.mobileNumber);
        bottomMenuListRecyclerView = (RecyclerView) findViewById(R.id.bottomMenuListRecyclerView);
        imageUrl = (CircularImageView) findViewById(R.id.imageUrl);
        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        mColorGenerator = ColorGenerator.MATERIAL;
        createBottomMenu();

        imageUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileImageClick();
            }
        });

        bottomSheetMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSheet();
            }
        });

        bottomSheetMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSheet();
            }
        });
    }

    public void openSheet() {

        isOpen = true;

        bottomSheetMenu.setVisibility(View.VISIBLE);
        bottomSheetMenuLayout.setVisibility(View.VISIBLE);

        AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(ANIMATION_DUR);
        bottomSheetMenu.startAnimation(animation1);

        Animation slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up_animation);
        bottomSheetMenuLayout.startAnimation(slideUpAnimation);
    }

    public void setUpAdapterForBottomSheet(String patientImageUrl, String patientName, String patientMobileNo) {

        mMobileNumber.setText("+91 - " + patientMobileNo);
        mPatientName.setText(patientName);

        int color2 = mColorGenerator.getColor(patientName);
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(Math.round(getResources().getDimension(R.dimen.dp40))) // width in px
                .height(Math.round(getResources().getDimension(R.dimen.dp40)))
                .useFont(Typeface.defaultFromStyle(Typeface.BOLD))// height in px
                .endConfig()
                .buildRound(("" + patientName.charAt(0)).toUpperCase(), color2);
        imageUrl.setImageDrawable(drawable);

        linearTableLayout.removeAllViews();

        List<BottomSheetMenu> bottomSheetMenus = new ArrayList<>();
        int size = this.bottomSheetMenus.size();

        for (int position = 0; position < size; position++) {
            mPosition = position;
            bottomSheetMenus.add(this.bottomSheetMenus.get(position));
            if (bottomSheetMenus.size() == 3 && position < 3) {
                linearTableLayout.addView(addTableRow(bottomSheetMenus, position, getResources().getDimensionPixelSize(R.dimen.dp28)));
                bottomSheetMenus.clear();
            } else if (bottomSheetMenus.size() == 2 && position >= 3) {
                linearTableLayout.addView(addTableRow(bottomSheetMenus, position, getResources().getDimensionPixelSize(R.dimen.dp45)));
                bottomSheetMenus.clear();
            }
        }
    }

    private View addTableRow(final List<BottomSheetMenu> bottomSheetMenus, final int groupPosition, int padding) {
        LinearLayout layout2 = new LinearLayout(this);
        layout2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout2.setOrientation(LinearLayout.HORIZONTAL);
        layout2.setPadding(padding, 0, padding, 0);

        for (int i = 0; i < bottomSheetMenus.size(); i++) {
            final BottomSheetMenu bottomSheetMenu = bottomSheetMenus.get(i);
            View item = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_menu_item_list, layout2, false);

            TextView bottomMenuName = (TextView) item.findViewById(R.id.menuName);
            ImageView menuBottomIcon = (ImageView) item.findViewById(R.id.menuImage);
            TextView badgeView = (TextView) item.findViewById(R.id.showCount);
            LinearLayout bottomSheetLayout = (LinearLayout) item.findViewById(R.id.bottomSheetLayout);
            final int finali = mPosition;
            bottomMenuName.setText(bottomSheetMenus.get(i).getName());

            if (bottomSheetMenus.get(i).getName().equalsIgnoreCase(getResources().getString(R.string.notifications))) {
                this.bottomSheetBadgeView = badgeView;
                if (bottomSheetMenus.get(i).getNotificationCount() > 0) {
                    badgeView.setVisibility(View.VISIBLE);
                    badgeView.setText(String.valueOf(bottomSheetMenus.get(i).getNotificationCount()));
                } else badgeView.setVisibility(View.GONE);
            } else badgeView.setVisibility(View.GONE);

            bottomSheetLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBottomSheetMenuClick(bottomSheetMenu);
                }
            });

            menuBottomIcon.setImageDrawable(bottomSheetMenu.getIconImageUrl());

            layout2.addView(item);
        }
        return layout2;
    }

    public void setBadgeCount(int count) {
        if (bottomSheetBadgeView != null && count > 0) {
            bottomSheetBadgeView.setVisibility(View.VISIBLE);
            bottomSheetBadgeView.setText(String.valueOf(count));
        }else{
            bottomSheetBadgeView.setVisibility(View.GONE);
            bottomSheetBadgeView.setText(String.valueOf(count));
        }

        bottomMenus.get(appIconIndex).setNotificationCount(count);
        bottomMenuAdapter.notifyDataSetChanged();
    }

    public void closeSheet() {

        isOpen = false;

        Animation slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down_animation);

        AlphaAnimation animation1 = new AlphaAnimation(1.0f, 0.0f);
        animation1.setDuration(ANIMATION_DUR);
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

        bottomMenuListRecyclerView.setLayoutManager(layoutManager);
        bottomMenuListRecyclerView.setAdapter(bottomMenuAdapter);


    }

    public void addBottomMenu(BottomMenu bottomMenu) {
        bottomMenus.add(bottomMenu);
        bottomMenuAdapter.notifyItemInserted(bottomMenus.size() - 1);
    }

    public void addBottomSheetMenu(BottomSheetMenu bottomSheetMenu) {
        bottomSheetMenus.add(bottomSheetMenu);
    }

    @Override
    public void onBackPressed() {

        if (isOpen)
            closeSheet();
        else
            super.onBackPressed();
    }

    @Override
    public void onBottomSheetMenuClick(BottomSheetMenu bottomMenu) {
        if (isOpen)
            closeSheet();
    }

    @Override
    public void onBottomMenuClick(BottomMenu bottomMenu) {
        if (bottomMenu.getMenuName().equalsIgnoreCase(getString(R.string.app_logo))) {
            if (isOpen)
                closeSheet();
            else
                openSheet();
        } else if (isOpen) {
            closeSheet();
        }
    }

    @Override
    public void onProfileImageClick() {

    }

    public void doNotifyDataSetChanged() {
        bottomMenuAdapter.notifyDataSetChanged();
    }
}