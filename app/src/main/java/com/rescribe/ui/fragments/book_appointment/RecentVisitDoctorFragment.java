package com.rescribe.ui.fragments.book_appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rescribe.R;
import com.rescribe.adapters.DoctorSpecialistBookAppointmentAdapter;
import com.rescribe.adapters.ShowRecentVisitedDoctorPagerAdapter;
import com.rescribe.adapters.book_appointment.BookAppointFilteredDocList;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.model.book_appointment.doctor_data.DoctorServicesModel;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.customesViews.CircleIndicator;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.customesViews.EditTextWithDeleteButton;

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
    @BindView(R.id.listView)
    RecyclerView listView;
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
    private View mRootView;
    Unbinder unbinder;
    DoctorSpecialistBookAppointmentAdapter mDoctorConnectSearchAdapter;
    private BookAppointFilteredDocList mBookAppointFilteredDocListAdapter;

    public RecentVisitDoctorFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.recent_visit_doctor, container, false);
        hideSoftKeyboard();
        unbinder = ButterKnife.bind(this, mRootView);

        Bundle arguments = getArguments();
        if (arguments != null) {
            //     mDoctorServicesModel = getArguments().getParcelable(RescribeConstants.DOCTOR_DATA_REQUEST);
        }
        init(mRootView);
        return mRootView;

    }

    private void init(View mRootView) {
        searchView.addClearTextButtonListener(new EditTextWithDeleteButton.OnClearButtonClickedInEditTextListener() {
            @Override
            public void onClearButtonClicked() {
                recentDoctorLayout.setVisibility(View.VISIBLE);

                showDoctorsRecyclerView.setVisibility(View.GONE);
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
                recentDoctorLayout.setVisibility(View.GONE);
                showDoctorsRecyclerView.setVisibility(View.VISIBLE);

                BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
                mBookAppointFilteredDocListAdapter = new BookAppointFilteredDocList(getActivity(), activity.getReceivedBookAppointmentBaseModel().getDoctorServicesModel().getDoctorList(),RecentVisitDoctorFragment.this );
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                showDoctorsRecyclerView.setLayoutManager(layoutManager);
                showDoctorsRecyclerView.setHasFixedSize(true);
                showDoctorsRecyclerView.setAdapter(mBookAppointFilteredDocListAdapter);

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
        Toast.makeText(getActivity(), "clicked :" + bundleData.getString(getString(R.string.clicked_item_data)), Toast.LENGTH_LONG).show();
        BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
        activity.loadFragment(BookAppointFilteredDoctorListFragment.newInstance(bundleData));
    }

    @OnClick({R.id.viewpager, R.id.circleIndicator, R.id.pickSpeciality, R.id.listView, R.id.recyclerViewLinearLayout, R.id.doubtMessage, R.id.emptyListView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.viewpager:
             /*   mStillInDoubtFragment = new StillInDoubtFragment();
                FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, mStillInDoubtFragment);
                fragmentTransaction.commit();*/
                break;
            case R.id.circleIndicator:
                break;
            case R.id.pickSpeciality:
                break;
            case R.id.listView:
                break;
            case R.id.recyclerViewLinearLayout:
                break;
            case R.id.doubtMessage:

                break;
            case R.id.emptyListView:
                break;
        }

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        BookAppointmentBaseModel bookAppointmentBaseModel = (BookAppointmentBaseModel) customResponse;
        if (bookAppointmentBaseModel.getDoctorServicesModel() == null) {
            pickSpeciality.setVisibility(View.GONE);
            doubtMessage.setVisibility(View.GONE);
            emptyListView.setVisibility(View.VISIBLE);
        } else {
            emptyListView.setVisibility(View.GONE);
            pickSpeciality.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            doubtMessage.setVisibility(View.VISIBLE);
            ViewPager viewpager = (ViewPager) mRootView.findViewById(R.id.viewpager);
            CircleIndicator indicator = (CircleIndicator) mRootView.findViewById(R.id.circleIndicator);
            DoctorServicesModel doctorServicesModel = new DoctorServicesModel();
          /*  for(int i = 0;i<mDoctorServicesModel.getDoctorList().size();i++){
                if(mDoctorServicesModel.getDoctorList().get(i).getRecentlyVisited()){
                    ArrayList<DoctorList> doctorLists = new ArrayList<>();
                    doctorLists.get(i).setAboutDoctor(mDoctorServicesModel.getDoctorList().get(i).getAboutDoctor());
                    doctorLists.get(i).setAmount(mDoctorServicesModel.getDoctorList().get(i).getAmount());
                    doctorLists.get(i).setAvailableTimeSlots(mDoctorServicesModel.getDoctorList().get(i).getAvailableTimeSlots());
                    doctorLists.get(i).setDegree(mDoctorServicesModel.getDoctorList().get(i).getDegree());
                    doctorLists.get(i).setDistance(mDoctorServicesModel.getDoctorList().get(i).getDistance());
                    doctorLists.get(i).setDocId(mDoctorServicesModel.getDoctorList().get(i).getDocId());
                    doctorLists.get(i).setDocName(mDoctorServicesModel.getDoctorList().get(i).getDocName());
                    doctorLists.get(i).setDoctorAddress(mDoctorServicesModel.getDoctorList().get(i).getDoctorAddress());
                    doctorLists.get(i).setDoctorImageUrl(mDoctorServicesModel.getDoctorList().get(i).getDoctorImageUrl());
                    doctorLists.get(i).setExperience(mDoctorServicesModel.getDoctorList().get(i).getExperience());
                    doctorLists.get(i).setMorePracticePlaces(mDoctorServicesModel.getDoctorList().get(i).getMorePracticePlaces());
                    doctorLists.get(i).setOpenToday(mDoctorServicesModel.getDoctorList().get(i).getOpenToday());
                    doctorLists.get(i).setWaitingTime(mDoctorServicesModel.getDoctorList().get(i).getWaitingTime());
                    doctorLists.get(i).setTokenNo(mDoctorServicesModel.getDoctorList().get(i).getTokenNo());
                    doctorLists.get(i).setRecentlyVisited(mDoctorServicesModel.getDoctorList().get(i).getRecentlyVisited());
                    doctorLists.get(i).setRating(mDoctorServicesModel.getDoctorList().get(i).getRating());
                    doctorLists.get(i).setSpeciality(mDoctorServicesModel.getDoctorList().get(i).getSpeciality());
                    doctorLists.get(i).setPaidStatus(mDoctorServicesModel.getDoctorList().get(i).getPaidStatus());
                    doctorServicesModel.setDoctorList(doctorLists);



                }
            }*/
            viewpager.setAdapter(new ShowRecentVisitedDoctorPagerAdapter(getActivity(), bookAppointmentBaseModel.getDoctorServicesModel().getDoctorList()));
            indicator.setViewPager(viewpager);
            listView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
            listView.setLayoutManager(layoutManager);
            listView.setItemAnimator(new DefaultItemAnimator());
            int spanCount = 3; // 3 columns
            int spacing = 50; // 50px
            boolean includeEdge = true;
            listView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
            mDoctorConnectSearchAdapter = new DoctorSpecialistBookAppointmentAdapter(getActivity(), this, bookAppointmentBaseModel.getDoctorServicesModel().getDoctorSpecialities());
            listView.setAdapter(mDoctorConnectSearchAdapter);
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

    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onClickOfDoctorRowItem(Bundle bundleData) {

    }

   /* @Override
    public void onClearButtonClicked() {
        recentDoctorLayout.setVisibility(View.VISIBLE);
        showDoctorsRecyclerView.setVisibility(View.GONE);

    }*/
}
