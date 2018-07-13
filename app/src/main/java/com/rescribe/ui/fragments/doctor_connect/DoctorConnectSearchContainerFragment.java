package com.rescribe.ui.fragments.doctor_connect;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rescribe.R;
import com.rescribe.util.RescribeConstants;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jeetal on 7/9/17.
 */

public class DoctorConnectSearchContainerFragment extends Fragment {
    Unbinder unbinder;
    private OnAddFragmentListener mListener;

    public DoctorConnectSearchContainerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.login_signup_layout, container, false);
        unbinder = ButterKnife.bind(this, mRootView);

        if (mListener.getSearchText().isEmpty())
            mListener.addSpecializationOfDoctorFragment(new Bundle());
        else {
            Bundle bundle = new Bundle();
            bundle.putString(RescribeConstants.ITEM_DATA, mListener.getSearchText());
            mListener.addSearchDoctorByNameFragment(bundle);
        }

        return mRootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddFragmentListener) {
            mListener = (OnAddFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAddFragmentListener {
        String getSearchText();
        void addSpecializationOfDoctorFragment(Bundle bundleData);

        void addSearchDoctorByNameFragment(Bundle bundleData);
    }
}
