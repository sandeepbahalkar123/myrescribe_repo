package com.rescribe.ui.fragments.book_appointment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
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
import com.rescribe.ui.activities.book_appointment.MapActivityPlotNearByDoctor;
import com.rescribe.ui.activities.book_appointment.SelectSlotToBookAppointmentBaseActivity;
import com.rescribe.ui.customesViews.BottomSheetDialog;
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
    @BindView(R.id.countDoctorExperience)
    TextView mCountDoctorExperience;
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
    @BindView(R.id.selectClinicLine)
    View selectClinicLine;
    @BindView(R.id.yearsExperienceLine)
    View yearsExperienceLine;
    //-------
    @BindView(R.id.servicesLine)
    View servicesLine;
    @BindView(R.id.ruppeeShadow)
    ImageView ruppeeShadow;
    private View mRootView;
    Unbinder unbinder;
    private DoctorList mClickedDoctorObject;
    private DoctorDataHelper mDoctorDataHelper;
    private String mReceivedTitle;
    private int mSelectedClinicDataPosition;
    private ClinicData clinicData;
    private BottomSheetDialog mBottomSheetDialog;
    private ColorGenerator mColorGenerator;
    int spinnePos;
    private String clinicNameSpinner;

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
        mFavorite.setImageResource(favorite ? R.drawable.fav_icon : R.drawable.result_line_heart_fav);
    }

    private void init() {

        mDoctorDataHelper = new DoctorDataHelper(getActivity(), this);
        mColorGenerator = ColorGenerator.MATERIAL;
        //   BookAppointDoctorListBaseActivity.setToolBarTitle(args.getString(getString(R.string.toolbarTitle)), false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mReceivedTitle = arguments.getString(getString(R.string.toolbarTitle));
        }
    }

    private void setDataInViews() {

        if (!mClickedDoctorObject.getClinicDataList().isEmpty())
            bookAppointmentButton.setText(mClickedDoctorObject.getClinicDataList().get(0).getAppointmentType().equalsIgnoreCase(getString(R.string.book)) ? getText(R.string.book_appointment) : getText(R.string.get_token));

        //-------
        SpannableString contentServices = new SpannableString(getString(R.string.services));
        contentServices.setSpan(new UnderlineSpan(), 0, contentServices.length(), 0);
        mServicesHeaderView.setText(contentServices);
        //-------
        if (mClickedDoctorObject.getAboutDoctor().equals("")) {
            aboutLayout.setVisibility(View.GONE);
            //-------
        } else {
            aboutLayout.setVisibility(View.VISIBLE);
            SpannableString content = new SpannableString(aboutDoctor.getText());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            aboutDoctor.setText(content);
            mAboutDoctorDescription.setText("" + mClickedDoctorObject.getAboutDoctor());
        }
        if (mClickedDoctorObject.getDoctorImageUrl() != null) {

            String doctorName = mClickedDoctorObject.getDocName();
            if (doctorName.contains("Dr. ")) {
                doctorName = doctorName.replace("Dr. ", "");
            }
            int color2 = mColorGenerator.getColor(doctorName);
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .width(Math.round(getActivity().getResources().getDimension(R.dimen.dp40))) // width in px
                    .height(Math.round(getActivity().getResources().getDimension(R.dimen.dp40))) // height in px
                    .endConfig()
                    .buildRound(("" + doctorName.charAt(0)).toUpperCase(), color2);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.dontAnimate();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.skipMemoryCache(true);
            requestOptions.placeholder(drawable);
            requestOptions.error(drawable);

            Glide.with(getActivity())
                    .load(mClickedDoctorObject.getDoctorImageUrl())
                    .apply(requestOptions).thumbnail(0.5f)
                    .into(mProfileImage);


        }
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
            mDoctorExperience.setText(experience + " " + getString(R.string.years_experience));
            mCountDoctorExperience.setText("" + experience);
        } else {
            mDoctorExperienceLayout.setVisibility(View.GONE);
        }
        //----------
        int size = mClickedDoctorObject.getClinicDataList().size();
        if (size > 0) {
            mAllClinicPracticeLocationMainLayout.setVisibility(View.VISIBLE);

            String mainString = getString(R.string.practices_at_locations);
            if (size == 1) {
                mainString = mainString.substring(0, mainString.length() - 1);
            }
            String updatedString = mainString.replace("$$", "" + size);
            SpannableString contentExp = new SpannableString(updatedString);
            contentExp.setSpan(new ForegroundColorSpan(
                            ContextCompat.getColor(getActivity(), R.color.tagColor)),
                    13, 13 + String.valueOf(size).length(),//hightlight mSearchString
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
            for(int i = 0 ;i < mClickedDoctorObject.getClinicDataList().size() ;i++){
                clinicNameSpinner = mClickedDoctorObject.getNameOfClinicString();
                if(clinicNameSpinner.equalsIgnoreCase(mClickedDoctorObject.getClinicDataList().get(i).getClinicName()))
                mClinicNameSpinner.setSelection(i);
            }

            mClinicNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    clinicData = mClickedDoctorObject.getClinicDataList().get(position);
                    if (clinicData.getClinicName().equals("")) {
                        mClinicName.setVisibility(View.GONE);
                    } else {
                        mClinicName.setVisibility(View.VISIBLE);
                        mClinicName.setText("" + clinicData.getClinicName());

                    }
                    if (clinicData.getAmount() == 0) {
                        ruppeeShadow.setVisibility(View.INVISIBLE);
                        rupeesLayout.setVisibility(View.INVISIBLE);
                    } else {
                        rupeesLayout.setVisibility(View.VISIBLE);
                        ruppeeShadow.setVisibility(View.VISIBLE);
                        mDoctorFees.setText("" + clinicData.getAmount());
                    }
                    mSelectedClinicDataPosition = position;

                    setServicesInView(clinicData.getDocServices());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            if (mClickedDoctorObject.getClinicDataList().size() == 1) {
                mClinicNameSpinner.setEnabled(false);
                mClinicNameSpinner.setClickable(false);
                mSelectedClinicDataPosition = 0;
                mClinicNameSpinner.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));
            } else {
                mClinicNameSpinner.setEnabled(true);
                mClinicNameSpinner.setClickable(true);
                mClinicNameSpinner.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.spinner_bg));
            }
        } else {
            mClinicNameSpinnerParentLayout.setVisibility(View.GONE);
        }

    }

    private void setServicesInView(ArrayList<String> receivedDocService) {
        //---------

        int receivedDocServiceSize = receivedDocService.size();
        if (receivedDocServiceSize > 0) {
            servicesLine.setVisibility(View.VISIBLE);
            servicesLayout.setVisibility(View.VISIBLE);
            ArrayList<String> docListToSend = new ArrayList<>();
            if (receivedDocServiceSize > 4) {
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
            servicesLine.setVisibility(View.GONE);
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
                    setFavorite(mClickedDoctorObject.getFavourite());
                }
                //    CommonMethods.showToast(getActivity(), temp.getCommonRespose().getStatusMessage());
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

    @Override
    public void onResume() {
        super.onResume();
        mClickedDoctorObject = ServicesCardViewImpl.getUserSelectedDoctorListDataObject();
        setDataInViews();
    }

    @OnClick({R.id.bookAppointmentButton, R.id.viewAllClinicsOnMap, R.id.favorite, R.id.doChat, R.id.readMoreDocServices})
    public void onClickOfView(View view) {

        switch (view.getId()) {

            case R.id.bookAppointmentButton:
                Intent intentObject = new Intent(getActivity(), SelectSlotToBookAppointmentBaseActivity.class);
                Bundle b = new Bundle();
                b.putString(getString(R.string.toolbarTitle), mReceivedTitle);
                b.putInt(getString(R.string.selected_clinic_data_position), mSelectedClinicDataPosition);
                intentObject.putExtras(b);
                startActivity(intentObject);
                break;
            case R.id.viewAllClinicsOnMap: // on view-all location clicked
                //-----Show all doc clinic on map, copied from BookAppointFilteredDoctorListFragment.java----
                //this list is sorted for plotting map for each clinic location, the values of clinicName and doctorAddress are set in string here, which are coming from arraylist.
                ArrayList<DoctorList> doctorListByClinics = new ArrayList<>();

                ArrayList<ClinicData> clinicNameList = mClickedDoctorObject.getClinicDataList();

                for (int i = 0; i < clinicNameList.size(); i++) {
                    DoctorList doctorListByClinic;
                    try {
                        doctorListByClinic = (DoctorList) mClickedDoctorObject.clone();
                        doctorListByClinic.setAddressOfDoctorString(clinicNameList.get(i).getClinicAddress());
                        doctorListByClinic.setNameOfClinicString(clinicNameList.get(i).getClinicName());
                        doctorListByClinics.add(doctorListByClinic);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }

                }
                Intent intentObjectMap = new Intent(getActivity(), MapActivityPlotNearByDoctor.class);
                intentObjectMap.putParcelableArrayListExtra(getString(R.string.doctor_data), doctorListByClinics);
                intentObjectMap.putExtra(getString(R.string.toolbarTitle), getString(R.string.location));
                intentObjectMap.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentObjectMap.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        mBottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.Material_App_BottomSheetDialog);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.services_dialog_modal, null);
        ///  CommonMethods.setBackground(v, new ThemeDrawable(R.array.bg_window));
        mBottomSheetDialog.setTitle("Services");
        mBottomSheetDialog.heightParam(ViewGroup.LayoutParams.MATCH_PARENT);
        ListView mServicesListView = (ListView) v.findViewById(R.id.servicesListView);
        DialogServicesListAdapter mServicesAdapter = new DialogServicesListAdapter(getActivity(), clinicData.getDocServices());
        mServicesListView.setAdapter(mServicesAdapter);
        AppCompatImageView closeButton = (AppCompatImageView) v.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog.contentView(v)
                .show();

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

}
