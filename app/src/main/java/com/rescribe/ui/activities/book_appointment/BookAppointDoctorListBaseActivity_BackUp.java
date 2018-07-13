package com.rescribe.ui.activities.book_appointment;

/**
 * Created by jeetal on 15/9/17.
 */
/*

@RuntimePermissions
public class BookAppointDoctorListBaseActivity_BackUp extends AppCompatActivity implements HelperResponse, DrawerForFilterDoctorBookAppointment.OnDrawerInteractionListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "BookAppointDoctorListBaseActivity";
    @BindView(R.id.bookAppointmentBackButton)
    ImageView bookAppointmentBackButton;
    static CustomTextView title;
    static CustomTextView locationTextView;
    static CustomTextView showlocation;
    @BindView(R.id.nav_view)
    FrameLayout mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    private DoctorDataHelper mDoctorDataHelper;
    private Fragment mCurrentlyLoadedFragment; //TODO, fragmentById is not working hence hold this object.
    private BookAppointmentBaseModel mReceivedBookAppointmentBaseModel;
    private BookAppointmentBaseModel mPreviousReqReceivedBookAppointmentBaseModel;

    private FragmentManager mSupportFragmentManager;
    private Fragment mDrawerLoadedFragment;
    private int PLACE_PICKER_REQUEST = 1;
    private boolean isLocationChange = false;

    private HashMap<String, String> mComplaintsUserSearchFor = new HashMap<>();

    //-----
    String latitude = "";
    String longitude = "";
    String address;

    private DoctorList object;

    //-----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appoint_doc_base_list);
        ButterKnife.bind(this);
        initialize();

    }

    private void initialize() {

        //----------------
        mDrawerLayout.setFocusableInTouchMode(false);
        //----------------
        new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        //--------
        title = (CustomTextView) findViewById(R.id.title);
        locationTextView = (CustomTextView) findViewById(R.id.locationTextView);
        locationTextView.setVisibility(View.GONE);
        showlocation = (CustomTextView) findViewById(R.id.showlocation);

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Called when a drawer's position changes.
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //Called when a drawer has settled in a completely open state.
                //The drawer is interactive at this point.
                // If you have 2 drawers (left and right) you can distinguish
                // them by using id of the drawerView. int id = drawerView.getId();
                // id will be your layout's id: for example R.id.left_drawer
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Called when a drawer has settled in a completely closed state.
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Called when the drawer motion state changes. The new state will be one of STATE_IDLE, STATE_DRAGGING or STATE_SETTLING.
            }
        });

        //------
        String locationReceived = "";
        Intent intent = getIntent();
        String title = "";
        if (intent != null) {
            HashMap<String, String> userSelectedLocationInfo = DoctorDataHelper.getUserSelectedLocationInfo();
            locationReceived = userSelectedLocationInfo.get(getString(R.string.location));
            locationTextView.setText("" + locationReceived);
            title = intent.getStringExtra(RescribeConstants.ITEM_DATA);
        }
        //------
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        mDrawerLoadedFragment = DrawerForFilterDoctorBookAppointment.newInstance();
        fragmentTransaction.replace(R.id.nav_view, mDrawerLoadedFragment);
        fragmentTransaction.commit();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.title), title);
        mCurrentlyLoadedFragment = RecentVisitDoctorFragment.newInstance(bundle);
        mDoctorDataHelper = new DoctorDataHelper(this, this);
        //----split based on location------
        if (locationReceived != null) {
            String[] split = locationReceived.split(",");
            if (split.length == 2) {
                mDoctorDataHelper.doGetDoctorData(split[1], split[0], mComplaintsUserSearchFor);
            } else {
                mDoctorDataHelper.doGetDoctorData("", "", mComplaintsUserSearchFor);
            }
        }
        //----------
    }

    @Override
    public void onSuccess(final String mOldDataTag, final CustomResponse customResponse) {

        mReceivedBookAppointmentBaseModel = (BookAppointmentBaseModel) customResponse;

        if (mReceivedBookAppointmentBaseModel.getDoctorServicesModel() != null) {
            for (int i = 0; i < mReceivedBookAppointmentBaseModel.getDoctorServicesModel().getDoctorList().size(); i++) {
                if (!mReceivedBookAppointmentBaseModel.getDoctorServicesModel().getDoctorList().get(i).getDocName().toLowerCase().contains("dr.")) {
                    mReceivedBookAppointmentBaseModel.getDoctorServicesModel().getDoctorList().get(i).setDocName("Dr. " + mReceivedBookAppointmentBaseModel.getDoctorServicesModel().getDoctorList().get(i).getDocName());
                }
            }
            if (isLocationChange) {
                if (mCurrentlyLoadedFragment instanceof BookAppointFilteredDoctorListFragment) {
                    BookAppointFilteredDoctorListFragment bookAppointFilteredDoctorListFragment = (BookAppointFilteredDoctorListFragment) mCurrentlyLoadedFragment;
                    bookAppointFilteredDoctorListFragment.updateViewData();
                } else if (mCurrentlyLoadedFragment instanceof BookAppointDoctorDescriptionFragment) {
                    BookAppointDoctorDescriptionFragment bookAppointDoctorDescriptionFragment = (BookAppointDoctorDescriptionFragment) mCurrentlyLoadedFragment;
                    bookAppointDoctorDescriptionFragment.updateViewData();
                } else if (mCurrentlyLoadedFragment instanceof RecentVisitDoctorFragment) {
                    RecentVisitDoctorFragment recentVisitDoctorFragment = (RecentVisitDoctorFragment) mCurrentlyLoadedFragment;
                    recentVisitDoctorFragment.updateViewData();
                }
            } else {
                loadFragment(mCurrentlyLoadedFragment, false);
            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        loadFragment(mCurrentlyLoadedFragment, false);

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onApply(Bundle b, boolean drawerRequired) {
        mDrawerLayout.closeDrawers();
        doOperationOnDrawer(drawerRequired);
        if (mCurrentlyLoadedFragment instanceof BookAppointFilteredDoctorListFragment) {
            BookAppointFilteredDoctorListFragment bookAppointFilteredDoctorListFragment = (BookAppointFilteredDoctorListFragment) mCurrentlyLoadedFragment;
            if (mDrawerLoadedFragment instanceof DrawerForFilterDoctorBookAppointment) {
                bookAppointFilteredDoctorListFragment.onApplyClicked(b);
            }
        }
    }

    @Override
    public void onReset(boolean drawerRequired) {
        mDrawerLayout.closeDrawers();
        doOperationOnDrawer(drawerRequired);
        if (mCurrentlyLoadedFragment instanceof BookAppointFilteredDoctorListFragment) {
            BookAppointFilteredDoctorListFragment bookAppointFilteredDoctorListFragment = (BookAppointFilteredDoctorListFragment) mCurrentlyLoadedFragment;
            if (mDrawerLoadedFragment instanceof DrawerForFilterDoctorBookAppointment) {
                bookAppointFilteredDoctorListFragment.onResetClicked();
            }
        }
    }

    public BookAppointmentBaseModel getReceivedBookAppointmentBaseModel() {

        return mReceivedBookAppointmentBaseModel;
    }

    public void setReceivedBookAppointmentBaseModel(BookAppointmentBaseModel temp) {
        if (mPreviousReqReceivedBookAppointmentBaseModel == null) {
            mPreviousReqReceivedBookAppointmentBaseModel = mReceivedBookAppointmentBaseModel;
        }
        this.mReceivedBookAppointmentBaseModel = temp;
    }

    @OnClick({R.id.bookAppointmentBackButton, R.id.title, R.id.locationTextView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookAppointmentBackButton:
                onBackPressed();
                break;
            case R.id.title:
                break;
            case R.id.locationTextView:
                //    BookAppointDoctorListBaseActivityPermissionsDispatcher.callPickPlaceWithCheck(this);
                Intent start = new Intent(this, BookAppointFindLocationActivity.class);
                startActivityForResult(start, PLACE_PICKER_REQUEST);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BookAppointDoctorListBaseActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION})
    public void callPickPlace() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            Intent intentPlace = builder.build(BookAppointDoctorListBaseActivity_BackUp.this);
            startActivityForResult(intentPlace, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                latitude = String.valueOf(place.getLatLng().latitude);
                longitude = String.valueOf(place.getLatLng().longitude);
                address = String.format("%s", place.getAddress());
                stBuilder.append("Name: ");
                stBuilder.append(placename);
                stBuilder.append("\n");
                stBuilder.append("Latitude: ");
                stBuilder.append(latitude);
                stBuilder.append("\n");
                stBuilder.append("Logitude: ");
                stBuilder.append(longitude);
                stBuilder.append("\n");
                stBuilder.append("Address: ");
                stBuilder.append(address);
                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (addresses != null && addresses.size() > 0) {
                    //-------
                    String locality = "";
                    //-------
                    if (placename.contains(" ")) {
                        locality = getArea(addresses.get(0));
                    } else {
                        locality = placename;
                    }
                    String city = addresses.get(0).getLocality();

                   */
/* Address address = addresses.get(0);
                    String addressLine = address.getAddressLine(1);
                    String addressLineArray[] = addressLine.split(",");
                    addressLine = addressLineArray[addressLineArray.length - 1];

                    if (placename.toLowerCase().contains(addressLine)) {
                        locality = addressLine;
                    } else if (addressLine.toLowerCase().contains(placename)) {
                        locality = placename;
                    }*//*

                    //-------
                    //DoctorDataHelper.setUserSelectedLocationInfo(BookAppointDoctorListBaseActivity.this, place.getLatLng(), placename + ", " + city);
                    DoctorDataHelper.setUserSelectedLocationInfo(BookAppointDoctorListBaseActivity_BackUp.this, place.getLatLng(), locality + ", " + city);
                    setSelectedLocationText(locality + ", " + city);
                    //-------
                    HashMap<String, String> userSelectedLocationInfo = DoctorDataHelper.getUserSelectedLocationInfo();
                    String s = userSelectedLocationInfo.get(getString(R.string.location));
                    if (s != null) {
                        isLocationChange = true;
                        String[] split = s.split(",");
                        mDoctorDataHelper.doGetDoctorData(city.trim(), split[0].trim(), mComplaintsUserSearchFor);
                    }

                }
                CommonMethods.Log("Address: ", stBuilder.toString());
            }
        } else if (requestCode == DOCTOR_DATA_REQUEST_CODE && data != null) {
            ArrayList<DoctorList> doctorLists = data.getParcelableArrayListExtra(DOCTOR_DATA);

            if (!doctorLists.isEmpty()) {
                if (object != null)
                    object.setFavourite(doctorLists.get(0).getFavourite());
                else object = doctorLists.get(0);

                // update ui

                if (mCurrentlyLoadedFragment instanceof BookAppointDoctorDescriptionFragment) {
                    BookAppointDoctorDescriptionFragment bookAppointDoctorDescriptionFragment = (BookAppointDoctorDescriptionFragment) mCurrentlyLoadedFragment;
                    bookAppointDoctorDescriptionFragment.setFavorite(doctorLists.get(0).getFavourite());
                }

                replaceDoctorListById(object.getDocId(), object, getResources().getString(R.string.object_update_common_to_doc));
            }
        }
    }

    private String getArea(Address obj) {

        if (obj.getThoroughfare() != null)
            return obj.getThoroughfare();
        else if (obj.getSubLocality() != null)
            return obj.getSubLocality();
        else if (obj.getSubAdminArea() != null)
            return obj.getSubAdminArea();
        else if (obj.getLocality() != null)
            return obj.getLocality();
        else if (obj.getAdminArea() != null)
            return obj.getAdminArea();
        else
            return obj.getCountryName();
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra(DOCTOR_DATA, mReceivedBookAppointmentBaseModel.getDoctorServicesModel().getDoctorList());
        setResult(DOCTOR_DATA_REQUEST_CODE, intent);

        if (mSupportFragmentManager == null) {
            finish();
        } else {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                mDrawerLayout.closeDrawer(GravityCompat.END);
            } else {
                int backStackEntryCount = mSupportFragmentManager.getBackStackEntryCount();
                if (backStackEntryCount == 1) {
                    finish();
                } else {
                    super.onBackPressed();
                    mCurrentlyLoadedFragment = mSupportFragmentManager.findFragmentById(R.id.viewContainer);

                    // This to recall API when came from ComplaintFragmnet.
                    if (mCurrentlyLoadedFragment instanceof RecentVisitDoctorFragment) {
                        if (mComplaintsUserSearchFor.size() > 0) {
                            // To clear complaints Map, and call API to get data.
                            mComplaintsUserSearchFor.clear();
                            HashMap<String, String> userSelectedLocationInfo = DoctorDataHelper.getUserSelectedLocationInfo();
                            String s = userSelectedLocationInfo.get(getString(R.string.location));
                            if (s != null) {
                                isLocationChange = true;
                                String[] split = s.split(",");
                                mDoctorDataHelper.doGetDoctorData(split[1].trim(), split[0].trim(), mComplaintsUserSearchFor);
                            }
                            //-------
                        }
                    }
                }
            }
        }
    }

    public void loadFragment(Fragment fragmentToLoad, boolean requiredDrawer) {
        if (fragmentToLoad != null) {
            mSupportFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mSupportFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.viewContainer, fragmentToLoad);
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commitAllowingStateLoss();
            this.mCurrentlyLoadedFragment = fragmentToLoad;

            //----This is done to hold complaints ----
            Bundle arguments = fragmentToLoad.getArguments();
            if (arguments != null) {
                if (RescribeConstants.TITLE.equalsIgnoreCase(arguments.getString(getString(R.string.opening_mode)))) {
                    mComplaintsUserSearchFor.put(RescribeConstants.TITLE, RescribeConstants.TITLE);
                    mComplaintsUserSearchFor.put(getString(R.string.complaint1), arguments.getString(getString(R.string.complaint1)));
                    mComplaintsUserSearchFor.put(getString(R.string.complaint2), arguments.getString(getString(R.string.complaint2)));
                }
            }
            //--------
            doOperationOnDrawer(requiredDrawer);
        }
    }

    public void doOperationOnDrawer(boolean flag) {

        if (flag) { // for open
            //  The drawer is unlocked.
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            //The drawer is locked closed. The user may not open it, though
            // the app may open it programmatically.
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    public DrawerLayout getActivityDrawerLayout() {
        return mDrawerLayout;
    }

    public static void setToolBarTitle(String toolbartitle, boolean isLocationVisible) {
        title.setText("" + toolbartitle);
        if (isLocationVisible) {
            locationTextView.setVisibility(View.VISIBLE);
            showlocation.setVisibility(View.GONE);
        } else {
            locationTextView.setVisibility(View.GONE);
            showlocation.setVisibility(View.VISIBLE);
            showlocation.setText(locationTextView.getText().toString());
        }
    }

    public static void setSelectedLocationText(String locationText) {
        locationTextView.setText("" + locationText);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void setObject(DoctorList object) {
        this.object = object;
    }

    public interface OnActivityDrawerListener {
        void onApplyClicked(Bundle data);

        void onResetClicked();
    }

    public interface AddUpdateViewDataListener {
        void updateViewData();
    }


    public void replaceDoctorListById(int docId, DoctorList docObjectToReplace, String objectUpdateType) {
        DoctorServicesModel doctorServicesModel = mReceivedBookAppointmentBaseModel.getDoctorServicesModel();
        if (doctorServicesModel != null) {
            ArrayList<DoctorList> tempDoctorList = doctorServicesModel.getDoctorList();
            ArrayList<DoctorList> newListToUpdateTempDoctorList = new ArrayList<>(tempDoctorList);
            boolean isUpdated = false;
            for (int i = 0; i < tempDoctorList.size(); i++) {
                DoctorList tempObject = tempDoctorList.get(i);
                if (docId == tempObject.getDocId()) {
                    isUpdated = true;
                    newListToUpdateTempDoctorList.set(i, docObjectToReplace);
                }
            }

            if (isUpdated) {
                doctorServicesModel.setDoctorList(newListToUpdateTempDoctorList);
                mReceivedBookAppointmentBaseModel.setDoctorServicesModel(doctorServicesModel);
            }
        }
    }
}
*/
