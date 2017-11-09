package com.rescribe.ui.fragments.book_appointment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.ListView;

import com.rescribe.R;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;

import java.util.ArrayList;

import static com.rescribe.util.RescribeConstants.DOCTOR_OBJECT;

/**
 * Created by ganeshshirole on 9/11/17.
 */

public class ServiceDialogFragment extends BottomSheetDialogFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View contentView = View.inflate(getContext(), R.layout.services_dialog_modal, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        Bundle arguments = getArguments();

        DoctorList doctorList = arguments.getParcelable(DOCTOR_OBJECT);

        ListView mServicesListView = (ListView) contentView.findViewById(R.id.servicesListView);
        BookAppointDoctorDescriptionFragment.DocServicesListAdapter mServicesAdapter = new BookAppointDoctorDescriptionFragment.DocServicesListAdapter(getActivity(), doctorList.getDocServices());
        mServicesListView.setAdapter(mServicesAdapter);

    }
}
