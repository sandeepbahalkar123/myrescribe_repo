package com.rescribe.ui.fragments.book_appointment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.book_appointment.ShowReviewsAdapter;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.model.book_appointment.reviews.ReviewList;
import com.rescribe.model.book_appointment.reviews.ReviewListBaseModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jeetal on 28/9/17.
 */

public class ShowReviewsOnDoctorFragment extends Fragment implements HelperResponse {

    private static final String DATA = "DATA";
    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    Unbinder unbinder;
    private static Bundle args;
    private View mRootView;
    ShowReviewsAdapter showReviewsAdapter;
    private static ReviewListBaseModel mReviewListBaseModel;
    private DoctorDataHelper doctorDataHelper;
    private DividerItemDecoration mDividerItemDecoration;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ShowReviewsOnDoctorFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ShowReviewsOnDoctorFragment newInstance(Bundle data) {
        ShowReviewsOnDoctorFragment fragment = new ShowReviewsOnDoctorFragment();
        args = new Bundle();
        args = data;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.global_recycle_view_list, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        init();
        return mRootView;
    }

    private void init() {
        doctorDataHelper = new DoctorDataHelper(getActivity(),this);
        doctorDataHelper.doGetReviewsList(args.getString(getString(R.string.doctorId)));
    }

    @Override
    public void onResume() {
        super.onResume();
        //  setDoctorListAdapter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if(customResponse!=null);
        mReviewListBaseModel = (ReviewListBaseModel) customResponse;
        setAdapter();

    }

    private void setAdapter() {
        listView.setVisibility(View.VISIBLE);
        showReviewsAdapter = new ShowReviewsAdapter(getActivity(), mReviewListBaseModel.getReviewList().getReviews().getReviews());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(layoutManager);
        listView.setHasFixedSize(true);
        mDividerItemDecoration = new DividerItemDecoration(
                listView.getContext(),
                layoutManager.getOrientation()
        );
        listView.addItemDecoration(mDividerItemDecoration);
        listView.setAdapter(showReviewsAdapter);
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
}