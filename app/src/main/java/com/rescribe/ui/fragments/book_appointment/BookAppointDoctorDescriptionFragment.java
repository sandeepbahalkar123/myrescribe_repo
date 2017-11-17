package com.rescribe.ui.fragments.book_appointment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatButton;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.helpers.book_appointment.ServicesCardViewImpl;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.book_appointment.doctor_data.ClinicData;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.doctor_connect.ChatDoctor;
import com.rescribe.ui.activities.ChatActivity;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.activities.book_appointment.MapActivityPlotNearByDoctor;
import com.rescribe.ui.activities.book_appointment.SelectSlotToBookAppointmentBaseActivity;
import com.rescribe.ui.activities.dashboard.DoctorDescriptionBaseActivity;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.rescribe.util.RescribeConstants.USER_STATUS.ONLINE;

//TODO , NNED TO IMPLEMNT AS PER NEW JSON

public class BookAppointDoctorDescriptionFragment extends Fragment implements HelperResponse {

    //-------------
    @BindView(R.id.doChat)
    ImageView doChat;
    @BindView(R.id.profileImage)
    CircularImageView mProfileImage;
    @BindView(R.id.docRating)
    CustomTextView mDocRating;
    @BindView(R.id.docRatingBar)
    RatingBar mDocRatingBar;
    @BindView(R.id.docRatingBarLayout)
    LinearLayout mDocRatingBarLayout;
    @BindView(R.id.doctorName)
    CustomTextView mDoctorName;
    @BindView(R.id.doctorSpecialization)
    CustomTextView mDoctorSpecialization;
    @BindView(R.id.doctorFees)
    CustomTextView mDoctorFees;
    @BindView(R.id.clinicName)
    CustomTextView mClinicName;
    @BindView(R.id.docPracticesLocationCount)
    CustomTextView mDocPracticesLocationCount;
    @BindView(R.id.premiumType)
    CustomTextView mPremiumType;
    @BindView(R.id.clinicNameSpinner)
    Spinner mClinicNameSpinner;
    @BindView(R.id.favorite)
    ImageView mFavorite;
    @BindView(R.id.doctorExperience)
    CustomTextView mDoctorExperience;
    @BindView(R.id.doctorExperienceLayout)
    LinearLayout mDoctorExperienceLayout;
    @BindView(R.id.clinicNameSpinnerParentLayout)
    LinearLayout mClinicNameSpinnerParentLayout;
    @BindView(R.id.allClinicPracticeLocationMainLayout)
    LinearLayout mAllClinicPracticeLocationMainLayout;
    //-------------
    @BindView(R.id.aboutDoctorDescription)
    CustomTextView mAboutDoctorDescription;
    @BindView(R.id.aboutDoctor)
    CustomTextView aboutDoctor;
    @BindView(R.id.bookAppointmentButton)
    AppCompatButton bookAppointmentButton;
    @BindView(R.id.servicesListView)
    ListView mServicesListView;
    @BindView(R.id.servicesHeaderView)
    CustomTextView mServicesHeaderView;
    @BindView(R.id.readMoreDocServices)
    CustomTextView mReadMoreDocServices;
    @BindView(R.id.rupeesLayout)
    LinearLayout rupeesLayout;
    @BindView(R.id.servicesLayout)
    LinearLayout servicesLayout;
    @BindView(R.id.aboutLayout)
    LinearLayout aboutLayout;
    //-------

    private View mRootView;
    private int mImageSize;
    Unbinder unbinder;
    private DoctorList mClickedDoctorObject;
    private DoctorDataHelper mDoctorDataHelper;
    private String mReceivedTitle;

    public BookAppointDoctorDescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.book_appoint_doc_description_new, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        init();
        return mRootView;
    }

    public static BookAppointDoctorDescriptionFragment newInstance(Bundle b) {
        BookAppointDoctorDescriptionFragment fragment = new BookAppointDoctorDescriptionFragment();
        Bundle args = b;
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    private void setFavorite(boolean favorite) {
        if (favorite) {
            mFavorite.setImageResource(R.drawable.fav_icon);
        } else {
            mFavorite.setImageResource(R.drawable.result_line_heart_fav);
        }
    }

    private void init() {
        mDoctorDataHelper = new DoctorDataHelper(getActivity(), this);

        setColumnNumber(getActivity(), 2);
        //   BookAppointDoctorListBaseActivity.setToolBarTitle(args.getString(getString(R.string.toolbarTitle)), false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mClickedDoctorObject = arguments.getParcelable(getString(R.string.clicked_item_data));
            mReceivedTitle = arguments.getString(getString(R.string.toolbarTitle));
            setDataInViews();
        }
    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        mImageSize = (widthPixels / columnNum) - CommonMethods.convertDpToPixel(30);
    }

    private void setDataInViews() {
        //-------
        SpannableString contentServices = new SpannableString(getString(R.string.services));
        contentServices.setSpan(new UnderlineSpan(), 0, contentServices.length(), 0);
        mServicesHeaderView.setText(contentServices);
        //-------
        if (aboutDoctor.getText().equals("")) {
            aboutLayout.setVisibility(View.INVISIBLE);
            //-------
        } else {
            aboutLayout.setVisibility(View.VISIBLE);
            SpannableString content = new SpannableString(aboutDoctor.getText());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            aboutDoctor.setText(content);
            mAboutDoctorDescription.setText("" + mClickedDoctorObject.getAboutDoctor());
        }
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        requestOptions.override(mImageSize, mImageSize);


        Glide.with(getActivity())
                .load(mClickedDoctorObject.getDoctorImageUrl())
                .apply(requestOptions).thumbnail(0.5f)
                .into(mProfileImage);
        //-------
        setFavorite(mClickedDoctorObject.getFavourite());
        //---------------

        if (mClickedDoctorObject.getRating() == 0) {
            mDocRatingBarLayout.setVisibility(View.INVISIBLE);
        } else {
            mDocRatingBarLayout.setVisibility(View.VISIBLE);
            mDocRating.setText("" + mClickedDoctorObject.getRating());
            mDocRatingBar.setRating((float) mClickedDoctorObject.getRating());
        }
        //----------
        mDoctorName.setText("" + mClickedDoctorObject.getDocName());
        mDoctorSpecialization.setText("" + mClickedDoctorObject.getDegree());
        //------------
        int experience = mClickedDoctorObject.getExperience();
        if (experience > 0) {
            mDoctorExperienceLayout.setVisibility(View.VISIBLE);
            mDoctorExperience.setText("" + experience + getString(R.string.space) + getString(R.string.years_experience));
        } else {
            mDoctorExperienceLayout.setVisibility(View.GONE);
        }
        //----------
        int size = mClickedDoctorObject.getClinicDataList().size();
        if (size > 0) {
            mAllClinicPracticeLocationMainLayout.setVisibility(View.VISIBLE);

            String updatedString = getString(R.string.practices_at_locations).replace("$$", "" + size);
            SpannableString contentExp = new SpannableString(updatedString);
            contentExp.setSpan(new ForegroundColorSpan(
                            ContextCompat.getColor(getActivity(), R.color.tagColor)),
                    13, 13 + size,//hightlight mSearchString
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mDocPracticesLocationCount.setText(contentExp);
        } else {
            mAllClinicPracticeLocationMainLayout.setVisibility(View.GONE);

        }
        //------------
        if (mClickedDoctorObject.getCategorySpeciality() != null) {
            mPremiumType.setText("" + mClickedDoctorObject.getCategorySpeciality());
            mPremiumType.setVisibility(View.VISIBLE);
        } else {
            mPremiumType.setVisibility(View.INVISIBLE);
        }
        //-----------
        //requestOptions.placeholder(R.drawable.layer_12);
      /*  if (!mClickedDoctorObject.getAddressOfDoctorString().isEmpty()) {
            Glide.with(getActivity())
                    .load("https://maps.googleapis.com/maps/api/staticmap?center=" + mClickedDoctorObject.getAddressOfDoctorString() + "&markers=color:red%7Clabel:C%7C" + mClickedDoctorObject.getAddressOfDoctorString() + "&zoom=12&size=640x250")
                    .into(locationImage);
        }*/

        //---------
        if (mClickedDoctorObject.getClinicDataList().size() > 0) {
            ArrayAdapter<ClinicData> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.global_item_simple_spinner, mClickedDoctorObject.getClinicDataList());

            mClinicNameSpinner.setAdapter(arrayAdapter);
            mClinicNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    ClinicData clinicData = mClickedDoctorObject.getClinicDataList().get(position);
                    if (clinicData.getClinicName().equals("")) {
                        mClinicName.setVisibility(View.GONE);
                    } else {
                        mClinicName.setVisibility(View.VISIBLE);
                        mClinicName.setText("" + clinicData.getClinicName());

                    }
                    if (clinicData.getAmount() == 0) {
                        rupeesLayout.setVisibility(View.INVISIBLE);
                    } else {
                        rupeesLayout.setVisibility(View.VISIBLE);
                        mDoctorFees.setText("" + clinicData.getAmount());
                    }
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            if (mClickedDoctorObject.getClinicDataList().size() == 1) {
                mClinicNameSpinner.setEnabled(false);
                mClinicNameSpinner.setClickable(false);
                mClinicNameSpinner.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));
            } else {
                mClinicNameSpinner.setEnabled(true);
                mClinicNameSpinner.setClickable(true);
                mClinicNameSpinner.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.spinner_bg));
            }
        } else {
            mClinicNameSpinnerParentLayout.setVisibility(View.GONE);
        }
        //---------
        ArrayList<String> receivedDocService = mClickedDoctorObject.getDocServices();
        int receivedDocServiceSize = receivedDocService.size();
        if (receivedDocServiceSize > 0) {
            servicesLayout.setVisibility(View.VISIBLE);
            ArrayList<String> docListToSend = new ArrayList<>();
            if (receivedDocServiceSize > 5) {
                docListToSend.addAll(receivedDocService.subList(0, 4));
                mReadMoreDocServices.setVisibility(View.VISIBLE);
            } else {
                docListToSend.addAll(receivedDocService);
                mReadMoreDocServices.setVisibility(View.GONE);
            }
            DocServicesListAdapter mServicesAdapter = new DocServicesListAdapter(getActivity(), docListToSend);
            mServicesListView.setAdapter(mServicesAdapter);
            CommonMethods.setListViewHeightBasedOnChildren(mServicesListView);
        } else {
            servicesLayout.setVisibility(View.GONE);
        }

        //---------
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        switch (mOldDataTag) {
            case RescribeConstants.TASK_SET_FAVOURITE_DOCTOR:
                CommonBaseModelContainer temp = (CommonBaseModelContainer) customResponse;
                if (temp.getCommonRespose().isSuccess()) {
                    ServicesCardViewImpl.updateFavStatusForDoctorDataObject(mClickedDoctorObject);
                    boolean status = !mClickedDoctorObject.getFavourite();
                    mClickedDoctorObject.setFavourite(status);
                    setFavorite(mClickedDoctorObject.getFavourite());
                }
                CommonMethods.showToast(getActivity(), temp.getCommonRespose().getStatusMessage());
                break;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.bookAppointmentButton, R.id.viewAllClinicsOnMap, R.id.favorite, R.id.doChat, R.id.readMoreDocServices})
    public void onClickOfView(View view) {

        switch (view.getId()) {

            case R.id.bookAppointmentButton:
                Intent intentObject = new Intent(getActivity(), SelectSlotToBookAppointmentBaseActivity.class);
                intentObject.putExtra(getString(R.string.clicked_item_data), mClickedDoctorObject);
                intentObject.putExtra(getString(R.string.toolbarTitle), mReceivedTitle);
                getActivity().startActivityForResult(intentObject, RescribeConstants.DOCTOR_DATA_REQUEST_CODE);
                break;
            case R.id.viewAllClinicsOnMap: // on view-all location clicked
                //-----Show all doc clinic on map, copied from BookAppointFilteredDoctorListFragment.java----
                //this list is sorted for plotting map for each clinic location, the values of clinicName and doctorAddress are set in string here, which are coming from arraylist.
                ArrayList<DoctorList> doctorListByClinics = new ArrayList<>();

                ArrayList<ClinicData> clinicNameList = mClickedDoctorObject.getClinicDataList();

                for (int i = 0; i < clinicNameList.size(); i++) {
                    DoctorList doctorListByClinic = new DoctorList();
                    doctorListByClinic = mClickedDoctorObject;
                    doctorListByClinic.setAddressOfDoctorString(clinicNameList.get(i).getClinicAddress());
                    doctorListByClinic.setNameOfClinicString(clinicNameList.get(i).getClinicName());
                    doctorListByClinics.add(doctorListByClinic);
                }
                Intent intentObjectMap = new Intent(getActivity(), MapActivityPlotNearByDoctor.class);
                intentObjectMap.putParcelableArrayListExtra(getString(R.string.doctor_data), doctorListByClinics);
                intentObjectMap.putExtra(getString(R.string.toolbarTitle), mReceivedTitle);
                startActivity(intentObjectMap);
                //--------
                break;
            case R.id.favorite:
                mDoctorDataHelper.setFavouriteDoctor(!mClickedDoctorObject.getFavourite(), mClickedDoctorObject.getDocId());
                break;
            case R.id.doChat:

                ChatDoctor chatDoctor = new ChatDoctor();
                chatDoctor.setId(mClickedDoctorObject.getDocId());
                chatDoctor.setDoctorName(mClickedDoctorObject.getDocName());
                chatDoctor.setOnlineStatus(ONLINE);
                chatDoctor.setAddress(mClickedDoctorObject.getAddressOfDoctorString());
                chatDoctor.setImageUrl(mClickedDoctorObject.getDoctorImageUrl());

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(RescribeConstants.DOCTORS_INFO, chatDoctor);
                startActivity(intent);

                break;

            case R.id.readMoreDocServices:

                showServiceDialog();

                break;
        }
    }

    public void showServiceDialog() {

        final Dialog dialog = new Dialog(getContext());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.services_dialog_modal);

        dialog.findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ListView mServicesListView = (ListView) dialog.findViewById(R.id.servicesListView);
        DialogServicesListAdapter mServicesAdapter = new DialogServicesListAdapter(getActivity(), mClickedDoctorObject.getDocServices());
        mServicesListView.setAdapter(mServicesAdapter);

        dialog.show();
    }

    class DialogServicesListAdapter extends BaseAdapter {
        Context mContext;
        private ArrayList<String> mDocServiceList;

        DialogServicesListAdapter(Context context, ArrayList<String> items) {
            this.mContext = context;
            this.mDocServiceList = items;
        }

        @Override
        public int getCount() {
            return mDocServiceList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = convertView;
            String data = mDocServiceList.get(position);

            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                view = layoutInflater.inflate(R.layout.services_item_textview, null);
            }

            CustomTextView dataView = (CustomTextView) view.findViewById(R.id.text);
            Drawable leftDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.services_dot);
            dataView.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);

            dataView.setText("" + data);
            return view;
        }
    }

    public class DocServicesListAdapter extends BaseAdapter {
        Context mContext;
        private ArrayList<String> mDocServiceList;


        DocServicesListAdapter(Context context, ArrayList<String> items) {
            this.mContext = context;
            this.mDocServiceList = items;
        }

        @Override
        public int getCount() {
            return mDocServiceList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = convertView;
            String data = mDocServiceList.get(position);

            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                view = layoutInflater.inflate(R.layout.global_item_textview, null);
            }
            CustomTextView dataView = (CustomTextView) view.findViewById(R.id.text);
            dataView.setText("" + data);
            return view;
        }
    }

    public void updateDataInViews(DoctorList receivedData) {
        if (receivedData != null) {
            mClickedDoctorObject = receivedData;
            setDataInViews();
        }
    }
}
