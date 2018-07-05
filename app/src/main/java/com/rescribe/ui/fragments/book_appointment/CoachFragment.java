package com.rescribe.ui.fragments.book_appointment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rescribe.R;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.RescribeConstants;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoachFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoachFragment extends Fragment {
    public CoachFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CoachFragment newInstance() {
        CoachFragment fragment = new CoachFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_coach, container, false);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.COACHMARK_GET_TOKEN, RescribeConstants.YES, getActivity());
            }
        });
        return rootView;
    }

}
