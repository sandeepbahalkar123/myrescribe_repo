package com.rescribe.ui.fragments.book_appointment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.book_appointment.ShowReviewsAdapter;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.model.book_appointment.doctor_data.ReviewList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jeetal on 28/9/17.
 */

public class ShowReviewsOnDoctorFragment extends Fragment {

    private static final String DATA = "DATA";
    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    Unbinder unbinder;
    private static Bundle args;
    private View mRootView;
    ShowReviewsAdapter showReviewsAdapter;
    private static BookAppointmentBaseModel bookAppointmentBaseModel;

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
        bookAppointmentBaseModel = args.getParcelable(getString(R.string.doctor_data));
        listView.setVisibility(View.VISIBLE);

        ArrayList<ReviewList> reviewLists= new ArrayList<>();

        for(int i= 0; i<bookAppointmentBaseModel.getDoctorServicesModel().getDoctorList().size();i++) {
            ReviewList reviewList = new ReviewList();
            for(int j = 0 ;j<bookAppointmentBaseModel.getDoctorServicesModel().getDoctorList().get(i).getReviewList().size();j++) {

                reviewList.setUserMessage(bookAppointmentBaseModel.getDoctorServicesModel().getDoctorList().get(i).getReviewList().get(j).getUserMessage());
                reviewList.setUserName(bookAppointmentBaseModel.getDoctorServicesModel().getDoctorList().get(i).getReviewList().get(j).getUserName());
            }
            reviewLists.add(reviewList);
        }
        showReviewsAdapter = new ShowReviewsAdapter(getActivity(), reviewLists);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(layoutManager);
        listView.setHasFixedSize(true);
        listView.setAdapter(showReviewsAdapter);
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
}