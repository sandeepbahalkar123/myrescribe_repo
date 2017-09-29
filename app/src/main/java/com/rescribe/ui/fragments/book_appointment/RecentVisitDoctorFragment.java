package com.rescribe.ui.fragments.book_appointment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rescribe.R;
import com.rescribe.adapters.DoctorSpecialistBookAppointmentAdapter;
import com.rescribe.adapters.ShowRecentVisitedDoctorPagerAdapter;
import com.rescribe.adapters.book_appointment.BookAppointFilteredDocList;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.Common;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.book_appointment.doctor_data.DoctorSpeciality;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.customesViews.CircleIndicator;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.customesViews.EditTextWithDeleteButton;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import droidninja.filepicker.utils.GridSpacingItemDecoration;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;


public class RecentVisitDoctorFragment extends Fragment implements DoctorSpecialistBookAppointmentAdapter.OnSpecialityClickListener, HelperResponse, BookAppointFilteredDocList.OnFilterDocListClickListener {
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.circleIndicator)
    CircleIndicator circleIndicator;
    @BindView(R.id.pickSpeciality)
    CustomTextView pickSpeciality;
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
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
    @BindView(R.id.showDoctorsRecyclerView)
    RecyclerView showDoctorsRecyclerView;
    @BindView(R.id.recentDoctorLayout)
    LinearLayout recentDoctorLayout;
    @BindView(R.id.prevBtn)
    ImageView prevBtn;
    @BindView(R.id.nextBtn)
    ImageView nextBtn;
    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.whiteUnderLine)
    TextView whiteUnderLine;
    private View mRootView;
    Unbinder unbinder;
    static Bundle args;
    BookAppointmentBaseModel bookAppointmentBaseModel;
    DoctorSpecialistBookAppointmentAdapter mDoctorConnectSearchAdapter;
    private BookAppointFilteredDocList mBookAppointFilteredDocListAdapter;
    private int currentPage = 0;
    private int totalPages;
    CustomResponse customResponse;

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
        Bundle arguments = getArguments();
        if (arguments != null) {
            //     mDoctorServicesModel = getArguments().getParcelable(RescribeConstants.DOCTOR_DATA_REQUEST);
        }
        init(mRootView);
        return mRootView;

    }

    private void init(View mRootView) {
        BookAppointDoctorListBaseActivity.setToolBarTitle(getString(R.string.doctorss), true);

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

    }


    public static RecentVisitDoctorFragment newInstance(Bundle b) {
        RecentVisitDoctorFragment fragment = new RecentVisitDoctorFragment();
        args = b;
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setOnClickOfDoctorSpeciality(Bundle bundleData) {
        bundleData.putString(getString(R.string.latitude), args.getString(getString(R.string.latitude)));
        bundleData.putString(getString(R.string.longitude), args.getString(getString(R.string.longitude)));
        BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
        activity.loadFragment(BookAppointFilteredDoctorListFragment.newInstance(bundleData), true);
    }

    @OnClick({R.id.viewpager, R.id.circleIndicator, R.id.pickSpeciality, R.id.listView, R.id.recyclerViewLinearLayout, R.id.doubtMessage, R.id.emptyListView, R.id.prevBtn, R.id.nextBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.viewpager:
             /*   mStillInDoubtFragment = new StillInDoubtFragment();
                FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, mStillInDoubtFragment);
                fragmentTransaction.commit();*/
                break;
            case R.id.prevBtn:
                currentPage -= 1;
                mDoctorConnectSearchAdapter = new DoctorSpecialistBookAppointmentAdapter(getActivity(), this, generatePage(currentPage, bookAppointmentBaseModel.getDoctorServicesModel().getDoctorSpecialities()));
                listView.setAdapter(mDoctorConnectSearchAdapter);
                toggleButtons(bookAppointmentBaseModel.getDoctorServicesModel().getDoctorSpecialities());
                // listView.getLayoutManager().scrollToPosition(linearLayoutManager.findLastVisibleItemPosition() + 1);
                break;
            case R.id.pickSpeciality:
                break;
            case R.id.listView:
                break;
            case R.id.recyclerViewLinearLayout:
                break;
            case R.id.doubtMessage:
                BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
                activity.loadFragment(ComplaintsFragment.newInstance(new Bundle()), false);
                break;
            case R.id.nextBtn:
                currentPage += 1;
                mDoctorConnectSearchAdapter = new DoctorSpecialistBookAppointmentAdapter(getActivity(), this, generatePage(currentPage, bookAppointmentBaseModel.getDoctorServicesModel().getDoctorSpecialities()));
                listView.setAdapter(mDoctorConnectSearchAdapter);
                toggleButtons(bookAppointmentBaseModel.getDoctorServicesModel().getDoctorSpecialities());
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
        BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
        bookAppointmentBaseModel = activity.getReceivedBookAppointmentBaseModel();
        setDoctorListAdapter(bookAppointmentBaseModel);
    }

    private void setDoctorListAdapter(BookAppointmentBaseModel bookAppointmentBaseModel) {
        if (bookAppointmentBaseModel.getDoctorServicesModel() == null) {
            pickSpeciality.setVisibility(View.GONE);
            doubtMessage.setVisibility(View.GONE);
            emptyListView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            prevBtn.setVisibility(View.GONE);
            prevBtn.setEnabled(false);
            nextBtn.setVisibility(View.VISIBLE);
            whiteUnderLine.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);
            emptyListView.setVisibility(View.GONE);
            ViewPager viewpager = (ViewPager) mRootView.findViewById(R.id.viewpager);
            CircleIndicator indicator = (CircleIndicator) mRootView.findViewById(R.id.circleIndicator);
            viewpager.setAdapter(new ShowRecentVisitedDoctorPagerAdapter(getActivity(), bookAppointmentBaseModel.getDoctorServicesModel().getDoctorList()));
            indicator.setViewPager(viewpager);
            listView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
            listView.setLayoutManager(layoutManager);
            listView.setItemAnimator(new DefaultItemAnimator());
            int spanCount = 3; // 3 columns
            int spacing = 30; // 50px
            boolean includeEdge = true;
            listView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
            mDoctorConnectSearchAdapter = new DoctorSpecialistBookAppointmentAdapter(getActivity(), this, generatePage(currentPage, bookAppointmentBaseModel.getDoctorServicesModel().getDoctorSpecialities()));
            // listView.setAdapter(mDoctorConnectSearchAdapter);
            listView.setAdapter(mDoctorConnectSearchAdapter);
            pickSpeciality.setVisibility(View.VISIBLE);
            doubtMessage.setVisibility(View.VISIBLE);
            isDataListViewVisible(false, false);
            BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
            ArrayList<DoctorList> doctorList = activity.getReceivedBookAppointmentBaseModel().getDoctorServicesModel().getDoctorList();
            mBookAppointFilteredDocListAdapter = new BookAppointFilteredDocList(getActivity(), doctorList, RecentVisitDoctorFragment.this, RecentVisitDoctorFragment.this);
            LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            showDoctorsRecyclerView.setLayoutManager(linearlayoutManager);
            showDoctorsRecyclerView.setHasFixedSize(true);
            showDoctorsRecyclerView.setAdapter(mBookAppointFilteredDocListAdapter);
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
            for (int i = startItem - 1; i < startItem + ITEMS_REMAINING; i++) {
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
            nextBtn.setVisibility(View.GONE);
            prevBtn.setVisibility(View.VISIBLE);
            nextBtn.setEnabled(false);
            prevBtn.setEnabled(true);
        } else if (currentPage == 0) {
            prevBtn.setVisibility(View.GONE);
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
            showDoctorsRecyclerView.setVisibility(View.VISIBLE);
            if (isShowEmptyListView) {
                emptyListView.setVisibility(View.VISIBLE);
                showDoctorsRecyclerView.setVisibility(View.GONE);
            }
        } else {
            emptyListView.setVisibility(View.GONE);
            recentDoctorLayout.setVisibility(View.VISIBLE);
            showDoctorsRecyclerView.setVisibility(View.GONE);
        }
    }

}

