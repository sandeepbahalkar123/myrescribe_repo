package com.rescribe.ui.fragments.book_appointment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.adapters.DoctorSpecialistBookAppointmentAdapter;
import com.rescribe.adapters.book_appointment.ShowRecentVisitedDoctorPagerAdapter;
import com.rescribe.adapters.book_appointment.BookAppointFilteredDocList;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.book_appointment.doctor_data.DoctorServicesModel;
import com.rescribe.model.book_appointment.doctor_data.DoctorSpeciality;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.activities.book_appointment.MapActivityPlotNearByDoctor;
import com.rescribe.ui.customesViews.CircleIndicator;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.customesViews.EditTextWithDeleteButton;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import droidninja.filepicker.utils.GridSpacingItemDecoration;

import static com.facebook.FacebookSdk.getApplicationContext;


public class RecentVisitDoctorFragment extends Fragment implements DoctorSpecialistBookAppointmentAdapter.OnSpecialityClickListener, HelperResponse, BookAppointFilteredDocList.OnFilterDocListClickListener, BookAppointDoctorListBaseActivity.AddUpdateViewDataListener {

    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.circleIndicator)
    CircleIndicator mCircleIndicator;
    @BindView(R.id.pickSpeciality)
    CustomTextView pickSpeciality;
    @BindView(R.id.emptyListView)
    RelativeLayout mFilterDocListEmptyListView;
    @BindView(R.id.specialityEmptyListView)
    RelativeLayout mSpecialityEmptyListView;
    @BindView(R.id.fragmentContainer)
    RelativeLayout fragmentContainer;
    @BindView(R.id.doubtMessage)
    RelativeLayout doubtMessage;
    @BindView(R.id.recyclerViewLinearLayout)
    LinearLayout recyclerViewLinearLayout;
    @BindView(R.id.searchView)
    EditTextWithDeleteButton searchView;
    @BindView(R.id.searchBarLinearLayout)
    LinearLayout searchBarLinearLayout;
    @BindView(R.id.listView)
    RecyclerView showDoctorsRecyclerView;
    @BindView(R.id.recentDoctorLayout)
    LinearLayout recentDoctorLayout;
    @BindView(R.id.prevBtn)
    ImageView prevBtn;
    @BindView(R.id.nextBtn)
    ImageView nextBtn;
    @BindView(R.id.bookAppointSpecialityListView)
    RecyclerView mBookAppointSpecialityListView;
    @BindView(R.id.whiteUnderLine)
    TextView whiteUnderLine;
    @BindView(R.id.clickHere)
    CustomTextView mClickHere;
    @BindView(R.id.rightFab)
    FloatingActionButton rightFab;
    @BindView(R.id.leftFab)
    FloatingActionButton leftFab;
    private View mRootView;
    Unbinder unbinder;
    BookAppointmentBaseModel bookAppointmentBaseModel;
    DoctorSpecialistBookAppointmentAdapter mDoctorConnectSearchAdapter;
    private BookAppointFilteredDocList mBookAppointFilteredDocListAdapter;
    private int currentPage = 0;
    private int totalPages;
    CustomResponse customResponse;
    private String mReceivedToolBarTitle;

    public RecentVisitDoctorFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.recent_visit_doctor, container, false);
        //  hideSoftKeyboard();
        unbinder = ButterKnife.bind(this, mRootView);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        init();
        return mRootView;

    }

    private void init() {
        //----------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mClickHere.setText(Html.fromHtml(getString(R.string.clickhere), Html.FROM_HTML_MODE_LEGACY));
        } else {
            mClickHere.setText(Html.fromHtml(getString(R.string.clickhere)));
        }
        //----------
        if (getArguments() != null) {
            mReceivedToolBarTitle = getArguments().getString(getString(R.string.title));
            BookAppointDoctorListBaseActivity.setToolBarTitle(mReceivedToolBarTitle, true);
        }
        //-----------
        searchView.addClearTextButtonListener(new EditTextWithDeleteButton.OnClearButtonClickedInEditTextListener() {
            @Override
            public void onClearButtonClicked() {
                isDataListViewVisible(false, false);
            }
        });

        searchView.addKeyboardDoneKeyPressedInEditTextListener(new EditTextWithDeleteButton.OnKeyboardDoneKeyPressedInEditTextListener() {
            @Override
            public void onKeyPressed(int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    CommonMethods.hideKeyboard(getActivity());
                }
            }
        });
        searchView.addTextChangedListener(new EditTextWithDeleteButton.TextChangedListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0) {
                    mBookAppointFilteredDocListAdapter.getFilter().filter(s);
                } else {
                    isDataListViewVisible(false, false);
                }
            }
        });
        updateViewData();

    }

    public static RecentVisitDoctorFragment newInstance(Bundle b) {
        RecentVisitDoctorFragment fragment = new RecentVisitDoctorFragment();
        Bundle args = b;
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setOnClickOfDoctorSpeciality(Bundle bundleData) {
        BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
        activity.loadFragment(BookAppointFilteredDoctorListFragment.newInstance(bundleData), true);
    }

    @OnClick({R.id.viewpager, R.id.circleIndicator, R.id.doubtMessage, R.id.prevBtn, R.id.nextBtn, R.id.rightFab, R.id.leftFab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.viewpager:
                break;
            case R.id.prevBtn:
                currentPage -= 1;
                mDoctorConnectSearchAdapter = new DoctorSpecialistBookAppointmentAdapter(getActivity(), this, generatePage(currentPage, bookAppointmentBaseModel.getDoctorServicesModel().getDoctorSpecialities()));
                mBookAppointSpecialityListView.setAdapter(mDoctorConnectSearchAdapter);
                toggleButtons(bookAppointmentBaseModel.getDoctorServicesModel().getDoctorSpecialities());
                // listView.getLayoutManager().scrollToPosition(linearLayoutManager.findLastVisibleItemPosition() + 1);
                break;
            case R.id.doubtMessage:
                BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
                activity.loadFragment(ComplaintsFragment.newInstance(new Bundle()), false);
                break;
            case R.id.nextBtn:
                currentPage += 1;
                mDoctorConnectSearchAdapter = new DoctorSpecialistBookAppointmentAdapter(getActivity(), this, generatePage(currentPage, bookAppointmentBaseModel.getDoctorServicesModel().getDoctorSpecialities()));
                mBookAppointSpecialityListView.setAdapter(mDoctorConnectSearchAdapter);
                toggleButtons(bookAppointmentBaseModel.getDoctorServicesModel().getDoctorSpecialities());
                break;
            case R.id.rightFab:
                activity = (BookAppointDoctorListBaseActivity) getActivity();
                activity.getActivityDrawerLayout().openDrawer(GravityCompat.END);
                break;
            case R.id.leftFab:
                Intent intent = new Intent(getActivity(), MapActivityPlotNearByDoctor.class);
                intent.putParcelableArrayListExtra(getString(R.string.doctor_data), bookAppointmentBaseModel.getDoctorServicesModel().getDoctorList());
                intent.putExtra(getString(R.string.toolbarTitle), mReceivedToolBarTitle);
                startActivity(intent);
              /*  activity = (BookAppointDoctorListBaseActivity) getActivity();
                activity.loadFragment(ShowNearByDoctorsOnMapFragment.newInstance(new Bundle()), false);*/
                break;
        }

    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse c) {
        this.customResponse = c;
    }

    @Override
    public void onResume() {
        super.onResume();
        CommonMethods.Log("REcenet", "onresume");
    }


    private void setDoctorListAdapter(BookAppointmentBaseModel bookAppointmentBaseModel) {
        if(bookAppointmentBaseModel==null){
            isDataListViewVisible(false,true);
        }else {
            DoctorServicesModel doctorServicesModel = bookAppointmentBaseModel.getDoctorServicesModel();

            //-------
            if (doctorServicesModel.getRecentlyVisitedAndFavoriteDoctorList().size() == 0) {
                mViewpager.setVisibility(View.GONE);
                mCircleIndicator.setVisibility(View.GONE);
            } else {
                mViewpager.setVisibility(View.VISIBLE);
                mViewpager.setAdapter(new ShowRecentVisitedDoctorPagerAdapter(getActivity(), doctorServicesModel.getRecentlyVisitedAndFavoriteDoctorList()));
                mCircleIndicator.setViewPager(mViewpager);
            }
            //------
            //----- to set doc data list, invisible by default -----
            isDataListViewVisible(false, false);
            ArrayList<DoctorList> doctorList = doctorServicesModel.getDoctorList();
            if (doctorList.size() > 0) {
                mBookAppointFilteredDocListAdapter = new BookAppointFilteredDocList(getActivity(), doctorList, RecentVisitDoctorFragment.this, RecentVisitDoctorFragment.this);
                LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                showDoctorsRecyclerView.setLayoutManager(linearlayoutManager);
                showDoctorsRecyclerView.setHasFixedSize(true);
                showDoctorsRecyclerView.setAdapter(mBookAppointFilteredDocListAdapter);
            }
            //-------------

            if (doctorServicesModel.getDoctorSpecialities().size() == 0) {
                pickSpeciality.setVisibility(View.GONE);
                doubtMessage.setVisibility(View.GONE);
                mSpecialityEmptyListView.setVisibility(View.VISIBLE);
                prevBtn.setVisibility(View.INVISIBLE);
                nextBtn.setVisibility(View.INVISIBLE);
                mBookAppointSpecialityListView.setVisibility(View.GONE);
            } else {
                recyclerViewLinearLayout.setVisibility(View.VISIBLE);
                mBookAppointSpecialityListView.setVisibility(View.VISIBLE);
          /*  prevBtn.setVisibility(View.INVISIBLE);
            prevBtn.setEnabled(false);*/
                prevBtn.setVisibility(View.GONE);
                prevBtn.setEnabled(false);
                nextBtn.setVisibility(View.VISIBLE);
                whiteUnderLine.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                mSpecialityEmptyListView.setVisibility(View.GONE);

                mBookAppointSpecialityListView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
                mBookAppointSpecialityListView.setLayoutManager(layoutManager);
                mBookAppointSpecialityListView.setItemAnimator(new DefaultItemAnimator());
                int spanCount = 3; // 3 columns
                int spacing = 30; // 50px
                boolean includeEdge = true;
                mBookAppointSpecialityListView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
                mDoctorConnectSearchAdapter = new DoctorSpecialistBookAppointmentAdapter(getActivity(), this, generatePage(currentPage, bookAppointmentBaseModel.getDoctorServicesModel().getDoctorSpecialities()));
                // mBookAppointSpecialityListView.setAdapter(mDoctorConnectSearchAdapter);
                mBookAppointSpecialityListView.setAdapter(mDoctorConnectSearchAdapter);
                pickSpeciality.setVisibility(View.VISIBLE);
                doubtMessage.setVisibility(View.VISIBLE);
            }
        }

        //---set data ---------
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

    @Override
    public void onClickOfDoctorRowItem(Bundle bundleData) {
        DoctorList doctorList = (DoctorList) bundleData.getParcelable(getString(R.string.clicked_item_data));
        //TODO: This is done as per requirement, need to set "DOCTOR" as toolbarHeader instead respective doc speciality.
        bundleData.putString(getString(R.string.toolbarTitle), getString(R.string.doctor));//doctorList.getSpeciality()

        BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
        activity.loadFragment(BookAppointDoctorDescriptionFragment.newInstance(bundleData), false);
    }

    /*
     * GENERATE A SINGLE PAGE DATA
     * PASS US THE CURRENT PAGE POSITION THEN WE GENERATE NECEASSARY DATA
     */
    public ArrayList<DoctorSpeciality> generatePage(int currentPage, ArrayList<DoctorSpeciality> doctorSpecialities) {
        int TOTAL_NUM_ITEMS = doctorSpecialities.size();
        final int ITEMS_PER_PAGE = 9;
        int ITEMS_REMAINING = TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
        int LAST_PAGE = TOTAL_NUM_ITEMS / ITEMS_PER_PAGE;

        int startItem = currentPage * ITEMS_PER_PAGE;
        int numOfData = ITEMS_PER_PAGE;
        ArrayList<DoctorSpeciality> pageData = new ArrayList();

        //  doctorSpecialities = activity.getReceivedBookAppointmentBaseModel().getDoctorServicesModel().getDoctorSpecialities();

        if (currentPage == LAST_PAGE && ITEMS_REMAINING > 0) {
            for (int i = startItem; i < startItem + ITEMS_REMAINING; i++) {
                //for (int i = startItem - 1; i < startItem + ITEMS_REMAINING; i++) { // TODO : need to fix??
                pageData.add(doctorSpecialities.get(i));
            }
        } else {
            for (int i = startItem; i < startItem + numOfData; i++) {
                pageData.add(doctorSpecialities.get(i));
            }
        }
        return pageData;
    }

    private void toggleButtons(ArrayList<DoctorSpeciality> doctorSpecialities) {
        totalPages = doctorSpecialities.size() / 9;
        if (currentPage == totalPages) {
            nextBtn.setVisibility(View.INVISIBLE);
            prevBtn.setVisibility(View.INVISIBLE);
            nextBtn.setEnabled(false);
            prevBtn.setEnabled(true);
        } else if (currentPage == 0) {
            prevBtn.setVisibility(View.INVISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
            prevBtn.setEnabled(false);
            nextBtn.setEnabled(true);
        } else if (currentPage >= 1 && currentPage <= 5) {
            nextBtn.setVisibility(View.VISIBLE);
            prevBtn.setVisibility(View.VISIBLE);
            nextBtn.setEnabled(true);
            prevBtn.setEnabled(true);
        }
    }

    public void isDataListViewVisible(boolean flag, boolean isShowEmptyListView) {
        if (flag) {
            recentDoctorLayout.setVisibility(View.GONE);
            leftFab.setVisibility(View.VISIBLE);
            rightFab.setVisibility(View.VISIBLE);
            showDoctorsRecyclerView.setVisibility(View.VISIBLE);
            mFilterDocListEmptyListView.setVisibility(View.GONE);

            if (isShowEmptyListView) {
                leftFab.setVisibility(View.GONE);
                rightFab.setVisibility(View.GONE);
                mFilterDocListEmptyListView.setVisibility(View.VISIBLE);
                showDoctorsRecyclerView.setVisibility(View.GONE);
            }
        } else {
            leftFab.setVisibility(View.GONE);
            rightFab.setVisibility(View.GONE);
            mFilterDocListEmptyListView.setVisibility(View.GONE);
            recentDoctorLayout.setVisibility(View.VISIBLE);
            showDoctorsRecyclerView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void updateViewData() {
        BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
        bookAppointmentBaseModel = activity.getReceivedBookAppointmentBaseModel();
        if(bookAppointmentBaseModel!=null) {
            setDoctorListAdapter(bookAppointmentBaseModel);
            toggleButtons(bookAppointmentBaseModel.getDoctorServicesModel().getDoctorSpecialities());

            HashMap<String, String> userSelectedLocationInfo = DoctorDataHelper.getUserSelectedLocationInfo();
            String s = userSelectedLocationInfo.get(getString(R.string.location));
            if (s != null) {
                BookAppointDoctorListBaseActivity.setSelectedLocationText(s);
            }
        }else{
            isDataListViewVisible(false,true);
        }
    }
}

