package com.rescribe.ui.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.helpers.doctor_connect.DoctorConnectSearchHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.chat.MQTTMessage;
import com.rescribe.model.doctor_connect.ChatDoctor;
import com.rescribe.model.doctor_connect_search.DoctorConnectSearchBaseModel;
import com.rescribe.model.doctor_connect_search.SearchDataModel;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.services.MQTTService;
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

import static com.rescribe.services.MQTTService.MESSAGE_TOPIC;
import static com.rescribe.services.MQTTService.NOTIFY;
import static com.rescribe.services.MQTTService.TOPIC;


/**
 * Created by jeetal on 5/9/17.
 */

public class DoctorConnectActivity extends AppCompatActivity implements DoctorConnectSearchContainerFragment.OnAddFragmentListener, SearchBySpecializationOfDoctorFragment.OnAddFragmentListener, HelperResponse {

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
    private String patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_connect);
        ButterKnife.bind(this);
        mFragmentTitleList[0] = getString(R.string.chats);
        mFragmentTitleList[1] = getString(R.string.connect);
        mFragmentTitleList[2] = getString(R.string.search);

        patientId = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, DoctorConnectActivity.this);

        setupViewPager();
        mTabsDoctorConnect.setupWithViewPager(mDoctorConnectViewpager);
        initialize();
    }

    private void initialize() {
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
                if (SPECIALIZATION_DOCTOR_FRAGMENT.equalsIgnoreCase(mFragmentLoaded) && mSearchView.getText().toString().trim().length() != 0) {
                    addSearchDoctorByNameFragment(null);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            searchDoctorByNameFragment.setOnClickOfSearchBar(mSearchView.getText().toString());
                        }
                    }, 500);
                } else {
                    //search Bar Code
                    if (searchDoctorByNameFragment != null) {
                        searchDoctorByNameFragment.setOnClickOfSearchBar(mSearchView.getText().toString());
                    }
                }
            }
        };
    }

    private void setupViewPager() {

        //Api call to get getDoctorSpeciality
        DoctorConnectSearchHelper doctorConnectSearchHelper = new DoctorConnectSearchHelper(this, this);
        doctorConnectSearchHelper.getDoctorSpecialityList();
        //Doctor connect , chat and search fragment loaded here
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
        mSearchView.setText("");
        onBackPressed();
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

    //TODO: parceable has to be used to getSpecialityOFDoctorList
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

    //Call Back from SearchBySpecializationOfDoctor
    public void addSearchDoctorByNameFragment(Bundle bundleData) {
        mFragmentLoaded = SPECIALIZATION_DOCTOR_FRAGMENT_BYNAME;
        if (bundleData != null) {
            mSearchView.setText("" + bundleData.getString(getString(R.string.clicked_item_data)));
        }
        searchDoctorByNameFragment = SearchDoctorByNameFragment.newInstance(getmChatDoctors(), bundleData);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, searchDoctorByNameFragment);
        fragmentTransaction.commit();
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
        registerReceiver(receiver, new IntentFilter(
                MQTTService.NOTIFY));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            ChatDoctor chatDoctor = data.getParcelableExtra(RescribeConstants.CHAT_USERS);
            doctorConnectChatFragment.addItem(chatDoctor);
        }
    }
}
