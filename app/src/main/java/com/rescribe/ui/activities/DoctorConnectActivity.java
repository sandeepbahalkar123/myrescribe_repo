package com.rescribe.ui.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.heinrichreimersoftware.materialdrawer.app_logo.BottomSheetMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuActivity;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuAdapter;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.CircularImageView;
import com.rescribe.BuildConfig;
import com.rescribe.R;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.database.MyRecordsData;
import com.rescribe.helpers.doctor_connect.DoctorConnectSearchHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.chat.MQTTMessage;
import com.rescribe.model.dashboard_api.DashboardBottomMenuList;
import com.rescribe.model.doctor_connect.ChatDoctor;
import com.rescribe.model.doctor_connect_search.DoctorConnectSearchBaseModel;
import com.rescribe.model.doctor_connect_search.SearchDataModel;
import com.rescribe.model.investigation.Image;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.services.MQTTService;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.activities.dashboard.SettingsActivity;
import com.rescribe.ui.activities.dashboard.UnreadNotificationMessageActivity;
import com.rescribe.ui.activities.doctor.DoctorListActivity;
import com.rescribe.ui.activities.saved_articles.SavedArticlesActivity;
import com.rescribe.ui.activities.vital_graph.VitalGraphActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.customesViews.EditTextWithDeleteButton;
import com.rescribe.ui.fragments.doctor_connect.DoctorConnectChatFragment;
import com.rescribe.ui.fragments.doctor_connect.DoctorConnectFragment;
import com.rescribe.ui.fragments.doctor_connect.DoctorConnectSearchContainerFragment;
import com.rescribe.ui.fragments.doctor_connect.SearchBySpecializationOfDoctorFragment;
import com.rescribe.ui.fragments.doctor_connect.SearchDoctorByNameFragment;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuAdapter.connectIndex;
import static com.rescribe.services.MQTTService.MESSAGE_TOPIC;
import static com.rescribe.services.MQTTService.NOTIFY;
import static com.rescribe.services.MQTTService.TOPIC;
import static com.rescribe.util.RescribeConstants.BOTTOM_MENU.APP_LOGO;
import static com.rescribe.util.RescribeConstants.BOTTOM_MENU.BOOK;
import static com.rescribe.util.RescribeConstants.BOTTOM_MENU.CONNECT;
import static com.rescribe.util.RescribeConstants.BOTTOM_MENU.HOME;
import static com.rescribe.util.RescribeConstants.BOTTOM_MENU.SETTINGS;
import static com.rescribe.util.RescribeConstants.BOTTOM_MENUS;
import static com.rescribe.util.RescribeConstants.DRAWABLE;
import static com.rescribe.util.RescribeConstants.SALUTATION;


/**
 * Created by jeetal on 5/9/17.
 */

public class DoctorConnectActivity extends BottomMenuActivity implements DoctorConnectSearchContainerFragment.OnAddFragmentListener, SearchBySpecializationOfDoctorFragment.OnAddFragmentListener, HelperResponse, BottomMenuAdapter.OnBottomMenuClickListener {

    private final static String TAG = "DoctorConnect";
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() != null) {
                if (intent.getAction().equals(NOTIFY)) {

                    String topic = intent.getStringExtra(MQTTService.TOPIC_KEY);

                    if (intent.getBooleanExtra(MQTTService.DELIVERED, false)) {

                        Log.d(TAG, "Delivery Complete");
                        Log.d(TAG + " MSG_ID", intent.getStringExtra(MQTTService.MESSAGE_ID));

                    } else if (topic.equals(TOPIC[MESSAGE_TOPIC])) {

                        // User message
                        CommonMethods.Log(TAG, "User message");
                        MQTTMessage message = intent.getParcelableExtra(MQTTService.MESSAGE);
                        doctorConnectChatFragment.notifyCount(message);

                    }
                }
            }
        }
    };

    public static final int PAID = 1;
    public static final int FREE = 0;

    @BindView(R.id.backButton)
    ImageView mBackButton;
    @BindView(R.id.tabsDoctorConnect)
    TabLayout mTabsDoctorConnect;
    @BindView(R.id.doctorConnectViewpager)
    ViewPager mDoctorConnectViewpager;
    String[] mFragmentTitleList = new String[3];
    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.searchView)
    EditTextWithDeleteButton mSearchView;
    @BindView(R.id.whiteUnderLine)
    TextView mWhiteUnderLine;
    private static final String SPECIALIZATION_DOCTOR_FRAGMENT = "SpecializationOfDoctorFragment";
    private static final String SPECIALIZATION_DOCTOR_FRAGMENT_BYNAME = "SearchDoctorByNameFragment";

    private DoctorConnectChatFragment doctorConnectChatFragment;
    private SearchBySpecializationOfDoctorFragment searchBySpecializationOfDoctorFragment;

    private SearchDoctorByNameFragment searchDoctorByNameFragment;
    private SearchDataModel searchDataModel;
    private String mFragmentLoaded;
    private ArrayList<ChatDoctor> mChatDoctors;

    ArrayList<DashboardBottomMenuList> dashboardBottomMenuLists;
    private UpdateAppUnreadNotificationCount mUpdateAppUnreadNotificationCount = new UpdateAppUnreadNotificationCount();
    private AppDBHelper appDBHelper;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_connect);
        ButterKnife.bind(this);
        mContext = this;
        mFragmentTitleList[0] = getString(R.string.chats);
        mFragmentTitleList[1] = getString(R.string.connect);
        mFragmentTitleList[2] = getString(R.string.search);

        setupViewPager();
        mTabsDoctorConnect.setupWithViewPager(mDoctorConnectViewpager);
        initialize();
    }

    private void initialize() {
        appDBHelper = new AppDBHelper(this);
        addBottomMenus();

        mSearchView.addTextChangedListener(editTextChanged());
        mSearchView.addClearTextButtonListener(new EditTextWithDeleteButton.OnClearButtonClickedInEditTextListener() {
            @Override
            public void onClearButtonClicked() {
                mFragmentLoaded = SPECIALIZATION_DOCTOR_FRAGMENT;
                searchBySpecializationOfDoctorFragment = SearchBySpecializationOfDoctorFragment.newInstance(searchDataModel.getDoctorSpecialities());
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, searchBySpecializationOfDoctorFragment);
                fragmentTransaction.commit();
            }
        });

        mTabsDoctorConnect.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = mTabsDoctorConnect.getSelectedTabPosition();
                if (tabPosition == 2) {
                    mSearchView.setVisibility(View.VISIBLE);
                    mWhiteUnderLine.setVisibility(View.VISIBLE);
                    title.setVisibility(View.GONE);
                } else {
                    mSearchView.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabPosition = mTabsDoctorConnect.getSelectedTabPosition();
                if (tabPosition == 2) {
                    title.setVisibility(View.GONE);
                    mSearchView.setVisibility(View.VISIBLE);
                    mWhiteUnderLine.setVisibility(View.VISIBLE);
                } else {
                    mSearchView.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int tabPosition = mTabsDoctorConnect.getSelectedTabPosition();
                if (tabPosition == 2) {
                    mSearchView.setVisibility(View.VISIBLE);
                    mWhiteUnderLine.setVisibility(View.VISIBLE);
                    title.setVisibility(View.GONE);
                } else {
                    mSearchView.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void addBottomMenus() {

        int notificationCount = RescribePreferencesManager.getInt(RescribeConstants.NOTIFICATION_COUNT, this);//appCount + invCount + medCount;// + tokCount;

        dashboardBottomMenuLists = getIntent().getParcelableArrayListExtra(BOTTOM_MENUS);

        if (dashboardBottomMenuLists != null) {

            bottomSheetMenus.clear();
            bottomMenus.clear();

            for (int i = 0; i < dashboardBottomMenuLists.size(); i++) {

                DashboardBottomMenuList dashboardBottomMenuList = dashboardBottomMenuLists.get(i);

                BottomMenu bottomMenu = new BottomMenu();
                int resourceId = getResources().getIdentifier(dashboardBottomMenuList.getIconImageUrl(), DRAWABLE, BuildConfig.APPLICATION_ID);
                if (resourceId > 0)
                    bottomMenu.setMenuIcon(getResources().getDrawable(resourceId));
                else
                    CommonMethods.Log(TAG, "Resource does not exist");
                bottomMenu.setMenuName(dashboardBottomMenuList.getName());
                bottomMenu.setAppIcon(dashboardBottomMenuList.getName().equals(APP_LOGO));
                bottomMenu.setNotificationCount(notificationCount);

                if (dashboardBottomMenuList.getName().equals(CONNECT))
                    bottomMenu.setSelected(dashboardBottomMenuList.getName().equals(CONNECT));

                addBottomMenu(bottomMenu);

                if (dashboardBottomMenuLists.get(i).getName().equals(APP_LOGO)) {

                    for (int j = 0; j < dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions().size(); j++) {

                        if (!dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions().get(j).getName().equalsIgnoreCase(getString(R.string.profile))) {
                            BottomSheetMenu bottomSheetMenu = new BottomSheetMenu();
                            bottomSheetMenu.setName(dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions().get(j).getName());

                            int resourceIdProfile = getResources().getIdentifier(dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions().get(j).getIconImageUrl(), DRAWABLE, BuildConfig.APPLICATION_ID);
                            if (resourceIdProfile > 0)
                                bottomSheetMenu.setIconImageUrl(getResources().getDrawable(resourceIdProfile));
                            else
                                CommonMethods.Log(TAG, "Resource does not exist");

                            bottomSheetMenu.setNotificationCount(notificationCount);

                            addBottomSheetMenu(bottomSheetMenu);
                        }
                    }
                } else if (dashboardBottomMenuLists.get(i).getName().equalsIgnoreCase(CONNECT))
                    connectIndex = i;
            }

            String userName = RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.USER_NAME, this);
            String salutation = RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.SALUTATION, this);

            String salutationText = "";


            salutationText = SALUTATION[Integer.parseInt(salutation)];

            setUpAdapterForBottomSheet(RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PROFILE_PHOTO, mContext), userName, RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.MOBILE_NUMBER, this), salutationText);
        }
    }

    private EditTextWithDeleteButton.TextChangedListener editTextChanged() {
        return new EditTextWithDeleteButton.TextChangedListener() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mSearchView.getText().toString().trim().length() != 0) {

                    if (SPECIALIZATION_DOCTOR_FRAGMENT.equalsIgnoreCase(mFragmentLoaded))
                        addSearchDoctorByNameFragment(null);

                    searchDoctorByNameFragment.setOnClickOfSearchBar(mSearchView.getText().toString());

                } else if (mSearchView.getText().toString().trim().length() == 0) {

                    mFragmentLoaded = SPECIALIZATION_DOCTOR_FRAGMENT;
                    searchBySpecializationOfDoctorFragment = SearchBySpecializationOfDoctorFragment.newInstance(searchDataModel.getDoctorSpecialities());
                    FragmentManager supportFragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, searchBySpecializationOfDoctorFragment);
                    fragmentTransaction.commit();
                }
            }
        };
    }

    private void setupViewPager() {
        // Api call to get getDoctorSpeciality
        DoctorConnectSearchHelper doctorConnectSearchHelper = new DoctorConnectSearchHelper(this, this);
        doctorConnectSearchHelper.getDoctorSpecialityList();
        // Doctor connect , chat and search fragment loaded here
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        doctorConnectChatFragment = DoctorConnectChatFragment.newInstance();
        DoctorConnectFragment doctorConnectFragment = DoctorConnectFragment.newInstance();
        DoctorConnectSearchContainerFragment doctorConnectSearchContainerFragment = new DoctorConnectSearchContainerFragment();
        adapter.addFragment(doctorConnectChatFragment, getString(R.string.chats));
        adapter.addFragment(doctorConnectFragment, getString(R.string.connect));
        adapter.addFragment(doctorConnectSearchContainerFragment, getString(R.string.search));
        mDoctorConnectViewpager.setAdapter(adapter);
    }

    @OnClick(R.id.backButton)
    public void onViewClicked() {
        if (SPECIALIZATION_DOCTOR_FRAGMENT.equalsIgnoreCase(mFragmentLoaded)) {
            mFragmentLoaded = SPECIALIZATION_DOCTOR_FRAGMENT_BYNAME;
        } else if (SPECIALIZATION_DOCTOR_FRAGMENT_BYNAME.equalsIgnoreCase(mFragmentLoaded)) {
            mFragmentLoaded = SPECIALIZATION_DOCTOR_FRAGMENT;
        }

        super.onBackPressed();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_DOCTOR__FILTER_DOCTOR_SPECIALITY_LIST)) {
            DoctorConnectSearchBaseModel doctorConnectSearchBaseModel = (DoctorConnectSearchBaseModel) customResponse;
            searchDataModel = doctorConnectSearchBaseModel.getSearchDataModel();
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


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public interface OnClickOfSearchBar {
        void setOnClickOfSearchBar(String searchText);
    }

    @Override
    public String getSearchText() {
        return mSearchView.getText().toString();
    }

    //Call Back from DoctorConnectSearchContainer
    public void addSpecializationOfDoctorFragment(Bundle bundleData) {
        // Show speciality of Doctor fragment loaded
        mFragmentLoaded = SPECIALIZATION_DOCTOR_FRAGMENT;
        if (searchDataModel == null) {
            searchDataModel = new SearchDataModel();
        }
        searchBySpecializationOfDoctorFragment = SearchBySpecializationOfDoctorFragment.newInstance(searchDataModel.getDoctorSpecialities());
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, searchBySpecializationOfDoctorFragment);
        fragmentTransaction.commit();

    }

    // Call Back from SearchBySpecializationOfDoctor
    public void addSearchDoctorByNameFragment(Bundle bundleData) {
        mFragmentLoaded = SPECIALIZATION_DOCTOR_FRAGMENT_BYNAME;

        searchDoctorByNameFragment = SearchDoctorByNameFragment.newInstance(getmChatDoctors(), bundleData);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, searchDoctorByNameFragment);
        fragmentTransaction.commit();

        if (bundleData != null)
            mSearchView.setText("" + bundleData.getString(RescribeConstants.ITEM_DATA));

    }

    public ArrayList<ChatDoctor> getmChatDoctors() {
        return mChatDoctors;
    }

    public void setmChatDoctors(ArrayList<ChatDoctor> mChatDoctors) {
        this.mChatDoctors = mChatDoctors;
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(receiver, new IntentFilter(MQTTService.NOTIFY));
        registerReceiver(mUpdateAppUnreadNotificationCount, new IntentFilter(getString(R.string.unread_notification_update_received)));

        int unreadMessageCount = appDBHelper.unreadMessageCount();
        setConnectBadgeCount(unreadMessageCount);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        unregisterReceiver(mUpdateAppUnreadNotificationCount);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            ChatDoctor chatDoctor = data.getParcelableExtra(RescribeConstants.CHAT_USERS);
            doctorConnectChatFragment.addItem(chatDoctor);
        }
    }

    private class UpdateAppUnreadNotificationCount extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals(getString(R.string.unread_notification_update_received))) {
                    int notificationCount = RescribePreferencesManager.getInt(RescribeConstants.NOTIFICATION_COUNT, context);//appCount + invCount + medCount;// + tokCount;
                    setBadgeCount(notificationCount);
                } else CommonMethods.Log(TAG, "Other Broadcast");
            } else CommonMethods.Log(TAG, "Other Broadcast");
        }
    }

    @Override
    public void onBottomMenuClick(BottomMenu bottomMenu) {

        String menuName = bottomMenu.getMenuName();

        if (menuName.equalsIgnoreCase(HOME)) {
            finish();
        } else if (menuName.equalsIgnoreCase(SETTINGS)) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();
        } else if (menuName.equalsIgnoreCase(BOOK)) {
            Intent intent = new Intent(this, BookAppointDoctorListBaseActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            Bundle bundle = new Bundle();
            bundle.putString(RescribeConstants.CALL_FROM_DASHBOARD, "");
            bundle.putString(RescribeConstants.ITEM_DATA, getString(R.string.doctorss));
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }

        super.onBottomMenuClick(bottomMenu);
    }

    @Override
    public void onProfileImageClick(CircularImageView profileImageView) {
//        Intent intent = new Intent(this, ProfileActivity.class);
//        startActivity(intent);

        super.onProfileImageClick(profileImageView);
    }

    @Override
    public void onBottomSheetMenuClick(BottomSheetMenu bottomMenu) {
        if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.vital_graph))) {
            Intent intent = new Intent(this, VitalGraphActivity.class);
            startActivity(intent);

        } else if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.notification) + "s")) {
            Intent intent = new Intent(this, UnreadNotificationMessageActivity.class);
            startActivity(intent);

            RescribePreferencesManager.putInt(RescribeConstants.NOTIFICATION_COUNT, 0, this);
            setBadgeCount(0);

        } else if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.my_records))) {
            MyRecordsData myRecordsData = appDBHelper.getMyRecordsData();
            int completeCount = 0;
            for (Image image : myRecordsData.getImageArrayList()) {
                if (image.isUploading() == RescribeConstants.COMPLETED)
                    completeCount++;
            }
            Intent intent;
            if (completeCount == myRecordsData.getImageArrayList().size()) {
                appDBHelper.deleteMyRecords();
                intent = new Intent(mContext, MyRecordsActivity.class);
            } else {
                intent = new Intent(mContext, SelectedRecordsGroupActivity.class);
                intent.putExtra(RescribeConstants.UPLOADING_STATUS, true);
                intent.putExtra(RescribeConstants.VISIT_DATE, myRecordsData.getVisitDate());
                intent.putExtra(RescribeConstants.OPD_ID, myRecordsData.getDocId());
                intent.putExtra(RescribeConstants.DOCTORS_ID, myRecordsData.getDocId());
                intent.putExtra(RescribeConstants.DOCUMENTS, myRecordsData.getImageArrayList());
            }
            startActivity(intent);
        } else if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.on_going_treatment))) {
            Intent intent = new Intent(mContext, PrescriptionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(RescribeConstants.ITEM_DATA_VALUE, bottomMenu.getName());
            intent.putExtras(bundle);
            startActivity(intent);
        }
        if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.doctor_details))) {
            Intent intent = new Intent(mContext, DoctorListActivity.class);
            startActivity(intent);
        } else if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.my_appointments))) {
            Intent intent = new Intent(mContext, AppointmentActivity.class);
            intent.putExtra(RescribeConstants.CALL_FROM_DASHBOARD, "");
            startActivity(intent);
        } else if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.saved_articles))) {
            Intent intent = new Intent(mContext, SavedArticlesActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(RescribeConstants.ITEM_DATA, bottomMenu.getName());
            intent.putExtras(bundle);
            startActivity(intent);
        }

        super.onBottomSheetMenuClick(bottomMenu);
    }
}
