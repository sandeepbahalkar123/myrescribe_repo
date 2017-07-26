package com.myrescribe.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myrescribe.R;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginMainTabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginMainTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginMainTabFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private ViewPagerAdapter mViewPagerAdapter;
    private Context mContext;

    public LoginMainTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUp.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginMainTabFragment newInstance(String param1, String param2) {
        LoginMainTabFragment fragment = new LoginMainTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.login_main_tab_fragment, container, false);
        ButterKnife.bind(this, inflate);
        mContext = this.getContext();
        setUpViewPager(mViewpager);
        mTabLayout.setupWithViewPager(mViewpager);

        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.recentblue));

        return inflate;
    }


    private void setUpViewPager(ViewPager viewPager) {
        mViewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        mViewPagerAdapter.addFragment(new SignUp(), getString(R.string.sign_up));
        mViewPagerAdapter.addFragment(new LogInApp(), getString(R.string.log_in));
        viewPager.setAdapter(mViewPagerAdapter);
        if (!MyRescribeConstants.BLANK.equalsIgnoreCase(MyRescribePreferencesManager.getString(getString(R.string.logout), mContext))) {
            viewPager.setCurrentItem(1);
        } else {
            viewPager.setCurrentItem(0);
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
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

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int currentItem = mViewpager.getCurrentItem();
        Fragment item = mViewPagerAdapter.getItem(currentItem);

        item.onActivityResult(requestCode, resultCode, data);

    }
}
